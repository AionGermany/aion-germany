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
package com.aionemu.gameserver.model.templates.transformation;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Falke_34
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "transformation")
public class TransformationTemplate {

	@XmlAttribute(name = "id", required = true)
	private int id;
	@XmlAttribute(name = "grade", required = true)
	private int grade;
	@XmlAttribute(name = "skill", required = true)
	private int skill;
	@XmlAttribute(name = "name", required = true)
	private String name;
	@XmlAttribute(name = "name_id", required = true)
	private int nameId;

	public int getId() {
		return id;
	}

	public int getGrade() {
		return grade;
	}

	public int getSkill() {
		return skill;
	}

	public String getName() {
		return name;
	}

	public int getNameId() {
		return nameId;
	}
}
