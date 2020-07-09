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
@XmlType(name = "lugbug_quest")
public class LugbugQuestTemplate {

	@XmlElement(name = "rewards")
	protected List<LugbugQuestRewards> rewards;

	@XmlAttribute(name = "quest_id", required = true)
	protected int questId;

	@XmlAttribute(name = "name", required = true)
	protected String name;

	@XmlAttribute(name = "name_id")
	protected int nameId;

	@XmlAttribute(name = "maxprogress")
	protected int maxprogress;

	@XmlAttribute(name = "completecond")
	protected int completecond;

	public List<LugbugQuestRewards> getRewards() {
		if (rewards == null) {
			rewards = new ArrayList<LugbugQuestRewards>();
		}
		return rewards;
	}

	public int getQuestId() {
		return this.questId;
	}

	public String getName() {
		return name;
	}

	public int getNameId() {
		return nameId;
	}

	public int getMaxprogress() {
		return maxprogress;
	}

	public int getCompletecond() {
		return completecond;
	}
}
