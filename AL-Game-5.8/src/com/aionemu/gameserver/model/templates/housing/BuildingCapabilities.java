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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Rolandas
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "caps")
public class BuildingCapabilities {

	@XmlAttribute(required = true)
	protected boolean addon;
	@XmlAttribute(required = true)
	protected int emblemId;
	@XmlAttribute(required = true)
	protected boolean floor;
	@XmlAttribute(required = true)
	protected boolean room;
	@XmlAttribute(required = true)
	protected int interior;
	@XmlAttribute(required = true)
	protected int exterior;

	public boolean canHaveAddon() {
		return addon;
	}

	public int getEmblemId() {
		return emblemId;
	}

	public boolean canChangeFloor() {
		return floor;
	}

	public boolean canChangeRoom() {
		return room;
	}

	public int canChangeInterior() {
		return interior;
	}

	public int canChangeExterior() {
		return exterior;
	}
}
