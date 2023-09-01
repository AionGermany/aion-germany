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
package com.aionemu.gameserver.model.gameobjects.player.equipmentsetting;

import com.aionemu.gameserver.model.gameobjects.PersistentState;

/**
 * @author Falke_34
 */
public class EquipmentSetting {

	private PersistentState persistentState;
	private int slot;
	private int display;
	private int mHand;
	private int sHand;
	private int helmet;
	private int torso;
	private int glove;
	private int boots;
	private int earringsLeft;
	private int earringsRight;
	private int ringLeft;
	private int ringRight;
	private int necklace;
	private int shoulder;
	private int pants;
	private int powershardLeft;
	private int powershardRight;
	private int wings;
	private int waist;
	private int mOffHand;
	private int sOffHand;
	private int plume;
	private int bracelet;

	public EquipmentSetting(int slot, int display, int mHand, int sHand, int helmet, int torso, int glove, int boots, int earringsLeft,
		int earringsRight, int ringLeft, int ringRight, int necklace, int shoulder, int pants, int powershardLeft, int powershardRight,
		int wings, int waist, int mOffHand, int sOffHand, int plume, int bracelet) {
		this.slot = slot;
		this.display = display;
		this.mHand = mHand;
		this.sHand = sHand;
		this.helmet = helmet;
		this.torso = torso;
		this.glove = glove;
		this.boots = boots;
		this.earringsLeft = earringsLeft;
		this.earringsRight = earringsRight;
		this.ringLeft = ringLeft;
		this.ringRight = ringRight;
		this.necklace = necklace;
		this.shoulder = shoulder;
		this.pants = pants;
		this.powershardLeft = powershardLeft;
		this.powershardRight = powershardRight;
		this.wings = wings;
		this.waist = waist;
		this.mOffHand = mOffHand;
		this.sOffHand = sOffHand;
		this.plume = plume;
		this.bracelet = bracelet;
	}

	public PersistentState getPersistentState() {
		return this.persistentState;
	}

	public void setPersistentState(PersistentState persistentState) {
		this.persistentState = persistentState;
	}

	public int getSlot() {
		return this.slot;
	}

	public void setSlot(int slot) {
		this.slot = slot;
	}

	public int getDisplay() {
		return display;
	}

	public void setDisplay(int display) {
		this.display = display;
	}

	public int getmHand() {
		return mHand;
	}

	public void setmHand(int mHand) {
		this.mHand = mHand;
	}

	public int getsHand() {
		return sHand;
	}

	public void setsHand(int sHand) {
		this.sHand = sHand;
	}

	public int getHelmet() {
		return helmet;
	}

	public void setHelmet(int helmet) {
		this.helmet = helmet;
	}

	public int getTorso() {
		return torso;
	}

	public void setTorso(int torso) {
		this.torso = torso;
	}

	public int getGlove() {
		return glove;
	}

	public void setGlove(int glove) {
		this.glove = glove;
	}

	public int getBoots() {
		return boots;
	}

	public void setBoots(int boots) {
		this.boots = boots;
	}

	public int getEarringsLeft() {
		return earringsLeft;
	}

	public void setEarringsLeft(int earringsLeft) {
		this.earringsLeft = earringsLeft;
	}

	public int getEarringsRight() {
		return earringsRight;
	}

	public void setEarringsRight(int earringsRight) {
		this.earringsRight = earringsRight;
	}

	public int getRingLeft() {
		return ringLeft;
	}

	public void setRingLeft(int ringLeft) {
		this.ringLeft = ringLeft;
	}

	public int getRingRight() {
		return ringRight;
	}

	public void setRingRight(int ringRight) {
		this.ringRight = ringRight;
	}

	public int getNecklace() {
		return necklace;
	}

	public void setNecklace(int necklace) {
		this.necklace = necklace;
	}

	public int getShoulder() {
		return shoulder;
	}

	public void setShoulder(int shoulder) {
		this.shoulder = shoulder;
	}

	public int getPants() {
		return pants;
	}

	public void setPants(int pants) {
		this.pants = pants;
	}

	public int getPowershardLeft() {
		return powershardLeft;
	}

	public void setPowershardLeft(int powershardLeft) {
		this.powershardLeft = powershardLeft;
	}

	public int getPowershardRight() {
		return powershardRight;
	}

	public void setPowershardRight(int powershardRight) {
		this.powershardRight = powershardRight;
	}

	public int getWings() {
		return wings;
	}

	public void setWings(int wings) {
		this.wings = wings;
	}

	public int getWaist() {
		return waist;
	}

	public void setWaist(int waist) {
		this.waist = waist;
	}

	public int getmOffHand() {
		return mOffHand;
	}

	public void setmOffHand(int mOffHand) {
		this.mOffHand = mOffHand;
	}

	public int getsOffHand() {
		return sOffHand;
	}

	public void setsOffHand(int sOffHand) {
		this.sOffHand = sOffHand;
	}

	public int getPlume() {
		return plume;
	}

	public void setPlume(int plume) {
		this.plume = plume;
	}

	public int getBracelet() {
		return bracelet;
	}

	public void setBracelet(int bracelet) {
		this.bracelet = bracelet;
	}
}
