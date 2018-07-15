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
			PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 968)); // TODO Asmodians Video
		}
	}

	@Override
	public void onDie(Npc npc) {
		switch (npc.getNpcId()) {
			case 248440: // 31st Anit-Aircraft Machine 1st Door
				deleteNpc(248437); // Hateful Pelida
				deleteNpc(248477); // Balaur Barricade
				hatefulPelida1();
				break;
			case 248441: // 31st Anit-Aircraft Machine 2nd Door
				deleteNpc(248437); // Hateful Pelida
				deleteNpc(248477); // Balaur Barricade
				hatefulPelida2();
				break;
			case 248442: // 31st Anit-Aircraft Machine 3rd Door
				deleteNpc(248437); // Hateful Pelida
				deleteNpc(248477); // Balaur Barricade
				hatefulPelida3();
				break;
			case 248443: // 31st Anit-Aircraft Machine 4th Door
				//deleteNpc(248437); // Hateful Pelida
				deleteNpc(248477); // Balaur Barricade
				break;
		}
	}

	private void hatefulPelida1() {
		spawn(248437, 291.44397f, 268.20294f, 389.05374f, (byte) 90); // Hateful Pelida
		spawn(248477, 293.0626f, 265.02142f, 388.9091f, (byte) 75, 70); // Balaur Barricade
	}

	private void hatefulPelida2() {
		spawn(248437, 249.28769f, 293.27655f, 397.51718f, (byte) 0); // Hateful Pelida
		spawn(248477, 252.39478f, 294.33484f, 397.56747f, (byte) 75, 69); // Balaur Barricade
	}

	private void hatefulPelida3() {
		spawn(248437, 219.81686f, 254.43588f, 402.46582f, (byte) 30); // Hateful Pelida
		spawn(248477, 219.12627f, 257.25183f, 402.4978f, (byte) 75, 68); // Balaur Barricade
	}

	private void deleteNpc(int npcId) {
		if (getNpc(npcId) != null) {
			getNpc(npcId).getController().onDelete();
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
