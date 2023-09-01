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
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.StaticDoor;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUEST_ACTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;

/**
 * @author Gigi
 * @author nrg
 * @author oslo0322
 * @author xTz
 * @author Antraxx TODO: normal mode + BossAI for normal mode
 */
@InstanceID(300170000)
public class BeshmundirInstance extends GeneralInstanceHandler {

	private int macunbello = 0;
	private int macunbello_souls = 0;
	private int kills;
	Npc npcMacunbello = null;
	private Map<Integer, StaticDoor> doors;

	private void macunbelloSouls() {
		macunbello += macunbello_souls;
		macunbello_souls = 0;
		if (macunbello >= 21) {
			sendSystemMessage(SM_SYSTEM_MESSAGE.STR_MSG_IDCatacombs_NmdLich_weakness3);
		}
		else if (macunbello >= 14) {
			sendSystemMessage(SM_SYSTEM_MESSAGE.STR_MSG_IDCatacombs_NmdLich_weakness2);
		}
		else if (macunbello >= 12) {
			sendSystemMessage(SM_SYSTEM_MESSAGE.STR_MSG_IDCatacombs_NmdLich_weakness1);
		}
	}

	@Override
	public void onDie(Npc npc) {
		switch (npc.getNpcId()) {
			case 216175: // Pahraza
				if (Rnd.get(100) > 10) {
					spawn(216764, 1437.2672f, 1579.4656f, 305.82492f, (byte) 97);
					sendMsg("Mystery Box spawned");
				}
				break;
			case 216179:
			case 216181:
			case 216177:
				int chance = Rnd.get(100);

				if (chance > 90) {
					switch (npc.getNpcId()) {
						case 216179:
							spawn(216764, 1625.5829f, 1493.408f, 329.94492f, (byte) 67);
							break;
						case 216181:
							spawn(216764, 1633.7206f, 1429.6768f, 305.83493f, (byte) 59);
							break;
						case 216177:
							spawn(216764, 1500.8236f, 1586.5652f, 329.94492f, (byte) 88);
							break;
					}
					sendMsg("Congratulation: Mystery Box spawned!\nChance: " + chance);
				}
				else {
					sendMsg("Chance: " + chance);
					switch (npc.getObjectTemplate().getTemplateId()) {
						case 216179: // Narma
							spawn(216173, 1546.5916f, 1471.214f, 300.33008f, (byte) 84);
							sendMsg("Gatekeeper Rhapsharr spawned");
							break;
						case 216181: // Kramaka
							spawn(216171, 1403.51f, 1475.79f, 307.793f, (byte) 98);
							sendMsg("Gatekeeper Kutarrun spawned");
							break;
						case 216177: // Dinata
							spawn(216170, 1499.78f, 1507.1f, 300.33f, (byte) 0);
							sendMsg("Gatekeeper Darfall spawned");
					}
				}
				break;
			case 216583:
				macunbelloSouls();
				spawn(799518, 936.0029f, 441.51712f, 220.5029f, (byte) 28);
				break;
			case 216584:
				macunbelloSouls();
				spawn(799519, 791.0439f, 439.79608f, 220.3506f, (byte) 28);
				break;
			case 216585:
				macunbelloSouls();
				spawn(799520, 820.70624f, 278.828f, 220.19385f, (byte) 55);
				break;
			case 216586: // Temadaro - Seelenwächter
				if (macunbello < 12) {
					npcMacunbello = (Npc) spawn(216735, 981.015015f, 134.373001f, 241.755005f, (byte) 30); // strongest macunbello
					SkillEngine.getInstance().applyEffectDirectly(19046, npcMacunbello, npcMacunbello, 0);
				}
				else if (macunbello < 14) {
					npcMacunbello = (Npc) spawn(216734, 981.015015f, 134.373001f, 241.755005f, (byte) 30); // 2th strongest macunbello
					SkillEngine.getInstance().applyEffectDirectly(19047, npcMacunbello, npcMacunbello, 0);
				}
				else if (macunbello < 21) {
					npcMacunbello = (Npc) spawn(216737, 981.015015f, 134.373001f, 241.755005f, (byte) 30); // 2th weakest macunbello
					SkillEngine.getInstance().applyEffectDirectly(19048, npcMacunbello, npcMacunbello, 0);
				}
				else {
					npcMacunbello = (Npc) spawn(216738, 981.015015f, 134.373001f, 241.755005f, (byte) 30); // weakest macunbello
				}
				macunbello = 0;
				sendPacket(new SM_QUEST_ACTION(0, 0));
				openDoor(467);
				break;
			case 799342:
				sendPacket(new SM_PLAY_MOVIE(0, 447));
				break;
			case 216238:
				// Captain Lakhara
				openDoor(470);
				spawn(216159, 1357.0598f, 388.6637f, 249.26372f, (byte) 90);
				break;
			case 216246:
				openDoor(473);
				break;
			case 216739:
			case 216740:
				kills++;
				if (kills < 10) {
					sendSystemMessage(SM_SYSTEM_MESSAGE.STR_MSG_IDCatacombs_NmdSpecter_Spawn);
				}
				else if (kills == 10) {
					sendSystemMessage(SM_SYSTEM_MESSAGE.STR_MSG_IDCatacombs_NmdSpecter_Start);
					// ahbana
					spawn(216158, 1356.5719f, 147.76418f, 246.27373f, (byte) 91);
				}
				break;
			case 216158:
				openDoor(471);
				break;
			case 216263:
				sendPacket(new SM_PLAY_MOVIE(0, 443));
				// this is a safety Mechanism
				// super boss
				spawn(216183, 558.306f, 1369.02f, 224.795f, (byte) 70);
				// gate
				sendSystemMessage(SM_SYSTEM_MESSAGE.STR_MSG_IDCatacombs_BigOrb_Spawn);
				spawn(730275, 1611.1266f, 1604.6935f, 311.00503f, (byte) 17);
				break;
			case 216169: // Dorakiki the Bold (Hardmode)
			case 216250: // Dorakiki the Bold (Lowmode)
				sendSystemMessage(SM_SYSTEM_MESSAGE.STR_MSG_IDCatacombs_NmdShulack_Rufukin);
				spawn(216527, 1161.859985f, 1213.859985f, 284.057007f, (byte) 110); // Lupukin: cat trader
				break;
			case 216206:
			case 216207:
			case 216208:
			case 216209:
			case 216210:
			case 216211:
			case 216212:
			case 216213:
				macunbello_souls++;
				switch (macunbello) {
					case 12:
						sendSystemMessage(SM_SYSTEM_MESSAGE.STR_MSG_IDCatacombs_NmdLich_weakness1);
						break;
					case 14:
						sendSystemMessage(SM_SYSTEM_MESSAGE.STR_MSG_IDCatacombs_NmdLich_weakness2);
						break;
					case 21:
						sendSystemMessage(SM_SYSTEM_MESSAGE.STR_MSG_IDCatacombs_NmdLich_weakness3);
						break;
				}
				break;
		}
	}

	private void sendSystemMessage(final SM_SYSTEM_MESSAGE msg) {
		instance.doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				PacketSendUtility.sendPacket(player, msg);
			}
		});
	}

	private void sendMsg(final String str) { // to do system message
		instance.doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				PacketSendUtility.sendMessage(player, str);
			}
		});
	}

	private void sendPacket(final AionServerPacket packet) {
		instance.doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				PacketSendUtility.sendPacket(player, packet);
			}
		});
	}

	@Override
	public void onPlayMovieEnd(Player player, int movieId) {
		switch (movieId) {
			case 443:
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_IDCatacombs_BigOrb_Spawn);
				break;
		}
	}

	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
		doors = instance.getDoors();
		doors.get(535).setOpen(true);
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
