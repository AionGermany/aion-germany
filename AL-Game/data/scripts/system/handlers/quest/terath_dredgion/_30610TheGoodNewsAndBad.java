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
package quest.terath_dredgion;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author Ritsu
 */
public class _30610TheGoodNewsAndBad extends QuestHandler {

	private final static int questId = 30610;

	public _30610TheGoodNewsAndBad() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(205864).addOnQuestStart(questId); // Skafir
		qe.registerQuestNpc(205864).addOnTalkEvent(questId);
		qe.registerQuestNpc(800327).addOnTalkEvent(questId);// Astella
		qe.registerQuestNpc(219256).addOnKillEvent(questId);
		qe.registerQuestNpc(219257).addOnKillEvent(questId);
		qe.registerQuestNpc(219264).addOnKillEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();
		int targetId = env.getTargetId();

		if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
			switch (targetId) {
				case 205864: {
					switch (dialog) {
						case QUEST_SELECT:
							return sendQuestDialog(env, 1011);
						default:
							return sendQuestStartDialog(env);
					}
				}
			}
		}
		else if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			switch (targetId) {
				case 800327: {
					switch (dialog) {
						case QUEST_SELECT:
							return sendQuestDialog(env, 1352);
						case SETPRO1: {
							return defaultCloseDialog(env, 0, 0);
						}
						default:
							break;
					}
				}
				case 205864: {
					switch (dialog) {
						case QUEST_SELECT:
							return sendQuestDialog(env, 2375);
						case SELECT_QUEST_REWARD: {
							if (var == 1) {
								qs.setStatus(QuestStatus.REWARD);
								updateQuestStatus(env);
								return sendQuestDialog(env, 5);
							}
						}
						default:
							break;

					}
				}
			}
		}
		else if (qs != null && qs.getStatus() == QuestStatus.REWARD) {
			switch (targetId) {
				case 205864: {
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
		int targetId = 0;
		if (qs == null || qs.getStatus() != QuestStatus.START) {
			return false;
		}
		else {
			if (env.getVisibleObject() instanceof Npc) {
				targetId = ((Npc) env.getVisibleObject()).getNpcId();
			}
			switch (targetId) {
				case 219256:
				case 219257:
					if (qs.getQuestVarById(0) == 0) {
						qs.setQuestVar(1);
						updateQuestStatus(env);
						return true;
					}

				case 219264:
					if (qs.getQuestVarById(0) == 1) {
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						return true;
					}
			}
		}
		return false;
	}
}
