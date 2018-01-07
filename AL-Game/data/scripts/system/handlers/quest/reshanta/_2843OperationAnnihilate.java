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
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author Hilgert
 */
public class _2843OperationAnnihilate extends QuestHandler {

	private final static int questId = 2843;

	public _2843OperationAnnihilate() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(268081).addOnQuestStart(questId);
		qe.registerQuestNpc(268081).addOnTalkEvent(questId);
		qe.registerQuestNpc(215134).addOnKillEvent(questId);
		qe.registerQuestNpc(215136).addOnKillEvent(questId);
		qe.registerQuestNpc(215122).addOnKillEvent(questId);
		qe.registerQuestNpc(215124).addOnKillEvent(questId);
		qe.registerQuestNpc(215116).addOnKillEvent(questId);
		qe.registerQuestNpc(215315).addOnKillEvent(questId);
		qe.registerQuestNpc(215299).addOnKillEvent(questId);
		qe.registerQuestNpc(215112).addOnKillEvent(questId);
		qe.registerQuestNpc(215120).addOnKillEvent(questId);
		qe.registerQuestNpc(215125).addOnKillEvent(questId);
		qe.registerQuestNpc(215118).addOnKillEvent(questId);
		qe.registerQuestNpc(215119).addOnKillEvent(questId);
		qe.registerQuestNpc(215312).addOnKillEvent(questId);
		qe.registerQuestNpc(215109).addOnKillEvent(questId);
		qe.registerQuestNpc(215097).addOnKillEvent(questId);
		qe.registerQuestNpc(215304).addOnKillEvent(questId);
		qe.registerQuestNpc(215308).addOnKillEvent(questId);
		qe.registerQuestNpc(215292).addOnKillEvent(questId);
		qe.registerQuestNpc(215101).addOnKillEvent(questId);
		qe.registerQuestNpc(215126).addOnKillEvent(questId);
		qe.registerQuestNpc(215098).addOnKillEvent(questId);
		qe.registerQuestNpc(215305).addOnKillEvent(questId);
		qe.registerQuestNpc(215289).addOnKillEvent(questId);
		qe.registerQuestNpc(215300).addOnKillEvent(questId);
		qe.registerQuestNpc(215113).addOnKillEvent(questId);
		qe.registerQuestNpc(215316).addOnKillEvent(questId);
		qe.registerQuestNpc(215094).addOnKillEvent(questId);
		qe.registerQuestNpc(215301).addOnKillEvent(questId);
		qe.registerQuestNpc(215313).addOnKillEvent(questId);
		qe.registerQuestNpc(215110).addOnKillEvent(questId);
		qe.registerQuestNpc(215297).addOnKillEvent(questId);
		qe.registerQuestNpc(215314).addOnKillEvent(questId);
		qe.registerQuestNpc(215111).addOnKillEvent(questId);
		qe.registerQuestNpc(215298).addOnKillEvent(questId);
		qe.registerQuestNpc(215104).addOnKillEvent(questId);
		qe.registerQuestNpc(215096).addOnKillEvent(questId);
		qe.registerQuestNpc(215303).addOnKillEvent(questId);
		qe.registerQuestNpc(215287).addOnKillEvent(questId);
		qe.registerQuestNpc(215128).addOnKillEvent(questId);
		qe.registerQuestNpc(215121).addOnKillEvent(questId);
		qe.registerQuestNpc(215106).addOnKillEvent(questId);
		qe.registerQuestNpc(215293).addOnKillEvent(questId);
		qe.registerQuestNpc(215107).addOnKillEvent(questId);
		qe.registerQuestNpc(215100).addOnKillEvent(questId);
		qe.registerQuestNpc(215291).addOnKillEvent(questId);
		qe.registerQuestNpc(215307).addOnKillEvent(questId);
		qe.registerQuestNpc(215290).addOnKillEvent(questId);
		qe.registerQuestNpc(215099).addOnKillEvent(questId);
		qe.registerQuestNpc(215306).addOnKillEvent(questId);
		qe.registerQuestNpc(215123).addOnKillEvent(questId);
		qe.registerQuestNpc(215108).addOnKillEvent(questId);
		qe.registerQuestNpc(215295).addOnKillEvent(questId);
		qe.registerQuestNpc(215311).addOnKillEvent(questId);
		qe.registerQuestNpc(215095).addOnKillEvent(questId);
		qe.registerQuestNpc(215302).addOnKillEvent(questId);
		qe.registerQuestNpc(215133).addOnKillEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();

		QuestState qs = player.getQuestStateList().getQuestState(questId);

		if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.getStatus() == QuestStatus.COMPLETE) {
			if (env.getTargetId() == 268081) {
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 1011);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			if (env.getTargetId() == 268081) {
				return true;
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD && env.getTargetId() == 268081) {
			qs.setQuestVarById(0, 0);
			updateQuestStatus(env);
			return sendQuestEndDialog(env);
		}
		return false;
	}

	@Override
	public boolean onKillEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() != QuestStatus.START) {
			return false;
		}

		if (qs.getStatus() == QuestStatus.START) {
			if (player.getCommonData().getPosition().getMapId() == 300140000) {
				if (qs.getQuestVarById(0) < 79) {
					qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
					updateQuestStatus(env);
					return true;
				}
				else if (qs.getQuestVarById(0) == 79 || qs.getQuestVarById(0) > 79) {
					qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					return true;
				}
			}
		}
		return false;
	}
}
