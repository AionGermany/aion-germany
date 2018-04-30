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


package com.aionemu.chatserver.model.channel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.LoggerFactory;

import org.slf4j.Logger;

import com.aionemu.chatserver.configs.Config;
import com.aionemu.chatserver.model.Gender;
import com.aionemu.chatserver.model.PlayerClass;
import com.aionemu.chatserver.model.Race;
import com.aionemu.chatserver.service.GameServerService;

/**
 * @author ATracer
 */
public class ChatChannels {

    private static final Logger log = LoggerFactory.getLogger(ChatChannels.class);
    private static final List<Channel> channels = new ArrayList<Channel>();

    static {
        // LFG channels
        addGroupChannel("partyFind_PF");
        // TRADE
        addTradeChannel("trade_LC1");
        addTradeChannel("trade_LC2");
        addTradeChannel("trade_DC1");
        addTradeChannel("trade_DC2");
        addTradeChannel("trade_Arena_L_Lobby");
		addTradeChannel("trade_Arena_L_CLobby");
        addTradeChannel("trade_Arena_D_Lobby");
        addTradeChannel("trade_LF1");
        addTradeChannel("trade_LF2");
        addTradeChannel("trade_LF1A");
        addTradeChannel("trade_LF3");
        addTradeChannel("trade_LF4");
		addTradeChannel("trade_LF5");
		addTradeChannel("trade_LF5_Ship");
        addTradeChannel("trade_LF2A");
        addTradeChannel("trade_DF1");
        addTradeChannel("trade_DF2");
        addTradeChannel("trade_DF1A");
        addTradeChannel("trade_DF3");
        addTradeChannel("trade_DF2A");
        addTradeChannel("trade_DF4");
		addTradeChannel("trade_DF5");
		addTradeChannel("trade_DF5_Ship");
		addTradeChannel("trade_LDF5_Under_L");
		addTradeChannel("trade_LDF5_Under_D");
        addTradeChannel("trade_IDAbPro");
        addTradeChannel("trade_IDTest_Dungeon");
        addTradeChannel("trade_IDAb1_MiniCastle");
        addTradeChannel("trade_IDLF1");
        addTradeChannel("trade_IDAbRe_Up_Asteria");
        addTradeChannel("trade_IDAbRe_Low_Divine");
        addTradeChannel("trade_IDAbRe_Up_Rhoo");
        addTradeChannel("trade_IDAbRe_Low_Wciel");
        addTradeChannel("trade_IDAbRe_Low_Eciel");
        addTradeChannel("trade_IDshulackShip");
        addTradeChannel("trade_IDAb1_Dreadgion");
        addTradeChannel("trade_IDAbRe_Up3_Dkisas");
        addTradeChannel("trade_IDAbRe_Up3_Lamiren");
        addTradeChannel("trade_IDAbRe_Up3_Crotan");
        addTradeChannel("trade_IDTemple_Up");
        addTradeChannel("trade_IDTemple_Low");
        addTradeChannel("trade_IDCatacombs");
        addTradeChannel("trade_IDElim");
        addTradeChannel("trade_IDNovice");
        addTradeChannel("trade_IDDreadgion_02");
        addTradeChannel("trade_IDAbRe_Core");
        addTradeChannel("trade_IDCromede");
        addTradeChannel("trade_IDStation");
        addTradeChannel("trade_IDF4Re_Drana");
        addTradeChannel("trade_IDElemental_1");
        addTradeChannel("trade_IDElemental_2");
        addTradeChannel("trade_IDYun");
        addTradeChannel("trade_Test_MRT_IDZone");
        addTradeChannel("trade_IDArena");
        addTradeChannel("trade_IDRaksha");
        addTradeChannel("trade_IDArena_Solo");
        addTradeChannel("trade_IDLDF4A");
        addTradeChannel("trade_IDArena_pvp01");
        addTradeChannel("trade_IDArena_pvp02");
        addTradeChannel("trade_IDLDF4A_Raid");
        addTradeChannel("trade_IDLDF4A_Lehpar");
        addTradeChannel("trade_IDLDF4b_Tiamat");
        addTradeChannel("trade_IDLDF4a_Intro");
		addTradeChannel("trade_IDLDF4Re_01");
        addTradeChannel("trade_IDArena_pvp01_T");
        addTradeChannel("trade_IDArena_pvp02_T");
        addTradeChannel("trade_IDDreadgion_03");
        addTradeChannel("trade_IDArena_Team01");
        addTradeChannel("trade_IDshulackShip_Solo");
        addTradeChannel("trade_IDTiamat_Reward");
        addTradeChannel("trade_IDLDF5RE_Solo");
        addTradeChannel("trade_IDTiamat_Solo");
        addTradeChannel("trade_IDTiamat_Israphel");
        addTradeChannel("trade_IDTiamat_1");
        addTradeChannel("trade_IDTiamat_2");
        addTradeChannel("trade_IDLDF5Re_02");
        addTradeChannel("trade_IDLDF5b_TD");
        addTradeChannel("trade_IDArena_Glory");
        addTradeChannel("trade_IDDF2Flying_Event01");
        addTradeChannel("trade_IDArena_Team01_T");
        addTradeChannel("trade_IDLDF5Re_03");
        addTradeChannel("trade_IDLDF5_Under_01");
        addTradeChannel("trade_IDAbRe_Core_02");
        addTradeChannel("trade_IDUnderpassRe");
        addTradeChannel("trade_IDRuneweapon");
        addTradeChannel("trade_IDRuneweapon_Q");
        addTradeChannel("trade_IDLDF5RE_solo_Q");
        addTradeChannel("trade_IDShulack_Rose_Solo_01");
        addTradeChannel("trade_IDShulack_Rose_Solo_02");
        addTradeChannel("trade_IDShulack_Rose_01");
        addTradeChannel("trade_IDShulack_Rose_02");
        addTradeChannel("trade_IDShulack_Rose_03");
        addTradeChannel("trade_IDArena_Team01_T2");
        addTradeChannel("trade_IDLDF5_Under_Rune");
        addTradeChannel("trade_IDKamar");
        addTradeChannel("trade_IDVritra_Base");
        addTradeChannel("trade_IDLDF5_Under_02");
        addTradeChannel("trade_IDAsteria_IU_Solo");
        addTradeChannel("trade_IDAsteria_IU_Party");
        addTradeChannel("trade_IDLDF5Re_02_L");
        addTradeChannel("trade_IDLDF5Re_03_L");
        addTradeChannel("trade_IDLDF5RE_Solo_L");
        addTradeChannel("trade_IDAsteria_IU_World");
        addTradeChannel("trade_IDAbProL1");
        addTradeChannel("trade_IDAbProL2");
        addTradeChannel("trade_IDAbGateL1");
        addTradeChannel("trade_IDAbGateL2");
        addTradeChannel("trade_IDLF3Lp");
        addTradeChannel("trade_IDLF1B");
        addTradeChannel("trade_IDLF1B_Stigma");
        addTradeChannel("trade_IDLC1_arena");
        addTradeChannel("trade_IDLF3_Castle_indratoo");
        addTradeChannel("trade_IDLF3_Castle_Lehpar");
        addTradeChannel("trade_IDLF2a_Lab");
        addTradeChannel("trade_IDAbProL3");
        addTradeChannel("trade_IDAbProD1");
        addTradeChannel("trade_IDAbProD2");
        addTradeChannel("trade_IDAbGateD1");
        addTradeChannel("trade_IDAbGateD2");
        addTradeChannel("trade_IDDF2Flying");
        addTradeChannel("trade_IDDF1B");
        addTradeChannel("trade_IDSpace");
        addTradeChannel("trade_IDDF3_Dragon");
        addTradeChannel("trade_IDDC1_arena");
        addTradeChannel("trade_IDDF2_Dflame");
        addTradeChannel("trade_IDDF3_LP");
        addTradeChannel("trade_IDDC1_Arena_3F");
        addTradeChannel("trade_IDDf2a_Adma");
        addTradeChannel("trade_IDAbProD3");
        addTradeChannel("trade_IDDramata_01");
        addTradeChannel("trade_Ab1");
        addTradeChannel("trade_LF_Prison");
        addTradeChannel("trade_DF_Prison");
        addTradeChannel("trade_Underpass");
        addTradeChannel("trade_LDF4a");
        addTradeChannel("trade_LDF4b");
        addTradeChannel("trade_Tiamat_Down");
        addTradeChannel("trade_LDF5a");
        addTradeChannel("trade_LDF5b");
        addTradeChannel("trade_LDF5_Under");
        addTradeChannel("trade_IDIU");
		addTradeChannel("trade_LDF4_Advance");
		addTradeChannel("trade_LDF5_Fortress");
		addTradeChannel("trade_Gab1_01");
		addTradeChannel("trade_GAb1_Sub");
        addTradeChannel("trade_Housing_LF_personal");
        addTradeChannel("trade_Housing_LC_legion");
        addTradeChannel("trade_Housing_DF_personal");
        addTradeChannel("trade_Housing_DC_legion");
        addTradeChannel("trade_housing_idlf_personal");
        addTradeChannel("trade_housing_iddf_personal");
        addTradeChannel("trade_Test_Basic");
        addTradeChannel("trade_Test_Server");
        addTradeChannel("trade_Test_GiantMonster");
        addTradeChannel("trade_Housing_barrack");
        addTradeChannel("trade_Test_IDArena");
        addTradeChannel("trade_IDLDF5RE_test");
        addTradeChannel("trade_Test_Kgw");
        addTradeChannel("trade_Test_Basic_Mj");
        addTradeChannel("trade_test_intro");
        addTradeChannel("trade_IDElemental_2_E");
		addTradeChannel("trade_LF6");
		addTradeChannel("trade_DF6");
        // REGION
        addRegionChannel(110010000, "public_LC1");
        addRegionChannel(110020000, "public_LC2");
        addRegionChannel(120010000, "public_DC1");
        addRegionChannel(120020000, "public_DC2");
        addRegionChannel(110070000, "public_Arena_L_Lobby");
		addRegionChannel(130090000, "public_Arena_L_CLobby");
        addRegionChannel(120080000, "public_Arena_D_Lobby");
        addRegionChannel(210010000, "public_LF1");
        addRegionChannel(210020000, "public_LF2");
        addRegionChannel(210030000, "public_LF1A");
        addRegionChannel(210040000, "public_LF3");
        addRegionChannel(210050000, "public_LF4");
		addRegionChannel(210070000, "public_LF5");
		addRegionChannel(210080000, "public_LF5_Ship");
		addRegionChannel(210090000, "public_LDF5_Under_L");
        addRegionChannel(210060000, "public_LF2A");
        addRegionChannel(220010000, "public_DF1");
        addRegionChannel(220020000, "public_DF2");
        addRegionChannel(220030000, "public_DF1A");
        addRegionChannel(220040000, "public_DF3");
        addRegionChannel(220050000, "public_DF2A");
        addRegionChannel(220070000, "public_DF4");
		addRegionChannel(220080000, "public_DF5");
		addRegionChannel(220090000, "public_DF5_Ship");
		addRegionChannel(210090000, "public_LDF5_Under_D");
        addRegionChannel(300010000, "public_IDAbPro");
        addRegionChannel(300020000, "public_IDTest_Dungeon");
        addRegionChannel(300030000, "public_IDAb1_MiniCastle");
        addRegionChannel(300040000, "public_IDLF1");
        addRegionChannel(300050000, "public_IDAbRe_Up_Asteria");
        addRegionChannel(300060000, "public_IDAbRe_Low_Divine");
        addRegionChannel(300070000, "public_IDAbRe_Up_Rhoo");
        addRegionChannel(300080000, "public_IDAbRe_Low_Wciel");
        addRegionChannel(300090000, "public_IDAbRe_Low_Eciel");
        addRegionChannel(300100000, "public_IDshulackShip");
        addRegionChannel(300110000, "public_IDAb1_Dreadgion");
        addRegionChannel(300120000, "public_IDAbRe_Up3_Dkisas");
        addRegionChannel(300130000, "public_IDAbRe_Up3_Lamiren");
        addRegionChannel(300140000, "public_IDAbRe_Up3_Crotan");
        addRegionChannel(300150000, "public_IDTemple_Up");
        addRegionChannel(300160000, "public_IDTemple_Low");
        addRegionChannel(300170000, "public_IDCatacombs");
        addRegionChannel(300190000, "public_IDElim");
        addRegionChannel(300200000, "public_IDNovice");
        addRegionChannel(300210000, "public_IDDreadgion_02");
        addRegionChannel(300220000, "public_IDAbRe_Core");
        addRegionChannel(300230000, "public_IDCromede");
        addRegionChannel(300240000, "public_IDStation");
        addRegionChannel(300250000, "public_IDF4Re_Drana");
        addRegionChannel(300260000, "public_IDElemental_1");
        addRegionChannel(300270000, "public_IDElemental_2");
        addRegionChannel(300280000, "public_IDYun");
        addRegionChannel(300290000, "public_Test_MRT_IDZone");
        addRegionChannel(300300000, "public_IDArena");
        addRegionChannel(300310000, "public_IDRaksha");
        addRegionChannel(300320000, "public_IDArena_Solo");
        addRegionChannel(300330000, "public_IDLDF4A");
        addRegionChannel(300350000, "public_IDArena_pvp01");
        addRegionChannel(300360000, "public_IDArena_pvp02");
        addRegionChannel(300380000, "public_IDLDF4A_Raid");
        addRegionChannel(300390000, "public_IDLDF4A_Lehpar");
        addRegionChannel(300400000, "public_IDLDF4b_Tiamat");
        addRegionChannel(300410000, "public_IDLDF4a_Intro");
        addRegionChannel(300420000, "public_IDArena_pvp01_T");
        addRegionChannel(300430000, "public_IDArena_pvp02_T");
        addRegionChannel(300440000, "public_IDDreadgion_03");
        addRegionChannel(300450000, "public_IDArena_Team01");
        addRegionChannel(300460000, "public_IDshulackShip_Solo");
        addRegionChannel(300470000, "public_IDTiamat_Reward");
        addRegionChannel(300480000, "public_IDLDF5RE_Solo");
        addRegionChannel(300490000, "public_IDTiamat_Solo");
        addRegionChannel(300500000, "public_IDTiamat_Israphel");
        addRegionChannel(300510000, "public_IDTiamat_1");
        addRegionChannel(300520000, "public_IDTiamat_2");
        addRegionChannel(300530000, "public_IDLDF5Re_02");
        addRegionChannel(300540000, "public_IDLDF5b_TD");
        addRegionChannel(300550000, "public_IDArena_Glory");
        addRegionChannel(300560000, "public_IDDF2Flying_Event01");
        addRegionChannel(300570000, "public_IDArena_Team01_T");
        addRegionChannel(300580000, "public_IDLDF5Re_03");
        addRegionChannel(300590000, "public_IDLDF5_Under_01");
        addRegionChannel(300600000, "public_IDAbRe_Core_02");
        addRegionChannel(300700000, "public_IDUnderpassRe");
        addRegionChannel(300800000, "public_IDRuneweapon");
        addRegionChannel(300900000, "public_IDRuneweapon_Q");
        addRegionChannel(301000000, "public_IDLDF5RE_solo_Q");
        addRegionChannel(301010000, "public_IDShulack_Rose_Solo_01");
        addRegionChannel(301020000, "public_IDShulack_Rose_Solo_02");
        addRegionChannel(301030000, "public_IDShulack_Rose_01");
        addRegionChannel(301040000, "public_IDShulack_Rose_02");
        addRegionChannel(301050000, "public_IDShulack_Rose_03");
        addRegionChannel(301100000, "public_IDArena_Team01_T2");
        addRegionChannel(301110000, "public_IDLDF5_Under_Rune");
        addRegionChannel(301120000, "public_IDKamar");
        addRegionChannel(301130000, "public_IDVritra_Base");
        addRegionChannel(301140000, "public_IDLDF5_Under_02");
        addRegionChannel(301150000, "public_IDAsteria_IU_Solo");
        addRegionChannel(301160000, "public_IDAsteria_IU_Party");
        addRegionChannel(301170000, "public_IDLDF5Re_02_L");
        addRegionChannel(301180000, "public_IDLDF5Re_03_L");
        addRegionChannel(301190000, "public_IDLDF5RE_Solo_L");
        addRegionChannel(301200000, "public_IDAsteria_IU_World");
        addRegionChannel(301210000, "public_IDLDF5_Under_01_War");
        addRegionChannel(301220000, "public_IDF5_TD_War");
        addRegionChannel(301230000, "public_IDLDF5_Under_03");
        addRegionChannel(301240000, "public_IDAbRe_Up3_Dkisas_02");
        addRegionChannel(301250000, "public_IDAbRe_Up3_Lamiren_02");
        addRegionChannel(301260000, "public_IDAbRe_Up3_Crotan_02");
        addRegionChannel(301270000, "public_IDLDF4Re_01");
        addRegionChannel(301280000, "public_IDAbRe_Up3_Dkisas_02_N");
        addRegionChannel(301290000, "public_IDAbRe_Up3_Lamiren_02_N");
        addRegionChannel(301300000, "public_IDAbRe_Up3_Crotan");
        addRegionChannel(301310000, "public_IDLDF5_Fortress_Re");
        addRegionChannel(301320000, "public_IDLDF5_Under_01_PC");
        addRegionChannel(301330000, "public_IDLDF5_Under_Rune_PC");
        addRegionChannel(301510000, "public_IDElemental_2_E");
        addRegionChannel(310010000, "public_IDAbProL1");
        addRegionChannel(310020000, "public_IDAbProL2");
        addRegionChannel(310030000, "public_IDAbGateL1");
        addRegionChannel(310040000, "public_IDAbGateL2");
        addRegionChannel(310050000, "public_IDLF3Lp");
        addRegionChannel(310060000, "public_IDLF1B");
        addRegionChannel(310070000, "public_IDLF1B_Stigma");
        addRegionChannel(310080000, "public_IDLC1_arena");
        addRegionChannel(310090000, "public_IDLF3_Castle_indratoo");
        addRegionChannel(310100000, "public_IDLF3_Castle_Lehpar");
        addRegionChannel(310110000, "public_IDLF2a_Lab");
        addRegionChannel(310120000, "public_IDAbProL3");
        addRegionChannel(320010000, "public_IDAbProD1");
        addRegionChannel(320020000, "public_IDAbProD2");
        addRegionChannel(320030000, "public_IDAbGateD1");
        addRegionChannel(320040000, "public_IDAbGateD2");
        addRegionChannel(320050000, "public_IDDF2Flying");
        addRegionChannel(320060000, "public_IDDF1B");
        addRegionChannel(320070000, "public_IDSpace");
        addRegionChannel(320080000, "public_IDDF3_Dragon");
        addRegionChannel(320090000, "public_IDDC1_arena");
        addRegionChannel(320100000, "public_IDDF2_Dflame");
        addRegionChannel(320110000, "public_IDDF3_LP");
        addRegionChannel(320120000, "public_IDDC1_Arena_3F");
        addRegionChannel(320130000, "public_IDDf2a_Adma");
        addRegionChannel(320140000, "public_IDAbProD3");
        addRegionChannel(320150000, "public_IDDramata_01");
        addRegionChannel(400010000, "public_Ab1");
        addRegionChannel(400020000, "public_Gab1_01");
		addRegionChannel(400030000, "public_GAb1_Sub");
        addRegionChannel(510010000, "public_LF_Prison");
        addRegionChannel(520010000, "public_DF_Prison");
        addRegionChannel(600010000, "public_Underpass");
        addRegionChannel(600020000, "public_LDF4a");
        addRegionChannel(600030000, "public_LDF4b");
        addRegionChannel(600040000, "public_Tiamat_Down");
        addRegionChannel(600050000, "public_LDF5a");
        addRegionChannel(600060000, "public_LDF5b");
        addRegionChannel(600070000, "public_LDF5_Under");
        addRegionChannel(600080000, "public_IDIU");
		addRegionChannel(600100000, "public_LDF4_Advance");
		addRegionChannel(600100000, "public_LDF5_Fortress");
        addRegionChannel(700010000, "public_Housing_LF_personal");
        addRegionChannel(700020000, "public_Housing_LC_legion");
        addRegionChannel(710010000, "public_Housing_DF_personal");
        addRegionChannel(710020000, "public_Housing_DC_legion");
        addRegionChannel(720010000, "public_housing_idlf_personal");
        addRegionChannel(730010000, "public_housing_iddf_personal");
        addRegionChannel(900020000, "public_Test_Basic");
        addRegionChannel(900030000, "public_Test_Server");
        addRegionChannel(900100000, "public_Test_GiantMonster");
        addRegionChannel(900110000, "public_Housing_barrack");
        addRegionChannel(900120000, "public_Test_IDArena");
        addRegionChannel(900130000, "public_IDLDF5RE_test");
        addRegionChannel(900140000, "public_Test_Kgw");
        addRegionChannel(900150000, "public_Test_Basic_Mj");
        addRegionChannel(900170000, "public_test_intro");
		addRegionChannel(210100000, "public_LF6");
		addRegionChannel(220110000, "public_DF6");
        // LANG & JOB
        // TODO : All other lang
        if (Config.LANG_CHAT == 1) {
            // LANG
            addLangChannel("User_English");
            addLangChannel("User_French");
            addLangChannel("User_German");
            addLangChannel("User_Italian");
            addLangChannel("User_Spanish");
            addLangChannel("User_Danish");
            addLangChannel("User_Swedish");
            addLangChannel("User_Finnish");
            addLangChannel("User_Norwegian");
            addLangChannel("User_Greek");

            // JOB
            // Male
            addJobChannel(Gender.MALE, PlayerClass.GLADIATOR, "job_Gladiator");
            addJobChannel(Gender.MALE, PlayerClass.TEMPLAR, "job_Templar");
            addJobChannel(Gender.MALE, PlayerClass.SORCERER, "job_Sorcerer");
            addJobChannel(Gender.MALE, PlayerClass.SPIRIT_MASTER, "job_Spiritmaster");
            addJobChannel(Gender.MALE, PlayerClass.CHANTER, "job_Chanter");
            addJobChannel(Gender.MALE, PlayerClass.RANGER, "job_Ranger");
            addJobChannel(Gender.MALE, PlayerClass.ASSASSIN, "job_Assassin");
            addJobChannel(Gender.MALE, PlayerClass.CLERIC, "job_Cleric");
            addJobChannel(Gender.MALE, PlayerClass.ENGINEER, "job_engineer");
            addJobChannel(Gender.MALE, PlayerClass.ARTIST, "job_artist");
            addJobChannel(Gender.MALE, PlayerClass.GUNNER, "job_gunner");
            addJobChannel(Gender.MALE, PlayerClass.BARD, "job_bard");
            addJobChannel(Gender.MALE, PlayerClass.RIDER, "job_rider");
            // Female
            addJobChannel(Gender.FEMALE, PlayerClass.GLADIATOR, "job_Gladiator");
            addJobChannel(Gender.FEMALE, PlayerClass.TEMPLAR, "job_Templar");
            addJobChannel(Gender.FEMALE, PlayerClass.SORCERER, "job_Sorcerer");
            addJobChannel(Gender.FEMALE, PlayerClass.SPIRIT_MASTER, "job_Spiritmaster");
            addJobChannel(Gender.FEMALE, PlayerClass.CHANTER, "job_Chanter");
            addJobChannel(Gender.FEMALE, PlayerClass.RANGER, "job_Ranger");
            addJobChannel(Gender.FEMALE, PlayerClass.ASSASSIN, "job_Assassin");
            addJobChannel(Gender.FEMALE, PlayerClass.CLERIC, "job_Cleric");
            addJobChannel(Gender.FEMALE, PlayerClass.ENGINEER, "job_engineer");
            addJobChannel(Gender.FEMALE, PlayerClass.ARTIST, "job_artist");
            addJobChannel(Gender.FEMALE, PlayerClass.GUNNER, "job_gunner");
            addJobChannel(Gender.FEMALE, PlayerClass.BARD, "job_bard");
            addJobChannel(Gender.FEMALE, PlayerClass.RIDER, "job_rider");
        } else if (Config.LANG_CHAT == 2) {
            // LANG
            addLangChannel("User_Anglais");
            addLangChannel("User_Francais");
            addLangChannel("User_Allemand");
            addLangChannel("User_Italien");
            addLangChannel("User_Espagnol");
            addLangChannel("User_Danois");
            addLangChannel("User_Suedois");
            addLangChannel("User_Finnois");
            addLangChannel("User_Norvegien");
            addLangChannel("User_Grec");

            // JOB
            // Male
            addJobChannel(Gender.MALE, PlayerClass.GLADIATOR, "job_Gladiateur");
            addJobChannel(Gender.MALE, PlayerClass.TEMPLAR, "job_Templier");
            addJobChannel(Gender.MALE, PlayerClass.SORCERER, "job_Sorcier");
            addJobChannel(Gender.MALE, PlayerClass.SPIRIT_MASTER, "job_Spiritualiste");
            addJobChannel(Gender.MALE, PlayerClass.CHANTER, "job_Aede");
            addJobChannel(Gender.MALE, PlayerClass.RANGER, "job_RÃ´deur");
            addJobChannel(Gender.MALE, PlayerClass.ASSASSIN, "job_Assassin");
            addJobChannel(Gender.MALE, PlayerClass.CLERIC, "job_Clerc");
            addJobChannel(Gender.MALE, PlayerClass.ENGINEER, "job_engineer");
            addJobChannel(Gender.MALE, PlayerClass.ARTIST, "job_artist");
            addJobChannel(Gender.MALE, PlayerClass.GUNNER, "job_gunner");
            addJobChannel(Gender.MALE, PlayerClass.BARD, "job_bard");
            addJobChannel(Gender.MALE, PlayerClass.RIDER, "job_rider");
            // Female
            addJobChannel(Gender.FEMALE, PlayerClass.GLADIATOR, "job_Gladiateur[f:" + '"' + "Gladiateur" + '"' + "]");
            addJobChannel(Gender.FEMALE, PlayerClass.TEMPLAR, "job_Templier[f:" + '"' + "Templier" + '"' + "]");
            addJobChannel(Gender.FEMALE, PlayerClass.SORCERER, "job_Sorcier[f:" + '"' + "Sorcier" + '"' + "]");
            addJobChannel(Gender.FEMALE, PlayerClass.SPIRIT_MASTER, "job_Spiritualiste[f:" + '"' + "Spiritualiste" + '"' + "]");
            addJobChannel(Gender.FEMALE, PlayerClass.CHANTER, "job_Chanteur[f:" + '"' + "Chanteur" + '"' + "]");
            addJobChannel(Gender.FEMALE, PlayerClass.RANGER, "job_Ranger[f:" + '"' + "Ranger" + '"' + "]");
            addJobChannel(Gender.FEMALE, PlayerClass.ASSASSIN, "job_Assassin[f:" + '"' + "Assassine" + '"' + "]");
            addJobChannel(Gender.FEMALE, PlayerClass.CLERIC, "job_Clerc[f:" + '"' + "Clerc" + '"' + "]");
            addJobChannel(Gender.FEMALE, PlayerClass.ENGINEER, "job_engineer[f:" + '"' + "Ingenieur" + '"' + "]");
            addJobChannel(Gender.FEMALE, PlayerClass.ARTIST, "job_artist[f:" + '"' + "Artiste" + '"' + "]");
            addJobChannel(Gender.FEMALE, PlayerClass.GUNNER, "job_gunner[f:" + '"' + "Garde" + '"' + "]");
            addJobChannel(Gender.FEMALE, PlayerClass.BARD, "job_bard[f:" + '"' + "Barde" + '"' + "]");
            addJobChannel(Gender.FEMALE, PlayerClass.RIDER, "job_rider[f:" + '"' + "Mecha" + '"' + "]");
        } else if (Config.LANG_CHAT == 3) {
            // LANG
            addLangChannel("User_Englisch");
            addLangChannel("User_Französisch");
            addLangChannel("User_Deutsch");
            addLangChannel("User_Italienisch");
            addLangChannel("User_Spanisch");
            addLangChannel("User_Dänisch");
            addLangChannel("User_Schwedisch");
            addLangChannel("User_Finnisch");
            addLangChannel("User_Norwegisch");
            addLangChannel("User_Griechisch");

            // JOB
            // Male
            addJobChannel(Gender.MALE, PlayerClass.WARRIOR, "job_Krieger");
            addJobChannel(Gender.MALE, PlayerClass.GLADIATOR, "job_Gladiator");
            addJobChannel(Gender.MALE, PlayerClass.TEMPLAR, "job_Templer");
            addJobChannel(Gender.MALE, PlayerClass.MAGE, "job_Magier");
            addJobChannel(Gender.MALE, PlayerClass.SORCERER, "job_Zauberer");
            addJobChannel(Gender.MALE, PlayerClass.SPIRIT_MASTER, "job_Beschwoerer");
			addJobChannel(Gender.MALE, PlayerClass.PRIEST, "job_Priester");
            addJobChannel(Gender.MALE, PlayerClass.CHANTER, "job_Kantor");
            addJobChannel(Gender.MALE, PlayerClass.CLERIC, "job_Kleriker");
			addJobChannel(Gender.MALE, PlayerClass.SCOUT, "job_Spaeher");
			addJobChannel(Gender.MALE, PlayerClass.RANGER, "job_Jaeger");
            addJobChannel(Gender.MALE, PlayerClass.ASSASSIN, "job_Assassine");
            addJobChannel(Gender.MALE, PlayerClass.ENGINEER, "job_Ingenieur");
			addJobChannel(Gender.MALE, PlayerClass.GUNNER, "job_Schuetze");
			addJobChannel(Gender.MALE, PlayerClass.RIDER, "job_Aethertech");
            addJobChannel(Gender.MALE, PlayerClass.ARTIST, "job_Kuenstler");
            addJobChannel(Gender.MALE, PlayerClass.BARD, "job_Barde");
            
            // Femaletest
            addJobChannel(Gender.FEMALE, PlayerClass.WARRIOR, "job_Krieger[f:" + '"' + "Kriegerin" + '"' + "]");
            addJobChannel(Gender.FEMALE, PlayerClass.GLADIATOR, "job_Gladiator[f:" + '"' + "Gladiatorin" + '"' + "]");
            addJobChannel(Gender.FEMALE, PlayerClass.TEMPLAR, "job_Templer[f:" + '"' + "Templerin" + '"' + "]");
			addJobChannel(Gender.FEMALE, PlayerClass.MAGE, "job_Magier[f:" + '"' + "Magierin" + '"' + "]");
            addJobChannel(Gender.FEMALE, PlayerClass.SORCERER, "job_Zauberer[f:" + '"' + "Zauberin" + '"' + "]");
            addJobChannel(Gender.FEMALE, PlayerClass.SPIRIT_MASTER, "job_Beschwoerer[f:" + '"' + "Beschwoererin" + '"' + "]");
			addJobChannel(Gender.FEMALE, PlayerClass.PRIEST, "job_Priester[f:" + '"' + "Priesterin" + '"' + "]");
			addJobChannel(Gender.FEMALE, PlayerClass.CLERIC, "job_Kleriker[f:" + '"' + "Klerikerin" + '"' + "]");
            addJobChannel(Gender.FEMALE, PlayerClass.CHANTER, "job_Kantor[f:" + '"' + "Kantorin" + '"' + "]");
			addJobChannel(Gender.FEMALE, PlayerClass.SCOUT, "job_Spaeher[f:" + '"' + "Spaeherin" + '"' + "]");
            addJobChannel(Gender.FEMALE, PlayerClass.RANGER, "job_Jaeger[f:" + '"' + "Jaegerin" + '"' + "]");
            addJobChannel(Gender.FEMALE, PlayerClass.ASSASSIN, "job_Assassine[f:" + '"' + "Assassine" + '"' + "]");
            addJobChannel(Gender.FEMALE, PlayerClass.ENGINEER, "job_Ingenieur[f:" + '"' + "Ingenieur" + '"' + "]");
            addJobChannel(Gender.FEMALE, PlayerClass.ARTIST, "job_Kuenstler[f:" + '"' + "Kuenstler" + '"' + "]");
            addJobChannel(Gender.FEMALE, PlayerClass.GUNNER, "job_Schuetze[f:" + '"' + "Schuetze" + '"' + "]");
            addJobChannel(Gender.FEMALE, PlayerClass.BARD, "job_Barde[f:" + '"' + "Barde" + '"' + "]");
            addJobChannel(Gender.FEMALE, PlayerClass.RIDER, "job_Aethertech[f:" + '"' + "Aethertech" + '"' + "]");
        }
    }

