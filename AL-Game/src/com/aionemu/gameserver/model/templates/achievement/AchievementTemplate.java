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

import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.achievement.AchievementType;

@XmlAccessorType(value = XmlAccessType.FIELD)
@XmlType(name = "achievement_template")
public class AchievementTemplate {

	@XmlElement(name = "rewards")
	protected AchievementRewards rewards;
	@XmlElement(name = "actions")
	protected AchievementAction actions;
	@XmlAttribute(name = "id", required = true)
	protected int id;
	@XmlAttribute(name = "name")
	protected String name;
	@XmlAttribute(name = "title")
	protected Integer title;
	@XmlAttribute(name = "type")
	protected AchievementType type;
	@XmlAttribute(name = "repeat")
	protected AchievementRepeat repeat;
	@XmlAttribute(name = "race")
	protected Race race;
	@XmlAttribute(name = "minlevel")
	protected Integer minlevel;
	@XmlAttribute(name = "maxlevel")
	protected Integer maxlevel;
	@XmlAttribute(name = "completecount")
	protected Integer completecount;

	public AchievementRewards getRewards() {
		return rewards;
	}

	public AchievementAction getActions() {
		return actions;
	}

	public AchievementRepeat getRepeat() {
		return repeat;
	}

	public AchievementType getType() {
		return type;
	}

	public int getId() {
		return id;
	}

	public Integer getCompletecount() {
		return completecount;
	}

	public Integer getMaxlevel() {
		return maxlevel;
	}

	public Integer getMinlevel() {
		return minlevel;
	}

	public Integer getTitle() {
		return title;
	}

	public Race getRace() {
		return race;
	}

	public String getName() {
		return name;
	}
}
