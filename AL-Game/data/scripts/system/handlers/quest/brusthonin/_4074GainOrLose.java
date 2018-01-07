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

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.services.item.ItemService;

/**
 * @author Wakizashi
 * @modified vlog
 */
public class _4074GainOrLose extends QuestHandler {

	private final static int questId = 4074;
	private int reward = -1;

	public _4074GainOrLose() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(205181).addOnQuestStart(questId);
		qe.registerQuestNpc(205181).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		int targetId = env.getTargetId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();

		if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
			if (targetId == 205181) { // Bonarunerk
				if (dialog == DialogAction.EXCHANGE_COIN) {
					if (QuestService.startQuest(env)) {
						return sendQuestDialog(env, 1011);
					}
					else {
						return sendQuestSelectionDialog(env);
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 205181) { // Bonarunerk
				long kinahAmount = player.getInventory().getKinah();
				long demonsEye = player.getInventory().getItemCountByItemId(186000038);
				switch (dialog) {
					case EXCHANGE_COIN: {
						return sendQuestDialog(env, 1011);
					}
					case SELECT_ACTION_1011: {
						if (kinahAmount >= 1000 && demonsEye >= 1) {
							changeQuestStep(env, 0, 0, true);
							reward = 0;
							return sendQuestDialog(env, 5);
						}
						else {
							return sendQuestDialog(env, 1009);
						}
					}
					case SELECT_ACTION_1352: {
						if (kinahAmount >= 5000 && demonsEye >= 1) {
							changeQuestStep(env, 0, 0, true);
							reward = 1;
							return sendQuestDialog(env, 6);
						}
						else {
							return sendQuestDialog(env, 1009);
						}
					}
					case SELECT_ACTION_1693: {
						if (kinahAmount >= 25000 && demonsEye >= 1) {
							changeQuestStep(env, 0, 0, true);
							reward = 2;
							return sendQuestDialog(env, 7);
						}
						else {
							return sendQuestDialog(env, 1009);
						}
					}
					case FINISH_DIALOG: {
						return sendQuestSelectionDialog(env);
					}
					default:
						break;
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 205181) { // Bonarunerk
				if (dialog == DialogAction.SELECTED_QUEST_NOREWARD) {
					switch (reward) {
						case 0: {
							if (QuestService.finishQuest(env, 0)) {
								player.getInventory().decreaseKinah(1000);
								removeQuestItem(env, 186000038, 1);
								ItemService.addItem(player, 186000010, 1);
								reward = -1;
								break;
							}
						}
						case 1: {
							if (QuestService.finishQuest(env, 1)) {
								player.getInventory().decreaseKinah(5000);
								removeQuestItem(env, 186000038, 1);
								ItemService.addItem(player, 186000010, Rnd.get(1, 3));
								reward = -1;
								break;
							}
						}
						case 2: {
							if (QuestService.finishQuest(env, 2)) {
								player.getInventory().decreaseKinah(25000);
								removeQuestItem(env, 186000038, 1);
								ItemService.addItem(player, 186000010, Rnd.get(1, 6));
								reward = -1;
								break;
							}
						}
					}
					return closeDialogWindow(env);
				}
				else {
					QuestService.abandonQuest(player, questId);
				}
			}
		}
		return false;
	}
}
