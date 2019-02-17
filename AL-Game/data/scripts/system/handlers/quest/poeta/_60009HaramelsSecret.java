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
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author QuestGenerator by Mariella
 * @rework FrozenKiller
 */
public class _60009HaramelsSecret extends QuestHandler {

	private final static int questId = 60009;
	private final static int[] mobs = { 653196, 653205, 653218 };

	public _60009HaramelsSecret() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerOnLevelUp(questId);
		qe.registerQuestNpc(820012).addOnTalkEvent(questId); // Third Odium Transport Track
		qe.registerQuestNpc(820133).addOnTalkEvent(questId); // Royer
		qe.registerQuestNpc(799524).addOnTalkEvent(questId); // Gestanerk
		qe.registerQuestNpc(820006).addOnTalkEvent(questId); // Kasis
		qe.registerQuestNpc(700834).addOnTalkEvent(questId); // Odella
		qe.registerOnEnterWorld(questId);
		qe.registerOnEnterZone(ZoneName.get("IDNOVICE_SENSORYAREA_Q60009A_300200000"), questId);
		for (int mob : mobs) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env, 60000, false);
	}
	
	@Override
	public boolean onEnterWorldEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null) {
			return false;
		}
		if (qs.getStatus() == QuestStatus.START) {
			if (qs.getQuestVarById(0) == 1 && player.getWorldId() == 300200000) {
				qs.setQuestVar(2);
				updateQuestStatus(env);
				return true;
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
			if (var == 4 && zoneName == ZoneName.get("IDNOVICE_SENSORYAREA_Q60009A_300200000")) {
				changeQuestStep(env, 4, 5, false); // 5
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();
		int targetId = env.getTargetId();

		if (qs == null) {
			return false;
		}

		if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 820012: {
					switch (dialog) {
						case USE_OBJECT: {
							qs.setQuestVar(1);
							updateQuestStatus(env);
							return false;
						}
						default: 
							break;
					}
					break;
				}
				case 820133: {
					switch (dialog) {
						case USE_OBJECT: {
							return sendQuestDialog(env, 1693);
						}
						case SETPRO3: {
							qs.setQuestVar(3);
							updateQuestStatus(env);
							return closeDialogWindow(env);
						}
						default: 
							break;
					}
				}
				case 700834: {
					switch (dialog) {
						case USE_OBJECT: {
							return true;							
						}
						default: 
							break;
					}
				}
				case 799524: {
					switch (dialog) {
						case QUEST_SELECT: {
							if (qs.getQuestVarById(0) == 6) {
								return sendQuestDialog(env, 3057);
							} else {
								return sendQuestDialog(env, 3398);
							}
						}
						case CHECK_USER_HAS_QUEST_ITEM: {
							if (QuestService.collectItemCheck(env,true)) {
								qs.setQuestVar(7);
								updateQuestStatus(env);
								return sendQuestDialog(env, 10000);
							} else {
								return sendQuestDialog(env, 10001);
							}
						}
						case SETPRO8: {
							qs.setQuestVar(8);
							updateQuestStatus(env);
							return closeDialogWindow(env);
						}
						default: 
							break;
					}
					break;
				}
				default:
					break;
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 820006) {
				return sendQuestEndDialog(env);
			}
		}

		return false;
	}
	
	@Override
	public boolean onKillEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);

		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);

			// (0) Step: 3, Count: 1, Mobs : 653196
			// (1) Step: 5, Count: 1, Mobs : 653205
			// (2) Step: 8, Count: 1, Mobs : 653218

			switch (var) {
				case 3: {
					return defaultOnKillEvent(env, 653196, 3, 4, 0);
				}
				case 5: {
					return defaultOnKillEvent(env, 653205, 5, 6, 0);
				}
				case 8: {
					qs.setQuestVar(9);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					return true;
				}
				default:
					break;
			}
			return false;
		}
		return false;
	}
}
