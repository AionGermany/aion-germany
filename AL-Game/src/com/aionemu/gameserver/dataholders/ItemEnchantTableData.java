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
import javax.xml.bind.annotation.XmlTransient;

import com.aionemu.gameserver.model.templates.item.ArmorType;
import com.aionemu.gameserver.model.templates.item.ItemCategory;
import com.aionemu.gameserver.model.templates.item.ItemEnchantTable;

import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * @author Alcapwnd
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "enchant_tables")
public class ItemEnchantTableData {

	@XmlElement(name = "enchant_table", required = true)
	protected List<ItemEnchantTable> enchantTables;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@XmlTransient
	private TIntObjectHashMap<ItemEnchantTable> enchants = new TIntObjectHashMap();

	void afterUnmarshal(Unmarshaller u, Object parent) {
		for (ItemEnchantTable it : this.enchantTables) {
			getEnchantMap().put(it.getId(), it);
		}
	}

	private TIntObjectHashMap<ItemEnchantTable> getEnchantMap() {
		return this.enchants;
	}

	public ItemEnchantTable getTableWeapon(ItemCategory cType) {
		for (ItemEnchantTable it : this.enchantTables) {
			if (it.getType().equalsIgnoreCase(cType.toString())) {
				return it;
			}

		}
		return null;
	}

	public ItemEnchantTable getTableArmor(ArmorType aType, ItemCategory cType) {
		for (ItemEnchantTable it : this.enchantTables) {
			if (it.getPart() == null)
				continue;
			else if (aType == ArmorType.NO_ARMOR)
				continue;
			if (it.getType().equalsIgnoreCase(aType.toString()) && it.getPart().equalsIgnoreCase(cType.toString())) {
				return it;
			}

		}
		return null;
	}

	public ItemEnchantTable getTablePlume() {
		for (ItemEnchantTable it : this.enchantTables) {
			if (it.getType() != "PLUME") {
				continue;
			}
			return it;

		}
		return null;
	}

	public ItemEnchantTable getTableAuthorize() {
		for (ItemEnchantTable it : this.enchantTables) {
			if (it.getType() != "AUTHORIZE") {
				continue;
			}
			return it;

		}
		return null;
	}

	public int size() {
		return this.enchants.size();
	}
}
