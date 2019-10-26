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
package quest.dumaha;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author QuestGenerator 1.15 by Mariella
 */
public class _73003ToTheStellaExcavationSite extends QuestHandler {

	private final static int questId = 73003;
	private final static int[] mobs = { 838031 };

	public _73003ToTheStellaExcavationSite() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(820412).addOnTalkEvent(questId);  // Taulant
		qe.registerQuestNpc(806999).addOnTalkEvent(questId);  // Skipjack Inanna
		qe.registerQuestItem(182216816, questId);             // STL41 Oil
		qe.registerQuestItem(182216875, questId);             // STL41 Oil
		qe.registerQuestItem(182216876, questId);             // STL41 Oil
		qe.registerQuestItem(182216817, questId);             // Inanna's Silver Ring

		for (int mob : mobs) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env, 1000, true);
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
				case 820412: {
					switch (dialog) {
						case CHECK_USER_HAS_QUEST_ITEM: {
							return checkQuestItems(env, 0, 0, true, 5, 2716);
						}
						case FINISH_DIALOG: {
							return sendQuestSelectionDialog(env);
						}
						default: 
							break;
					}
					break;
				}
				case 806999: {
					switch (dialog) {
						case QUEST_SELECT: {
							return sendQuestDialog(env, 1011);
						}
						case SET_SUCCEED: {
							qs.setQuestVar(1);
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
							return closeDialogWindow(env);
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
			if (targetId == 806999) {
				return sendQuestEndDialog(env);
			}
		}

		return false;
	}

	@Override
	public boolean onKillEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);

		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var  = qs.getQuestVarById(0);
			int var1 = qs.getQuestVarById(1);

			// (0) Step: 5, Count: 10, Mobs : 838031

			if (var == 5 && var1 < 9) {
				return defaultOnKillEvent(env, mobs, var1, var1 + 1, 1);
			} else {
				qs.setQuestVar(6);
				updateQuestStatus(env);
			}
		}
		return false;
	}
}
