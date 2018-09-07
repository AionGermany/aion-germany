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

import com.aionemu.commons.utils.Rnd;
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
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;

/**
 * @author Falke_34
 */
@InstanceID(301540000)
public class LibraryOfKnowledgeInstance extends GeneralInstanceHandler {

	private Race spawnRace;
	private Map<Integer, StaticDoor> doors;

	@Override
	public void onEnterInstance(final Player player) {
		super.onInstanceCreate(instance);
		player.getController().updateNearbyQuests();
		// Talk with the Agent
		sendMsgByRace(1403340, Race.PC_ALL, 5000);
		// You must destroy the Aether seals to enter
		sendMsgByRace(1403210, Race.PC_ALL, 30000);
		// The Antiquarian has begun activating the Eternity Relics
		sendMsgByRace(1403212, Race.PC_ALL, 60000);
		// The Antiquarian of Atreia has activated all Eternity Relics
		sendMsgByRace(1403213, Race.PC_ALL, 120000);
		if (spawnRace == null) {
			spawnRace = player.getRace();
			spawnLibraryGuardianRace();
			spawnLibraryNPCRace();
			spawnHistoriesOfAtreia();
			spawnRecordsFromTheEraOfMen();
			spawnEmpyreanHistories();
		}
	}

	private void spawnLibraryGuardianRace() {
		final int libraryGuardian = spawnRace == Race.ASMODIANS ? 806151 : 806150;
		spawn(libraryGuardian, 720.90985f, 521.5394f, 468.99832f, (byte) 110);
		spawn(libraryGuardian, 721.03485f, 502.2894f, 468.99838f, (byte) 10);
		spawn(libraryGuardian, 737.40985f, 493.0394f, 468.99835f, (byte) 30);
		spawn(libraryGuardian, 737.28485f, 531.0394f, 468.99835f, (byte) 90);
	}

	private void spawnLibraryNPCRace() {
		final int libraryGuardian = spawnRace == Race.ASMODIANS ? 806149 : 806148; // Peregrine, Viola
		spawn(libraryGuardian, 737.3194f, 511.91815f, 469.0941f, (byte) 0);
	}

	private void spawnHistoriesOfAtreia() {
		final int historiesOfAtreia = spawnRace == Race.ASMODIANS ? 703149 : 703131;
		spawn(historiesOfAtreia, 625.27313f, 500.36285f, 468.95096f, (byte) 0, 133);
		spawn(historiesOfAtreia, 619.94202f, 422.01804f, 468.95096f, (byte) 0, 137);
		spawn(historiesOfAtreia, 620.38477f, 600.65179f, 468.95096f, (byte) 0, 220);
		spawn(historiesOfAtreia, 569.55731f, 526.27197f, 469.02530f, (byte) 0, 229);
	}

	private void spawnRecordsFromTheEraOfMen() {
		final int recordsFromTheEraOfMen = spawnRace == Race.ASMODIANS ? 703150 : 703132;
		spawn(recordsFromTheEraOfMen, 570.76123f, 337.31241f, 468.95096f, (byte) 0, 343);
		spawn(recordsFromTheEraOfMen, 443.34570f, 341.27530f, 469.01694f, (byte) 0, 355);
		spawn(recordsFromTheEraOfMen, 394.60165f, 443.42435f, 468.95096f, (byte) 0, 360);
		spawn(recordsFromTheEraOfMen, 480.38297f, 678.60730f, 469.01431f, (byte) 0, 394);
		spawn(recordsFromTheEraOfMen, 387.74930f, 500.32230f, 468.95096f, (byte) 0, 396);
		spawn(recordsFromTheEraOfMen, 319.92542f, 568.84387f, 468.95096f, (byte) 0, 404);
	}

