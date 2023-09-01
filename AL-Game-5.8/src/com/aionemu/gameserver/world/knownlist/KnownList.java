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
package com.aionemu.gameserver.world.knownlist;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.configs.main.SecurityConfig;
import com.aionemu.gameserver.model.gameobjects.AionObject;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.world.MapRegion;

import javolution.util.FastMap;

/**
 * KnownList.
 *
 * @author -Nemesiss-
 * @modified kosyachok
 */
public class KnownList {

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(KnownList.class);
	/**
	 * Owner of this KnownList.
	 */
	protected final VisibleObject owner;
	/**
	 * List of objects that this KnownList owner known
	 */
	protected final FastMap<Integer, VisibleObject> knownObjects = new FastMap<Integer, VisibleObject>().shared();
	/**
	 * List of player that this KnownList owner known
	 */
	protected volatile FastMap<Integer, Player> knownPlayers;
	/**
	 * List of objects that this KnownList owner known
	 */
	protected final FastMap<Integer, VisibleObject> visualObjects = new FastMap<Integer, VisibleObject>().shared();
	/**
	 * List of player that this KnownList owner known
	 */
	protected volatile FastMap<Integer, Player> visualPlayers;
	private ReentrantLock lock = new ReentrantLock();

	/**
	 * @param owner
	 */
	public KnownList(VisibleObject owner) {
		this.owner = owner;
	}

	/**
	 * Do KnownList update.
	 */
	public void doUpdate() {
		lock.lock();
		try {
			forgetObjects();
			findVisibleObjects();
		}
		finally {
			lock.unlock();
		}
	}

	/**
	 * Clear known list. Used when object is despawned.
	 */
	public void clear() {
		for (VisibleObject object : knownObjects.values()) {
			object.getKnownList().del(owner, false);
		}
		knownObjects.clear();
		if (knownPlayers != null) {
			knownPlayers.clear();
		}
		visualObjects.clear();
		if (visualPlayers != null) {
			visualPlayers.clear();
		}
	}

	/**
	 * Check if object is known
	 *
	 * @param object
	 * @return true if object is known
	 */
	public boolean knowns(AionObject object) {
		return knownObjects.containsKey(object.getObjectId());
	}

	/**
	 * Add VisibleObject to this KnownList.
	 *
	 * @param object
	 */
	protected boolean add(VisibleObject object) {
		if (!isAwareOf(object)) {
			return false;
		}

		if (knownObjects.put(object.getObjectId(), object) == null) {
			if (object instanceof Player) {
				checkKnownPlayersInitialized();
				knownPlayers.put(object.getObjectId(), (Player) object);
			}

			addVisualObject(object);
			return true;
		}

		return false;
	}

	public void addVisualObject(VisibleObject object) {
		if (object instanceof Creature) {
			if (SecurityConfig.INVIS && object instanceof Player) {
				if (!owner.canSee((Player) object)) {
					return;
				}
			}

			if (visualObjects.put(object.getObjectId(), object) == null) {
				if (object instanceof Player) {
					checkVisiblePlayersInitialized();
					visualPlayers.put(object.getObjectId(), (Player) object);
				}
				owner.getController().see(object);
			}
		}
		else if (visualObjects.put(object.getObjectId(), object) == null) {
			owner.getController().see(object);
		}
	}

	/**
	 * Delete VisibleObject from this KnownList.
	 *
	 * @param object
	 */
	private void del(VisibleObject object, boolean isOutOfRange) {
		/**
		 * object was known.
		 */
		if (knownObjects.remove(object.getObjectId()) != null) {
			if (knownPlayers != null) {
				knownPlayers.remove(object.getObjectId());
			}
			delVisualObject(object, isOutOfRange);
		}
	}

	public void delVisualObject(VisibleObject object, boolean isOutOfRange) {
		if (visualObjects.remove(object.getObjectId()) != null) {
			if (visualPlayers != null) {
				visualPlayers.remove(object.getObjectId());
			}
			owner.getController().notSee(object, isOutOfRange);
		}
	}

	/**
	 * forget out of distance objects.
	 */
	private void forgetObjects() {
		for (VisibleObject object : knownObjects.values()) {
			if (!checkObjectInRange(object) && !object.getKnownList().checkReversedObjectInRange(owner)) {
				del(object, true);
				object.getKnownList().del(owner, true);
			}
		}
	}

	/**
	 * Find objects that are in visibility range.
	 */
	protected void findVisibleObjects() {
		if (owner == null || !owner.isSpawned()) {
			return;
		}

		MapRegion[] regions = owner.getActiveRegion().getNeighbours();
		for (int i = 0; i < regions.length; i++) {
			MapRegion r = regions[i];
			FastMap<Integer, VisibleObject> objects = r.getObjects();
			for (FastMap.Entry<Integer, VisibleObject> e = objects.head(), mapEnd = objects.tail(); (e = e.getNext()) != mapEnd;) {
				VisibleObject newObject = e.getValue();
				if (newObject == owner || newObject == null) {
					continue;
				}

				if (!isAwareOf(newObject)) {
					continue;
				}
				if (knownObjects.containsKey(newObject.getObjectId())) {
					continue;
				}

				if (!checkObjectInRange(newObject) && !newObject.getKnownList().checkReversedObjectInRange(owner)) {
					continue;
				}

				/**
				 * New object is not known.
				 */
				if (add(newObject)) {
					newObject.getKnownList().add(owner);
				}
			}
		}
	}

