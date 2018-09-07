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
public class GloryInstancePosition extends GenerealInstancePosition {

	@Override
	public void port(Player player, int zone, int position) {
		switch (position) {
			case 1:
				switch (zone) {
					case 1:
						teleport(player, 1728.7698f, 1735.5115f, 270.35883f, (byte) 0);
						break;
					case 2:
						teleport(player, 1848.7509f, 1122.0343f, 338.25327f, (byte) 0);
						break;
					case 3:
						teleport(player, 1601.3693f, 262.18213f, 507.90225f, (byte) 0);
						break;
					case 4:
						teleport(player, 695.56299f, 1830.2776f, 236.63037f, (byte) 0);
						break;
					case 5:
						teleport(player, 466.32935f, 1137.7463f, 436.90363f, (byte) 0);
						break;
					case 6:
						teleport(player, 491.25992f, 396.44275f, 210.79913f, (byte) 0);
						break;
				}
				break;
			case 2:
				switch (zone) {
					case 1:
						teleport(player, 1782.6428f, 1749.6447f, 270.2099f, (byte) 0);
						break;
					case 2:
						teleport(player, 1840.5874f, 1131.8651f, 337.86191f, (byte) 0);
						break;
					case 3:
						teleport(player, 1601.5317f, 255.78668f, 508.06595f, (byte) 0);
						break;
					case 4:
						teleport(player, 693.49695f, 1826.8826f, 236.33366f, (byte) 0);
						break;
					case 5:
						teleport(player, 491.62064f, 1113.132f, 435.51141f, (byte) 0);
						break;
					case 6:
						teleport(player, 481.56711f, 392.17386f, 195.64738f, (byte) 0);
						break;
				}
				break;
			case 3:
				switch (zone) {
					case 1:
						teleport(player, 1779.3856f, 1763.3234f, 270.1344f, (byte) 0);
						break;
					case 2:
						teleport(player, 1840.9456f, 1148.8275f, 338.19537f, (byte) 0);
						break;
					case 3:
						teleport(player, 1651.7954f, 236.90672f, 514.25f, (byte) 0);
						break;
					case 4:
						teleport(player, 681.13123f, 1813.1442f, 204.35895f, (byte) 0);
						break;
					case 5:
						teleport(player, 487.12833f, 1174.527f, 432.93042f, (byte) 0);
						break;
					case 6:
						teleport(player, 502.35532f, 383.3923f, 207.53601f, (byte) 0);
						break;
				}
				break;
			case 4:
				switch (zone) {
					case 1:
						teleport(player, 1789.2378f, 1766.1699f, 270.11316f, (byte) 0);
						break;
					case 2:
						teleport(player, 1849.8066f, 1158.5144f, 338.1109f, (byte) 0);
						break;
					case 3:
						teleport(player, 1648.1063f, 233.50453f, 514.00116f, (byte) 0);
						break;
					case 4:
						teleport(player, 713.56201f, 1829.1324f, 188.6698f, (byte) 0);
						break;
					case 5:
						teleport(player, 492.18265f, 1146.7538f, 433.27365f, (byte) 0);
						break;
					case 6:
						teleport(player, 470.37747f, 379.49329f, 219.26476f, (byte) 0);
						break;
				}
				break;
			case 5:
				switch (zone) {
					case 1:
						teleport(player, 1813.1642f, 1704.0671f, 272.46228f, (byte) 0);
						break;
					case 2:
						teleport(player, 1868.9424f, 1122.505f, 337.85699f, (byte) 0);
						break;
					case 3:
						teleport(player, 1647.5758f, 237.77393f, 514.22351f, (byte) 0);
						break;
					case 4:
						teleport(player, 695.18799f, 1816.3867f, 195.84592f, (byte) 0);
						break;
					case 5:
						teleport(player, 504.25089f, 1130.025f, 433.1051f, (byte) 0);
						break;
					case 6:
						teleport(player, 484.43649f, 382.09552f, 187.54836f, (byte) 0);
						break;
				}
				break;
			case 6:
				switch (zone) {
					case 1:
						teleport(player, 1786.9512f, 1792.4897f, 272.69449f, (byte) 0);
						break;
					case 2:
						teleport(player, 1877.2324f, 1130.4788f, 337.91608f, (byte) 0);
						break;
					case 3:
						teleport(player, 1651.5988f, 279.14572f, 514.23566f, (byte) 0);
						break;
					case 4:
						teleport(player, 711.16937f, 1822.9017f, 183.47443f, (byte) 0);
						break;
					case 5:
						teleport(player, 509.46603f, 1169.5813f, 433.32385f, (byte) 0);
						break;
					case 6:
						teleport(player, 508.7309f, 378.73807f, 199.08717f, (byte) 0);
						break;
				}
				break;
			case 7:
				switch (zone) {
					case 1:
						teleport(player, 1783.3029f, 1686.1635f, 280.87762f, (byte) 0);
						break;
					case 2:
						teleport(player, 1877.5284f, 1150.0294f, 338.69321f, (byte) 0);
						break;
					case 3:
						teleport(player, 1648.1837f, 282.2934f, 514.32208f, (byte) 0);
						break;
					case 4:
						teleport(player, 675.26306f, 1836.4603f, 171.69542f, (byte) 0);
						break;
					case 5:
						teleport(player, 520.46014f, 1151.5432f, 432.93042f, (byte) 0);
						break;
					case 6:
						teleport(player, 507.07452f, 406.64331f, 209.75935f, (byte) 0);
						break;
				}
				break;
			case 8:
				switch (zone) {
					case 1:
						teleport(player, 1812.7941f, 1789.109f, 288.74359f, (byte) 0);
						break;
					case 2:
						teleport(player, 1869.6437f, 1157.5081f, 338.42526f, (byte) 0);
						break;
					case 3:
						teleport(player, 1647.8574f, 278.70276f, 514.76154f, (byte) 0);
						break;
					case 4:
						teleport(player, 703.90625f, 1827.9193f, 163.59734f, (byte) 0);
						break;
					case 5:
						teleport(player, 530.52057f, 1123.7206f, 433.0639f, (byte) 0);
						break;
					case 6:
						teleport(player, 471.09128f, 400.79608f, 224.43683f, (byte) 0);
						break;
				}
				break;
		}
	}
}
