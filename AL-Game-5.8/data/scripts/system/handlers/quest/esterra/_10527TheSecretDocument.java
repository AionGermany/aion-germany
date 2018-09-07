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
package quest.esterra;

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
public class _10527TheSecretDocument extends QuestHandler {

	public static final int questId = 10527;

	public _10527TheSecretDocument() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestItem(182216075, questId);
		qe.registerOnLevelUp(questId);
		qe.registerQuestNpc(806075).addOnTalkEvent(questId); // Weda
		qe.registerQuestNpc(703313).addOnTalkEvent(questId); // Liane
		qe.registerQuestNpc(806291).addOnTalkEvent(questId); // Dezabo
		qe.registerQuestNpc(731705).addOnTalkEvent(questId); // Fragmento de Torre Luminoso
		qe.registerQuestNpc(703314).addOnTalkEvent(questId); // Caldero
		qe.registerQuestNpc(731706).addOnTalkEvent(questId); // Fragmento de Torre Claro
		qe.registerQuestNpc(703315).addOnTalkEvent(questId); // Saco de Materiales
		qe.registerOnEnterZone(ZoneName.get("FOREST_OF_LIFE_210100000"), questId);
		qe.registerOnEnterZone(ZoneName.get("TARHA_VILLAGE_210100000"), questId);
		qe.registerOnEnterZone(ZoneName.get("GORGE_OF_FALLACY_210100000"), questId);
		qe.registerQuestNpc(244107).addOnKillEvent(questId);
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
				case 806075: { // Weda
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
							ItemService.addItem(player, 182216075, 1);
							return defaultCloseDialog(env, 2, 3); // 1
						case CHECK_USER_HAS_QUEST_ITEM: {
							return checkQuestItems(env, 12, 13, false, 10000, 10001); // 4
						}
						default:
							break;
					}
				}
					break;
				case 806291: { // Dezabo
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
				case 703313: { // Liane
					switch (dialog) {
						case USE_OBJECT:
							if (var == 4) {
								return defaultCloseDialog(env, 4, 5);
							}
						default:
							break;
					}
				}
				case 731705: { // Fragmento de Torre Luminoso
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
				case 703314: { // Caldero
					switch (dialog) {
						case USE_OBJECT:
							if (var == 7) {
								return defaultCloseDialog(env, 7, 8);
							}
						default:
							break;
					}
				}
				case 703315: { // Saco de Materiales
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
				case 731706: { // Fragmento de Torre Claro
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
			if (targetId == 806075) {
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
			if (var == 3 && zoneName == ZoneName.get("FOREST_OF_LIFE_210100000")) {
				changeQuestStep(env, 3, 4, false);
				return true;
			}
			else if (var == 6 && zoneName == ZoneName.get("TARHA_VILLAGE_210100000")) {
				changeQuestStep(env, 6, 7, false);
				return true;
			}
			else if (var == 9 && zoneName == ZoneName.get("GORGE_OF_FALLACY_210100000")) {
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
		if (var == 14 && id == 182216075) {
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
			case 244107:
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
