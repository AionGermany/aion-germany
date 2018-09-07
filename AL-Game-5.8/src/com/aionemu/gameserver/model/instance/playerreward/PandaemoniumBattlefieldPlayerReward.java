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

/**
 * @author Falke_34
 */
public class PandaemoniumBattlefieldPlayerReward extends InstancePlayerReward {

	private boolean isRewarded;
	private int premiumFrigidaLegionSupplyBox;
	private int majorFrigidaLegionSupplyBox;
	private int greaterFrigidaLegionSupplyBox;
	private int lesserFrigidaLegionSupplyBox;
	private int minorFrigidaLegionSupplyBox;
	private int premiumFrigidaLegionLootBox;
	private int majorFrigidaLegionLootBox;
	private int greaterFrigidaLegionLootBox;
	private int lesserFrigidaLegionLootBox;
	private int minorFrigidaLegionLootBox;

	public PandaemoniumBattlefieldPlayerReward(Integer object) {
		super(object);
		this.isRewarded = false;
	}

	public boolean isRewarded() {
		return this.isRewarded;
	}

	public void setRewarded() {
		this.isRewarded = true;
	}

	public int getPremiumFrigidaLegionSupplyBox() {
		return premiumFrigidaLegionSupplyBox;
	}

	public int getMajorFrigidaLegionSupplyBox() {
		return majorFrigidaLegionSupplyBox;
	}

	public int getGreaterFrigidaLegionSupplyBox() {
		return greaterFrigidaLegionSupplyBox;
	}

	public int getLesserFrigidaLegionSupplyBox() {
		return lesserFrigidaLegionSupplyBox;
	}

	public int getMinorFrigidaLegionSupplyBox() {
		return minorFrigidaLegionSupplyBox;
	}

	public void setPremiumFrigidaLegionSupplyBox(int premiumFrigidaLegionSupplyBox) {
		this.premiumFrigidaLegionSupplyBox = premiumFrigidaLegionSupplyBox;
	}

	public void setMajorFrigidaLegionSupplyBox(int majorFrigidaLegionSupplyBox) {
		this.majorFrigidaLegionSupplyBox = majorFrigidaLegionSupplyBox;
	}

	public void setGreaterFrigidaLegionSupplyBox(int greaterFrigidaLegionSupplyBox) {
		this.greaterFrigidaLegionSupplyBox = greaterFrigidaLegionSupplyBox;
	}

	public void setLesserFrigidaLegionSupplyBox(int lesserFrigidaLegionSupplyBox) {
		this.lesserFrigidaLegionSupplyBox = lesserFrigidaLegionSupplyBox;
	}

	public void setMinorFrigidaLegionSupplyBox(int minorFrigidaLegionSupplyBox) {
		this.minorFrigidaLegionSupplyBox = minorFrigidaLegionSupplyBox;
	}

	public int getPremiumFrigidaLegionLootBox() {
		return premiumFrigidaLegionLootBox;
	}

	public int getMajorFrigidaLegionLootBox() {
		return majorFrigidaLegionLootBox;
	}

	public int getGreaterFrigidaLegionLootBox() {
		return greaterFrigidaLegionLootBox;
	}

	public int getLesserFrigidaLegionLootBox() {
		return lesserFrigidaLegionLootBox;
	}

	public int getMinorFrigidaLegionLootBox() {
		return minorFrigidaLegionLootBox;
	}

	public void setPremiumFrigidaLegionLootBox(int premiumFrigidaLegionLootBox) {
		this.premiumFrigidaLegionLootBox = premiumFrigidaLegionLootBox;
	}

	public void setMajorFrigidaLegionLootBox(int majorFrigidaLegionLootBox) {
		this.majorFrigidaLegionLootBox = majorFrigidaLegionLootBox;
	}

	public void setGreaterFrigidaLegionLootBox(int greaterFrigidaLegionLootBox) {
		this.greaterFrigidaLegionLootBox = greaterFrigidaLegionLootBox;
	}

	public void setLesserFrigidaLegionLootBox(int lesserFrigidaLegionLootBox) {
		this.lesserFrigidaLegionLootBox = lesserFrigidaLegionLootBox;
	}

	public void setMinorFrigidaLegionLootBox(int minorFrigidaLegionLootBox) {
		this.minorFrigidaLegionLootBox = minorFrigidaLegionLootBox;
	}
}
