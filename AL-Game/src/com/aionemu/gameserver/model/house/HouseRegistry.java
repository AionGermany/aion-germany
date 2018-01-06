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
package com.aionemu.gameserver.model.house;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.PlayerRegisteredItemsDAO;
import com.aionemu.gameserver.model.gameobjects.HouseDecoration;
import com.aionemu.gameserver.model.gameobjects.HouseObject;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.templates.housing.PartType;

import javolution.util.FastList;
import javolution.util.FastMap;

/**
 * @author Rolandas
 */
public class HouseRegistry {

	private static final Logger log = LoggerFactory.getLogger(HouseRegistry.class);
	private House owner;
	private FastMap<Integer, HouseObject<?>> objects;
	private FastMap<Integer, HouseDecoration> customParts;
	private HouseDecoration[] defaultParts = new HouseDecoration[28];
	private PersistentState persistentState = PersistentState.UPDATED;

	public HouseRegistry(House owner) {
		this.owner = owner;
		this.objects = FastMap.newInstance();
		this.customParts = FastMap.newInstance();
	}

	public House getOwner() {
		return owner;
	}

	/**
	 * Get all objects including deleted
	 *
	 * @return
	 */
	public FastList<HouseObject<?>> getObjects() {
		FastList<HouseObject<?>> temp = FastList.newInstance();
		for (HouseObject<?> obj : objects.values()) {
			temp.add(obj);
		}
		return temp;
	}

	public FastList<HouseObject<?>> getSpawnedObjects() {
		FastList<HouseObject<?>> temp = FastList.newInstance();
		for (HouseObject<?> obj : objects.values()) {
			if (obj.isSpawnedByPlayer() && obj.getPersistentState() != PersistentState.DELETED) {
				temp.add(obj);
			}
		}
		return temp;
	}

	public FastList<HouseObject<?>> getNotSpawnedObjects() {
		FastList<HouseObject<?>> temp = FastList.newInstance();
		for (HouseObject<?> obj : objects.values()) {
			if (!obj.isSpawnedByPlayer() && obj.getPersistentState() != PersistentState.DELETED) {
				temp.add(obj);
			}
		}
		return temp;
	}

	public HouseObject<?> getObjectByObjId(int itemObjId) {
		return objects.get(itemObjId);
	}

	public boolean putObject(HouseObject<?> houseObject) {
		if (objects.containsKey(houseObject.getObjectId())) {
			return false;
		}

		if (houseObject.getPersistentState() != PersistentState.NEW) {
			log.error("Inserting not new HouseObject: " + houseObject.getObjectId());
			return false;
		}

		objects.put(houseObject.getObjectId(), houseObject);
		setPersistentState(PersistentState.UPDATE_REQUIRED);
		return true;
	}

	public HouseObject<?> removeObject(int itemObjId) {
		if (!objects.containsKey(itemObjId)) {
			return null;
		}

		HouseObject<?> oldObject = objects.get(itemObjId);
		if (oldObject.getPersistentState() == PersistentState.NEW) {
			discardObject(itemObjId);
		}
		else {
			oldObject.setPersistentState(PersistentState.DELETED);
		}
		setPersistentState(PersistentState.UPDATE_REQUIRED);

		return oldObject;
	}

	/**
	 * In packets used custom parts are not included (kinda semi-deleted)
	 */
	public FastList<HouseDecoration> getCustomParts() {
		FastList<HouseDecoration> temp = FastList.newInstance();
		for (HouseDecoration decor : customParts.values()) {
			if (decor.getPersistentState() != PersistentState.DELETED && !decor.isUsed()) {
				temp.add(decor);
			}
		}
		return temp;
	}

	public HouseDecoration getCustomPartByType(PartType partType, int floor) {
		for (HouseDecoration deco : customParts.values()) {
			if (deco.getPersistentState() != PersistentState.DELETED && deco.getTemplate().getType() == partType) {
				if (floor == deco.getFloor()) {
					return deco;
				}
			}
		}
		return null;
	}

	public HouseDecoration getCustomPartByObjId(int itemObjId) {
		return customParts.get(itemObjId);
	}

	public HouseDecoration getCustomPartByPartId(int partId, int floor) {
		for (HouseDecoration deco : customParts.values()) {
			if (deco.getPersistentState() != PersistentState.DELETED && deco.getTemplate().getId() == partId && deco.getFloor() == floor) {
				return deco;
			}
		}
		return null;
	}

	public int getCustomPartCountByPartId(int partId) {
		int counter = 0;
		for (HouseDecoration deco : customParts.values()) {
			if (deco.getPersistentState() != PersistentState.DELETED && deco.getTemplate().getId() == partId) {
				counter++;
			}
		}
		return counter;
	}

	public boolean putCustomPart(HouseDecoration houseDeco) {
		if (customParts.containsKey(houseDeco.getObjectId())) {
			return false;
		}
		if (houseDeco.getPersistentState() != PersistentState.NEW) {
			log.error("Inserting not new HouseDecoration: " + houseDeco.getObjectId());
			return false;
		}

		customParts.put(houseDeco.getObjectId(), houseDeco);
		setPersistentState(PersistentState.UPDATE_REQUIRED);
		return true;
	}

