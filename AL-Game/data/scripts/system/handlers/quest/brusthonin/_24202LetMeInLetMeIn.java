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
package quest.brusthonin;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;

/**
 * @author FrozenKiller
 */

public class _24202LetMeInLetMeIn extends QuestHandler {

	private final static int questId = 24202;

	public _24202LetMeInLetMeIn() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(205150).addOnQuestStart(questId);
		qe.registerQuestNpc(205150).addOnTalkEvent(questId); // Surt
		qe.registerQuestNpc(205159).addOnTalkEvent(questId); // Neligor
		qe.registerQuestNpc(205198).addOnTalkEvent(questId); // Cayron
	}

	@Override
	public boolean onDialogEvent(final QuestEnv env) {
		final Player player = env.getPlayer();
		int targetId = env.getTargetId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 205150) { // Surt
				if (dialog == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 1011);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (targetId == 205159) { // Neligor
				switch (dialog) {
					case QUEST_SELECT: {
						if (var == 0) {
							return sendQuestDialog(env, 1352);
						}
					}
					case SETPRO1: {
						return defaultCloseDialog(env, 0, 1); // 1
					}
					default:
						break;
				}
			}
			else if (targetId == 205198) { // Cayron
				if (var == 1) {
					switch (dialog) {
						case QUEST_SELECT: {
							return sendQuestDialog(env, 1693);
						}
						case SETPRO2: {
							return defaultCloseDialog(env, 1, 2);
						}
						default:
							break;
					}
				}
			}
			else if (targetId == 205150) { // Surt
				if (var == 2) {
					switch (dialog) {
						case QUEST_SELECT: {
							return sendQuestDialog(env, 2375);
						}
						case CHECK_USER_HAS_QUEST_ITEM_SIMPLE: {
							if (QuestService.collectItemCheck(env, true)) {
								changeQuestStep(env, 2, 3, true);
								return sendQuestDialog(env, 5);
							}
							else {
								return closeDialogWindow(env);
							}
						}
						default:
							break;
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 205150) { // Surt
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}
