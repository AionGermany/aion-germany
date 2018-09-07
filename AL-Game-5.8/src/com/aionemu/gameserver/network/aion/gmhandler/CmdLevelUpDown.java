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
package com.aionemu.gameserver.network.aion.gmhandler;

import com.aionemu.gameserver.configs.main.GSConfig;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Alcapwnd
 */
public class CmdLevelUpDown extends AbstractGMHandler {

	public enum LevelUpDownState {
		UP,
		DOWN
	}

	;

	private LevelUpDownState state;

	public CmdLevelUpDown(Player admin, String params, LevelUpDownState state) {
		super(admin, params);
		this.state = state;
		run();
	}

	public void run() {
		Player t = target != null ? target : admin;
		Integer level = Integer.parseInt(params);

		if (state == LevelUpDownState.DOWN) {
			if (t.getCommonData().getLevel() - level >= 1) {
				int newLevel = t.getCommonData().getLevel() - level;
				t.getCommonData().setLevel(newLevel);
			}
			else {
				PacketSendUtility.sendMessage(admin, "The value of <level> will minus calculated to the current player level!");
			}
		}
		else if (state == LevelUpDownState.UP) {
			if (t.getCommonData().getLevel() + level <= GSConfig.PLAYER_MAX_LEVEL) {
				int newLevel = t.getCommonData().getLevel() + level;
				t.getCommonData().setLevel(newLevel);
			}
			else {
				PacketSendUtility.sendMessage(admin, "The value of <level> will plus calculated to the current player level!");
			}
		}
	}

}
