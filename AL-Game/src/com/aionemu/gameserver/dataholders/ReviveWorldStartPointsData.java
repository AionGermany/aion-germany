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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.templates.revive_start_points.WorldReviveStartPoints;

/**
 * @author Falke_34
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "WorldStartPoints" })
@XmlRootElement(name = "revive_world_start_points")
public class ReviveWorldStartPointsData {

	@XmlElement(name = "revive_world_start_point")
	protected List<WorldReviveStartPoints> WorldStartPoints;

	@XmlTransient
	protected List<WorldReviveStartPoints> StartPointsList = new ArrayList<WorldReviveStartPoints>();

	void afterUnmarshal(Unmarshaller unmarshaller, Object parent) {
		for (WorldReviveStartPoints exit : WorldStartPoints) {
			StartPointsList.add(exit);
		}
		WorldStartPoints.clear();
		WorldStartPoints = null;
	}

	public WorldReviveStartPoints getReviveStartPoint(int worldId, Race race, int playerLevel) {
		for (WorldReviveStartPoints revive : StartPointsList) {
			if (revive.getReviveWorld() == worldId
					&& (race.equals(revive.getRace()) || revive.getRace().equals(Race.PC_ALL))
					&& playerLevel >= revive.getMinlevel() && playerLevel <= revive.getMaxlevel()) {
				return revive;
			}
		}
		return null;
	}

	public int size() {
		return StartPointsList.size();
	}
}
