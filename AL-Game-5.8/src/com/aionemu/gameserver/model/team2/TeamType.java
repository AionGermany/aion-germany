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
package com.aionemu.gameserver.model.team2;

/**
 * @author Source
 */
public enum TeamType {

	GROUP(0x3F, 0),
	AUTO_GROUP(0x02, 1),
	ALLIANCE(0x3F, 0),
	ALLIANCE_DEFENCE(0x3F, 4),
	ALLIANCE_OFFENCE(0x02, 3);
	// TODO UPDATE OR ADD NEW F6

	private int type;
	private int subType;

	private TeamType(int type, int subType) {
		this.type = type;
		this.subType = subType;
	}

	public int getType() {
		return type;
	}

	public int getSubType() {
		return subType;
	}

	public boolean isAutoTeam() {
		return this.getType() == 0x02;
	}

	public boolean isOffence() {
		return this.getSubType() == 3;
	}

	public boolean isDefence() {
		return this.getSubType() == 4;
	}
}
