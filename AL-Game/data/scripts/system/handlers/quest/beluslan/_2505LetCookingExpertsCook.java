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
package quest.beluslan;

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
 * @author VladimirZ,Modifly by Newlives@aioncore 23-1-2015
 */
public class _2505LetCookingExpertsCook extends QuestHandler {

	private final static int questId = 2505;
	private final static int[] npc_ids = { 204720, 204731 };

	public _2505LetCookingExpertsCook() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(204720).addOnQuestStart(questId);
		for (int npc_id : npc_ids) {
			qe.registerQuestNpc(npc_id).addOnTalkEvent(questId);
		}
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc) {
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (targetId == 204720) { // AI
			if (qs == null || qs.getStatus() == QuestStatus.NONE) {
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 1011);
				}
				else if (env.getDialogId() == DialogAction.QUEST_ACCEPT_1.id()) {
					if (giveQuestItem(env, 182204404, 1)) {
						return sendQuestStartDialog(env);
					}
					else {
						return true;
					}
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
		if (var == 1) {
			if (targetId == 204720) { // AI
				if (env.getDialog() == DialogAction.USE_OBJECT) {
					return sendQuestDialog(env, 2375);
				}
				else if (env.getDialogId() == DialogAction.SELECT_QUEST_REWARD.id()) {
					qs.setStatus(QuestStatus.REWARD);
					return sendQuestDialog(env, 5);
				}
				else {
					return sendQuestEndDialog(env);
				}
			}
		}
		else if (qs.getStatus() != QuestStatus.START) {
			return false;
		}
		if (targetId == 204731) { // Aurvandil
			switch (env.getDialog()) {
				case QUEST_SELECT:
					if (var == 0) {
						return sendQuestDialog(env, 1352);
					}
				case SETPRO1:
					if (var == 0) {
						removeQuestItem(env, 182204404, 1);
						if (!giveQuestItem(env, 182204405, 1)) {
						}
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						changeQuestStep(env, 0, 1, false);
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
