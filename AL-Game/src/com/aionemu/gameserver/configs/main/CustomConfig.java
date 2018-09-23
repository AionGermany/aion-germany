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

public class CustomConfig {

	/**
	 * Enables challenge tasks
	 */
	@Property(key = "gameserver.challenge.tasks.enabled", defaultValue = "false")
	public static boolean CHALLENGE_TASKS_ENABLED;
	/**
	 * Enables fatigue system
	 */
	@Property(key = "gameserver.fatigue.system.enabled", defaultValue = "false")
	public static boolean FATIGUE_SYSTEM_ENABLED;
	/**
	 * Enables enchant table bonuses
	 */
	@Property(key = "gameserver.enchant.bonus.enabled", defaultValue = "false")
	public static boolean ENABLE_ENCHANT_BONUS;
	/**
	 * Show premium account details on login
	 */
	@Property(key = "gameserver.premium.notify", defaultValue = "false")
	public static boolean PREMIUM_NOTIFY;
	/**
	 * Enable announce when a player succes enchant item 15
	 */
	@Property(key = "gameserver.enchant.announce.enable", defaultValue = "true")
	public static boolean ENABLE_ENCHANT_ANNOUNCE;
	/**
	 * Enable speaking between factions
	 */
	@Property(key = "gameserver.chat.factions.enable", defaultValue = "false")
	public static boolean SPEAKING_BETWEEN_FACTIONS;
	/**
	 * Minimum level to use whisper
	 */
	@Property(key = "gameserver.chat.whisper.level", defaultValue = "10")
	public static int LEVEL_TO_WHISPER;
	/**
	 * Factions search mode
	 */
	@Property(key = "gameserver.search.factions.mode", defaultValue = "false")
	public static boolean FACTIONS_SEARCH_MODE;
	/**
	 * list gm when search players
	 */
	@Property(key = "gameserver.search.gm.list", defaultValue = "false")
	public static boolean SEARCH_GM_LIST;
	/**
	 * Minimum level to use search
	 */
	@Property(key = "gameserver.search.player.level", defaultValue = "10")
	public static int LEVEL_TO_SEARCH;
	/**
	 * Allow opposite factions to bind in enemy territories
	 */
	@Property(key = "gameserver.cross.faction.binding", defaultValue = "false")
	public static boolean ENABLE_CROSS_FACTION_BINDING;
	/**
	 * Enable second class change without quest
	 */
	@Property(key = "gameserver.simple.secondclass.enable", defaultValue = "false")
	public static boolean ENABLE_SIMPLE_2NDCLASS;
	/**
	 * Disable chain trigger rate (chain skill with 100% success)
	 */
	@Property(key = "gameserver.skill.chain.triggerrate", defaultValue = "true")
	public static boolean SKILL_CHAIN_TRIGGERRATE;
	/**
	 * Unstuck delay
	 */
	@Property(key = "gameserver.unstuck.delay", defaultValue = "3600")
	public static int UNSTUCK_DELAY;
	/**
	 * The price for using dye command
	 */
	@Property(key = "gameserver.admin.dye.price", defaultValue = "1000000")
	public static int DYE_PRICE;
	/**
	 * Base Fly Time
	 */
	@Property(key = "gameserver.base.flytime", defaultValue = "60")
	public static int BASE_FLYTIME;
	/**
	 * Disable prevention using old names with coupon & command
	 */
	@Property(key = "gameserver.oldnames.coupon.disable", defaultValue = "false")
	public static boolean OLD_NAMES_COUPON_DISABLED;
	@Property(key = "gameserver.oldnames.command.disable", defaultValue = "true")
	public static boolean OLD_NAMES_COMMAND_DISABLED;
	/**
	 * Friendlist size
	 */
	@Property(key = "gameserver.friendlist.size", defaultValue = "90")
	public static int FRIENDLIST_SIZE;
	/**
	 * Basic Quest limit size
	 */
	@Property(key = "gameserver.basic.questsize.limit", defaultValue = "40")
	public static int BASIC_QUEST_SIZE_LIMIT;
	/**
	 * Enable instances
	 */
	@Property(key = "gameserver.instances.enable", defaultValue = "true")
	public static boolean ENABLE_INSTANCES;
	/**
	 * Enable instances mob always aggro player ignore level
	 */
	@Property(key = "gameserver.instances.mob.aggro", defaultValue = "300080000,300090000,300060000")
	public static String INSTANCES_MOB_AGGRO;
	/**
	 * Enable instances cooldown filtring
	 */
	@Property(key = "gameserver.instances.cooldown.filter", defaultValue = "0")
	public static String INSTANCES_COOL_DOWN_FILTER;
	/**
	 * Instances formula
	 */
	@Property(key = "gameserver.instances.cooldown.rate", defaultValue = "1")
	public static int INSTANCES_RATE;
	/**
	 * Enable Kinah cap
	 */
	@Property(key = "gameserver.enable.kinah.cap", defaultValue = "false")
	public static boolean ENABLE_KINAH_CAP;
	/**
	 * Kinah cap value
	 */
	@Property(key = "gameserver.kinah.cap.value", defaultValue = "999999999")
	public static long KINAH_CAP_VALUE;
	/**
	 * Enable AP cap
	 */
	@Property(key = "gameserver.enable.ap.cap", defaultValue = "false")
	public static boolean ENABLE_AP_CAP;
	/**
	 * AP cap value
	 */
	@Property(key = "gameserver.ap.cap.value", defaultValue = "1000000")
	public static long AP_CAP_VALUE;
	/**
	 * Enable GP cap
	 */
	@Property(key = "gameserver.enable.gp.cap", defaultValue = "false")
	public static boolean ENABLE_GP_CAP;
	/**
	 * GP cap value
	 */
	@Property(key = "gameserver.gp.cap.value", defaultValue = "1000000")
	public static long GP_CAP_VALUE;
	/**
	 * Enable EXP cap
	 */
	@Property(key = "gameserver.enable.exp.cap", defaultValue = "false")
	public static boolean ENABLE_EXP_CAP;
	/**
	 * EXP cap value
	 */
	@Property(key = "gameserver.exp.cap.value", defaultValue = "48000000")
	public static long EXP_CAP_VALUE;
	/**
	 * Enable no AP in mentored group.
	 */
	@Property(key = "gameserver.noap.mentor.group", defaultValue = "false")
	public static boolean MENTOR_GROUP_AP;
	/**
	 * .faction cfg
	 */
	@Property(key = "gameserver.faction.free", defaultValue = "true")
	public static boolean FACTION_FREE_USE;
	@Property(key = "gameserver.faction.prices", defaultValue = "10000")
	public static int FACTION_USE_PRICE;
	@Property(key = "gameserver.faction.cmdchannel", defaultValue = "true")
	public static boolean FACTION_CMD_CHANNEL;
	@Property(key = "gameserver.faction.chatchannels", defaultValue = "false")
	public static boolean FACTION_CHAT_CHANNEL;
	/**
	 * Time in milliseconds in which players are limited for killing one player
	 */
	@Property(key = "gameserver.pvp.dayduration", defaultValue = "86400000")
	public static long PVP_DAY_DURATION;
	/**
	 * Allowed Kills in configuered time for full AP. Move to separate config when more pvp options.
	 */
	@Property(key = "gameserver.pvp.maxkills", defaultValue = "5")
	public static int MAX_DAILY_PVP_KILLS;
	/**
	 * Add a reward to player for pvp kills
	 */
	@Property(key = "gameserver.kill.reward.enable", defaultValue = "false")
	public static boolean ENABLE_KILL_REWARD;

