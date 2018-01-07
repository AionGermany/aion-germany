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
public class _25565DefendAzphelsTemple extends QuestHandler {

	private final static int questId = 25565;

	public _25565DefendAzphelsTemple() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(806098).addOnQuestStart(questId);
		qe.registerOnEnterZone(ZoneName.get("AZPHELS_TEMPLE_220110000"), questId);
		qe.registerQuestNpc(806116).addOnTalkEvent(questId); // Renard
		qe.registerQuestNpc(806116).addOnTalkEvent(questId);
		qe.registerQuestNpc(241293).addOnKillEvent(questId);
		qe.registerQuestNpc(241294).addOnKillEvent(questId);
		qe.registerQuestNpc(241295).addOnKillEvent(questId);
		qe.registerQuestNpc(241296).addOnKillEvent(questId);
		qe.registerQuestNpc(241297).addOnKillEvent(questId);
		qe.registerQuestNpc(241298).addOnKillEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();

		if (qs != null && qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 806116) { // Renard
				switch (env.getDialog()) {
					case USE_OBJECT:
						return sendQuestDialog(env, 10002);
					case SELECT_QUEST_REWARD:
						return sendQuestDialog(env, 5);
					default:
						return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}

	@Override
	public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName) {
		if (zoneName == ZoneName.get("AZPHELS_TEMPLE_220110000")) {
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

	@Override
	public boolean onKillEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() != QuestStatus.START)
			return false;

		int targetId = env.getTargetId();

		switch (targetId) {
			case 241293:
			case 241294:
			case 241295:
			case 241296:
			case 241297:
			case 241298:
				if (qs.getQuestVarById(1) != 9) {
					qs.setQuestVarById(1, qs.getQuestVarById(1) + 1);
					updateQuestStatus(env);
				}
				else {
					qs.setQuestVarById(0, 10);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
				}
		}
		return false;
	}
}
