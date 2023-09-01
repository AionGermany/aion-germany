/**
 * This file is part of Aion-Lightning <aion-lightning.org>.
 * <p>
 * Aion-Lightning is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * Aion-Lightning is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details. *
 * You should have received a copy of the GNU General Public License
 * along with Aion-Lightning.
 * If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.dataholders;

import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.aionemu.gameserver.model.templates.ExpTemplate;

import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * Object of this class is containing info about experience that are required for each level that player can obtain.
 *
 * @author Luno
 */
@XmlRootElement(name = "player_experience_table")
@XmlAccessorType(XmlAccessType.NONE)
public class PlayerExperienceTable {

	/**
	 * Exp table
	 */
	@XmlElement(name = "exp")
	private List<ExpTemplate> expTemp;
	private TIntObjectHashMap<ExpTemplate> exp;

	void afterUnmarshal(Unmarshaller u, Object parent) {
		exp = new TIntObjectHashMap<ExpTemplate>();
		for (ExpTemplate expT : expTemp) {
			exp.put(expT.getLevel() - 1, expT);
		}
		expTemp = null;
	}

	public ExpTemplate getExpTemplate(int level) {
		return exp.get(level);
	}

	/**
	 * Returns the number of experience that player have at the beginning of given level.<br>
	 * For example at lv 1 it's 0
	 *
	 * @param level
	 * @return count of experience. If <tt>level</tt> parameter is higher than the max level that player can gain, then IllegalArgumentException is thrown.
	 */
	public long getStartExpForLevel(int level) {
		if (level > exp.size()) {
			throw new IllegalArgumentException("The given level is higher than possible max");
		}

		return level == 0 ? 0 : exp.get(level - 1).getExp();
	}

	public int getLevelForExp(long expValue) {
		int level = 0;
		for (int i = exp.size(); i > 0; i--) {
			if (expValue >= exp.get(i - 1).getExp()) {
				level = i;
				break;
			}
		}
		if (getMaxLevel() <= level) {
			return getMaxLevel() - 1;
		}
		return level;
	}

	/**
	 * Max possible level,that player can obtain.
	 *
	 * @return max level.
	 */
	public int getMaxLevel() {
		return exp == null ? 0 : exp.size();
	}

}
