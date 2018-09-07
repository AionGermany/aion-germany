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

import java.util.Iterator;
import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.aionemu.gameserver.model.templates.teleport.ScrollItem;
import com.aionemu.gameserver.model.templates.teleport.ScrollItemLocationList;

import gnu.trove.map.hash.TIntObjectHashMap;

@XmlRootElement(name = "item_multi_returns")
@XmlAccessorType(XmlAccessType.FIELD)
public class MultiReturnItemData {

	@XmlElement(name = "item")
	private List<ScrollItem> ItemList;
	private TIntObjectHashMap<List<ScrollItemLocationList>> ItemLocationList = new TIntObjectHashMap<List<ScrollItemLocationList>>();

	void afterUnmarshal(Unmarshaller Unmarshaller, Object Object) {
		ItemLocationList.clear();
		Iterator<ScrollItem> Iterator = ItemList.iterator();
		while (Iterator.hasNext()) {
			ScrollItem ScrollItem = Iterator.next();
			ItemLocationList.put(ScrollItem.getId(), ScrollItem.getLocationList());
		}
	}

	public int size() {
		return ItemLocationList.size();
	}

	public ScrollItem getScrollItembyId(int id) {
		Iterator<ScrollItem> Iterator = ItemList.iterator();
		while (Iterator.hasNext()) {
			ScrollItem ScrollItem = Iterator.next();
			if (ScrollItem.getId() == id) {
				return ScrollItem;
			}
		}
		return null;
	}

	public List<ScrollItem> getScrollItems() {
		return ItemList;
	}
}
