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
package quest.inggison;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.services.teleport.TeleportService2;

/**
 * @author FrozenKiller
 */
public class _10031ARiskForTheObelisk extends QuestHandler {

	private final static int questId = 10031;
	private final static int[] mobs = { 215504, 215505, 216463, 216783, 216692, 215517, 216648, 215519, 216691, 215516, 216647, 215518, 215508 };

	public _10031ARiskForTheObelisk() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerOnEnterWorld(questId);
		int[] npcs = { 203700, 798600, 798408, 798926, 799052, 798927, 730224, 702662 };
		qe.registerOnLevelUp(questId);
		qe.registerOnEnterWorld(questId);
		qe.registerQuestItem(182215590, questId);
		for (int npc : npcs) {
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
		for (int mob : mobs) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env);
	}

	@Override
	public boolean onEnterWorldEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);

		if (player.getWorldId() == 110010000) {
			if (qs == null) {
				env.setQuestId(questId);
				if (QuestService.startQuest(env)) {
					return true;
				}
			}
		}
		else if (player.getWorldId() == 210050000) {
			if (qs != null && qs.getStatus() == QuestStatus.START) {
				int var = qs.getQuestVarById(0);
				if (var == 3) {
					qs.setQuestVar(++var);
					updateQuestStatus(env);
					return playQuestMovie(env, 501);
				}
			}
		}
		return false;
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final DialogAction dialog = env.getDialog();
		if (qs == null) {
			return false;
		}
		int var = qs.getQuestVarById(0);
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc) {
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 203700) { // Fasimedes
				switch (dialog) {
					case QUEST_SELECT:
						if (var == 0) {
							return sendQuestDialog(env, 1011);
						}
					case SETPRO1:
						return defaultCloseDialog(env, 0, 1);
					default:
						break;
				}
			}
			else if (targetId == 798600) { // Eremitia
				switch (dialog) {
					case QUEST_SELECT:
						if (var == 1) {
							return sendQuestDialog(env, 1352);
						}
					case SETPRO2:
						return defaultCloseDialog(env, 1, 2);
					default:
						break;
				}
			}
			else if (targetId == 798408) { // Sibylle
				switch (dialog) {
					case QUEST_SELECT:
						if (var == 2) {
							return sendQuestDialog(env, 1693);
						}
					case SETPRO3:
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						TeleportService2.teleportTo(player, 210050000, 1, 1440, 408, 553, (byte) 77);
						return true;
					default:
						break;
				}
			}
			else if (targetId == 798926) { // Outremus
				switch (dialog) {
					case QUEST_SELECT:
						if (var == 4) {
							return sendQuestDialog(env, 2375);
						}
					case SETPRO5:
						return defaultCloseDialog(env, 4, 5);
					default:
						break;
				}
			}
			else if (targetId == 799052) { // Steropes
				switch (dialog) {
					case QUEST_SELECT:
						if (var == 5) {
							return sendQuestDialog(env, 2716);
						}
					case SETPRO6:
						return defaultCloseDialog(env, 5, 6);
					default:
						break;
				}
			}
			else if (targetId == 798927) { // Versetti
				switch (dialog) {
					case QUEST_SELECT:
						if (var == 6) {
							return sendQuestDialog(env, 3057);
						}
					case SETPRO7:
						giveQuestItem(env, 182215616, 1);
						giveQuestItem(env, 182215617, 1);
						playQuestMovie(env, 516);
						return defaultCloseDialog(env, 6, 7);
					default:
						break;
				}
			}
			else if (targetId == 730224) { // Overheated Obelisk
				switch (dialog) {
					case USE_OBJECT: {
						if (var == 7) {
							return sendQuestDialog(env, 3398);
						}
					}
					case SETPRO8:
						removeQuestItem(env, 182215616, 1);
						return defaultCloseDialog(env, 7, 8);
					default:
						break;
				}
			}
			else if (targetId == 702662) { // Southern Obelisk Support
				if (env.getDialog() == DialogAction.USE_OBJECT) {
					if (var == 8) {
						removeQuestItem(env, 182215617, 1);
						changeQuestStep(env, 8, 9, false); // 9
						return closeDialogWindow(env);
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 798927) { // Versetti
				if (env.getDialog() == DialogAction.USE_OBJECT) {
					return sendQuestDialog(env, 10002);
				}
				else if (env.getDialogId() == DialogAction.SELECT_QUEST_REWARD.id()) {
					return sendQuestDialog(env, 5);
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
		int targetId = env.getTargetId();
		if (qs == null) {
			return false;
		}
		int var = qs.getQuestVarById(0);
		int var1 = qs.getQuestVarById(1);
		int var2 = qs.getQuestVarById(2);

		int[] mobs = { 215504, 215505, 216463, 216783, 216692, 215517, 216648, 215519, 216691, 215516, 216647, 215518, 215508 };

		if (qs.getStatus() == QuestStatus.START) {
			if (var == 9) {
				if (var1 + var2 < 11) {
					if (targetId == 215508) {
						if (var2 < 2) {
							return defaultOnKillEvent(env, 215508, var2, var2 + 1, 2);
						}
					}
					else {
						if (var1 < 10) {
							return defaultOnKillEvent(env, mobs, var1, var1 + 1, 1);
						}
					}
				}
				else {
					qs.setQuestVar(10); // 10
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					return true;
				}
			}
		}
		return false;
	}
}
