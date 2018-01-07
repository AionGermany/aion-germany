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
package quest.heiron;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;

/**
 * Find Litonos (204616) (bring him the Berone's Necklace (182201780)). Talk with Litonos. Take Litonos to Berone (204589). Talk with Berone.
 *
 * @author Balthazar
 * @reworked vlog
 */
public class _1562CrossedDestiny extends QuestHandler {

	private final static int questId = 1562;

	public _1562CrossedDestiny() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(204589).addOnQuestStart(questId);
		qe.registerOnLogOut(questId);
		qe.registerQuestNpc(204589).addOnTalkEvent(questId);
		qe.registerQuestNpc(204616).addOnTalkEvent(questId);
		qe.registerAddOnReachTargetEvent(questId);
		qe.registerAddOnLostTargetEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);

		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc) {
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 204589) { // Berone
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 4762);
				}
				if (env.getDialogId() == DialogAction.ASK_QUEST_ACCEPT.id()) {
					return sendQuestDialog(env, 4);
				}
				if (env.getDialogId() == DialogAction.QUEST_REFUSE_1.id()) {
					return sendQuestDialog(env, 1004);
				}
				if (env.getDialog() == DialogAction.QUEST_ACCEPT_1) {
					QuestService.startQuest(env);
					return defaultCloseDialog(env, 0, 1, false, false, 182201780, 1, 0, 0);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 204616: { // Litonos
					switch (env.getDialog()) {
						case QUEST_SELECT:
							if (qs.getQuestVarById(0) == 1 && player.getInventory().getItemCountByItemId(182201780) == 1) {
								return sendQuestDialog(env, 1352);
							}
							else {
								return sendQuestDialog(env, 1438);
							}
						case FINISH_DIALOG:
							return defaultCloseDialog(env, 0, 0);
						case SETPRO2:
							if (qs.getQuestVarById(0) == 1) {
								defaultStartFollowEvent(env, (Npc) env.getVisibleObject(), 204589, 0, 0);
								return defaultCloseDialog(env, 1, 2, false, false, 0, 0, 182201780, 1); // 2
							}
						case USE_OBJECT:
							if (qs.getQuestVarById(0) == 1) {
								return defaultStartFollowEvent(env, (Npc) env.getVisibleObject(), 204589, 1, 2);
							}
						default:
							break;
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 204589) { // Berone
				if (env.getDialogId() == DialogAction.SELECT_QUEST_REWARD.id()) {
					return sendQuestDialog(env, 10002);
				}
				else {
					return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}

	@Override
	public boolean onLogOutEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (var == 2) {
				changeQuestStep(env, 2, 1, false);
			}
		}
		return false;
	}

	@Override
	public boolean onNpcReachTargetEvent(QuestEnv env) {
		return defaultFollowEndEvent(env, 2, 2, true); // reward
	}

	@Override
	public boolean onNpcLostTargetEvent(QuestEnv env) {
		return defaultFollowEndEvent(env, 2, 1, false); // 1
	}
}
