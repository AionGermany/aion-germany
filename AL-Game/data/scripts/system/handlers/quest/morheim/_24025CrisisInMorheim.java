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
package quest.morheim;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;

/**
 * @author pralinka
 */
public class _24025CrisisInMorheim extends QuestHandler {

	private final static int questId = 24025;

	public _24025CrisisInMorheim() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerOnEnterZoneMissionEnd(questId);
		qe.registerOnLevelUp(questId);
		qe.registerOnEnterWorld(questId);
		qe.registerQuestNpc(204388).addOnTalkEvent(questId);
		qe.registerQuestNpc(204414).addOnTalkEvent(questId);
		qe.registerQuestNpc(204345).addOnTalkEvent(questId);
		qe.registerQuestNpc(204304).addOnTalkEvent(questId);
	}

	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env) {
		return defaultOnZoneMissionEndEvent(env);
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env, 24020, true);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null) {
			return false;
		}

		int var = qs.getQuestVarById(0);
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc) {
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 204388: {
					switch (env.getDialog()) {
						case QUEST_SELECT:
							if (var == 0) {
								return sendQuestDialog(env, 1011);
							}
							else if (var == 2) {
								return sendQuestDialog(env, 1693);
							}
						case SETPRO1:
							return defaultCloseDialog(env, 0, 1);
						case CHECK_USER_HAS_QUEST_ITEM:
							if (var == 2) {
								if (QuestService.collectItemCheck(env, true)) {
									removeQuestItem(env, 182215370, 1);
									return defaultCloseDialog(env, 2, 3);
								}
							}
						default:
							break;
					}
					break;
				}
				case 204414: {
					switch (env.getDialog()) {
						case QUEST_SELECT:
							return sendQuestDialog(env, 1352);
						case SETPRO2:
							return defaultCloseDialog(env, 1, 2);
						default:
							break;
					}
					break;
				}
				case 204345: {
					switch (env.getDialog()) {
						case QUEST_SELECT:
							return sendQuestDialog(env, 2034);
						case SET_SUCCEED:
							return defaultCloseDialog(env, 3, 3, true, false);
						default:
							break;
					}
					break;
				}

			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 204304) {
				if (env.getDialog() == DialogAction.USE_OBJECT) {
					return sendQuestDialog(env, 10002);
				}
				else {
					return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}
}
