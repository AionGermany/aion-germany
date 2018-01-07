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
package quest.fort_tiamat;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author Cheatkiller
 */
public class _30765StopTheDarkPoetaCase extends QuestHandler {

	private final static int questId = 30765;
	private final static int npcs[] = { 800071, 205846, 205866 };

	public _30765StopTheDarkPoetaCase() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(800071).addOnQuestStart(questId);
		for (int npc : npcs) {
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
		qe.registerQuestNpc(214904).addOnKillEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();
		int targetId = env.getTargetId();

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 800071) {
				if (dialog == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 1011);
				}
				else {
					return sendQuestStartDialog(env, 1073741824);
				}
			}
		}
		else if (qs != null && qs.getStatus() == QuestStatus.START) {
			if (targetId == 205846) {
				switch (dialog) {
					case QUEST_SELECT: {
						return sendQuestDialog(env, 1352);
					}
					case SETPRO1: {
						qs.setQuestVarById(5, 2);
						updateQuestStatus(env);
						return closeDialogWindow(env);
					}
					default:
						break;
				}
			}
			else if (targetId == 205866) {
				switch (dialog) {
					case QUEST_SELECT: {
						return sendQuestDialog(env, 1693);
					}
					case SETPRO2: {
						qs.setQuestVar(0);
						updateQuestStatus(env);
						return closeDialogWindow(env);
					}
					default:
						break;
				}
			}
		}
		else if (qs != null && qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 800071) {
				if (dialog == DialogAction.USE_OBJECT) {
					return sendQuestDialog(env, 2375);
				}
				else {
					return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}

	@Override
	public boolean onKillEvent(QuestEnv env) {
		return defaultOnKillEvent(env, 214904, 0, true);
	}
}
