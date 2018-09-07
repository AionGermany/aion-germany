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
package com.aionemu.gameserver.model.instance.instancereward;

import static ch.lambdaj.Lambda.maxFrom;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.sort;

import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.mutable.MutableInt;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.geometry.Point3D;
import com.aionemu.gameserver.model.instance.playerreward.NeviwindCanyonPlayerReward;
import com.aionemu.gameserver.model.instance.playerreward.PvPArenaPlayerReward;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INSTANCE_SCORE;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;

/**
 * @author Falke_34
 */
public class NeviwindCanyonReward extends InstanceReward<NeviwindCanyonPlayerReward> {

	private MutableInt asmodiansPoints = new MutableInt(3800);
	private MutableInt elyosPoins = new MutableInt(3800);

	private MutableInt asmodiansPvpKills = new MutableInt(0);
	private MutableInt elyosPvpKills = new MutableInt(0);	
	
    private int capPoints;
    private Race race;
    protected WorldMapInstance instance;
    private long instanceTime;
    private int bonusTime;
    private final byte buffId;
    
    public NeviwindCanyonReward(Integer mapId, int instanceId, WorldMapInstance instance) {
        super(mapId, instanceId);
        this.instance = instance;
        capPoints = 500000;
        bonusTime = 12000;
        buffId = 10;
    }

	public int AbyssReward(boolean isWin, boolean isBossKilled) {
		int BossKilled = 1993;
		int Win = 3163;
		int Loss = 1031;

		if (isBossKilled) {
			return isWin ? (Win + BossKilled) : (Loss + BossKilled);
		}
		else {
			return isWin ? Win : Loss;
		}
	}

	public int GloryReward(boolean isWin, boolean isBossKilled) {
		int BossKilled = 50;
		int Win = 150;
		int Loss = 75;

		if (isBossKilled) {
			return isWin ? (Win + BossKilled) : (Loss + BossKilled);
		}
		else {
			return isWin ? Win : Loss;
		}
	}
    
    public int ExpReward(boolean isWin, boolean isBossKilled) {
        int BossKilled = 20000;
        int Win = 10000;
        int Loss = 5000;

		if (isBossKilled) {
			return isWin ? (Win + BossKilled) : (Loss + BossKilled);
		}
		else {
			return isWin ? Win : Loss;
		}
	}
    
    public List<NeviwindCanyonPlayerReward> sortPoints() {
    	return sort(getInstanceRewards(), on(PvPArenaPlayerReward.class).getScorePoints(), new Comparator<Integer>() {

            @Override
			public int compare(Integer o1, Integer o2) {
				return o2 != null ? o2.compareTo(o1) : -o1.compareTo(o2);
			}
        });
    }
    
    public void portToPosition(Player player) {
		/*
		 * Get Random Position (Elyos - Asmo)
		 */
		float Rx = Rnd.get(-5, 5);
		float Ry = Rnd.get(-5, 5);
        Point3D asmodiansStartPosition = new Point3D(1094.0016f + Rx, 752.5455f + Ry, 336.30457f);
        Point3D elyosStartPosition = new Point3D(402.67786f + Rx, 751.9347f + Ry, 336.30457f);

        if (player.getRace() == Race.ASMODIANS) {
            TeleportService2.teleportTo(player, mapId, instanceId, asmodiansStartPosition.getX(), asmodiansStartPosition.getY(), asmodiansStartPosition.getZ(), (byte) 45);
        }
        else {
            TeleportService2.teleportTo(player, mapId, instanceId, elyosStartPosition.getX(), elyosStartPosition.getY(), elyosStartPosition.getZ(), (byte) 45);
        }
    }
    
    public MutableInt getPointsByRace(final Race race) {
        return (race == Race.ELYOS) ? elyosPoins : ((race == Race.ASMODIANS) ? asmodiansPoints : null);
    }
    
    public void addPointsByRace(Race race, int points) {
        MutableInt pointsByRace = getPointsByRace(race);
        pointsByRace.add(points);
        if (pointsByRace.intValue() < 0) {
            pointsByRace.setValue(0);
        }
    }
    
    public MutableInt getPvpKillsByRace(Race race) {
        return (race == Race.ELYOS) ? elyosPvpKills : (race == Race.ASMODIANS) ? asmodiansPvpKills : null;
    }
    
    public void addPvpKillsByRace(Race race, int points) {
        MutableInt pvpKillsByRace = getPvpKillsByRace(race);
        pvpKillsByRace.add(points);
        if (pvpKillsByRace.intValue() < 0) {
            pvpKillsByRace.setValue(0);
        }
    }
    
    public void setWinnerRace(final Race race) {
        this.race = race;
    }
    
    public Race getWinnerRace() {
        return race;
    }
    
    public Race getWinnerRaceByScore() {
        return asmodiansPoints.compareTo(elyosPoins) > 0 ? Race.ASMODIANS : Race.ELYOS;
    }
    
    @Override
    public void clear() {
        super.clear();
    }
    
    public void regPlayerReward(Player player) {
        if (!containPlayer(player.getObjectId())) {
            addPlayerReward(new NeviwindCanyonPlayerReward(player.getObjectId(), bonusTime, player.getRace()));
        }
    }
    
    @Override
    public void addPlayerReward(NeviwindCanyonPlayerReward reward) {
        super.addPlayerReward(reward);
    }
    
    @Override
    public NeviwindCanyonPlayerReward getPlayerReward(Integer object) {
        return (NeviwindCanyonPlayerReward) super.getPlayerReward(object);
    }
    
    public void sendPacket(final int type, final Integer object) {
        instance.doOnAllPlayers(new Visitor<Player>() {

            @Override
            public void visit(Player player) {
                PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(type, getTime(), getInstanceReward(), object));
            }
        });
    }
    
    public int getTime() {
        long result = System.currentTimeMillis() - instanceTime;
        if (result < 90000) {
            return (int) (90000 - result);
        }
        if (result < 1800000) {
            return (int)(1800000 - (result - 90000));
        }
        return 0;
    }
    
    public byte getBuffId() {
        return buffId;
    }

    public void setInstanceStartTime() {
        this.instanceTime = System.currentTimeMillis();
    }
    
	public int getCapPoints() {
		return capPoints;
	}
    
	public boolean hasCapPoints() {
		return maxFrom(getInstanceRewards()).getPoints() >= capPoints;
	}
}
