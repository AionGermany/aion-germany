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
//	private boolean asMaterial;

	public SM_MINIONS(int actionId) {
		this.actionId = actionId;
	}

//	public SM_MINIONS(int actionId, int subType, int dopeAction, int minionObjectId, int ItemId2, int slot, int slot2, boolean isLooting) {
//		this.actionId = actionId;
//		switch (subType) {
//		case 0: {
//			switch (dopeAction) {
//			case 0:
//				this.dopeAction = 0;
//				this.minionObjectId = minionObjectId;
//				this.ItemId = ItemId2;
//				this.dopeSlot = slot;
//			case 2:
//				this.dopeAction = 512;
//				this.minionObjectId = minionObjectId;
//				this.dopeSlot = slot;
//				this.dopeSlot2 = slot2;
//			case 1:
//				this.dopeAction = 256;
//				this.minionObjectId = minionObjectId;
//				this.dopeSlot = slot;
//			case 3:
//				this.dopeAction = 768;
//				this.minionObjectId = minionObjectId;
//				this.ItemId = ItemId2;
//				this.dopeSlot = slot;
//			}
//			}
//			break;
//		case 1:
//			switch (dopeAction) {
//			case 5: {
//				this.minionObjectId = minionObjectId;
//				this.isActing = isLooting;
//			}
//			}
//		}
//	}

//	public SM_MINIONS(boolean isLooting) {
//		this.actionId = 8;
//		this.isActing = isLooting;
//		this.subType = 1;
//	}

	public SM_MINIONS(int actionId, Collection<MinionCommonData> minions) {
		this.actionId = actionId;
		this.minions = minions;
	}

	public SM_MINIONS(int actionId, MinionCommonData minion) {
		this.actionId = actionId;
		this.minionsCommonData = minion;
	}

//	public SM_MINIONS(int actionId, MinionCommonData minion, boolean asMaterial) {
//		this.actionId = actionId;
//		this.minionsCommonData = minion;
//		this.asMaterial = asMaterial;
//	}

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
//		case 2:
//			if (minionsCommonData == null) {
//				return;
//			}
//			writeH(asMaterial ? 1 : 0);
//			writeD(minionsCommonData.getObjectId());
//			break;
		case 3: // Rename
			if (minionsCommonData == null) {
				return;
			}
			writeD(minionsCommonData.getObjectId());
			writeS(minionsCommonData.getName());
			break;
		case 4: // Lock / Unlock
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
			writeC(1);
			break;
//		case 7:
//			if (minionsCommonData == null) {
//				return;
//			}
//			writeD(minionsCommonData.getObjectId());
//			writeD(minionsCommonData.getMinionLevel());
//			writeD(minionsCommonData.getMinionGrowthPoint());
//			break;
		case 8:
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
		case 9:
			writeD(1501224031);
			writeD(1);
			break;
		case 10:
		case 11:
			writeD(1521877022);
			writeD(0);
			break;
		case 12:
			writeD(0);
			writeC(0);
			break;
		case 13:
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
