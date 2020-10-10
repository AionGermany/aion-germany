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
package quest.ishalgen;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.Race;
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
 * @author Falke_34
 */
public class _70006WheresRae extends QuestHandler {

	private final static int questId = 70006;

	public _70006WheresRae() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(203534).addOnTalkEvent(questId); // Dabi
		qe.registerQuestNpc(790002).addOnTalkEvent(questId); // Verdandi
		qe.registerQuestNpc(806813).addOnTalkEvent(questId); // Rae
		qe.registerQuestNpc(651860).addOnKillEvent(questId);
		qe.registerQuestItem(182216399, questId);
		qe.addHandlerSideQuestDrop(questId, 651774, 182216396, 1, 100);
		qe.addHandlerSideQuestDrop(questId, 651775, 182216396, 1, 100);
		qe.addHandlerSideQuestDrop(questId, 651777, 182216397, 1, 100);
		qe.addHandlerSideQuestDrop(questId, 651778, 182216397, 1, 100);
		qe.addHandlerSideQuestDrop(questId, 651779, 182216398, 1, 100);
		qe.registerOnLevelUp(questId);
		qe.registerOnEnterWorld(questId);
		qe.registerOnMovieEndQuest(52, questId);
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env);
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
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null) {
			return false;
		}

		int targetId = env.getTargetId();
		DialogAction action = env.getDialog();
		int var = qs.getQuestVars().getQuestVars();

		if (qs != null && qs.getStatus() == QuestStatus.START) {
			if (targetId == 203534) {
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
			} 
			else if (targetId == 790002) {
				switch (action) {
				case QUEST_SELECT:
					if (var == 2) {
						return sendQuestDialog(env, 1693);
					} else if (var == 3) {
						return sendQuestDialog(env, 2034);
					} else if (var == 5) {
						return sendQuestDialog(env, 2716);
					}
					return sendQuestDialog(env, 1352);
				case SELECT_ACTION_1353:
					return sendQuestDialog(env, 1353);
				case SELECT_ACTION_1354:
					return sendQuestDialog(env, 1354);
				case SELECT_ACTION_2034:
					return sendQuestDialog(env, 2035);
				case SELECT_ACTION_2716:
					playQuestMovie(env, 1, 3);
					return sendQuestDialog(env, 2717);
				case SETPRO4:
					qs.setQuestVar(4);
					updateQuestStatus(env);
					ItemService.addItem(player, 182216399, 1);
					return closeDialogWindow(env);
				case SETPRO2:
					qs.setQuestVar(2);
					updateQuestStatus(env);
					return closeDialogWindow(env);
				case CHECK_USER_HAS_QUEST_ITEM:
					return checkQuestItems(env, 2, 3, false, 10000, 10001);
				case SET_SUCCEED:
					qs.setQuestVar(7);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					TeleportService2.teleportTo(player, 220010000, 951.5539f, 1696.2922f, 259.75f, (byte) 40, TeleportAnimation.BEAM_ANIMATION);
					return closeDialogWindow(env);
				default:
					break;

				}
			}
		} 
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 806813) {
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

		if (item.getItemId() != 182216399) {
			return HandlerResult.FAILED;
		}

		if (qs != null && qs.getStatus() == QuestStatus.START) {
			PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), item.getObjectId(), item.getItemId(), 3000, 0, 0), true);
			ThreadPoolManager.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), item.getObjectId(), item.getItemId(), 0, 1, 0), true);
					removeQuestItem(env, 182216399, 1);
					qs.setQuestVar(5);
					updateQuestStatus(env);
				}
			}, 3000);
		}
		return HandlerResult.SUCCESS;
	}

	@Override
	public boolean onMovieEndEvent(QuestEnv env, int movieId) {
		if (movieId != 3) {
			return false;
		}
		Player player = env.getPlayer();
		if (player.getCommonData().getRace() != Race.ASMODIANS) {
			return false;
		}
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() != QuestStatus.START) {
			return false;
		}
		qs.setQuestVar(6);
		updateQuestStatus(env);
		return true;
	}
}
