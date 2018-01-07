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
package ai.worlds.upper_abyss;

import ai.AggressiveNpcAI2;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.AbyssLandingService;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

@AIName("spirit_of_miren")
public class Spirit_Of_MirenAI2 extends AggressiveNpcAI2 {

	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
	}

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		startLifeTask();
		announceSpiritOfMiren();
		announceSpiritOfMirenAppears();
	}

	private void startLifeTask() {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				World.getInstance().doOnAllPlayers(new Visitor<Player>() {

					@Override
					public void visit(Player player) {
						AI2Actions.deleteOwner(Spirit_Of_MirenAI2.this);
						// The Miren Berserker Soul has disappeared..
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_Ab1_BossNamed_65_Al_Despawn_lamiren);
					}
				});
			}
		}, 1800000); // 30 Minutes.
	}

	private void announceSpiritOfMirenAppears() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				// Miren Guardian Appears.
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_Ab1_Lamiren_Named_Spawn_In);
			}
		});
	}

	private void announceSpiritOfMiren() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				// The Miren Berserker Soul has appeared.
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_Ab1_BossNamed_65_Al_Spawn_lamiren);
			}
		});
	}

	@Override
	protected void handleDied() {
		updateMirenLanding();
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				// The Miren Berserker Soul has been slain.
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_Ab1_BossNamed_65_Al_Die_lamiren);
			}
		});
		super.handleDied();
		AI2Actions.deleteOwner(this);
	}

	private void updateMirenLanding() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				if (MathUtil.isIn3dRange(getOwner().getAggroList().getMostHated(), getOwner(), 20)) {
					if (getOwner().getAggroList().getPlayerWinnerRace() == Race.ASMODIANS) {
						AbyssLandingService.getInstance().onRewardMonuments(Race.ASMODIANS, 14, 20000);
					}
					else if (getOwner().getAggroList().getPlayerWinnerRace() == Race.ELYOS) {
						AbyssLandingService.getInstance().onRewardMonuments(Race.ELYOS, 2, 20000);
					}
				}
			}
		});
	}
}
