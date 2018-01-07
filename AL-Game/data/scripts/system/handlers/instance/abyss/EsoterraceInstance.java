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
package instance.abyss;

import java.util.Map;
import java.util.Set;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.drop.DropItem;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.StaticDoor;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.NpcShoutsService;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.zone.ZoneInstance;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author xTz, Gigi
 */
@InstanceID(300250000)
public class EsoterraceInstance extends GeneralInstanceHandler {

	private Map<Integer, StaticDoor> doors;

	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
		doors = instance.getDoors();
		doors.get(367).setOpen(true);
		if (Rnd.get(0, 100) < 21) {
			spawn(799580, 1034.11f, 985.01f, 327.35095f, (byte) 105);
			spawn(217649, 1033.67f, 978.08f, 327.35095f, (byte) 35);
		}
	}

	@Override
	public void onDie(Npc npc) {
		switch (npc.getObjectTemplate().getTemplateId()) {
			case 282295:
				openDoor(39);
				break;
			case 282291: // Surkana Feeder enables "hardmode"
				sendMsg(1400996);
				getNpc(217204).getController().onDelete();
				spawn(217205, 1315.43f, 1171.04f, 51.8054f, (byte) 66);
				break;
			case 217289:
				sendMsg(1400924);
				openDoor(122);
				break;
			case 217281:
				sendMsg(1400921);
				openDoor(70);
				break;
			case 217195:
				sendMsg(1400922);
				openDoor(45);
				openDoor(52);
				openDoor(67);
				spawn(701027, 751.513489f, 1136.021851f, 365.031158f, (byte) 60, 41);
				spawn(701027, 829.620789f, 1134.330078f, 365.031281f, (byte) 60, 77);
				break;
			case 217185:
				spawn(701023, 1264.862061f, 644.995178f, 296.831818f, (byte) 60, 112);
				doors.get(367).setOpen(false);
				break;
			case 217204:
				spawn(205437, 1309.390259f, 1163.644287f, 51.493992f, (byte) 13);
				spawn(701027, 1318.669800f, 1180.467651f, 52.879887f, (byte) 75, 727);
				break;
			case 217206:
				spawn(205437, 1309.390259f, 1163.644287f, 51.493992f, (byte) 13);
				spawn(701027, 1318.669800f, 1180.467651f, 52.879887f, (byte) 75, 727);
				spawn(701027, 1325.484497f, 1173.198486f, 52.879887f, (byte) 75, 726);
				break;
			case 217649:
				// keening sirokin treasure chest
				Npc keeningSirokin = getNpc(799580);
				spawn(701025, 1038.63f, 987.74f, 328.356f, (byte) 0, 725);
				NpcShoutsService.getInstance().sendMsg(keeningSirokin, 342359, keeningSirokin.getObjectId(), 0, 0);
				keeningSirokin.getController().onDelete();
			case 217284:
			case 217283:
			case 217282:
				Npc npc1 = getNpc(217284);
				Npc npc2 = getNpc(217283);
				Npc npc3 = getNpc(217282);
				if (isDead(npc1) && isDead(npc2) && isDead(npc3)) {
					sendMsg(1400920);
					openDoor(111);
				}
				break;
		}
	}

	@Override
	public boolean onReviveEvent(Player player) {
		PlayerReviveService.revive(player, 25, 25, false, 0);
		player.getGameStats().updateStatsAndSpeedVisually();
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
		TeleportService2.teleportTo(player, mapId, instanceId, 384.57535f, 535.4073f, 321.6642f, (byte) 17);
		return true;
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

	private boolean isDead(Npc npc) {
		return (npc == null || npc.getLifeStats().isAlreadyDead());
	}

	@Override
	public void onEnterZone(Player player, ZoneInstance zone) {
		if (zone.getAreaTemplate().getZoneName() == ZoneName.get("DRANA_PRODUCTION_LAB_300250000")) {
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400919));
		}
	}

	private void openDoor(int doorId) {
		StaticDoor door = doors.get(doorId);
		if (door != null) {
			door.setOpen(true);
		}
	}

	@Override
	public void onInstanceDestroy() {
		doors.clear();
	}

	@Override
	public void onDropRegistered(Npc npc) {
		Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		int npcId = npc.getNpcId();
		switch (npcId) {
			case 217185: // dalia
				int index = dropItems.size() + 1;
				for (Player player : instance.getPlayersInside()) {
					if (player.isOnline()) {
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 185000111, 1));
					}
				}
				break;
		}
	}
}
