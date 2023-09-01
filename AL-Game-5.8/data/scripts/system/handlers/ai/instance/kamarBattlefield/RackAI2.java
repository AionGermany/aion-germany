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
package ai.instance.kamarBattlefield;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.item.ItemService;

import ai.ActionItemNpcAI2;

/**
 * @author Ever
 */
@AIName("rack")
// 801777
public class RackAI2 extends ActionItemNpcAI2 {

	int rackCount = Rnd.get(1, 10);

	@Override
	protected void handleUseItemFinish(Player player) {
		switch (getNpcId()) {
			case 801777:
				ItemService.addItem(player, 162000134, rackCount); // Kamar Special foods that increase Physical Attack by 15 points and Magic Boost by 70 points for 30 minutes
				AI2Actions.deleteOwner(this);
				break;
		}
	}
}
