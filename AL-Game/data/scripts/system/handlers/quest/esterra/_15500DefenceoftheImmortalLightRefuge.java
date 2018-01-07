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
package quest.esterra;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author Phantom_KNA
 */
public class _15500DefenceoftheImmortalLightRefuge extends QuestHandler {

	private final static int questId = 15500;

	public _15500DefenceoftheImmortalLightRefuge() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(806089).addOnQuestStart(questId); // Aquaris
		qe.registerQuestNpc(806089).addOnTalkEvent(questId);
		qe.registerQuestNpc(241656).addOnKillEvent(questId);
		qe.registerQuestNpc(241657).addOnKillEvent(questId);
		qe.registerQuestNpc(241658).addOnKillEvent(questId);
		qe.registerQuestNpc(241659).addOnKillEvent(questId);
		qe.registerQuestNpc(241660).addOnKillEvent(questId);
		qe.registerQuestNpc(241661).addOnKillEvent(questId);
		qe.registerQuestNpc(241662).addOnKillEvent(questId);
		qe.registerQuestNpc(241663).addOnKillEvent(questId);
		qe.registerQuestNpc(241664).addOnKillEvent(questId);
		qe.registerQuestNpc(241665).addOnKillEvent(questId);
		qe.registerQuestNpc(240475).addOnKillEvent(questId);
		qe.registerQuestNpc(240476).addOnKillEvent(questId);
		qe.registerQuestNpc(240477).addOnKillEvent(questId);
		qe.registerQuestNpc(240478).addOnKillEvent(questId);
		qe.registerQuestNpc(240479).addOnKillEvent(questId);
		qe.registerQuestNpc(240480).addOnKillEvent(questId);
		qe.registerQuestNpc(240481).addOnKillEvent(questId);
		qe.registerQuestNpc(240482).addOnKillEvent(questId);
		qe.registerQuestNpc(240483).addOnKillEvent(questId);
		qe.registerQuestNpc(240484).addOnKillEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		int targetId = env.getTargetId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);

		if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
			if (targetId == 806089) { // Aquaris
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 4762);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 806089) { // Aquaris
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
			if (targetId == 806089) { // Aquaris
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
			case 241656:
			case 241657:
			case 241658:
			case 241659:
			case 241660:
			case 241661:
			case 241662:
			case 241663:
			case 241664:
			case 241665:
			case 240475:
			case 240476:
			case 240477:
			case 240478:
			case 240479:
			case 240480:
			case 240481:
			case 240482:
			case 240483:
			case 240484:
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
