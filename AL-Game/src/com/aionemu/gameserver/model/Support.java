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
package com.aionemu.gameserver.model;

import com.aionemu.gameserver.model.gameobjects.player.Player;

/**
 * @author paranaix
 */
public class Support {

	private Player owner;
	private String summary = "";
	private String description = "";

	public Support(Player owner, String summary, String description) {
		this.owner = owner;
		this.summary = summary;
		this.description = description;
	}

	public Support(Player owner) {
		this(owner, "", "");
	}

	public Player getOwner() {
		return owner;
	}

	public String getSummary() {
		return summary;
	}

	public String getDescription() {
		return description;
	}

	public void setOwner(Player owner) {
		if (owner == null) {
			throw new IllegalArgumentException("Owner cant be null");
		}
		this.owner = owner;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
