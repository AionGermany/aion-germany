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
package com.aionemu.gameserver.model.templates.creativity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author FrozenKiller
 */
@XmlRootElement(name = "creativity_template")
@XmlAccessorType(XmlAccessType.NONE)
public class CreativityTemplate {

	@XmlAttribute(name = "id", required = true)
	private int Id;

	@XmlAttribute(name = "name")
	private String name = "";

	@XmlAttribute(name = "skillid", required = true)
	private int skillId;

	@XmlAttribute(name = "category")
	private String category = "";

	@XmlAttribute(name = "maxuse")
	private int maxUse;

	@XmlAttribute(name = "minlevel")
	private int minLevel;

	public int getCreativityId() {
		return Id;
	}

	public String getName() {
		return name;
	}

	public int getSkillId() {
		return skillId;
	}

	public String getCategory() {
		return category;
	}

	public int getmaxUse() {
		return maxUse;
	}

	public int getminLevel() {
		return minLevel;
	}
}
