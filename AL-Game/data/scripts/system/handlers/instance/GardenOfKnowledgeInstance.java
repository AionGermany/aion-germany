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

import java.util.Map;
import java.util.concurrent.Future;

import com.aionemu.gameserver.controllers.effect.PlayerEffectController;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.StaticDoor;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;

@InstanceID(301550000)
public class GardenOfKnowledgeInstance extends GeneralInstanceHandler {

	private Race spawnRace;
	@SuppressWarnings("unused")
	private long startTime;
	private Future<?> instanceTimer;
	private Map<Integer, StaticDoor> doors;
	protected boolean isInstanceDestroyed = false;

	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
		doors = instance.getDoors();
	}

	@Override
	public void onEnterInstance(Player player) {
		super.onInstanceCreate(instance);
		if (instanceTimer == null) {
			startTime = System.currentTimeMillis();
			instanceTimer = ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					deleteNpc(806118);
					sendMsgByRace(1403552, Race.PC_ALL, 10000);
				}
			}, 100);
		}

		if (spawnRace == null) {
			spawnRace = player.getRace();
			SpawnGardenRace();
		}
	}

	@Override
	public void onDie(Npc npc) {
		switch (npc.getObjectTemplate().getTemplateId()) {
			case 653784: // Fallen Jotun Warrior
				spawn(834000, 1198.6876f, 774.06604f, 1038.4924f, (byte) 90, 297);
				spawn(834000, 1152.3656f, 726.8832f, 1036.4382f, (byte) 60, 301);
				sendMsgByRace(1403507, Race.PC_ALL, 0);
				break;
			case 653817: // All-Seeing Eye
				sendMsgByRace(1403211, Race.PC_ALL, 0);
				spawn(834003, 1069.1040f, 783.19177f, 511.93161f, (byte) 0, 323);
				deleteNpc(653877); // Imprisoned Earth Jotun
				spawn(653878, 1109.5481f, 783.1569f, 508.24936f, (byte) 0); // Liberated Earth Jotun 
				break;
			case 653825: // Fallen Water Jotun
				spawn(834004, 209.37688f, 1234.7327f, 826.13635f, (byte) 0, 414);
				break;
			case 653884: // Piton
				final int Peregran2_Weda2 = spawnRace == Race.ASMODIANS ? 806290 : 806285;
				spawn(Peregran2_Weda2, 600.31885f, 530.53156f, 509.4301f, (byte) 30);
				spawn(806053, 599.73315f, 522.17584f, 509.43018f, (byte) 30);
				break;
		}
	}

	private void SpawnGardenRace() {
		final int Zangrik_Benio = spawnRace == Race.ASMODIANS ? 806286 : 806281;
		final int Ube_Teria = spawnRace == Race.ASMODIANS ? 806287 : 806282;
		final int Stiget_Radilis = spawnRace == Race.ASMODIANS ? 806288 : 806283;
		final int Peregran1_Weda1 = spawnRace == Race.ASMODIANS ? 806289 : 806284;
		spawn(Zangrik_Benio, 1469.3253f, 770.00507f, 1035.2671f, (byte) 15);
		spawn(Ube_Teria, 1473.5835f, 765.50055f, 1035.2672f, (byte) 23);
		spawn(Stiget_Radilis, 758.8295f, 852.4262f, 578.01526f, (byte) 45);
		spawn(Peregran1_Weda1, 618.96204f, 704.305f, 555.8918f, (byte) 26);
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
	public void onPlayerLogOut(Player player) {
		removeEffects(player);
		TeleportService2.moveToInstanceExit(player, mapId, player.getRace());
	}

	@Override
	public void onLeaveInstance(Player player) {
		removeEffects(player);
	}

	private void removeEffects(Player player) {
		PlayerEffectController effectController = player.getEffectController();
		effectController.removeEffect(21340);
	}

	@Override
	public void onInstanceDestroy() {
		isInstanceDestroyed = true;
		doors.clear();
	}

	private void deleteNpc(int npcId) {
		if (getNpc(npcId) != null) {
			getNpc(npcId).getController().onDelete();
		}
	}

	@Override
	public boolean onDie(final Player player, Creature lastAttacker) {
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);
		PacketSendUtility.sendPacket(player, new SM_DIE(player.haveSelfRezEffect(), player.haveSelfRezItem(), 0, 8));
		return true;
	}
}
