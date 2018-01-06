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

/**
 * @author orz
 */
@XmlRootElement(name = "teleloc_template")
@XmlAccessorType(XmlAccessType.NONE)
public class TelelocationTemplate {

	/**
	 * Location Id.
	 */
	@XmlAttribute(name = "loc_id", required = true)
	private int locId;
	@XmlAttribute(name = "mapid", required = true)
	private int mapid = 0;
	/**
	 * location name.
	 */
	@XmlAttribute(name = "name", required = true)
	private String name = "";
	@XmlAttribute(name = "name_id", required = true)
	private int nameId;
	@XmlAttribute(name = "posX")
	private float x = 0;
	@XmlAttribute(name = "posY")
	private float y = 0;
	@XmlAttribute(name = "posZ")
	private float z = 0;
	@XmlAttribute(name = "heading")
	private int heading = 0;

	public int getLocId() {
		return locId;
	}

	public int getMapId() {
		return mapid;
	}

	public String getName() {
		return name;
	}

	public int getNameId() {
		return nameId;
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
}
