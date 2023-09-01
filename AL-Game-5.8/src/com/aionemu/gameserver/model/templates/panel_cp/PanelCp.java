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
package com.aionemu.gameserver.model.templates.panel_cp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "panel_cp")
@XmlAccessorType(XmlAccessType.NONE)
public class PanelCp {

	@XmlAttribute
	protected int id;
	@XmlAttribute
	protected String name;
	@XmlAttribute(name = "panelType", required = true)
	private PanelCpType panelCpType;
	@XmlAttribute
	protected int learnSkill;
	@XmlAttribute
	protected int additionalSkill;
	@XmlAttribute
	protected int statsId;
	@XmlAttribute
	protected int skillId;
	@XmlAttribute
	protected int statValue;
	@XmlAttribute
	protected int cost;
	@XmlAttribute
	protected int countMax;
	@XmlAttribute
	protected int costAdj;
	@XmlAttribute
	protected int preCondId;
	@XmlAttribute
	protected int preEnchantCount;
	@XmlAttribute
	protected int minLevel;

	public int getId() {
		return this.id;
	}

	public String getName() {
		return name;
	}

	public PanelCpType getPanelCpType() {
		return panelCpType;
	}

	public int getLearnSkill() {
		return learnSkill;
	}

	public int getAdditionalSkill() {
		return additionalSkill;
	}

	public int getStatsId() {
		return statsId;
	}

	public int getSkillId() {
		return skillId;
	}

	public int getStatValue() {
		return statValue;
	}

	public int getCost() {
		return cost;
	}

	public int getCountMax() {
		return countMax;
	}

	public int getCostAdj() {
		return costAdj;
	}

	public int getPreCondId() {
		return preCondId;
	}

	public int getPreEnchantCount() {
		return preEnchantCount;
	}

	public int getMinLevel() {
		return minLevel;
	}
}
