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

import com.aionemu.gameserver.model.DialogAction;
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
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author Mariella
 */
public class _14153AStormOfVengeance extends QuestHandler {

	private final static int questId = 14153;

	public _14153AStormOfVengeance() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerOnMovieEndQuest(193, questId);
		qe.registerQuestItem(182215459, questId);
		qe.registerQuestNpc(204504).addOnQuestStart(questId); // Sofne
		qe.registerQuestNpc(204505).addOnTalkEvent(questId);
		qe.registerQuestNpc(204533).addOnTalkEvent(questId);
		qe.registerQuestNpc(204535).addOnTalkEvent(questId);
		qe.registerQuestNpc(700282).addOnTalkEvent(questId);
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
			if (targetId == 204504) { // Sofne
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 4762);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}

		if (qs == null) {
			return false;
		}

		int var = qs.getQuestVarById(0);

		if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 204505) {
				return sendQuestEndDialog(env);
			}
		}
		else if (qs.getStatus() != QuestStatus.START) {
			return false;
		}
		if (targetId == 204505) {
			switch (env.getDialog()) {
				case QUEST_SELECT:
					if (var == 0) {
						return sendQuestDialog(env, 1011);
					}
				case SETPRO1:
					if (var == 0) {
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
					return false;
				default:
					break;
			}
		}
		else if (targetId == 204533) {
			switch (env.getDialog()) {
				case QUEST_SELECT:
					if (var == 1) {
						return sendQuestDialog(env, 1352);
					}
					else if (var == 3) {
						return sendQuestDialog(env, 2034);
					}
				case SETPRO2:
					if (var == 1) {
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
				case SETPRO4:
					if (var == 3) {
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
					return false;
				default:
					break;
			}
		}
		else if (targetId == 204535) {
			switch (env.getDialog()) {
				case QUEST_SELECT:
					if (var == 4) {
						return sendQuestDialog(env, 2375);
					}
				case SETPRO5:
					if (var == 4) {
						if (!giveQuestItem(env, 182215459, 1)) {
							return true;
						}
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
					return false;
				default:
					break;
			}
		}
		else if (targetId == 700282 && var == 2) {
			if (env.getDialog() == DialogAction.USE_OBJECT) {
				return playQuestMovie(env, 193);
			}
		}
		return false;
	}

	@Override
	public boolean onMovieEndEvent(QuestEnv env, int movieId) {
		if (movieId != 193) {
			return false;
		}
		final Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() != QuestStatus.START || qs.getQuestVars().getQuestVars() != 2) {
			return false;
		}
		qs.setQuestVar(3);
		updateQuestStatus(env);

		// apply transformation, TODO figure out how long should effect stay on
		SkillEngine.getInstance().applyEffectDirectly(1864, player, player, 15000);

		return true;
	}

	@Override
	public HandlerResult onItemUseEvent(final QuestEnv env, Item item) {
		final Player player = env.getPlayer();
		final int id = item.getItemTemplate().getTemplateId();
		final int itemObjId = item.getObjectId();

		if (id != 182215459) {
			return HandlerResult.UNKNOWN;
		}
		if (!player.isInsideZone(ZoneName.get("LF3_ITEMUSEAREA_Q1059"))) {
			return HandlerResult.UNKNOWN;
		}
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() == QuestStatus.COMPLETE) {
			return HandlerResult.UNKNOWN;
		}
		PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 3000, 0, 0), true);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 0, 0, 1), true);
				playQuestMovie(env, 192);
				removeQuestItem(env, 182215459, 1);
				qs.setQuestVarById(0, 5);
				qs.setStatus(QuestStatus.REWARD);
				updateQuestStatus(env);
			}
		}, 3000);
		return HandlerResult.SUCCESS;
	}
}
