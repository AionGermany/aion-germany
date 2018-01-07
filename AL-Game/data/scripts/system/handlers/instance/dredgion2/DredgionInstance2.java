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
package instance.dredgion2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang.mutable.MutableInt;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.ai2.manager.WalkManager;
import com.aionemu.gameserver.configs.main.GroupConfig;
import com.aionemu.gameserver.configs.main.RateConfig;
import com.aionemu.gameserver.controllers.observer.ActionObserver;
import com.aionemu.gameserver.controllers.observer.ObserverType;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.actions.CreatureActions;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.StaticDoor;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.instance.InstanceScoreType;
import com.aionemu.gameserver.model.instance.instancereward.DredgionReward;
import com.aionemu.gameserver.model.instance.instancereward.InstanceReward;
import com.aionemu.gameserver.model.instance.playerreward.DredgionPlayerReward;
import com.aionemu.gameserver.model.instance.playerreward.InstancePlayerReward;
import com.aionemu.gameserver.model.team2.group.PlayerGroupService;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INSTANCE_SCORE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.questEngine.QuestEngine;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.services.AutoGroupService;
import com.aionemu.gameserver.services.abyss.AbyssPointsService;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.skillengine.model.Skill;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.i18n.CustomMessageId;
import com.aionemu.gameserver.utils.i18n.LanguageHandler;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;

/**
 * @author xTz
 */
public class DredgionInstance2 extends GeneralInstanceHandler {

	protected int surkanKills;
	private Map<Integer, StaticDoor> doors;
	protected DredgionReward dredgionReward;
	private float loosingGroupMultiplier = 1;
	private boolean isInstanceDestroyed = false;
	protected AtomicBoolean isInstanceStarted = new AtomicBoolean(false);
	private long instanceTime;
	private Future<?> instanceTask;

	protected DredgionPlayerReward getPlayerReward(Player player) {
		Integer object = player.getObjectId();
		if (dredgionReward.getPlayerReward(object) == null) {
			addPlayerToReward(player);
		}
		return (DredgionPlayerReward) dredgionReward.getPlayerReward(object);
	}

	protected void captureRoom(Race race, int roomId) {
		dredgionReward.getDredgionRoomById(roomId).captureRoom(race);
	}

	private void addPlayerToReward(Player player) {
		dredgionReward.addPlayerReward(new DredgionPlayerReward(player.getObjectId()));
	}

	private boolean containPlayer(Integer object) {
		return dredgionReward.containPlayer(object);
	}

