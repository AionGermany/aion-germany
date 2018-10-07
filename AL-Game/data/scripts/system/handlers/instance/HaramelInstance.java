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
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.knownlist.Visitor;

/**
 * @author Falke_34
 */
@InstanceID(300200000)
public class HaramelInstance extends GeneralInstanceHandler {

	private Race spawnRace;

	@Override
	public void onEnterInstance(Player player) {
		super.onInstanceCreate(instance);

		if (spawnRace == null) {
			spawnRace = player.getRace();
			SpawnHaramelRace();
		}
	}

	@Override
	public void onDie(Npc npc) {
		Player player = npc.getAggroList().getMostPlayerDamage();
		if (player == null) {
			return;
		}
		switch (npc.getNpcId()) {
			case 653196: // Drudgelord Kakiti
				sendMsgByRace(1500094, Race.PC_ALL, 0);
				break;
			case 653205: // MuMu Ham the Grey
				sendMsgByRace(1500096, Race.PC_ALL, 0);
				break;
			case 653213: // Overseer Nukiti
				sendMsgByRace(1500098, Race.PC_ALL, 0);
				break;
			case 653218: //Hamerun the Bleeder
				sendMsg(1500099);
				sendMsg(1400713); // Hamerun has dropped a treasure chest.
				PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 457));
				
				// TODO - Treasure Chest only tested with Priest
				spawn(700829, 224.1367f, 268.60825f, 144.89798f, (byte) 90); // Antique Treasure Chest
				spawn(749306, 224.36278f, 261.913f, 144.89798f, (byte) 30); // Haramel Exit
				break;
		}
	}

	private void SpawnHaramelRace() {
		final int Cheska_Royer1 = spawnRace == Race.ASMODIANS ? 799995 : 799994;
		final int Cheska_Royer2 = spawnRace == Race.ASMODIANS ? 806883 : 820133;
		spawn(Cheska_Royer1, 221.85893f, 351.66858f, 141.01141f, (byte) 30);
		spawn(Cheska_Royer2, 141.7932f, 22.274172f, 144.2455f, (byte) 0);
	}

	protected void sendMsgByRace(final int msg, final Race race, int time) {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				instance.doOnAllPlayers(new Visitor<Player>() {

					@Override
					public void visit(Player player) {
						if (player.getRace().equals(race) || race.equals(Race.PC_ALL)) {
							PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(msg));
						}
					}
				});
			}
		}, time);
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

	@Override
	public void onExitInstance(Player player) {
		TeleportService2.moveToInstanceExit(player, mapId, player.getRace());
	}
}
