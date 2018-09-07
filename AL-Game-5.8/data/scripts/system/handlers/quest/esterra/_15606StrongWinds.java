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
public class _15606StrongWinds extends QuestHandler {

	public static final int questId = 15606;

	public _15606StrongWinds() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(806163).addOnQuestStart(questId); // Taleia
		qe.registerQuestNpc(806163).addOnTalkEvent(questId); // Taleia
		qe.registerQuestNpc(703147).addOnTalkEvent(questId); // Storm Daeva Chest
		qe.registerOnEnterZone(ZoneName.get("STORM_PLATEAU_210100000"), questId);
		qe.registerOnEnterZone(ZoneName.get("ISLAND_OF_FREEDOM_210100000"), questId);

	}

	@Override
	public boolean onDialogEvent(final QuestEnv env) {
		final Player player = env.getPlayer();
		int targetId = env.getTargetId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();

		if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
			if (targetId == 806163) { // Taleia
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
			if (targetId == 806163) { // Taleia
				switch (dialog) {
					case QUEST_SELECT: {
						if (var == 2) {
							return sendQuestDialog(env, 1693);
						}
						else if (var == 4) {
							return sendQuestDialog(env, 2375);
						}
					}
					case SETPRO3: {
						qs.setQuestVar(3);
						updateQuestStatus(env);
						return closeDialogWindow(env);
					}
					case CHECK_USER_HAS_QUEST_ITEM: {
						qs.setStatus(QuestStatus.REWARD);
						return checkQuestItems(env, 4, 5, false, 10000, 10001);
					}
					default:
						break;
				}
			}
			else if (targetId == 703147) { // Storm Daeva Chest
				switch (dialog) {
					case USE_OBJECT: {
						if (var == 1) {
							return defaultCloseDialog(env, 1, 2);
						}
					}
					default:
						break;
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 806163) { // Taleia
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
			if (var == 0 && zoneName == ZoneName.get("STORM_PLATEAU_210100000")) {
				changeQuestStep(env, 0, 1, false);
				return true;
			}
			else if (var == 3 && zoneName == ZoneName.get("ISLAND_OF_FREEDOM_210100000")) {
				changeQuestStep(env, 3, 4, false);
				return true;
			}
		}
		return false;
	}
}
