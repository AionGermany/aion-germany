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

import org.joda.time.DateTime;

import com.aionemu.gameserver.model.gameobjects.player.Player;
//import com.aionemu.gameserver.model.gameobjects.player.PortalCooldownItem; TODO?
import com.aionemu.gameserver.model.templates.InstanceCooltime;

import javolution.util.FastMap;

/**
 * @author VladimirZ
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "instance_cooltimes")
public class InstanceCooltimeData {

	@XmlElement(name = "instance_cooltime", required = true)
	protected List<InstanceCooltime> instanceCooltime;
	private FastMap<Integer, InstanceCooltime> instanceCooltimes = new FastMap<Integer, InstanceCooltime>();
	private HashMap<Integer, Integer> syncIdToMapId = new HashMap<Integer, Integer>();

	/**
	 * @param u
	 * @param parent
	 */
	void afterUnmarshal(Unmarshaller u, Object parent) {
		for (InstanceCooltime tmp : instanceCooltime) {
			instanceCooltimes.put(tmp.getWorldId(), tmp);
			syncIdToMapId.put(tmp.getId(), tmp.getWorldId());
		}
		instanceCooltime.clear();
	}

	public FastMap<Integer, InstanceCooltime> getAllInstances() {
		return instanceCooltimes;
	}

	/**
	 * @param worldId
	 * @return
	 */
	public InstanceCooltime getInstanceCooltimeByWorldId(int worldId) {
		return instanceCooltimes.get(worldId);
	}

	public int getWorldId(int syncId) {
		if (!syncIdToMapId.containsKey(syncId)) {
			return 0;
		}
		return syncIdToMapId.get(syncId);
	}

	public long getInstanceEntranceCooltimeById(Player player, int syncId) {
		if (!syncIdToMapId.containsKey(syncId)) {
			return 0;
		}
		return getInstanceEntranceCooltime(player, syncIdToMapId.get(syncId));
	}

	public int getInstanceEntranceCountByWorldId(int worldId) {
		InstanceCooltime clt = getInstanceCooltimeByWorldId(worldId);
		if (clt != null) {
			return clt.getMaxEntriesCount();
		}
		else {
			return 0;
		}
	}

	public long getInstanceEntranceCooltime(Player player, int worldId) {
		InstanceCooltime clt = getInstanceCooltimeByWorldId(worldId);
		long cooltime = 0;
		if (clt != null) {
			if (clt.getCoolTimeType().isDaily()) {
				DateTime now = DateTime.now();
				DateTime repeatDate = new DateTime(now.getYear(), now.getMonthOfYear(), now.getDayOfMonth(), clt.getEntCoolTime() / 100, 0, 0);
				if (now.isAfter(repeatDate)) {
					repeatDate = repeatDate.plusHours(24);
				}
				cooltime = repeatDate.getMillis();
			}
			else if (clt.getCoolTimeType().isWeekly()) {
				String[] days = clt.getTypeValue().split(",");
				cooltime = getUpdateHours(days, clt.getEntCoolTime() / 100);
			}
			else if (clt.getCoolTimeType().isRelative()) {
				switch (worldId) {
					case 300020000:
					case 300290000:
					case 300480000:
					case 300560000:
					case 301160000:
					case 301200000:
					case 301320000:
					case 301330000:
					case 301340000:
					case 301400000:
					case 301520000:
					case 301570000:
					case 301580000:
					case 301690000:
					case 310010000:
					case 310020000:
					case 310030000:
					case 310040000:
					case 310060000:
					case 310070000:
					case 310080000:
					case 310120000:
					case 320010000:
					case 320020000:
					case 320030000:
					case 320040000:
					case 320050000:
					case 320060000:
					case 320070000:
					case 320090000:
					case 320120000:
					case 320140000:
					case 720010000:
					case 730010000:
					case 900120000:
					case 900130000:
					case 900140000:
					case 900150000:
					case 900190000:
					case 900200000:
					case 900210000:
					case 900230000:
						DateTime now = DateTime.now();
						DateTime repeatDate = new DateTime(now.getYear(), now.getMonthOfYear(), now.getDayOfMonth(), 9, 0, 0); // 9 AM
						if (now.isAfter(repeatDate)) {
							repeatDate = repeatDate.plusHours(24);
						}
						cooltime = repeatDate.getMillis();
						break;
					default:
						cooltime = System.currentTimeMillis() + (clt.getEntCoolTime() * 60 * 1000);
				}
			}
		}
		return cooltime;
	}

	private long getUpdateHours(String[] days, int hour) {
		DateTime now = DateTime.now();
		DateTime repeatDate = new DateTime(now.getYear(), now.getMonthOfYear(), now.getDayOfMonth(), hour, 0, 0);
		int curentDay = now.getDayOfWeek();
		for (String name : days) {
			int day = getDay(name);
			if (day < curentDay) {
				continue;
			}
			if (day == curentDay) {
				if (now.isBefore(repeatDate)) {
					return repeatDate.getMillis();
				}
			}
			else {
				repeatDate = repeatDate.plusDays(day - curentDay);
				return repeatDate.getMillis();
			}
		}
		return repeatDate.plusDays((7 - curentDay) + getDay(days[0])).getMillis();
	}

	private int getDay(String day) {
		if (day.equals("Mon")) {
			return 1;
		}
		else if (day.equals("Tue")) {
			return 2;
		}
		else if (day.equals("Wed")) {
			return 3;
		}
		else if (day.equals("Thu")) {
			return 4;
		}
		else if (day.equals("Fri")) {
			return 5;
		}
		else if (day.equals("Sat")) {
			return 6;
		}
		else if (day.equals("Sun")) {
			return 7;
		}
		throw new IllegalArgumentException("Invalid Day: " + day);
	}

	public Integer size() {
		return instanceCooltimes.size();
	}
}