	public HouseDecoration removeCustomPart(int itemObjId) {
		HouseDecoration obj = null;

		if (customParts.containsKey(itemObjId)) {
			obj = customParts.get(itemObjId);
			obj.setPersistentState(PersistentState.DELETED);
			setPersistentState(PersistentState.UPDATE_REQUIRED);
		}
		return obj;
	}

	public FastList<HouseDecoration> getDefaultParts() {
		FastList<HouseDecoration> temp = FastList.newInstance();
		for (HouseDecoration deco : defaultParts) {
			if (deco != null) {
				temp.add(deco);
			}
		}
		return temp;
	}

	/**
	 * Returns default decoration for the part type<br>
	 * Currently they are the same for all house floors
	 *
	 * @param partType
	 * @return
	 */
	public HouseDecoration getDefaultPartByType(PartType partType, int floor) {
		return defaultParts[partType.getStartLineNr() + floor];
	}

	public void putDefaultPart(HouseDecoration houseDeco, int floor) {
		defaultParts[houseDeco.getTemplate().getType().getStartLineNr() + floor] = houseDeco;
		houseDeco.setPersistentState(PersistentState.NOACTION);
	}

	/**
	 * Get all decoration parts including deleted
	 *
	 * @return
	 */
	public FastList<HouseDecoration> getAllParts() {
		FastList<HouseDecoration> temp = FastList.newInstance();
		for (HouseDecoration deco : defaultParts) {
			if (deco != null) {
				temp.add(deco);
			}
		}
		for (HouseDecoration decor : customParts.values()) {
			temp.add(decor);
		}
		return temp;
	}

	public HouseDecoration getRenderPart(PartType partType, int floor) {
		for (HouseDecoration decor : customParts.values()) {
			if (decor.getTemplate().getType() == partType && decor.isUsed() && decor.getFloor() == floor) {
				return decor;
			}
		}
		return getDefaultPartByType(partType, floor);
	}

	public void setPartInUse(HouseDecoration decorationUse, int floor) {
		HouseDecoration defaultDecor = defaultParts[decorationUse.getTemplate().getType().getStartLineNr() + floor];
		if (defaultDecor.getTemplate().getId() == decorationUse.getTemplate().getId()) {
			defaultDecor.setUsed(true);
			for (HouseDecoration decor : customParts.values()) {
				if (decor.getTemplate().getType() != decorationUse.getTemplate().getType()) {
					continue;
				}
				if (decor.getPersistentState() != PersistentState.DELETED) {
					if (decor.isUsed()) {
						decor.setUsed(false);
						decor.setFloor(-1);
						if (decor.getPersistentState() == PersistentState.NEW) {
							discardPart(decor);
						}
						else {
							decor.setPersistentState(PersistentState.DELETED);
						}
					}
				}
			}
			return;
		}
		for (HouseDecoration decor : customParts.values()) {
			if (decor.getTemplate().getType() != decorationUse.getTemplate().getType()) {
				continue;
			}
			if (decor.getPersistentState() != PersistentState.DELETED) {
				if (decorationUse.equals(decor)) {
					decor.setUsed(true);
					decor.setFloor(floor);
					defaultDecor.setUsed(false);
				}
				else {
					if (decor.isUsed() && !decorationUse.equals(decor) && decor.getFloor() == floor) {
						decor.setUsed(false);
						decor.setFloor(-1);
						if (decor.getPersistentState() == PersistentState.NEW) {
							discardPart(decor);
						}
						else {
							decor.setPersistentState(PersistentState.DELETED);
						}
					}
				}
			}
		}
	}

	public void discardObject(Integer objectId) {
		objects.remove(objectId);
	}

	public void discardPart(HouseDecoration decor) {
		customParts.remove(decor.getObjectId());
	}

	public void save() {
		if (persistentState == PersistentState.UPDATE_REQUIRED) {
			DAOManager.getDAO(PlayerRegisteredItemsDAO.class).store(this, getOwner().getOwnerId());
		}
	}

	public final PersistentState getPersistentState() {
		return persistentState;
	}

	public final void setPersistentState(PersistentState persistentState) {
		this.persistentState = persistentState;
	}

	public int size() {
		return objects.size() + customParts.size();
	}

	public void despawnObjects() {
		if (getSpawnedObjects().isEmpty()) {
			DAOManager.getDAO(PlayerRegisteredItemsDAO.class).resetRegistry(owner.getOwnerId());
		}
		else {
			despawnObjects(true);
		}
	}

	/**
	 * Use before switching house
	 */
	public void despawnObjects(boolean remove) {
		for (HouseObject<?> obj : getSpawnedObjects()) {
			if (obj.isInWorld()) {
				obj.getController().onDelete();
				obj.clearKnownlist();
			}
			if (remove) {
				obj.removeFromHouse();
			}
		}
		if (remove) {
			setPersistentState(PersistentState.UPDATE_REQUIRED);
			save();
		}
	}
}
