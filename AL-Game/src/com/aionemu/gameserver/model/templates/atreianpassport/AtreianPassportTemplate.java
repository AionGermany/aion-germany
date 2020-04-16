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
package com.aionemu.gameserver.model.templates.atreianpassport;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Falke_34
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "atreian_passport")
public class AtreianPassportTemplate {

	@XmlElement(name = "rewards")
	protected List<AtreianPassportRewards> rewards;

	@XmlAttribute(name = "id", required = true)
	protected int id;

	@XmlAttribute(name = "name")
	protected String name;

	@XmlAttribute(name = "active")
	protected int active;

	@XmlAttribute(name = "attend_type", required = true)
	private AttendType attendType;

	public List<AtreianPassportRewards> getRewards() {
		if (rewards == null) {
			rewards = new ArrayList<AtreianPassportRewards>();
		}
		return rewards;
	}

	public int getId() {
		return this.id;
	}

	public String getName() {
		return name;
	}

	public int getActive() {
		return this.active;
	}

	public AttendType getAttendType() {
		return attendType;
	}
}
