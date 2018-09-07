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

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.instance.handlers.InstanceHandler;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.instance.StageType;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author xTz
 */
@AIName("recordkeeper")
public class RecordkeeperAI2 extends NpcAI2 {

	@Override
	protected void handleDialogStart(Player player) {
		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1011));
	}

	@Override
	public boolean onDialogSelect(Player player, int dialogId, int questId, int extendedRewardIndex) {
		int instanceId = getPosition().getInstanceId();
		InstanceHandler instanceHandler = getPosition().getWorldMapInstance().getInstanceHandler();
		if (dialogId == 10000) {
			switch (getNpcId()) {
				case 205668: // start stage 1
					instanceHandler.onChangeStage(StageType.START_STAGE_1_ROUND_1);
					break;
				case 205674: // move to stage 2
					TeleportService2.teleportTo(player, 300320000, instanceId, 1796.5513f, 306.9967f, 469.25f, (byte) 60);
					spawn(205683, 1821.5643f, 311.92484f, 469.4562f, (byte) 60);
					spawn(205669, 1784.4633f, 306.98645f, 469.25f, (byte) 0);
					break;
				case 205669: // start stage 2
					instanceHandler.onChangeStage(StageType.START_STAGE_2_ROUND_1);
					break;
				case 205675: // move to stage 3
					TeleportService2.teleportTo(player, 300320000, instanceId, 1324.433f, 1738.2279f, 316.476f, (byte) 70);
					spawn(205684, 1358.4021f, 1758.744f, 319.1873f, (byte) 70);
					spawn(205670, 1307.5472f, 1732.9865f, 316.0777f, (byte) 6);
					break;
				case 205670: // start stage 3
					instanceHandler.onChangeStage(StageType.START_STAGE_3_ROUND_1);
					break;
				case 205676: // movet to stage 4
					switch (Rnd.get(1, 2)) {
						case 1:
							TeleportService2.teleportTo(player, 300320000, instanceId, 1283.1246f, 791.6683f, 436.6403f, (byte) 60);
							spawn(205685, 1308.9664f, 796.20276f, 437.29678f, (byte) 60);
							spawn(205671, 1271.4222f, 791.36145f, 436.64017f, (byte) 0);
							break;
						case 2:
							TeleportService2.teleportTo(player, 300320000, instanceId, 1270.8877f, 237.93307f, 405.38028f, (byte) 60);
							spawn(205663, 1295.7217f, 242.15009f, 406.03677f, (byte) 60);
							spawn(205666, 1258.7214f, 237.85518f, 405.3968f, (byte) 0);
							break;
					}
					break;
				case 205666: // start stage 4
					instanceHandler.onChangeStage(StageType.START_ALTERNATIVE_STAGE_4_ROUND_1);
					break;
				case 205671:
					instanceHandler.onChangeStage(StageType.START_STAGE_4_ROUND_1);
					break;
				case 205667: // move to stage 5
				case 205677:
					TeleportService2.teleportTo(player, 300320000, instanceId, 357.98798f, 349.19116f, 96.09108f, (byte) 60);
					spawn(205686, 383.30933f, 354.07846f, 96.07846f, (byte) 60);
					spawn(205672, 346.52298f, 349.25586f, 96.0098f, (byte) 0);
					break;
				case 205672: // start stage 5
					instanceHandler.onChangeStage(StageType.START_STAGE_5_ROUND_1);
					break;
				case 205678: // move to stage 6
					TeleportService2.teleportTo(player, 300320000, instanceId, 1759.5004f, 1273.5414f, 389.11743f, (byte) 10);
					spawn(205687, 1747.3901f, 1250.201f, 389.11765f, (byte) 16);
					spawn(205673, 1767.1036f, 1288.4425f, 389.11728f, (byte) 76);
					break;
				case 205673: // start stage 6
					instanceHandler.onChangeStage(StageType.START_STAGE_6_ROUND_1);
					break;
				case 205679: // get score
					getPosition().getWorldMapInstance().getInstanceHandler().doReward(player);
					break;
			}
			if (getNpcId() != 205679) {
				AI2Actions.deleteOwner(this);
			}
		}
		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 0));
		return true;
	}
}
