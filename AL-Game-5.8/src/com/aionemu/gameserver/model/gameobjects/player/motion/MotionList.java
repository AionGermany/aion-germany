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
package com.aionemu.gameserver.model.gameobjects.player.motion;

import java.util.Collections;
import java.util.Map;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.MotionDAO;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MOTION;
import com.aionemu.gameserver.taskmanager.tasks.ExpireTimerTask;
import com.aionemu.gameserver.utils.PacketSendUtility;

import javolution.util.FastMap;

/**
 * @author MrPoke
 */
public class MotionList {

	private Player owner;
	private Map<Integer, Motion> activeMotions;
	private Map<Integer, Motion> motions;

	/**
	 * @param owner
	 */
	public MotionList(Player owner) {
		this.owner = owner;
	}

	/**
	 * @return the activeMotions
	 */
	public Map<Integer, Motion> getActiveMotions() {
		if (activeMotions == null) {
			return Collections.emptyMap();
		}
		return activeMotions;
	}

	/**
	 * @return the motions
	 */
	public Map<Integer, Motion> getMotions() {
		if (motions == null) {
			return Collections.emptyMap();
		}
		return motions;
	}

	public void add(Motion motion, boolean persist) {
		if (motions == null) {
			motions = new FastMap<Integer, Motion>();
		}
		if (motions.containsKey(motion.getId()) && motion.getExpireTime() == 0) {
			remove(motion.getId());
		}
		motions.put(motion.getId(), motion);
		if (motion.isActive()) {
			if (activeMotions == null) {
				activeMotions = new FastMap<Integer, Motion>();
			}
			Motion old = activeMotions.put(Motion.motionType.get(motion.getId()), motion);
			if (old != null) {
				old.setActive(false);
				DAOManager.getDAO(MotionDAO.class).updateMotion(owner.getObjectId(), old);
			}
		}
		if (persist) {
			if (motion.getExpireTime() != 0) {
				ExpireTimerTask.getInstance().addTask(motion, owner);
			}
			DAOManager.getDAO(MotionDAO.class).storeMotion(owner.getObjectId(), motion);
		}
	}

	public boolean remove(int motionId) {
		Motion motion = motions.remove(motionId);
		if (motion != null) {
			PacketSendUtility.sendPacket(owner, new SM_MOTION((short) motionId));
			DAOManager.getDAO(MotionDAO.class).deleteMotion(owner.getObjectId(), motionId);
			if (motion.isActive()) {
				activeMotions.remove(Motion.motionType.get(motionId));
				return true;
			}
		}
		return false;
	}

	public void setActive(int motionId, int motionType) {
		if (motionId != 0) {
			Motion motion = motions.get(motionId);
			if (motion == null || motion.isActive()) {
				return;
			}
			if (activeMotions == null) {
				activeMotions = new FastMap<Integer, Motion>();
			}
			Motion old = activeMotions.put(motionType, motion);
			if (old != null) {
				old.setActive(false);
				DAOManager.getDAO(MotionDAO.class).updateMotion(owner.getObjectId(), old);
			}
			motion.setActive(true);
			DAOManager.getDAO(MotionDAO.class).updateMotion(owner.getObjectId(), motion);
		}
		else if (activeMotions != null) {
			Motion old = activeMotions.remove(motionType);
			if (old == null) {
				return; // TODO packet hack??
			}
			old.setActive(false);
			DAOManager.getDAO(MotionDAO.class).updateMotion(owner.getObjectId(), old);
		}
		PacketSendUtility.sendPacket(owner, new SM_MOTION((short) motionId, (byte) motionType));
		PacketSendUtility.broadcastPacket(owner, new SM_MOTION(owner.getObjectId(), activeMotions), true);
	}
}
