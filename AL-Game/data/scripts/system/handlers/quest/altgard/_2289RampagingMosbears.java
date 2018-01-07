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
package quest.altgard;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author vlog
 */
public class _2289RampagingMosbears extends QuestHandler {

	private static final int questId = 2289;

	public _2289RampagingMosbears() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(203616).addOnQuestStart(questId);
		qe.registerQuestNpc(203616).addOnTalkEvent(questId);
		qe.registerQuestNpc(203618).addOnTalkEvent(questId);
		qe.registerQuestNpc(210564).addOnKillEvent(questId);
		qe.registerQuestNpc(210584).addOnKillEvent(questId);
		qe.registerQuestNpc(210442).addOnKillEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();
		int targetId = env.getTargetId();

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 203616) { // Gefion
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
			switch (targetId) {
				case 203616: { // Gefion
					switch (dialog) {
						case SETPRO1: {
							return sendQuestSelectionDialog(env);
						}
						case QUEST_SELECT: {
							if (var == 5) {
								return sendQuestDialog(env, 1352);
							}
							else if (var == 7) {
								return sendQuestDialog(env, 2034);
							}
						}
						case SELECT_ACTION_1354: {
							playQuestMovie(env, 62);
							return sendQuestDialog(env, 1354);
						}
						case SETPRO2: {
							return defaultCloseDialog(env, 5, 6); // 6
						}
						case CHECK_USER_HAS_QUEST_ITEM: {
							return checkQuestItems(env, 7, 7, true, 5, 2120); // reward
						}
						case FINISH_DIALOG: {
							return defaultCloseDialog(env, 7, 7);
						}
						default:
							break;
					}
					break;
				}
				case 203618: { // Skanin
					switch (dialog) {
						case QUEST_SELECT: {
							if (var == 6) {
								return sendQuestDialog(env, 1693);
							}
						}
						case SETPRO3: {
							return defaultCloseDialog(env, 6, 7, 182203017, 1, 0, 0); // 7
						}
						default:
							break;
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 203616) { // Gefion
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}

	@Override
	public boolean onKillEvent(QuestEnv env) {
		int[] mobs = { 210564, 210584 };
		return defaultOnKillEvent(env, mobs, 0, 5); // 1, 2, 3, 4, 5
	}
}
