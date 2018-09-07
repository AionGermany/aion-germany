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

package quest.sanctum;

import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapType;

/**
 * @author xaerolt, Rolandas modified for 4.8 Mariella
 */
public class _1926SecretLibraryAccess extends QuestHandler {

	private final static int questId = 1926;
	private final static int[] npc_ids = { 203894, 203895, 203701 };

	public _1926SecretLibraryAccess() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(203894).addOnQuestStart(questId);
		for (int npc_id : npc_ids) {
			qe.registerQuestNpc(npc_id).addOnTalkEvent(questId);
		}
	}

	/*
	 * ToDo: check, which quest must be completed, there are no definitions in quest_data.xml private boolean AreVerteronQuestsFinished(Player player) { QuestState qs =
	 * player.getQuestStateList().getQuestState(1020); return ((qs == null) || (qs.getStatus() != QuestStatus.COMPLETE && qs.getStatus() != QuestStatus.NONE)) ? false : true; } private boolean
	 * AreAethertechQuestsFinished(Player player) { QuestState qs = player.getQuestStateList().getQuestState(14016); return ((qs == null) || (qs.getStatus() != QuestStatus.COMPLETE && qs.getStatus()
	 * != QuestStatus.NONE)) ? false : true; }
	 */
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc) {
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		final QuestState qs = player.getQuestStateList().getQuestState(questId);

		if (targetId == 203894) { // Latri
			if (qs == null || qs.getStatus() == QuestStatus.NONE) {
				switch (env.getDialog()) {
					case QUEST_SELECT: {
						return sendQuestDialog(env, 4762);
					}
					default:
						return sendQuestStartDialog(env);
				}
			}
			else if (qs.getStatus() == QuestStatus.REWARD) {
				switch (env.getDialog()) {
					case USE_OBJECT:
						return sendQuestDialog(env, 10002);
					case SELECT_QUEST_REWARD: {
						removeQuestItem(env, 182206022, 1);
						return sendQuestEndDialog(env);
					}
					case SELECTED_QUEST_NOREWARD: {
						return sendQuestEndDialog(env);
					}
					default:
						break;
				}
			}
			else if (qs.getStatus() == QuestStatus.COMPLETE) {
				TeleportService2.teleportTo(player, WorldMapType.SANCTUM.getId(), 2032.9f, 1473.1f, 592.22534f, (byte) 195, TeleportAnimation.BEAM_ANIMATION);
				return true;
			}
		}
		else if (targetId == 203895) { // Lamid
			if (qs.getStatus() == QuestStatus.COMPLETE) {
				TeleportService2.teleportTo(player, WorldMapType.SANCTUM.getId(), 2006.1f, 1479.1f, 591.96124f, (byte) 195, TeleportAnimation.BEAM_ANIMATION);
				return true;
			}
		}
		else if (targetId == 203701) { // Lavirintos
			switch (env.getDialog()) {
				case QUEST_SELECT: {
					return sendQuestDialog(env, 1011);
				}
				case SET_SUCCEED: {
					if (giveQuestItem(env, 182206022, 1)) {
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
						return true;
					}
					break;
				}
				default:
					break;
			}
		}
		return false;
	}
}
