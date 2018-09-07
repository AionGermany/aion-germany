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

import java.util.HashMap;
import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.model.templates.walker.RouteParent;
import com.aionemu.gameserver.model.templates.walker.RouteVersion;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "routeGroups" })
@XmlRootElement(name = "walker_versions")
public class WalkerVersionsData {

	@XmlElement(name = "walk_parent")
	protected List<RouteParent> routeGroups;
	@XmlTransient
	protected HashMap<String, String> walkParents = new HashMap<>();

	protected void afterUnmarshal(Unmarshaller u, Object parent) {
		for (RouteParent group : routeGroups) {
			for (RouteVersion version : group.getRouteVersion()) {
				walkParents.put(version.getId(), group.getId());
			}
		}
	}

	public boolean isRouteVersioned(String routeId) {
		if (routeId == null) {
			return false;
		}
		return walkParents.containsKey(routeId);
	}

	public String getRouteVersionId(String routeId) {
		if (routeId == null) {
			return null;
		}
		return walkParents.get(routeId);
	}

	public int size() {
		return walkParents.size();
	}
}
