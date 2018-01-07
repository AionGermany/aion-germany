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
package quest.eltnen;

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
 * @author MrPoke remod By Xitanium
 */
public class _1471FakeStigma extends QuestHandler {

	private final static int questId = 1471;

	public _1471FakeStigma() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(203991).addOnQuestStart(questId); // Dionera
		qe.registerQuestNpc(203991).addOnTalkEvent(questId); // Dionera
		qe.registerQuestNpc(203703).addOnTalkEvent(questId); // Likesan
		qe.registerQuestNpc(798048).addOnTalkEvent(questId);// Kierunerk
		qe.registerQuestNpc(798024).addOnTalkEvent(questId); // Chicorinerk
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc) {
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (targetId == 203991) // Dionera
		{
			if (qs == null || qs.getStatus() == QuestStatus.NONE) {
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 1011);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}

		}
		else if (targetId == 203703) {
			if (qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 0) {
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 1352);
				}
				else if (env.getDialog() == DialogAction.SETPRO1) {
					qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
					updateQuestStatus(env);
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
					return true;
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
			else if (qs != null && qs.getStatus() == QuestStatus.START) {
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 2375);
				}
				else if (env.getDialogId() == DialogAction.SELECT_QUEST_REWARD.id()) {
					qs.setQuestVar(3);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					return sendQuestEndDialog(env);
				}
				else {
					return sendQuestEndDialog(env);
				}
			}
			else if (qs != null && qs.getStatus() == QuestStatus.REWARD) {
				return sendQuestEndDialog(env);
			}
		}
		else if (targetId == 798024) {
			if (qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 1) {
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 1693);
				}
				else if (env.getDialog() == DialogAction.SETPRO2) {
					qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
					updateQuestStatus(env);
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
					return true;
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (targetId == 798048) {
			if (qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 2) {
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 2034);
				}
				else if (env.getDialog() == DialogAction.SETPRO3) {
					qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
					updateQuestStatus(env);
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
					return true;
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}

		return false;
	}
}
