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
package quest.balaur_marching_route;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author Phantom_KNA
 */
public class _27505BoostfortheBalaurMarchingRoute extends QuestHandler {

	public static final int questId = 27505;

	public _27505BoostfortheBalaurMarchingRoute() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestItem(182216068, questId); // Shugo Potion
		qe.registerQuestNpc(806268).addOnQuestStart(questId); // Baura
		qe.registerQuestNpc(806271).addOnTalkEvent(questId); // Togorinerk
		qe.registerQuestNpc(806269).addOnTalkEvent(questId); // Genga
		qe.registerQuestNpc(806268).addOnTalkEvent(questId); // Togorinerk
		qe.registerOnEnterWorld(questId);
		qe.registerOnEnterZone(ZoneName.get("BALAUR_MARCHING_ROUTE_301670000"), questId);
		qe.registerOnEnterZone(ZoneName.get("SOUTH_GUARD_POST_3_301670000"), questId);
	}

	@Override
	public boolean onDialogEvent(final QuestEnv env) {
		final Player player = env.getPlayer();
		int targetId = env.getTargetId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();

		if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
			if (targetId == 806268) { // Baura
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 4762);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (targetId == 806271) { // Togorinerk
				switch (dialog) {
					case QUEST_SELECT: {
						if (var == 0) {
							return sendQuestDialog(env, 1011);
						}
					}
					case SETPRO1: {
						qs.setQuestVar(1);
						updateQuestStatus(env);
						giveQuestItem(env, 182216068, 1);
						return closeDialogWindow(env);
					}
					default:
						break;
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (targetId == 806269) { // Genga
				switch (dialog) {
					case QUEST_SELECT: {
						if (var == 3) {
							return sendQuestDialog(env, 2034); // Var - Problem in the client
						}
					}
					case SETPRO4: {
						qs.setQuestVar(4);
						updateQuestStatus(env);
						return closeDialogWindow(env);
					}
					default:
						break;
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 806268) {
				if (dialog == DialogAction.USE_OBJECT) {
					return sendQuestDialog(env, 10002);
				}
				else {
					return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}

	@Override
	public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName) {
		Player player = env.getPlayer();
		if (player == null) {
			return false;
		}

		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (var == 1 && zoneName == ZoneName.get("BALAUR_MARCHING_ROUTE_301670000")) {
				changeQuestStep(env, 1, 2, false);
				return true;
			}
			else if (var == 4 && zoneName == ZoneName.get("SOUTH_GUARD_POST_3_301670000")) {
				qs.setQuestVar(5);
				qs.setStatus(QuestStatus.REWARD);
				updateQuestStatus(env);
				return true;
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

		if (id != 182216068) {
			return HandlerResult.UNKNOWN;
		}

		PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 20, 1, 0), true);
		int var = qs.getQuestVarById(0);
		if (qs.getStatus() == QuestStatus.START && var == 2)
			qs.setQuestVar(4); // Var original = 3
		updateQuestStatus(env);
		return HandlerResult.SUCCESS;
	}
}
