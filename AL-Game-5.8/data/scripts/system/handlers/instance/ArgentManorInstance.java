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

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.instance.InstanceScoreType;
import com.aionemu.gameserver.model.instance.instancereward.ArgentManorReward;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INSTANCE_SCORE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.abyss.AbyssPointsService;
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
@InstanceID(301510000)
public class ArgentManorInstance extends GeneralInstanceHandler {

	// TODO add Ai for 237195 - Elemantal Iron Prison
	// TODO removes all NPCs after Timeout or Boss kill
	// TODO Rewards

	private ArgentManorReward instanceReward;
	private long startTime;
	private Future<?> timerPrepare;
	private Future<?> timerInstance;
	private int startDoorId = 14;
	private int secondDoorId = 26;
	private int thirdDoorId = 64;
	private int prepareTimerSeconds = 60000; // 1 Minute
	private int instanceTimerSeconds = 900000; // 15 Minutes

	/**
	 * list of npcIds to check after instance timer is done this Npc's must stay in instance
	 */
	private List<Integer> npcCheckList = new ArrayList<Integer>(Arrays.asList(237180, 237181, 237182, 237186, 237188, 237190, 237191, 237192, 237195, 237196, 237197, 237198, 237199, 237200, 237202, 237193, 237194));

	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
		instanceReward = new ArgentManorReward(mapId, instanceId);
		instanceReward.setInstanceScoreType(InstanceScoreType.PREPARING);

		switch (Rnd.get(1, 4)) {
			case 1:
				spawn(237180, 1025.0f, 1130.0f, 70.5f, (byte) 60);
				spawn(237190, 941.0f, 1136.0f, 71.67f, (byte) 90);
				spawn(237180, 943.0f, 1050.0f, 70.0f, (byte) 119);
				spawn(237190, 1022.0f, 1063.0f, 70.75f, (byte) 60);
				break;
			case 2:
				spawn(237190, 1025.0f, 1130.0f, 70.5f, (byte) 60);
				spawn(237180, 941.0f, 1136.0f, 71.67f, (byte) 90);
				spawn(237190, 943.0f, 1050.0f, 70.0f, (byte) 119);
				spawn(237180, 1022.0f, 1063.0f, 70.75f, (byte) 60);
				break;
			case 3:
				spawn(237180, 1025.0f, 1130.0f, 70.5f, (byte) 60);
				spawn(237180, 941.0f, 1136.0f, 71.67f, (byte) 90);
				spawn(237190, 943.0f, 1050.0f, 70.0f, (byte) 119);
				spawn(237190, 1022.0f, 1063.0f, 70.75f, (byte) 60);
				break;
			case 4:
				spawn(237190, 1025.0f, 1130.0f, 70.5f, (byte) 60);
				spawn(237190, 941.0f, 1136.0f, 71.67f, (byte) 90);
				spawn(237180, 943.0f, 1050.0f, 70.0f, (byte) 119);
				spawn(237180, 1022.0f, 1063.0f, 70.75f, (byte) 60);
				break;
		}

		// spawn random boss
		int npcId = 0;
		switch (Rnd.get(1, 2)) {
			case 1:
				npcId = 237193; // Forgotten Zadra PhyBoss
				break;
			case 2:
				npcId = 237194; // Forgotten Zadra MagBoss
				break;
		}
		spawn(npcId, 819.59827f, 1420.4639f, 194.97882f, (byte) 61);
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
		instance.getDoors().get(secondDoorId).setOpen(true);
		instance.getDoors().get(thirdDoorId).setOpen(true);

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
			public void visit(Player player) {
				switch (checkRank(instanceReward.getPoints())) {
					case 1:
						instanceReward.setScoreAP(14000);
						instanceReward.setSeniorBox(1);
						break;
					case 2:
						instanceReward.setScoreAP(12000);
						instanceReward.setIntermediateBox(1);
						break;
					case 3:
						instanceReward.setScoreAP(10000);
						instanceReward.setLesserBox(1);
						break;
					case 4:
						instanceReward.setScoreAP(5000);
						instanceReward.setLesserBox(1);
						break;
					case 5:
						instanceReward.setScoreAP(2500);
						instanceReward.setLesserBox(1);
						break;
				}
				instanceReward.setInstanceScoreType(InstanceScoreType.END_PROGRESS);
				sendPacket(0, 0);

				AbyssPointsService.addAp(player, instanceReward.getScoreAP());
				ItemService.addItem(player, 188054114, instanceReward.getSeniorBox());
				ItemService.addItem(player, 188054115, instanceReward.getIntermediateBox());
				ItemService.addItem(player, 188054116, instanceReward.getLesserBox());

				// Argent Manor Exit
				spawn(731665, 818.8859f, 1401.1049f, 194.94592f, (byte) 31);
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
			case 237186:
			case 237188:
			case 237190:
			case 237191:
			case 237192:
				points = 0;
				break;
			case 237180:
			case 237181:
			case 237182:
				points = 300;
				break;
			case 237195: // Elemantal Iron Prison
			case 237199:
			case 237200:
			case 237202:
				points = 400;
				break;
			case 237196: // Perfectly Restored Hetgolem
			case 237197: // Restored Hetgolem
			case 237198: // Poorly Restored Hetgolem
				points = 1000;
				break;
			case 237193: // Forgotten Zadra PhyBoss
			case 237194: // Forgotten Zadra MagBoss
				points = 1500;
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

	/**
	 * Elemental Iron Prison Ai
	 */
	@Override
	public void handleUseItemFinish(Player player, Npc npc) {
		switch (npc.getNpcId()) {
			case 701001:
				SkillEngine.getInstance().getSkill(npc, 19316, 60, player).useNoAnimationSkill();
				break;
			case 701002:
				SkillEngine.getInstance().getSkill(npc, 19317, 60, player).useNoAnimationSkill();
				break;
			case 701003:
				SkillEngine.getInstance().getSkill(npc, 19318, 60, player).useNoAnimationSkill();
				break;
			case 701004:
				SkillEngine.getInstance().getSkill(npc, 19319, 60, player).useNoAnimationSkill();
				break;
		}
	}

	@SuppressWarnings("unused")
	private void removeEffects(Player player) {
		player.getEffectController().removeEffect(19316);
		player.getEffectController().removeEffect(19317);
		player.getEffectController().removeEffect(19318);
		player.getEffectController().removeEffect(19319);
	}

	private int checkRank(int totalPoints) {
		int rank = 0;

		if (totalPoints >= 16000) { // S Rank
			rank = 1;
		}
		else if (totalPoints >= 13000) { // A Rank
			rank = 2;
		}
		else if (totalPoints >= 11500) { // B Rank
			rank = 3;
		}
		else if (totalPoints >= 10100) { // C Rank
			rank = 4;
		}
		else if (totalPoints >= 8100) { // D Rank
			rank = 5;
		}
		else if (totalPoints > 1500) { // F Rank
			rank = 6;
		}
		else {
			rank = 8;
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

	@Override
	public void onOpenDoor(int door) {
		if (door == startDoorId) {
			if ((timerPrepare != null) && (!timerPrepare.isDone() || !timerPrepare.isCancelled())) {
				startMainInstanceTimer();
			}
		}
	}
}
