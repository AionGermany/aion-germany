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
package quest.daevation;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;

/**
 * @author Alcapnwd
 */
public class _80292ANewChallenge extends QuestHandler {

	private static final int questId = 80292;
	private final static int dialogs[] = { 1013, 1034, 1055, 1076, 5103, 1098, 1119, 1140, 1161, 5104, 1183, 1204, 1225, 1246, 5105, 1268, 1289, 1310, 1331, 5106, 2376, 2461, 2546, 2631, 2632 };
	private final static int items[] = { 110601356, 113601308, 114601305, 112601299, 111601319, 110301409, 113301374, 114301409, 112301293, 111301350, 110101513, 113101375, 114101403, 112101312, 111101358, 110501382, 113501355, 114501363, 112501280, 111501340, 110301669, 113301638, 114301673, 112301546, 111301607 };
	private int choice = 0;
	private int item;

	public _80292ANewChallenge() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(831385).addOnQuestStart(questId);
		qe.registerQuestNpc(831385).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		int targetId = env.getTargetId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int dialogId = env.getDialogId();

		if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
			if (targetId == 831385) {
				if (dialogId == DialogAction.EXCHANGE_COIN.id()) {
					QuestService.startQuest(env);
					return sendQuestDialog(env, 1011);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs != null && qs.getStatus() == QuestStatus.START) {
			if (targetId == 831385) {
				if (dialogId == DialogAction.EXCHANGE_COIN.id()) {
					return sendQuestDialog(env, 1011);
				}
				switch (dialogId) {
					case 1012:
					case 1097:
					case 1182:
					case 1267:
					case 2375:
						return sendQuestDialog(env, dialogId);
					case 1013:
					case 1034:
					case 1055:
					case 1076:
					case 5103:
					case 1098:
					case 1119:
					case 1140:
					case 1161:
					case 5104:
					case 1183:
					case 1204:
					case 1225:
					case 1246:
					case 5105:
					case 1268:
					case 1289:
					case 1310:
					case 1331:
					case 5106:
					case 2376:
					case 2461:
					case 2546:
					case 2631:
					case 2632: {
						item = getItem(dialogId);
						if (player.getInventory().getItemCountByItemId(item) > 0)
							return sendQuestDialog(env, 1013);
						else
							return sendQuestDialog(env, 1352);
					}
					case 10000:
					case 10001:
					case 10002:
					case 10003: {
						if (player.getInventory().getItemCountByItemId(186000041) < 10)
							return sendQuestDialog(env, 1009);
						changeQuestStep(env, 0, 0, true);
						choice = dialogId - 10000;
						return sendQuestDialog(env, choice + 5);
					}
					case 10004:
						if (player.getInventory().getItemCountByItemId(186000041) < 10)
							return sendQuestDialog(env, 1009);
						changeQuestStep(env, 0, 0, true);
						choice = dialogId - 10000;
						return sendQuestDialog(env, 45);
				}
			}
		}
		else if (qs != null && qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 831385) {
				removeQuestItem(env, item, 1);
				removeQuestItem(env, 186000041, 10);
				return sendQuestEndDialog(env, choice);
			}
		}
		return false;
	}

	private int getItem(int dialogId) {
		int x = 0;
		for (int id : dialogs) {
			if (id == dialogId)
				break;
			x++;
		}
		return (items[x]);
	}
}
