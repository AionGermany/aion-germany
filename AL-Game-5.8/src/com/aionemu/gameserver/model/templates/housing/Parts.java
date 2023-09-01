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
import javax.xml.bind.annotation.XmlType;

/**
 * @author Rolandas
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Parts", propOrder = { "fence", "garden", "frame", "outwall", "roof", "infloor", "inwall", "door" })
public class Parts {

	protected Integer fence;
	protected Integer garden;
	protected Integer frame;
	protected Integer outwall;
	protected Integer roof;
	protected int infloor;
	protected int inwall;
	protected int door;

	/**
	 * Gets the value of the fence property.
	 *
	 * @return possible object is {@link Integer }
	 */
	public Integer getFence() {
		return fence;
	}

	/**
	 * Gets the value of the garden property.
	 *
	 * @return possible object is {@link Integer }
	 */
	public Integer getGarden() {
		return garden;
	}

	/**
	 * Gets the value of the frame property.
	 *
	 * @return possible object is {@link Integer }
	 */
	public Integer getFrame() {
		return frame;
	}

	/**
	 * Gets the value of the outwall property.
	 *
	 * @return possible object is {@link Integer }
	 */
	public Integer getOutwall() {
		return outwall;
	}

	/**
	 * Gets the value of the roof property.
	 *
	 * @return possible object is {@link Integer }
	 */
	public Integer getRoof() {
		return roof;
	}

	/**
	 * Gets the value of the infloor property.
	 */
	public int getInfloor() {
		return infloor;
	}

	/**
	 * Gets the value of the inwall property.
	 */
	public int getInwall() {
		return inwall;
	}

	/**
	 * Gets the value of the door property.
	 */
	public int getDoor() {
		return door;
	}
}
