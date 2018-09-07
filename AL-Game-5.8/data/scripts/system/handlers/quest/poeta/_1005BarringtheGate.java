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
package quest.poeta;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/**
 * @author MrPoke
 * @reworked apozema
 * @rework FrozenKiller
 */
public class _1005BarringtheGate extends QuestHandler {

	private final static int questId = 1005;

	public _1005BarringtheGate() {
		super(questId);
	}

	@Override
	public void register() {
		int[] talkNpcs = { 203067, 203081, 790001, 203085, 203086, 700080, 700081, 700082, 700083 };
		qe.registerOnEnterZoneMissionEnd(questId);
		qe.registerOnLevelUp(questId);
		for (int id : talkNpcs) {
			qe.registerQuestNpc(id).addOnTalkEvent(questId);
		}
	}

	@Override
	public boolean onDialogEvent(final QuestEnv env) {
		final Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();
		if (qs == null) {
			return false;
		}

		int var = qs.getQuestVarById(0);
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc) {
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}

		if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 203067: // Kalio
					switch (dialog) {
						case QUEST_SELECT:
							if (var == 0) {
								return sendQuestDialog(env, 1011);
							}
						case SETPRO1:
							if (var == 0) {
								return defaultCloseDialog(env, 0, 1);
							}
						default:
							break;
					}
					break;
				case 203081: // Oz
					switch (dialog) {
						case QUEST_SELECT:
							if (var == 1) {
								return sendQuestDialog(env, 1352);
							}
						case SETPRO2:
							if (var == 1) {
								return defaultCloseDialog(env, 1, 2);
							}
						default:
							break;
					}
					break;
				case 790001: // Pernos
					switch (dialog) {
						case QUEST_SELECT:
							if (var == 2) {
								return sendQuestDialog(env, 1693);
							}
						case SETPRO3:
							if (var == 2) {
								return defaultCloseDialog(env, 2, 3);
							}
						default:
							break;
					}
					break;
				case 203085: // Poa
					switch (dialog) {
						case QUEST_SELECT:
							if (var == 3) {
								return sendQuestDialog(env, 2034);
							}
						case SETPRO4:
							if (var == 3) {
								return defaultCloseDialog(env, 3, 4);
							}
						default:
							break;
					}
					break;
				case 203086: // Ino
					switch (dialog) {
						case QUEST_SELECT:
							if (var == 4) {
								return sendQuestDialog(env, 2375);
							}
						case SETPRO5:
							if (var == 4) {
								return defaultCloseDialog(env, 4, 5);
							}
						default:
							break;
					}
					break;
				case 700081: // Green Power Generator
					if (var == 5) {
						destroy(6, env);
						return false;
					}
					break;
				case 700082: // Blue Power Generator
					if (var == 6) {
						destroy(7, env);
						return false;
					}
					break;
				case 700083: // Violet Power Generator
					if (var == 7) {
						destroy(8, env);
						return false;
					}
					break;
				case 700080: // Poeta Abyss Gate
					if (var == 8) {
						destroy(-1, env);
						return false;
					}
					break;
			}

		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 203067) { // Kalio
				switch (dialog) {
					case USE_OBJECT:
						return sendQuestDialog(env, 2716);
					case SELECT_QUEST_REWARD:
						playQuestMovie(env, 171);
						return sendQuestDialog(env, 5);
					default:
						break;
				}
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}

	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env) {
		int[] quests = { 1100, 1001, 1002, 1003, 1004 };
		return defaultOnZoneMissionEndEvent(env, quests);
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		int[] quests = { 1100, 1001, 1002, 1003, 1004 };
		return defaultOnLvlUpEvent(env, quests, true);
	}

	private void destroy(final int var, final QuestEnv env) { // TODO: add Emotion for destroying generators
		final int targetObjectId = env.getVisibleObject().getObjectId();

		final Player player = env.getPlayer();
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (player.getTarget().getObjectId() != targetObjectId) {
					return;
				}
				QuestState qs = player.getQuestStateList().getQuestState(questId);
				switch (var) {
					case 6:
					case 7:
					case 8:
						qs.setQuestVar(var);
						break;
					case -1:
						playQuestMovie(env, 21);
						qs.setStatus(QuestStatus.REWARD);
						break;
				}
				updateQuestStatus(env);
			}
		}, 100);
	}
}
