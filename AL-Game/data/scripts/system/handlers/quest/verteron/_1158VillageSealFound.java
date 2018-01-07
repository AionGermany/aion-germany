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
package quest.verteron;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author Rhys2002
 * @rework zhkchi
 */
public class _1158VillageSealFound extends QuestHandler {

	private final static int questId = 1158;

	public _1158VillageSealFound() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(798003).addOnQuestStart(questId);
		qe.registerQuestNpc(798003).addOnTalkEvent(questId);
		qe.registerQuestNpc(700003).addOnTalkEvent(questId);
		qe.registerQuestNpc(203128).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(final QuestEnv env) {
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();

		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc) {
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 798003) {
				switch (dialog) {
					case QUEST_SELECT:
						return sendQuestDialog(env, 1011);
					default:
						return sendQuestStartDialog(env);
				}
			}
		}
		if (qs == null) {
			return false;
		}

		int var = qs.getQuestVarById(0);
		if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 700003 && var == 0) {
				switch (dialog) {
					case USE_OBJECT:
						return sendQuestDialog(env, 1352);
					case SELECT_ACTION_1353:
						return sendQuestDialog(env, 1353);
					case SETPRO1: {
						if (!giveQuestItem(env, 182200502, 1)) {
							return true;
						}
						qs.setQuestVarById(0, 1);
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						return closeDialogWindow(env);
					}
					default:
						break;
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 203128) {
				switch (dialog) {
					case USE_OBJECT:
						return sendQuestDialog(env, 2375);
					case SELECT_QUEST_REWARD:
						removeQuestItem(env, 182200502, 1);
						return sendQuestDialog(env, 5);
					default:
						return sendQuestEndDialog(env);
				}
			}
			return false;
		}
		return false;
	}
}
