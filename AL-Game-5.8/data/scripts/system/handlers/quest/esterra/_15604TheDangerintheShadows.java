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
package quest.esterra;

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
public class _15604TheDangerintheShadows extends QuestHandler {

	public static final int questId = 15604;

	public _15604TheDangerintheShadows() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(806161).addOnQuestStart(questId); // Siklon
		qe.registerQuestNpc(806161).addOnTalkEvent(questId); // Siklon
		qe.registerQuestNpc(241161).addOnKillEvent(questId); // Shadow Goliant
		qe.registerQuestNpc(241160).addOnKillEvent(questId); // Shadow Sheluk
		qe.registerOnEnterZone(ZoneName.get("BLACK_CAVERN_210100000"), questId);

	}

	@Override
	public boolean onDialogEvent(final QuestEnv env) {
		final Player player = env.getPlayer();
		int targetId = env.getTargetId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();

		if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
			if (targetId == 806161) { // Siklon
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
			if (targetId == 806161) { // Siklon
				switch (dialog) {
					case QUEST_SELECT: {
						if (var == 2) {
							return sendQuestDialog(env, 1693);
						}
						else if (var == 3) {
							return sendQuestDialog(env, 2034);
						}
					}
					case SETPRO3: {
						qs.setQuestVar(3);
						updateQuestStatus(env);
						return closeDialogWindow(env);
					}
					case CHECK_USER_HAS_QUEST_ITEM: {
						qs.setStatus(QuestStatus.REWARD);
						return checkQuestItems(env, 3, 4, false, 10000, 10001); // reward
					}
					default:
						break;
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 806161) { // Siklon
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
			if (var == 0 && zoneName == ZoneName.get("BLACK_CAVERN_210100000")) {
				changeQuestStep(env, 0, 1, false);
				return true;
			}
			else if (var == 3 && zoneName == ZoneName.get("BLACK_CAVERN_210100000")) {
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
		if (qs == null || qs.getStatus() != QuestStatus.START) {
			return false;
		}

		int targetId = env.getTargetId();
		int var = qs.getQuestVarById(0);
		int var1 = qs.getQuestVarById(1);
		if (var == 1 && var1 < 4) {
			return defaultOnKillEvent(env, 241161, var1, var1 + 1, 1);
		}
		else if (var == 1 && var1 == 4) {
			qs.setQuestVar(2);
			updateQuestStatus(env);
			return true;
		}
		switch (targetId) {
			case 241160:
				if (qs.getQuestVarById(1) != 0) {
					qs.setQuestVar(5);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
				}
		}
		return false;
	}
}
