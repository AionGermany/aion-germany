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
package com.aionemu.gameserver.dataholders;

import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.skillengine.model.ChargeSkillEntry;

import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * @author Rolandas
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "chargeSkills" })
@XmlRootElement(name = "skill_charge")
public class SkillChargeData {

	@XmlElement(name = "charge", required = true)
	protected List<ChargeSkillEntry> chargeSkills;
	@XmlTransient
	private TIntObjectHashMap<ChargeSkillEntry> skillChargeData = new TIntObjectHashMap<ChargeSkillEntry>();

	void afterUnmarshal(Unmarshaller u, Object parent) {
		for (ChargeSkillEntry chargeSkill : chargeSkills) {
			skillChargeData.put(chargeSkill.getId(), chargeSkill);
		}
		chargeSkills.clear();
		chargeSkills = null;
	}

	public ChargeSkillEntry getChargedSkillEntry(int chargeId) {
		return skillChargeData.get(chargeId);
	}

	public int size() {
		return skillChargeData.size();
	}
}
