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
package quest.silentera_canyon;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author zhkchi
 */
public class _30151SnufftheSunsucker extends QuestHandler {

	private final static int questId = 30151;

	public _30151SnufftheSunsucker() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(799383).addOnQuestStart(questId);
		qe.registerQuestNpc(799383).addOnTalkEvent(questId);
		qe.registerOnKillInWorld(600010000, questId);
	}

	@Override
	public boolean onKillInWorldEvent(QuestEnv env) {
		if (env.getVisibleObject() instanceof Player) {
			Player killed = ((Player) env.getVisibleObject());
			if ((killed.getLevel() + 9) >= env.getPlayer().getLevel() || (killed.getLevel() - 5) <= env.getPlayer().getLevel()) {
				return defaultOnKillRankedEvent(env, 0, 7, true);
			}
		}
		return false;
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (env.getTargetId() == 799383) {
			if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 1011);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
			else if (qs != null && qs.getStatus() == QuestStatus.REWARD) {
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 1352);
				}
				else {
					return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}
}
