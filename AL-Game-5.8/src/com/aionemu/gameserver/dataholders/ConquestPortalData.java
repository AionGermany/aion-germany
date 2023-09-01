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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.aionemu.gameserver.model.templates.portal.ConquestPortal;

import javolution.util.FastList;

/**
 * @author CoolyT
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "conquest_portals")
public class ConquestPortalData {

	@XmlElement(name = "portal")
	public FastList<ConquestPortal> portals = new FastList<ConquestPortal>();

	public int size() {
		return portals.size();
	}

	public ConquestPortal getPortalbyNpcId(int id) {
		for (ConquestPortal portal : portals) {
			if (portal.npcId == id)
				return portal;

		}
		return null;
	}

	public FastList<ConquestPortal> getPortals() {
		return portals;
	}
}
