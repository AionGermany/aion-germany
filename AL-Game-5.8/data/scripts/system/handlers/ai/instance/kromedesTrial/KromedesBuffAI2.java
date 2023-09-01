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
package ai.instance.kromedesTrial;

import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;

import ai.ActionItemNpcAI2;

/**
 * @author Tiger0319, Gigi, xTz
 */
@AIName("krbuff")
public class KromedesBuffAI2 extends ActionItemNpcAI2 {

	@Override
	protected void handleUseItemFinish(Player player) {
		switch (getNpcId()) {
			case 730336:
				SkillEngine.getInstance().applyEffectDirectly(19216, player, player, 0);
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400655));
				break;
			case 730337:
				SkillEngine.getInstance().applyEffectDirectly(19217, player, player, 0);
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400656));
				AI2Actions.deleteOwner(this);
				break;
			case 730338:
				SkillEngine.getInstance().applyEffectDirectly(19218, player, player, 0);
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400657));
				AI2Actions.deleteOwner(this);
				break;
			case 730339:
				SkillEngine.getInstance().applyEffectDirectly(19219, player, player, 0);
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400658));
				break;
		}
	}
}
