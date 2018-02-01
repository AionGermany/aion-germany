package com.aionemu.gameserver.model.templates.minion;

import java.util.Arrays;
/**
 * 
 * @author jacjozs
 * this copy Pet_Bag
 */
public class MinionDopingBag
{
	private int[] itemBag = null;
	private boolean isDirty = false;
    
	public void setFoodItem(int itemId) {
		setItem(itemId, 0);
	}

	public int getFoodItem() {
		if (itemBag == null || itemBag.length < 1) {
			return 0;
		}
		return itemBag[0];
	}

	public void setDrinkItem(int itemId) {
		setItem(itemId, 1);
	}

	public int getDrinkItem() {
		if (itemBag == null || itemBag.length < 2) {
			return 0;
		}
		return itemBag[1];
	}

	/**
	 * Adds or removes item to the bag
	 *
	 * @param itemId
	 *            - item Id, or 0 to remove
	 * @param slot
	 *            - slot number; 0 for food, 1 for drink, the rest are for scrolls
	 */
	public void setItem(int itemId, int slot) {
		if (itemBag == null) {
			itemBag = new int[slot + 1];
			isDirty = true;
		}
		else if (slot > itemBag.length - 1) {
			itemBag = Arrays.copyOf(itemBag, slot + 1);
			isDirty = true;
		}
		if (itemBag[slot] != itemId) {
			itemBag[slot] = itemId;
			isDirty = true;
		}
	}

	public int[] getScrollsUsed() {
		if (itemBag == null || itemBag.length < 3) {
			return new int[0];
		}
		return Arrays.copyOfRange(itemBag, 2, itemBag.length);
	}

	/**
	 * @return true if the bag needs saving
	 */
	public boolean isDirty() {
		return isDirty;
	}
}