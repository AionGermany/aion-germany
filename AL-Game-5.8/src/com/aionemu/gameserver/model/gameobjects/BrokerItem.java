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
package com.aionemu.gameserver.model.gameobjects;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Comparator;

import org.apache.commons.lang.StringUtils;

import com.aionemu.gameserver.model.broker.BrokerRace;

/**
 * @author kosyachok
 */
public class BrokerItem implements Comparable<BrokerItem> {

	private Item item;
	private int itemId;
	private int itemUniqueId;
	private long itemCount;
	private String itemCreator;
	private long price;
	private String seller;
	private int sellerId;
	private BrokerRace itemBrokerRace;
	private boolean isSold, isCanceled;
	private boolean isSettled;
	private Timestamp expireTime;
	private Timestamp settleTime;
	PersistentState state;
	private boolean partSale;

	/**
	 * Used where registering item
	 *
	 * @param item
	 * @param price
	 * @param seller
	 * @param sellerId
	 * @param sold
	 * @param itemBrokerRace
	 */
	public BrokerItem(Item item, long price, String seller, int sellerId, BrokerRace itemBrokerRace, boolean partSale) {
		this.item = item;
		this.itemId = item.getItemTemplate().getTemplateId();
		this.itemUniqueId = item.getObjectId();
		this.itemCount = item.getItemCount();
		this.itemCreator = item.getItemCreator();
		this.price = price;
		this.seller = seller;
		this.sellerId = sellerId;
		this.itemBrokerRace = itemBrokerRace;
		this.isSold = false;
		this.isSettled = false;
		this.expireTime = new Timestamp(Calendar.getInstance().getTimeInMillis() + 691200000); // 8 days
		this.settleTime = new Timestamp(Calendar.getInstance().getTimeInMillis());
		this.partSale = partSale;

		this.state = PersistentState.NEW;
	}

	/**
	 * Used onDBLoad
	 *
	 * @param item
	 * @param itemId
	 * @param price
	 * @param seller
	 * @param sellerId
	 * @param itemBrokerRace
	 */
	public BrokerItem(Item item, int itemId, int itemUniqueId, long itemCount, String itemCreator, long price, String seller, int sellerId, BrokerRace itemBrokerRace, boolean isSold, boolean isSettled, Timestamp expireTime, Timestamp settleTime, boolean partSale) {
		this.item = item;
		this.itemId = itemId;
		this.itemUniqueId = itemUniqueId;
		this.itemCount = itemCount;
		this.itemCreator = itemCreator;
		this.price = price;
		this.seller = seller;
		this.sellerId = sellerId;
		this.itemBrokerRace = itemBrokerRace;

		this.partSale = partSale;

		if (item == null) {
			this.isSold = true;
			this.isSettled = true;

		}
		else {
			this.isSold = isSold;
			this.isSettled = isSettled;
		}

		this.expireTime = expireTime;
		this.settleTime = settleTime;

		this.state = PersistentState.NOACTION;
	}

	/**
	 * @param return
	 *            itemCreator
	 */
	public String getItemCreator() {
		if (itemCreator == null) {
			return StringUtils.EMPTY;
		}
		return itemCreator;
	}

	public boolean isPartSale() {
		return this.partSale;
	}

	/**
	 * @param itemCreator
	 *            the itemCreator to set
	 */
	public void setItemCreator(String itemCreator) {
		this.itemCreator = itemCreator;
	}

	/**
	 * @return
	 */
	public Item getItem() {
		return item;
	}

	public boolean isCanceled() {
		return isCanceled;
	}

	public void setIsCanceled(boolean isCanceled) {
		this.isCanceled = isCanceled;
	}

	public void removeItem() {
		// this.item = null;
		this.isSold = true;
		this.isSettled = true;
		this.settleTime = new Timestamp(Calendar.getInstance().getTimeInMillis());
	}

	public int getItemId() {
		return itemId;
	}

	public int getItemUniqueId() {
		return itemUniqueId;
	}

	/**
	 * @return
	 */
	public long getPrice() {
		return price;
	}

	public void setPrice(long price) {
		this.price = price;
	}

	/**
	 * @return
	 */
	public String getSeller() {
		return seller;
	}

	public int getSellerId() {
		return sellerId;
	}

	/**
	 * @return
	 */
	public BrokerRace getItemBrokerRace() {
		return itemBrokerRace;
	}

	/**
	 * @return
	 */
	public boolean isSold() {
		return this.isSold;
	}

	public void setPersistentState(PersistentState persistentState) {
		switch (persistentState) {
			case DELETED:
				if (this.state == PersistentState.NEW) {
					this.state = PersistentState.NOACTION;
				}
				else {
					this.state = PersistentState.DELETED;
				}
				break;
			case UPDATE_REQUIRED:
				if (this.state == PersistentState.NEW) {
					break;
				}
			default:
				this.state = persistentState;
		}

	}

	public PersistentState getPersistentState() {
		return state;
	}

	public boolean isSettled() {
		return isSettled;
	}

	public void setSettled() {
		this.isSettled = true;
		this.settleTime = new Timestamp(Calendar.getInstance().getTimeInMillis());
	}

	public Timestamp getExpireTime() {
		return expireTime;
	}

	public Timestamp getSettleTime() {
		return settleTime;
	}

	public long getItemCount() {
		return itemCount;
	}

	public void setItemCount(long itemCount) {
		this.itemCount = itemCount;
	}

