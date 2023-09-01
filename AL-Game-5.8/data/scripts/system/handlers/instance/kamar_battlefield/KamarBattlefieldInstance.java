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
package instance.kamar_battlefield;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang.mutable.MutableInt;

import com.aionemu.commons.utils.Rnd;
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
import com.aionemu.gameserver.model.instance.instancereward.KamarBattlefieldReward;
import com.aionemu.gameserver.model.instance.playerreward.InstancePlayerReward;
import com.aionemu.gameserver.model.instance.playerreward.KamarBattlefieldPlayerReward;
import com.aionemu.gameserver.model.team2.group.PlayerGroupService;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INSTANCE_SCORE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.questEngine.QuestEngine;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
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

/**
 * @author Alcapwnd
 */
@InstanceID(301120000)
public class KamarBattlefieldInstance extends GeneralInstanceHandler {

	private Map<Integer, StaticDoor> doors;
	protected KamarBattlefieldReward kamarBattlefieldReward;
	private float loosingGroupMultiplier = 1;
	private boolean isInstanceDestroyed = false;
	protected AtomicBoolean isInstanceStarted = new AtomicBoolean(false);
	private final FastList<Future<?>> tasks = FastList.newInstance();
	private long instanceTime;

	protected KamarBattlefieldPlayerReward getPlayerReward(Player player) {
		kamarBattlefieldReward.regPlayerReward(player);
		return kamarBattlefieldReward.getPlayerReward(player.getObjectId());
	}

	private boolean containPlayer(Integer object) {
		return kamarBattlefieldReward.containPlayer(object);
	}

