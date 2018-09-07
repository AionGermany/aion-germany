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

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.ai2.manager.WalkManager;
import com.aionemu.gameserver.configs.main.GroupConfig;
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
import com.aionemu.gameserver.model.instance.instancereward.JormungandReward;
import com.aionemu.gameserver.model.instance.playerreward.InstancePlayerReward;
import com.aionemu.gameserver.model.instance.playerreward.JormungandPlayerReward;
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
import com.aionemu.gameserver.world.zone.ZoneInstance;
import com.aionemu.gameserver.world.zone.ZoneName;

import javolution.util.FastList;

@InstanceID(301210000)
public class JormungandMarchingRouteInstance extends GeneralInstanceHandler {

	private long instanceTime;
	private int powerGenerator;
	private Map<Integer, StaticDoor> doors;
	protected JormungandReward jormungandReward;
	private float loosingGroupMultiplier = 1;
	private boolean isInstanceDestroyed = false;
	protected AtomicBoolean isInstanceStarted = new AtomicBoolean(false);
	private final FastList<Future<?>> ophidanTask = FastList.newInstance();

	protected JormungandPlayerReward getPlayerReward(Player player) {
		jormungandReward.regPlayerReward(player);
		return jormungandReward.getPlayerReward(player.getObjectId());
	}

	private boolean containPlayer(Integer object) {
		return jormungandReward.containPlayer(object);
	}

