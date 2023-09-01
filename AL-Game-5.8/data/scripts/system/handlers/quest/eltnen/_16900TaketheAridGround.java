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
 */

public class _16900TaketheAridGround extends QuestHandler {

	private final static int questId = 16900;

	public _16900TaketheAridGround() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(203901).addOnQuestStart(questId);
		qe.registerQuestNpc(203901).addOnTalkEvent(questId); // Telemachus
		qe.registerQuestNpc(203965).addOnTalkEvent(questId); // Castor
		qe.registerQuestNpc(231549).addOnKillEvent(questId); // Brash Lepharist Captain
		qe.registerQuestNpc(231551).addOnKillEvent(questId); // Eltnen Guerilla
	}

	@Override
	public boolean onDialogEvent(final QuestEnv env) {
		final Player player = env.getPlayer();
		int targetId = env.getTargetId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 203901) { // Telemachus
				if (dialog == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 1011);
				}
				else {
					return sendQuestStartDialog(env, -1);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (targetId == 203965) { // Castor
				switch (dialog) {
					case USE_OBJECT: {
						if (var == 1) {
							return sendQuestDialog(env, 2375);
						}
						else {
							return sendQuestDialog(env, 1352);
						}
					}
					case SETPRO1: {
						qs.setQuestVar(0);
						updateQuestStatus(env);
						return sendQuestSelectionDialog(env);
					}
					case SELECT_QUEST_REWARD: {
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						return sendQuestDialog(env, 5);
					}
					case FINISH_DIALOG: {
						return sendQuestSelectionDialog(env);
					}
					default:
						break;
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 203965) { // Dionera
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}

	@Override
	public boolean onKillEvent(QuestEnv env) {
		int targetId = env.getTargetId();
		if (targetId == 231549) {
			return defaultOnKillEvent(env, 231549, 0, 1); // 1
		}
		else if (targetId == 231551) {
			return defaultOnKillEvent(env, 231551, 0, 1); // 1
		}
		return false;
	}
}
