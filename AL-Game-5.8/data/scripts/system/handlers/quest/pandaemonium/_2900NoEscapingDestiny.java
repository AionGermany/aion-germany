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
package quest.pandaemonium;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.SystemMessageId;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.services.instance.InstanceService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;

/**
 * @author Mr. Poke
 * @reworked vlog
 * @modified Rolandas, apozema
 */
public class _2900NoEscapingDestiny extends QuestHandler {

	private final static int questId = 2900;

	public _2900NoEscapingDestiny() {
		super(questId);
	}

	@Override
	public void register() {
		int[] npcs = { 204182, 203550, 790003, 790002, 203546, 204264, 204061 };
		int[] stigmas = { 140000001, 140000002, 140000003, 140000004 };
		qe.registerOnLevelUp(questId);
		qe.registerOnMovieEndQuest(156, questId);
		qe.registerQuestNpc(204263).addOnKillEvent(questId);
		qe.registerOnEnterWorld(questId);
		qe.registerOnDie(questId);
		for (int npc : npcs) {
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
		for (int stigma : stigmas) {
			qe.registerOnEquipItem(stigma, questId);
		}
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null) {
			return false;
		}
		int var = qs.getQuestVars().getQuestVars();
		int targetId = env.getTargetId();
		DialogAction dialog = env.getDialog();

		if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 204182: { // Heimdall
					switch (dialog) {
						case QUEST_SELECT: {
							if (var == 0) {
								return sendQuestDialog(env, 1011);
							}
						}
						case SETPRO1: {
							TeleportService2.teleportTo(player, 220010000, 388.1806f, 1896.387f, 327.62662f, (byte) 61, TeleportAnimation.BEAM_ANIMATION);
							return defaultCloseDialog(env, 0, 1);
						}
						default:
							break;
					}
					break;
				}
				case 203550: { // Munin
					switch (dialog) {
						case QUEST_SELECT: {
							if (var == 1) {
								return sendQuestDialog(env, 1352);
							}
							else if (var == 10) {
								return sendQuestDialog(env, 4080);
							}
						}
						case SETPRO2: {
							return defaultCloseDialog(env, 1, 2);
						}
						case SETPRO10: {
							TeleportService2.teleportTo(player, 120010000, 1292.0074f, 1214.6287f, 214.3387f, (byte) 11, TeleportAnimation.BEAM_ANIMATION);
							return defaultCloseDialog(env, 10, 10, true, false);
						}
						default:
							break;
					}
					break;
				}
				case 790003: { // Urd
					switch (dialog) {
						case QUEST_SELECT: {
							if (var == 2) {
								return sendQuestDialog(env, 1693);
							}
						}
						case SETPRO3: {
							return defaultCloseDialog(env, 2, 3);
						}
						default:
							break;
					}
					break;
				}
				case 790002: { // Verdandi
					switch (dialog) {
						case QUEST_SELECT: {
							if (var == 3) {
								return sendQuestDialog(env, 2034);
							}
						}
						case SETPRO4: {
							return defaultCloseDialog(env, 3, 4);
						}
						default:
							break;
					}
					break;
				}
				case 203546: { // Skuld
					switch (dialog) {
						case QUEST_SELECT: {
							if (var == 4) {
								return sendQuestDialog(env, 2375);
							}
							else if (var == 9) {
								return sendQuestDialog(env, 3739);
							}
						}
						case SETPRO5: {
							if (var == 4) {
								changeQuestStep(env, 4, 95, false);
								WorldMapInstance newInstance = InstanceService.getNextAvailableInstance(320070000);
								InstanceService.registerPlayerWithInstance(newInstance, player);
								TeleportService2.teleportTo(player, 320070000, newInstance.getInstanceId(), 253.66963f, 259.2077f, 125.8369f, (byte) 87, TeleportAnimation.BEAM_ANIMATION);
								return closeDialogWindow(env);
							}
						}
						case SETPRO9: {
							changeQuestStep(env, 9, 10, false);
							TeleportService2.teleportTo(player, 220010000, 388.1806f, 1896.387f, 327.62662f, (byte) 61, TeleportAnimation.BEAM_ANIMATION);
							return closeDialogWindow(env);
						}
						default:
							break;
					}
					break;
				}
				case 204264: { // Skuld
					switch (dialog) {
						case USE_OBJECT: {
							if (var == 99 && !isStigmaEquipped(env)) {
								return sendQuestDialog(env, 3057);
							}
						}
						case QUEST_SELECT: {
							if (var == 95) {
								return sendQuestDialog(env, 2716);
							}
							else if (var == 96) {
								return sendQuestDialog(env, 3057);
							}
							else if (var == 97) {
								return sendQuestDialog(env, 3398);
							}
						}
						case SETPRO6: {
							if (var == 95) {
								playQuestMovie(env, 156);
								return closeDialogWindow(env);
							}
						}
						case SELECT_ACTION_3058: {
							if (var == 96) {
								if (giveQuestItem(env, getStoneId(player), 1) && !isStigmaEquipped(env)) {
									/*
									 * long existendStigmaShards = player.getInventory().getItemCountByItemId(141000001); if (existendStigmaShards < 60) { if (!player.getInventory().isFull()) {
									 * ItemService.addItem(player, 141000001, 60 - existendStigmaShards); changeQuestStep(env, 96, 99, false); return sendQuestDialog(env, 3058); } else { return
									 * closeDialogWindow(env); } } else {
									 */
									changeQuestStep(env, 96, 99, false);
									return sendQuestDialog(env, 3058);
									/* } */
								}
								else {
									return closeDialogWindow(env);
								}
							}
							else if (var == 99) {
								return sendQuestDialog(env, 3058);
							}
						}
						case SETPRO7: {
							if (var == 99) {
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 1));
								return true;
							}
						}
						case SETPRO8: {
							if (var == 97) {
								changeQuestStep(env, 97, 98, false);
								QuestService.addNewSpawn(320070000, player.getInstanceId(), 204263, 257.5f, 245f, 125f, (byte) 65);
								return closeDialogWindow(env);
							}
						}
						default:
							break;
					}
					break;
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 204061) { // Aud
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}

	@Override
	public boolean onMovieEndEvent(QuestEnv env, int movieId) {
		if (movieId == 156) {
			changeQuestStep(env, 95, 96, false);
			return true;
		}
		return false;
	}

	@Override
	public boolean onEquipItemEvent(QuestEnv env, int itemId) {
		changeQuestStep(env, 99, 97, false);
		return closeDialogWindow(env);
	}

	@Override
	public boolean onKillEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (var == 98) {
				changeQuestStep(env, 98, 9, false); // 9
				TeleportService2.teleportTo(player, 220010000, 1112.3679f, 1720.8495f, 270.0949f, (byte) 102, TeleportAnimation.BEAM_ANIMATION);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onDieEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVars().getQuestVars();
			if (player.getWorldId() != 320070000) {
				if (var >= 95 && var <= 99) {
					removeStigma(env);
					qs.setQuestVar(4);
					updateQuestStatus(env);
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(SystemMessageId.QUEST_FAILED_$1, DataManager.QUEST_DATA.getQuestById(questId).getName()));
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean onEnterWorldEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVars().getQuestVars();
			if (player.getWorldId() != 320070000) {
				if (var >= 95 && var <= 99) {
					removeStigma(env);
					qs.setQuestVar(4);
					updateQuestStatus(env);
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(SystemMessageId.QUEST_FAILED_$1, DataManager.QUEST_DATA.getQuestById(questId).getName()));
					return true;
				}
				else if (var == 9) {
					removeStigma(env);
					return true;
				}
			}
		}
		return false;
	}

	private int getStoneId(Player player) {
		switch (player.getCommonData().getPlayerClass()) {
			case GLADIATOR: {
				return 140000003; // Ferocious Strike III
			}
			case TEMPLAR: {
				return 140000003; // Ferocious Strike III
			}
			case ASSASSIN: {
				return 140000003; // Ferocious Strike III
			}
			case RANGER: {
				return 140000003; // Ferocious Strike III
			}
			case SORCERER: {
				return 140000002; // Flame Cage I
			}
			case SPIRIT_MASTER: {
				return 140000002; // Flame Cage I
			}
			case CLERIC: {
				return 140000002; // Flame Cage I
			}
			case CHANTER: {
				return 140000003; // Ferocious Strike III
			}
			case GUNNER: {
				return 140000004; // Hydro Eruption II
			}
			case BARD: {
				return 140000004; // Hydro Eruption II
			}
			case RIDER: {
				return 140000004; // Hydro Eruption II
			}
			default: {
				return 0;
			}
		}
	}

	private void removeStigma(QuestEnv env) {
		Player player = env.getPlayer();
		for (Item item : player.getEquipment().getEquippedItemsByItemId(getStoneId(player))) {
			player.getEquipment().unEquipItem(item.getObjectId(), 0);
		}
		removeQuestItem(env, getStoneId(player), 1);
	}

	private boolean isStigmaEquipped(QuestEnv env) {
		Player player = env.getPlayer();
		for (Item i : player.getEquipment().getEquippedItemsAllStigma()) {
			if (i.getItemId() == getStoneId(player)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env);
	}
}
