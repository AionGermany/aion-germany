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
package ai.rvr.asmodianWarshipInvasion;

import java.util.List;

import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Npc;

import ai.AggressiveNpcAI2;

@AIName("DF6_Event_G1_S1_Kn_75_Ah") // 240663
public class Archon_Frigate_Veteran_Protectors_CaptainAI2 extends AggressiveNpcAI2 {

	@Override
	public void think() {
	}

	@Override
	protected void handleDied() {
		despawnNpc(240664);
		despawnNpc(240665);
		despawnNpc(240666);
		spawn(240667, 1409.8998f, 1369.7438f, 1336.7855f, (byte) 60); // Archon Frigate Special Grade Combat Captain
		spawn(240668, 1407.2133f, 1371.8616f, 1336.7855f, (byte) 60); // Archon Frigate Special Grade Assault Leader
		spawn(240668, 1412.3649f, 1367.3982f, 1336.7855f, (byte) 60); // Archon Frigate Special Grade Assault Leader
		spawn(240668, 1412.2811f, 1372.0088f, 1336.7855f, (byte) 60); // Archon Frigate Special Grade Assault Leader
		spawn(240668, 1407.1234f, 1367.6641f, 1336.7855f, (byte) 60); // Archon Frigate Special Grade Assault Leader
		super.handleDied();
		AI2Actions.deleteOwner(this);
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
