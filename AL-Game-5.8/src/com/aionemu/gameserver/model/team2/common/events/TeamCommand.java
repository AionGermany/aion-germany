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
package com.aionemu.gameserver.model.team2.common.events;

import com.google.common.base.Preconditions;

import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * @author ATracer
 */
public enum TeamCommand {

	GROUP_BAN_MEMBER(2),
	GROUP_SET_LEADER(3),
	GROUP_REMOVE_MEMBER(6),
	GROUP_SET_LFG(9), // TODO confirm
	GROUP_START_MENTORING(10),
	GROUP_END_MENTORING(11),
	ALLIANCE_LEAVE(14),
	ALLIANCE_BAN_MEMBER(16),
	ALLIANCE_SET_CAPTAIN(17),
	ALLIANCE_CHECKREADY_CANCEL(20),
	ALLIANCE_CHECKREADY_START(21),
	ALLIANCE_CHECKREADY_AUTOCANCEL(22),
	ALLIANCE_CHECKREADY_READY(23),
	ALLIANCE_CHECKREADY_NOTREADY(24),
	ALLIANCE_SET_VICECAPTAIN(25),
	ALLIANCE_UNSET_VICECAPTAIN(26),
	ALLIANCE_CHANGE_GROUP(27),
	LEAGUE_LEAVE(29),
	LEAGUE_EXPEL(30);

	private static TIntObjectHashMap<TeamCommand> teamCommands;

	static {
		teamCommands = new TIntObjectHashMap<TeamCommand>();
		for (TeamCommand eventCode : values()) {
			teamCommands.put(eventCode.getCodeId(), eventCode);
		}
	}

	private final int commandCode;

	private TeamCommand(int commandCode) {
		this.commandCode = commandCode;
	}

	public int getCodeId() {
		return commandCode;
	}

	public static final TeamCommand getCommand(int commandCode) {
		TeamCommand command = teamCommands.get(commandCode);
		Preconditions.checkNotNull(command, "Invalid team command code " + commandCode);
		return command;
	}
}
