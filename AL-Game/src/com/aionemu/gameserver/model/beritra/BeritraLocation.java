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
package com.aionemu.gameserver.model.beritra;

import java.util.ArrayList;
import java.util.List;

import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.beritra.BeritraTemplate;
import com.aionemu.gameserver.services.beritraservice.BeritraInvasion;

import javolution.util.FastMap;

/**
 * @author Cx3
 */
public class BeritraLocation {

	protected int id;
	protected boolean isActive;
	protected BeritraTemplate template;
	protected BeritraInvasion<BeritraLocation> activeBeritra;
	protected FastMap<Integer, Player> players = new FastMap<Integer, Player>();
	private final List<VisibleObject> spawned = new ArrayList<VisibleObject>();

	public BeritraLocation() {
	}

	public BeritraLocation(BeritraTemplate template) {
		this.template = template;
		this.id = template.getId();
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActiveBeritra(BeritraInvasion<BeritraLocation> beritra) {
		isActive = beritra != null;
		this.activeBeritra = beritra;
	}

	public BeritraInvasion<BeritraLocation> getActiveBeritra() {
		return activeBeritra;
	}

	public final BeritraTemplate getTemplate() {
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
}
