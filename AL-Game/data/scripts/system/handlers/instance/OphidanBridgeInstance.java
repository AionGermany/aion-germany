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

import com.aionemu.commons.utils.Rnd;
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
@InstanceID(300590000)
public class OphidanBridgeInstance extends GeneralInstanceHandler {

    //TODO: Ice Flaeche Buff=8812

	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
		switch (Rnd.get(1, 3)) {
			case 1:
				spawn(235780, 752.8477f, 529.4818f, 576.11774f, (byte) 0); // Fugitive Majikin
				break;
			case 2:
				spawn(235781, 752.8477f, 529.4818f, 576.11774f, (byte) 0); // Fugitive Hirakiki
				break;
			case 3:
				spawn(235782, 752.8477f, 529.4818f, 576.11774f, (byte) 0); // Fugitive Asachin
				break;
		}
		switch (Rnd.get(1, 3)) {
			case 1:
				spawn(235768, 322.42197f, 491.16843f, 607.64343f, (byte) 0); // Fighting Spirit Belkur
				break;
			case 2:
				spawn(235769, 322.42197f, 491.16843f, 607.64343f, (byte) 0); // Merciless Belkur
				break;
			case 3:
				spawn(235770, 322.42197f, 491.16843f, 607.64343f, (byte) 0); // Merciless Belkur
				break;
		}
	}

	@Override
	public void onEnterInstance(Player player) {
		if (player.getRace() == Race.ELYOS) {
			PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 498));
		}
		else {
			PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 499));
		}
	}

	@Override
	public void onDie(Npc npc) {
		switch (npc.getNpcId()) {
			case 235768: // Fighting Spirit Belkur
			case 235769: // Merciless Belkur
				//spawn(702659, 321.6655f, 488.56683f, 607.6435f, (byte) 0); // Shining Chest of the Returnees
				spawn(730868, 315.88037f, 488.6518f, 607.6435f, (byte) 1); // Exit from Ophidan's Bridge
				spawn(702959, 304.62424f, 474.36063f, 607.95703f, (byte) 1, 36); // Beritra Booty Chest
				break;
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
