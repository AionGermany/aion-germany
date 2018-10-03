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
package ai;

import java.util.concurrent.Future;

import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_FLAG_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_FLAG_UPDATE;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

@AIName("flag")
public class FlagAI2 extends NoActionAI2 {

	private Future<?> sendPacketTask;

	@Override
	public void handleSpawned() {
		super.handleSpawned();
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(final Player player) {
				sendPacketTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

					@Override
					public void run() {
						if (player.getWorldId() == getOwner().getWorldId()) {
							if (getOwner().isSpawned()) {
								PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, getOwner()));
							}
						}
					}
				}, 1000, 2000);
			}
		});
	}

	@Override
	protected void handleDespawned() {
		super.handleDespawned();
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(final Player player) {
				sendPacketTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {
					@Override
					public void run() {
						if (player.getWorldId() == getOwner().getWorldId()) {
							PacketSendUtility.sendPacket(player, new SM_FLAG_UPDATE(getOwner()));
							AI2Actions.deleteOwner(FlagAI2.this);
						}
					}
				}, 1000, 2000);
			}
		});
	}

	private void cancelTask() {
		if (sendPacketTask != null && !sendPacketTask.isCancelled()) {
			sendPacketTask.cancel(true);
		}
	}
}
