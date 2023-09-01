/**
 * 
 */
package com.aionemu.gameserver.model.gameobjects.player;

/**
 * @author CoolyT
 */
public class PlayerUpgradeArcade {

	private int frenzyPoints = 0;
	private int frenzyCount = 0;
	private int frenzyLevel = 1;
	private int failedLevel = 1;
	private boolean isFrenzy = false;
	private boolean reTry = false;
	private boolean failed = false;

	public int getFrenzyPoints() {
		return frenzyPoints;
	}

	public void setFrenzyPoints(int frenzyPoints) {
		this.frenzyPoints = frenzyPoints;
	}

	public int getFrenzyCount() {
		return frenzyCount;
	}

	public void setFrenzyCount(int frenzyCount) {
		this.frenzyCount = frenzyCount;
	}

	public int getFrenzyLevel() {
		return frenzyLevel;
	}

	public void setFrenzyLevel(int frenzyLevel) {
		this.frenzyLevel = frenzyLevel;
	}

	public int getFailedLevel() {
		return failedLevel;
	}

	public void setFailedLevel(int failedLevel) {
		this.failedLevel = failedLevel;
	}

	public boolean isFrenzy() {
		return isFrenzy;
	}

	public void setFrenzy(boolean isFrenzy) {
		this.isFrenzy = isFrenzy;
	}

	public boolean isReTry() {
		return reTry;
	}

	public void setReTry(boolean reTry) {
		this.reTry = reTry;
	}

	public boolean isFailed() {
		return failed;
	}

	public void setFailed(boolean failed) {
		this.failed = failed;
	}

	public void reset() {
		this.isFrenzy = false;
		this.failed = false;
		this.frenzyLevel = 1;
		this.failedLevel = 1;
		this.reTry = false;
	}

}
