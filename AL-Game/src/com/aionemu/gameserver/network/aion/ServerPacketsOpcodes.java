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
package com.aionemu.gameserver.network.aion;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.aionemu.gameserver.network.aion.serverpackets.*;

/**
 * This class is holding opcodes for all server packets. It's used only to have all opcodes in one place
 *
 * @author Luno, alexa026, ATracer, avol, orz, cura, Alcapwnd
 * @version 4.7.5.0
 */
public class ServerPacketsOpcodes {

	private static Map<Class<? extends AionServerPacket>, Integer> opcodes = new HashMap<Class<? extends AionServerPacket>, Integer>();

	static {
		Set<Integer> idSet = new HashSet<Integer>();

		addPacketOpcode(SM_VERSION_CHECK.class, 0x00, idSet);
		addPacketOpcode(SM_STATS_INFO.class, 0x01, idSet);
		addPacketOpcode(SM_STATUPDATE_HP.class, 0x03, idSet);
		addPacketOpcode(SM_STATUPDATE_MP.class, 0x04, idSet);
		addPacketOpcode(SM_ATTACK_STATUS.class, 0x05, idSet);
		addPacketOpcode(SM_STATUPDATE_DP.class, 0x06, idSet);
		addPacketOpcode(SM_DP_INFO.class, 0x07, idSet);
		addPacketOpcode(SM_STATUPDATE_EXP.class, 0x08, idSet);
		addPacketOpcode(SM_NPC_ASSEMBLER.class, 0x0A, idSet);
		addPacketOpcode(SM_LEGION_UPDATE_NICKNAME.class, 0x0B, idSet);
		addPacketOpcode(SM_LEGION_TABS.class, 0x0C, idSet);
		addPacketOpcode(SM_ENTER_WORLD_CHECK.class, 0x0D, idSet);
		addPacketOpcode(SM_NPC_INFO.class, 0x0E, idSet);
		addPacketOpcode(SM_PLAYER_SPAWN.class, 0x0F, idSet);
		addPacketOpcode(SM_FORTRESS_INFO.class, 0xF5, idSet); // 5.6
		addPacketOpcode(SM_GATHERABLE_INFO.class, 0x11, idSet);
		addPacketOpcode(SM_TELEPORT_LOC.class, 0x14, idSet);
		addPacketOpcode(SM_PLAYER_MOVE.class, 0x15, idSet);
		addPacketOpcode(SM_DELETE.class, 0x16, idSet);
		addPacketOpcode(SM_LOGIN_QUEUE.class, 0x17, idSet);
		addPacketOpcode(SM_MESSAGE.class, 0x18, idSet);
		addPacketOpcode(SM_SYSTEM_MESSAGE.class, 0x19, idSet);
		addPacketOpcode(SM_INVENTORY_INFO.class, 0x1A, idSet);
		addPacketOpcode(SM_INVENTORY_ADD_ITEM.class, 0x1B, idSet);
		addPacketOpcode(SM_DELETE_ITEM.class, 0x1C, idSet);
		addPacketOpcode(SM_INVENTORY_UPDATE_ITEM.class, 0x1D, idSet);
		addPacketOpcode(SM_UI_SETTINGS.class, 0x1E, idSet);
		addPacketOpcode(SM_PLAYER_STANCE.class, 0x1F, idSet);
		addPacketOpcode(SM_PLAYER_INFO.class, 0x20, idSet);
		addPacketOpcode(SM_CASTSPELL.class, 0x21, idSet);
		addPacketOpcode(SM_GATHER_STATUS.class, 0x22, idSet);
		addPacketOpcode(SM_GATHER_UPDATE.class, 0x23, idSet);
		addPacketOpcode(SM_UPDATE_PLAYER_APPEARANCE.class, 0x24, idSet);
		addPacketOpcode(SM_EMOTION.class, 0x25, idSet);
		addPacketOpcode(SM_GAME_TIME.class, 0x26, idSet);
		addPacketOpcode(SM_TIME_CHECK.class, 0x27, idSet);
		addPacketOpcode(SM_LOOKATOBJECT.class, 0x28, idSet);
		addPacketOpcode(SM_TARGET_SELECTED.class, 0x29, idSet);
		addPacketOpcode(SM_SKILL_CANCEL.class, 0x2A, idSet);
		addPacketOpcode(SM_CASTSPELL_RESULT.class, 0x2B, idSet);
		addPacketOpcode(SM_SKILL_LIST.class, 0x2C, idSet);
		addPacketOpcode(SM_SKILL_REMOVE.class, 0x2D, idSet);
		addPacketOpcode(SM_SKILL_ACTIVATION.class, 0x2E, idSet);
		addPacketOpcode(SM_BODYGUARD.class, 0x2F, idSet);
		addPacketOpcode(SM_BODYGUARD_END.class, 0x30, idSet);
		addPacketOpcode(SM_ABNORMAL_STATE.class, 0x31, idSet);
		addPacketOpcode(SM_ABNORMAL_EFFECT.class, 0x32, idSet);
		addPacketOpcode(SM_SKILL_COOLDOWN.class, 0x33, idSet);
		addPacketOpcode(SM_QUESTION_WINDOW.class, 0x34, idSet);
		addPacketOpcode(SM_DUEL_REQUEST_CANCEL.class, 0x35, idSet);
		addPacketOpcode(SM_ATTACK.class, 0x36, idSet);
		addPacketOpcode(SM_MOVE.class, 0x37, idSet);
		addPacketOpcode(SM_HEADING_UPDATE.class, 0x39, idSet); // TODO! not used
		addPacketOpcode(SM_TRANSFORM.class, 0x3A, idSet);
		addPacketOpcode(SM_DIALOG_WINDOW.class, 0x3C, idSet);
		addPacketOpcode(SM_SELL_ITEM.class, 0x3E, idSet);
		addPacketOpcode(SM_VIEW_PLAYER_DETAILS.class, 0x41, idSet);
		addPacketOpcode(SM_WEATHER.class, 0x43, idSet);
		addPacketOpcode(SM_PLAYER_STATE.class, 0x44, idSet);
		addPacketOpcode(SM_LEVEL_UPDATE.class, 0x46, idSet);
		addPacketOpcode(SM_QUEST_LIST.class, 0x47, idSet);
		addPacketOpcode(SM_KEY.class, 0x48, idSet);
		addPacketOpcode(SM_SUMMON_PANEL_REMOVE.class, 0x49, idSet);
		addPacketOpcode(SM_EXCHANGE_REQUEST.class, 0x4A, idSet);
		addPacketOpcode(SM_EXCHANGE_ADD_ITEM.class, 0x4B, idSet);
		addPacketOpcode(SM_EXCHANGE_ADD_KINAH.class, 0x4D, idSet);
		addPacketOpcode(SM_EXCHANGE_CONFIRMATION.class, 0x4E, idSet);
		addPacketOpcode(SM_EMOTION_LIST.class, 0x4F, idSet);
		addPacketOpcode(SM_TARGET_UPDATE.class, 0x51, idSet);
		addPacketOpcode(SM_HOUSE_EDIT.class, 0x52, idSet);
		addPacketOpcode(SM_PLASTIC_SURGERY.class, 0x53, idSet);
		addPacketOpcode(SM_CONQUEROR_PROTECTOR.class, 0x54, idSet);
		addPacketOpcode(SM_INFLUENCE_RATIO.class, 0x55, idSet);
		addPacketOpcode(SM_FORTRESS_STATUS.class, 0x56, idSet);
		addPacketOpcode(SM_CAPTCHA.class, 0x57, idSet);
		addPacketOpcode(SM_RENAME.class, 0x58, idSet);
		addPacketOpcode(SM_SHOW_NPC_ON_MAP.class, 0x59, idSet);
		addPacketOpcode(SM_GROUP_INFO.class, 0x5A, idSet);
		addPacketOpcode(SM_GROUP_MEMBER_INFO.class, 0x5B, idSet);
		addPacketOpcode(SM_ABYSS_ARTIFACT_INFO.class, 0xDC, idSet);
		addPacketOpcode(SM_QUIT_RESPONSE.class, 0x62, idSet);
		addPacketOpcode(SM_CHAT_WINDOW.class, 0x63, idSet);
		addPacketOpcode(SM_PET.class, 0x65, idSet);
		addPacketOpcode(SM_ITEM_COOLDOWN.class, 0x67, idSet);
		addPacketOpcode(SM_UPDATE_NOTE.class, 0x68, idSet);
		addPacketOpcode(SM_PLAY_MOVIE.class, 0x69, idSet); // 5.0
		addPacketOpcode(SM_LEGION_INFO.class, 0x6E, idSet);
		addPacketOpcode(SM_LEGION_ADD_MEMBER.class, 0x6F, idSet);
		addPacketOpcode(SM_LEGION_LEAVE_MEMBER.class, 0x70, idSet);
		addPacketOpcode(SM_LEGION_UPDATE_MEMBER.class, 0x71, idSet);
		addPacketOpcode(SM_LEGION_UPDATE_TITLE.class, 0x72, idSet);
		addPacketOpcode(SM_HOUSE_PAY_RENT.class, 0x108, idSet); // 5.6
		addPacketOpcode(SM_HOUSE_REGISTRY.class, 0x74, idSet);
		addPacketOpcode(SM_LEGION_UPDATE_SELF_INTRO.class, 0x77, idSet);
		addPacketOpcode(SM_INSTANCE_SCORE.class, 0x79, idSet);
		// addPacketOpcode(SM_RIFT_STATUS.class, 0x8C, idSet);
		addPacketOpcode(SM_AUTO_GROUP.class, 0x7A, idSet);
		addPacketOpcode(SM_QUEST_COMPLETED_LIST.class, 0x7B, idSet);
		addPacketOpcode(SM_QUEST_ACTION.class, 0x7C, idSet);
		addPacketOpcode(SM_NEARBY_QUESTS.class, 0x7F, idSet);
		addPacketOpcode(SM_PING_RESPONSE.class, 0x80, idSet);
		addPacketOpcode(SM_CUBE_UPDATE.class, 0x82, idSet);
		addPacketOpcode(SM_HOUSE_SCRIPTS.class, 0x83, idSet);
		addPacketOpcode(SM_FRIEND_LIST.class, 0x84, idSet);
		addPacketOpcode(SM_PRIVATE_STORE.class, 0x86, idSet);
		addPacketOpcode(SM_GROUP_LOOT.class, 0x87, idSet);
		addPacketOpcode(SM_ABYSS_RANK_UPDATE.class, 0x88, idSet);
		addPacketOpcode(SM_MAY_LOGIN_INTO_GAME.class, 0x89, idSet);
		addPacketOpcode(SM_ABYSS_RANKING_PLAYERS.class, 0x8A, idSet);
		addPacketOpcode(SM_ABYSS_RANKING_LEGIONS.class, 0x8B, idSet);
		addPacketOpcode(SM_INSTANCE_INFO.class, 0x8D, idSet);
		addPacketOpcode(SM_PONG.class, 0x8E, idSet);
		addPacketOpcode(SM_KISK_UPDATE.class, 0x90, idSet);
		addPacketOpcode(SM_PRIVATE_STORE_NAME.class, 0x91, idSet);
		addPacketOpcode(SM_BROKER_SERVICE.class, 0x92, idSet);
		addPacketOpcode(SM_MOTION.class, 0x94, idSet);
		addPacketOpcode(SM_TRADE_IN_LIST.class, 0x97, idSet);
		// addPacketOpcode(SM_BROKER_REGISTRATION_SERVICE.class, 0x93, idSet);
		addPacketOpcode(SM_SUMMON_OWNER_REMOVE.class, 0x9A, idSet);
		addPacketOpcode(SM_SUMMON_PANEL.class, 0x99, idSet);
		addPacketOpcode(SM_SUMMON_UPDATE.class, 0x9B, idSet);
		addPacketOpcode(SM_TRANSFORM_IN_SUMMON.class, 0x9C, idSet);
		addPacketOpcode(SM_LEGION_MEMBERLIST.class, 0x9D, idSet);
		addPacketOpcode(SM_LEGION_EDIT.class, 0x9E, idSet);
		addPacketOpcode(SM_TOLL_INFO.class, 0x9F, idSet); // ingameshop
		addPacketOpcode(SM_MAIL_SERVICE.class, 0xA1, idSet);
		addPacketOpcode(SM_SUMMON_USESKILL.class, 0xA2, idSet);
		addPacketOpcode(SM_WINDSTREAM.class, 0xA3, idSet);
		addPacketOpcode(SM_WINDSTREAM_ANNOUNCE.class, 0xA4, idSet);
		addPacketOpcode(SM_FIND_GROUP.class, 0xA6, idSet);
		addPacketOpcode(SM_REPURCHASE.class, 0xA7, idSet);
		addPacketOpcode(SM_WAREHOUSE_INFO.class, 0xA8, idSet);
		addPacketOpcode(SM_WAREHOUSE_ADD_ITEM.class, 0xA9, idSet);
		addPacketOpcode(SM_DELETE_WAREHOUSE_ITEM.class, 0xAA, idSet);
		addPacketOpcode(SM_WAREHOUSE_UPDATE_ITEM.class, 0xAB, idSet);
		addPacketOpcode(SM_TITLE_INFO.class, 0xB0, idSet);
		addPacketOpcode(SM_CHARACTER_SELECT.class, 0xB1, idSet);
		// addPacketOpcode(SM_BROKER_REGISTERED_LIST.class, 0xB1, idSet);
		addPacketOpcode(SM_CRAFT_ANIMATION.class, 0xB4, idSet);
		addPacketOpcode(SM_CRAFT_UPDATE.class, 0xB5, idSet);
		addPacketOpcode(SM_ASCENSION_MORPH.class, 0xB6, idSet);
		addPacketOpcode(SM_ITEM_USAGE_ANIMATION.class, 0xB7, idSet);
		addPacketOpcode(SM_CUSTOM_SETTINGS.class, 0xB8, idSet);
		addPacketOpcode(SM_DUEL.class, 0xB9, idSet);
		addPacketOpcode(SM_PET_EMOTE.class, 0xBB, idSet);
		addPacketOpcode(SM_QUESTIONNAIRE.class, 0xBF, idSet);
		addPacketOpcode(SM_DIE.class, 0xC1, idSet);
		addPacketOpcode(SM_RESURRECT.class, 0xC2, idSet);
		addPacketOpcode(SM_FORCED_MOVE.class, 0xC3, idSet);
		addPacketOpcode(SM_TELEPORT_MAP.class, 0xC4, idSet);
		addPacketOpcode(SM_USE_OBJECT.class, 0xC5, idSet);
		addPacketOpcode(SM_L2AUTH_LOGIN_CHECK.class, 0xC7, idSet);
		addPacketOpcode(SM_CHARACTER_LIST.class, 0xC8, idSet);
		addPacketOpcode(SM_CREATE_CHARACTER.class, 0xC9, idSet);
		addPacketOpcode(SM_DELETE_CHARACTER.class, 0xCA, idSet);
		addPacketOpcode(SM_RESTORE_CHARACTER.class, 0xCB, idSet);
		addPacketOpcode(SM_TARGET_IMMOBILIZE.class, 0xCC, idSet);
		addPacketOpcode(SM_LOOT_STATUS.class, 0xCD, idSet);
		addPacketOpcode(SM_LOOT_ITEMLIST.class, 0xCE, idSet);
		addPacketOpcode(SM_RECIPE_LIST.class, 0xCF, idSet);
		addPacketOpcode(SM_MANTRA_EFFECT.class, 0xD0, idSet);
		addPacketOpcode(SM_SIEGE_LOCATION_INFO.class, 0xD1, idSet);
		addPacketOpcode(SM_SIEGE_LOCATION_STATE.class, 0xD2, idSet);
		addPacketOpcode(SM_PLAYER_SEARCH.class, 0xD3, idSet);
		addPacketOpcode(SM_PLAYER_LEGION_JOIN_REQUEST_INFO.class, 0x137, idSet); // 5.6
		addPacketOpcode(SM_LEGION_JOIN_REQUEST_LIST.class, 0x138, idSet); // 5.6
		//addPacketOpcode(SM_LEGION_JOIN_REQUEST_FROM_PLAYER.class, 0x138, idSet);
		addPacketOpcode(SM_LEGION_ANSWER_JOIN_REQUEST.class, 0x141, idSet);
		addPacketOpcode(SM_STONESPEAR_SIEGE.class, 0x130, idSet); // 5.6 TODO
		//addPacketOpcode(SM_LEGION_JOIN_SETTING.class, 0x13E, idSet); // TODO
		addPacketOpcode(SM_LEGION_SEARCH.class, 0x135, idSet); // 5.6
		addPacketOpcode(SM_LEGION_SEND_EMBLEM.class, 0xD5, idSet);
		addPacketOpcode(SM_LEGION_SEND_EMBLEM_DATA.class, 0xD6, idSet);
		addPacketOpcode(SM_LEGION_UPDATE_EMBLEM.class, 0xD7, idSet);
		addPacketOpcode(SM_TERRITORY_LIST.class, 0x131, idSet); // 5.6
		addPacketOpcode(SM_REGION_INFO.class, 0xD8, idSet);
		addPacketOpcode(SM_PLAYER_REGION.class, 0xD9, idSet);
		addPacketOpcode(SM_SHIELD_EFFECT.class, 0xDA, idSet);
		//addPacketOpcode(SM_FRIEND_RESPONSE.class, 0xDB, idSet); // TODO Temp added DB
		addPacketOpcode(SM_TELEPORT_BACK.class, 0xDE, idSet);
		addPacketOpcode(SM_BLOCK_RESPONSE.class, 0xDF, idSet);
		addPacketOpcode(SM_BLOCK_LIST.class, 0xE1, idSet);
		addPacketOpcode(SM_FRIEND_NOTIFY.class, 0xE2, idSet);
		addPacketOpcode(SM_TOWNS_LIST.class, 0xE3, idSet);
		addPacketOpcode(SM_FRIEND_STATUS.class, 0xE4, idSet);
		addPacketOpcode(SM_CHANNEL_INFO.class, 0xE6, idSet);
		addPacketOpcode(SM_CHAT_INIT.class, 0xE7, idSet);
		addPacketOpcode(SM_MACRO_LIST.class, 0xE8, idSet);
		addPacketOpcode(SM_MACRO_RESULT.class, 0xE9, idSet);
		addPacketOpcode(SM_NICKNAME_CHECK_RESPONSE.class, 0xEA, idSet);
		addPacketOpcode(SM_BIND_POINT_INFO.class, 0xEC, idSet);
		addPacketOpcode(SM_RIFT_ANNOUNCE.class, 0xED, idSet);
		addPacketOpcode(SM_ABYSS_RANK.class, 0xEF, idSet); // 5.6
		addPacketOpcode(SM_PETITION.class, 0x170, idSet); // 5.6 TODO
		addPacketOpcode(SM_FRIEND_UPDATE.class, 0xF2, idSet); // 5.6
		addPacketOpcode(SM_LEARN_RECIPE.class, 0xF3, idSet); // 5.6
		addPacketOpcode(SM_RECIPE_DELETE.class, 0xF4, idSet); // 5.6
		addPacketOpcode(SM_FLY_TIME.class, 0xF6, idSet); // 5.6
		addPacketOpcode(SM_ALLIANCE_INFO.class, 0xF7, idSet); // 5.6
		addPacketOpcode(SM_ALLIANCE_MEMBER_INFO.class, 0xF8, idSet); // 5.6
		addPacketOpcode(SM_LEAVE_GROUP_MEMBER.class, 0xF9, idSet); // 5.6
		addPacketOpcode(SM_SHOW_BRAND.class, 0xFB, idSet); // 5.6
		addPacketOpcode(SM_ALLIANCE_READY_CHECK.class, 0xFC, idSet); // 5.6
		addPacketOpcode(SM_PRICES.class, 0xFE, idSet); // 5.6
		addPacketOpcode(SM_TRADELIST.class, 0xFF, idSet); // 5.6
		addPacketOpcode(SM_RECONNECT_KEY.class, 0x101, idSet); // 5.6
		addPacketOpcode(SM_INSTANCE_STAGE_INFO.class, 0x8C, idSet);
		addPacketOpcode(SM_HOUSE_BIDS.class, 0x102, idSet); // 5.6
		addPacketOpcode(SM_RECEIVE_BIDS.class, 0x104, idSet); // 5.6
		addPacketOpcode(SM_HOUSE_OWNER_INFO.class, 0x109, idSet); // 5.6
		addPacketOpcode(SM_OBJECT_USE_UPDATE.class, 0x10A, idSet); // 5.6
		addPacketOpcode(SM_PACKAGE_INFO_NOTIFY.class, 0x10C, idSet); // 5.6
		addPacketOpcode(SM_HOUSE_OBJECT.class, 0x10E, idSet); // 5.6
		addPacketOpcode(SM_DELETE_HOUSE_OBJECT.class, 0x10F, idSet); // 5.6
		addPacketOpcode(SM_HOUSE_OBJECTS.class, 0x110, idSet); // 5.6
		addPacketOpcode(SM_HOUSE_RENDER.class, 0x111, idSet); // 5.6
		addPacketOpcode(SM_HOUSE_UPDATE.class, 0x3D, idSet);
		addPacketOpcode(SM_DELETE_HOUSE.class, 0x112, idSet); // 5.6
		addPacketOpcode(SM_HOUSE_ACQUIRE.class, 0x115, idSet); // 5.6
		addPacketOpcode(SM_GROUP_DATA_EXCHANGE.class, 0xB2, idSet);
		addPacketOpcode(SM_INSTANCE_COUNT_INFO.class, 0x93, idSet);
		addPacketOpcode(SM_MARK_FRIENDLIST.class, 0x119, idSet); // 5.6
		addPacketOpcode(SM_DISPUTE_LAND.class, 0x11D, idSet); // 5.6
		addPacketOpcode(SM_HOUSE_TELEPORT.class, 0xDD, idSet);
		addPacketOpcode(SM_CHALLENGE_LIST.class, 0x11A, idSet); // 5.6
		addPacketOpcode(SM_ACCOUNT_ACCESS_PROPERTIES.class, 0xF0, idSet); // 5.6
		addPacketOpcode(SM_ICON_INFO.class, 0xAF, idSet);
		addPacketOpcode(SM_MEGAPHONE.class, 0x123, idSet);
		addPacketOpcode(SM_RIDE_ROBOT.class, 0x5C, idSet);
		addPacketOpcode(SM_EMOTION_SWITCH.class, 0xC6, idSet);
		addPacketOpcode(SM_SECURITY_TOKEN.class, 0x114, idSet); // 5.6
		addPacketOpcode(SM_SELECT_ITEM_ADD.class, 0x120, idSet); // 5.6
		addPacketOpcode(SM_SELECT_ITEM_LIST.class, 0x11E, idSet); // 5.6
		addPacketOpcode(SM_FATIGUE.class, 0xB3, idSet);
		addPacketOpcode(SM_HOTSPOT_TELEPORT.class, 0x12A, idSet); // 5.6
		addPacketOpcode(SM_UPGRADE_ARCADE.class, 0x12C, idSet); // 5.6
		addPacketOpcode(SM_ATREIAN_PASSPORT.class, 0x12D, idSet); // 5.6
		addPacketOpcode(SM_GAMEGUARD.class, 0x128, idSet); // 5.6
		addPacketOpcode(SM_CUSTOM_PACKET.class, 99999, idSet); // fake packet
		addPacketOpcode(SM_AFTER_TIME_CHECK.class, 0x126, idSet); // 5.6
		addPacketOpcode(SM_QUEST_REPEAT.class, 0x124, idSet); // 5.6
		//addPacketOpcode(SM_EVENT_BUFF.class, 0x11A, idSet); // TODO
		addPacketOpcode(SM_BOOST_EVENTS.class, 0x148, idSet); // 5.6 TODO
		addPacketOpcode(SM_SPLIT_ITEM.class, 0xEB, idSet); // 5.4 TODO
		addPacketOpcode(SM_TUNE_RESULT.class, 0x122, idSet); // 5.6 TODO
		addPacketOpcode(SM_LUCKY_DICE.class, 0x14B, idSet); // 5.6 TODO
		addPacketOpcode(SM_PLAYER_PROTECTION.class, 0x100, idSet); // 5.6
		addPacketOpcode(SM_FLAG_INFO.class, 0x152, idSet); // 5.6
		addPacketOpcode(SM_MAC_ADDRESS.class, 0x166, idSet); // 5.6 TODO
		addPacketOpcode(SM_CHECK_MAIL_SIZE.class, 0x95, idSet); // 5.4 TODO
		addPacketOpcode(SM_MINIONS.class, 0x16A, idSet); // 5.6 TODO
		addPacketOpcode(SM_EVENT_WINDOW.class, 0x13E, idSet); // 5.8 TODO
		addPacketOpcode(SM_EVENT_WINDOW_ITEMS.class, 0x154, idSet); // 5.8 TODO
		// --------------------ABYSS LANDING-------------------------//
		addPacketOpcode(SM_ABYSS_LANDING_LEVEL.class, 0x13C, idSet); // 5.6 TODO
		addPacketOpcode(SM_ABYSS_LANDING.class, 0x13D, idSet); // 5.6 TODO
		// ------------------FAST TRACK PACKETS----------------------//
		addPacketOpcode(SM_SERVER_IDS.class, 0x116, idSet); // 5.6
		addPacketOpcode(SM_FAST_TRACK.class, 0x96, idSet);
		addPacketOpcode(SM_FAST_TRACK_MOVE.class, 0x107, idSet); // 5.6
		// -------------CREATIVITY_POINTS PACKETS 5.0----------------//
		addPacketOpcode(SM_CREATIVITY_POINTS.class, 0x5D, idSet);
		addPacketOpcode(SM_CREATIVITY_POINTS_APPLY.class, 0x5E, idSet);
		// -----------------LUNA SHOP PACKETS 5.1--------------------//
		addPacketOpcode(SM_LUNA_SYSTEM_INFO.class, 0x149, idSet); // 5.6
		addPacketOpcode(SM_LUNA_SYSTEM.class, 0x14A, idSet); // 5.6
		// ------------------MAGIC MORPH + CRAFT---------------------//
		addPacketOpcode(SM_MAGIC_CRAFT_ANIMATION.class, 0x14D, idSet); // 5.6 TODO
		addPacketOpcode(SM_MAGIC_CRAFT.class, 0x14C, idSet); // 5.6 TODO
		addPacketOpcode(SM_MAGIC_MORPH_WINDOW.class, 0x15D, idSet); // 5.6 TODO
		addPacketOpcode(SM_MAGIC_MORPH_RESULT.class, 0x14E, idSet); // 5.6 TODO
		// ----------------------MONSTERBOOK-------------------------//
		addPacketOpcode(SM_MONSTERBOOK_LIST.class, 0x15B, idSet); // 5.6 TODO
		addPacketOpcode(SM_MONSTERBOOK.class, 0x15C, idSet); // 5.6 TODO
		addPacketOpcode(SM_UNK_135.class, 0x136, idSet); // TODO After SM_Monsterbook
		// -------------------EQUIPMENT SETTING----------------------//
		addPacketOpcode(SM_EQUIPMENT_SETTING.class, 0x165, idSet); // 5.6 TODO
		// -------------------BATTLEFIELD UNION----------------------//
		//addPacketOpcode(SM_BATTLEFIELD_UNION_REGISTER.class, 0x154, idSet); // 5.6 TODO
		addPacketOpcode(SM_BATTLEFIELD_UNION.class, 0x156, idSet); // 5.6 TODO
		addPacketOpcode(SM_BATTLEFIELD_UNION_POINTS.class, 0x157, idSet); // 5.6 TODO
		// --------------------UNKNOWN PACKETS-----------------------//
		addPacketOpcode(SM_UNK_60.class, 0x60, idSet);
		addPacketOpcode(SM_UNK_106.class, 0x106, idSet);
		addPacketOpcode(SM_UNK_FD.class, 0xFD, idSet);
		addPacketOpcode(SM_UNK_BD.class, 0xBD, idSet);
		addPacketOpcode(SM_UNK_A5.class, 0xA5, idSet);
		addPacketOpcode(SM_UNK_127.class, 0x127, idSet);
		addPacketOpcode(SM_UNK_98.class, 0x98, idSet); // TODO
		addPacketOpcode(SM_UNK_133.class, 0x133, idSet); // TODO
		addPacketOpcode(SM_UNK_7E.class, 0x7E, idSet); // TODO
		addPacketOpcode(SM_UNK_14F.class, 0x14F, idSet); // 5.6 TODO
		addPacketOpcode(SM_UNK_117.class, 0x118, idSet); // 5.6 TODO Kisk: Stigmameister
		addPacketOpcode(SM_UNK_13B.class, 0x13B, idSet); // 5.1 TODO
		addPacketOpcode(SM_UNK_158.class, 0x159, idSet); // 5.3 TODO SM_RANK_LIST
		addPacketOpcode(SM_MY_DOCUMENTATION.class, 0x15A, idSet); // 5.6 TODO
		addPacketOpcode(SM_UNK_12B.class, 0x12B, idSet); // 5.6 TODO
		addPacketOpcode(SM_UNK_168.class, 0x168, idSet); // 5.6 TODO
		addPacketOpcode(SM_SKILL_ANIMATION.class, 0x150, idSet); // 5.6 TODO
		addPacketOpcode(SM_UNK_11B.class, 0x11C, idSet); // 5.6 TODO
		addPacketOpcode(SM_UNK_15E.class, 0x15E, idSet); // 5.4 TODO
		addPacketOpcode(SM_UNK_15D.class, 0x162, idSet); // 5.6 TODO Lodas Silver Star
		addPacketOpcode(SM_NEVIWIND_CANYON.class, 0x169, idSet); // 5.6 TODO
		addPacketOpcode(SM_TOWER_OF_CHALLENGE.class, 0xEE, idSet); // 5.6
		addPacketOpcode(SM_GODSTONE_DESTROY.class, 0x12E, idSet); // 5.6
		// --------------------UNKNOWN PACKETS-----------------------//
	}

	static int getOpcode(Class<? extends AionServerPacket> packetClass) {
		Integer opcode = opcodes.get(packetClass);
		if (opcode == null)
			throw new IllegalArgumentException("There is no opcode for " + packetClass + " defined.");

		return opcode;
	}

	private static void addPacketOpcode(Class<? extends AionServerPacket> packetClass, int opcode, Set<Integer> idSet) {
		if (opcode < 0)
			return;

		if (idSet.contains(opcode))
			throw new IllegalArgumentException(String.format("There already exists another packet with id 0x%02X", opcode));

		idSet.add(opcode);
		opcodes.put(packetClass, opcode);
	}

}
