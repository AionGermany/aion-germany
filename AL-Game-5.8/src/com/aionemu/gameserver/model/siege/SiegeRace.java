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
package com.aionemu.gameserver.model.siege;

import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.Race;

/**
 * @author Sarynth
 */
public enum SiegeRace {

	ELYOS(0, 1800481),
	ASMODIANS(1, 1800483),
	BALAUR(2, 1800485);

	private int raceId;
	private DescriptionId descriptionId;

	private SiegeRace(int id, int descriptionId) {
		this.raceId = id;
		this.descriptionId = new DescriptionId(descriptionId);
	}

	public int getRaceId() {
		return this.raceId;
	}

	public static SiegeRace getByRace(Race race) {
		switch (race) {
			case ASMODIANS:
				return SiegeRace.ASMODIANS;
			case ELYOS:
				return SiegeRace.ELYOS;
			default:
				return SiegeRace.BALAUR;
		}
	}

	/**
	 * @return the descriptionId
	 */
	public DescriptionId getDescriptionId() {
		return descriptionId;
	}
}
