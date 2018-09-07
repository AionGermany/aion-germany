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
package com.aionemu.gameserver.model.items;

//added by Blackhive original credits to xTr 2.0.0.5 mod by Tomate
public class ItemMask {

	public static final int LIMIT_ONE = 1;
	public static final int TRADEABLE = (1 << 1);
	public static final int SELLABLE = (1 << 2);
	public static final int STORABLE_IN_WH = (1 << 3);
	public static final int STORABLE_IN_AWH = (1 << 4);
	public static final int STORABLE_IN_LWH = (1 << 5);
	public static final int BREAKABLE = (1 << 6);
	public static final int SOUL_BOUND = (1 << 7);
	public static final int REMOVE_LOGOUT = (1 << 8);
	public static final int NO_ENCHANT = (1 << 9);
	public static final int CAN_PROC_ENCHANT = (1 << 10);
	public static final int CAN_COMPOSITE_WEAPON = (1 << 11);
	public static final int REMODELABLE = (1 << 12);
	public static final int CAN_SPLIT = (1 << 13);
	public static final int DELETABLE = (1 << 14);
	public static final int DYEABLE = (1 << 15);
	public static final int CAN_AP_EXTRACT = (1 << 16);
	public static final int CAN_POLISH = (1 << 17);
	public static final int CAN_AMPLIFICATION = (1 << 18);
	public static final int ITEM_HIGHDAEVA = (1 << 19);
}