	/**
	 * @return item level according to template
	 */
	private int getItemLevel() {
		return item.getItemTemplate().getLevel();
	}

	/**
	 * @return price for one piece
	 */
	private long getPiecePrice() {
		return getPrice() / getItemCount();
	}

	/**
	 * @return name of the item
	 */
	private String getItemName() {
		return item.getItemName();
	}

	/**
	 * Default sorting: using itemUniqueId
	 */
	@Override
	public int compareTo(BrokerItem o) {
		return itemUniqueId > o.getItemUniqueId() ? 1 : -1;
	}

	/**
	 * Sorting using price of item
	 */
	static Comparator<BrokerItem> NAME_SORT_ASC = new Comparator<BrokerItem>() {

		@Override
		public int compare(BrokerItem o1, BrokerItem o2) {
			if (o1 == null || o2 == null) {
				return comparePossiblyNull(o1, o2);
			}
			return o1.getItemName().compareTo(o2.getItemName());
		}
	};
	static Comparator<BrokerItem> NAME_SORT_DESC = new Comparator<BrokerItem>() {

		@Override
		public int compare(BrokerItem o1, BrokerItem o2) {
			if (o1 == null || o2 == null) {
				return comparePossiblyNull(o1, o2);
			}
			return o1.getItemName().compareTo(o2.getItemName());
		}
	};
	/**
	 * Sorting using price of item
	 */
	static Comparator<BrokerItem> PRICE_SORT_ASC = new Comparator<BrokerItem>() {

		@Override
		public int compare(BrokerItem o1, BrokerItem o2) {
			if (o1 == null || o2 == null) {
				return comparePossiblyNull(o1, o2);
			}
			if (o1.getPrice() == o2.getPrice()) {
				return 0;
			}
			return o1.getPrice() > o2.getPrice() ? 1 : -1;
		}
	};
	static Comparator<BrokerItem> PRICE_SORT_DESC = new Comparator<BrokerItem>() {

		@Override
		public int compare(BrokerItem o1, BrokerItem o2) {
			if (o1 == null || o2 == null) {
				return comparePossiblyNull(o1, o2);
			}
			if (o1.getPrice() == o2.getPrice()) {
				return 0;
			}
			return o1.getPrice() > o2.getPrice() ? -1 : 1;
		}
	};
	/**
	 * Sorting using piece price of item
	 */
	static Comparator<BrokerItem> PIECE_PRICE_SORT_ASC = new Comparator<BrokerItem>() {

		@Override
		public int compare(BrokerItem o1, BrokerItem o2) {
			if (o1 == null || o2 == null) {
				return comparePossiblyNull(o1, o2);
			}
			if (o1.getPiecePrice() == o2.getPiecePrice()) {
				return 0;
			}
			return o1.getPiecePrice() > o2.getPiecePrice() ? 1 : -1;
		}
	};
	static Comparator<BrokerItem> PIECE_PRICE_SORT_DESC = new Comparator<BrokerItem>() {

		@Override
		public int compare(BrokerItem o1, BrokerItem o2) {
			if (o1 == null || o2 == null) {
				return comparePossiblyNull(o1, o2);
			}
			if (o1.getPiecePrice() == o2.getPiecePrice()) {
				return 0;
			}
			return o1.getPiecePrice() > o2.getPiecePrice() ? -1 : 1;
		}
	};
	/**
	 * Sorting using level of item
	 */
	static Comparator<BrokerItem> LEVEL_SORT_ASC = new Comparator<BrokerItem>() {

		@Override
		public int compare(BrokerItem o1, BrokerItem o2) {
			if (o1 == null || o2 == null) {
				return comparePossiblyNull(o1, o2);
			}
			if (o1.getItemLevel() == o2.getItemLevel()) {
				return 0;
			}
			return o1.getItemLevel() > o2.getItemLevel() ? 1 : -1;
		}
	};
	static Comparator<BrokerItem> LEVEL_SORT_DESC = new Comparator<BrokerItem>() {

		@Override
		public int compare(BrokerItem o1, BrokerItem o2) {
			if (o1 == null || o2 == null) {
				return comparePossiblyNull(o1, o2);
			}
			if (o1.getItemLevel() == o2.getItemLevel()) {
				return 0;
			}
			return o1.getItemLevel() > o2.getItemLevel() ? -1 : 1;
		}
	};

	private static <T extends Comparable<T>> int comparePossiblyNull(T aThis, T aThat) {
		int result = 0;
		if (aThis == null && aThat != null) {
			result = -1;
		}
		else if (aThis != null && aThat == null) {
			result = 1;
		}
		return result;
	}

	/**
	 * 1 - by name;<br>
	 * 2 - by level;<br>
	 * 4 - by totalPrice;<br>
	 * 6 - by price for piece (Math.round(item.getPrice() / item.getItemCount))<br>
	 *
	 * @param sortType
	 * @return
	 */
	public static Comparator<BrokerItem> getComparatoryByType(int sortType) {
		switch (sortType) {
			case 0:
				return NAME_SORT_ASC;
			case 1:
				return NAME_SORT_DESC;
			case 2:
				return LEVEL_SORT_ASC;
			case 3:
				return LEVEL_SORT_DESC;
			case 4:
				return PRICE_SORT_ASC;
			case 5:
				return PRICE_SORT_DESC;
			case 6:
				return PIECE_PRICE_SORT_ASC;
			case 7:
				return PIECE_PRICE_SORT_DESC;
			default:
				throw new IllegalArgumentException("Illegal sort type for broker items");
		}
	}
}
