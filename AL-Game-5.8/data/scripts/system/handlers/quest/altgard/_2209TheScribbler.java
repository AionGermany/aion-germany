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
package quest.altgard;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author Mr. Poke
 */
public class _2209TheScribbler extends QuestHandler {

	private final static int questId = 2209;

	public _2209TheScribbler() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(203555).addOnQuestStart(questId);
		qe.registerQuestNpc(203555).addOnTalkEvent(questId);
		qe.registerQuestNpc(203562).addOnTalkEvent(questId);
		qe.registerQuestNpc(203592).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc) {
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null) {
			if (targetId == 203555) {
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 1011);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 203562: {
					if (qs.getQuestVarById(0) == 0) {
						if (env.getDialog() == DialogAction.QUEST_SELECT) {
							return sendQuestDialog(env, 1352);
						}
						else if (env.getDialog() == DialogAction.SETPRO1) {
							return defaultCloseDialog(env, 0, 1); // 1
						}
					}
				}
					break;
				case 203572: {
					if (qs.getQuestVarById(0) == 1) {
						if (env.getDialog() == DialogAction.QUEST_SELECT) {
							return sendQuestDialog(env, 1693);
						}
						else if (env.getDialog() == DialogAction.SETPRO2) {
							return defaultCloseDialog(env, 1, 2); // 2
						}
					}
				}
					break;
				case 203592: {
					if (qs.getQuestVarById(0) == 2) {
						if (env.getDialog() == DialogAction.QUEST_SELECT) {
							return sendQuestDialog(env, 2034);
						}
						else if (env.getDialog() == DialogAction.SETPRO3) {
							return defaultCloseDialog(env, 2, 3); // 3
						}
					}
				}
					break;
				case 203555: {
					if (qs.getQuestVarById(0) == 3) {
						if (env.getDialog() == DialogAction.QUEST_SELECT) {
							return sendQuestDialog(env, 2375);
						}
						else if (env.getDialogId() == DialogAction.SELECT_QUEST_REWARD.id()) {
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
							return sendQuestEndDialog(env);
						}
						else {
							return sendQuestEndDialog(env);
						}
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 203555) {
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}
