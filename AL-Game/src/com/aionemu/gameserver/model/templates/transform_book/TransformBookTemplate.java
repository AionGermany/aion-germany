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
package com.aionemu.gameserver.model.templates.transform_book;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "transform_book_template")
@XmlAccessorType(XmlAccessType.NONE)
public class TransformBookTemplate {

	@XmlAttribute(name = "id", required = true)
	private int id;
	@XmlAttribute(name = "name")
	private String name;
	@XmlAttribute(name = "grade", required = true)
	private int grade;
	@XmlAttribute(name = "skill_id", required = true)
	private int skillId;
	@XmlAttribute(name = "compose_value")
	private int composeValue;

	public TransformBookTemplate() {
		this.name = "";
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getGrade() {
		return grade;
	}

	public int getSkillId() {
		return skillId;
	}

	public int getComposeValue() {
		return composeValue;
	}
}
