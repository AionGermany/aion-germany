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
package com.aionemu.gameserver.model.landing;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.landing.LandingTemplate;
import com.aionemu.gameserver.services.abysslandingservice.Landing;

import javolution.util.FastMap;

public class LandingLocation {

	protected int siege;
	protected int commander;
	protected int artifact;
	protected int base;
	protected int monuments;
	protected int quest;
	protected int facility;
	protected Timestamp levelUpDate;
	protected int id;
	protected int level;
	protected int points;
	protected boolean isActive;
	protected Race race;
	protected LandingTemplate template;
	protected Landing<LandingLocation> activeLanding;
	protected FastMap<Integer, Player> players = new FastMap<Integer, Player>();
	private final List<VisibleObject> spawned = new ArrayList<VisibleObject>();
	private PersistentState persistentState;

	public LandingLocation() {
	}

	public LandingLocation(LandingTemplate template) {
		this.template = template;
		this.id = template.getId();
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActiveLanding(Landing<LandingLocation> landing) {
		isActive = landing != null;
		this.activeLanding = landing;
	}

	public Landing<LandingLocation> getActiveLanding() {
		return activeLanding;
	}

	public final LandingTemplate getTemplate() {
		return template;
	}

	public int getId() {
		return id;
	}

	public List<VisibleObject> getSpawned() {
		return spawned;
	}

	public FastMap<Integer, Player> getPlayers() {
		return players;
	}

	public int getLevel() {
		if (this.level == 0) {
			return this.level + 1;
		}
		else {
			return this.level;
		}
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getPoints() {
		return this.points;
	}

	public void setPoints(int pts) {
		this.points = pts;
	}

	public int getSiegePoints() {
		return this.siege;
	}

	public void setSiegePoints(int pts) {
		this.siege = pts;
	}

	public int getCommanderPoints() {
		return this.commander;
	}

	public void setCommanderPoints(int pts) {
		this.commander = pts;
	}

	public int getArtifactPoints() {
		return this.artifact;
	}

	public void setArtifactPoints(int pts) {
		this.artifact = pts;
	}

	public int getBasePoints() {
		return this.base;
	}

	public void setBasePoints(int pts) {
		this.base = pts;
	}

	public int getQuestPoints() {
		return this.quest;
	}

	public void setQuestPoints(int pts) {
		this.quest = pts;
	}

	public int getFacilityPoints() {
		return this.facility;
	}

	public void setFacilityPoints(int pts) {
		this.facility = pts;
	}

	public int getMonumentsPoints() {
		return this.monuments;
	}

	public void setMonumentsPoints(int pts) {
		this.monuments = pts;
	}

	public Timestamp getLevelUpDate() {
		return levelUpDate;
	}

	public Timestamp setLevelUpDate(Timestamp timestamp) {
		return levelUpDate = timestamp;
	}

	public PersistentState getPersistentState() {
		return persistentState;
	}

	public void setPersistentState(PersistentState state) {
		if (this.persistentState == PersistentState.NEW && state == PersistentState.UPDATE_REQUIRED) {
			return;
		}
		else {
			this.persistentState = state;
		}
	}

	public Race getRace() {
		return this.race;
	}

	public Race setRace(Race race) {
		return this.race = race;
	}
}
