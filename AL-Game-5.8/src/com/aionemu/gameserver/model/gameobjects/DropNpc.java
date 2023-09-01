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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.aionemu.gameserver.model.gameobjects.player.Player;

/**
 * @author Simple
 */
public class DropNpc {

	private Collection<Integer> allowedList = new ArrayList<Integer>();
	private Collection<Player> inRangePlayers = new ArrayList<Player>();
	private Collection<Player> playerStatus = new ArrayList<Player>();
	private Player lootingPlayer = null;
	private int distributionId = 0;
	private boolean distributionType;
	private int currentIndex = 0;
	private int groupSize = 0;
	private boolean isFreeForAll = false;
	private final int npcUniqueId;
	private long reamingDecayTime;

	public DropNpc(int npcUniqueId) {
		this.npcUniqueId = npcUniqueId;
	}

	public void setPlayersObjectId(List<Integer> allowedList) {
		this.allowedList = allowedList;
	}

	public void setPlayerObjectId(Integer object) {
		if (!allowedList.contains(object)) {
			allowedList.add(object);
		}
	}

	public Collection<Integer> getPlayersObjectId() {
		return allowedList;
	}

	/**
	 * @return true if playerObjId is found in list
	 */
	public boolean containsKey(int playerObjId) {
		return allowedList.contains(playerObjId);
	}

	/**
	 * @param player
	 *            the lootingPlayer to set
	 */
	public void setBeingLooted(Player player) {
		this.lootingPlayer = player;
	}

	/**
	 * @return lootingPlayer
	 */
	public Player getBeingLooted() {
		return lootingPlayer;
	}

	/**
	 * @return the beingLooted
	 */
	public boolean isBeingLooted() {
		return lootingPlayer != null;
	}

	/**
	 * @param distributionId
	 */
	public void setDistributionId(int distributionId) {
		this.distributionId = distributionId;
	}

	/**
	 * @return the DistributionId
	 */
	public int getDistributionId() {
		return distributionId;
	}

	/**
	 * @param distributionType
	 */
	public void setDistributionType(boolean distributionType) {
		this.distributionType = distributionType;
	}

	/**
	 * @return the DistributionType
	 */
	public boolean getDistributionType() {
		return distributionType;
	}

	/**
	 * @param currentIndex
	 */
	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}

	/**
	 * @return currentIndex
	 */
	public int getCurrentIndex() {
		return currentIndex;
	}

	/**
	 * @param groupSize
	 */
	public void setGroupSize(int groupSize) {
		this.groupSize = groupSize;
	}

	/**
	 * @return groupSize
	 */
	public int getGroupSize() {
		return groupSize;
	}

	/**
	 * @param inRangePlayers
	 */
	public void setInRangePlayers(Collection<Player> inRangePlayers) {
		this.inRangePlayers = inRangePlayers;
	}

	/**
	 * @return the inRangePlayers
	 */
	public Collection<Player> getInRangePlayers() {
		return inRangePlayers;
	}

	/**
	 * @param addPlayerStatus
	 */
	public void addPlayerStatus(Player player) {
		playerStatus.add(player);
	}

	/**
	 * @param delPlayerStatus
	 */
	public void delPlayerStatus(Player player) {
		playerStatus.remove(player);
	}

	/**
	 * @return the playerStatus
	 */
	public Collection<Player> getPlayerStatus() {
		return playerStatus;
	}

	/**
	 * @return true if player is found in list
	 */
	public boolean containsPlayerStatus(Player player) {
		return playerStatus.contains(player);
	}

	/**
	 * @return isFreeForAll.
	 */
	public boolean isFreeForAll() {
		return isFreeForAll;
	}

	public void startFreeForAll() {
		isFreeForAll = true;
		distributionId = 0;
		allowedList.clear();
	}

	public final int getNpcUniqueId() {
		return npcUniqueId;
	}

	public long getReamingDecayTime() {
		return reamingDecayTime;
	}

	public void setReamingDecayTime(long reamingDecayTime) {
		this.reamingDecayTime = reamingDecayTime;
	}
}
