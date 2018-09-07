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
package com.aionemu.gameserver.skillengine.condition;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.model.stats.calc.Stat2;
import com.aionemu.gameserver.model.stats.calc.functions.IStatFunction;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.skillengine.model.Skill;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Conditions", propOrder = { "conditions" })
public class Conditions {

	@XmlElements({ @XmlElement(name = "abnormal", type = AbnormalStateCondition.class), @XmlElement(name = "target", type = TargetCondition.class), @XmlElement(name = "mp", type = MpCondition.class), @XmlElement(name = "hp", type = HpCondition.class), @XmlElement(name = "dp", type = DpCondition.class), @XmlElement(name = "move_casting", type = PlayerMovedCondition.class), @XmlElement(name = "arrowcheck", type = ArrowCheckCondition.class), @XmlElement(name = "onfly", type = OnFlyCondition.class), @XmlElement(name = "weapon", type = WeaponCondition.class), @XmlElement(name = "noflying", type = NoFlyingCondition.class), @XmlElement(name = "lefthandweapon", type = LeftHandCondition.class), @XmlElement(name = "charge", type = ItemChargeCondition.class), @XmlElement(name = "chargeweapon", type = ChargeWeaponCondition.class), @XmlElement(name = "chargearmor", type = ChargeArmorCondition.class), @XmlElement(name = "polishchargeweapon", type = PolishChargeCondition.class), @XmlElement(name = "skillcharge", type = SkillChargeCondition.class), @XmlElement(name = "targetflying", type = TargetFlyingCondition.class), @XmlElement(name = "selfflying", type = SelfFlyingCondition.class),
		@XmlElement(name = "combatcheck", type = CombatCheckCondition.class), @XmlElement(name = "chain", type = ChainCondition.class), @XmlElement(name = "front", type = FrontCondition.class), @XmlElement(name = "back", type = BackCondition.class), @XmlElement(name = "form", type = FormCondition.class), @XmlElement(name = "robotcheck", type = RobotCheckCondition.class) })
	protected List<Condition> conditions;

	/**
	 * Gets the value of the conditions property.
	 * <p/>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to the returned list will be present inside the JAXB object. This is why there is
	 * not a <CODE>set</CODE> method for the conditions property.
	 * <p/>
	 * For example, to add a new item, do as follows:
	 * <p/>
	 * 
	 * <pre>
	 * getConditions().add(newItem);
	 * </pre>
	 */
	public List<Condition> getConditions() {
		if (conditions == null) {
			conditions = new ArrayList<Condition>();
		}
		return this.conditions;
	}

	public boolean validate(Skill skill) {
		if (conditions != null) {
			for (Condition condition : getConditions()) {
				if (!condition.validate(skill)) {
					return false;
				}
			}
		}
		return true;
	}

	public boolean validate(Stat2 stat, IStatFunction statFunction) {
		if (conditions != null) {
			for (Condition condition : getConditions()) {
				if (!condition.validate(stat, statFunction)) {
					return false;
				}
			}
		}
		return true;
	}

	public boolean validate(Effect effect) {
		if (conditions != null) {
			for (Condition condition : getConditions()) {
				if (!condition.validate(effect)) {
					return false;
				}
			}
		}
		return true;
	}
}
