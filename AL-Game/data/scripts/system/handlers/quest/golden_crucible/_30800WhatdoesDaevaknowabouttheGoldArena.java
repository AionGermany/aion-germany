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
package quest.golden_crucible;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Phantom_KNA
 */
public class _30800WhatdoesDaevaknowabouttheGoldArena extends QuestHandler {

	private final static int questId = 30800;

	public _30800WhatdoesDaevaknowabouttheGoldArena() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestItem(182216169, questId);
		qe.registerOnEnterWorld(questId);
		qe.registerOnLevelUp(questId);
		qe.registerQuestNpc(834986).addOnTalkEvent(questId); // Peronerk, remove AI: dummy
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

		if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 834986) { // Peronerk
				switch (dialog) {
					case USE_OBJECT: {
						return sendQuestDialog(env, 10002);
					}
					case SELECT_QUEST_REWARD: {
						return sendQuestDialog(env, 5);
					}
					default:
						return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}

	@Override
	public HandlerResult onItemUseEvent(QuestEnv env, Item item) {
		final Player player = env.getPlayer();
		final int id = item.getItemTemplate().getTemplateId();
		final int itemObjId = item.getObjectId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);

		if (id != 182216169) { // What does Daeva know about the Gold Arena, ak?
			return HandlerResult.UNKNOWN;
		}

		PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 20, 1, 0), true);
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			QuestService.startQuest(env);
		}
		else if (qs.getStatus() == QuestStatus.START)
			qs.setQuestVar(1);
		qs.setStatus(QuestStatus.REWARD);
		updateQuestStatus(env);
		return HandlerResult.SUCCESS;
	}

	@Override
	public boolean onEnterWorldEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.NONE && player.getInventory().getItemCountByItemId(182216169) < 1 && player.getWorldId() == 302320000) { // Golden Crucible
			return giveQuestItem(env, 182216169, 1); // What does Daeva know about the Gold Arena, ak?
		}
		return false;
	}
}
