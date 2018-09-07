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

public class CraftConfig {

	/**
	 * Enable craft skills unrestricted level-up
	 */
	@Property(key = "gameserver.craft.skills.unrestricted.levelup.enable", defaultValue = "false")
	public static boolean UNABLE_CRAFT_SKILLS_UNRESTRICTED_LEVELUP;
	@Property(key = "gameserver.craft.skills.delete.excess.enable", defaultValue = "false")
	public static boolean DELETE_EXCESS_CRAFT_ENABLE;
	/**
	 * Maximum number of expert skills a player can have
	 */
	@Property(key = "gameserver.craft.max.expert.skills", defaultValue = "2")
	public static int MAX_EXPERT_CRAFTING_SKILLS;
	/**
	 * Maximum number of master skills a player can have
	 */
	@Property(key = "gameserver.craft.max.master.skills", defaultValue = "1")
	public static int MAX_MASTER_CRAFTING_SKILLS;
	/**
	 * Chance to have a critical procraft (applied on first step)
	 */
	@Property(key = "gameserver.craft.critical.rate.regular", defaultValue = "15")
	public static int CRAFT_CRIT_RATE;
	@Property(key = "gameserver.craft.critical.rate.premium", defaultValue = "15")
	public static int PREMIUM_CRAFT_CRIT_RATE;
	@Property(key = "gameserver.craft.critical.rate.vip", defaultValue = "15")
	public static int VIP_CRAFT_CRIT_RATE;
	/**
	 * Chance to have a combo procraft (applied on second step)
	 */
	@Property(key = "gameserver.craft.combo.rate.regular", defaultValue = "25")
	public static int CRAFT_COMBO_RATE;
	@Property(key = "gameserver.craft.combo.rate.premium", defaultValue = "25")
	public static int PREMIUM_CRAFT_COMBO_RATE;
	@Property(key = "gameserver.craft.combo.rate.vip", defaultValue = "25")
	public static int VIP_CRAFT_COMBO_RATE;
	public static boolean CRAFT_CHECKTASK = false;
	@Property(key = "gameserver.craft.chance.purplecrit", defaultValue = "1")
	public static int CRAFT_CHANCE_PURPLECRIT;
	@Property(key = "gameserver.craft.chance.bluecrit", defaultValue = "10")
	public static int CRAFT_CHANCE_BLUECRIT;
	@Property(key = "gameserver.craft.chance.instant", defaultValue = "100")
	public static int CRAFT_CHANCE_INSTANT;
	@Property(key = "gameserver.craft.timer.delay", defaultValue = "2000")
	public static int CRAFT_TIMER_DELAY;
	@Property(key = "gameserver.craft.timer.period", defaultValue = "1900")
	public static int CRAFT_TIMER_PERIOD;
	/**
	 * Settings for Substance Morphing
	 */
	@Property(key = "gameserver.craft.timermorph.delay", defaultValue = "3500")
	public static int CRAFT_TIMERMORPH_DELAY;
	@Property(key = "gameserver.craft.timermorph.period", defaultValue = "3500")
	public static int CRAFT_TIMERMORPH_PERIOD;
	@Property(key = "gameserver.craft.extract_cast_delay", defaultValue = "5000")
	public static int EXTRACT_CAST_DELAY;
	/**
	 * Player Protection
	 */
	@Property(key = "gameserver.protection.gather.enable", defaultValue = "true")
	public static boolean PROTECTION_GATHER_ENABLE;
}
