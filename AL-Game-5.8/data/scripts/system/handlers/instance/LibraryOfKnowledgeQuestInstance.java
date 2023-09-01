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
 * @author Falke_34, FrozenKiller
 */

@InstanceID(301570000)
public class LibraryOfKnowledgeQuestInstance extends GeneralInstanceHandler {

	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
	}

	@Override
	public void onEnterInstance(Player player) {
		sendMovie(player, 935);
		instance.getDoors().get(28).setOpen(true);
		instance.getDoors().get(22).setOpen(true);
		instance.getDoors().get(53).setOpen(true);
	}

	private void sendMovie(Player player, int movie) {
		PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, movie));
	}

	@Override
	public void onDie(Npc npc) {
		switch (npc.getNpcId()) {
			case 857782:
				if (!instance.getDoors().get(56).isOpen()) {
					instance.getDoors().get(56).setOpen(true);
				}
				break;
			case 857901:
				if (!instance.getDoors().get(384).isOpen()) {
					instance.getDoors().get(384).setOpen(true);
					instance.getDoors().get(21).setOpen(true);
				}
				else if (instance.getDoors().get(384).isOpen() && !instance.getDoors().get(67).isOpen()) {
					instance.getDoors().get(67).setOpen(true);
					instance.getDoors().get(54).setOpen(true);
					sendMsg(1403298);
				}
				break;
			case 857783:
				if (!instance.getDoors().get(252).isOpen()) {
					instance.getDoors().get(252).setOpen(true);
					instance.getDoors().get(33).setOpen(true);
					sendMsg(1403297);
				}
				break;
			case 857784:
				if (!instance.getDoors().get(64).isOpen()) {
					instance.getDoors().get(64).setOpen(true);
					instance.getDoors().get(27).setOpen(true);
					sendMsg(1403300);
				}
				break;
			case 857902:
				if (!instance.getDoors().get(449).isOpen()) {
					instance.getDoors().get(449).setOpen(true);
					instance.getDoors().get(55).setOpen(true);
					sendMsg(1403299);
				}
				break;
			case 857903:
				if (!instance.getDoors().get(77).isOpen()) {
					instance.getDoors().get(77).setOpen(true);
					instance.getDoors().get(52).setOpen(true);
					sendMsg(1403302);
				}
				break;
			case 857948:
				if (!instance.getDoors().get(311).isOpen()) {
					instance.getDoors().get(311).setOpen(true);
					instance.getDoors().get(57).setOpen(true);
					sendMsg(1403301);
				}
				break;
			case 857914:
				if (!instance.getDoors().get(24).isOpen()) {
					instance.getDoors().get(24).setOpen(true);
					instance.getDoors().get(26).setOpen(true);
					sendMsg(1403302);
				}
				break;
			case 857916:
				if (!instance.getDoors().get(421).isOpen()) {
					instance.getDoors().get(421).setOpen(true);
					instance.getDoors().get(65).setOpen(true);
					sendMsg(1403303);
				}
				break;
			case 857785:
				instance.getDoors().get(90).setOpen(true);
				break;
		}
	}

	@Override
	public void onInstanceDestroy() {
		instance.getDoors().clear();
	}

	@Override
	public void onLeaveInstance(Player player) {
		player.getEffectController().removeEffect(22943);
		player.getEffectController().removeEffect(22944);
		player.getEffectController().removeEffect(22945);
		player.getEffectController().removeEffect(22969);
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
