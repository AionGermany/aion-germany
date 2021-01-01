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
package com.aionemu.gameserver.model.templates.achievement;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(value = XmlAccessType.FIELD)
@XmlType(name = "achievement_action_template")
public class AchievementActionTemplate {

	@XmlElement(name = "required")
	protected AchievementRequired required;
	@XmlElement(name = "rewards")
	protected ActionRewards rewards;
	@XmlAttribute(name = "id", required = true)
	protected int id;
	@XmlAttribute(name = "name")
	protected String name;
	@XmlAttribute(name = "title")
	protected Integer title;
	@XmlAttribute(name = "type")
	protected AchievementActionType type;
	@XmlAttribute(name = "maxvalue")
	protected Integer maxvalue;

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Integer getTitle() {
		return title;
	}

	public ActionRewards getRewards() {
		return rewards;
	}

	public AchievementRequired getRequired() {
		return required;
	}

	public AchievementActionType getType() {
		return type;
	}

	public Integer getMaxvalue() {
		return maxvalue;
	}
}
