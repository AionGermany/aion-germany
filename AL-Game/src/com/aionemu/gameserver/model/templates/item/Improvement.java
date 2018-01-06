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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Rolandas
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Improvement")
public class Improvement {

	@XmlAttribute(name = "way", required = true)
	private int way;
	@XmlAttribute(name = "price2")
	private int price2;
	@XmlAttribute(name = "price1")
	private int price1;
	@XmlAttribute(name = "burn_defend")
	private int burnDefend;
	@XmlAttribute(name = "burn_attack")
	private int burnAttack;
	@XmlAttribute(name = "level")
	private int level;

	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @return the way
	 */
	public int getChargeWay() {
		return way;
	}

	/**
	 * @return the price1
	 */
	public int getPrice1() {
		return price1;
	}

	/**
	 * @return the price2
	 */
	public int getPrice2() {
		return price2;
	}

	public int getBurnAttack() {
		return burnAttack;
	}

	public int getBurnDefend() {
		return burnDefend;
	}
}
