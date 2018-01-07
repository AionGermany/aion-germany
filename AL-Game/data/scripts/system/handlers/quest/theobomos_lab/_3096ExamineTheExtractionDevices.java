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
package quest.theobomos_lab;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.RewardType;
import com.aionemu.gameserver.model.templates.quest.Rewards;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUEST_ACTION;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Balthazar
 */
public class _3096ExamineTheExtractionDevices extends QuestHandler {

	private final static int questId = 3096;

	public _3096ExamineTheExtractionDevices() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(798225).addOnQuestStart(questId);
		qe.registerQuestNpc(798225).addOnTalkEvent(questId);
		qe.registerQuestNpc(700423).addOnTalkEvent(questId);
		qe.registerQuestNpc(700424).addOnTalkEvent(questId);
		qe.registerQuestNpc(700425).addOnTalkEvent(questId);
		qe.registerQuestNpc(700426).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);

		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc) {
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}

		if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.getStatus() == QuestStatus.COMPLETE) {
			if (targetId == 798225) {
				switch (env.getDialog()) {
					case QUEST_SELECT: {
						return sendQuestDialog(env, 1011);
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
			switch (targetId) {
				case 798225: {
					switch (env.getDialog()) {
						case QUEST_SELECT: {
							long itemCount1 = player.getInventory().getItemCountByItemId(182208067);
							long itemCount2 = player.getInventory().getItemCountByItemId(182208068);
							long itemCount3 = player.getInventory().getItemCountByItemId(182208069);
							long itemCount4 = player.getInventory().getItemCountByItemId(182208070);
							if (itemCount1 >= 1 && itemCount2 >= 1 && itemCount3 >= 1 && itemCount4 >= 1) {
								return sendQuestDialog(env, 5);
							}
						}
						case SELECTED_QUEST_NOREWARD: {
							qs.setStatus(QuestStatus.COMPLETE);
							qs.setCompleteCount(qs.getCompleteCount() + 1);
							removeQuestItem(env, 182208067, 1);
							removeQuestItem(env, 182208068, 1);
							removeQuestItem(env, 182208069, 1);
							removeQuestItem(env, 182208070, 1);
							Rewards rewards = DataManager.QUEST_DATA.getQuestById(questId).getRewards().get(0);
							int rewardExp = rewards.getExp();
							int rewardKinah = (int) (player.getRates().getQuestKinahRate() * rewards.getGold());
							giveQuestItem(env, 182400001, rewardKinah);
							player.getCommonData().addExp(rewardExp, RewardType.QUEST);
							PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(questId, QuestStatus.COMPLETE, 2));
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
							updateQuestStatus(env);
							return true;
						}
						default:
							break;
					}
				}
				case 700423: {
					switch (env.getDialog()) {
						case USE_OBJECT: {
							if (player.getInventory().getItemCountByItemId(182208067) < 1) {
								return true;
							}
						}
						default:
							break;
					}
				}
				case 700424: {
					switch (env.getDialog()) {
						case USE_OBJECT: {
							if (player.getInventory().getItemCountByItemId(182208068) < 1) {
								return true;
							}
						}
						default:
							break;
					}
				}
				case 700425: {
					switch (env.getDialog()) {
						case USE_OBJECT: {
							if (player.getInventory().getItemCountByItemId(182208069) < 1) {
								return true;
							}
						}
						default:
							break;
					}
				}
				case 700426: {
					switch (env.getDialog()) {
						case USE_OBJECT: {
							if (player.getInventory().getItemCountByItemId(182208070) < 1) {
								return true;
							}
						}
						default:
							break;
					}
				}
			}
		}
		return false;
	}
}
