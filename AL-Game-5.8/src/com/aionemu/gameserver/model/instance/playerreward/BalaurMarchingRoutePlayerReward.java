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

/**
 * @author Falke_34
 */
public class BalaurMarchingRoutePlayerReward extends InstancePlayerReward {

	private int timeBonus;
	private long logoutTime;
	private float timeBonusModifier;
	private Race race;
	private int rewardAp;
	private int rewardGp;
	private int bonusAp;
	private int bonusGp;
	private int bonusReward;
	private int bonusReward2;
	private int bloodMark;
	private int ophidanVictoryBox;
	private float rewardCount;
	private InstanceBuff boostMorale;

	public BalaurMarchingRoutePlayerReward(Integer object, int timeBonus, Race race) {
		super(object);
		this.timeBonus = timeBonus;
		timeBonusModifier = ((float) this.timeBonus / (float) 660000);
		this.race = race;
	}

	public void setBonusAp(int ap) {
		this.bonusAp = ap;
	}

	public int getBonusAp() {
		return bonusAp;
	}

	public void setRewardAp(int ap) {
		this.rewardAp = ap;
	}

	public int getRewardAp() {
		return rewardAp;
	}

	public void setBonusGp(int bonusGp) {
		this.bonusGp = bonusGp;
	}

	public int getRewardGp() {
		return rewardGp;
	}

	public int getBonusGp() {
		return bonusGp;
	}

	public void setRewardGp(int rewardGp) {
		this.rewardGp = rewardGp;
	}

	public int getRewardCount() {
		return (int) rewardCount;
	}

	public int getTimeBonus() {
		return timeBonus > 0 ? timeBonus : 0;
	}

	public void updateBonusTime() {
		int offlineTime = (int) (System.currentTimeMillis() - logoutTime);
		timeBonus -= offlineTime * timeBonusModifier;
	}

	public void updateLogOutTime() {
		logoutTime = System.currentTimeMillis();
	}

	public Race getRace() {
		return race;
	}

	public void setRewardCount(float rewardCount) {
		this.rewardCount = rewardCount;
	}

	public int getBonusReward() {
		return bonusReward;
	}

	public void setBonusReward(int reward) {
		this.bonusReward = reward;
	}

	public int getBonusReward2() {
		return bonusReward2;
	}

	public void setBonusReward2(int bonusReward2) {
		this.bonusReward2 = bonusReward2;
	}

	public void endBoostMoraleEffect(Player player) {
		boostMorale.endEffect(player);
	}

	public boolean hasBoostMorale() {
		return boostMorale.hasInstanceBuff();
	}

	public void applyBoostMoraleEffect(Player player) {
		boostMorale.applyEffect(player, 20000);
	}

	public void setOphidanVictoryBox(int ophidanVictoryBox) {
		this.ophidanVictoryBox = ophidanVictoryBox;
	}

	public void setBloodMark(int bloodMark) {
		this.bloodMark = bloodMark;
	}

	public int getRemaningTime() {
		int time = boostMorale.getRemaningTime();
		if (time >= 0 && time < 20) {
			return 20 - time;
		}
		return 0;
	}

	public int getBloodMark() {
		return bloodMark;
	}

	public int getScorePoints() {
		return timeBonus + getPoints();
	}

	public int getOphidanVictoryBox() {
		return ophidanVictoryBox;
	}

	public float getParticipation() {
		return (float) getTimeBonus() / timeBonus;
	}
}
