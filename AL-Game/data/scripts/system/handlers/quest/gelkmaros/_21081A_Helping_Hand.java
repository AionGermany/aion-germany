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
package quest.gelkmaros;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author zhkchi
 */
public class _21081A_Helping_Hand extends QuestHandler {

	private final static int questId = 21081;

	public _21081A_Helping_Hand() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(799225).addOnQuestStart(questId); // Richelle
		qe.registerQuestNpc(799225).addOnTalkEvent(questId); // Richelle
		qe.registerQuestNpc(799332).addOnTalkEvent(questId); // Agovard
		qe.registerQuestNpc(799217).addOnTalkEvent(questId); // Renato
		qe.registerQuestNpc(799202).addOnTalkEvent(questId); // Ipses
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();
		int targetId = env.getTargetId();

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 799225) {
				switch (dialog) {
					case QUEST_SELECT:
						return sendQuestDialog(env, 1011);
					default: {
						return sendQuestStartDialog(env, 182214017, 1);
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 799332: // Brontes
				{
					switch (dialog) {
						case QUEST_SELECT: {
							return sendQuestDialog(env, 1353);
						}
						case SELECT_ACTION_1353: {
							return sendQuestDialog(env, 1353);
						}
						case SETPRO1: {
							return defaultCloseDialog(env, 0, 1);
						}
						default:
							break;
					}
				}
				case 799217: // Pilipides
				{
					switch (dialog) {
						case QUEST_SELECT: {
							return sendQuestDialog(env, 1693);
						}
						case SELECT_ACTION_1694: {
							return sendQuestDialog(env, 1694);
						}
						case SETPRO2: {
							return defaultCloseDialog(env, 1, 2);
						}
						default:
							break;
					}
				}
				case 799202: // Drenia
				{
					switch (dialog) {
						case QUEST_SELECT: {
							return sendQuestDialog(env, 2375);
						}
						case SELECT_QUEST_REWARD: {
							return defaultCloseDialog(env, 2, 3, true, true);
						}
						default:
							break;
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 799202) // Drenia
			{
				switch (env.getDialogId()) {
					case 1009: {
						return sendQuestDialog(env, 5);
					}
					default:
						return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}
}
