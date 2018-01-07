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
package ai.worlds.nosra;

import java.util.List;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Npc;

import ai.AggressiveNpcAI2;

@AIName("crafty_nosra_monster")
public class Crafty_Nosra_MonsterAI2 extends AggressiveNpcAI2 {

	// Crafty (Icy) Mobs in Esterra and Nosra spawns random a Portal
	// Todo - add this AI to more NPCs

	@Override
	protected void handleDied() {
		switch (Rnd.get(1, 2)) {
			case 1:
				spawnDF6EventDoor();
				break;
			case 2:
				break;
		}
		super.handleDied();
	}

	private void spawnDF6EventDoor() {
		spawn(241054, getOwner().getX(), getOwner().getY(), getOwner().getZ(), (byte) 0); // Portal
		switch (Rnd.get(1, 4)) {
			case 1:
				spawn(240971, getOwner().getX(), getOwner().getY(), getOwner().getZ(), (byte) 0); // Guardian Warrior
				break;
			case 2:
				spawn(240972, getOwner().getX(), getOwner().getY(), getOwner().getZ(), (byte) 0); // Guardian Mage
				break;
			case 3:
				spawn(240973, getOwner().getX(), getOwner().getY(), getOwner().getZ(), (byte) 0); // Guardian Scout
				break;
			case 4:
				spawn(240974, getOwner().getX(), getOwner().getY(), getOwner().getZ(), (byte) 0); // Guardian Marksman
				break;
		}
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				despawnNpc(241054); // Portal
			}
		}, 60000);
	}

	private void despawnNpc(int npcId) {
		if (getPosition().getWorldMapInstance().getNpcs(npcId) != null) {
			List<Npc> npcs = getPosition().getWorldMapInstance().getNpcs(npcId);
			for (Npc npc : npcs) {
				npc.getController().onDelete();
			}
		}
	}
}
