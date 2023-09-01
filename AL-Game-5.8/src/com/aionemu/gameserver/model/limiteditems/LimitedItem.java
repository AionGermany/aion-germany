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
package com.aionemu.gameserver.model.limiteditems;

import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * @author xTz
 */
public class LimitedItem {

	private int itemId;
	private int sellLimit;
	private int buyLimit;
	private int defaultSellLimit;
	private String salesTime;
	private TIntObjectHashMap<Integer> buyCounts = new TIntObjectHashMap<Integer>();

	public LimitedItem() {
	}

	public LimitedItem(int itemId, int sellLimit, int buyLimit, String salesTime) {
		this.itemId = itemId;
		this.sellLimit = sellLimit;
		this.buyLimit = buyLimit;
		this.defaultSellLimit = sellLimit;
		this.salesTime = salesTime;
	}

	/**
	 * return itemId.
	 */
	public int getItemId() {
		return itemId;
	}

	/**
	 * @param set
	 *            playerObjectId.
	 * @param set
	 *            count.
	 */
	public void setBuyCount(int playerObjectId, int count) {
		buyCounts.putIfAbsent(playerObjectId, count);
	}

	/**
	 * return playerListByObject.
	 */
	public TIntObjectHashMap<Integer> getBuyCount() {
		return buyCounts;
	}

	/**
	 * @param set
	 *            itemId.
	 */
	public void setItem(int itemId) {
		this.itemId = itemId;
	}

	/**
	 * return sellLimit.
	 */
	public int getSellLimit() {
		return sellLimit;
	}

	/**
	 * return buyLimit.
	 */
	public int getBuyLimit() {
		return buyLimit;
	}

	public void setToDefault() {
		sellLimit = defaultSellLimit;
		buyCounts.clear();
	}

	/**
	 * @param set
	 *            sellLimit.
	 */
	public void setSellLimit(int sellLimit) {
		this.sellLimit = sellLimit;
	}

	/**
	 * return defaultSellLimit.
	 */
	public int getDefaultSellLimit() {
		return defaultSellLimit;
	}

	public String getSalesTime() {
		return salesTime;
	}
}
