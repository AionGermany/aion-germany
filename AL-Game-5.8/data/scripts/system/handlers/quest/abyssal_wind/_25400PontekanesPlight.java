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
package quest.abyssal_wind;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author FrozenKiller
 */
public class _25400PontekanesPlight extends QuestHandler {

	public static final int questId = 25400;

	public _25400PontekanesPlight() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerOnEnterWorld(questId);
		qe.registerQuestNpc(805356).addOnTalkEvent(questId); // Pontekai
		qe.registerQuestNpc(805357).addOnTalkEvent(questId); // Damia
		qe.registerQuestNpc(805358).addOnTalkEvent(questId); // Batei
		qe.registerQuestNpc(702833).addOnTalkEvent(questId); // Army Supply Chest
		qe.registerQuestNpc(702834).addOnTalkEvent(questId); // Defective Gate Repair Equipment
		qe.registerQuestNpc(702835).addOnTalkEvent(questId); // Large Bomb Chest
		qe.registerQuestNpc(805359).addOnTalkEvent(questId); // Dukas
		qe.registerQuestNpc(805360).addOnTalkEvent(questId); // Ranba
		qe.registerOnMovieEndQuest(296, questId);
		qe.registerOnEnterZone(ZoneName.get("MIREN_FORTRESS_400010000"), questId);
		qe.registerQuestNpc(883643).addOnKillEvent(questId);
	}

	@Override
	public boolean onEnterWorldEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (player.getLevel() >= 65 && player.getWorldId() == 400010000) {
				QuestService.startQuest(env);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		int targetId = env.getTargetId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();
		if (qs == null) {
			return false;
		}

		if (qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			switch (targetId) {
				case 805356: { // Pontekai
					switch (dialog) {
						case USE_OBJECT: {
							if (var == 0) {
								return sendQuestDialog(env, 1011);
							}
							else {
								return sendQuestStartDialog(env);
							}
						}
						case SETPRO1: {
							changeQuestStep(env, 0, 1, false);
							return closeDialogWindow(env);
						}
						default:
							break;
					}
				}
				case 805357: { // Damia
					switch (dialog) {
						case USE_OBJECT: {
							if (var == 1) {
								return sendQuestDialog(env, 1352);
							}
							else if (var == 8) {
								return sendQuestDialog(env, 3739);
							}
						}
						case SETPRO2: {
							changeQuestStep(env, 1, 2, false);
							return closeDialogWindow(env);
						}
						case SELECT_ACTION_3740: {
							playQuestMovie(env, 297);
							return sendQuestDialog(env, 3740);
						}
						case SET_SUCCEED: {
							changeQuestStep(env, 8, 9, false);
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
							return closeDialogWindow(env);
						}
						default:
							break;
					}
				}
				case 805358: { // Batei
					switch (dialog) {
						case USE_OBJECT: {
							return sendQuestDialog(env, 1693);
						}
						case SETPRO3: {
							changeQuestStep(env, 2, 3, false);
							return closeDialogWindow(env);
						}
						default:
							break;
					}
				}
				case 702833: { // Army Supply Chest
					switch (dialog) {
						case USE_OBJECT: {
							return true;
						}
						default:
							break;
					}
				}
				case 702834: { // Defective Gate Repair Equipment
					switch (dialog) {
						case USE_OBJECT: {
							return true;
						}
						default:
							break;

					}
				}
				case 702835: { // Large Bomb Chest
					switch (dialog) {
						case USE_OBJECT: {
							return true;
						}
						default:
							break;

					}
				}
				case 805359: { // Dukas
					switch (dialog) {
						case QUEST_SELECT: {
							if (var == 3) {
								return sendQuestDialog(env, 2034);
							}
							else if (var == 4) {
								return sendQuestDialog(env, 2375);
							}
						}
						case CHECK_USER_HAS_QUEST_ITEM: {
							return checkQuestItems(env, 3, 4, false, 10000, 10001); // 4
						}
						case SETPRO5: {
							changeQuestStep(env, 4, 5, false);
							return closeDialogWindow(env);
						}
						default:
							break;
					}
				}
				case 805360: { // Ranba
					switch (dialog) {
						case QUEST_SELECT: {
							return sendQuestDialog(env, 3398);
						}
						case SETPRO8: {
							changeQuestStep(env, 7, 8, false);
							playQuestMovie(env, 296);
							return closeDialogWindow(env);
						}
						default:
							break;
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 805356) {
				switch (dialog) {
					case USE_OBJECT: {
						return sendQuestDialog(env, 10002);
					}
					case SELECT_QUEST_REWARD: {
						return sendQuestDialog(env, 5);
					}
					default:
						return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}

	@Override
	public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName) {
		Player player = env.getPlayer();
		if (player == null) {
			return false;
		}

		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (var == 5 && zoneName == ZoneName.get("MIREN_FORTRESS_400010000")) {
				changeQuestStep(env, 5, 6, false); // 6
				return true;
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
		int var1 = qs.getQuestVarById(1);
		if (var == 6 && var1 == 0) {
			return defaultOnKillEvent(env, 883643, var1, var1 + 1, 1);
		}
		else if (var == 6 && var1 == 1) {
			qs.setQuestVar(7);
			updateQuestStatus(env);
			return true;
		}
		return false;
	}

	@Override
	public boolean onMovieEndEvent(QuestEnv env, int movieId) {
		if (movieId == 296) {
			Player player = env.getPlayer();
			TeleportService2.teleportTo(player, 400010000, 1304.8257f, 3133.5073f, 3033.7405f, (byte) 95, TeleportAnimation.BEAM_ANIMATION);
			return true;
		}
		return false;
	}
}
