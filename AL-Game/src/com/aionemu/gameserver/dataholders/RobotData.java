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
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.model.templates.robot.RobotInfo;

import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * @author Ever'
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "robots" })
@XmlRootElement(name = "robots")
public class RobotData {

	@XmlElement(name = "robot_info")
	private List<RobotInfo> robots;
	@XmlTransient
	private TIntObjectHashMap<RobotInfo> robotInfos;

	void afterUnmarshal(Unmarshaller u, Object parent) {
		robotInfos = new TIntObjectHashMap<RobotInfo>();
		for (RobotInfo info : robots) {
			robotInfos.put(info.getRobotId(), info);
		}
		robots.clear();
		robots = null;
	}

	public RobotInfo getRobotInfo(int npcId) {
		return robotInfos.get(npcId);
	}

	public int size() {
		return robotInfos.size();
	}
}
