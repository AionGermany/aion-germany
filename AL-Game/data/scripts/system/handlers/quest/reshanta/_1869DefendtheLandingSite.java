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

package quest.reshanta;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author Phantom_KNA
 */
public class _1869DefendtheLandingSite extends QuestHandler {

	private final static int questId = 1869;

	public _1869DefendtheLandingSite() {
		super(questId);
	}

	@SuppressWarnings("unused")
	private final static int[] mobs = { 220273, 220258, 220263, 220268, 220272, 220288, 220278, 220293, 220283 };

	@Override
	public void register() {
		qe.registerQuestNpc(278501).addOnQuestStart(questId); // Michalis
		qe.registerQuestNpc(278501).addOnTalkEvent(questId);
		qe.registerQuestNpc(278501).addOnTalkEvent(questId);
		qe.registerQuestNpc(220273).addOnKillEvent(questId);
		qe.registerQuestNpc(220258).addOnKillEvent(questId);
		qe.registerQuestNpc(220263).addOnKillEvent(questId);
		qe.registerQuestNpc(220268).addOnKillEvent(questId);
		qe.registerQuestNpc(220272).addOnKillEvent(questId);
		qe.registerQuestNpc(220288).addOnKillEvent(questId);
		qe.registerQuestNpc(220278).addOnKillEvent(questId);
		qe.registerQuestNpc(220293).addOnKillEvent(questId);
		qe.registerQuestNpc(220283).addOnKillEvent(questId);
	}

	@Override
	public boolean onKillEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (var == 0) {
				int[] mobs = { 220273, 220258, 220263, 216464, 220268, 220272 };
				int[] mobo = { 220288, 220278, 220293, 220283, 220292, 220287 };
				int targetId = env.getTargetId();
				int var1 = qs.getQuestVarById(1);
				int var2 = qs.getQuestVarById(2);
				switch (targetId) {
					case 220273:
					case 220263:
					case 216464:
					case 220268:
					case 220272:
					case 220258: {
						if (var1 < 5) {
							return defaultOnKillEvent(env, mobs, 0, 5, 1);
						}
						else if (var1 == 5) {
							if (var2 == 6) {
								qs.setQuestVar(1);
								qs.setStatus(QuestStatus.REWARD);
								updateQuestStatus(env);
								return true;
							}
							else {
								return defaultOnKillEvent(env, mobs, 5, 6, 1);
							}
						}
						break;
					}
					case 220292:
					case 220287:
					case 220283:
					case 220293:
					case 220278:
					case 220288: {
						if (var2 < 5) {
							return defaultOnKillEvent(env, mobo, 0, 5, 2);
						}
						else if (var2 == 5) {
							if (var1 == 6) {
								qs.setQuestVar(1);
								qs.setStatus(QuestStatus.REWARD);
								updateQuestStatus(env);
								return true;
							}
							else {
								return defaultOnKillEvent(env, mobo, 5, 6, 2);
							}
						}
						break;
					}
				}
			}
		}
		return false;
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		int targetId = env.getTargetId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);

		if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
			if (targetId == 278501) { // Michalis
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 4762);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 278501) { // Michalis
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 10002);
				}
				else if (env.getDialog() == DialogAction.SELECT_QUEST_REWARD) {
					changeQuestStep(env, 0, 0, true);
					return sendQuestDialog(env, 5);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 278501) { // Michalis
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}
