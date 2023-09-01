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
package quest.satra_treasure_hoard;

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
 * @author Ritsu
 */
class _18904AccordingToHisAbility extends QuestHandler {

	private final static int questId = 18904;
	private final static int[] npc_ids = { 219302, 219303, 219304, 219305, 219306, 219307, 219308, 219309, 219310 }; // STR_DIC_E_Q18904a

	public _18904AccordingToHisAbility() {
		super(questId);

	}

	@Override
	public void register() {
		qe.registerQuestNpc(800331).addOnQuestStart(questId);
		qe.registerQuestNpc(800331).addOnTalkEvent(questId); // Apsilon
		qe.registerQuestNpc(205844).addOnTalkEvent(questId); // Karuti
		for (int id : npc_ids) {
			qe.registerQuestNpc(id).addOnKillEvent(questId);
		}

	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int var = qs.getQuestVarById(0);
		int targetId = env.getTargetId();
		if (env.getVisibleObject() instanceof Npc) {
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 800331) {
				if (env.getDialog() == DialogAction.QUEST_SELECT)// 26
				{
					return sendQuestDialog(env, 1011); // select1
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 800331) {
				if (env.getDialog() == DialogAction.QUEST_SELECT)// 26
				{
					return sendQuestDialog(env, 1011);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
			else if (targetId == 205844) {
				if (env.getDialog() == DialogAction.QUEST_SELECT)// 26
				{
					return sendQuestDialog(env, 1352);// select2
				}
				else if (var == 9) {
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
					return true;
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 205844) {
				if (env.getDialog() == DialogAction.USE_OBJECT) // -1=ERROR
				{
					return sendQuestDialog(env, 1008); // quest_complete
				}
				else if (env.getDialogId() == DialogAction.SELECT_QUEST_REWARD.id()) // SET_SUCCEED
				{
					return sendQuestDialog(env, 5); // select_quest_reward1
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
		return defaultOnKillEvent(env, npc_ids, 0, 9);
	}
}
