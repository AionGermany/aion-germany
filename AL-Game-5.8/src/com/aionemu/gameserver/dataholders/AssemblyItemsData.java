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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.model.templates.item.AssemblyItem;

/**
 * @author xTz
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "item" })
@XmlRootElement(name = "assembly_items")
public class AssemblyItemsData {

	@XmlElement(required = true)
	protected List<AssemblyItem> item;
	@XmlTransient
	private List<AssemblyItem> items = new ArrayList<AssemblyItem>();

	/**
	 * @param unmarshaller
	 * @param parent
	 */
	void afterUnmarshal(Unmarshaller unmarshaller, Object parent) {
		for (AssemblyItem template : item) {
			items.add(template);
		}
	}

	public int size() {
		return items.size();
	}

	public AssemblyItem getAssemblyItem(int itemId) {
		for (AssemblyItem assemblyItem : items) {
			if (assemblyItem.getId() == itemId) {
				return assemblyItem;
			}
		}
		return null;
	}
}
