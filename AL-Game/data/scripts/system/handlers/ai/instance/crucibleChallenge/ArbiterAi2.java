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
package ai.instance.crucibleChallenge;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author xTz
 */
@AIName("arbiter")
public class ArbiterAi2 extends NpcAI2 {

	@Override
	protected void handleDialogStart(Player player) {
		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1011));
	}

	@Override
	public boolean onDialogSelect(Player player, int dialogId, int questId, int extendedRewardIndex) {
		int instanceId = getPosition().getInstanceId();
		if (dialogId == 10000) {
			switch (getNpcId()) {
				case 205682:
					TeleportService2.teleportTo(player, 300320000, instanceId, 357.10208f, 1662.702f, 95.9803f, (byte) 60);
					break;
				case 205683:
					TeleportService2.teleportTo(player, 300320000, instanceId, 1796.5513f, 306.9967f, 469.25f, (byte) 60);
					break;
				case 205684:
					TeleportService2.teleportTo(player, 300320000, instanceId, 1324.433f, 1738.2279f, 316.476f, (byte) 70);
					break;
				case 205663:
					TeleportService2.teleportTo(player, 300320000, instanceId, 1270.8877f, 237.93307f, 405.38028f, (byte) 60);
					break;
				case 205686:
					TeleportService2.teleportTo(player, 300320000, instanceId, 357.98798f, 349.19116f, 96.09108f, (byte) 60);
					break;
				case 205687:
					TeleportService2.teleportTo(player, 300320000, instanceId, 1759.5004f, 1273.5414f, 389.11743f, (byte) 10);
					break;
				case 205685:
					TeleportService2.teleportTo(player, 300320000, instanceId, 1283.1246f, 791.6683f, 436.6403f, (byte) 60);
					break;
			}
		}
		return true;
	}
}
