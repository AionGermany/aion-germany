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
package quest.sanctum;

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
 * @author Rolandas
 */
public class _3966SaluteANewUniform extends QuestHandler {

	private final static int questId = 3966;

	public _3966SaluteANewUniform() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(798391).addOnQuestStart(questId);
		qe.registerQuestNpc(203994).addOnTalkEvent(questId);
		qe.registerQuestNpc(204030).addOnTalkEvent(questId);
		qe.registerQuestNpc(204568).addOnTalkEvent(questId);
		qe.registerQuestNpc(798391).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc) {
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (targetId == 798391) {
			if (qs == null || qs.getStatus() == QuestStatus.NONE) {
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 1011);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}

		if (qs == null) {
			return false;
		}

		int var = qs.getQuestVarById(0);

		if (targetId == 203994) {
			if (qs.getStatus() == QuestStatus.START && var == 0) {
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 1352);
				}
				else if (env.getDialog() == DialogAction.SETPRO1) {
					qs.setQuestVar(++var);
					updateQuestStatus(env);
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
					return true;
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (targetId == 204030) {
			if (qs.getStatus() == QuestStatus.START && var == 1) {
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 1693);
				}
				else if (env.getDialog() == DialogAction.SETPRO2) {
					qs.setQuestVar(++var);
					updateQuestStatus(env);
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
					return true;
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (targetId == 204568) {
			if (qs.getStatus() == QuestStatus.START && var == 2) {
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 2034);
				}
				else if (env.getDialog() == DialogAction.SETPRO3) {
					qs.setQuestVar(++var);
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
		else if (targetId == 798391) {
			if (env.getDialog() == DialogAction.USE_OBJECT && qs.getStatus() == QuestStatus.REWARD) {
				return sendQuestDialog(env, 2375);
			}
			return sendQuestEndDialog(env);
		}
		return false;
	}
}
