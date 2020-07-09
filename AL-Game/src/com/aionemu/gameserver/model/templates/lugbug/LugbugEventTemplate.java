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
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

import org.joda.time.DateTime;

import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.utils.gametime.DateTimeUtil;

/**
 * @author Falke_34
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "lugbug_event")
public class LugbugEventTemplate {

	@XmlAttribute(name = "id", required = true)
	protected int id;

	@XmlAttribute(name = "name")
	protected String name;

	@XmlAttribute(name = "quest_id")
	protected int questId;

	@XmlAttribute(name = "event_section")
	protected int eventSection;

	@XmlAttribute(name = "active")
	protected int active;

	@XmlAttribute(name = "start_day", required = true)
	@XmlSchemaType(name = "dateTime")
	protected XMLGregorianCalendar startday;

	@XmlAttribute(name = "end_day", required = true)
	@XmlSchemaType(name = "dateTime")
	protected XMLGregorianCalendar endday;

	@XmlAttribute(name = "event_period")
	protected int eventPeriod;

	@XmlAttribute
	private Race race = Race.PC_ALL;

	@XmlAttribute(name = "minlevel")
	protected int minlevel;

	@XmlAttribute(name = "maxlevel")
	protected int maxlevel;

	@XmlAttribute(name = "maxrewardcount")
	protected int maxrewardcount;

	@XmlAttribute(name = "completepoint")
	protected int completePoint;

	@XmlAttribute(name = "maxstage")
	protected int maxstage;

	public int getId() {
		return this.id;
	}

	public String getName() {
		return name;
	}

	public int getQuestId() {
		return this.questId;
	}

	public int getEventSection() {
		return this.eventSection;
	}

	public int getActive() {
		return this.active;
	}

	public DateTime getStartday() {
		return DateTimeUtil.getDateTime(startday.toGregorianCalendar());
	}

	public DateTime getEndday() {
		return DateTimeUtil.getDateTime(endday.toGregorianCalendar());
	}

	public int getEventPeriod() {
		return this.eventPeriod;
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

	public int getMaxrewardcount() {
		return maxrewardcount;
	}

	public int getCompletePoint() {
		return completePoint;
	}

	public int getMaxstage() {
		return maxstage;
	}
}
