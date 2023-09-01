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
package com.aionemu.gameserver.model.gameobjects;

import com.aionemu.gameserver.controllers.VisibleObjectController;
import com.aionemu.gameserver.model.templates.VisibleObjectTemplate;
import com.aionemu.gameserver.model.templates.npc.NpcTemplate;
import com.aionemu.gameserver.model.templates.npc.NpcTemplateType;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.world.MapRegion;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.WorldPosition;
import com.aionemu.gameserver.world.WorldType;
import com.aionemu.gameserver.world.knownlist.KnownList;

/**
 * This class is representing visible objects. It's a base class for all in-game objects that can be spawned in the world at some particular position (such as players, npcs).<br>
 * <br>
 * Objects of this class, as can be spawned in game, can be seen by other visible objects. To keep track of which objects are already "known" by this visible object and which are not, VisibleObject is
 * containing {@link KnownList} which is responsible for holding this information.
 *
 * @author -Nemesiss-
 */
public abstract class VisibleObject extends AionObject {

	protected VisibleObjectTemplate objectTemplate;
	// how far player will see visible object
	public static final float VisibilityDistance = 95;
	// maxZvisibleDistance
	public static final float maxZvisibleDistance = 95;

	/**
	 * Constructor.
	 *
	 * @param objId
	 * @param objectTemplate
	 */
	public VisibleObject(int objId, VisibleObjectController<? extends VisibleObject> controller, SpawnTemplate spawnTemplate, VisibleObjectTemplate objectTemplate, WorldPosition position) {
		super(objId);
		this.controller = controller;
		this.position = position;
		this.spawn = spawnTemplate;
		this.objectTemplate = objectTemplate;
	}

	/**
	 * Position of object in the world.
	 */
	protected WorldPosition position;
	/**
	 * KnownList of this VisibleObject.
	 */
	private KnownList knownlist;
	/**
	 * Controller of this VisibleObject
	 */
	private final VisibleObjectController<? extends VisibleObject> controller;
	/**
	 * Visible object's target
	 */
	private VisibleObject target;
	/**
	 * Spawn template of this visibleObject. .
	 */
	private SpawnTemplate spawn;

	/**
	 * Returns current WorldRegion AionObject is in.
	 */
	public MapRegion getActiveRegion() {
		return getPosition().getMapRegion();
	}

	public int getInstanceId() {
		return getPosition().getInstanceId();
	}

	/**
	 * Return World map id.
	 */
	public int getWorldId() {
		return getPosition().getMapId();
	}

	/**
	 * Return the WorldType of the current location
	 */
	public WorldType getWorldType() {
		return World.getInstance().getWorldMap(getWorldId()).getWorldType();
	}

	/**
	 * Return World position x
	 */
	public float getX() {
		return getPosition().getX();
	}

	/**
	 * Return World position y
	 */
	public float getY() {
		return getPosition().getY();
	}

	/**
	 * Return World position z
	 */
	public float getZ() {
		return getPosition().getZ();
	}

	public void setXYZH(Float x, Float y, Float z, Byte h) {
		getPosition().setXYZH(x, y, z, h);
	}

	/**
	 * Heading of the object. Values from <0,120)
	 */
	public byte getHeading() {
		return getPosition().getHeading();
	}

	/**
	 * Return object position
	 *
	 * @return position.
	 */
	public WorldPosition getPosition() {
		return position;
	}

	/**
	 * Check if object is spawned.
	 *
	 * @return true if object is spawned.
	 */
	public boolean isSpawned() {
		return getPosition().isSpawned();
	}

	/**
	 * @return
	 */
	public boolean isInWorld() {
		return World.getInstance().findVisibleObject(getObjectId()) != null;
	}

	/**
	 * Check if map is instance
	 *
	 * @return true if object in one of the instance maps
	 */
	public boolean isInInstance() {
		return getPosition().isInstanceMap();
	}

	public void clearKnownlist() {
		getKnownList().clear();
	}

	public void updateKnownlist() {
		getKnownList().doUpdate();
	}

	public boolean canSee(Creature creature) {
		return creature != null;
	}

	/**
	 * Set KnownList to this VisibleObject
	 *
	 * @param knownlist
	 */
	public void setKnownlist(KnownList knownlist) {
		this.knownlist = knownlist;
	}

	/**
	 * Returns KnownList of this VisibleObject.
	 *
	 * @return knownList.
	 */
	public KnownList getKnownList() {
		return knownlist;
	}

	/**
	 * Return VisibleObjectController of this VisibleObject
	 *
	 * @return VisibleObjectController.
	 */
	public VisibleObjectController<? extends VisibleObject> getController() {
		return controller;
	}

	/**
	 * @return VisibleObject
	 */
	public final VisibleObject getTarget() {
		return target;
	}

	/**
	 * @return distance to target or 0 if no target
	 */
	public float getDistanceToTarget() {
		VisibleObject currTarget = target;
		if (currTarget == null) {
			return 0;
		}
		return (float) MathUtil.getDistance(getX(), getY(), getZ(), currTarget.getX(), currTarget.getY(), currTarget.getZ()) - this.getObjectTemplate().getBoundRadius().getCollision() - currTarget.getObjectTemplate().getBoundRadius().getCollision();
	}

	/**
	 * @param creature
	 */
	public void setTarget(VisibleObject creature) {
		target = creature;
	}

	/**
	 * @param objectId
	 * @return target is object with id equal to objectId
	 */
	public boolean isTargeting(int objectId) {
		return target != null && target.getObjectId() == objectId;
	}

	/**
	 * Return spawn template of this VisibleObject
	 *
	 * @return SpawnTemplate
	 */
	public SpawnTemplate getSpawn() {
		return spawn;
	}

	public void setSpawn(SpawnTemplate spawn) {
		this.spawn = spawn;
	}

	/**
	 * @return the objectTemplate
	 */
	public VisibleObjectTemplate getObjectTemplate() {
		return objectTemplate;
	}

	/**
	 * @param objectTemplate
	 *            the objectTemplate to set
	 */
	public void setObjectTemplate(VisibleObjectTemplate objectTemplate) {
		this.objectTemplate = objectTemplate;
	}

	/**
	 * @param position
	 */
	public void setPosition(WorldPosition position) {
		this.position = position;
	}

	public float getVisibilityDistance() {
		if (this instanceof Npc) {
			NpcTemplate npcTemplate = (NpcTemplate) this.getObjectTemplate();
			if (npcTemplate.getNpcTemplateType().equals(NpcTemplateType.FLAG) || npcTemplate.getNpcTemplateType().equals(NpcTemplateType.RAID_MONSTER)) {
				return Integer.MAX_VALUE;
			}
		}
		return VisibilityDistance;
	}

	public float getMaxZVisibleDistance() {
		if (this instanceof Npc) {
			NpcTemplate npcTemplate = (NpcTemplate) this.getObjectTemplate();
			if (npcTemplate.getNpcTemplateType().equals(NpcTemplateType.FLAG) || npcTemplate.getNpcTemplateType().equals(NpcTemplateType.RAID_MONSTER)) {
				return Integer.MAX_VALUE;
			}
		}
		return maxZvisibleDistance;
	}

	@Override
	public String toString() {
		if (objectTemplate == null) {
			return super.toString();
		}
		return objectTemplate.getName() + " (" + objectTemplate.getTemplateId() + ")";
	}
}
