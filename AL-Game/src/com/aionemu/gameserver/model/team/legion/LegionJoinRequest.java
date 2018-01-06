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
package com.aionemu.gameserver.model.team.legion;

import java.sql.Timestamp;

import com.aionemu.gameserver.model.gameobjects.player.Player;

/**
 * @author CoolyT
 */
public class LegionJoinRequest {

	private int legionId = 0;
	private int playerId = 0;
	private String playerName = "";
	private int playerClass = 0;
	private int race = 0;
	private int level = 0;
	private int genderId = 0;
	private String msg = "";
	private Timestamp timestamp = new Timestamp(System.currentTimeMillis());

	public LegionJoinRequest(int legionId, Player player, String msg) {
		this.legionId = legionId;
		this.playerId = player.getObjectId();
		this.playerName = player.getName();
		this.playerClass = player.getPlayerClass().ordinal();
		this.race = player.getRace().getRaceId();
		this.level = player.getLevel();
		this.genderId = player.getGender().getGenderId();
		this.msg = msg;
	}

	public LegionJoinRequest() {

	}

	/**
	 * @return the legionId
	 */
	public int getLegionId() {
		return legionId;
	}

	/**
	 * @param legionId
	 *            the legionId to set
	 */
	public void setLegionId(int legionId) {
		this.legionId = legionId;
	}

	/**
	 * @return the playerId
	 */
	public int getPlayerId() {
		return playerId;
	}

	/**
	 * @param playerId
	 *            the playerId to set
	 */
	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	/**
	 * @return the playerName
	 */
	public String getPlayerName() {
		return playerName;
	}

	/**
	 * @param playerName
	 *            the playerName to set
	 */
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	/**
	 * @return the playerClass
	 */
	public int getPlayerClass() {
		return playerClass;
	}

	/**
	 * @param playerClass
	 *            the playerClass to set
	 */
	public void setPlayerClass(int playerClass) {
		this.playerClass = playerClass;
	}

	/**
	 * @return the race
	 */
	public int getRace() {
		return race;
	}

	/**
	 * @param race
	 *            the race to set
	 */
	public void setRace(int race) {
		this.race = race;
	}

	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @param level
	 *            the level to set
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * @return the genderId
	 */
	public int getGenderId() {
		return genderId;
	}

	/**
	 * @param genderId
	 *            the genderId to set
	 */
	public void setGenderId(int genderId) {
		this.genderId = genderId;
	}

	/**
	 * @return the JoinRequestMessage
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @param msg
	 *            the JoinRequestMessage to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * @return the date
	 */
	public Timestamp getDate() {
		return timestamp;
	}

	/**
	 * @param timestamp
	 *            the date to set
	 */
	public void setDate(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

}