	private void spawnEmpyreanHistories() {
		final int empyreanHistories = spawnRace == Race.ASMODIANS ? 703151 : 703133;
		spawn(empyreanHistories, 502.64456f, 454.79669f, 468.95096f, (byte) 0, 268);
		spawn(empyreanHistories, 413.44009f, 568.73181f, 468.95096f, (byte) 0, 371);
		spawn(empyreanHistories, 528.64270f, 599.86584f, 468.95096f, (byte) 0, 372);
		spawn(empyreanHistories, 549.72137f, 648.74438f, 468.95096f, (byte) 0, 373);
		spawn(empyreanHistories, 439.38571f, 504.14023f, 468.95096f, (byte) 0, 399);
	}

	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
		doors = instance.getDoors();
		switch (Rnd.get(1, 3)) {
			case 1:
				spawn(857452, 552.1911f, 511.7292f, 468.97675f, (byte) 0); // Relic Techgolem
				spawn(857456, 460.161f, 672.068f, 468.97745f, (byte) 92); // Augmented Fleshgolem
				spawn(857459, 460.66083f, 351.61194f, 468.9799f, (byte) 21); // Crystalized Shardgolem
				break;
			case 2:
				spawn(857456, 552.1911f, 511.7292f, 468.97675f, (byte) 0); // Augmented Fleshgolem
				spawn(857459, 460.161f, 672.068f, 468.97745f, (byte) 92); // Crystalized Shardgolem
				spawn(857452, 460.66083f, 351.61194f, 468.9799f, (byte) 21); // Relic Techgolem
				break;
			case 3:
				spawn(857459, 552.1911f, 511.7292f, 468.97675f, (byte) 0); // Crystalized Shardgolem
				spawn(857452, 460.161f, 672.068f, 468.97745f, (byte) 92); // Relic Techgolem
				spawn(857456, 460.66083f, 351.61194f, 468.9799f, (byte) 21); // Augmented Fleshgolem
				break;
		}
		switch (Rnd.get(1, 3)) {
			case 1:
				spawn(857460, 255.67651f, 512.3747f, 468.84964f, (byte) 0); // Ancient Relic Techgolem
				break;
			case 2:
				spawn(857462, 255.67651f, 512.3747f, 468.84964f, (byte) 0); // Fleshgolem Captain
				break;
			case 3:
				spawn(857464, 255.67651f, 512.3747f, 468.84964f, (byte) 0); // Mountainous Shardgolem
				break;
		}
	}

	@Override
	public void onDie(Npc npc) {
		switch (npc.getObjectTemplate().getTemplateId()) {
			case 701432: // IDEternity_01_Secret_Door_01
				despawnNpc(npc);
				break;
			case 703009: // Shedim Eternity Relic
				despawnNpc(npc);
				// Shedim Seal has been destroyed
				sendMsgByRace(1403269, Race.PC_ALL, 0);
				break;
			case 703010: // Seraphim Eternity Relic
				despawnNpc(npc);
				// Seraphim Seal has been destroyed
				sendMsgByRace(1403270, Race.PC_ALL, 0);
				deleteNpc(703017);
				break;
			case 703011: // Shedim Eternity Relic
				despawnNpc(npc);
				// Shedim Seal has been destroyed
				sendMsgByRace(1403269, Race.PC_ALL, 0);
				break;
			case 703012: // Seraphim Eternity Relic
				despawnNpc(npc);
				// Seraphim Seal has been destroyed
				sendMsgByRace(1403270, Race.PC_ALL, 0);
				deleteNpc(703018);
				break;
			case 703013: // Shedim Eternity Relic
				despawnNpc(npc);
				// Shedim Seal has been destroyed
				sendMsgByRace(1403269, Race.PC_ALL, 0);
				break;
			case 703014: // Seraphim Eternity Relic
				despawnNpc(npc);
				// Seraphim Seal has been destroyed
				sendMsgByRace(1403270, Race.PC_ALL, 0);
				deleteNpc(703019);
				break;
			case 703015: // Shedim Eternity Relic
				despawnNpc(npc);
				// Shedim Seal has been destroyed
				sendMsgByRace(1403269, Race.PC_ALL, 0);
				break;
			case 703016: // Seraphim Eternity Relic
				despawnNpc(npc);
				// Seraphim Seal has been destroyed
				sendMsgByRace(1403270, Race.PC_ALL, 0);
				deleteNpc(703020);
				break;
			case 857460: // Ancient Relic Techgolem
			case 857462: // Fleshgolem Captain
			case 857464: // Mountainous Shardgolem
				doors.get(33).setOpen(true);
				// The Antiquarian of Atreia is defeated and the Eternity Relics ceased functioning
				sendMsgByRace(1403214, Race.PC_ALL, 0);
				final int IDEternity01OutPortal = spawnRace == Race.ASMODIANS ? 806192 : 806191;
				spawn(IDEternity01OutPortal, 222.88667f, 511.78955f, 468.80215f, (byte) 0);
				final int IDEternity01To02Portal = spawnRace == Race.ASMODIANS ? 806057 : 806055;
				spawn(IDEternity01To02Portal, 256.28693f, 512.5591f, 468.84964f, (byte) 118);
				spawn(806153, 245.83438f, 512.4957f, 468.80215f, (byte) 119); // Cryptograph Cube
				break;
		}
	}

	@Override
	public boolean onReviveEvent(Player player) {
		player.getGameStats().updateStatsAndSpeedVisually();
		PlayerReviveService.revive(player, 100, 100, false, 0);
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
		PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_INSTANT_DUNGEON_RESURRECT, 0, 0));
		return TeleportService2.teleportTo(player, mapId, instanceId, 737f, 511f, 469f, (byte) 0);
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

	private void deleteNpc(int npcId) {
		if (getNpc(npcId) != null) {
			getNpc(npcId).getController().onDelete();
		}
	}

	private void despawnNpc(Npc npc) {
		if (npc != null) {
			npc.getController().onDelete();
		}
	}

	@Override
	public void onInstanceDestroy() {
		doors.clear();
	}

	@Override
	public boolean onDie(final Player player, Creature lastAttacker) {
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);
		PacketSendUtility.sendPacket(player, new SM_DIE(player.haveSelfRezEffect(), player.haveSelfRezItem(), 0, 8));
		return true;
	}
}
