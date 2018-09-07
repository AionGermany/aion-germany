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
package quest.altgard;

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

public class _24114YouGottaStopUmkata extends QuestHandler {

	private final static int questId = 24114;
	private final static int[] mobs = { 210722, 210588 }; // Hero Spirit

	public _24114YouGottaStopUmkata() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(203649).addOnQuestStart(questId);
		qe.registerQuestNpc(203649).addOnTalkEvent(questId); // Gulkalla
		qe.registerQuestNpc(700097).addOnTalkEvent(questId); // Umkata's Jewel Box
		qe.registerQuestNpc(700098).addOnTalkEvent(questId); // Umkata's Grave
		qe.registerQuestItem(182215475, questId);
		qe.registerQuestNpc(210752).addOnKillEvent(questId); // Umkata
		for (int mob : mobs) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
	}

	@Override
	public boolean onDialogEvent(final QuestEnv env) {
		final Player player = env.getPlayer();
		int targetId = env.getTargetId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 203649) { // Gulkalla
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
			switch (targetId) {
				case 203649: { // Gulkalla
					switch (dialog) {
						case QUEST_SELECT: {
							if (var == 3) {
								return sendQuestDialog(env, 1011);
							}
							else if (var == 1) {
								return sendQuestDialog(env, 2375);
							}
						}
						case SETPRO1: {
							return defaultCloseDialog(env, 3, 4);
						}
						default:
							break;
					}
				}
					break;
				case 700097: {
					if (env.getDialog() == DialogAction.USE_OBJECT && var == 4) {
						return true;
					}
				}
					break;
				case 700098: { // Umkata's Grave
					switch (dialog) {
						case USE_OBJECT: {
							if (var == 4) {
								return sendQuestDialog(env, 1693);
							}
						}
						case CHECK_USER_HAS_QUEST_ITEM: {
							if (QuestService.collectItemCheck(env, false)) { // don't remove yet
								QuestService.addNewSpawn(220030000, player.getInstanceId(), 210752, 2889.9834f, 1741.3108f, 254.75f, (byte) 0);
								return closeDialogWindow(env);
							}
							else {
								return sendQuestDialog(env, 1779);
							}
						}
						default:
							break;
					}
				}
					break;
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 203649) { // Gulkalla
				switch (dialog) {
					case USE_OBJECT: {
						return sendQuestDialog(env, 1352);
					}
					case SELECT_QUEST_REWARD: {
						return sendQuestDialog(env, 5);
					}
					default: {
						return sendQuestEndDialog(env);
					}
				}
			}
		}
		return false;
	}

	@Override
	public boolean onKillEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() != QuestStatus.START) {
			return false;
		}

		int var = qs.getQuestVarById(0);
		if (var >= 0 && var < 3) {
			return defaultOnKillEvent(env, mobs, var, var + 1); // 0 - 3
		}
		else if (var == 4) {
			QuestService.collectItemCheck(env, true); // remove collected items
			return defaultOnKillEvent(env, 210752, 4, true); // reward
		}
		return false;
	}
}
