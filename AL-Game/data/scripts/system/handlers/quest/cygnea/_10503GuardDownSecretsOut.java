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
package quest.cygnea;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author pralinka
 * @rework FrozenKiller
 */
public class _10503GuardDownSecretsOut extends QuestHandler {

	public static final int questId = 10503;

	public _10503GuardDownSecretsOut() {
		super(questId);
	}

	@Override
	public void register() {
		int[] npcs = { 804704, 804705, 702670 };
		qe.registerQuestNpc(236252).addOnKillEvent(questId);
		qe.registerOnLevelUp(questId);
		for (int npc : npcs) {
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
		qe.registerQuestItem(182215604, questId);
		qe.registerOnEnterZone(ZoneName.get("LF5_SensoryArea_Q10503"), questId);
		qe.registerOnMovieEndQuest(991, questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null) {
			return false;
		}
		int var = qs.getQuestVarById(0);
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc) {
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 804704) {
				switch (env.getDialog()) {
					case QUEST_SELECT:
						if (var == 0) {
							return sendQuestDialog(env, 1011);
						}
					case SETPRO1:
						changeQuestStep(env, 0, 1, false);
						return closeDialogWindow(env);
					default:
						break;
				}
			}
			if (targetId == 804705) {
				switch (env.getDialog()) {
					case QUEST_SELECT:
						if (var == 2) {
							return sendQuestDialog(env, 1693);
						}
						else if (var == 5) {
							return sendQuestDialog(env, 2716);
						}

					case CHECK_USER_HAS_QUEST_ITEM:
						if (QuestService.collectItemCheck(env, true)) {
							giveQuestItem(env, 182215604, 1);
							changeQuestStep(env, 2, 3, false);
							return sendQuestDialog(env, 10000);
						}
						else {
							return sendQuestDialog(env, 10001);
						}
					case SETPRO4:
						changeQuestStep(env, 3, 4, false);
						return closeDialogWindow(env);
					case SETPRO6:
						changeQuestStep(env, 5, 6, false);
						return closeDialogWindow(env);
					default:
						break;

				}
			}
			if (targetId == 702670) {
				switch (env.getDialog()) {
					case USE_OBJECT:
						return true;
					default:
						break;
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 804704) {
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
	public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (var == 6) {
				playQuestMovie(env, 991);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onMovieEndEvent(QuestEnv env, int movieId) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (movieId == 991) {
			qs.setQuestVar(7);
			qs.setStatus(QuestStatus.REWARD);
			updateQuestStatus(env);
			return true;
		}
		return false;
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env, 10502, true);
	}

	@Override
	public HandlerResult onItemUseEvent(final QuestEnv env, Item item) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {

			int var = qs.getQuestVarById(0);
			if (var == 4) {
				return HandlerResult.fromBoolean(useQuestItem(env, item, 4, 5, false));
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
			if (var == 1) {
				int var1 = qs.getQuestVarById(1);
				if (var1 >= 0 && var1 < 2) {
					return defaultOnKillEvent(env, 236252, var1, var1 + 1, 1);
				}
				else if (var1 == 2) {
					qs.setQuestVar(2);
					updateQuestStatus(env);
					return true;
				}
			}
		}
		return false;
	}
}
