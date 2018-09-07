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

/**
 * @author Leunam
 * @rework zhkchi
 */
public class _4078ALightThroughtheTrees extends QuestHandler {

	private final static int questId = 4078;
	private final static int[] npc_ids = { 205157, 700427, 700428, 700429 };

	public _4078ALightThroughtheTrees() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(205157).addOnQuestStart(questId);
		for (int npc_id : npc_ids) {
			qe.registerQuestNpc(npc_id).addOnTalkEvent(questId);
		}
	}

	@Override
	public boolean onDialogEvent(final QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();
		int targetId = env.getTargetId();

		if (targetId == 205157) {
			if (qs == null || qs.getStatus() == QuestStatus.NONE) {
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
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

		if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 205157) {
				switch (dialog) {
					case USE_OBJECT:
						return sendQuestDialog(env, 10002);
					case SELECT_QUEST_REWARD:
						return sendQuestDialog(env, 5);
					default:
						return sendQuestEndDialog(env);
				}
			}
		}
		else if (qs.getStatus() != QuestStatus.START) {
			return false;
		}

		if (targetId == 205157) {
			switch (env.getDialog()) {
				case QUEST_SELECT:
					if (var == 0) {
						return sendQuestDialog(env, 1011);
					}
				case CHECK_USER_HAS_QUEST_ITEM:
					if (player.getInventory().getItemCountByItemId(182209049) >= 9) {
						if (!giveQuestItem(env, 182209050, 1)) {
							return true;
						}
						removeQuestItem(env, 182209049, 9);
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						return sendQuestDialog(env, 10000);
					}
					else {
						return sendQuestDialog(env, 10001);
					}
				default:
					break;
			}
		}
		else if (targetId == 700428) {
			switch (env.getDialog()) {
				case USE_OBJECT:
					if (var == 1) {
						if (player.getInventory().getItemCountByItemId(182209050) == 1) {
							return useQuestObject(env, 1, 2, false, false); // 1
						}
					}
					return false;
				default:
					break;
			}
		}
		else if (targetId == 700427) {
			switch (env.getDialog()) {
				case USE_OBJECT:
					if (var == 2) {
						if (player.getInventory().getItemCountByItemId(182209050) == 1) {
							return useQuestObject(env, 2, 3, false, false); // 2
						}
					}
					return false;
				default:
					break;
			}
		}
		else if (targetId == 700429) {
			switch (env.getDialog()) {
				case USE_OBJECT:
					if (var == 3) {
						if (player.getInventory().getItemCountByItemId(182209050) == 1) {
							return useQuestObject(env, 3, 4, true, false); // 3
						}
					}
					return false;
				default:
					break;
			}
		}
		return false;
	}
}
