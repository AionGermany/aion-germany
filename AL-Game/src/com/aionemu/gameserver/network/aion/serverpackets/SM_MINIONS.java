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
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author Falke_34, FrozenKiller
 */
public class SM_MINIONS extends AionServerPacket {
	private int action;
	@SuppressWarnings("unused")
	private int expiredTimeMillis;
	private int minionSkillPoints;
	private boolean autoCharge;
	private MinionCommonData commonData;
	private Collection<MinionCommonData> minions;
	private long timeLeft;
	private int subSwitch;
	private int minionObjectId;
	private int ItemId;
	private int slot;
	private int slot2;
	Player player;
	private int addType;
	private boolean isMaterial;
	private boolean isloot;
	private int lootNpcId;

	public SM_MINIONS(int action) {
		this.action = action;
	}
	
	public SM_MINIONS(int action, int minionSkillPoints, boolean autoCharge) {
		this.action = action;
		this.minionSkillPoints = minionSkillPoints;
		this.autoCharge = autoCharge;
	}

	public SM_MINIONS(int action, MinionCommonData commonData, int addType) {
		this.action = action;
		this.commonData = commonData;
		this.addType = addType;
	}

	public SM_MINIONS(int action, MinionCommonData commonData) {
		this.action = action;
		this.commonData = commonData;
	}

	public SM_MINIONS(int action, Collection<MinionCommonData> minions) {
		this.action = action;
		this.minions = minions;
	}
	
	public SM_MINIONS(int action, long timeLeft) { // TODO
		this.action = action;
		this.timeLeft = timeLeft;
	}
	
	public SM_MINIONS(int action, int subSwitch, int lootNpcId ,boolean isloot) { // TODO
		this.action = action;
		this.subSwitch = subSwitch;
		this.isloot = isloot;
		this.lootNpcId = lootNpcId;
	}
	
	public SM_MINIONS(int action, int subSwitch, int minionObjectId, int ItemId, int slot, int slot2) {
        this.action = action;
        switch (subSwitch) {
            case 0: {
                this.subSwitch = 0;
                this.minionObjectId = minionObjectId;
                this.ItemId = ItemId;
                this.slot = slot;
                break;
            }
            case 2: {
                this.subSwitch = 512;
                this.minionObjectId = minionObjectId;
                this.slot = slot;
                this.slot2 = slot2;
                break;
            }
            case 1: {
                this.subSwitch = 256;
                this.minionObjectId = minionObjectId;
                this.slot = slot;
                break;
            }
            case 3: {
                this.subSwitch = 768;
                this.minionObjectId = minionObjectId;
                this.ItemId = ItemId;
                this.slot = slot;
                break;
            }
            case 4: {
            	this.subSwitch = 1;
            }
        }
    }
	
	public SM_MINIONS(int action, boolean isMaterial , MinionCommonData commonData) {
        this.action = action;
        this.isMaterial = isMaterial;
        this.commonData = commonData;
    }

