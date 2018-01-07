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
package quest.oriel;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.house.House;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author Rolandas
 */
public class _18821AlmostForgotMyBlessings extends QuestHandler {

	private static final int questId = 18821;
	private static final Set<Integer> butlers;

	static {
		butlers = new HashSet<Integer>();
		butlers.add(810017);
		butlers.add(810018);
		butlers.add(810019);
		butlers.add(810020);
		butlers.add(810021);
		butlers.add(810022);
	}

	public _18821AlmostForgotMyBlessings() {
		super(questId);
	}

	@Override
	public void register() {
		Iterator<Integer> iter = butlers.iterator();
		while (iter.hasNext()) {
			int butlerId = iter.next();
			qe.registerQuestNpc(butlerId).addOnQuestStart(questId);
			qe.registerQuestNpc(butlerId).addOnTalkEvent(questId);
		}
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		int targetId = env.getTargetId();

		if (!butlers.contains(targetId)) {
			return false;
		}

		House house = player.getActiveHouse();
		if (house == null || house.getButler() == null || house.getButler().getNpcId() != targetId) {
			return false;
		}

		DialogAction dialog = env.getDialog();
		QuestState qs = player.getQuestStateList().getQuestState(questId);

		if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
			switch (dialog) {
				case QUEST_SELECT:
					return sendQuestDialog(env, 1011);
				case QUEST_ACCEPT_1:
				case QUEST_ACCEPT_SIMPLE:
					return sendQuestStartDialog(env);
				default:
					break;
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			switch (dialog) {
				case QUEST_SELECT:
					return sendQuestDialog(env, 2375);
				case SELECT_QUEST_REWARD:
					changeQuestStep(env, 0, 0, true);
					return sendQuestDialog(env, 5);
				case SELECTED_QUEST_NOREWARD:
					return sendQuestEndDialog(env);
				default:
					break;
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			switch (dialog) {
				case USE_OBJECT:
					return sendQuestDialog(env, 5);
				case SELECTED_QUEST_NOREWARD:
					sendQuestEndDialog(env);
					return true;
				default:
					break;
			}
		}

		return false;
	}
}
