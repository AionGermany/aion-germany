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
package com.aionemu.gameserver.utils.rates;

import com.aionemu.gameserver.configs.main.CraftConfig;
import com.aionemu.gameserver.configs.main.RateConfig;

/**
 * @author ATracer
 * @author GiGatR00n v4.7.5.x
 */
public class VipRates extends Rates {

	int holidayRate = HolidayRates.getHolidayRates(2);

	@Override
	public float getXpRate() {
		return RateConfig.VIP_XP_RATE + holidayRate;
	}

	@Override
	public float getGroupXpRate() {
		return RateConfig.VIP_GROUPXP_RATE + holidayRate;
	}

    @Override
    public float getBookXpRate() {
        return RateConfig.VIP_BOOK_RATE + holidayRate;
    }

	@Override
	public float getQuestXpRate() {
		return RateConfig.VIP_QUEST_XP_RATE + holidayRate;
	}

	@Override
	public float getGatheringXPRate() {
		return RateConfig.VIP_GATHERING_XP_RATE + holidayRate;
	}

	@Override
	public int getGatheringCountRate() {
		return RateConfig.VIP_GATHERING_COUNT_RATE + holidayRate;
	}

	@Override
	public float getCraftingXPRate() {
		return RateConfig.VIP_CRAFTING_XP_RATE + holidayRate;
	}

	@Override
	public float getDropRate() {
		return RateConfig.VIP_DROP_RATE + holidayRate;
	}

	@Override
	public float getQuestKinahRate() {
		return RateConfig.VIP_QUEST_KINAH_RATE + holidayRate;
	}

	@Override
	public float getQuestApRate() {
		return RateConfig.VIP_QUEST_AP_RATE + holidayRate;
	}

	@Override
	public float getQuestGpRate() {
		return RateConfig.VIP_QUEST_GP_RATE + holidayRate;
	}

	@Override
	public float getQuestAbyssOpRate() {
		return RateConfig.VIP_QUEST_ABYSS_OP_RATE + holidayRate;
	}

	@Override
	public float getQuestExpBoostRate() {
		return RateConfig.VIP_QUEST_EXP_BOOST_RATE + holidayRate;
	}

	@Override
	public float getApPlayerGainRate() {
		return RateConfig.VIP_AP_PLAYER_GAIN_RATE + holidayRate;
	}

	@Override
	public float getGpPlayerGainRate() {
		return RateConfig.VIP_GP_PLAYER_GAIN_RATE;
	}

	@Override
	public float getXpPlayerGainRate() {
		return RateConfig.VIP_XP_PLAYER_GAIN_RATE + holidayRate;
	}

	@Override
	public float getApPlayerLossRate() {
		return RateConfig.VIP_AP_PLAYER_LOSS_RATE + holidayRate;
	}

	@Override
	public float getGpPlayerLossRate() {
		return RateConfig.VIP_GP_PLAYER_LOSS_RATE;
	}

	@Override
	public float getApNpcRate() {
		return RateConfig.VIP_AP_NPC_RATE + holidayRate;
	}

	@Override
	public float getDpNpcRate() {
		return RateConfig.VIP_DP_NPC_RATE + holidayRate;
	}

	@Override
	public float getDpPlayerRate() {
		return RateConfig.VIP_DP_PLAYER_RATE + holidayRate;
	}

	@Override
	public int getCraftCritRate() {
		return CraftConfig.VIP_CRAFT_CRIT_RATE + holidayRate;
	}

	@Override
	public int getComboCritRate() {
		return CraftConfig.VIP_CRAFT_COMBO_RATE + holidayRate;
	}

	@Override
	public float getDisciplineRewardRate() {
		return RateConfig.VIP_PVP_ARENA_DISCIPLINE_REWARD_RATE + holidayRate;
	}

	@Override
	public float getChaosRewardRate() {
		return RateConfig.VIP_PVP_ARENA_CHAOS_REWARD_RATE + holidayRate;
	}

	@Override
	public float getHarmonyRewardRate() {
		return RateConfig.VIP_PVP_ARENA_HARMONY_REWARD_RATE + holidayRate;
	}

	@Override
	public float getGloryRewardRate() {
		return RateConfig.VIP_PVP_ARENA_GLORY_REWARD_RATE + holidayRate;
	}

	@Override
	public float getSellLimitRate() {
		return RateConfig.VIP_SELL_LIMIT_RATE + holidayRate;
	}

	@Override
	public float getKamarRewardRate() {
		return RateConfig.KAMAR_REWARD_RATE + holidayRate;
	}

	@Override
	public float getRunatoriumBoxRewardRate() {
		return RateConfig.RUNATORIUM_BOX_REWARD_RATE + holidayRate;
	}

	@Override
	public float getJormungandRewardRate() {
		return RateConfig.VIP_JORMUNGAND_REWARD_RATE + holidayRate;
	}

	@Override
	public float getSteelWallRewardRate() {
		return RateConfig.VIP_STEELWALL_REWARD_RATE + holidayRate;
	}
}
