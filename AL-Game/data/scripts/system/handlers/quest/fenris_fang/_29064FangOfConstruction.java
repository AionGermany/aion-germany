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
package quest.fenris_fang;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author Cheatkiller
 */
public class _29064FangOfConstruction extends QuestHandler {

	private final static int questId = 29064;

	public _29064FangOfConstruction() {
		super(questId);
	}

	@Override
	public void register() {
		int[] npcs = { 798452, 204075, 204053 };
		qe.registerQuestNpc(204053).addOnQuestStart(questId);
		for (int npc : npcs) {
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();
		int targetId = env.getTargetId();

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 204053) {
				if (dialog == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 1011);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}

		if (qs == null) {
			return false;
		}

		int var = qs.getQuestVarById(0);

		if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 798452:
					switch (dialog) {
						case QUEST_SELECT:
							return sendQuestDialog(env, 1352);
						case SETPRO1:
							return defaultCloseDialog(env, 0, 1); // 1
						default:
							break;
					}
					break;
				case 204075:
					switch (dialog) {
						case QUEST_SELECT: {
							if (var == 1 && player.getInventory().getItemCountByItemId(182213239) >= 1 && player.getInventory().getItemCountByItemId(186000085) >= 1) {
								return sendQuestDialog(env, 2375);
							}
						}
						case CHECK_USER_HAS_QUEST_ITEM_SIMPLE: {
							if (player.getInventory().getItemCountByItemId(182213239) >= 1 && player.getInventory().getItemCountByItemId(186000085) >= 1) {
								removeQuestItem(env, 182213239, 1);
								removeQuestItem(env, 186000085, 1);
								return defaultCloseDialog(env, 1, 1, true, false);
							}
						}
						default:
							break;
					}
					break;
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 204075) {
				if (dialog == DialogAction.USE_OBJECT) {
					return sendQuestDialog(env, 5);
				}
				else {
					return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}
}
