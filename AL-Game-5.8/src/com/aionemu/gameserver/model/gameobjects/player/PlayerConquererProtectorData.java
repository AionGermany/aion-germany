/**
 * 
 */
package com.aionemu.gameserver.model.gameobjects.player;

/**
 * @author CoolyT
 */
public class PlayerConquererProtectorData {

	private boolean isProtector = false;
	private int ProtectorBuffId = 0;
	private int ConquerorBuffId = 0;
	private int killCountAsConquerer = 0;
	private int killCountAsProtector = 0;

	public boolean isProtector() {
		return isProtector;
	}

	private void setProtector(boolean isProtector) {
		this.isProtector = isProtector;
	}

	public int getProtectorBuffLevel() {
		return ProtectorBuffId;
	}

	public void setProtectorBuffId(int protectorBuffId) {
		ProtectorBuffId = protectorBuffId;
	}

	public int getConquerorBuffLevel() {
		return ConquerorBuffId;
	}

	public void setConquerorBuffId(int conquerorBuffId) {
		ConquerorBuffId = conquerorBuffId;
	}

	public int getKillCountAsConquerer() {
		return killCountAsConquerer;
	}

	public void setKillCountAsConquerer(int killCountAsConquerer) {
		setProtector(false);
		this.killCountAsConquerer = killCountAsConquerer;
	}

	public int getKillCountAsProtector() {
		return killCountAsProtector;
	}

	public void setKillCountAsProtector(int killCountAsProtector) {
		setProtector(true);
		this.killCountAsProtector = killCountAsProtector;
	}

}
