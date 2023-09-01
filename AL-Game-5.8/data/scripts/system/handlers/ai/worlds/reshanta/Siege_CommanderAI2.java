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
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.AbyssLandingService;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

import ai.AggressiveNpcAI2;

@AIName("siege_commander")
public class Siege_CommanderAI2 extends AggressiveNpcAI2 {

	@Override
	protected void handleDied() {
		switch (getNpcId()) {
			// Elyos Commander
			case 883635: // Krotan Commander <15,000 Points>
				updateCommanderLanding1(4);
				break;
			case 883665: // Miren Commander <15,000 Points>
				updateCommanderLanding1(5);
				break;
			case 883666: // Kysis Commander <20,000 Points>
				updateCommanderLanding2(6);
				break;

			// Asmodians Commander
			case 883636: // Krotan Commander <15,000 Points>
				updateCommanderLanding1(16);
				break;
			case 883667: // Miren Commander <15,000 Points>
				updateCommanderLanding1(17);
				break;
			case 883668: // Kysis Commander <20,000 Points>
				updateCommanderLanding2(18);
				break;
		}

		super.handleDied();
		AI2Actions.deleteOwner(this);
	}

	private void updateCommanderLanding1(final int id) {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				if (MathUtil.isIn3dRange(getOwner().getAggroList().getMostHated(), getOwner(), 20)) {
					if (getOwner().getAggroList().getPlayerWinnerRace() == Race.ASMODIANS) {
						AbyssLandingService.getInstance().onDieCommander(Race.ASMODIANS, id, 15000);
					}
					else if (getOwner().getAggroList().getPlayerWinnerRace() == Race.ELYOS) {
						AbyssLandingService.getInstance().onDieCommander(Race.ELYOS, id, 15000);
					}
				}
			}
		});
	}

	private void updateCommanderLanding2(final int id) {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				if (MathUtil.isIn3dRange(getOwner().getAggroList().getMostHated(), getOwner(), 20)) {
					if (getOwner().getAggroList().getPlayerWinnerRace() == Race.ASMODIANS) {
						AbyssLandingService.getInstance().onDieCommander(Race.ASMODIANS, id, 20000);
					}
					else if (getOwner().getAggroList().getPlayerWinnerRace() == Race.ELYOS) {
						AbyssLandingService.getInstance().onDieCommander(Race.ELYOS, id, 20000);
					}
				}
			}
		});
	}
}
