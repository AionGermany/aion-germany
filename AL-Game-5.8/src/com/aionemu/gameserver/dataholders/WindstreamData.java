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
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.model.templates.windstreams.WindstreamTemplate;

import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * @author LokiReborn
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "windstreams")
public class WindstreamData {

	@XmlElement(name = "windstream")
	private List<WindstreamTemplate> wts;
	private TIntObjectHashMap<WindstreamTemplate> windstreams;

	void afterUnmarshal(Unmarshaller u, Object parent) {
		windstreams = new TIntObjectHashMap<WindstreamTemplate>();
		for (WindstreamTemplate wt : wts) {
			windstreams.put(wt.getMapid(), wt);
		}

		wts = null;
	}

	public WindstreamTemplate getStreamTemplate(int mapId) {
		return windstreams.get(mapId);
	}

	/**
	 * @return items.size()
	 */
	public int size() {
		return windstreams.size();
	}
}
