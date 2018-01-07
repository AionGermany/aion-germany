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
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author Phantom_KNA
 */
public class _10526TheSecretOrder extends QuestHandler {

	public static final int questId = 10526;

	public _10526TheSecretOrder() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerOnLevelUp(questId);
		qe.registerQuestItem(182216072, questId);
		qe.registerQuestNpc(806075).addOnTalkEvent(questId); // Weda
		qe.registerQuestNpc(806393).addOnTalkEvent(questId); // Pores
		qe.registerQuestNpc(806291).addOnTalkEvent(questId); // Dezabo
		qe.registerQuestNpc(806292).addOnTalkEvent(questId); // Awakened Dezabo
		qe.registerQuestNpc(703310).addOnTalkEvent(questId); // Strange Aether Piece
		qe.registerQuestNpc(703311).addOnTalkEvent(questId); // Mysterious Aether Piece
		qe.registerQuestNpc(703312).addOnTalkEvent(questId); // Suspicious Aether Piece
		qe.registerOnEnterZone(ZoneName.get("TOWER_OF_ETERNITY_210110000"), questId); // Tower of Eternity
		qe.registerOnEnterZone(ZoneName.get("210100000"), questId); // Esterra
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();
		DialogAction dialog = env.getDialog();
		
		if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.getStatus() == QuestStatus.COMPLETE) {
			switch (targetId) {
				case 703310:
				case 703311:
				case 703312:
					return closeDialogWindow(env);
			}
			return false;
		}

		if (qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			switch (targetId) {
				case 806075: { // Wedas
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
				case 806393: { // Pores
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
				case 806291: { // Dezabo
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
				case 806292: { // Awakened Dezabo
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
							return defaultCloseDialog(env, 5, 6); // 6
						case SETPRO8:
							return defaultCloseDialog(env, 7, 8); // 8
						case SETPRO10:
							return defaultCloseDialog(env, 9, 10); // 10
						case SET_SUCCEED:
							removeQuestItem(env, 164002347, 1);
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
							return defaultCloseDialog(env, 11, 12); // 12
						default:
							break;
					}
				}
					break;
				case 703310: { // Strange Aether Piece
					switch (dialog) {
						case USE_OBJECT:
							if (var == 6) {
								return defaultCloseDialog(env, 6, 7); // 7
							} else {
								return closeDialogWindow(env);
							}
						default:
							break;
					}
				}
					break;
				case 703311: { // Mysterious Aether Piece
					switch (dialog) {
						case USE_OBJECT:
							if (var == 8) {
								return defaultCloseDialog(env, 8, 9); // 9
							} else {
								return closeDialogWindow(env);
							}
						default:
							break;
					}
				}
					break;
				case 703312: { // Suspicious Aether Piece
					switch (dialog) {
						case USE_OBJECT:
							if (var == 10) {
								return defaultCloseDialog(env, 10, 11); // 11
							} else {
								return closeDialogWindow(env);
							}
						default:
							break;
					}
				}
					break;
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
			if (var == 1 && zoneName == ZoneName.get("TOWER_OF_ETERNITY_210110000")) {
				changeQuestStep(env, 1, 2, false); // 2
				return true;
			}
			else if (var == 4 && zoneName == ZoneName.get("210100000")) {
				changeQuestStep(env, 4, 5, false); // 5
				ItemService.addItem(player, 164002347, 1); // Slumbering Dezabo
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
