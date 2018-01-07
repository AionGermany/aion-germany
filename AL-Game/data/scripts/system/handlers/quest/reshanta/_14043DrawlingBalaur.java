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
package quest.reshanta;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author pralinka
 */
public class _14043DrawlingBalaur extends QuestHandler {

	private final static int questId = 14043;

	public _14043DrawlingBalaur() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerOnEnterZoneMissionEnd(questId);
		qe.registerOnLevelUp(questId);
		qe.registerQuestItem(182215351, questId);
		qe.registerQuestNpc(278532).addOnTalkEvent(questId);
		qe.registerQuestNpc(798026).addOnTalkEvent(questId);
		qe.registerQuestNpc(798025).addOnTalkEvent(questId);
		qe.registerQuestNpc(279019).addOnTalkEvent(questId);
	}

	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env) {
		return defaultOnZoneMissionEndEvent(env);
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env, 14040, true);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null) {
			return false;
		}

		int var = qs.getQuestVarById(0);
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc) {
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}

		if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 278532) {
				if (env.getDialog() == DialogAction.USE_OBJECT) {
					return sendQuestDialog(env, 10002);
				}
				else if (env.getDialogId() == 1009) {
					return sendQuestDialog(env, 5);
				}
				else {
					return sendQuestEndDialog(env);
				}
			}
			return false;
		}
		else if (qs.getStatus() != QuestStatus.START) {
			return false;
		}
		if (targetId == 278532) {
			switch (env.getDialog()) {
				case QUEST_SELECT:
					if (var == 0) {
						return sendQuestDialog(env, 1011);
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
		else if (targetId == 798026) {
			switch (env.getDialog()) {
				case QUEST_SELECT:
					if (var == 1) {
						return sendQuestDialog(env, 1352);
					}
					else if (var == 4) {
						return sendQuestDialog(env, 2375);
					}
					else if (var == 6) {
						return sendQuestDialog(env, 3057);
					}
				case SETPRO5:
					if (var == 4) {
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						removeQuestItem(env, 182215352, 1);
						giveQuestItem(env, 182215351, 1);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
					return false;
				case SETPRO7:
					if (var == 6) {
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
					return false;
				case SETPRO2:
					if (var == 1) {
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
				case SETPRO11:
					if (var == 1 && player.getInventory().tryDecreaseKinah(20000)) {
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
					else {
						return sendQuestDialog(env, 1355);
					}
				case SETPRO12:
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
		else if (targetId == 798025) {
			switch (env.getDialog()) {
				case QUEST_SELECT:
					if (var == 2) {
						return sendQuestDialog(env, 1693);
					}
				case SETPRO3:
					if (var == 2) {
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
		else if (targetId == 279019) {
			switch (env.getDialog()) {
				case QUEST_SELECT:
					if (var == 3) {
						return sendQuestDialog(env, 2034);
					}
					return false;
				case SETPRO4:
					if (var == 3) {
						if (!giveQuestItem(env, 182215352, 1)) {
							return true;
						}
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

		return false;
	}

	@Override
	public HandlerResult onItemUseEvent(QuestEnv env, Item item) {
		final Player player = env.getPlayer();
		final int id = item.getItemTemplate().getTemplateId();
		final int itemObjId = item.getObjectId();

		if (id != 182215351) {
			return HandlerResult.UNKNOWN;
		}
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null) {
			return HandlerResult.FAILED;
		}

		PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 1, 1, 0), true);
		removeQuestItem(env, 182215351, 1);
		qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
		updateQuestStatus(env);
		return HandlerResult.SUCCESS;
	}
}
