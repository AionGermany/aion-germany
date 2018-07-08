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
package instance;

import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;

/**
 * @author Falke_34
 */
@InstanceID(310160000)
public class HolyTowerInstance extends GeneralInstanceHandler {

	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
	}

	@Override
	public void onEnterInstance(Player player) {
		if (player.getRace() == Race.ELYOS) {
			PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 967));
		}
		else {
			PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 967)); // TODO Asmodians Video
		}
	}

	@Override
	public void onDie(Npc npc) {
		switch (npc.getNpcId()) {
			//case 23576:
				//spawn(701644, 438.49078f, 496.7822f, 604.88715f, (byte) 1); // Corridor Torch
				//break;
			//case 235768:
				//spawn(702659, 321.6655f, 488.56683f, 607.6435f, (byte) 0); // Shining Chest of the Returnees
				//spawn(730868, 315.88037f, 488.6518f, 607.6435f, (byte) 0); // Exit from Ophidan's Bridge
				//break;
		}
	}

	@Override
	public void onInstanceDestroy() {
	}

	@Override
	public void onExitInstance(Player player) {
		TeleportService2.moveToInstanceExit(player, mapId, player.getRace());
	}

	@Override
	public boolean onDie(final Player player, Creature lastAttacker) {
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);
		PacketSendUtility.sendPacket(player, new SM_DIE(player.haveSelfRezEffect(), player.haveSelfRezItem(), 0, 8));
		return true;
	}
}
