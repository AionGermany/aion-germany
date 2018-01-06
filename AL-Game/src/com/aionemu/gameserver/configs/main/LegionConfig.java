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

import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.configuration.Property;

/**
 * @author Simple
 */
public class LegionConfig {

	/**
	 * Logger for this class.
	 */
	protected static final Logger log = LoggerFactory.getLogger(LegionConfig.class);
	/**
	 * Announcement pattern (checked when announcement is being created)
	 */
	@Property(key = "gameserver.legion.pattern", defaultValue = "[a-zA-Z ]{2,32}")
	public static Pattern LEGION_NAME_PATTERN;
	/**
	 * Self Intro pattern (checked when self intro is being changed)
	 */
	@Property(key = "gameserver.legion.selfintropattern", defaultValue = ".{1,32}")
	public static Pattern SELF_INTRO_PATTERN;
	/**
	 * Nickname pattern (checked when nickname is being changed)
	 */
	@Property(key = "gameserver.legion.nicknamepattern", defaultValue = ".{1,10}")
	public static Pattern NICKNAME_PATTERN;
	/**
	 * Announcement pattern (checked when announcement is being created)
	 */
	@Property(key = "gameserver.legion.announcementpattern", defaultValue = ".{1,256}")
	public static Pattern ANNOUNCEMENT_PATTERN;
	/**
	 * Sets disband legion time
	 */
	@Property(key = "gameserver.legion.disbandtime", defaultValue = "86400")
	public static int LEGION_DISBAND_TIME;
	/**
	 * Sets required difference between disband time and can can create legion again
	 */
	@Property(key = "gameserver.legion.disbanddifference", defaultValue = "604800")
	public static int LEGION_DISBAND_DIFFERENCE;
	/**
	 * Sets required kinah to create a legion
	 */
	@Property(key = "gameserver.legion.creationrequiredkinah", defaultValue = "10000")
	public static int LEGION_CREATE_REQUIRED_KINAH;
	/**
	 * Sets required kinah to create emblem
	 */
	@Property(key = "gameserver.legion.emblemrequiredkinah", defaultValue = "10000")
	public static int LEGION_EMBLEM_REQUIRED_KINAH;
	/**
	 * Sets required kinah to level legion up to 2
	 */
	@Property(key = "gameserver.legion.level2requiredkinah", defaultValue = "100000")
	public static int LEGION_LEVEL2_REQUIRED_KINAH;
	/**
	 * Sets required kinah to level legion up to 3
	 */
	@Property(key = "gameserver.legion.level3requiredkinah", defaultValue = "1000000")
	public static int LEGION_LEVEL3_REQUIRED_KINAH;
	/**
	 * Sets required kinah to level legion up to 4
	 */
	@Property(key = "gameserver.legion.level4requiredkinah", defaultValue = "5000000")
	public static int LEGION_LEVEL4_REQUIRED_KINAH;
	/**
	 * Sets required kinah to level legion up to 5
	 */
	@Property(key = "gameserver.legion.level5requiredkinah", defaultValue = "25000000")
	public static int LEGION_LEVEL5_REQUIRED_KINAH;
	/**
	 * Sets required kinah to level legion up to 6
	 */
	@Property(key = "gameserver.legion.level6requiredkinah", defaultValue = "50000000")
	public static int LEGION_LEVEL6_REQUIRED_KINAH;
	/**
	 * Sets required kinah to level legion up to 7
	 */
	@Property(key = "gameserver.legion.level7requiredkinah", defaultValue = "75000000")
	public static int LEGION_LEVEL7_REQUIRED_KINAH;
	/**
	 * Sets required kinah to level legion up to 8
	 */
	@Property(key = "gameserver.legion.level8requiredkinah", defaultValue = "100000000")
	public static int LEGION_LEVEL8_REQUIRED_KINAH;
	/**
	 * Sets required amount of members to level legion up to 2
	 */
	@Property(key = "gameserver.legion.level2requiredmembers", defaultValue = "10")
	public static int LEGION_LEVEL2_REQUIRED_MEMBERS;
	/**
	 * Sets required amount of members to level legion up to 3
	 */
	@Property(key = "gameserver.legion.level3requiredmembers", defaultValue = "20")
	public static int LEGION_LEVEL3_REQUIRED_MEMBERS;
	/**
	 * Sets required amount of members to level legion up to 4
	 */
	@Property(key = "gameserver.legion.level4requiredmembers", defaultValue = "30")
	public static int LEGION_LEVEL4_REQUIRED_MEMBERS;
	/**
	 * Sets required amount of members to level legion up to 5
	 */
	@Property(key = "gameserver.legion.level5requiredmembers", defaultValue = "40")
	public static int LEGION_LEVEL5_REQUIRED_MEMBERS;
	/**
	 * Sets required amount of members to level legion up to 6
	 */
	@Property(key = "gameserver.legion.level6requiredmembers", defaultValue = "50")
	public static int LEGION_LEVEL6_REQUIRED_MEMBERS;
	/**
	 * Sets required amount of members to level legion up to 7
	 */
	@Property(key = "gameserver.legion.level7requiredmembers", defaultValue = "60")
	public static int LEGION_LEVEL7_REQUIRED_MEMBERS;
	/**
	 * Sets required amount of members to level legion up to 8
	 */
	@Property(key = "gameserver.legion.level8requiredmembers", defaultValue = "70")
	public static int LEGION_LEVEL8_REQUIRED_MEMBERS;
	/**
	 * Sets required amount of abyss point to level legion up to 2
	 */
	@Property(key = "gameserver.legion.level2requiredcontribution", defaultValue = "0")
	public static int LEGION_LEVEL2_REQUIRED_CONTRIBUTION;
	/**
	 * Sets required amount of abyss point to level legion up to 3
	 */
	@Property(key = "gameserver.legion.level3requiredcontribution", defaultValue = "20000")
	public static int LEGION_LEVEL3_REQUIRED_CONTRIBUTION;
	/**
	 * Sets required amount of abyss point to level legion up to 4
	 */
	@Property(key = "gameserver.legion.level4requiredcontribution", defaultValue = "100000")
	public static int LEGION_LEVEL4_REQUIRED_CONTRIBUTION;
	/**
	 * Sets required amount of abyss point to level legion up to 5
	 */
	@Property(key = "gameserver.legion.level5requiredcontribution", defaultValue = "500000")
	public static int LEGION_LEVEL5_REQUIRED_CONTRIBUTION;
	/**
	 * Sets required amount of abyss point to level legion up to 6
	 */
	@Property(key = "gameserver.legion.level6requiredcontribution", defaultValue = "2500000")
	public static int LEGION_LEVEL6_REQUIRED_CONTRIBUTION;
	/**
	 * Sets required amount of abyss point to level legion up to 7
	 */
	@Property(key = "gameserver.legion.level7requiredcontribution", defaultValue = "12500000")
	public static int LEGION_LEVEL7_REQUIRED_CONTRIBUTION;
	/**
	 * Sets required amount of abyss point to level legion up to 8
	 */
	@Property(key = "gameserver.legion.level8requiredcontribution", defaultValue = "62500000")
	public static int LEGION_LEVEL8_REQUIRED_CONTRIBUTION;
	/**
	 * Sets max members of a level 1 legion
	 */
	@Property(key = "gameserver.legion.level1maxmembers", defaultValue = "30")
	public static int LEGION_LEVEL1_MAX_MEMBERS;
	/**
	 * Sets max members of a level 2 legion
	 */
	@Property(key = "gameserver.legion.level2maxmembers", defaultValue = "60")
	public static int LEGION_LEVEL2_MAX_MEMBERS;
	/**
	 * Sets max members of a level 3 legion
	 */
	@Property(key = "gameserver.legion.level3maxmembers", defaultValue = "90")
	public static int LEGION_LEVEL3_MAX_MEMBERS;
	/**
	 * Sets max members of a level 4 legion
	 */
	@Property(key = "gameserver.legion.level4maxmembers", defaultValue = "120")
	public static int LEGION_LEVEL4_MAX_MEMBERS;
	/**
	 * Sets max members of a level 5 legion
	 */
	@Property(key = "gameserver.legion.level5maxmembers", defaultValue = "150")
	public static int LEGION_LEVEL5_MAX_MEMBERS;
	/**
	 * Sets max members of a level 6 legion
	 */
	@Property(key = "gameserver.legion.level6maxmembers", defaultValue = "180")
	public static int LEGION_LEVEL6_MAX_MEMBERS;
	/**
	 * Sets max members of a level 7 legion
	 */
	@Property(key = "gameserver.legion.level7maxmembers", defaultValue = "210")
	public static int LEGION_LEVEL7_MAX_MEMBERS;
	/**
	 * Sets max members of a level 8 legion
	 */
	@Property(key = "gameserver.legion.level8maxmembers", defaultValue = "240")
	public static int LEGION_LEVEL8_MAX_MEMBERS;
	/**
	 * Enable/disable Legion Warehouse
	 */
	@Property(key = "gameserver.legion.warehouse", defaultValue = "true")
	public static boolean LEGION_WAREHOUSE;
	/**
	 * Enable/disable Legion Invite Other Faction
	 */
	@Property(key = "gameserver.legion.inviteotherfaction", defaultValue = "false")
	public static boolean LEGION_INVITEOTHERFACTION;
	/**
	 * LWH slots
	 */
	@Property(key = "gameserver.legion.warehouse.level1.slots", defaultValue = "24")
	public static int LWH_LEVEL1_SLOTS;
	@Property(key = "gameserver.legion.warehouse.level2.slots", defaultValue = "32")
	public static int LWH_LEVEL2_SLOTS;
	@Property(key = "gameserver.legion.warehouse.level3.slots", defaultValue = "40")
	public static int LWH_LEVEL3_SLOTS;
	@Property(key = "gameserver.legion.warehouse.level4.slots", defaultValue = "48")
	public static int LWH_LEVEL4_SLOTS;
	@Property(key = "gameserver.legion.warehouse.level5.slots", defaultValue = "56")
	public static int LWH_LEVEL5_SLOTS;
	@Property(key = "gameserver.legion.warehouse.level6.slots", defaultValue = "64")
	public static int LWH_LEVEL6_SLOTS;
	@Property(key = "gameserver.legion.warehouse.level7.slots", defaultValue = "72")
	public static int LWH_LEVEL7_SLOTS;
	@Property(key = "gameserver.legion.warehouse.level8.slots", defaultValue = "80")
	public static int LWH_LEVEL8_SLOTS;
	@Property(key = "gameserver.legion.task.requirement.enable", defaultValue = "true")
	public static boolean ENABLE_GUILD_TASK_REQ;
	/**
	 * Legion Buff System
	 */
	@Property(key = "gameserver.legion.buff.required.members", defaultValue = "10")
	public static int LEGION_BUFF_REQUIRED_MEMBERS;
}
