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
package com.aionemu.gameserver.model.instance.playerreward;

import com.aionemu.gameserver.model.Race;

/**
 * @author Alcapwnd
 */
public class KamarBattlefieldPlayerReward extends InstancePlayerReward {

	private int timeBonus;
	private long logoutTime;
	private float timeBonusModifier;
	private Race race;
	private int rewardAp;
	private int rewardGp;
	private int bonusAp;
	private int bonusGp;
	private int reward1;
	private int reward2;
	private int bonusReward;
	private int bonusReward2;
	private float rewardCount;

	public KamarBattlefieldPlayerReward(Integer object, int timeBonus, Race race) {
		super(object);
		this.timeBonus = timeBonus;
		timeBonusModifier = ((float) this.timeBonus / (float) 660000);
		this.race = race;
	}

	public float getParticipation() {
		return (float) getTimeBonus() / timeBonus;
	}

	public int getScorePoints() {
		return timeBonus + getPoints();
	}

	public int getTimeBonus() {
		return timeBonus > 0 ? timeBonus : 0;
	}

	public void updateLogOutTime() {
		logoutTime = System.currentTimeMillis();
	}

	public void updateBonusTime() {
		int offlineTime = (int) (System.currentTimeMillis() - logoutTime);
		timeBonus -= offlineTime * timeBonusModifier;
	}

	public Race getRace() {
		return race;
	}

	public int getRewardAp() {
		return rewardAp;
	}

	public int getBonusAp() {
		return bonusAp;
	}

	public void setBonusAp(int ap) {
		this.bonusAp = ap;
	}

	public void setRewardAp(int ap) {
		this.rewardAp = ap;
	}

	public int getReward1() {
		return reward1;
	}

	public int getReward2() {
		return reward2;
	}

	public int getBonusReward() {
		return bonusReward;
	}

	public int getRewardCount() {
		return (int) rewardCount;
	}

	public void setReward1(int reward) {
		this.reward1 = reward;
	}

	public void setReward2(int reward) {
		this.reward2 = reward;
	}

	public void setBonusReward(int reward) {
		this.bonusReward = reward;
	}

	public void setRewardCount(float rewardCount) {
		this.rewardCount = rewardCount;
	}

	public int getRewardGp() {
		return rewardGp;
	}

	public void setRewardGp(int rewardGp) {
		this.rewardGp = rewardGp;
	}

	public int getBonusGp() {
		return bonusGp;
	}

	public void setBonusGp(int bonusGp) {
		this.bonusGp = bonusGp;
	}

	public int getBonusReward2() {
		return bonusReward2;
	}

	public void setBonusReward2(int bonusReward2) {
		this.bonusReward2 = bonusReward2;
	}
}
