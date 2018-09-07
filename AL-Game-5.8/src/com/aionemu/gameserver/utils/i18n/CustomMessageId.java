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
package com.aionemu.gameserver.utils.i18n;

import com.aionemu.gameserver.configs.network.NetworkConfig;

/**
 * @author Maestros
 * @co-author Voidstar
 */
public enum CustomMessageId {
	WELCOME_PREMIUM("Welcome Premium Member to %s server.\nPowered by " + getMaster() + ".\nSERVER RATES:\nExp Rate: %d\nQuest Rate: %d\nDrop Rate: %d"),
	WELCOME_REGULAR("Welcome to %s server.\nPowered by " + getMaster() + ".\nSERVER RATES:\nExp Rate: %d\nQuest Rate: %d\nDrop Rate: %d"),
	SERVER_REVISION("Server Revision: %-6s"),
	WELCOME_BASIC("Welcome On "),
	HOMEPAGE("Homepage:www.myserver.com"),
	TEAMSPEAK("Teamspeak3:ts3.myserver.com"),
	INFO1("WARNING: Using of third-party software (speed hack&Co.) will be punished with permanent ban (Ban MAC)"),
	INFO2("Note: Likewise, hacking will result in an immediate ban regardless of reasons behind it."),
	INFO3("Note: The advertising for other servers is prohibited.Breaking this rule will result a permanent ban!"),
	INFO4("Note: Our team will never ask you for your account password!"),
	INFO5("Chat: Use .faction, .ely, .asmo <text>, to write in your faction World chat."),
	INFO6("Tip: Use /1, /2 and /3 to use the other Chat Channels."),
	INFO7("Help: use the command: .help to see other available player commands"),
	SERVERVERSION("Supported NCSoft Version "),
	ENDMESSAGE("Have fun on: "),
	ANNOUNCE_GM_CONNECTION(" is now available for support!"),
	ANNOUNCE_GM_DECONNECTION(" is now unavailable for support!"),
	ANNOUNCE_MEMBER_CONNECTION("%s just entered into Atreia."),
	COMMAND_NOT_ENOUGH_RIGHTS("You dont have enough rights to execute this command"),
	PLAYER_NOT_ONLINE("The player %s is not online"),
	INTEGER_PARAMETER_REQUIRED("Parameter needs to be an integer"),
	INTEGER_PARAMETERS_ONLY("Parameters need to be only integers"),
	SOMETHING_WRONG_HAPPENED("Something wrong happened"),
	COMMAND_DISABLED("This command is disabled"),
	COMMAND_ADD_SYNTAX("Syntax: //add <player name> <item id> <quantity>"),
	COMMAND_ADD_ADMIN_SUCCESS("Item(s) successfully added to player %s"),
	COMMAND_ADD_PLAYER_SUCCESS("Admin %s gave you %d item(s)"),
	COMMAND_ADD_FAILURE("Item %d does not exists and/or cannot be added to %s"),
	COMMAND_ADDDROP_SYNTAX("Syntax: //adddrop <mob id> <item id> <min> <max> <chance>"),
	COMMAND_ADDSET_SYNTAX("Syntax: //addset <player name> <itemset id>"),
	COMMAND_ADDSET_SET_DOES_NOT_EXISTS("Item set with id %d does not exists"),
	COMMAND_ADDSET_NOT_ENOUGH_SLOTS("Inventory needs at least %d free slots"),
	COMMAND_ADDSET_CANNOT_ADD_ITEM("Item %d cannot be added to %s"),
	COMMAND_ADDSET_ADMIN_SUCCESS("Item set %d successfully added to %s"),
	COMMAND_ADDSET_PLAYER_SUCCESS("Admin %s gave you an item set"),
	COMMAND_ADDSKILL_SYNTAX("Syntax: //addskill <skill id> <skill level>"),
	COMMAND_ADDSKILL_ADMIN_SUCCESS("Skill %d was added to player %s with success"),
	COMMAND_ADDSKILL_PLAYER_SUCCESS("%s gave you a new skill"),
	COMMAND_ADDTITLE_SYNTAX("Syntax: //addtitle <title id> <player name> [special]"),
	COMMAND_ADDTITLE_TITLE_INVALID("Title %d is invalid, it must be between 1 and 50"),
	COMMAND_ADDTITLE_CANNOT_ADD_TITLE_TO_ME("You can't add title %d to yourself"),
	COMMAND_ADDTITLE_CANNOT_ADD_TITLE_TO_PLAYER("You can't add title %d to %s"),
	COMMAND_ADDTITLE_ADMIN_SUCCESS_ME("You added title %d to yourself with success"),
	COMMAND_ADDTITLE_ADMIN_SUCCESS("You added title %d to %s with success"),
	COMMAND_ADDTITLE_PLAYER_SUCCESS("Admin %s gave you title %d"),
	COMMAND_SEND_SYNTAX("Syntax: //send <filename>"),
	COMMAND_SEND_MAPPING_NOT_FOUND("Mapping %s not found"),
	COMMAND_SEND_NO_PACKET("No packet to send"),
	COMMAND_WRONG_SKILL_ID("Wrong skill id!"),
	COMMAND_ADDEFFECT_SYNTAX("Syntax: //addeffect <skillid> <duration>\nThis command adds specified effect to the target for a specified duration (in seconds). Leaving duration null will use the default skill duration."),
	COMMAND_ADDEFFECT_SUCCESS("Effect %s added to player %s for %d second(s)."),
	COMMAND_DELSKILL_SYNTAX("Syntax: //delsskill <skill id>"),
	COMMAND_DELSKILL_ADMIN_SUCCESS("Skill %d was deleted from player %s with success"),
	COMMAND_DELSKILL_PLAYER_SUCCESS("%s removed from you Skill %d"),
	CHANNEL_WORLD_DISABLED("The channel %s is disabled, please use channel %s or %s according to your faction"),
	CHANNEL_ALL_DISABLED("The custom channels are disabled"),
	CHANNEL_ALREADY_FIXED("Your channel is already fixed on %s"),
	CHANNEL_FIXED("Your chat is now fixed on %s"),
	CHANNEL_NOT_ALLOWED("You're not allowed to speak on this channel"),
	CHANNEL_FIXED_BOTH("Your chat is now fixed on %s and %s"),
	CHANNEL_UNFIX_HELP("Type %s unfix to release your chat"),
	CHANNEL_NOT_FIXED("Your chat isn't fixed on any channel"),
	CHANNEL_FIXED_OTHER("Your chat is not fixed on this channel, but on %s"),
	CHANNEL_RELEASED("Your chat has been released from %s channel"),
	CHANNEL_RELEASED_BOTH("Your chat has been released from %s and %s channels"),
	CHANNEL_BAN_ENDED("You're not anymore banned from chat channels"),
	CHANNEL_BAN_ENDED_FOR("The player %s is no more banned from chat channels"),
	CHANNEL_BANNED("You can't use chat channels because %s banned you for following reason: %s, remaining time: %s"),
	COMMAND_MISSING_SKILLS_STIGMAS_ADDED("%d basic skills and %d stigma skills has been added to you"),
	COMMAND_MISSING_SKILLS_ADDED("%d basic skills has been added to you"),
	USER_COMMAND_DOES_NOT_EXIST("This user command does not exist"),
	COMMAND_XP_DISABLED("Your experience gain has been disabled. Type .xpon to re-enable"),
	COMMAND_XP_ALREADY_DISABLED("Your experience gain is already disabled"),
	COMMAND_XP_ENABLED("Your experience gain has been enabled"),
	COMMAND_XP_ALREADY_ENABLED("Your experience gain is already enabled"),
	COMMAND_ADDCUBE_SYNTAX("Syntax: //addcube <player name>"),
	COMMAND_ADDCUBE_ADMIN_SUCCESS("9 cube slots successfully added to player %s!"),
	COMMAND_ADDCUBE_PLAYER_SUCCESS("Admin %s gave you a cube expansion!"),
	COMMAND_ADDCUBE_FAILURE("Cube expansion cannot be added to %s!\nReason: player cube already fully expanded."),
	DREDGION_LEVEL_TOO_LOW("Your level is too low to enter the Dredgion."),
	DEFAULT_FINISH_MESSAGE("Finish!"),
	/**
	 * Asmo and Ely Channel
	 */
	ASMO_FAIL("You are Elyos! You can't use this chat. Please use .ely <message> to use you're faction chat!"),
	ELY_FAIL("You are Asmo! You can't use this chat. Please use .asmo <message> to use you're faction chat!"),
	/**
	 * PvP Service
	 */
	ADV_WINNER_MSG("[PvP System] You kill player "),
	ADV_LOSER_MSG("[PvP System] You killed by "),
	PLAP_LOST1("[PL-AP] You lost "),
	PLAP_LOST2("% of your total ap"),
	PVP_TOLL_REWARD1("You have "),
	PVP_TOLL_REWARD2(" Toll earned."),
	PVP_NO_REWARD1("You dont won anything for killing "),
	PVP_NO_REWARD2(" because you killed him too often!"),
	EASY_MITHRIL_MSG("[Mithril System] du bekamst:"),
	/**
	 * Reward Service Login Messages
	 */
	REWARD10("You can use .start to get an level 10 set!"),
	REWARD30("You can use .start to get an level 30 set!"),
	REWARD40("You can use .start to get an level 40 set!"),
	REWARD50("You can use .start to get an level 50 set!"),
	REWARD60("You can use .start to get an level 60 set!"),
	/**
	 * Advanced PvP System
	 */
	PVP_ADV_MESSAGE1("Today PvP Map: Reshanta"),
	PVP_ADV_MESSAGE2("Today PvP Map: Tiamaranta"),
	PVP_ADV_MESSAGE3("Today PvP Map: Gelkmaros"),
	PVP_ADV_MESSAGE4("Today PvP Map: Inggison"),
	PVP_ADV_MESSAGE5("Today PvP Map: Katalam"),
	PVP_ADV_MESSAGE6("Today PvP Map: Danaria"),
	/**
	 * Wedding related
	 */
	WEDDINGNO1("You can't use this command on fighting state!"),
	WEDDINGNO2("Wedding not started!"),
	WEDDINGNO3("You didn't accept the marry request!"),
	WEDDINGYES("You have accept the marry request!"),
	/**
	 * Clean Command related
	 */
	CANNOTCLEAN("You must enter a item id or an item link!"),
	CANNOTCLEAN2("You don't have this item!"),
	SUCCESSCLEAN("Item was successfully removed from you're cube!"),
	/**
	 * Mission check command related
	 */
	SUCCESCHECKED("Mission successfully checked!"),
	/**
	 * No Exp Command
	 */
	EPACTIVATED("You're exp gain re-activated!"),
	ACTODE("To disable you're exp gain, use .noexp"),
	EPDEACTIVATED("You're exp gain deactivated!"),
	DETOAC("To activate you're exp gain, use .noexp"),
	/**
	 * Auto Quest Command
	 */
	WRONGQID("That's an wrong quest id!"),
	NOTSTARTED("Quest could'nt be started!"),
	NOTSUPPORT("This Quest is not supported from this system!"),
	/**
	 * Quest Restart Command
	 */
	CANNOTRESTART("] cannot be restarted"),
	/**
	 * Exchange Toll Command
	 */
	TOLLTOBIG("You have to much toll!"),
	TOLOWAP("You don't have enough AP!"),
	TOLOWTOLL("You don't have enough Toll!"),
	WRONGTOLLNUM("Something went wrong!"),
	/**
	 * Luna System
	 */
	LUNATOBIG("You have to much Luna Coins!"),
	TOLOWLUNA("You don't have enough Luna Coins!"),
	WRONGLUNANUM("Something went wrong!"),
	/**
	 * Cube Command
	 */
	CUBE_ALLREADY_EXPANDED("You're cube is allready fully expanded!"),
	CUBE_SUCCESS_EXPAND("You're cube was successfully expanded!"),
	/**
	 * GMList Command
	 */
	ONE_GM_ONLINE("There's one team member online: "),
	MORE_GMS_ONLINE("There are team member online: "),
	NO_GM_ONLINE("There's no team member online!"),
	/**
	 * Go Command (PvP Command)
	 */
	NOT_USE_WHILE_FIGHT("You cannot use this command while fighting!"),
	NOT_USE_ON_PVP_MAP("You cannot use this command on an pvp map!"),
	LEVEL_TOO_LOW("You can only use this command if you're level 55 or higher!"),
	/**
	 * Paint Command
	 */
	WRONG_TARGET("Please use an correct target!"),
	/**
	 * Enchant Command
	 */
	ENCHANT_SUCCES("All you're equiped items were enchanted to: "),
	ENCHANT_INFO(" Info: This command enchant all your equiped items to <value>!"),
	ENCHANT_SAMPLE(" Sample: (.eq 15) would enchant all your equiped items to 15"),
	/**
	 * Userinfo Command
	 */
	CANNOT_SPY_PLAYER("Don't take info's from other players!"),
	/**
	 * Custom BossSpawnService
	 */
	CUSTOM_BOSS_SPAWNED(" was spawned in "),
	CUSTOM_BOSS_KILLED(" was killed from "),
	CUSTOM_BOSS_NAME1("Awaked Dragon"),
	CUSTOM_BOSS_NAME2("Shadowshift"),
	CUSTOM_BOSS_NAME3("Flamestorm"),
	CUSTOM_BOSS_NAME4("Thunderstorm"),
	CUSTOM_BOSS_NAME5("Terra-Explosion"),
	CUSTOM_BOSS_NAME6("The Canyonguard"),
	CUSTOM_BOSS_NAME7("Padmarashka"),
	CUSTOM_BOSS_NAME8("Sematariux"),
	CUSTOM_BOSS_NAME9("Cedella"),
	CUSTOM_BOSS_NAME10("Tarotran"),
	CUSTOM_BOSS_NAME11("Raksha Boilheart"),
	CUSTOM_BOSS_NAME12("Big Red Orb"),
	CUSTOM_BOSS_UNK("Unknown NPC"),
	CUSTOM_BOSS_LOC1("Beluslan"),
	CUSTOM_BOSS_LOC2("Morheim"),
	CUSTOM_BOSS_LOC3("Eltnen"),
	CUSTOM_BOSS_LOC4("Heiron"),
	CUSTOM_BOSS_LOC5("Inggison"),
	CUSTOM_BOSS_LOC6("Gelkmaros"),
	CUSTOM_BOSS_LOC7("Silentera Canyon"),
	CUSTOM_BOSS_LOC8("Padmarashkas Cave"),
	CUSTOM_BOSS_LOC9("Sarpan"),
	CUSTOM_BOSS_LOC10("Tiamaranta"),
	CUSTOM_BOSS_LOC_UNK(""),
	/**
	 * Battleground System
	 */
	BATTLEGROUND_MESSAGE1("You have earned %d battleground points."),
	BATTLEGROUND_MESSAGE2("You have lost %d battleground points."),
	BATTLEGROUND_MESSAGE3("The Battleground: %s is now ready to start. You will be teleported in 30 seconds."),
	BATTLEGROUND_MESSAGE4("seconds before starting ..."),
	BATTLEGROUND_MESSAGE5("You are now invisible."),
	BATTLEGROUND_MESSAGE6("You are now immortal."),
	BATTLEGROUND_MESSAGE7("The battleground is now open!"),
	BATTLEGROUND_MESSAGE8("The battleground will end in 30 seconds !"),
	BATTLEGROUND_MESSAGE9("Betting time has now ended."),
	BATTLEGROUND_MESSAGE10("The battle has now ended! Click the survey to show the rank board. If you are dead, use the spell Return and you will be teleported back."),
	BATTLEGROUND_MESSAGE11("You are now visible."),
	BATTLEGROUND_MESSAGE12("You are now mortal."),
	BATTLEGROUND_MESSAGE13("Do you want to join a battleground again?"),
	BATTLEGROUND_MESSAGE14("You are already registered in a battleground."),
	BATTLEGROUND_MESSAGE15("Use your spell Return to leave the battleground."),
	BATTLEGROUND_MESSAGE16("You are already registered in a battleground."),
	BATTLEGROUND_MESSAGE17("Use the command .bg unregister to cancel your registration."),
	BATTLEGROUNDMANAGER_MESSAGE1("Only group leader can register group."),
	BATTLEGROUNDMANAGER_MESSAGE2("You are now registered for the battleground: %s"),
	BATTLEGROUNDMANAGER_MESSAGE3("Please wait while your team is in formation..."),
	BATTLEGROUNDMANAGER_MESSAGE4("All members can't join this battleground."),
	BATTLEGROUNDMANAGER_MESSAGE5("You are too many to register in this battleground now."),
	BATTLEGROUNDMANAGER_MESSAGE6("You are now registered for the battleground:%s"),
	BATTLEGROUNDMANAGER_MESSAGE7("Please wait while your team is in formation..."),
	BATTLEGROUNDMANAGER_MESSAGE8("All members can't join this battleground."),
	BATTLEGROUNDMANAGER_MESSAGE9("You are too many to register in this battleground now."),
	BATTLEGROUNDMANAGER_MESSAGE10("Some members are already registered in a battleground."),
	BATTLEGROUNDMANAGER_MESSAGE11("You are now registered for the battleground: %s"),
	BATTLEGROUNDMANAGER_MESSAGE12("Please wait while your team is in formation..."),
	BATTLEGROUNDMANAGER_MESSAGE13("You are now registered to observe the battleground: %s"),
	BATTLEGROUNDMANAGER_MESSAGE14("Please wait until the battleground is full..."),
	BATTLEGROUNDMANAGER_MESSAGE15("No battleground available for you with your level and your battleground points."),
	BATTLEGROUNDMANAGER_MESSAGE16("Register to battlegrounds"),
	BATTLEGROUNDMANAGER_MESSAGE016("You can register for the following battlegrounds :"),
	BATTLEGROUNDMANAGER_MESSAGE17("No battleground available."),
	BATTLEGROUNDMANAGER_MESSAGE18("Register to battlegrounds"),
	BATTLEGROUNDMANAGER_MESSAGE018("You can register for the following battlegrounds :"),
	BATTLEGROUNDHEALERCONTROLLER_1("You were alone in the battleground, you have been teleported back."),
	BATTLEGROUNDHEALERCONTROLLER_2("You were alone in the battleground, you have been teleported back."),
	BATTLEGROUNDAGENTCONTROLLER_1("You are already registered in a battleground."),
	BATTLEGROUNDFLAGCONTROLLER_2("unhandled case."),
	BATTLEGROUNDFLAGCONTROLLER_3("Do you want to register in a battleground ?"),

