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

@AIName("unsealed_miren")
public class Miren_Master_GeneralAI2 extends AggressiveNpcAI2 {

	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
	}

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		announceUnsealedMiren();
	}

	@Override
	protected void handleDied() {
		switch (getNpcId()) {
			// Miren Fortress.
			case 279251:
			case 279545:
			case 279838:
				treasureChest();
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						spawn(701481, 1786.9448f, 2265.7905f, 2904.0754f, (byte) 115);
						spawn(701481, 1785.1183f, 2261.2131f, 2904.0754f, (byte) 111);
						spawn(701481, 1782.0602f, 2256.8357f, 2904.0754f, (byte) 104);
						spawn(701481, 1787.8588f, 2271.5967f, 2904.0754f, (byte) 119);
						spawn(701481, 1778.1943f, 2254.396f, 2904.0754f, (byte) 101);
						spawn(701481, 1787.6333f, 2276.0862f, 2904.0754f, (byte) 1);
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

	private void announceUnsealedMiren() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				// Unsealed Miren.
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_Ab1_Lamiren_Named_Spawn);
			}
		});
	}
}
