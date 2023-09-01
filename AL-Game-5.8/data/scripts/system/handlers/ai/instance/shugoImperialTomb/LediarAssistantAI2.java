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
package ai.instance.shugoImperialTomb;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.manager.EmoteManager;
import com.aionemu.gameserver.model.gameobjects.Npc;

import ai.AggressiveNpcAI2;

/**
 * @author Swig
 */
@AIName("lediar_assistant")
// 219461
public class LediarAssistantAI2 extends AggressiveNpcAI2 {

	private final static int[] npc_ids = { 831251, 831250, 831305 };

	@Override
	public int modifyOwnerDamage(int damage) {
		return damage = 1;
	}

	@Override
	protected void handleSpawned() {
		addHate();
		super.handleSpawned();
	}

	private void addHate() {
		EmoteManager.emoteStopAttacking(getOwner());
		for (int npc_id : npc_ids) {
			Npc tower = getOwner().getPosition().getWorldMapInstance().getNpc(npc_id);
			if (tower != null && !tower.getLifeStats().isAlreadyDead()) {
				switch (npc_id) {
					case 831305:
						getOwner().getAggroList().addHate(tower, 10000);
					case 831250:
						getOwner().getAggroList().addHate(tower, 10000);
					case 831251:
						getOwner().getAggroList().addHate(tower, 10000);
						break;
				}
			}
		}
	}
}
