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
import com.aionemu.gameserver.services.item.ItemService;
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

		qe.registerQuestNpc(730007).addOnTalkEvent(questId); // Forest Protector Noah
		qe.registerQuestNpc(730008).addOnTalkEvent(questId); // Daminu
		qe.registerQuestNpc(820007).addOnTalkEvent(questId); // Rooted Elder
		qe.registerQuestNpc(820008).addOnTalkEvent(questId); // Branching Elder
		qe.registerQuestNpc(820009).addOnTalkEvent(questId); // Leafy Elder
		qe.registerQuestNpc(651860).addOnKillEvent(questId);
		qe.registerQuestItem(182216250, questId);
		qe.addHandlerSideQuestDrop(questId, 651860, 182216249, 1, 100);
		qe.registerOnLevelUp(questId);
		qe.registerOnEnterWorld(questId);
	}

	@Override
	public boolean onEnterWorldEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null) {
			env.setQuestId(questId);
			QuestService.startQuest(env);
		}
		return false;
	}

	@Override
	public boolean onDialogEvent(final QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null) {
			return false;
		}

		int targetId = env.getTargetId();
		DialogAction action = env.getDialog();

		if (qs != null && qs.getStatus() == QuestStatus.START) {
			if (targetId == 730007) {
				switch (action) {
				case QUEST_SELECT:
					return sendQuestDialog(env, 1011);
				case SELECT_ACTION_1012:
					return sendQuestDialog(env, 1012);
				case SETPRO1:
					qs.setQuestVar(1);
					updateQuestStatus(env);
					return closeDialogWindow(env);
				default:
					break;
				}
			} else if (targetId == 820007) {
				switch (action) {
				case QUEST_SELECT:
					return sendQuestDialog(env, 1352);
				case SELECT_ACTION_1353:
					return sendQuestDialog(env, 1353);
				case SELECT_ACTION_1354:
					return sendQuestDialog(env, 1354);
				case SETPRO2:
					qs.setQuestVar(2);
					updateQuestStatus(env);
					return closeDialogWindow(env);
				default:
					break;
				}
			} else if (targetId == 820008) {
				switch (action) {
				case QUEST_SELECT:
					return sendQuestDialog(env, 1693);
				case SELECT_ACTION_1693:
					return sendQuestDialog(env, 1693);
				case SETPRO3:
					qs.setQuestVar(3);
					updateQuestStatus(env);
					return closeDialogWindow(env);
				default:
					break;
				}
			} else if (targetId == 820009) {
				switch (action) {
				case QUEST_SELECT:
					return sendQuestDialog(env, 2034);
				case CHECK_USER_HAS_QUEST_ITEM:
					return checkQuestItems(env, 3, 4, false, 10000, 10001);
				case SELECT_ACTION_2375:
					return sendQuestDialog(env, 2375);
				case SETPRO5:
					qs.setQuestVar(5);
					updateQuestStatus(env);
					ItemService.addItem(player, 182216250, 1);
					return closeDialogWindow(env);
				default:
					break;
				}
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 730008) {
				switch (action) {
				case USE_OBJECT:
					return sendQuestDialog(env, 10002);
				case SELECT_QUEST_REWARD:
					return sendQuestDialog(env, 5);
				case SELECTED_QUEST_NOREWARD:
					return sendQuestEndDialog(env);
				default:
					break;
				}
			}

		}
		return false;
	}

	@Override
	public HandlerResult onItemUseEvent(final QuestEnv env, final Item item) {
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);

		if (item.getItemId() != 182216250) {
			return HandlerResult.FAILED;
		}

		if (qs != null && qs.getStatus() == QuestStatus.START) {
			PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), item.getObjectId(), item.getItemId(), 3000, 0, 0), true);
			ThreadPoolManager.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), item.getObjectId(), item.getItemId(), 0, 1, 0), true);
					removeQuestItem(env, 182216250, 1);
					TeleportService2.teleportTo(player, 210010000, 498f, 1531.2f, 104.7877f, (byte) 40, TeleportAnimation.BEAM_ANIMATION);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
				}
			}, 3000);
		}
		return HandlerResult.SUCCESS;
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env);
	}
}
