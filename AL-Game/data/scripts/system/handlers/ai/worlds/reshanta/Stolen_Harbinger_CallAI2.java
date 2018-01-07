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
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

@AIName("stolen_harbinger_call")
public class Stolen_Harbinger_CallAI2 extends NpcAI2 {

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		SkillEngine.getInstance().getSkill(getOwner(), 22781, 1, getOwner()).useNoAnimationSkill(); // Guardian Sanctuary Icon
		SkillEngine.getInstance().getSkill(getOwner(), 22783, 1, getOwner()).useNoAnimationSkill(); // Guardian Sanctuary Field
	}

	@Override
	protected void handleDied() {
		switch (getNpcId()) {
			case 884030: // Stolen Harbinger's Call <6,000 Points>
			case 884031: // Stolen Harbinger's Call <6,000 Points>
			case 884032: // Stolen Harbinger's Call <6,000 Points>
				updateHarbingerLanding1();
				break;
			case 884033: // Stolen Harbinger's Call <10,000 Points>
			case 884034: // Stolen Harbinger's Call <10,000 Points>
				updateHarbingerLanding2();
				break;
			case 884035: // Stolen Harbinger's Call <16,000 Points>
			case 884036: // Stolen Harbinger's Call <16,000 Points>
				updateHarbingerLanding3();
				break;
			case 884037: // Stolen Harbinger's Call <30,000 Points>
				updateHarbingerLanding4();
				break;
		}
		super.handleDied();
		AI2Actions.deleteOwner(this);
	}

	private void updateHarbingerLanding1() {
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

	private void updateHarbingerLanding2() {
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

	private void updateHarbingerLanding3() {
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

	private void updateHarbingerLanding4() {
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
