/**
 * This file is part of Aion-Lightning <aion-lightning.org>.
 * <p/>
 * Aion-Lightning is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * Aion-Lightning is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details. *
 * You should have received a copy of the GNU General Public License
 * along with Aion-Lightning.
 * If not, see <http://www.gnu.org/licenses/>.
 */

package com.aionemu.gameserver.model.templates.pet;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.model.stats.container.StatEnum;
import com.aionemu.gameserver.skillengine.change.Func;

/**
 * Created by Ace on 01/08/2016.
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PetPenaltyAttr")
public class PetPenaltyAttr {

	@XmlAttribute(required = true)
	protected StatEnum stat;

	@XmlAttribute(required = true)
	protected Func func;

	@XmlAttribute(required = true)
	protected int value;

	public StatEnum getStat() {
		return stat;
	}

	public void setStat(StatEnum value) {
		this.stat = value;
	}

	public Func getFunc() {
		return func;
	}

	public void setFunc(Func value) {
		this.func = value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
