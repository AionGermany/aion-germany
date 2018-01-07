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
package quest.brusthonin;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author VladimirZ
 */
public class _4012TroublesomePromise extends QuestHandler {

	private final static int questId = 4012;

	public _4012TroublesomePromise() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(205142).addOnQuestStart(questId);
		qe.registerQuestNpc(205142).addOnTalkEvent(questId);
		qe.registerQuestNpc(730104).addOnTalkEvent(questId);
		qe.registerQuestNpc(700342).addOnTalkEvent(questId);
		qe.addHandlerSideQuestDrop(questId, 700342, 182209005, 1, 100);
		qe.registerGetingItem(182209005, questId);
	}

	@Override
	public boolean onDialogEvent(final QuestEnv env) {
		final Player player = env.getPlayer();
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc) {
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 205142) {
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 1011);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 700342: {
					if (qs.getQuestVarById(0) == 0 && env.getDialog() == DialogAction.USE_OBJECT) {
						return true; // loot
					}
					break;
				}
				case 730104: {
					if (qs.getQuestVarById(0) == 1) {
						if (env.getDialog() == DialogAction.QUEST_SELECT) {
							return sendQuestDialog(env, 2375);
						}
						else if (env.getDialogId() == DialogAction.CHECK_USER_HAS_QUEST_ITEM.id()) {
							removeQuestItem(env, 182209005, 1);
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
							return sendQuestDialog(env, 5);
						}
						else {
							return sendQuestEndDialog(env);
						}
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 730104) {
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}

	@Override
	public boolean onGetItemEvent(QuestEnv env) {
		return defaultOnGetItemEvent(env, 0, 0, true); // reward
	}
}
