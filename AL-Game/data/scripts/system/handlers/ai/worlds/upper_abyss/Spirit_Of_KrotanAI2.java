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

@AIName("spirit_of_krotan")
public class Spirit_Of_KrotanAI2 extends AggressiveNpcAI2 {

	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
	}

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		startLifeTask();
		announceSpiritOfKrotan();
		announceSpiritOfKrotanAppears();
	}

	private void startLifeTask() {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				World.getInstance().doOnAllPlayers(new Visitor<Player>() {

					@Override
					public void visit(Player player) {
						AI2Actions.deleteOwner(Spirit_Of_KrotanAI2.this);
						// The Krotan Berserker Soul has disappeared.
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_Ab1_BossNamed_65_Al_Despawn_Crotan);
					}
				});
			}
		}, 1800000); // 30 Minutes.
	}

	private void announceSpiritOfKrotanAppears() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				// Krotan Guardian Appears.
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_Ab1_Crotan_Named_Spawn_In);
			}
		});
	}

	private void announceSpiritOfKrotan() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				// The Krotan Berserker Soul has appeared.
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_Ab1_BossNamed_65_Al_Spawn_Crotan);
			}
		});
	}

	@Override
	protected void handleDied() {
		updateKrotanLanding();
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				// The Krotan Berserker Soul has been slain.
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_Ab1_BossNamed_65_Al_Die_Crotan);
			}
		});
		super.handleDied();
		AI2Actions.deleteOwner(this);
	}

	private void updateKrotanLanding() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				if (MathUtil.isIn3dRange(getOwner().getAggroList().getMostHated(), getOwner(), 20)) {
					if (getOwner().getAggroList().getPlayerWinnerRace() == Race.ASMODIANS) {
						AbyssLandingService.getInstance().onRewardMonuments(Race.ASMODIANS, 13, 20000);
					}
					else if (getOwner().getAggroList().getPlayerWinnerRace() == Race.ELYOS) {
						AbyssLandingService.getInstance().onRewardMonuments(Race.ELYOS, 1, 20000);
					}
				}
			}
		});
	}
}
