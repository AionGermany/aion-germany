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
package quest.nosra;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author Ghost_KNA
 */
public class _25500DefenceoftheRedDawnPlains extends QuestHandler {

	private final static int questId = 25500;

	public _25500DefenceoftheRedDawnPlains() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(806101).addOnQuestStart(questId); // Badorei
		qe.registerQuestNpc(806101).addOnTalkEvent(questId);
		qe.registerQuestNpc(240369).addOnKillEvent(questId);
		qe.registerQuestNpc(240370).addOnKillEvent(questId);
		qe.registerQuestNpc(241496).addOnKillEvent(questId);
		qe.registerQuestNpc(241497).addOnKillEvent(questId);
		qe.registerQuestNpc(240371).addOnKillEvent(questId);
		qe.registerQuestNpc(240372).addOnKillEvent(questId);
		qe.registerQuestNpc(241498).addOnKillEvent(questId);
		qe.registerQuestNpc(241499).addOnKillEvent(questId);
		qe.registerQuestNpc(240373).addOnKillEvent(questId);
		qe.registerQuestNpc(240374).addOnKillEvent(questId);
		qe.registerQuestNpc(241500).addOnKillEvent(questId);
		qe.registerQuestNpc(241501).addOnKillEvent(questId);
		qe.registerQuestNpc(240375).addOnKillEvent(questId);
		qe.registerQuestNpc(240376).addOnKillEvent(questId);
		qe.registerQuestNpc(241502).addOnKillEvent(questId);
		qe.registerQuestNpc(241503).addOnKillEvent(questId);
		qe.registerQuestNpc(240377).addOnKillEvent(questId);
		qe.registerQuestNpc(240378).addOnKillEvent(questId);
		qe.registerQuestNpc(241504).addOnKillEvent(questId);
		qe.registerQuestNpc(241505).addOnKillEvent(questId);
		qe.registerQuestNpc(241177).addOnKillEvent(questId);
		qe.registerQuestNpc(241178).addOnKillEvent(questId);
		qe.registerQuestNpc(243264).addOnKillEvent(questId);
		qe.registerQuestNpc(243265).addOnKillEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		int targetId = env.getTargetId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);

		if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
			if (targetId == 806101) { // Badorei
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 4762);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 806101) { // Badorei
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 10002);
				}
				else if (env.getDialog() == DialogAction.SELECT_QUEST_REWARD) {
					changeQuestStep(env, 0, 0, true);
					return sendQuestDialog(env, 5);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 806101) { // Badorei
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}

	@Override
	public boolean onKillEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() != QuestStatus.START)
			return false;

		int targetId = env.getTargetId();

		switch (targetId) {
			case 240369:
			case 240370:
			case 241496:
			case 241497:
			case 240371:
			case 240372:
			case 241498:
			case 241499:
			case 240373:
			case 240374:
			case 241500:
			case 241501:
			case 240375:
			case 240376:
			case 241502:
			case 241503:
			case 240377:
			case 240378:
			case 241504:
			case 241505:
			case 241177:
			case 241178:
			case 243264:
			case 243265:
				if (qs.getQuestVarById(1) != 39) {
					qs.setQuestVarById(1, qs.getQuestVarById(1) + 1);
					updateQuestStatus(env);
				}
				else {
					qs.setQuestVarById(0, 40);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
				}
		}
		return false;
	}
}
