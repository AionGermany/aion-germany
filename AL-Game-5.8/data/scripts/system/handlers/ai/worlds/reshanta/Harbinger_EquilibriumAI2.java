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

@AIName("harbinger_equilibrium")
public class Harbinger_EquilibriumAI2 extends NpcAI2 {

	@Override
	protected void handleDied() {
		switch (getNpcId()) {
			case 883931: // Harbinger's Equilibrium <6,000 Points>
			case 883932: // Harbinger's Equilibrium <6,000 Points>
			case 883933: // Harbinger's Equilibrium <6,000 Points>
				updateEquilibriumLanding1();
				break;
			case 883934: // Harbinger's Equilibrium <10,000 Points>
			case 883935: // Harbinger's Equilibrium <10,000 Points>
				updateEquilibriumLanding2();
				break;
			case 883936: // Harbinger's Equilibrium <16,000 Points>
			case 883937: // Harbinger's Equilibrium <16,000 Points>
				updateEquilibriumLanding3();
				break;
			case 883938: // Harbinger's Equilibrium <30,000 Points>
				updateEquilibriumLanding4();
				break;
		}
		super.handleDied();
		AI2Actions.deleteOwner(this);
	}

	private void updateEquilibriumLanding1() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				if (MathUtil.isIn3dRange(getOwner().getAggroList().getMostHated(), getOwner(), 20)) {
					if (getOwner().getAggroList().getPlayerWinnerRace() == Race.ASMODIANS) {
						AbyssLandingService.getInstance().onRewardFacility(Race.ASMODIANS, 6000);
					}
					else if (getOwner().getAggroList().getPlayerWinnerRace() == Race.ELYOS) {
						AbyssLandingService.getInstance().onRewardFacility(Race.ELYOS, 6000);
					}
				}
			}
		});
	}

	private void updateEquilibriumLanding2() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				if (MathUtil.isIn3dRange(getOwner().getAggroList().getMostHated(), getOwner(), 20)) {
					if (getOwner().getAggroList().getPlayerWinnerRace() == Race.ASMODIANS) {
						AbyssLandingService.getInstance().onRewardFacility(Race.ASMODIANS, 10000);
					}
					else if (getOwner().getAggroList().getPlayerWinnerRace() == Race.ELYOS) {
						AbyssLandingService.getInstance().onRewardFacility(Race.ELYOS, 10000);
					}
				}
			}
		});
	}

	private void updateEquilibriumLanding3() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				if (MathUtil.isIn3dRange(getOwner().getAggroList().getMostHated(), getOwner(), 20)) {
					if (getOwner().getAggroList().getPlayerWinnerRace() == Race.ASMODIANS) {
						AbyssLandingService.getInstance().onRewardFacility(Race.ASMODIANS, 16000);
					}
					else if (getOwner().getAggroList().getPlayerWinnerRace() == Race.ELYOS) {
						AbyssLandingService.getInstance().onRewardFacility(Race.ELYOS, 16000);
					}
				}
			}
		});
	}

	private void updateEquilibriumLanding4() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				if (MathUtil.isIn3dRange(getOwner().getAggroList().getMostHated(), getOwner(), 20)) {
					if (getOwner().getAggroList().getPlayerWinnerRace() == Race.ASMODIANS) {
						AbyssLandingService.getInstance().onRewardFacility(Race.ASMODIANS, 30000);
					}
					else if (getOwner().getAggroList().getPlayerWinnerRace() == Race.ELYOS) {
						AbyssLandingService.getInstance().onRewardFacility(Race.ELYOS, 30000);
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