	protected void startInstanceTask() {
		instanceTime = System.currentTimeMillis();
		kamarBattlefieldReward.setInstanceStartTime();
		tasks.add(ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				openFirstDoors();
				kamarBattlefieldReward.setInstanceScoreType(InstanceScoreType.START_PROGRESS);
				startInstancePacket();
				kamarBattlefieldReward.sendPacket(4, null);
				sp(701807, 1413.02f, 1462.26f, 598.64f, (byte) 0, 0);
				sp(701808, 1285.83f, 1489.19f, 595.64f, (byte) 0, 0);
				sp(701806, 1364.83f, 1467.69f, 599.72f, (byte) 41, 0);
				sp(701902, 1259.38f, 1630.10f, 584.875f, (byte) 98, 0);
			}
		}, 110000)); // 1 Min 50 sec

		tasks.add(ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				sendPacket(false);// 7
				kamarBattlefieldReward.sendPacket(4, null);
				sendMsgByRace(1401913, Race.PC_ALL, 0);
			}
		}, 300000)); // 5 Min.

		tasks.add(ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				sendPacket(false);// 7
				kamarBattlefieldReward.sendPacket(4, null);
				sp(701908, 1366.79f, 1546.70f, 595.37f, (byte) 0, 0, 1401840, Race.PC_ALL);
				sp(701907, 1335.49f, 1507.48f, 593.92f, (byte) 0, 0);
				sp(701906, 1354.58f, 1412.01f, 598.75f, (byte) 0, 0);
			}
		}, 600000)); // 10 Min.

		tasks.add(ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				sendPacket(false);// 7
				kamarBattlefieldReward.sendPacket(4, null);
				sp(232852, 1151.40f, 1664.43f, 601.01f, (byte) 98, 0, 1401843, Race.PC_ALL);
				sp(232846, 1147.30f, 1663.23f, 601.01f, (byte) 98, 0);
				sp(232846, 1155.56f, 1667.46f, 601.01f, (byte) 98, 0);
				sp(232852, 1212.08f, 1512.60f, 583.88f, (byte) 37, 0);
				sp(232846, 1209.02f, 1511.38f, 583.88f, (byte) 37, 0);
				sp(232846, 1216.44f, 1514.34f, 583.88f, (byte) 37, 0);
				sp(232852, 1368.45f, 1656.74f, 583.93f, (byte) 68, 0);
				sp(232846, 1366.87f, 1659.93f, 583.93f, (byte) 68, 0);
				sp(232846, 1370.34f, 1652.91f, 583.93f, (byte) 68, 0);
				sp(232852, 1556.39f, 1461.25f, 596.83f, (byte) 77, 0);
				sp(232846, 1553.00f, 1464.62f, 596.83f, (byte) 77, 0);
				sp(232846, 1559.46f, 1458.20f, 596.83f, (byte) 77, 0);
			}
		}, 900000)); // 15 Min.

		tasks.add(ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				sendPacket(false);// 7
				kamarBattlefieldReward.sendPacket(4, null);
				sp(232858, 1383.79f, 1443.30f, 599.38f, (byte) 40, 0, 1401847, Race.PC_ALL);
				sp(232860, 1388.11f, 1442.80f, 599.38f, (byte) 40, 0);
				sp(232860, 1382.31f, 1438.43f, 599.38f, (byte) 40, 0);
				sp(232857, 1253.72f, 1640.45f, 584.87f, (byte) 98, 0);
				sp(232859, 1249.29f, 1641.00f, 584.87f, (byte) 98, 0);
				sp(232859, 1255.62f, 1643.86f, 584.87f, (byte) 98, 0);
			}
		}, 1200000)); // 20 Min.

		tasks.add(ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				sendPacket(false);// 7
				kamarBattlefieldReward.sendPacket(4, null);
				sp(232854, 1437.53f, 1368.61f, 600.89f, (byte) 42, 0, 1401844, Race.PC_ALL);
				int rnd = Rnd.get(3);
				switch (rnd) {
					case 0:
						sp(232853, 1453.98f, 1347.39f, 606.35f, (byte) 42, 0);
						break;
					case 1:
						sp(232853, 1427.97f, 1617.27f, 599.94f, (byte) 72, 0);
						break;
					case 2:
						sp(232853, 1174.90f, 1440.46f, 586.55f, (byte) 42, 0);
						break;
				}

			}
		}, 1500000)); // 25 Min.

		tasks.add(ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				stopInstance(kamarBattlefieldReward.getWinningRaceByScore());
			}
		}, 1800000)); // 30 Min.
	}

	protected void stopInstance(Race race) {
		stopInstanceTask();
		kamarBattlefieldReward.setWinningRace(race);
		kamarBattlefieldReward.setInstanceScoreType(InstanceScoreType.END_PROGRESS);
		doReward();
		kamarBattlefieldReward.sendPacket(5, null);
	}

	@Override
	public void onEnterInstance(final Player player) {
		if (!containPlayer(player.getObjectId())) {
			kamarBattlefieldReward.regPlayerReward(player);
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
		sendPacket(true);// 6
		sendPacket(false);// 7
		PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(4, getTime(), getInstanceReward(), player.getObjectId(), 20, 0));
	}

	private void startInstancePacket() {
		instance.doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(7, getTime(), kamarBattlefieldReward, instance.getPlayersInside()));
				PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(3, getTime(), kamarBattlefieldReward, player.getObjectId(), 0, 0));
				PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(7, getTime(), kamarBattlefieldReward, instance.getPlayersInside()));
				PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(11, getTime(), getInstanceReward(), player.getObjectId()));
			}
		});
	}

	private void sendPacket(boolean isObjects) {
		if (isObjects) {
			instance.doOnAllPlayers(new Visitor<Player>() {

				@Override
				public void visit(Player player) {
					PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(6, getTime(), kamarBattlefieldReward, instance.getPlayersInside()));
				}
			});
		}
		else {
			instance.doOnAllPlayers(new Visitor<Player>() {

				@Override
				public void visit(Player player) {
					PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(7, getTime(), kamarBattlefieldReward, instance.getPlayersInside()));
				}
			});
		}
	}

	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
		kamarBattlefieldReward = new KamarBattlefieldReward(mapId, instanceId, instance);
		kamarBattlefieldReward.setInstanceScoreType(InstanceScoreType.PREPARING);
		doors = instance.getDoors();
		startInstanceTask();
	}

	public void doReward() {
		for (Player player : instance.getPlayersInside()) {
			KamarBattlefieldPlayerReward playerReward = kamarBattlefieldReward.getPlayerReward(player.getObjectId());
			float abyssPoint = playerReward.getPoints() * RateConfig.KAMAR_REWARD_RATE;
			float gloryPoint = playerReward.getPoints() * RateConfig.KAMAR_REWARD_RATE;
			playerReward.setBonusAp((int) abyssPoint);
			playerReward.setBonusGp((int) gloryPoint);
			if (player.getRace().equals(kamarBattlefieldReward.getWinningRace())) {
				abyssPoint += kamarBattlefieldReward.getWinnerPoints();
				gloryPoint += kamarBattlefieldReward.getWinnerPoints();
				playerReward.setRewardAp(kamarBattlefieldReward.getWinnerPoints());
				playerReward.setRewardGp(kamarBattlefieldReward.getWinnerPoints());
			}
			else {
				abyssPoint += kamarBattlefieldReward.getLooserPoints();
				gloryPoint += kamarBattlefieldReward.getLooserPoints();
				playerReward.setRewardAp(kamarBattlefieldReward.getLooserPoints());
				playerReward.setRewardGp(kamarBattlefieldReward.getLooserPoints());
			}
			playerReward.setReward1(188052670);// Kamar Victory Box
			playerReward.setReward2(186000236);// Blood Mark
			playerReward.setRewardCount(player.getRates().getKamarRewardRate());
			ItemService.addItem(player, 188052670, (long) player.getRates().getKamarRewardRate());
			ItemService.addItem(player, 186000236, (long) player.getRates().getKamarRewardRate());
			AbyssPointsService.addAp(player, (int) abyssPoint);
			AbyssPointsService.addGp(player, (int) gloryPoint);
			QuestEnv env = new QuestEnv(null, player, 0, 0);
			QuestEngine.getInstance().onKamarReward(env);
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

	@Override
	public boolean onReviveEvent(Player player) {
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
		PlayerReviveService.revive(player, 100, 100, false, 0);
		player.getGameStats().updateStatsAndSpeedVisually();
		kamarBattlefieldReward.portToPosition(player);
		return true;
	}

	@Override
	public boolean onDie(Player player, Creature lastAttacker) {
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);
		PacketSendUtility.sendPacket(player, new SM_DIE(player.haveSelfRezEffect(), false, 0, 8));
		int points = 60;
		if (lastAttacker instanceof Player) {
			if (lastAttacker.getRace() != player.getRace()) {
				InstancePlayerReward playerReward = kamarBattlefieldReward.getPlayerReward(player.getObjectId());
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
		return kamarBattlefieldReward.getPointsByRace(race);
	}

	private void addPointsByRace(Race race, int points) {
		kamarBattlefieldReward.addPointsByRace(race, points);
	}

	private void addPvpKillsByRace(Race race, int points) {
		kamarBattlefieldReward.addPvpKillsByRace(race, points);
	}

	private void addPointToPlayer(Player player, int points) {
		kamarBattlefieldReward.getPlayerReward(player.getObjectId()).addPoints(points);
	}

	private void addPvPKillToPlayer(Player player) {
		kamarBattlefieldReward.getPlayerReward(player.getObjectId()).addPvPKillToPlayer();
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
		kamarBattlefieldReward.sendPacket(11, player.getObjectId());
		if (kamarBattlefieldReward.hasCapPoints()) {
			stopInstance(kamarBattlefieldReward.getWinningRaceByScore());
		}
	}

	@Override
	public void onDie(Npc npc) {
		Player mostPlayerDamage = npc.getAggroList().getMostPlayerDamage();
		if (mostPlayerDamage == null) {
			return;
		}
		int point = 50;
		switch (npc.getNpcId()) {
			case 232847:
			case 232848:
			case 232849:
			case 232850:
			case 232851:
			case 233261:
				point = 140;
				break;
			case 232853:
				point = 3500;
				sendMsgByRace(1401846, Race.PC_ALL, 0);
				break;
			case 232854:
				point = 2100;
				break;
			case 232857:
			case 232858:
				point = 4500;
				break;
			case 232860:
			case 232859:
				point = 500;
				break;
			case 232852:
			case 232855:
				point = 1250;
				break;
			case 801771:
			case 801773:
				point = 75;
				break;
			case 701807:
				point = 225;
				sp(701807, 1413.02f, 1462.26f, 598.64f, (byte) 0, 60000);
				break;
			case 701808:
				point = 225;
				sp(701808, 1285.83f, 1489.19f, 595.64f, (byte) 0, 60000);
				break;
			case 701806:
				sp(701806, 1364.83f, 1467.69f, 599.72f, (byte) 41, 60000);
				break;
			case 701902:
				sp(701902, 1259.38f, 1630.10f, 589.82f, (byte) 98, 60000);
				break;
		}
		updateScore(mostPlayerDamage, npc, point, false);
	}

	@Override
	public void handleUseItemFinish(Player player, Npc npc) {
		int point = 225;
		switch (npc.getNpcId()) {
			case 801903:
				point = 1500;
				break;
			case 730878:
			case 730879:
			case 730880:
				point = 200;
				break;
			case 701906:
			case 701907:
			case 701908:
				point = 525;
				break;
		}
		updateScore(player, npc, point, false);
	}

	@Override
	public void onInstanceDestroy() {
		stopInstanceTask();
		isInstanceDestroyed = true;
		kamarBattlefieldReward.clear();
		doors.clear();
	}

	protected void openFirstDoors() {
		openDoor(10);
		openDoor(11);
		openDoor(3);
		openDoor(8);
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

	protected void sp(final int npcId, final float x, final float y, final float z, final byte h, final int staticId, final int time, final int msg, final Race race) {
		tasks.add(ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (!isInstanceDestroyed) {
					spawn(npcId, x, y, z, h, staticId);
					if (msg > 0) {
						sendMsgByRace(msg, race, 0);
					}
				}
			}
		}, time));
	}

	protected void sp(final int npcId, final float x, final float y, final float z, final byte h, final int time, final String walkerId) {
		tasks.add(ThreadPoolManager.getInstance().schedule(new Runnable() {

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
		tasks.add(ThreadPoolManager.getInstance().schedule(new Runnable() {

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
		for (FastList.Node<Future<?>> n = tasks.head(), end = tasks.tail(); (n = n.getNext()) != end;) {
			if (n.getValue() != null) {
				n.getValue().cancel(true);
			}
		}
	}

	@Override
	public InstanceReward<?> getInstanceReward() {
		return kamarBattlefieldReward;
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
		kamarBattlefieldReward.sendPacket(10, player.getObjectId());
	}
}
