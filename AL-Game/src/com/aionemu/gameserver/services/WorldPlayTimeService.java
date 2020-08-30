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

import java.util.Map;
import java.util.concurrent.Future;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_WORLD_PLAYTIME;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;

import javolution.util.FastMap;

public class WorldPlayTimeService {

    @SuppressWarnings("unused")
	private Future<?> checkPlayTimeTask;
    @SuppressWarnings({ "rawtypes", "unchecked" })
	private final Map<Integer, Player> players = new FastMap();

    public void onStart() {
        this.checkWorldPlayTime();
    }

    public void onEnterWorld(Player player) {
        switch (player.getWorldId()) {
            case 800030000: 
            case 800040000: 
            case 800050000: 
            case 800060000: 
            case 800070000: {
                if (this.players.containsKey(player.getObjectId())) break;
                this.players.put(player.getObjectId(), player);
                break;
            }
            default: {
                if (!this.players.containsKey(player.getObjectId())) break;
                this.players.remove(player.getObjectId());
            }
        }
    }

    public void checkWorldPlayTime() {
        this.checkPlayTimeTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable(){

            @Override
            public void run() {
                for (Player player : WorldPlayTimeService.this.players.values()) {
                    if (player.getCommonData().getWorldPlayTime() == 0) {
                        player.setWorldPlayTime(0);
                        PacketSendUtility.sendPacket(player, new SM_WORLD_PLAYTIME(player));
                        PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1405813, new Object[0]));
                        if (player.getRace() == Race.ELYOS) {
                            TeleportService2.teleportTo(player, 210050000, 1306.0f, 238.0f, 595.0f, (byte)17);
                            continue;
                        }
                        TeleportService2.teleportTo(player, 220070000, 1788.0f, 2917.0f, 554.0f, (byte)99);
                        continue;
                    }
                    player.getCommonData().setWorldPlayTime(player.getCommonData().getWorldPlayTime() - 1);
                    PacketSendUtility.sendPacket(player, new SM_WORLD_PLAYTIME(player));
                }
            }
        }, 60000, 60000);
    }

	public static WorldPlayTimeService getInstance() {
		return NewSingletonHolder.INSTANCE;
	}

	private static class NewSingletonHolder {

		private static final WorldPlayTimeService INSTANCE = new WorldPlayTimeService();
	}    
}
