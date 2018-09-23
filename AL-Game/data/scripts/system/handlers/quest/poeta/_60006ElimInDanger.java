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
package quest.poeta;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/**
 * @author FrozenKiller
 */
public class _60006ElimInDanger extends QuestHandler {

	private final static int questId = 60006;

	public _60006ElimInDanger() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerOnLevelUp(questId);
		qe.registerQuestNpc(730007).addOnTalkEvent(questId); // Forest Protector Noah
		qe.registerQuestNpc(820007).addOnTalkEvent(questId); // Rooted Elder
		qe.registerQuestNpc(820008).addOnTalkEvent(questId); // Branching Elder
		qe.registerQuestNpc(820009).addOnTalkEvent(questId); // Leafy Elder
		qe.registerQuestNpc(730008).addOnTalkEvent(questId); // Daminu
		qe.registerQuestItem(182216250, questId);
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env, 60000, false);

	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();
		int targetId = env.getTargetId();
		if (qs == null) {
			return false;
		}

		if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
			case 730007:
				switch (dialog) {
				case QUEST_SELECT:
					return sendQuestDialog(env, 1011);
				case SETPRO1:
					qs.setQuestVar(1);
					updateQuestStatus(env);
					return closeDialogWindow(env);
				default:
					break;
				}
				break;
			case 820007:
				switch (dialog) {
				case QUEST_SELECT:
					return sendQuestDialog(env, 1352);
				case SETPRO2:
					qs.setQuestVar(2);
					updateQuestStatus(env);
					giveQuestItem(env, 182216248, 1);
					return closeDialogWindow(env);
				default:
					break;
				}
				break;
			case 820008:
				switch (dialog) {
				case QUEST_SELECT:
					return sendQuestDialog(env, 1693);
				case SETPRO3:
					qs.setQuestVar(3);
					updateQuestStatus(env);
					return closeDialogWindow(env);
				default:
					break;
				}
				break;
			case 820009:
				switch (dialog) {
				case QUEST_SELECT:
					return sendQuestDialog(env, 2034);
				case CHECK_USER_HAS_QUEST_ITEM:
					if (QuestService.collectItemCheck(env, true)) {
						removeQuestItem(env, 182216248, 1);
						qs.setQuestVar(4);
						updateQuestStatus(env);
						return sendQuestDialog(env, 10000);
					} else {
						return sendQuestDialog(env, 10001);
					}
				case SETPRO5:
					giveQuestItem(env, 182216250, 1);
					qs.setQuestVar(5);
					updateQuestStatus(env);
					return closeDialogWindow(env);
				default:
					break;
				}
				break;
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 730008) {
				if (dialog == DialogAction.USE_OBJECT) {
					return sendQuestDialog(env, 10002);
				}
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}

	@Override
	public HandlerResult onItemUseEvent(final QuestEnv env, Item item) {
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final int id = item.getItemTemplate().getTemplateId();
		final int itemObjId = item.getObjectId();

		if (id != 182216250) {
			return HandlerResult.UNKNOWN;
		}
		PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), 0, itemObjId, id, 2000, 0), true);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), 0, itemObjId, id, 0, 1), true);
				TeleportService2.teleportTo(player, 210010000, 502.2042f, 1529.8256f, 104.6827f, (byte) 45,	TeleportAnimation.BEAM_ANIMATION);
				qs.setQuestVar(6);
				qs.setStatus(QuestStatus.REWARD);
				updateQuestStatus(env);
			}
		}, 2000);
		return HandlerResult.SUCCESS;
	}
}