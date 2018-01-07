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
package quest.baruna_lab;

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
public class _16942FoundryFinds extends QuestHandler {

	private final static int questId = 16942;

	public _16942FoundryFinds() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(206361).addOnTalkEvent(questId); // Ketesivius
		qe.registerQuestNpc(802350).addOnTalkEvent(questId); // Eljer
		qe.registerOnEnterWorld(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();
		int targetId = env.getTargetId();

		if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 206361) {
				switch (dialog) {
					case QUEST_SELECT:
						return sendQuestDialog(env, 1011);
					case SETPRO1:
						return defaultCloseDialog(env, 0, 1);
					default:
						break;
				}
			}
			else if (targetId == 802350) {
				switch (dialog) {
					case QUEST_SELECT:
						return sendQuestDialog(env, 1352);
					case CHECK_USER_HAS_QUEST_ITEM:
						if (QuestService.collectItemCheck(env, true)) {
							removeQuestItem(env, 182215786, 3);
							removeQuestItem(env, 182215788, 1);
							qs.setStatus(QuestStatus.REWARD);
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
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 802350) { //
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}

	@Override
	public boolean onEnterWorldEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (player.getWorldId() == 600100000) {
				QuestService.startQuest(env);
				return true;
			}
			else {
				return false;
			}
		}
		return false;
	}
}
