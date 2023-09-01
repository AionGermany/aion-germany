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
package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.siege.ArtifactLocation;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * Opens a yes/no question window on the client. Question based on the code given, defined in client_strings.xml
 *
 * @author Ben, avol, Lyahim
 */
public class SM_QUESTION_WINDOW extends AionServerPacket {

	public static final int STR_DUEL_DO_YOU_ACCEPT_REQUEST = 50028;
	public static final int STR_DUEL_DO_YOU_WITHDRAW_REQUEST = 50030;
	public static final int STR_PARTY_DO_YOU_ACCEPT_INVITATION = 60000;
	public static final int STR_PARTY_ALLIANCE_CHANGE_LOOT_TO_FREE_HE_ASKED = 70001;
	public static final int STR_PARTY_ALLIANCE_CHANGE_LOOT_TO_RANDOM_HE_ASKED = 70002;
	public static final int STR_PARTY_ALLIANCE_PICKUP_ITEM_HE_ASKED = 70003;
	public static final int STR_FORCE_DO_YOU_ACCEPT_INVITATION = 70004;
	public static final int STR_GUILD_CREATE_DO_YOU_ACCEPT_PAY = 80000;
	public static final int STR_GUILD_INVITE_I_JOINED_MSGBOX = 906024;
	public static final int STR_GUILD_TRANSFER_GUILDMASTER = 80005;
	public static final int STR_GUILD_DO_YOU_LEAVE = 80006;
	public static final int STR_GUILD_DO_YOU_BANISH = 80007;
	public static final int STR_GUILD_DISPERSE_STAYMODE = 80008;
	public static final int STR_GUILD_DISPERSE_STAYMODE_CANCEL = 80009;
	public static final int STR_GUILD_CHANGE_LEVEL_DO_YOU_ACCEPT_PAY = 80010;
	public static final int STR_GUILD_CHANGE_MASTER_DO_YOU_ACCEPT_OFFER = 80011;
	public static final int STR_BUY_SELL_CONFIRM_PURCHASE_EXCESSIVE_PRICE = 90000;
	public static final int STR_EXCHANGE_DO_YOU_ACCEPT_EXCHANGE = 90001;
	public static final int STR_QUEST_GIVEUP = 150000;
	public static final int STR_QUEST_GIVEUP_WHEN_DELETE_QUEST_ITEM = 150001;
	public static final int STR_ASK_RECOVER_EXPERIENCE = 160011;
	public static final int STR_ASK_RECOVER_EXPERIENCE2 = 1404454;
	public static final int STR_ASK_REGISTER_RESURRECT_POINT = 160012;
	public static final int STR_ASK_GROUP_GATE_DO_YOU_ACCEPT_MOVE = 160014;
	public static final int STR_ASK_USE_ARTIFACT = 160016;
	public static final int STR_ASK_PASS_BY_GATE = 160017;
	public static final int STR_ASK_REGISTER_BINDSTONE = 160018;
	public static final int STR_ASK_PASS_BY_DIRECT_PORTAL = 160019;
	public static final int STR_ASK_DOOR_REPAIR_DO_YOU_ACCEPT_REPAIR = 160021;
	public static final int STR_ASK_DOOR_REPAIR_POPUPDIALOG = 160027;
	public static final int STR_ASK_ARTIFACT_POPUPDIALOG = 160028;
	public static final int STR_ASK_JOIN_NEW_FACTION = 160033;
	public static final int STR_CONFIRM_LOOT = 900495;
	public static final int STR_WAREHOUSE_EXPAND_WARNING = 900686;
	public static final int STR_CRAFT_ADDSKILL_CONFIRM = 900852;
	public static final int STR_SUMMON_PARTY_DO_YOU_ACCEPT_REQUEST = 901721;
	public static final int STR_MSGBOX_UNION_INVITE_ME = 902249;
	public static final int STR_SOUL_BOUND_ITEM_DO_YOU_WANT_SOUL_BOUND = 95006;
	public static final int STR_ITEM_CHARGE_ALL_CONFIRM = 903026;
	public static final int STR_ITEM_CHARGE2_ALL_CONFIRM = 904039;
	public static final int STR_ITEM_CHARGE_CONFIRM_SOME_ALREADY_CHARGED = 903028;
	public static final int STR_ASSEMBLY_ITEM_POPUP_CONFIRM = 903441;
	public static final int STR_ASK_REMOVE_BINDSTONE = 904901;
	public static final int STR_INSTANCE_DUNGEON_WITH_DIFFICULTY_ENTER_CONFIRM = 902050;
	public static final int STR_INSTANCE_DUNGEON_DIFFICULTY_NORMAL = 902051;
	public static final int STR_INSTANCE_DUNGEON_DIFFICULTY_HARD = 902052;

