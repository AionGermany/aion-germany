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
package com.aionemu.gameserver.model.templates.revive_start_points;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.model.Race;

/**
 * @author Falke_34
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WorldReviveStartPoints")
public class WorldReviveStartPoints {

	@XmlAttribute(name = "world_id")
	protected int worldId;

	@XmlAttribute(name = "name")
	protected String name;

	@XmlAttribute(name = "race")
	protected Race race = Race.PC_ALL;

	@XmlAttribute(name = "x")
	protected float x;

	@XmlAttribute(name = "y")
	protected float y;

	@XmlAttribute(name = "z")
	protected float z;

	@XmlAttribute(name = "h")
	protected byte h;

	@XmlAttribute(name = "max_level")
	protected int maxLevel;

	@XmlAttribute(name = "min_level")
	protected int minLevel;

	public int getReviveWorld() {
		return worldId;
	}

	public Race getRace() {
		return race;
	}

	public void setRace(Race value) {
		race = value;
	}

	public float getX() {
		return x;
	}

	public void setX(float value) {
		x = value;
	}

	public float getY() {
		return y;
	}

	public void setY(float value) {
		y = value;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float value) {
		z = value;
	}

	public byte getH() {
		return h;
	}

	public void setH(byte value) {
		h = value;
	}

	public int getMaxlevel() {
		return maxLevel;
	}

	public int getMinlevel() {
		return minLevel;
	}
}
