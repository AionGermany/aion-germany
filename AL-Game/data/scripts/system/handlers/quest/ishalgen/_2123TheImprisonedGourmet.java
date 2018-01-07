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
package quest.ishalgen;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author vlog
 */
public class _2123TheImprisonedGourmet extends QuestHandler {

	private final static int questId = 2123;

	public _2123TheImprisonedGourmet() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(203550).addOnQuestStart(questId);
		qe.registerQuestNpc(203550).addOnTalkEvent(questId);
		qe.registerQuestNpc(700128).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(final QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();
		int targetId = env.getTargetId();

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 203550) { // Munin
				switch (dialog) {
					case QUEST_SELECT: {
						return sendQuestDialog(env, 1011);
					}
					default: {
						return sendQuestStartDialog(env);
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 203550: { // Munin
					switch (dialog) {
						case QUEST_SELECT: {
							return sendQuestDialog(env, 1352);
						}
						case SETPRO1: {
							if (player.getInventory().getItemCountByItemId(182203121) >= 1) {
								qs.setQuestVar(5); // 5
								qs.setStatus(QuestStatus.REWARD); // rewatd
								updateQuestStatus(env);
								removeQuestItem(env, 182203121, 1);
								return sendQuestDialog(env, 5);
							}
							else {
								return sendQuestDialog(env, 1693);
							}
						}
						case SETPRO2: {
							if (player.getInventory().getItemCountByItemId(182203122) >= 1) {
								qs.setQuestVar(6); // 6
								qs.setStatus(QuestStatus.REWARD); // rewatd
								updateQuestStatus(env);
								removeQuestItem(env, 182203122, 1);
								return sendQuestDialog(env, 6);
							}
							else {
								return sendQuestDialog(env, 1693);
							}
						}
						case SETPRO3: {
							if (player.getInventory().getItemCountByItemId(182203123) >= 1) {
								qs.setQuestVar(7); // 7
								qs.setStatus(QuestStatus.REWARD); // rewatd
								updateQuestStatus(env);
								removeQuestItem(env, 182203123, 1);
								return sendQuestDialog(env, 7);
							}
							else {
								return sendQuestDialog(env, 1693);
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
				case 700128: { // Methu Egg
					return true;
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 203550) { // Munin
				return sendQuestEndDialog(env, qs.getQuestVarById(0) - 5);
			}
		}
		return false;
	}
}