	/**
	 * Whether knownlist owner aware of found object (should be kept in knownlist)
	 *
	 * @param newObject
	 * @return
	 */
	protected boolean isAwareOf(VisibleObject newObject) {
		return true;
	}

	protected boolean checkObjectInRange(VisibleObject newObject) {
		// check if Z distance is greater than maxZvisibleDistance
		if (Math.abs(owner.getZ() - newObject.getZ()) > owner.getMaxZVisibleDistance()) {
			return false;
		}

		return MathUtil.isInRange(owner, newObject, owner.getVisibilityDistance());
	}

	/**
	 * Check can be overriden if new object has different known range and that value should be used
	 *
	 * @param newObject
	 * @return
	 */
	protected boolean checkReversedObjectInRange(VisibleObject newObject) {
		return false;
	}

	public void doOnAllNpcs(Visitor<Npc> visitor) {
		doOnAllNpcs(visitor, Integer.MAX_VALUE);
	}

	public int doOnAllNpcs(Visitor<Npc> visitor, int iterationLimit) {
		int counter = 0;
		try {
			for (FastMap.Entry<Integer, VisibleObject> e = knownObjects.head(), mapEnd = knownObjects.tail(); (e = e.getNext()) != mapEnd;) {
				VisibleObject newObject = e.getValue();
				if (newObject instanceof Npc) {
					if ((++counter) == iterationLimit) {
						break;
					}
					visitor.visit((Npc) newObject);
				}
			}
		}
		catch (Exception ex) {
			// log.error("Exception when running visitor on all npcs" + ex);
		}
		return counter;
	}

	public void doOnAllNpcsWithOwner(VisitorWithOwner<Npc, VisibleObject> visitor) {
		doOnAllNpcsWithOwner(visitor, Integer.MAX_VALUE);
	}

	public int doOnAllNpcsWithOwner(VisitorWithOwner<Npc, VisibleObject> visitor, int iterationLimit) {
		int counter = 0;
		try {
			for (FastMap.Entry<Integer, VisibleObject> e = knownObjects.head(), mapEnd = knownObjects.tail(); (e = e.getNext()) != mapEnd;) {
				VisibleObject newObject = e.getValue();
				if (newObject instanceof Npc) {
					if ((++counter) == iterationLimit) {
						break;
					}
					visitor.visit((Npc) newObject, owner);
				}
			}
		}
		catch (Exception ex) {
			// log.error("Exception when running visitor on all npcs" + ex);
		}
		return counter;
	}

	public void doOnAllPlayers(Visitor<Player> visitor) {
		if (knownPlayers == null) {
			return;
		}
		try {
			for (FastMap.Entry<Integer, Player> e = knownPlayers.head(), mapEnd = knownPlayers.tail(); (e = e.getNext()) != mapEnd;) {
				Player player = e.getValue();
				if (player != null) {
					visitor.visit(player);
				}
			}
		}
		catch (Exception ex) {
			// log.error("Exception when running visitor on all players" + ex);
		}
	}

	public void doOnAllObjects(Visitor<VisibleObject> visitor) {
		try {
			for (FastMap.Entry<Integer, VisibleObject> e = knownObjects.head(), mapEnd = knownObjects.tail(); (e = e.getNext()) != mapEnd;) {
				VisibleObject newObject = e.getValue();
				if (newObject != null) {
					visitor.visit(newObject);
				}
			}
		}
		catch (Exception ex) {
			// log.error("Exception when running visitor on all objects" + ex);
		}
	}

	public Map<Integer, VisibleObject> getKnownObjects() {
		return knownObjects;
	}

	public Map<Integer, VisibleObject> getVisibleObjects() {
		return visualObjects;
	}

	public Map<Integer, Player> getKnownPlayers() {
		return knownPlayers != null ? knownPlayers : Collections.<Integer, Player> emptyMap();
	}

	public Map<Integer, Player> getVisiblePlayers() {
		return visualPlayers != null ? visualPlayers : Collections.<Integer, Player> emptyMap();
	}

	final void checkKnownPlayersInitialized() {
		if (knownPlayers == null) {
			synchronized (this) {
				if (knownPlayers == null) {
					knownPlayers = new FastMap<Integer, Player>().shared();
				}
			}
		}
	}

	final void checkVisiblePlayersInitialized() {
		if (visualPlayers == null) {
			synchronized (this) {
				if (visualPlayers == null) {
					visualPlayers = new FastMap<Integer, Player>().shared();
				}
			}
		}
	}

	public VisibleObject getObject(int targetObjectId) {
		return this.knownObjects.get(targetObjectId);
	}
}