    private static void addChannel(Channel Channel) {
        channels.add(Channel);
    }

    /**
     * @param channelId
     * @return
     */
    public static Channel getChannelById(int channelId) {
        for (Channel channel : channels) {
            if (channel.getChannelId() == channelId) {
                return channel;
            }
        }
        if (Config.LOG_CHANNEL_INVALID) {
            log.warn("No registered channel with id " + channelId);
        }
        throw new IllegalArgumentException("No channel provided for id " + channelId);
    }

    /**
     * @param identifier
     * @return
     */
    public static Channel getChannelByIdentifier(byte[] identifier) {
        for (Channel channel : channels) {
            if (Arrays.equals(channel.getIdentifierBytes(), identifier)) {
                return channel;
            }
        }
        if (Config.LOG_CHANNEL_INVALID) {
            log.warn("No registered channel with identifier " + identifier);
        }
        // we can't throw runtime exceptions before support of i18n channel names
        return null;
    }

    private static void addGroupChannel(String channelName) {
        addChannel(new LfgChannel(Race.ELYOS, "@\u0001" + channelName + "\u0001" + GameServerService.GAMESERVER_ID + ".0.AION.KOR"));
        addChannel(new LfgChannel(Race.ASMODIANS, "@\u0001" + channelName + "\u0001" + GameServerService.GAMESERVER_ID + ".1.AION.KOR"));
    }

