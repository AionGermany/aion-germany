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
package com.aionemu.gameserver.services.player.CreativityPanel.stats;

import java.util.ArrayList;
import java.util.List;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.stats.calc.StatOwner;
import com.aionemu.gameserver.model.stats.calc.functions.IStatFunction;
import com.aionemu.gameserver.model.stats.calc.functions.StatAddFunction;
import com.aionemu.gameserver.model.stats.container.StatEnum;

public class Power implements StatOwner {

	private List<IStatFunction> power = new ArrayList<IStatFunction>();

	public void onChange(Player player, int point) {
		if (point >= 1) {
			power.clear();
			player.getGameStats().endEffect(this);
			power.add(new StatAddFunction(StatEnum.PHYSICAL_ATTACK, getPower(point, StatEnum.PHYSICAL_ATTACK), true));
			power.add(new StatAddFunction(StatEnum.PHYSICAL_DEFENSE, getPower(point, StatEnum.PHYSICAL_DEFENSE), true));
			player.getGameStats().addEffect(this, power);
		}
		else if (point == 0) {
			power.clear();
			power.add(new StatAddFunction(StatEnum.PHYSICAL_ATTACK, getPower(point, StatEnum.PHYSICAL_ATTACK), false));
			power.add(new StatAddFunction(StatEnum.PHYSICAL_DEFENSE, getPower(point, StatEnum.PHYSICAL_DEFENSE), false));
			player.getGameStats().endEffect(this);
		}
	}

