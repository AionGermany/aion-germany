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
package quest.miragent_holy_templar;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * Quest starter: Lavirintos (203701). Collect the amulets (600) from Calydon Chasers and Calydon Shamans and take them Lavirintos. Defeat Great Protectors in the Eye of Reshanta (300): Aether's
 * Defender (251002), Fire's Defender (251021), Ancient Defender (251018), Nature's Defender (251039), Light's Defender (251033), Shadow's Defender (251036). Talk with Lavirintos. Go to the Dredgion
 * and kill a Dredgion Captains (1): Captain Adhati (214823), Captain Mituna (216850). Talk with Lavirintos. Fill yourself with Divine Power and take Divine Hoat Stone (186000082) to High Priest
 * Jucleas (203752) for the final blessing ritual. Talk with Lavirintos.
 *
 * @author vlog, bobobear
 */
public class _3940Loyalty extends QuestHandler {

	private static final int questId = 3940;
	private static final int[] npcs = { 203701, 203752 };
	private static final int[] mobs = { 251002, 251021, 251018, 251039, 251033, 251036, 214823, 216850 };

	public _3940Loyalty() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(203701).addOnQuestStart(questId);
		for (int npc : npcs) {
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
		for (int mob : mobs) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();
		DialogAction dialog = env.getDialog();
		if (qs == null) {
			if (targetId == 203701) { // Lavirintos
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

		int var = qs.getQuestVars().getQuestVars();

		if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 203701: { // Lavirintos
					switch (dialog) {
						case QUEST_SELECT: {
							if (var == 0) {
								return sendQuestDialog(env, 1011);
							}
							else if (var == 306) {
								return sendQuestDialog(env, 1693);
							}
							else if (var == 4) {
								return sendQuestDialog(env, 2375);
							}
						}
						case CHECK_USER_HAS_QUEST_ITEM: {
							return checkQuestItems(env, 0, 6, false, 10000, 10001); // 1
						}
						case FINISH_DIALOG: {
							return defaultCloseDialog(env, 0, 0);
						}
						case SETPRO3: {
							qs.setQuestVar(3); // 3
							updateQuestStatus(env);
							return sendQuestSelectionDialog(env);
						}
						case SETPRO5: {
							return defaultCloseDialog(env, 4, 5); // 5
						}
						default:
							break;
					}
					break;
				}
				case 203752: { // Jucleas
					switch (dialog) {
						case QUEST_SELECT: {
							if (var == 5) {
								return sendQuestDialog(env, 2716);
							}
						}
						case SELECT_ACTION_2718: {
							if (player.getCommonData().getDp() >= 4000) {
								return checkItemExistence(env, 5, 5, false, 186000083, 1, true, 2718, 2887, 0, 0);
							}
							else {
								return sendQuestDialog(env, 2802);
							}
						}
						case SET_SUCCEED: {
							player.getCommonData().setDp(0);
							return defaultCloseDialog(env, 5, 5, true, false); // reward
						}
						case FINISH_DIALOG: {
							return defaultCloseDialog(env, 5, 5);
						}
						default:
							break;
					}
					break;
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 203701) { // Lavirintos
				if (dialog == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 10002);
				}
				else {
					return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}

	@Override
	public boolean onKillEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVars().getQuestVars();
			if (var >= 6 && var < 306) {
				int[] npcids = { 251002, 251021, 251018, 251039, 251033, 251036 };
				for (int id : npcids) {
					if (targetId == id) {
						qs.setQuestVar(var + 1); // 6 - 306
						updateQuestStatus(env);
						return true;
					}
				}
			}
			else if (var == 3) {
				int[] npcids = { 214823, 216850 };
				return defaultOnKillEvent(env, npcids, 3, 4); // 4
			}
		}
		return false;
	}
}
