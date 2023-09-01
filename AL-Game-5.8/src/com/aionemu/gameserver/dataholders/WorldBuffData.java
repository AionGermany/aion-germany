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

import com.aionemu.gameserver.model.templates.worldbuff.WorldBuffMap;
import com.aionemu.gameserver.model.templates.worldbuff.WorldBuffTemplate;

import javolution.util.FastList;
import javolution.util.FastMap;

/**
 * @author Steve
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "world_buffs")
public class WorldBuffData {

	@XmlElement(name = "world_buff_map")
	private List<WorldBuffMap> template;
	@XmlTransient
	private FastMap<Integer, FastList<WorldBuffTemplate>> map = new FastMap<>();

	void afterUnmarshal(Unmarshaller u, Object parent) {
		for (WorldBuffMap tmp : template) {
			map.put(tmp.getId(), tmp.getBuff());
		}
	}

	public int size() {
		return map.size();
	}

	public FastMap<Integer, FastList<WorldBuffTemplate>> getWorldbuffs() {
		return map;
	}

	public FastList<WorldBuffTemplate> getWorldBuff(int id) {
		return map.get(id);
	}
}
