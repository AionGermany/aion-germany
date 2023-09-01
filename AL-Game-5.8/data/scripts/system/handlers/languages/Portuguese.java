/**
 * This file is part of Aion-Lightning <aion-lightning.org>.
 *
 * Aion-Lightning is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Aion-Lightning is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details. *
 * You should have received a copy of the GNU General Public License
 * along with Aion-Lightning.
 * If not, see <http://www.gnu.org/licenses/>.
 */

package languages;

import com.aionemu.gameserver.utils.i18n.CustomMessageId;
import com.aionemu.gameserver.utils.i18n.Language;

/**
 * @author Eloann
 * @reworked Voidstar
 * @translate Insaneclimax
 */
public class Portuguese extends Language {

	public Portuguese() {
		super("pt");
		addSupportedLanguage("pt_PT");
		addTranslatedMessage(CustomMessageId.SERVER_REVISION, "Versão do servidor : %-6s");
		addTranslatedMessage(CustomMessageId.WELCOME_PREMIUM, "Bem-vindo ao ");
		addTranslatedMessage(CustomMessageId.WELCOME_REGULAR, "Bem-vindo ao ");
		addTranslatedMessage(CustomMessageId.WELCOME_BASIC, "Bem-vindo ao ");
		/**
		 * Disabled on English, as they comes already from CustomMessageId.java. addTranslatedMessage(CustomMessageId.INFO1, "WARNING: Using of third-party software (speed hack&Co.) will be punished
		 * with permanent ban (Ban MAC)" ); addTranslatedMessage(CustomMessageId.INFO2, "Note: Likewise, hacking will result in an immediate ban regardless of reasons behind it." );
		 * addTranslatedMessage(CustomMessageId.INFO3, "Note: The advertising for other servers is prohibited.Breaking this rule will result a permanent ban!" );
		 * addTranslatedMessage(CustomMessageId.INFO4, "Note: Our team will never ask you for your account password!"); addTranslatedMessage(CustomMessageId.INFO5, "Chat: Use .faction, .ely, .asmo
		 * <text>, to write in your faction World chat." ); addTranslatedMessage(CustomMessageId.INFO6, "Tip: Use /1, /2 and /3 to use the other Chat Channels.");
		 * addTranslatedMessage(CustomMessageId.INFO7, "Help: use the command: .help to see other available player commands." );
		 */
		addTranslatedMessage(CustomMessageId.SERVERVERSION, "Compativel com a versão da NCsoft");
		addTranslatedMessage(CustomMessageId.ENDMESSAGE, "Divirta-se.");
		addTranslatedMessage(CustomMessageId.ANNOUNCE_GM_CONNECTION, " está disponível para apoio!");
		addTranslatedMessage(CustomMessageId.ANNOUNCE_GM_DECONNECTION, " agora está indisponíveis para apoio!");
		addTranslatedMessage(CustomMessageId.ANNOUNCE_MEMBER_CONNECTION, "%s entrou em Atreia.");
		addTranslatedMessage(CustomMessageId.COMMAND_NOT_ENOUGH_RIGHTS, "Você não tem direitos para utilizar este comando");
		addTranslatedMessage(CustomMessageId.PLAYER_NOT_ONLINE, "Jogador não está on-line");
		addTranslatedMessage(CustomMessageId.INTEGER_PARAMETER_REQUIRED, "Parâmetro inteiro necessária");
		addTranslatedMessage(CustomMessageId.INTEGER_PARAMETERS_ONLY, "Únicamente parâmetros inteiros");
		addTranslatedMessage(CustomMessageId.SOMETHING_WRONG_HAPPENED, "Algo aconteceu de errado");
		addTranslatedMessage(CustomMessageId.COMMAND_DISABLED, "Comando desativado");
		addTranslatedMessage(CustomMessageId.COMMAND_ADD_SYNTAX, "Syntax: //add <nick> <itemid> [<montante>]");
		addTranslatedMessage(CustomMessageId.COMMAND_ADD_ADMIN_SUCCESS, "item adicionado com Sucesso ao jogador %s");
		addTranslatedMessage(CustomMessageId.COMMAND_ADD_PLAYER_SUCCESS, "Administrador %s deu-te %d item(s)");
		addTranslatedMessage(CustomMessageId.COMMAND_ADD_FAILURE, "Item %d não existem ou não podem ser dado %s");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDDROP_SYNTAX, "Syntax: //adddrop <npc id> <itemid> <min> <max> <chance>");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDSET_SYNTAX, "Syntax: //addset <nick> <id set>");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDSET_SET_DOES_NOT_EXISTS, "Set %d não existe");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDSET_NOT_ENOUGH_SLOTS, "Não existe espaço no invantario %d para adicionar este set");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDSET_CANNOT_ADD_ITEM, "Item %d não pode ser adicionado ao %s");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDSET_ADMIN_SUCCESS, "Set %d dado com sucesso %s");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDSET_PLAYER_SUCCESS, "%s deu-lhe um set");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDSKILL_SYNTAX, "Syntax: //addskill <skill id> <skill lvl");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDSKILL_ADMIN_SUCCESS, "Skill %d dada com sucesso %s");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDSKILL_PLAYER_SUCCESS, "%s deu-lhe habilidade");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDTITLE_SYNTAX, "Syntax: //addtitle <title id> <player name> [special]");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDTITLE_TITLE_INVALID, "Título deve ser de 1 a 50");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDTITLE_CANNOT_ADD_TITLE_TO_ME, "Não é possível adicionar título %d si mesmo");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDTITLE_CANNOT_ADD_TITLE_TO_PLAYER, "Não é possível adicionar título %d a %s");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDTITLE_ADMIN_SUCCESS_ME, "Titulo %d adicionados sucesso");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDTITLE_ADMIN_SUCCESS, "Titulo adicionados sucesso%d ao jogador %s");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDTITLE_PLAYER_SUCCESS, "%s deu-lhe um titulo %d");
		addTranslatedMessage(CustomMessageId.COMMAND_SEND_SYNTAX, "Syntax: //send <file name>");
		addTranslatedMessage(CustomMessageId.COMMAND_SEND_MAPPING_NOT_FOUND, "%s não encontrado");
		addTranslatedMessage(CustomMessageId.COMMAND_SEND_NO_PACKET, "Envie nenhum pacote");
		addTranslatedMessage(CustomMessageId.CHANNEL_WORLD_DISABLED, "Channel %s Desativado, use channel %s ou %s com base em sua raça");
		addTranslatedMessage(CustomMessageId.CHANNEL_ALL_DISABLED, "Todos os canais desativados");
		addTranslatedMessage(CustomMessageId.CHANNEL_ALREADY_FIXED, "O seu canal foi instalado com sucesso %s");
		addTranslatedMessage(CustomMessageId.CHANNEL_FIXED, "Canais instalados %s");
		addTranslatedMessage(CustomMessageId.CHANNEL_NOT_ALLOWED, "Você não pode usar este canal");
		addTranslatedMessage(CustomMessageId.CHANNEL_FIXED_BOTH, "Canais instalados %s e %s");
		addTranslatedMessage(CustomMessageId.CHANNEL_UNFIX_HELP, "Escreva %s para sair do canal"); //
		addTranslatedMessage(CustomMessageId.CHANNEL_NOT_FIXED, "Você não está instalado no canal");
		addTranslatedMessage(CustomMessageId.CHANNEL_FIXED_OTHER, "Seu bate-papo não está instalado neste canal, mas em %s");
		addTranslatedMessage(CustomMessageId.CHANNEL_RELEASED, "Você saiu do canal %s");
		addTranslatedMessage(CustomMessageId.CHANNEL_RELEASED_BOTH, "Você está fora de %s e %s");
		addTranslatedMessage(CustomMessageId.CHANNEL_BAN_ENDED, "Você pode juntar os canais");
		addTranslatedMessage(CustomMessageId.CHANNEL_BAN_ENDED_FOR, "Jogador %s pode voltar a entrar no canal");
		addTranslatedMessage(CustomMessageId.CHANNEL_BANNED, "Você não pode acessar o canal, porque %s proibição você por causa de: %s , para a esquerda para destravar: %s");
		addTranslatedMessage(CustomMessageId.COMMAND_MISSING_SKILLS_STIGMAS_ADDED, "%d skill %d estigma dado a você");
		addTranslatedMessage(CustomMessageId.COMMAND_MISSING_SKILLS_ADDED, "%d habilidade dado a você");
		addTranslatedMessage(CustomMessageId.USER_COMMAND_DOES_NOT_EXIST, "Este comando existe jogo");
		addTranslatedMessage(CustomMessageId.COMMAND_XP_DISABLED, "Acúmulo XP desativado. Enter. XPON para desbloquear");
		addTranslatedMessage(CustomMessageId.COMMAND_XP_ALREADY_DISABLED, "Acúmulo XP desativado");
		addTranslatedMessage(CustomMessageId.COMMAND_XP_ENABLED, "Acúmulo XP habilitado");
		addTranslatedMessage(CustomMessageId.COMMAND_XP_ALREADY_ENABLED, "Acúmulo XP já habilitado");
		addTranslatedMessage(CustomMessageId.DREDGION_LEVEL_TOO_LOW, "Seu nível atual é muito baixo para entrar no Dredgion.");
		addTranslatedMessage(CustomMessageId.DEFAULT_FINISH_MESSAGE, "Terminar!");

		/**
		 * Asmo and Ely Channel
		 */
		addTranslatedMessage(CustomMessageId.ASMO_FAIL, "Você é Elyos! Você não pode usar este chat. Ely <mensagem> para postar uma nova facção de bate-papo!");
		addTranslatedMessage(CustomMessageId.ELY_FAIL, "Você é Asmo! Você não pode usar este chat. Asmo <mensagem> para postar uma nova facção de bate-papo!");

		/**
		 * PvP Service
		 */
		addTranslatedMessage(CustomMessageId.ADV_WINNER_MSG, "[PvP System] Você matou um jogador ");
		addTranslatedMessage(CustomMessageId.ADV_LOSER_MSG, "[PvP System] Você foi morto por ");
		addTranslatedMessage(CustomMessageId.PLAP_LOST1, "[PL-AP] Você perdeu ");
		addTranslatedMessage(CustomMessageId.PLAP_LOST2, "% do seu total de ap");
		addTranslatedMessage(CustomMessageId.PVP_NO_REWARD1, "Você não ganhou nada para matar");
		addTranslatedMessage(CustomMessageId.PVP_NO_REWARD2, " porque você o matou muitas vezes!");

		/**
		 * Reward Service Login Messages
		 */
		addTranslatedMessage(CustomMessageId.REWARD10, "Você pode. Comece a usar um nível para obter 10 Features!");
		addTranslatedMessage(CustomMessageId.REWARD30, "Você pode. Comece a usar um nível de 30 equipamentos conseguir!");
		addTranslatedMessage(CustomMessageId.REWARD40, "Você pode. Comece a usar um nível para obter 40 Recursos!");
		addTranslatedMessage(CustomMessageId.REWARD50, "Você pode. Iniciar um uso nível 50 características de obter!");
		addTranslatedMessage(CustomMessageId.REWARD60, "Você pode. Comece a usar um nível de 60 Features conseguir!");

		/**
		 * Advanced PvP System
		 */
		addTranslatedMessage(CustomMessageId.PVP_ADV_MESSAGE1, "Mapa PvP de hoje: Reshanta");
		addTranslatedMessage(CustomMessageId.PVP_ADV_MESSAGE2, "Mapa PvP de hoje: Tiamaranta");
		addTranslatedMessage(CustomMessageId.PVP_ADV_MESSAGE3, "Mapa PvP de hoje: Inggison/Gelkmaros");
		addTranslatedMessage(CustomMessageId.PVP_ADV_MESSAGE4, "Mapa PvP de hoje: Idian Depths");
		addTranslatedMessage(CustomMessageId.PVP_ADV_MESSAGE5, "Mapa PvP de hoje: Katalam");
		addTranslatedMessage(CustomMessageId.PVP_ADV_MESSAGE6, "Mapa PvP de hoje: Danaria");

		/**
		 * Wedding related
		 */
		addTranslatedMessage(CustomMessageId.WEDDINGNO1, "Você não pode usar este comando durante a luta!");
		addTranslatedMessage(CustomMessageId.WEDDINGNO2, "Casamento não começou!");
		addTranslatedMessage(CustomMessageId.WEDDINGNO3, "Você se recusou a casar!");
		addTranslatedMessage(CustomMessageId.WEDDINGYES, "Você aceitou o casamento!");

		/**
		 * Clean Command related
		 */
		addTranslatedMessage(CustomMessageId.CANNOTCLEAN, "Você tem que digitar um ID Item, ou postar um link!");
		addTranslatedMessage(CustomMessageId.CANNOTCLEAN2, "Você não possui este item!");
		addTranslatedMessage(CustomMessageId.SUCCESSCLEAN, "O item foi removido com sucesso a partir de um cubo!");

		/**
		 * Mission check command related
		 */
		addTranslatedMessage(CustomMessageId.SUCCESCHECKED, "Missão verificado com sucesso!");

		/**
		 * No Exp Command
		 */
		addTranslatedMessage(CustomMessageId.EPACTIVATED, "Seu EP foram re-ativado!");
		addTranslatedMessage(CustomMessageId.ACTODE, "Para desativar o seu EP, use noexp.");
		addTranslatedMessage(CustomMessageId.EPDEACTIVATED, "Seu EP foram desativados!");
		addTranslatedMessage(CustomMessageId.DETOAC, "Para activar o seu EP, use noexp.");

		/**
		 * Auto Quest Command
		 */
		addTranslatedMessage(CustomMessageId.WRONGQID, "Por favor, indique um Id busca correto!");
		addTranslatedMessage(CustomMessageId.NOTSTARTED, "Missão não pôde ser iniciado!");
		addTranslatedMessage(CustomMessageId.NOTSUPPORT, "Essa busca não é suportado por este comando!");

		/**
		 * Quest Restart Command
		 */
		addTranslatedMessage(CustomMessageId.CANNOTRESTART, "] não pode ser reiniciado");

		/**
		 * Exchange Toll Command
		 */
		addTranslatedMessage(CustomMessageId.TOLLTOBIG, "Você tem muitos Toll!");
		addTranslatedMessage(CustomMessageId.TOLOWAP, "Você não tem o suficiente AP!");
		addTranslatedMessage(CustomMessageId.TOLOWTOLL, "Você não tem Toll suficiente!");
		addTranslatedMessage(CustomMessageId.WRONGTOLLNUM, "Algo deu errado!");

		/**
		 * Cube Command
		 */
		addTranslatedMessage(CustomMessageId.CUBE_ALLREADY_EXPANDED, "Seu cubo é completamente estendido!");
		addTranslatedMessage(CustomMessageId.CUBE_SUCCESS_EXPAND, "Seu cubo é expandido com sucesso!");

		/**
		 * GMList Command
		 */
		addTranslatedMessage(CustomMessageId.ONE_GM_ONLINE, "Um membro da equipe está online: ");
		addTranslatedMessage(CustomMessageId.MORE_GMS_ONLINE, "Os seguintes membros da equipe on-line: ");
		addTranslatedMessage(CustomMessageId.NO_GM_ONLINE, "Nenhum membro da equipe on-line!");

		/**
		 * Go Command (PvP Command)
		 */
		addTranslatedMessage(CustomMessageId.NOT_USE_WHILE_FIGHT, "Você não pode usar este comando durante a luta!");
		addTranslatedMessage(CustomMessageId.NOT_USE_ON_PVP_MAP, "Você não pode usar este comando em um PvP Mapa!");
		addTranslatedMessage(CustomMessageId.LEVEL_TOO_LOW, "Você pode usar este comando somente com nível 55 ou superior para usar!");

		/**
		 * Paint Command
		 */
		addTranslatedMessage(CustomMessageId.WRONG_TARGET, "Por favor, use um alvo legal!");

		/**
		 * Shiva Command
		 */
		addTranslatedMessage(CustomMessageId.ENCHANT_SUCCES, "Todos os seus itens foram encantado para: ");
		addTranslatedMessage(CustomMessageId.ENCHANT_INFO, "Info: Este comando todos os seus itens encantados em <valor>!");
		addTranslatedMessage(CustomMessageId.ENCHANT_SAMPLE, "Por exemplo, se encantar todos os seus itens a 15 (eq 15.)");

		/**
		 * Userinfo Command
		 */
		addTranslatedMessage(CustomMessageId.CANNOT_SPY_PLAYER, "Você não pode obter informações de outros jogadores!");

		/**
		 * Check AFK Status
		 */
		addTranslatedMessage(CustomMessageId.KICKED_AFK_OUT, "Você foi expulso por ser inativo por muito tempo.");

		/**
		 * Exchange Command
		 */
		addTranslatedMessage(CustomMessageId.NOT_ENOUGH_ITEM, "Você não tem o suficiente de: ");
		addTranslatedMessage(CustomMessageId.NOT_ENOUGH_AP, "Você não tem ap o suficiente, você só tem: ");

		/**
		 * Medal Command
		 */
		addTranslatedMessage(CustomMessageId.NOT_ENOUGH_SILVER, "Você não tem número suficiente de medalhas de prata.");
		addTranslatedMessage(CustomMessageId.NOT_ENOUGH_GOLD, "Você não tem medalhas de ouro suficientes.");
		addTranslatedMessage(CustomMessageId.NOT_ENOUGH_PLATIN, "Você não tem medalhas de platina suficientes.");
		addTranslatedMessage(CustomMessageId.NOT_ENOUGH_MITHRIL, "Você não tem medalhas mithril suficientes.");
		addTranslatedMessage(CustomMessageId.NOT_ENOUGH_AP2, "Você não tem ap o suficiente, você precisa: ");
		addTranslatedMessage(CustomMessageId.EXCHANGE_SILVER, "Você trocou [item:186000031] por [item:186000030].");
		addTranslatedMessage(CustomMessageId.EXCHANGE_GOLD, "Você trocou [item:186000030] por [item:186000096].");
		addTranslatedMessage(CustomMessageId.EXCHANGE_PLATIN, "Você trocou [item:186000096] por [item:186000147].");
		addTranslatedMessage(CustomMessageId.EXCHANGE_MITHRIL, "Você trocou [item:186000147] por [item:186000223].");
		addTranslatedMessage(CustomMessageId.EX_SILVER_INFO, "\nSyntax: prata .medal - Troca de prata para ouro.");
		addTranslatedMessage(CustomMessageId.EX_GOLD_INFO, "\nSyntax: ouro .medal - Troca Gold Platin.");
		addTranslatedMessage(CustomMessageId.EX_PLATIN_INFO, "\nSyntax: platina .medal - Exchnage Platin para Mitral.");
		addTranslatedMessage(CustomMessageId.EX_MITHRIL_INFO, "\nSyntax: .medal Mitral - para Honrosa Mitral Mitral Exchange.");

		/**
		 * Legendary Raid Spawn Events
		 */
		addTranslatedMessage(CustomMessageId.LEGENDARY_RAID_SPAWNED_ASMO, "[Spawn do Evento] Ragnarok foi chamado para Elyos e Beluslan!");
		addTranslatedMessage(CustomMessageId.LEGENDARY_RAID_SPAWNED_ELYOS, "[Spawn do Evento] Omega foi chamado para Elyos na Heiron!");
		addTranslatedMessage(CustomMessageId.LEGENDARY_RAID_DESPAWNED_ASMO, "[Spawn do Evento] Ragnarok foi removido, ninguém o matou!");
		addTranslatedMessage(CustomMessageId.LEGENDARY_RAID_DESPAWNED_ELYOS, "[Spawn do Evento] Omega foi removido, ninguém o matou!");

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
		addTranslatedMessage(CustomMessageId.NOT_ENOUGH_MEDALS, "Você não tem Medalhas suficientes, é necessário:");
		addTranslatedMessage(CustomMessageId.PLATE_ARMOR_USE_INFO, "Use .items and the equal ID (Example: .items 1");
		addTranslatedMessage(CustomMessageId.LEATHER_ARMOR_USE_INFO, "Use .items and the equal ID (Example: .items 6");
		addTranslatedMessage(CustomMessageId.CLOTH_ARMOR_USE_INFO, "Use .items and the equal ID (Example: .items 11");
		addTranslatedMessage(CustomMessageId.CHAIN_ARMOR_USE_INFO, "Use .items and the equal ID (Example: .items 16");
		addTranslatedMessage(CustomMessageId.WEAPONS_USE_INFO, "Use .items and the equal ID (Example: .items 21");
		addTranslatedMessage(CustomMessageId.SUCCESSFULLY_TRADED, "Você fez com sucesso o seu negocio");

		/**
		 * Moltenus Announce
		 */
		addTranslatedMessage(CustomMessageId.MOLTENUS_APPEAR, "Moltenus Fragment of the Wrath has spawn in the Abyss.");
		addTranslatedMessage(CustomMessageId.MOLTENUS_DISAPPEAR, "Moltenus Fragment of the Wrath has disappear.");

		/**
		 * Dredgion Announce
		 */
		addTranslatedMessage(CustomMessageId.DREDGION_ASMO_GROUP, "Um grupo Asmodian está esperando por Dredgion.");
		addTranslatedMessage(CustomMessageId.DREDGION_ELYOS_GROUP, "Um grupo Elyos está esperando por Dredgion.");
		addTranslatedMessage(CustomMessageId.DREDGION_ASMO, "Um só Asmodian está esperando por Dredgion.");
		addTranslatedMessage(CustomMessageId.DREDGION_ELYOS, "Um só Elyos está esperando por Dredgion.");

		/**
		 * PvP Service
		 */
		addTranslatedMessage(CustomMessageId.PVP_TOLL_REWARD1, "Você ganhou");
		addTranslatedMessage(CustomMessageId.PVP_TOLL_REWARD2, " Abso'Points.");

		/**
		 * Invasion Rift
		 */
		addTranslatedMessage(CustomMessageId.INVASION_RIFT_MIN_LEVEL, "Seu nível é muito baixo para entrar.");
		addTranslatedMessage(CustomMessageId.INVASION_RIFT_ELYOS, "Uma fenda para Pandaemonium está aberto a Inggison");
		addTranslatedMessage(CustomMessageId.INVASION_RIFT_ASMOS, "Uma fenda para Sanctum está aberto ao Gelkmaros");

		/**
		 * Additional Chest Drops
		 */
		addTranslatedMessage(CustomMessageId.DECOMPOSE_SERVICE_MESSAGE1, "%s obteve %s de %s.");
		addTranslatedMessage(CustomMessageId.DECOMPOSE_SERVICE_MESSAGE2, "%s obteve adicional %s de %s (Premium).");
		addTranslatedMessage(CustomMessageId.DECOMPOSE_SERVICE_MESSAGE3, "%s obteve adicional %s de %s (VIP).");

		/**
		 * PvP Spree Service
		 */
		addTranslatedMessage(CustomMessageId.SPREE1, "Tempestade sangrenta");
		addTranslatedMessage(CustomMessageId.SPREE2, "Carnificina");
		addTranslatedMessage(CustomMessageId.SPREE3, "Genocídio");
		addTranslatedMessage(CustomMessageId.KILL_COUNT, "Mortes consecutivas: ");
		addTranslatedMessage(CustomMessageId.CUSTOM_MSG1, " de ");
		addTranslatedMessage(CustomMessageId.MSG_SPREE1, " iniciou um ");
		addTranslatedMessage(CustomMessageId.MSG_SPREE1_1, " !");
		addTranslatedMessage(CustomMessageId.MSG_SPREE2, " está realizando um verdadeiro ");
		addTranslatedMessage(CustomMessageId.MSG_SPREE2_1, " ! Detê-lo rápido!");
		addTranslatedMessage(CustomMessageId.MSG_SPREE3, " está fazendo um");
		addTranslatedMessage(CustomMessageId.MSG_SPREE3_1, " ! Fuja se você poder!");
		addTranslatedMessage(CustomMessageId.SPREE_END_MSG1, "A matança de ");
		addTranslatedMessage(CustomMessageId.SPREE_END_MSG2, " foi parada por ");
		addTranslatedMessage(CustomMessageId.SPREE_END_MSG3, " depois ");
		addTranslatedMessage(CustomMessageId.SPREE_END_MSG4, " assassinatos ininterruptas!");
		addTranslatedMessage(CustomMessageId.SPREE_MONSTER_MSG, "um monstro");
	}
}
