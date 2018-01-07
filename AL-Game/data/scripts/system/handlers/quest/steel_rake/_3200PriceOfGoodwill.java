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
package quest.steel_rake;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.TeleportAnimation;
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
import com.aionemu.gameserver.services.instance.InstanceService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldMapInstance;

/**
 * @author kecimis
 * @modified apozema
 */
public class _3200PriceOfGoodwill extends QuestHandler {

	private final static int questId = 3200;
	private final static int[] npc_ids = { 204658, 798332, 700522, 279006, 798322 };

	public _3200PriceOfGoodwill() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(204658).addOnQuestStart(questId);
		qe.registerQuestItem(182209082, questId);
		for (int npc_id : npc_ids) {
			qe.registerQuestNpc(npc_id).addOnTalkEvent(questId);
		}
	}

	@Override
	public boolean onDialogEvent(final QuestEnv env) {
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);

		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc) {
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 204658) { // Roikinerk
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 4762);
				}
				else {
					return sendQuestStartDialog(env);
				}

			}
			return false;
		}

		int var = qs.getQuestVarById(0);

		if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 798322) { // Kuruminerk
				if (env.getDialog() == DialogAction.USE_OBJECT) {
					return sendQuestDialog(env, 10002);
				}
				else if (env.getDialogId() == DialogAction.SELECT_QUEST_REWARD.id()) {
					return sendQuestDialog(env, 5);
				}
				else {
					return sendQuestEndDialog(env);
				}
			}
			return false;

		}
		else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 204658) { // Roikinerk
				switch (env.getDialog()) {
					case QUEST_SELECT:
						return sendQuestDialog(env, 1003);
					case SELECT_ACTION_1011:
						return sendQuestDialog(env, 1011);
					case SETPRO1:
						WorldMapInstance newInstance = InstanceService.getNextAvailableInstance(300100000);
						InstanceService.registerPlayerWithInstance(newInstance, player);
						TeleportService2.teleportTo(player, 300100000, newInstance.getInstanceId(), 408.45093f, 508.07065f, 885.7603f, (byte) 31, TeleportAnimation.BEAM_ANIMATION);
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						return true;
					default:
						break;
				}
			}

		}
		else if (targetId == 798332 && var == 1) { // Haorunerk
			switch (env.getDialog()) {
				case QUEST_SELECT:
					return sendQuestDialog(env, 1352);
				case SELECT_ACTION_1353:
					playQuestMovie(env, 431);
					break;
				case SETPRO2:
					qs.setQuestVarById(0, var + 1);
					updateQuestStatus(env);
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
					return true;
				default:
					break;
			}
		}
		else if (targetId == 700522 && var == 2) { // Haorunerks Bag
			ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					updateQuestStatus(env);
				}
			}, 3000);
			return true;
		}
		else if (targetId == 279006 && var == 3) { // Garkbinerk
			switch (env.getDialog()) {
				case QUEST_SELECT:
					return sendQuestDialog(env, 2034);
				case SET_SUCCEED:
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					return true;
				default:
					break;
			}
		}
		return false;
	}

	@Override
	public HandlerResult onItemUseEvent(final QuestEnv env, Item item) {
		final Player player = env.getPlayer();
		final int id = item.getItemTemplate().getTemplateId();
		final int itemObjId = item.getObjectId();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);

		if (id != 182209082 || qs == null || qs.getQuestVarById(0) != 2) {
			return HandlerResult.UNKNOWN;
		}

		PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 3000, 0, 0), true);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 0, 0, 1), true);
				removeQuestItem(env, 182209082, 1);
				TeleportService2.teleportTo(player, 400010000, 3411.0608f, 2419.0928f, 2763.1975f, (byte) 0, TeleportAnimation.BEAM_ANIMATION);
				qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
				updateQuestStatus(env);
			}
		}, 3000);
		return HandlerResult.SUCCESS;
	}
}
