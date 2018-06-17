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
package com.aionemu.gameserver.services;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import javolution.util.FastMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.PlayerEventsWindowDAO;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.event_window.PlayerEventWindowEntry;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.event.EventsWindow;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EVENT_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EVENT_WINDOW_ITEMS;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/**
 * @author Ghostfur (Aion-Unique)
 */
public class EventWindowService {

	private static final Logger log = LoggerFactory.getLogger(EventWindowService.class);
	private Map<Integer, EventsWindow> data = new HashMap<>(1);
	private Map<Integer, EventsWindow> event = new HashMap<>(1);
	private ScheduledFuture<?> schedule = null;
	private long tStart = 0; // Start Time.
	private long tEnd = 0; // End Time.

	/**
	 * initialize all events
	 */
	public void initialize() {
		Map<Integer, EventsWindow> allEvents = DataManager.EVENTS_WINDOW.getAllEvents();
		if (allEvents.size() != 0) {
			getEvents(allEvents);
		}
	}

	/**
	 * get events window start and end time
	 */
	public void getEvents(Map<Integer, EventsWindow> map) {
		data.putAll(map);
		for (EventsWindow eventsWindow : data.values()) {
			getEvent(eventsWindow.getId(), eventsWindow);
			log.info("[EventWindowService] Start " + eventsWindow.getPeriodStart() + " End " + eventsWindow.getPeriodEnd());
		}
	}

	/**
	 * get events window
	 */
	public void getEvent(int accountId, EventsWindow eventsWindow) {
		if (event.containsValue(accountId)) {
			return;
		}
		event.put(accountId, eventsWindow);
	}

	/**
	 * get events window active events
	 */
	public Map<Integer, EventsWindow> getActiveEvents() {
		HashMap<Integer, EventsWindow> hashMap = new HashMap<Integer, EventsWindow>();
		for (EventsWindow eventsWindow : event.values()) {
			if (!eventsWindow.getPeriodStart().isBeforeNow() || !eventsWindow.getPeriodEnd().isAfterNow())
				continue;
				hashMap.put(eventsWindow.getId(), eventsWindow);
		}
		return hashMap;
	}

	/**
	 * get player events window
	 */
	public Map<Integer, EventsWindow> getPlayerEventsWindow(int accountId) {
		HashMap<Integer, EventsWindow> hashMap = new HashMap<Integer, EventsWindow>();
		List<Integer> list = DAOManager.getDAO(PlayerEventsWindowDAO.class).getEventsWindow(accountId);
		for (Integer Time : list) {
			hashMap.put(Time, data.get(Time));
		}
		return hashMap;
	}

	/**
	 * activate events window on login
	 */
	public void onLogin(final Player player) {
		if (player == null) {
			return;
		}
		tStart = System.currentTimeMillis();
		final int accountId = player.getPlayerAccount().getId();
		final PlayerEventsWindowDAO playerEventsWindowDAO = DAOManager.getDAO(PlayerEventsWindowDAO.class);
		Map<Integer, EventsWindow> activeEvents = getActiveEvents();
		Map<Integer, EventsWindow> playerEventsWindow = getPlayerEventsWindow(accountId);
		final FastMap<Integer, EventsWindow> fastMap = new FastMap<>();
		@SuppressWarnings("unused")
		double timeZ = 0.0;
		double time = playerEventsWindowDAO.getElapsed(accountId);
		int Time = (int) (time % 3600.0 / 60.0);
		player.getEventWindow().setRemaining(Time);
		for (PlayerEventWindowEntry playerEventWindowEntry : player.getEventWindow().getAll()) {
			timeZ = playerEventWindowEntry.getElapsed();
		}
		for (final EventsWindow eventsWindow : activeEvents.values()) {
			if (!eventsWindow.getPeriodStart().isBeforeNow() || !eventsWindow.getPeriodEnd().isAfterNow() || playerEventsWindow.containsKey(eventsWindow.getId()))
				continue;		
				fastMap.put(eventsWindow.getId(), eventsWindow);
				log.info("Start counting id " + eventsWindow.getId() + " time " + eventsWindow.getRemainingTime() + " minute(s)");
				schedule = ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					if (player.isOnline()) {
						playerEventsWindowDAO.insert(accountId, eventsWindow.getId(), new Timestamp(System.currentTimeMillis()));
						ItemService.addItem(player, eventsWindow.getItemId(), eventsWindow.getCount());
						fastMap.remove(eventsWindow.getId());
						log.info("Player " + player.getName() + " get reward of events window item " + eventsWindow.getItemId());
					}
				}
			}, eventsWindow.getRemainingTime() * 60000);
		}
		PacketSendUtility.sendPacket(player, new SM_EVENT_WINDOW_ITEMS(fastMap.values()));
		PacketSendUtility.sendPacket(player, new SM_EVENT_WINDOW(1, 3));
	}

	/**
	 * deactivate events window on logout
	 */
	public void onLogout(Player player) {
		int accountId = player.getPlayerAccount().getId();
		Map<Integer, EventsWindow> playerEventsWindow = getPlayerEventsWindow(accountId);
		PlayerEventsWindowDAO playerEventsWindowDAO = DAOManager.getDAO(PlayerEventsWindowDAO.class);
		if (!player.isOnline() && schedule != null) {
			tEnd = System.currentTimeMillis();
			schedule.cancel(true);
			schedule = null;
			if (playerEventsWindow != null) {
				double d2 = playerEventsWindowDAO.getElapsed(accountId);
				long Long = tEnd - tStart;
				double d3 = (double) Long / 1000.0;
				double time = d2 + d3;
				playerEventsWindowDAO.updateElapsed(accountId, time);
			}
			log.info("Schedule canceled");
		}
	}

	private static class SingletonHolder {

		protected static final EventWindowService instance = new EventWindowService();
	}

	public static final EventWindowService getInstance() {
		return SingletonHolder.instance;
	}
}
