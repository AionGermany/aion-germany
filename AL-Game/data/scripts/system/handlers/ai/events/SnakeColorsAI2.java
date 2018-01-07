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

import org.joda.time.DateTime;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.questEngine.QuestEngine;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;

import ai.GeneralNpcAI2;

/**
 * @author Romanz
 * @rework Ever'
 * @rework yayaya
 */
@AIName("snakecolors")
// 832974, 832975, 832963, 832964
public class SnakeColorsAI2 extends GeneralNpcAI2 {

	@Override
	protected void handleDialogStart(Player player) {
		switch (getNpcId()) {
			case 832974:// Dolores
			case 832975:// Lucius
			case 832963:// Dolores
			case 832964:// Lucius
				super.handleDialogStart(player);
				break;
			default:
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1011));
				break;
		}
	}

	@Override
	public boolean onDialogSelect(Player player, int dialogId, int questId, int extendedRewardIndex) {
		QuestEnv env = new QuestEnv(getOwner(), player, questId, dialogId);
		env.setExtendedRewardIndex(extendedRewardIndex);
		if (QuestEngine.getInstance().onDialog(env) && dialogId != DialogAction.SETPRO1.id()) {
			return true;
		}
		if (dialogId == DialogAction.SETPRO1.id()) {
			switch (getNpcId()) {
				case 832975:
				case 832964:
					switch (Rnd.get(1, 2)) {
						case 1:// [Event] Rainbow Snake's Judgement
							SkillEngine.getInstance().getSkill(getOwner(), 10979, 1, player).useWithoutPropSkill();
							break;
						case 2:// [Event] Rainbow Snake's Love
							SkillEngine.getInstance().getSkill(getOwner(), 10978, 1, player).useWithoutPropSkill();
							break;
					}
					break;
				case 832974:
				case 832963:
					switch (Rnd.get(1, 2)) {
						case 1:// [Event] Rainbow Snake's Grace
							SkillEngine.getInstance().getSkill(getOwner(), 10977, 1, player).useWithoutPropSkill();
							break;
						case 2:// [Event] Rainbow Snake's Splendor
							SkillEngine.getInstance().getSkill(getOwner(), 10976, 1, player).useWithoutPropSkill();
							break;
					}

					break;
			}
		}

		else if (dialogId == DialogAction.QUEST_SELECT.id() && questId != 0) {
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), dialogId, questId));
		}
		return true;
	}

	@Override
	protected void handleSpawned() {
		DateTime now = DateTime.now();
		int currentDay = now.getDayOfWeek();
		switch (getNpcId()) {
			case 832975:
			case 832964: {
				if (currentDay >= 1 && currentDay <= 4) {
					super.handleSpawned();
				}
				else if (!isAlreadyDead()) {
					getOwner().getController().onDelete();
				}
				break;
			}
			case 832974:
			case 832963: {
				if (currentDay >= 5 && currentDay <= 7) {
					super.handleSpawned();
				}
				else if (!isAlreadyDead()) {
					getOwner().getController().onDelete();
				}
				break;
			}
		}
	}
}
