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
package com.aionemu.gameserver.model.templates.housing;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Rolandas
 * @see client_housing_object_place_tag.xml</tt>
 */
@XmlType(name = "LimitType")
@XmlEnum
public enum LimitType {

	// Limits are in the order of house type: a, b, c, d, s
	NONE(0, new int[] { 0, 0, 0, 0, 0 }, new int[] { 0, 0, 0, 0, 0 }),
	OWNER_POT(1, new int[] { 6, 4, 3, 8, 8 }, new int[] { 0, 0, 0, 4, 0 }),
	VISITOR_POT(2, new int[] { 7, 5, 2, 8, 9 }, new int[] { 0, 0, 0, 4, 0 }),
	STORAGE(3, new int[] { 6, 5, 4, 8, 7 }, new int[] { 0, 0, 0, 4, 0 }),
	POT(4, new int[] { 6, 5, 4, 3, 7 }, new int[] { 6, 5, 4, 1, 7 }),
	COOKING(5, new int[] { 1, 1, 1, 1, 1 }, new int[] { 1, 1, 1, 1, 1 }),
	PICTURE(6, new int[] { 1, 1, 1, 1, 1 }, new int[] { 1, 1, 1, 0, 1 }),
	JUKEBOX(7, new int[] { 1, 1, 1, 1, 1 }, new int[] { 1, 1, 1, 0, 1 });

	int id;
	int[] personalLimits;
	int[] trialLimits;

	private LimitType(int id, int[] maxPersonalLimits, int[] maxTrialLimits) {
		this.id = id;
		this.personalLimits = maxPersonalLimits;
		this.trialLimits = maxTrialLimits;
	}

	public String value() {
		return name();
	}

	public int getId() {
		return id;
	}

	public int getObjectPlaceLimit(HouseType houseType) {
		return personalLimits[houseType.getLimitTypeIndex()];
	}

	public int getTrialObjectPlaceLimit(HouseType houseType) {
		return trialLimits[houseType.getLimitTypeIndex()];
	}

	public static LimitType fromValue(String value) {
		return valueOf(value);
	}
}
