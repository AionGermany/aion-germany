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
import java.util.Set;
import java.util.concurrent.Future;

import com.aionemu.gameserver.controllers.effect.PlayerEffectController;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.drop.DropItem;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.StaticDoor;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;
import com.aionemu.gameserver.world.zone.ZoneInstance;
import com.aionemu.gameserver.world.zone.ZoneName;

import javolution.util.FastMap;

@InstanceID(301550000)
public class GardenOfKnowledgeInstance extends GeneralInstanceHandler {

	private Race spawnRace;
	@SuppressWarnings("unused")
	private long startTime;
	private int covetousFallen;
	private Future<?> instanceTimer;
	private Map<Integer, StaticDoor> doors;
	protected boolean isInstanceDestroyed = false;
	private FastMap<Integer, VisibleObject> objects = new FastMap<Integer, VisibleObject>();

	@Override
	public void onDropRegistered(Npc npc) {
		Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		int npcId = npc.getNpcId();
		int index = dropItems.size() + 1;
		switch (npcId) {
			case 220526: // Insightful Eye
			case 220534: // Fallen Sea Jotun
			case 220540: // Typhon
				for (Player player : instance.getPlayersInside()) {
					if (player.isOnline()) {
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053619, 1)); // Ancient Manastone Bundle
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188055414, 1)); // Cradle Of Eternity Illusion Godstone Bundle
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188055415, 1)); // Cradle Of Eternity Enchant Supplement Bundle
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188055416, 1)); // Cradle Of Eternity Manastone Bundle
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188100371, 1)); // Silver Starlight Particle
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188100372, 1)); // Gold Starlight Particle
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188100373, 1)); // Ruby Starlight Particle
					}
				}
				break;
			case 220470: // Covetous Fallen Guardian
			case 220471: // Covetous Fallen Guardian
			case 220472: // Covetous Fallen Guardian
			case 220594: // Covetous Fallen Guardian
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000266, 1)); // Earthen Malachite
				break;
			case 834091: // Box With Sun Seal
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000267, 1)); // Sun Quartz
				break;
		}
	}

	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
		doors = instance.getDoors();
		SpawnTemplate IDEternity02Shield1 = SpawnEngine.addNewSingleTimeSpawn(301550000, 834123, 1462.8610f, 774.33978f, 1035.3840f, (byte) 0);
		IDEternity02Shield1.setStaticId(725);
		objects.put(834123, SpawnEngine.spawnObject(IDEternity02Shield1, instanceId));
		SpawnTemplate IDEternity02Shield2 = SpawnEngine.addNewSingleTimeSpawn(301550000, 703026, 307.59805f, 1471.2153f, 919.05554f, (byte) 0);
		IDEternity02Shield2.setStaticId(272);
		objects.put(703026, SpawnEngine.spawnObject(IDEternity02Shield2, instanceId));
	}

	@Override
	public void onEnterInstance(Player player) {
		super.onInstanceCreate(instance);
		// As the shields fell, the 1st Defense Line was breached and overrun
		sendMsgByRace(1403501, Race.PC_ALL, 70000);
		// As the shields fell, the 2nd Defense Line was breached and overrun
		sendMsgByRace(1403502, Race.PC_ALL, 90000);
		// As the shields fell, the 3rd Defense Line was breached and overrun
		sendMsgByRace(1403503, Race.PC_ALL, 110000);
		// As the shields fell, the 4th Defense Line was breached and overrun
		sendMsgByRace(1403504, Race.PC_ALL, 130000);
		if (instanceTimer == null) {
			startTime = System.currentTimeMillis();
			instanceTimer = ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					deleteNpc(834123);
					// The enemies are coming. Kill them all
					sendMsgByRace(1403547, Race.PC_ALL, 0);
				}
			}, 60000);
		}
		if (spawnRace == null) {
			spawnRace = player.getRace();
			SpawnIDEternity02Race();
		}
	}

	@Override
	public void onDie(Npc npc) {
		switch (npc.getObjectTemplate().getTemplateId()) {
			case 220470: // Covetous Fallen Guardian
			case 220471: // Covetous Fallen Guardian
			case 220472: // Covetous Fallen Guardian
			case 220594: // Covetous Fallen Guardian
				covetousFallen++;
				if (covetousFallen == 1) {
				}
				else if (covetousFallen == 2) {
				}
				else if (covetousFallen == 3) {
				}
				else if (covetousFallen == 4) {
				}
				else if (covetousFallen == 5) {
					// You�ve killed the Cruel Protector
					// There are still Advance unit Magical Soldiers who need help
					sendMsgByRace(1403542, Race.PC_ALL, 0);
					// You�ve killed all Cruel Protectors
					sendMsgByRace(1403545, Race.PC_ALL, 8000);
					spawn(281446, 1477.0033f, 774.52344f, 1036.7559f, (byte) 0);
					spawn(806036, 1477.0033f, 774.52344f, 1036.7559f, (byte) 0); // Geodesic "Walking Path"
					spawn(834014, 976.31232f, 774.7804f, 1043.3522f, (byte) 0); // The Walking Path's
					spawn(834015, 713.17285f, 1191.156f, 1036.5265f, (byte) 0); // The Garden Temple's
				}
				break;
			case 220480: // Fallen Jotun Warrior
				// The door to the Sanctuary of Domination was activated
				sendMsgByRace(1403507, Race.PC_ALL, 0);
				spawn(834000, 1196.7842f, 774.28778f, 1037.6906f, (byte) 0, 297);
				spawn(834000, 1152.3800f, 728.48621f, 1036.1700f, (byte) 60, 301);
				break;
			case 220526: // Insightful Eye
				// The detection of the Insightful Eye has disappeared
				sendMsgByRace(1403544, Race.PC_ALL, 0);
				spawn(834003, 1069.1040f, 783.19177f, 511.93161f, (byte) 0, 323);
				spawn(834088, 1162.5231f, 790.3289f, 505.0359f, (byte) 60);
				break;
			case 220534: // Fallen Sea Jotun.
				spawn(834004, 209.23192f, 1234.7489f, 825.43243f, (byte) 0, 414);
				spawn(834089, 234.61803f, 1227.7375f, 825.46594f, (byte) 56);
				spawn(834045, 563.11910f, 1428.8512f, 826.44550f, (byte) 0); // The Library Gap's
				spawn(834016, 761.55054f, 859.54266f, 578.24963f, (byte) 63); // The All-Knowing Tree Center's
				break;
			case 220539: // Fiery Typhon
				// The Fiery Glycon is dead, quickly kill the Vile Glycon!
				sendMsgByRace(1403530, Race.PC_ALL, 0);
				break;
			case 220540: // Typhon
				final int Peregrine_Viola = spawnRace == Race.ASMODIANS ? 806290 : 806285;
				spawn(Peregrine_Viola, 595.3497f, 540.4742f, 509.43015f, (byte) 22);
				spawn(834005, 599.93695f, 538.61847f, 509.43015f, (byte) 22);
				spawn(834090, 604.9655f, 536.7014f, 509.43015f, (byte) 24);
				break;
			case 220541: // Vile Typhon.
				// The Vile Glycon is dead, quickly kill the Fiery Glycon!
				sendMsgByRace(1403529, Race.PC_ALL, 0);
				break;
			case 220597: // Locked Door To The Vile Library
				doors.get(509).setOpen(true);
				// You can use a Wind Road to get to the center of the All-knowing Tree
				sendMsgByRace(1403520, Race.PC_ALL, 5000);
				// Glyon has appeared
				sendMsgByRace(1403533, Race.PC_ALL, 10000);
				break;
		}
	}

	@Override
	public void handleUseItemFinish(Player player, Npc npc) {
		switch (npc.getNpcId()) {
			case 703021: // Heavy Door Lever
			case 703022: // Heavy Door Lever
			case 703023: // Heavy Door Lever
			case 703024: // Heavy Door Lever
			case 703025: // Heavy Door Lever
				despawnNpc(npc);
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						deleteNpc(703026);
						// You�ve removed the pollutants from the library
						sendMsgByRace(1403526, Race.PC_ALL, 0);
						// The library�s pollutants have disappeared
						sendMsgByRace(1403527, Race.PC_ALL, 10000);
					}
				}, 10000);
				break;
			case 834007: // Altar Of Sun
				if (player.getInventory().decreaseByItemId(185000267, 1)) { // Sun Quartz.
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							deleteNpc(834007);
							deleteNpc(834018);
							// The Mysterious Waterfall has stopped flowing
							// You�ve discovered a hidden entrance
							sendMsgByRace(1403522, Race.PC_ALL, 0);
							// The Rose Quarz of Sun emits a light and starts to float
							sendMsgByRace(1403590, Race.PC_ALL, 5000);
							spawn(834007, 745.79639f, 728.73376f, 547.07489f, (byte) 0, 42);
							spawn(834091, 794.84790f, 737.12927f, 542.71869f, (byte) 0, 633);
						}
					}, 5000);
				}
				else {
					// You don�t have a Rose Quarz of Sun to place on the altar
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1403448));
				}
				break;
			case 834008: // Bridge Activation Device
				despawnNpc(npc);
				doors.get(112).setOpen(true);
				doors.get(114).setOpen(true);
				doors.get(115).setOpen(true);
				break;
		}
	}

	@Override
	public void onEnterZone(Player player, ZoneInstance zone) {
		if (zone.getAreaTemplate().getZoneName() == ZoneName.get("TEMPLE_OF_THE_VOID_301550000")) {
			// You�ve discovered the Altar of Emptiness
			// At the center of the altar is an indentation into which a book would fit
			sendMsgByRace(1403505, Race.PC_ALL, 0);
		}
		else if (zone.getAreaTemplate().getZoneName() == ZoneName.get("WALKING_PATH_301550000")) {
			// You�ve discovered the Altar of Earth
			// Looks like you could place something on it
			sendMsgByRace(1403509, Race.PC_ALL, 0);
		}
		else if (zone.getAreaTemplate().getZoneName() == ZoneName.get("SEALED_LIGHT_301550000")) {
			// The Garden Fairies and Watchers have become aggressive
			sendMsgByRace(1403515, Race.PC_ALL, 0);
		}
		else if (zone.getAreaTemplate().getZoneName() == ZoneName.get("ALRAS_GARDEN_301550000")) {
			// The Garden Fairies and Watchers have become aggressive
			sendMsgByRace(1403515, Race.PC_ALL, 0);
		}
		else if (zone.getAreaTemplate().getZoneName() == ZoneName.get("CONTAMINATED_1ST_HALL_301550000")) {
			removeEffects(player);
			// You can glide in this area
			sendMsgByRace(1403517, Race.PC_ALL, 5000);
			// You can fly in this area
			sendMsgByRace(1403518, Race.PC_ALL, 10000);
		}
		else if (zone.getAreaTemplate().getZoneName() == ZoneName.get("CONNECTING_HALL_IN_THE_LIBRARY_301550000")) {
			removeEffects(player);
			// You can glide in this area
			sendMsgByRace(1403517, Race.PC_ALL, 5000);
			// You can fly in this area
			sendMsgByRace(1403518, Race.PC_ALL, 10000);
		}
		else if (zone.getAreaTemplate().getZoneName() == ZoneName.get("CONTAMINATED_2ND_HALL_301550000")) {
			// You�ve discovered the entrance to Vid�s Secret Library
			sendMsgByRace(1403519, Race.PC_ALL, 0);
		}
		else if (zone.getAreaTemplate().getZoneName() == ZoneName.get("CONTAMINATED_3RD_HALL_301550000")) {
			// If you move the switch, something should activate
			sendMsgByRace(1403525, Race.PC_ALL, 0);
		}
		else if (zone.getAreaTemplate().getZoneName() == ZoneName.get("MYSTERIOUS_WATERFALL_301550000")) {
			// You�ve discovered the Altar of Sun
			// Looks like you could place something on it
			sendMsgByRace(1403521, Race.PC_ALL, 0);
			// You�ve discovered a strange Kisk
			sendMsgByRace(1403523, Race.PC_ALL, 10000);
		}
	}

	private void SpawnIDEternity02Race() {
		// Npc
		final int Jangrike_Venio = spawnRace == Race.ASMODIANS ? 806286 : 806281;
		final int Ubepe_Teria = spawnRace == Race.ASMODIANS ? 806287 : 806282;
		final int Stigeter_Radilis = spawnRace == Race.ASMODIANS ? 806288 : 806283;
		final int Feregran1_Weatha1 = spawnRace == Race.ASMODIANS ? 806289 : 806284;
		final int Bregat_Burgos = spawnRace == Race.ASMODIANS ? 806427 : 806426;
		final int SavePointStart = spawnRace == Race.ASMODIANS ? 834041 : 834042;
		// Guard
		final int IDEternity02DGuardKn75Ae = spawnRace == Race.ASMODIANS ? 220510 : 220507;
		final int IDEternity02DGuardAs75Ae = spawnRace == Race.ASMODIANS ? 220511 : 220508;
		final int IDEternity02DGuardLeaderAs = spawnRace == Race.ASMODIANS ? 220512 : 220509;
		final int IDEternity02EventGuardFi01 = spawnRace == Race.ASMODIANS ? 220613 : 220607;
		final int IDEternity02EventGuardFi02 = spawnRace == Race.ASMODIANS ? 220614 : 220608;
		final int IDEternity02EventGuardFi04 = spawnRace == Race.ASMODIANS ? 220616 : 220610;
		final int IDEternity02EventGuardFi05 = spawnRace == Race.ASMODIANS ? 220617 : 220611;
		final int IDEternity02DGuardEventRun = spawnRace == Race.ASMODIANS ? 220654 : 220655;
		final int IDEternity02DieGuardFi01 = spawnRace == Race.ASMODIANS ? 220658 : 220661;
		final int IDEternity02DieGuardFi02 = spawnRace == Race.ASMODIANS ? 220659 : 220662;
		final int IDEternity02DieGuardFi03 = spawnRace == Race.ASMODIANS ? 220660 : 220663;
		final int IDEternity02AGuardOffRun = spawnRace == Race.ASMODIANS ? 220666 : 220667;
		// Guard Shield
		final int IDEternity02GuardWi01 = spawnRace == Race.ASMODIANS ? 220574 : 220575;
		final int IDEternity02GuardWi02 = spawnRace == Race.ASMODIANS ? 220576 : 220577;
		final int IDEternity02GuardWi03 = spawnRace == Race.ASMODIANS ? 220578 : 220579;
		final int IDEternity02GuardWi03_1 = spawnRace == Race.ASMODIANS ? 220578 : 220579;
		final int IDEternity02GuardWi04 = spawnRace == Race.ASMODIANS ? 220580 : 220581;
		// Npc
		spawn(Jangrike_Venio, 1468.9469f, 769.9438f, 1035.2671f, (byte) 14);
		spawn(Ubepe_Teria, 1473.3698f, 766.1101f, 1035.2671f, (byte) 23);
		spawn(Stigeter_Radilis, 758.68756f, 852.1812f, 578.01447f, (byte) 24);
		spawn(Feregran1_Weatha1, 617.21466f, 702.872f, 555.8918f, (byte) 118);
		spawn(Bregat_Burgos, 1387.8287f, 715.3606f, 1022.6617f, (byte) 30);
		spawn(SavePointStart, 750.68359f, 710.28125f, 546.67133f, (byte) 0, 629);
		// Guard Shield
		spawn(IDEternity02GuardWi01, 1389.5039f, 713.70197f, 1022.6617f, (byte) 90);
		spawn(IDEternity02GuardWi02, 1242.333f, 872.4472f, 1028.6085f, (byte) 30);
		spawn(IDEternity02GuardWi03, 1179.9739f, 905.1821f, 1033.4952f, (byte) 60);
		spawn(IDEternity02GuardWi03_1, 1127.9623f, 890.67255f, 1034.0122f, (byte) 10);
		spawn(IDEternity02GuardWi04, 1187.867f, 661.21954f, 1038.2015f, (byte) 59);
		// Guard
		spawn(IDEternity02DGuardKn75Ae, 740.6303f, 706.6944f, 546.68524f, (byte) 25);
		spawn(IDEternity02DGuardAs75Ae, 739.0f, 708.0f, 546.68524f, (byte) 20);
		spawn(IDEternity02DGuardAs75Ae, 736.0f, 725.0f, 546.8194f, (byte) 25);
		spawn(IDEternity02DGuardAs75Ae, 740.9052f, 727.32166f, 546.81946f, (byte) 65);
		spawn(IDEternity02DGuardLeaderAs, 732.9028f, 717.37646f, 546.6853f, (byte) 34);
		spawn(IDEternity02EventGuardFi01, 669.0931f, 894.0213f, 561.7104f, (byte) 4);
		spawn(IDEternity02EventGuardFi01, 722.0f, 852.0f, 570.8592f, (byte) 4);
		spawn(IDEternity02EventGuardFi02, 679.3196f, 904.9947f, 562.1936f, (byte) 119);
		spawn(IDEternity02EventGuardFi02, 678.6026f, 903.56537f, 562.1183f, (byte) 109);
		spawn(IDEternity02EventGuardFi02, 698.0f, 877.0f, 565.59314f, (byte) 117);
		spawn(IDEternity02EventGuardFi02, 697.66364f, 875.56177f, 565.8108f, (byte) 0);
		spawn(IDEternity02EventGuardFi02, 669.0165f, 892.3247f, 561.791f, (byte) 0);
		spawn(IDEternity02EventGuardFi04, 662.1175f, 892.0533f, 562.484f, (byte) 8);
		spawn(IDEternity02EventGuardFi04, 713.98157f, 843.9322f, 569.4606f, (byte) 15);
		spawn(IDEternity02EventGuardFi05, 693.17615f, 884.75433f, 564.29205f, (byte) 105);
		spawn(IDEternity02DGuardEventRun, 754.0f, 710.0f, 546.69696f, (byte) 43);
		spawn(IDEternity02DieGuardFi01, 746.0f, 705.0f, 546.68524f, (byte) 102);
		spawn(IDEternity02DieGuardFi01, 703.0f, 869.0f, 565.7065f, (byte) 94);
		spawn(IDEternity02DieGuardFi01, 1198.2053f, 654.4297f, 1038.2018f, (byte) 10);
		spawn(IDEternity02DieGuardFi01, 732.3859f, 852.12067f, 573.5313f, (byte) 5);
		spawn(IDEternity02DieGuardFi01, 686.15967f, 897.8577f, 562.20325f, (byte) 106);
		spawn(IDEternity02DieGuardFi01, 678.30023f, 891.9868f, 562.2933f, (byte) 119);
		spawn(IDEternity02DieGuardFi01, 711.0f, 846.0f, 568.56586f, (byte) 119);
		spawn(IDEternity02DieGuardFi01, 688.0f, 782.0f, 553.5308f, (byte) 111);
		spawn(IDEternity02DieGuardFi01, 698.1583f, 883.79517f, 564.90424f, (byte) 103);
		spawn(IDEternity02DieGuardFi01, 754.0f, 707.0f, 546.69696f, (byte) 103);
		spawn(IDEternity02DieGuardFi01, 674.37524f, 900.89624f, 562.0333f, (byte) 114);
		spawn(IDEternity02DieGuardFi01, 755.0f, 713.0f, 546.69696f, (byte) 114);
		spawn(IDEternity02DieGuardFi01, 1202.2423f, 900.94867f, 1028.8885f, (byte) 1);
		spawn(IDEternity02DieGuardFi01, 708.3556f, 849.0528f, 567.65295f, (byte) 2);
		spawn(IDEternity02DieGuardFi01, 697.0f, 771.0f, 550.51337f, (byte) 112);
		spawn(IDEternity02DieGuardFi02, 1195.3984f, 653.76013f, 1038.2015f, (byte) 29);
		spawn(IDEternity02DieGuardFi03, 1238.9576f, 861.2851f, 1028.6086f, (byte) 91);
		spawn(IDEternity02DieGuardFi03, 760.93414f, 868.1584f, 578.11847f, (byte) 91);
		spawn(IDEternity02DieGuardFi03, 1385.5106f, 715.0047f, 1022.6617f, (byte) 119);
		spawn(IDEternity02DieGuardFi03, 1393.5161f, 717.77704f, 1022.6627f, (byte) 60);
		spawn(IDEternity02DieGuardFi03, 1246.124f, 861.8041f, 1028.6086f, (byte) 90);
		spawn(IDEternity02DieGuardFi03, 1470.7714f, 784.3111f, 1035.2671f, (byte) 102);
		spawn(IDEternity02DieGuardFi03, 1246.1979f, 859.48865f, 1028.6158f, (byte) 60);
		spawn(IDEternity02DieGuardFi03, 756.93384f, 866.1005f, 578.0571f, (byte) 101);
		spawn(IDEternity02DieGuardFi03, 1201.9664f, 909.4555f, 1028.8885f, (byte) 90);
		spawn(IDEternity02DieGuardFi03, 1195.3267f, 668.4876f, 1038.2015f, (byte) 91);
		spawn(IDEternity02AGuardOffRun, 1191.131f, 661.11096f, 1038.2015f, (byte) 61);
		spawn(IDEternity02AGuardOffRun, 1389.7367f, 715.46387f, 1022.6617f, (byte) 31);
		spawn(IDEternity02AGuardOffRun, 1196.7313f, 905.05646f, 1028.8958f, (byte) 0);
		spawn(IDEternity02AGuardOffRun, 1121.2787f, 884.2282f, 1034.1548f, (byte) 13);
		spawn(IDEternity02AGuardOffRun, 1242.3444f, 860.0123f, 1028.6128f, (byte) 91);
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
		effectController.removeEffect(21340); // Sylfae Queens Blessing
		// The Sylfae Queen�s power has disappeared
		sendMsgByRace(1403607, Race.PC_ALL, 0);
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

	private void despawnNpc(Npc npc) {
		if (npc != null) {
			npc.getController().onDelete();
		}
	}

	@Override
	public boolean onDie(final Player player, Creature lastAttacker) {
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);
		PacketSendUtility.sendPacket(player, new SM_DIE(player.haveSelfRezEffect(), player.haveSelfRezItem(), 0, 8));
		return true;
	}
}
