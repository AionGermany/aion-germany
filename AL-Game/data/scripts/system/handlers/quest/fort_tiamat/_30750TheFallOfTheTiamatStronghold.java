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
package quest.fort_tiamat;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.SystemMessageId;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author Cheatkiller
 */
public class _30750TheFallOfTheTiamatStronghold extends QuestHandler {

	private final static int questId = 30750;
	private final static int npcs[] = { 205864, 800368, 800338 };
	private final static int mobs[] = { 219400, 219406 };

	public _30750TheFallOfTheTiamatStronghold() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(205864).addOnQuestStart(questId);
		for (int npc : npcs) {
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
		for (int mob : mobs) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
		qe.registerOnEnterZone(ZoneName.get("DREDGION_CONTROL_CENTER_2_300510000"), questId);
		qe.registerOnEnterWorld(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();
		int targetId = env.getTargetId();

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 205864) {
				if (dialog == DialogAction.QUEST_SELECT) {
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

		if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 800368) {
				switch (dialog) {
					case QUEST_SELECT: {
						if (var == 3) {
							return sendQuestDialog(env, 2034);
						}
					}
					case SETPRO4: {
						return defaultCloseDialog(env, 3, 4);
					}
					default:
						break;
				}
			}
			else if (targetId == 800338) {
				switch (dialog) {
					case USE_OBJECT: {
						if (var == 5) {
							return sendQuestDialog(env, 2716);
						}
					}
					case SET_SUCCEED: {
						return defaultCloseDialog(env, 5, 6, true, false);
					}
					default:
						break;
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 205864) {
				if (dialog == DialogAction.USE_OBJECT) {
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
	public boolean onKillEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (var == 1) {
				return defaultOnKillEvent(env, 219400, 1, 2);
			}
			else if (var == 4) {
				return defaultOnKillEvent(env, 219406, 4, 5);
			}
		}
		return false;
	}

	@Override
	public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (zoneName.equals(ZoneName.get("DREDGION_CONTROL_CENTER_2_300510000"))) {
				if (var == 2) {
					changeQuestStep(env, 2, 3, false);
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean onEnterWorldEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);

		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (player.getWorldId() != 300510000) {
				if (var >= 1) {
					qs.setQuestVar(1);
					updateQuestStatus(env);
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(SystemMessageId.QUEST_FAILED_$1, DataManager.QUEST_DATA.getQuestById(questId).getName()));
					return true;
				}
			}
			else {
				if (var == 0) {
					qs.setQuestVar(1);
					updateQuestStatus(env);
					return true;
				}
			}
		}
		return false;
	}
}
