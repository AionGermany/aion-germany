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
package com.aionemu.gameserver.model.templates.minion;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MinionAttribute", propOrder = { "physicalAttr" })
public class MinionAttribute {

	@XmlElement(name = "physical_attr")
	protected List<MinionAttr> physicalAttr;
	@XmlElement(name = "magical_attr")
	protected List<MinionAttr> magicalAttr;

	public List<MinionAttr> getPyhsicalAttr() {
		if (this.physicalAttr == null) {
			this.physicalAttr = new ArrayList<MinionAttr>();
		}
		return this.physicalAttr;
	}

	public List<MinionAttr> getMagicalAttr() {
		if (this.magicalAttr == null) {
			this.magicalAttr = new ArrayList<MinionAttr>();
		}
		return this.magicalAttr;
	}
}
