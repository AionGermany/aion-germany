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
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author Phantom_KNA
 */
public class _20526TheSecretOrder extends QuestHandler {

	public static final int questId = 20526;

	public _20526TheSecretOrder() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerOnLevelUp(questId);
		qe.registerQuestNpc(806079).addOnTalkEvent(questId); // Peregran
		qe.registerQuestNpc(806394).addOnTalkEvent(questId); // Barota
		qe.registerQuestNpc(806296).addOnTalkEvent(questId); // Wizabo
		qe.registerQuestNpc(806297).addOnTalkEvent(questId); // Awakened Wizabo
		qe.registerQuestNpc(703318).addOnTalkEvent(questId); // Strange Aether Piece
		qe.registerQuestNpc(703319).addOnTalkEvent(questId); // Mysterious Aether Piece
		qe.registerQuestNpc(703320).addOnTalkEvent(questId); // Suspicious Aether Piece
		qe.registerOnEnterZone(ZoneName.get("TOWER_OF_ETERNITY_1_220120000"), questId); // Tower of Eternity
		qe.registerOnEnterZone(ZoneName.get("220110000"), questId); // Nosra
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
						case SETPRO1:
							return defaultCloseDialog(env, 0, 1); // 1
						default:
							break;
					}
				}
					break;
				case 806394: { // Barota
					switch (env.getDialog()) {
						case QUEST_SELECT:
							if (var == 2) {
								return sendQuestDialog(env, 1693);
							}
						case SETPRO3:
							return defaultCloseDialog(env, 2, 3); // 3
						default:
							break;
					}
				}
					break;
				case 806296: { // Wizabo
					switch (env.getDialog()) {
						case QUEST_SELECT:
							if (var == 3) {
								return sendQuestDialog(env, 2034);
							}
						case SETPRO4:
							return defaultCloseDialog(env, 3, 4); // 4
						default:
							break;
					}
				}
					break;
				case 806297: { // Awakened Dezabo
					switch (env.getDialog()) {
						case QUEST_SELECT:
							if (var == 5) {
								return sendQuestDialog(env, 2716);
							}
							else if (var == 7) {
								return sendQuestDialog(env, 3398);
							}
							else if (var == 9) {
								return sendQuestDialog(env, 4080);
							}
							else if (var == 11) {
								return sendQuestDialog(env, 6841);
							}
						case SETPRO6:
							TeleportService2.teleportTo(player, 220110000, 1394f, 2895f, 253f, (byte) 15, TeleportAnimation.BEAM_ANIMATION);
							return defaultCloseDialog(env, 5, 6); // 6
						case SETPRO8:
							return defaultCloseDialog(env, 7, 8); // 8
						case SETPRO10:
							return defaultCloseDialog(env, 9, 10); // 10
						case SET_SUCCEED:
							removeQuestItem(env, 164002348, 1);
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
							return defaultCloseDialog(env, 11, 12); // 12
						default:
							break;
					}
				}
					break;
				case 703318: { // Strange Aether Piece
					switch (dialog) {
						case USE_OBJECT:
							if (var == 6) {
								return defaultCloseDialog(env, 6, 7); // 7
							}
						default:
							break;
					}
				}
					break;
				case 703319: { // Mysterious Aether Piece
					switch (dialog) {
						case USE_OBJECT:
							if (var == 8) {
								return defaultCloseDialog(env, 8, 9); // 9
							}
						default:
							break;
					}
				}
					break;
				case 703320: { // Suspicious Aether Piece
					switch (dialog) {
						case USE_OBJECT:
							if (var == 10) {
								return defaultCloseDialog(env, 10, 11); // 11
							}
						default:
							break;
					}
				}
					break;
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
			if (var == 1 && zoneName == ZoneName.get("TOWER_OF_ETERNITY_1_220120000")) {
				changeQuestStep(env, 1, 2, false); // 2
				return true;
			}
			else if (var == 4 && zoneName == ZoneName.get("220110000")) {
				changeQuestStep(env, 4, 5, false); // 5
				ItemService.addItem(player, 164002348, 1); // Slumbering Dezabo
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env);
	}
}
