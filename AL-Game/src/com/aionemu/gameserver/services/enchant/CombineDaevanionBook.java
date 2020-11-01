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
package com.aionemu.gameserver.services.enchant;

import java.util.ArrayList;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DAEVANION_SKILL_FUSION;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.utils.PacketSendUtility;

public class CombineDaevanionBook {

	public static void combineDaevanionBook(Player player, ArrayList<Integer> sacrificeBook) {
		for (int sacrifices : sacrificeBook) {
			player.getInventory().decreaseByObjectId(sacrifices, 1L);
		}
		int result = 0;
		int chance = Rnd.get((int) 0, (int) 3);
		switch (player.getPlayerClass()) {
		case GLADIATOR: {
			if (chance == 0) {
				result = Rnd.get((int) 169501640, (int) 169501645);
			}
			if (chance == 1) {
				result = Rnd.get((int) 169501784, (int) 169501785);
			}
			if (chance == 2) {
				result = Rnd.get((int) 169501806, (int) 169501807);
			}
			if (chance != 3)
				break;
			result = 169501773;
			break;
		}
		case TEMPLAR: {
			if (chance == 0) {
				result = Rnd.get((int) 169501646, (int) 169501651);
			}
			if (chance == 1) {
				result = Rnd.get((int) 169501786, (int) 169501787);
			}
			if (chance == 2) {
				result = Rnd.get((int) 169501808, (int) 169501809);
			}
			if (chance != 3)
				break;
			result = 169501774;
			break;
		}
		case ASSASSIN: {
			if (chance == 0) {
				result = Rnd.get((int) 169501652, (int) 169501657);
			}
			if (chance == 1) {
				result = Rnd.get((int) 169501788, (int) 169501789);
			}
			if (chance == 2) {
				result = Rnd.get((int) 169501810, (int) 169501811);
			}
			if (chance != 3)
				break;
			result = 169501775;
			break;
		}
		case RANGER: {
			if (chance == 0) {
				result = Rnd.get((int) 169501658, (int) 169501663);
			}
			if (chance == 1) {
				result = Rnd.get((int) 169501790, (int) 169501791);
			}
			if (chance == 2) {
				result = Rnd.get((int) 169501812, (int) 169501813);
			}
			if (chance != 3)
				break;
			result = 169501776;
			break;
		}
		case SORCERER: {
			if (chance == 0) {
				result = Rnd.get((int) 169501676, (int) 169501681);
			}
			if (chance == 1) {
				result = Rnd.get((int) 169501792, (int) 169501793);
			}
			if (chance == 2) {
				result = Rnd.get((int) 169501818, (int) 169501819);
			}
			if (chance != 3)
				break;
			result = 169501777;
			break;
		}
		case SPIRIT_MASTER: {
			if (chance == 0) {
				result = Rnd.get((int) 169501682, (int) 169501687);
			}
			if (chance == 1) {
				result = Rnd.get((int) 169501794, (int) 169501795);
			}
			if (chance == 2) {
				result = Rnd.get((int) 169501820, (int) 169501821);
			}
			if (chance != 3)
				break;
			result = 169501778;
			break;
		}
		case CLERIC: {
			if (chance == 0) {
				result = Rnd.get((int) 169501664, (int) 169501669);
			}
			if (chance == 1) {
				result = Rnd.get((int) 169501796, (int) 169501797);
			}
			if (chance == 2) {
				result = Rnd.get((int) 169501816, (int) 169501817);
			}
			if (chance != 3)
				break;
			result = 169501779;
			break;
		}
		case CHANTER: {
			if (chance == 0) {
				result = Rnd.get((int) 169501670, (int) 169501675);
			}
			if (chance == 1) {
				result = Rnd.get((int) 169501798, (int) 169501799);
			}
			if (chance == 2) {
				result = Rnd.get((int) 169501814, (int) 169501815);
			}
			if (chance != 3)
				break;
			result = 169501780;
			break;
		}
		case GUNNER: {
			if (chance == 0) {
				result = Rnd.get((int) 169501688, (int) 169501693);
			}
			if (chance == 1) {
				result = Rnd.get((int) 169501800, (int) 169501801);
			}
			if (chance == 2) {
				result = Rnd.get((int) 169501826, (int) 169501827);
			}
			if (chance != 3)
				break;
			result = 169501781;
			break;
		}
		case BARD: {
			if (chance == 0) {
				result = Rnd.get((int) 169501700, (int) 169501705);
			}
			if (chance == 1) {
				result = Rnd.get((int) 169501802, (int) 169501803);
			}
			if (chance == 2) {
				result = Rnd.get((int) 169501822, (int) 169501823);
			}
			if (chance != 3)
				break;
			result = 169501782;
			break;
		}
		case RIDER: {
			if (chance == 0) {
				result = Rnd.get((int) 169501694, (int) 169501699);
			}
			if (chance == 1) {
				result = Rnd.get((int) 169501804, (int) 169501805);
			}
			if (chance == 2) {
				result = Rnd.get((int) 169501824, (int) 169501825);
			}
			if (chance != 3)
				break;
			result = 169501783;
			break;
		}
		case PAINTER: {
			if (chance == 0) {
				result = Rnd.get((int) 169501872, (int) 169501877);
			}
			if (chance == 1) {
				result = Rnd.get((int) 169501878, (int) 169501879);
			}
			if (chance == 2) {
				result = Rnd.get((int) 169501880, (int) 169501881);
			}
			if (chance != 3)
				break;
			result = 169501882;
		}
		default:
			break;
		}
		PacketSendUtility.sendPacket(player, new SM_DAEVANION_SKILL_FUSION(1, result));
		ItemService.addItem(player, result, 1L);
	}
}
