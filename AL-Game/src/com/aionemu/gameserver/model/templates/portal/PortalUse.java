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
package com.aionemu.gameserver.model.templates.portal;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.model.Race;

/**
 * @author xTz
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PortalUse")
public class PortalUse {

	@XmlElement(name = "portal_path")
	protected List<PortalPath> portalPath;
	@XmlAttribute(name = "npc_id")
	protected int npcId;
	@XmlAttribute(name = "siege_id")
	protected int siegeId;

	public List<PortalPath> getPortalPaths() {
		return portalPath;
	}

	public PortalPath getPortalPath(Race race) {
		if (portalPath != null) {
			for (PortalPath path : portalPath) {
				if (path.getRace().equals(race) || path.getRace().equals(Race.PC_ALL)) {
					return path;
				}
			}
		}
		return null;
	}

	public int getNpcId() {
		return npcId;
	}

	public void setNpcId(int value) {
		this.npcId = value;
	}

	public int getSiegeId() {
		return siegeId;
	}

	public void setSiegeId(int value) {
		this.siegeId = value;
	}
}
