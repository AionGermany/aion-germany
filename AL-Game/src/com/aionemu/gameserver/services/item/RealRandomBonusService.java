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
package com.aionemu.gameserver.services.item;

import java.util.ArrayList;
import java.util.List;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.dao.RealItemRndBonusDAO;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.RealRandomBonus;
import com.aionemu.gameserver.model.items.RealRandomBonusStat;
import com.aionemu.gameserver.model.stats.calc.functions.StatFunction;
import com.aionemu.gameserver.model.stats.container.StatEnum;
import com.aionemu.gameserver.model.stats.listeners.ItemEquipmentListener;
import com.aionemu.gameserver.model.templates.item.bonuses.BonusStat;
import com.aionemu.gameserver.model.templates.item.bonuses.RealItemRandomBonus;

public class RealRandomBonusService {

	public static void setBonus(Item item) {
		RealItemRandomBonus rndBonus = DataManager.ITEM_REAL_RANDOM_BONUSES.getRealBonusById(item.getItemTemplate().getRealRndBonus());
		if (rndBonus != null) {
			List<RealRandomBonusStat> statsList = new ArrayList<RealRandomBonusStat>();
			List<BonusStat> stats = new ArrayList<BonusStat>();
			stats.addAll(rndBonus.getRndStat());
			for (int i = 0; i < rndBonus.getRandomNumber(); ++i) {
				BonusStat stat = stats.get(Rnd.get(0, stats.size() - 1));
				statsList.add(new RealRandomBonusStat(stat.getName(), Rnd.get(stat.getMin(), stat.getMax()), false));
				stats.remove(stat);
			}
			RealRandomBonus bonus = new RealRandomBonus(item.getObjectId(), statsList);
			item.setRealRndBonus(bonus);
			DAOManager.getDAO(RealItemRndBonusDAO.class).updateRandomBonuses(bonus);
		}
	}

	public static void rerollAllBonuses(Player player, Item item) {
		DAOManager.getDAO(RealItemRndBonusDAO.class).deleteMainRandomBonuses(item);
		List<StatFunction> fusionStat = item.getRealRndBonus().getFusionFunctions();
		item.setRealRndBonus(null);
		setBonus(item);
		item.getRealRndBonus().getFusionFunctions().addAll(fusionStat);
		refreshStats(player, item);
	}

	public static void rerollSingleBonus(Player player, Item item, int statId) {
		RealItemRandomBonus rndBonus = DataManager.ITEM_REAL_RANDOM_BONUSES.getRealBonusById(item.getItemTemplate().getRealRndBonus());
		StatEnum statName = StatEnum.findByItemStoneMask(statId);
		BonusStat stat = rndBonus.getRndStat().get(statId);
		int value = Rnd.get(stat.getMin(), stat.getMax());
		RealRandomBonus bonus = item.getRealRndBonus();
		RealRandomBonusStat oldStat = null;
		RealRandomBonusStat newStat = new RealRandomBonusStat(statName, value, false);
		for (RealRandomBonusStat rs : bonus.getStats()) {
			if (!rs.isFusion() && rs.getStat().equals(statName)) {
				oldStat = rs;
			}
		}
		bonus.getStats().remove(oldStat);
		bonus.getStats().add(newStat);
		bonus.recalcStats();
		refreshStats(player, item);
		DAOManager.getDAO(RealItemRndBonusDAO.class).deleteMainRandomBonuses(item);
		DAOManager.getDAO(RealItemRndBonusDAO.class).updateRandomBonuses(bonus);
	}

	public static void addFusionRandomBonuses(Player player, Item firstItem, Item secondItem) {
		if (secondItem.getRealRndBonus() != null) {
			DAOManager.getDAO(RealItemRndBonusDAO.class).updateFusionRandomBonuses(firstItem, secondItem);
			DAOManager.getDAO(RealItemRndBonusDAO.class).deleteAllRandomBonuses(secondItem);
			if (firstItem.getRealRndBonus() != null) {
				RealRandomBonus bonus = firstItem.getRealRndBonus();
				bonus.getFusionFunctions().addAll(secondItem.getRealRndBonus().getFunctions());
				refreshStats(player, firstItem);
			}
			secondItem.setRealRndBonus(null);
		}
	}

	public static void deleteFusionRandomBonuses(Player player,  Item item) {
		if (item.getRealRndBonus() != null) {
			RealRandomBonus bonus = item.getRealRndBonus();
			bonus.getFusionFunctions().clear();
			DAOManager.getDAO(RealItemRndBonusDAO.class).deleteFusionRandomBonuses(item);
			refreshStats(player, item);
		}
	}

	private static void refreshStats(Player player,  Item item) {
		if (item.isEquipped()) {
			ItemEquipmentListener.onItemUnequipment(item, player);
			ItemEquipmentListener.onItemEquipment(item, player);
		}
	}
}
