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
package com.aionemu.gameserver.utils.stats;

/**
 * @author ATracer, Jangan
 */
public enum XPLossEnum {

	/*
	 * LEVEL_6(6, 1.0), LEVEL_30(30, 1.0), LEVEL_40(40, 0.35),
	 */
	LEVEL_50(50, 0.25),
	LEVEL_55(55, 0.25),
	LEVEL_60(60, 0.25),
	LEVEL_65(65, 0.25),
	LEVEL_70(70, 0.25),
	LEVEL_75(75, 0.25);

	private int level;
	private double param;

	private XPLossEnum(int level, double param) {
		this.level = level;
		this.param = param;
	}

	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @return the param
	 */
	public double getParam() {
		return param;
	}

	/**
	 * @param level
	 * @param expNeed
	 * @return long
	 */
	public static long getExpLoss(int level, long expNeed) {
		if (level < 50) {
			return 0;
		}

		for (XPLossEnum xpLossEnum : values()) {
			if (level <= xpLossEnum.getLevel()) {
				return Math.round(expNeed / 100 * xpLossEnum.getParam());
			}
		}
		return 0;
	}
}
