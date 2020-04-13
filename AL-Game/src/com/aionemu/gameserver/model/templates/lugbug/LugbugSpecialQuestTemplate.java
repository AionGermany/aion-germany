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
package com.aionemu.gameserver.model.templates.lugbug;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.model.Race;

/**
 * @author Falke_34
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "lugbug_special_quest")
public class LugbugSpecialQuestTemplate {

	@XmlAttribute(name = "id", required = true)
	protected int id;

	@XmlAttribute(name = "name")
	protected String name;

	@XmlAttribute(name = "title")
	protected String title;

	@XmlAttribute(name = "name_id")
	protected int nameId;

	@XmlAttribute(name = "type")
	private LugbugQuestType lugbugQuestType;

	@XmlAttribute
	private Race race = Race.PC_ALL;

	@XmlAttribute(name = "minlevel")
	protected int minlevel;

	@XmlAttribute(name = "maxlevel")
	protected int maxlevel;

	@XmlAttribute(name = "completecount")
	protected int completecount;

	public int getId() {
		return this.id;
	}

	public String getName() {
		return name;
	}

	public String getTitle() {
		return this.title;
	}

	public int getNameId() {
		return this.nameId;
	}

	public LugbugQuestType getLugbugQuestType() {
		return lugbugQuestType;
	}

	public Race getRace() {
		return race;
	}

	public int getMinlevel() {
		return minlevel;
	}

	public int getMaxlevel() {
		return maxlevel;
	}

	public int getCompletecount() {
		return this.completecount;
	}
}
