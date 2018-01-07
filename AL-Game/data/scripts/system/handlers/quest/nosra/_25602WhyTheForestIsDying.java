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
package quest.nosra;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author Phantom_KNA
 */
public class _25602WhyTheForestIsDying extends QuestHandler {

	public static final int questId = 25602;

	public _25602WhyTheForestIsDying() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(806171).addOnQuestStart(questId); // Phindar
		qe.registerQuestNpc(806171).addOnTalkEvent(questId); // Phindar
		qe.registerQuestNpc(241200).addOnKillEvent(questId); // Urughal
		qe.registerOnEnterZone(ZoneName.get("CURSED_GORGE_220110000"), questId);

	}

	@Override
	public boolean onDialogEvent(final QuestEnv env) {
		final Player player = env.getPlayer();
		int targetId = env.getTargetId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();

		if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
			if (targetId == 806171) { // Phindar
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 4762);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (targetId == 806171) { // Phindar
				switch (dialog) {
					case QUEST_SELECT: {
						if (var == 1) {
							return sendQuestDialog(env, 1352);
						}
						else if (var == 2) {
							return sendQuestDialog(env, 1693);
						}
					}
					case CHECK_USER_HAS_QUEST_ITEM: {
						return checkQuestItems(env, 1, 2, false, 10000, 10001);
					}
					case SETPRO3: {
						qs.setQuestVar(3);
						updateQuestStatus(env);

						return closeDialogWindow(env);
					}
					default:
						break;
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 806171) { // Phindar
				if (dialog == DialogAction.USE_OBJECT) {
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
		if (player == null) {
			return false;
		}

		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (var == 0 && zoneName == ZoneName.get("CURSED_GORGE_220110000")) { // TODO Original Zone
				changeQuestStep(env, 0, 1, false);
				return true;
			}
			else if (var == 3 && zoneName == ZoneName.get("CURSED_GORGE_220110000")) { // TODO Original Zone
				changeQuestStep(env, 3, 4, false);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onKillEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() != QuestStatus.START)
			return false;

		int targetId = env.getTargetId();

		switch (targetId) {
			case 241200:
				if (qs.getQuestVarById(1) != 0) {
					qs.setQuestVarById(1, qs.getQuestVarById(1) + 1);
					updateQuestStatus(env);
				}
				else {
					qs.setQuestVarById(0, 5);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
				}
		}
		return false;
	}
}