	COMMAND_BATTLEGROUND_MESSAGE0("You cannot register for battlegrounds while you are in prison."),
	COMMAND_BATTLEGROUND_MESSAGE1("You are already in a battleground."),
	COMMAND_BATTLEGROUND_MESSAGE2("Use your spell Return to leave the battleground."),
	COMMAND_BATTLEGROUND_MESSAGE3("You are already registered in a battleground."),
	COMMAND_BATTLEGROUND_MESSAGE4("Use the command .bg unregister to cancel your registration."),
	COMMAND_BATTLEGROUND_MESSAGE5("You are already registered in the Arena Team waiting list."),
	COMMAND_BATTLEGROUND_MESSAGE6("Use the command .arena to cancel your registration."),
	COMMAND_BATTLEGROUND_MESSAGE7("You are already registered in the Arena Team waiting list."),
	COMMAND_BATTLEGROUND_MESSAGE8("Use your spell Return to leave the Arena Team Battle."),
	COMMAND_BATTLEGROUND_MESSAGE9("You are already in a battleground."),
	COMMAND_BATTLEGROUND_MESSAGE10("Use your spell Return to leave the battleground."),
	COMMAND_BATTLEGROUND_MESSAGE11("You are already registered in a battleground."),
	COMMAND_BATTLEGROUND_MESSAGE12("Use the command .bg unregister to cancel your registration."),
	COMMAND_BATTLEGROUND_MESSAGE13("You are playing in a battleground, not an observer."),
	COMMAND_BATTLEGROUND_MESSAGE14("You are now visible."),
	COMMAND_BATTLEGROUND_MESSAGE15("You are now mortal."),
	COMMAND_BATTLEGROUND_MESSAGE16("You have lost your bet of %d kinah."),
	COMMAND_BATTLEGROUND_MESSAGE17("You are not observing any battleground."),
	COMMAND_BATTLEGROUND_MESSAGE18("You are not registered in any battleground or the battleground is over."),
	COMMAND_BATTLEGROUND_MESSAGE19("The exchange tool is not available."),
	COMMAND_BATTLEGROUND_MESSAGE20("The exchange rate is 1 BG point for 3 Abyss points."),
	COMMAND_BATTLEGROUND_MESSAGE21("To exchange some points, write .bg exchange <bg_points_number>"),
	COMMAND_BATTLEGROUND_MESSAGE22("You don't have enough BG points."),
	COMMAND_BATTLEGROUND_MESSAGE23("You have lost %d BG points."),
	COMMAND_BATTLEGROUND_MESSAGE24("Syntax error. Use .bg exchange <bg_points_number>."),
	COMMAND_BATTLEGROUND_MESSAGE25("You are not registered in any battleground."),
	COMMAND_BATTLEGROUND_MESSAGE26("Registration canceled."),
	COMMAND_BATTLEGROUND_MESSAGE27(".bg register : register in a BG"),
	COMMAND_BATTLEGROUND_MESSAGE28(".bg observe : observe a battleground"),
	COMMAND_BATTLEGROUND_MESSAGE29(".bg unregister : unregister from the BG (before starting)"),
	COMMAND_BATTLEGROUND_MESSAGE30(".bg stop : stop observe and back to home"),
	COMMAND_BATTLEGROUND_MESSAGE31(".bg rank : see your rank during a BG"),
	COMMAND_BATTLEGROUND_MESSAGE32(".bg points : : to see your points"),
	COMMAND_BATTLEGROUND_MESSAGE33(".bet : bet on a faction during observe mode"),
	COMMAND_BATTLEGROUND_MESSAGE34("This command doesn't exist, use .bg help"),
	COMMAND_BATTLEGROUND_MESSAGE35("register"),
	COMMAND_BATTLEGROUND_MESSAGE36("observe"),
	COMMAND_BATTLEGROUND_MESSAGE37("stop"),
	COMMAND_BATTLEGROUND_MESSAGE38("rank"),
	COMMAND_BATTLEGROUND_MESSAGE39("stat"),
	COMMAND_BATTLEGROUND_MESSAGE40("exchange"),
	COMMAND_BATTLEGROUND_MESSAGE41("unregister"),
	COMMAND_BATTLEGROUND_MESSAGE42("help"),
	COMMAND_BATTLEGROUND_MESSAGE43("points"),
	/**
	 * Exchange Command
	 */
	NOT_ENOUGH_ITEM("You dont have enough from: "),
	NOT_ENOUGH_AP("You dont have enough ap, you only have: "),
	/**
	 * Medal Command
	 */
	NOT_ENOUGH_SILVER("You dont have enough silver medals."),
	NOT_ENOUGH_GOLD("You dont have enough gold medals."),
	NOT_ENOUGH_PLATIN("You dont have enough platin medals."),
	NOT_ENOUGH_MITHRIL("You dont have enough mithril medals."),
	NOT_ENOUGH_AP2("You dont have enough ap, you need: "),
	EXCHANGE_SILVER("You have exchange [item:186000031] to [item:186000030]."),
	EXCHANGE_GOLD("You have exchange [item:186000030] to [item:186000096]."),
	EXCHANGE_PLATIN("You have exchange [item:186000096] to [item:186000147]."),
	EXCHANGE_MITHRIL("You have exchange [item:186000147] to [item:186000223]."),
	EX_SILVER_INFO("\nSyntax: //medal silver - Exchange Silver to Gold."),
	EX_GOLD_INFO("\nSyntax: //medal gold - Exchange Gold to Platin."),
	EX_PLATIN_INFO("\nSyntax: //medal platinum - Exchnage Platin to Mithril."),
	EX_MITHRIL_INFO("\nSyntax: //medal mithril - Exchange Mithril to Honorable Mithril."),
	/**
	 * Dimensional Vortex
	 */
	DIM_VORTEX_SPAWNED_ELYOS("The Dimensional Vortex was opened for Elyos!"),
	DIM_VORTEX_SPAWNED_ASMO("The Dimensional Vortex was opened for Asmodians!"),
	DIM_VORTEX_DESPAWNED("The Dimensional Assault finished!"),

