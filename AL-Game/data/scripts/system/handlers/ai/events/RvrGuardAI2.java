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
package ai.events;

import org.joda.time.DateTime;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.SiegeService;

import ai.AggressiveNpcAI2;

/**
 * @author Bobobear
 */
@AIName("rvr_guard")
// 831417, 831418
public class RvrGuardAI2 extends AggressiveNpcAI2 {

	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		// add player to event list for additional reward
		if (creature instanceof Player && getPosition().getMapId() == 600010000) {
			DateTime now = DateTime.now();
			int hour = now.getHourOfDay();
			if (hour >= 19 && hour <= 23) {
				Npc bossAsmo = getPosition().getWorldMapInstance().getNpc(220948);
				Npc bossElyos = getPosition().getWorldMapInstance().getNpc(220949);
				if (bossAsmo != null && bossElyos != null) {
					SiegeService.getInstance().checkRvrPlayerOnEvent((Player) creature);
				}
			}
		}
	}
}
