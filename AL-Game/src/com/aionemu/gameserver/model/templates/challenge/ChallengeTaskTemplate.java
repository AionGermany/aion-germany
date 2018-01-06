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
package com.aionemu.gameserver.model.templates.challenge;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.model.Race;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ChallengeTask", propOrder = { "quest", "contrib", "reward" })
public class ChallengeTaskTemplate {

	@XmlElement(required = true)
	protected List<ChallengeQuestTemplate> quest;
	protected List<ContributionReward> contrib;
	@XmlElement(required = true)
	protected ChallengeReward reward;
	@XmlAttribute
	protected Boolean repeat;
	@XmlAttribute(name = "town_residence")
	protected Boolean townResidence;
	@XmlAttribute(name = "name_id")
	protected Integer nameId;
	@XmlAttribute(name = "max_level", required = true)
	protected int maxLevel;
	@XmlAttribute(name = "min_level", required = true)
	protected int minLevel;
	@XmlAttribute(name = "prev_task")
	protected Integer prevTask;
	@XmlAttribute(required = true)
	protected Race race;
	@XmlAttribute(required = true)
	protected ChallengeType type;
	@XmlAttribute(required = true)
	protected int id;

	public List<ChallengeQuestTemplate> getQuests() {
		return this.quest;
	}

	public List<ContributionReward> getContrib() {
		return this.contrib;
	}

	public ChallengeReward getReward() {
		return this.reward;
	}

	public boolean isRepeatable() {
		return this.repeat != null && this.repeat == true;
	}

	public boolean isTownResidence() {
		return this.townResidence != null && this.townResidence == true;
	}

	public Integer getNameId() {
		return this.nameId;
	}

	public int getMaxLevel() {
		return this.maxLevel;
	}

	public int getMinLevel() {
		return this.minLevel;
	}

	public Integer getPrevTask() {
		return this.prevTask;
	}

	public Race getRace() {
		return this.race;
	}

	public ChallengeType getType() {
		return this.type;
	}

	public int getId() {
		return this.id;
	}
}
