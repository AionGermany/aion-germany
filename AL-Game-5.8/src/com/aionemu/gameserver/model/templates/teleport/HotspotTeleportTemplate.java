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
package com.aionemu.gameserver.model.templates.teleport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.aionemu.gameserver.model.Race;

/**
 * @author Alcapwnd
 */
@XmlRootElement(name = "hotspot_template")
@XmlAccessorType(XmlAccessType.NONE)
public class HotspotTeleportTemplate {

	/**
	 * Location Id.
	 */
	@XmlAttribute(name = "id", required = true)
	private int locId;
	/**
	 * location name.
	 */
	@XmlAttribute(name = "name")
	private String name = "";
	@XmlAttribute(name = "mapId", required = true)
	private int mapId;
	@XmlAttribute(name = "posX", required = true)
	private float x = 0;
	@XmlAttribute(name = "posY", required = true)
	private float y = 0;
	@XmlAttribute(name = "posZ", required = true)
	private float z = 0;
	@XmlAttribute(name = "heading")
	private int heading = 0;
	@XmlAttribute(name = "race")
	private Race race;
	@XmlAttribute(name = "kinah")
	private int kinah = 0;
	@XmlAttribute(name = "kinah_dis")
	private float kinah_dis = 0;
	@XmlAttribute(name = "level")
	private int level = 0;

	public int getLocId() {
		return locId;
	}

	public int getMapId() {
		return mapId;
	}

	public String getName() {
		return name;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getZ() {
		return z;
	}

	public int getHeading() {
		return heading;
	}

	public Race getRace() {
		return race;
	}

	public int getKinah() {
		return kinah;
	}

	public float getDisKinah() {
		return kinah_dis;
	}

	public int getLevel() {
		return level;
	}
}
