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

/**
 * @author FrozenKiller
 */
public class _1127AncientCube extends QuestHandler {

	private final static int questId = 1127;

	public _1127AncientCube() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(798008).addOnQuestStart(questId); // Baevrunerk
		qe.registerQuestNpc(798008).addOnTalkEvent(questId); // Baevrunerk
		qe.registerQuestNpc(700001).addOnTalkEvent(questId); // Ancient Cube
		qe.registerGetingItem(182200215, questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();
		int targetId = env.getTargetId();

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 798008) { // Baevrunerk
				switch (dialog) {
					case QUEST_SELECT:
						return sendQuestDialog(env, 1011);
					case ASK_QUEST_ACCEPT:
						return sendQuestDialog(env, 4);
					case QUEST_ACCEPT_1:
						return sendQuestStartDialog(env);
					case QUEST_REFUSE_1:
						return sendQuestDialog(env, 1004);
					default:
						break;
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 700001) { // Ancient Cube
				if (dialog == DialogAction.USE_OBJECT) {
					if (!player.getInventory().isFullSpecialCube()) {
						return true; // loot
					}
				}
			}
			else if (targetId == 798008) { // Baevrunerk
				switch (dialog) {
					case QUEST_SELECT:
						return sendQuestDialog(env, 2375);
					case CHECK_USER_HAS_QUEST_ITEM:
						return checkQuestItems(env, 1, 1, true, 5, 2716); // reward
					default:
						break;
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 798008) { // Baevrunerk
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}

	@Override
	public boolean onGetItemEvent(QuestEnv env) {
		return defaultOnGetItemEvent(env, 0, 1, false); // 1
	}
}