	protected void startInstanceTask() {
		instanceTime = System.currentTimeMillis();
		jormungandReward.setInstanceStartTime();
		ophidanTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				openFirstDoors();
				jormungandReward.setInstanceScoreType(InstanceScoreType.START_PROGRESS);
				startInstancePacket();
				jormungandReward.sendPacket(4, null);
			}
		}, 110000));
		ophidanTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				sendPacket(false);
				jormungandReward.sendPacket(4, null);
				sendMsgByRace(1401967, Race.ELYOS, 0);
				sendMsgByRace(1401968, Race.ASMODIANS, 0);
				sp(701988, 313.6124f, 489.13992f, 597.13184f, (byte) 2, 0); // Rearguard Telekesis.
				sp(701989, 759.2739f, 569.3167f, 577.37885f, (byte) 87, 0); // Rearguard Freidr.
			}
		}, 400000));
		ophidanTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				sendPacket(false);
				jormungandReward.sendPacket(4, null);
				sendMsgByRace(1401965, Race.PC_ALL, 0);
				sendMsgByRace(1402086, Race.PC_ALL, 12000);
				sp(701974, 322.18567f, 490.11285f, 596.1117f, (byte) 1, 0); // Emergency Supply Box.
				sp(701974, 758.0247f, 560.9797f, 576.9838f, (byte) 87, 0); // Emergency Supply Box.
				sp(701975, 574.02966f, 477.84848f, 620.6126f, (byte) 93, 12000); // Emergency Supply Box.
				sp(701975, 619.36755f, 515.6929f, 592.13336f, (byte) 55, 12000); // Emergency Supply Box.
				sp(701975, 582.56866f, 396.15695f, 603.4048f, (byte) 2, 12000); // Emergency Supply Box.
			}
		}, 600000));
		ophidanTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				sendPacket(false);
				jormungandReward.sendPacket(4, null);
				switch (Rnd.get(1, 2)) {
					case 1:
						sendMsg("<Captain Avran> appear near <Northern Approach Post>");
						sp(233491, 531.4785f, 434.995f, 620.25f, (byte) 16, 0); // Captain Avran.
						break;
					case 2:
						sendMsg("<Captain Avran> appear near <Southern Approach Post>");
						sp(233491, 612.2417f, 552.89136f, 590.625f, (byte) 114, 0); // Captain Avran.
						break;
				}
			}
		}, 900000));
		ophidanTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				stopInstance(jormungandReward.getWinningRaceByScore());
			}
		}, 1800000));
	}

	protected void stopInstance(Race race) {
		stopInstanceTask();
		jormungandReward.setWinningRace(race);
		jormungandReward.setInstanceScoreType(InstanceScoreType.END_PROGRESS);
		reward();
		jormungandReward.sendPacket(5, null);
	}

	@Override
	public void onEnterInstance(final Player player) {
		if (!containPlayer(player.getObjectId())) {
			jormungandReward.regPlayerReward(player);
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
				PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(7, getTime(), jormungandReward, instance.getPlayersInside()));
				PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(3, getTime(), jormungandReward, player.getObjectId(), 0, 0));
				PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(7, getTime(), jormungandReward, instance.getPlayersInside()));
				PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(11, getTime(), getInstanceReward(), player.getObjectId()));
			}
		});
	}

	private void sendPacket(boolean isObjects) {
		if (isObjects) {
			instance.doOnAllPlayers(new Visitor<Player>() {

				@Override
				public void visit(Player player) {
					PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(6, getTime(), jormungandReward, instance.getPlayersInside()));
				}
			});
		}
		else {
			instance.doOnAllPlayers(new Visitor<Player>() {

				@Override
				public void visit(Player player) {
					PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(7, getTime(), jormungandReward, instance.getPlayersInside()));
				}
			});
		}
	}

	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
		jormungandReward = new JormungandReward(mapId, instanceId, instance);
		jormungandReward.setInstanceScoreType(InstanceScoreType.PREPARING);
		doors = instance.getDoors();
		startInstanceTask();
	}

	protected void reward() {
		for (Player player : instance.getPlayersInside()) {
			if (CreatureActions.isAlreadyDead(player)) {
				PlayerReviveService.duelRevive(player);
			}
			JormungandPlayerReward playerReward = jormungandReward.getPlayerReward(player.getObjectId());
			float abyssPoint = (1.0f * 0.5f) * 100;
			float gloryPoint = (1.0f * 0.5f) * 100;
			playerReward.setBonusAp((int) abyssPoint);
			playerReward.setBonusGp((int) gloryPoint);
			if (player.getRace().equals(jormungandReward.getWinningRace())) {
				abyssPoint += jormungandReward.getWinnerPoints();
				gloryPoint += jormungandReward.getWinnerPoints();
				playerReward.setRewardAp(jormungandReward.getWinnerPoints());
				playerReward.setRewardGp(jormungandReward.getWinnerPoints());
			}
			else {
				abyssPoint += jormungandReward.getLooserPoints();
				gloryPoint += jormungandReward.getLooserPoints();
				playerReward.setRewardAp(jormungandReward.getLooserPoints());
				playerReward.setRewardGp(jormungandReward.getLooserPoints());
			}
			playerReward.setOphidanVictoryBox(188052681);
			playerReward.setBloodMark(186000236);
			playerReward.setRewardCount(player.getRates().getJormungandRewardRate());
			ItemService.addItem(player, 188052681, (long) player.getRates().getJormungandRewardRate());
			ItemService.addItem(player, 186000236, (long) player.getRates().getJormungandRewardRate());
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
		jormungandReward.portToPosition(player);
		return true;
	}

	@Override
	public boolean onDie(Player player, Creature lastAttacker) {
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);
		PacketSendUtility.sendPacket(player, new SM_DIE(player.haveSelfRezEffect(), false, 0, 8));
		int points = 60;
		if (lastAttacker instanceof Player) {
			if (lastAttacker.getRace() != player.getRace()) {
				InstancePlayerReward playerReward = jormungandReward.getPlayerReward(player.getObjectId());
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
		return jormungandReward.getPointsByRace(race);
	}

	private void addPointsByRace(Race race, int points) {
		jormungandReward.addPointsByRace(race, points);
	}

	private void addPvpKillsByRace(Race race, int points) {
		jormungandReward.addPvpKillsByRace(race, points);
	}

	private void addPointToPlayer(Player player, int points) {
		jormungandReward.getPlayerReward(player.getObjectId()).addPoints(points);
	}

	private void addPvPKillToPlayer(Player player) {
		jormungandReward.getPlayerReward(player.getObjectId()).addPvPKillToPlayer();
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
		jormungandReward.sendPacket(11, player.getObjectId());
		if (jormungandReward.hasCapPoints()) {
			stopInstance(jormungandReward.getWinningRaceByScore());
		}
	}

	@Override
	public void onEnterZone(Player player, ZoneInstance zone) {
		if (zone.getAreaTemplate().getZoneName() == ZoneName.get("DEFENCE_POST_1_301210000")) {
			powerGenerator = 1;
		}
		else if (zone.getAreaTemplate().getZoneName() == ZoneName.get("NORTH_GUARD_POST_1_301210000")) {
			powerGenerator = 2;
		}
		else if (zone.getAreaTemplate().getZoneName() == ZoneName.get("SOUTH_GUARD_POST_1_301210000")) {
			powerGenerator = 3;
		}
		else if (zone.getAreaTemplate().getZoneName() == ZoneName.get("GUARD_POST_1_301210000")) {
			powerGenerator = 4;
		}
	}

	@Override
	public void onDie(Npc npc) {
		Player mostPlayerDamage = npc.getAggroList().getMostPlayerDamage();
		if (mostPlayerDamage == null) {
			return;
		}
		Race race = mostPlayerDamage.getRace();
		int point = 0;
		switch (npc.getObjectTemplate().getTemplateId()) {
			case 233856: // Beritra Barricade.
				despawnNpc(npc);
				sendMsgByRace(1401948, Race.PC_ALL, 0);
				break;
			case 233473: // Beritra's Sentinel.
				point = 100;
				despawnNpc(npc);
				break;
			case 233474: // Defense Post Magus.
			case 233475: // Defense Post Combatant.
			case 233476: // Defense Post Scout.
			case 233478: // Northern Approach Post Magus.
			case 233479: // Northern Approach Post Combatant.
			case 233481: // Southern Approach Post Combatant.
			case 233482: // Southern Approach Post Scout.
			case 233484: // Guard Post Magus.
			case 233485: // Guard Post Combatant.
			case 233486: // Guard Post Scout.
				point = 200;
				despawnNpc(npc);
				break;
			case 233477: // Defense Post Rearguard.
			case 233480: // Northern Approach Post Magician.
			case 233483: // Southern Approach Post Assaulter.
			case 233487: // Guard Post Rearguard.
				point = 300;
				despawnNpc(npc);
				break;
			case 233846: // Templar Rearguard.
			case 233847: // Cleric Rearguard.
			case 233848: // Sorcerer Rearguard.
			case 233849: // Templar Rearguard.
			case 233850: // Cleric Rearguard.
			case 233851: // Sorcerer Rearguard.
				point = 1500;
				despawnNpc(npc);
				break;
			case 233491: // Captain Avran.
				point = 5000;
				despawnNpc(npc);
				break;
			case 701943: // Elyos Power Generator.
				despawnNpc(npc);
				point = 5000;
				if (powerGenerator == 1) {
					if (race.equals(Race.ASMODIANS)) {
						deleteNpc(802033);
						deleteNpc(701969);
						sendMsgByRace(1401991, Race.PC_ALL, 0);
						sp(802034, 667.11389f, 474.22995f, 600.48346f, (byte) 0, 0); // Chokepoint Defense Post Flag.
						sp(701944, 667.11389f, 474.22995f, 600.48346f, (byte) 0, 173); // Asmodians Power Generator.
						sp(701969, 762.6721f, 544.30493f, 577.7007f, (byte) 91, 0); // Chokepoint Defense Post Mortar.
						sp(233849, 672.62286f, 467.2902f, 599.53894f, (byte) 107, 0); // Templar Rearguard.
						sp(233849, 663.40594f, 483.60574f, 599.7871f, (byte) 37, 0); // Templar Rearguard.
						sp(233850, 660.30194f, 466.5498f, 599.8218f, (byte) 77, 0); // Cleric Rearguard.
						sp(233851, 674.94977f, 478.36877f, 599.5594f, (byte) 8, 0); // Sorcerer Rearguard.
					}
				}
				else if (powerGenerator == 2) {
					if (race.equals(Race.ASMODIANS)) {
						deleteNpc(802036);
						deleteNpc(701970);
						sendMsgByRace(1401992, Race.PC_ALL, 0);
						sp(802037, 524.84589f, 427.63959f, 621.21320f, (byte) 0, 0); // Northern Approach Post Flag.
						sp(701944, 524.84589f, 427.63959f, 621.21320f, (byte) 0, 174); // Asmodians Power Generator.
						sp(701970, 760.40955f, 544.2923f, 577.7035f, (byte) 90, 0); // Northern Approach Post Mortar.
						sp(233849, 519.06854f, 434.295f, 620.125f, (byte) 45, 0); // Templar Rearguard.
						sp(233850, 533.65063f, 428.35898f, 620.25f, (byte) 5, 0); // Cleric Rearguard.
						sp(233851, 525.0882f, 436.3445f, 620.25f, (byte) 27, 0); // Sorcerer Rearguard.
					}
				}
				else if (powerGenerator == 3) {
					if (race.equals(Race.ASMODIANS)) {
						deleteNpc(802039);
						deleteNpc(701971);
						sendMsgByRace(1401993, Race.PC_ALL, 0);
						sp(802040, 602.73395f, 556.29407f, 591.52533f, (byte) 0, 0); // Southern Approach Post Flag.
						sp(701944, 602.73395f, 556.29407f, 591.52533f, (byte) 0, 166); // Asmodians Power Generator.
						sp(701971, 750.75836f, 545.71686f, 577.7213f, (byte) 84, 0); // Southern Approach Post Mortar.
						sp(233849, 610.57794f, 559.381f, 590.625f, (byte) 5, 0); // Templar Rearguard.
						sp(233850, 593.646f, 556.11426f, 590.5221f, (byte) 58, 0); // Cleric Rearguard.
						sp(233851, 607.1519f, 548.563f, 590.5f, (byte) 103, 0); // Sorcerer Rearguard.
					}
				}
				else if (powerGenerator == 4) {
					if (race.equals(Race.ASMODIANS)) {
						deleteNpc(802042);
						deleteNpc(701972);
						sendMsgByRace(1401994, Race.PC_ALL, 0);
						sp(802043, 492.81982f, 536.56732f, 598.24933f, (byte) 0, 0); // Bridgewatch Post Flag.
						sp(701944, 492.81982f, 536.56732f, 598.24933f, (byte) 0, 170); // Asmodians Power Generator.
						sp(701972, 748.57916f, 546.3481f, 577.72815f, (byte) 84, 0); // Bridgewatch Post Mortar.
						sp(233849, 483.3327f, 538.1493f, 597.5f, (byte) 58, 0); // Templar Rearguard.
						sp(233849, 498.373f, 543.4837f, 597.5f, (byte) 9, 0); // Templar Rearguard.
						sp(233850, 500.20483f, 532.2458f, 597.5f, (byte) 116, 0); // Cleric Rearguard.
						sp(233851, 484.61765f, 531.6245f, 597.375f, (byte) 70, 0); // Sorcerer Rearguard.
					}
				}
				break;
			case 701944: // Asmodians Power Generator.
				point = 5000;
				despawnNpc(npc);
				if (powerGenerator == 1) {
					if (race.equals(Race.ELYOS)) {
						deleteNpc(802034);
						deleteNpc(701969);
						sendMsgByRace(1401961, Race.PC_ALL, 0);
						sp(802033, 667.11389f, 474.22995f, 600.48346f, (byte) 0, 0); // Chokepoint Defense Post Flag.
						sp(701943, 667.11389f, 474.22995f, 600.48346f, (byte) 0, 172); // Elyos Power Generator.
						sp(701969, 337.6665f, 498.31458f, 597.0435f, (byte) 3, 0); // Chokepoint Defense Post Mortar.
						sp(233846, 672.62286f, 467.2902f, 599.53894f, (byte) 107, 0); // Templar Rearguard.
						sp(233846, 663.40594f, 483.60574f, 599.7871f, (byte) 37, 0); // Templar Rearguard.
						sp(233847, 660.30194f, 466.5498f, 599.8218f, (byte) 77, 0); // Cleric Rearguard.
						sp(233848, 674.94977f, 478.36877f, 599.5594f, (byte) 8, 0); // Sorcerer Rearguard.
					}
				}
				else if (powerGenerator == 2) {
					if (race.equals(Race.ELYOS)) {
						deleteNpc(802037);
						deleteNpc(701970);
						sendMsgByRace(1401962, Race.PC_ALL, 0);
						sp(802036, 524.84589f, 427.63959f, 621.21320f, (byte) 0, 0); // Northern Approach Post Flag.
						sp(701943, 524.84589f, 427.63959f, 621.21320f, (byte) 0, 175); // Elyos Power Generator.
						sp(701970, 338.08813f, 496.11847f, 597.04626f, (byte) 3, 0); // Northern Approach Post Mortar.
						sp(233846, 519.06854f, 434.295f, 620.125f, (byte) 45, 0); // Templar Rearguard.
						sp(233847, 533.65063f, 428.35898f, 620.25f, (byte) 5, 0); // Cleric Rearguard.
						sp(233848, 525.0882f, 436.3445f, 620.25f, (byte) 27, 0); // Sorcerer Rearguard.
					}
				}
				else if (powerGenerator == 3) {
					if (race.equals(Race.ELYOS)) {
						deleteNpc(802040);
						deleteNpc(701971);
						sendMsgByRace(1401963, Race.PC_ALL, 0);
						sp(802039, 602.73395f, 556.29407f, 591.52533f, (byte) 0, 0); // Southern Approach Post Flag.
						sp(701943, 602.73395f, 556.29407f, 591.52533f, (byte) 0, 169); // Elyos Power Generator.
						sp(701971, 338.6412f, 486.42004f, 597.0637f, (byte) 118, 0); // Southern Approach Post Mortar.
						sp(233846, 610.57794f, 559.381f, 590.625f, (byte) 5, 0); // Templar Rearguard.
						sp(233847, 593.646f, 556.11426f, 590.5221f, (byte) 58, 0); // Cleric Rearguard.
						sp(233848, 607.1519f, 548.563f, 590.5f, (byte) 103, 0); // Sorcerer Rearguard.
					}
				}
				else if (powerGenerator == 4) {
					if (race.equals(Race.ELYOS)) {
						deleteNpc(802043);
						deleteNpc(701972);
						sendMsgByRace(1401964, Race.PC_ALL, 0);
						sp(802042, 492.81982f, 536.56732f, 598.24933f, (byte) 0, 0); // Bridgewatch Post Flag.
						sp(701943, 492.81982f, 536.56732f, 598.24933f, (byte) 0, 171); // Elyos Power Generator.
						sp(701972, 338.46423f, 484.23608f, 597.07074f, (byte) 118, 0); // Bridgewatch Post Mortar.
						sp(233846, 483.3327f, 538.1493f, 597.5f, (byte) 58, 0); // Templar Rearguard.
						sp(233846, 498.373f, 543.4837f, 597.5f, (byte) 9, 0); // Templar Rearguard.
						sp(233847, 500.20483f, 532.2458f, 597.5f, (byte) 116, 0); // Cleric Rearguard.
						sp(233848, 484.61765f, 531.6245f, 597.375f, (byte) 70, 0); // Sorcerer Rearguard.
					}
				}
				break;
			case 701945: // Balaur Power Generator.
				point = 5000;
				despawnNpc(npc);
				if (powerGenerator == 1) {
					if (race.equals(Race.ELYOS)) {
						deleteNpc(802035);
						sendMsgByRace(1401961, Race.PC_ALL, 0);
						sp(802033, 667.11389f, 474.22995f, 600.48346f, (byte) 0, 0); // Chokepoint Defense Post Flag.
						sp(701943, 667.11389f, 474.22995f, 600.48346f, (byte) 0, 172); // Elyos Power Generator.
						sp(701969, 337.6665f, 498.31458f, 597.0435f, (byte) 3, 0); // Chokepoint Defense Post Mortar.
						sp(233846, 672.62286f, 467.2902f, 599.53894f, (byte) 107, 0); // Templar Rearguard.
						sp(233846, 663.40594f, 483.60574f, 599.7871f, (byte) 37, 0); // Templar Rearguard.
						sp(233847, 660.30194f, 466.5498f, 599.8218f, (byte) 77, 0); // Cleric Rearguard.
						sp(233848, 674.94977f, 478.36877f, 599.5594f, (byte) 8, 0); // Sorcerer Rearguard.
					}
					else if (race.equals(Race.ASMODIANS)) {
						deleteNpc(802035);
						sendMsgByRace(1401991, Race.PC_ALL, 0);
						sp(802034, 667.11389f, 474.22995f, 600.48346f, (byte) 0, 0); // Chokepoint Defense Post Flag.
						sp(701944, 667.11389f, 474.22995f, 600.48346f, (byte) 0, 173); // Asmodians Power Generator.
						sp(701969, 762.6721f, 544.30493f, 577.7007f, (byte) 91, 0); // Chokepoint Defense Post Mortar.
						sp(233849, 672.62286f, 467.2902f, 599.53894f, (byte) 107, 0); // Templar Rearguard.
						sp(233849, 663.40594f, 483.60574f, 599.7871f, (byte) 37, 0); // Templar Rearguard.
						sp(233850, 660.30194f, 466.5498f, 599.8218f, (byte) 77, 0); // Cleric Rearguard.
						sp(233851, 674.94977f, 478.36877f, 599.5594f, (byte) 8, 0); // Sorcerer Rearguard.
					}
				}
				else if (powerGenerator == 2) {
					if (race.equals(Race.ELYOS)) {
						deleteNpc(802038);
						sendMsgByRace(1401962, Race.PC_ALL, 0);
						sp(802036, 524.84589f, 427.63959f, 621.21320f, (byte) 0, 0); // Northern Approach Post Flag.
						sp(701943, 524.84589f, 427.63959f, 621.21320f, (byte) 0, 175); // Elyos Power Generator.
						sp(701970, 338.08813f, 496.11847f, 597.04626f, (byte) 3, 0); // Northern Approach Post Mortar.
						sp(233846, 519.06854f, 434.295f, 620.125f, (byte) 45, 0); // Templar Rearguard.
						sp(233847, 533.65063f, 428.35898f, 620.25f, (byte) 5, 0); // Cleric Rearguard.
						sp(233848, 525.0882f, 436.3445f, 620.25f, (byte) 27, 0); // Sorcerer Rearguard.
					}
					else if (race.equals(Race.ASMODIANS)) {
						deleteNpc(802038);
						sendMsgByRace(1401992, Race.PC_ALL, 0);
						sp(802037, 524.84589f, 427.63959f, 621.21320f, (byte) 0, 0); // Northern Approach Post Flag.
						sp(701944, 524.84589f, 427.63959f, 621.21320f, (byte) 0, 174); // Asmodians Power Generator.
						sp(701970, 760.40955f, 544.2923f, 577.7035f, (byte) 90, 0); // Northern Approach Post Mortar.
						sp(233849, 519.06854f, 434.295f, 620.125f, (byte) 45, 0); // Templar Rearguard.
						sp(233850, 533.65063f, 428.35898f, 620.25f, (byte) 5, 0); // Cleric Rearguard.
						sp(233851, 525.0882f, 436.3445f, 620.25f, (byte) 27, 0); // Sorcerer Rearguard.
					}
				}
				else if (powerGenerator == 3) {
					if (race.equals(Race.ELYOS)) {
						deleteNpc(802041);
						sendMsgByRace(1401963, Race.PC_ALL, 0);
						sp(802039, 602.73395f, 556.29407f, 591.52533f, (byte) 0, 0); // Southern Approach Post Flag.
						sp(701943, 602.73395f, 556.29407f, 591.52533f, (byte) 0, 169); // Elyos Power Generator.
						sp(701971, 338.6412f, 486.42004f, 597.0637f, (byte) 118, 0); // Southern Approach Post Mortar.
						sp(233846, 610.57794f, 559.381f, 590.625f, (byte) 5, 0); // Templar Rearguard.
						sp(233847, 593.646f, 556.11426f, 590.5221f, (byte) 58, 0); // Cleric Rearguard.
						sp(233848, 607.1519f, 548.563f, 590.5f, (byte) 103, 0); // Sorcerer Rearguard.
					}
					else if (race.equals(Race.ASMODIANS)) {
						deleteNpc(802041);
						sendMsgByRace(1401993, Race.PC_ALL, 0);
						sp(802040, 602.73395f, 556.29407f, 591.52533f, (byte) 0, 0); // Southern Approach Post Flag.
						sp(701944, 602.73395f, 556.29407f, 591.52533f, (byte) 0, 166); // Asmodians Power Generator.
						sp(701971, 750.75836f, 545.71686f, 577.7213f, (byte) 84, 0); // Southern Approach Post Mortar.
						sp(233849, 610.57794f, 559.381f, 590.625f, (byte) 5, 0); // Templar Rearguard.
						sp(233850, 593.646f, 556.11426f, 590.5221f, (byte) 58, 0); // Cleric Rearguard.
						sp(233851, 607.1519f, 548.563f, 590.5f, (byte) 103, 0); // Sorcerer Rearguard.
					}
				}
				else if (powerGenerator == 4) {
					if (race.equals(Race.ELYOS)) {
						deleteNpc(802044);
						sendMsgByRace(1401964, Race.PC_ALL, 0);
						sp(802042, 492.81982f, 536.56732f, 598.24933f, (byte) 0, 0); // Bridgewatch Post Flag.
						sp(701943, 492.81982f, 536.56732f, 598.24933f, (byte) 0, 171); // Elyos Power Generator.
						sp(701972, 338.46423f, 484.23608f, 597.07074f, (byte) 118, 0); // Bridgewatch Post Mortar.
						sp(233846, 483.3327f, 538.1493f, 597.5f, (byte) 58, 0); // Templar Rearguard.
						sp(233846, 498.373f, 543.4837f, 597.5f, (byte) 9, 0); // Templar Rearguard.
						sp(233847, 500.20483f, 532.2458f, 597.5f, (byte) 116, 0); // Cleric Rearguard.
						sp(233848, 484.61765f, 531.6245f, 597.375f, (byte) 70, 0); // Sorcerer Rearguard.
					}
					else if (race.equals(Race.ASMODIANS)) {
						deleteNpc(802044);
						sendMsgByRace(1401994, Race.PC_ALL, 0);
						sp(802043, 492.81982f, 536.56732f, 598.24933f, (byte) 0, 0); // Bridgewatch Post Flag.
						sp(701944, 492.81982f, 536.56732f, 598.24933f, (byte) 0, 170); // Asmodians Power Generator.
						sp(701972, 748.57916f, 546.3481f, 577.72815f, (byte) 84, 0); // Bridgewatch Post Mortar.
						sp(233849, 483.3327f, 538.1493f, 597.5f, (byte) 58, 0); // Templar Rearguard.
						sp(233849, 498.373f, 543.4837f, 597.5f, (byte) 9, 0); // Templar Rearguard.
						sp(233850, 500.20483f, 532.2458f, 597.5f, (byte) 116, 0); // Cleric Rearguard.
						sp(233851, 484.61765f, 531.6245f, 597.375f, (byte) 70, 0); // Sorcerer Rearguard.
					}
				}
				break;
		}
		updateScore(mostPlayerDamage, npc, point, false);
	}

	@Override
	public void handleUseItemFinish(Player player, Npc npc) {
		switch (npc.getNpcId()) {
			case 701947: // Elyos Field Gun.
			case 701949: // Elyos Field Gun.
				if (player.getInventory().decreaseByItemId(164000277, 1)) {
				}
				break;
			case 701948: // Asmodians Field Gun.
			case 701950: // Asmodians Field Gun.
				if (player.getInventory().decreaseByItemId(164000277, 1)) {
				}
				break;
			case 701969: // Chokepoint Defense Post Mortar.
				if (player.getInventory().decreaseByItemId(164000278, 1)) {
					spawn(701723, 663.4942f, 483.9678f, 599.7816f, (byte) 39);
					spawn(701723, 675.484f, 478.4795f, 599.5928f, (byte) 9);
					spawn(701723, 672.83636f, 466.89484f, 599.55225f, (byte) 108);
					spawn(701723, 660.0421f, 466.35815f, 599.85f, (byte) 78);
					spawn(701723, 676.948f, 472.36368f, 599.625f, (byte) 117);
				}
				despawnNpc(npc);
				break;
			case 701970: // Northern Approach Post Mortar.
				if (player.getInventory().decreaseByItemId(164000278, 1)) {
					spawn(701723, 525.23694f, 437.11435f, 620.25f, (byte) 26);
					spawn(701723, 518.671f, 434.8366f, 620.125f, (byte) 47);
					spawn(701723, 531.77075f, 435.03348f, 620.25f, (byte) 17);
					spawn(701723, 533.9718f, 428.34567f, 620.25f, (byte) 6);
				}
				despawnNpc(npc);
				break;
			case 701971: // Southern Approach Post Mortar.
				if (player.getInventory().decreaseByItemId(164000278, 1)) {
					spawn(701723, 611.7534f, 559.57806f, 590.625f, (byte) 2);
					spawn(701723, 612.5473f, 552.83856f, 590.625f, (byte) 114);
					spawn(701723, 607.31134f, 548.25256f, 590.5f, (byte) 104);
					spawn(701723, 592.66534f, 556.22736f, 590.58344f, (byte) 65);
					spawn(701723, 600.7587f, 547.3253f, 590.5f, (byte) 85);
				}
				despawnNpc(npc);
				break;
			case 701972: // Bridgewatch Post Mortar.
				if (player.getInventory().decreaseByItemId(164000278, 1)) {
					spawn(701723, 499.52f, 544.5333f, 597.5f, (byte) 16);
					spawn(701723, 501.48218f, 531.45575f, 597.5f, (byte) 115);
					spawn(701723, 483.88568f, 531.17413f, 597.375f, (byte) 70);
					spawn(701723, 482.55066f, 538.5496f, 597.5f, (byte) 54);
				}
				despawnNpc(npc);
				break;
		}
	}

	private void despawnNpc(Npc npc) {
		if (npc != null) {
			npc.getController().onDelete();
		}
	}

	private void deleteNpc(int npcId) {
		if (getNpc(npcId) != null) {
			getNpc(npcId).getController().onDelete();
		}
	}

	@Override
	public void onInstanceDestroy() {
		stopInstanceTask();
		isInstanceDestroyed = true;
		jormungandReward.clear();
		doors.clear();
	}

	protected void openFirstDoors() {
		openDoor(176);
		openDoor(177);
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
		ophidanTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {

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
		ophidanTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {

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
		ophidanTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {

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

	private void sendMsg(final String str) {
		instance.doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				PacketSendUtility.sendMessage(player, str);
			}
		});
	}

	private void stopInstanceTask() {
		for (FastList.Node<Future<?>> n = ophidanTask.head(), end = ophidanTask.tail(); (n = n.getNext()) != end;) {
			if (n.getValue() != null) {
				n.getValue().cancel(true);
			}
		}
	}

	@Override
	public InstanceReward<?> getInstanceReward() {
		return jormungandReward;
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
		jormungandReward.sendPacket(10, player.getObjectId());
	}
}