	@Override
	protected void writeImpl(AionConnection con) {
		writeH(action);
		switch (action) {
			case 0: {
				writeC(0);
                if (minions == null) {
                    writeH(0);
                    break;
                }
				writeH(minions.size());
				for (MinionCommonData commonData : minions) {
					writeD(commonData.getObjectId());
					writeD(commonData.getMinionId());
					writeD(0); 
					writeD(commonData.getMasterObjectId());
					writeD(commonData.getMinionId());
					writeS(commonData.getName());
					writeD(commonData.getBirthday());
					writeD(0);
					writeD(commonData.getMinionGrowthPoint());
					writeC(commonData.isLock() ? 1 : 0);
					//if (commonData.getDopingBag() == null) {
                        writeB(new byte[24]);
                    /*} else {
                    	int[] scrollBag = commonData.getDopingBag().getScrollsUsed();
    					writeD(commonData.getDopingBag().getFoodItem());
    					writeD(commonData.getDopingBag().getDrinkItem());
    					if (scrollBag != null) {
    						writeD(scrollBag[0]);
                            writeD((scrollBag.length > 1) ? scrollBag[1] : 0);
                            writeD((scrollBag.length > 2) ? scrollBag[2] : 0);
                            writeD((scrollBag.length > 3) ? scrollBag[3] : 0);
    					} else {
        					writeB(new byte[16]);
    					}
                    }*/
					writeC(0); 
				}
				break;
			}
			case 1: {
                if (commonData == null) {
                    return;
                }
				writeD(addType);//3 nem siker combination, 2 siker combination, 1 levelup, 0 new minion (effect)
				writeD(0);
				writeH(0);
				writeD(commonData.getObjectId());
				writeD(commonData.getMinionId());
				writeD(0); 
				writeD(commonData.getMasterObjectId());
				writeD(commonData.getMinionId());
				writeS(commonData.getName());
				writeD(commonData.getBirthday());
				writeB(new byte[34]);
				break;
			}
			case 2: {//delete
				if (commonData == null) {
                    return;
                }
                writeH(isMaterial ? 1 : 0);
                writeD(commonData.getObjectId());
                break;
            }
			case 3: {//rename
                if (commonData == null) {
                    return;
                }
				writeD(commonData.getObjectId());
				writeS(commonData.getName());
				break;
			}
			case 4: {//lock
                if (commonData == null) {
                    return;
                }
				writeD(commonData.getObjectId());
                writeC(commonData.isLock() ? 1 : 0);
                break;
			}
			case 5: {// spawn
                if (commonData == null) {
                    return;
                }
				writeS(commonData.getName());
				writeD(commonData.getObjectId());
				writeD(commonData.getMinionId());
				writeD(commonData.getMasterObjectId());
				break;
			}
			case 6: {// despawn
                if (commonData == null) {
                    return;
                }
				writeD(commonData.getObjectId());
//				if (player != null && player.getLifeStats().isAlreadyDead()) {
//                    writeC(0);
//                    break;
//                }
				writeC(21);
				break;
			}
			case 7: {//growthUp alpha
                if (commonData == null) {
                    return;
                }
				writeD(commonData.getObjectId());
				writeD(commonData.getMinionGrowthPoint());
				break;
			}
			case 8: {
				writeH(subSwitch);
				System.out.println("SM subSwitch: "+subSwitch);
				switch (subSwitch) {
	                case 0: { //Add item
	                    writeD(minionObjectId);
	                    writeD(ItemId);
	                    writeD(slot);
	                    System.out.println("SM minion minionObjectId: "+minionObjectId+" ItemId: "+ItemId+" slot: "+slot+" slot2: "+slot2);
	                    break;
	                }
	                case 1: {//Auto Loot
						if (lootNpcId != 0) {
							writeC(isloot ? 1 : 2); // 0x02 display looted msg.
							writeD(lootNpcId);
						}
						else {
							writeC(isloot ? 1 : 0);
						}
	                	break;
	                }
	                case 256: {
	                    writeD(minionObjectId);
	                    writeD(slot);
	                    System.out.println("SM minion minionObjectId: "+minionObjectId+" ItemId: "+ItemId+" slot: "+slot+" slot2: "+slot2);
	                    break;
	                }
	                case 512: {
	                    writeD(minionObjectId);
	                    writeD(slot);
	                    writeD(slot2);
	                    System.out.println("SM minion minionObjectId: "+minionObjectId+" ItemId: "+ItemId+" slot: "+slot+" slot2: "+slot2);
	                    break;
	                }
	                case 768: {//BUFF
	                    writeD(minionObjectId);
	                    writeD(ItemId);
	                    //writeD(slot);
	                    break;
	                }
				}
				break;
			}
			case 9: {// Aktivate Miol funktion Warn TODO
				writeD((int)timeLeft);
				writeD(1);
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
				writeD(0);
				break;
			}
			case 13: { //Test!
                writeD(319480);
                writeD(319480);
                writeD(319480);
                writeD(319480);
                writeD(319480);
                writeD(319480);
                writeD(319480);
                writeD(319480);
                writeD(319480);
                writeD(319480);
                writeD(319480);
                writeD(319480);
                break;
            }
		}
	}
}
