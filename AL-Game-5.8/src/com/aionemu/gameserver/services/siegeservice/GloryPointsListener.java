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
package com.aionemu.gameserver.services.siegeservice;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.siege.SiegeLocation;
import com.aionemu.gameserver.services.abyss.AbyssPointsService;

/**
 * @author Ever
 */
public class GloryPointsListener extends AbyssPointsService.AddGPGlobalCallback {

	private final Siege<?> siege;

	public GloryPointsListener(Siege<?> siege) {
		this.siege = siege;
	}

	@Override
	public void onGloryPointsAdded(Player player, int gloryPoints) {
		SiegeLocation fortress = siege.getSiegeLocation();

		// Make sure that only GP earned near this fortress will be added
		// Abyss points can be added only while in the siege zones
		if (fortress.isInsideLocation(player)) {
			siege.addGloryPoints(player, gloryPoints);
		}
	}
}
