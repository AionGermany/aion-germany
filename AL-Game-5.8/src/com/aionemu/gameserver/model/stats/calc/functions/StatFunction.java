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
package com.aionemu.gameserver.model.stats.calc.functions;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.model.stats.calc.Stat2;
import com.aionemu.gameserver.model.stats.calc.StatOwner;
import com.aionemu.gameserver.model.stats.container.StatEnum;
import com.aionemu.gameserver.skillengine.condition.Conditions;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SimpleModifier")
public class StatFunction implements IStatFunction {

	@XmlAttribute(name = "name")
	protected StatEnum stat;
	@XmlAttribute
	private boolean bonus;
	@XmlAttribute
	protected int value;
    @XmlAttribute(name = "class_type")
    protected String classType;
	@XmlElement(name = "conditions")
	private Conditions conditions;

	public StatFunction() {
	}

	public StatFunction(StatEnum stat, int value, boolean bonus) {
		this.stat = stat;
		this.value = value;
		this.bonus = bonus;
	}

	@Override
	public int compareTo(IStatFunction o) {
		int result = getPriority() - o.getPriority();
		if (result == 0) {
			return this.hashCode() - o.hashCode();
		}
		return result;
	}

    public String getClassType() {
        return this.classType;
    }

	@Override
	public StatOwner getOwner() {
		return null;
	}

	@Override
	public final StatEnum getName() {
		return stat;
	}

	@Override
	public final boolean isBonus() {
		return bonus;
	}

	@Override
	public int getPriority() {
		return 0x10;
	}

	@Override
	public int getValue() {
		return value;
	}

	@Override
	public boolean validate(Stat2 stat, IStatFunction statFunction) {
		return conditions != null ? conditions.validate(stat, statFunction) : true;
	}

	@Override
	public void apply(Stat2 stat) {
	}

	@Override
	public String toString() {
		return this.getClass().getName() + " [stat=" + stat + ", bonus=" + bonus + ", value=" + value + ", priority=" + getPriority() + "]";
	}

	public StatFunction withConditions(Conditions conditions) {
		this.conditions = conditions;
		return this;
	}

	@Override
	public boolean hasConditions() {
		return conditions != null;
	}

	/**
	 * Creates a final list of modifiers combining bonuses with random bonuses
	 * 
	 * @param modifiers
	 *            - can be null if do not exist
	 * @param rndBonuses
	 *            - can be null if do not exist
	 * @return a list of modifiers, empty if none
	 */
	public static List<StatFunction> mergeRandomBonuses(List<StatFunction> modifiers, List<StatFunction> rndBonuses) {
		if (modifiers == null)
			modifiers = new ArrayList<StatFunction>();

		if (rndBonuses == null)
			return modifiers;

		List<StatFunction> allModifiers = new ArrayList<StatFunction>();
		EnumSet<StatEnum> rndNames = EnumSet.noneOf(StatEnum.class);

		for (IStatFunction func : rndBonuses)
			rndNames.add(func.getName());

		// add values to original stats
		for (StatFunction modifier : modifiers) {
			if (!rndNames.contains(modifier.getName()) || !modifier.isBonus() || modifier.hasConditions()) {
				allModifiers.add(modifier);
				continue;
			}

			IStatFunction rndBonus = null;
			for (IStatFunction func : rndBonuses) {
				if (func.getName() == modifier.getName()) {
					rndBonus = func;
					rndNames.remove(func.getName());
					break;
				}
			}

			int finalValue = modifier.getValue() + rndBonus.getValue();

			if (modifier instanceof StatAddFunction) {
				if (finalValue != 0)
					allModifiers.add(new StatAddFunction(modifier.getName(), finalValue, true));
			}
			else if (modifier instanceof StatRateFunction) {
				if (finalValue != 0)
					allModifiers.add(new StatRateFunction(modifier.getName(), finalValue, true));
			}
			else
				allModifiers.add(modifier);
		}

		// add new stat values
		for (StatFunction modifier : rndBonuses) {
			if (rndNames.contains(modifier.getName()))
				allModifiers.add(modifier);
		}

		return allModifiers;
	}

	@Override
	public int getRandomNumber() {
		// TODO Auto-generated method stub
		return 0;
	}

}
