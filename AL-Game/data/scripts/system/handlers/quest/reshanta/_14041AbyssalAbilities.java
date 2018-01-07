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
package quest.reshanta;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author pralinka
 */
public class _14041AbyssalAbilities extends QuestHandler {

	private final static int questId = 14041;
	private final static int[] npc_ids = { 278627, 278628, 278629, 278630, 278631, 278632, 278633, 278554 };

	public _14041AbyssalAbilities() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerOnEnterZoneMissionEnd(questId);
		qe.registerOnLevelUp(questId);
		for (int npc_id : npc_ids) {
			qe.registerQuestNpc(npc_id).addOnTalkEvent(questId);
		}
	}

	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env) {
		return defaultOnZoneMissionEndEvent(env);
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env, 1701, true);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null) {
			return false;
		}
		int var = qs.getQuestVarById(0);
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc) {
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 278554) {
				if (env.getDialog() == DialogAction.USE_OBJECT) {
					return sendQuestDialog(env, 10002);
				}
				else if (env.getDialogId() == 1009) {
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
		if (targetId == 278627) {
			switch (env.getDialog()) {
				case QUEST_SELECT:
					if (var == 0) {
						return sendQuestDialog(env, 1011);
					}
				case SELECT_ACTION_1013:
					playQuestMovie(env, 262);
					break;
				case SETPRO1:
					if (var == 0) {
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
					return false;
				default:
					break;
			}
		}
		else if (targetId == 278628) {
			switch (env.getDialog()) {
				case QUEST_SELECT:
					if (var == 1) {
						return sendQuestDialog(env, 1352);
					}
				case SELECT_ACTION_1353:
					playQuestMovie(env, 263);
					break;
				case SETPRO2:
					if (var == 1) {
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
					return false;
				default:
					break;
			}
		}
		else if (targetId == 278629) {
			switch (env.getDialog()) {
				case QUEST_SELECT:
					if (var == 2) {
						return sendQuestDialog(env, 1693);
					}
				case SELECT_ACTION_1694:
					playQuestMovie(env, 264);
					break;
				case SETPRO3:
					if (var == 2) {
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
					return false;
				default:
					break;
			}
		}
		else if (targetId == 278630) {
			switch (env.getDialog()) {
				case QUEST_SELECT:
					if (var == 3) {
						return sendQuestDialog(env, 2034);
					}
				case SELECT_ACTION_2035:
					playQuestMovie(env, 265);
					break;
				case SETPRO4:
					if (var == 3) {
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
					return false;
				default:
					break;
			}
		}
		else if (targetId == 278631) {
			switch (env.getDialog()) {
				case QUEST_SELECT:
					if (var == 4) {
						return sendQuestDialog(env, 2375);
					}
				case SELECT_ACTION_2376:
					playQuestMovie(env, 266);
					break;
				case SETPRO5:
					if (var == 4) {
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
					return false;
				default:
					break;
			}
		}
		else if (targetId == 278632) {
			switch (env.getDialog()) {
				case QUEST_SELECT:
					if (var == 5) {
						return sendQuestDialog(env, 2716);
					}
				case SELECT_ACTION_2717:
					playQuestMovie(env, 267);
					break;
				case SETPRO6:
					if (var == 5) {
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
					return false;
				default:
					break;
			}
		}
		else if (targetId == 278633) {
			switch (env.getDialog()) {
				case QUEST_SELECT:
					if (var == 6) {
						return sendQuestDialog(env, 3057);
					}
				case SELECT_ACTION_3058:
					playQuestMovie(env, 268);
					break;
				case SET_SUCCEED:
					if (var == 6) {
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
					return false;
				default:
					break;
			}
		}
		return false;
	}
}
