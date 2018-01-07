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
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author pralinka
 */
public class _24054CrisisInBeluslan extends QuestHandler {

	private final static int questId = 24054;
	private final static int[] npcs = { 204702, 802053, 204701 };

	public _24054CrisisInBeluslan() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerOnEnterZoneMissionEnd(questId);
		qe.registerOnLevelUp(questId);
		for (int npc : npcs) {
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
		qe.registerQuestNpc(702041).addOnKillEvent(questId);
		qe.registerQuestNpc(233865).addOnKillEvent(questId);
	}

	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env) {
		return defaultOnZoneMissionEndEvent(env);
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env, 24040, true);
	}

	@Override
	public boolean onKillEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int targetId = env.getTargetId();
			switch (targetId) {
				case 702041: {
					return defaultOnKillEvent(env, 702041, 2, 5); // 5
				}
				case 233865: {
					return defaultOnKillEvent(env, 233865, 5, 6); // 6
				}
			}
		}
		return false;
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(env.getQuestId());
		if (qs == null) {
			return false;
		}
		Npc target = (Npc) env.getVisibleObject();
		int targetId = target.getNpcId();
		int var = qs.getQuestVarById(0);
		DialogAction dialog = env.getDialog();
		if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 204702) { // nerita
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 10002);
				}
				else {
					return sendQuestEndDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 204702: { // nerita
					if (dialog == DialogAction.QUEST_SELECT && var == 0) {
						return sendQuestDialog(env, 1011);
					} /*
						 * else if (dialog == DialogAction.QUEST_SELECT && var == 6) { return sendQuestDialog(env, 2375); }
						 */

					if (dialog == DialogAction.SETPRO1) {
						playQuestMovie(env, 258);
						return defaultCloseDialog(env, 0, 1);
					}
					// if (dialog == DialogAction.SET_SUCCEED) {
					// return defaultCloseDialog(env, 6, 6, true, false);
					// }
					break;
				}
				case 802053: { // fafner
					if (dialog == DialogAction.QUEST_SELECT && var == 1) {
						return sendQuestDialog(env, 1352);
					}
					if (dialog == DialogAction.SETPRO2) {
						return defaultCloseDialog(env, 1, 2);
					}
					break;
				}
				case 204701: { // hod
					if (dialog == DialogAction.USE_OBJECT && var == 6) {
						return sendQuestDialog(env, 2375);
					}
					if (dialog == DialogAction.SET_SUCCEED) {
						return defaultCloseDialog(env, 6, 6, true, false);
					}
					break;
				}
			}
		}
		return false;
	}
}
