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
package com.aionemu.gameserver.model.templates.housing;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Rolandas
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HousingUseableItem", propOrder = { "action" })
public class HousingUseableItem extends PlaceableHouseObject {

	@XmlElement(required = true)
	protected UseItemAction action;
	@XmlAttribute(required = true)
	protected boolean owner;
	@XmlAttribute
	protected Integer cd;
	@XmlAttribute(required = true)
	protected int delay;
	@XmlAttribute(name = "use_count")
	protected Integer useCount;
	@XmlAttribute(name = "required_item")
	protected Integer requiredItem;

	public UseItemAction getAction() {
		return action;
	}

	/**
	 * Can the object be used only by the owner or visitors too
	 */
	public boolean isOwnerOnly() {
		return owner;
	}

	/**
	 * @return null if no Cooltime is used
	 */
	public Integer getCd() {
		return cd;
	}

	public int getDelay() {
		return delay;
	}

	/**
	 * @return null if use is not restricted
	 */
	public Integer getUseCount() {
		return useCount;
	}

	/**
	 * @return null if no item is required
	 */
	public Integer getRequiredItem() {
		return requiredItem;
	}

	@Override
	public byte getTypeId() {
		return 1;
	}
}
