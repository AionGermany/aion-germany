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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.ai2.event.AIEventType;
import com.aionemu.gameserver.configs.administration.DeveloperConfig;
import com.aionemu.gameserver.configs.main.SiegeConfig;
import com.aionemu.gameserver.configs.main.WorldConfig;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.StaticDoor;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.siege.SiegeNpc;
import com.aionemu.gameserver.model.templates.zone.ZoneClassName;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.zone.ZoneInstance;
import com.aionemu.gameserver.world.zone.ZoneName;

import javolution.util.FastMap;
import javolution.util.FastMap.Entry;

/**
 * Just some part of map.
 *
 * @author -Nemesiss-
 */
public class MapRegion {

	private static final Logger log = LoggerFactory.getLogger(MapRegion.class);
	/**
	 * Region id of this map region [NOT WORLD ID!]
	 */
	private final int regionId;
	/**
	 * WorldMapInstance witch is parent of this map region.
	 */
	private final WorldMapInstance parent;
	/**
	 * Surrounding regions + self.
	 */
	private volatile MapRegion[] neighbours = new MapRegion[0];
	/**
	 * Objects on this map region.
	 */
	private final FastMap<Integer, VisibleObject> objects = new FastMap<Integer, VisibleObject>().shared();
	private final AtomicInteger playerCount = new AtomicInteger(0);
	private final AtomicBoolean regionActive = new AtomicBoolean(false);
	private final int zoneCount;
	/**
	 * Zones in this region
	 */
	private FastMap<Integer, TreeSet<ZoneInstance>> zoneMap;

	/**
	 * Constructor.
	 *
	 * @param id
	 * @param parent
	 */
	MapRegion(int id, WorldMapInstance parent, ZoneInstance[] zones) {
		this.regionId = id;
		this.parent = parent;
		this.zoneCount = zones.length;
		createZoneMap(zones);
		addNeighbourRegion(this);
	}

	/**
	 * Return World map id.
	 *
	 * @return world map id
	 */
	public Integer getMapId() {
		return getParent().getMapId();
	}

	/**
	 * Return an instance of {@link World}, which keeps map, to which belongs this region
	 */
	public World getWorld() {
		return getParent().getWorld();
	}

	/**
	 * Returns region id of this map region. [NOT WORLD ID!]
	 *
	 * @return region id.
	 */
	public int getRegionId() {
		return regionId;
	}

	/**
	 * Returns WorldMapInstance witch is parent of this instance
	 *
	 * @return parent
	 */
	public WorldMapInstance getParent() {
		return parent;
	}

	/**
	 * Returns iterator over AionObjects on this region
	 *
	 * @return objects iterator
	 */
	public FastMap<Integer, VisibleObject> getObjects() {
		return objects;
	}

	public Map<Integer, StaticDoor> getDoors() {
		Map<Integer, StaticDoor> doors = new HashMap<Integer, StaticDoor>();
		for (VisibleObject obj : objects.values()) {
			if (obj instanceof StaticDoor) {
				StaticDoor door = (StaticDoor) obj;
				doors.put(door.getSpawn().getStaticId(), door);
			}
		}
		return doors;
	}

	/**
	 * @return the neighbours
	 */
	public MapRegion[] getNeighbours() {
		return neighbours;
	}

	/**
	 * Add neighbour region to this region neighbours list.
	 *
	 * @param neighbour
	 */
	void addNeighbourRegion(MapRegion neighbour) {
		neighbours = (MapRegion[]) ArrayUtils.add(neighbours, neighbour);
	}

	/**
	 * Add AionObject to this region objects list.
	 *
	 * @param object
	 */
	void add(VisibleObject object) {
		if (objects.put(object.getObjectId(), object) == null) {
			if (object instanceof Player) {
				checkActiveness(playerCount.incrementAndGet() > 0);
			}
			else if (DeveloperConfig.SPAWN_CHECK) {
				Iterator<TreeSet<ZoneInstance>> zoneIter = zoneMap.values().iterator();
				while (zoneIter.hasNext()) {
					TreeSet<ZoneInstance> zones = zoneIter.next();
					for (ZoneInstance zone : zones) {
						if (!zone.isInsideCordinate(object.getX(), object.getY(), object.getZ())) {
							continue;
						}
						if (zone.getZoneTemplate().getZoneType() != ZoneClassName.DUMMY) {
							return;
						}
					}
				}
				log.warn("Outside any zones: id=" + object + " > X:" + object.getX() + ",Y:" + object.getY() + ",Z:" + object.getZ());
			}
		}
	}

	/**
	 * Remove AionObject from region objects list.
	 *
	 * @param object
	 */
	void remove(VisibleObject object) {
		if (objects.remove(object.getObjectId()) != null) {
			if (object instanceof Player) {
				checkActiveness(playerCount.decrementAndGet() > 0);
			}
		}
	}

	final void checkActiveness(boolean active) {
		if (active && regionActive.compareAndSet(false, true)) {
			startActivation();
		}
		else if (!active) {
			startDeactivation();
		}
	}