	// DIVERS
	public static final int STR_MSGBOX_BUY_RANKITEM_WITH_RANKDOWN_CONFIRM = 904006;
	public static final int STR_VIP_LOBBY_NOTICE_CASE12_POPUP_01 = 906080;
	public static final int STR_MSGBOX_AKS_ENTER_PK_SERVER = 902812;
	public static final int STR_MSGBOX_DISASSEMBLEBOX_NOTICE = 904307;
	public static final int STR_MSGBOX_FORCE_INVITE_PARTY = 901256;
	public static final int STR_ENCHANT_ITEM_DO_YOU_WANT_ENCHANT_ITEM = 95001;
	public static final int STR_GIVE_ITEM_OPTION_DO_YOU_WANT_GIVE_OPTION_WITH_WARNING = 95003;
	public static final int STR_MSGBOX_CANT_REOBTAIN_QUEST_OVER_MAX_LEVEL = 903065;

	// INSTANCES
	public static final int STR_INSTANT_DUNGEON_RESURRECT = 901874;
	public static final int STR_INSTANT_DUNGEON_IDLF1_RESURRECT = 901891;
	public static final int STR_IDARENA_RESURRECT = 903241;
	public static final int STR_IDARENA_PVP_RESURRECT = 903487;
	public static final int STR_INSTANT_DUNGEON_RESURRECT_RESURRECT_POINT = 904731;
	public static final int STR_INDUN_COOLTIME_RESET_CONFIRM_MONEY = 906115;
	public static final int STR_INDUN_COOLTIME_RESET_CONFIRM_MONEY_2 = 906151;
	public static final int STR_INDUN_COOLTIME_RESET_CONFIRM_ITEM = 906153;
	public static final int STR_INDUN_COOLTIME_RESET_CONFIRM_ITEM_2 = 906154;
	public static final int STR_INDUN_COOLTIME_RESET_CONFIRM_ITEM_3 = 906535;
	public static final int STR_INDUN_COOLTIME_RESET_SELECT_1 = 906171;
	public static final int STR_INDUN_COOLTIME_RESET_SELECT_2 = 906172;

	// HOUSING 3.0
	public static final int STR_HOUSING_TELEPORT_HOME_CONFIRM = 903533;
	public static final int STR_HOUSING_TELEPORT_BUDDY_CONFIRM = 903534;
	public static final int STR_HOUSING_TELEPORT_RANDOM_CONFIRM = 903535;
	public static final int STR_HOUSING_TELEPORT_GUILD_CONFIRM = 903536;
	public static final int STR_BUDDYLIST_ADD_BUDDY_REQUEST = 1300911;
	public static final int STR_EXCHANGE_HE_REJECTED_EXCHANGE = 1300354;

	// DIMENSIONAL RIFT 3.5
	public static final int STR_ASK_PASS_BY_INVADE_DIRECT_PORTAL = 904304;
	public static final int STR_CONFIRM_INVADE_DIRECT_PORTAL_OUT = 904305;
	public static final int STR_ASK_INVADE_DIRECT_PORTAL_DEFENSE_FORCE = 904306;
	public static final int STR_CONFIRM_INVADE_DIRECT_PORTAL_DEFENSE_FORCE_OUT = 904391;
	public static final int STR_ASK_INVADE_DIRECT_PORTAL_DEFENSE_FORCE_DARK = 904393;
	public static final int STR_INVADE_DIRECT_POTAL_RESURRECT = 904404;
	public static final int STR_ASK_PASS_BY_RVR_DIRECT_PORTAL = 906458;

	// EMERGENCY_ESCAPE 3.7
	public static final int STR_CMD_EMERGENCY_ESCAPE = 904653;
	public static final int STR_POPUP_EMERGENCY_ESCAPE = 904643;

	// LIVE PARTY CONCERT ALL 4.3
	public static final int STR_ASK_PASS_BY_EVENT_DIRECT_PORTAL = 904837;

	// PANESTERRA 4.7
	public static final int STR_ASK_PASS_BY_SVS_DIRECT_PORTAL = 905067;
	public static final int STR_CONFIRM_SVS_DIRECT_PORTAL_OUT = 905068;
	public static final int STR_MSG_SVS_DIRECT_PORTAL_OPEN_NOTICE = 1402418;

	// UPGRADE ARCADE 4.7
	public static final int STR_POPUP_GACHA_FEVER_TIME_CHECK = 905394;
	public static final int STR_POPUP_GACHA_USER_WEB_SHOP_GO = 906302;

	// VOLATILE/CHAOS RIFT 4.8
	public static final int STR_ASK_PASS_BY_CHAOS_DIRECT_PORTAL = 905959;
	public static final int STR_ASK_PASS_BY_LEGION_DIRECT_PORTAL = 905960;

