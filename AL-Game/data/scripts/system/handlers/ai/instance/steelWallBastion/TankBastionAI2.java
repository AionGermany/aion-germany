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
package ai.instance.steelWallBastion;

import java.util.List;

import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.questEngine.QuestEngine;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;

import ai.GeneralNpcAI2;

/**
 * @author Alcapwnd
 */
@AIName("tank_bastion")
public class TankBastionAI2 extends GeneralNpcAI2 {

	protected int rewardDialogId = 5;
	protected int startingDialogId = 10;
	protected int questDialogId = 10;

	@Override
	protected void handleDialogStart(Player player) {
		checkDialog(player);
		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 10));
	}

	@Override
	public boolean onDialogSelect(Player player, int dialogId, int questId, int extendedRewardIndex) {
		QuestEnv env = new QuestEnv(getOwner(), player, questId, dialogId);
		env.setExtendedRewardIndex(extendedRewardIndex);
		checkEntryConditions(player, dialogId, questId);
		if (QuestEngine.getInstance().onDialog(env)) {
			return true;
		}
		return true;
	}

	private void checkDialog(Player player) {
		int npcId = getNpcId();
		List<Integer> relatedQuests = QuestEngine.getInstance().getQuestNpc(npcId).getOnTalkEvent();
		boolean playerHasQuest = false;
		boolean playerCanStartQuest = false;
		if (!relatedQuests.isEmpty()) {
			for (int questId : relatedQuests) {
				QuestState qs = player.getQuestStateList().getQuestState(questId);
				if (qs != null && (qs.getStatus() == QuestStatus.START || qs.getStatus() == QuestStatus.REWARD)) {
					playerHasQuest = true;
					break;
				}
				else if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
					if (QuestService.checkStartConditions(new QuestEnv(getOwner(), player, questId, 0), true)) {
						playerCanStartQuest = true;
						continue;
					}
				}
			}
		}

		if (playerHasQuest) {
			boolean isRewardStep = false;
			for (int questId : relatedQuests) {
				QuestState qs = player.getQuestStateList().getQuestState(questId);
				if (qs != null && qs.getStatus() == QuestStatus.REWARD) {
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), rewardDialogId, questId));
					isRewardStep = true;
					break;
				}
			}
			if (!isRewardStep) {
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), questDialogId));
			}
		}
		else if (playerCanStartQuest) {
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), startingDialogId));
		}
		else {
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1011, 0));
		}
	}

	private void checkEntryConditions(Player player, int dialogId, int questId) {
		if (dialogId == 10000) {
			if (player.getInventory().decreaseByItemId(185000137, 1)) {
				SkillEngine.getInstance().applyEffectDirectly(21141, player, player, 160000 * 3);
				PacketSendUtility.sendBrightYellowMessageOnCenter(player, "You ride now a tank!");
				AI2Actions.deleteOwner(this);
			}
			else {
				PacketSendUtility.sendBrightYellowMessageOnCenter(player, "You dont have the correct item!");
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 0, 0));
			}
		}
		else {
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), dialogId, questId));
		}
	}
}
