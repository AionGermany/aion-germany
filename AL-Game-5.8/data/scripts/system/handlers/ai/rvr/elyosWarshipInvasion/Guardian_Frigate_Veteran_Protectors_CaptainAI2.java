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
package ai.rvr.elyosWarshipInvasion;

import java.util.List;

import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Npc;

import ai.AggressiveNpcAI2;

@AIName("LF6_Event_G1_S1_Kn_75_Ah") // 240756
public class Guardian_Frigate_Veteran_Protectors_CaptainAI2 extends AggressiveNpcAI2 {

	@Override
	public void think() {
	}

	@Override
	protected void handleDied() {
		despawnNpc(240757);
		despawnNpc(240758);
		despawnNpc(240759);
		spawn(240760, 1391.9437f, 1615.8804f, 1010.55457f, (byte) 27); // Guardian Frigate Special Grade Combat Captain.
		spawn(240761, 1395.8307f, 1620.0297f, 1010.55457f, (byte) 15); // Guardian Frigate Special Grade Assault Leader.
		spawn(240761, 1387.7642f, 1611.8877f, 1010.55457f, (byte) 75); // Guardian Frigate Special Grade Assault Leader.
		spawn(240761, 1394.0009f, 1610.4152f, 1010.55457f, (byte) 98); // Guardian Frigate Special Grade Assault Leader.
		spawn(240761, 1389.7134f, 1621.1094f, 1010.55457f, (byte) 37); // Guardian Frigate Special Grade Assault Leader.
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
