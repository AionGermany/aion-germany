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
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Mariella
 */
public class _14150SecretsOfTheSuperus extends QuestHandler {

	private final static int questId = 14150;

	public _14150SecretsOfTheSuperus() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestItem(182215458, questId);
		qe.registerQuestNpc(204501).addOnQuestStart(questId); // Sarantus
		qe.registerQuestNpc(204501).addOnTalkEvent(questId); // Sarantus
		qe.registerQuestNpc(204582).addOnTalkEvent(questId); // Ibelia
		qe.registerQuestNpc(702423).addOnTalkEvent(questId); // Engraved Stone Tablet
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
			if (targetId == 204501) { // Sarantus
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					return sendQuestDialog(env, 1011);
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
			if (targetId == 204501) { // Sarantus
				return sendQuestEndDialog(env);
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 204501) { // Sarantus
				switch (env.getDialog()) {
					case QUEST_SELECT: {
						if (var == 0) {
							return sendQuestDialog(env, 1011);
						}
						else if (var == 1) {
							return sendQuestDialog(env, 2375);
						}
						break;
					}
					case CHECK_USER_HAS_QUEST_ITEM_SIMPLE: {
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						removeQuestItem(env, 182215458, 1);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
					default:
						break;
				}
			}
			else if (targetId == 204582) { // Ibelia
				switch (env.getDialog()) {
					case QUEST_SELECT: {
						if (var == 0) {
							return sendQuestDialog(env, 1352);
						}
						return false;
					}
					case SETPRO1: {
						if (var == 0) {
							qs.setQuestVarById(0, var + 1); // 1
							updateQuestStatus(env);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
							return true;
						}
					}
					default:
						break;
				}
			}
			else if (targetId == 702423 && qs.getQuestVarById(0) == 1) { // Engraved Stone Tablet
				if (env.getDialog() == DialogAction.USE_OBJECT) {
					if (!giveQuestItem(env, 182215458, 1)) { // Engraved Stone Tablet
						return false;
					}
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
					return true;
				}
			}
		}
		return false;
	}
}
