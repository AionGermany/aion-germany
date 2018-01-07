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

import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.services.teleport.TeleportService2;

/**
 * @author pralinka
 * @rework FrozenKiller
 */
public class _14025CookingUpDisasters extends QuestHandler {

	private final static int questId = 14025;
	private final static int[] npcs = { 203989, 204020, 203901 };
	private final static int[] mobs = { 211017, 232133, 217090, };

	public _14025CookingUpDisasters() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerOnEnterZoneMissionEnd(questId);
		qe.registerOnLevelUp(questId);
		for (int npc : npcs) {
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
		for (int mob : mobs) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
	}

	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env) {
		return defaultOnZoneMissionEndEvent(env, 14024);
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		int[] quests = { 14020, 14024 };
		return defaultOnLvlUpEvent(env, quests, true);
	}

	@Override
	public boolean onKillEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (var == 5) {
				int[] kaidan = { 211017, 232133 };
				int[] kalabar = { 217090 };
				switch (targetId) {
					case 211017:
					case 232133: {
						return defaultOnKillEvent(env, kaidan, 0, 4, 1); // 1: 4x
					}
					case 217090: {
						return defaultOnKillEvent(env, kalabar, 0, 1, 2); // 2: 1x
					}
				}
			}
		}
		return false;
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null) {
			return false;
		}

		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc) {
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 203901) // Telemachus
			{
				return sendQuestEndDialog(env);
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			int var1 = qs.getQuestVarById(1);
			int var2 = qs.getQuestVarById(2);
			if (targetId == 203989) { // Tumblusen
				switch (env.getDialog()) {
					case QUEST_SELECT:
						if (var == 0) {
							return sendQuestDialog(env, 1011);
						}
						else if (var == 1) {
							return sendQuestDialog(env, 1352);
						}
						else if (var == 2) {
							return sendQuestDialog(env, 1438);
						}
						else if (var == 4) {
							return sendQuestDialog(env, 2034);
						}
						else if (var == 5 && var1 == 4 && var2 == 1) {
							return sendQuestDialog(env, 2716);
						}
					case CHECK_USER_HAS_QUEST_ITEM:
						if (var == 1) {
							if (QuestService.collectItemCheck(env, true)) {
								qs.setQuestVar(2);
								updateQuestStatus(env);
								return sendQuestDialog(env, 1438);
							}
							else {
								return sendQuestDialog(env, 1353);
							}
						}
					case SETPRO1:
						if (var == 0) {
							return defaultCloseDialog(env, 0, 1);
						}
					case SETPRO2:
						if (var == 2) {
							qs.setQuestVar(3);
							updateQuestStatus(env);
							TeleportService2.teleportTo(player, 210020000, 1596, 1529, 317, (byte) 120, TeleportAnimation.BEAM_ANIMATION);
							return closeDialogWindow(env);
						}
					case SETPRO4:
						// playQuestMovie(env, 36); REALY NEEDED?
						removeQuestItem(env, 182201005, 1);
						return defaultCloseDialog(env, 4, 5);
					case SETPRO6:
						return defaultCloseDialog(env, 5, 6, true, false);
					case FINISH_DIALOG: {
						return closeDialogWindow(env);
					}
					default:
						break;
				}
			}
			else if (targetId == 204020) { // Mabangtah
				switch (env.getDialog()) {
					case QUEST_SELECT:
						if (var == 3) {
							return sendQuestDialog(env, 1693);
						}
					case SETPRO3: {
						giveQuestItem(env, 182201005, 1);
						TeleportService2.teleportTo(player, 210020000, 1759.697f, 905.983f, 427.812f, (byte) 23, TeleportAnimation.BEAM_ANIMATION);
						return defaultCloseDialog(env, 3, 4);
					}
					default:
						break;
				}
			}
		}
		return false;
	}
}
