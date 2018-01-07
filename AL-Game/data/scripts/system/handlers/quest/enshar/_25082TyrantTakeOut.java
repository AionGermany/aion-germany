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
package quest.enshar;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;

/**
 * @author pralinka
 */
public class _25082TyrantTakeOut extends QuestHandler {

	private final static int questId = 25082;

	public _25082TyrantTakeOut() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(804923).addOnQuestStart(questId);
		qe.registerQuestNpc(804923).addOnTalkEvent(questId);
		qe.registerQuestNpc(731559).addOnTalkEvent(questId);
		qe.registerQuestNpc(804924).addOnTalkEvent(questId);
		qe.registerQuestNpc(220037).addOnKillEvent(questId);
	}

	@Override
	public boolean onKillEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() != QuestStatus.START) {
			return false;
		}
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc) {
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		switch (targetId) {
			case 220037:
				if (qs.getQuestVarById(0) == 2) {
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					return true;
				}
		}
		return false;
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();
		DialogAction dialog = env.getDialog();
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 804923) {
				if (dialog == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 4762);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (targetId == 731559) {
				if (dialog == DialogAction.QUEST_SELECT) {
					if (var == 0 && player.getInventory().getItemCountByItemId(182215728) == 1) {
						return sendQuestDialog(env, 1011);
					}
					else if (var == 1) {
						return sendQuestDialog(env, 1352);
					}
				}
				else if (dialog == DialogAction.CHECK_USER_HAS_QUEST_ITEM) {
					removeQuestItem(env, 182215728, 1);
					QuestService.addNewSpawn(220080000, player.getInstanceId(), 833465, 1056.4f, 194.3f, 227.1f, (byte) 90);
					changeQuestStep(env, 0, 1, false);
					return closeDialogWindow(env);
				}
				else if (dialog == DialogAction.SETPRO2) {
					QuestService.addNewSpawn(220080000, player.getInstanceId(), 220037, 1050.5f, 200.4f, 228.2f, (byte) 90);
					changeQuestStep(env, 1, 2, false);
					return closeDialogWindow(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 804924) {
				if (dialog == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 2034);
				}
				else {
					return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}
}
