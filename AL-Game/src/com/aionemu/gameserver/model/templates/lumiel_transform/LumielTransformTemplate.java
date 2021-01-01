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
package com.aionemu.gameserver.model.templates.lumiel_transform;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(value = XmlAccessType.FIELD)
@XmlType(name = "LumielTransformTemplate")
public class LumielTransformTemplate {

	@XmlElement(name = "reward")
	private List<LumielTransformReward> lumielTransformRewards;
	@XmlAttribute(name = "id")
	protected int id;
	@XmlAttribute(name = "name")
	protected String name;
	@XmlAttribute(name = "need_point")
	protected int needPoints;
	@XmlAttribute(name = "activate")
	protected boolean activate;

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getNeedPoints() {
		return needPoints;
	}

	public boolean isActivate() {
		return activate;
	}

	public List<LumielTransformReward> getLumielTransformRewards() {
		return lumielTransformRewards;
	}
}
