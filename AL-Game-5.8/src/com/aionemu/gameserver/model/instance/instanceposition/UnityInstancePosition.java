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
package com.aionemu.gameserver.model.instance.instanceposition;

import com.aionemu.gameserver.model.gameobjects.player.Player;

/**
 * @author xTz
 */
public class UnityInstancePosition extends GenerealInstancePosition {

	@Override
	public void port(Player player, int zone, int position) {
		switch (position) {
		case 1:
			switch (zone) {
			case 1:
				teleport(player, 1851.0062f, 1702.5195f, 310.42856f, (byte) 0);
				break;
			case 2:
				teleport(player, 446.29938f, 1805.3363f, 172.48746f, (byte) 0);
				break;
			case 3:
				teleport(player, 523.44098f, 1175.9973f, 433.18271f, (byte) 0);
				break;
			case 4:
				teleport(player, 484.3735f, 385.22284f, 186.00145f, (byte) 0);
				break;
			case 5:
				teleport(player, 1553.1068f, 340.37198f, 200.51918f, (byte) 0);
				break;
			case 6:
				teleport(player, 1858.165f, 1132.8064f, 217.73499f, (byte) 0);
				break;
			}
			break;
		case 2:
			switch (zone) {
			case 1:
				teleport(player, 1854.8535f, 1702.7606f, 310.42856f, (byte) 0);
				break;
			case 2:
				teleport(player, 441.7691f, 1780.3921f, 172.4404f, (byte) 0);
				break;
			case 3:
				teleport(player, 536.21954f, 1152.8105f, 433.18625f, (byte) 0);
				break;
			case 4:
				teleport(player, 503.5722f, 376.03281f, 196.5117f, (byte) 0);
				break;
			case 5:
				teleport(player, 1575.3278f, 352.82626f, 203.68724f, (byte) 0);
				break;
			case 6:
				teleport(player, 1818.9377f, 1187.3922f, 217.85521f, (byte) 0);
				break;
			}
			break;
		case 3:
			switch (zone) {
			case 1:
				teleport(player, 1858.8142f, 1702.8604f, 310.42856f, (byte) 0);
				break;
			case 2:
				teleport(player, 445.51358f, 1756.308f, 174.83492f, (byte) 0);
				break;
			case 3:
				teleport(player, 529.83026f, 1123.1311f, 433.16101f, (byte) 0);
				break;
			case 4:
				teleport(player, 513.78851f, 387.05893f, 202.90605f, (byte) 0);
				break;
			case 5:
				teleport(player, 1572.9283f, 356.70764f, 204.03906f, (byte) 0);
				break;
			case 6:
				teleport(player, 1860.4971f, 1200.7257f, 229.35692f, (byte) 0);
				break;
			}
			break;
		case 4:
			switch (zone) {
			case 1:
				teleport(player, 1815.9524f, 1732.0352f, 310.44937f, (byte) 0);
				break;
			case 2:
				teleport(player, 474.90109f, 1750.0125f, 172.47906f, (byte) 0);
				break;
			case 3:
				teleport(player, 507.31659f, 1102.8146f, 433.29669f, (byte) 0);
				break;
			case 4:
				teleport(player, 507.27774f, 407.05258f, 209.96556f, (byte) 0);
				break;
			case 5:
				teleport(player, 1587.1409f, 354.17056f, 198.43542f, (byte) 0);
				break;
			case 6:
				teleport(player, 1842.7477f, 1162.9127f, 217.70515f, (byte) 0);
				break;
			}
			break;
		case 5:
			switch (zone) {
			case 1:
				teleport(player, 1815.9185f, 1736.5698f, 310.44937f, (byte) 0);
				break;
			case 2:
				teleport(player, 495.06195f, 1747.3091f, 173.5493f, (byte) 0);
				break;
			case 3:
				teleport(player, 506.33411f, 1131.1976f, 433.19296f, (byte) 0);
				break;
			case 4:
				teleport(player, 482.01779f, 412.5983f, 218.26518f, (byte) 0);
				break;
			case 5:
				teleport(player, 1588.2511f, 378.15894f, 200.05574f, (byte) 0);
				break;
			case 6:
				teleport(player, 1830.0492f, 1203.9077f, 220.14816f, (byte) 0);
				break;
			}
			break;
		case 6:
			switch (zone) {
			case 1:
				teleport(player, 1815.7126f, 1741.1333f, 310.44937f, (byte) 0);
				break;
			case 2:
				teleport(player, 515.41498f, 1749.9364f, 171.58592f, (byte) 0);
				break;
			case 3:
				teleport(player, 496.44434f, 1114.3512f, 433.28073f, (byte) 0);
				break;
			case 4:
				teleport(player, 468.30084f, 398.0802f, 225.12308f, (byte) 0);
				break;
			case 5:
				teleport(player, 1560.8124f, 389.32593f, 202.26889f, (byte) 0);
				break;
			case 6:
				teleport(player, 1850.8292f, 1173.2179f, 230.76379f, (byte) 0);
				break;
			}
			break;
		case 7:
			switch (zone) {
			case 1:
				teleport(player, 1853.0728f, 1772.45f, 310.44879f, (byte) 0);
				break;
			case 2:
				teleport(player, 543.84558f, 1755.5155f, 173.66292f, (byte) 0);
				break;
			case 3:
				teleport(player, 510.25061f, 1148.4823f, 433.18365f, (byte) 0);
				break;
			case 4:
				teleport(player, 468.2298f, 384.94141f, 219.99139f, (byte) 0);
				break;
			case 5:
				teleport(player, 1557.0649f, 389.01395f, 203.41882f, (byte) 0);
				break;
			case 6:
				teleport(player, 1851.0189f, 1145.1492f, 231.58502f, (byte) 0);
				break;
			}
			break;
		case 8:
			switch (zone) {
			case 1:
				teleport(player, 1849.2573f, 1772.3376f, 310.44879f, (byte) 0);
				break;
			case 2:
				teleport(player, 549.66162f, 1780.8221f, 171.59732f, (byte) 0);
				break;
			case 3:
				teleport(player, 497.4404f, 1160.4882f, 433.11658f, (byte) 0);
				break;
			case 4:
				teleport(player, 480.7193f, 368.64465f, 216.83505f, (byte) 0);
				break;
			case 5:
				teleport(player, 1553.0083f, 388.73923f, 203.36218f, (byte) 0);
				break;
			case 6:
				teleport(player, 1828.4608f, 1130.5066f, 223.68491f, (byte) 0);
				break;
			}
			break;
		case 9:
			switch (zone) {
			case 1:
				teleport(player, 1845.6466f, 1772.197f, 310.44879f, (byte) 0);
				break;
			case 2:
				teleport(player, 544.16589f, 1806.5024f, 173.60339f, (byte) 0);
				break;
			case 3:
				teleport(player, 486.33145f, 1173.7235f, 433.16763f, (byte) 0);
				break;
			case 4:
				teleport(player, 499.29306f, 374.73746f, 210.01361f, (byte) 0);
				break;
			case 5:
				teleport(player, 1558.7871f, 372.97946f, 203.56261f, (byte) 0);
				break;
			case 6:
				teleport(player, 1832.5094f, 1148.7605f, 217.78876f, (byte) 0);
				break;
			}
			break;
		case 10:
			switch (zone) {
			case 1:
				teleport(player, 1886.043f, 1733.7983f, 310.41644f, (byte) 0);
				break;
			case 2:
				teleport(player, 514.97675f, 1811.9063f, 172.38895f, (byte) 0);
				break;
			case 3:
				teleport(player, 473.27823f, 1153.5302f, 433.13321f, (byte) 0);
				break;
			case 4:
				teleport(player, 499.47296f, 393.79526f, 204.07152f, (byte) 0);
				break;
			case 5:
				teleport(player, 1539.7013f, 377.34818f, 204.68753f, (byte) 0);
				break;
			case 6:
				teleport(player, 1814.7136f, 1149.2618f, 217.65062f, (byte) 0);
				break;
			}
			break;
		case 11:
			switch (zone) {
			case 1:
				teleport(player, 1885.9547f, 1737.7559f, 310.41644f, (byte) 0);
				break;
			case 2:
				teleport(player, 495.44571f, 1811.359f, 175.76955f, (byte) 0);
				break;
			case 3:
				teleport(player, 474.12839f, 1123.361f, 441.54596f, (byte) 0);
				break;
			case 4:
				teleport(player, 482.25574f, 391.00235f, 195.26796f, (byte) 0);
				break;
			case 5:
				teleport(player, 1535.8186f, 375.17035f, 204.85748f, (byte) 0);
				break;
			case 6:
				teleport(player, 1813.3f, 1176.4271f, 217.70609f, (byte) 0);
				break;
			}
			break;
		case 12:
			switch (zone) {
			case 1:
				teleport(player, 1885.8027f, 1741.9368f, 310.41644f, (byte) 0);
				break;
			case 2:
				teleport(player, 474.75629f, 1811.5469f, 173.62592f, (byte) 0);
				break;
			case 3:
				teleport(player, 468.48843f, 1137.6251f, 436.42511f, (byte) 0);
				break;
			case 4:
				teleport(player, 490.99121f, 394.31186f, 211.07214f, (byte) 0);
				break;
			case 5:
				teleport(player, 1548.5005f, 366.883f, 198.40457f, (byte) 0);
				break;
			case 6:
				teleport(player, 1847.0271f, 1149.4534f, 217.66801f, (byte) 0);
				break;
			}
			break;
		}
	}
}
