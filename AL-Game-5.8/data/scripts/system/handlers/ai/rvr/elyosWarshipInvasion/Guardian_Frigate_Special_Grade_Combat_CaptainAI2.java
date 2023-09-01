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

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Npc;

import ai.AggressiveNpcAI2;

@AIName("LF6_Event_G1_S2_Fi_75_Al") // 240760
public class Guardian_Frigate_Special_Grade_Combat_CaptainAI2 extends AggressiveNpcAI2 {

	@Override
	public void think() {
	}

	@Override
	protected void handleDied() {
		switch (Rnd.get(1, 3)) {
			case 1:
				spawn(240762, 1391.9735f, 1615.5792f, 1010.55457f, (byte) 25); // Luluran.
				break;
			case 2:
				spawn(240763, 1391.9735f, 1615.5792f, 1010.55457f, (byte) 25); // Nanabel.
				break;
			case 3:
				spawn(240764, 1391.9735f, 1615.5792f, 1010.55457f, (byte) 25); // Mandos.
				break;
		}
		super.handleDied();
		despawnNpc(240761); // Guardian Frigate Special Grade Assault Leader.
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
