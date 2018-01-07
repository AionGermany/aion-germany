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
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author pralinka
 * @rework FrozenKiller
 */
public class _14014TurningTheIde extends QuestHandler {

	private final static int questId = 14014;
	private final static int[] npcs = { 203146, 203147, 802045, 203098 }; // Estino, Meteina, Livanon, Spatalos
	private final static int[] mobs = { 210178, 216892 }; // Veteran Tursin Smackstopper, Tursin Senior Command Soldier
	private final static int[] items = { 182215314 }; // <-- New Transformation Potion

	public _14014TurningTheIde() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerOnEnterZoneMissionEnd(questId);
		qe.registerOnLevelUp(questId);
		for (int npc : npcs) {
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
		for (int item : items) {
			qe.registerQuestItem(item, questId);
		}
		for (int mob : mobs) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
	}

	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env) {
		return defaultOnZoneMissionEndEvent(env);
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env, 14010, true);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null) {
			return false;
		}
		DialogAction dialog = env.getDialog();
		int targetId = env.getTargetId();

		if (qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			switch (targetId) {
				case 203146: { // Estino
					switch (dialog) {
						case QUEST_SELECT: {
							if (var == 0) {
								return sendQuestDialog(env, 1011);
							}
						}
						case SETPRO1: {
							giveQuestItem(env, 182215314, 1);
							return defaultCloseDialog(env, 0, 1); // 1
						}
						default:
							break;
					}
					break;
				}
				case 203147: { // Meteina
					switch (dialog) {
						case USE_OBJECT: {
							if (var == 2) {
								return sendQuestDialog(env, 1693);
							}
						}
						case SETPRO3: {
							return defaultCloseDialog(env, 2, 3); // 3
						}
						default:
							break;
					}
					break;
				}
				case 802045: { // Livanon
					switch (dialog) {
						case QUEST_SELECT: {
							if (var == 3) {
								return sendQuestDialog(env, 2034);
							}
						}
						case CHECK_USER_HAS_QUEST_ITEM: {
							checkQuestItems(env, 3, 5, false, 10000, 10001); // 5
							return closeDialogWindow(env);
						}
						case FINISH_DIALOG: {
							return closeDialogWindow(env);
						}
						default:
							break;
					}
					break;
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 203098) { // Spatalos
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 10002);
				}
				else {
					return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}

	@Override
	public HandlerResult onItemUseEvent(QuestEnv env, final Item item) {
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() != QuestStatus.START) {
			return HandlerResult.UNKNOWN;
		}
		int var = qs.getQuestVarById(0);
		final int id = item.getItemTemplate().getTemplateId();
		if (id == 182215314) // Transformation potion
		{
			if (var == 1 && player.isInsideZone(ZoneName.get("LF1A_USEITEMAREA_Q80"))) {
				return HandlerResult.fromBoolean(useQuestItem(env, item, 1, 2, false, 18)); // 2
			}
		}
		return HandlerResult.FAILED;
	}

	@Override
	public boolean onKillEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (var >= 5 && var < 7) {
				return defaultOnKillEvent(env, mobs, 5, 7);
			}
			else if (var == 7) {
				qs.setStatus(QuestStatus.REWARD);
				updateQuestStatus(env);
				return true;
			}
		}
		return false;
	}
}
