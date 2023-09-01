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
package com.aionemu.gameserver.model.templates.spawns;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Rolandas
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HouseSpawn")
public class HouseSpawn {

	@XmlAttribute(name = "x", required = true)
	protected float x;
	@XmlAttribute(name = "y", required = true)
	protected float y;
	@XmlAttribute(name = "z", required = true)
	protected float z;
	@XmlAttribute(name = "h")
	protected Byte h;
	@XmlAttribute(name = "static_id")
	private int staticId;
	@XmlAttribute(name = "type", required = true)
	protected SpawnType type;

	public float getX() {
		return x;
	}

	public void setX(float value) {
		this.x = value;
	}

	public float getY() {
		return y;
	}

	public void setY(float value) {
		this.y = value;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float value) {
		this.z = value;
	}

	public byte getH() {
		if (h == null) {
			return ((byte) 0);
		}

		return h;
	}

	public void setH(Byte value) {
		this.h = value;
	}

	public SpawnType getType() {
		return type;
	}

	public void setType(SpawnType value) {
		this.type = value;
	}

	public int getStaticId() {
		return staticId;
	}

	public void setStaticId(int staticId) {
		this.staticId = staticId;
	}
}
