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
package com.aionemu.gameserver.questEngine.handlers.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.questEngine.QuestEngine;

/**
 * @author MrPoke, Hilgert
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QuestScriptData")
@XmlSeeAlso({ ReportToData.class, RelicRewardsData.class, CraftingRewardsData.class, ReportToManyData.class, MonsterHuntData.class, ItemCollectingData.class, WorkOrdersData.class, XmlQuestData.class,
	// MentorMonsterHuntData.class,
	ItemOrdersData.class, FountainRewardsData.class, SkillUseData.class })

public abstract class XMLQuest {

	@XmlAttribute(name = "id", required = true)
	protected int id;
	@XmlAttribute(name = "movie", required = false)
	protected int questMovie;
	@XmlAttribute(name = "mission", required = false)
	protected boolean mission;

	/**
	 * Gets the value of the id property.
	 */
	public int getId() {
		return id;
	}

	public int getQuestMovie() {
		return questMovie;
	}

	/**
	 * @return the mission
	 */
	public boolean isMission() {
		return mission;
	}

	/**
	 * @param mission
	 *            the mission to set
	 */
	public void setMission(boolean mission) {
		this.mission = mission;
	}

	public abstract void register(QuestEngine questEngine);
}
