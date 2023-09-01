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
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang.mutable.MutableInt;

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
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.instance.InstanceScoreType;
import com.aionemu.gameserver.model.instance.instancereward.InstanceReward;
import com.aionemu.gameserver.model.instance.instancereward.RunatoriumReward;
import com.aionemu.gameserver.model.instance.packetfactory.RunatoriumPacketsHandler;
import com.aionemu.gameserver.model.instance.playerreward.InstancePlayerReward;
import com.aionemu.gameserver.model.instance.playerreward.RunatoriumPlayerReward;
import com.aionemu.gameserver.model.team2.group.PlayerGroupService;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
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

import javolution.util.FastList;

/**
 * @author GiGatR00n v4.7.5.x
 */
@InstanceID(301310000)
public class RunatoriumInstance extends GeneralInstanceHandler {

	private final FastList<Future<?>> idgelTask = FastList.newInstance();
	protected AtomicBoolean isInstanceStarted = new AtomicBoolean(false);
	protected RunatoriumReward runatoriumReward;
	private float loosingGroupMultiplier = 1;
	private boolean isInstanceDestroyed = false;
	private Race RaceKilledBoss = null;// Used to getting Additional Reward Box

	/**
	 * Holds Idgel Dome Instance
	 */
	private WorldMapInstance Runatorium;

	/**
	 * Used to send Idgel Dome Instance Packets e.g. Score, Reward, Revive, PlayersInfo, ...
	 */
	private RunatoriumPacketsHandler RunatoriumPackets;

	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
		Runatorium = instance;
		runatoriumReward = new RunatoriumReward(mapId, instanceId, Runatorium);
		runatoriumReward.setInstanceScoreType(InstanceScoreType.PREPARING);

		RunatoriumPackets = new RunatoriumPacketsHandler(mapId, instanceId, instance, runatoriumReward);
		RunatoriumPackets.startInstanceTask();

