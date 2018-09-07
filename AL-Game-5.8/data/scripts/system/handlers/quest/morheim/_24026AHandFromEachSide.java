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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.ai2.AbstractAI;
import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.WorldMapType;

/**
 * @author pralinka
 */
public class _24026AHandFromEachSide extends QuestHandler {

	private final static int questId = 24026;

	private static List<Integer> mobs = new ArrayList<Integer>();

	static {
		mobs.add(280818);
		mobs.add(211624);
		mobs.add(213578);
		mobs.add(213579);
	}

	public _24026AHandFromEachSide() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerOnEnterZoneMissionEnd(questId);
		qe.registerOnLevelUp(questId);
		for (int mob : mobs) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
		qe.registerOnQuestTimerEnd(questId);
		qe.registerOnDie(questId);
		qe.registerOnLogOut(questId);
		qe.registerQuestNpc(204301).addOnTalkEvent(questId);
		qe.registerQuestNpc(204403).addOnTalkEvent(questId);
		qe.registerQuestNpc(204432).addOnTalkEvent(questId);
	}

	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env) {
		int[] quests = { 24021, 24022, 24023, 24024, 24025 };
		return defaultOnZoneMissionEndEvent(env, quests);
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		int[] quests = { 24021, 24022, 24023, 24024, 24025 };
		return defaultOnLvlUpEvent(env, quests, true);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null) {
			return false;
		}
		int var = qs.getQuestVarById(0);
		int targetId = env.getTargetId();

		if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 204301: {
					switch (env.getDialog()) {
						case QUEST_SELECT:
							if (var == 0) {
								return sendQuestDialog(env, 1011);
							}
						case SETPRO1:
							giveQuestItem(env, 182215371, 1);
							TeleportService2.teleportTo(player, 220020000, 2795.9f, 478.37f, 265.86f, (byte) 51, TeleportAnimation.BEAM_ANIMATION);
							return defaultCloseDialog(env, 0, 1);
						default:
							break;
					}
					break;
				}
				case 204403: {
					switch (env.getDialog()) {
						case QUEST_SELECT:
							if (var == 1) {
								return sendQuestDialog(env, 1352);
							}
						case SETPRO2:
							giveQuestItem(env, 182215372, 1);
							TeleportService2.teleportTo(player, 220020000, 3025.54f, 868.31f, 363.22f, (byte) 14, TeleportAnimation.BEAM_ANIMATION);
							return defaultCloseDialog(env, 1, 2);
						default:
							break;
					}
					break;
				}
				case 204432: {
					switch (env.getDialog()) {
						case QUEST_SELECT: {
							switch (qs.getQuestVarById(0)) {
								case 2: {
									return sendQuestDialog(env, 1693);
								}
								case 4: {
									return sendQuestDialog(env, 2034);
								}
							}
						}
						case SETPRO3: {
							qs.setQuestVar(3);
							updateQuestStatus(env);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
							QuestService.questTimerStart(env, 180);
							spawn(player);
							return true;
						}
						case SETPRO4: {
							removeQuestItem(env, 182215371, 1);
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
							TeleportService2.teleportTo(player, WorldMapType.MORHEIM.getId(), 3030.8676f, 875.6538f, 363.2065f, (byte) 50, TeleportAnimation.BEAM_ANIMATION);
							return true;
						}
						default:
							break;
					}
					break;
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 204301) {
				if (env.getDialog() == DialogAction.USE_OBJECT) {
					return sendQuestDialog(env, 2375);
				}
				else {
					return sendQuestEndDialog(env);
				}
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
			if (var == 3) {
				int targetId = env.getTargetId();
				if (mobs.contains(targetId)) {
					spawn(player);
					return true;
				}
			}
		}
		return false;
	}

	private void spawn(Player player) {
		int mobToSpawn = mobs.get(Rnd.get(0, 2));
		float x = 0;
		float y = 0;
		final float z = 217.48f;
		switch (mobToSpawn) {
			case 280818: {
				x = 254.74f;
				y = 236.72f;
				break;
			}
			case 211624: {
				x = 257.92f;
				y = 237.39f;
				break;
			}
			case 213578: {
				x = 261.86f;
				y = 237.5f;
				break;
			}
			case 213579: {
				x = 268.86f;
				y = 243.5f;
				break;
			}
		}
		Npc spawn = (Npc) QuestService.spawnQuestNpc(320040000, player.getInstanceId(), mobToSpawn, x, y, z, (byte) 95);
		Collection<Npc> allNpcs = World.getInstance().getNpcs();
		Npc target = null;
		for (Npc npc : allNpcs) {
			if (npc.getNpcId() == 204432) {
				target = npc;
			}
		}
		if (target != null) {
			spawn.setTarget(target);
			((AbstractAI) spawn.getAi2()).setStateIfNot(AIState.WALKING);
			spawn.setState(1);
			spawn.getMoveController().moveToTargetObject();
			PacketSendUtility.broadcastPacket(spawn, new SM_EMOTION(spawn, EmotionType.START_EMOTE2, 0, spawn.getObjectId()));
		}
	}

	@Override
	public boolean onQuestTimerEndEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (var == 3) {
				qs.setQuestVar(4);
				updateQuestStatus(env);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onDieEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() != QuestStatus.START) {
			return false;
		}
		if (qs.getQuestVarById(0) == 3) {
			QuestService.questTimerEnd(env);
			qs.setQuestVar(2); // 2
			updateQuestStatus(env);
			return true;
		}
		return false;
	}

	@Override
	public boolean onLogOutEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (var == 3) {
				qs.setQuestVar(2);
				updateQuestStatus(env);
				return true;
			}
		}
		return false;
	}
}
