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

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

@AIName("unsealed_krotan")
public class Krotan_Auxillary_GeneralAI2 extends AggressiveNpcAI2 {

	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
	}

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		announceUnsealedKrotan();
	}

	@Override
	protected void handleDied() {
		switch (getNpcId()) {
			// Krotan Refuge.
			case 279153:
			case 279447:
			case 279741:
				treasureChest();
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						spawn(701481, 2062.9846f, 1294.5881f, 2917.9434f, (byte) 44);
						spawn(701481, 2067.8923f, 1297.4761f, 2917.9434f, (byte) 38);
						spawn(701481, 2059.5066f, 1291.7311f, 2917.9434f, (byte) 47);
						spawn(701481, 2056.3684f, 1286.5242f, 2917.9434f, (byte) 51);
						spawn(701481, 2054.6772f, 1282.0839f, 2917.9434f, (byte) 54);
						spawn(701481, 2071.7107f, 1298.3964f, 2917.9434f, (byte) 34);
					}
				}, 10000);
				break;
		}
		super.handleDied();
	}

	private void treasureChest() {
		getPosition().getWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				// A treasure chest has appeared.
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_IDAbRe_Core_NmdC_BoxSpawn);
			}
		});
	}

	private void announceUnsealedKrotan() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				// Unsealed Krotan.
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_Ab1_Crotan_Named_Spawn);
			}
		});
	}
}
