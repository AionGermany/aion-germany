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
package quest.gelkmaros;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.Npc;
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
 * @author pralinka
 */
public class _20034RescueTheReians extends QuestHandler {

	private final static int questId = 20034;
	private final static int[] npc_ids = { 799295, 799297, 730243, 799513, 799514, 799515, 799516, 799341, 700706 };

	public _20034RescueTheReians() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerOnDie(questId);
		qe.registerOnLogOut(questId);
		qe.registerOnLevelUp(questId);
		qe.registerQuestNpc(216592).addOnKillEvent(questId);
		qe.registerOnMovieEndQuest(442, questId);
		qe.registerOnEnterWorld(questId);
		qe.registerOnEnterZoneMissionEnd(questId);
		for (int npc_id : npc_ids) {
			qe.registerQuestNpc(npc_id).addOnTalkEvent(questId);
		}
	}

	@Override
	public boolean onEnterWorldEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVars().getQuestVars();
			if (var == 2) {
				if (player.getWorldId() == 300150000) {
					qs.setQuestVar(3);
					updateQuestStatus(env);
				}
			}
			if (var == 6) {
				if (player.getWorldId() == 220070000) {
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
				}
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
		int var = qs.getQuestVars().getQuestVars();
		if (var >= 3 && var < 6) {
			qs.setQuestVar(2);
			updateQuestStatus(env);
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(SystemMessageId.QUEST_FAILED_$1, DataManager.QUEST_DATA.getQuestById(questId).getName()));
		}

		return false;
	}

	@Override
	public boolean onLogOutEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (var >= 3 && var < 6) {
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(SystemMessageId.QUEST_FAILED_$1, DataManager.QUEST_DATA.getQuestById(questId).getName()));
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
		return defaultOnLvlUpEvent(env, 20031, true);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null) {
			return false;
		}
		int var = qs.getQuestVarById(0);
		int var1 = qs.getQuestVarById(1);
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc) {
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		Npc npc = (Npc) player.getTarget();
		if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 799295) { // Ortiz
				if (env.getDialog() == DialogAction.USE_OBJECT) {
					return sendQuestDialog(env, 10002);
				}
				else if (env.getDialogId() == DialogAction.SELECT_QUEST_REWARD.id()) {
					return sendQuestDialog(env, 5);
				}
				else {
					return sendQuestEndDialog(env);
				}
			}
			return false;
		}
		else if (qs.getStatus() != QuestStatus.START) {
			return false;
		}
		if (targetId == 799297) { // Fjoelnir
			switch (env.getDialog()) {
				case QUEST_SELECT:
					if (var == 0) {
						return sendQuestDialog(env, 1011);
					}
				case SETPRO1:
					giveQuestItem(env, 182215630, 1);
					return defaultCloseDialog(env, 0, 1); // 1
				default:
					break;

			}
		}
		else if (targetId == 799295) { // Ortiz
			switch (env.getDialog()) {
				case QUEST_SELECT:
					if (var == 1) {
						return sendQuestDialog(env, 1352);
					}
				case SETPRO2:
					removeQuestItem(env, 182215630, 1);
					giveQuestItem(env, 182215595, 1);
					return defaultCloseDialog(env, 1, 2); // 2
				default:
					break;
			}
		}
		else if (targetId == 730243) {
			switch (env.getDialog()) {
				case USE_OBJECT:
					if (var == 2) {
						return sendQuestDialog(env, 1693);
					}
				case SETPRO3:
					if (var == 2) {
						WorldMapInstance newInstance = InstanceService.getNextAvailableInstance(300150000);
						InstanceService.registerPlayerWithInstance(newInstance, player);
						TeleportService2.teleportTo(player, 300150000, newInstance.getInstanceId(), 561.8651f, 221.91483f, 134.53333f, (byte) 90);
						return true;
					}
				default:
					break;
			}
		}
		else if (targetId == 799513 || targetId == 799514 || targetId == 799515 || targetId == 799516) {
			switch (env.getDialog()) {
				case USE_OBJECT:
					if (var == 3 && var1 < 3) {
						qs.setQuestVarById(1, var1 + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						npc.getController().onDie(player);
						return true;
					}
					else if (var == 3 && var1 == 3) {
						playQuestMovie(env, 442);
						qs.setQuestVar(4);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						npc.getController().onDie(player);
						return true;
					}
				default:
					break;
			}
		}
		else if (targetId == 799341) {
			switch (env.getDialog()) {
				case USE_OBJECT:
					if (var == 5) {
						changeQuestStep(env, 5, 6, false);
						npc.getController().onDelete();
						return closeDialogWindow(env);
					}
				default:
					break;
			}
		}
		else if (targetId == 700706) {
			switch (env.getDialog()) {
				case USE_OBJECT:
					if (var == 6) {
						removeQuestItem(env, 182215595, 1);
						TeleportService2.teleportTo(env.getPlayer(), 220070000, 2499, 2139, 469, (byte) 10, TeleportAnimation.BEAM_ANIMATION);
					}
				default:
					break;
			}
		}
		return false;
	}

	@Override
	public boolean onKillEvent(QuestEnv env) {
		Player player = env.getPlayer();
		int instanceId = player.getInstanceId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() != QuestStatus.START) {
			return false;
		}
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc) {
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		int var = qs.getQuestVarById(0);
		if (targetId == 216592) {
			if (var == 4) {
				QuestService.addNewSpawn(300150000, instanceId, 799341, 561.8763f, 192.25128f, 135.88919f, (byte) 30);
				qs.setQuestVarById(0, var + 1);
				updateQuestStatus(env);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onMovieEndEvent(QuestEnv env, int movieId) {
		if (movieId != 442) {
			return false;
		}
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() != QuestStatus.START || qs.getQuestVars().getQuestVars() != 4) {
			return false;
		}
		QuestService.addNewSpawn(300150000, player.getInstanceId(), 216592, (float) 561.8763, (float) 192.25128, (float) 135.88919, (byte) 30);
		return true;
	}
}
