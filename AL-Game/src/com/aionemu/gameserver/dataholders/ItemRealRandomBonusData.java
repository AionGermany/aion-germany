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

import com.aionemu.gameserver.model.templates.item.bonuses.RealItemRandomBonus;

import gnu.trove.map.hash.TIntObjectHashMap;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "real_random_bonuses")
public class ItemRealRandomBonusData {

	@XmlElement(name = "real_random_bonus", required = true)
	protected List<RealItemRandomBonus> randomBonuses;
	private TIntObjectHashMap<RealItemRandomBonus> bonuslistData = new TIntObjectHashMap<RealItemRandomBonus>();

	void afterUnmarshal(Unmarshaller u, Object parent) {
		for (RealItemRandomBonus bonus : randomBonuses) {
			bonuslistData.put(bonus.getId(), bonus);
		}
		randomBonuses.clear();
		randomBonuses = null;
	}

	public RealItemRandomBonus getRealBonusById(int id) {
		return bonuslistData.get(id);
	}

	public int size() {
		return bonuslistData.size();
	}
}
