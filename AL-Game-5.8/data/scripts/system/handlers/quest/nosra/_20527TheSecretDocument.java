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
package quest.nosra;

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
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author Phantom_KNA
 */
public class _20527TheSecretDocument extends QuestHandler {

	public static final int questId = 20527;

	public _20527TheSecretDocument() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestItem(182216087, questId);
		qe.registerOnLevelUp(questId);
		qe.registerQuestNpc(806079).addOnTalkEvent(questId); // Peregran
		qe.registerQuestNpc(703321).addOnTalkEvent(questId); // Tree tendrill
		qe.registerQuestNpc(806296).addOnTalkEvent(questId); // Wizabo
		qe.registerQuestNpc(731711).addOnTalkEvent(questId); // Fragmento de torre
		qe.registerQuestNpc(703322).addOnTalkEvent(questId); // Caldero de cocina
		qe.registerQuestNpc(731712).addOnTalkEvent(questId); // Fragmento de Torre Claro
		qe.registerQuestNpc(703323).addOnTalkEvent(questId); // Saco de Ceriales
		qe.registerOnEnterZone(ZoneName.get("FOREST_OF_ILLUSIONS_220110000"), questId);
		qe.registerOnEnterZone(ZoneName.get("ZENZEN_TERRITORY_220110000"), questId);
		qe.registerOnEnterZone(ZoneName.get("FEATHER_BRANCH_FOREST_220110000"), questId);
		qe.registerQuestNpc(244123).addOnKillEvent(questId);
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
		DialogAction dialog = env.getDialog();
		if (env.getVisibleObject() instanceof Npc) {
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 806079: { // Peregrans
					switch (env.getDialog()) {
						case QUEST_SELECT:
							if (var == 0) {
								return sendQuestDialog(env, 1011);
							}
							else if (var == 2) {
								return sendQuestDialog(env, 1693);
								// } else if (var == 12) {
								// return sendQuestDialog(env, 4100);
							}
						case SETPRO1:
							return defaultCloseDialog(env, 0, 1); // 1
						case SETPRO3:
							ItemService.addItem(player, 182216087, 1);
							return defaultCloseDialog(env, 2, 3); // 1
						case CHECK_USER_HAS_QUEST_ITEM: {
							return checkQuestItems(env, 12, 13, false, 10000, 10001); // 4
						}
						default:
							break;
					}
				}
					break;
				case 806296: { // Wizabo
					switch (env.getDialog()) {
						case QUEST_SELECT:
							if (var == 1) {
								return sendQuestDialog(env, 1352);
							}
						case SETPRO2:
							return defaultCloseDialog(env, 1, 2); // 2
						default:
							break;
					}
				}
					break;
				case 703321: { // Saco de Ceriales
					switch (dialog) {
						case USE_OBJECT:
							if (var == 4) {
								return defaultCloseDialog(env, 4, 5);
							}
						default:
							break;
					}
				}
				case 731711: { // Fragmento de Torre brillnate
					switch (dialog) {
						case USE_OBJECT:
							if (var == 5) {
								return sendQuestDialog(env, 2716);
							}
						case SETPRO6:
							return defaultCloseDialog(env, 5, 6);
						default:
							break;
					}
				}
					break;
				case 703322: { // Caldero de cocina
					switch (dialog) {
						case USE_OBJECT:
							if (var == 7) {
								return defaultCloseDialog(env, 7, 8);
							}
						default:
							break;
					}
				}
				case 703323: { // Saco de Ceriales
					switch (dialog) {
						case USE_OBJECT:
							if (var == 10) {
								return defaultCloseDialog(env, 10, 11);
							}
						default:
							break;
					}
				}
					break;
				case 731712: { // Fragmento de Torre Claro
					switch (dialog) {
						case USE_OBJECT:
							if (var == 8) {
								return sendQuestDialog(env, 3739);
							}
						case SETPRO9:
							return defaultCloseDialog(env, 8, 9);
						default:
							break;
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 806079) {
				if (env.getDialog() == DialogAction.USE_OBJECT) {
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
			if (var == 3 && zoneName == ZoneName.get("FOREST_OF_ILLUSIONS_220110000")) {
				changeQuestStep(env, 3, 4, false);
				return true;
			}
			else if (var == 6 && zoneName == ZoneName.get("ZENZEN_TERRITORY_220110000")) {
				changeQuestStep(env, 6, 7, false);
				return true;
			}
			else if (var == 9 && zoneName == ZoneName.get("FEATHER_BRANCH_FOREST_220110000")) {
				changeQuestStep(env, 9, 10, false);
				return true;
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
		if (qs == null) {
			return HandlerResult.UNKNOWN;
		}
		final int var = qs.getQuestVarById(0);
		if (var == 14 && id == 182216087) {
			PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 3000, 0, 0), true);
			ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 0, 0, 1), true);
					qs.setQuestVar(15);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
				}
			}, 3000);
			return HandlerResult.SUCCESS;
		}
		return HandlerResult.UNKNOWN;
	}

	@Override
	public boolean onKillEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() != QuestStatus.START)
			return false;

		int targetId = env.getTargetId();

		switch (targetId) {
			case 244123:
				if (qs.getQuestVarById(1) != 6) {
					qs.setQuestVarById(1, qs.getQuestVarById(1) + 1);
					updateQuestStatus(env);
				}
				else {
					qs.setQuestVar(14); // TODO var 12 - 13 - 14
					// qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
				}
		}
		return false;
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env);
	}
}
