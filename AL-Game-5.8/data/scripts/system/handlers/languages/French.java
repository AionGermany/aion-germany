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
 * @author Pearl
 * @reworked Voidstar
 */
public class French extends Language {

	public French() {
		super("fr");
		addSupportedLanguage("fr_FR");
		addTranslatedMessage(CustomMessageId.SERVER_REVISION, "Version du serveur : %-6s");
		addTranslatedMessage(CustomMessageId.WELCOME_PREMIUM, "Bienvenue sur le serveur ");
		addTranslatedMessage(CustomMessageId.WELCOME_REGULAR, "Bienvenue sur le serveur ");
		addTranslatedMessage(CustomMessageId.WELCOME_BASIC, "Bienvenue sur le serveur ");
		addTranslatedMessage(CustomMessageId.INFO1, "Attention : La pub pour d'autres serveurs est sanctionne de Ban !");
		addTranslatedMessage(CustomMessageId.INFO2, "Attention : Le Staff ne vous demandera jamais votre mot de passe !");
		addTranslatedMessage(CustomMessageId.INFO3, "Attention : L'utilisation de logiciel tierce est sanctionne de Ban !");
		addTranslatedMessage(CustomMessageId.INFO4, "Note : /1, /2 et /3 vous permet de discuter avec votre faction.");
		addTranslatedMessage(CustomMessageId.SERVERVERSION, "Client NCSoft Supporte ");
		addTranslatedMessage(CustomMessageId.ENDMESSAGE, "Bon jeu a vous");
		addTranslatedMessage(CustomMessageId.ANNOUNCE_GM_CONNECTION, " est maintenant disponible !");
		addTranslatedMessage(CustomMessageId.ANNOUNCE_GM_DECONNECTION, " est maintenant indisponible !");
		addTranslatedMessage(CustomMessageId.ANNOUNCE_MEMBER_CONNECTION, "%s vient de se connecter.");
		addTranslatedMessage(CustomMessageId.COMMAND_NOT_ENOUGH_RIGHTS, "Vous n'avez pas le droit d'executer cette commande.");
		addTranslatedMessage(CustomMessageId.PLAYER_NOT_ONLINE, "Le joueur %s n'est pas en ligne.");
		addTranslatedMessage(CustomMessageId.INTEGER_PARAMETER_REQUIRED, "Le parametre doit etre un nombre.");
		addTranslatedMessage(CustomMessageId.INTEGER_PARAMETERS_ONLY, "Les parametres doivent etre des nombres.");
		addTranslatedMessage(CustomMessageId.SOMETHING_WRONG_HAPPENED, "Quelque chose s'est mal passe.");
		addTranslatedMessage(CustomMessageId.COMMAND_DISABLED, "Cette commande est desactivee.");
		addTranslatedMessage(CustomMessageId.COMMAND_ADD_SYNTAX, "Syntaxe: //add <nom du joueur> <item id> [<quantite>]");
		addTranslatedMessage(CustomMessageId.COMMAND_ADD_ADMIN_SUCCESS, "Objet(s) ajoute(s) a %s avec succes.");
		addTranslatedMessage(CustomMessageId.COMMAND_ADD_PLAYER_SUCCESS, "%s vous a ajoute %d objet(s).");
		addTranslatedMessage(CustomMessageId.COMMAND_ADD_FAILURE, "L'objet %d n'existe pas et/ou ne peut pas etre ajoute a %s.");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDDROP_SYNTAX, "Syntaxe: //adddrop <mob id> <item id> <min> <max> <chance>");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDSET_SYNTAX, "Syntaxe: <nom du joueur> <set id>");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDSET_SET_DOES_NOT_EXISTS, "L'ensemble d'armures %d n'existe pas.");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDSET_NOT_ENOUGH_SLOTS, "L'inventaire requiert au moins %d emplacements libres.");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDSET_CANNOT_ADD_ITEM, "L'objet %d ne peut pas etre ajoute a %s.");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDSET_ADMIN_SUCCESS, "L'ensemble d'armures %d a ete ajoute a %s avec succes.");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDSET_PLAYER_SUCCESS, "%s vous a ajoute un ensemble d'armures.");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDSKILL_SYNTAX, "Syntaxe: //addskill <id de la competence> <niveau de la competence>");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDSKILL_ADMIN_SUCCESS, "Vous avez ajoute la competence %d a %s avec succes.");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDSKILL_PLAYER_SUCCESS, "%s vous a ajoute une nouvelle competence.");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDTITLE_SYNTAX, "Syntaxe: //addtitle <id du titre> <nom du joueur> [special]");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDTITLE_TITLE_INVALID, "Le titre %d est invalide, il doit etre entre 1 et 50.");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDTITLE_CANNOT_ADD_TITLE_TO_ME, "Vous ne pouvez pas vous ajouter le titre %d.");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDTITLE_CANNOT_ADD_TITLE_TO_PLAYER, "Vous ne pouvez pas ajouter le titre %d a %s.");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDTITLE_ADMIN_SUCCESS_ME, "Vous vous etes ajoute le titre %d avec succes.");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDTITLE_ADMIN_SUCCESS, "Vous avez ajoute le titre %d a %s avec succes.");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDTITLE_PLAYER_SUCCESS, "%s vous a donne le titre %d.");
		addTranslatedMessage(CustomMessageId.COMMAND_SEND_SYNTAX, "Syntaxe: //send <nom de fichier>");
		addTranslatedMessage(CustomMessageId.COMMAND_SEND_MAPPING_NOT_FOUND, "Fichier %s introuvable.");
		addTranslatedMessage(CustomMessageId.COMMAND_SEND_NO_PACKET, "Pas de paquet a envoyer.");
		addTranslatedMessage(CustomMessageId.CHANNEL_WORLD_DISABLED, "Le canal %s est desactive, merci d'utiliser les canaux %s ou %s selon votre faction.");
		addTranslatedMessage(CustomMessageId.CHANNEL_ALL_DISABLED, "Les canaux sont desactives.");
		addTranslatedMessage(CustomMessageId.CHANNEL_ALREADY_FIXED, "Vous etes deja fixe sur le canal %s.");
		addTranslatedMessage(CustomMessageId.CHANNEL_FIXED, "Votre chat est maintenant fixe sur le canal %s.");
		addTranslatedMessage(CustomMessageId.CHANNEL_NOT_ALLOWED, "Vous n'etes pas autorise a parler sur ce canal.");
		addTranslatedMessage(CustomMessageId.CHANNEL_FIXED_BOTH, "Votre chat est maintenant fixe sur les canaux %s et %s.");
		addTranslatedMessage(CustomMessageId.CHANNEL_UNFIX_HELP, "Tapez %s unfix pour liberer votre chat.");
		addTranslatedMessage(CustomMessageId.CHANNEL_NOT_FIXED, "Votre chat n'est fixe sur aucun canal.");
		addTranslatedMessage(CustomMessageId.CHANNEL_FIXED_OTHER, "Votre chat n'est pas fixe sur ce canal, mais sur %s.");
		addTranslatedMessage(CustomMessageId.CHANNEL_RELEASED, "Votre chat a ete libere du canal %s.");
		addTranslatedMessage(CustomMessageId.CHANNEL_RELEASED_BOTH, "Votre chat a ete libere des canaux %s et %s.");
		addTranslatedMessage(CustomMessageId.CHANNEL_BAN_ENDED, "Vous n'etes plus banni des canaux de discussion.");
		addTranslatedMessage(CustomMessageId.CHANNEL_BAN_ENDED_FOR, "Le joueur %s n'est plus banni des canaux de discussion.");
		addTranslatedMessage(CustomMessageId.CHANNEL_BANNED, "Vous ne pouvez pas utiliser les canaux de discussion car %s vous a banni pour la raison suivante: %s, temps restant: %s.");
		addTranslatedMessage(CustomMessageId.COMMAND_MISSING_SKILLS_STIGMAS_ADDED, "%d competences basiques et %d competences stigmas vous ont ete ajoutees.");
		addTranslatedMessage(CustomMessageId.COMMAND_MISSING_SKILLS_ADDED, "%d competences basiques vous ont ete ajoutees.");
		addTranslatedMessage(CustomMessageId.USER_COMMAND_DOES_NOT_EXIST, "Cette commande joueur n'existe pas.");
		addTranslatedMessage(CustomMessageId.COMMAND_XP_DISABLED, "Votre gain d'XP a ete desactive. Tapez .xpon pour le reactiver.");
		addTranslatedMessage(CustomMessageId.COMMAND_XP_ALREADY_DISABLED, "Votre gain d'XP est deja desactive.");
		addTranslatedMessage(CustomMessageId.COMMAND_XP_ENABLED, "Votre gain d'XP a ete re-active.");
		addTranslatedMessage(CustomMessageId.COMMAND_XP_ALREADY_ENABLED, "Votre gain d'XP est deja active.");
		addTranslatedMessage(CustomMessageId.DREDGION_LEVEL_TOO_LOW, "Votre niveau actuel est trop bas pour entrer dans le Dredgion.");
		addTranslatedMessage(CustomMessageId.DEFAULT_FINISH_MESSAGE, "Pret!");

		/**
		 * Asmo and Ely Channel
		 */
		addTranslatedMessage(CustomMessageId.ASMO_FAIL, "Vous etes Elyseens! Vous ne pouvez pas utiliser ce chat. .ely <message> pour discuter avec votre faction!");
		addTranslatedMessage(CustomMessageId.ELY_FAIL, "Vous etes Asmodiens! Vous ne pouvez pas utiliser ce chat. .asmo <message> pour discuter avec votre faction!");

		/**
		 * PvP Service
		 */
		addTranslatedMessage(CustomMessageId.ADV_WINNER_MSG, "[PvP] Le joueur a tue ");
		addTranslatedMessage(CustomMessageId.ADV_LOSER_MSG, "[PvP] Vous avez ete tue par ");
		addTranslatedMessage(CustomMessageId.PLAP_LOST1, "[PowerLzvel-AP] Vous perdez ");
		addTranslatedMessage(CustomMessageId.PLAP_LOST2, "% de votre total d'AP");
		addTranslatedMessage(CustomMessageId.PVP_NO_REWARD1, "Vous n'obtiendrez rien de la mort de ");
		addTranslatedMessage(CustomMessageId.PVP_NO_REWARD2, " parce que vous l'avez deja tue trop souvent!");

		/**
		 * Reward Service Login Messages
		 */
		addTranslatedMessage(CustomMessageId.REWARD10, "Vous pouvez utiliser .start une fois arrive au niveau 10 !");
		addTranslatedMessage(CustomMessageId.REWARD30, "Vous pouvez utiliser .start une fois arrive au niveau 30 !");
		addTranslatedMessage(CustomMessageId.REWARD40, "Vous pouvez utiliser .start une fois arrive au niveau 40 !");
		addTranslatedMessage(CustomMessageId.REWARD50, "Vous pouvez utiliser .start une fois arrive au niveau 50 !");
		addTranslatedMessage(CustomMessageId.REWARD60, "Vous pouvez utiliser .start une fois arrive au niveau 60 !");

		/**
		 * Advanced PvP System
		 */
		addTranslatedMessage(CustomMessageId.PVP_ADV_MESSAGE1, "Map PvP du jour : Reshanta");
		addTranslatedMessage(CustomMessageId.PVP_ADV_MESSAGE2, "Map PvP du jour : Tiamaranta");
		addTranslatedMessage(CustomMessageId.PVP_ADV_MESSAGE3, "Maps PvP du jour : Inggison/Gelkmaros");
		addTranslatedMessage(CustomMessageId.PVP_ADV_MESSAGE4, "Map PvP du jour : Profondeur d'Idian");
		addTranslatedMessage(CustomMessageId.PVP_ADV_MESSAGE5, "Map PvP du jour : Katalam");
		addTranslatedMessage(CustomMessageId.PVP_ADV_MESSAGE6, "Map PvP du jour : Danaria");

		/**
		 * Wedding related
		 */
		addTranslatedMessage(CustomMessageId.WEDDINGNO1, "Vous ne pouvez pas utiliser cette commande pendant le combat!");
		addTranslatedMessage(CustomMessageId.WEDDINGNO2, "Le mariage n'a pas commence!");
		addTranslatedMessage(CustomMessageId.WEDDINGNO3, "Vous refusez de vous marier!");
		addTranslatedMessage(CustomMessageId.WEDDINGYES, "Vous acceptez de vous marier!");

		/**
		 * Clean Command related
		 */
		addTranslatedMessage(CustomMessageId.CANNOTCLEAN, "Vous devez entrer un ID d'item!");
		addTranslatedMessage(CustomMessageId.CANNOTCLEAN2, "Vous n'avez pas cet item en votre possession!");
		addTranslatedMessage(CustomMessageId.SUCCESSCLEAN, "L'item a bien ete retirer de l'inventaire!");

		/**
		 * Mission check command related
		 */
		addTranslatedMessage(CustomMessageId.SUCCESCHECKED, "La mission a ete verifier avec succes!");

		/**
		 * No Exp Command
		 */
		addTranslatedMessage(CustomMessageId.EPACTIVATED, "Votre Exp a ete reactiver!");
		addTranslatedMessage(CustomMessageId.ACTODE, "Pour arreter votre Exp, utiliser .noexp");
		addTranslatedMessage(CustomMessageId.EPDEACTIVATED, "Votre Exp a ete arreter!");
		addTranslatedMessage(CustomMessageId.DETOAC, "Pour activer votre Exp, utiliser .noexp");

		/**
		 * Auto Quest Command
		 */
		addTranslatedMessage(CustomMessageId.WRONGQID, "S'il vous plaît entrer l'ID correcte de la quete!");
		addTranslatedMessage(CustomMessageId.NOTSTARTED, "La quete n'a pas commence!");
		addTranslatedMessage(CustomMessageId.NOTSUPPORT, "Cette quete n'a pas ete gerer par la commande!");

		/**
		 * Quest Restart Command
		 */
		addTranslatedMessage(CustomMessageId.CANNOTRESTART, "] ne peut pas etre recommence");

		/**
		 * Exchange Toll Command
		 */
		addTranslatedMessage(CustomMessageId.TOLLTOBIG, "Vous avez trop de credits!");
		addTranslatedMessage(CustomMessageId.TOLOWAP, "Vous n'avez pas assez de points Abyssaux!");
		addTranslatedMessage(CustomMessageId.TOLOWTOLL, "Vous n'avez pas assez de credits!");
		addTranslatedMessage(CustomMessageId.WRONGTOLLNUM, "Quelque chose ne va pas!");

		/**
		 * Cube Command
		 */
		addTranslatedMessage(CustomMessageId.CUBE_ALLREADY_EXPANDED, "Votre cube a deja ete agrandi!");
		addTranslatedMessage(CustomMessageId.CUBE_SUCCESS_EXPAND, "Votre cube a ete agrandi!");

		/**
		 * GMList Command
		 */
		addTranslatedMessage(CustomMessageId.ONE_GM_ONLINE, "Un membre du Staff est connecte: ");
		addTranslatedMessage(CustomMessageId.MORE_GMS_ONLINE, "Plusieurs membres du staff sont connectes: ");
		addTranslatedMessage(CustomMessageId.NO_GM_ONLINE, "Aucun membre du staff n'est connecte!");

		/**
		 * Eye Command (PvP Command)
		 */
		addTranslatedMessage(CustomMessageId.NOT_USE_WHILE_FIGHT, "Vous ne pouvez pas utiliser cette commande pendant le combat!");
		addTranslatedMessage(CustomMessageId.NOT_USE_ON_PVP_MAP, "Vous ne pouvez pas utiliser cette commande sur une Map PVP!");
		addTranslatedMessage(CustomMessageId.LEVEL_TOO_LOW, "Vous pouvez utiliser cette commande seulement avec un level 55 ou plus!");

		/**
		 * Paint Command
		 */
		addTranslatedMessage(CustomMessageId.WRONG_TARGET, "S'il vous plaît utiliser une cible valide!");

		/**
		 * Enchant Command
		 */
		addTranslatedMessage(CustomMessageId.ENCHANT_SUCCES, "Tous vos items ont ete enchanter a: ");
		addTranslatedMessage(CustomMessageId.ENCHANT_INFO, "Info: Cette commande a enchante tous vos items a <value>!");
		addTranslatedMessage(CustomMessageId.ENCHANT_SAMPLE, "Par exemple, pour enchanter vos items a 15 (eq 15.)");

		/**
		 * Userinfo Command
		 */
		addTranslatedMessage(CustomMessageId.CANNOT_SPY_PLAYER, "Vous ne pouvez pas avoir les informations d'autres joueurs!");

		/**
		 * Exchange Command
		 */
		addTranslatedMessage(CustomMessageId.NOT_ENOUGH_ITEM, "Vous n'avez pas assez de: ");
		addTranslatedMessage(CustomMessageId.NOT_ENOUGH_AP, "Vous n'avez pas assez d'ap, vous avez seulement: ");

		/**
		 * Check AFK Status
		 */
		addTranslatedMessage(CustomMessageId.KICKED_AFK_OUT, "Vous avez ete expulse de l'instance pour avoir AFK.");

		/**
		 * Medal Command
		 */
		addTranslatedMessage(CustomMessageId.NOT_ENOUGH_SILVER, "Vous n'avez pas assez de medailles d'argents.");
		addTranslatedMessage(CustomMessageId.NOT_ENOUGH_GOLD, "Vous n'avez pas assez de medailles dorees.");
		addTranslatedMessage(CustomMessageId.NOT_ENOUGH_PLATIN, "Vous n'avez pas assez de medailles de platines.");
		addTranslatedMessage(CustomMessageId.NOT_ENOUGH_MITHRIL, "Vous n'avez pas assez de medailles de mithrils.");
		addTranslatedMessage(CustomMessageId.NOT_ENOUGH_AP2, "Vous n'avez pas assez d'ap, vous avez besoin de: ");
		addTranslatedMessage(CustomMessageId.EXCHANGE_SILVER, "Vous avez echange [item:186000031] contre [item:186000030].");
		addTranslatedMessage(CustomMessageId.EXCHANGE_GOLD, "Vous avez echange [item:186000030] contre [item:186000096].");
		addTranslatedMessage(CustomMessageId.EXCHANGE_PLATIN, "Vous avez echange [item:186000096] contre [item:186000147].");
		addTranslatedMessage(CustomMessageId.EXCHANGE_MITHRIL, "Vous avez echange [item:186000147] contre [item:186000223].");
		addTranslatedMessage(CustomMessageId.EX_SILVER_INFO, "\nSyntax: .medal silver - Echange argents contre dorees.");
		addTranslatedMessage(CustomMessageId.EX_GOLD_INFO, "\nSyntax: .medal gold - Echange dorees contre Platines.");
		addTranslatedMessage(CustomMessageId.EX_PLATIN_INFO, "\nSyntax: .medal platinum - Echange Platines contre Mithrils.");
		addTranslatedMessage(CustomMessageId.EX_MITHRIL_INFO, "\nSyntax: .medal mithril - Echange Mithrils contre Honorable Mithrils.");

		/**
		 * Legendary Raid Spawn Events
		 */
		addTranslatedMessage(CustomMessageId.LEGENDARY_RAID_SPAWNED_ASMO, "[Spawn Event] Ragnarok est apparu a Beluslan !");
		addTranslatedMessage(CustomMessageId.LEGENDARY_RAID_SPAWNED_ELYOS, "[Spawn Event] Omega est apparu a Heiron !");
		addTranslatedMessage(CustomMessageId.LEGENDARY_RAID_DESPAWNED_ASMO, "[Spawn Event] Ragnarok a maintenant disparu !");
		addTranslatedMessage(CustomMessageId.LEGENDARY_RAID_DESPAWNED_ELYOS, "[Spawn Event] Omega a maintenant disparu !");

		/**
		 * HonorItems Command
		 */
		addTranslatedMessage(CustomMessageId.PLATE_ARMOR, "Armure Plate");
		addTranslatedMessage(CustomMessageId.LEATHER_ARMOR, "Armure Cuir");
		addTranslatedMessage(CustomMessageId.CLOTH_ARMOR, "Armure Tissu");
		addTranslatedMessage(CustomMessageId.CHAIN_ARMOR, "Armure Maille");
		addTranslatedMessage(CustomMessageId.WEAPONS, "Armes");
		addTranslatedMessage(CustomMessageId.PLATE_ARMOR_PRICES, "Prix de l'armure Plate");
		addTranslatedMessage(CustomMessageId.LEATHER_ARMOR_PRICES, "Prix de l'armure Cuir");
		addTranslatedMessage(CustomMessageId.CLOTH_ARMOR_PRICES, "Prix de l'armure Tissu");
		addTranslatedMessage(CustomMessageId.CHAIN_ARMOR_PRICES, "Prix de l'armure Maille");
		addTranslatedMessage(CustomMessageId.WEAPONS_PRICES, "Prix d'Armes");
		addTranslatedMessage(CustomMessageId.NOT_ENOUGH_MEDALS, "Vous n'avez pas assez de medailles, vous avez besoin de: ");
		addTranslatedMessage(CustomMessageId.PLATE_ARMOR_USE_INFO, "Utiliser .items et l'ID correspondant (Example: .items 1");
		addTranslatedMessage(CustomMessageId.LEATHER_ARMOR_USE_INFO, "Utiliser .items et l'ID correspondant (Example: .items 6");
		addTranslatedMessage(CustomMessageId.CLOTH_ARMOR_USE_INFO, "Utiliser .items et l'ID correspondant (Example: .items 11");
		addTranslatedMessage(CustomMessageId.CHAIN_ARMOR_USE_INFO, "Utiliser .items et l'ID correspondant (Example: .items 16");
		addTranslatedMessage(CustomMessageId.WEAPONS_USE_INFO, "Utiliser .items et l'ID correspondant (Example: .items 21");
		addTranslatedMessage(CustomMessageId.SUCCESSFULLY_TRADED, "Vous avez fait votre echange avec succes!");

		/**
		 * Moltenus Announce
		 */
		addTranslatedMessage(CustomMessageId.MOLTENUS_APPEAR, "Fragment du courroux de Menotios est apparu dans les abysses.");
		addTranslatedMessage(CustomMessageId.MOLTENUS_DISAPPEAR, "Fragment du courroux de Menotios a disparu.");

		/**
		 * Dredgion Announce
		 */
		addTranslatedMessage(CustomMessageId.DREDGION_ASMO_GROUP, "Un groupe asmodien s'est inscrit pour le dredgion.");
		addTranslatedMessage(CustomMessageId.DREDGION_ELYOS_GROUP, "Un groupe elyseen s'est inscrit pour le dredgion.");
		addTranslatedMessage(CustomMessageId.DREDGION_ASMO, "Un asmodien s'est inscrit pour le dredgion.");
		addTranslatedMessage(CustomMessageId.DREDGION_ELYOS, "Un elyseen s'est inscrit pour le dredgion.");

		/**
		 * PvP Service
		 */
		addTranslatedMessage(CustomMessageId.PVP_TOLL_REWARD1, "Vous avez gagne ");
		addTranslatedMessage(CustomMessageId.PVP_TOLL_REWARD2, " Abso'Point.");

		/**
		 * Invasion Rift
		 */
		addTranslatedMessage(CustomMessageId.INVASION_RIFT_MIN_LEVEL, "Votre level est trop bas pour entrer.");
		addTranslatedMessage(CustomMessageId.INVASION_RIFT_ELYOS, "Une faille pour Pandaemonium est apparu a Ingisson");
		addTranslatedMessage(CustomMessageId.INVASION_RIFT_ASMOS, "Une faille pour Sanctum est apparu a Gelkmaros");

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
