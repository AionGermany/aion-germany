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
 * @author Xitanium
 */
public class _1319PrioritesMoney extends QuestHandler // NEED FIX ITEM
{

	private final static int questId = 1319;

	public _1319PrioritesMoney() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(203908).addOnQuestStart(questId); // Priorite
		qe.registerQuestNpc(203908).addOnTalkEvent(questId); // Priorite
		qe.registerQuestNpc(203923).addOnTalkEvent(questId); // Krato
		qe.registerQuestNpc(203910).addOnTalkEvent(questId); // Hebestis
		qe.registerQuestNpc(203906).addOnTalkEvent(questId); // Benos
		qe.registerQuestNpc(203915).addOnTalkEvent(questId); // Diokles
		qe.registerQuestNpc(203907).addOnTalkEvent(questId); // Tuskeos
		qe.registerQuestNpc(798050).addOnTalkEvent(questId); // Girrinerk
		qe.registerQuestNpc(798049).addOnTalkEvent(questId); // Shaoranyerk
		qe.registerQuestNpc(205240).addOnTalkEvent(questId); // Arnesonerk
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc) {
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 203908) // Priorite
			{
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 1011);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs != null && qs.getStatus() == QuestStatus.REWARD) // Reward
		{
			if (env.getDialog() == DialogAction.QUEST_SELECT) {
				return sendQuestDialog(env, 4080);
			}
			else if (env.getDialogId() == DialogAction.SELECT_QUEST_REWARD.id()) {
				qs.setQuestVar(8);
				qs.setStatus(QuestStatus.REWARD);
				updateQuestStatus(env);
				return sendQuestEndDialog(env);
			}
			else {
				return sendQuestEndDialog(env);
			}
		}
		else if (targetId == 203923) // Krato
		{

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

		}
		else if (targetId == 203910) // Hebestis
		{
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
		else if (targetId == 203906) // Benos
		{
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
		else if (targetId == 203915) // Diokles
		{
			if (qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 3) {
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 2375);
				}
				else if (env.getDialog() == DialogAction.SETPRO4) {
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
		else if (targetId == 203907) // Tuskeos
		{
			if (qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 4) {
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 2716);
				}
				else if (env.getDialogId() == DialogAction.SETPRO5.id()) {
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
		else if (targetId == 798050) // Girrinerk
		{
			if (qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 5) {
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 3057);
				}
				else if (env.getDialogId() == DialogAction.SETPRO6.id()) {
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
		else if (targetId == 798049) // Shaoranranerk
		{
			if (qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 6) {
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 3398);
				}
				else if (env.getDialogId() == DialogAction.SETPRO7.id()) {
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
		else if (targetId == 205240) // Arnesonerk
		{
			if (qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 7) {
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 3739);
				}
				else if (env.getDialogId() == DialogAction.SETPRO8.id() && qs.getStatus() != QuestStatus.COMPLETE && qs.getStatus() != QuestStatus.NONE) {
					qs.setStatus(QuestStatus.REWARD);
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
