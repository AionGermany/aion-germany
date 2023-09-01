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
package com.aionemu.gameserver.model.templates.item;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * @author ATracer
 */
@XmlType(name = "quality")
@XmlEnum
public enum ItemQuality {
	// TODO: Reorder and rename - requires ATracer parser update (?)

	JUNK(0), // Junk - Gray
	COMMON(1), // Common - White
	RARE(2), // Superior - Green
	LEGEND(3), // Heroic - Blue
	UNIQUE(4), // Fabled - Yellow
	EPIC(5), // Eternal - Orange
	MYTHIC(6); // Test - Purple

	private int qualityId;

	/**
	 * Constructors
	 */
	private ItemQuality(int qualityId) {
		this.qualityId = qualityId;
	}

	/**
	 * Accessors
	 */
	public int getQualityId() {
		return qualityId;
	}
}
