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

package ai.events;

import java.util.Random;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Master
 */
@AIName("buffer")
// 831857, 831858
public class BufferEventAI2 extends NpcAI2 {

	@Override
	protected void handleDialogStart(Player player) {
		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1011));
	}

	@Override
	public boolean onDialogSelect(Player player, int dialogId, int questId, int exp) {
		switch (dialogId) {
			case 10000: {
				int[] rr = { 2, 2, 1, 1, 1 };
				Random rand = new Random();

				int skillLevel = 1;
				getOwner().setTarget(player);

				int skillId1 = 20950; // Blessing of Rock I SKILLID:20950
				int skillId2 = 20950; // Blessing of Health I

				if (rr[rand.nextInt(4)] == 2) {
					SkillEngine.getInstance().getSkill(getOwner(), skillId1, skillLevel, player).useWithoutPropSkill();
				}
				else {
					if (rand.nextInt(1) == 0) {
						SkillEngine.getInstance().getSkill(getOwner(), skillId1, skillLevel, player).useWithoutPropSkill();
					}
					else {
						SkillEngine.getInstance().getSkill(getOwner(), skillId2, skillLevel, player).useWithoutPropSkill();
					}
				}

				break;
			}
		}
		return true;
	}
}
