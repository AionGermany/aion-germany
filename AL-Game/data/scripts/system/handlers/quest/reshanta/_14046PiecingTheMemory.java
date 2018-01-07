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
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/**
 * @author pralinka
 * @rework FrozenKiller (Fixed QE: exception in onMovieEnd)
 */
public class _14046PiecingTheMemory extends QuestHandler {

	private final static int questId = 14046;
	private final static int[] npc_ids = { 278500, 203834, 203786, 203754, 203704 };

	public _14046PiecingTheMemory() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerOnEnterZoneMissionEnd(questId);
		qe.registerOnLevelUp(questId);
		qe.registerQuestItem(182215354, questId);
		qe.registerOnMovieEndQuest(170, questId);
		for (int npc_id : npc_ids) {
			qe.registerQuestNpc(npc_id).addOnTalkEvent(questId);
		}
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
			if (targetId == 203704) {
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
		if (targetId == 278500) {
			switch (env.getDialog()) {
				case QUEST_SELECT:
					if (var == 0) {
						return sendQuestDialog(env, 1011);
					}
				case SETPRO1:
					if (var == 0) {
						return defaultCloseDialog(env, 0, 1);
					}
				default:
					break;
			}
		}
		else if (targetId == 203834) {
			switch (env.getDialog()) {
				case QUEST_SELECT:
					if (var == 1) {
						return sendQuestDialog(env, 1352);
					}
					else if (var == 3) {
						return sendQuestDialog(env, 2034);
					}
					else if (var == 5) {
						return sendQuestDialog(env, 2716);
					}
				case SELECT_ACTION_1353:
					playQuestMovie(env, 102);
					break;
				case SETPRO2:
					if (var == 1) {
						return defaultCloseDialog(env, 1, 2);
					}
				case SETPRO4:
					if (var == 3) {
						return defaultCloseDialog(env, 3, 4);
					}
				case SETPRO6:
					if (var == 5) {
						removeQuestItem(env, 182215354, 1);
						return defaultCloseDialog(env, 5, 6);
					}
				default:
					break;
			}
		}
		else if (targetId == 203786) {
			switch (env.getDialog()) {
				case QUEST_SELECT:
					if (var == 2) {
						return sendQuestDialog(env, 1693);
					}
				case CHECK_USER_HAS_QUEST_ITEM:
					return checkQuestItems(env, 2, 3, false, 10000, 10001, 182215354, 1);
				default:
					break;
			}
		}
		else if (targetId == 203754) {
			switch (env.getDialog()) {
				case QUEST_SELECT:
					if (var == 6) {
						return sendQuestDialog(env, 3057);
					}
				case SET_SUCCEED:
					if (var == 6) {
						return defaultCloseDialog(env, 6, 6, true, false);
					}
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

		if (id != 182215354) {
			return HandlerResult.UNKNOWN;
		}

		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() != QuestStatus.START || qs.getQuestVarById(0) != 4) {
			return HandlerResult.UNKNOWN;
		}

		PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 1000, 0, 0), true);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 0, 0, 1), true);
				playQuestMovie(env, 170);
			}
		}, 1000);
		return HandlerResult.SUCCESS;
	}

	@Override
	public boolean onMovieEndEvent(QuestEnv env, int movieId) {
		if (movieId == 170) {
			int var = env.getPlayer().getQuestStateList().getQuestState(questId).getQuestVarById(0);
			if (var == 4) {
				changeQuestStep(env, 4, 5, false); // 5
				return true;
			}
		}
		return false;
	}
}