	final void startActivation() {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				log.debug("Activating in map {} region {}", getMapId(), regionId);
				MapRegion.this.activateObjects();
				for (MapRegion neighbor : getNeighbours()) {
					neighbor.activate();
				}
			}
		}, 1000);
	}

	final void startDeactivation() {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				log.debug("Deactivating in map {} region {}", getMapId(), regionId);
				for (MapRegion neighbor : getNeighbours()) {
					if (!neighbor.isNeighboursActive()) {
						neighbor.deactivate();
					}
				}
			}
		}, 60000);
	}

	public void activate() {
		if (regionActive.compareAndSet(false, true)) {
			activateObjects();
		}
	}

	/**
	 * Send ACTIVATE event to all objects with AI2
	 */
	private final void activateObjects() {
		for (VisibleObject visObject : objects.values()) {
			if (visObject instanceof Creature) {
				Creature creature = (Creature) visObject;
				creature.getAi2().onGeneralEvent(AIEventType.ACTIVATE);
			}
		}
	}

	public void deactivate() {
		if (regionActive.compareAndSet(true, false)) {
			deactivateObjects();
		}
	}

	/**
	 * Send DEACTIVATE event to all objects with AI2
	 */
	private void deactivateObjects() {
		for (VisibleObject visObject : objects.values()) {
			if (visObject instanceof Creature && !(SiegeConfig.BALAUR_AUTO_ASSAULT && visObject instanceof SiegeNpc) && !((Creature) visObject).isFlag() && !((Creature) visObject).isRaidMonster()) {
				Creature creature = (Creature) visObject;
				creature.getAi2().onGeneralEvent(AIEventType.DEACTIVATE);
			}
		}
	}

	public boolean isMapRegionActive() {
		return !WorldConfig.WORLD_ACTIVE_TRACE || regionActive.get();
	}

	boolean isNeighboursActive() {
		for (int i = 0; i < neighbours.length; i++) {
			MapRegion r = neighbours[i];
			if (r != null && r.regionActive.get() && r.playerCount.get() > 0) {
				return true;
			}
		}
		return false;
	}

	public void revalidateZones(Creature creature) {
		for (Entry<Integer, TreeSet<ZoneInstance>> e = zoneMap.head(), mapEnd = zoneMap.tail(); (e = e.getNext()) != mapEnd;) {
			boolean foundZone = false;
			int category = e.getKey();
			TreeSet<ZoneInstance> zones = e.getValue();
			for (ZoneInstance zone : zones) {
				if (!creature.isSpawned() || (category != -1 && foundZone)) {
					zone.onLeave(creature);
					continue;
				}
				boolean result = zone.revalidate(creature);
				if (!result) {
					zone.onLeave(creature);
					continue;
				}
				if (category != -1) {
					foundZone = true;
				}
				zone.onEnter(creature);
			}
		}
	}

	public List<ZoneInstance> getZones(Creature creature) {
		List<ZoneInstance> z = new ArrayList<ZoneInstance>();
		for (Entry<Integer, TreeSet<ZoneInstance>> e = zoneMap.head(), mapEnd = zoneMap.tail(); (e = e.getNext()) != mapEnd;) {
			TreeSet<ZoneInstance> zones = e.getValue();
			for (ZoneInstance zone : zones) {
				if (zone.isInsideCreature(creature)) {
					z.add(zone);
				}
			}
		}
		return z;
	}

	public boolean onDie(Creature attacker, Creature target) {
		for (Entry<Integer, TreeSet<ZoneInstance>> e = zoneMap.head(), mapEnd = zoneMap.tail(); (e = e.getNext()) != mapEnd;) {
			TreeSet<ZoneInstance> zones = e.getValue();
			for (ZoneInstance zone : zones) {
				if (zone.isInsideCreature(target)) {
					if (zone.onDie(attacker, target)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public boolean isInsideZone(ZoneName zoneName, float x, float y, float z) {
		for (Entry<Integer, TreeSet<ZoneInstance>> e = zoneMap.head(), mapEnd = zoneMap.tail(); (e = e.getNext()) != mapEnd;) {
			TreeSet<ZoneInstance> zones = e.getValue();
			for (ZoneInstance zone : zones) {
				if (zone.getZoneTemplate().getName() != zoneName) {
					continue;
				}
				return zone.isInsideCordinate(x, y, z);
			}
		}
		return false;
	}

	public boolean isInsideZone(ZoneName zoneName, Creature creature) {
		for (Entry<Integer, TreeSet<ZoneInstance>> e = zoneMap.head(), mapEnd = zoneMap.tail(); (e = e.getNext()) != mapEnd;) {
			TreeSet<ZoneInstance> zones = e.getValue();
			for (ZoneInstance zone : zones) {
				if (zone.getZoneTemplate().getName() != zoneName) {
					continue;
				}
				return zone.isInsideCreature(creature);
			}
		}
		return false;
	}

	/**
	 * Item use zones always have the same names instances, while we have unique names; Thus, a special check for item use.
	 *
	 * @param zoneName
	 * @param creature
	 * @return
	 */
	public boolean isInsideItemUseZone(ZoneName zoneName, Creature creature) {
		for (Entry<Integer, TreeSet<ZoneInstance>> e = zoneMap.head(), mapEnd = zoneMap.tail(); (e = e.getNext()) != mapEnd;) {
			TreeSet<ZoneInstance> zones = e.getValue();
			for (ZoneInstance zone : zones) {
				if (!zone.getZoneTemplate().getXmlName().startsWith(zoneName.toString())) {
					continue;
				}
				if (!zone.isInsideCreature(creature)) {
					continue;
				}
				return true;
			}
		}
		return false;
	}

	private void createZoneMap(ZoneInstance[] zones) {
		zoneMap = new FastMap<Integer, TreeSet<ZoneInstance>>();
		for (int i = 0; i < zones.length; i++) {
			ZoneInstance zone = zones[i];
			int category = -1;
			if (zone.getZoneTemplate().getPriority() != 0) {
				category = zone.getZoneTemplate().getZoneType().ordinal();
			}
			TreeSet<ZoneInstance> zoneCategory = zoneMap.get(category);
			if (zoneCategory == null) {
				zoneCategory = new TreeSet<ZoneInstance>();
				zoneMap.put(category, zoneCategory);
			}
			zoneCategory.add(zone);
		}
	}

	public int getZoneCount() {
		return zoneCount;
	}
}
