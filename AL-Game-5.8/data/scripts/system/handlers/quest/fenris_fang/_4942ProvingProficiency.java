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
 * @author Nanou, modified by bobobear
 */
public class _4942ProvingProficiency extends QuestHandler {

	private final static int questId = 4942;

	public _4942ProvingProficiency() {
		super(questId);
	}

	@Override
	public void register() {
		int[] npcs = { 204104, 204108, 204106, 204110, 204100, 204102, 798317, 204075, 204053 };
		qe.registerQuestNpc(204053).addOnQuestStart(questId); // Kvasir
		for (int npc : npcs) {
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		int targetId = env.getTargetId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();

		// 0 - Start to Kvasir
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 204053) {
				if (dialog == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 4762);
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
				// 1 - Talk with Kvasir to choose the crafting skill
				case 204053:
					if (var == 0) {
						switch (dialog) {
							case QUEST_SELECT:
								return sendQuestDialog(env, 1011);
							case SETPRO1:
								return defaultCloseDialog(env, 0, 1);
							case SETPRO2:
								return defaultCloseDialog(env, 0, 2);
							case SETPRO3:
								return defaultCloseDialog(env, 0, 3);
							case SETPRO4:
								return defaultCloseDialog(env, 0, 4);
							case SETPRO5:
								return defaultCloseDialog(env, 0, 5);
							case SETPRO6:
								return defaultCloseDialog(env, 0, 6);
							default:
								break;
						}
						break;
					}
					// 2 - Talk with Weaponsmithing Master Logi.
				case 204104:
					if (var == 1) {
						switch (dialog) {
							case QUEST_SELECT:
								return sendQuestDialog(env, 1352);
							case SETPRO7:
								return defaultCloseDialog(env, 1, 7, 152206598, 1, 0, 0);
							default:
								break;
						}
					}
					break;
				// 3 - Talk with Handicrafting Master Lanse
				case 204108:
					if (var == 2) {
						switch (dialog) {
							case QUEST_SELECT:
								return sendQuestDialog(env, 1693);
							case SETPRO7:
								return defaultCloseDialog(env, 2, 7, 152206641, 1, 0, 0);
							default:
								break;
						}
					}
					break;
				// 4 - Talk with Armorsmithing Master Kinterun
				case 204106:
					if (var == 3) {
						switch (dialog) {
							case QUEST_SELECT:
								return sendQuestDialog(env, 2034);
							case SETPRO7:
								return defaultCloseDialog(env, 3, 7, 152206617, 1, 0, 0);
							default:
								break;
						}
					}
					break;
				// 5 - Talk with Tailoring Master Zyakia
				case 204110:
					if (var == 4) {
						switch (dialog) {
							case QUEST_SELECT:
								return sendQuestDialog(env, 2375);
							case SETPRO7:
								return defaultCloseDialog(env, 4, 7, 152206634, 1, 0, 0);
							default:
								break;
						}
					}
					break;
				// 6 - Talk with Cooking Master Lainita
				case 204100:
					if (var == 5) {
						switch (dialog) {
							case QUEST_SELECT:
								return sendQuestDialog(env, 2716);
							case SETPRO7:
								return defaultCloseDialog(env, 5, 7, 152206646, 1, 0, 0);
							default:
								break;
						}
					}
					break;
				// 7 - Talk with Alchemy Master Honir
				case 204102:
					if (var == 6) {
						switch (dialog) {
							case QUEST_SELECT:
								return sendQuestDialog(env, 3057);
							case SETPRO7:
								return defaultCloseDialog(env, 6, 7, 152206645, 1, 0, 0);
							default:
								break;
						}
					}
					break;
				// 8 - Take Crafted Heart to Usener
				case 798317:
					if (var == 7) {
						switch (dialog) {
							case QUEST_SELECT:
								return sendQuestDialog(env, 3398);
							case CHECK_USER_HAS_QUEST_ITEM:
								return checkItemExistence(env, 7, 8, false, 186000077, 1, true, 10000, 10001, 0, 0);
							default:
								break;
						}
					}
					break;
				// 10 - Take Brightly Glowing Holy Water to visit High Priest Balder for a purification ritual.
				case 204075:
					switch (dialog) {
						case QUEST_SELECT: {
							if (var == 8) {
								return sendQuestDialog(env, 3740);
							}
						}
						case SET_SUCCEED: {
							if (player.getInventory().getItemCountByItemId(186000085) >= 1) {
								removeQuestItem(env, 186000085, 1);
								return defaultCloseDialog(env, 8, 8, true, false, 0);
							}
							else {
								return sendQuestDialog(env, 3825);
							}
						}
						case FINISH_DIALOG: {
							return sendQuestSelectionDialog(env);
						}
						default:
							break;
					}
					break;
				// No match
				default:
					return sendQuestStartDialog(env);
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 204053) { // Kvasir
				if (dialog == DialogAction.USE_OBJECT) {
					return sendQuestDialog(env, 10002);
				}
				else {
					return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}
}
