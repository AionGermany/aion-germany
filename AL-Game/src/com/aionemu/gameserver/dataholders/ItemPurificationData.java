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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.model.templates.item.purification.ItemPurificationTemplate;
import com.aionemu.gameserver.model.templates.item.purification.PurificationResultItem;

import gnu.trove.map.hash.TIntObjectHashMap;
import javolution.util.FastMap;

/**
 * @author Ranastic
 * @rework Navyan
 */
@XmlRootElement(name = "item_purifications")
@XmlAccessorType(XmlAccessType.FIELD)
public class ItemPurificationData {

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(ItemPurificationData.class);
	@XmlElement(name = "item_purification")
	protected List<ItemPurificationTemplate> ItemPurificationTemplates;
	private TIntObjectHashMap<ItemPurificationTemplate> itemPurificationSets;
	private FastMap<Integer, FastMap<Integer, PurificationResultItem>> ResultItemMap;

	void afterUnmarshal(Unmarshaller u, Object parent) {
		itemPurificationSets = new TIntObjectHashMap<ItemPurificationTemplate>();
		ResultItemMap = new FastMap<Integer, FastMap<Integer, PurificationResultItem>>();

		for (ItemPurificationTemplate set : ItemPurificationTemplates) {
			itemPurificationSets.put(set.getPurification_base_item_id(), set);

			ResultItemMap.put(set.getPurification_base_item_id(), new FastMap<Integer, PurificationResultItem>());

			if (!set.getPurification_result_item().isEmpty()) {
				for (PurificationResultItem resultItem : set.getPurification_result_item()) {
					ResultItemMap.get(set.getPurification_base_item_id()).put(resultItem.getItem_id(), resultItem);
				}
			}
		}
		ItemPurificationTemplates = null;
	}

	/**
	 * @param itemSetId
	 * @return
	 */
	public ItemPurificationTemplate getItemPurificationTemplate(int itemSetId) {
		return itemPurificationSets.get(itemSetId);
	}

	public FastMap<Integer, PurificationResultItem> getResultItemMap(int baseItemId) {
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
		return itemPurificationSets.size();
	}
}
