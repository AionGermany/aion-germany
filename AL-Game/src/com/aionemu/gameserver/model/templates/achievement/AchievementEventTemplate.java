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
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

import org.joda.time.DateTime;

import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.achievement.AchievementType;
import com.aionemu.gameserver.utils.gametime.DateTimeUtil;

@XmlAccessorType(value = XmlAccessType.FIELD)
@XmlType(name = "achievement_event_template")
public class AchievementEventTemplate {

	@XmlElement(name = "event_item")
	protected AchievementEventRewards rewards;
	@XmlAttribute(name = "id", required = true)
	protected int id;
	@XmlAttribute(name = "name")
	protected String name;
	@XmlAttribute(name = "event_section")
	protected int section;
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
	@XmlAttribute(name = "active")
	protected Boolean active;
	@XmlAttribute(name = "complete_point")
	protected int completePoint;
	@XmlAttribute(name = "action_id")
	protected int actionId;
	@XmlAttribute(name = "start", required = true)
	@XmlSchemaType(name = "dateTime")
	protected XMLGregorianCalendar startDate;
	@XmlAttribute(name = "end", required = true)
	@XmlSchemaType(name = "dateTime")
	protected XMLGregorianCalendar endDate;

	public int getId() {
		return id;
	}

	public AchievementEventRewards getRewards() {
		return rewards;
	}

	public AchievementRepeat getRepeat() {
		return repeat;
	}

	public AchievementType getType() {
		return type;
	}

	public Boolean getActive() {
		return active;
	}

	public int getActionId() {
		return actionId;
	}

	public int getCompletePoint() {
		return completePoint;
	}

	public int getSection() {
		return section;
	}

	public Integer getMaxlevel() {
		return maxlevel;
	}

	public Integer getMinlevel() {
		return minlevel;
	}

	public Race getRace() {
		return race;
	}

	public String getName() {
		return name;
	}

	public DateTime getStartDate() {
		return DateTimeUtil.getDateTime(startDate.toGregorianCalendar());
	}

	public DateTime getEndDate() {
		return DateTimeUtil.getDateTime(endDate.toGregorianCalendar());
	}
}
