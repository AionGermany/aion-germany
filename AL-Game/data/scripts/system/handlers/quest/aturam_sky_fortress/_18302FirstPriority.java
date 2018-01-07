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
package quest.aturam_sky_fortress;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author Luzien
 */
public class _18302FirstPriority extends QuestHandler {

	private final static int questId = 18302;
	private int[] mobIds = new int[] { 700981, 700982, 700983, 700984, 700985 };

	public _18302FirstPriority() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(799530).addOnQuestStart(questId);
		qe.registerQuestNpc(799530).addOnTalkEvent(questId);
		qe.registerQuestNpc(730375).addOnTalkEvent(questId);
		for (int id : mobIds) {
			qe.registerQuestNpc(id).addOnKillEvent(questId);
		}
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
			if (targetId == 799530) {
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 4762);
				}
				else if (env.getDialog() == DialogAction.QUEST_ACCEPT_1) {
					playQuestMovie(env, 468);
					return sendQuestStartDialog(env);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}

		if (qs == null) {
			return false;
		}

		int var = qs.getQuestVarById(0);

		if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 730375) {
				if (var == 5) {
					switch (env.getDialog()) {
						case USE_OBJECT:
							return sendQuestDialog(env, 1352);
						case SET_SUCCEED:
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
							return closeDialogWindow(env);
						default:
							return sendQuestDialog(env, 2716);
					}
				}
			}
			else if (targetId == 799530) {
				return sendQuestDialog(env, 1004);
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 799530) {
				if (env.getDialog() == DialogAction.USE_OBJECT) {
					return sendQuestDialog(env, 10002);
				}
				if (env.getDialog() == DialogAction.SELECT_QUEST_REWARD) {
					removeQuestItem(env, 182212101, 1);
					return sendQuestDialog(env, 5);
				}
				else {
					return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}

	@Override
	public boolean onKillEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);

		if (qs == null || qs.getStatus() != QuestStatus.START) {
			return false;
		}

		int targetId = 0;
		int var = qs.getQuestVarById(0);

		if (env.getVisibleObject() instanceof Npc) {
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		if (var > 4) {
			return false;
		}

		for (int id : mobIds) {
			if (targetId == id) {
				qs.setQuestVarById(0, var + 1);
				updateQuestStatus(env);
				return true;
			}
		}
		return false;
	}
}
