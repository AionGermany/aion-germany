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

package ai.instance.danuarSanctuary;

import java.util.List;

import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.WorldPosition;

import ai.ActionItemNpcAI2;

/**
 * @author Eloann
 */
@AIName("sanctuary_cannon_1")
public class SanctuaryCannon1AI2 extends ActionItemNpcAI2 {

	@Override
	protected void handleUseItemFinish(Player player) {
		// Use the cannon on the 2nd floor to destroy the door in the 3rd floor.
		WorldPosition worldPosition = player.getPosition();
		if (worldPosition.isInstanceMap()) {
			if (worldPosition.getMapId() == 301140000) {
				WorldMapInstance worldMapInstance = worldPosition.getWorldMapInstance();
				killNpc(worldMapInstance.getNpcs(730866));
			}
		}
	}

	private void killNpc(List<Npc> npcs) {
		for (Npc npc : npcs) {
			AI2Actions.killSilently(this, npc);
		}
	}
}
