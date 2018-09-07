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
package com.aionemu.gameserver.world;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.aionemu.gameserver.model.templates.world.WorldMapTemplate;
import com.aionemu.gameserver.world.zone.ZoneAttributes;

import javolution.util.FastMap;

/**
 * This object is representing one in-game map and can have instances.
 *
 * @author -Nemesiss-
 */
public class WorldMap {

	private WorldMapTemplate worldMapTemplate;
	private AtomicInteger nextInstanceId = new AtomicInteger(0);
	/**
	 * List of instances.
	 */
	private Map<Integer, WorldMapInstance> instances = new FastMap<Integer, WorldMapInstance>().shared();
	/**
	 * World to which belongs this WorldMap
	 */
	private World world;
	private int worldOptions;

	public WorldMap(WorldMapTemplate worldMapTemplate, World world) {
		this.world = world;
		this.worldMapTemplate = worldMapTemplate;
		this.worldOptions = worldMapTemplate.getFlags();

		for (int i = 1; i <= getInstanceCount(); i++) {
			int nextId = getNextInstanceId();
			addInstance(nextId, WorldMapInstanceFactory.createWorldMapInstance(this, nextId));
		}
	}

	public String getName() {
		return worldMapTemplate.getName();
	}

	public int getWaterLevel() {
		return worldMapTemplate.getWaterLevel();
	}

	public int getDeathLevel() {
		return worldMapTemplate.getDeathLevel();
	}

	public WorldType getWorldType() {
		return worldMapTemplate.getWorldType();
	}

	public int getWorldSize() {
		return worldMapTemplate.getWorldSize();
	}

	public Integer getMapId() {
		return worldMapTemplate.getMapId();
	}

	public boolean isPossibleFly() {
		return (worldOptions & ZoneAttributes.FLY.getId()) != 0;
	}

	public boolean isExceptBuff() {
		return worldMapTemplate.isExceptBuff();
	}

	public boolean canGlide() {
		return (worldOptions & ZoneAttributes.GLIDE.getId()) != 0;
	}

	public boolean canPutKisk() {
		return (worldOptions & ZoneAttributes.BIND.getId()) != 0;
	}

	public boolean canRecall() {
		return (worldOptions & ZoneAttributes.RECALL.getId()) != 0;
	}

	public boolean canRide() {
		return (worldOptions & ZoneAttributes.RIDE.getId()) != 0;
	}

	public boolean canFlyRide() {
		return (worldOptions & ZoneAttributes.FLY_RIDE.getId()) != 0;
	}

	public boolean isPvpAllowed() {
		return (worldOptions & ZoneAttributes.PVP_ENABLED.getId()) != 0;
	}

	public boolean isSameRaceDuelsAllowed() {
		return (worldOptions & ZoneAttributes.DUEL_SAME_RACE_ENABLED.getId()) != 0;
	}

	public boolean isOtherRaceDuelsAllowed() {
		return (worldOptions & ZoneAttributes.DUEL_OTHER_RACE_ENABLED.getId()) != 0;
	}

	public void setWorldOption(ZoneAttributes option) {
		worldOptions |= option.getId();
	}

	public void removeWorldOption(ZoneAttributes option) {
		worldOptions &= ~option.getId();
	}

	public boolean hasOverridenOption(ZoneAttributes option) {
		if ((worldMapTemplate.getFlags() & option.getId()) == 0) {
			return (worldOptions & option.getId()) != 0;
		}
		return (worldOptions & option.getId()) == 0;
	}

	public int getInstanceCount() {
		int twinCount = worldMapTemplate.getTwinCount();
		if (twinCount == 0 && !worldMapTemplate.isInstance()) {
			twinCount = 1;
		}
		twinCount += worldMapTemplate.getBeginnerTwinCount();
		return twinCount;
	}

	/**
	 * Return a WorldMapInstance - depends on map configuration one map may have twins instances to balance player. This method will return WorldMapInstance by server chose.
	 *
	 * @return WorldMapInstance.
	 */
	public WorldMapInstance getMainWorldMapInstance() {
		// TODO Balance players into instances.
		return getWorldMapInstance(1);
	}

	/**
	 * This method return WorldMapInstance by specified instanceId
	 *
	 * @param instanceId
	 * @return WorldMapInstance
	 */
	public WorldMapInstance getWorldMapInstanceById(int instanceId) {
		// instanceId is a count, some code still uses 0 for the default instance
		if (instanceId == 0) {
			instanceId = 1;
		}
		if (!isInstanceType()) {
			if (instanceId > getInstanceCount()) {
				throw new IllegalArgumentException("WorldMapInstance " + getMapId() + " has lower instances count than " + instanceId);
			}
		}
		return getWorldMapInstance(instanceId);
	}

	/**
	 * Returns WorldMapInstance by instanceId.
	 *
	 * @param instanceId
	 * @return WorldMapInstance/
	 */
	private WorldMapInstance getWorldMapInstance(int instanceId) {
		// instanceId is a count, some code still uses 0 for the default instance
		if (instanceId == 0) {
			instanceId = 1;
		}
		return instances.get(instanceId);
	}

	/**
	 * Remove WorldMapInstance by instanceId.
	 *
	 * @param instanceId
	 */
	public void removeWorldMapInstance(int instanceId) {
		// instanceId is a count, some code still uses 0 for the default instance
		if (instanceId == 0) {
			instanceId = 1;
		}
		instances.remove(instanceId);
	}

	/**
	 * Add instance to map
	 *
	 * @param instanceId
	 * @param instance
	 */
	public void addInstance(int instanceId, WorldMapInstance instance) {
		// instanceId is a count, some code still uses 0 for the default instance
		if (instanceId == 0) {
			instanceId = 1;
		}
		instances.put(instanceId, instance);
	}

	/**
	 * Returns the World containing this WorldMap.
	 */
	public World getWorld() {
		return world;
	}

	public final WorldMapTemplate getTemplate() {
		return worldMapTemplate;
	}

	/**
	 * @return the nextInstanceId
	 */
	public int getNextInstanceId() {
		return nextInstanceId.incrementAndGet();
	}

	/**
	 * Whether this world map is instance type
	 *
	 * @return
	 */
	public boolean isInstanceType() {
		return worldMapTemplate.isInstance();
	}

	/**
	 * @return
	 */
	public Iterator<WorldMapInstance> iterator() {
		return instances.values().iterator();
	}

	/**
	 * All instance ids of this map
	 */
	public Collection<Integer> getAvailableInstanceIds() {
		return instances.keySet();
	}
}
