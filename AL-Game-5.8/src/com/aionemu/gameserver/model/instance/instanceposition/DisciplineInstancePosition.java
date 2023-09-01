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
public class DisciplineInstancePosition extends GenerealInstancePosition {

	@Override
	public void port(Player player, int zone, int position) {
		switch (position) {
			case 1:
				switch (zone) {
					case 1:
						teleport(player, 1841.294f, 1041.223f, 338.20056f, (byte) 15);
						break;
					case 2:
						teleport(player, 278.18478f, 1265.8389f, 263.1712f, (byte) 73);
						break;
					case 3:
						teleport(player, 709.78845f, 1766.1855f, 183.43953f, (byte) 60);
						break;
					case 4:
						teleport(player, 1817.1067f, 1737.4899f, 311.49692f, (byte) 1);
						break;
				}
				break;
			case 2:
				switch (zone) {
					case 1:
						teleport(player, 1869.4803f, 1041.8444f, 337.9918f, (byte) 43);
						break;
					case 2:
						teleport(player, 251.03516f, 1297.7039f, 248.11426f, (byte) 105);
						break;
					case 3:
						teleport(player, 693.93176f, 1761.0234f, 196.12753f, (byte) 21);
						break;
					case 4:
						teleport(player, 1851.6932f, 1765.4813f, 305.23187f, (byte) 90);
						break;
				}
				break;
			case 3:
				switch (zone) {
					case 1:
						teleport(player, 1869.0569f, 1069.1344f, 337.6657f, (byte) 71);
						break;
					case 2:
						teleport(player, 315.8269f, 1221.0648f, 263.4517f, (byte) 51);
						break;
					case 3:
						teleport(player, 686.09247f, 1756.8987f, 163.4386f, (byte) 25);
						break;
					case 4:
						teleport(player, 1851.7856f, 1709.3085f, 305.23566f, (byte) 31);
						break;
				}
				break;
			case 4:
				switch (zone) {
					case 1:
						teleport(player, 1841.7906f, 1069.6471f, 338.10706f, (byte) 107);
						break;
					case 2:
						teleport(player, 346.1267f, 1185.1802f, 244.43742f, (byte) 44);
						break;
					case 3:
						teleport(player, 693.11945f, 1771.6886f, 236.5583f, (byte) 17);
						break;
					case 4:
						teleport(player, 1887.0206f, 1737.6492f, 311.49692f, (byte) 62);
						break;
				}
				break;
		}
	}
}
