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
package com.aionemu.gameserver.network.aion.serverpackets;

import java.util.Collection;

import com.aionemu.gameserver.model.gameobjects.player.MinionCommonData;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author Falke_34, FrozenKiller
 */
public class SM_MINIONS extends AionServerPacket {
	private int action;
	private int minionObjId;
	private int minionId;
	private int masterObjId;
	String name;
	@SuppressWarnings("unused")
	private int expiredTimeMillis;
	private int minionSkillPoints;
	private boolean autoCharge;
	private MinionCommonData commonData;
	private Collection<MinionCommonData> minions;
	private long timeLeft;
	private int subSwitch;
	private int value1;
	private int value2;
	private int value3;
	private int levelup = 0; //new

	public SM_MINIONS(int action) {
		this.action = action;
	}
	
	public SM_MINIONS(int action, int minionSkillPoints, boolean autoCharge) {
		this.action = action;
		this.minionSkillPoints = minionSkillPoints;
		this.autoCharge = autoCharge;
	}

	public SM_MINIONS(int action, int minionObjId, int minionId) {
		this.action = action;
		this.minionObjId = minionObjId;
		this.minionId = minionId;
	}

	public SM_MINIONS(int action, int minionObjId) {
		this.action = action;
		this.minionObjId = minionObjId;
	}
	
	public SM_MINIONS(int action, MinionCommonData commonData, int levelup) {
		this.action = action;
		this.commonData = commonData;
		this.levelup = levelup;
	}

	public SM_MINIONS(int action, MinionCommonData commonData) {
		this.action = action;
		this.commonData = commonData;
	}

	public SM_MINIONS(int action, String name, int minionObjId, int minionId, int masterObjId) {
		this.action = action;
		this.name = name;
		this.minionObjId = minionObjId;
		this.minionId = minionId;
		this.masterObjId = masterObjId;
	}

	public SM_MINIONS(int action, Collection<MinionCommonData> minions) {
		this.action = action;
		this.minions = minions;
	}
	
	public SM_MINIONS(int action, long timeLeft) { // TODO
		this.action = action;
		this.timeLeft = timeLeft;
	}
	
	public SM_MINIONS(int action, int subSwitch, int value1, int value2, int value3) {
		this.action = action;
		this.subSwitch = subSwitch;
		this.value1 = value1;
		this.value2 = value2;
		this.value3 = value3;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeH(action);
		switch (action) {
			case 0: {
				writeC(0);
				writeH(minions.size());
				for (MinionCommonData minionCommonData : minions) {
					writeD(minionCommonData.getObjectId());
					writeQ(2550); // Todo what is this?
					writeD(minionCommonData.getMasterObjectId());
					writeD(minionCommonData.getMinionId());
					writeS(minionCommonData.getName()); // Name
					writeD(minionCommonData.getBirthday());
					writeD(0);
					writeD(minionCommonData.getMiniongrowpoint()); // Minion Growth Points
					writeB(new byte[26]);
				}
				break;
			}
			case 1: {
				writeD(levelup);//1 levelup, 0 new minion (effect)
				writeD(0);
				writeH(0);
				writeD(commonData.getObjectId());
				writeD(2550);
				writeD(0); 
				writeD(commonData.getMasterObjectId());
				writeD(commonData.getMinionId());
				writeH(0);
				writeD(commonData.getBirthday());
				writeB(new byte[34]);
				break;
			}
			case 3: {
				writeD(commonData.getObjectId());
				writeS(commonData.getName());
				break;
			}
			case 5: {// Spawn
				writeS(name); // Name
				writeD(minionObjId);
				writeD(minionId);
				writeD(masterObjId);
				break;
			}
			case 6: {// Despawn
				writeD(minionObjId);
				writeC(21);
				break;
			}
			case 7: {//growthUp alpha new
				writeD(commonData.getMiniongrowpoint());
				break;
			}
			case 8: {
				writeH(subSwitch);
				switch (subSwitch) {
					case 0: // AddItem
						writeD(value1); // MinionObjId
						writeD(value2); // ItemId
						writeD(value3); // ItemSlot
						break;
					case 1:
						writeC(1);
						break;
					case 768:
						writeD(value1); // MinionObjId
						writeD(value2); // ItemId
						break;
					}
			}
			case 9: {// Aktivate Miol funktion Warn TODO
				writeD((int) timeLeft);
				writeD(0);
				break;
			}
			case 10: // Deaktivate Miol funktion Warn
				writeC(0);
				break;
			case 11: {
				writeD(minionSkillPoints); // Minion SkillPoints
				writeH(autoCharge ? 1 : 0); // Auto Recharge ?
				break;
			}
			case 12: { //Miol funktion Warn AutoCharge (1 = ON  0 = OFF)
				writeC(0);
				break;
			}
			//bad
			case 13: { //Combination
				writeD(commonData.getObjectId());
				break;
			}
		}
	}
}
