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
package com.aionemu.gameserver.dataholders;

import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.aionemu.gameserver.model.templates.item.upgrade.ItemUpgradeTemplate;
import com.aionemu.gameserver.model.templates.item.upgrade.UpgradeResultItem;

import gnu.trove.map.hash.TIntObjectHashMap;
import javolution.util.FastMap;

/**
 * @author Ranastic
 * @rework Navyan
 */
@XmlRootElement(name = "item_upgradess")
@XmlAccessorType(XmlAccessType.FIELD)
public class ItemUpgradeData {

	@XmlElement(name = "item_upgrade")
	protected List<ItemUpgradeTemplate> ItemUpgradeTemplates;
	private TIntObjectHashMap<ItemUpgradeTemplate> itemUpgradeSets;
	private FastMap<Integer, FastMap<Integer, UpgradeResultItem>> ResultItemMap;

	void afterUnmarshal(Unmarshaller u, Object parent) {
		itemUpgradeSets = new TIntObjectHashMap<ItemUpgradeTemplate>();
		ResultItemMap = new FastMap<Integer, FastMap<Integer, UpgradeResultItem>>();

		for (ItemUpgradeTemplate set : ItemUpgradeTemplates) {
			itemUpgradeSets.put(set.getUpgrade_base_item_id(), set);

			ResultItemMap.put(set.getUpgrade_base_item_id(), new FastMap<Integer, UpgradeResultItem>());

			if (!set.getUpgrade_result_item().isEmpty()) {
				for (UpgradeResultItem resultItem : set.getUpgrade_result_item()) {
					ResultItemMap.get(set.getUpgrade_base_item_id()).put(resultItem.getItem_id(), resultItem);
				}
			}
		}
		ItemUpgradeTemplates = null;
	}

	/**
	 * @param itemSetId
	 * @return
	 */
	public ItemUpgradeTemplate getItemUpgradeTemplate(int itemSetId) {
		return itemUpgradeSets.get(itemSetId);
	}

	public FastMap<Integer, UpgradeResultItem> getResultItemMap(int baseItemId) {
		if (ResultItemMap.containsKey(baseItemId)) {
			if (!ResultItemMap.get(baseItemId).isEmpty()) {
				return ResultItemMap.get(baseItemId);
			}
			else {
				return null;
			}
		}
		else {
			return null;
		}
	}

	/**
	 * @return itemSets.size()
	 */
	public int size() {
		return itemUpgradeSets.size();
	}
}
