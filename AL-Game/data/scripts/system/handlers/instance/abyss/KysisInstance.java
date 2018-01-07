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
package instance.abyss;

import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.teleport.TeleportService2;

/**
 * @author keqi, xTz
 * @reworked Luzien
 */
@InstanceID(300120000)
public class KysisInstance extends GeneralInstanceHandler {

	private boolean rewarded = false;

	@Override
	public void onDie(Npc npc) {
		switch (npc.getNpcId()) {
			case 215179: // bosses
			case 215178:
				spawnChests(npc);
				break;
			case 215414: // artifact spawns weakened boss
				Npc boss = getNpc(215179);
				if (boss != null && !boss.getLifeStats().isAlreadyDead()) {
					spawn(215178, boss.getX(), boss.getY(), boss.getZ(), boss.getHeading());
					boss.getController().onDelete();
				}
		}
	}

	private void spawnChests(Npc npc) {
		if (!rewarded) {
			rewarded = true; // safety mechanism
			if (npc.getAi2().getRemainigTime() != 0) {
				long rtime = (600000 - npc.getAi2().getRemainigTime()) / 30000; // Spawn chests depending on time needed for boss kill
				spawn(700541, 478.7917f, 815.5538f, 199.70894f, (byte) 9);
				if (rtime > 1) {
					spawn(700541, 471, 853, 199f, (byte) 117);
				}
				if (rtime > 2) {
					spawn(700541, 477, 873, 199.7f, (byte) 109);
				}
				if (rtime > 3) {
					spawn(700541, 507, 899, 199.7f, (byte) 96);
				}
				if (rtime > 4) {
					spawn(700541, 548, 889, 199.7f, (byte) 83);
				}
				if (rtime > 5) {
					spawn(700541, 565, 889, 199.7f, (byte) 76);
				}
				if (rtime > 6) {
					spawn(700541, 585, 855, 199.7f, (byte) 65);
				}
				if (rtime > 7) {
					spawn(700541, 578, 874, 199.7f, (byte) 9);
				}
				if (rtime > 8) {
					spawn(700541, 528, 903, 199.7f, (byte) 30);
				}
				if (rtime > 9) {
					spawn(700541, 490, 899, 199.7f, (byte) 44);
				}
				if (rtime > 10) {
					spawn(700560, 470, 834, 199.7f, (byte) 63);
				}
				if (rtime > 11 && npc.getNpcId() == 215179) {
					spawn(700542, 577.5694f, 836.9684f, 199.7f, (byte) 120); // last chest only for big boss
				}
			}
		}
	}

	@Override
	public void onPlayerLogOut(Player player) {
		TeleportService2.moveToInstanceExit(player, mapId, player.getRace());
	}
}
