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

import java.sql.Timestamp;

import com.aionemu.gameserver.model.IExpirable;
import com.aionemu.gameserver.model.templates.VisibleObjectTemplate;
import com.aionemu.gameserver.model.templates.minion.MinionDopingBag;
import com.aionemu.gameserver.utils.idfactory.IDFactory;

public class MinionCommonData extends VisibleObjectTemplate implements IExpirable {

	private int minionId;
	private Timestamp birthday;
	private int minionObjId;
	private final int masterObjectId;
	private String minionGrade;
	private String name;
	private int minionLevel;
	private int growthPoints;
	private boolean locked;
	private boolean isLooting = false;
	private boolean isBuffing = false;
	MinionDopingBag dopingBag = null;
	private Timestamp despawnTime;
	private int expireTime;

	public MinionCommonData(int minionId, int masterObjectId, String name, String minionGrade, int minionLevel, int growthPoints, boolean locked) {
		if (minionObjId == 0) {
			minionObjId = IDFactory.getInstance().nextId();
		}
		this.minionId = minionId;
		this.masterObjectId = masterObjectId;
		this.name = name;
		this.minionGrade = minionGrade;
		this.minionLevel = minionLevel;
		this.growthPoints = growthPoints;
		this.locked = locked;
		this.dopingBag = new MinionDopingBag();
	}

	public void setObjectId(int minionObjId) {
		this.minionObjId = minionObjId;
	}

	public int getObjectId() {
		return minionObjId;
	}

	public int getMasterObjectId() {
		return masterObjectId;
	}

	public final int getMinionId() {
		return minionId;
	}

	public String getMinionGrade() {
		return minionGrade;
	}

	public int getMinionLevel() {
		return minionLevel;
	}

	public int getGrowthPoints() {
		return growthPoints;
	}

	public int getBirthday() {
		if (birthday == null) {
			return 0;
		}
		return (int) (birthday.getTime() / 1000);
	}

	public boolean isLocked() {
		return locked;
	}

	public Timestamp getBirthdayTimestamp() {
		return birthday;
	}

	public void setBirthday(Timestamp birthday) {
		this.birthday = birthday;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setGrowthPoints(int growthPoints) {
		this.growthPoints = growthPoints;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	@Override
	public int getExpireTime() {
		return expireTime;
	}

	@Override
	public void expireEnd(Player player) {

	}

	@Override
	public boolean canExpireNow() {
		return false;
	}

	@Override
	public void expireMessage(Player player, int time) {
	}

	@Override
	public int getTemplateId() {
		return minionId;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getNameId() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Timestamp getDespawnTime() {
		return despawnTime;
	}

	public void setDespawnTime(Timestamp despawnTime) {
		this.despawnTime = despawnTime;
	}

	public void setIsLooting(boolean isLooting) {
		this.isLooting = isLooting;
	}

	public boolean isLooting() {
		return isLooting;
	}

	public MinionDopingBag getDopingBag() {
		return dopingBag;
	}

	public void setIsBuffing(boolean isBuffing) {
		this.isBuffing = isBuffing;
	}

	public boolean isBuffing() {
		return isBuffing;
	}
}