    private static void addTradeChannel(String channelName) {
        addChannel(new TradeChannel(Race.ELYOS, "@\u0001" + channelName + "\u0001" + GameServerService.GAMESERVER_ID + ".0.AION.KOR"));
        addChannel(new TradeChannel(Race.ASMODIANS, "@\u0001" + channelName + "\u0001" + GameServerService.GAMESERVER_ID + ".1.AION.KOR"));
    }

    private static void addRegionChannel(int mapId, String channelName) {
        addChannel(new RegionChannel(mapId, Race.ELYOS, "@\u0001" + channelName + "\u0001" + GameServerService.GAMESERVER_ID + ".0.AION.KOR"));
        addChannel(new RegionChannel(mapId, Race.ASMODIANS, "@\u0001" + channelName + "\u0001" + GameServerService.GAMESERVER_ID + ".1.AION.KOR"));
    }

    private static void addJobChannel(Gender gender, PlayerClass playerClass, String channelName) {
        addChannel(new JobChannel(gender, playerClass, Race.ELYOS, "@\u0001" + channelName + "\u0001" + GameServerService.GAMESERVER_ID + ".0.AION.KOR"));
        addChannel(new JobChannel(gender, playerClass, Race.ASMODIANS, "@\u0001" + channelName + "\u0001" + GameServerService.GAMESERVER_ID + ".1.AION.KOR"));
    }

    private static void addLangChannel(String channelName) {
        addChannel(new LangChannel(Race.ELYOS, "@\u0001" + channelName + "\u0001" + GameServerService.GAMESERVER_ID + ".0.AION.KOR"));
        addChannel(new LangChannel(Race.ASMODIANS, "@\u0001" + channelName + "\u0001" + GameServerService.GAMESERVER_ID + ".1.AION.KOR"));
    }
}