	private int getPower(int point, StatEnum stat) {
		switch (point) {
			case 1:
				if (stat == StatEnum.PHYSICAL_ATTACK) {
					return 2;
				}
				else if (stat == StatEnum.PHYSICAL_DEFENSE) {
					return 25;
				}
			case 2:
				if (stat == StatEnum.PHYSICAL_ATTACK) {
					return 3;
				}
				else if (stat == StatEnum.PHYSICAL_DEFENSE) {
					return 50;
				}
			case 3:
				if (stat == StatEnum.PHYSICAL_ATTACK) {
					return 5;
				}
				else if (stat == StatEnum.PHYSICAL_DEFENSE) {
					return 75;
				}
			case 4:
				if (stat == StatEnum.PHYSICAL_ATTACK) {
					return 6;
				}
				else if (stat == StatEnum.PHYSICAL_DEFENSE) {
					return 99;
				}
			case 5:
				if (stat == StatEnum.PHYSICAL_ATTACK) {
					return 8;
				}
				else if (stat == StatEnum.PHYSICAL_DEFENSE) {
					return 123;
				}
			case 6:
				if (stat == StatEnum.PHYSICAL_ATTACK) {
					return 9;
				}
				else if (stat == StatEnum.PHYSICAL_DEFENSE) {
					return 146;
				}
			case 7:
				if (stat == StatEnum.PHYSICAL_ATTACK) {
					return 11;
				}
				else if (stat == StatEnum.PHYSICAL_DEFENSE) {
					return 170;
				}
			case 8:
				if (stat == StatEnum.PHYSICAL_ATTACK) {
					return 12;
				}
				else if (stat == StatEnum.PHYSICAL_DEFENSE) {
					return 192;
				}
			case 9:
				if (stat == StatEnum.PHYSICAL_ATTACK) {
					return 14;
				}
				else if (stat == StatEnum.PHYSICAL_DEFENSE) {
					return 215;
				}
			case 10:
				if (stat == StatEnum.PHYSICAL_ATTACK) {
					return 15;
				}
				else if (stat == StatEnum.PHYSICAL_DEFENSE) {
					return 237;
				}
			case 11:
				if (stat == StatEnum.PHYSICAL_ATTACK) {
					return 17;
				}
				else if (stat == StatEnum.PHYSICAL_DEFENSE) {
					return 259;
				}
			case 12:
				if (stat == StatEnum.PHYSICAL_ATTACK) {
					return 18;
				}
				else if (stat == StatEnum.PHYSICAL_DEFENSE) {
					return 281;
				}
			case 13:
				if (stat == StatEnum.PHYSICAL_ATTACK) {
					return 19;
				}
				else if (stat == StatEnum.PHYSICAL_DEFENSE) {
					return 302;
				}
			case 14:
				if (stat == StatEnum.PHYSICAL_ATTACK) {
					return 21;
				}
				else if (stat == StatEnum.PHYSICAL_DEFENSE) {
					return 323;
				}
			case 15:
				if (stat == StatEnum.PHYSICAL_ATTACK) {
					return 22;
				}
				else if (stat == StatEnum.PHYSICAL_DEFENSE) {
					return 344;
				}
			case 16:
				if (stat == StatEnum.PHYSICAL_ATTACK) {
					return 24;
				}
				else if (stat == StatEnum.PHYSICAL_DEFENSE) {
					return 365;
				}
			case 17:
				if (stat == StatEnum.PHYSICAL_ATTACK) {
					return 25;
				}
				else if (stat == StatEnum.PHYSICAL_DEFENSE) {
					return 385;
				}
			case 18:
				if (stat == StatEnum.PHYSICAL_ATTACK) {
					return 27;
				}
				else if (stat == StatEnum.PHYSICAL_DEFENSE) {
					return 405;
				}
			case 19:
				if (stat == StatEnum.PHYSICAL_ATTACK) {
					return 28;
				}
				else if (stat == StatEnum.PHYSICAL_DEFENSE) {
					return 424;
				}
			case 20:
				if (stat == StatEnum.PHYSICAL_ATTACK) {
					return 30;
				}
				else if (stat == StatEnum.PHYSICAL_DEFENSE) {
					return 444;
				}
			case 21:
				if (stat == StatEnum.PHYSICAL_ATTACK) {
					return 31;
				}
				else if (stat == StatEnum.PHYSICAL_DEFENSE) {
					return 463;
				}
			case 22:
				if (stat == StatEnum.PHYSICAL_ATTACK) {
					return 33;
				}
				else if (stat == StatEnum.PHYSICAL_DEFENSE) {
					return 482;
				}
			case 23:
				if (stat == StatEnum.PHYSICAL_ATTACK) {
					return 34;
				}
				else if (stat == StatEnum.PHYSICAL_DEFENSE) {
					return 501;
				}
			case 24:
				if (stat == StatEnum.PHYSICAL_ATTACK) {
					return 36;
				}
				else if (stat == StatEnum.PHYSICAL_DEFENSE) {
					return 519;
				}
			case 25:
				if (stat == StatEnum.PHYSICAL_ATTACK) {
					return 37;
				}
				else if (stat == StatEnum.PHYSICAL_DEFENSE) {
					return 538;
				}
			case 26:
				if (stat == StatEnum.PHYSICAL_ATTACK) {
					return 38;
				}
				else if (stat == StatEnum.PHYSICAL_DEFENSE) {
					return 556;
				}
			case 27:
				if (stat == StatEnum.PHYSICAL_ATTACK) {
					return 40;
				}
				else if (stat == StatEnum.PHYSICAL_DEFENSE) {
					return 573;
				}
			case 28:
				if (stat == StatEnum.PHYSICAL_ATTACK) {
					return 41;
				}
				else if (stat == StatEnum.PHYSICAL_DEFENSE) {
					return 591;
				}
			case 29:
				if (stat == StatEnum.PHYSICAL_ATTACK) {
					return 43;
				}
				else if (stat == StatEnum.PHYSICAL_DEFENSE) {
					return 608;
				}
			case 30:
				if (stat == StatEnum.PHYSICAL_ATTACK) {
					return 44;
				}
				else if (stat == StatEnum.PHYSICAL_DEFENSE) {
					return 625;
				}
			case 31:
				if (stat == StatEnum.PHYSICAL_ATTACK) {
					return 45;
				}
				else if (stat == StatEnum.PHYSICAL_DEFENSE) {
					return 642;
				}
			case 32:
				if (stat == StatEnum.PHYSICAL_ATTACK) {
					return 47;
				}
				else if (stat == StatEnum.PHYSICAL_DEFENSE) {
					return 659;
				}
			case 33:
				if (stat == StatEnum.PHYSICAL_ATTACK) {
					return 48;
				}
				else if (stat == StatEnum.PHYSICAL_DEFENSE) {
					return 676;
				}
			case 34:
				if (stat == StatEnum.PHYSICAL_ATTACK) {
					return 50;
				}
				else if (stat == StatEnum.PHYSICAL_DEFENSE) {
					return 692;
				}
			case 35:
				if (stat == StatEnum.PHYSICAL_ATTACK) {
					return 51;
				}
				else if (stat == StatEnum.PHYSICAL_DEFENSE) {
					return 708;
				}
			case 36:
				if (stat == StatEnum.PHYSICAL_ATTACK) {
					return 53;
				}
				else if (stat == StatEnum.PHYSICAL_DEFENSE) {
					return 724;
				}
			case 37:
				if (stat == StatEnum.PHYSICAL_ATTACK) {
					return 54;
				}
				else if (stat == StatEnum.PHYSICAL_DEFENSE) {
					return 740;
				}
			case 38:
				if (stat == StatEnum.PHYSICAL_ATTACK) {
					return 55;
				}
				else if (stat == StatEnum.PHYSICAL_DEFENSE) {
					return 756;
				}
			case 39:
				if (stat == StatEnum.PHYSICAL_ATTACK) {
					return 57;
				}
				else if (stat == StatEnum.PHYSICAL_DEFENSE) {
					return 771;
				}
			case 40:
				if (stat == StatEnum.PHYSICAL_ATTACK) {
					return 58;
				}
				else if (stat == StatEnum.PHYSICAL_DEFENSE) {
					return 786;
				}
			case 41:
				if (stat == StatEnum.PHYSICAL_ATTACK) {
					return 59;
				}
				else if (stat == StatEnum.PHYSICAL_DEFENSE) {
					return 801;
				}
			case 42:
				if (stat == StatEnum.PHYSICAL_ATTACK) {
					return 61;
				}
				else if (stat == StatEnum.PHYSICAL_DEFENSE) {
					return 816;
				}
			case 43:
				if (stat == StatEnum.PHYSICAL_ATTACK) {
					return 62;
				}
				else if (stat == StatEnum.PHYSICAL_DEFENSE) {
					return 831;
				}
			case 44:
				if (stat == StatEnum.PHYSICAL_ATTACK) {
					return 64;
				}
				else if (stat == StatEnum.PHYSICAL_DEFENSE) {
					return 846;
				}
			case 45:
				if (stat == StatEnum.PHYSICAL_ATTACK) {
					return 65;
				}
				else if (stat == StatEnum.PHYSICAL_DEFENSE) {
					return 860;
				}
			case 46:
				if (stat == StatEnum.PHYSICAL_ATTACK) {
					return 66;
				}
				else if (stat == StatEnum.PHYSICAL_DEFENSE) {
					return 874;
				}
			case 47:
				if (stat == StatEnum.PHYSICAL_ATTACK) {
					return 68;
				}
				else if (stat == StatEnum.PHYSICAL_DEFENSE) {
					return 888;
				}
			case 48:
				if (stat == StatEnum.PHYSICAL_ATTACK) {
					return 69;
				}
				else if (stat == StatEnum.PHYSICAL_DEFENSE) {
					return 902;
				}
			case 49:
				if (stat == StatEnum.PHYSICAL_ATTACK) {
					return 70;
				}
				else if (stat == StatEnum.PHYSICAL_DEFENSE) {
					return 916;
				}
			case 50:
				if (stat == StatEnum.PHYSICAL_ATTACK) {
					return 72;
				}
				else if (stat == StatEnum.PHYSICAL_DEFENSE) {
					return 930;
				}
			case 51:
				if (stat == StatEnum.PHYSICAL_ATTACK) {
					return 73;
				}
				else if (stat == StatEnum.PHYSICAL_DEFENSE) {
					return 943;
				}
			case 52:
				if (stat == StatEnum.PHYSICAL_ATTACK) {
					return 74;
				}
				else if (stat == StatEnum.PHYSICAL_DEFENSE) {
					return 957;
				}
			case 53:
				if (stat == StatEnum.PHYSICAL_ATTACK) {
					return 76;
				}
				else if (stat == StatEnum.PHYSICAL_DEFENSE) {
					return 970;
				}
			case 54:
				if (stat == StatEnum.PHYSICAL_ATTACK) {
					return 77;
				}
				else if (stat == StatEnum.PHYSICAL_DEFENSE) {
					return 983;
				}
			case 55:
				if (stat == StatEnum.PHYSICAL_ATTACK) {
					return 79;
				}
				else if (stat == StatEnum.PHYSICAL_DEFENSE) {
					return 996;
				}
			case 56:
				if (stat == StatEnum.PHYSICAL_ATTACK) {
					return 80;
				}
				else if (stat == StatEnum.PHYSICAL_DEFENSE) {
					return 1009;
				}
			case 57:
				if (stat == StatEnum.PHYSICAL_ATTACK) {
					return 81;
				}
				else if (stat == StatEnum.PHYSICAL_DEFENSE) {
					return 1021;
				}
			case 58:
				if (stat == StatEnum.PHYSICAL_ATTACK) {
					return 83;
				}
				else if (stat == StatEnum.PHYSICAL_DEFENSE) {
					return 1034;
				}
			case 59:
				if (stat == StatEnum.PHYSICAL_ATTACK) {
					return 84;
				}
				else if (stat == StatEnum.PHYSICAL_DEFENSE) {
					return 1046;
				}
			case 60:
				if (stat == StatEnum.PHYSICAL_ATTACK) {
					return 85;
				}
				else if (stat == StatEnum.PHYSICAL_DEFENSE) {
					return 1058;
				}
		}
		return point;
	}

	public static Power getInstance() {
		return NewSingletonHolder.INSTANCE;
	}

	private static class NewSingletonHolder {

		private static final Power INSTANCE = new Power();
	}
}
