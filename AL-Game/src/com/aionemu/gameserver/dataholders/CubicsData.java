/**
 * This file is part of Aion-Lightning <aion-lightning.org>.
 *
 * Aion-Lightning is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * Aion-Lightning is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. * You should have received a copy of the GNU General Public License
 * along with Aion-Lightning. If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.dataholders;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.aionemu.gameserver.model.templates.cubics.CubicsTemplate;

import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * @author Phantom_KNA
 */
@XmlRootElement(name = "cubics_templates")
@XmlAccessorType(XmlAccessType.FIELD)
public class CubicsData {

	@XmlElement(name = "cubics_template")
	private List<CubicsTemplate> tlist;
	private TIntObjectHashMap<CubicsTemplate> cubicsData;
	private Map<Integer, CubicsTemplate> cubicsDataMap;

	public CubicsData() {
		this.cubicsData = (TIntObjectHashMap<CubicsTemplate>) new TIntObjectHashMap<CubicsTemplate>();
		this.cubicsDataMap = new HashMap<Integer, CubicsTemplate>(1);
	}

	void afterUnmarshal(final Unmarshaller u, final Object parent) {
		for (final CubicsTemplate id : this.tlist) {
			this.cubicsData.put(id.getId(), id);
			this.cubicsDataMap.put(id.getId(), id);
		}
	}

	public CubicsTemplate getCubicsId(final int id) {
		return (CubicsTemplate) this.cubicsData.get(id);
	}

	public Map<Integer, CubicsTemplate> getAll() {
		return this.cubicsDataMap;
	}

	/**
	 * @return CubicsData.size()
	 */
	public int size() {
		return cubicsData.size();
	}
}
