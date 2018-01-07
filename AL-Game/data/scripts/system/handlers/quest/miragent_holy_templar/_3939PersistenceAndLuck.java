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
package quest.miragent_holy_templar;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * Talk with Cornelius (203780). Talk with Sabotes (203781). Collect Tear of Luck (182206098) (20) and take them to Cornelius. Take the Oath Stone (186000080) to High Priest Jucleas (203752) and ask
 * him to perform the ritual of affirmation. Talk with Lavirintos (203701).
 *
 * @author Nanou
 * @reworked vlog
 * @modified Gigi
 */
public class _3939PersistenceAndLuck extends QuestHandler {

	private final static int questId = 3939;

	public _3939PersistenceAndLuck() {
		super(questId);
	}

	@Override
	public void register() {
		int[] npcs = { 203701, 203780, 203781, 203752, 700537 };
		qe.registerQuestNpc(203701).addOnQuestStart(questId);
		for (int npc : npcs) {
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		int targetId = env.getTargetId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 203701) { // Lavirintos
				if (dialog == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 4762);
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
			switch (targetId) {
				case 203780: { // Cornelius
					switch (dialog) {
						case QUEST_SELECT:
							if (var == 0) {
								return sendQuestDialog(env, 1011);
							}
							if (var == 2) {
								return sendQuestDialog(env, 1693);
							}
						case SETPRO1:
							return defaultCloseDialog(env, 0, 1); // 1
						case CHECK_USER_HAS_QUEST_ITEM:
							return checkQuestItems(env, 2, 3, false, 10000, 10001, 182206099, 1); // 3
						case FINISH_DIALOG:
							return defaultCloseDialog(env, var, var);
						default:
							break;
					}
					break;
				}
				case 203781: { // Sabotes
					switch (dialog) {
						case QUEST_SELECT:
							if (var == 1) {
								return sendQuestDialog(env, 1352);
							}
						case SELECT_ACTION_1354: {
							if (var == 1 && player.getInventory().tryDecreaseKinah(3400000)) {
								return defaultCloseDialog(env, 1, 2, 122001274, 1, 0, 0); // 2
							}
							else {
								return sendQuestDialog(env, 1438);
							}
						}
						case FINISH_DIALOG:
							return defaultCloseDialog(env, 1, 1);
						default:
							break;
					}
					break;
				}
				case 700537:
					if (dialog == DialogAction.USE_OBJECT && var == 2) {
						return useQuestObject(env, 2, 2, false, 0);
					}
					break;
				case 203752: { // Jucleas
					switch (dialog) {
						case QUEST_SELECT: {
							if (var == 3) {
								return sendQuestDialog(env, 2034);
							}
						}
						case SET_SUCCEED: {
							if (player.getInventory().getItemCountByItemId(186000080) >= 1) {
								removeQuestItem(env, 186000080, 1);
								return defaultCloseDialog(env, 3, 3, true, false, 0);
							}
							else {
								return sendQuestDialog(env, 2120);
							}
						}
						case FINISH_DIALOG: {
							return sendQuestSelectionDialog(env);
						}
						default:
							break;
					}
					break;
				}
				// No match
				default:
					return sendQuestStartDialog(env);
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 203701) { // Lavirintos
				if (dialog == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 10002);
				}
				else {
					return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}
}
