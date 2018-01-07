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
package ai.instance.sauroSupplyBase;

import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Npc;

import ai.AggressiveNpcAI2;

/**
 * @author blackfire
 */

@AIName("sauro_beacon")
public class BeaconAI2 extends AggressiveNpcAI2 {

	Npc cannon;

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		cannon = getPosition().getWorldMapInstance().getNpc(284453);
		AI2Actions.targetCreature(BeaconAI2.this, cannon);
		getAggroList().addHate(cannon, 100000);
	}
}
