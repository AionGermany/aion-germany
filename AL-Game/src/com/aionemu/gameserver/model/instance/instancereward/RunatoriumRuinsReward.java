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
import com.aionemu.gameserver.model.instance.playerreward.RunatoriumRuinsPlayerReward;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.world.WorldMapInstance;

/**
 * @author GiGatR00n
 */
public class RunatoriumRuinsReward extends InstanceReward<RunatoriumRuinsPlayerReward> {

	/**
	 * Calculates Total Asmo & Elyos Point<br>
	 * <br>
	 * Default Start Points: 1000
	 */
	private MutableInt asmodiansPoints = new MutableInt(1000);
	private MutableInt elyosPoins = new MutableInt(1000);

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

	private long instanceStartTime;
	private long instanceEndTime;
	private long PreparingTime;
	private int bonusTime;

	public RunatoriumRuinsReward(Integer mapId, int instanceId, WorldMapInstance instance) {
		super(mapId, instanceId);
		this.instance = instance;
		this.PreparingTime = 97504; // v4.7.5.13 NA Retail
		this.instanceEndTime = 20 * 60 * 1000; // 20-minutes
		this.bonusTime = 12000;
	}

	/**
	 * Calculates the Abyss Bonus Reward for Winner and Looser Team individually. 5156(Win+BossKilled) : 3163(Win) 3024(Loss+BossKilled) : 1031(Loss)
	 */
	public int CalcBonusAbyssReward(boolean isWin, boolean isBossKilled) {
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

	/**
	 * Calculates the Glory Bonus Reward for Winner and Looser Team individually. 200(Win+BossKilled) : 150(Win) 125(Loss+BossKilled) : 75(Loss)
	 */
	public int CalcBonusGloryReward(boolean isWin, boolean isBossKilled) {
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

	@Override
	public void clear() {
		super.clear();
	}

	public void regPlayerReward(Player player) {
		if (!containPlayer(player.getObjectId())) {
			addPlayerReward(new RunatoriumRuinsPlayerReward(player.getObjectId(), bonusTime, player.getRace()));
		}
	}

	@Override
	public void addPlayerReward(RunatoriumRuinsPlayerReward reward) {
		super.addPlayerReward(reward);
	}

	@Override
	public RunatoriumRuinsPlayerReward getPlayerReward(Integer object) {
		return (RunatoriumRuinsPlayerReward) super.getPlayerReward(object);
	}

	public List<RunatoriumRuinsPlayerReward> sortPoints() {
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
		Point3D ElyosStartPoint = new Point3D(270.1437f + Rx, 348.6699f + Ry, 79.44365f);// Elyos Center
		Point3D AsmoStartPoint = new Point3D(258.5553f + Rx, 169.85149f + Ry, 79.430855f);// Asmo Center

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
		this.instanceStartTime = System.currentTimeMillis();
	}

	public long getPreparingTime() {
		return this.PreparingTime;
	}

	public long getEndTime() {
		return (this.PreparingTime + this.instanceEndTime);
	}

	public int getRemainingTime() {
		long result = System.currentTimeMillis() - instanceStartTime;
		if (result < PreparingTime) {
			return (int) (PreparingTime - result);// ...Count Down Before Starting Instance. 60,59,58,57,...,0
		}
		else if (result < getEndTime()) {// ..........20 Minutes + PreparingTime(1.30 Min)
			return (int) (instanceEndTime - (result - PreparingTime));
		}
		return 0;
	}
}
