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
package com.aionemu.gameserver.controllers.movement;

/**
 * @author Mr. Poke
 */
public class MovementMask {

	public static final byte IMMEDIATE = (byte) 0x00;
	public static final byte GLIDE = (byte) 0x04;
	public static final byte FALL = (byte) 0x08;
	public static final byte VEHICLE = (byte) 0x10;
	public static final byte MOUSE = (byte) 0x20;
	public static final byte STARTMOVE = (byte) 0xC0;
	public static final byte STARTMOVE_NEW = (byte) 0xC4;
	public static final byte NPC_WALK_SLOW = (byte) 0xEA;
	public static final byte NPC_WALK_FAST = (byte) 0xE8;
	public static final byte NPC_RUN_SLOW = (byte) 0xE4;
	public static final byte NPC_RUN_FAST = (byte) 0xE2;
	public static final byte NPC_STARTMOVE = (byte) 0xE0;
}
