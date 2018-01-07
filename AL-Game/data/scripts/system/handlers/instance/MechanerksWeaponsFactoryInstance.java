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
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.instance.InstanceScoreType;
import com.aionemu.gameserver.model.instance.instancereward.MechanerksWeaponsFactoryReward;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INSTANCE_SCORE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;

/**
 * @author Falke_34
 */
@InstanceID(301640000)
public class MechanerksWeaponsFactoryInstance extends GeneralInstanceHandler {

	// Die Barrikade wird mittels SM_Emotion (27) beim benutzen des Z�ndkastens zerst�rt
	// Texte der Npc's nach dem Spawn:
	// Herez -> 1502543, Marek -> 1501598, Manad -> 1501599, Roxy -> 1501597, Joel -> 1501600

	private MechanerksWeaponsFactoryReward instanceReward;
	private long startTime;
	private Future<?> timerPrepare;
	private Future<?> timerInstance;
	// private int startDoorId = 27;
	private int prepareTimerSeconds = 15000; // 180000; 3 Minute
	private int instanceTimerSeconds = 3600000; // 60 Minutes
	int[] helper = { 833826, 833827, 833828, 833829, 833897 };

	/**
	 * list of npcIds to check after instance timer is done this Npc's must stay in instance
	 */
	private List<Integer> npcCheckList = new ArrayList<Integer>(Arrays.asList(703375, 703376, 703377, 703378, 703379, 703380, 703381, 833998));

	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
		instanceReward = new MechanerksWeaponsFactoryReward(mapId, instanceId);
		instanceReward.setInstanceScoreType(InstanceScoreType.PREPARING);
	}

	@Override
	public void onEnterInstance(final Player player) {
		startPrepareTimer();
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (player.getRace().equals(Race.ASMODIANS)) {
					SkillEngine.getInstance().applyEffectDirectly(21348, player, player, 0);
				}
				else {
					SkillEngine.getInstance().applyEffectDirectly(21347, player, player, 0);
				}
			}
		}, 1000);
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
		instance.getDoors().get(27).setOpen(true);

		// spawn helper
		for (final int spawnHelper : helper) {
			try {
				Thread.sleep(500);
				SpawnHelper(spawnHelper);
			}
			catch (InterruptedException e) {
			}
		}

		// Time before despawn so other NPC's can spawn,shout and run to 833868
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				despawn(833868);
			}
		}, 10000);

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

	private void despawn(int npcId) {
		for (Npc npc : instance.getNpcs()) {
			if (npc.getNpcId() == npcId) {
				npc.getController().die();
			}
		}
	}

	private void SpawnHelper(int npcId) {
		switch (npcId) {
			case 833826: { // Roxy
				spawn(npcId, 385.28497f, 286.91418f, 198.56036f, (byte) 60);
				break;
			}
			case 833827: { // Marek
				spawn(npcId, 385.9122f, 282.71356f, 198.16763f, (byte) 60);
				break;
			}
			case 833828: {// Manad
				spawn(npcId, 387.5144f, 289.1547f, 198.48718f, (byte) 60);
				break;
			}
			case 833829: { // Herez
				spawn(npcId, 382.41357f, 283.83466f, 198.50821f, (byte) 60);
				break;
			}
			case 833897: { // Joel
				spawn(npcId, 387.9199f, 281.45786f, 198.08975f, (byte) 60);
				break;
			}
		}
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
			public void visit(Player player) {
				switch (checkRank(instanceReward.getPoints())) {
					case 1:
						instanceReward.setSecretChest(1);
						break;
					case 2:
						instanceReward.setTreasureChest(1);
						break;
					case 3:
						instanceReward.setGoldChest(1);
						break;
					case 4:
						instanceReward.setGoldChest(1);
						break;
					case 5:
						instanceReward.setGoldChest(1);
						break;
				}
				instanceReward.setInstanceScoreType(InstanceScoreType.END_PROGRESS);
				sendPacket(0, 0);

				ItemService.addItem(player, 188055475, instanceReward.getSecretChest());
				ItemService.addItem(player, 188055647, instanceReward.getTreasureChest());
				ItemService.addItem(player, 188055648, instanceReward.getGoldChest());

				spawn(833998, 145.68423f, 268.69803f, 191.87268f, (byte) 0); // Atreia Corridor
				spawn(834167, 162.66718f, 260.04907f, 192.11089f, (byte) 60); // Jei

				spawn(834444, 143.73082f, 260.3489f, 191.8727f, (byte) 0); // Mechanerk's Gold Chest
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
			case 243657:
			case 243658:
			case 243663: // Mechanerk's Creature
				spawn(244147, 154.86708f, 259.40338f, 191.92947f, (byte) 0); // Mechanerk
				break;
			case 243705:
			case 243706: // Mechanerk's Defence Tower
			case 243993: // Mechanerk's Cannon
				spawn(243994, 230.57559f, 259.4906f, 191.01645f, (byte) 60); // Modified Cannon
				break;
			case 244023:
			case 244024:
			case 244025:
			case 244026:
			case 244027:
			case 244028:
			case 244033:
			case 244034:
			case 244035:
				points = 0;
				break;
			case 243968: // Remirinerk
			case 243969: // Bomirinerk
				points = 500;
				break;
			case 244147: // Mechanerk
				points = 878600;
				timerEnd = true;
				break;
		}
		if (instanceReward.getInstanceScoreType().isStartProgress()) {
			instanceReward.addPoints(points);
			instanceReward.setRank(checkRank(instanceReward.getPoints()));
			if (timerEnd)
				onTimerEnd();
			sendPacket(npc.getObjectTemplate().getNameId(), points);
		}
	}

	private int checkRank(int totalPoints) {
		int rank = 0;

		if (totalPoints >= 878600) { // S Rank
			rank = 1;
		}
		else if (totalPoints >= 1000) { // A Rank
			rank = 2;
		}
		else if (totalPoints >= 50) { // B Rank
			rank = 3;
		}
		else if (totalPoints >= 50) { // C Rank
			rank = 4;
		}
		else if (totalPoints >= 50) { // D Rank
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
	public void handleUseItemFinish(Player player, Npc npc) {
		switch (npc.getNpcId()) {
			case 833868:
				PacketSendUtility.sendPacket(player, new SM_EMOTION(player, EmotionType.OPEN_DOOR, 9, 0));
				instance.getDoors().get(27).setOpen(true);
				break;
		}
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
	public void onLeaveInstance(Player player) {
	}
}
