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

import java.util.ArrayList;
import java.util.List;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.PlayerClass;
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.actions.NpcActions;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.state.CreatureState;
import com.aionemu.gameserver.network.aion.SystemMessageId;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ASCENSION_MORPH;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.ClassChangeService;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.services.instance.InstanceService;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldMapInstance;

/**
 * @author FrozenKiller
 */
public class _1006LastTest extends QuestHandler {

	private final static int questId = 1006;

	public _1006LastTest() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(820006).addOnTalkEvent(questId);
		qe.registerQuestNpc(820148).addOnTalkEvent(questId);
		qe.registerQuestNpc(730008).addOnTalkEvent(questId);
		qe.registerQuestNpc(205000).addOnTalkEvent(questId);
		qe.registerQuestNpc(211042).addOnKillEvent(questId);
		qe.registerQuestNpc(211043).addOnKillEvent(questId);
		qe.registerOnLevelUp(questId);
		qe.registerOnDie(questId);
		qe.registerOnMovieEndQuest(14, questId);
		qe.registerOnMovieEndQuest(151, questId);
		qe.registerQuestItem(182200007, questId);
	}

	@Override
	public boolean onDialogEvent(final QuestEnv env) {
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null) {
			return false;
		}
		int targetId = env.getTargetId();
		DialogAction action = env.getDialog();

		if (qs != null && qs.getStatus() == QuestStatus.START) {
			if (targetId == 820006) {
				int var = qs.getQuestVarById(0);
				if (var == 0) {
					switch (action) {
					case QUEST_SELECT:
						return sendQuestDialog(env, 1011);
					case SELECT_ACTION_1012:
						return sendQuestDialog(env, 1012);
					case SETPRO1:
						qs.setQuestVar(1);
						updateQuestStatus(env);
						ItemService.addItem(player, 182200007, 1);
						return closeDialogWindow(env);
					default:
						break;
					}
				} else if (var == 3) {
					switch (action) {
					case QUEST_SELECT:
						return sendQuestDialog(env, 2034);
					case SETPRO4:
						WorldMapInstance newInstance = InstanceService.getNextAvailableInstance(310020000);
						InstanceService.registerPlayerWithInstance(newInstance, player);
						TeleportService2.teleportTo(player, 310020000, newInstance.getInstanceId(), 53.68013f, 177.00006f, 229.04645f, (byte) 5, TeleportAnimation.BEAM_ANIMATION);
						qs.setQuestVar(99); // 99
						updateQuestStatus(env);
						removeQuestItem(env, 182200009, 1);
						return closeDialogWindow(env);
					default:
						break;

					}
				}
			} else if (targetId == 820148) {
				switch (action) {
				case QUEST_SELECT:
					return sendQuestDialog(env, 2034);
				case SETPRO4:
					PlayerClass playerClass = player.getCommonData().getPlayerClass();
					if (playerClass.isStartingClass()) {
						if (playerClass == PlayerClass.WARRIOR) {
							return sendQuestDialog(env, 2375);
						} else if (playerClass == PlayerClass.SCOUT) {
							return sendQuestDialog(env, 2716);
						} else if (playerClass == PlayerClass.MAGE) {
							return sendQuestDialog(env, 3057);
						} else if (playerClass == PlayerClass.PRIEST) {
							return sendQuestDialog(env, 3398);
						} else if (playerClass == PlayerClass.ENGINEER) {
							return sendQuestDialog(env, 3739);
						} else if (playerClass == PlayerClass.ARTIST) {
							return sendQuestDialog(env, 4080);
						}
					}
				case SETPRO5:
					return setPlayerClass(env, qs, PlayerClass.GLADIATOR);
				case SETPRO6:
					return setPlayerClass(env, qs, PlayerClass.TEMPLAR);
				case SETPRO7:
					return setPlayerClass(env, qs, PlayerClass.ASSASSIN);
				case SETPRO8:
					return setPlayerClass(env, qs, PlayerClass.RANGER);
				case SETPRO9:
					return setPlayerClass(env, qs, PlayerClass.SORCERER);
				case SETPRO10:
					return setPlayerClass(env, qs, PlayerClass.SPIRIT_MASTER);
				case SETPRO11:
					return setPlayerClass(env, qs, PlayerClass.CLERIC);
				case SETPRO12:
					return setPlayerClass(env, qs, PlayerClass.CHANTER);
				case SETPRO13:
					return setPlayerClass(env, qs, PlayerClass.GUNNER);
				case SETPRO14:
					return setPlayerClass(env, qs, PlayerClass.BARD);
				case SETPRO15:
					return setPlayerClass(env, qs, PlayerClass.RIDER);
				case SETPRO16:
					return setPlayerClass(env, qs, PlayerClass.PAINTER);
				default:
					break;
				}
			} else if (targetId == 730008) {
				switch (action) {
				case QUEST_SELECT:
					return sendQuestDialog(env, 1693);
				case SELECT_ACTION_1694:
					return sendQuestDialog(env, 1694);
				case SETPRO3:
					playQuestMovie(env, 14);
					return true;
				default:
					break;
				}
			} else if (targetId == 205000) {
				switch (action) {
				case QUEST_SELECT: {
					if (qs.getQuestVars().getQuestVars() == 99) {
						SkillEngine.getInstance().applyEffectDirectly(281, player, player, 0);
						player.setState(CreatureState.FLIGHT_TELEPORT);
						player.unsetState(CreatureState.ACTIVE);
						player.setFlightTeleportId(1001);
						PacketSendUtility.sendPacket(player, new SM_EMOTION(player, EmotionType.START_FLYTELEPORT, 1001, 0));
						qs.setQuestVar(50); // 50
						updateQuestStatus(env);
						final int instanceId = player.getInstanceId();
						ThreadPoolManager.getInstance().schedule(new Runnable() {
							@Override
							public void run() {
								qs.setQuestVar(51);
								updateQuestStatus(env);
								List<Npc> mobs = new ArrayList<Npc>();
								mobs.add((Npc) QuestService.spawnQuestNpc(310020000, instanceId, 211042,
										(float) 224.073, (float) 239.1, (float) 206.7, (byte) 0));
								mobs.add((Npc) QuestService.spawnQuestNpc(310020000, instanceId, 211042, (float) 233.5,
										(float) 241.04, (float) 206.365, (byte) 0));
								mobs.add((Npc) QuestService.spawnQuestNpc(310020000, instanceId, 211042, (float) 229.6,
										(float) 265.7, (float) 205.7, (byte) 0));
								mobs.add((Npc) QuestService.spawnQuestNpc(310020000, instanceId, 211042, (float) 222.8,
										(float) 262.5, (float) 205.7, (byte) 0));
								for (Npc mob : mobs) {
									mob.getAggroList().addDamage(player, 1000);
								}
							}
						}, 43000);
						return true;
					}
				}
				default:
					break;
				}
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 820148) {
				switch (env.getDialog()) {
				case SELECTED_QUEST_NOREWARD:
					if (player.getWorldId() == 310020000) {
						TeleportService2.teleportTo(player, 210010000, 862.7504f, 1233.8612f, 118.703094f, (byte) 15,
								TeleportAnimation.BEAM_ANIMATION);
					}
					break;
				default:
					break;
				}
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
			Npc npc = (Npc) env.getVisibleObject();
			int var = qs.getQuestVarById(0);
			int targetId = env.getTargetId();
			if (targetId == 211042) {
				NpcActions.delete(npc);
				if (var >= 51 && var < 54) {
					return defaultOnKillEvent(env, 211042, 51, 54); // 52 - 54
				} else if (var == 54) {
					qs.setQuestVar(4); // 4
					updateQuestStatus(env);
					Npc mob = (Npc) QuestService.spawnQuestNpc(310020000, player.getInstanceId(), 211043, (float) 226.7,
							(float) 251.5, (float) 205.5, (byte) 0);
					mob.getAggroList().addDamage(player, 1000);
					return true;
				}
			} else if (targetId == 211043 && var == 4) {
				playQuestMovie(env, 151);
				for (Npc npcInside : player.getPosition().getWorldMapInstance().getNpcs()) {
					NpcActions.delete(npcInside);
				}
				QuestService.addNewSpawn(310020000, player.getInstanceId(), 820148, (float) 220.6, (float) 247.8,
						(float) 206.0, (byte) 0);
				qs.setQuestVar(5); // 5
				updateQuestStatus(env);
			}
		}
		return false;
	}

	private boolean setPlayerClass(QuestEnv env, QuestState qs, PlayerClass playerClass) {
		Player player = env.getPlayer();
		if (player.getPlayerClass().isStartingClass()) {
			ClassChangeService.setClass(player, playerClass);
			player.getController().upgradePlayer();
			changeQuestStep(env, 5, 5, true); // reward
			return sendQuestDialog(env, 5);
		}
		return false;
	}

	@Override
	public boolean onDieEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() != QuestStatus.START) {
			int var = qs.getQuestVars().getQuestVars();
			if (var == 4 || (var == 5 && player.getPlayerClass().isStartingClass()) || (var >= 50 && var <= 55)) {
				qs.setQuestVar(3);
				updateQuestStatus(env);
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(SystemMessageId.QUEST_FAILED_$1, DataManager.QUEST_DATA.getQuestById(questId).getName()));
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
			if (var == 4 || (var == 5 && player.getPlayerClass().isStartingClass()) || (var >= 50 && var <= 55)
					|| var == 99) {
				if (player.getWorldId() != 310020000) {
					qs.setQuestVar(3);
					updateQuestStatus(env);
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(SystemMessageId.QUEST_FAILED_$1, DataManager.QUEST_DATA.getQuestById(questId).getName()));
				} else {
					PacketSendUtility.sendPacket(player, new SM_ASCENSION_MORPH(1));
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public HandlerResult onItemUseEvent(final QuestEnv env, final Item item) {
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);

		if (item.getItemId() != 182200007) {
			return HandlerResult.FAILED;
		}

		if (qs != null && qs.getStatus() == QuestStatus.START) {
			PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), item.getObjectId(), item.getItemId(), 3000, 0, 0), true);
			ThreadPoolManager.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), item.getObjectId(), item.getItemId(), 0, 1, 0), true);
					removeQuestItem(env, 182200007, 1);
					TeleportService2.teleportTo(player, 210010000, 498f, 1531.2f, 104.7877f, (byte) 40, TeleportAnimation.BEAM_ANIMATION);
					qs.setQuestVar(2);
					updateQuestStatus(env);
				}
			}, 3000);
		}
		return HandlerResult.SUCCESS;
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env);
	}

	@Override
	public boolean onMovieEndEvent(QuestEnv env, int movieId) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);

		if (qs == null || qs.getStatus() != QuestStatus.START) {
			return false;
		}

		if (movieId == 14) {
			removeQuestItem(env, 182200008, 1);
			giveQuestItem(env, 182200009, 1);
			qs.setQuestVar(3);
			updateQuestStatus(env);
			return true;
		}
		return false;
	}
}
