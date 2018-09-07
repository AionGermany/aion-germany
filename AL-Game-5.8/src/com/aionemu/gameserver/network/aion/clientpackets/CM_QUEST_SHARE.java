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
package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.configs.main.GroupConfig;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.QuestTemplate;
import com.aionemu.gameserver.model.templates.quest.QuestTarget;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUEST_ACTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author ginho1
 */
public class CM_QUEST_SHARE extends AionClientPacket {

	public int questId;

	public CM_QUEST_SHARE(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl() {
		this.questId = readD();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl() {
		Player player = this.getConnection().getActivePlayer();

		if (player == null) {
			return;
		}

		QuestTemplate questTemplate = DataManager.QUEST_DATA.getQuestById(questId);

		if (questTemplate == null || questTemplate.isCannotShare()) {
			return;
		}

		QuestState questState = player.getQuestStateList().getQuestState(this.questId);

		if ((questState == null) || (questState.getStatus() == QuestStatus.COMPLETE)) {
			return;
		}

		if (player.isInGroup2()) {
			for (Player member : player.getPlayerGroup2().getOnlineMembers()) {
				if (player == member) {
					continue;
				}

				if (!MathUtil.isIn3dRange(member, player, GroupConfig.GROUP_MAX_DISTANCE)) {
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1100000, member.getName()));
					continue;
				}

				if (questTemplate.getTarget().equals(QuestTarget.ALLIANCE)) {
					PacketSendUtility.sendPacket(member, new SM_SYSTEM_MESSAGE(1100005, player.getName()));
					continue;
				}

				if (!questTemplate.isRepeatable()) {
					if (member.getQuestStateList().getQuestState(questId) != null) {
						if (member.getQuestStateList().getQuestState(questId).getStatus() != null && member.getQuestStateList().getQuestState(questId).getStatus() != QuestStatus.NONE) {
							continue;
						}
					}
				}
				else {
					if (member.getQuestStateList().getQuestState(questId) != null) {
						if (member.getQuestStateList().getQuestState(questId).getStatus() == QuestStatus.START || member.getQuestStateList().getQuestState(questId).getStatus() == QuestStatus.REWARD) {
							continue;
						}
					}
				}

				if (member.isInFlyingState()) {
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1100003, member.getName()));
					continue;
				}

				if (!QuestService.checkLevelRequirement(this.questId, member.getLevel())) {
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1100003, member.getName()));
					PacketSendUtility.sendPacket(member, new SM_SYSTEM_MESSAGE(1100003, player.getName()));
					continue;
				}
				PacketSendUtility.sendPacket(member, new SM_QUEST_ACTION(this.questId, member.getObjectId(), true));
			}
		}
		else if (player.isInAlliance2()) {
			for (Player member : player.getPlayerAllianceGroup2().getOnlineMembers()) {
				if (player == member) {
					continue;
				}
				if (!MathUtil.isIn3dRange(member, player, GroupConfig.GROUP_MAX_DISTANCE)) {
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1100000, member.getName()));
					continue;
				}

				if (!questTemplate.isRepeatable()) {
					if (member.getQuestStateList().getQuestState(questId).getStatus() != null && member.getQuestStateList().getQuestState(questId).getStatus() != QuestStatus.NONE) {
						continue;
					}
				}
				else {
					if (member.getQuestStateList().getQuestState(questId).getStatus() == QuestStatus.START || member.getQuestStateList().getQuestState(questId).getStatus() == QuestStatus.REWARD) {
						continue;
					}
				}

				if (member.isInFlyingState()) {
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1100003, member.getName()));
					continue;
				}

				if (!QuestService.checkLevelRequirement(this.questId, member.getLevel())) {
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1100003, member.getName()));
					PacketSendUtility.sendPacket(member, new SM_SYSTEM_MESSAGE(1100003, player.getName()));
					continue;
				}
				PacketSendUtility.sendPacket(member, new SM_QUEST_ACTION(this.questId, member.getObjectId(), true));
			}
		}
		else {
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1100000));
			return;
		}
	}
}
