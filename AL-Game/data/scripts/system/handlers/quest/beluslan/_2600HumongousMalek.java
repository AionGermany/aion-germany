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
package quest.beluslan;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author VladimirZ
 */
public class _2600HumongousMalek extends QuestHandler {

	private final static int questId = 2600;
	private final static int[] npc_ids = { 204734, 798119, 700512 };

	public _2600HumongousMalek() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(204734).addOnQuestStart(questId);
		for (int npc_id : npc_ids) {
			qe.registerQuestNpc(npc_id).addOnTalkEvent(questId);
		}
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc) {
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (targetId == 204734) {
			if (qs == null || qs.getStatus() == QuestStatus.NONE) {
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
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
		if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 204734) {
				return sendQuestEndDialog(env);
			}
		}
		else if (qs.getStatus() != QuestStatus.START) {
			return false;
		}
		if (targetId == 798119) {
			switch (env.getDialog()) {
				case QUEST_SELECT:
					if (var == 0) {
						return sendQuestDialog(env, 1352);
					}
				case SETPRO1:
					if (var == 0) {
						if (giveQuestItem(env, 182204528, 1))
							;
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;

					}
					return false;
				default:
					break;
			}
		}
		else if (targetId == 700512) {
			switch (env.getDialog()) {
				case USE_OBJECT:
					if (var == 1) {
						if (player.getInventory().getItemCountByItemId(182204528) == 1) {
							removeQuestItem(env, 182204528, 1);
							QuestService.addNewSpawn(220040000, 1, 215383, (float) 1140.78, (float) 432.85, (float) 341.0825, (byte) 0);
							return true;
						}
					}
					return false;
				default:
					break;
			}
		}
		else if (targetId == 204734) {
			switch (env.getDialog()) {
				case QUEST_SELECT:
					if (var == 1) {
						return sendQuestDialog(env, 2375);
					}
				case SELECT_QUEST_REWARD:
					if (var == 1) {
						removeQuestItem(env, 182204529, 1);
						qs.setQuestVarById(0, var + 1);
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						return sendQuestDialog(env, 5);
					}
					return false;
				default:
					break;
			}
		}
		return false;
	}
}
