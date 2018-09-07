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
package com.aionemu.gameserver.configs.main;

import com.aionemu.commons.configuration.Property;

/**
 * @author KID
 */
public class PlayerTransferConfig {

	@Property(key = "ptransfer.max.kinah", defaultValue = "0")
	public static long MAX_KINAH;
	@Property(key = "ptransfer.bindpoint.elyos", defaultValue = "210010000 1212.9423 1044.8516 140.75568 32")
	public static String BIND_ELYOS;
	@Property(key = "ptransfer.bindpoint.asmo", defaultValue = "220010000 571.0388 2787.3420 299.8750 32")
	public static String BIND_ASMO;
	@Property(key = "ptransfer.allow.emotions", defaultValue = "true")
	public static boolean ALLOW_EMOTIONS;
	@Property(key = "ptransfer.allow.motions", defaultValue = "true")
	public static boolean ALLOW_MOTIONS;
	@Property(key = "ptransfer.allow.macro", defaultValue = "true")
	public static boolean ALLOW_MACRO;
	@Property(key = "ptransfer.allow.npcfactions", defaultValue = "true")
	public static boolean ALLOW_NPCFACTIONS;
	@Property(key = "ptransfer.allow.pets", defaultValue = "true")
	public static boolean ALLOW_PETS;
	@Property(key = "ptransfer.allow.recipes", defaultValue = "true")
	public static boolean ALLOW_RECIPES;
	@Property(key = "ptransfer.allow.skills", defaultValue = "true")
	public static boolean ALLOW_SKILLS;
	@Property(key = "ptransfer.allow.titles", defaultValue = "true")
	public static boolean ALLOW_TITLES;
	@Property(key = "ptransfer.allow.quests", defaultValue = "true")
	public static boolean ALLOW_QUESTS;
	@Property(key = "ptransfer.allow.inventory", defaultValue = "true")
	public static boolean ALLOW_INV;
	@Property(key = "ptransfer.allow.warehouse", defaultValue = "true")
	public static boolean ALLOW_WAREHOUSE;
	@Property(key = "ptransfer.allow.stigma", defaultValue = "true")
	public static boolean ALLOW_STIGMA;
	@Property(key = "ptransfer.block.samename", defaultValue = "false")
	public static boolean BLOCK_SAMENAME;
	@Property(key = "ptransfer.server.name.prefix", defaultValue = "_UNK")
	public static String NAME_PREFIX;
	@Property(key = "ptransfer.retransfer.hours", defaultValue = "_UNK")
	public static int REUSE_HOURS;
	@Property(key = "ptransfer.remove.skills.list", defaultValue = "*")
	public static String REMOVE_SKILL_LIST;
}
