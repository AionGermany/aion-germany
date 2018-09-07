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
package quest.panesterra;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author FrozenKiller
 */

public class _14220NewZoneNewRules extends QuestHandler {

	private final static int questId = 14220;
	private final static int[] movies = { 904, 905, 906, 907 };

	public _14220NewZoneNewRules() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(802540).addOnQuestStart(questId);
		qe.registerQuestNpc(802540).addOnTalkEvent(questId); // Cadmar
		qe.registerQuestNpc(802541).addOnTalkEvent(questId); // Allona
		qe.registerQuestNpc(802544).addOnTalkEvent(questId); // Bellus Escort
		qe.registerQuestNpc(802545).addOnTalkEvent(questId); // Aspida Escort
		qe.registerQuestNpc(802546).addOnTalkEvent(questId); // Atanatos Escort
		qe.registerQuestNpc(802547).addOnTalkEvent(questId); // Disillon Escort
		for (int movie : movies) {
			qe.registerOnMovieEndQuest(movie, questId);
		}
	}

	@Override
	public boolean onDialogEvent(final QuestEnv env) {
		final Player player = env.getPlayer();
		int targetId = env.getTargetId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 802540) { // Cadmar
				if (dialog == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 4762);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (targetId == 802541) { // Allona
				switch (dialog) {
					case QUEST_SELECT: {
						if (var == 0) {
							return sendQuestDialog(env, 1011);
						}
						else if (var == 1) {
							return sendQuestDialog(env, 2375);
						}
					}
					case SETPRO1: {
						qs.setQuestVar(1);
						updateQuestStatus(env);
						return closeDialogWindow(env);
					}
					default:
						break;
				}
			}
			else if (targetId == 802544) { // Bellus Escort
				switch (dialog) {
					case QUEST_SELECT: {
						if (var == 1) {
							return sendQuestDialog(env, 1352);
						}
					}
					case SETPRO2: {
						playQuestMovie(env, 904);
						return true;
					}
					default:
						break;
				}
			}
			else if (targetId == 802545) { // Aspida Escort
				switch (dialog) {
					case QUEST_SELECT: {
						if (var == 1) {
							return sendQuestDialog(env, 1693);
						}
					}
					case SETPRO2: {
						playQuestMovie(env, 905);
						return true;
					}
					default:
						break;
				}
			}
			else if (targetId == 802546) { // Atanatos Escort
				switch (dialog) {
					case QUEST_SELECT: {
						if (var == 1) {
							return sendQuestDialog(env, 2034);
						}
					}
					case SETPRO2: {
						playQuestMovie(env, 906);
						return true;
					}
					default:
						break;
				}
			}
			else if (targetId == 802547) { // Disillon Escort
				switch (dialog) {
					case QUEST_SELECT: {
						if (var == 1) {
							return sendQuestDialog(env, 2375);
						}
					}
					case SETPRO2: {
						playQuestMovie(env, 907);
						return true;
					}
					default:
						break;
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 802540) { // Cadmar
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}

	@Override
	public boolean onMovieEndEvent(QuestEnv env, int movieId) {
		if (movieId == 904 || movieId == 905 || movieId == 906 || movieId == 907) {
			changeQuestStep(env, 1, 1, true); // Reward
			return closeDialogWindow(env);
		}
		return false;
	}
}
