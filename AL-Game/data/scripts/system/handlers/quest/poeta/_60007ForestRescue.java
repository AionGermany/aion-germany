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
package quest.poeta;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_USE_OBJECT;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/**
 * @author QuestGenerator by Mariella
 */
public class _60007ForestRescue extends QuestHandler {

	private final static int questId = 60007;

	public _60007ForestRescue() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(730008).addOnTalkEvent(questId); // Daminu
		qe.registerQuestNpc(820002).addOnTalkEvent(questId); // Investigating Royer
		qe.registerQuestNpc(820003).addOnTalkEvent(questId); // Implementer Royer
		qe.registerQuestNpc(651867).addOnKillEvent(questId);
		qe.registerQuestNpc(651848).addOnKillEvent(questId);
		qe.registerQuestNpc(700030).addOnTalkEvent(questId); // Odium Cauldron
		qe.registerQuestItem(182216249, questId); // Thin Flute Of Elim
		qe.registerQuestItem(182216250, questId); // Daminu Flute
		qe.registerOnLevelUp(questId);
		qe.registerOnEnterWorld(questId);
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
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null) {
			return false;
		}

		int targetId = env.getTargetId();
		DialogAction action = env.getDialog();

		if (qs != null && qs.getStatus() == QuestStatus.START) {
			if (targetId == 730008) {
				switch (action) {
				case QUEST_SELECT:
					final int targetObjectId = env.getVisibleObject().getObjectId();
					PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 1));
					PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_QUESTLOOT, 0, targetObjectId), true);
					ThreadPoolManager.getInstance().schedule(new Runnable() {
						@Override
						public void run() {
							if (player.getTarget() == null || player.getTarget().getObjectId() != targetObjectId) {
								return;
							}
							PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.END_QUESTLOOT, 0, targetObjectId), true);
							PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 2));
							PacketSendUtility.sendPacket(env.getPlayer(), new SM_DIALOG_WINDOW(targetObjectId, 1011, env.getQuestId()));
						}
					}, 3000);
				case SETPRO1:
					qs.setQuestVar(1);
					updateQuestStatus(env);
					return closeDialogWindow(env);
				default:
					break;
				}
			} else if (targetId == 820002) {
				int var = qs.getQuestVarById(0);
				if (var == 1) {
					switch (action) {
					case QUEST_SELECT:
						return sendQuestDialog(env, 1352);
					case SELECT_ACTION_1353:
						return sendQuestDialog(env, 1353);
					case SETPRO2:
						qs.setQuestVar(2);
						updateQuestStatus(env);
						return closeDialogWindow(env);
					case CHECK_USER_HAS_QUEST_ITEM:
						return checkQuestItems(env, 2, 3, false, 10000, 10001);
					default:
						break;
					}
				} else if (var == 2) {

				}
			} else if (targetId == 820003) {
				switch (action) {
				case QUEST_SELECT:
					return sendQuestDialog(env, 1693);
				case CHECK_USER_HAS_QUEST_ITEM:
					return checkQuestItems(env, 2, 3, false, 10000, 10001);
				default:
					break;
				}
			} else if (targetId == 700030) {
				switch (action) {
				case USE_OBJECT:
					qs.setQuestVar(4);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					return closeDialogWindow(env);
				default:
					break;
				}
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 820003) {
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
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env);
	}
}
