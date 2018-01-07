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
package quest.verteron;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author MrPoke
 */
public class _1131UndeliveredArmor extends QuestHandler {

	private final static int questId = 1131;

	public _1131UndeliveredArmor() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(203097).addOnQuestStart(questId);
		qe.registerQuestNpc(203097).addOnTalkEvent(questId);
		qe.registerQuestNpc(799093).addOnTalkEvent(questId);
		qe.registerQuestNpc(203101).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		int targetId = env.getTargetId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 203097) {
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 1011);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs != null && qs.getStatus() == QuestStatus.START) {
			if (targetId == 799093) {
				switch (env.getDialog()) {
					case QUEST_SELECT:
						return sendQuestDialog(env, 1352);
					case SETPRO1: {
						qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
					default:
						return sendQuestStartDialog(env);
				}
			}
			else if (targetId == 203101) {
				switch (env.getDialog()) {
					case QUEST_SELECT:
						return sendQuestDialog(env, 2375);
					case SELECT_QUEST_REWARD: {
						qs.setQuestVar(2);
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						return sendQuestEndDialog(env);
					}
					default:
						break;
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 203101) {
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}
