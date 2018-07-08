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
package quest.event_quests;

import java.util.List;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.quest.QuestItems;
import com.aionemu.gameserver.model.templates.rewards.BonusType;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.events.EventService;
import com.aionemu.gameserver.services.QuestService;

/**
 * @author Rolandas
 */
public class _80016EventSockHop extends QuestHandler {

	private final static int questId = 80016;

	public _80016EventSockHop() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerOnLevelUp(questId);
		qe.registerQuestNpc(799763).addOnQuestStart(questId);
		qe.registerQuestNpc(799763).addOnTalkEvent(questId);
		qe.registerOnBonusApply(questId, BonusType.MOVIE);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);

		if ((qs == null || qs.getStatus() == QuestStatus.NONE) && !onLvlUpEvent(env)) {
			return false;
		}

		if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.getStatus() == QuestStatus.COMPLETE && qs.getCompleteCount() < 10) {
			if (env.getTargetId() == 799763) {
				switch (env.getDialog()) {
					case USE_OBJECT:
						return sendQuestDialog(env, 1011);
					case QUEST_ACCEPT_1: {
						QuestService.startEventQuest(env, QuestStatus.START);
						return sendQuestDialog(env, 1003);
					}
					default:
						return sendQuestStartDialog(env);
				}
			}
			return false;
		}

		int var = qs.getQuestVarById(0);

		if (qs.getStatus() == QuestStatus.START) {
			if (env.getTargetId() == 799763) {
				switch (env.getDialog()) {
					case USE_OBJECT:
					case QUEST_SELECT:
						if (var == 0) {
							return sendQuestDialog(env, 2375);
						}
					case CHECK_USER_HAS_QUEST_ITEM:
						return checkQuestItems(env, 0, 1, true, 5, 2716);
					default:
						break;
				}
			}
		}

		return sendQuestRewardDialog(env, 799763, 0);
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);

		if (EventService.getInstance().checkQuestIsActive(questId)) {
			if (!QuestService.checkLevelRequirement(questId, player.getCommonData().getLevel())) {
				return false;
			}

			// Start once
			if (qs == null || qs.getStatus() == QuestStatus.NONE) {
				return QuestService.startEventQuest(env, QuestStatus.START);
			}
		}
		else if (qs != null) {
			// Set as expired
			QuestService.abandonQuest(player, questId);
		}
		return false;
	}

	@Override
	public HandlerResult onBonusApplyEvent(QuestEnv env, BonusType bonusType, List<QuestItems> rewardItems) {
		if (bonusType != BonusType.MOVIE || env.getQuestId() != questId) {
			return HandlerResult.UNKNOWN;
		}

		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.REWARD) {
			if (qs.getCompleteCount() == 9) { // [Event] Hat Box
				rewardItems.add(new QuestItems(188051106, 1));
			}
			// randomize movie
			if (Rnd.get() * 100 < 50) {
				playQuestMovie(env, 103);
			}
			else {
				playQuestMovie(env, 104);
			}
			return HandlerResult.SUCCESS;
		}
		return HandlerResult.FAILED;
	}
}
