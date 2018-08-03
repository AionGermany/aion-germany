/**
 * This file is part of Aion-Lightning <aion-lightning.org>.
 *
 *  Aion-Lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Aion-Lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details. *
 *  You should have received a copy of the GNU General Public License
 *  along with Aion-Lightning.
 *  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.services.events;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.PlayerEventsWindowDAO;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.event.EventsWindow;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EVENT_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EVENT_WINDOW_ITEMS;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;

import javolution.util.FastMap;

/**
 * @author Ghostfur (Aion-Unique)
 * @rework FrozenKiller
 */
public class EventWindowService {

	private static final Logger log = LoggerFactory.getLogger(EventWindowService.class);
	private Map<Integer, EventsWindow> allEvents = DataManager.EVENTS_WINDOW.getAllEvents();
	private HashMap<Integer, EventsWindow> activeEvents = new HashMap<Integer, EventsWindow>();
	private HashMap<Integer, EventsWindow> activeEventsForPlayer = new HashMap<Integer, EventsWindow>();
	private final FastMap<Integer, EventsWindow> sendActiveEventsForPlayer = new FastMap<>();
	private long tStart = 0; // Start Time.
	private long tEnd = 0; // End Time.

	/**
	 * initialize all events
	 */
	public void initialize() {
		if (allEvents.size() != 0) {
			for (EventsWindow eventsWindow : allEvents.values()) {
				log.info("[EventWindowService] Start " + eventsWindow.getPeriodStart() + " End " + eventsWindow.getPeriodEnd());
			}
		}
	}

	/**
	 * get events window start and end time
	 * @return 
	 */
	public Map<Integer, EventsWindow> getActiveEvents(Player player) {
		for (EventsWindow eventsWindow : allEvents.values()) {
			if (activeEvents.containsValue(eventsWindow.getId())) {
				continue;
			}
			if (!eventsWindow.getPeriodStart().isBeforeNow() || !eventsWindow.getPeriodEnd().isAfterNow()) {
				continue;
			}
			if (player.getLevel() >= eventsWindow.getMinLevel() && player.getLevel() <= eventsWindow.getMaxLevel()) {
				activeEventsForPlayer.put(eventsWindow.getId(), eventsWindow);
				log.info("[EventWindowService] Start " + eventsWindow.getPeriodStart() + " End " + eventsWindow.getPeriodEnd());
			}
		}
		return activeEventsForPlayer;
	}

