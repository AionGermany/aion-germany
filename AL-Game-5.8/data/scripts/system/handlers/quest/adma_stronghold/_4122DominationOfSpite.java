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
package quest.adma_stronghold;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.world.zone.ZoneName;

public class _4122DominationOfSpite extends QuestHandler {

	private final static int questId = 4122;

	public _4122DominationOfSpite() {
		super(questId);
	}

	@Override
	public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName) {
		return defaultOnEnterZoneEvent(env, zoneName, ZoneName.get("ADMA_STRONGHOLD_INTERIOR_320130000"));
	}

	@Override
	public void register() {
		qe.registerQuestNpc(798155).addOnTalkEvent(questId);
		qe.registerQuestNpc(237245).addOnKillEvent(questId);
		qe.registerQuestNpc(237154).addOnKillEvent(questId);
		qe.registerQuestNpc(237152).addOnKillEvent(questId);
		qe.registerQuestNpc(237243).addOnKillEvent(questId);
		qe.registerQuestNpc(237153).addOnKillEvent(questId);
		qe.registerQuestNpc(237239).addOnKillEvent(questId);
		qe.registerOnEnterZone(ZoneName.get("ADMA_STRONGHOLD_INTERIOR_320130000"), questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();
		int targetId = env.getTargetId();
		if (env.getVisibleObject() instanceof Npc) {
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
			if (targetId == 0) {
				switch (dialog) {
					case QUEST_SELECT:
						return sendQuestDialog(env, 4762);
					case QUEST_ACCEPT_1:
					case QUEST_ACCEPT_SIMPLE:
						return sendQuestStartDialog(env);
					case QUEST_REFUSE_SIMPLE:
						return closeDialogWindow(env);
					default:
						break;
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 205225: {
					switch (dialog) {
						case QUEST_SELECT: {
							return sendQuestDialog(env, 10002);
						}
						case SELECT_QUEST_REWARD: {
							return sendQuestEndDialog(env);
						}
						default:
							return sendQuestEndDialog(env);
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 205225) {
				switch (dialog) {
					case SELECT_QUEST_REWARD: {
						return sendQuestDialog(env, 5);
					}
					default:
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
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (var == 0) {
				return defaultOnKillEvent(env, 237245, 0, 1);
			}
			else if (var == 1) {
				return defaultOnKillEvent(env, 237154, 1, 2);
			}
			else if (var == 2) {
				return defaultOnKillEvent(env, 237152, 2, 3);
			}
			else if (var == 3) {
				return defaultOnKillEvent(env, 237243, 3, 4);
			}
			else if (var == 4) {
				return defaultOnKillEvent(env, 237153, 4, 5);
			}
			else if (var == 5) {
				return defaultOnKillEvent(env, 237239, 5, true);
			}
		}
		return false;
	}
}
