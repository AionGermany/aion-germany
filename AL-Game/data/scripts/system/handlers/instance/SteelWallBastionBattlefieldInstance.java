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
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang.mutable.MutableInt;

import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.ai2.manager.WalkManager;
import com.aionemu.gameserver.configs.main.GroupConfig;
import com.aionemu.gameserver.configs.main.RateConfig;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.actions.CreatureActions;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.StaticDoor;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.instance.InstanceScoreType;
import com.aionemu.gameserver.model.instance.instancereward.InstanceReward;
import com.aionemu.gameserver.model.instance.instancereward.SteelWallBastionBattlefieldReward;
import com.aionemu.gameserver.model.instance.playerreward.InstancePlayerReward;
import com.aionemu.gameserver.model.instance.playerreward.SteelWallBastionBattlefieldPlayerReward;
import com.aionemu.gameserver.model.team2.group.PlayerGroupService;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INSTANCE_SCORE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.AutoGroupService;
import com.aionemu.gameserver.services.abyss.AbyssPointsService;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;

import javolution.util.FastList;

@InstanceID(301220000)
public class SteelWallBastionBattlefieldInstance extends GeneralInstanceHandler {

	private Map<Integer, StaticDoor> doors;
	protected SteelWallBastionBattlefieldReward steelWallBastionBattlefieldReward;
	private float loosingGroupMultiplier = 1;
	private boolean isInstanceDestroyed = false;
	protected AtomicBoolean isInstanceStarted = new AtomicBoolean(false);
	private final FastList<Future<?>> ironTask = FastList.newInstance();
	private long instanceTime;

	protected SteelWallBastionBattlefieldPlayerReward getPlayerReward(Player player) {
		steelWallBastionBattlefieldReward.regPlayerReward(player);
		return steelWallBastionBattlefieldReward.getPlayerReward(player.getObjectId());
	}

	private boolean containPlayer(Integer object) {
		return steelWallBastionBattlefieldReward.containPlayer(object);
	}

