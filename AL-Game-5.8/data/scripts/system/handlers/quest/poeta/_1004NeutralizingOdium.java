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
package quest.poeta;

import com.aionemu.gameserver.model.EmotionId;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;

/**
 * @author MrPoke
 */
public class _1004NeutralizingOdium extends QuestHandler {

	private final static int questId = 1004;

	public _1004NeutralizingOdium() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerOnEnterZoneMissionEnd(questId);
		qe.registerOnLevelUp(questId);
		qe.registerQuestNpc(203082).addOnTalkEvent(questId);
		qe.registerQuestNpc(700030).addOnTalkEvent(questId);
		qe.registerQuestNpc(790001).addOnTalkEvent(questId);
		qe.registerQuestNpc(203067).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(final QuestEnv env) {
		final Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null) {
			return false;
		}

		int var = qs.getQuestVarById(0);
		int targetId = env.getTargetId();

		if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 203082) {
				switch (env.getDialog()) {
					case QUEST_SELECT:
						if (var == 0) {
							return sendQuestDialog(env, 1011);
						}
						else if (var == 5) {
							return sendQuestDialog(env, 2034);
						}
					case SELECT_ACTION_1013:
						if (var == 0) {
							playQuestMovie(env, 19);
						}
						return false;
					case SETPRO1:
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						sendQuestSelectionDialog(env);
						return true;
					case SETPRO3:
						if (var == 5) {
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
							sendQuestSelectionDialog(env);
							return true;
						}
					default:
						break;
				}
			}
			else if (targetId == 700030 && var == 1 || var == 4) {
				switch (env.getDialog()) {
					case USE_OBJECT:
						if (qs.getQuestVarById(0) == 1) {
							if (giveQuestItem(env, 182200005, 1)) {
								qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
							}
						}
						else if (qs.getQuestVarById(0) == 4) {
							qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
							removeQuestItem(env, 182200005, 1);
						}
						updateQuestStatus(env);
						sendEmotion(env, player, EmotionId.STAND, true);
						return false;
					default:
						break;
				}
			}
			else if (targetId == 790001) {
				switch (env.getDialog()) {
					case QUEST_SELECT:
						if (var == 2) {
							return sendQuestDialog(env, 1352);
						}
						else if (var == 3) {
							return sendQuestDialog(env, 1693);
						}
						else if (var == 11) {
							return sendQuestDialog(env, 1694);
						}
					case SETPRO2:
						if (var == 2) {
							qs.setQuestVarById(0, var + 1);
							updateQuestStatus(env);
							sendQuestSelectionDialog(env);
							return true;
						}
					case SETPRO3:
						if (var == 11) {
							if (!giveQuestItem(env, 182200006, 1)) {
								return true;
							}
							qs.setQuestVarById(0, 4);
							updateQuestStatus(env);
							removeQuestItem(env, 182200006, 1);
							sendQuestSelectionDialog(env);
							return true;
						}
					case CHECK_USER_HAS_QUEST_ITEM: {
						if (QuestService.collectItemCheck(env, true)) {
							qs.setQuestVarById(0, 11);
							updateQuestStatus(env);
							return sendQuestDialog(env, 1694);
						}
						else {
							return sendQuestDialog(env, 1779);
						}
					}
					default:
						break;
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 203067) {
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}

	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env) {
		return defaultOnZoneMissionEndEvent(env);
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env, 1100, true);
	}
}
