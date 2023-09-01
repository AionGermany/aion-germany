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
import java.util.concurrent.Future;

import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.instance.InstanceScoreType;
import com.aionemu.gameserver.model.instance.instancereward.ShugoEmperorVaultReward;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INSTANCE_SCORE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;

/**
 * @author Lyras
 * @author AntraXX
 */
@InstanceID(301400000)
public class ShugoEmperorVaultInstance extends GeneralInstanceHandler {

	private ShugoEmperorVaultReward instanceReward;
	private long startTime;
	private long currentTime;
	private long result;
	private int doorcount = 0;
	private boolean boss1_spawned = false;
	private boolean boss2_spawned = false;
	private boolean boss3_spawned = false;
	private boolean boss4_spawned = false;
	private boolean boss5_spawned = false;
	private int playerTeleportCount = 0;
	private Future<?> timerPrepare;
	private Future<?> timerInstance;
	private int startDoorId = 430;
	private int prepareTimerSeconds = 100000;
	private int instanceTimerSeconds = 480000;

	/**
	 * list of npcIds to check after instance timer is done this Npc's must stay in instance
	 */
	private List<Integer> npcCheckList = new ArrayList<Integer>(Arrays.asList(832929, 832930, 832931, 833461, 833462, 832950, 832925, 832919, 832922, 832924));

	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
		instanceReward = new ShugoEmperorVaultReward(mapId, instanceId);
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
				else if (nameId < 0) {
					PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(0, instanceReward, null));
				}
				PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(getTime() < 0 ? 0 : getTime(), instanceReward, null));
				System.out.println("Time: " + getTime());
			}
		});
	}

	private int getTime() {
		currentTime = System.currentTimeMillis();
		result = (currentTime - startTime);
		return (int) (instanceTimerSeconds - result);
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
						ItemService.addItem(object, 185000222, 7);// Rusted Vault Key
						break;
					case 2:
						instanceReward.addKeys(3);
						ItemService.addItem(object, 185000222, 3);// Rusted Vault Key
						break;
					case 3:
						instanceReward.addKeys(2);
						ItemService.addItem(object, 185000222, 2);// Rusted Vault Key
						break;
					case 4:
						instanceReward.addKeys(1);
						ItemService.addItem(object, 185000222, 1);// Rusted Vault Key
						break;
					case 5:
						ItemService.addItem(object, 185000222, 0);// Rusted Vault Key
						break;
					default:
						break;
				}
				instanceReward.setInstanceScoreType(InstanceScoreType.END_PROGRESS);
				sendPacket(0, 0);

				// The Shugo Emperor's Butler
				spawn(832932, 373.92227f, 745.97162f, 398.47003f, (byte) 107);
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
			case 235631:
				points = 160;
				break;
			case 235629:
			case 235630:
				points = 180;
				break;
			case 235652:
			case 235653:
				points = 250;
				break;
			case 235680:
			case 235681:
				points = 530;
				break;
			case 235641:
				doorcount++;
				points = 660;
				if (doorcount == 2)
					instance.getDoors().get(428).setOpen(true);
				break;
			case 235635:
			case 235636:
			case 235650:
				points = 700;
				break;
			case 235649: // ?????
				points = 760;
				break;
			case 235637:
			case 235638: // NOT SURE
				points = 820;
				break;
			case 235633:
				points = 1070;
				break;
			case 235651:
				points = 1400;
				break;
			case 235660:
				points = 1740;
				instance.getDoors().get(431).setOpen(true);
				break;
			case 235634:
				spawnObjects();
				points = 2040;
				break;
			case 235640:
				points = 12000;
				break;
			case 235685:
				points = 14400;
				break;
			case 235684:
				points = 16000;
				break;
			case 235683:
				points = 88000;
				break;
			case 235647:
				points = 224000;
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
		spawn(832925, 469.62042f, 657.500f, 396.950f, (byte) 105);// Opened Vault Door
		spawn(832919, 464.49826f, 641.73578f, 395.51651f, (byte) 0, 251);// Healing Spring
		spawn(832922, 466.7f, 646.5f, 395.75623f, (byte) 111);// Magarunerk

		// Start-Area
		spawn(832924, 544.99615f, 307.98904f, 400.2247f, (byte) 0, 433);// Opened Vault Door
		spawn(832919, 542.70917f, 299.71155f, 400.48386f, (byte) 0, 252);// Healing Spring
	}

	private void checkBosses() {
		if (instanceReward.getPoints() >= 50000 && !boss1_spawned) {
			// Captain Mirez - 12000 Points
			spawn(235640, 361.15561f, 759.19238f, 398.12143f, (byte) 103);

			spawn(235681, 356.07928f, 754.44824f, 398.00464f, (byte) 107);
			spawn(235681, 365.39032f, 763.16846f, 398.00464f, (byte) 107);
			boss1_spawned = true;
		}
		else if (instanceReward.getPoints() >= 80000 && !boss2_spawned) {
			// Longknife Zodica - 14400 Points
			spawn(235685, 361.14322f, 759.15277f, 398.14407f, (byte) 103);

			spawn(235680, 354.49707f, 752.60779f, 398.31464f, (byte) 107);
			spawn(235680, 356.54559f, 754.59491f, 398.31464f, (byte) 107);
			spawn(235680, 365.85922f, 762.83258f, 398.31464f, (byte) 107);
			spawn(235680, 368.02707f, 764.87537f, 398.31464f, (byte) 107);
			boss2_spawned = true;
		}
		else if (instanceReward.getPoints() >= 120000 && !boss3_spawned) {
			// Sorcerer Budyn - 16000 Points
			spawn(235684, 360.98407f, 759.1925f, 398.1474f, (byte) 103);

			spawn(235650, 352.86508f, 751.05011f, 398.46912f, (byte) 107);
			spawn(235650, 354.97177f, 752.91522f, 398.46912f, (byte) 107);
			spawn(235650, 356.95865f, 754.97601f, 398.46912f, (byte) 107);
			spawn(235650, 365.01694f, 762.53729f, 398.46912f, (byte) 107);
			spawn(235650, 367.00552f, 764.35352f, 398.46912f, (byte) 107);
			spawn(235650, 369.24979f, 766.2699f, 398.3624f, (byte) 107);
			boss3_spawned = true;
		}
		else if (instanceReward.getPoints() >= 160000 && !boss4_spawned) {
			// Elite Captain Rupasha - 88000 Points
			spawn(235683, 360.86523f, 759.22784f, 398.77142f, (byte) 103);

			spawn(235637, 353.62424f, 750.42053f, 398.44461f, (byte) 107);
			spawn(235637, 356.14502f, 752.76715f, 398.44461f, (byte) 107);
			spawn(235637, 358.25516f, 754.55573f, 398.44461f, (byte) 107);
			spawn(235637, 365.42892f, 762.09705f, 398.54306f, (byte) 107);
			spawn(235637, 367.64066f, 764.07806f, 398.54306f, (byte) 107);
			spawn(235637, 370.03738f, 766.17987f, 398.54306f, (byte) 107);
			boss4_spawned = true;
		}
		else if (instanceReward.getPoints() >= 270000 && !boss5_spawned) {
			// Grand Commander Gradi - 224000 Points
			spawn(235647, 361.08771f, 758.99939f, 398.42184f, (byte) 103);

			spawn(235651, 352.5451f, 749.77875f, 398.38272f, (byte) 107);
			spawn(235651, 355.40225f, 752.3205f, 398.38272f, (byte) 107);
			spawn(235651, 367.96906f, 764.01843f, 398.38272f, (byte) 107);
			spawn(235651, 370.26614f, 766.35583f, 398.38272f, (byte) 107);
			boss5_spawned = true;
		}
	}

	private int checkRank(int totalPoints) {
		int rank = 0;

		if (totalPoints >= 471200) {
			rank = 1;
		}
		else if (totalPoints >= 233700) {
			rank = 2;
		}
		else if (totalPoints >= 86400) {
			rank = 3;
		}
		else if (totalPoints >= 52100) {
			rank = 4;
		}
		else if (totalPoints >= 180) {
			rank = 5;
		}
		else {
			rank = 6;
		}

		return rank;
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

	public int getPlayerTeleportCount() {
		return playerTeleportCount;
	}

	public void incrasePlayerTeleportCount() {
		playerTeleportCount++;
		if (playerTeleportCount == 3) {
			playerTeleportCount = 0;
		}
	}

	@Override
	public void onOpenDoor(int door) {
		if (door == startDoorId) {
			if ((timerPrepare != null) && (!timerPrepare.isDone() || !timerPrepare.isCancelled())) {
				startMainInstanceTimer();
			}
		}
	}

}
