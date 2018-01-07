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

package ai.instance.sealedHallOfKnowledge;

import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.utils.ThreadPoolManager;

import ai.AggressiveNpcAI2;

/**
 * @author Ranastic
 */
@AIName("pashid_lab_unit_sentinel")
public class PashidLabUnitSentinelAI2 extends AggressiveNpcAI2 {

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		int lifetime = (getNpcId() == 230077 ? 20000 : 600000);
		toDespawn(lifetime);
	}

	@Override
	protected void handleDied() {
		super.handleDied();
		AI2Actions.deleteOwner(PashidLabUnitSentinelAI2.this);
	}

	private void toDespawn(int delay) {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				AI2Actions.deleteOwner(PashidLabUnitSentinelAI2.this);
			}
		}, delay);
	}
}
