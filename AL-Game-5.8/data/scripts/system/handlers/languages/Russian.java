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

public class Russian extends Language {

	public Russian() {
		super("ru");
		addSupportedLanguage("ru_RU");
		addTranslatedMessage(CustomMessageId.SERVER_REVISION, "Версия сервера : %-6s");
		addTranslatedMessage(CustomMessageId.WELCOME_PREMIUM, "Добро пожаловать!");
		addTranslatedMessage(CustomMessageId.WELCOME_REGULAR, "Добро пожаловать!");
		addTranslatedMessage(CustomMessageId.WELCOME_BASIC, "Добро пожаловать!");
		addTranslatedMessage(CustomMessageId.SERVERVERSION, "Поддерживаемые версии NCSoft клиента");
		addTranslatedMessage(CustomMessageId.ENDMESSAGE, "Веселиться.");
		addTranslatedMessage(CustomMessageId.ANNOUNCE_GM_CONNECTION, "теперь доступен за поддержку!");
		addTranslatedMessage(CustomMessageId.ANNOUNCE_GM_DECONNECTION, "Сейчас недоступны за поддержку!");
		addTranslatedMessage(CustomMessageId.ANNOUNCE_MEMBER_CONNECTION, "%s только что вступили в Атрею.");
		addTranslatedMessage(CustomMessageId.COMMAND_NOT_ENOUGH_RIGHTS, "У вас нет прав, чтобы использовать эту команду");
		addTranslatedMessage(CustomMessageId.PLAYER_NOT_ONLINE, "Игрока нет сейчас в игре");
		addTranslatedMessage(CustomMessageId.INTEGER_PARAMETER_REQUIRED, "Параметр должен содержать цифры");
		addTranslatedMessage(CustomMessageId.INTEGER_PARAMETERS_ONLY, "Параметр должен содержать только цифры");
		addTranslatedMessage(CustomMessageId.SOMETHING_WRONG_HAPPENED, "Произошла ошибка");
		addTranslatedMessage(CustomMessageId.COMMAND_DISABLED, "Команда неактивна");
		addTranslatedMessage(CustomMessageId.COMMAND_ADD_SYNTAX, "Синтекс: //add <имя игрока> <id предмета> [<количество>]");
		addTranslatedMessage(CustomMessageId.COMMAND_ADD_ADMIN_SUCCESS, "Предмет(ы) успешно выдан игроку %s");
		addTranslatedMessage(CustomMessageId.COMMAND_ADD_PLAYER_SUCCESS, "Администратор %s добавил вам %d предмет(ы)");
		addTranslatedMessage(CustomMessageId.COMMAND_ADD_FAILURE, "Предмет %d не существует и/или не может быть добавлен %s");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDDROP_SYNTAX, "Синтекс: //adddrop <id моба> <id вещи> <min> <max> <шанс>");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDSET_SYNTAX, "Синтекс: //addset <имя игрока> <id сета>");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDSET_SET_DOES_NOT_EXISTS, "Сет %d не существует");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDSET_NOT_ENOUGH_SLOTS, "Не хватает %d слотов в инвентаре, чтобы добавить этот сет");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDSET_CANNOT_ADD_ITEM, "Предмет %d не может быть добавлен к %s");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDSET_ADMIN_SUCCESS, "Сет %d успешно выдан %s");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDSET_PLAYER_SUCCESS, "%s добавил вам сет");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDSKILL_SYNTAX, "Синтекс: //addskill <id скилла> <уровен скилла");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDSKILL_ADMIN_SUCCESS, "Скилл %d успешно добавлен %s");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDSKILL_PLAYER_SUCCESS, "%s добавил вам скилл");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDTITLE_SYNTAX, "Синтекс: //addtitle <id титула> <имя игрока> [special]");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDTITLE_TITLE_INVALID, "Титул должен быть от 1 до 50");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDTITLE_CANNOT_ADD_TITLE_TO_ME, "Вы не можете добавить себе титул %d ");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDTITLE_CANNOT_ADD_TITLE_TO_PLAYER, "Невозможно добавить титул %d к %s");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDTITLE_ADMIN_SUCCESS_ME, "Вы успешно добавили титул %d себе");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDTITLE_ADMIN_SUCCESS, "Вы успешно добавили игроку %s титул %d");
		addTranslatedMessage(CustomMessageId.COMMAND_ADDTITLE_PLAYER_SUCCESS, "%s добавил вам титул %d");
		addTranslatedMessage(CustomMessageId.COMMAND_SEND_SYNTAX, "Синтекс: //send <имя файла>");
		addTranslatedMessage(CustomMessageId.COMMAND_SEND_MAPPING_NOT_FOUND, "%s не найдено");
		addTranslatedMessage(CustomMessageId.COMMAND_SEND_NO_PACKET, "Пакет не послан");
		addTranslatedMessage(CustomMessageId.CHANNEL_WORLD_DISABLED, "Канал %s закрыт, используйте канал %s или %s исходя из вашей расы");
		addTranslatedMessage(CustomMessageId.CHANNEL_ALL_DISABLED, "Все каналы деактивированы");
		addTranslatedMessage(CustomMessageId.CHANNEL_ALREADY_FIXED, "Ваш канал успешно установлен %s");
		addTranslatedMessage(CustomMessageId.CHANNEL_FIXED, "Установлен канал %s");
		addTranslatedMessage(CustomMessageId.CHANNEL_NOT_ALLOWED, "Вы не можете использовать этот канал");
		addTranslatedMessage(CustomMessageId.CHANNEL_FIXED_BOTH, "Установлены каналы %s и %s");
		addTranslatedMessage(CustomMessageId.CHANNEL_UNFIX_HELP, "Напишите %s чтобы выйти из канала"); // ;)
		addTranslatedMessage(CustomMessageId.CHANNEL_NOT_FIXED, "Вы не установлены на канал");
		addTranslatedMessage(CustomMessageId.CHANNEL_FIXED_OTHER, "Ваш чат не установлен на этом канале, но на %s");
		addTranslatedMessage(CustomMessageId.CHANNEL_RELEASED, "Вы вышли из канала %s");
		addTranslatedMessage(CustomMessageId.CHANNEL_RELEASED_BOTH, "Вы вышли из %s и %s");
		addTranslatedMessage(CustomMessageId.CHANNEL_BAN_ENDED, "Вы можете опять присоединиться к каналам");
		addTranslatedMessage(CustomMessageId.CHANNEL_BAN_ENDED_FOR, "Игрок %s снова может присоединяться к каналам");
		addTranslatedMessage(CustomMessageId.CHANNEL_BANNED, "Вы не можете присоедениться к каналу, так как %s забанил вас по причине: %s, до разблокировки осталось: %s");
		addTranslatedMessage(CustomMessageId.COMMAND_MISSING_SKILLS_STIGMAS_ADDED, "%d умения %d стигма выданы вам");
		addTranslatedMessage(CustomMessageId.COMMAND_MISSING_SKILLS_ADDED, "%d умение добавлено вам");
		addTranslatedMessage(CustomMessageId.USER_COMMAND_DOES_NOT_EXIST, "Этой игровой команды не существует");
		addTranslatedMessage(CustomMessageId.COMMAND_XP_DISABLED, "Получение опыта отключено. Введите .xpon чтобы разблокировать");
		addTranslatedMessage(CustomMessageId.COMMAND_XP_ALREADY_DISABLED, "Получение опыта отключено");
		addTranslatedMessage(CustomMessageId.COMMAND_XP_ENABLED, "Получение опыта включено");
		addTranslatedMessage(CustomMessageId.COMMAND_XP_ALREADY_ENABLED, "Получение опыта уже включено");
		addTranslatedMessage(CustomMessageId.DREDGION_LEVEL_TOO_LOW, "Текущий уровень слишком низкий, чтобы войти в Дерадикон.");
		addTranslatedMessage(CustomMessageId.DEFAULT_FINISH_MESSAGE, "Финиш!");

		/**
		 * Asmo and Ely Channel
		 */
		addTranslatedMessage(CustomMessageId.ASMO_FAIL, "Вы Элиец! Вы не можете использовать данный чат. Синтекс: .ely <сообщение>, для публикации сообщения в чат своей фракции!");
		addTranslatedMessage(CustomMessageId.ELY_FAIL, "Вы Асмодианен! Вы не можете использовать данный чат. Синтекс: .asmo <сообщение>, для публикации сообщения в чат своей фракции!");

		/**
		 * PvP Service
		 */
		addTranslatedMessage(CustomMessageId.ADV_WINNER_MSG, "[ПвП Система] Вы убили игрока ");
		addTranslatedMessage(CustomMessageId.ADV_LOSER_MSG, "[ПвП Система] Вы убиты игроком ");
		addTranslatedMessage(CustomMessageId.PLAP_LOST1, "[Система ОБ] Вы потеряли ");
		addTranslatedMessage(CustomMessageId.PLAP_LOST2, "% от общего кол-ва очков бездны");
		addTranslatedMessage(CustomMessageId.PVP_NO_REWARD1, "Вы не получаете ничего за убийство игрока ");
		addTranslatedMessage(CustomMessageId.PVP_NO_REWARD2, " Вы слишком часто убивали его!");

		/**
		 * Reward Service Login Messages
		 */
		addTranslatedMessage(CustomMessageId.REWARD10, "Вы можете. Start using a level to get 10 Features!");
		addTranslatedMessage(CustomMessageId.REWARD30, "Вы можете. Start to use a Level 30 Equipment get!");
		addTranslatedMessage(CustomMessageId.REWARD40, "Вы можете. Start using a level to get 40 Features!");
		addTranslatedMessage(CustomMessageId.REWARD50, "Вы можете. Start a level use 50 features to get!");
		addTranslatedMessage(CustomMessageId.REWARD60, "Вы можете. Start to use a Level 60 Features get!");

		/**
		 * Advanced PvP System
		 */
		addTranslatedMessage(CustomMessageId.PVP_ADV_MESSAGE1, "Сегодня ПвП локация: Арэшурат");
		addTranslatedMessage(CustomMessageId.PVP_ADV_MESSAGE2, "Сегодня ПвП локация: Тиамаранта");
		addTranslatedMessage(CustomMessageId.PVP_ADV_MESSAGE3, "Сегодня ПвП локация: Ингисон / Келькмарос");
		addTranslatedMessage(CustomMessageId.PVP_ADV_MESSAGE4, "Сегодня ПвП локация: Подземный Каталам");
		addTranslatedMessage(CustomMessageId.PVP_ADV_MESSAGE5, "Сегодня ПвП локация: Каталам");
		addTranslatedMessage(CustomMessageId.PVP_ADV_MESSAGE6, "Сегодня ПвП локация: Данария");

		/**
		 * Wedding related
		 */
		addTranslatedMessage(CustomMessageId.WEDDINGNO1, "Вы не можете использовать эту команду во время боя!");
		addTranslatedMessage(CustomMessageId.WEDDINGNO2, "Свадьба не запущена!");
		addTranslatedMessage(CustomMessageId.WEDDINGNO3, "Вы отказались от предложения руки и сердца!");
		addTranslatedMessage(CustomMessageId.WEDDINGYES, "Да, Вы согласны!");

		/**
		 * Clean Command related
		 */
		addTranslatedMessage(CustomMessageId.CANNOTCLEAN, "Вы должны ввести <id вещи> или отправить ссылку!");
		addTranslatedMessage(CustomMessageId.CANNOTCLEAN2, "Это не пренадлежит вам!");
		addTranslatedMessage(CustomMessageId.SUCCESSCLEAN, "Предмет был успешно удален из куба!");

		/**
		 * Mission check command related
		 */
		addTranslatedMessage(CustomMessageId.SUCCESCHECKED, "Миссии успешно прошли проверку!");

		/**
		 * No Exp Command
		 */
		addTranslatedMessage(CustomMessageId.EPACTIVATED, "Получение опыта - активировано!");
		addTranslatedMessage(CustomMessageId.ACTODE, "Для отключения получения опыта, используйте команду: .noexp");
		addTranslatedMessage(CustomMessageId.EPDEACTIVATED, "Получение опыта - отключено!");
		addTranslatedMessage(CustomMessageId.DETOAC, "Для активации получения опыта, повторно используйте команду: .noexp");

		/**
		 * Auto Quest Command
		 */
		addTranslatedMessage(CustomMessageId.WRONGQID, "Пожалуйста, введите правильное <id задания>!");
		addTranslatedMessage(CustomMessageId.NOTSTARTED, "Задание нельзя запустить!");
		addTranslatedMessage(CustomMessageId.NOTSUPPORT, "Нельзя использовать команду для данного задания!");

		/**
		 * Quest Restart Command
		 */
		addTranslatedMessage(CustomMessageId.CANNOTRESTART, "] нельзя перезапустить");

		/**
		 * Exchange Toll Command
		 */
		addTranslatedMessage(CustomMessageId.TOLLTOBIG, "У вас слишком много Поинтов!");
		addTranslatedMessage(CustomMessageId.TOLOWAP, "У вас недостаточно очков бездны!");
		addTranslatedMessage(CustomMessageId.TOLOWTOLL, "У вас недостаточно Поинтов!");
		addTranslatedMessage(CustomMessageId.WRONGTOLLNUM, "Что-то пошло не так!");

		/**
		 * Cube Command
		 */
		addTranslatedMessage(CustomMessageId.CUBE_ALLREADY_EXPANDED, "Ваш куб увеличен до максимума!");
		addTranslatedMessage(CustomMessageId.CUBE_SUCCESS_EXPAND, "Ваш куб успешно увеличен!");

		/**
		 * GMList Command
		 */
		addTranslatedMessage(CustomMessageId.ONE_GM_ONLINE, "Представитель администрации онлайн: ");
		addTranslatedMessage(CustomMessageId.MORE_GMS_ONLINE, "Представители администрации онлайн: ");
		addTranslatedMessage(CustomMessageId.NO_GM_ONLINE, "Представителей администрации нет в игре!");

		/**
		 * Go Command (PvP Command)
		 */
		addTranslatedMessage(CustomMessageId.NOT_USE_WHILE_FIGHT, "Вы не можете использовать эту команду во время боя!");
		addTranslatedMessage(CustomMessageId.NOT_USE_ON_PVP_MAP, "Вы не можете использовать эту команду в ПвП локации!");
		addTranslatedMessage(CustomMessageId.LEVEL_TOO_LOW, "Вы можете использовать эту команду только с 55-го уровня или выше!");

		/**
		 * Paint Command
		 */
		addTranslatedMessage(CustomMessageId.WRONG_TARGET, "Выбрана неправильная цель!");

		/**
		 * Shiva Command
		 */
		addTranslatedMessage(CustomMessageId.ENCHANT_SUCCES, "Все вещи успешно заточены до: ");
		addTranslatedMessage(CustomMessageId.ENCHANT_INFO, "Информация: эта команда заточет вещи до указанного значения!");
		addTranslatedMessage(CustomMessageId.ENCHANT_SAMPLE, "Например, чтобы заточить все вещи на +15 используйте значение 15.");

		/**
		 * Userinfo Command
		 */
		addTranslatedMessage(CustomMessageId.CANNOT_SPY_PLAYER, "Вы не можете просматривать информацию о других игроках!");

		/**
		 * Check AFK Status
		 */
		addTranslatedMessage(CustomMessageId.KICKED_AFK_OUT, "Вы были отключены, из-за долгого отсутствия за компьютером.");

		/**
		 * Exchange Command
		 */
		addTranslatedMessage(CustomMessageId.NOT_ENOUGH_ITEM, "You dont have enough from: ");
		addTranslatedMessage(CustomMessageId.NOT_ENOUGH_AP, "You dont have enough ap, you only have: ");

		/**
		 * Medal Command
		 */
		addTranslatedMessage(CustomMessageId.NOT_ENOUGH_SILVER, "У вас недостаточно Серебряных медалей.");
		addTranslatedMessage(CustomMessageId.NOT_ENOUGH_GOLD, "У вас недостаточно Золотох медалей.");
		addTranslatedMessage(CustomMessageId.NOT_ENOUGH_PLATIN, "У вас недостаточно Платиновых медалей.");
		addTranslatedMessage(CustomMessageId.NOT_ENOUGH_MITHRIL, "У вас недостаточно Мифриловых медалей.");
		addTranslatedMessage(CustomMessageId.NOT_ENOUGH_AP2, "У вас недостаточно очков бездны, вам нужно: ");
		addTranslatedMessage(CustomMessageId.EXCHANGE_SILVER, "Вы обмениваете: [item:186000031] to [item:186000030].");
		addTranslatedMessage(CustomMessageId.EXCHANGE_GOLD, "Вы обмениваете: [item:186000030] to [item:186000096].");
		addTranslatedMessage(CustomMessageId.EXCHANGE_PLATIN, "Вы обмениваете: [item:186000096] to [item:186000147].");
		addTranslatedMessage(CustomMessageId.EXCHANGE_MITHRIL, "Вы обмениваете: [item:186000147] to [item:186000223].");
		addTranslatedMessage(CustomMessageId.EX_SILVER_INFO, "\nСинтекс: .medal silver - Обменять Серебро на Золото.");
		addTranslatedMessage(CustomMessageId.EX_GOLD_INFO, "\nСинтекс: .medal gold - Обменять Золото на Платину.");
		addTranslatedMessage(CustomMessageId.EX_PLATIN_INFO, "\nСинтекс: .medal platinum - Обменять Платину на Мифрил.");
		addTranslatedMessage(CustomMessageId.EX_MITHRIL_INFO, "\nСинтекс: .medal mithril - Обменять Мифрил на Почетный мифрил.");

		/**
		 * Legendary Raid Spawn Events
		 */
		addTranslatedMessage(CustomMessageId.LEGENDARY_RAID_SPAWNED_ASMO, "[Ивент] Мутант Натараз возродился в Бесуслане для битвы с Асмодианами!");
		addTranslatedMessage(CustomMessageId.LEGENDARY_RAID_SPAWNED_ELYOS, "[Ивент] Хранитель Валон возродился в Интердике для битвы с Элийцами!");
		addTranslatedMessage(CustomMessageId.LEGENDARY_RAID_DESPAWNED_ASMO, "[Ивент] Мутант Натараз покинул локацию, никто не убил его!");
		addTranslatedMessage(CustomMessageId.LEGENDARY_RAID_DESPAWNED_ELYOS, "[Ивент] Хранитель Валон покинул локацию, никто не убил его!");

		/**
		 * HonorItems Command
		 */
		addTranslatedMessage(CustomMessageId.PLATE_ARMOR, "Латы");
		addTranslatedMessage(CustomMessageId.LEATHER_ARMOR, "Кожаная броня");
		addTranslatedMessage(CustomMessageId.CLOTH_ARMOR, "Одежда");
		addTranslatedMessage(CustomMessageId.CHAIN_ARMOR, "Кольчуга");
		addTranslatedMessage(CustomMessageId.WEAPONS, "Оружие");
		addTranslatedMessage(CustomMessageId.PLATE_ARMOR_PRICES, "Цена за Латы");
		addTranslatedMessage(CustomMessageId.LEATHER_ARMOR_PRICES, "Цена за Кажанную броню");
		addTranslatedMessage(CustomMessageId.CLOTH_ARMOR_PRICES, "Цена за Одежду");
		addTranslatedMessage(CustomMessageId.CHAIN_ARMOR_PRICES, "Цена за Кольчугу");
		addTranslatedMessage(CustomMessageId.WEAPONS_PRICES, "Цена за Оружие");
		addTranslatedMessage(CustomMessageId.NOT_ENOUGH_MEDALS, "У вас недостаточно медалей, вам нужно: ");
		addTranslatedMessage(CustomMessageId.PLATE_ARMOR_USE_INFO, "Используйте команду: .items и equal ID (Example: .items 1");
		addTranslatedMessage(CustomMessageId.LEATHER_ARMOR_USE_INFO, "Используйте команду: .items и equal ID (Example: .items 6");
		addTranslatedMessage(CustomMessageId.CLOTH_ARMOR_USE_INFO, "Используйте команду: .items и equal ID (Example: .items 11");
		addTranslatedMessage(CustomMessageId.CHAIN_ARMOR_USE_INFO, "Используйте команду: .items и equal ID (Example: .items 16");
		addTranslatedMessage(CustomMessageId.WEAPONS_USE_INFO, "Используйте команду: .items и equal ID (Example: .items 21");
		addTranslatedMessage(CustomMessageId.SUCCESSFULLY_TRADED, "Торговля завершена успешно!");

		/**
		 * Moltenus Announce
		 */
		addTranslatedMessage(CustomMessageId.MOLTENUS_APPEAR, "Moltenus Fragment of the Wrath has spawn in the Abyss.");
		addTranslatedMessage(CustomMessageId.MOLTENUS_DISAPPEAR, "Moltenus Fragment of the Wrath has disappear.");

		/**
		 * Dredgion Announce
		 */
		addTranslatedMessage(CustomMessageId.DREDGION_ASMO_GROUP, "Группа Асмодиан ожидает вход в Дерадикон.");
		addTranslatedMessage(CustomMessageId.DREDGION_ELYOS_GROUP, "Группа Элийцев ожидает вход в Дерадикон.");
		addTranslatedMessage(CustomMessageId.DREDGION_ASMO, "Асмодианен ожидает вход в Дерадикон.");
		addTranslatedMessage(CustomMessageId.DREDGION_ELYOS, "Элиец ожидает вход в Дерадикон.");

		/**
		 * PvP Service
		 */
		addTranslatedMessage(CustomMessageId.PVP_TOLL_REWARD1, "Вы заслужили");
		addTranslatedMessage(CustomMessageId.PVP_TOLL_REWARD2, " Очков Бездны.");

		/**
		 * Invasion Rift
		 */
		addTranslatedMessage(CustomMessageId.INVASION_RIFT_MIN_LEVEL, "Ваш уровень слишком низкий, чтобы войти.");
		addTranslatedMessage(CustomMessageId.INVASION_RIFT_ELYOS, "Пространственный разлом в Пандемониум открылся в локации: Ингисон");
		addTranslatedMessage(CustomMessageId.INVASION_RIFT_ASMOS, "Пространственный разлом в Элизиум открылся в локации: Келькмарос");

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
