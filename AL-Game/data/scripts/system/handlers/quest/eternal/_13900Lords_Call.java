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
package quest.eternal;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author Phantom_KNA
 */
public class _13900Lords_Call extends QuestHandler {

	private final static int questId = 13900;

	public _13900Lords_Call() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerOnEnterZone(ZoneName.get("INGGISON_ILLUSION_FORTRESS_210050000"), questId);
		qe.registerQuestNpc(798926).addOnQuestStart(questId);
		qe.registerQuestNpc(798926).addOnTalkEvent(questId);
		qe.registerQuestNpc(203726).addOnTalkEvent(questId);
		qe.registerQuestNpc(798514).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();
		int targetId = env.getTargetId();
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 798926) { // Outrmus.
				switch (dialog) {
					case QUEST_SELECT: {
						return sendQuestDialog(env, 1011);
					}
					case QUEST_ACCEPT_1:
					case QUEST_ACCEPT_SIMPLE:
						return sendQuestStartDialog(env);
					default:
						break;
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 798926: { // Outrmus.
					switch (dialog) {
						case QUEST_SELECT: {
							return sendQuestDialog(env, 2375);
						}
						case SELECT_QUEST_REWARD: {
							changeQuestStep(env, 0, 0, true);
							return sendQuestEndDialog(env);
						}
						default:
							break;
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 798926) { // Outrmus.
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}

	@Override
	public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName) {
		if (zoneName == ZoneName.get("INGGISON_ILLUSION_FORTRESS_210050000")) {
			Player player = env.getPlayer();
			if (player == null)
				return false;
			QuestState qs = player.getQuestStateList().getQuestState(questId);
			if (qs == null || qs.getStatus() == QuestStatus.NONE || qs.canRepeat()) {
				QuestService.startQuest(env);
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(0, 0));
				return true;

			}
		}
		return false;
	}
}
