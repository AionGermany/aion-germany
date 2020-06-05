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
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/**
 * @author QuestGenerator by Mariella
 */
public class _70006WheresRae extends QuestHandler {

	private final static int questId = 70006;

	public _70006WheresRae() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerOnLevelUp(questId);
		qe.registerQuestNpc(203534).addOnTalkEvent(questId);  // Dabi
		qe.registerQuestNpc(790002).addOnTalkEvent(questId);  // Verdandi
		qe.registerQuestNpc(806813).addOnTalkEvent(questId);  // Rae
		qe.registerQuestItem(182216399, questId);
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env, 70000, false);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();
		int targetId = env.getTargetId();

		if (qs == null) {
			return false;
		}

		if (qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			switch (targetId) {
				case 203534: {
					switch (dialog) {
						case QUEST_SELECT: {
							return sendQuestDialog(env, 1011);
						}
						case SETPRO1: {
							qs.setQuestVar(1);
							updateQuestStatus(env);
							return closeDialogWindow(env);
						}
						default: 
							break;
					}
					break;
				}
				case 790002: {
					switch (dialog) {
						case QUEST_SELECT: {
							if (var == 1) {
								return sendQuestDialog(env, 1352);
							}
							else
								if (var == 2) {
									return sendQuestDialog(env, 1693);
								}
							else
								if (var == 3) {
									return sendQuestDialog(env, 2034);
								}
							else
								if (var == 5) {
									return sendQuestDialog(env, 2716);
								}
						}
						case SETPRO2: {
							qs.setQuestVar(2);
							updateQuestStatus(env);
							return closeDialogWindow(env);
						}
						case CHECK_USER_HAS_QUEST_ITEM: {
							if (QuestService.collectItemCheck(env, true)) {
								qs.setQuestVar(3);
                                updateQuestStatus(env);
								return sendQuestDialog(env, 10000);
							} else {
								return sendQuestDialog(env, 10001);
							}
						}
						case SETPRO4: {
							giveQuestItem(env, 182216399, 1);
							qs.setQuestVar(4);
							updateQuestStatus(env);
							return closeDialogWindow(env);
						}
						case SET_SUCCEED: {
							TeleportService2.teleportTo(player, 220010000, 948.8798f, 1708.6317f, 259.5f, (byte) 87, TeleportAnimation.BEAM_ANIMATION);
							qs.setQuestVar(6);
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
							return closeDialogWindow(env);
							
						}
							
						
						default:
							break;
							
					}
						
					break;
				}
				default:
					break;
				
				
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 806813) {
				if (dialog == DialogAction.USE_OBJECT) {
					return sendQuestDialog(env, 10002);
				}
				return sendQuestEndDialog(env);
			}
		}

		return false;
	}
	
	@Override
	public HandlerResult onItemUseEvent(final QuestEnv env, Item item) {
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final int id = item.getItemTemplate().getTemplateId();
		final int itemObjId = item.getObjectId();

		if (id != 182216399) {
			return HandlerResult.UNKNOWN;
		}
		qs.setQuestVar(5);
		updateQuestStatus(env);
		
		return HandlerResult.SUCCESS;
	}
}
