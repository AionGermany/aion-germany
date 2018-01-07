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
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author Xitanium
 */
public class _1414OperationWindmill extends QuestHandler {

	private final static int questId = 1414;

	public _1414OperationWindmill() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(203989).addOnQuestStart(questId); // Tumblusen
		qe.registerQuestNpc(203989).addOnTalkEvent(questId);
		qe.registerQuestNpc(700175).addOnTalkEvent(questId); // Old Gear
	}

	@Override
	public boolean onDialogEvent(final QuestEnv env) {
		final Player player = env.getPlayer();
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc) {
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 203989) // Tumblusen
			{
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 1011);
				}
				else if (env.getDialogId() == DialogAction.QUEST_ACCEPT_1.id()) {
					if (giveQuestItem(env, 182201349, 1)) {
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
		else if (qs != null && qs.getStatus() == QuestStatus.REWARD) {
			return sendQuestEndDialog(env);
		}
		else if (qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 0) {
			switch (targetId) {
				case 700175: { // Old Gear
					if (qs.getQuestVarById(0) == 0 && env.getDialog() == DialogAction.USE_OBJECT) {
						qs.setQuestVar(1);
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						removeQuestItem(env, 182201349, 1);
						return true;
					}
				}
			}
		}
		return false;
	}
}
