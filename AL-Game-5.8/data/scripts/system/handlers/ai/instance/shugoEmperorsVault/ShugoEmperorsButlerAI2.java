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
package ai.instance.shugoEmperorsVault;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.teleport.TeleportService2;

import ai.GeneralNpcAI2;

/**
 * @author Falke_34
 */
@AIName("shugoEmperorsButler")
// 832932
public class ShugoEmperorsButlerAI2 extends GeneralNpcAI2 {

	int teleportCounter = 0;

	@Override
	public boolean onDialogSelect(Player player, int dialogId, int questId, int extendedRewardIndex) {
		int instanceId = player.getInstanceId();

		if (dialogId == 10000) {
			if (teleportCounter == 3) {
				teleportCounter = 0;
			}

			if (teleportCounter == 0) {
				TeleportService2.teleportTo(player, 301400000, instanceId, 171.532f, 230.76953f, 395.0f, (byte) 90, TeleportAnimation.BEAM_ANIMATION);
				teleportCounter++;
			}
			else if (teleportCounter == 1) {
				TeleportService2.teleportTo(player, 301400000, instanceId, 171.18474f, 381.22818f, 394.83136f, (byte) 90, TeleportAnimation.BEAM_ANIMATION);
				teleportCounter++;
			}
			else if (teleportCounter == 2) {
				TeleportService2.teleportTo(player, 301400000, instanceId, 172.38368f, 531.9615f, 395.29797f, (byte) 90, TeleportAnimation.BEAM_ANIMATION);
				teleportCounter++;
			}

			return true;
		}

		return true;
	}

}
