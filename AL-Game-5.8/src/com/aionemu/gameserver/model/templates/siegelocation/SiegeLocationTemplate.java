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
package com.aionemu.gameserver.model.templates.siegelocation;

import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.model.siege.SiegeType;

/**
 * @author Sarynth modified by antness & Source & Wakizashi
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "siegelocation")
public class SiegeLocationTemplate {

	@XmlAttribute(name = "id")
	protected int id;
	@XmlAttribute(name = "type")
	protected SiegeType type;
	@XmlAttribute(name = "world")
	protected int world;
	@XmlElement(name = "artifact_activation")
	protected ArtifactActivation artifactActivation;
	@XmlElement(name = "door_repair")
	protected DoorRepair doorRepair;
	@XmlElement(name = "siege_reward")
	protected List<SiegeReward> siegeRewards;
	@XmlElement(name = "legion_reward")
	protected List<SiegeLegionReward> siegeLegionRewards;
	@XmlAttribute(name = "name_id")
	protected int nameId = 0;
	@XmlAttribute(name = "buff_id")
	protected int buffId = 0;
	@XmlAttribute(name = "buff_idA")
	protected int buffIdA = 0;
	@XmlAttribute(name = "buff_idE")
	protected int buffIdE = 0;
	@XmlAttribute(name = "owner_gp")
	protected int ownerGp = 0;
	@XmlAttribute(name = "repeat_count")
	protected int repeatCount = 1;
	@XmlAttribute(name = "repeat_interval")
	protected int repeatInterval = 1;
	@XmlAttribute(name = "siege_duration")
	protected int siegeDuration;
	@XmlAttribute(name = "influence")
	protected int influenceValue;
	@XmlAttribute(name = "occupy_count")
	protected int occupyCount = 0;
	@XmlList
	@XmlAttribute(name = "fortress_dependency")
	protected List<Integer> fortressDependency;

	// Luna System
	@XmlElement(name = "luna_boost_price")
	protected List<LunaBoostPrice> lunaBoostPrice;
	@XmlElement(name = "luna_teleport_price")
	protected List<LunaTeleportPrice> lunaTeleportPrice;
	@XmlElement(name = "luna_reward")
	protected List<LunaReward> lunaReward;
	@XmlElement(name = "luna_teleport")
	protected List<LunaTeleport> lunaTeleport;

	/**
	 * @return the location id
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * @return the location type
	 */
	public SiegeType getType() {
		return this.type;
	}

	/**
	 * @return the world id
	 */
	public int getWorldId() {
		return this.world;
	}

	public ArtifactActivation getActivation() {
		return this.artifactActivation;
	}

	/**
	 * @return the reward list
	 */
	public List<SiegeReward> getSiegeRewards() {
		return this.siegeRewards;
	}

	/**
	 * @return the siege zone
	 */
	public List<SiegeLegionReward> getSiegeLegionRewards() {
		return this.siegeLegionRewards;
	}

	/**
	 * @return the nameId
	 */
	public int getNameId() {
		return nameId;
	}

	/**
	 * @return the occupyCount
	 */
	public int getOccupyCount() {
		return occupyCount;
	}

	/**
	 * @return the repeatCount
	 */
	public int getRepeatCount() {
		return repeatCount;
	}

	/**
	 * @return the repeatInterval
	 */
	public int getRepeatInterval() {
		return repeatInterval;
	}

	/**
	 * @return the fortressDependency
	 */
	public List<Integer> getFortressDependency() {
		if (fortressDependency == null) {
			return Collections.emptyList();
		}
		return fortressDependency;
	}

	/**
	 * @return the Duration in Seconds
	 */
	public int getSiegeDuration() {
		return this.siegeDuration;
	}

	/**
	 * @return the influence Points
	 */
	public int getInfluenceValue() {
		return this.influenceValue;
	}

	/**
	 * @return the Door Repair
	 */
	public DoorRepair getRepair() {
		return doorRepair;
	}

	/**
	 * @return the Buff ID
	 */
	public int getBuffId() {
		return buffId;
	}

	/**
	 * @return the Buff ID Asmodians
	 */
	public int getBuffIdA() {
		return buffIdA;
	}

	/**
	 * @return the Buff ID Elyos
	 */
	public int getBuffIdE() {
		return buffIdE;
	}

	/**
	 * @return the Owner GP
	 */
	public int getOwnerGp() {
		return ownerGp;
	}

	// Luna System
	public List<LunaBoostPrice> getLunaBoostPrice() {
		return this.lunaBoostPrice;
	}

	public List<LunaTeleportPrice> getLunaTeleportPrice() {
		return this.lunaTeleportPrice;
	}

	public List<LunaReward> getLunaReward() {
		return this.lunaReward;
	}

	public List<LunaTeleport> getLunaTeleport() {
		return this.lunaTeleport;
	}
}
