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
package ai.instance.drakensphire;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;

import ai.ActionItemNpcAI2;

/**
 * @author blackfire
 */
@AIName("beritra_portal")
// 804697
public class BeritraPortalAI2 extends ActionItemNpcAI2 {

	@Override
	protected void handleUseItemFinish(final Player player) {
		switch (getNpcId()) {
			case 804697:
				TeleportService2.teleportTo(player, 301390000, 174.7f, 518.2f, 1749.6f, (byte) 59, TeleportAnimation.BEAM_ANIMATION);
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 915));
					}
				}, 3000);
				break;
		}
	}
}
