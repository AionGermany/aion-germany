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

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.RewardType;
import com.aionemu.gameserver.model.templates.quest.Rewards;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUEST_ACTION;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldMapType;

/**
 * @author Balthazar
 */
public class _1640TeleporterRepairs extends QuestHandler {

	private final static int questId = 1640;

	public _1640TeleporterRepairs() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(730033).addOnQuestStart(questId);
		qe.registerQuestNpc(730033).addOnTalkEvent(questId);
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
			if (targetId == 730033) {
				switch (env.getDialog()) {
					case QUEST_SELECT: {
						return sendQuestDialog(env, 1011);
					}
					case SETPRO1: {
						QuestService.startQuest(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(0, 0));
						return true;
					}
					default:
						return sendQuestStartDialog(env);
				}
			}
		}

		if (qs == null) {
			return false;
		}

		if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 730033 && env.getDialog() == DialogAction.USE_OBJECT && player.getInventory().getItemCountByItemId(182201790) >= 1) {
				final int targetObjectId = env.getVisibleObject().getObjectId();
				PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.SIT, 0, targetObjectId), true);
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						if (!player.isTargeting(targetObjectId)) {
							return;
						}

						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
					}
				}, 3000);
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 730033) {
				removeQuestItem(env, 182201790, 1);

				if (qs == null || qs.getStatus() != QuestStatus.REWARD) {
					return false;
				}
				Rewards rewards = DataManager.QUEST_DATA.getQuestById(questId).getRewards().get(0);
				int rewardExp = rewards.getExp();
				int rewardKinah = (int) (player.getRates().getQuestKinahRate() * rewards.getGold());
				player.getCommonData().addExp(rewardExp, RewardType.QUEST);
				giveQuestItem(env, 182400001, rewardKinah);
				qs.setStatus(QuestStatus.COMPLETE);
				qs.setCompleteCount(255);
				updateQuestStatus(env);
				PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(questId, QuestStatus.COMPLETE, 2));
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(0, 0));
				return true;
			}
		}
		else if (qs.getStatus() == QuestStatus.COMPLETE) {
			ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					TeleportService2.teleportTo(player, WorldMapType.HEIRON.getId(), 187.71689f, 2712.14870f, 141.91672f, (byte) 195);
				}
			}, 1000);
		}
		return false;
	}
}
