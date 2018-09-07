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

import org.apache.commons.lang.mutable.MutableInt;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.geometry.Point3D;
import com.aionemu.gameserver.model.instance.playerreward.DredgionPlayerReward;
import com.aionemu.gameserver.services.teleport.TeleportService2;

import javolution.util.FastList;

/**
 * @author xTz
 */
public class DredgionReward extends InstanceReward<DredgionPlayerReward> {

	private int winnerPoints;
	private int looserPoints;
	@SuppressWarnings("unused")
	private int drawPoins;
	private MutableInt asmodiansPoints = new MutableInt(0);
	private MutableInt elyosPoins = new MutableInt(0);
	private Race race;
	private FastList<DredgionRooms> dredgionRooms = new FastList<DredgionRooms>();
	private Point3D asmodiansStartPosition;
	private Point3D elyosStartPosition;

	public DredgionReward(Integer mapId, int instanceId) {
		super(mapId, instanceId);
		winnerPoints = mapId == 300110000 ? 3000 : 4500;
		looserPoints = mapId == 300110000 ? 1500 : 2500;
		drawPoins = mapId == 300110000 ? 2250 : 3750;
		setStartPositions();
		for (int i = 1; i < 15; i++) {
			dredgionRooms.add(new DredgionRooms(i));
		}
	}

	private void setStartPositions() {
		Point3D a = new Point3D(570.468f, 166.897f, 432.28986f);
		Point3D b = new Point3D(400.741f, 166.713f, 432.290f);

		if (Rnd.get(2) == 0) {
			asmodiansStartPosition = a;
			elyosStartPosition = b;
		}
		else {
			asmodiansStartPosition = b;
			elyosStartPosition = a;
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

	/**
	 * 1 Primary Armory 2 Backup Armory 3 Gravity Control 4 Engine Room 5 Auxiliary Power 6 Weapons Deck 7 Lower Weapons Deck 8 Ready Room 1 9 Ready Room 2 10 Barracks 11 Logistics Managment 12
	 * Logistics Storage 13 The Bridge 14 Captain's Room
	 */
	public class DredgionRooms {

		private int roomId;
		private int state = 0xFF;

		public DredgionRooms(int roomId) {
			this.roomId = roomId;
		}

		public int getRoomId() {
			return roomId;
		}

		public void captureRoom(Race race) {
			state = race.equals(Race.ASMODIANS) ? 0x01 : 0x00;
		}

		public int getState() {
			return state;
		}
	}

	public FastList<DredgionRooms> getDredgionRooms() {
		return dredgionRooms;
	}

	public DredgionRooms getDredgionRoomById(int roomId) {
		for (DredgionRooms dredgionRoom : dredgionRooms) {
			if (dredgionRoom.getRoomId() == roomId) {
				return dredgionRoom;
			}
		}
		return null;
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
		dredgionRooms.clear();
	}
}