	protected void startInstanceTask() {
		instanceTime = System.currentTimeMillis();
		steelWallBastionBattlefieldReward.setInstanceStartTime();
		ironTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				openFirstDoors();
				steelWallBastionBattlefieldReward.setInstanceScoreType(InstanceScoreType.START_PROGRESS);
				startInstancePacket();
				steelWallBastionBattlefieldReward.sendPacket(4, null);
			}
		}, 110000));
		// TO DO SCRIPT
		ironTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				stopInstance(steelWallBastionBattlefieldReward.getWinningRaceByScore());
			}
		}, 1800000));
	}

	protected void stopInstance(Race race) {
		stopInstanceTask();
		steelWallBastionBattlefieldReward.setWinningRace(race);
		steelWallBastionBattlefieldReward.setInstanceScoreType(InstanceScoreType.END_PROGRESS);
		reward();
		steelWallBastionBattlefieldReward.sendPacket(5, null);
	}

	@Override
	public void onEnterInstance(final Player player) {
		if (!containPlayer(player.getObjectId())) {
			steelWallBastionBattlefieldReward.regPlayerReward(player);
		}
		sendEnterPacket(player);
	}

	private void sendEnterPacket(final Player player) {
		instance.doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player opponent) {
				if (player.getRace() != opponent.getRace()) {
					PacketSendUtility.sendPacket(opponent, new SM_INSTANCE_SCORE(11, getTime(), getInstanceReward(), player.getObjectId()));
					PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(11, getTime(), getInstanceReward(), opponent.getObjectId()));
					PacketSendUtility.sendPacket(opponent, new SM_INSTANCE_SCORE(3, getTime(), getInstanceReward(), player.getObjectId()));
				}
				else {
					PacketSendUtility.sendPacket(opponent, new SM_INSTANCE_SCORE(11, getTime(), getInstanceReward(), opponent.getObjectId()));
					if (player.getObjectId() != opponent.getObjectId()) {
						PacketSendUtility.sendPacket(opponent, new SM_INSTANCE_SCORE(3, getTime(), getInstanceReward(), player.getObjectId(), 20, 0));
					}
				}
			}
		});
		sendPacket(true);
		sendPacket(false);
		PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(4, getTime(), getInstanceReward(), player.getObjectId(), 20, 0));
	}

	private void startInstancePacket() {
		instance.doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(7, getTime(), steelWallBastionBattlefieldReward, instance.getPlayersInside()));
				PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(3, getTime(), steelWallBastionBattlefieldReward, player.getObjectId(), 0, 0));
				PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(7, getTime(), steelWallBastionBattlefieldReward, instance.getPlayersInside()));
				PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(11, getTime(), getInstanceReward(), player.getObjectId()));
			}
		});
	}

	private void sendPacket(boolean isObjects) {
		if (isObjects) {
			instance.doOnAllPlayers(new Visitor<Player>() {

				@Override
				public void visit(Player player) {
					PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(6, getTime(), steelWallBastionBattlefieldReward, instance.getPlayersInside()));
				}
			});
		}
		else {
			instance.doOnAllPlayers(new Visitor<Player>() {

				@Override
				public void visit(Player player) {
					PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(7, getTime(), steelWallBastionBattlefieldReward, instance.getPlayersInside()));
				}
			});
		}
	}

	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
		steelWallBastionBattlefieldReward = new SteelWallBastionBattlefieldReward(mapId, instanceId, instance);
		steelWallBastionBattlefieldReward.setInstanceScoreType(InstanceScoreType.PREPARING);
		doors = instance.getDoors();
		startInstanceTask();
	}

	protected void reward() {
		for (Player player : instance.getPlayersInside()) {
			if (CreatureActions.isAlreadyDead(player)) {
				PlayerReviveService.duelRevive(player);
			}
			SteelWallBastionBattlefieldPlayerReward playerReward = steelWallBastionBattlefieldReward.getPlayerReward(player.getObjectId());
			float abyssPoint = playerReward.getPoints() * RateConfig.STEELWALL_REWARD_RATE;
			float gloryPoint = (1.0f * 0.5f) * 100;
			playerReward.setBonusAp((int) abyssPoint);
			playerReward.setBonusGp((int) gloryPoint);
			if (player.getRace().equals(steelWallBastionBattlefieldReward.getWinningRace())) {
				abyssPoint += steelWallBastionBattlefieldReward.getWinnerPoints();
				gloryPoint += steelWallBastionBattlefieldReward.getWinnerPoints();
				playerReward.setRewardAp(steelWallBastionBattlefieldReward.getWinnerPoints());
				playerReward.setRewardGp(steelWallBastionBattlefieldReward.getWinnerPoints());
			}
			else {
				abyssPoint += steelWallBastionBattlefieldReward.getLooserPoints();
				gloryPoint += steelWallBastionBattlefieldReward.getLooserPoints();
				playerReward.setRewardAp(steelWallBastionBattlefieldReward.getLooserPoints());
				playerReward.setRewardGp(steelWallBastionBattlefieldReward.getLooserPoints());
			}
			playerReward.setMedalBundle(188052938);
			playerReward.setBloodMark(186000236);
			playerReward.setRewardCount(player.getRates().getSteelWallRewardRate());
			ItemService.addItem(player, 188052938, (long) player.getRates().getSteelWallRewardRate());
			ItemService.addItem(player, 186000236, (long) player.getRates().getSteelWallRewardRate());
			AbyssPointsService.addAp(player, (int) abyssPoint);
			AbyssPointsService.addGp(player, (int) gloryPoint);
		}
		for (Npc npc : instance.getNpcs()) {
			npc.getController().onDelete();
		}
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (!isInstanceDestroyed) {
					for (Player player : instance.getPlayersInside()) {
						onExitInstance(player);
					}
					AutoGroupService.getInstance().unRegisterInstance(instanceId);
				}
			}
		}, 10000);
	}

	private int getTime() {
		long result = System.currentTimeMillis() - instanceTime;
		if (result < 60000) {
			return (int) (60000 - result);
		}
		else if (result < 2520000) {
			return (int) (2400000 - (result - 60000));
		}
		return 0;
	}

	@Override
	public boolean onReviveEvent(Player player) {
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
		PlayerReviveService.revive(player, 100, 100, false, 0);
		player.getGameStats().updateStatsAndSpeedVisually();
		steelWallBastionBattlefieldReward.portToPosition(player);
		return true;
	}

	@Override
	public boolean onDie(Player player, Creature lastAttacker) {
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);
		PacketSendUtility.sendPacket(player, new SM_DIE(player.haveSelfRezEffect(), false, 0, 8));
		int points = 60;
		if (lastAttacker instanceof Player) {
			if (lastAttacker.getRace() != player.getRace()) {
				InstancePlayerReward playerReward = steelWallBastionBattlefieldReward.getPlayerReward(player.getObjectId());
				if (getPointsByRace(lastAttacker.getRace()).compareTo(getPointsByRace(player.getRace())) < 0) {
					points *= loosingGroupMultiplier;
				}
				else if (loosingGroupMultiplier == 10 || playerReward.getPoints() == 0) {
					points = 0;
				}
				updateScore((Player) lastAttacker, player, points, true);
			}
		}
		updateScore(player, player, -points, false);
		return true;
	}

	private MutableInt getPointsByRace(Race race) {
		return steelWallBastionBattlefieldReward.getPointsByRace(race);
	}

	private void addPointsByRace(Race race, int points) {
		steelWallBastionBattlefieldReward.addPointsByRace(race, points);
	}

	private void addPvpKillsByRace(Race race, int points) {
		steelWallBastionBattlefieldReward.addPvpKillsByRace(race, points);
	}

	private void addPointToPlayer(Player player, int points) {
		steelWallBastionBattlefieldReward.getPlayerReward(player.getObjectId()).addPoints(points);
	}

	private void addPvPKillToPlayer(Player player) {
		steelWallBastionBattlefieldReward.getPlayerReward(player.getObjectId()).addPvPKillToPlayer();
	}

	protected void updateScore(Player player, Creature target, int points, boolean pvpKill) {
		if (points == 0) {
			return;
		}
		addPointsByRace(player.getRace(), points);
		List<Player> playersToGainScore = new ArrayList<Player>();
		if (target != null && player.isInGroup2()) {
			for (Player member : player.getPlayerAlliance2().getOnlineMembers()) {
				if (member.getLifeStats().isAlreadyDead()) {
					continue;
				}
				if (MathUtil.isIn3dRange(member, target, GroupConfig.GROUP_MAX_DISTANCE)) {
					playersToGainScore.add(member);
				}
			}
		}
		else {
			playersToGainScore.add(player);
		}
		for (Player playerToGainScore : playersToGainScore) {
			addPointToPlayer(playerToGainScore, points / playersToGainScore.size());
			if (target instanceof Npc) {
				PacketSendUtility.sendPacket(playerToGainScore, new SM_SYSTEM_MESSAGE(1400237, new DescriptionId(((Npc) target).getObjectTemplate().getNameId() * 2 + 1), points));
			}
			else if (target instanceof Player) {
				PacketSendUtility.sendPacket(playerToGainScore, new SM_SYSTEM_MESSAGE(1400237, target.getName(), points));
			}
		}
		int pointDifference = getPointsByRace(Race.ASMODIANS).intValue() - (getPointsByRace(Race.ELYOS)).intValue();
		if (pointDifference < 0) {
			pointDifference *= -1;
		}
		if (pointDifference >= 3000) {
			loosingGroupMultiplier = 10;
		}
		else if (pointDifference >= 1000) {
			loosingGroupMultiplier = 1.5f;
		}
		else {
			loosingGroupMultiplier = 1;
		}
		if (pvpKill && points > 0) {
			addPvpKillsByRace(player.getRace(), 1);
			addPvPKillToPlayer(player);
		}
		steelWallBastionBattlefieldReward.sendPacket(11, player.getObjectId());
	}

	@Override
	public void onDie(Npc npc) {
		Player mostPlayerDamage = npc.getAggroList().getMostPlayerDamage();
		if (mostPlayerDamage == null) {
			return;
		}
		int point = 0;
		switch (npc.getNpcId()) {
		}
		updateScore(mostPlayerDamage, npc, point, false);
	}

	@Override
	public void handleUseItemFinish(Player player, Npc npc) {
		int point = 0;
		switch (npc.getNpcId()) {
		}
		updateScore(player, npc, point, false);
	}

	@SuppressWarnings("unused")
	private void despawnNpc(Npc npc) {
		if (npc != null) {
			npc.getController().onDelete();
		}
	}

	@Override
	public void onInstanceDestroy() {
		stopInstanceTask();
		isInstanceDestroyed = true;
		steelWallBastionBattlefieldReward.clear();
		doors.clear();
	}

	protected void openFirstDoors() {
		openDoor(2);
		openDoor(17);
		openDoor(26);
		openDoor(35);
	}

	protected void openDoor(int doorId) {
		StaticDoor door = doors.get(doorId);
		if (door != null) {
			door.setOpen(true);
		}
	}

	protected void sp(final int npcId, final float x, final float y, final float z, final byte h, final int time) {
		sp(npcId, x, y, z, h, 0, time, 0, null);
	}

	protected void sp(final int npcId, final float x, final float y, final float z, final byte h, final int time, final int msg, final Race race) {
		sp(npcId, x, y, z, h, 0, time, msg, race);
	}

	protected void sp(final int npcId, final float x, final float y, final float z, final byte h, final int entityId, final int time, final int msg, final Race race) {
		ironTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (!isInstanceDestroyed) {
					spawn(npcId, x, y, z, h, entityId);
					if (msg > 0) {
						sendMsgByRace(msg, race, 0);
					}
				}
			}
		}, time));
	}

	protected void sp(final int npcId, final float x, final float y, final float z, final byte h, final int time, final String walkerId) {
		ironTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (!isInstanceDestroyed) {
					Npc npc = (Npc) spawn(npcId, x, y, z, h);
					npc.getSpawn().setWalkerId(walkerId);
					WalkManager.startWalking((NpcAI2) npc.getAi2());
				}
			}
		}, time));
	}

	protected void sendMsgByRace(final int msg, final Race race, int time) {
		ironTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {

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
		}, time));
	}

	private void stopInstanceTask() {
		for (FastList.Node<Future<?>> n = ironTask.head(), end = ironTask.tail(); (n = n.getNext()) != end;) {
			if (n.getValue() != null) {
				n.getValue().cancel(true);
			}
		}
	}

	@Override
	public InstanceReward<?> getInstanceReward() {
		return steelWallBastionBattlefieldReward;
	}

	@Override
	public void onExitInstance(Player player) {
		TeleportService2.moveToInstanceExit(player, mapId, player.getRace());
	}

	@Override
	public void onLeaveInstance(Player player) {
		if (player.isInGroup2()) {
			PlayerGroupService.removePlayer(player);
		}
	}

	@Override
	public void onPlayerLogin(Player player) {
		steelWallBastionBattlefieldReward.sendPacket(10, player.getObjectId());
	}
}
