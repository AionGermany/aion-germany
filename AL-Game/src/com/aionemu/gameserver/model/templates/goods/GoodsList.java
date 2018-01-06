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
package com.aionemu.gameserver.model.templates.goods;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.model.limiteditems.LimitedItem;

import javolution.util.FastList;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GoodsList")
public class GoodsList {

	@XmlElement(name = "item")
	private List<Item> items;
	@XmlAttribute(name = "id")
	private int id;
	@XmlElement(name = "salestime")
	private String salesTime;
	private List<Integer> itemIdList;

	void afterUnmarshal(Unmarshaller u, Object parent) {
		itemIdList = new ArrayList<Integer>();
		if (items == null) {
			return;
		}

		for (Item item : items) {
			itemIdList.add(item.getId());
		}
	}

	/**
	 * return the limitedItems.
	 */
	public FastList<LimitedItem> getLimitedItems() {
		FastList<LimitedItem> limitedItems = new FastList<LimitedItem>();
		if (items != null) {
			for (Item item : items) {
				if (item.getBuyLimit() != null && item.getSellLimit() != null) {
					limitedItems.add(new LimitedItem(item.getId(), item.getSellLimit(), item.getBuyLimit(), salesTime));
				}
			}
		}
		return limitedItems;
	}

	/**
	 * Gets the value of the id property.
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the itemIdList
	 */
	public List<Integer> getItemIdList() {
		return itemIdList;
	}

	/**
	 * <p/>
	 * Java class for anonymous complex type.
	 * <p/>
	 * The following schema fragment specifies the expected content contained within this class.
	 * <p/>
	 * 
	 * <pre>
	 * &lt;complexType>
	 *   &lt;complexContent>
	 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
	 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}int" />
	 *     &lt;/restriction>
	 *   &lt;/complexContent>
	 * &lt;/complexType>
	 * </pre>
	 */
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "")
	public static class Item {

		@XmlAttribute
		private int id;
		@XmlAttribute(name = "sell_limit")
		private Integer sellLimit;
		@XmlAttribute(name = "buy_limit")
		private Integer buyLimit;

		/**
		 * Gets the value of the id property.
		 */
		public int getId() {
			return id;
		}

		/**
		 * return sellLimit.
		 */
		public Integer getSellLimit() {
			return sellLimit;
		}

		/**
		 * return buyLimit.
		 */
		public Integer getBuyLimit() {
			return buyLimit;
		}
	}
}
