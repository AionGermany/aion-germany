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
package com.aionemu.gameserver.model.gameobjects.player.fame;

public enum FameExp {

	LEVEL1(1, 128520),
	LEVEL2(2, 257040),
	LEVEL3(3, 385560),
	LEVEL4(4, 514080),
	LEVEL5(5, 642600),
	LEVEL6(6, 742600),
	LEVEL7(7, 842600),
	LEVEL8(8, 942600),
	LEVEL9(9, 1285200);

	int level;
	long exp;

	private FameExp(int level, long exp) {
		this.level = level;
		this.exp = exp;
	}

	public static FameExp getFameExp(int value) {
		for (FameExp pc : FameExp.values()) {
			if (pc.getLevel() != value)
				continue;
			return pc;
		}
		throw new IllegalArgumentException("There is no fame level with id " + value);
	}

	public long getExp() {
		return exp;
	}

	public int getLevel() {
		return level;
	}
}
