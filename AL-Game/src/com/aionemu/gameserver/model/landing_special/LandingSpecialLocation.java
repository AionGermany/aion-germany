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
package com.aionemu.gameserver.model.landing_special;

import java.util.ArrayList;
import java.util.List;

import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.landing_special.LandingSpecialTemplate;
import com.aionemu.gameserver.services.abysslandingservice.landingspecialservice.SpecialLanding;

import javolution.util.FastMap;

public class LandingSpecialLocation {

	protected int id;
	protected boolean isActive;
	protected LandingSpecialStateType type;
	protected LandingSpecialTemplate template;
	protected SpecialLanding<LandingSpecialLocation> activeLandingSpecial;
	protected FastMap<Integer, Player> players = new FastMap<Integer, Player>();
	private final List<VisibleObject> spawned = new ArrayList<VisibleObject>();

	public LandingSpecialLocation() {
	}

	public LandingSpecialLocation(LandingSpecialTemplate template) {
		this.template = template;
		this.id = template.getId();
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActiveLanding(SpecialLanding<LandingSpecialLocation> landingSpecial) {
		isActive = landingSpecial != null;
		this.activeLandingSpecial = landingSpecial;
	}

	public SpecialLanding<LandingSpecialLocation> getActiveLandingSpecial() {
		return activeLandingSpecial;
	}

	public final LandingSpecialTemplate getTemplate() {
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

	public void setType(LandingSpecialStateType type) {
		this.type = type;
	}

	public LandingSpecialStateType getType() {
		return this.type;
	}
}