	protected void startInstanceTask() {
		instanceTime = System.currentTimeMillis();
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				openFirstDoors();
				dredgionReward.setInstanceScoreType(InstanceScoreType.START_PROGRESS);
				sendPacket();
			}
		}, 120000);
		instanceTask = ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				stopInstance(dredgionReward.getWinningRaceByScore());
			}
		}, 2520000);
	}

	@Override
	public void onEnterInstance(final Player player) {

		if (!containPlayer(player.getObjectId())) {
			addPlayerToReward(player);
		}
		sendPacket();
	}

	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
		dredgionReward = new DredgionReward(mapId, instanceId);
		dredgionReward.setInstanceScoreType(InstanceScoreType.PREPARING);
		doors = instance.getDoors();
	}

	protected void stopInstance(Race race) {
		stopInstanceTask();
		dredgionReward.setWinningRace(race);
		dredgionReward.setInstanceScoreType(InstanceScoreType.END_PROGRESS);
		doReward();
		sendPacket();
	}

	public void doReward() {
		for (Player player : instance.getPlayersInside()) {
			InstancePlayerReward playerReward = getPlayerReward(player);
			float abyssPoint = playerReward.getPoints(); // to do finde on what depend this modifier
			if (player.getRace().equals(dredgionReward.getWinningRace())) {
				abyssPoint += dredgionReward.getWinnerPoints();
			}
			else {
				abyssPoint += dredgionReward.getLooserPoints();
			}
			abyssPoint = abyssPoint * RateConfig.DREDGION_REWARD_RATE;
			AbyssPointsService.addAp(player, (int) abyssPoint);
			QuestEnv env = new QuestEnv(null, player, 0, 0);
			QuestEngine.getInstance().onDredgionReward(env);
		}
		for (Npc npc : instance.getNpcs()) {
			npc.getController().onDelete();
		}
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (!isInstanceDestroyed) {
					for (Player player : instance.getPlayersInside()) {
						if (CreatureActions.isAlreadyDead(player)) {
							PlayerReviveService.duelRevive(player);
						}
						onExitInstance(player);
					}
					AutoGroupService.getInstance().unRegisterInstance(instanceId);
				}
			}
		}, 10000);
	}

	@Override
	public boolean onReviveEvent(Player player) {
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
		PlayerReviveService.revive(player, 100, 100, false, 0);
		player.getGameStats().updateStatsAndSpeedVisually();
		dredgionReward.portToPosition(player);
		return true;
	}

	@Override
	public boolean onDie(Player player, Creature lastAttacker) {
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);

		PacketSendUtility.sendPacket(player, new SM_DIE(player.haveSelfRezEffect(), false, 0, 8));
		int points = 60;
		if (lastAttacker instanceof Player) {
			if (lastAttacker.getRace() != player.getRace()) {
				InstancePlayerReward playerReward = getPlayerReward(player);

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
		return dredgionReward.getPointsByRace(race);
	}

	private void addPointsByRace(Race race, int points) {
		dredgionReward.addPointsByRace(race, points);
	}

	private void addPointToPlayer(Player player, int points) {
		getPlayerReward(player).addPoints(points);
	}

	private void addPvPKillToPlayer(Player player) {
		getPlayerReward(player).addPvPKillToPlayer();
	}

	private void addBalaurKillToPlayer(Player player) {
		getPlayerReward(player).addMonsterKillToPlayer();
	}

	protected void updateScore(Player player, Creature target, int points, boolean pvpKill) {
		if (points == 0) {
			return;
		}

		// group score
		addPointsByRace(player.getRace(), points);

		// player score
		List<Player> playersToGainScore = new ArrayList<Player>();

		if (target != null && player.isInGroup2()) {
			for (Player member : player.getPlayerGroup2().getOnlineMembers()) {
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

		// recalculate point multiplier
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

		// pvpKills for pvp and balaurKills for pve
		if (pvpKill && points > 0) {
			addPvPKillToPlayer(player);
		}
		else if (target instanceof Npc && ((Npc) target).getRace().equals(Race.DRAKAN)) {
			addBalaurKillToPlayer(player);
		}
		sendPacket();
	}

	@Override
	public void onDie(Npc npc) {
		int hpGauge = npc.getObjectTemplate().getHpGauge();
		Player mostPlayerDamage = npc.getAggroList().getMostPlayerDamage();
		if (hpGauge <= 5) {
			updateScore(mostPlayerDamage, npc, 12, false);
		}
		else if (hpGauge <= 9) {
			updateScore(mostPlayerDamage, npc, 32, false);
		}
		else {
			updateScore(mostPlayerDamage, npc, 42, false);
		}
	}

	@Override
	public void onInstanceDestroy() {
		stopInstanceTask();
		isInstanceDestroyed = true;
		dredgionReward.clear();
		doors.clear();
	}

	protected void openFirstDoors() {
	}

	protected void openDoor(int doorId) {
		StaticDoor door = doors.get(doorId);
		if (door != null) {
			door.setOpen(true);
		}
	}

	private void sendPacket() {
		instance.doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(getTime(), dredgionReward, instance.getPlayersInside()));
			}
		});
	}

	private int getTime() {
		long result = System.currentTimeMillis() - instanceTime;
		if (result < 120000) {
			return (int) (120000 - result);
		}
		else if (result < 2520000) {
			return (int) (2400000 - (result - 120000));
		}
		return 0;
	}

	protected void sp(final int npcId, final float x, final float y, final float z, final byte h, final int time) {
		sp(npcId, x, y, z, h, 0, time);
	}

	protected void sp(final int npcId, final float x, final float y, final float z, final byte h, final int staticId, final int time) {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (!isInstanceDestroyed) {
					spawn(npcId, x, y, z, h, staticId);
				}
			}
		}, time);
	}

	protected void sp(final int npcId, final float x, final float y, final float z, final byte h, final int time, final String walkerId) {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (!isInstanceDestroyed) {
					Npc npc = (Npc) spawn(npcId, x, y, z, h);
					npc.getSpawn().setWalkerId(walkerId);
					WalkManager.startWalking((NpcAI2) npc.getAi2());
				}
			}
		}, time);
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

	private void stopInstanceTask() {
		if (instanceTask != null) {
			instanceTask.cancel(true);
		}
	}

	@Override
	public InstanceReward<?> getInstanceReward() {
		return dredgionReward;
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
	public void onCheckAfk(final Player player) {
		if (!dredgionReward.isStartProgress()) {
			return;
		}
		final ActionObserver observer1 = new ActionObserver(ObserverType.SKILLUSE) {

			@Override
			public void skilluse(Skill skill) {
				player.setAFKMode(false);
				player.setTimer(0);
			}
		};
		player.getObserveController().addObserver(observer1);
		final ActionObserver observer2 = new ActionObserver(ObserverType.MOVE) {

			@Override
			public void moved() {
				player.setAFKMode(false);
				player.setTimer(0);
			}
		};
		player.getObserveController().addObserver(observer2);
		final ActionObserver observer3 = new ActionObserver(ObserverType.ITEMUSE) {

			@Override
			public void skilluse(Skill skill) {
				player.setAFKMode(false);
				player.setTimer(0);
			}
		};
		player.getObserveController().addObserver(observer3);

		ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				if (player.getTimer() >= 60) { // 1min
					if (player.isInGroup2()) {
						PlayerGroupService.removePlayer(player);
					}
					TeleportService2.moveToBindLocation(player, true);
					player.getObserveController().removeObserver(observer1);
					player.getObserveController().removeObserver(observer2);
					player.getObserveController().removeObserver(observer3);
					PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.KICKED_AFK_OUT));
					player.setTimer(0);
				}
				else {
					player.setTimer(player.getTimer() + 1);
					if (!player.isAFKMode()) {
						player.setAFKMode(true);
					}
				}
			}
		}, 1000, 1000);
		player.addStatus(true);
	}
}
