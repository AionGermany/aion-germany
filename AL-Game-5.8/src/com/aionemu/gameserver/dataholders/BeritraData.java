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

import com.aionemu.gameserver.model.beritra.BeritraLocation;
import com.aionemu.gameserver.model.templates.beritra.BeritraTemplate;

import javolution.util.FastMap;

/**
 * @author Cx3
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "beritra_invasion")
public class BeritraData {

	@XmlElement(name = "beritra_location")
	private List<BeritraTemplate> beritraTemplates;

	@XmlTransient
	private FastMap<Integer, BeritraLocation> beritra = new FastMap<Integer, BeritraLocation>();

	/**
	 * @param u
	 * @param parent
	 */
	void afterUnmarshal(Unmarshaller u, Object parent) {
		for (BeritraTemplate template : beritraTemplates) {
			beritra.put(template.getId(), new BeritraLocation(template));
		}
	}

	public int size() {
		return beritra.size();
	}

	public FastMap<Integer, BeritraLocation> getBeritraLocations() {
		return beritra;
	}
}
