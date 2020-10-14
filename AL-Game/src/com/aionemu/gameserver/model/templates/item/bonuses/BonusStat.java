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
package com.aionemu.gameserver.model.templates.item.bonuses;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.model.stats.container.StatEnum;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BonusStat")
public class BonusStat {

	@XmlAttribute(name = "name", required = true)
	protected StatEnum name;
	@XmlAttribute(name = "min_value", required = true)
	protected int min;
	@XmlAttribute(name = "max_value", required = true)
	protected int max;

	public StatEnum getName() {
		return name;
	}

	public int getMin() {
		return min;
	}

	public int getMax() {
		return max;
	}
}
