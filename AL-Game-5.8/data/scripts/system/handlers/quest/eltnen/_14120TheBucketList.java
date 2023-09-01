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
package quest.eltnen;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author FrozenKiller
 * @rework pralinka
 */
public class _14120TheBucketList extends QuestHandler {

	private final static int questId = 14120;

	public _14120TheBucketList() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestItem(182215478, questId);
		qe.registerQuestNpc(203932).addOnQuestStart(questId); // Phomona
		qe.registerQuestNpc(203932).addOnTalkEvent(questId);
		qe.registerQuestNpc(730020).addOnTalkEvent(questId); // Demro
		qe.registerQuestNpc(730019).addOnTalkEvent(questId); // Lodas
		qe.registerQuestNpc(700157).addOnTalkEvent(questId); // Kerubian Bucket
	}

	@Override
	public boolean onDialogEvent(final QuestEnv env) {
		final Player player = env.getPlayer();
		int targetId = env.getTargetId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 203932) { // Phomona
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
			if (targetId == 730019) { // Lodas
				switch (dialog) {
					case QUEST_SELECT: {
						if (var == 1) {
							return sendQuestDialog(env, 2375);
						}
					}
					case CHECK_USER_HAS_QUEST_ITEM_SIMPLE: {
						return checkQuestItems(env, 1, 1, true, 5, 2716); // reward
					}
					case FINISH_DIALOG: {
						return sendQuestSelectionDialog(env);
					}
					default:
						break;
				}
			}
			else if (targetId == 700157) { // Kerubian Bucket
				if (dialog == DialogAction.USE_OBJECT) {
					return true; // loot
				}
			}
			else if (targetId == 730020) { // Demro
				if (dialog == DialogAction.QUEST_SELECT) {
					if (var == 0) {
						return sendQuestDialog(env, 1352);
					}
				}
				else if (env.getDialog() == DialogAction.SETPRO1) {
					return defaultCloseDialog(env, 0, 1); // 1
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 730019) { // Lodas
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}
