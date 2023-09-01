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
package quest.cygnea;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author pralinka
 */
public class _15042CallingKaisinelsButterfly extends QuestHandler {

	private final static int questId = 15042;

	public _15042CallingKaisinelsButterfly() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestItem(182215676, questId);
		qe.registerQuestNpc(804885).addOnQuestStart(questId);
		qe.registerQuestNpc(804885).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();
		DialogAction dialog = env.getDialog();

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 804885) {
				if (dialog == DialogAction.QUEST_SELECT) {
					giveQuestItem(env, 182215676, 1);
					return sendQuestDialog(env, 4762);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 804885) {
				if (dialog == DialogAction.QUEST_SELECT) {
					removeQuestItem(env, 182215676, 1);
					return sendQuestDialog(env, 2034);
				}
				else {
					return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}

	@Override
	public HandlerResult onItemUseEvent(final QuestEnv env, Item item) {
		final Player player = env.getPlayer();
		final int id = item.getItemTemplate().getTemplateId();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (id != 182215676 || qs.getStatus() == QuestStatus.COMPLETE) {
			return HandlerResult.UNKNOWN;
		}
		if (!player.isInsideZone(ZoneName.get("LF5_ItemUseArea_Q15042"))) {
			return HandlerResult.UNKNOWN;
		}
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			int var1 = qs.getQuestVarById(1);
			if (var == 0) {
				if (var1 < 2) {
					qs.setQuestVarById(1, var1 + 1);
					updateQuestStatus(env);
				}
				else if (var1 == 2) {
					giveQuestItem(env, 182215753, 1);
					removeQuestItem(env, 182215676, 1);
					qs.setQuestVar(1);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					return HandlerResult.SUCCESS;
				}
			}
		}
		return HandlerResult.SUCCESS;
	}
}
