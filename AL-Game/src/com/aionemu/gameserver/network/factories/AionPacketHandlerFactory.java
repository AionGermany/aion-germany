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
package com.aionemu.gameserver.network.factories;

import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.AionPacketHandler;
import com.aionemu.gameserver.network.aion.clientpackets.*;

/**
 * This factory is responsible for creating {@link AionPacketHandler} object. It also initializes created handler with a set of packet prototypes.<br>
 * Object of this classes uses <tt>Injector</tt> for injecting dependencies into prototype objects.<br>
 * <br>
 *
 * @author Luno, Alcapwnd, Ever, Falke34, FrozenKiller
 * @version 4.8.x.x
 */
public class AionPacketHandlerFactory {

	private AionPacketHandler handler;

	public static AionPacketHandlerFactory getInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * Creates new instance of <tt>AionPacketHandlerFactory</tt><br>
	 */
	public AionPacketHandlerFactory() {

		handler = new AionPacketHandler();
		
		addPacket(new CM_VERSION_CHECK(0xEF, State.CONNECTED)); // 7.2 EU
		addPacket(new CM_TIME_CHECK(0xBD, State.CONNECTED, State.AUTHED, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_L2AUTH_LOGIN_CHECK(0x141, State.CONNECTED)); // 7.2 EU
		addPacket(new CM_MAC_ADDRESS(0x1A9, State.CONNECTED, State.AUTHED, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_CHARACTER_LIST(0x146, State.AUTHED)); // 7.2 EU
		addPacket(new CM_MAY_LOGIN_INTO_GAME(0x1AA, State.AUTHED)); // 7.2 EU
		addPacket(new CM_ENTER_WORLD(0xB7, State.AUTHED)); // 7.2 EU
		addPacket(new CM_UI_SETTINGS(0xB5, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_MOTION(0x2F6, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_WINDSTREAM(0x131, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_STOP_TRAINING(0x119, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_REVIVE(0xF0, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_DUEL_REQUEST(0x162, State.IN_GAME)); // 7.2 EU
		//addPacket(new CM_CRAFT(0x155, State.IN_GAME)); // 5.4 EU removed?
		addPacket(new CM_QUESTION_RESPONSE(0x104, State.IN_GAME)); // 7.x EU
		addPacket(new CM_OPEN_STATICDOOR(0xC6, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_SPLIT_ITEM(0x149, State.IN_GAME)); // 7.2 EU
		//addPacket(new CM_CUSTOM_SETTINGS(0xD2, State.IN_GAME)); // 7.x EU
		addPacket(new CM_PLAY_MOVIE_END(0x2FC, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_LEVEL_READY(0xB4, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_ENTER_WORLD(0xC3, State.AUTHED)); // 5.4 EU
		//addPacket(new CM_TIME_CHECK(0xE9, State.CONNECTED, State.AUTHED, State.IN_GAME)); // 5.4 EU
		addPacket(new CM_QUIT(0xF2, State.AUTHED, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_CREATE_CHARACTER(0x147, State.AUTHED)); // 7.2 EU
		addPacket(new CM_CHARACTER_PASSKEY(0x182, State.AUTHED)); // 7.2 EU
		addPacket(new CM_MOVE(0x11F, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_CASTSPELL(0xCC, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_EMOTION(0x11A, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_TITLE_SET(0x1D5, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_DELETE_ITEM(0x160, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_QUEST_SHARE(0x150, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_DELETE_QUEST(0x2FF, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_ABYSS_RANKING_PLAYERS(0x1A8, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_ABYSS_RANKING_LEGIONS(0x166, State.IN_GAME)); // 7.2 EU
		//addPacket(new CM_PRIVATE_STORE(0x133, State.IN_GAME)); // 5.4 EU removed?
		addPacket(new CM_USE_ITEM(0xD0, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_TARGET_SELECT(0xCE, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_SHOW_DIALOG(0x123, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_CHECK_NICKNAME(0x19D, State.AUTHED)); // 7.2 EU
		//addPacket(new CM_PRIVATE_STORE_NAME(0x144, State.IN_GAME)); // 6.x EU removed?
		addPacket(new CM_DELETE_CHARACTER(0x144, State.AUTHED)); // 7.2 EU
		addPacket(new CM_RESTORE_CHARACTER(0x145, State.AUTHED)); // 7.2 EU
		addPacket(new CM_MACRO_CREATE(0x19F, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_MACRO_DELETE(0x19C, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_GATHER(0xC2, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_INSTANCE_INFO(0x1AC, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_CLIENT_COMMAND_ROLL(0x1A4, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_START_LOOT(0x14B, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_CLOSE_DIALOG(0x120, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_DIALOG_SELECT(0x121, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_BUY_ITEM(0x122, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_EQUIP_ITEM(0xD1, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_TELEPORT_SELECT(0x140, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_LOOT_ITEM(0x14A, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_QUESTIONNAIRE(0x164, State.IN_GAME)); // 7.x EU
		addPacket(new CM_ATTACK(0xCF, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_PET(0xC1, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_PET_EMOTE(0xC0, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_CHALLENGE_LIST(0x1D4, State.IN_GAME)); // 7.2 EU

		// ********************(FRIEND LIST)*********************
		addPacket(new CM_SHOW_FRIENDLIST(0x1D6, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_FRIEND_ADD(0x15F, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_FRIEND_DEL(0x15C, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_FRIEND_STATUS(0x19A, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_FRIEND_EDIT(0x1DF, State.IN_GAME)); // 7.2 EU (Notiz Friendlist)
		addPacket(new CM_SET_NOTE(0x125, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_MARK_FRIENDLIST(0x15E, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_SHOW_BLOCKLIST(0x14E, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_BLOCK_ADD(0x196, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_BLOCK_DEL(0x197, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_PLAYER_SEARCH(0x14F, State.IN_GAME)); // 7.2 EU

		// ********************(LEGION)*********************
		addPacket(new CM_LEGION(0x118, State.IN_GAME)); // 7.2 EU
		//addPacket(new CM_LEGION_WH_KINAH(0x117, State.IN_GAME)); // 5.4 EU
		//addPacket(new CM_LEGION_UPLOAD_INFO(0x16C, State.IN_GAME)); // 6.x EU
		//addPacket(new CM_LEGION_UPLOAD_EMBLEM(0x16D, State.IN_GAME)); // 6.x EU
		addPacket(new CM_LEGION_SEARCH(0x1E8, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_LEGION_JOIN_REQUEST(0x1D9, State.IN_GAME)); // 6.x EU
		//addPacket(new CM_LEGION_JOIN_REQUEST_CANCEL(0x1DA, State.IN_GAME)); // 5.4 EU
		addPacket(new CM_LEGION_SEND_EMBLEM_INFO(0xBF, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_LEGION_SEND_EMBLEM(0xE6, State.IN_GAME)); // 7.x EU
		//addPacket(new CM_LEGION_MODIFY_EMBLEM(0x106, State.IN_GAME)); // 6.x EU
		addPacket(new CM_LEGION_TABS(0x126, State.IN_GAME)); // 7.2 EU
		//addPacket(new CM_STONESPEAR_SIEGE(0xE4, State.IN_GAME)); // 5.4 EU

		// ******************(GROUP)******************* (BUGGY)
		addPacket(new CM_FIND_GROUP(0x2F8, State.IN_GAME)); // 7.2 EU
		//addPacket(new CM_AUTO_GROUP(0x19F, State.IN_GAME)); // 7.x EU
		addPacket(new CM_INVITE_TO_GROUP(0x10D, State.IN_GAME)); // 7.2 EU
		//addPacket(new CM_GROUP_DISTRIBUTION(0x134, State.IN_GAME)); // 5.4 EU
		//addPacket(new CM_GROUP_LOOT(0x184, State.IN_GAME)); // 6.x EU
		addPacket(new CM_GROUP_DATA_EXCHANGE(0x2FE, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_DISTRIBUTION_SETTINGS(0x1A5, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_SHOW_BRAND(0x1A1, State.IN_GAME)); // 7.2 EU (Group Mark Target etc)

		// ******************(BROKER)******************
		addPacket(new CM_BROKER_LIST(0x16B, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_BROKER_SEARCH(0x168, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_REGISTER_BROKER_ITEM(0x156, State.IN_GAME)); // 7.x EU
		addPacket(new CM_BROKER_ADD_ITEM(0x16F, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_BROKER_SETTLE_LIST(0x16D, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_BROKER_REGISTERED(0x169, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_BUY_BROKER_ITEM(0x16E, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_BROKER_CANCEL_REGISTERED(0x16C, State.IN_GAME)); // 7.2 EU
		//addPacket(new CM_BROKER_SETTLE_ACCOUNT(0x154, State.IN_GAME)); // 7.x EU

		// ******************(PING)******************
		addPacket(new CM_PING_REQUEST(0x13E, State.IN_GAME)); // 7.x EU
		addPacket(new CM_PING(0x11B, State.AUTHED, State.IN_GAME)); // 7.2 EU

		// ******************(SUMMON)******************
		addPacket(new CM_SUMMON_EMOTION(0x17A, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_SUMMON_ATTACK(0x17B, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_SUMMON_CASTSPELL(0x179, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_SUMMON_COMMAND(0x165, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_SUMMON_MOVE(0x175, State.IN_GAME)); // 7.2 EU

		// ******************(MAIL)******************
		addPacket(new CM_CHECK_MAIL_SIZE(0x171, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_CHECK_MAIL_SIZE2(0x181, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_SEND_MAIL(0x170, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_READ_MAIL(0x136, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_READ_EXPRESS_MAIL(0x152, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_DELETE_MAIL(0x135, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_GET_MAIL_ATTACHMENT(0x134, State.IN_GAME)); // 7.2 EU

		// ******************(EXCHANGE)******************
		addPacket(new CM_EXCHANGE_ADD_ITEM(0x12F, State.IN_GAME)); // 7.2 EU
		//addPacket(new CM_EXCHANGE_ADD_KINAH(0x119, State.IN_GAME)); // 5.4 EU
		addPacket(new CM_EXCHANGE_LOCK(0x132, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_EXCHANGE_CANCEL(0x130, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_EXCHANGE_OK(0x133, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_EXCHANGE_REQUEST(0x12E, State.IN_GAME)); // 7.2 EU

		// *************(HOUSE)***************************
		//addPacket(new CM_HOUSE_OPEN_DOOR(0x1AE, State.IN_GAME)); // 5.4 EU
		addPacket(new CM_HOUSE_TELEPORT_BACK(0x13A, State.IN_GAME)); // 5.0
		addPacket(new CM_HOUSE_SCRIPT(0xE0, State.IN_GAME)); // 7.x EU
		addPacket(new CM_HOUSE_TELEPORT(0x18E, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_HOUSE_EDIT(0x2FD, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_USE_HOUSE_OBJECT(0x18C, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_HOUSE_SETTINGS(0x114, State.IN_GAME)); // 6.x EU
		addPacket(new CM_HOUSE_KICK(0x2F7, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_GET_HOUSE_BIDS(0x1AD, State.IN_GAME)); // 7.x EU
		//addPacket(new CM_HOUSE_PAY_RENT(0x1BB, State.IN_GAME)); // 5.4 EU
		//addPacket(new CM_REGISTER_HOUSE(0x1A7, State.IN_GAME)); // 5.4 EU
		addPacket(new CM_PLACE_BID(0x1A0, State.IN_GAME)); // 7.x EU
		//addPacket(new CM_HOUSE_DECORATE(0x116, State.IN_GAME)); // 5.3 EU
		addPacket(new CM_RELEASE_OBJECT(0x18D, State.IN_GAME)); // 7.2 EU

		// ******************(OTHERS)******************
		addPacket(new CM_OBJECT_SEARCH(0xDD, State.IN_GAME)); // 7.x EU
		addPacket(new CM_MOVE_IN_AIR(0x11C, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_VIEW_PLAYER_DETAILS(0x110, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_TELEPORT_DONE(0xBE, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_CHARACTER_EDIT(0xB6, State.AUTHED)); // 7.2 EU
		addPacket(new CM_PLAYER_STATUS_INFO(0x10C, State.IN_GAME)); // 7.2 EU (Mentor etc)
		addPacket(new CM_MANASTONE(0x2F5, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_FUSION_WEAPONS(0x17E, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_ITEM_REMODEL(0x105, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_TOGGLE_SKILL_DEACTIVATE(0xED, State.IN_GAME)); // 6.x EU
		//addPacket(new CM_RECIPE_DELETE(0x110, State.IN_GAME)); // 5.3 EU
		addPacket(new CM_REMOVE_ALTERED_STATE(0xD2, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_MAY_QUIT(0xF3, State.AUTHED, State.IN_GAME)); // 7.2 EU
		//addPacket(new CM_REPORT_PLAYER(0x1E1, State.IN_GAME)); // 7.2 EU
		//addPacket(new CM_PLAYER_LISTENER(0x1A8, State.IN_GAME)); // 7.x EU NOT SURE (TODO)
		//addPacket(new CM_BONUS_TITLE(0x1BC, State.IN_GAME)); // 7.x EU
		//addPacket(new CM_BUY_TRADE_IN_TRADE(0x113, State.IN_GAME)); // 5.6 (Machtkampf aufwertung)
		addPacket(new CM_BREAK_WEAPONS(0x17F, State.IN_GAME)); // 7.2 EU
		// addPacket(new CM_CHARGE_ITEM(0x115, State.IN_GAME)); // 5.4
		addPacket(new CM_USE_CHARGE_SKILL(0x1B6, State.IN_GAME)); // 5.4
		//addPacket(new CM_RECONNECT_AUTH(0x18E, State.AUTHED)); // 7.x EU
		addPacket(new CM_BLOCK_SET_REASON(0x1A3, State.IN_GAME)); // 7.2 EU
		//addPacket(new CM_INSTANCE_LEAVE(0x11F, State.IN_GAME)); // 5.4 EU
		addPacket(new CM_APPEARANCE(0x181, State.IN_GAME)); // 6.x old 0x18D
		// addPacket(new CM_CAPTCHA(0xC8, State.IN_GAME)); // 4.9
		// addPacket(new CM_COMPOSITE_STONES(0x1AE, State.IN_GAME)); // 4.9
		// addPacket(new CM_MEGAPHONE(0x1B5, State.IN_GAME)); // 5.4 EU
		// addPacket(new CM_SUBZONE_CHANGE(0x17D, State.IN_GAME)); // 5.0 NOT SURE
		addPacket(new CM_MOVE_ITEM(0x148, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_SELECTITEM_OK(0x1D8, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_GAMEGUARD(0x154, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_CHANGE_CHANNEL(0x198, State.IN_GAME)); // 7.2 EU

		// // ******************(Fast Track Server)******************
		addPacket(new CM_FAST_TRACK_CHECK(0x1AE, State.IN_GAME)); // 7.x EU
		// addPacket(new CM_FAST_TRACK(0x191, State.IN_GAME)); // 5.4
		addPacket(new CM_DIRECT_ENTER_WORLD(0x187, State.IN_GAME)); // 7.2 EU
		//addPacket(new CM_FAST_TRACK_MOVE(0x190, State.IN_GAME)); // 5.4 TODO

		// // ******************(CHAT)******************
		addPacket(new CM_CHAT_AUTH(0x19E, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_CHAT_MESSAGE_PUBLIC(0xCA, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_CHAT_GROUP_INFO(0x128, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_CHAT_MESSAGE_WHISPER(0xCB, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_CHAT_PLAYER_INFO(0x116, State.IN_GAME)); // 7.2 EU

		// ********************(WEAPON/ARMOR TUNE)*********************
		addPacket(new CM_TUNE(0x1DB, State.IN_GAME)); // 7.2 EU
		//addPacket(new CM_TUNE_RESULT(0x1CA, State.IN_GAME)); // 5.6

		// // ******************(Emu Feature)******************
		addPacket(new CM_FATIGUE_RECOVER(0x135, State.IN_GAME));

		// // /////////////////// NEW 4.7 //////////////////////
		addPacket(new CM_HOTSPOT_TELEPORT(0x1E0, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_ITEM_UPGRADE(0x1E7, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_UPGRADE_ARCADE(0x1E6, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_FILE_VERIFY(0x129, State.IN_GAME)); // 7.2 EU

		// // /////////////////// NEW 4.9 //////////////////////
		addPacket(new CM_EXPAND_CUBE(0x1C2, State.IN_GAME)); // 7.x EU
		//addPacket(new CM_INTRUDER_SCAN(0x18C, State.IN_GAME)); // 5.4 EU

		// // /////////////////// SHUGO SWEEP/LUCKY DICE //////////////////////
		//addPacket(new CM_SHUGO_SWEEP(0x1D6, State.IN_GAME)); //5.8 EU

		// // ////////////////// LUNA SYSTEM /////////////////////
		addPacket(new CM_LUNA_SYSTEM(0x1B7, State.IN_GAME)); // 7.2 EU
		//addPacket(new CM_LUNA_INSTANCE(0x1CE, State.IN_GAME)); // 7.2 EU
		//addPacket(new CM_LUNA_INSTANCE_ENTRY(0x1EA, State.IN_GAME)); // 7.2 EU

		// // ////////////////// MAGIC CRAFT //////////////////
		addPacket(new CM_MAGIC_CRAFT(0x1B8, State.IN_GAME)); // 7.2

		// // /////////////// EQUIPMENT SETTING //////////////////
		addPacket(new CM_EQUIPMENT_SETTING_SAVE(0x1C7, State.IN_GAME)); // 7.2 TODO
		addPacket(new CM_EQUIPMENT_SETTING_USE(0x1C4, State.IN_GAME)); // 7.2 TODO

		// // //////////////// DAEVANION SKILL //////////////////
		addPacket(new CM_DAEVANION_SKILL_ENCHANT(0x1D3, State.IN_GAME)); // 7.2 TODO
		addPacket(new CM_DAEVANION_SKILL_FUSION(0x1D0, State.IN_GAME)); // 7.2 TODO

		// // /////////////////// GM PACKET ////////////////////
		addPacket(new CM_GM_COMMAND_SEND(0x115, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_GM_BOOKMARK(0x2FF, State.IN_GAME)); // 7.x EU

		// // ////////////////// UNK PACKET ////////////////////
		addPacket(new CM_UNK_122(0x10B, State.AUTHED)); // 7.2 EU
		// addPacket(new CM_UNK_1DB(0x1DB, State.IN_GAME)); // 5.0 TODO
		addPacket(new CM_UNK_E3(0x117, State.IN_GAME)); // 7.2 EU
		// addPacket(new CM_UNK_1EA(0x1EA, State.IN_GAME)); // 5.3 EU
		addPacket(new CM_RANK_LIST(0x1BC, State.IN_GAME)); // 7.2 EU TODO
		addPacket(new CM_MY_DOCUMENTATION(0x1BD, State.IN_GAME)); // 7.2 EU TODO

		// // /////////////////////////////////////////////////
		addPacket(new CM_SECURITY_TOKEN(0x191, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_ENCHANTMENT_EXTRACTION(0x1F0, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_USE_PACK_ITEM(0x1C8, State.IN_GAME)); // 5.4 EU
		addPacket(new CM_SELL_BROKEN_ITEMS(0x1B4, State.IN_GAME)); // 7.2 TODO
		addPacket(new CM_AUTOMATIC_GOLDENSTAR(0x1BE, State.IN_GAME)); // 7.2 EU
		addPacket(new CM_TELEPORT_BACK(0x10E, State.IN_GAME)); // 7.2 EU TODO
		addPacket(new CM_SKILL_ANIMATION(0x1B9, State.IN_GAME)); // 7.2 EU TODO
		addPacket(new CM_MINIONS(0x1CA, State.IN_GAME)); // 7.2 TODO
		addPacket(new CM_NEVIWIND_CANYON(0x1C5, State.IN_GAME)); // 7.2 EU TODO
		addPacket(new CM_TRANSFOMATION(0x1C8, State.IN_GAME)); // 7.2 TODO
		addPacket(new CM_CUBIC(0x1C1, State.IN_GAME)); // 7.2 EU TODO
		//addPacket(new CM_ATREIAN_PASSPORT(0x1CF, State.IN_GAME)); // 7.x EU TODO
		addPacket(new CM_STIGMA(0x1D2, State.IN_GAME)); // 7.2 TODO
		addPacket(new CM_REMOVE_DYE(0x1C3, State.IN_GAME)); // 7.2 EU TODO
	}

	public AionPacketHandler getPacketHandler() {
		return handler;
	}

	private void addPacket(AionClientPacket prototype) {
		handler.addPacketPrototype(prototype);
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder {

		protected static final AionPacketHandlerFactory instance = new AionPacketHandlerFactory();
	}
}
