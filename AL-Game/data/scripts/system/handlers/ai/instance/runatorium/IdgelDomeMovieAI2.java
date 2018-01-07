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
package ai.instance.runatorium;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.utils.PacketSendUtility;

import ai.GeneralNpcAI2;

/**
 * @author GiGatR00n v4.7.5.x
 */
@AIName("idgedome_quest_movie")
public class IdgelDomeMovieAI2 extends GeneralNpcAI2 {

	@Override
	protected void handleDialogStart(Player player) {
		final QuestState qs1 = player.getQuestStateList().getQuestState(player.getRace().equals(Race.ELYOS) ? 18941 : 28941);
		final QuestState qs2 = player.getQuestStateList().getQuestState(player.getRace().equals(Race.ELYOS) ? 18940 : 28940);
		if ((qs1 == null || qs1.getStatus() == QuestStatus.NONE) || !(qs2 == null || qs2.getStatus() == QuestStatus.NONE)) {
			// super.handleDialogStart(player);
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 10));

		}
		else {
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1011));
		}
	}

	@SuppressWarnings("incomplete-switch")
	@Override
	public boolean onDialogSelect(final Player player, int dialogId, int questId, int extendedRewardIndex) {
		QuestEnv env = new QuestEnv(getOwner(), player, questId, dialogId);
		env.setExtendedRewardIndex(extendedRewardIndex);
		DialogAction dialog = env.getDialog();
		final QuestState qs = player.getQuestStateList().getQuestState(player.getRace().equals(Race.ELYOS) ? 18941 : 28941);

		if (qs != null && qs.getStatus() == QuestStatus.START && isRightQuest(questId)) {

			switch (dialog) {
				case SELECT_ACTION_1012:
					switch (getNpcId()) {
						case 802383: // Talle (Elyos Quest Giver)
						case 802384: // Salade (Asmo Quest Qiver)
							PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 901));
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1012, 0));
							break;
					}
			}
		}
		return false;
	}

	private boolean isRightQuest(int QuestId) {
		// QuestId:18941(Elyos) = [Group] Destroying the Destroyer
		// QuestId:28941(Asmo) = [Group] Destroying the Destroyer
		return (QuestId == 18941 || QuestId == 28941);
	}
}
