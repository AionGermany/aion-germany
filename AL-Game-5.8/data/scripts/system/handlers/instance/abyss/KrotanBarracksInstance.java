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
 * @author Everlight
 */
@InstanceID(301300000)
public class KrotanBarracksInstance extends GeneralInstanceHandler {

	private boolean rewarded = false;

	@Override
	public void onDie(Npc npc) {
		switch (npc.getNpcId()) {
			case 233633: // Krotan Duke lvl.65
			case 233632: // weakened boss lvl.65
				spawnChests(npc);
				break;
			case 215413: // artifact spawns weak boss
				Npc boss = getNpc(233633);
				if (boss != null && !boss.getLifeStats().isAlreadyDead()) {
					spawn(233632, boss.getX(), boss.getY(), boss.getZ(), boss.getHeading());
					boss.getController().onDelete();
				}
		}
	}

	private void spawnChests(Npc npc) {
		if (!rewarded) {
			rewarded = true; // safety mechanism
			if (npc.getAi2().getRemainigTime() != 0) {
				long rtime = (600000 - npc.getAi2().getRemainigTime()) / 30000;
				spawn(702288, 471.05634f, 834.5538f, 199.70894f, (byte) 63);
				if (rtime > 1) {
					spawn(702288, 490f, 889f, 199f, (byte) 43);
				}
				if (rtime > 2) {
					spawn(702288, 528, 903, 199.7f, (byte) 32);
				}
				if (rtime > 3) {
					spawn(702288, 578, 874, 199.7f, (byte) 10);
				}
				if (rtime > 4) {
					spawn(702289, 477, 814, 199.7f, (byte) 8);
				}
				if (rtime > 5) {
					spawn(702289, 470, 854, 199.7f, (byte) 115);
				}
				if (rtime > 6) {
					spawn(702289, 478, 873, 199.7f, (byte) 110);
				}
				if (rtime > 7) {
					spawn(702289, 507, 898, 199.7f, (byte) 96);
				}
				if (rtime > 8) {
					spawn(702290, 547, 899, 199.7f, (byte) 85);
				}
				if (rtime > 9) {
					spawn(702290, 564, 889, 199.7f, (byte) 78);
				}
				if (rtime > 10) {
					spawn(702290, 584, 855, 199.7f, (byte) 85);
				}
				if (rtime > 11 && npc.getNpcId() == 233633) {
					spawn(702291, 576.4634f, 837.3374f, 199.7f, (byte) 99);
				}
			}
		}
	}
}
