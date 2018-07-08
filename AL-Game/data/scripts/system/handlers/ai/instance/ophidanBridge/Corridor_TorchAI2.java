/*
 * This file is part of aion-lightning <aion-lightning.com>.
 *
 *  aion-lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-lightning.  If not, see <http://www.gnu.org/licenses/>.
 */
package ai.instance.ophidanBridge;

import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.player.Player;

import ai.ActionItemNpcAI2;

/**
 * @author Falke_34
 */
@AIName("corridor_torch")// 701644
public class Corridor_TorchAI2 extends ActionItemNpcAI2 {

	@Override
	protected void handleUseItemFinish(Player player) {
		switch (getNpcId()) {
			case 701644: // Control Corridor
				spawn(731544, 436.3948f, 496.45862f, 606.90356f, (byte) 1, 7); // Control Corridor
				spawn(731545, 372.55792f, 492.0503f, 606.90356f, (byte) 1, 34); // Access Corridor
				AI2Actions.deleteOwner(this);
				break;
		}
	}
}
