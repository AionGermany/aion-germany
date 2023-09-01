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
package quest.daevation;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author Phantom_KNA
 */
public class _25322ThroughtheRift extends QuestHandler {

	public static final int questId = 25322;
	private final static int[] mobs = { 235801, 235802, 235803, 235804, 235805, 235806, 235807, 235808, 235809, 235810 };
	private final static int[] mobs2 = { 235851, 235852, 235853, 235854, 235855, 235856, 235857, 235858, 235859 };
	private final static int[] mobs3 = { 235876, 235877, 235878, 235879 };
	private final static int[] mobs4 = { 235889, 235890, 235891, 235892, 235893, 235894, 235895, 235896, 235897, 235898, 235899 };
	private final static int[] mobs5 = { 235938, 235939, 235940, 235941, 235942, 235943, 235944, 235945, 235946, 235947, 235948, 235949 };

	public _25322ThroughtheRift() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(805342).addOnQuestStart(questId);
		qe.registerQuestNpc(805342).addOnTalkEvent(questId); // Hikait
		qe.registerOnEnterZone(ZoneName.get("CRIMSON_HILLS_210070000"), questId);
		qe.registerOnEnterZone(ZoneName.get("DRAGON_LORDS_GARDENS_210070000"), questId);
		qe.registerOnEnterZone(ZoneName.get("TWILIGHT_TEMPLE_210070000"), questId);
		qe.registerOnEnterZone(ZoneName.get("PERENNIAL_MOSSWOOD_210070000"), questId);
		qe.registerOnEnterZone(ZoneName.get("WAILING_DUNES_210070000"), questId);
		for (int mob : mobs) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
		for (int mob2 : mobs2) {
			qe.registerQuestNpc(mob2).addOnKillEvent(questId);
		}
		for (int mob3 : mobs3) {
			qe.registerQuestNpc(mob3).addOnKillEvent(questId);
		}
		for (int mob4 : mobs4) {
			qe.registerQuestNpc(mob4).addOnKillEvent(questId);
		}
		for (int mob5 : mobs5) {
			qe.registerQuestNpc(mob5).addOnKillEvent(questId);
		}
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();
		DialogAction dialog = env.getDialog();

		if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
			if (targetId == 805342) { // Hikait
				switch (dialog) {
					case QUEST_SELECT: {
						return sendQuestDialog(env, 4762);
					}
					case QUEST_ACCEPT_1:
					case QUEST_ACCEPT_SIMPLE:
						QuestService.startQuest(env);
						return closeDialogWindow(env);
					case QUEST_REFUSE_1:
					case QUEST_REFUSE_SIMPLE:
						return closeDialogWindow(env);
					default:
						break;
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 805342) {
				switch (dialog) {
					case USE_OBJECT: {
						return sendQuestDialog(env, 10002);
					}
					case SELECT_QUEST_REWARD: {
						return sendQuestDialog(env, 5);
					}
					case SELECTED_QUEST_NOREWARD: {
						return sendQuestEndDialog(env);
					}
					default:
						break;
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
		int var1 = qs.getQuestVarById(1);
		if (var == 1 && var1 >= 0 && var1 < 9) {
			return defaultOnKillEvent(env, mobs, var1, var1 + 1, 1);
		}
		else if (var == 1 && var1 == 9) {
			qs.setQuestVarById(1, 0);
			changeQuestStep(env, 1, 2, false);
			updateQuestStatus(env);
			return true;
		}
		if (var == 3 && var1 >= 0 && var1 < 9) {
			return defaultOnKillEvent(env, mobs2, var1, var1 + 1, 1);
		}
		else if (var == 3 && var1 == 9) {
			qs.setQuestVarById(1, 0);
			changeQuestStep(env, 3, 4, false);
			updateQuestStatus(env);
			return true;
		}
		if (var == 5 && var1 >= 0 && var1 < 9) {
			return defaultOnKillEvent(env, mobs3, var1, var1 + 1, 1);
		}
		else if (var == 5 && var1 == 9) {
			qs.setQuestVarById(1, 0);
			changeQuestStep(env, 5, 6, false);
			updateQuestStatus(env);
			return true;
		}
		if (var == 7 && var1 >= 0 && var1 < 9) {
			return defaultOnKillEvent(env, mobs4, var1, var1 + 1, 1);
		}
		else if (var == 7 && var1 == 9) {
			qs.setQuestVarById(1, 0);
			changeQuestStep(env, 7, 8, false);
			updateQuestStatus(env);
			return true;
		}
		if (var == 9 && var1 >= 0 && var1 < 9) {
			return defaultOnKillEvent(env, mobs5, var1, var1 + 1, 1);
		}
		else if (var == 9 && var1 == 9) {
			qs.setQuestVarById(1, 0);
			qs.setQuestVar(10);
			qs.setStatus(QuestStatus.REWARD);
			updateQuestStatus(env);
			return true;
		}
		return false;
	}

	@Override
	public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName) {
		Player player = env.getPlayer();
		if (player == null) {
			return false;
		}

		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (var == 0 && zoneName == ZoneName.get("CRIMSON_HILLS_210070000")) {
				changeQuestStep(env, 0, 1, false);
				return true;
			}
			else if (var == 2 && zoneName == ZoneName.get("DRAGON_LORDS_GARDENS_210070000")) {
				changeQuestStep(env, 2, 3, false);
				return true;
			}
			else if (var == 4 && zoneName == ZoneName.get("TWILIGHT_TEMPLE_210070000")) {
				changeQuestStep(env, 4, 5, false);
				return true;
			}
			else if (var == 6 && zoneName == ZoneName.get("PERENNIAL_MOSSWOOD_210070000")) {
				changeQuestStep(env, 6, 7, false);
				return true;
			}
			else if (var == 8 && zoneName == ZoneName.get("WAILING_DUNES_210070000")) {
				changeQuestStep(env, 8, 9, false);
				return true;
			}
		}
		return false;
	}
}
