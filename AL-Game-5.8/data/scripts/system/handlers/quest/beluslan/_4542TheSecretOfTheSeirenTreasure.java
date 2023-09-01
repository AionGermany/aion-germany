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
package quest.beluslan;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author pralinka
 */
public class _4542TheSecretOfTheSeirenTreasure extends QuestHandler {

	private final static int questId = 4542;

	public _4542TheSecretOfTheSeirenTreasure() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(204768).addOnQuestStart(questId);
		qe.registerQuestNpc(204768).addOnTalkEvent(questId);
		qe.registerQuestNpc(204743).addOnTalkEvent(questId);
		qe.registerQuestNpc(204808).addOnTalkEvent(questId);
		qe.registerOnLevelUp(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();

		int targetId = env.getTargetId();
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 204768) {
				switch (dialog) {
					case QUEST_SELECT:
						return sendQuestDialog(env, 4762);
					case QUEST_ACCEPT_SIMPLE:
						giveQuestItem(env, 182215327, 1);
						return sendQuestStartDialog(env);
					default:
						break;
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			switch (targetId) {
				case 204743: // Rubelik
					switch (env.getDialog()) {
						case QUEST_SELECT:
							if (var == 0) {
								return sendQuestDialog(env, 1011);
							}
							if (var == 6 && player.getInventory().getItemCountByItemId(182215330) >= 1) {
								return sendQuestDialog(env, 3057);
							}
						case SETPRO1:
							if (var == 0) {
								giveQuestItem(env, 182215328, 1);
								removeQuestItem(env, 182215327, 1);
								return defaultCloseDialog(env, 0, 1); // 1
							}
						case SETPRO3:
							return defaultCloseDialog(env, 2, 3); // 3
						case SELECT_QUEST_REWARD: {
							removeQuestItem(env, 182215330, 1);
							return defaultCloseDialog(env, 6, 6, true, true); // reward
						}
						case SELECT_ACTION_3143:
							return sendQuestDialog(env, 3143);
						case SETPRO7: {
							playQuestMovie(env, 239);
							removeQuestItem(env, 182215330, 1);
							return defaultCloseDialog(env, 6, 6, true, true); // reward
						}
						default:
							break;
					}
					break;
				case 204768: // sleipnir
					switch (env.getDialog()) {
						case QUEST_SELECT:
							if (var == 1) {
								return sendQuestDialog(env, 1352);
							}
							else if (var == 5) {
								return sendQuestDialog(env, 2716);
							}
						case SETPRO2:
							removeQuestItem(env, 182215328, 1);
							playQuestMovie(env, 239);
							return defaultCloseDialog(env, 1, 2); // 2
						case SELECT_QUEST_REWARD:
							removeQuestItem(env, 182215330, 1);
							return defaultCloseDialog(env, 5, 5, true, false); // 2
						default:
							break;
					}
					break;
				case 204808: // esnu
					switch (env.getDialog()) {
						case QUEST_SELECT:
							if (var == 2) {
								return sendQuestDialog(env, 1693);
							}
							if (var == 3) {
								return sendQuestDialog(env, 2034);
							}
							if (var == 4) {
								return sendQuestDialog(env, 2376);
							}
						case SETPRO3:
							if (var == 2) {
								playQuestMovie(env, 240);
								return defaultCloseDialog(env, 2, 3); // 3
							}
						case CHECK_USER_HAS_QUEST_ITEM:
							return checkQuestItems(env, 3, 4, false, 10000, 10001); // 4
						case SETPRO5:
							return defaultCloseDialog(env, 4, 5, false, false, 182215330, 1, 0, 0); // 6
						default:
							break;
					}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 204768) {
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}
