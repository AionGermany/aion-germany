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
package com.aionemu.gameserver.model.dynamicportal;

import java.util.*;
import javolution.util.FastMap;

import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.dynamicportal.DynamicPortalTemplate;
import com.aionemu.gameserver.services.dynamicportal.DynamicPortal;

/**
 * @author Falke_34
 */
public class DynamicPortalLocation {

	protected int id;
	protected boolean isActive;
	protected DynamicPortalTemplate template;
	protected DynamicPortal<DynamicPortalLocation> activeDynamicPortal;
	protected FastMap<Integer, Player> players = new FastMap<Integer, Player>();
	private final List<VisibleObject> spawned = new ArrayList<VisibleObject>();
	
	public DynamicPortalLocation() {
	}
	
	public DynamicPortalLocation(DynamicPortalTemplate template) {
		this.template = template;
		this.id = template.getId();
	}
	
	public boolean isActive() {
		return isActive;
	}
	
	public void setActiveDynamicPortal(DynamicPortal<DynamicPortalLocation> dynamicPortal) {
		isActive = dynamicPortal != null;
		this.activeDynamicPortal = dynamicPortal;
	}
	
	public DynamicPortal<DynamicPortalLocation> getActiveDynamicPortal() {
		return activeDynamicPortal;
	}
	
	public final DynamicPortalTemplate getTemplate() {
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
