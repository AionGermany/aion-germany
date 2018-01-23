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
package com.aionemu.gameserver.model.templates.event;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.aionemu.gameserver.model.AttendType;

/**
 * @author Falke_34
 */
@XmlRootElement(name = "atreian_passport")
@XmlAccessorType(XmlAccessType.FIELD)
public class AtreianPassport {

	@XmlAttribute(name = "active", required = true)
	private int active;
	@XmlAttribute(name = "attend_type", required = true)
	private AttendType attendType;
	@XmlAttribute(name = "id", required = true)
	private int id;
	@XmlAttribute(name = "name")
	private String name = "";
    protected List<AtreianPassportRewards> atreian_passport_reward;

	public int getActive() {
		return active;
	}

	public AttendType getAttendType() {
		return attendType;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

    public List<AtreianPassportRewards> getAtreianPassportRewards() {
        if (atreian_passport_reward == null) {
            atreian_passport_reward = new ArrayList<AtreianPassportRewards>();
        }
        return atreian_passport_reward;
    }
}
