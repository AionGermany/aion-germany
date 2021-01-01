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

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(value = XmlAccessType.FIELD)
@XmlType(name = "ActionRewards")
public class ActionRewards {

	@XmlAttribute(name = "gold")
	protected Integer gold;
	@XmlAttribute(name = "exp")
	protected Integer exp;
	@XmlAttribute(name = "ap")
	protected Integer ap;
	@XmlAttribute(name = "gp")
	protected Integer gp;
	@XmlElement(name = "reward_item")
	protected List<ActionsItems> achievementItems;

	public Integer getGold() {
		return gold;
	}

	public Integer getAp() {
		return ap;
	}

	public Integer getExp() {
		return exp;
	}

	public Integer getGp() {
		return gp;
	}

	public List<ActionsItems> getAchievementItems() {
		return achievementItems;
	}
}
