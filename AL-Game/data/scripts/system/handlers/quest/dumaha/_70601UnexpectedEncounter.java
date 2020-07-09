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
public class _70601UnexpectedEncounter extends QuestHandler {

	private final static int questId = 70601;
	private final static int[] mobs = { 657540, 657570, 657760, 657764, 657542, 657572, 657762, 657765, 657415, 657434, 657435, 657436, 657437, 657438 };

	public _70601UnexpectedEncounter() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerOnLevelUp(questId);
		qe.registerQuestNpc(806988).addOnTalkEvent(questId);  // Monan
		qe.registerQuestItem(182216762, questId);             // Nakisix Supply

		for (int mob : mobs) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env, 70600, false);
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
				case 806988: {
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

			// (0) Step: 5, Count: 5, Mobs : 657540, 657570, 657760, 657764
			// (1) Step: 5, Count: 5, Mobs : 657542, 657572, 657762, 657765
			// (2) Step: 5, Count: 1, Mobs : 657415
			// (3) Step: 8, Count: 5, Mobs : 657434, 657435, 657436, 657437
			// (4) Step: 8, Count: 1, Mobs : 657438

			switch (var) {
				case 0: {
				// ???
				}
				case 1: {
				// ???
				}
				case 2: {
					return defaultOnKillEvent(env, 657415, 5, 6, 0);
				}
				case 3: {
				// ???
				}
				case 4: {
					qs.setQuestVar(9);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					return true;
				}
				default:
					break;
			}
			return false;
		}
		return false;
	}
}
