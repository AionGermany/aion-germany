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
package quest.crafting;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Gigi,Modifly by Newlives@aioncore 29-1-2015
 */
public class _19002ExpertAethertappersTest extends QuestHandler {

	private final static int questId = 19002;

	public _19002ExpertAethertappersTest() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(203782).addOnQuestStart(questId);
		qe.registerQuestNpc(203782).addOnTalkEvent(questId);
		qe.registerQuestNpc(203783).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);

		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc) {
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 203782) { // Jhaelas
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
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

		if (qs != null && qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 203783: { // Maire
					switch (env.getDialog()) {
						case QUEST_SELECT:
							return sendQuestDialog(env, 1011);
						case SETPRO1:
							if (!giveQuestItem(env, 122001251, 1)) {
								return true;
							}
							qs.setQuestVarById(0, 1);
							updateQuestStatus(env);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
							return true;
						default:
							break;
					}
				}
				case 203782: {
					switch (env.getDialog()) {
						case QUEST_SELECT: {
							long itemCount1 = player.getInventory().getItemCountByItemId(152003007);
							long itemCount2 = player.getInventory().getItemCountByItemId(152003008);
							if (itemCount1 > 0 && itemCount2 > 0) {
								removeQuestItem(env, 152003007, 1);
								removeQuestItem(env, 152003008, 1);
								qs.setStatus(QuestStatus.REWARD);
								updateQuestStatus(env);
								return sendQuestDialog(env, 1352);
							}
							else {
								return sendQuestDialog(env, 10001);
							}
						}
						default:
							break;
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 203782) { // Jhaelas
				if (env.getDialogId() == DialogAction.CHECK_USER_HAS_QUEST_ITEM.id()) {
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
