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
package com.aionemu.gameserver.model.templates.item;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.model.Gender;
import com.aionemu.gameserver.utils.stats.AbyssRankEnum;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author Rolandas
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UseLimits")
public class ItemUseLimits {

	@XmlAttribute(name = "usedelay")
	private int useDelay;
	@XmlAttribute(name = "usedelayid")
	private int useDelayId;
	@XmlAttribute(name = "ownership_world")
	private int ownershipWorldId;
	@XmlAttribute
	private String usearea;
	@XmlAttribute(name = "gender")
	private Gender genderPermitted;
	@XmlAttribute(name = "ride_usable")
	private Boolean rideUsable;
	@XmlAttribute(name = "rank_min")
	private int minRank;
	@XmlAttribute(name = "rank_max")
	private int maxRank = AbyssRankEnum.SUPREME_COMMANDER.getId();
	@XmlAttribute(name = "guild_level")
	private int guildLevel;

	public int getDelayId() {
		return useDelayId;
	}

	public void setDelayId(int delayId) {
		useDelayId = delayId;
	}

	public int getDelayTime() {
		return useDelay;
	}

	public void setDelayTime(int useDelay) {
		this.useDelay = useDelay;
	}

	public ZoneName getUseArea() {
		if (this.usearea == null) {
			return null;
		}

		try {
			return ZoneName.get(this.usearea);
		}
		catch (Exception e) {
			return null;
		}
	}

	public int getOwnershipWorld() {
		return ownershipWorldId;
	}

	public Gender getGenderPermitted() {
		return genderPermitted;
	}

	public boolean isRideUsable() {
		if (rideUsable == null) {
			return false;
		}
		return rideUsable;
	}

	public int getMinRank() {
		return minRank;
	}

	public int getMaxRank() {
		return maxRank;
	}

	public boolean verifyRank(int rank) {
		return minRank <= rank && maxRank >= rank;
	}

	public int getGuildLevelPermitted() {
		return guildLevel;
	}
}
