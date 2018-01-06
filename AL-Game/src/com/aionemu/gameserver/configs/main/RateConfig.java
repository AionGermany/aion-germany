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
package com.aionemu.gameserver.configs.main;

import com.aionemu.commons.configuration.Property;

/**
 * @author ATracer
 * @author GiGatR00n v4.7.5.x
 */
public class RateConfig {

	/**
	 * Display server rates when player enter in world
	 */
	@Property(key = "gameserver.rate.display.rates", defaultValue = "false")
	public static boolean DISPLAY_RATE;
	/**
	 * XP Rates - Regular (1), Premium (2), VIP (3)
	 */
	@Property(key = "gameserver.rate.regular.xp", defaultValue = "1.0")
	public static float XP_RATE;
	@Property(key = "gameserver.rate.premium.xp", defaultValue = "2.0")
	public static float PREMIUM_XP_RATE;
	@Property(key = "gameserver.rate.vip.xp", defaultValue = "3.0")
	public static float VIP_XP_RATE;
	/**
	 * Group XP Rates - Regular (1), Premium (2), VIP (3)
	 */
	@Property(key = "gameserver.rate.regular.group.xp", defaultValue = "1.0")
	public static float GROUPXP_RATE;
	@Property(key = "gameserver.rate.premium.group.xp", defaultValue = "2.0")
	public static float PREMIUM_GROUPXP_RATE;
	@Property(key = "gameserver.rate.vip.group.xp", defaultValue = "3.0")
	public static float VIP_GROUPXP_RATE;
	/**
	 * Quest XP Rates - Regular (1), Premium (2), VIP (3)
	 */
	@Property(key = "gameserver.rate.regular.quest.xp", defaultValue = "2")
	public static float QUEST_XP_RATE;
	@Property(key = "gameserver.rate.premium.quest.xp", defaultValue = "4")
	public static float PREMIUM_QUEST_XP_RATE;
	@Property(key = "gameserver.rate.vip.quest.xp", defaultValue = "6")
	public static float VIP_QUEST_XP_RATE;
	/**
	 * Gathering XP Rates - Regular (1), Premium (2), VIP (3)
	 */
	@Property(key = "gameserver.rate.regular.gathering.xp", defaultValue = "1.0")
	public static float GATHERING_XP_RATE;
	@Property(key = "gameserver.rate.premium.gathering.xp", defaultValue = "2.0")
	public static float PREMIUM_GATHERING_XP_RATE;
	@Property(key = "gameserver.rate.vip.gathering.xp", defaultValue = "3.0")
	public static float VIP_GATHERING_XP_RATE;
	/**
	 * Gathering Count Rates - Regular (1), Premium (1), VIP (1)
	 */
	@Property(key = "gameserver.rate.regular.gathering.count", defaultValue = "1")
	public static int GATHERING_COUNT_RATE;
	@Property(key = "gameserver.rate.premium.gathering.count", defaultValue = "1")
	public static int PREMIUM_GATHERING_COUNT_RATE;
	@Property(key = "gameserver.rate.vip.gathering.count", defaultValue = "1")
	public static int VIP_GATHERING_COUNT_RATE;
	/**
	 * Crafting XP Rates - Regular (1), Premium (2), VIP (3)
	 */
	@Property(key = "gameserver.rate.regular.crafting.xp", defaultValue = "1.0")
	public static float CRAFTING_XP_RATE;
	@Property(key = "gameserver.rate.premium.crafting.xp", defaultValue = "2.0")
	public static float PREMIUM_CRAFTING_XP_RATE;
	@Property(key = "gameserver.rate.vip.crafting.xp", defaultValue = "3.0")
	public static float VIP_CRAFTING_XP_RATE;
	/**
	 * Quest Kinah Rates - Regular (1), Premium (2), VIP (3)
	 */
	@Property(key = "gameserver.rate.regular.quest.kinah", defaultValue = "1.0")
	public static float QUEST_KINAH_RATE;
	@Property(key = "gameserver.rate.premium.quest.kinah", defaultValue = "2.0")
	public static float PREMIUM_QUEST_KINAH_RATE;
	@Property(key = "gameserver.rate.vip.quest.kinah", defaultValue = "3.0")
	public static float VIP_QUEST_KINAH_RATE;
	/**
	 * Quest AP Rates - Regular (1), Premium (2), VIP (3)
	 */
	@Property(key = "gameserver.rate.regular.quest.ap", defaultValue = "1.0")
	public static float QUEST_AP_RATE;
	@Property(key = "gameserver.rate.premium.quest.ap", defaultValue = "2.0")
	public static float PREMIUM_QUEST_AP_RATE;
	@Property(key = "gameserver.rate.vip.quest.ap", defaultValue = "3.0")
	public static float VIP_QUEST_AP_RATE;
	/**
	 * Quest GP Rates - Regular (1), Premium (1.5), VIP (2)
	 */
	@Property(key = "gameserver.rate.regular.quest.gp", defaultValue = "1.0")
	public static float QUEST_GP_RATE;
	@Property(key = "gameserver.rate.premium.quest.gp", defaultValue = "1.5")
	public static float PREMIUM_QUEST_GP_RATE;
	@Property(key = "gameserver.rate.vip.quest.gp", defaultValue = "2.0")
	public static float VIP_QUEST_GP_RATE;
	/**
	 * Quest Abyss OP - Regular (1), Premium (1.5), VIP (2)
	 */
	@Property(key = "gameserver.rate.regular.quest.abyss_op", defaultValue = "1")
	public static float QUEST_ABYSS_OP_RATE;
	@Property(key = "gameserver.rate.premium.quest.abyss_op", defaultValue = "2")
	public static float PREMIUM_QUEST_ABYSS_OP_RATE;
	@Property(key = "gameserver.rate.vip.quest.abyss_op", defaultValue = "3")
	public static float VIP_QUEST_ABYSS_OP_RATE;
	/**
	 * Drop Rates - Regular (1), Premium (2), VIP (3)
	 */
	@Property(key = "gameserver.rate.regular.drop", defaultValue = "1.0")
	public static float DROP_RATE;
	@Property(key = "gameserver.rate.premium.drop", defaultValue = "2.0")
	public static float PREMIUM_DROP_RATE;
	@Property(key = "gameserver.rate.vip.drop", defaultValue = "3.0")
	public static float VIP_DROP_RATE;
	/**
	 * Player Abyss Points Rates (Gain) - Regular (1), Premium (2), VIP (3)
	 */
	@Property(key = "gameserver.rate.regular.ap.player.gain", defaultValue = "1.0")
	public static float AP_PLAYER_GAIN_RATE;
	@Property(key = "gameserver.rate.premium.ap.player.gain", defaultValue = "2.0")
	public static float PREMIUM_AP_PLAYER_GAIN_RATE;
	@Property(key = "gameserver.rate.vip.ap.player.gain", defaultValue = "3.0")
	public static float VIP_AP_PLAYER_GAIN_RATE;
	/**
	 * Player glory Points Rates (Gain) - Regular (1), Premium (2), VIP (3)
	 */
	@Property(key = "gameserver.rate.regular.gp.player.gain", defaultValue = "1.0")
	public static float GP_PLAYER_GAIN_RATE;
	@Property(key = "gameserver.rate.premium.gp.player.gain", defaultValue = "2.0")
	public static float PREMIUM_GP_PLAYER_GAIN_RATE;
	@Property(key = "gameserver.rate.vip.gp.player.gain", defaultValue = "3.0")
	public static float VIP_GP_PLAYER_GAIN_RATE;
	/**
	 * Player Experience Points Rates (Gain) - Regular (1), Premium (2), VIP (3)
	 */
	@Property(key = "gameserver.rate.regular.xp.player.gain", defaultValue = "1.0")
	public static float XP_PLAYER_GAIN_RATE;
	@Property(key = "gameserver.rate.premium.xp.player.gain", defaultValue = "2.0")
	public static float PREMIUM_XP_PLAYER_GAIN_RATE;
	@Property(key = "gameserver.rate.vip.xp.player.gain", defaultValue = "3.0")
	public static float VIP_XP_PLAYER_GAIN_RATE;
	/**
	 * Player Abyss Points Rates - Regular (1), Premium (2), VIP (3)
	 */
	@Property(key = "gameserver.rate.regular.ap.player.loss", defaultValue = "1.0")
	public static float AP_PLAYER_LOSS_RATE;
	@Property(key = "gameserver.rate.premium.ap.player.loss", defaultValue = "2.0")
	public static float PREMIUM_AP_PLAYER_LOSS_RATE;
	@Property(key = "gameserver.rate.vip.ap.player.loss", defaultValue = "3.0")
	public static float VIP_AP_PLAYER_LOSS_RATE;
	/**
	 * Player glory Points Rates - Regular (1), Premium (2), VIP (3)
	 */
	@Property(key = "gameserver.rate.regular.gp.player.loss", defaultValue = "1.0")
	public static float GP_PLAYER_LOSS_RATE;
	@Property(key = "gameserver.rate.premium.gp.player.loss", defaultValue = "2.0")
	public static float PREMIUM_GP_PLAYER_LOSS_RATE;
	@Property(key = "gameserver.rate.vip.gp.player.loss", defaultValue = "3.0")
	public static float VIP_GP_PLAYER_LOSS_RATE;
	/**
	 * NPC Abyss Points Rates - Regular (1), Premium (2), VIP (3)
	 */
	@Property(key = "gameserver.rate.regular.ap.npc", defaultValue = "1.0")
	public static float AP_NPC_RATE;
	@Property(key = "gameserver.rate.premium.ap.npc", defaultValue = "2.0")
	public static float PREMIUM_AP_NPC_RATE;
	@Property(key = "gameserver.rate.vip.ap.npc", defaultValue = "3.0")
	public static float VIP_AP_NPC_RATE;
	/**
	 * NPC Glory Points Rates - Regular (1), Premium (2), VIP (3)
	 */
	@Property(key = "gameserver.rate.regular.gp.npc", defaultValue = "1.0")
	public static float GP_NPC_RATE;
	@Property(key = "gameserver.rate.premium.gp.npc", defaultValue = "2.0")
	public static float PREMIUM_GP_NPC_RATE;
	@Property(key = "gameserver.rate.vip.gp.npc", defaultValue = "3.0")
	public static float VIP_GP_NPC_RATE;
	/**
	 * PVE DP Rates - Regular (1), Premium (2), VIP (3)
	 */
	@Property(key = "gameserver.rate.regular.dp.npc", defaultValue = "1.0")
	public static float DP_NPC_RATE;
	@Property(key = "gameserver.rate.premium.dp.npc", defaultValue = "2.0")
	public static float PREMIUM_DP_NPC_RATE;
	@Property(key = "gameserver.rate.vip.dp.npc", defaultValue = "3.0")
	public static float VIP_DP_NPC_RATE;
	/**
	 * PVP DP Rates - Regular (1), Premium (2), VIP (3)
	 */
	@Property(key = "gameserver.rate.regular.dp.player", defaultValue = "1.0")
	public static float DP_PLAYER_RATE;
	@Property(key = "gameserver.rate.premium.dp.player", defaultValue = "2.0")
	public static float PREMIUM_DP_PLAYER_RATE;
	@Property(key = "gameserver.rate.vip.dp.player", defaultValue = "3.0")
	public static float VIP_DP_PLAYER_RATE;
	/**
	 * PVP Arena and Dredgion reward rates
	 */
	@Property(key = "gameserver.rate.abyss.runatorium", defaultValue = "4")
	public static float RUNATORIUM_ABYSS_REWARD_RATE;
	@Property(key = "gameserver.rate.glory.runatorium", defaultValue = "1.4")
	public static float RUNATORIUM_GLORY_REWARD_RATE;
	@Property(key = "gameserver.rate.box.runatorium", defaultValue = "1")
	public static float RUNATORIUM_BOX_REWARD_RATE;
	@Property(key = "gameserver.rate.dredgion", defaultValue = "5")
	public static float DREDGION_REWARD_RATE;
	@Property(key = "gameserver.rate.kamar", defaultValue = "5")
	public static float KAMAR_REWARD_RATE;
	@Property(key = "gameserver.rate.ophidan", defaultValue = "5")
	public static float OPHIDAN_REWARD_RATE;
	@Property(key = "gameserver.rate.regular.pvparena.discipline", defaultValue = "3")
	public static float PVP_ARENA_DISCIPLINE_REWARD_RATE;
	@Property(key = "gameserver.rate.premium.pvparena.discipline", defaultValue = "3")
	public static float PREMIUM_PVP_ARENA_DISCIPLINE_REWARD_RATE;
	@Property(key = "gameserver.rate.vip.pvparena.discipline", defaultValue = "3")
	public static float VIP_PVP_ARENA_DISCIPLINE_REWARD_RATE;
	@Property(key = "gameserver.rate.regular.pvparena.chaos", defaultValue = "3")
	public static float PVP_ARENA_CHAOS_REWARD_RATE;
	@Property(key = "gameserver.rate.premium.pvparena.chaos", defaultValue = "3")
	public static float PREMIUM_PVP_ARENA_CHAOS_REWARD_RATE;
	@Property(key = "gameserver.rate.vip.pvparena.chaos", defaultValue = "3")
	public static float VIP_PVP_ARENA_CHAOS_REWARD_RATE;
	@Property(key = "gameserver.rate.regular.pvparena.harmony", defaultValue = "3")
	public static float PVP_ARENA_HARMONY_REWARD_RATE;
	@Property(key = "gameserver.rate.premium.pvparena.harmony", defaultValue = "3")
	public static float PREMIUM_PVP_ARENA_HARMONY_REWARD_RATE;
	@Property(key = "gameserver.rate.vip.pvparena.harmony", defaultValue = "3")
	public static float VIP_PVP_ARENA_HARMONY_REWARD_RATE;
	@Property(key = "gameserver.rate.regular.pvparena.glory", defaultValue = "3")
	public static float PVP_ARENA_GLORY_REWARD_RATE;
	@Property(key = "gameserver.rate.premium.pvparena.glory", defaultValue = "1")
	public static float PREMIUM_PVP_ARENA_GLORY_REWARD_RATE;
	@Property(key = "gameserver.rate.vip.pvparena.glory", defaultValue = "3")
	public static float VIP_PVP_ARENA_GLORY_REWARD_RATE;
	@Property(key = "gameserver.rate.regular.jormungand", defaultValue = "1")
	public static float REGULAR_JORMUNGAND_REWARD_RATE;
	@Property(key = "gameserver.rate.premium.jormungand", defaultValue = "1")
	public static float PREMIUM_JORMUNGAND_REWARD_RATE;
	@Property(key = "gameserver.rate.vip.jormungand", defaultValue = "1")
	public static float VIP_JORMUNGAND_REWARD_RATE;
	@Property(key = "gameserver.rate.steelwall", defaultValue = "1")
	public static float STEELWALL_REWARD_RATE;
	@Property(key = "gameserver.rate.regular.steelwall", defaultValue = "1")
	public static float REGULAR_STEELWALL_REWARD_RATE;
	@Property(key = "gameserver.rate.premium.steelwall", defaultValue = "1")
	public static float PREMIUM_STEELWALL_REWARD_RATE;
	@Property(key = "gameserver.rate.vip.steelwall", defaultValue = "1")
	public static float VIP_STEELWALL_REWARD_RATE;
	/**
	 * Rate which affects amount of required ap for Abyss rank
	 */
	@Property(key = "gameserver.rate.ap.rank", defaultValue = "1")
	public static int ABYSS_RANK_RATE;
	/**
	 * Sell limits rate
	 */
	@Property(key = "gameserver.rate.regular.sell.limit", defaultValue = "2000")
	public static float SELL_LIMIT_RATE;
	@Property(key = "gameserver.rate.premium.sell.limit", defaultValue = "2000")
	public static float PREMIUM_SELL_LIMIT_RATE;
	@Property(key = "gameserver.rate.vip.sell.limit", defaultValue = "2000")
	public static float VIP_SELL_LIMIT_RATE;
	/**
	 * Instance reward rate like Dredgion for new instance
	 */
	@Property(key = "gameserver.rate.instance", defaultValue = "2")
	public static float INSTANCE_REWARD_RATE;
	/*
	 * Holiday Rates
	 */
	@Property(key = "gameserver.rate.holiday.enable", defaultValue = "false")
	public static boolean HOLIDAY_RATE_ENAMBLE;
	@Property(key = "gameserver.rate.holiday.regular", defaultValue = "1")
	public static int HOLIDAY_RATE_REGULAR;
	@Property(key = "gameserver.rate.holiday.premium", defaultValue = "2")
	public static int HOLIDAY_RATE_PREMIUM;
	@Property(key = "gameserver.rate.holiday.vip", defaultValue = "4")
	public static int HOLIDAY_RATE_VIP;
	@Property(key = "gameserver.rate.holiday.days", defaultValue = "1,7")
	public static String HOLIDAY_RATE_DAYS;
	/*
	 * Growth Energy (5.x)
	 */
	@Property(key = "gameserver.rate.growth.energy.chance", defaultValue = "50")
	public static int GROWTH_ENERGY;
	@Property(key = "gameserver.rate.regular.quest.exp_boost", defaultValue = "1")
	public static float QUEST_EXP_BOOST_RATE;
	@Property(key = "gameserver.rate.premium.quest.exp_boost", defaultValue = "2")
	public static float PREMIUM_QUEST_EXP_BOOST_RATE;
	@Property(key = "gameserver.rate.vip.quest.exp_boost", defaultValue = "3")
	public static float VIP_QUEST_EXP_BOOST_RATE;

	/*
	 * Monsterbook XP Rate (5.x)
	 */
    @Property(key = "gameserver.rate.regular.book", defaultValue = "1")
    public static float BOOK_RATE;
    @Property(key = "gameserver.rate.premium.book", defaultValue = "2")
    public static float PREMIUM_BOOK_RATE;
    @Property(key = "gameserver.rate.vip.book", defaultValue = "3")
    public static float VIP_BOOK_RATE;
}
