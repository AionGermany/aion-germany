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

import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.sort;

import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.mutable.MutableInt;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.geometry.Point3D;
import com.aionemu.gameserver.model.instance.playerreward.PvPArenaPlayerReward;
import com.aionemu.gameserver.model.instance.playerreward.SteelWallBastionBattlefieldPlayerReward;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INSTANCE_SCORE;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;

/**
 * @author Eloann
 */
public class SteelWallBastionBattlefieldReward extends InstanceReward<SteelWallBastionBattlefieldPlayerReward> {

	/**
	 * Calculates Total Asmo & Elyos Point<br>
	 * <br>
	 * Default Start Points: 1000
	 */
	private MutableInt asmodiansPoints = new MutableInt(0);
	private MutableInt elyosPoins = new MutableInt(0);

	/**
	 * Calculates Total Asmo & Elyos PvP Kills<br>
	 * <br>
	 * Default PvP Kills: 0
	 */
	private MutableInt asmodiansPvpKills = new MutableInt(0);
	private MutableInt elyosPvpKills = new MutableInt(0);

	// Determines the Winner or Looser Race
	private Race race;

	protected WorldMapInstance instance;

	private int capPoints;
	private int winnerPoints;
	private int looserPoints;
	private int bonusTime;
	private long instanceTime;
	private final byte buffId;

	public SteelWallBastionBattlefieldReward(Integer mapId, int instanceId, WorldMapInstance instance) {
		super(mapId, instanceId);
		this.instance = instance;
		asmodiansPoints = new MutableInt(3800);
		elyosPoins = new MutableInt(3800);
		asmodiansPvpKills = new MutableInt(0);
		elyosPvpKills = new MutableInt(0);
		winnerPoints = 3000;
		looserPoints = 2500;
		capPoints = 30000;
		bonusTime = 12000;
		buffId = 12;
	}

	@Override
	public void clear() {
		super.clear();
	}

	public void regPlayerReward(Player player) {
		if (!containPlayer(player.getObjectId())) {
			addPlayerReward(new SteelWallBastionBattlefieldPlayerReward(player.getObjectId(), bonusTime, player.getRace()));
		}
	}

	@Override
	public void addPlayerReward(SteelWallBastionBattlefieldPlayerReward reward) {
		super.addPlayerReward(reward);
	}

	@Override
	public SteelWallBastionBattlefieldPlayerReward getPlayerReward(Integer object) {
		return (SteelWallBastionBattlefieldPlayerReward) super.getPlayerReward(object);
	}

	public List<SteelWallBastionBattlefieldPlayerReward> sortPoints() {
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
		Point3D ElyosStartPoint = new Point3D(570.468f + Rx, 166.897f + Ry, 432.28986f);// Elyos Center
		Point3D AsmoStartPoint = new Point3D(400.741f + Rx, 166.713f + Ry, 432.290f);// Asmo Center

		if (player.getRace() == Race.ASMODIANS) {
			TeleportService2.teleportTo(player, mapId, instanceId, AsmoStartPoint.getX(), AsmoStartPoint.getY(), AsmoStartPoint.getZ(), (byte) 45);
		}
		else {
			TeleportService2.teleportTo(player, mapId, instanceId, ElyosStartPoint.getX(), ElyosStartPoint.getY(), ElyosStartPoint.getZ(), (byte) 105);
		}
	}

	public MutableInt getPointsByRace(Race race) {
		return (race == Race.ELYOS) ? elyosPoins : (race == Race.ASMODIANS) ? asmodiansPoints : null;
	}

	public void addPointsByRace(Race race, int points) {
		MutableInt racePoints = getPointsByRace(race);
		racePoints.add(points);
		if (racePoints.intValue() < 0) {
			racePoints.setValue(0);
		}
	}

	public MutableInt getPvpKillsByRace(Race race) {
		return (race == Race.ELYOS) ? elyosPvpKills : (race == Race.ASMODIANS) ? asmodiansPvpKills : null;
	}

	public void addPvpKillsByRace(Race race, int points) {
		MutableInt racePoints = getPvpKillsByRace(race);
		racePoints.add(points);
		if (racePoints.intValue() < 0) {
			racePoints.setValue(0);
		}
	}

	public void setWinningRace(Race race) {
		this.race = race;
	}

	public Race getWinningRace() {
		return race;
	}

	public Race getWinningRaceByScore() {
		return asmodiansPoints.compareTo(elyosPoins) > 0 ? Race.ASMODIANS : Race.ELYOS;
	}

	public void setInstanceStartTime() {
		this.instanceTime = System.currentTimeMillis();
	}

	public int getTime() {
		long result = System.currentTimeMillis() - instanceTime;
		if (result < 45000) {
			return (int) (45000 - result);
		}
		if (result < 1800000) { // 30 Minutes
			return (int) (1800000 - (result - 20000));
		}
		return 0;
	}

	public int getWinnerPoints() {
		return winnerPoints;
	}

	public int getCapPoints() {
		return capPoints;
	}

	public int getLooserPoints() {
		return looserPoints;
	}

	public byte getBuffId() {
		return buffId;
	}

	public void sendPacket(final int type, final Integer object) {
		instance.doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(type, getTime(), getInstanceReward(), object));
			}
		});
	}
}
