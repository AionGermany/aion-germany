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

import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.services.player.AchievementService;

public class CM_ACHIEVEMENT_COMPLETE extends AionClientPacket {

	private int templateId;
	private long achievementObj;
	private long actionObj;

	public CM_ACHIEVEMENT_COMPLETE(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	protected void readImpl() {
		templateId = readD();
		achievementObj = readQ();
		actionObj = readQ();
	}

	protected void runImpl() {
		if (achievementObj == 0) {
			AchievementService.getInstance().onRewardAchievement(getConnection().getActivePlayer(), templateId);
		} 
		else {
			AchievementService.getInstance().onRewardAction(getConnection().getActivePlayer(), (int) actionObj, templateId);
		}
	}
}
