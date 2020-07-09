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
package com.aionemu.gameserver.services.lugbug;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.lugbug.LugbugEventTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_LUGBUG_EVENT;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Falke_34
 * @rework FrozenKiller
 */
public class LugbugEventService {

	private static final Logger log = LoggerFactory.getLogger("LUGBUG_EVENT_QUEST_LOG"); 
	private Map<Integer, LugbugEventTemplate> allEvents = DataManager.LUGBUG_EVENT_DATA.getAll();
	private HashMap<Integer, LugbugEventTemplate> activeEvents = new HashMap<Integer, LugbugEventTemplate>();
	private HashMap<Integer, LugbugEventTemplate> activeLugbugEventsForPlayer = new HashMap<Integer, LugbugEventTemplate>();

	/**
	 * initialize all events
	 */
	public void initialize() {
		if (allEvents.size() != 0) {
			for (LugbugEventTemplate lugbugEvent : allEvents.values()) {
				log.info("[LugbugEventService] Loaded Event " + lugbugEvent.getId() + " Start " + lugbugEvent.getStartday() + " End " + lugbugEvent.getEndday());
			}
			getActiveEvents();
		}
	}

	/**
	 * get active events
	 * @return 
	 */
	public HashMap<Integer, LugbugEventTemplate> getActiveEvents() {
		for (LugbugEventTemplate activeLugbugEvent : allEvents.values()) {
			if (activeEvents.containsKey(activeLugbugEvent.getId())) {
				continue;
			}
			
			if (!activeLugbugEvent.getStartday().isBeforeNow() || !activeLugbugEvent.getEndday().isAfterNow()) {
				continue;
			}
			else {
				activeEvents.put(activeLugbugEvent.getId(), activeLugbugEvent);
			}
		}
		return activeEvents;
	}
	
	/**
	 * get active events for player
	 * @return 
	 */
	public HashMap<Integer, LugbugEventTemplate> getActiveEventsForPlayer(Player player) {
		for (LugbugEventTemplate activeLugbugEventPlayer : activeEvents.values()) {
			if (activeLugbugEventsForPlayer.containsKey(activeLugbugEventPlayer.getId())) {
				continue;
			}
			
			if (player.getLevel() >= activeLugbugEventPlayer.getMinlevel() && player.getLevel() <= activeLugbugEventPlayer.getMaxlevel()) {
				activeLugbugEventsForPlayer.put(activeLugbugEventPlayer.getId(), activeLugbugEventPlayer);
			} else {
				continue;
			}
		}
		return activeLugbugEventsForPlayer;
	}

	/**
	 * activate events on login
	 */
	public void onLogin(final Player player) {
		if (player == null) {
			return;
		}
		getActiveEventsForPlayer(player);
		PacketSendUtility.sendPacket(player, new SM_LUGBUG_EVENT(activeLugbugEventsForPlayer));
	}
	
	private static class SingletonHolder {
		protected static final LugbugEventService instance = new LugbugEventService();
	}

	public static final LugbugEventService getInstance() {
		return SingletonHolder.instance;
	}
}