	/**
	 * activate events window on login
	 */
	public void onLogin(final Player player) {
		if (player == null) {
			return;
		}
		getActiveEvents(player);
		final int accountId = player.getPlayerAccount().getId();
		final PlayerEventsWindowDAO playerEventsWindowDAO = DAOManager.getDAO(PlayerEventsWindowDAO.class);
		for (final EventsWindow eventsWindow : activeEventsForPlayer.values()) {
			final int elapsed = playerEventsWindowDAO.getElapsed(accountId, eventsWindow.getId());
			final int recivedCount = playerEventsWindowDAO.getRewardRecivedCount(accountId, eventsWindow.getId());
			if (!eventsWindow.getPeriodStart().isBeforeNow() || !eventsWindow.getPeriodEnd().isAfterNow())
				continue;
				sendActiveEventsForPlayer.put(eventsWindow.getId(), eventsWindow);
				if (!playerEventsWindowDAO.getEventsWindow(accountId).contains(eventsWindow.getId())) {
					playerEventsWindowDAO.insert(accountId, eventsWindow.getId(), new Timestamp(System.currentTimeMillis()));
				} else {
					playerEventsWindowDAO.store(accountId, eventsWindow.getId(), new Timestamp(System.currentTimeMillis()), elapsed); // Temp for updating TiemStamp
				}
				log.info("Start counting id " + eventsWindow.getId() + " time " + eventsWindow.getRemainingTime() + " minute(s)");
				ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					if (player.isOnline()) {
						if (recivedCount == eventsWindow.getMaxCountOfDay()) {
							sendActiveEventsForPlayer.remove(eventsWindow.getId());	
							return;
						}
						playerEventsWindowDAO.setRewardRecivedCount(accountId, eventsWindow.getId(), (recivedCount + 1)); // It also Set elapsed to 0 and updates TimeStamp (MySQL5PlayerEventsWindowDAO)
						ItemTemplate itemTemplate = DataManager.ITEM_DATA.getItemTemplate(eventsWindow.getItemId());
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_GET_HCOIN_07(itemTemplate.getNameId()));
						ItemService.addItem(player, eventsWindow.getItemId(), eventsWindow.getCount());
						restartTimer(player, eventsWindow.getId());
						PacketSendUtility.sendPacket(player, new SM_EVENT_WINDOW_ITEMS(sendActiveEventsForPlayer.values()));
					}
				}
			}, (eventsWindow.getRemainingTime() - elapsed) * 60000);
		}
		PacketSendUtility.sendPacket(player, new SM_EVENT_WINDOW_ITEMS(sendActiveEventsForPlayer.values()));
		PacketSendUtility.sendPacket(player, new SM_EVENT_WINDOW(1, sendActiveEventsForPlayer.size()));
	}
	
	public void restartTimer(final Player player, final int eventId) {
		final int accountId = player.getPlayerAccount().getId();
		final PlayerEventsWindowDAO playerEventsWindowDAO = DAOManager.getDAO(PlayerEventsWindowDAO.class);
		final int recivedCount = playerEventsWindowDAO.getRewardRecivedCount(accountId, eventId);
		for (final EventsWindow eventsWindow : sendActiveEventsForPlayer.values()) {
			if (!eventsWindow.getPeriodStart().isBeforeNow() || !eventsWindow.getPeriodEnd().isAfterNow())
				continue;
			if (eventsWindow.getId() == eventId) {
				ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						if (player.isOnline()) {
							if (recivedCount == eventsWindow.getMaxCountOfDay()) {
								sendActiveEventsForPlayer.remove(eventsWindow.getId());
								return;
							}
							playerEventsWindowDAO.setRewardRecivedCount(accountId, eventsWindow.getId(), (recivedCount + 1));
							ItemTemplate itemTemplate = DataManager.ITEM_DATA.getItemTemplate(eventsWindow.getItemId());
							PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_GET_HCOIN_07(itemTemplate.getNameId()));
							ItemService.addItem(player, eventsWindow.getItemId(), eventsWindow.getCount());
							restartTimer(player, eventId);
						}
					}
				}, eventsWindow.getRemainingTime() * 60000);
			}
		}
	}

	/**
	 * deactivate events window on logout
	 */
	public void onLogout(Player player) {
		int accountId = player.getPlayerAccount().getId();
		PlayerEventsWindowDAO playerEventsWindowDAO = DAOManager.getDAO(PlayerEventsWindowDAO.class);
		for (final EventsWindow eventsWindow : activeEventsForPlayer.values()) {
			if (playerEventsWindowDAO.getEventsWindow(accountId).contains(eventsWindow.getId()) && player.isOnline()) {
				tStart = (playerEventsWindowDAO.getLastStamp(accountId, eventsWindow.getId()).getTime() / 1000);
				tEnd = (System.currentTimeMillis() / 1000);
				int d2 = playerEventsWindowDAO.getElapsed(accountId, eventsWindow.getId());
				int time = (int) (((tEnd - tStart) / 60) + d2);
				playerEventsWindowDAO.updateElapsed(accountId, eventsWindow.getId(), time);
			}
		}
		activeEventsForPlayer.clear();
		sendActiveEventsForPlayer.clear();
	}

	private static class SingletonHolder {
		protected static final EventWindowService instance = new EventWindowService();
	}

	public static final EventWindowService getInstance() {
		return SingletonHolder.instance;
	}
}
