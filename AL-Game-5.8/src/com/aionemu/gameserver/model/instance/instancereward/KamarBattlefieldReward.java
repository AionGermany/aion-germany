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
import com.aionemu.gameserver.model.instance.playerreward.KamarBattlefieldPlayerReward;
import com.aionemu.gameserver.model.instance.playerreward.PvPArenaPlayerReward;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INSTANCE_SCORE;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;

/**
 * @author Alcapwnd
 */
public class KamarBattlefieldReward extends InstanceReward<KamarBattlefieldPlayerReward> {

	private int winnerPoints;
	private int looserPoints;
	private int capPoints;
	private MutableInt asmodiansPoints = new MutableInt(3800);
	private MutableInt elyosPoins = new MutableInt(3800);
	private MutableInt asmodiansPvpKills = new MutableInt(0);
	private MutableInt elyosPvpKills = new MutableInt(0);
	private Race race;
	private Point3D asmodiansStartPosition;
	private Point3D elyosStartPosition;
	protected WorldMapInstance instance;
	private long instanceTime;
	private int bonusTime;

	public KamarBattlefieldReward(Integer mapId, int instanceId, WorldMapInstance instance) {
		super(mapId, instanceId);
		this.instance = instance;
		winnerPoints = 3000;
		looserPoints = 2500;
		capPoints = 30000;
		bonusTime = 12000;
		setStartPositions();
	}

	public List<KamarBattlefieldPlayerReward> sortPoints() {
		return sort(getInstanceRewards(), on(PvPArenaPlayerReward.class).getScorePoints(), new Comparator<Integer>() {

			@Override
			public int compare(Integer o1, Integer o2) {
				return o2 != null ? o2.compareTo(o1) : -o1.compareTo(o2);
			}
		});
	}

	private void setStartPositions() {
		Point3D a = new Point3D(1535.6626f, 1573.9294f, 612.42f);
		Point3D b = new Point3D(1463.7689f, 1227.7777f, 581.62f);
		Point3D c = new Point3D(1204.9827f, 1350.6719f, 612.91f);
		Point3D d = new Point3D(1098.1141f, 1540.7119f, 585.10f);
		if (Rnd.get(2) == 0) {
			asmodiansStartPosition = a;
			elyosStartPosition = c;
		}
		else {
			asmodiansStartPosition = b;
			elyosStartPosition = d;
		}
	}

	public void portToPosition(Player player) {
		if (player.getRace() == Race.ASMODIANS) {
			TeleportService2.teleportTo(player, mapId, instanceId, asmodiansStartPosition.getX(), asmodiansStartPosition.getY(), asmodiansStartPosition.getZ());
		}
		else {
			TeleportService2.teleportTo(player, mapId, instanceId, elyosStartPosition.getX(), elyosStartPosition.getY(), elyosStartPosition.getZ());
		}
	}

	public MutableInt getPointsByRace(Race race) {
		switch (race) {
			case ELYOS:
				return elyosPoins;
			case ASMODIANS:
				return asmodiansPoints;
			default:
				break;
		}
		return null;
	}

	public void addPointsByRace(Race race, int points) {
		MutableInt racePoints = getPointsByRace(race);
		racePoints.add(points);
		if (racePoints.intValue() < 0) {
			racePoints.setValue(0);
		}
	}

	public MutableInt getPvpKillsByRace(Race race) {
		switch (race) {
			case ELYOS:
				return elyosPvpKills;
			case ASMODIANS:
				return asmodiansPvpKills;
			default:
				break;
		}
		return null;
	}

	public void addPvpKillsByRace(Race race, int points) {
		MutableInt racePoints = getPvpKillsByRace(race);
		racePoints.add(points);
		if (racePoints.intValue() < 0) {
			racePoints.setValue(0);
		}
	}

	public int getLooserPoints() {
		return looserPoints;
	}

	public int getWinnerPoints() {
		return winnerPoints;
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

	@Override
	public void clear() {
		super.clear();
	}

	public void regPlayerReward(Player player) {
		if (!containPlayer(player.getObjectId())) {
			addPlayerReward(new KamarBattlefieldPlayerReward(player.getObjectId(), bonusTime, player.getRace()));
		}
	}

	@Override
	public void addPlayerReward(KamarBattlefieldPlayerReward reward) {
		super.addPlayerReward(reward);
	}

	@Override
	public KamarBattlefieldPlayerReward getPlayerReward(Integer object) {
		return (KamarBattlefieldPlayerReward) super.getPlayerReward(object);
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
		if (result < 45000) {
			return (int) (45000 - result);
		}
		else if (result < 1800000) { // 30 Minutes.
			return (int) (1800000 - (result - 20000));
		}
		return 0;
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
