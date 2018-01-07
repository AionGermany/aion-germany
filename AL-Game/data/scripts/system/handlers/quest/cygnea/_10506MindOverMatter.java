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
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author pralinka
 */
public class _10506MindOverMatter extends QuestHandler {

	public static final int questId = 10506;

	public _10506MindOverMatter() {
		super(questId);
	}

	@Override
	public void register() {
		int[] npcs = { 804709, 804710, 702666, 702667 };
		qe.registerQuestNpc(236259).addOnKillEvent(questId);
		qe.registerQuestNpc(236263).addOnKillEvent(questId);
		qe.registerOnLevelUp(questId);
		for (int npc : npcs) {
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
		qe.registerQuestItem(182215604, questId);
		qe.registerOnEnterZone(ZoneName.get("LF5_SensoryArea_Q10506"), questId);
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env, 10505, true);
	}

	@Override
	public boolean onKillEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (var == 3) {
				int var1 = qs.getQuestVarById(1);
				if (var1 >= 0 && var1 < 1) {
					return defaultOnKillEvent(env, 236259, var1, var1 + 1, 1);
				}
				else if (var1 == 1) {
					qs.setQuestVar(4);
					updateQuestStatus(env);
					return true;
				}
			}
			if (var == 6) {
				return defaultOnKillEvent(env, 236263, 6, 7);

			}
		}
		return false;
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final Npc npc = (Npc) env.getVisibleObject();
		if (qs == null) {
			return false;
		}
		int var = qs.getQuestVarById(0);
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc) {
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 804709) {
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
			if (targetId == 804710) {
				switch (env.getDialog()) {
					case QUEST_SELECT:
						if (var == 1) {
							return sendQuestDialog(env, 1352);
						}
						else if (var == 5) {
							return sendQuestDialog(env, 2716);
						}
						else if (var == 7) {
							return sendQuestDialog(env, 3399);
						}
					case SETPRO2:
						changeQuestStep(env, 1, 2, false);
						return closeDialogWindow(env);
					case SETPRO6:
						QuestService.addNewSpawn(210070000, 1, 236263, npc.getX(), npc.getY(), npc.getZ(), (byte) 0);
						changeQuestStep(env, 5, 6, false);
						return closeDialogWindow(env);
					case SET_SUCCEED:
						giveQuestItem(env, 182215613, 1);
						qs.setQuestVar(8);
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						return closeDialogWindow(env);
					default:
						break;

				}
			}
			if (targetId == 702666) {
				switch (env.getDialog()) {
					case USE_OBJECT:
						TeleportService2.teleportTo(env.getPlayer(), 210070000, 2837, 2991, 680, (byte) 67, TeleportAnimation.BEAM_ANIMATION);
						changeQuestStep(env, 4, 5, false);
						return true;
					default:
						break;
				}
			}
			if (targetId == 702667) {
				switch (env.getDialog()) {
					case USE_OBJECT:
						TeleportService2.teleportTo(env.getPlayer(), 210070000, 2774, 3007, 275, (byte) 100, TeleportAnimation.BEAM_ANIMATION);
						return true;
					default:
						break;
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 804709) {
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
			if (var == 2) {
				changeQuestStep(env, 2, 3, false);
				return true;
			}
		}
		return false;
	}
}