	/**
	 * Legendary Raid Spawn Events
	 */
	LEGENDARY_RAID_SPAWNED_ASMO("[Legendary Raid Spawn Event] Ragnarok was spawned for Asmodians at Beluslan!"),
	LEGENDARY_RAID_SPAWNED_ELYOS("[Legendary Raid Spawn Event] Omega was spawned for Elyos at Heiron!"),
	LEGENDARY_RAID_DESPAWNED_ASMO("[Legendary Raid Spawn Event] Ragnarok was unspawned, nobody kill him!"),
	LEGENDARY_RAID_DESPAWNED_ELYOS("[Legendary Raid Spawn Event] Omega was unspawned, nobody kill him!"),

	/**
	 * HonorItems Command
	 */
	PLATE_ARMOR("Plate Armor"),
	LEATHER_ARMOR("Leather Armor"),
	CLOTH_ARMOR("Cloth Armor"),
	CHAIN_ARMOR("Chain Armor"),
	WEAPONS("Weapons"),
	PLATE_ARMOR_PRICES("Plate Armor Prices"),
	LEATHER_ARMOR_PRICES("Leather Armor Prices"),
	CLOTH_ARMOR_PRICES("Cloth Armor Prices"),
	CHAIN_ARMOR_PRICES("Chain Armor Prices"),
	WEAPONS_PRICES("Weapons Prices"),
	NOT_ENOUGH_MEDALS("You dont have enough Medals, you need: "),
	PLATE_ARMOR_USE_INFO("Use .items and the equal ID (Example: .items 1"),
	LEATHER_ARMOR_USE_INFO("Use .items and the equal ID (Example: .items 6"),
	CLOTH_ARMOR_USE_INFO("Use .items and the equal ID (Example: .items 11"),
	CHAIN_ARMOR_USE_INFO("Use .items and the equal ID (Example: .items 16"),
	WEAPONS_USE_INFO("Use .items and the equal ID (Example: .items 21"),
	SUCCESSFULLY_TRADED("You got successfully your Trade!"),
	/**
	 * PvP Arena Announce
	 */
	PVP_ARENA_WEEKEND("PvP Arena are now open (10am to 2am)."),
	PVP_ARENA_WEEK("PvP Arena are now open for 2 hours."),
	/**
	 * Check AFK Status
	 */
	KICKED_AFK_OUT("You have been kicked out for being inactive too long."),
	/**
	 * Moltenus Announce
	 */
	MOLTENUS_APPEAR("Moltenus Fragment of the anger has spawn in the Abyss."),
	MOLTENUS_DISAPPEAR("Moltenus Fragment of anger gone."),
	/**
	 * Dredgion Announce
	 */
	DREDGION_ASMO_GROUP("An asmodian group is waiting for dredgion."),
	DREDGION_ELYOS_GROUP("An elyos group is waiting for dredgion."),
	DREDGION_ASMO("An alone asmodian is waiting for dredgion."),
	DREDGION_ELYOS("An alone elyos is waiting for dredgion."),
	/**
	 * GM Announce
	 */
	TAG_1(" [Supporter] "),
	TAG_2(" [Junior-GM] "),
	TAG_3(" [Senior-GM] "),
	TAG_4(" [Head-GM] "),
	TAG_5(" [Admin] "),
	TAG_6(" [Developer] "),
	TAG_7(" [Server-CoAdmin] "),
	TAG_8(" [Server-Admin] "),
	TAG_9(" (Server-CoOwner) "),
	TAG_10(" [Server-Owner] "),
	/**
	 * Shugo Imperial Tomb Event
	 */
	SHUGO_TOMB_OPEN("The Shugo Imperial Tomb Event instance is now started"),
	SHUGO_TOMB_CLOSE("The Shugo Imperial Tomb Event instance is now closed"),
	/**
	 * Guardian General Showdown Event
	 */
	GUARDIAND_SHOWDOWN_SPAWN("[Event] The Guardian General Showdown is now started"),
	GUARDIAND_SHOWDOWN_DESPAWN("[Event] The Guardian General Showdown is now finished"),
	/**
	 * Invasion Rift
	 */
	INVASION_RIFT_MIN_LEVEL("Your level is too low to enter."),
	INVASION_RIFT_ELYOS("A rift for Pandaemonium is open at Ingisson"),
	INVASION_RIFT_ASMOS("A rift for Sanctum is open at Gelkmaros"),
	/**
	 * Additional Chest Drops
	 */
	DECOMPOSE_SERVICE_MESSAGE1("%s has obtained %s from %s."),
	DECOMPOSE_SERVICE_MESSAGE2("%s has obtained additional %s from %s (Premium)."),
	DECOMPOSE_SERVICE_MESSAGE3("%s has obtained additional %s from %s (VIP)."),
	/**
	 * PvP Spree Service
	 */
	SPREE1("Bloody Storm"),
	SPREE2("Carnage"),
	SPREE3("Genocide"),
	KILL_COUNT("Kills in a row: "),
	CUSTOM_MSG1(" of "),
	MSG_SPREE1(" has started a "),
	MSG_SPREE1_1(" !"),
	MSG_SPREE2(" is performing a true "),
	MSG_SPREE2_1(" ! Stop him fast !"),
	MSG_SPREE3(" is doing a "),
	MSG_SPREE3_1(" ! Run away if you can!"),
	SPREE_END_MSG1("The killing spree of "),
	SPREE_END_MSG2(" has been stopped by "),
	SPREE_END_MSG3(" after "),
	SPREE_END_MSG4(" uninterrupted murders !"),
	SPREE_MONSTER_MSG("a monster");

	private String fallbackMessage;

	private CustomMessageId(String fallbackMessage) {
		this.fallbackMessage = fallbackMessage;
	}

	public String getFallbackMessage() {
		return fallbackMessage;
	}

	public static String getMaster() {
		if (NetworkConfig.GAME_BIND_ADDRESS.equals(NetworkConfig.GAME_BIND_ADDRESS)) {
			return "";
		}
		return "Emulator";
	}
}
