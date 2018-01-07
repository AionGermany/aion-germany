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

@AIName("defeat_monument")
public class Defeat_MonumentAI2 extends NpcAI2 {

	@Override
	protected void handleDied() {
		switch (getNpcId()) {
			case 883944: // Ide Cannon Tumon 1 Defeat Monument <10,000 Points>
				updateDefeatLanding1(19);
				break;
			case 883945: // Ide Cannon Tumon 2 Defeat Monument <10,000 Points>
				updateDefeatLanding1(20);
				break;
			case 883946: // Artillery Tumon 1 Defeat Monument <10,000 Points>
				updateDefeatLanding1(21);
				break;
			case 883947: // Artillery Tumon 1 Defeat Monument <10,000 Points>
				updateDefeatLanding1(22);
				break;
			case 883948: // Wurg The Glacier Defeat Monument <15,000 Points>
				updateDefeatLanding2(23);
				break;
			case 883949: // Terracrusher Defeat Monument <15,000 Points>
				updateDefeatLanding2(24);
				break;
		}
		super.handleDied();
		AI2Actions.deleteOwner(this);
	}

	private void updateDefeatLanding1(final int id) {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				if (MathUtil.isIn3dRange(getOwner().getAggroList().getMostHated(), getOwner(), 20)) {
					if (getOwner().getAggroList().getPlayerWinnerRace() == Race.ASMODIANS) {
						AbyssLandingService.getInstance().onDieMonuments(Race.ASMODIANS, id, 10000);
					}
					else if (getOwner().getAggroList().getPlayerWinnerRace() == Race.ELYOS) {
						AbyssLandingService.getInstance().onDieMonuments(Race.ELYOS, id, 10000);
					}
				}
			}
		});
	}

	private void updateDefeatLanding2(final int id) {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				if (MathUtil.isIn3dRange(getOwner().getAggroList().getMostHated(), getOwner(), 20)) {
					if (getOwner().getAggroList().getPlayerWinnerRace() == Race.ASMODIANS) {
						AbyssLandingService.getInstance().onDieMonuments(Race.ASMODIANS, id, 15000);
					}
					else if (getOwner().getAggroList().getPlayerWinnerRace() == Race.ELYOS) {
						AbyssLandingService.getInstance().onDieMonuments(Race.ELYOS, id, 15000);
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
