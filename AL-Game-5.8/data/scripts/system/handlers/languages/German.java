/**
 * This file is part of Aion-Lightning <aion-lightning.org>.
 *
 *  Aion-Lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 2 of the License, or
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
public class German extends Language {

	public German() {
		super("de");
		addSupportedLanguage("de_DE");
		addTranslatedMessage(CustomMessageId.SERVER_REVISION, "Server Version : %-6s");
		addTranslatedMessage(CustomMessageId.WELCOME_PREMIUM, "Willkommen bei ");
		addTranslatedMessage(CustomMessageId.WELCOME_REGULAR, "Willkommen bei ");
		addTranslatedMessage(CustomMessageId.WELCOME_BASIC, "Willkommen bei ");
		addTranslatedMessage(CustomMessageId.INFO1, "Warnung: Benutzung von fremde software, wie (speed hack&Co ist strengst verboten.");
		addTranslatedMessage(CustomMessageId.INFO2, "Hinweis: Je nach Fall, fuehrt ausnahmslos zu unbefristete Account Sperrung.");
		addTranslatedMessage(CustomMessageId.INFO3, "Warnung: Werben anderer Aion Server oder abwerben von Spieler hier auf dem Server, ist untersagt.");
		addTranslatedMessage(CustomMessageId.INFO4, "Anmerkung: Das Team wird Sie niemals nach Ihrem Account Passwort fragen!");
		addTranslatedMessage(CustomMessageId.INFO5, "Chat: Benutze .faction, .ely, ,asmo <text> um im Chat zu schreiben. ");
		addTranslatedMessage(CustomMessageId.INFO6, "Chat: Benutze /1 /2 /3, um in den Chatkanaelen zu schreiben.");
		addTranslatedMessage(CustomMessageId.INFO7, "Befehle: Benutze .help, um zu sehen welche Spielerbefehle noch zur Verfuegung stehen.");
		addTranslatedMessage(CustomMessageId.SERVERVERSION, "Supported NCSoft Version ");
		addTranslatedMessage(CustomMessageId.ENDMESSAGE, "Viel Spass auf unserem Server: ");
		addTranslatedMessage(CustomMessageId.ANNOUNCE_GM_CONNECTION, " Ist jetzt fuer den Support verfuegbar!");
		addTranslatedMessage(CustomMessageId.ANNOUNCE_GM_DECONNECTION, " Ist fuer den Support nicht mehr verfuegbar!");
		addTranslatedMessage(CustomMessageId.ANNOUNCE_MEMBER_CONNECTION, "%s ist der Welt von Atreia beigetreten.");
		addTranslatedMessage(CustomMessageId.COMMAND_NOT_ENOUGH_RIGHTS, "Sie haben keine Berechtigung zur Verwendung dieses Befehls");
		addTranslatedMessage(CustomMessageId.PLAYER_NOT_ONLINE, "Spieler nicht online");
		addTranslatedMessage(CustomMessageId.INTEGER_PARAMETER_REQUIRED, "Integer parameter required");
		addTranslatedMessage(CustomMessageId.INTEGER_PARAMETERS_ONLY, "Integer parameters only");
		addTranslatedMessage(CustomMessageId.SOMETHING_WRONG_HAPPENED, "Etwas ging schief");
		addTranslatedMessage(CustomMessageId.COMMAND_DISABLED, "Befehl deaktiviert");
		addTranslatedMessage(CustomMessageId.COMMAND_ADD_SYNTAX, "Syntax: //add <player name> <itemid> [<amount>]");
		addTranslatedMessage(CustomMessageId.COMMAND_ADD_ADMIN_SUCCESS, "Item erfolgreich dem Spieler %s hinzugefuegt ");
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
		addTranslatedMessage(CustomMessageId.DREDGION_LEVEL_TOO_LOW, "Ihr derzeitges Level ist zu niedrig, um in die Dredgion einzutreten.");
		addTranslatedMessage(CustomMessageId.DEFAULT_FINISH_MESSAGE, "Ende!");

		/**
		 * PvP Service
		 */
		addTranslatedMessage(CustomMessageId.ADV_WINNER_MSG, "[PvP System] Spieler getoetet ");
		addTranslatedMessage(CustomMessageId.ADV_LOSER_MSG, "[PvP System] Du wurdest getoetet von ");
		addTranslatedMessage(CustomMessageId.PLAP_LOST1, "[PL-AP] Du verlierst ");
		addTranslatedMessage(CustomMessageId.PLAP_LOST2, "% deiner totalen AP");
		addTranslatedMessage(CustomMessageId.PVP_TOLL_REWARD1, "Du hast ");
		addTranslatedMessage(CustomMessageId.PVP_TOLL_REWARD2, " Toll erhalten.");
		addTranslatedMessage(CustomMessageId.PVP_NO_REWARD1, "Du bekommst nichts fuer das toeten von ");
		addTranslatedMessage(CustomMessageId.PVP_NO_REWARD2, " weil du ihn schon zu oft getoetet hast!");
		addTranslatedMessage(CustomMessageId.EASY_MITHRIL_MSG, "[Mithril System] du bekamst: ");

		/**
		 * Reward Service Login Messages
		 */
		addTranslatedMessage(CustomMessageId.REWARD10, "Du kannst .start benutzen um eine Level 10 Ausstattung zu bekommen!");
		addTranslatedMessage(CustomMessageId.REWARD30, "Du kannst .start benutzen um eine Level 30 Ausstattung zu bekommen!");
		addTranslatedMessage(CustomMessageId.REWARD40, "Du kannst .start benutzen um eine Level 40 Ausstattung zu bekommen!");
		addTranslatedMessage(CustomMessageId.REWARD50, "Du kannst .start benutzen um eine Level 50 Ausstattung zu bekommen!");
		addTranslatedMessage(CustomMessageId.REWARD60, "Du kannst .start benutzen um eine Level 60 Ausstattung zu bekommen!");

		/**
		 * Advanced PvP System
		 */
		addTranslatedMessage(CustomMessageId.PVP_ADV_MESSAGE1, "Heutige PvP Map: Reshanta");
		addTranslatedMessage(CustomMessageId.PVP_ADV_MESSAGE2, "Heutige PvP Map: Tiamaranta");
		addTranslatedMessage(CustomMessageId.PVP_ADV_MESSAGE3, "Heutige PvP Map: Inggison/Gelkmaros");
		addTranslatedMessage(CustomMessageId.PVP_ADV_MESSAGE4, "Heutige PvP Map: Idian Depths");
		addTranslatedMessage(CustomMessageId.PVP_ADV_MESSAGE5, "Heutige PvP Map: Katalam");
		addTranslatedMessage(CustomMessageId.PVP_ADV_MESSAGE6, "Heutige PvP Map: Danaria");

		/**
		 * Asmo, Ely and World Channel
		 */
		addTranslatedMessage(CustomMessageId.ASMO_FAIL, "Du bist Elyos! Du kannst diesen Chat nicht benutzen. Nutze .ely <Nachricht> um im Fraktions Chat zu schreiben!");
		addTranslatedMessage(CustomMessageId.ELY_FAIL, "Du bist Asmo! Du kannst diesen Chat nicht benutzen. Nutze .asmo <Nachricht> um im Fraktions Chat zu schreiben!");

		/**
		 * Wedding related
		 */
		addTranslatedMessage(CustomMessageId.WEDDINGNO1, "Du kannst dieses Kommando nicht waehrend des Kampfes nutzen!");
		addTranslatedMessage(CustomMessageId.WEDDINGNO2, "Hochzeit wurde nicht gestartet!");
		addTranslatedMessage(CustomMessageId.WEDDINGNO3, "Du hast die Heirat abgelehnt!");
		addTranslatedMessage(CustomMessageId.WEDDINGYES, "Du hast die Heirat akzeptiert!");

		/**
		 * Clean Command related
		 */
		addTranslatedMessage(CustomMessageId.CANNOTCLEAN, "Du musst eine Item Id eingeben oder einen Link posten!");
		addTranslatedMessage(CustomMessageId.CANNOTCLEAN2, "Du besitzt dieses Item nicht!");
		addTranslatedMessage(CustomMessageId.SUCCESSCLEAN, "Item wurde erfolgreich aus deinem Wuerfel entfernt!");

		/**
		 * Mission check command related
		 */
		addTranslatedMessage(CustomMessageId.SUCCESCHECKED, "Mission erfolgreich ueberprueft!");

		/**
		 * No Exp Command
		 */
		addTranslatedMessage(CustomMessageId.EPACTIVATED, "Deine EP wurden wieder aktiviert!");
		addTranslatedMessage(CustomMessageId.ACTODE, "Um deine EP zu deaktivieren, nutze .noexp");
		addTranslatedMessage(CustomMessageId.EPDEACTIVATED, "Deine EP wurden deaktiviert!");
		addTranslatedMessage(CustomMessageId.DETOAC, "Um deine EP zu aktivieren, nutze .noexp");

		/**
		 * Auto Quest Command
		 */
		addTranslatedMessage(CustomMessageId.WRONGQID, "Bitte gebe eine richtige Quest Id an!");
		addTranslatedMessage(CustomMessageId.NOTSTARTED, "Quest konnte nicht gestartet werden!");
		addTranslatedMessage(CustomMessageId.NOTSUPPORT, "Diese Quest wird nicht von diesem Kommando unterstuetzt!");

		/**
		 * Quest Restart Command
		 */
		addTranslatedMessage(CustomMessageId.CANNOTRESTART, "] kann nicht neugestartet werden");

		/**
		 * Exchange Toll Command
		 */
		addTranslatedMessage(CustomMessageId.TOLLTOBIG, "Du hast zu viele Toll!");
		addTranslatedMessage(CustomMessageId.TOLOWAP, "Du hast nicht genug AP!");
		addTranslatedMessage(CustomMessageId.TOLOWTOLL, "Du hast nicht genug Toll!");
		addTranslatedMessage(CustomMessageId.WRONGTOLLNUM, "Irgendwas lief schief!");

		/**
		 * Cube Command
		 */
		addTranslatedMessage(CustomMessageId.CUBE_ALLREADY_EXPANDED, "Dein Wuerfel ist voll erweitert!");
		addTranslatedMessage(CustomMessageId.CUBE_SUCCESS_EXPAND, "Dein Wuerfel wurde erfolgreich erweitert!");

		/**
		 * GMList Command
		 */
		addTranslatedMessage(CustomMessageId.ONE_GM_ONLINE, "Ein Team Mitglied ist online: ");
		addTranslatedMessage(CustomMessageId.MORE_GMS_ONLINE, "Es sind folgende Team Mitglieder online: ");
		addTranslatedMessage(CustomMessageId.NO_GM_ONLINE, "Es ist kein Team Mitglied online!");

		/**
		 * Go Command (PvP Command)
		 */
		addTranslatedMessage(CustomMessageId.NOT_USE_WHILE_FIGHT, "Du kannst dieses Kommando nicht waehrend des Kampfes nutzen!");
		addTranslatedMessage(CustomMessageId.NOT_USE_ON_PVP_MAP, "Du kannst dieses Kommando nicht auf einer PvP Map benutzen!");
		addTranslatedMessage(CustomMessageId.LEVEL_TOO_LOW, "Du kannst dieses Kommando nur mit Level 55 oder hoeher nutzen!");

		/**
		 * Paint Command
		 */
		addTranslatedMessage(CustomMessageId.WRONG_TARGET, "Bitte benutze ein erlaubtes Ziel!");

		/**
		 * Shiva Command
		 */
		addTranslatedMessage(CustomMessageId.ENCHANT_SUCCES, "Alle deine Items wurden verzaubert auf: ");
		addTranslatedMessage(CustomMessageId.ENCHANT_INFO, "Info: Dieses Kommando verzaubert alle deine Items auf <value>!");
		addTranslatedMessage(CustomMessageId.ENCHANT_SAMPLE, "Beispiel: (.eq 15) wuerde alle deine Items auf 15 verzaubern");

		/**
		 * Userinfo Command
		 */
		addTranslatedMessage(CustomMessageId.CANNOT_SPY_PLAYER, "Du kannst keine Infos von anderen Spielern bekommen!");

		/**
		 * Check AFK Status
		 */
		addTranslatedMessage(CustomMessageId.KICKED_AFK_OUT, "You have been kicked out for being inactive too long.");

		/**
		 * Exchange Command
		 */
		addTranslatedMessage(CustomMessageId.NOT_ENOUGH_ITEM, "Sie besitzen nicht genug von: ");
		addTranslatedMessage(CustomMessageId.NOT_ENOUGH_AP, "Sie besitzen nicht genug AP, Sie haben nur: ");

		/**
		 * Medal Command
		 */
		addTranslatedMessage(CustomMessageId.NOT_ENOUGH_SILVER, "Sie besitzen nicht genug Silber Medaillen.");
		addTranslatedMessage(CustomMessageId.NOT_ENOUGH_GOLD, "Sie besitzen nicht genug Gold Medaillen.");
		addTranslatedMessage(CustomMessageId.NOT_ENOUGH_PLATIN, "Sie besitzen nicht genug Platin Medaillen.");
		addTranslatedMessage(CustomMessageId.NOT_ENOUGH_MITHRIL, "Sie besitzen nicht genug Mithril Medaillen.");
		addTranslatedMessage(CustomMessageId.NOT_ENOUGH_AP2, "Sie besitzen nicht genug AP, Sie benoetigen: ");
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
		addTranslatedMessage(CustomMessageId.LEGENDARY_RAID_SPAWNED_ASMO, "[Spawn Event] Ragnarok ist fuer die Asmodier in Beluslan erschienen!");
		addTranslatedMessage(CustomMessageId.LEGENDARY_RAID_SPAWNED_ELYOS, "[Spawn Event] Omega ist fuer die Elyos in Heiron erschienen!");
		addTranslatedMessage(CustomMessageId.LEGENDARY_RAID_DESPAWNED_ASMO, "[Spawn Event] Ragnarok ist verschwunden, niemand hat ihn getoetet!");
		addTranslatedMessage(CustomMessageId.LEGENDARY_RAID_DESPAWNED_ELYOS, "[Spawn Event] Omega ist verschwunden, niemand hat ihn getoetet!");

		/**
		 * HonorItems Command
		 */
		addTranslatedMessage(CustomMessageId.PLATE_ARMOR, "Platten Ruestung");
		addTranslatedMessage(CustomMessageId.LEATHER_ARMOR, "Leder Ruestung");
		addTranslatedMessage(CustomMessageId.CLOTH_ARMOR, "Stoff Ruestung");
		addTranslatedMessage(CustomMessageId.CHAIN_ARMOR, "Ketten Ruestung");
		addTranslatedMessage(CustomMessageId.WEAPONS, "Waffen");
		addTranslatedMessage(CustomMessageId.PLATE_ARMOR_PRICES, "Platten ruestungs Preis");
		addTranslatedMessage(CustomMessageId.LEATHER_ARMOR_PRICES, "Leder Ruestungs Preis");
		addTranslatedMessage(CustomMessageId.CLOTH_ARMOR_PRICES, "Stoff Ruestung Preis");
		addTranslatedMessage(CustomMessageId.CHAIN_ARMOR_PRICES, "Ketten Ruestungs Preis");
		addTranslatedMessage(CustomMessageId.WEAPONS_PRICES, "Waffen Preis");
		addTranslatedMessage(CustomMessageId.NOT_ENOUGH_MEDALS, "Sie besitzen nicht genuegend Medaillen, Sie benoetigen: ");
		addTranslatedMessage(CustomMessageId.PLATE_ARMOR_USE_INFO, "Use .items and the equal ID (Example: .items 1");
		addTranslatedMessage(CustomMessageId.LEATHER_ARMOR_USE_INFO, "Use .items and the equal ID (Example: .items 6");
		addTranslatedMessage(CustomMessageId.CLOTH_ARMOR_USE_INFO, "Use .items and the equal ID (Example: .items 11");
		addTranslatedMessage(CustomMessageId.CHAIN_ARMOR_USE_INFO, "Use .items and the equal ID (Example: .items 16");
		addTranslatedMessage(CustomMessageId.WEAPONS_USE_INFO, "Use .items and the equal ID (Example: .items 21");
		addTranslatedMessage(CustomMessageId.SUCCESSFULLY_TRADED, "You got successfully your Trade!");

		/**
		 * Moltenus Announce
		 */
		addTranslatedMessage(CustomMessageId.MOLTENUS_APPEAR, "Moltenus Fragment of the Wrath ist im Abyss erschienen.");
		addTranslatedMessage(CustomMessageId.MOLTENUS_DISAPPEAR, "Moltenus Fragment of the Wrath ist verschwunden.");

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
		addTranslatedMessage(CustomMessageId.PVP_TOLL_REWARD1, "Sie haben verdient");
		addTranslatedMessage(CustomMessageId.PVP_TOLL_REWARD2, " Abyss Punkte.");

		/**
		 * Invasion Rift
		 */
		addTranslatedMessage(CustomMessageId.INVASION_RIFT_MIN_LEVEL, "Dein Level ist zu niedrig um zu passieren.");
		addTranslatedMessage(CustomMessageId.INVASION_RIFT_ELYOS, "A rift for Pandaemonium is open at Ingisson");
		addTranslatedMessage(CustomMessageId.INVASION_RIFT_ASMOS, "A rift for Sanctum is open at Gelkmaros");

		/**
		 * Additional Chest Drops
		 */
		addTranslatedMessage(CustomMessageId.DECOMPOSE_SERVICE_MESSAGE1, "%s hat erhalten %s von %s.");
		addTranslatedMessage(CustomMessageId.DECOMPOSE_SERVICE_MESSAGE2, "%s hat zus�tzlich erhalten %s von %s (Premium).");
		addTranslatedMessage(CustomMessageId.DECOMPOSE_SERVICE_MESSAGE3, "%s hat zus�tzlich erhalten %s von %s (VIP).");

		/**
		 * PvP Spree Service
		 */
		addTranslatedMessage(CustomMessageId.SPREE1, "Blutiger Sturm");
		addTranslatedMessage(CustomMessageId.SPREE2, "Blutbad");
		addTranslatedMessage(CustomMessageId.SPREE3, "Voelkermord");
		addTranslatedMessage(CustomMessageId.KILL_COUNT, "Kills in Folge: ");
		addTranslatedMessage(CustomMessageId.CUSTOM_MSG1, " von ");
		addTranslatedMessage(CustomMessageId.MSG_SPREE1, " hat begonnen ein ");
		addTranslatedMessage(CustomMessageId.MSG_SPREE1_1, " !");
		addTranslatedMessage(CustomMessageId.MSG_SPREE2, " fuehrt eine echte ");
		addTranslatedMessage(CustomMessageId.MSG_SPREE2_1, " ! Haltet ihn schnell !");
		addTranslatedMessage(CustomMessageId.MSG_SPREE3, " tut ein ");
		addTranslatedMessage(CustomMessageId.MSG_SPREE3_1, " ! Lauf weg, wenn du kannst!");
		addTranslatedMessage(CustomMessageId.SPREE_END_MSG1, "Der Amoklauf von ");
		addTranslatedMessage(CustomMessageId.SPREE_END_MSG2, " wurde gestoppt von ");
		addTranslatedMessage(CustomMessageId.SPREE_END_MSG3, " nach ");
		addTranslatedMessage(CustomMessageId.SPREE_END_MSG4, " ununterbrochene Morde !");
		addTranslatedMessage(CustomMessageId.SPREE_MONSTER_MSG, "ein Monster");
	}
}
