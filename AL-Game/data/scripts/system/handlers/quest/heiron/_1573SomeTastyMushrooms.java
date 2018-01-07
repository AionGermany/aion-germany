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
package quest.heiron;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author Ritsu
 */
public class _1573SomeTastyMushrooms extends QuestHandler {

	private final static int questId = 1573;

	public _1573SomeTastyMushrooms() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestItem(182201784, questId);
		qe.registerQuestNpc(730025).addOnQuestStart(questId);
		qe.registerQuestNpc(730025).addOnTalkEvent(questId);
		qe.registerQuestNpc(700194).addOnTalkEvent(questId);
	}

	@Override
	public HandlerResult onItemUseEvent(final QuestEnv env, Item item) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			if (player.isInsideZone(ZoneName.get("LF3_ITEMUSEAREA_Q1573"))) {
				removeQuestItem(env, 182201784, 1);
				return HandlerResult.fromBoolean(useQuestItem(env, item, 1, 2, false, 182201735, 1, 0));
			}
		}
		return HandlerResult.SUCCESS;
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		int targetId = env.getTargetId();
		if (env.getVisibleObject() instanceof Npc) {
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 730025) {
				if (dialog == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 4762);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (targetId == 700194) {
				return true;
			}
			if (targetId == 730025) {
				switch (dialog) {
					case QUEST_SELECT:
						if (var == 0) {
							return sendQuestDialog(env, 1011);
						}
						if (var == 2) {
							removeQuestItem(env, 182201735, 1);
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
							return sendQuestDialog(env, 10002);
						}
					case CHECK_USER_HAS_QUEST_ITEM:
						return checkQuestItems(env, 0, 1, false, 0, 10001, 182201784, 1);
					case SELECT_QUEST_REWARD:
						return sendQuestEndDialog(env);
					default:
						break;
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 730025) {
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}
