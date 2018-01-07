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
package quest.heiron;

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
public class _1559WhatsintheBox extends QuestHandler {

	private final static int questId = 1559;

	public _1559WhatsintheBox() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(700513).addOnTalkEvent(questId);
		qe.registerQuestNpc(798072).addOnTalkEvent(questId);
		qe.registerQuestNpc(204571).addOnTalkEvent(questId);
		qe.registerQuestNpc(798013).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		int targetId = 0;
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (env.getVisibleObject() instanceof Npc) {
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		if (targetId == 0) {
			if (env.getDialogId() == DialogAction.QUEST_ACCEPT_1.id()) {
				QuestService.startQuest(env);
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(0, 0));
				return true;
			}
		}
		else if (targetId == 700513) {
			if (qs == null || qs.getStatus() == QuestStatus.NONE) {
				switch (env.getDialog()) {
					case USE_OBJECT: {
						if (player.getInventory().getItemCountByItemId(182201823) == 0) {
							return giveQuestItem(env, 182201823, 1);
						}
					}
					default:
						break;
				}
			}
		}
		if (qs == null) {
			return false;
		}

		int var = qs.getQuestVarById(0);
		if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 798072) {
				if (env.getDialog() == DialogAction.USE_OBJECT) {
					return sendQuestDialog(env, 2375);
				}
				else if (env.getDialogId() == DialogAction.SELECT_QUEST_REWARD.id()) {
					return sendQuestDialog(env, 5);
				}
				else {
					return sendQuestEndDialog(env);
				}
			}
		}
		else if (qs.getStatus() != QuestStatus.START) {
			return false;
		}
		if (targetId == 798072) {
			switch (env.getDialog()) {
				case QUEST_SELECT:
					if (var == 0) {
						return sendQuestDialog(env, 1352);
					}
				case SETPRO1:
					if (var == 0) {
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
		else if (targetId == 204571) {
			switch (env.getDialog()) {
				case QUEST_SELECT:
					if (var == 1) {
						return sendQuestDialog(env, 1693);
					}
				case SETPRO2:
					if (var == 1) {
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
		else if (targetId == 798013) {
			switch (env.getDialog()) {
				case QUEST_SELECT:
					if (var == 2) {
						return sendQuestDialog(env, 2034);
					}
				case SETPRO3:
					if (var == 2) {
						{
							qs.setQuestVarById(0, var + 1);
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
							if (giveQuestItem(env, 182201824, 1))
								;
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
							return true;
						}
					}
					return false;
				default:
					break;
			}
		}
		return false;
	}
}
