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
package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.achievement.AchievementAction;
import com.aionemu.gameserver.model.gameobjects.player.achievement.PlayerAchievement;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.services.player.AchievementService;

public class SM_ACHIEVEMENT_LIST extends AionServerPacket {

	private Player player;

	public SM_ACHIEVEMENT_LIST(Player player) {
		this.player = player;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeQ(AchievementService.getInstance().getLastUpdate().getTime() / 1000);
		writeH(player.getPlayerAchievements().size());
		for (PlayerAchievement achievement : player.getPlayerAchievements().values()) {
			writeC(1);
			writeQ((long) achievement.getObjectId());
			writeD(achievement.getId());
			writeD(0);
			writeD(0);
			writeC(achievement.getType().getValue());
			writeC(0);
			writeC(achievement.getState().getValue());
			writeD(achievement.getStep());
			writeC(0);
			writeD(0);
			writeD(0);
			writeD(0);
			writeD(0);
			writeD(0);
			writeQ(achievement.getStartDate().getTime() / 1000);
			writeQ(achievement.getEndateDate().getTime() / 1000);
			writeH(achievement.getActionMap().size());
			for (AchievementAction action : achievement.getActionMap().values()) {
				writeQ((long) action.getObjectId());
				writeD(action.getId());
				writeQ((long) action.getAchievementObjectId());
				writeC(0);
				writeC(0);
				writeC(action.getState().getValue());
				writeD(action.getStep());
				writeC(0);
				writeD(0);
				writeD(0);
				writeD(0);
				writeD(0);
				writeD(0);
				writeQ(action.getStartDate().getTime() / 1000);
				writeQ(action.getEndateDate().getTime() / 1000);
			}
		}
	}
}
