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

import com.aionemu.gameserver.model.templates.teleport.TelelocationTemplate;

import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * @author orz
 */
@XmlRootElement(name = "teleport_location")
@XmlAccessorType(XmlAccessType.FIELD)
public class TeleLocationData {

	@XmlElement(name = "teleloc_template")
	private List<TelelocationTemplate> tlist;
	/**
	 * A map containing all teleport location templates
	 */
	private TIntObjectHashMap<TelelocationTemplate> loctlistData = new TIntObjectHashMap<TelelocationTemplate>();

	void afterUnmarshal(Unmarshaller u, Object parent) {
		for (TelelocationTemplate loc : tlist) {
			loctlistData.put(loc.getLocId(), loc);
		}
	}

	public int size() {
		return loctlistData.size();
	}

	public TelelocationTemplate getTelelocationTemplate(int id) {
		return loctlistData.get(id);
	}
}
