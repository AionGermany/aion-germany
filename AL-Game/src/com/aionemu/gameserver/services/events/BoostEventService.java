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

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.stats.calc.StatOwner;
import com.aionemu.gameserver.model.templates.event.BoostEvents;
import com.aionemu.gameserver.network.aion.serverpackets.SM_BOOST_EVENTS;
import com.aionemu.gameserver.utils.PacketSendUtility;

public class BoostEventService implements StatOwner {

//  private static BoostEventBonus bonus;
	private static final Logger log = LoggerFactory.getLogger(BoostEventService.class);
    public Map<Integer, BoostEvents> data;
    public HashMap<Integer, BoostEvents> activeEvents = new HashMap<Integer, BoostEvents>();
    
    public BoostEventService() {
        data = new HashMap<Integer, BoostEvents>(1);
    }
    
    public void onStart() {
        Map<Integer, BoostEvents> all = DataManager.BOOST_EVENT_DATA.getAll();
        if (all.size() != 0) {
            getBoostEvent(all);
        }
    }
    
    public void sendPacket(Player player) {
        PacketSendUtility.sendPacket(player, new SM_BOOST_EVENTS((HashMap<Integer, BoostEvents>) getCurrentBoost()));
    }
    
    private Map<Integer, BoostEvents> getCurrentBoost() {
        for (BoostEvents boostEvents : data.values()) {
            if (boostEvents.getStartDate().isBeforeNow() && boostEvents.getEndDate().isAfterNow()) {
            	activeEvents.put(boostEvents.getId(), boostEvents);
            }
        }
        return activeEvents;
    }
    
    private void getBoostEvent(int eventId, BoostEvents boostEvents) {
        if (data.containsValue(eventId)) {
            return;
        }
        data.put(eventId, boostEvents);
    }
    
    private void getBoostEvent(Map<Integer, BoostEvents> map) {
        data.putAll(map);
        for (BoostEvents boostEvents : data.values()) {
            getBoostEvent(boostEvents.getId(), boostEvents);
        }
        log.info("[BoostEventService] BoostEventService initialized");
    }

	public static BoostEventService getInstance() {
		return SingletonHolder.INSTANCE;
	}

	private static class SingletonHolder {

		private static final BoostEventService INSTANCE = new BoostEventService();
	}
}