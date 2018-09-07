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
package ai.instance.argentManor;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.world.WorldPosition;

import ai.ActionItemNpcAI2;

/**
 * @author Falke_34
 */
@AIName("drained_hetgolem")
// 856547
public class DrainedHetgolemAI2 extends ActionItemNpcAI2 {

	private boolean isSpawned;

	@Override
	protected void handleUseItemFinish(Player player) {
		final WorldPosition p = getPosition();
		if (!isSpawned && player.getInventory().getItemCountByItemId(185000242) > 0) {
			isSpawned = true;
			AI2Actions.handleUseItemFinish(this, player);
			switch (Rnd.get(1, 2)) {
				case 1:
					spawn(237196, p.getX(), p.getY(), p.getZ(), (byte) 0);
					break;
				case 2:
					spawn(237197, p.getX(), p.getY(), p.getZ(), (byte) 0);
					break;
				default:
					break;
			}
		}
		else {
			return;
		}
		player.getInventory().decreaseByItemId(185000242, 1);
		AI2Actions.deleteOwner(this);
	}
}