	// CUBE 4.9
	public static final int STR_ASK_EXTEND_INVENTORY_CONFIRM_2 = 906178;
	public static final int STR_ASK_EXTEND_INVENTORY_CONFIRM_3 = 906179;

	/**
	 * You have silenced %0. Abusing this function can lead to penalties.
	 */
	public static final int STR_MSG_REPORT_CHAT_CONFIRM = 905083;

	/**
	 * You cannot silence characters of the opposing faction.
	 */
	public static final int STR_MSG_REPORT_CHAT_CANT_OTHER_RACE = 905084;

	/**
	 * You cannot silence the same person more than once.
	 */
	public static final int STR_MSG_REPORT_CHAT_CANT_NO_USER = 905085;

	/**
	 * This character is currently not logged in or does not exist.
	 */
	public static final int STR_MSG_REPORT_CHAT_CANT_NO_ENTER = 905086;

	/**
	 * You must be Level 10 or above to use the Silence function.
	 */
	public static final int STR_MSG_REPORT_CHAT_CANT_LOW_LEVEL = 905087;

	/**
	 * You cannot send mail as you are under probation for spamming.
	 */
	public static final int STR_MSG_CANT_SEND_CHATRESTRICT_MAILS = 905088;

	/**
	 * You cannot open a private store as you are under probation for spamming.
	 */
	public static final int STR_PERSONAL_SHOP_DISABLED_CHATRESTRICT_MODE = 905089;

	public static final int STR_ASK_ROUND_RETURN_ITEM_DO_YOU_ACCEPT_MOVE = 907535;

	public static final int STR_ASK_ROUND_RETURN_ITEM_ACCEPT_MOVE_DONT_RETURN = 907536;

	public static final int STR_HOTSPOT_CONFIRM_NO_COST = 905097;

    public static final int STR_INFINITY_INDUN_RESURRECT = 913809;

	private int code;
	private int senderId;
	private int range;
	private Object[] params;
	private ArtifactLocation artifact;

	/**
	 * Creates a new <tt>SM_QUESTION_WINDOW<tt> packet
	 *
	 * @param code
	 *            code The string code to display, found in client_strings.xml
	 * @param senderId
	 *            sender Object id
	 * @param params
	 *            params The parameters for the string, if any
	 */
	public SM_QUESTION_WINDOW(int code, int senderId, int range, Object... params) {
		this.code = code;
		this.senderId = senderId;
		this.range = range;
		this.params = params;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con) {
		writeD(code);

		if (code == STR_INSTANCE_DUNGEON_WITH_DIFFICULTY_ENTER_CONFIRM) {
			writeH(0x33);
			writeH(0x30);
			writeH(0x30);
			writeH(0x31);
			writeH(0x37);
			writeH(0x30);
			writeH(0x30);
			writeH(0x30);
			writeH(0x30);
			writeH(0x00);
		}

		for (Object param : params) {
			if (param instanceof DescriptionId) {
				writeH(0x24);
				writeD(((DescriptionId) param).getValue());
			}
			else if (param instanceof ArtifactLocation) {
				this.artifact = (ArtifactLocation) param;
			}
			else {
				writeS(String.valueOf(param));
			}
		}

		// Guardian Stone Activation Window
		if (code == STR_ASK_DOOR_REPAIR_POPUPDIALOG) {
			writeD(0x00);
			writeD(0x00);
			writeD(0x00);
			writeH(0x00);
			writeC(0x01);
			writeD(senderId);
			writeD(0x05);
		} // ArtifactLocation Activation Window
		else if (code == 160028) {
			writeD(0x00);
			writeD(0x00);
			writeH(0x00);
			writeC(0x00);
			writeD(0x00);
			if (artifact == null) {
				writeD(0x00);
			}
			else {
				writeD(artifact.getCoolDown());// ArtifactLocation reuse
			}
		}
		else if (code == STR_BUDDYLIST_ADD_BUDDY_REQUEST) {
			writeB(new byte[17]);
		}
		else if (code == STR_INSTANCE_DUNGEON_WITH_DIFFICULTY_ENTER_CONFIRM) {
			writeD(0x00);
			writeH(0x00);
			writeC(0x01);
			writeD(senderId);
			writeD(0x05);
		}
		else if (code == STR_ASK_RECOVER_EXPERIENCE2) {
			System.out.println("RECOVER 2");
			writeD(0x00);// unk
			writeD(0x00);// unk
			writeC(range > 0 ? 0x01 : 0x00);// unk maybe boolean for rangecheck?
			writeD(senderId);
			writeD(range);// range within the Question is valid
		}
		else {
			writeD(0x00);// unk
			writeD(0x00);// unk
			writeH(0x00);// unk
			writeC(range > 0 ? 0x01 : 0x00);// unk maybe boolean for rangecheck?
			writeD(senderId);
			writeD(range);// range within the Question is valid
		}
	}
}
