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

package com.aionemu.gameserver.model.npcdrops;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Falke_34
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "drop")
public class XmlDrop {

	@XmlAttribute(name = "item_id", required = true)
	protected int itemId;
	@XmlAttribute(name = "min_amount", required = true)
	protected int minAmount;
	@XmlAttribute(name = "max_amount", required = true)
	protected int maxAmount;
	@XmlAttribute(required = true)
	protected float chance;
	@XmlAttribute(name = "no_reduce")
	protected boolean noReduce = false;
	@XmlAttribute(name = "eachmember")
	protected boolean eachMember = false;
	@XmlAttribute(name = "name", required = false)
	protected String name;

	public int getItemId() {
		return this.itemId;
	}

	public int getMinAmount() {
		return this.minAmount;
	}

	public int getMaxAmount() {
		return this.maxAmount;
	}

	public float getChance() {
		return this.chance;
	}

	public boolean isNoReduction() {
		return this.noReduce;
	}

	public boolean isEachMember() {
		return this.eachMember;
	}

	public String getName() {
		return this.name;
	}
}
