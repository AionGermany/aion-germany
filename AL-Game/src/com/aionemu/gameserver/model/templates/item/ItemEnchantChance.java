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
package com.aionemu.gameserver.model.templates.item;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.model.templates.item.ItemEnchantChanceList;

@XmlType(name = "ItemEnchantChance")
public class ItemEnchantChance {

	@XmlAttribute(name = "id")
	protected int enchantChanceId;
	@XmlAttribute(name = "name")
	protected String name;
	@XmlAttribute(name = "type")
	protected String type;
	@XmlAttribute(name = "quality")
	protected String quality;
	@XmlAttribute(name = "target_quality")
	protected String target_quality;
	@XmlElement(name = "item")
	private List<ItemEnchantChanceList> itemEnchantChanceList;
	
	public final int getId() {
		return enchantChanceId;
	}

	public final String name() {
		return name;
	}
	
	public final String getType() {
		return type;
	}
	
	public final String getQuality() {
		return quality;
	}
	
	public final String getTargetQuality() {
		return target_quality;
	}
	
	public ItemEnchantChanceList getChancesById(int id) {
		if (itemEnchantChanceList != null) {
			return itemEnchantChanceList.get(id);
		}
		return null;
	}

	public List<ItemEnchantChanceList> getItemEnchantChanceList() {
		return itemEnchantChanceList;
	}
}
