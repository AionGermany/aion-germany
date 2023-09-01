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
@XmlRootElement(name = "address")
public class HouseAddress {

	@XmlAttribute(name = "exit_z")
	protected Float exitZ;
	@XmlAttribute(name = "exit_y")
	protected Float exitY;
	@XmlAttribute(name = "exit_x")
	protected Float exitX;
	@XmlAttribute(name = "exit_map")
	protected Integer exitMap;
	@XmlAttribute(required = true)
	protected float z;
	@XmlAttribute(required = true)
	protected float y;
	@XmlAttribute(required = true)
	protected float x;
	@XmlAttribute(name = "town", required = true)
	private int townId;
	@XmlAttribute(required = true)
	protected int map;
	@XmlAttribute(required = true)
	protected int id;

	public Float getExitZ() {
		return exitZ;
	}

	public Float getExitY() {
		return exitY;
	}

	public Float getExitX() {
		return exitX;
	}

	public Integer getExitMapId() {
		return exitMap;
	}

	public float getZ() {
		return z;
	}

	public float getY() {
		return y;
	}

	public float getX() {
		return x;
	}

	public int getMapId() {
		return map;
	}

	public int getId() {
		return id;
	}

	public int getTownId() {
		return townId;
	}
}
