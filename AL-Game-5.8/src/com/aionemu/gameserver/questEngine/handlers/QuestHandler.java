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
package com.aionemu.gameserver.questEngine.handlers;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import com.aionemu.gameserver.ai2.event.AIEventType;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.DialogPage;
import com.aionemu.gameserver.model.EmotionId;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.QuestTemplate;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.model.templates.quest.QuestDrop;
import com.aionemu.gameserver.model.templates.quest.QuestItems;
import com.aionemu.gameserver.model.templates.quest.QuestWorkItems;
import com.aionemu.gameserver.model.templates.quest.XMLStartCondition;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_NPC_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUEST_ACTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.questEngine.QuestEngine;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.questEngine.task.QuestTasks;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.services.item.ItemPacketService.ItemAddType;
import com.aionemu.gameserver.services.item.ItemPacketService.ItemUpdateType;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author MrPoke
 * @modified vlog
 */
public abstract class QuestHandler extends AbstractQuestHandler implements ConstantSpawnHandler {

	protected final int questId;
	protected QuestEngine qe;
	protected List<QuestItems> workItems;
	protected HashSet<Integer> actionItems;
	protected HashSet<Integer> constantSpawns;

	/**
	 * Create a new QuestHandler object
	 */
	protected QuestHandler(int questId) {
		this.questId = questId;
		this.qe = QuestEngine.getInstance();
		loadWorkItems();
		loadActionItems();
		onWorkItemsLoaded();
	}

	private void loadWorkItems() {
		QuestTemplate template = DataManager.QUEST_DATA.getQuestById(questId);
		if (template == null) {
			return; // Some artificial quests have dummy questIds
		}
		QuestWorkItems qwi = DataManager.QUEST_DATA.getQuestById(questId).getQuestWorkItems();
		if (qwi == null) {
			return;
		}
		workItems = qwi.getQuestWorkItem();
	}

	private void loadActionItems() {
		QuestTemplate template = DataManager.QUEST_DATA.getQuestById(questId);
		if (template == null) {
			return; // Some artificial quests have dummy questIds
		}
		List<QuestDrop> qDrop = DataManager.QUEST_DATA.getQuestById(questId).getQuestDrop();
		if (qDrop == null) {
			return;
		}
		for (QuestDrop drop : qDrop) {
			if (drop.getNpcId() / 100000 != 7) {
				continue;
			}
			if (actionItems == null) {
				actionItems = new HashSet<>();
			}
			actionItems.add(drop.getNpcId());
		}
	}

	/**
	 * Override it to clear them if not used or log details
	 */
	protected void onWorkItemsLoaded() {
	}

	/**
	 * Update the status of the quest in player's journal
	 */
	public synchronized void updateQuestStatus(QuestEnv env) {
		sendUpdatePacket(env);
	}

	public void changeQuestStep(QuestEnv env, int step, int nextStep, boolean reward) {
		changeQuestStep(env, step, nextStep, reward, 0);
	}