	/**
	 * Kills needed for item reward
	 */
	@Property(key = "gameserver.kills.needed1", defaultValue = "5")
	public static int KILLS_NEEDED1;
	@Property(key = "gameserver.kills.needed2", defaultValue = "10")
	public static int KILLS_NEEDED2;
	@Property(key = "gameserver.kills.needed3", defaultValue = "15")
	public static int KILLS_NEEDED3;

	/**
	 * Item Rewards
	 */
	@Property(key = "gameserver.item.reward1", defaultValue = "186000031")
	public static int REWARD1;
	@Property(key = "gameserver.item.reward2", defaultValue = "186000030")
	public static int REWARD2;
	@Property(key = "gameserver.item.reward3", defaultValue = "186000096")
	public static int REWARD3;
	@Property(key = "gameserver.item.reward4", defaultValue = "186000147")
	public static int REWARD4;
	/**
	 * Show dialog id and quest id
	 */
	@Property(key = "gameserver.dialog.showid", defaultValue = "true")
	public static boolean ENABLE_SHOW_DIALOGID;
	/**
	 * Enable one kisk restriction
	 */
	@Property(key = "gameserver.kisk.restriction.enable", defaultValue = "true")
	public static boolean ENABLE_KISK_RESTRICTION;
	/**
	 * Disputed Land
	 */
	@Property(key = "gameserver.dispute.land.enable", defaultValue = "true")
	public static boolean DISPUTE_LAND_ENABLED;
	@Property(key = "gameserver.dispute.land.schedule", defaultValue = "0 0 2 ? * *")
	public static String DISPUTE_LAND_SCHEDULE;
	@Property(key = "gameserver.dispute.land.duration", defaultValue = "2")
	public static int DISPUTE_LAND_DURATION;
	@Property(key = "gameserver.dispute.time", defaultValue = "5")
	public static int DISPUTE_LAND_TIME;
	/**
	 * Rift and Vortex System
	 */
	@Property(key = "gameserver.rift.enable", defaultValue = "true")
	public static boolean RIFT_ENABLED;
	@Property(key = "gameserver.rift.duration", defaultValue = "1")
	public static int RIFT_DURATION;
	@Property(key = "gameserver.rift.spawnchance", defaultValue = "50")
	public static int RIFT_SPAWNCHANCE;
	@Property(key = "gameserver.vortex.enable", defaultValue = "true")
	public static boolean VORTEX_ENABLED;
	@Property(key = "gameserver.vortex.brusthonin.schedule", defaultValue = "0 0 16 ? * SAT")
	public static String VORTEX_THEOBOMOS_SCHEDULE;
	@Property(key = "gameserver.vortex.theobomos.schedule", defaultValue = "0 0 16 ? * SUN")
	public static String VORTEX_BRUSTHONIN_SCHEDULE;
	@Property(key = "gameserver.vortex.duration", defaultValue = "1")
	public static int VORTEX_DURATION;
	@Property(key = "gameserver.reward.service.enable", defaultValue = "false")
	public static boolean ENABLE_REWARD_SERVICE;
	/**
	 * Limits Config
	 */
	@Property(key = "gameserver.limits.enable", defaultValue = "true")
	public static boolean LIMITS_ENABLED;
	@Property(key = "gameserver.limits.update", defaultValue = "0 0 0 * * ?")
	public static String LIMITS_UPDATE;
	@Property(key = "gameserver.sell.limit.kinah", defaultValue = "5300047")
	public static long SELL_LIMIT_KINAH_LV1_LV30;
	@Property(key = "gameserver.sell.limit.kinah", defaultValue = "7100047")
	public static long SELL_LIMIT_KINAH_LV31_LV40;
	@Property(key = "gameserver.sell.limit.kinah", defaultValue = "12050047")
	public static long SELL_LIMIT_KINAH_LV41_LV55;
	@Property(key = "gameserver.sell.limit.kinah", defaultValue = "14600047")
	public static long SELL_LIMIT_KINAH_LV56_LV60;
	@Property(key = "gameserver.sell.limit.kinah", defaultValue = "17150047")
	public static long SELL_LIMIT_KINAH_LV61_LV65;
	@Property(key = "gameserver.sell.limit.kinah", defaultValue = "21200047")
	public static long SELL_LIMIT_KINAH_LV66_LV75;
	@Property(key = "gameserver.chat.text.length", defaultValue = "150")
	public static int MAX_CHAT_TEXT_LENGHT;
	@Property(key = "gameserver.abyssxform.afterlogout", defaultValue = "false")
	public static boolean ABYSSXFORM_LOGOUT;
	@Property(key = "gameserver.instance.duel.enable", defaultValue = "true")
	public static boolean INSTANCE_DUEL_ENABLE;
	@Property(key = "gameserver.ride.restriction.enable", defaultValue = "true")
	public static boolean ENABLE_RIDE_RESTRICTION;
	@Property(key = "gameserver.quest.questdatakills", defaultValue = "true")
	public static boolean QUESTDATA_MONSTER_KILLS;
	@Property(key = "gameserver.commands.admin.dot.enable", defaultValue = "false")
	public static boolean ENABLE_ADMIN_DOT_COMMANDS;

