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
package com.aionemu.gameserver.model.templates.spawns;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.model.templates.spawns.basespawns.BaseSpawn;
import com.aionemu.gameserver.model.templates.spawns.beritraspawns.BeritraSpawn;
import com.aionemu.gameserver.model.templates.spawns.landingspawns.LandingSpawn;
import com.aionemu.gameserver.model.templates.spawns.landingspecialspawns.LandingSpecialSpawn;
import com.aionemu.gameserver.model.templates.spawns.riftspawns.RiftSpawn;
import com.aionemu.gameserver.model.templates.spawns.rvrspawns.RvrSpawn;
import com.aionemu.gameserver.model.templates.spawns.siegespawns.SiegeSpawn;
import com.aionemu.gameserver.model.templates.spawns.svsspawns.SvsSpawn;
import com.aionemu.gameserver.model.templates.spawns.vortexspawns.VortexSpawn;

/**
 * @author xTz
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "SpawnMap")
public class SpawnMap {

	@XmlElement(name = "spawn")
	private List<Spawn> spawns;
	@XmlElement(name = "base_spawn")
	private List<BaseSpawn> baseSpawns;
	@XmlElement(name = "rift_spawn")
	private List<RiftSpawn> riftSpawns;
	@XmlElement(name = "siege_spawn")
	private List<SiegeSpawn> siegeSpawns;
	@XmlElement(name = "vortex_spawn")
	private List<VortexSpawn> vortexSpawns;
	@XmlElement(name = "beritra_spawn")
	private List<BeritraSpawn> beritraSpawns;
	@XmlElement(name = "rvr_spawn")
	private List<RvrSpawn> rvrSpawns;
	@XmlElement(name = "svs_spawn")
	private List<SvsSpawn> svsSpawns;
	@XmlElement(name = "landing_spawn")
	private List<LandingSpawn> landingSpawns;
	@XmlElement(name = "landing_special_spawn")
	private List<LandingSpecialSpawn> landingSpecialSpawns;

	@XmlAttribute(name = "map_id")
	private int mapId;

	public SpawnMap() {
	}

	public SpawnMap(int mapId) {
		this.mapId = mapId;
	}

	public int getMapId() {
		return mapId;
	}

	public List<Spawn> getSpawns() {
		if (spawns == null) {
			spawns = new ArrayList<Spawn>();
		}
		return spawns;
	}

	public void addSpawns(Spawn spawns) {
		getSpawns().add(spawns);
	}

	public void removeSpawns(Spawn spawns) {
		getSpawns().remove(spawns);
	}

	public List<BaseSpawn> getBaseSpawns() {
		if (baseSpawns == null) {
			baseSpawns = new ArrayList<BaseSpawn>();
		}
		return baseSpawns;
	}

	public void addBaseSpawns(BaseSpawn spawns) {
		getBaseSpawns().add(spawns);
	}

	public List<RiftSpawn> getRiftSpawns() {
		if (riftSpawns == null) {
			riftSpawns = new ArrayList<RiftSpawn>();
		}
		return riftSpawns;
	}

	public void addRiftSpawns(RiftSpawn spawns) {
		getRiftSpawns().add(spawns);
	}

	public List<SiegeSpawn> getSiegeSpawns() {
		if (siegeSpawns == null) {
			siegeSpawns = new ArrayList<SiegeSpawn>();
		}
		return siegeSpawns;
	}

	public void addSiegeSpawns(SiegeSpawn spawns) {
		getSiegeSpawns().add(spawns);
	}

	public List<BeritraSpawn> getBeritraSpawns() {
		if (beritraSpawns == null) {
			beritraSpawns = new ArrayList<BeritraSpawn>();
		}
		return beritraSpawns;
	}

	public void addBeritraSpawns(BeritraSpawn beritraSpawn) {
		getBeritraSpawns().add(beritraSpawn);
	}

	public List<RvrSpawn> getRvrSpawns() {
		if (rvrSpawns == null) {
			rvrSpawns = new ArrayList<RvrSpawn>();
		}
		return rvrSpawns;
	}

	public List<SvsSpawn> getSvsSpawns() {
		if (svsSpawns == null) {
			svsSpawns = new ArrayList<SvsSpawn>();
		}
		return svsSpawns;
	}

	public void addSvsSpawns(SvsSpawn spawns) {
		getSvsSpawns().add(spawns);
	}

	public List<VortexSpawn> getVortexSpawns() {
		if (vortexSpawns == null) {
			vortexSpawns = new ArrayList<VortexSpawn>();
		}
		return vortexSpawns;
	}

	public List<LandingSpawn> getLandingSpawns() {
		if (landingSpawns == null) {
			landingSpawns = new ArrayList<LandingSpawn>();
		}
		return landingSpawns;
	}

	public List<LandingSpecialSpawn> getLandingSpecialSpawns() {
		if (landingSpecialSpawns == null) {
			landingSpecialSpawns = new ArrayList<LandingSpecialSpawn>();
		}
		return landingSpecialSpawns;
	}

	public void addVortexSpawns(VortexSpawn spawns) {
		getVortexSpawns().add(spawns);
	}

	public void addLandingSpawns(LandingSpawn spawns) {
		getLandingSpawns().add(spawns);
	}

	public void addLandingSpecialSpawns(LandingSpecialSpawn spawns) {
		getLandingSpecialSpawns().add(spawns);
	}
}
