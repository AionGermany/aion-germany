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

import java.sql.Timestamp;
import java.util.Collection;

import com.aionemu.gameserver.model.gameobjects.player.MinionCommonData;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author Falke_34, FrozenKiller
 */
public class SM_MINIONS extends AionServerPacket {

    int action;
    int subaction;
    int minionObjId;
    int minionId;
    int masterObjId;
    int energy;
    boolean bool;
    private boolean isActing;
    private int lootNpcId;
    String name;
    private Timestamp expiredTimeMillis;
    private MinionCommonData commonData;
    private Collection<MinionCommonData> minions;
    private int dopeAction;
    private int dopeSlot;
    private int itemObjectId;
    private int code;
    private Player player;
    private long timeLeft;
    private int slot;
    private int slot2;
    
    public SM_MINIONS(int action) {
        this.action = action;
    }
    
    public SM_MINIONS(int action, int energy, boolean auto) {
        this.action = action;
        this.energy = energy;
        bool = auto;
    }
    
    public SM_MINIONS(int action, int minionObjId) {
        this.action = action;
        this.minionObjId = minionObjId;
    }
    
    public SM_MINIONS(int action, MinionCommonData commonData) {
        this.action = action;
        this.commonData = commonData;
    }
    
    public SM_MINIONS(int action, MinionCommonData commonData, int code) {
        this.action = action;
        this.commonData = commonData;
        this.code = code;
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
    
    public SM_MINIONS(int action, Player player) {
        this.action = action;
        this.player = player;
    }
    
    public SM_MINIONS(boolean isLooting) {
        action = 9;
        isActing = isLooting;
        subaction = 1;
    }
    
    public SM_MINIONS(boolean isLooting, int npcId) {
        this(isLooting);
        action = 9;
        lootNpcId = npcId;
        subaction = 1;
    }
    
    public SM_MINIONS(int dopeAction, int itemId, int slot) {
        this(dopeAction, true);
        itemObjectId = itemId;
        dopeSlot = slot;
        action = 9;
        subaction = 1;
    }
    
    public SM_MINIONS(int action, int dopeAction, int itemId, int slot) {
        this(dopeAction, true);
        this.action = action;
        itemObjectId = itemId;
        dopeSlot = slot;
        subaction = 0;
    }
    
    public SM_MINIONS(Player player, int action) {
        this.action = action;
        this.player = player;
    }
    
    public SM_MINIONS(int dopeAction, boolean isBuffing) {
        action = 14;
        this.dopeAction = dopeAction;
        isActing = isBuffing;
        subaction = 0;
    }
    
    public SM_MINIONS(Timestamp expire, boolean isAuto) {
        action = 9;
        expiredTimeMillis = expire;
        bool = isAuto;
    }
    
    public SM_MINIONS(int action, long timeLeft) {
        this.action = action;
        this.timeLeft = timeLeft;
    }
    
    @Override
    protected void writeImpl(AionConnection con) {
        writeH(action);
        switch (action) {
            case 0: {
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
            }
            case 1: {
                writeC(0);
                writeH(minions.size());
                for (MinionCommonData mcd : minions) {
                    writeD(mcd.getObjectId());
                    writeD(0);
                    writeD(0);
                    writeD(mcd.getMasterObjectId());
                    writeD(mcd.getMinionId());
                    writeS(mcd.getName());
                    writeD(mcd.getBirthday());
                    writeQ(mcd.getGrowthPoints());
                    writeC(mcd.isLocked() ? 0 : 1);
                    final int[] scrollBag = mcd.getDopingBag().getScrollsUsed();
                    if (scrollBag.length == 0) {
                        writeB(new byte[28]);
                    }
                    else {
                        writeD((scrollBag.length > 1) ? scrollBag[0] : 0);
                        writeD((scrollBag.length > 2) ? scrollBag[1] : 0);
                        writeD((scrollBag.length > 3) ? scrollBag[2] : 0);
                        writeD((scrollBag.length > 4) ? scrollBag[3] : 0);
                        writeD((scrollBag.length > 5) ? scrollBag[4] : 0);
                        writeD((scrollBag.length > 6) ? scrollBag[5] : 0);
                    }
                    writeC(0);
                }
                break;
            }
            case 2: {
                writeD(code);
                writeD(0);
                writeH(0);
                writeD(commonData.getObjectId());
                writeD(0);
                writeD(0);
                writeD(commonData.getMasterObjectId());
                writeD(commonData.getMinionId());
                writeS(commonData.getName());
                writeD(commonData.getBirthday());
                writeQ(commonData.getGrowthPoints());
                writeC(commonData.isLocked() ? 0 : 1);
                writeB(new byte[28]);
                writeC(0);
                break;
            }
            case 3: {
                writeH(code);
                writeD(commonData.getObjectId());
                break;
            }
            case 4: {
                writeD(commonData.getObjectId());
                writeS(commonData.getName());
                break;
            }
            case 5: {
                writeD(commonData.getObjectId());
                writeC(commonData.isLocked() ? 0 : 1);
                break;
            }
            case 6: {
                if (commonData == null) {
                    return;
                }
                writeS(commonData.getName());
                writeD(commonData.getObjectId());
                writeD(commonData.getMinionId());
                writeD(commonData.getMasterObjectId());
                break;
            }
            case 7: {
                if (commonData == null) {
                    return;
                }
                writeD(commonData.getObjectId());
                writeC(21);
                break;
            }
            case 8: {
                if (commonData == null) {
                    return;
                }
                writeD(commonData.getObjectId());
                writeD(commonData.getGrowthPoints());
                break;
            }
            case 9: {
                writeC(subaction);
                if (subaction == 1) {
                    if (lootNpcId > 0) {
                        writeC(isActing ? 1 : 2);
                        writeD(lootNpcId);
                        break;
                    }
                    writeC(0);
                    writeC(isActing ? 1 : 0);
                    break;
                }
                else {
                    if (subaction == 0) {
                        writeC(dopeAction);
                        switch (dopeAction) {
                            case 0: {
                                writeD(minionObjId);
                                writeD(itemObjectId);
                                writeD(dopeSlot);
                                break;
                            }
                            case 1: {
                                writeD(minionObjId);
                                writeD(dopeSlot);
                                break;
                            }
                            case 3: {
                                writeD(minionObjId);
                                writeD(itemObjectId);
                                break;
                            }
                        }
                        break;
                    }
                    break;
                }
            }
            case 11: {
                writeD(0); // Function Expire Date
                writeD(0); // unk
                break;
            }            
            case 13: {
                writeD(con.getActivePlayer().getCommonData().getMinionEnergy());
                writeC(1);
                break;
            }
            case 14: {
                writeC(0);
                break;
            }
        }
    }
}