	/**
	 * World Channel
	 */
	@Property(key = "gameserver.worldchannel.costs", defaultValue = "50000")
	public static int WORLD_CHANNEL_AP_COSTS;

	/**
	 * Anti-Zerg system
	 */
	@Property(key = "gameserver.antizerg.enable", defaultValue = "false")
	public static boolean ANTI_ZERG_ENABLED;
	@Property(key = "gameserver.antizerg.map", defaultValue = "")
	public static String ANTI_ZERG_MAP;
	@Property(key = "gameserver.antizerg.playercount", defaultValue = "3")
	public static int ANTI_ZERG_COUNT;

	/**
	 * Store Bind Point before Logout
	 */
	@Property(key = "gameserver.storebindpoint.enable", defaultValue = "false")
	public static boolean ENABLE_STORE_BINDPOINT;

	/**
	 * Custom Delay for Hotspot Teleportation in Milliseconds
	 */
	@Property(key = "gameserver.hotspot.teleport.delay", defaultValue = "10000")
	public static int HOTSPOT_TELEPORT_DELAY;

	/**
	 * Custom Delay for Hotspot Teleportation Cooldown Time Default: 60 seconds (5.x)
	 */
	@Property(key = "gameserver.hotspot.teleport.cooldown.delay", defaultValue = "60")
	public static int HOTSPOT_TELEPORT_COOLDOWN_DELAY;

	/**
	 * Allow's you to disable Teleporter and Flight transporter by NpcId
	 */
	@Property(key = "gameserver.disable.teleport.npcs", defaultValue = "0")
	public static String DISABLE_TELEPORTER_NPCS;

	// Illusion Godstones
	@Property(key = "gameserver.break.illusion.godstones", defaultValue = "1.0")
	public static float ILLUSION_GODSTONE_BREAK_RATE;

	// Minions
	@Property(key = "gameserver.max.minion.list", defaultValue = "200")
	public static int MAX_MINION_LIST;	
}
