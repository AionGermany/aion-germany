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
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.instance.InstanceBuff;

public class SteelWallBastionBattlefieldPlayerReward extends InstancePlayerReward {

	private int timeBonus;
	private int rewardGp;
	private int bonusAp;
	private int bonusReward2;
	private int rewardAp;
	private float rewardCount;
	private int bonusReward;
	private Race race;
	private long logoutTime;
	private InstanceBuff boostMorale;
	private int bloodMark;
	private int medalBundle;
	private int bonusGp;
	private float timeBonusModifier;

	public SteelWallBastionBattlefieldPlayerReward(Integer object, int timeBonus, Race race) {
		super(object);
		this.timeBonus = timeBonus;
		timeBonusModifier = ((float) this.timeBonus / (float) 660000);
		this.race = race;
	}

	public void setBonusReward2(int bonusReward2) {
		this.bonusReward2 = bonusReward2;
	}

	public int getRewardAp() {
		return rewardAp;
	}

	public void setBonusGp(int bonusGp) {
		this.bonusGp = bonusGp;
	}

	public int getBonusReward2() {
		return bonusReward2;
	}

	public void updateBonusTime() {
		int offlineTime = (int) (System.currentTimeMillis() - logoutTime);
		timeBonus -= offlineTime * timeBonusModifier;
	}

	public Race getRace() {
		return race;
	}

	public void setBloodMark(int bloodMark) {
		this.bloodMark = bloodMark;
	}

	public void setBonusAp(int bonusAp) {
		this.bonusAp = bonusAp;
	}

	public void setRewardAp(int rewardAp) {
		this.rewardAp = rewardAp;
	}

	public int getBonusReward() {
		return bonusReward;
	}

	public boolean hasBoostMorale() {
		return boostMorale.hasInstanceBuff();
	}

	public float getParticipation() {
		return (float) getTimeBonus() / timeBonus;
	}

	public int getRewardGp() {
		return rewardGp;
	}

	public int getRemaningTime() {
		int time = boostMorale.getRemaningTime();
		if (time >= 0 && time < 20) {
			return 20 - time;
		}
		return 0;
	}

	public int getMedalBundle() {
		return medalBundle;
	}

	public int getBonusGp() {
		return bonusGp;
	}

	public void endBoostMoraleEffect(Player player) {
		boostMorale.endEffect(player);
	}

	public void setRewardCount(float rewardCount) {
		this.rewardCount = rewardCount;
	}

	public void updateLogOutTime() {
		logoutTime = System.currentTimeMillis();
	}

	public int getRewardCount() {
		return (int) rewardCount;
	}

	public int getScorePoints() {
		return timeBonus + getPoints();
	}

	public int getBonusAp() {
		return bonusAp;
	}

	public void setRewardGp(int rewardGp) {
		this.rewardGp = rewardGp;
	}

	public void setMedalBundle(int medalBundle) {
		this.medalBundle = medalBundle;
	}

	public int getTimeBonus() {
		return timeBonus > 0 ? timeBonus : 0;
	}

	public int getBloodMark() {
		return bloodMark;
	}

	public void applyBoostMoraleEffect(Player player) {
		boostMorale.applyEffect(player, 20000);
	}

	public void setBonusReward(int bonusReward) {
		this.bonusReward = bonusReward;
	}
}
