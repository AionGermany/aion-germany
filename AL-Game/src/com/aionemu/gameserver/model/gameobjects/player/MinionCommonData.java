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

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.PlayerMinionsDAO;
import com.aionemu.gameserver.model.IExpirable;
import com.aionemu.gameserver.model.templates.VisibleObjectTemplate;
import com.aionemu.gameserver.utils.idfactory.IDFactory;

public class MinionCommonData extends VisibleObjectTemplate implements IExpirable {
	private int minionId;
	private Timestamp birthday;
	private int minionObjId;
	private int masterObjectId;
	private String minionGrade;
	private String name;
	private int minionLevel;
	private int miniongrowpoint; //new

	public MinionCommonData(int minionId, int masterObjectId, String name, String minionGrade, int minionLevel, int growpoints) {
		switch(this.minionObjId) {
			case 0: {
				this.minionObjId = IDFactory.getInstance().nextId();
				break;
			}
			default:
				do 
				{
					if(DAOManager.getDAO(PlayerMinionsDAO.class).PlayerMinions(masterObjectId, minionObjId)) {
						this.minionObjId = IDFactory.getInstance().nextId();
					}
				} 
				while(DAOManager.getDAO(PlayerMinionsDAO.class).PlayerMinions(masterObjectId, minionObjId));
				break;
		}
		this.minionId = minionId;
		this.masterObjectId = masterObjectId;
		this.name = name;
		this.minionGrade = minionGrade;
		this.minionLevel = minionLevel;
		this.miniongrowpoint = growpoints;
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

	public int getMinionId() {
		return minionId;
	}

	public String getMinionGrade() {
		return minionGrade;
	}

	public int getMinionLevel() {
		return minionLevel;
	}

	public int getBirthday() {
		if (birthday == null) {
			return 0;
		}
		return (int) (birthday.getTime() / 1000);
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

	@Override
	public int getExpireTime() {
		return 0;
	}

	@Override
	public void expireEnd(Player player) {
	}

	@Override
	public boolean canExpireNow() {
		return false;
	}

	@Override
	public void expireMessage(Player player, int n) {
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
		return 0;
	}

	public int getMiniongrowpoint() {
		return miniongrowpoint;
	}

	public void setMiniongrowpoint(int miniongrowpoint) {
		this.miniongrowpoint = miniongrowpoint;
	}
}
