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

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;

/**
 * @author QuestGenerator by Mariella
 */
public class _60007ForestRescue extends QuestHandler {

	private final static int questId = 60007;

	public _60007ForestRescue() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerOnLevelUp(questId);
		qe.registerQuestNpc(730008).addOnTalkEvent(questId); // Daminu
		qe.registerQuestNpc(820002).addOnTalkEvent(questId); // Investigating Royer
		qe.registerQuestNpc(820003).addOnTalkEvent(questId); // Implementer Royer
		qe.registerQuestNpc(700030).addOnTalkEvent(questId); // Odium Cauldron
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env, 60000, false);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();
		int targetId = env.getTargetId();

		if (qs == null) {
			return false;
		}

		if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 730008: {
					switch (dialog) {
						case QUEST_SELECT: {
							return sendQuestDialog(env, 1011);
						}
						case SETPRO1: {
							changeQuestStep(env, 0, 1, false);
							return closeDialogWindow(env);
						}
						default: 
							break;
					}
					break;
				}
				case 820002: {
					switch (dialog) {
						case QUEST_SELECT: {
							return sendQuestDialog(env, 1352);
						}
						case SETPRO2: {
							changeQuestStep(env, 1, 2, false);
							return closeDialogWindow(env);
						}
						default: 
							break;
					}
					break;
				}
				case 820003: {
					switch (dialog) {
						case QUEST_SELECT: {
							return sendQuestDialog(env, 1693);
						}
						case CHECK_USER_HAS_QUEST_ITEM: {
							if (QuestService.collectItemCheck(env,true)) {
								changeQuestStep(env, 2, 3, false);
								return sendQuestDialog(env, 10000);
							} else {
								return sendQuestDialog(env, 10001);
							}
						}
						default: 
							break;
					}
					break;
				}
				case 700030: {
					switch (dialog) {
						case USE_OBJECT: {
							changeQuestStep(env, 3, 4, true);
							return false;
						}
						default: 
							break;
					}
					break;
				}
				default:
					break;
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 820003) {
				if (dialog == DialogAction.USE_OBJECT) {
					return sendQuestDialog(env, 10002);
				}
				return sendQuestEndDialog(env);
			}
		}

		return false;
	}
}
