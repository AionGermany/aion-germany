package com.aionemu.gameserver.services.rift;

/**
 * @author CoolyT
 */
public class RiftStatistics {

	int worldMap = 0;
	int spawnedRifts = 0;
	int spawnedNpcs = 0;
	boolean isSpawned = false;
	boolean isVortex = false;

	/**
	 * @return the worldMap
	 */
	public int getWorldMap() {
		return worldMap;
	}

	/**
	 * @param worldMap
	 *            the worldMap to set
	 */
	public void setWorldMap(int worldMap) {
		this.worldMap = worldMap;
	}

	/**
	 * @return the spawnedRifts
	 */
	public int getSpawnedRifts() {
		return spawnedRifts;
	}

	/**
	 * @param spawnedRifts
	 *            the spawnedRifts to set
	 */
	public void setSpawnedRifts(int spawnedRifts) {
		this.spawnedRifts = spawnedRifts;
	}

	/**
	 * @param spawnedRifts
	 *            the spawnedRifts to set
	 */
	public void addSpawnedRifts(int spawnedRifts) {
		this.spawnedRifts += spawnedRifts;
	}

	/**
	 * @return the spawnedNpcs
	 */
	public int getSpawnedNpcs() {
		return spawnedNpcs;
	}

	/**
	 * @param spawnedNpcs
	 *            the spawnedNpcs to set
	 */
	public void setSpawnedNpcs(int spawnedNpcs) {
		this.spawnedNpcs = spawnedNpcs;
	}

	/**
	 * @param spawnedNpcs
	 *            the spawnedNpcs to set
	 */
	public void addSpawnedNpcs(int spawnedNpcs) {
		this.spawnedNpcs += spawnedNpcs;
	}

	/**
	 * @return the isSpawned
	 */
	public boolean isSpawned() {
		return isSpawned;
	}

	/**
	 * @param isSpawned
	 *            the isSpawned to set
	 */
	public void setSpawned(boolean isSpawned) {
		this.isSpawned = isSpawned;
	}

	/**
	 * @return the isVortex
	 */
	public boolean isVortex() {
		return isVortex;
	}

	/**
	 * @param isVortex
	 *            the isVortex to set
	 */
	public void setVortex(boolean isVortex) {
		this.isVortex = isVortex;
	}

}
