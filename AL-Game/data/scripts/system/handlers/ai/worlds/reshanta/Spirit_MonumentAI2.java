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
package ai.worlds.reshanta;

import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.AbyssLandingService;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

@AIName("spirit_monument")
public class Spirit_MonumentAI2 extends NpcAI2 {

	@Override
	protected void handleDied() {
		switch (getNpcId()) {
			// Elyos Monument
			case 883922: // Krotan Guardian Spirit's Monument <20,000 Points>
				updateGuardianLanding(1);
				break;
			case 883923: // Miren Guardian Spirit's Monument <20,000 Points>
				updateGuardianLanding(2);
				break;
			case 883924: // Kysis Guardian Spirit's Monument <20,000 Points>
				updateGuardianLanding(3);
				break;

			// Asmodians Monument
			case 883941: // Krotan Guardian Spirit's Monument <20,000 Points>
				updateGuardianLanding(13);
				break;
			case 883942: // Miren Guardian Spirit's Monument <20,000 Points>
				updateGuardianLanding(14);
				break;
			case 883943: // Kysis Guardian Spirit's Monument <20,000 Points>
				updateGuardianLanding(15);
				break;
		}
		super.handleDied();
		AI2Actions.deleteOwner(this);
	}

	private void updateGuardianLanding(final int id) {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				if (MathUtil.isIn3dRange(getOwner().getAggroList().getMostHated(), getOwner(), 20)) {
					if (getOwner().getAggroList().getPlayerWinnerRace() == Race.ASMODIANS) {
						AbyssLandingService.getInstance().onDieMonuments(Race.ASMODIANS, id, 20000);
					}
					else if (getOwner().getAggroList().getPlayerWinnerRace() == Race.ELYOS) {
						AbyssLandingService.getInstance().onDieMonuments(Race.ELYOS, id, 20000);
					}
				}
			}
		});
	}

	@Override
	public boolean isMoveSupported() {
		return false;
	}
}
