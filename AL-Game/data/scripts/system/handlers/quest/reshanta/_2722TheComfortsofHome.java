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

public class _2722TheComfortsofHome extends QuestHandler {

	private final static int questId = 2722;

	public _2722TheComfortsofHome() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(278047).addOnQuestStart(questId);
		qe.registerQuestNpc(278056).addOnTalkEvent(questId);
		qe.registerQuestNpc(278126).addOnTalkEvent(questId);
		qe.registerQuestNpc(278043).addOnTalkEvent(questId);
		qe.registerQuestNpc(278032).addOnTalkEvent(questId);
		qe.registerQuestNpc(278037).addOnTalkEvent(questId);
		qe.registerQuestNpc(278040).addOnTalkEvent(questId);
		qe.registerQuestNpc(278068).addOnTalkEvent(questId);
		qe.registerQuestNpc(278066).addOnTalkEvent(questId);
		qe.registerQuestNpc(278047).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		int targetId = 0;
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (env.getVisibleObject() instanceof Npc) {
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 278047) {
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 4762);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (targetId == 278056) {
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 1011);
				}
				else if (env.getDialog() == DialogAction.SETPRO1) {
					qs.setQuestVarById(0, var + 1);
					updateQuestStatus(env);
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
					return true;
				}
			}
			else if (targetId == 278126) {
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 1352);
				}
				else if (env.getDialog() == DialogAction.SETPRO2) {
					qs.setQuestVarById(0, var + 1);
					updateQuestStatus(env);
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
					return true;
				}
			}
			else if (targetId == 278043) {
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 1693);
				}
				else if (env.getDialog() == DialogAction.SETPRO3) {
					qs.setQuestVarById(0, var + 1);
					updateQuestStatus(env);
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
					return true;
				}
			}
			else if (targetId == 278032) {
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 2034);
				}
				else if (env.getDialog() == DialogAction.SETPRO4) {
					qs.setQuestVarById(0, var + 1);
					updateQuestStatus(env);
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
					return true;
				}
			}
			else if (targetId == 278037) {
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 2375);
				}
				else if (env.getDialogId() == DialogAction.SETPRO5.id()) {
					qs.setQuestVarById(0, var + 1);
					updateQuestStatus(env);
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
					return true;
				}
			}
			else if (targetId == 278040) {
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 2716);
				}
				else if (env.getDialogId() == DialogAction.SETPRO6.id()) {
					qs.setQuestVarById(0, var + 1);
					updateQuestStatus(env);
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
					return true;
				}
			}
			else if (targetId == 278068) {
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 3057);
				}
				else if (env.getDialogId() == DialogAction.SETPRO7.id()) {
					qs.setQuestVarById(0, var + 1);
					updateQuestStatus(env);
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
					return true;
				}
			}
			else if (targetId == 278066) {
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 3398);
				}
				else if (env.getDialogId() == DialogAction.SET_SUCCEED.id()) {
					if (!giveQuestItem(env, 182205654, 1)) {
						return true;
					}
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
					return true;
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD && targetId == 278047) {
			return sendQuestEndDialog(env);
		}
		return false;
	}
}
