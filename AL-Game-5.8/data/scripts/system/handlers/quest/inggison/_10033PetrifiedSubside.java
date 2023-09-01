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
package quest.inggison;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.teleport.TeleportService2;

/**
 * @author pralinka
 */
public class _10033PetrifiedSubside extends QuestHandler {

	private final static int questId = 10033;

	public _10033PetrifiedSubside() {
		super(questId);
	}

	@Override
	public void register() {
		int[] npcs = { 798970, 798975, 798981, 730226, 730227, 730228 };
		qe.registerOnEnterZoneMissionEnd(questId);
		qe.registerOnLevelUp(questId);
		for (int npc : npcs) {
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
	}

	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env) {
		return defaultOnZoneMissionEndEvent(env);
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env, 10032, true);
	}

	@Override
	public boolean onDialogEvent(final QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null) {
			return false;
		}
		DialogAction dialog = env.getDialog();
		int var = qs.getQuestVarById(0);
		int targetId = env.getTargetId();
		if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 798970: {
					switch (dialog) {
						case QUEST_SELECT: {
							if (var == 0) {
								return sendQuestDialog(env, 1011);
							}
						}
						case SETPRO1: {
							return defaultCloseDialog(env, 0, 1);
						}
						default:
							break;
					}
					break;
				}
				case 798975: {
					switch (dialog) {
						case QUEST_SELECT: {
							if (var == 1) {
								return sendQuestDialog(env, 1352);
							}
							else if (var == 6) {
								return sendQuestDialog(env, 3057);
							}
						}
						case SETPRO2: {
							return defaultCloseDialog(env, 1, 2);
						}
						case SET_SUCCEED: {
							removeQuestItem(env, 182215625, 1);
							TeleportService2.teleportTo(env.getPlayer(), 210050000, 2679, 1458, 374, (byte) 102, TeleportAnimation.BEAM_ANIMATION);
							return checkQuestItems(env, 6, 7, true, 10000, 10001);
						}
						default:
							break;
					}
					break;
				}
				case 798981: {
					switch (dialog) {
						case QUEST_SELECT: {
							if (var == 2) {
								return sendQuestDialog(env, 1693);
							}
						}
						case SETPRO3: {
							giveQuestItem(env, 182215622, 1);
							return defaultCloseDialog(env, 2, 3);
						}
						default:
							break;
					}
					break;
				}
				case 730226: {
					if (var == 3 && dialog == DialogAction.USE_OBJECT) {
						removeQuestItem(env, 182215622, 1);
						giveQuestItem(env, 182215623, 1);
						return useQuestObject(env, 3, 4, false, 0);
					}
					break;
				}
				case 730227: {
					if (var == 4 && dialog == DialogAction.USE_OBJECT) {
						removeQuestItem(env, 182215623, 1);
						giveQuestItem(env, 182215624, 1);
						return useQuestObject(env, 4, 5, false, 0);
					}
					break;
				}
				case 730228: {
					if (var == 5 && dialog == DialogAction.USE_OBJECT) {
						removeQuestItem(env, 182215624, 1);
						giveQuestItem(env, 182215625, 1);
						return useQuestObject(env, 5, 6, false, 0);
					}
					break;
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 798970) {
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
