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
package com.aionemu.gameserver.model.templates.item;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.slf4j.LoggerFactory;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.model.PlayerClass;
import com.aionemu.gameserver.model.Race;

/**
 * @author antness
 */
@XmlType(name = "ResultedItem")
public class ResultedItem {

	@XmlAttribute(name = "name")
	public String itemName;
	@XmlAttribute(name = "id")
	public int itemId;
	@XmlAttribute(name = "count")
	public int count;
	@XmlAttribute(name = "rnd_min")
	public int rndMin;
	@XmlAttribute(name = "rnd_max")
	public int rndMax;
	@XmlAttribute(name = "race")
	public Race race = Race.PC_ALL;
	@XmlAttribute(name = "player_class")
	public PlayerClass playerClass = PlayerClass.ALL;

	public String getItemName() {
		return itemName;
	}

	public int getItemId() {
		return itemId;
	}

	public int getCount() {
		return count;
	}

	public int getRndMin() {
		return rndMin;
	}

	public int getRndMax() {
		return rndMax;
	}

	public final Race getRace() {
		return race;
	}

	public PlayerClass getPlayerClass() {
		return playerClass;
	}

	public final int getResultCount() {
		if (count == 0 && rndMin == 0 && rndMax == 0) {
			return 1;
		}
		else if (rndMin > 0 || rndMax > 0) {
			if (rndMax < rndMin) {
				LoggerFactory.getLogger(ResultedItem.class).warn("Wronte rnd result item definition {} {}", rndMin, rndMax);
				return 1;
			}
			else {
				return Rnd.get(rndMin, rndMax);
			}
		}
		else {
			return count;
		}
	}
}
