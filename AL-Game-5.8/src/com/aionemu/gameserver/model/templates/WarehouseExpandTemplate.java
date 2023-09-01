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
package com.aionemu.gameserver.model.templates;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.aionemu.gameserver.model.templates.expand.Expand;
import com.aionemu.gameserver.utils.Util;

/**
 * @author Simple
 */
@XmlRootElement(name = "warehouse_npc")
@XmlAccessorType(XmlAccessType.FIELD)
public class WarehouseExpandTemplate {

	@XmlElement(name = "expand", required = true)
	protected List<Expand> warehouseExpands;
	/**
	 * NPC ID
	 */
	@XmlAttribute(name = "id", required = true)
	protected int id;
	/**
	 * NPC name
	 */
	@XmlAttribute(name = "name", required = true)
	protected String name = "";

	public int getNpcId() {
		return id;
	}

	/**
	 * Gets the value of the material property.
	 */
	public List<Expand> getWarehouseExpand() {
		return this.warehouseExpands;
	}

	/**
	 * Gets the value of the name property.
	 *
	 * @return possible object is {@link String }
	 */
	public String getName() {
		return Util.convertName(name);
	}

	/**
	 * Returns true if list contains level
	 *
	 * @return true or false
	 */
	public boolean contains(int level) {
		for (Expand expand : warehouseExpands) {
			if (expand.getLevel() == level) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns true if list contains level
	 *
	 * @return expand
	 */
	public Expand get(int level) {
		for (Expand expand : warehouseExpands) {
			if (expand.getLevel() == level) {
				return expand;
			}
		}
		return null;
	}
}
