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

/**
 * @author Undertrey
 * @Reworked Majka Ajural
 */
@InstanceID(300200000)
public class HaramelInstance extends GeneralInstanceHandler {

	@Override
	public void onDie(Npc npc) {
		Player player = npc.getAggroList().getMostPlayerDamage();
		if (player == null) {
			return;
		}
		switch (npc.getNpcId()) {
			case 216922:
				sendMsg(1400713); // Hamerun has dropped a treasure chest.
				PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 457));

				switch (player.getPlayerClass()) { // Reward
					case GLADIATOR:
					case TEMPLAR:
						spawn(700829, 224.137f, 268.608f, 144.898f, (byte) 90); // Chest for Gladiator and Templar
						break;
					case ASSASSIN:
					case RANGER:
					case GUNNER:
						spawn(700830, 224.137f, 268.608f, 144.898f, (byte) 90); // Chest for Scout, Ranger and Gunner
						break;
					case SORCERER:
					case SPIRIT_MASTER:
					case BARD:
						spawn(700831, 224.137f, 268.608f, 144.898f, (byte) 90); // Chest for Sorcerer and Spirit Master
						break;
					case CLERIC:
					case CHANTER:
					case RIDER:
						spawn(700832, 224.137f, 268.608f, 144.898f, (byte) 90); // Chest for Cleric, Chanter and Rider
						break;
					default:
						break;
				}

				spawn(700852, 224.598f, 331.143f, 141.892f, (byte) 90); // Dimensional Portal
				break;
			case 216920: // Brainwashed Dukaki Weakarm
			case 216921: // Brainwashed Dukaki Peon
			case 217067: // Brainwashed MuMu Worker
			case 700950: // Aether Cart
				npc.getController().onDelete();
				break;
		}
	}

	@Override
	public boolean onDie(final Player player, Creature lastAttacker) {
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);
		PacketSendUtility.sendPacket(player, new SM_DIE(player.haveSelfRezEffect(), player.haveSelfRezItem(), 0, 8));
		return true;
	}

	@Override
	public void onPlayerLogOut(Player player) {
		TeleportService2.moveToInstanceExit(player, mapId, player.getRace());
	}
}
