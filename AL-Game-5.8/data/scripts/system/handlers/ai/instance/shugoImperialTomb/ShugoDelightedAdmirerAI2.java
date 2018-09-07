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
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Swig
 */
@AIName("shugodelightedadmirer")
// 831114, 831306, 831115, 831195
public class ShugoDelightedAdmirerAI2 extends NpcAI2 {

	@Override
	protected void handleDialogStart(Player player) {
		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1011));
	}

	@Override
	public boolean onDialogSelect(Player player, int dialogId, int questId, int extendedRewardIndex) {
		int instanceId = player.getInstanceId();

		switch (DialogAction.getActionByDialogId(dialogId)) {
			case SETPRO2:
				switch (getNpcId()) {
					case 831114:
					case 831306:
						skillId = player.getRace() == Race.ASMODIANS ? 21104 : 21095;
						SkillEngine.getInstance().applyEffectDirectly(skillId, player, player, 0);
						break;
					case 831115:
					case 831195:
						skillId = player.getRace() == Race.ASMODIANS ? 21105 : 21096;
						SkillEngine.getInstance().applyEffectDirectly(skillId, player, player, 0);
						break;
				}
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1012));
				break;
			case SETPRO1:
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 0));
				switch (getNpcId()) {
					case 831114:
					case 831306:
						TeleportService2.teleportTo(player, 300560000, instanceId, 346.27332f, 424.07101f, 294.75793f, (byte) 90, TeleportAnimation.BEAM_ANIMATION);
						break;
					case 831115:
					case 831195:
						TeleportService2.teleportTo(player, 300560000, instanceId, 450.8527f, 105.94637f, 212.20023f, (byte) 90, TeleportAnimation.BEAM_ANIMATION);
						break;
				}
				break;
			default:
				break;
		}
		return true;
	}
}
