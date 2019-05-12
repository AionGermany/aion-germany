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

	private int actionId;
	private Collection<MinionCommonData> minions;
	private MinionCommonData minionsCommonData;
	private int subType;
	private boolean isActing;
	private int lootNpcId;
	private int dopeAction;
	private int dopeSlot;
	private int dopeSlot2;
	private int minionObjectId;
	private int ItemId;
	private long expireTime;
	private int minionSkillPoints;
	private boolean autoCharge;
	//private boolean asMaterial;

	public SM_MINIONS(int actionId) {
		this.actionId = actionId;
	}

	public SM_MINIONS(boolean isLooting) {
		this.actionId = 9;
		this.isActing = isLooting;
		//this.subType = 1;
	}

	public SM_MINIONS(int actionId, Collection<MinionCommonData> minions) {
		this.actionId = actionId;
		this.minions = minions;
	}

	public SM_MINIONS(int actionId, MinionCommonData minionsCommonData) { // spawn
		this.actionId = actionId;
		this.minionsCommonData = minionsCommonData;
	}
	
	public SM_MINIONS(int actionId, long expireTime) {
		this.actionId = actionId;
		this.expireTime = expireTime;
	}

	public SM_MINIONS(int actionId, int minionSkillPoints, boolean autoCharge) {
		this.actionId = actionId;
		this.minionSkillPoints = minionSkillPoints;
		this.autoCharge = autoCharge;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeH(actionId);
		switch (actionId) {
		case 0:
			writeH(10);
			writeD(2500000);
			writeD(0);
			writeD(2);
			writeD(5000);
			writeD(0);
			writeD(10000);
			writeD(0);
			writeD(5000);
			writeD(0);
			writeD(5000);
			writeD(0);
			writeD(10000);
			writeD(0);
			writeH(4);
			break;
		case 1: //Send player MinionList
			writeC(0);
			if (minions == null) {
				writeH(0);
				break;
			}
			writeH(minions != null ? minions.size() : 0);
			for (MinionCommonData commonData : minions) {
				writeD(commonData.getObjectId());
				writeD(0);
				writeD(0);
				writeD(commonData.getMasterObjectId());
				writeD(commonData.getMinionId());
				writeS(commonData.getName());
				writeD(commonData.getBirthday());
				writeD(0);
				writeD(commonData.getMinionGrowthPoint());
				writeC(commonData.isLock() ? 1 : 0);
				if (commonData.getDopingBag() == null) {
					writeB(new byte[24]);
				} else {
					int[] scrollBag = commonData.getDopingBag().getScrollsUsed();
					writeD(commonData.getDopingBag().getFoodItem());
					writeD(commonData.getDopingBag().getDrinkItem());
					if (scrollBag.length == 0) {
						writeB(new byte[16]);
					} else {
						writeD(scrollBag[0]);
						writeD(scrollBag.length > 1 ? scrollBag[1] : 0);
						writeD(scrollBag.length > 2 ? scrollBag[2] : 0);
						writeD(scrollBag.length > 3 ? scrollBag[3] : 0);
					}
				}
				writeC(0);
			}
			break;
		case 2:
			if (minionsCommonData == null) {
				return;
			}
			writeD(0);
			writeD(0);
			writeH(0);
			writeD(minionsCommonData.getObjectId());
			writeD(0);
			writeD(0);
			writeD(minionsCommonData.getMasterObjectId());
			writeD(minionsCommonData.getMinionId());
			writeS(minionsCommonData.getName());
			writeD(minionsCommonData.getBirthday());
			writeB(new byte[34]);
			break;
		//case 2:
		//	if (minionsCommonData == null) {
		//		return;
		//	}
		//	writeH(asMaterial ? 1 : 0);
		//	writeD(minionsCommonData.getObjectId());
		//	break;
		case 3: // Delete
			writeH(0);
			writeD(minionsCommonData.getObjectId());
			break;
		case 4: // Rename
			if (minionsCommonData == null) {
				return;
			}
			writeD(minionsCommonData.getObjectId());
			writeS(minionsCommonData.getName());
			break;
		case 5: // Lock / Unlock
			if (minionsCommonData == null) {
				return;
			}
			writeD(minionsCommonData.getObjectId());
			writeC(minionsCommonData.isLock() ? 1 : 0);
			break;
		case 6: // Spawn
			if (minionsCommonData == null) {
				return;
			}
			writeS(minionsCommonData.getName());
			writeD(minionsCommonData.getObjectId());
			writeD(minionsCommonData.getMinionId());
			writeD(minionsCommonData.getMasterObjectId());
			break;
		case 7: // Despawn
			if (minionsCommonData == null) {
				return;
			}
			writeD(minionsCommonData.getObjectId());
			writeC(21);
			break;
		case 8: // evolve
			if (minionsCommonData == null) {
				return;
			}
			writeD(minionsCommonData.getObjectId()); // minionobjectId
			writeD(minionsCommonData.getMinionGrowthPoint()); // minion points
			break;
		//case 8:
		//	writeH(1);
		//	writeC(isActing ? 1 : 0);
		//	break;
		case 9: // add bufffood and activate loot etc
			switch (subType) {
				case 0: {
					switch (dopeAction) {
						case 0: {
							writeH(dopeAction);
							writeD(minionObjectId);
							writeD(ItemId);
							writeD(dopeSlot);
							break;
						}
						case 256: {
							writeH(dopeAction);
							writeD(minionObjectId);
							writeD(dopeSlot);
							break;
						}
						case 768: {
							writeH(dopeAction);
							writeD(minionObjectId);
							writeD(ItemId);
							break;
						}
						case 512: {
							writeH(dopeAction);
							writeD(minionObjectId);
							writeD(dopeSlot);
							writeD(dopeSlot2);
						}
					}
					break;
				}
				case 1: {
					switch (dopeAction) {
						case 5: {
							if (lootNpcId > 0) {
								writeD(minionObjectId);
								writeC(isActing ? 1 : 2);
								writeD(lootNpcId);
								break;
							}
							writeD(minionObjectId);
							writeC(0);
							writeC(isActing ? 1 : 0);
						}
					}
				}
			}
			break;
		case 10: // use function
			writeD((int) expireTime);
			writeD(0);
		case 11: // stop function
			break;
		case 12: // skill use or skill points 
			writeD(minionSkillPoints); // Minion SkillPoints
			writeC(autoCharge ? 1 : 0); // Auto Recharge
			break;
		case 13: // auto function
			writeC(0);
			break;
		case 14:
		case 15:
		case 16:
		case 17:
		case 18:
		case 19:
		case 20:
		case 21:
			writeD(323045);
			writeD(323045);
			writeD(323045);
			writeD(323045);
			writeD(323045);
			writeD(323045);
			writeD(323045);
			writeD(323045);
			writeD(323045);
			writeD(323045);
			writeD(323045);
			writeD(323045);
		}
	}
}
