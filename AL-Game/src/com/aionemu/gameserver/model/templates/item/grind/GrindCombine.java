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
package com.aionemu.gameserver.model.templates.item.grind;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.aionemu.gameserver.model.PlayerClass;

@XmlAccessorType(value = XmlAccessType.FIELD)
@XmlRootElement(name = "GrindCombine")
public class GrindCombine {

	@XmlElement(name = "reward_grind")
	protected List<GrindReward> rewards;

	@XmlAttribute(name = "id")
	protected int id;

	@XmlAttribute(name = "target_class")
	protected PlayerClass playerClass;

	@XmlAttribute(name = "price")
	protected int price;

	@XmlAttribute(name = "materiel_color_1")
	protected int color1;

	@XmlAttribute(name = "materiel_color_2")
	protected int color2;

	public int getId() {
		return id;
	}

	public int getColor1() {
		return color1;
	}

	public int getColor2() {
		return color2;
	}

	public int getPrice() {
		return price;
	}

	public PlayerClass getPlayerClass() {
		return playerClass;
	}

	public List<GrindReward> getRewards() {
		return rewards;
	}
}
