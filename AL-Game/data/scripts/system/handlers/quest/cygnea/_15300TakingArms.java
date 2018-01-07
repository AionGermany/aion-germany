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
package quest.cygnea;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.instance.InstanceService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.world.WorldMapInstance;

/**
 * @author FrozenKiller
 */
public class _15300TakingArms extends QuestHandler {

	public static final int questId = 15300;
	private final static int[] mobs = { 237228, 237229 }; // Lava Protector, Heatvent Protector
	private final static int[] mobs2 = { 237225, 237226, 237227 }; // Rapacious Kadena (right),Rapacious Kadena (middle),Rapacious Kadena (left)
	private final static int[] weapons = { 100001897, 100101424, 100201604, 100501384, 100601503, 100901459, 101301347, 101501442, 101701440, 101801292, 101901198, 102001320, 102101136 };
	private static boolean hasWeaponEquiped = false;

	public _15300TakingArms() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerOnEnterZoneMissionEnd(questId);
		qe.registerOnLevelUp(questId);
		qe.registerQuestNpc(805327).addOnQuestStart(questId);
		qe.registerQuestNpc(805327).addOnTalkEvent(questId); // Dike
		qe.registerQuestNpc(805362).addOnTalkEvent(questId); // Kaisinel
		qe.registerQuestNpc(805361).addOnTalkEvent(questId); // Shabee
		qe.registerQuestNpc(209863).addOnTalkEvent(questId); // Masionel
		qe.registerQuestNpc(805363).addOnTalkEvent(questId); // Killios
		qe.registerQuestNpc(805377).addOnTalkEvent(questId); // Agony's Well
		qe.registerQuestNpc(237224).addOnKillEvent(questId); // Fetid Phantomscorch Chimera
		qe.registerQuestNpc(237231).addOnKillEvent(questId); // Exhausted Orrisan
		qe.registerQuestNpc(237236).addOnKillEvent(questId); // Commander Virtsha
		qe.registerQuestNpc(237238).addOnKillEvent(questId); // Beritra
		qe.registerGetingItem(182215903, questId);
		for (int weapon : weapons) {
			qe.registerOnEquipItem(weapon, questId);
		}
		for (int mob : mobs) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
		for (int mob2 : mobs2) {
			qe.registerQuestNpc(mob2).addOnKillEvent(questId);
		}
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();
		DialogAction dialog = env.getDialog();

		if (qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			switch (targetId) {
				case 805327: { // Dike
					switch (dialog) {
						case QUEST_SELECT: {
							return sendQuestDialog(env, 1011);
						}
						case SETPRO1: {
							TeleportService2.teleportTo(player, 110020000, 464.0f, 499.0f, 499.5998f, (byte) 0, TeleportAnimation.BEAM_ANIMATION);
							changeQuestStep(env, 0, 1, false);
							return closeDialogWindow(env);
						}
						default:
							break;
					}
				}
				case 805362: { // Kaisinel
					switch (dialog) {
						case QUEST_SELECT: {
							if (var == 13) {
								return sendQuestDialog(env, 7523);
							}
							else {
								return sendQuestDialog(env, 1352);
							}
						}
						case SETPRO2: {
							giveQuestItem(env, 182215903, 1);
							return closeDialogWindow(env);
						}
						case SETPRO14: {
							changeQuestStep(env, 13, 13, true); // Reward
							return closeDialogWindow(env);
						}
						default:
							break;
					}
				}
				case 805361: { // Shabee
					switch (dialog) {
						case QUEST_SELECT: {
							return sendQuestDialog(env, 1693);
						}
						case SETPRO3: {
							WorldMapInstance newInstance = InstanceService.getNextAvailableInstance(301520000);
							InstanceService.registerPlayerWithInstance(newInstance, player);
							TeleportService2.teleportTo(player, 301520000, newInstance.getInstanceId(), 324.513f, 183.244f, 1687.255f, (byte) 0, TeleportAnimation.BEAM_ANIMATION);
							changeQuestStep(env, 2, 3, false);
							return closeDialogWindow(env);
						}
						default:
							break;
					}
				}
				case 209863: { // Masionel
					switch (dialog) {
						case QUEST_SELECT: {
							return sendQuestDialog(env, 2375);
						}
						case SETPRO5: {
							changeQuestStep(env, 4, 5, false);
							return closeDialogWindow(env);
						}
						default:
							break;
					}
				}
				case 805363: { // Killios
					switch (dialog) {
						case QUEST_SELECT: {
							if (var == 8) {
								if (hasWeaponEquiped) {
									hasWeaponEquiped = false;
								}
								return sendQuestDialog(env, 3739);
							}
							else if (var == 9 && hasWeaponEquiped) {
								return sendQuestDialog(env, 4080);
							}
							else {
								return sendQuestDialog(env, 4166);
							}
						}
						case SETPRO9: {
							giveQuestItem(env, 188055177, 1);
							changeQuestStep(env, 8, 9, false); // 9
							return closeDialogWindow(env);
						}
						case SETPRO10: {
							changeQuestStep(env, 9, 10, false);
							return closeDialogWindow(env);
						}
						case FINISH_DIALOG: {
							return closeDialogWindow(env);
						}
						default:
							break;
					}
				}
				case 805377: { // Agony's Well
					switch (dialog) {
						case QUEST_SELECT: {
							return sendQuestDialog(env, 6841);
						}
						case SETPRO12: {
							TeleportService2.teleportTo(player, 301520000, 207.258f, 543.17f, 1754.626f, (byte) 66, TeleportAnimation.BEAM_ANIMATION);
							changeQuestStep(env, 11, 12, false);
							return closeDialogWindow(env);
						}
						default:
							break;
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 805327) {
				switch (dialog) {
					case USE_OBJECT: {
						return sendQuestDialog(env, 10002);
					}
					case SELECT_QUEST_REWARD: {
						return sendQuestDialog(env, 5);
					}
					case SELECTED_QUEST_NOREWARD: {
						return sendQuestEndDialog(env);
					}
					default:
						break;
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
		int targetId = env.getTargetId();
		if (var == 3 && var < 4) {
			return defaultOnKillEvent(env, mobs, var, var + 1); // 4
		}
		else if (var == 5 && targetId == 237224) {
			return defaultOnKillEvent(env, 237224, 5, 6); // 6
		}
		else if (var == 6 && var < 7) {
			return defaultOnKillEvent(env, mobs2, var, var + 1); // 7
		}
		else if (var == 7 && targetId == 237231) {
			return defaultOnKillEvent(env, 237231, 7, 8); // 8
		}
		else if (var == 10 && targetId == 237236) {
			return defaultOnKillEvent(env, 237236, 10, 11); // 11
		}
		else if (var == 12 && targetId == 237238) {
			return defaultOnKillEvent(env, 237238, 12, 13); // 13
		}
		return false;
	}

	@Override
	public boolean onEquipItemEvent(QuestEnv env, int itemId) {
		hasWeaponEquiped = true;
		return true;
	}

	@Override
	public boolean onGetItemEvent(QuestEnv env) {
		return defaultOnGetItemEvent(env, 1, 2, false); // 2
	}

	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env) {
		return defaultOnZoneMissionEndEvent(env, 10507);
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env, 10507, true);
	}
}
