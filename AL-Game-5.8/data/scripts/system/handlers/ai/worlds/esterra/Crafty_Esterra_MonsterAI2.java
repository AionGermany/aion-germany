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
package ai.worlds.esterra;

import java.util.List;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Npc;

import ai.AggressiveNpcAI2;

@AIName("crafty_esterra_monster")
public class Crafty_Esterra_MonsterAI2 extends AggressiveNpcAI2 {

	// Crafty (Icy) Mobs in Esterra and Nosra spawns random a Portal
	// Todo - add this AI to more NPCs

	@Override
	protected void handleDied() {
		switch (Rnd.get(1, 2)) {
			case 1:
				spawnLF6EventDoor();
				break;
			case 2:
				break;
		}
		super.handleDied();
	}

	private void spawnLF6EventDoor() {
		spawn(241053, getOwner().getX(), getOwner().getY(), getOwner().getZ(), (byte) 0); // Portal
		switch (Rnd.get(1, 4)) {
			case 1:
				spawn(240887, getOwner().getX(), getOwner().getY(), getOwner().getZ(), (byte) 0); // Archon Warrior
				break;
			case 2:
				spawn(240888, getOwner().getX(), getOwner().getY(), getOwner().getZ(), (byte) 0); // Archon Mage
				break;
			case 3:
				spawn(240889, getOwner().getX(), getOwner().getY(), getOwner().getZ(), (byte) 0); // Archon Scout
				break;
			case 4:
				spawn(240890, getOwner().getX(), getOwner().getY(), getOwner().getZ(), (byte) 0); // Archon Marksman
				break;
		}
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				despawnNpc(241053); // Portal
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
