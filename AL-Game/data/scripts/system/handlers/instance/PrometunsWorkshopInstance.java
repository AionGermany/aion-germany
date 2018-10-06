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

@InstanceID(302430000)
public class PrometunsWorkshopInstance extends GeneralInstanceHandler {

	private Map<Integer, StaticDoor> doors;
	protected boolean isInstanceDestroyed = false;
	private int startRimOreGrinder;

	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
		doors = instance.getDoors();
	}

	@Override
	public void onEnterInstance(Player player) {
		super.onInstanceCreate(instance);
	}

	@Override
	public void onDie(Npc npc) {
		switch (npc.getObjectTemplate().getTemplateId()) {
			case 654728: // Drakan Legate of the 39th Legion
				doors.get(179).setOpen(true);
				sendMsgByRace(1404585, Race.PC_ALL, 0);
				break;
			case 650016: // Yarkhan
				doors.get(206).setOpen(true);
				break;
			case 655210: // Drakan Prefect of the 39th Legion
				doors.get(177).setOpen(true);
				break;
			case 655277: // Rim Ore Grinder
			case 655279: // Rim Ore Grinder
			    startRimOreGrinder++;
				if (startRimOreGrinder == 2) {
					doors.get(235).setOpen(true);
					sendMsgByRace(1404592, Race.PC_ALL, 0);
				}
				break;
			case 650018: // Suffering Prometun
				doors.get(92).setOpen(true);
				spawn(655269, 797.97754f, 872.6895f, 694.6929f, (byte) 90); // Workshop Treasure Chest

				ThreadPoolManager.getInstance().schedule(new Runnable() {
				    @Override
				    public void run() {
						deleteNpc(650042); // TODO
				    }
			    }, 5000);
				break;
			case 655211: // Drakan Strategist of the 39th Legion
				doors.get(183).setOpen(true);
				sendMsgByRace(1404589, Race.PC_ALL, 0);
				break;
			case 650021: // Tarukkan
				ThreadPoolManager.getInstance().schedule(new Runnable() {
				    @Override
				    public void run() {
						deleteNpc(837097); // TODO
				    }
			    }, 5000);
				break;
			case 650025: // Prigga
				doors.get(381).setOpen(true);
				break;
		}
	}

	// Open Door id: 233 Message 1404590 comes and the Timer start 3 Min
	// Second Message Id: 1404732
	// Destroy Rim Ore Grinder and Door Id 235 will open
	// After this 3 min the Treasure Chest have vanished, Message Id: 1404591
	
	// TODO: Jotun and the Bridge
	// TODO: Jotun and the Rim Furnace

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

	@Override
	public void onPlayerLogOut(Player player) {
		TeleportService2.moveToInstanceExit(player, mapId, player.getRace());
	}

	@Override
	public void onLeaveInstance(Player player) {
	}

	@Override
	public void onInstanceDestroy() {
		isInstanceDestroyed = true;
		doors.clear();
	}

	@Override
	public boolean onDie(final Player player, Creature lastAttacker) {
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);
		PacketSendUtility.sendPacket(player, new SM_DIE(player.haveSelfRezEffect(), player.haveSelfRezItem(), 0, 8));
		return true;
	}
}