	/**
	 * Change the quest step to the next step or set quest status to reward
	 */
	public void changeQuestStep(QuestEnv env, int step, int nextStep, boolean reward, int varNum) {
		QuestState qs = env.getPlayer().getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getQuestVarById(varNum) == step) {
			if (reward) { // ignore nextStep
				qs.setStatus(QuestStatus.REWARD);
			}
			else { // quest can be rolled back if nextStep < step
				if (nextStep != step) {
					qs.setQuestVarById(varNum, nextStep);
				}
			}
			if (reward || nextStep != step) {
				updateQuestStatus(env);
			}
		}
	}

	/**
	 * Send dialog to the player
	 */
	public boolean sendQuestDialog(QuestEnv env, int dialogId) {
		boolean isExploitDialog = false;
		if (DialogPage.getPageByAction(dialogId) != null) {
			switch (DialogPage.getPageByAction(dialogId)) {
				case SELECT_QUEST_REWARD_WINDOW1:
				case SELECT_QUEST_REWARD_WINDOW2:
				case SELECT_QUEST_REWARD_WINDOW3:
				case SELECT_QUEST_REWARD_WINDOW4:
				case SELECT_QUEST_REWARD_WINDOW5:
				case SELECT_QUEST_REWARD_WINDOW6:
				case SELECT_QUEST_REWARD_WINDOW7:
				case SELECT_QUEST_REWARD_WINDOW8:
				case SELECT_QUEST_REWARD_WINDOW9:
				case SELECT_QUEST_REWARD_WINDOW10:
					isExploitDialog = true;
					break;
				default:
					break;
			}
		}
		if (isExploitDialog) { // reward packet exploitation fix
			Player player = env.getPlayer();
			QuestState qs = player.getQuestStateList().getQuestState(questId);
			if (qs == null || qs.getStatus() != QuestStatus.REWARD) {
				return false;
			}
		}
		sendDialogPacket(env, dialogId);
		return true;
	}

	public boolean sendQuestSelectionDialog(QuestEnv env) {
		sendQuestSelectionPacket(env, 10);
		return true;
	}

	public boolean closeDialogWindow(QuestEnv env) {
		sendQuestSelectionPacket(env, 0);
		return true;
	}

	public boolean sendQuestStartDialog(QuestEnv env) {
		return sendQuestStartDialog(env, 0, 0, 0);
	}

	public boolean sendQuestStartDialog(QuestEnv env, int step) {
		return sendQuestStartDialog(env, 0, 0, step);
	}

	public boolean sendQuestStartDialog(QuestEnv env, int itemId, int itemCount) {
		return sendQuestStartDialog(env, itemId, itemCount, 0);
	}

	/**
	 * Send default start quest dialog and start it (give the item on start)
	 */
	public boolean sendQuestStartDialog(QuestEnv env, int itemId, int itemCount, int step) {
		switch (env.getDialog()) {
			case ASK_QUEST_ACCEPT: {
				return sendQuestDialog(env, 4);
			}
			case QUEST_ACCEPT_1: {
				if (itemId != 0 && itemCount != 0) {
					if (!env.getPlayer().getInventory().isFullSpecialCube()) {
						if (QuestService.startQuest(env, step)) {
							giveQuestItem(env, itemId, itemCount);
							return sendQuestDialog(env, 1003);
						}
					}
				}
				else {
					if (QuestService.startQuest(env, step)) {
						if (env.getVisibleObject() == null || env.getVisibleObject() instanceof Player) {
							return closeDialogWindow(env);
						}
						else {
							return sendQuestDialog(env, 1003);
						}
					}
				}
			}
			case QUEST_ACCEPT_SIMPLE: {
				if (itemId != 0 && itemCount != 0) {
					if (!env.getPlayer().getInventory().isFullSpecialCube()) {
						if (QuestService.startQuest(env, step)) {
							giveQuestItem(env, itemId, itemCount);
							return closeDialogWindow(env);
						}
					}
				}
				else {
					if (QuestService.startQuest(env, step)) {
						if (env.getVisibleObject() == null || env.getVisibleObject() instanceof Player) {
							return closeDialogWindow(env);
						}
						else {
							return closeDialogWindow(env);
						}
					}
				}
			}
			case QUEST_REFUSE_1:
			case QUEST_REFUSE_2: {
				return sendQuestDialog(env, 1004);
			}
			case QUEST_REFUSE_SIMPLE: {
				return closeDialogWindow(env);
			}
			case FINISH_DIALOG: {
				return sendQuestSelectionDialog(env);
			}
			default:
				break;
		}
		return false;
	}

	/**
	 * Remove all quest items and send and finish the quest
	 */
	public boolean sendQuestEndDialog(QuestEnv env, int[] questItemsToRemove) {
		Player player = env.getPlayer();
		for (int item : questItemsToRemove) {
			long count = player.getInventory().getItemCountByItemId(item);
			if (count > 0) {
				player.getInventory().decreaseByItemId(item, count);
			}
		}
		return sendQuestEndDialog(env);
	}

	/**
	 * Send completion dialog of the quest and finish it. Give the default reward from quest_data.xml
	 */
	public boolean sendQuestEndDialog(QuestEnv env) {
		return sendQuestEndDialog(env, 0);
	}

	/**
	 * Send completion dialog of the quest and finish it
	 *
	 * @param env
	 * @param reward
	 *            The index of the List<Reward>.
	 */
	public boolean sendQuestEndDialog(QuestEnv env, int reward) {
		Player player = env.getPlayer();
		int dialogId = env.getDialogId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (dialogId >= DialogAction.SELECTED_QUEST_REWARD1.id() && dialogId <= DialogAction.SELECTED_QUEST_NOREWARD.id()) {
			if (qs == null || qs.getStatus() != QuestStatus.REWARD) {
				return false; // reward packet exploitation fix
			}
			if (QuestService.finishQuest(env, reward)) {
				Npc npc = (Npc) env.getVisibleObject();
				if ("useitem".equals(npc.getAi2().getName()) || ("quest_use_item".equals(npc.getAi2().getName()))) {
					return closeDialogWindow(env);
				}
				else {
					return closeDialogWindow(env);
				}
			}
			return false;
		}
		else if (dialogId == DialogAction.SELECT_QUEST_REWARD.id() || dialogId == DialogAction.USE_OBJECT.id()) {
			if (qs != null && qs.getStatus() == QuestStatus.REWARD) {
				return sendQuestDialog(env, 5 + reward);
			}
		}
		return false;
	}

	public boolean defaultCloseDialog(QuestEnv env, int step, int nextStep) {
		return defaultCloseDialog(env, step, nextStep, false, false, 0, 0, 0, 0, 0);
	}

	public boolean defaultCloseDialog(QuestEnv env, int step, int nextStep, int varNum) {
		return defaultCloseDialog(env, step, nextStep, false, false, 0, 0, 0, 0, 0, varNum);
	}

	public boolean defaultCloseDialog(QuestEnv env, int step, int nextStep, boolean reward, boolean sameNpc) {
		return defaultCloseDialog(env, step, nextStep, reward, sameNpc, 0, 0, 0, 0, 0);
	}

	public boolean defaultCloseDialog(QuestEnv env, int step, int nextStep, boolean reward, boolean sameNpc, int rewardId) {
		return defaultCloseDialog(env, step, nextStep, reward, sameNpc, rewardId, 0, 0, 0, 0);
	}

	public boolean defaultCloseDialog(QuestEnv env, int step, int nextStep, int giveItemId, int giveItemCount, int removeItemId, int removeItemCount) {
		return defaultCloseDialog(env, step, nextStep, false, false, 0, giveItemId, giveItemCount, removeItemId, removeItemCount);
	}

	public boolean defaultCloseDialog(QuestEnv env, int step, int nextStep, boolean reward, boolean sameNpc, int giveItemId, int giveItemCount, int removeItemId, int removeItemCount) {
		return defaultCloseDialog(env, step, nextStep, reward, sameNpc, 0, giveItemId, giveItemCount, removeItemId, removeItemCount);
	}

	public boolean defaultCloseDialog(QuestEnv env, int step, int nextStep, boolean reward, boolean sameNpc, int rewardId, int giveItemId, int giveItemCount, int removeItemId, int removeItemCount) {
		return defaultCloseDialog(env, step, nextStep, reward, sameNpc, rewardId, giveItemId, giveItemCount, removeItemId, removeItemCount, 0);
	}

	/**
	 * Handle on close dialog event, changing the quest status and giving/removing quest items
	 */
	public boolean defaultCloseDialog(QuestEnv env, int step, int nextStep, boolean reward, boolean sameNpc, int rewardId, int giveItemId, int giveItemCount, int removeItemId, int removeItemCount, int varNum) {
		QuestState qs = env.getPlayer().getQuestStateList().getQuestState(questId);
		if (qs.getQuestVarById(0) == step) {
			if (giveItemId != 0 && giveItemCount != 0) {
				if (!giveQuestItem(env, giveItemId, giveItemCount)) {
					return false;
				}
			}
			removeQuestItem(env, removeItemId, removeItemCount, qs.getStatus());
			changeQuestStep(env, step, nextStep, reward, varNum);
			if (sameNpc) {
				return sendQuestEndDialog(env, rewardId);
			}
			Npc npc = (Npc) env.getVisibleObject();
			if ("useitem".equals(npc.getAi2().getName())) {
				return closeDialogWindow(env);
			}
			else {
				return sendQuestSelectionDialog(env);
			}
		}
		return false;
	}

	public boolean checkQuestItems(QuestEnv env, int step, int nextStep, boolean reward, int checkOkId, int checkFailId) {
		return checkQuestItems(env, step, nextStep, reward, checkOkId, checkFailId, 0, 0);
	}

	/**
	 * Check if the player has quest item, listed in the quest_data.xml in his inventory
	 */
	public boolean checkQuestItems(QuestEnv env, int step, int nextStep, boolean reward, int checkOkId, int checkFailId, int giveItemId, int giveItemCount) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs.getQuestVarById(0) == step) {
			if (QuestService.collectItemCheck(env, true)) {
				if (giveItemId != 0 && giveItemCount != 0) {
					if (!giveQuestItem(env, giveItemId, giveItemCount)) {
						return false;
					}
				}
				changeQuestStep(env, step, nextStep, reward);
				return sendQuestDialog(env, checkOkId);
			}
			else {
				return sendQuestDialog(env, checkFailId);
			}
		}
		return false;
	}

	/**
	 * Check if the player has quest item (simple version), listed in the quest_data.xml in his inventory
	 */
	public boolean checkQuestItemsSimple(QuestEnv env, int step, int nextStep, boolean reward, int checkOkId, int giveItemId, int giveItemCount) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs.getQuestVarById(0) == step) {
			if (QuestService.collectItemCheck(env, true)) {
				if (giveItemId != 0 && giveItemCount != 0) {
					if (!giveQuestItem(env, giveItemId, giveItemCount)) {
						return false;
					}
				}
				changeQuestStep(env, step, nextStep, reward);
				return sendQuestDialog(env, checkOkId);
			}
			else {
				return closeDialogWindow(env);
			}
		}
		return false;
	}

	/**
	 * To use for checking the items, not listed in the collect_items in the quest_data.xml
	 */
	public boolean checkItemExistence(QuestEnv env, int step, int nextStep, boolean reward, int itemId, int itemCount, boolean remove, int checkOkId, int checkFailId, int giveItemId, int giveItemCount) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs.getQuestVarById(0) == step) {
			if (checkItemExistence(env, itemId, itemCount, remove)) {
				if (giveItemId != 0 && giveItemCount != 0) {
					if (!giveQuestItem(env, giveItemId, giveItemCount)) {
						return false;
					}
				}
				changeQuestStep(env, step, nextStep, reward);
				return sendQuestDialog(env, checkOkId);
			}
			else {
				return sendQuestDialog(env, checkFailId);
			}
		}
		return false;
	}

	/**
	 * Check, if item exists in the player's inventory and probably remove it
	 */
	public boolean checkItemExistence(QuestEnv env, int itemId, int itemCount, boolean remove) {
		Player player = env.getPlayer();
		if (player.getInventory().getItemCountByItemId(itemId) >= itemCount) {
			if (remove) {
				if (!removeQuestItem(env, itemId, itemCount)) {
					return false;
				}
			}
			return true;
		}
		else {
			return false;
		}
	}

	public void sendEmotion(QuestEnv env, Creature emoteCreature, EmotionId emotion, boolean broadcast) {
		Player player = env.getPlayer();
		int targetId = player.equals(emoteCreature) ? env.getVisibleObject().getObjectId() : player.getObjectId();

		// TODO: fix it, broadcast and direction sometimes do not work when the emoteCreature is NPC
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(emoteCreature, EmotionType.EMOTE, emotion.id(), targetId), broadcast);
	}

	/**
	 * Give the quest item to player's inventory
	 */
	public boolean giveQuestItem(QuestEnv env, int itemId, long itemCount) {
		return giveQuestItem(env, itemId, itemCount, ItemAddType.QUEST_WORK_ITEM, ItemUpdateType.INC_ITEM_COLLECT);
	}

	public boolean giveQuestItem(QuestEnv env, int itemId, long itemCount, ItemAddType addType) {
		return giveQuestItem(env, itemId, itemCount, addType, ItemUpdateType.INC_ITEM_COLLECT);
	}

	public boolean giveQuestItem(QuestEnv env, int itemId, long itemCount, ItemAddType addType, ItemUpdateType updateType) {
		Player player = env.getPlayer();
		ItemTemplate item = DataManager.ITEM_DATA.getItemTemplate(itemId);
		if (itemId != 0 && itemCount != 0) {
			long existentItemCount = player.getInventory().getItemCountByItemId(itemId);
			if (existentItemCount < itemCount) {
				long itemsToGive = itemCount - existentItemCount;
				ItemService.ItemUpdatePredicate predicate = new ItemService.ItemUpdatePredicate(addType, updateType);
				return ItemService.addQuestItems(player, Collections.singletonList(new QuestItems(itemId, itemsToGive)), predicate);
			}
			else {
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CAN_NOT_GET_LORE_ITEM((new DescriptionId(item.getNameId()))));
				return true;
			}
		}
		return false;
	}

	/**
	 * Remove the quest item from player's inventory
	 */
	public boolean removeQuestItem(QuestEnv env, int itemId, long itemCount) {
		Player player = env.getPlayer();
		if (itemId != 0 && itemCount != 0) {
			QuestState qs = player.getQuestStateList().getQuestState(questId);
			return player.getInventory().decreaseByItemId(itemId, itemCount, qs == null ? QuestStatus.START : qs.getStatus());
		}
		return false;
	}

	public boolean removeQuestItem(QuestEnv env, int itemId, long itemCount, QuestStatus questStatus) {
		Player player = env.getPlayer();
		if (itemId != 0 && itemCount != 0) {
			return player.getInventory().decreaseByItemId(itemId, itemCount, questStatus);
		}
		return false;
	}

	/**
	 * Play movie with given ID
	 */
	public boolean playQuestMovie(QuestEnv env, int MovieId) {
		Player player = env.getPlayer();
		PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, MovieId));
		return false;
	}

	/**
	 * Play movie with given ID and Type (0 or 1)
	 */
	public boolean playQuestMovie(QuestEnv env, int Type, int MovieId) {
		Player player = env.getPlayer();
		PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(Type, MovieId));
		return false;
	}

	/**
	 * For single kill
	 */
	public boolean defaultOnKillEvent(QuestEnv env, int npcId, int startVar, int endVar) {
		int[] mobids = { npcId };
		return defaultOnKillEvent(env, mobids, startVar, endVar);
	}

	/**
	 * For multiple kills
	 */
	public boolean defaultOnKillEvent(QuestEnv env, int[] npcIds, int startVar, int endVar) {
		return defaultOnKillEvent(env, npcIds, startVar, endVar, 0);
	}

	/**
	 * For single kill on another QuestVar
	 */
	public boolean defaultOnKillEvent(QuestEnv env, int npcId, int startVar, int endVar, int varNum) {
		int[] mobids = { npcId };
		return defaultOnKillEvent(env, mobids, startVar, endVar, varNum);
	}

	/**
	 * Handle onKill event
	 */
	public boolean defaultOnKillEvent(QuestEnv env, int[] npcIds, int startVar, int endVar, int varNum) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(varNum);
			int targetId = env.getTargetId();
			for (int id : npcIds) {
				if (targetId == id) {
					if (var >= startVar && var < endVar) {
						qs.setQuestVarById(varNum, var + 1);
						updateQuestStatus(env);
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * For single kill and reward status after it
	 */
	public boolean defaultOnKillEvent(QuestEnv env, int npcId, int startVar, boolean reward) {
		int[] mobids = { npcId };
		return (defaultOnKillEvent(env, mobids, startVar, reward, 0));
	}

	/**
	 * For single kill on another QuestVar and reward status after it
	 */
	public boolean defaultOnKillEvent(QuestEnv env, int npcId, int startVar, boolean reward, int varNum) {
		int[] mobids = { npcId };
		return (defaultOnKillEvent(env, mobids, startVar, reward, varNum));
	}

	/**
	 * For multiple kills and reward status after it
	 */
	public boolean defaultOnKillEvent(QuestEnv env, int[] npcIds, int startVar, boolean reward) {
		return (defaultOnKillEvent(env, npcIds, startVar, reward, 0));
	}

	/**
	 * Handle onKill event with reward status
	 */
	public boolean defaultOnKillEvent(QuestEnv env, int[] npcIds, int startVar, boolean reward, int varNum) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(varNum);
			int targetId = env.getTargetId();
			for (int id : npcIds) {
				if (targetId == id) {
					if (var == startVar) {
						if (reward) {
							qs.setStatus(QuestStatus.REWARD);
						}
						else {
							qs.setQuestVarById(varNum, var + 1);
						}
						updateQuestStatus(env);
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Handle onKillPlayer event
	 */
	public boolean defaultOnKillRankedEvent(QuestEnv env, int startVar, int endVar, boolean reward) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (var >= startVar && var < (endVar - 1)) {
				changeQuestStep(env, var, var + 1, false);
				return true;
			}
			else if (var == (endVar - 1)) {
				if (reward) {
					qs.setStatus(QuestStatus.REWARD);
				}
				else {
					qs.setQuestVarById(0, var + 1);
				}
				updateQuestStatus(env);
				return true;
			}
		}
		return false;
	}

	public boolean defaultOnUseSkillEvent(QuestEnv env, int startVar, int endVar, int varNum) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(varNum);
			if (var >= startVar && var < endVar) {
				changeQuestStep(env, var, var + 1, false, varNum);
				return true;
			}
		}
		return false;
	}

	/**
	 * NPC starts following the player to the target. Use onLostTarget and onReachTarget for further actions.
	 */
	public boolean defaultStartFollowEvent(QuestEnv env, Npc follower, int targetNpcId, int step, int nextStep) {
		final Player player = env.getPlayer();
		if (!(env.getVisibleObject() instanceof Npc)) {
			return false;
		}
		PacketSendUtility.sendPacket(player, new SM_NPC_INFO(follower, player));
		follower.getAi2().onCreatureEvent(AIEventType.FOLLOW_ME, player);
		player.getController().addTask(TaskId.QUEST_FOLLOW, QuestTasks.newFollowingToTargetCheckTask(env, follower, targetNpcId));
		if (step == 0 && nextStep == 0) {
			return true;
		}
		else {
			return defaultCloseDialog(env, step, nextStep);
		}
	}

	/**
	 * NPC starts following the player to the target location. Use onLostTarget and onReachTarget for further actions.
	 */
	public boolean defaultStartFollowEvent(QuestEnv env, Npc follower, float x, float y, float z, int step, int nextStep) {
		final Player player = env.getPlayer();
		if (!(env.getVisibleObject() instanceof Npc)) {
			return false;
		}
		PacketSendUtility.sendPacket(player, new SM_NPC_INFO(follower, player));
		follower.getAi2().onCreatureEvent(AIEventType.FOLLOW_ME, player);
		player.getController().addTask(TaskId.QUEST_FOLLOW, QuestTasks.newFollowingToTargetCheckTask(env, follower, x, y, z));
		if (step == 0 && nextStep == 0) {
			return true;
		}
		else {
			return defaultCloseDialog(env, step, nextStep);
		}
	}

	public boolean defaultStartFollowEvent(QuestEnv env, Npc follower, ZoneName zonename, int step, int nextStep) {
		final Player player = env.getPlayer();
		if (!(env.getVisibleObject() instanceof Npc)) {
			return false;
		}
		PacketSendUtility.sendPacket(player, new SM_NPC_INFO(follower, player));
		follower.getAi2().onCreatureEvent(AIEventType.FOLLOW_ME, player);
		player.getController().addTask(TaskId.QUEST_FOLLOW, QuestTasks.newFollowingToTargetCheckTask(env, follower, zonename));
		if (step == 0 && nextStep == 0) {
			return true;
		}
		else {
			return defaultCloseDialog(env, step, nextStep);
		}
	}

	public boolean defaultStartFollowEvent(QuestEnv env, Npc follower, ZoneName zone1, ZoneName zone2, int step, int nextStep) {
		final Player player = env.getPlayer();
		if (!(env.getVisibleObject() instanceof Npc)) {
			return false;
		}
		PacketSendUtility.sendPacket(player, new SM_NPC_INFO(follower, player));
		follower.getAi2().onCreatureEvent(AIEventType.FOLLOW_ME, player);
		player.getController().addTask(TaskId.QUEST_FOLLOW, QuestTasks.newFollowingToTargetCheckTask(env, follower, zone1, zone2));
		if (step == 0 && nextStep == 0) {
			return true;
		}
		else {
			return defaultCloseDialog(env, step, nextStep);
		}
	}

	/**
	 * NPC stops following the player. Used in both onLostTargetEvent and onReachTargetEvent.
	 */
	public boolean defaultFollowEndEvent(QuestEnv env, int step, int nextStep, boolean reward, int movie) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			if (qs.getQuestVarById(0) == step) {
				changeQuestStep(env, step, nextStep, reward);
				if (movie != 0) {
					playQuestMovie(env, movie);
				}
				return true;
			}
		}
		return false;
	}

	public boolean defaultFollowEndEvent(QuestEnv env, int step, int nextStep, boolean reward) {
		return defaultFollowEndEvent(env, step, nextStep, reward, 0);
	}

	/**
	 * Changing quest step on getting item
	 */
	public boolean defaultOnGetItemEvent(QuestEnv env, int step, int nextStep, boolean reward) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			if (qs.getQuestVarById(0) == step) {
				changeQuestStep(env, step, nextStep, reward);
				return true;
			}
		}
		return false;
	}

	public boolean useQuestObject(QuestEnv env, int step, int nextStep, boolean reward, boolean die) {
		return useQuestObject(env, step, nextStep, reward, 0, 0, 0, 0, 0, 0, die);
	}

	public boolean useQuestObject(QuestEnv env, int step, int nextStep, boolean reward, int varNum, boolean die) {
		return useQuestObject(env, step, nextStep, reward, varNum, 0, 0, 0, 0, 0, die);
	}

	public boolean useQuestObject(QuestEnv env, int step, int nextStep, boolean reward, int varNum) {
		return useQuestObject(env, step, nextStep, reward, varNum, 0, 0, 0, 0, 0, false);
	}

	public boolean useQuestObject(QuestEnv env, int step, int nextStep, boolean reward, int varNum, int addItemId, int addItemCount) {
		return useQuestObject(env, step, nextStep, reward, varNum, addItemId, addItemCount, 0, 0, 0, false);
	}

	public boolean useQuestObject(QuestEnv env, int step, int nextStep, boolean reward, int varNum, int addItemId, int addItemCount, int removeItemId, int removeItemCount) {
		return useQuestObject(env, step, nextStep, reward, varNum, addItemId, addItemCount, removeItemId, removeItemCount, 0, false);
	}

	public boolean useQuestObject(QuestEnv env, int step, int nextStep, boolean reward, int varNum, int movieId) {
		return useQuestObject(env, step, nextStep, reward, varNum, 0, 0, 0, 0, movieId, false);
	}

	/**
	 * Handle use object event
	 */
	public boolean useQuestObject(QuestEnv env, int step, int nextStep, boolean reward, int varNum, int addItemId, int addItemCount, int removeItemId, int removeItemCount, int movieId, boolean dieObject) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null) {
			return false;
		}
		if (qs.getQuestVarById(varNum) == step) {
			if (addItemId != 0 && addItemCount != 0) {
				if (!giveQuestItem(env, addItemId, addItemCount)) {
					return false;
				}
			}
			if (removeItemId != 0 && removeItemCount != 0) {
				removeQuestItem(env, removeItemId, removeItemCount);
			}
			if (movieId != 0) {
				playQuestMovie(env, movieId);
			}
			if (dieObject) {
				Npc npc = (Npc) player.getTarget();
				if (npc == null || npc.getObjectId() != env.getVisibleObject().getObjectId()) {
					return false;
				}
				npc.getController().onDie(player);
			}
			changeQuestStep(env, step, nextStep, reward, varNum);
			return true;
		}
		return false;
	}

	public boolean useQuestItem(QuestEnv env, Item item, int step, int nextStep, boolean reward) {
		return useQuestItem(env, item, step, nextStep, reward, 0, 0, 0);
	}

	public boolean useQuestItem(QuestEnv env, Item item, int step, int nextStep, boolean reward, final int addItemId, final int addItemCount) {
		return useQuestItem(env, item, step, nextStep, reward, addItemId, addItemCount, 0);
	}

	public boolean useQuestItem(QuestEnv env, Item item, int step, int nextStep, boolean reward, int movieId) {
		return useQuestItem(env, item, step, nextStep, reward, 0, 0, movieId);
	}

	public boolean useQuestItem(final QuestEnv env, final Item item, final int step, final int nextStep, final boolean reward, final int addItemId, final int addItemCount, final int movieId) {
		return useQuestItem(env, item, step, nextStep, reward, addItemId, addItemCount, movieId, 0);
	}

	/**
	 * Handle use item event
	 */
	public boolean useQuestItem(final QuestEnv env, final Item item, final int step, final int nextStep, final boolean reward, final int addItemId, final int addItemCount, final int movieId, final int varNum) {
		final Player player = env.getPlayer();
		if (player == null) {
			return false;
		}
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null) {
			return false;
		}
		final int itemId = item.getItemId();
		final int objectId = item.getObjectId();

		if (qs.getQuestVarById(varNum) == step) {
			PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), 0, objectId, itemId, 3000, 0), true);
			ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), 0, objectId, itemId, 0, 1), true);
					removeQuestItem(env, itemId, 1);

					if (addItemId != 0 && addItemCount != 0) {
						if (!giveQuestItem(env, addItemId, addItemCount)) {
							return;
						}
					}
					if (movieId != 0) {
						playQuestMovie(env, movieId);
					}
					changeQuestStep(env, step, nextStep, reward, varNum);
				}
			}, 3000);
			return true;
		}
		return false;
	}

	/**
	 * For missions after on enter zone mission complete without preconditions
	 */
	public boolean defaultOnZoneMissionEndEvent(QuestEnv env) {
		int[] quests = { 0 };
		return defaultOnZoneMissionEndEvent(env, quests);
	}

	/**
	 * For missions after on enter zone mission complete with one precondition
	 */
	public boolean defaultOnZoneMissionEndEvent(QuestEnv env, int quest) {
		int[] quests = { quest };
		return defaultOnZoneMissionEndEvent(env, quests);
	}

	/**
	 * Check requirements and starts or lock mission after completing the onEnterZone mission. Should only be used from onEnterZone missions handler! Will be called only once for every on zone mission
	 * end quest
	 */
	public boolean defaultOnZoneMissionEndEvent(QuestEnv env, int[] quests) {
		Player player = env.getPlayer();
		env.setQuestId(questId);
		QuestState qs = player.getQuestStateList().getQuestState(questId);

		// Only null quests can be started!
		if (qs != null) {
			return false;
		}

		// Check all player requirements
		if (!QuestService.checkMissionStatConditions(env)) {
			return false;
		}

		// Check, if the player has required level
		if (!QuestService.checkLevelRequirement(questId, player.getCommonData().getLevel())) {
			QuestService.startMission(env, QuestStatus.LOCKED);
			return false;
		}

		// Check the quests, that has to be done before starting this one
		for (int id : quests) {
			if (id != 0) {
				QuestState qs2 = player.getQuestStateList().getQuestState(id);
				if (qs2 == null || qs2.getStatus() != QuestStatus.COMPLETE) {
					QuestService.startMission(env, QuestStatus.LOCKED);
					return false;
				}
			}
		}

		// Check other start conditions, listed in the quest_data
		// Zone missions should be already LOCKED before!
		QuestTemplate template = DataManager.QUEST_DATA.getQuestById(env.getQuestId());
		for (XMLStartCondition startCondition : template.getXMLStartConditions()) {
			if (!startCondition.check(player, false)) {
				if (qs == null) {
					QuestService.startMission(env, QuestStatus.LOCKED);
				}
				return false;
			}
		}

		// All conditions are done. Start the quest
		QuestService.startMission(env, QuestStatus.START);
		return true;
	}

	/**
	 * For normal missions (not zone missions) without preconditions
	 */
	public boolean defaultOnLvlUpEvent(QuestEnv env) {
		int[] quests = { 0 };
		return defaultOnLvlUpEvent(env, quests, false);
	}

	/**
	 * For normal missions with one precondition
	 */
	public boolean defaultOnLvlUpEvent(QuestEnv env, int quest) {
		int[] quests = { quest };
		return defaultOnLvlUpEvent(env, quests, false);
	}

	/**
	 * For zone missions with one precondition
	 */
	public boolean defaultOnLvlUpEvent(QuestEnv env, int quest, boolean isZoneMission) {
		int[] quests = { quest };
		return defaultOnLvlUpEvent(env, quests, isZoneMission);
	}

	/**
	 * Check the mission starting conditions on the level up
	 *
	 * @param env
	 * @param quests
	 *            The quests to be completed before starting this one
	 * @return true if successfully started
	 */
	public boolean defaultOnLvlUpEvent(QuestEnv env, int[] quests, boolean isZoneMission) {
		Player player = env.getPlayer();
		env.setQuestId(questId);
		QuestState qs = player.getQuestStateList().getQuestState(questId);

		// Only null and LOCKED quests can be started
		if (qs != null && qs.getStatus() != QuestStatus.LOCKED) {
			return false;
		}

		// Check all player requirements
		if (!QuestService.checkMissionStatConditions(env)) {
			return false;
		}

		// Check, if the player has required level
		if (!QuestService.checkLevelRequirement(questId, player.getCommonData().getLevel())) {
			return false;
		}

		// Check the quests, that has to be done before starting this one
		// Set the quest status to LOCKED, if these requirements aren't there
		// Zone missions should be already LOCKED before!
		// TEMPORARY till the new quest_data will be parsed
		for (int id : quests) {
			if (id != 0) {
				QuestState qs2 = player.getQuestStateList().getQuestState(id);
				if (qs2 == null || qs2.getStatus() != QuestStatus.COMPLETE) {
					if (qs == null && !isZoneMission) {
						QuestService.startMission(env, QuestStatus.LOCKED);
					}
					return false;
				}
			}
		}

		// Check other start conditions, listed in the quest_data
		// Zone missions should be already LOCKED before!
		QuestTemplate template = DataManager.QUEST_DATA.getQuestById(env.getQuestId());
		for (XMLStartCondition startCondition : template.getXMLStartConditions()) {
			if (!startCondition.check(player, false)) {
				if (qs == null && !isZoneMission) {
					QuestService.startMission(env, QuestStatus.LOCKED);
				}
				return false;
			}
		}

		// All conditions are done. Start the quest
		if (qs == null) {
			QuestService.startMission(env, QuestStatus.START);
		}
		else {
			qs.setStatus(QuestStatus.START);
			updateQuestStatus(env);
		}

		return true;
	}

	/**
	 * Start a mission on enter the questZone
	 */
	public boolean defaultOnEnterZoneEvent(QuestEnv env, ZoneName currentZoneName, ZoneName questZoneName) {
		if (questZoneName == currentZoneName) {
			Player player = env.getPlayer();
			if (player == null) {
				return false;
			}
			QuestState qs = player.getQuestStateList().getQuestState(questId);
			if (qs == null) {
				env.setQuestId(questId);
				if (QuestService.startQuest(env)) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean sendQuestRewardDialog(QuestEnv env, int rewardNpcId, int reportDialogId) {
		return sendQuestRewardDialog(env, rewardNpcId, reportDialogId, 0);
	}

	public boolean sendQuestRewardDialog(QuestEnv env, int rewardNpcId, int reportDialogId, int rewardId) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs.getStatus() == QuestStatus.REWARD) {
			if (env.getTargetId() == rewardNpcId) {
				if (env.getDialog() == DialogAction.USE_OBJECT && reportDialogId != 0) {
					return sendQuestDialog(env, reportDialogId);
				}
				else {
					return sendQuestEndDialog(env, rewardId);
				}
			}
		}
		return false;
	}

	public boolean sendQuestNoneDialog(QuestEnv env, int startNpcId) {
		QuestTemplate template = DataManager.QUEST_DATA.getQuestById(questId);
		return sendQuestNoneDialog(env, template, startNpcId, 1011);
	}

	public boolean sendQuestNoneDialog(QuestEnv env, int startNpcId, int dialogId) {
		QuestTemplate template = DataManager.QUEST_DATA.getQuestById(questId);
		return sendQuestNoneDialog(env, template, startNpcId, dialogId);
	}

	public boolean sendQuestNoneDialog(QuestEnv env, QuestTemplate template, int startNpcId, int dialogId) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
			if (env.getTargetId() == startNpcId) {
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, dialogId);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		return false;
	}

	public boolean sendQuestNoneDialog(QuestEnv env, int startNpcId, int dialogId, int itemId, int itemCout) {
		QuestTemplate template = DataManager.QUEST_DATA.getQuestById(questId);
		return sendQuestNoneDialog(env, template, startNpcId, dialogId, itemId, itemCout);
	}

	public boolean sendQuestNoneDialog(QuestEnv env, int startNpcId, int itemId, int itemCout) {
		QuestTemplate template = DataManager.QUEST_DATA.getQuestById(questId);
		return sendQuestNoneDialog(env, template, startNpcId, 1011, itemId, itemCout);
	}

	public boolean sendQuestNoneDialog(QuestEnv env, QuestTemplate template, int startNpcId, int dialogId, int itemId, int itemCout) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
			if (env.getTargetId() == startNpcId) {
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, dialogId);
				}
				if (itemId != 0 && itemCout != 0) {
					if (env.getDialog() == DialogAction.QUEST_ACCEPT_1) {
						if (giveQuestItem(env, itemId, itemCout)) {
							return sendQuestStartDialog(env);
						}
						else {
							return true;
						}
					}
					else {
						return sendQuestStartDialog(env);
					}
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		return false;
	}

	public boolean sendItemCollectingStartDialog(QuestEnv env) {
		switch (env.getDialog()) {
			case QUEST_ACCEPT_1: {
				QuestService.startQuest(env);
				return sendQuestSelectionDialog(env);
			}
			case QUEST_REFUSE_1: {
				return sendQuestSelectionDialog(env);
			}
			default:
				break;
		}
		return false;
	}

	@Override
	public int getQuestId() {
		return questId;
	}

	private void sendUpdatePacket(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(questId, qs.getStatus(), qs.getQuestVars().getQuestVars()));
		if (qs.getStatus() == QuestStatus.COMPLETE || qs.getStatus() == QuestStatus.REWARD) {
			player.getController().updateNearbyQuests();
		}
	}

	private void sendDialogPacket(QuestEnv env, int dialogId) {
		int objId = 0;
		if (env.getVisibleObject() != null) {
			objId = env.getVisibleObject().getObjectId();
		}
		// Not using questId, because some quests may handle events when quests are finished
		// In that case questId must be zero!!! (Kromede entry for example)
		PacketSendUtility.sendPacket(env.getPlayer(), new SM_DIALOG_WINDOW(objId, dialogId, env.getQuestId()));
	}

	private void sendQuestSelectionPacket(QuestEnv env, int dialogId) {
		int objId = 0;
		if (env.getVisibleObject() != null) {
			objId = env.getVisibleObject().getObjectId();
		}
		PacketSendUtility.sendPacket(env.getPlayer(), new SM_DIALOG_WINDOW(objId, dialogId));
	}

	public void changeQuestStepRew(QuestEnv env, int varNumbr, int varRew) {
		QuestState qs = env.getPlayer().getQuestStateList().getQuestState(questId);
		qs.setStatus(QuestStatus.REWARD);
		qs.setQuestVarById(varNumbr, varRew);
		updateQuestStatus(env);
	}

	/**
	 * @see com.aionemu.gameserver.questEngine.handlers.AbstractQuestHandler#register()
	 */
	@Override
	public abstract void register();

	@Override
	public HashSet<Integer> getNpcIds() {
		return null;
	}
}
