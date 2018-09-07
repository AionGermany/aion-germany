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
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.services.instance.InstanceService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.world.WorldMapInstance;

/**
 * @author pralinka
 * @rework FrozenKiller //TODO missing Animation or Movie + NPC remove 216530 => ! SNIFF ON OFFI ! + Add missing spawns (216530 + 216529)
 */
public class _10034FoundUnderground extends QuestHandler {

	private final static int questId = 10034;

	public _10034FoundUnderground() {
		super(questId);
	}

	@Override
	public void register() {
		int[] npcs = { 799030, 799029, 798990, 730295, 700604, 730229 };
		qe.registerOnEnterWorld(questId);
		qe.registerOnEnterZoneMissionEnd(questId);
		qe.registerOnLevelUp(questId);
		qe.registerQuestNpc(216531).addOnKillEvent(questId);
		qe.registerQuestItem(182215628, questId);
		qe.registerGetingItem(182215628, questId);
		for (int npc : npcs) {
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
	}

	@Override
	public boolean onEnterWorldEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVars().getQuestVars();
			if (var == 3) {
				if (player.getWorldId() == 300160000) {
					qs.setQuestVar(4);
					updateQuestStatus(env);
					return true;
				}
				else {
					if (player.getInventory().getItemCountByItemId(182215627) == 0) {
						return giveQuestItem(env, 182215627, 1);
					}
				}
			}
			else if (var >= 4 && var < 7) {
				if (player.getWorldId() != 300160000) {
					changeQuestStep(env, var, 3, false);
					if (player.getInventory().getItemCountByItemId(182215627) == 0) {
						giveQuestItem(env, 182215627, 1);
					}
					return true;
				}
			}
		}
		return false;
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
				case 799030: {
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
				case 799029: {
					switch (dialog) {
						case QUEST_SELECT: {
							if (var == 1) {
								return sendQuestDialog(env, 1352);
							}
						}
						case SETPRO2: {
							return defaultCloseDialog(env, 1, 2);
						}
						default:
							break;
					}
					break;
				}
				case 798990: {
					switch (dialog) {
						case QUEST_SELECT: {
							if (var == 2) {
								return sendQuestDialog(env, 1693);
							}
						}
						case SETPRO3: {
							playQuestMovie(env, 504);
							return defaultCloseDialog(env, 2, 3);
						}
						default:
							break;
					}
					break;
				}
				case 730295: {
					switch (dialog) {
						case QUEST_SELECT: {
							if (var == 3) {
								return sendQuestDialog(env, 2034);
							}
						}
						case SETPRO4: {
							if (var == 3) {
								if (player.getInventory().getItemCountByItemId(182215627) > 0) {
									removeQuestItem(env, 182215627, 1);
									WorldMapInstance newInstance = InstanceService.getNextAvailableInstance(300160000);
									InstanceService.registerPlayerWithInstance(newInstance, player);
									TeleportService2.teleportTo(player, 300160000, newInstance.getInstanceId(), 795.28143f, 918.806f, 149.80243f, (byte) 73, TeleportAnimation.BEAM_ANIMATION);
									return true;
								}
								else {
									return sendQuestDialog(env, 10001);
								}
							}
						}
						default:
							break;
					}
					break;
				}
				case 700604: {
					if (var == 4 && dialog == DialogAction.USE_OBJECT) {
						return useQuestObject(env, 4, 5, false, 0);
					}
					break;
				}
				case 730229: {
					if (var == 6) {
						switch (dialog) {
							case USE_OBJECT: { // Handled by QuestItemNpcAI2
								return true;
							}
							case SETPRO7: {
								giveQuestItem(env, 182215628, 1);
								VisibleObject cre = player.getTarget();
								Npc npc = (Npc) cre;
								npc.getController().onDelete();
								return defaultOnGetItemEvent(env, 6, 7, false);
							}
							default:
								break;
						}
					}
				}
					break;
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 799030) {
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

	@Override
	public HandlerResult onItemUseEvent(QuestEnv env, Item item) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int itemId = item.getItemId();
			if (itemId == 182215628) {
				qs.setQuestVar(8);
				qs.setStatus(QuestStatus.REWARD);
				updateQuestStatus(env);
				TeleportService2.teleportTo(env.getPlayer(), 210050000, 388, 980, 461, (byte) 2, TeleportAnimation.BEAM_ANIMATION);
				return HandlerResult.SUCCESS;
			}
		}
		return HandlerResult.FAILED;
	}

	@Override
	public boolean onKillEvent(final QuestEnv env) {
		final Player player = env.getPlayer();
		final int instanceId = player.getInstanceId();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() != QuestStatus.START) {
			return false;
		}
		int var = qs.getQuestVarById(0);
		switch (env.getTargetId()) {
			case 216531:
				if (var == 5) {
					QuestService.addNewSpawn(300160000, instanceId, 730229, 744, 885, 154, (byte) 90);
					QuestService.addNewSpawn(300160000, instanceId, 804815, 776, 876, 151, (byte) 90);
					qs.setQuestVarById(0, var + 1);
					updateQuestStatus(env);
					return true;
				}
		}
		return false;
	}

	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env) {
		return defaultOnZoneMissionEndEvent(env);
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env, 10033, true);
	}
}
