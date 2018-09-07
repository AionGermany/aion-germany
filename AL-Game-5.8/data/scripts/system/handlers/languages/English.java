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

package languages;

import com.aionemu.gameserver.utils.i18n.CustomMessageId;
import com.aionemu.gameserver.utils.i18n.Language;

/**
 * @author Eloann
 * @reworked Voidstar
 */
public class English extends Language {

	public English() {
		super("en");
		addSupportedLanguage("en_EN");
		addTranslatedMessage(CustomMessageId.SERVER_REVISION, "Server Version : %-6s");
		addTranslatedMessage(CustomMessageId.WELCOME_PREMIUM, "Welcome To ");
		addTranslatedMessage(CustomMessageId.WELCOME_REGULAR, "Welcome To ");
		addTranslatedMessage(CustomMessageId.WELCOME_BASIC, "Welcome To ");
		/**
		 * Disabled on English, as they comes already from CustomMessageId.java. addTranslatedMessage(CustomMessageId.INFO1, "WARNING: Using of third-party software (speed hack&Co.) will be punished
		 * with permanent ban (Ban MAC)" ); addTranslatedMessage(CustomMessageId.INFO2, "Note: Likewise, hacking will result in an immediate ban regardless of reasons behind it." );
		 * addTranslatedMessage(CustomMessageId.INFO3, "Note: The advertising for other servers is prohibited.Breaking this rule will result a permanent ban!" );
		 * addTranslatedMessage(CustomMessageId.INFO4, "Note: Our team will never ask you for your account password!"); addTranslatedMessage(CustomMessageId.INFO5, "Chat: Use .faction, .ely, .asmo
		 * <text>, to write in your faction World chat." ); addTranslatedMessage(CustomMessageId.INFO6, "Tip: Use /1, /2 and /3 to use the other Chat Channels.");
		 * addTranslatedMessage(CustomMessageId.INFO7, "Help: use the command: .help to see other available player commands." );
		 */
		addTranslatedMessage(CustomMessageId.SERVERVERSION, "Supported NCSoft Version ");
		addTranslatedMessage(CustomMessageId.ENDMESSAGE, "Have fun.");
		addTranslatedMessage(CustomMessageId.ANNOUNCE_GM_CONNECTION, " is now available for support!");
		addTranslatedMessage(CustomMessageId.ANNOUNCE_GM_DECONNECTION, " is now unavailable for support!");
		addTranslatedMessage(CustomMessageId.ANNOUNCE_MEMBER_CONNECTION, "%s just entered into Atreia.");
		addTranslatedMessage(CustomMessageId.COMMAND_NOT_ENOUGH_RIGHTS, "You not have rights to use this command");
		addTranslatedMessage(CustomMessageId.PLAYER_NOT_ONLINE, "Player not online");
		addTranslatedMessage(CustomMessageId.INTEGER_PARAMETER_REQUIRED, "Integer parameter required");
		addTranslatedMessage(CustomMessageId.INTEGER_PARAMETERS_ONLY, "Integer parameters only");
		addTranslatedMessage(CustomMessageId.SOMETHING_WRONG_HAPPENED, "Somthing wrong happened");
		addTranslatedMessage(CustomMessageId.COMMAND_DISABLED, "Command disabled");
		addTranslatedMessage(CustomMessageId.COMMAND_ADD_SYNTAX, "Syntax: //add <player name> <itemid> [<amount>]");
		addTranslatedMessage(CustomMessageId.COMMAND_ADD_ADMIN_SUCCESS, "Item success added to player %s");
		addTranslatedMessage(CustomMessageId.COMMAND_ADD_PLAYER_SUCCESS, "Administrator %s give you %d item(s)");
		addTranslatedMessage(CustomMessageId.COMMAND_ADD_FAILURE, "Item %d not exist or cant be added %s");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDDROP_SYNTAX, "Syntax: //adddrop <npc id> <itemid> <min> <max> <chance>");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDSET_SYNTAX, "Syntax: //addset <player name> <id set>");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDSET_SET_DOES_NOT_EXISTS, "Set %d not exist");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDSET_NOT_ENOUGH_SLOTS, "Not enough inventory %d slots to add this set");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDSET_CANNOT_ADD_ITEM, "Item %d can not be added to the %s");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDSET_ADMIN_SUCCESS, "Set %d added success %s");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDSET_PLAYER_SUCCESS, "%s give you set");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDSKILL_SYNTAX, "Syntax: //addskill <skill id> <skill lvl");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDSKILL_ADMIN_SUCCESS, "Skill %d added success %s");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDSKILL_PLAYER_SUCCESS, "%s give you skill");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDTITLE_SYNTAX, "Syntax: //addtitle <title id> <player name> [special]");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDTITLE_TITLE_INVALID, "Title must be from 1 to 50");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDTITLE_CANNOT_ADD_TITLE_TO_ME, "Cannot add title %d self");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDTITLE_CANNOT_ADD_TITLE_TO_PLAYER, "Cannot add title %d to %s");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDTITLE_ADMIN_SUCCESS_ME, "Title %d added success self");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDTITLE_ADMIN_SUCCESS, "You success added title %d to player %s");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDTITLE_PLAYER_SUCCESS, "%s give you title %d");
		addTranslatedMessage(CustomMessageId.COMMAND_SEND_SYNTAX, "Syntax: //send <file name>");
		addTranslatedMessage(CustomMessageId.COMMAND_SEND_MAPPING_NOT_FOUND, "%s not found");
		addTranslatedMessage(CustomMessageId.COMMAND_SEND_NO_PACKET, "Send no packet");
		addTranslatedMessage(CustomMessageId.CHANNEL_WORLD_DISABLED, "Channel %s disabled, use channel %s or %s based on your race");
		addTranslatedMessage(CustomMessageId.CHANNEL_ALL_DISABLED, "All channels disabled");
		addTranslatedMessage(CustomMessageId.CHANNEL_ALREADY_FIXED, "Your channel has been successfully installed %s");
		addTranslatedMessage(CustomMessageId.CHANNEL_FIXED, "Installed channel %s");
		addTranslatedMessage(CustomMessageId.CHANNEL_NOT_ALLOWED, "You cant use this channel");
		addTranslatedMessage(CustomMessageId.CHANNEL_FIXED_BOTH, "Installed channels %s and %s");
		addTranslatedMessage(CustomMessageId.CHANNEL_UNFIX_HELP, "Write %s to come out of the channel"); // ;)
		addTranslatedMessage(CustomMessageId.CHANNEL_NOT_FIXED, "You are not installed on the channel");
		addTranslatedMessage(CustomMessageId.CHANNEL_FIXED_OTHER, "Your chat is not installed on this channel, but on %s");
		addTranslatedMessage(CustomMessageId.CHANNEL_RELEASED, "You come out of the channel %s");
		addTranslatedMessage(CustomMessageId.CHANNEL_RELEASED_BOTH, "You are out of %s and %s");
		addTranslatedMessage(CustomMessageId.CHANNEL_BAN_ENDED, "You can rejoin the channels");
		addTranslatedMessage(CustomMessageId.CHANNEL_BAN_ENDED_FOR, "Player %s may again join the channel");
		addTranslatedMessage(CustomMessageId.CHANNEL_BANNED, "You can not access the channel, because %s ban you because of: %s, left to unlock: %s");
		addTranslatedMessage(CustomMessageId.COMMAND_MISSING_SKILLS_STIGMAS_ADDED, "%d skill %d stigma given to you");
		addTranslatedMessage(CustomMessageId.COMMAND_MISSING_SKILLS_ADDED, "%d ability given to you");
		addTranslatedMessage(CustomMessageId.USER_COMMAND_DOES_NOT_EXIST, "This game command exists");
		addTranslatedMessage(CustomMessageId.COMMAND_XP_DISABLED, "Accrual XP disabled. Enter. xpon to unlock");
		addTranslatedMessage(CustomMessageId.COMMAND_XP_ALREADY_DISABLED, "Accrual XP disabled");
		addTranslatedMessage(CustomMessageId.COMMAND_XP_ENABLED, "Accrual XP enabled");
		addTranslatedMessage(CustomMessageId.COMMAND_XP_ALREADY_ENABLED, "Accrual XP already enabled");
		addTranslatedMessage(CustomMessageId.DREDGION_LEVEL_TOO_LOW, "Your current level is too low to enter the Dredgion.");
		addTranslatedMessage(CustomMessageId.DEFAULT_FINISH_MESSAGE, "Finish!");

		/**
		 * Asmo and Ely Channel
		 */
		addTranslatedMessage(CustomMessageId.ASMO_FAIL, "You are Elyos! You can not use this chat. Ely <message> to post a new faction chat!");
		addTranslatedMessage(CustomMessageId.ELY_FAIL, "You are Asmo! You can not use this chat. Asmo <message> to post a new faction chat!");

		/**
		 * PvP Service
		 */
		addTranslatedMessage(CustomMessageId.ADV_WINNER_MSG, "[PvP System] You kill player ");
		addTranslatedMessage(CustomMessageId.ADV_LOSER_MSG, "[PvP System] You killed by ");
		addTranslatedMessage(CustomMessageId.PLAP_LOST1, "[PL-AP] You lost ");
		addTranslatedMessage(CustomMessageId.PLAP_LOST2, "% of your total ap");
		addTranslatedMessage(CustomMessageId.PVP_NO_REWARD1, "You dont won anything for killing ");
		addTranslatedMessage(CustomMessageId.PVP_NO_REWARD2, " because you killed him too often!");

		/**
		 * Reward Service Login Messages
		 */
		addTranslatedMessage(CustomMessageId.REWARD10, "You can. Start using a level to get 10 Features!");
		addTranslatedMessage(CustomMessageId.REWARD30, "You can. Start to use a Level 30 Equipment get!");
		addTranslatedMessage(CustomMessageId.REWARD40, "You can. Start using a level to get 40 Features!");
		addTranslatedMessage(CustomMessageId.REWARD50, "You can. Start a level use 50 features to get!");
		addTranslatedMessage(CustomMessageId.REWARD60, "You can. Start to use a Level 60 Features get!");

		/**
		 * Advanced PvP System
		 */
		addTranslatedMessage(CustomMessageId.PVP_ADV_MESSAGE1, "Today PvP Map: Reshanta");
		addTranslatedMessage(CustomMessageId.PVP_ADV_MESSAGE2, "Today PvP Map: Tiamaranta");
		addTranslatedMessage(CustomMessageId.PVP_ADV_MESSAGE3, "Today PvP Map: Inggison/Gelkmaros");
		addTranslatedMessage(CustomMessageId.PVP_ADV_MESSAGE4, "Today PvP Map: Idian Depths");
		addTranslatedMessage(CustomMessageId.PVP_ADV_MESSAGE5, "Today PvP Map: Katalam");
		addTranslatedMessage(CustomMessageId.PVP_ADV_MESSAGE6, "Today PvP Map: Danaria");

		/**
		 * Wedding related
		 */
		addTranslatedMessage(CustomMessageId.WEDDINGNO1, "You can not use this command during the fight!");
		addTranslatedMessage(CustomMessageId.WEDDINGNO2, "Wedding has not started!");
		addTranslatedMessage(CustomMessageId.WEDDINGNO3, "You refused to marry!");
		addTranslatedMessage(CustomMessageId.WEDDINGYES, "You have accepted the marriage!");

		/**
		 * Clean Command related
		 */
		addTranslatedMessage(CustomMessageId.CANNOTCLEAN, "You have to enter an Item ID, or post a link!");
		addTranslatedMessage(CustomMessageId.CANNOTCLEAN2, "You do not own this item!");
		addTranslatedMessage(CustomMessageId.SUCCESSCLEAN, "Item has been successfully removed from a cube!");

		/**
		 * Mission check command related
		 */
		addTranslatedMessage(CustomMessageId.SUCCESCHECKED, "Mission successfully checked!");

		/**
		 * No Exp Command
		 */
		addTranslatedMessage(CustomMessageId.EPACTIVATED, "Your EP were re-activated!");
		addTranslatedMessage(CustomMessageId.ACTODE, "To disable your EP, use noexp.");
		addTranslatedMessage(CustomMessageId.EPDEACTIVATED, "Your EP were disabled!");
		addTranslatedMessage(CustomMessageId.DETOAC, "To activate your EP, use noexp.");

		/**
		 * Auto Quest Command
		 */
		addTranslatedMessage(CustomMessageId.WRONGQID, "Please enter a correct quest Id!");
		addTranslatedMessage(CustomMessageId.NOTSTARTED, "Quest could not be started!");
		addTranslatedMessage(CustomMessageId.NOTSUPPORT, "This quest is not supported by this command!");

		/**
		 * Quest Restart Command
		 */
		addTranslatedMessage(CustomMessageId.CANNOTRESTART, "] can not be restarted");

		/**
		 * Exchange Toll Command
		 */
		addTranslatedMessage(CustomMessageId.TOLLTOBIG, "You have too many Toll!");
		addTranslatedMessage(CustomMessageId.TOLOWAP, "You do not have enough AP!");
		addTranslatedMessage(CustomMessageId.TOLOWTOLL, "You do not have enough Toll!");
		addTranslatedMessage(CustomMessageId.WRONGTOLLNUM, "Something went wrong!");

		/**
		 * Cube Command
		 */
		addTranslatedMessage(CustomMessageId.CUBE_ALLREADY_EXPANDED, "Your cube is fully extended!");
		addTranslatedMessage(CustomMessageId.CUBE_SUCCESS_EXPAND, "Your cube is successfully expanded!");

		/**
		 * GMList Command
		 */
		addTranslatedMessage(CustomMessageId.ONE_GM_ONLINE, "A team member is online: ");
		addTranslatedMessage(CustomMessageId.MORE_GMS_ONLINE, "The following team members online: ");
		addTranslatedMessage(CustomMessageId.NO_GM_ONLINE, "No team member online!");

		/**
		 * Go Command (PvP Command)
		 */
		addTranslatedMessage(CustomMessageId.NOT_USE_WHILE_FIGHT, "You can not use this command during the fight!");
		addTranslatedMessage(CustomMessageId.NOT_USE_ON_PVP_MAP, "You can not use this command on a PvP Map!");
		addTranslatedMessage(CustomMessageId.LEVEL_TOO_LOW, "You can use this command only with level 55 or higher to use!");

		/**
		 * Paint Command
		 */
		addTranslatedMessage(CustomMessageId.WRONG_TARGET, "Please use a legal target!");

		/**
		 * Shiva Command
		 */
		addTranslatedMessage(CustomMessageId.ENCHANT_SUCCES, "All your items have been enchanted to: ");
		addTranslatedMessage(CustomMessageId.ENCHANT_INFO, "Info: This command all your enchanted items on <value>!");
		addTranslatedMessage(CustomMessageId.ENCHANT_SAMPLE, "For example, would enchant all your items to 15 (eq 15.)");

		/**
		 * Userinfo Command
		 */
		addTranslatedMessage(CustomMessageId.CANNOT_SPY_PLAYER, "You can not get information from other players!");

		/**
		 * Check AFK Status
		 */
		addTranslatedMessage(CustomMessageId.KICKED_AFK_OUT, "You have been kicked out for being inactive too long.");

		/**
		 * Exchange Command
		 */
		addTranslatedMessage(CustomMessageId.NOT_ENOUGH_ITEM, "You dont have enough from: ");
		addTranslatedMessage(CustomMessageId.NOT_ENOUGH_AP, "You dont have enough ap, you only have: ");

		/**
		 * Medal Command
		 */
		addTranslatedMessage(CustomMessageId.NOT_ENOUGH_SILVER, "You dont have enough silver medals.");
		addTranslatedMessage(CustomMessageId.NOT_ENOUGH_GOLD, "You dont have enough gold medals.");
		addTranslatedMessage(CustomMessageId.NOT_ENOUGH_PLATIN, "You dont have enough platin medals.");
		addTranslatedMessage(CustomMessageId.NOT_ENOUGH_MITHRIL, "You dont have enough mithril medals.");
		addTranslatedMessage(CustomMessageId.NOT_ENOUGH_AP2, "You dont have enough ap, you need: ");
		addTranslatedMessage(CustomMessageId.EXCHANGE_SILVER, "You have exchange [item:186000031] to [item:186000030].");
		addTranslatedMessage(CustomMessageId.EXCHANGE_GOLD, "You have exchange [item:186000030] to [item:186000096].");
		addTranslatedMessage(CustomMessageId.EXCHANGE_PLATIN, "You have exchange [item:186000096] to [item:186000147].");
		addTranslatedMessage(CustomMessageId.EXCHANGE_MITHRIL, "You have exchange [item:186000147] to [item:186000223].");
		addTranslatedMessage(CustomMessageId.EX_SILVER_INFO, "\nSyntax: .medal silver - Exchange Silver to Gold.");
		addTranslatedMessage(CustomMessageId.EX_GOLD_INFO, "\nSyntax: .medal gold - Exchange Gold to Platin.");
		addTranslatedMessage(CustomMessageId.EX_PLATIN_INFO, "\nSyntax: .medal platinum - Exchnage Platin to Mithril.");
		addTranslatedMessage(CustomMessageId.EX_MITHRIL_INFO, "\nSyntax: .medal mithril - Exchange Mithril to Honorable Mithril.");

		/**
		 * Legendary Raid Spawn Events
		 */
		addTranslatedMessage(CustomMessageId.LEGENDARY_RAID_SPAWNED_ASMO, "[Spawn Event] Ragnarok was spawned for Asmodians at Beluslan!");
		addTranslatedMessage(CustomMessageId.LEGENDARY_RAID_SPAWNED_ELYOS, "[Spawn Event] Omega was spawned for Elyos at Heiron!");
		addTranslatedMessage(CustomMessageId.LEGENDARY_RAID_DESPAWNED_ASMO, "[Spawn Event] Ragnarok was unspawned, nobody kill him!");
		addTranslatedMessage(CustomMessageId.LEGENDARY_RAID_DESPAWNED_ELYOS, "[Spawn Event] Omega was unspawned, nobody kill him!");

		/**
		 * HonorItems Command
		 */
		addTranslatedMessage(CustomMessageId.PLATE_ARMOR, "Plate Armor");
		addTranslatedMessage(CustomMessageId.LEATHER_ARMOR, "Leather Armor");
		addTranslatedMessage(CustomMessageId.CLOTH_ARMOR, "Cloth Armor");
		addTranslatedMessage(CustomMessageId.CHAIN_ARMOR, "Chain Armor");
		addTranslatedMessage(CustomMessageId.WEAPONS, "Weapons");
		addTranslatedMessage(CustomMessageId.PLATE_ARMOR_PRICES, "Plate Armor Prices");
		addTranslatedMessage(CustomMessageId.LEATHER_ARMOR_PRICES, "Leather Armor Prices");
		addTranslatedMessage(CustomMessageId.CLOTH_ARMOR_PRICES, "Cloth Armor Prices");
		addTranslatedMessage(CustomMessageId.CHAIN_ARMOR_PRICES, "Chain Armor Prices");
		addTranslatedMessage(CustomMessageId.WEAPONS_PRICES, "Weapons Prices");
		addTranslatedMessage(CustomMessageId.NOT_ENOUGH_MEDALS, "You dont have enough Medals, you need: ");
		addTranslatedMessage(CustomMessageId.PLATE_ARMOR_USE_INFO, "Use .items and the equal ID (Example: .items 1");
		addTranslatedMessage(CustomMessageId.LEATHER_ARMOR_USE_INFO, "Use .items and the equal ID (Example: .items 6");
		addTranslatedMessage(CustomMessageId.CLOTH_ARMOR_USE_INFO, "Use .items and the equal ID (Example: .items 11");
		addTranslatedMessage(CustomMessageId.CHAIN_ARMOR_USE_INFO, "Use .items and the equal ID (Example: .items 16");
		addTranslatedMessage(CustomMessageId.WEAPONS_USE_INFO, "Use .items and the equal ID (Example: .items 21");
		addTranslatedMessage(CustomMessageId.SUCCESSFULLY_TRADED, "You got successfully your Trade!");

		/**
		 * Moltenus Announce
		 */
		addTranslatedMessage(CustomMessageId.MOLTENUS_APPEAR, "Moltenus Fragment of the Wrath has spawn in the Abyss.");
		addTranslatedMessage(CustomMessageId.MOLTENUS_DISAPPEAR, "Moltenus Fragment of the Wrath has disappear.");

		/**
		 * Dredgion Announce
		 */
		addTranslatedMessage(CustomMessageId.DREDGION_ASMO_GROUP, "An asmodian group is waiting for dredgion.");
		addTranslatedMessage(CustomMessageId.DREDGION_ELYOS_GROUP, "An elyos group is waiting for dredgion.");
		addTranslatedMessage(CustomMessageId.DREDGION_ASMO, "An alone asmodian is waiting for dredgion.");
		addTranslatedMessage(CustomMessageId.DREDGION_ELYOS, "An alone elyos is waiting for dredgion.");

		/**
		 * PvP Service
		 */
		addTranslatedMessage(CustomMessageId.PVP_TOLL_REWARD1, "You have earned");
		addTranslatedMessage(CustomMessageId.PVP_TOLL_REWARD2, " Abso'Points.");

		/**
		 * Invasion Rift
		 */
		addTranslatedMessage(CustomMessageId.INVASION_RIFT_MIN_LEVEL, "Your level is too low to enter.");
		addTranslatedMessage(CustomMessageId.INVASION_RIFT_ELYOS, "A rift for Pandaemonium is open at Ingisson");
		addTranslatedMessage(CustomMessageId.INVASION_RIFT_ASMOS, "A rift for Sanctum is open at Gelkmaros");

		/**
		 * Additional Chest Drops
		 */
		addTranslatedMessage(CustomMessageId.DECOMPOSE_SERVICE_MESSAGE1, "%s has obtained %s from %s.");
		addTranslatedMessage(CustomMessageId.DECOMPOSE_SERVICE_MESSAGE2, "%s has obtained additional %s from %s (Premium).");
		addTranslatedMessage(CustomMessageId.DECOMPOSE_SERVICE_MESSAGE3, "%s has obtained additional %s from %s (VIP).");

		/**
		 * PvP Spree Service
		 */
		addTranslatedMessage(CustomMessageId.SPREE1, "Bloody Storm");
		addTranslatedMessage(CustomMessageId.SPREE2, "Carnage");
		addTranslatedMessage(CustomMessageId.SPREE3, "Genocide");
		addTranslatedMessage(CustomMessageId.KILL_COUNT, "Kills in a row: ");
		addTranslatedMessage(CustomMessageId.CUSTOM_MSG1, " of ");
		addTranslatedMessage(CustomMessageId.MSG_SPREE1, " has started a ");
		addTranslatedMessage(CustomMessageId.MSG_SPREE1_1, " !");
		addTranslatedMessage(CustomMessageId.MSG_SPREE2, " is performing a true ");
		addTranslatedMessage(CustomMessageId.MSG_SPREE2_1, " ! Stop him fast !");
		addTranslatedMessage(CustomMessageId.MSG_SPREE3, " is doing a ");
		addTranslatedMessage(CustomMessageId.MSG_SPREE3_1, " ! Run away if you can!");
		addTranslatedMessage(CustomMessageId.SPREE_END_MSG1, "The killing spree of ");
		addTranslatedMessage(CustomMessageId.SPREE_END_MSG2, " has been stopped by ");
		addTranslatedMessage(CustomMessageId.SPREE_END_MSG3, " after ");
		addTranslatedMessage(CustomMessageId.SPREE_END_MSG4, " uninterrupted murders !");
		addTranslatedMessage(CustomMessageId.SPREE_MONSTER_MSG, "a monster");
	}
}
