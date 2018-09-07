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

package quest.heiron;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
// import com.aionemu.gameserver.network.aion.serverpackets.SM_LOOT_STATUS; // TODO: Only needed if Loot window is fixed.
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;

// import com.aionemu.gameserver.utils.PacketSendUtility; // TODO: Only needed if Loot window is fixed.

/**
 * @author FrozenKiller
 */
public class _14052RestlessSouls extends QuestHandler {

	private final static int questId = 14052;
	private final static int[] npc_ids = { 204629, 204625, 204628, 204627, 204626, 204622, 700270 };

	public _14052RestlessSouls() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerOnEnterZoneMissionEnd(questId);
		qe.registerOnLevelUp(questId);
		// qe.addHandlerSideQuestDrop(questId, 204628, 182215340, 1, 100); // TODO: Only needed if Loot window is fixed.
		for (int npc_id : npc_ids) {
			qe.registerQuestNpc(npc_id).addOnTalkEvent(questId);
		}
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null) {
			return false;
		}

		int targetId = env.getTargetId();
		DialogAction dialog = env.getDialog();

		if (qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (targetId == 204629) { // Tessia
				switch (dialog) {
					case QUEST_SELECT:
						if (var == 0) {
							return sendQuestDialog(env, 1011);
						}
						else if (var == 2) {
							return sendQuestDialog(env, 1693);
						}
					case SETPRO1:
						if (var == 0) {
							return defaultCloseDialog(env, 0, 1);
						}
						return false;
					default:
						break;
				}
			}
			else if (targetId == 204625) { // Kacias
				switch (dialog) {
					case QUEST_SELECT:
						if (var == 1) {
							return sendQuestDialog(env, 1352);
						}
						else if (var == 2) {
							return sendQuestDialog(env, 1693);
						}
						else if (var == 4) {
							return sendQuestDialog(env, 2375);
						}
					case CHECK_USER_HAS_QUEST_ITEM:
						if (QuestService.collectItemCheck(env, true)) {
							if (!giveQuestItem(env, 182215344, 1)) {
								return true;
							}
							changeQuestStep(env, 2, 3, false); // 3
							return sendQuestDialog(env, 10000);
						}
						else {
							return sendQuestDialog(env, 10001);
						}
					case SETPRO2:
						if (var == 1) {
							return defaultCloseDialog(env, 1, 2);
						}
					case SET_SUCCEED:
						if (var == 4) {
							changeQuestStep(env, 4, 4, true); // 4 Reward
							return closeDialogWindow(env);
						}
						return false;
					default:
						break;
				}
			}
			else if (targetId == 204628) { // Kalkas
				if (var == 2) {
					if (player.getInventory().getItemCountByItemId(182215340) == 1) {
						return closeDialogWindow(env);
					}
				}
				switch (dialog) {
					case QUEST_SELECT:
						if (var == 2) {
							return sendQuestDialog(env, 1694);
						}
					case SETPRO3:
						if (var == 2) {
							if (player.getInventory().getItemCountByItemId(182215340) == 0) {
								if (!giveQuestItem(env, 182215340, 1)) {
									return true;
								}
								VisibleObject target = player.getTarget();
								Creature creature = (Creature) target;
								creature.getController().onAttack(player, creature.getLifeStats().getMaxHp() + 1, true);
								// PacketSendUtility.sendPacket(player, new SM_LOOT_STATUS(targetId, 2)); //TODO Loot window empty!
								return true;
							}
						}
						return false;
					default:
						break;
				}
			}
			else if (targetId == 204627) { // Mempion
				if (var == 2) {
					if (player.getInventory().getItemCountByItemId(182215341) == 1) {
						return closeDialogWindow(env);
					}
				}
				switch (dialog) {
					case QUEST_SELECT:
						if (var == 2) {
							return sendQuestDialog(env, 1779);
						}
					case SETPRO3:
						if (var == 2) {
							if (player.getInventory().getItemCountByItemId(182215341) == 0) {
								if (!giveQuestItem(env, 182215341, 1)) {
									return true;
								}
								VisibleObject target = player.getTarget();
								Creature creature = (Creature) target;
								creature.getController().onAttack(player, creature.getLifeStats().getMaxHp() + 1, true);
								// PacketSendUtility.sendPacket(player, new SM_LOOT_STATUS(targetId, 2)); //TODO Loot window empty!
								return closeDialogWindow(env);
							}
						}
						return false;
					default:
						break;
				}
			}
			else if (targetId == 204626) { // Spina
				if (var == 2) {
					if (player.getInventory().getItemCountByItemId(182215342) == 1) {
						return closeDialogWindow(env);
					}
				}
				switch (dialog) {
					case QUEST_SELECT:
						if (var == 2) {
							return sendQuestDialog(env, 1864);
						}
					case SETPRO3:
						if (var == 2) {
							if (player.getInventory().getItemCountByItemId(182215342) == 0) {
								if (!giveQuestItem(env, 182215342, 1)) {
									return true;
								}
								VisibleObject target = player.getTarget();
								Creature creature = (Creature) target;
								creature.getController().onAttack(player, creature.getLifeStats().getMaxHp() + 1, true);
								// PacketSendUtility.sendPacket(player, new SM_LOOT_STATUS(targetId, 2)); //TODO Loot window empty!
								return closeDialogWindow(env);
							}
						}
						return false;
					default:
						break;
				}
			}
			else if (targetId == 204622) { // Ladon
				if (var == 2) {
					if (player.getInventory().getItemCountByItemId(182215343) == 1) {
						return true;
					}
				}
				switch (dialog) {
					case QUEST_SELECT:
						if (var == 2) {
							return sendQuestDialog(env, 1949);
						}
					case SETPRO3:
						if (var == 2) {
							if (player.getInventory().getItemCountByItemId(182215343) == 0) {
								if (!giveQuestItem(env, 182215343, 1)) {
									return true;
								}
								VisibleObject target = player.getTarget();
								Creature creature = (Creature) target;
								creature.getController().onAttack(player, creature.getLifeStats().getMaxHp() + 1, true);
								// PacketSendUtility.sendPacket(player, new SM_LOOT_STATUS(targetId, 2)); //TODO Loot window empty!
								return closeDialogWindow(env);
							}
						}
						return false;
					default:
						break;
				}
			}
			else if (targetId == 700270) { // Empty Stone Coffin
				if (env.getDialog() == DialogAction.USE_OBJECT) {
					return useQuestObject(env, 3, 4, false, 0, 0, 1, 182215344, 1); // 4
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 204629) {
				return sendQuestEndDialog(env);
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
		return defaultOnLvlUpEvent(env, 14050, true);
	}
}
