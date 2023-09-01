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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.drop.DropItem;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.instance.InstanceScoreType;
import com.aionemu.gameserver.model.instance.instancereward.FireTempleOfMemoryReward;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INSTANCE_SCORE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;

/**
 * @author Falke_34
 */
@InstanceID(302000000)
public class FireTempleOfMemory extends GeneralInstanceHandler {

	private FireTempleOfMemoryReward instanceReward;
	private long startTime;
	private boolean boss1_spawned = false;
	private boolean boss2_spawned = false;
	private boolean boss3_spawned = false;
	private boolean boss4_spawned = false;
	private Future<?> timerPrepare;
	private Future<?> timerInstance;
	private int startDoorId = 2;
	private int prepareTimerSeconds = 100000; // 1:40 Minutes
	private int instanceTimerSeconds = 480000; // 8 Minutes

	/**
	 * list of npcIds to check after instance timer is done this Npc's must stay in instance
	 */
	private List<Integer> npcCheckList = new ArrayList<Integer>(Arrays.asList(833058, 833059, 834065, 834066, 834068, 834071));

	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
		instanceReward = new FireTempleOfMemoryReward(mapId, instanceId);
		instanceReward.setInstanceScoreType(InstanceScoreType.PREPARING);
	}

	@Override
	public void onEnterInstance(final Player player) {
		startPrepareTimer();
	}

	@Override
	public void onInstanceDestroy() {
		if (timerInstance != null) {
			timerInstance.cancel(false);
		}
		if (timerPrepare != null) {
			timerPrepare.cancel(false);
		}
	}

	private void startPrepareTimer() {
		if (timerPrepare == null) {
			timerPrepare = ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					startMainInstanceTimer();
				}
			}, prepareTimerSeconds);
		}

		instance.doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(prepareTimerSeconds, instanceReward, null));
			}
		});
	}

	private void startMainInstanceTimer() {
		// cancel the prepare-timer if is set
		if (!timerPrepare.isDone()) {
			timerPrepare.cancel(false);
		}

		// open the start door
		instance.getDoors().get(startDoorId).setOpen(true);

		// set the current time for later calculation
		startTime = System.currentTimeMillis();

		// set instanceReward state
		instanceReward.setInstanceScoreType(InstanceScoreType.START_PROGRESS);
		sendPacket(0, 0);

		// start main instance timer
		timerInstance = ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				onTimerEnd();
			}
		}, instanceTimerSeconds);
	}

	private void sendPacket(final int nameId, final int point) {
		instance.doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				if (nameId != 0) {
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400237, new DescriptionId(nameId * 2 + 1), point));
				}
				PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(getTime(), instanceReward, null));
			}
		});
	}

	private int getTime() {
		long result = (int) (System.currentTimeMillis() - startTime);

		return instanceTimerSeconds - (int) result;
	}

	private void onTimerEnd() {
		if (!timerInstance.isDone()) {
			timerInstance.cancel(true);
		}

		for (Npc npc : instance.getNpcs()) {
			if (npcCheckList.contains(npc.getNpcId())) {
				continue;
			}
			npc.getController().delete();
		}

		instance.doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player object) {
				switch (checkRank(instanceReward.getPoints())) {
					case 1:
						instanceReward.addKeys(7);
						ItemService.addItem(object, 185000270, 7);
						break;
					case 2:
						instanceReward.addKeys(3);
						ItemService.addItem(object, 185000270, 3);
						break;
					case 3:
						instanceReward.addKeys(2);
						ItemService.addItem(object, 185000270, 2);
						break;
					case 4:
						instanceReward.addKeys(1);
						ItemService.addItem(object, 185000270, 1);
						break;
					case 5:
						ItemService.addItem(object, 185000270, 0);
						break;
					default:
						break;
				}
				instanceReward.setInstanceScoreType(InstanceScoreType.END_PROGRESS);
				sendPacket(0, 0);

				// Butler at Entrance to Treasure Chamber
				spawn(834068, 421.26712f, 93.82741f, 117.30522f, (byte) 50);
			}
		});
	}

	@Override
	public void onDie(Npc npc) {
		Creature master = npc.getMaster();
		boolean timerEnd = false;
		if (master instanceof Player)
			return;

		int npcId = npc.getNpcId();
		int points = 0;
		switch (npcId) {
			case 244084:
			case 244085:
			case 244091:
			case 244092:
				points = 180;
				break;
			case 244086:
				points = 160;
				break;
			case 244087:
			case 244088:
			case 244093:
				points = 250;
				break;
			case 244089:
				points = 660;
				break;
			case 244094: // Special Molgat Blue Crystal
				points = 1740;
				instance.getDoors().get(5).setOpen(true);
				break;
			case 244095: // Strong Silver Blade Rotan
				points = 2040;
				instance.getDoors().get(8).setOpen(true);
				break;
			case 244096: // Strong Tough Sipus
				spawnObjects();
				points = 12000;
				break;
			case 244097: // Temple Guardian
				points = 14400;
				break;
			case 244098: // Raging Madame Anger
				points = 48000;
				break;
			case 244099: // Raging Kaliga
				points = 272000;
				break;
			case 244100: // Raging Kromede
				points = 500000;
				timerEnd = true;
				break;
		}
		if (instanceReward.getInstanceScoreType().isStartProgress()) {
			instanceReward.addPoints(points);
			instanceReward.setRank(checkRank(instanceReward.getPoints()));
			if (timerEnd) {
				onTimerEnd();
			}
			sendPacket(npc.getObjectTemplate().getNameId(), points);
			checkBosses();
		}
	}

	private void spawnObjects() {
		// Vault-Area
		spawn(834067, 292.06992f, 167.17885f, 120.11304f, (byte) 37, 40);// Kromede's Room
		spawn(834065, 280.79156f, 170.82999f, 120.42773f, (byte) 38, 306);// Source of Life

		// Start-Area
		spawn(834066, 168.82631f, 416.70334f, 142.41232f, (byte) 38, 3);// Kromede's Room
		spawn(834065, 156.97145f, 426.73328f, 140.64249f, (byte) 38, 306);// Source of Life
	}

	private void checkBosses() {
		if (instanceReward.getPoints() >= 32000 && !boss1_spawned) { // TODO Points and Adds
			// Temple Guardian
			spawn(244097, 414.2329f, 97.95426f, 117.194f, (byte) 50);

			boss1_spawned = true;
		}
		else if (instanceReward.getPoints() >= 72000 && !boss2_spawned) { // TODO Points and Adds
			// Raging Madame Anger
			spawn(244098, 415.44394f, 97.25835f, 117.194f, (byte) 50);

			boss2_spawned = true;
		}
		else if (instanceReward.getPoints() >= 152000 && !boss3_spawned) { // TODO Points and Adds
			// Raging Kaliga
			spawn(244099, 416.76453f, 96.47669f, 117.30521f, (byte) 50);

			boss3_spawned = true;
		}
		else if (instanceReward.getPoints() >= 462000 && !boss4_spawned) { // TODO Points and Adds
			// Raging Kromede
			spawn(244100, 418.02496f, 95.696465f, 117.30521f, (byte) 50);

			boss4_spawned = true;
		}
	}

	private int checkRank(int totalPoints) {
		int rank = 0;

		if (totalPoints >= 878600) { // S Rank
			rank = 1;
		}
		else if (totalPoints >= 463800) { // A Rank
			rank = 2;
		}
		else if (totalPoints >= 165100) { // B Rank
			rank = 3;
		}
		else if (totalPoints >= 54000) { // C Rank
			rank = 4;
		}
		else if (totalPoints >= 180) { // D Rank
			rank = 5;
		}
		else if (totalPoints > 0) { // F Rank
			rank = 6;
		}
		else {
			rank = 8;
		}

		return rank;
	}

	@Override
	public void onOpenDoor(int door) {
		if (door == startDoorId) {
			if ((timerPrepare != null) && (!timerPrepare.isDone() || !timerPrepare.isCancelled())) {
				startMainInstanceTimer();
			}
		}
	}

	protected void despawnNpc(Npc npc) {
		if (npc != null) {
			npc.getController().onDelete();
		}
	}

	@Override
	public void onLeaveInstance(Player player) {

	}

	@Override
	public void onExitInstance(Player player) {
		TeleportService2.moveToInstanceExit(player, mapId, player.getRace());
	}

	@Override
	public void onPlayerLogOut(Player player) {
		TeleportService2.moveToInstanceExit(player, mapId, player.getRace());
	}

	@Override
	public boolean onDie(final Player player, Creature lastAttacker) {
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);
		PacketSendUtility.sendPacket(player, new SM_DIE(player.haveSelfRezEffect(), player.haveSelfRezItem(), 0, 8));
		return true;
	}

	@Override
	public void onDropRegistered(Npc npc) {
		Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		int npcId = npc.getNpcId();
		switch (npcId) {
			case 244435: // Potions Chest
				int index = dropItems.size() + 1;
				for (Player player : instance.getPlayersInside()) {
					if (player.isOnline()) {
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 162002085, 6));
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 162002086, 3));
					}
				}
				break;
		}
	}
}
