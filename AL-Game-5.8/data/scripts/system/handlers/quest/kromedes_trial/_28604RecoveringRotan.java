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
package quest.kromedes_trial;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestActionType;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author Rolandas
 */
public class _28604RecoveringRotan extends QuestHandler {

	private final static int questId = 28604;

	public _28604RecoveringRotan() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerOnEnterZone(ZoneName.get("GRAND_CAVERN_300230000"), questId);
		qe.registerQuestNpc(700961).addOnTalkEvent(questId); // Grave Robber's Corpse
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null) {
			return false;
		}

		if (env.getTargetId() == 700961) {
			if (env.getDialog() == DialogAction.USE_OBJECT) {
				if (qs.getStatus() == QuestStatus.START) {
					env.setQuestId(questId);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					return QuestService.finishQuest(env);
				}
				if (this.checkItemExistence(env, 164000141, 1, false)) {
					env.setQuestId(0);
					return sendQuestDialog(env, 27);
				}
				else {
					env.setQuestId(0);
					giveQuestItem(env, 164000141, 1);
					return sendQuestDialog(env, 1012);
				}
			}
		}

		return false;
	}

	@Override
	public boolean onCanAct(QuestEnv env, QuestActionType questEventType, Object... objects) {
		// Allow to use body even when quest was completed
		return env.getTargetId() == 700961 && questEventType == QuestActionType.ACTION_ITEM_USE;
	}

	@Override
	public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName) {
		if (zoneName != ZoneName.get("GRAND_CAVERN_300230000")) {
			return false;
		}

		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			env.setQuestId(questId);
			return QuestService.startQuest(env, QuestStatus.START);
		}

		return false;
	}
}
