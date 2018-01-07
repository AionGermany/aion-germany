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
 * @author pralinka
 */
public class _14021ToCureACurse extends QuestHandler {

	private final static int questId = 14021;
	private final static int[] mob_ids = { 210771, 210758, 210763, 210764, 210759, 210770 };
	private final static int[] npc_ids = { 203902, 700179, 204043, 204030 };

	public _14021ToCureACurse() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerOnEnterZoneMissionEnd(questId);
		qe.registerOnLevelUp(questId);
		for (int npc : npc_ids) {
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
		for (int mob_id : mob_ids) {
			qe.registerQuestNpc(mob_id).addOnKillEvent(questId);
		}
	}

	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env) {
		return defaultOnZoneMissionEndEvent(env);
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env, 14020, true);
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
			return false;
		}
		int var = qs.getQuestVarById(0);

		if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 203902:
					switch (env.getDialog()) {
						case QUEST_SELECT:
							if (var == 0) {
								return sendQuestDialog(env, 1011);
							}
						case SETPRO1:
							return defaultCloseDialog(env, 0, 1); // 1
						default:
							break;
					}
					break;
				case 700179:
					if (var == 7) {
						switch (env.getDialog()) {
							case USE_OBJECT:
								return sendQuestDialog(env, 1693);
							case SETPRO3: {
								changeQuestStep(env, 7, 8, false);
								return sendQuestDialog(env, 0);
							}
							default:
								break;
						}
					}
					break;
				case 204043:
					switch (env.getDialog()) {
						case QUEST_SELECT:
							if (var == 8) {
								return sendQuestDialog(env, 2034);
							}
						case SETPRO4:
							changeQuestStep(env, 8, 9, false);
							return sendQuestDialog(env, 0);
						default:
							break;
					}
					break;
				case 204030:
					switch (env.getDialog()) {
						case QUEST_SELECT:
							if (var == 9) {
								return sendQuestDialog(env, 2375);
							}
						case SETPRO5:
							return defaultCloseDialog(env, 9, 9, true, false); // reward
						default:
							break;
					}
			}
		}
		if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 203902) {
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 3398);
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
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() != QuestStatus.START) {
			return false;
		}
		int var = qs.getQuestVarById(0);
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc) {
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		switch (targetId) {
			case 210771:
			case 210758:
			case 210763:
			case 210764:
			case 210759:
			case 210770:
				if (var >= 1 && var <= 6) {
					qs.setQuestVarById(0, var + 1);
					updateQuestStatus(env);
					return true;
				}
		}
		return false;
	}
}
