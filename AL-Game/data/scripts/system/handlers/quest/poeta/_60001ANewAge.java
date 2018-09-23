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
package quest.poeta;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author FrozenKiller
 */
public class _60001ANewAge extends QuestHandler {

	private final static int questId = 60001;

	public _60001ANewAge() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerOnLevelUp(questId);
		qe.registerQuestNpc(790001).addOnTalkEvent(questId); // Pernos
		qe.registerQuestNpc(820134).addOnTalkEvent(questId); // Guid to Rare Poisons
		qe.registerQuestNpc(820135).addOnTalkEvent(questId); // Day of the Storm
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env, 60000, false);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();
		int targetId = env.getTargetId();
		if (qs == null) {
			return false;
		}

		if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
			case 790001:
				switch (dialog) {
				case QUEST_SELECT:
					return sendQuestDialog(env, 1011);
				case SETPRO1:
					qs.setQuestVar(1);
					updateQuestStatus(env);
					return closeDialogWindow(env);
				default:
					break;
				}
				break;
			case 820134:
				switch (dialog) {
				case QUEST_SELECT:
					return sendQuestDialog(env, 1352);
				case SETPRO2:
					qs.setQuestVar(2);
					updateQuestStatus(env);
					return closeDialogWindow(env);
				default:
					break;
				}
				break;
			case 820135:
				switch (dialog) {
				case QUEST_SELECT:
					return sendQuestDialog(env, 1693);
				case SET_SUCCEED:
					qs.setQuestVar(3);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					return closeDialogWindow(env);
				default:
					break;
				}
				break;
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 790001) {
				if (dialog == DialogAction.USE_OBJECT) {
					return sendQuestDialog(env, 10002);
				}
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}
