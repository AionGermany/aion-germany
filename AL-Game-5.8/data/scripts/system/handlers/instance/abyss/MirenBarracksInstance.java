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

/**
 * @author Ever'
 */
@InstanceID(301290000)
public class MirenBarracksInstance extends GeneralInstanceHandler {

	private boolean rewarded = false;

	@Override
	public void onDie(Npc npc) {
		switch (npc.getNpcId()) {
			case 233719: // Miren Duke lvl.65
			case 233718: // weakened boss lvl.65
				spawnChests(npc);
				break;
			case 215415: // artifact spawns weak boss
				Npc boss = getNpc(233719);
				if (boss != null && !boss.getLifeStats().isAlreadyDead()) {
					spawn(233718, boss.getX(), boss.getY(), boss.getZ(), boss.getHeading());
					boss.getController().onDelete();
				}
		}
	}

	private void spawnChests(Npc npc) {
		if (!rewarded) {
			rewarded = true; // safety mechanism
			if (npc.getAi2().getRemainigTime() != 0) {
				long rtime = (600000 - npc.getAi2().getRemainigTime()) / 30000;
				spawn(702298, 478.7917f, 815.5538f, 199.70894f, (byte) 8);
				if (rtime > 1) {
					spawn(702298, 471, 853, 199f, (byte) 115);
				}
				if (rtime > 2) {
					spawn(702298, 477, 873, 199.7f, (byte) 109);
				}
				if (rtime > 3) {
					spawn(702298, 507, 899, 199.7f, (byte) 96);
				}
				if (rtime > 4) {
					spawn(702296, 548, 889, 199.7f, (byte) 83);
				}
				if (rtime > 5) {
					spawn(702296, 565, 889, 199.7f, (byte) 76);
				}
				if (rtime > 6) {
					spawn(702296, 585, 855, 199.7f, (byte) 63);
				}
				if (rtime > 7) {
					spawn(702296, 578, 874, 199.7f, (byte) 11);
				}
				if (rtime > 8) {
					spawn(702297, 528, 903, 199.7f, (byte) 30);
				}
				if (rtime > 9) {
					spawn(702297, 490, 899, 199.7f, (byte) 44);
				}
				if (rtime > 10) {
					spawn(702297, 470, 834, 199.7f, (byte) 63);
				}
				if (rtime > 11 && npc.getNpcId() == 233719) {
					spawn(702299, 576.8508f, 836.40424f, 199.7f, (byte) 44);
				}
			}
		}
	}
}