		/* Used to handling instance timeout event */
		idgelTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				stopInstance(runatoriumReward.getWinningRaceByScore());
			}
		}, runatoriumReward.getEndTime())); // 20 Min.
	}

	@Override
	public void onEnterInstance(final Player player) {
		if (!containPlayer(player.getObjectId())) {
			runatoriumReward.regPlayerReward(player);
		}
		RunatoriumPackets.sendPreparingPacket(player);
	}

	protected RunatoriumPlayerReward getPlayerReward(Player player) {
		runatoriumReward.regPlayerReward(player);
		return getPlayerReward(player.getObjectId());
	}

	private boolean containPlayer(Integer object) {
		return runatoriumReward.containPlayer(object);
	}

	protected void reward() {
		/*
		 * Elyos & Asmodian PvP and Points
		 */
//		int ElyosPvPKills = getPvpKillsByRace(Race.ELYOS).intValue();
//		int ElyosPoints = getPointsByRace(Race.ELYOS).intValue();
//		int AsmoPvPKills = getPvpKillsByRace(Race.ASMODIANS).intValue();
//		int AsmoPoints = getPointsByRace(Race.ASMODIANS).intValue();

		for (Player player : Runatorium.getPlayersInside()) {
			if (CreatureActions.isAlreadyDead(player)) {
				PlayerReviveService.duelRevive(player);
			}
			RunatoriumPlayerReward playerReward = getPlayerReward(player.getObjectId());
			float abyssPoint = playerReward.getPoints() * RateConfig.RUNATORIUM_ABYSS_REWARD_RATE;
			float gloryPoint = 50f * RateConfig.RUNATORIUM_GLORY_REWARD_RATE;
			playerReward.setRewardAp((int) abyssPoint);
			playerReward.setRewardGp((int) gloryPoint);

			float PlayerRateModifire = player.getRates().getRunatoriumBoxRewardRate();

			if (player.getRace().equals(runatoriumReward.getWinningRace())) {
				abyssPoint += runatoriumReward.CalcBonusAbyssReward(true, isBossKilledBy(player.getRace()));
				gloryPoint += runatoriumReward.CalcBonusGloryReward(true, isBossKilledBy(player.getRace()));
				playerReward.setBonusAp(runatoriumReward.CalcBonusAbyssReward(true, isBossKilledBy(player.getRace())));
				playerReward.setBonusGp(runatoriumReward.CalcBonusGloryReward(true, isBossKilledBy(player.getRace())));
				playerReward.setReward1Count(6f * PlayerRateModifire);// Winner Team always got 6 <Fragmented Ceramium>
				playerReward.setReward2Count(1f * PlayerRateModifire);// Winner Team always got 1 <Idgel Dome Reward Box>
				playerReward.setReward1(186000243);// Fragmented Ceramium
				playerReward.setReward2(188053030);// Idgel Dome Reward Box
			}
			else {
				abyssPoint += runatoriumReward.CalcBonusAbyssReward(false, isBossKilledBy(player.getRace()));
				gloryPoint += runatoriumReward.CalcBonusGloryReward(false, isBossKilledBy(player.getRace()));
				playerReward.setRewardAp(runatoriumReward.CalcBonusAbyssReward(false, isBossKilledBy(player.getRace())));
				playerReward.setRewardGp(runatoriumReward.CalcBonusGloryReward(false, isBossKilledBy(player.getRace())));
				playerReward.setReward1Count(2f * PlayerRateModifire);// Looser Team always got 2 <Fragmented Ceramium>
				playerReward.setReward2Count(0f * PlayerRateModifire);// Winner Team always got 0 <Idgel Dome Reward Box>
				playerReward.setReward1(186000243);// Fragmented Ceramium
				playerReward.setReward2(0);// Idgel Dome Reward Box
			}

			/*
			 * Idgel Dome Tribute Box (Additional Reward) for The Team that killed the boss (Destroyer Kunax)
			 */
			if (RaceKilledBoss == player.getRace()) {
				playerReward.setAdditionalReward(188053032);
				playerReward.setAdditionalRewardCount(1f * PlayerRateModifire);
				ItemService.addItem(player, 188053032, playerReward.getAdditionalRewardCount());
			}

			playerReward.setRewardCount(PlayerRateModifire);
			ItemService.addItem(player, 186000243, playerReward.getReward1Count());// Fragmented Ceramium
			ItemService.addItem(player, 188053030, playerReward.getReward2Count());// Idgel Dome Reward Box
			AbyssPointsService.addAp(player, (int) abyssPoint);
			AbyssPointsService.addGp(player, (int) gloryPoint);
		}

		for (Npc npc : Runatorium.getNpcs()) {
			npc.getController().onDelete();
		}

		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (!isInstanceDestroyed) {
					for (Player player : Runatorium.getPlayersInside()) {
						onExitInstance(player);
					}
					AutoGroupService.getInstance().unRegisterInstance(instanceId);
				}
			}
		}, 15000);
	}

	@Override
	public boolean onReviveEvent(Player player) {
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
		PlayerReviveService.revive(player, 100, 100, false, 0);
		player.getGameStats().updateStatsAndSpeedVisually();
		runatoriumReward.portToPosition(player);

		Race OpponentRace = (player.getRace() == Race.ELYOS) ? Race.ASMODIANS : (player.getRace() == Race.ASMODIANS) ? Race.ELYOS : null;
		if (OpponentRace == null) {
			return true;
		}
		if (getPointsByRace(player.getRace()).intValue() < getPointsByRace(OpponentRace).intValue()) {
			/* Applies the BUFF_SHIELD (Underdog's Fervor) to Player for 30-Seconds */
			getPlayerReward(player.getObjectId()).endResurrectionBuff(player);
			getPlayerReward(player.getObjectId()).applyResurrectionBuff(player);
		}

		/* Send only when a PLAYER has been Revived OnRevive() */
		RunatoriumPackets.sendPlayerRevivedPacket(player);

		return true;
	}

	@Override
	public boolean onDie(Player player, Creature lastAttacker) {
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);
		PacketSendUtility.sendPacket(player, new SM_DIE(player.haveSelfRezEffect(), false, 0, 8));
		int points = 60;

		/* Send only when a PLAYER has been Killed (in PvP or PvE) */
		RunatoriumPackets.sendPlayerDiePacket(player);

		if (lastAttacker instanceof Player) {
			if (lastAttacker.getRace() != player.getRace()) {
				InstancePlayerReward playerReward = getPlayerReward(player.getObjectId());
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

	private boolean isBossKilledBy(Race PlayerRace) {
		if (PlayerRace == RaceKilledBoss) {
			return true;
		}
		return false;
	}

	private RunatoriumPlayerReward getPlayerReward(Integer ObjectId) {
		return runatoriumReward.getPlayerReward(ObjectId);
	}

	private MutableInt getPvpKillsByRace(Race race) {
		return runatoriumReward.getPvpKillsByRace(race);
	}

	private MutableInt getPointsByRace(Race race) {
		return runatoriumReward.getPointsByRace(race);
	}

	private void addPointsByRace(Race race, int points) {
		runatoriumReward.addPointsByRace(race, points);
	}

	private void addPvpKillsByRace(Race race, int points) {
		runatoriumReward.addPvpKillsByRace(race, points);
	}

	private void addPointToPlayer(Player player, int points) {
		getPlayerReward(player.getObjectId()).addPoints(points);
	}

	private void addPvPKillToPlayer(Player player) {
		getPlayerReward(player.getObjectId()).addPvPKillToPlayer();
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
		/* Send only when a NPC has been killed OnNpcDie() */
		RunatoriumPackets.sendNpcScorePacket(player);
	}

	@Override
	public void onDie(Npc npc) {
		Player mostPlayerDamage = npc.getAggroList().getMostPlayerDamage();
		if (mostPlayerDamage == null) {
			return;
		}
		int Points = 0;
		int npcId = npc.getNpcId();

		switch (npcId) {
			case 234189: // Sheban Intelligence Unit Stitch.
			case 234188: // Sheban Intelligence Unit Mongrel.
			case 234187: // Sheban Intelligence Unit Hunter.
			case 234186: // Sheban Intelligence Unit Ridgeblade.
				Points = 120;
				break;
			case 234754: // Sheban Elite Medic.
			case 234753: // Sheban Elite Marauder.
			case 234752: // Sheban Elite Sniper.
			case 234751: // Sheban Elite Stalwart.
				Points = 200;
				break;
			case 234190: // Destroyer Kunax (Ku-Nag The Slayer)
				Points = 6000;
				RaceKilledBoss = mostPlayerDamage.getRace();
				stopInstance(runatoriumReward.getWinningRaceByScore());
				break;
		}
		updateScore(mostPlayerDamage, npc, Points, false);
	}

	protected void stopInstance(Race race) {
		stopInstanceTask();
		runatoriumReward.setWinningRace(race);
		runatoriumReward.setInstanceScoreType(InstanceScoreType.END_PROGRESS);
		reward();
		/* Reward Packet */
		RunatoriumPackets.sendScoreTypePacket();// Send 1-Time when Start_Progress and End_Progress
		RunatoriumPackets.sendRewardPacket();// Send Reward Packet OnBossKilled()|OnTimeOut()
	}

	@Override
	public void onInstanceDestroy() {
		stopInstanceTask();
		isInstanceDestroyed = true;
		runatoriumReward.clear();
	}

	private void stopInstanceTask() {
		for (FastList.Node<Future<?>> n = idgelTask.head(), end = idgelTask.tail(); (n = n.getNext()) != end;) {
			if (n.getValue() != null) {
				n.getValue().cancel(true);
			}
		}
		RunatoriumPackets.ClearTasks();
	}

	@Override
	public InstanceReward<?> getInstanceReward() {
		return runatoriumReward;
	}

	@Override
	public void onLeaveInstance(Player player) {
		if (player.isInGroup2()) {
			PlayerGroupService.removePlayer(player);
		}
		/* Player Leave Packet */
		RunatoriumPackets.sendPlayerLeavePacket(player);
	}

	@Override
	public void onPlayerLogin(Player player) {
		RunatoriumPackets.sendNpcScorePacket(player);
	}

	@Override
	public void onExitInstance(Player player) {
		TeleportService2.moveToInstanceExit(player, mapId, player.getRace());
	}
}
