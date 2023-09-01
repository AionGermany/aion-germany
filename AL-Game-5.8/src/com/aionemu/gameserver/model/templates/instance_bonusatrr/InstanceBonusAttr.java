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
package com.aionemu.gameserver.model.templates.instance_bonusatrr;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author xTz
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InstanceBonusAttr", propOrder = { "penaltyAttr" })
public class InstanceBonusAttr {

	@XmlElement(name = "penalty_attr")
	protected List<InstancePenaltyAttr> penaltyAttr;
	@XmlAttribute(name = "buff_id", required = true)
	protected int buffId;

	/**
	 * Gets the value of the penaltyAttr property.
	 * <p/>
	 * <p/>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to the returned list will be present inside the JAXB object. This is why there is
	 * not a <CODE>set</CODE> method for the penaltyAttr property.
	 * <p/>
	 * <p/>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getPenaltyAttr().add(newItem);
	 * </pre>
	 * <p/>
	 * <p/>
	 * <p/>
	 * Objects of the following type(s) are allowed in the list {@link InstancePenaltyAttr }
	 */
	public List<InstancePenaltyAttr> getPenaltyAttr() {
		if (penaltyAttr == null) {
			penaltyAttr = new ArrayList<InstancePenaltyAttr>();
		}
		return this.penaltyAttr;
	}

	/**
	 * Gets the value of the buffId property.
	 */
	public int getBuffId() {
		return buffId;
	}

	/**
	 * Sets the value of the buffId property.
	 */
	public void setBuffId(int value) {
		this.buffId = value;
	}
}
