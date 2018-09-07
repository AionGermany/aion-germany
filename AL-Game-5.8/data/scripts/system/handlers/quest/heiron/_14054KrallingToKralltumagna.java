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

import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author pralinka
 */
public class _14054KrallingToKralltumagna extends QuestHandler {

	private final static int questId = 14054;

	public _14054KrallingToKralltumagna() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerOnEnterZoneMissionEnd(questId);
		qe.registerOnLevelUp(questId);
		qe.registerQuestNpc(204602).addOnTalkEvent(questId);
		qe.registerQuestNpc(800413).addOnTalkEvent(questId);
		qe.registerQuestNpc(802050).addOnTalkEvent(questId);
		int[] mobs = { 214010, 214013, 214014, 214015, 214016, 214017, 214018, 214019, 214020, 214021, 214022, 214023, 214081, 214082, 214083, 214084, 214085, 214086, 214087, 214088, 700220, 233861 };
		for (int mob : mobs) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
	}

	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env) {
		return defaultOnZoneMissionEndEvent(env);
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env, 14050, true);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		int targetId = env.getTargetId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null) {
			return false;
		}
		if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 800413) {
				return sendQuestEndDialog(env);
			}
			return false;
		}
		else if (qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (targetId == 204602) {
				switch (env.getDialog()) {
					case QUEST_SELECT:
						if (var == 0) {
							return sendQuestDialog(env, 1011);
						}
					case SETPRO1:
						if (var == 0) {
							qs.setQuestVarById(0, var + 1);
							updateQuestStatus(env);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
							return true;
						}
					default:
						break;
				}
			}
			else if (targetId == 800413) {
				switch (env.getDialog()) {
					case QUEST_SELECT:
						if (var == 1) {
							return sendQuestDialog(env, 1352);
						}
					case SETPRO2:
						if (var == 1) {
							TeleportService2.teleportTo(player, 210040000, 1854f, 2651f, 149.6f, (byte) 118, TeleportAnimation.BEAM_ANIMATION);
							qs.setQuestVarById(0, var + 1);
							updateQuestStatus(env);
							return true;
						}
					default:
						break;
				}
			}
			else if (targetId == 802050) {
				switch (env.getDialog()) {
					case QUEST_SELECT:
						if (var == 2) {
							return sendQuestDialog(env, 2034);
						}
						else if (var == 4) {
							return sendQuestDialog(env, 2716);
						}
					case SETPRO4:
						if (var == 2) {
							qs.setQuestVarById(0, var + 1);
							updateQuestStatus(env);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
							return true;
						}
					case SETPRO6:
						TeleportService2.teleportTo(player, 210040000, 2448f, 348f, 415.6f, (byte) 118, TeleportAnimation.BEAM_ANIMATION);
						return defaultCloseDialog(env, 4, 4, true, false);
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
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc) {
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		int var = qs.getQuestVarById(0);
		if (targetId == 214010 || targetId == 214013 || targetId == 214014 || targetId == 214015 || targetId == 214016 || targetId == 214017 || targetId == 214018 || targetId == 214019 || targetId == 214020 || targetId == 214021 || targetId == 214022 || targetId == 214023 || targetId == 214081 || targetId == 214082 || targetId == 214083 || targetId == 214084 || targetId == 214085 || targetId == 214086 || targetId == 214087 || targetId == 214088) {
			switch (qs.getQuestVarById(1)) {
				case 0:
				case 1:
				case 2:
				case 3:
				case 4:
				case 5: {
					qs.setQuestVarById(1, qs.getQuestVarById(1) + 1);
					updateQuestStatus(env);
					return true;
				}
			}
		}
		else if (targetId == 700220) { // Baranath Crusher
			switch (qs.getQuestVarById(2)) {
				case 0:
				case 1:
				case 2: {
					qs.setQuestVarById(2, qs.getQuestVarById(2) + 1);
					updateQuestStatus(env);
					if (qs.getQuestVarById(1) == 6 && qs.getQuestVarById(2) == 3) {
						updateQuestStatus(env);
						return true;
					}
					return true;
				}
			}
		}
		else if (targetId == 233861) { // Officer Vitusa
			if (var == 3) {
				qs.setQuestVarById(0, var + 1);
				updateQuestStatus(env);
				return true;
			}
		}
		return false;
	}
}
