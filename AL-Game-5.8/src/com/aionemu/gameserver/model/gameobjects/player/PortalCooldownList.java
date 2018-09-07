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
package com.aionemu.gameserver.model.gameobjects.player;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INSTANCE_INFO;
import com.aionemu.gameserver.utils.PacketSendUtility;

import javolution.util.FastMap;

/**
 * @author ATracer
 */
public class PortalCooldownList {

	private Player owner;
	private FastMap<Integer, PortalCooldownItem> portalCooldowns;

	/**
	 * @param owner
	 */
	PortalCooldownList(Player owner) {
		this.owner = owner;
	}

	/**
	 * @param worldId
	 * @return
	 */
	public boolean isPortalUseDisabled(int worldId) {
		if (portalCooldowns == null || !portalCooldowns.containsKey(worldId)) {
			return false;
		}

		PortalCooldownItem coolDown = portalCooldowns.get(worldId);
		if (coolDown == null) {
			return false;
		}

		if (DataManager.INSTANCE_COOLTIME_DATA.getInstanceEntranceCountByWorldId(worldId) == 0 || coolDown.getEntryCount() < DataManager.INSTANCE_COOLTIME_DATA.getInstanceEntranceCountByWorldId(worldId)) {
			return false;
		}

		if (coolDown.getCooldown() < System.currentTimeMillis()) {
			portalCooldowns.remove(worldId);
			return false;
		}

		return true;
	}

	/**
	 * @param worldId
	 * @return
	 */
	public long getPortalCooldown(int worldId) {
		if (portalCooldowns == null || !portalCooldowns.containsKey(worldId)) {
			return 0;
		}

		return portalCooldowns.get(worldId).getCooldown();
	}

	public long getEntryCount(int worldId) {
		if (portalCooldowns == null || !portalCooldowns.containsKey(worldId)) {
			return 0;
		}

		return portalCooldowns.get(worldId).getEntryCount();
	}

	public PortalCooldownItem getPortalCooldownItem(int worldId) {
		if (portalCooldowns == null || !portalCooldowns.containsKey(worldId)) {
			return null;
		}
		return portalCooldowns.get(worldId);
	}

	public FastMap<Integer, PortalCooldownItem> getPortalCoolDowns() {
		return portalCooldowns;
	}

	public void setPortalCoolDowns(FastMap<Integer, PortalCooldownItem> portalCoolDowns) {
		this.portalCooldowns = portalCoolDowns;
	}

	/**
	 * @param worldId
	 * @param useDelay
	 */
	public void addPortalCooldown(int worldId, int entryCount, long useDelay) {
		if (portalCooldowns == null) {
			portalCooldowns = new FastMap<Integer, PortalCooldownItem>();
		}
		portalCooldowns.put(worldId, new PortalCooldownItem(worldId, entryCount, useDelay));

		if (owner.isInTeam()) {
			owner.getCurrentTeam().sendPacket(new SM_INSTANCE_INFO(owner, worldId));
		}
		else {
			PacketSendUtility.sendPacket(owner, new SM_INSTANCE_INFO(owner, worldId));
		}
	}

	/**
	 * @param worldId
	 */
	public void removePortalCoolDown(int worldId) {
		if (portalCooldowns != null) {
			portalCooldowns.remove(worldId);
		}

		if (owner.isInTeam()) {
			owner.getCurrentTeam().sendPacket(new SM_INSTANCE_INFO(owner, worldId));
		}
		else {
			PacketSendUtility.sendPacket(owner, new SM_INSTANCE_INFO(owner, worldId));
		}
	}

	public void addEntry(int worldId) {
		if (portalCooldowns != null && portalCooldowns.containsKey(worldId)) {
			portalCooldowns.get(worldId).setEntryCount(portalCooldowns.get(worldId).getEntryCount() + 1);
		}

		if (owner.isInTeam()) {
			owner.getCurrentTeam().sendPacket(new SM_INSTANCE_INFO(owner, worldId));
		}
		else {
			PacketSendUtility.sendPacket(owner, new SM_INSTANCE_INFO(owner, worldId));
		}
	}

	public void reduceEntry(int worldId) {
		if (portalCooldowns != null && portalCooldowns.containsKey(worldId)) {
			portalCooldowns.get(worldId).setEntryCount(portalCooldowns.get(worldId).getEntryCount() - 1);
		}

		if (portalCooldowns.get(worldId).getEntryCount() == 0) {
			removePortalCoolDown(worldId);
			return;
		}

		if (owner.isInTeam()) {
			owner.getCurrentTeam().sendPacket(new SM_INSTANCE_INFO(owner, worldId));
		}
		else {
			PacketSendUtility.sendPacket(owner, new SM_INSTANCE_INFO(owner, worldId));
		}
	}

	/**
	 * @return
	 */
	public boolean hasCooldowns() {
		return portalCooldowns != null && portalCooldowns.size() > 0;
	}

	/**
	 * @return
	 */
	public int size() {
		return portalCooldowns != null ? portalCooldowns.size() : 0;
	}
}
