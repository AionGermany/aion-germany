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
package com.aionemu.gameserver.network.aion.serverpackets;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.instance.InstanceScoreType;
import com.aionemu.gameserver.model.instance.instancereward.ArgentManorReward;
import com.aionemu.gameserver.model.instance.instancereward.BalaurMarchingRouteReward;
import com.aionemu.gameserver.model.instance.instancereward.DarkPoetaReward;
import com.aionemu.gameserver.model.instance.instancereward.DredgionReward;
import com.aionemu.gameserver.model.instance.instancereward.FireTempleOfMemoryReward;
import com.aionemu.gameserver.model.instance.instancereward.HarmonyArenaReward;
import com.aionemu.gameserver.model.instance.instancereward.InstanceReward;
import com.aionemu.gameserver.model.instance.instancereward.JormungandReward;
import com.aionemu.gameserver.model.instance.instancereward.KamarBattlefieldReward;
import com.aionemu.gameserver.model.instance.instancereward.MechanerksWeaponsFactoryReward;
import com.aionemu.gameserver.model.instance.instancereward.NeviwindCanyonReward;
import com.aionemu.gameserver.model.instance.instancereward.PandaemoniumBattlefieldReward;
import com.aionemu.gameserver.model.instance.instancereward.PvPArenaReward;
import com.aionemu.gameserver.model.instance.instancereward.RiftOfOblivionReward;
import com.aionemu.gameserver.model.instance.instancereward.RunatoriumReward;
import com.aionemu.gameserver.model.instance.instancereward.SanctumBattlefieldReward;
import com.aionemu.gameserver.model.instance.instancereward.SealedHallOfKnowledgeReward;
import com.aionemu.gameserver.model.instance.instancereward.ShugoEmperorVaultReward;
import com.aionemu.gameserver.model.instance.instancereward.SteelWallBastionBattlefieldReward;
import com.aionemu.gameserver.model.instance.instancereward.SteelWallBastionReward;
import com.aionemu.gameserver.model.instance.playerreward.BalaurMarchingRoutePlayerReward;
import com.aionemu.gameserver.model.instance.playerreward.CruciblePlayerReward;
import com.aionemu.gameserver.model.instance.playerreward.DredgionPlayerReward;
import com.aionemu.gameserver.model.instance.playerreward.HarmonyGroupReward;
import com.aionemu.gameserver.model.instance.playerreward.InstancePlayerReward;
import com.aionemu.gameserver.model.instance.playerreward.JormungandPlayerReward;
import com.aionemu.gameserver.model.instance.playerreward.KamarBattlefieldPlayerReward;
import com.aionemu.gameserver.model.instance.playerreward.NeviwindCanyonPlayerReward;
import com.aionemu.gameserver.model.instance.playerreward.PvPArenaPlayerReward;
import com.aionemu.gameserver.model.instance.playerreward.RunatoriumPlayerReward;
import com.aionemu.gameserver.model.instance.playerreward.SteelWallBastionBattlefieldPlayerReward;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

import javolution.util.FastList;

/**
 * @author Dns, ginho1, nrg, xTz
 * @author GiGatR00n v4.7.5.x
 * @Reworked Eloann
 */
@SuppressWarnings("rawtypes")
public class SM_INSTANCE_SCORE extends AionServerPacket {

	private int type;
	private int mapId;
	private int instanceTime;
	private InstanceScoreType instanceScoreType;
	private InstanceReward instanceReward;
	private List<Player> players;
	private Integer object;
	private int PlayerStatus = 0;// Spawn(0) - Dead(60)
	private int PlayerRaceId = 0;// Elyos(0) - Asmo(1)
	private static final Logger log = LoggerFactory.getLogger(SM_INSTANCE_SCORE.class);

	// Type: 3, 4
	public SM_INSTANCE_SCORE(int type, int instanceTime, InstanceReward instanceReward, Integer object, int PlayerStatus, int PlayerRaceId) {
		this.mapId = instanceReward.getMapId();
		this.type = type;
		this.instanceTime = instanceTime;
		this.instanceReward = instanceReward;
		this.object = object;
		this.PlayerStatus = PlayerStatus;
		this.PlayerRaceId = PlayerRaceId;
		instanceScoreType = instanceReward.getInstanceScoreType();
	}

	// Type: 5, 8, 10, 11
	public SM_INSTANCE_SCORE(int type, int instanceTime, InstanceReward instanceReward, Integer object) {
		this.mapId = instanceReward.getMapId();
		this.type = type;
		this.instanceTime = instanceTime;
		this.instanceReward = instanceReward;
		this.object = object;
		instanceScoreType = instanceReward.getInstanceScoreType();
	}

	public SM_INSTANCE_SCORE(int instanceTime, InstanceReward instanceReward, List<Player> players) {
		this.mapId = instanceReward.getMapId();
		this.instanceTime = instanceTime;
		this.instanceReward = instanceReward;
		this.players = players;
		instanceScoreType = instanceReward.getInstanceScoreType();
	}

	// Type: 6, 7
	public SM_INSTANCE_SCORE(int type, int instanceTime, InstanceReward instanceReward, List<Player> players) {
		this.mapId = instanceReward.getMapId();
		this.type = type;
		this.instanceTime = instanceTime;
		this.instanceReward = instanceReward;
		this.players = players;
		instanceScoreType = instanceReward.getInstanceScoreType();
	}

	public SM_INSTANCE_SCORE(InstanceReward instanceReward, InstanceScoreType instanceScoreType) {
		this.mapId = instanceReward.getMapId();
		this.instanceReward = instanceReward;
		this.instanceScoreType = instanceScoreType;
	}

	public SM_INSTANCE_SCORE(InstanceReward instanceReward) {
		this.mapId = instanceReward.getMapId();
		this.instanceReward = instanceReward;
		this.instanceScoreType = instanceReward.getInstanceScoreType();
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	protected void writeImpl(AionConnection con) {
		int playerCount = 0;
		Player owner = con.getActivePlayer();
		Integer ownerObject = owner.getObjectId();
		writeD(mapId);
		writeD(instanceTime);
		writeD(instanceScoreType.getId());
		switch (mapId) {
			case 300450000: // Arena of Cooperation
			case 300570000: // Training Camp of Cooperation
			case 301100000: // Training Arena of Unity
				HarmonyArenaReward harmonyArena = (HarmonyArenaReward) instanceReward;
				if (object == null) {
					object = ownerObject;
				}
				HarmonyGroupReward harmonyGroupReward = harmonyArena.getHarmonyGroupReward(object);
				writeC(type);
				switch (type) {
					case 2:
						writeD(0);
						writeD(harmonyArena.getRound());
						break;
					case 3:
						writeD(harmonyGroupReward.getOwner() - 1);
						writeS(harmonyGroupReward.getAGPlayer(object).getName(), 52);
						writeD(harmonyGroupReward.getOwner());
						writeD(object);
						break;
					case 4:
						writeD(harmonyArena.getPlayerReward(object).getRemaningTime());
						writeD(0);
						writeD(0);
						writeD(object);
						break;
					case 5: // Reward Type
						writeD(harmonyGroupReward.getBasicAP());
						writeD(harmonyGroupReward.getBasicGP());
						writeD(harmonyGroupReward.getScoreAP());
						writeD(harmonyGroupReward.getScoreGP());
						writeD(harmonyGroupReward.getRankingAP());
						writeD(harmonyGroupReward.getRankingGP());
						writeD(186000137);
						writeD(harmonyGroupReward.getBasicCourage());
						writeD(harmonyGroupReward.getScoreCourage());
						writeD(harmonyGroupReward.getRankingCourage());
						if (harmonyGroupReward.getGloryTicket() != 0) {
							writeD(186000185);
							writeD(harmonyGroupReward.getGloryTicket());
						}
						else {
							writeD(0);
							writeD(0);
						}
						writeD(0);
						writeD(0);
						writeD(0);
						writeD(0);
						writeD(0);
						writeD(0);
						writeD((int) harmonyGroupReward.getParticipation() * 100);
						writeD(harmonyGroupReward.getPoints());
						break;
					case 6:
						writeD(3);
						writeD(harmonyArena.getCapPoints());
						writeD(3);
						writeD(1);
						writeD(harmonyArena.getBuffId());
						writeD(2);
						writeD(0);
						writeD(harmonyArena.getRound());
						FastList<HarmonyGroupReward> groups = harmonyArena.getHarmonyGroupInside();
						writeC(groups.size());
						for (HarmonyGroupReward group : groups) {
							writeC(harmonyArena.getRank(group.getPoints()));
							writeD(group.getPvPKills());
							writeD(group.getPoints());
							writeD(group.getOwner());
							FastList<Player> members = harmonyArena.getPlayersInside(group);
							writeC(members.size());
							int i = 0;
							for (Player p : members) {
								PvPArenaPlayerReward rewardedPlayer = harmonyArena.getPlayerReward(p.getObjectId());
								writeD(0);
								writeD(rewardedPlayer.getRemaningTime());
								writeD(0);
								writeC(group.getOwner() - 1);
								writeC(i);
								writeH(0);
								writeS(p.getName(), 52);
								writeD(p.getObjectId());
								i++;
							}
						}
						break;
					case 10:
						writeC(harmonyArena.getRank(harmonyGroupReward.getPoints()));
						writeD(harmonyGroupReward.getPvPKills());
						writeD(harmonyGroupReward.getPoints());
						writeD(harmonyGroupReward.getOwner());
						break;
				}
				break;
			case 300110000: // Baranath Dredgion.
			case 300210000: // Chantra Dredgion.
			case 300440000: // Terath Dredgion
			case 301650000: // Ashunatal Dredgion
				fillTableWithGroup(Race.ELYOS);
				fillTableWithGroup(Race.ASMODIANS);
				DredgionReward dredgionReward = (DredgionReward) instanceReward;
				int elyosScore = dredgionReward.getPointsByRace(Race.ELYOS).intValue();
				int asmosScore = dredgionReward.getPointsByRace(Race.ASMODIANS).intValue();
				writeD(instanceScoreType.isEndProgress() ? (asmosScore > elyosScore ? 1 : 0) : 255);
				writeD(elyosScore);
				writeD(asmosScore);
				writeH(0);
				for (DredgionReward.DredgionRooms dredgionRoom : dredgionReward.getDredgionRooms()) {
					writeC(dredgionRoom.getState());
				}
				break;
			case 301120000: // Kamar's Battlefield
				KamarBattlefieldReward kbr = (KamarBattlefieldReward) instanceReward;
				if (object == null) {
					object = ownerObject;
				}
				KamarBattlefieldPlayerReward kbpr = kbr.getPlayerReward(object);
				writeC(type);
				switch (type) {
					case 2:
						writeD(0);
						writeD(kbr.getTime());
						break;
					case 3:
						writeD(10);
						writeD(PlayerStatus);
						writeD(object);
						writeD(PlayerRaceId);
						break;
					case 4:
						writeD(10);
						writeD(PlayerStatus);
						writeD(object);
						break;
					case 5:
						writeD((int) kbpr.getParticipation()); // percent
						writeD(kbpr.getRewardAp());
						writeD(kbpr.getBonusAp());
						writeD(kbpr.getRewardGp());
						writeD(kbpr.getBonusGp());
						writeD(kbpr.getReward1()); // Kamar Victory Box
						writeQ(kbpr.getRewardCount());
						writeD(kbpr.getReward2()); // Blood Mark
						writeQ(kbpr.getRewardCount());
						writeD(kbpr.getBonusReward()); // bonus item
						writeD(kbpr.getRewardCount());
						writeD(kbpr.getBonusReward2()); // bonus item
						writeD(kbpr.getRewardCount());
						break;
					case 6:
						int counter = 0;
						writeD(100);// unk
						for (Player player : players) {
							writeD(10);
							writeD(0);
							writeD(player.getObjectId());
							counter++;
						}
						if (counter < 48) {
							writeB(new byte[12 * (48 - counter)]);
						}
						writeC(0);
						writeD(0);
						writeD(kbr.getPointsByRace(Race.ELYOS).intValue());
						writeD(0);
						writeD(65535);
						writeC(0);
						writeD(0);
						writeD(kbr.getPointsByRace(Race.ASMODIANS).intValue());
						writeD(1);
						writeD(65535);
						break;
					case 7:
						kamarTable(Race.ELYOS);
						kamarTable(Race.ASMODIANS);
						break;
					case 10:
						writeC(0);
						writeD(kbr.getPvpKillsByRace(kbpr.getRace()).intValue());
						writeD(kbr.getPointsByRace(kbpr.getRace()).intValue());
						writeD(kbpr.getRace().getRaceId());
						writeD(object);
						break;
					case 11:
						writeC(0);
						writeD(kbr.getPvpKillsByRace(kbpr.getRace()).intValue());
						writeD(kbr.getPointsByRace(kbpr.getRace()).intValue());
						writeD(kbpr.getRace().getRaceId());
						writeD(object);
						break;
				}
				break;
			case 301310000: // Runatorium
			case 301680000: // Runatorium Ruins
				RunatoriumReward rr = (RunatoriumReward) instanceReward;
				if (object == null) {
					object = ownerObject; // Player ObjectId
				}
				RunatoriumPlayerReward rpr = rr.getPlayerReward(object);
				writeC(type);
				switch (type) {
					case 2: // Send 1-Time when "Start_Progress" and "End_Progress"
						InstanceScoreType isc = rr.getInstanceScoreType();
						writeD(isc == InstanceScoreType.START_PROGRESS ? 0 : (isc == InstanceScoreType.END_PROGRESS) ? 2 : 0);
						break;
					case 3:
						writeD(15);
						writeD(PlayerStatus);// Spawn(0) - Dead(60)
						writeD(object);// PlayerObjectId
						writeD(PlayerRaceId); // 0=Elyos : 1=Asmo
						break;
					case 4:// Dead=60 - Spawn=0 (For Each <PLAYER> Died/Spawned) OnDie()|OnRevive()
						writeD(15);
						writeD(PlayerStatus);// Spawn(0) - Dead(60)
						writeD(object); // PlayerObjectId
						break;
					case 5: // Reward OnBossKilled()|OnTimeOut() (End-Instance Packet)
						writeD((int) rpr.getParticipation());
						writeD(rpr.getRewardAp());// ...........Abyss Points
						writeD(rpr.getBonusAp()); // ...........Bonus Abyss Points
						writeD(rpr.getRewardGp());// ...........Glory Points
						writeD(rpr.getBonusGp()); // ...........Bonus Glory Points
						writeD(rpr.getReward1()); // ...........Fragmented Ceramium
						writeQ(rpr.getReward1Count());
						writeD(rpr.getReward2()); // ...........Idgel Dome Reward Box
						writeQ(rpr.getReward2Count());
						writeD(rpr.getBonusReward());// ........bonus item
						writeD(rpr.getRewardCount());
						writeD(rpr.getBonusReward2());// .......bonus item
						writeD(rpr.getRewardCount());
						writeD(rpr.getAdditionalReward());// ...Additional Reward Box #1
						writeD(rpr.getAdditionalRewardCount());
						writeC(1);
						break;
					case 6: // Preparing Time & Starting Score/PvPKill (BothTeam)
						int counter = 0;
						writeD(100);// unk
						for (Player player : players) {
							if (player.getRace() != Race.ELYOS) {
								continue;
							}
							writeD(15);
							writeD(player.getLifeStats().isAlreadyDead() ? 60 : 0);
							writeD(player.getObjectId());
							counter++;
						}
						if (counter < 24) {
							writeB(new byte[12 * (24 - counter)]);
						}

						counter = 0;
						for (Player player : players) {
							if (player.getRace() != Race.ASMODIANS) {
								continue;
							}
							writeD(15);
							writeD(player.getLifeStats().isAlreadyDead() ? 60 : 0);
							writeD(player.getObjectId());
							counter++;
						}
						if (counter < 24) {
							writeB(new byte[12 * (24 - counter)]);
						}
						writeC(0);
						writeD(rr.getPvpKillsByRace(Race.ELYOS).intValue());// ......Elyos PvP Kills
						writeD(rr.getPointsByRace(Race.ELYOS).intValue());// ........Elyos Score
						writeD(0);
						writeD((rr.getInstanceScoreType() == InstanceScoreType.PREPARING ? 65535 : 1));
						writeC(0);
						writeD(rr.getPvpKillsByRace(Race.ASMODIANS).intValue());// ...Asmo PvP Kills
						writeD(rr.getPointsByRace(Race.ASMODIANS).intValue());// .....Asmo Score
						writeD(1);
						writeD((rr.getInstanceScoreType() == InstanceScoreType.PREPARING ? 65535 : 1));
						break;
					case 7: // Instance Players Info (PlayersName, ClassId, AbyssRank, PvPKills, Score, ...) (Who still in Instance)
						DomeTable(Race.ELYOS);
						DomeTable(Race.ASMODIANS);
						break;
					case 8:// if Player Disconnects or Leave Instance
						writeD(object); // PlayerObjectId
						break;
					case 10:
						writeC(0);
						writeD(rr.getPvpKillsByRace(rpr.getRace()).intValue());
						writeD(rr.getPointsByRace(rpr.getRace()).intValue());
						writeD(rpr.getRace().getRaceId());
						writeD(object);
						break;
					case 11: // Send when a NPC/Player has been dead (Send 2-times with different RaceId)
						int TeamScore = rr.getPointsByRace(rpr.getRace()).intValue();
						int OppositeTeamScore = rr.getPointsByRace(getOppositeRace(rpr.getRace())).intValue();
						writeC(0);
						writeD(rr.getPvpKillsByRace(rpr.getRace()).intValue());
						writeD(TeamScore);
						writeD(rpr.getRace().getRaceId());
						writeD(TeamScore == OppositeTeamScore ? 65535 : 0);
						break;
				}
				break;
			case 301210000: // Jormungand Marching Route
				JormungandReward jr = (JormungandReward) instanceReward;
				if (object == null) {
					object = ownerObject; // Player ObjectId
				}
				JormungandPlayerReward jpr = jr.getPlayerReward(object);
				writeC(type);
				switch (type) {
					case 2:
						writeD(0);
						writeD(jr.getTime());
						break;
					case 3:
						writeD(10);
						writeD(PlayerStatus);
						writeD(object);
						writeD(PlayerRaceId);
						break;
					case 4:
						writeD(10);
						writeD(PlayerStatus);
						writeD(object);
						break;
					case 5:
						writeD((int) jpr.getParticipation());
						writeD(jpr.getRewardAp());
						writeD(jpr.getBonusAp());
						writeD(jpr.getRewardGp());
						writeD(jpr.getBonusGp());
						writeD(jpr.getOphidanVictoryBox());
						writeQ(jpr.getRewardCount());
						writeD(jpr.getBloodMark());
						writeQ(jpr.getRewardCount());
						writeD(jpr.getBonusReward());
						writeD(jpr.getRewardCount());
						writeD(jpr.getBonusReward2());
						writeD(jpr.getRewardCount());
						break;
					case 6:
						int counter = 0;
						writeD(100);
						for (Player player : players) {
							if (player.getRace() != Race.ELYOS) {
								continue;
							}
							writeD(10);
							writeD(0);
							counter++;
							writeD(player.getObjectId());
						}
						if (counter < 48) {
							writeB(new byte[12 * (48 - counter)]);
						}
						writeC(0);
						writeD(0);
						writeD(jr.getPointsByRace(Race.ELYOS).intValue());
						writeD(0);
						writeD(65535);
						writeC(0);
						writeD(0);
						writeD(jr.getPointsByRace(Race.ASMODIANS).intValue());
						writeD(1);
						writeD(65535);
						break;
					case 7:
						JormungandTable(Race.ELYOS);
						JormungandTable(Race.ASMODIANS);
						break;
					case 10:
						writeC(0);
						writeD(jr.getPvpKillsByRace(jpr.getRace()).intValue());
						writeD(jr.getPointsByRace(jpr.getRace()).intValue());
						writeD(jpr.getRace().getRaceId());
						writeD(object);
						break;
					case 11:
						writeC(0);
						writeD(jr.getPvpKillsByRace(jpr.getRace()).intValue());
						writeD(jr.getPointsByRace(jpr.getRace()).intValue());
						writeD(jpr.getRace().getRaceId());
						writeD(object);
						break;
				}
				break;
			case 301670000: // Balaur Marching Route
				BalaurMarchingRouteReward bmrr = (BalaurMarchingRouteReward) instanceReward;
				if (object == null) {
					object = ownerObject; // Player ObjectId
				}
				BalaurMarchingRoutePlayerReward bmrpr = bmrr.getPlayerReward(object);
				writeC(type);
				switch (type) {
					case 2:
						writeD(0);
						writeD(bmrr.getTime());
						break;
					case 3:
						writeD(10);
						writeD(PlayerStatus);
						writeD(object);
						writeD(PlayerRaceId);
						break;
					case 4:
						writeD(10);
						writeD(PlayerStatus);
						writeD(object);
						break;
					case 5:
						writeD((int) bmrpr.getParticipation());
						writeD(bmrpr.getRewardAp());
						writeD(bmrpr.getBonusAp());
						writeD(bmrpr.getRewardGp());
						writeD(bmrpr.getBonusGp());
						writeD(bmrpr.getOphidanVictoryBox());
						writeQ(bmrpr.getRewardCount());
						writeD(bmrpr.getBloodMark());
						writeQ(bmrpr.getRewardCount());
						writeD(bmrpr.getBonusReward());
						writeD(bmrpr.getRewardCount());
						writeD(bmrpr.getBonusReward2());
						writeD(bmrpr.getRewardCount());
						break;
					case 6:
						int counter = 0;
						writeD(100);
						for (Player player : players) {
							if (player.getRace() != Race.ELYOS) {
								continue;
							}
							writeD(10);
							writeD(0);
							counter++;
							writeD(player.getObjectId());
						}
						if (counter < 48) {
							writeB(new byte[12 * (48 - counter)]);
						}
						writeC(0);
						writeD(0);
						writeD(bmrr.getPointsByRace(Race.ELYOS).intValue());
						writeD(0);
						writeD(65535);
						writeC(0);
						writeD(0);
						writeD(bmrr.getPointsByRace(Race.ASMODIANS).intValue());
						writeD(1);
						writeD(65535);
						break;
					case 7:
						BalaurMarchingRouteTable(Race.ELYOS);
						BalaurMarchingRouteTable(Race.ASMODIANS);
						break;
					case 10:
						writeC(0);
						writeD(bmrr.getPvpKillsByRace(bmrpr.getRace()).intValue());
						writeD(bmrr.getPointsByRace(bmrpr.getRace()).intValue());
						writeD(bmrpr.getRace().getRaceId());
						writeD(object);
						break;
					case 11:
						writeC(0);
						writeD(bmrr.getPvpKillsByRace(bmrpr.getRace()).intValue());
						writeD(bmrr.getPointsByRace(bmrpr.getRace()).intValue());
						writeD(bmrpr.getRace().getRaceId());
						writeD(object);
						break;
				}
				break;
			case 301220000: // Steel Wall Bastion Battlefield
				SteelWallBastionBattlefieldReward swbbr = (SteelWallBastionBattlefieldReward) instanceReward;
				if (object == null) {
					object = ownerObject; // Player ObjectId
				}
				SteelWallBastionBattlefieldPlayerReward swbbpr = swbbr.getPlayerReward(object);
				writeC(type);
				switch (type) {
					case 2:
						writeD(0);
						writeD(swbbr.getTime());
						break;
					case 3:
						writeD(10);
						writeD(PlayerStatus);
						writeD(object);
						writeD(PlayerRaceId);
						break;
					case 4:
						writeD(10);
						writeD(PlayerStatus);
						writeD(object);
						break;
					case 5:
						writeD((int) swbbpr.getParticipation());
						writeD(swbbpr.getRewardAp());
						writeD(swbbpr.getBonusAp());
						writeD(swbbpr.getRewardGp());
						writeD(swbbpr.getBonusGp());
						writeD(swbbpr.getMedalBundle());
						writeQ(swbbpr.getRewardCount());
						writeD(swbbpr.getBloodMark());
						writeQ(swbbpr.getRewardCount());
						writeD(swbbpr.getBonusReward());
						writeD(swbbpr.getRewardCount());
						writeD(swbbpr.getBonusReward2());
						writeD(swbbpr.getRewardCount());
						break;
					case 6:
						int counter = 0;
						writeD(100);
						for (Player player : players) {
							if (player.getRace() != Race.ELYOS) {
								continue;
							}
							writeD(10);
							writeD(0);
							counter++;
							writeD(player.getObjectId());
						}
						if (counter < 48) {
							writeB(new byte[12 * (48 - counter)]);
						}
						writeC(0);
						writeD(0);
						writeD(swbbr.getPointsByRace(Race.ELYOS).intValue());
						writeD(0);
						writeD(65535);
						writeC(0);
						writeD(0);
						writeD(swbbr.getPointsByRace(Race.ASMODIANS).intValue());
						writeD(1);
						writeD(65535);
						break;
					case 7:
						SteelWallBastionBattlefieldTable(Race.ELYOS);
						SteelWallBastionBattlefieldTable(Race.ASMODIANS);
						break;
					case 10:
						writeC(0);
						writeD(swbbr.getPvpKillsByRace(swbbpr.getRace()).intValue());
						writeD(swbbr.getPointsByRace(swbbpr.getRace()).intValue());
						writeD(swbbpr.getRace().getRaceId());
						writeD(object);
						break;
					case 11:
						writeC(0);
						writeD(swbbr.getPvpKillsByRace(swbbpr.getRace()).intValue());
						writeD(swbbr.getPointsByRace(swbbpr.getRace()).intValue());
						writeD(swbbpr.getRace().getRaceId());
						writeD(object);
						break;
				}
				break;
			case 300300000: // Empyrean Crucible
			case 300320000: // Empyrean Crucible Challenge
				for (CruciblePlayerReward playerReward : (FastList<CruciblePlayerReward>) instanceReward.getInstanceRewards()) {
					writeD(playerReward.getOwner());
					writeD(playerReward.getPoints());
					writeD(instanceScoreType.isEndProgress() ? 3 : 1);
					writeD(playerReward.getInsignia());
					playerCount++;
				}
				if (playerCount < 6) {
					writeB(new byte[16 * (6 - playerCount)]);
				}
				break;
			case 300040000: // Dark Poeta.
				DarkPoetaReward dpr = (DarkPoetaReward) instanceReward;
				writeD(dpr.getPoints());
				writeD(dpr.getNpcKills());
				writeD(dpr.getGatherCollections());
				writeD(dpr.getRank());
				break;
			case 300480000: // Sealed Hall of Knowledge
				SealedHallOfKnowledgeReward hok = (SealedHallOfKnowledgeReward) instanceReward;
				writeD(hok.getPoints());
				writeD(hok.getNpcKills());
				writeD(0);
				writeD(hok.getRank());
				writeD(hok.getBasicAP());
				if (hok.getPoints() >= 30481) { // S
					writeD(186000239); // Sillus Crest
					writeD(hok.getSillusCrest());
					writeD(186000242); // Ceramium Medal
					writeD(hok.getCeramiumMedal());
					writeD(188052543); // Favorable Reward Bundle
				}
				else if (hok.getPoints() >= 22252) { // A
					writeD(186000239);
					writeD(hok.getSillusCrest());
					writeD(152012578); // Ceramium Fragments
					writeD(hok.getCeramiumFragments());
					writeD(188052547); // Valor Reward Bundle
				}
				else if (hok.getPoints() >= 15394) { // B
					writeD(186000239);
					writeD(hok.getSillusCrest());
					writeD(186000147); // Mithril Medal
					writeD(hok.getMithrilMedal());
					writeD(0);
				}
				else if (hok.getPoints() >= 9908) { // C
					writeD(186000239);
					writeD(hok.getSillusCrest());
					writeD(0);
					writeD(0);
					writeD(0);
				}
				else if (hok.getPoints() >= 7165) { // D
					writeD(186000239);
					writeD(hok.getSillusCrest());
					writeD(0);
					writeD(0);
					writeD(0);
				}
				else { // F
					writeD(0);
					writeD(0);
					writeD(0);
					writeD(0);
					writeD(0);
				}
				if (hok.getPoints() >= 30481) { // S
					writeD(hok.getFavorableBundle());
				}
				else if (hok.getPoints() >= 22252) { // A
					writeD(hok.getValorBundle());
				}
				else {
					writeD(0);
				}
				writeD(0);
				writeD(0);
				break;
			case 300540000: // Steel Wall Bastion
				SteelWallBastionReward swbr = (SteelWallBastionReward) instanceReward;
				writeD(swbr.getPoints());
				writeD(swbr.getNpcKills());
				writeD(0);
				writeD(swbr.getRank());
				writeD(swbr.getBasicAP());
				if (swbr.getPoints() >= 92000) { // S
					writeD(186000242);
					writeD(swbr.getCeramiumMedal());
					writeD(188052596);
					writeD(swbr.getPowerfulBundleWater());
					writeD(188052594);
				}
				else if (swbr.getPoints() >= 84000) { // A
					writeD(186000242);
					writeD(swbr.getCeramiumMedal());
					writeD(188052594);
					writeD(swbr.getPowerfulBundleEssence());
					writeD(188052597);
				}
				else if (swbr.getPoints() >= 76000) { // B
					writeD(186000242);
					writeD(swbr.getCeramiumMedal());
					writeD(188052595);
					writeD(swbr.getLargeBundleEssence());
					writeD(188052598);
				}
				else if (swbr.getPoints() >= 50000) { // C
					writeD(188052598);
					writeD(swbr.getSmallBundleWater());
					writeD(0);
					writeD(0);
					writeD(0);
				}
				else if (swbr.getPoints() >= 10000) { // D
					writeD(0);
					writeD(0);
					writeD(0);
					writeD(0);
					writeD(0);
				}
				else { // F
					writeD(0);
					writeD(0);
					writeD(0);
					writeD(0);
					writeD(0);
				}
				writeD(0);
				writeD(0);
				break;
			case 300350000: // Arena of Chaos
			case 300360000: // Arena of Discipline
			case 300420000: // Chaos Training Grounds
			case 300430000: // Discipline Training Grounds
			case 300550000: // Arena of Glory
				PvPArenaReward arenaReward = (PvPArenaReward) instanceReward;
				PvPArenaPlayerReward rewardedPlayer = arenaReward.getPlayerReward(ownerObject);
				int rank, points;
				boolean isRewarded = arenaReward.isRewarded();
				for (Player player : players) {
					InstancePlayerReward reward = arenaReward.getPlayerReward(player.getObjectId());
					PvPArenaPlayerReward playerReward = (PvPArenaPlayerReward) reward;
					points = playerReward.getPoints();
					rank = arenaReward.getRank(playerReward.getScorePoints());
					writeD(playerReward.getOwner());
					writeD(playerReward.getPvPKills());
					writeD(isRewarded ? points + playerReward.getTimeBonus() : points);
					writeD(0);
					writeC(0);
					writeC(player.getPlayerClass().getClassId());
					writeC(1);
					writeC(rank);
					writeD(playerReward.getRemaningTime());
					writeD(isRewarded ? playerReward.getTimeBonus() : 0);
					writeD(0);
					writeD(0);
					writeH(isRewarded ? (short) (playerReward.getParticipation() * 100) : 0);
					writeS(player.getName(), 54);
					playerCount++;
				}
				if (playerCount < 12) {
					writeB(new byte[92 * (12 - playerCount)]);
				}
				if (isRewarded && arenaReward.canRewarded() && rewardedPlayer != null) {
					writeD(rewardedPlayer.getBasicAP());
					writeD(rewardedPlayer.getRankingAP());
					writeD(rewardedPlayer.getScoreAP());
					if (mapId == 300550000) { // Arena Of Glory.
						writeD(rewardedPlayer.getBasicGP());
						writeD(rewardedPlayer.getRankingGP());
						writeD(rewardedPlayer.getScoreGP());
						writeB(new byte[32]);
						if (rewardedPlayer.getMithrilMedal() != 0) {
							writeD(186000147); // Mithril Medal.
							writeD(rewardedPlayer.getMithrilMedal());
						}
						else if (rewardedPlayer.getPlatinumMedal() != 0) {
							writeD(186000096); // Platinum Medal.
							writeD(rewardedPlayer.getPlatinumMedal());
						}
						else if (rewardedPlayer.getLifeSerum() != 0) {
							writeD(162000077); // Fine Life Serum.
							writeD(rewardedPlayer.getLifeSerum());
						}
						else {
							writeD(0);
							writeD(0);
						}
						if (rewardedPlayer.getGloriousInsignia() != 0) {
							writeD(182213259); // Glorious Insignia.
							writeD(rewardedPlayer.getGloriousInsignia());
						}
						else {
							writeD(0);
							writeD(0);
						}
					}
					else {
						writeD(186000130); // Crucible Insignia.
						writeD(rewardedPlayer.getBasicCrucible());
						writeD(rewardedPlayer.getScoreCrucible());
						writeD(rewardedPlayer.getRankingCrucible());
						writeD(186000137); // Courage Insignia.
						writeD(rewardedPlayer.getBasicCourage());
						writeD(rewardedPlayer.getScoreCourage());
						writeD(rewardedPlayer.getRankingCourage());
						if (rewardedPlayer.getOpportunity() != 0) {
							writeD(186000165); // Opportunity Token.
							writeD(rewardedPlayer.getOpportunity());
						}
						else if (rewardedPlayer.getGloryTicket() != 0) {
							writeD(186000185); // Arena Of Glory Ticket.
							writeD(rewardedPlayer.getGloryTicket());
						}
						else {
							writeD(0);
							writeD(0);
						}
						writeD(0);
						writeD(0);
					}
				}
				else {
					writeB(new byte[60]);
				}
				writeD(arenaReward.getBuffId());
				writeD(0);
				writeD(arenaReward.getRound());
				writeD(arenaReward.getCapPoints());
				writeD(3);
				writeD(0);
				break;
			case 301400000: // Trillonerk's Gold Vault
				ShugoEmperorVaultReward shugoReward = (ShugoEmperorVaultReward) instanceReward;
				writeD(shugoReward.getPoints());
				writeD(shugoReward.getPoints());
				writeD(0);
				writeD(shugoReward.getRank());
				writeD(0);
				writeD(0);
				writeD(instanceScoreType.isEndProgress() ? shugoReward.getKeys() : 0);
				writeD(instanceScoreType.isEndProgress() ? shugoReward.getKeys() : 0);
				writeD(0);
				writeD(0);
				writeD(0);
				writeD(0);
				writeD(0);
				break;
			case 301510000: { // Cursed Argent Manor
				ArgentManorReward argentManorReward = (ArgentManorReward) instanceReward;
				writeD(argentManorReward.getPoints()); // points
				writeD(argentManorReward.getNpcKills()); // points
				writeD(0);
				writeD(argentManorReward.getRank()); // rank
				writeD(0); // score
				writeD(argentManorReward.getScoreAP()); // ap points
				writeD(0);
				writeD(0);
				if (argentManorReward.getPoints() >= 16000) {
					writeD(argentManorReward.getSeniorBox());
					writeD(0); // count
					return;
				}
				if (argentManorReward.getPoints() >= 13000) {
					writeD(argentManorReward.getIntermediateBox());
					writeD(0); // count
					return;
				}
				if (argentManorReward.getPoints() >= 10000) {
					writeD(argentManorReward.getLesserBox());
					writeD(0); // count
					return;
				}
				return;
			}
			case 301640000: { // Mechanerk's Weapons Factory
				MechanerksWeaponsFactoryReward mechanerksWeaponsFactoryReward = (MechanerksWeaponsFactoryReward) instanceReward;
				writeD(mechanerksWeaponsFactoryReward.getPoints()); // points
				writeD(mechanerksWeaponsFactoryReward.getNpcKills()); // points
				writeD(0);
				writeD(mechanerksWeaponsFactoryReward.getRank());
				writeD(0);
				writeD(0);
				writeD(0);
				writeD(0);
				if (mechanerksWeaponsFactoryReward.getPoints() >= 878600) {
					writeD(mechanerksWeaponsFactoryReward.getSecretChest());
					writeD(0); // count
					return;
				}
				if (mechanerksWeaponsFactoryReward.getPoints() >= 1000) {
					writeD(mechanerksWeaponsFactoryReward.getTreasureChest());
					writeD(0); // count
					return;
				}
				if (mechanerksWeaponsFactoryReward.getPoints() >= 50) {
					writeD(mechanerksWeaponsFactoryReward.getGoldChest());
					writeD(0); // count
					return;
				}
				return;
			}
			case 302000000: { // Fire Temple of Memory
				FireTempleOfMemoryReward fireTempleReward = (FireTempleOfMemoryReward) instanceReward;
				writeD(fireTempleReward.getPoints());
				writeD(fireTempleReward.getPoints());
				writeD(0);
				writeD(fireTempleReward.getRank());
				writeD(0);
				writeD(0);
				writeD(instanceScoreType.isEndProgress() ? fireTempleReward.getKeys() : 0);
				writeD(instanceScoreType.isEndProgress() ? fireTempleReward.getKeys() : 0);
				writeD(0);
				writeD(0);
				writeD(0);
				writeD(0);
				writeD(0);
				break;
			}
			case 302100000: { // Rift of Oblivion
				RiftOfOblivionReward riftOfOblivionReward = (RiftOfOblivionReward) instanceReward;
				writeD(riftOfOblivionReward.getPoints()); // points
				writeD(riftOfOblivionReward.getNpcKills()); // points
				writeD(0);
				writeD(riftOfOblivionReward.getRank());
				writeD(0);
				writeD(0);
				if (riftOfOblivionReward.getPoints() >= 23550) {
					writeD(riftOfOblivionReward.getIcyOrbOfMemory());
					writeD(0); // count
					return;
				}
				if (riftOfOblivionReward.getPoints() >= 21200) {
					writeD(riftOfOblivionReward.getIcyOrbOfMemory());
					writeD(0); // count
					return;
				}
				if (riftOfOblivionReward.getPoints() >= 17700) {
					writeD(riftOfOblivionReward.getIcyOrbOfMemory());
					writeD(0); // count
					return;
				}
				if (riftOfOblivionReward.getPoints() >= 14100) {
					writeD(riftOfOblivionReward.getIcyOrbOfMemory());
					writeD(0); // count
					return;
				}
				if (riftOfOblivionReward.getPoints() >= 9400) {
					writeD(riftOfOblivionReward.getIcyOrbOfMemory());
					writeD(0); // count
					return;
				}
				return;
			}
			case 302200000: // Sanctum Battlefield
                SanctumBattlefieldReward sanctumBattlefieldReward = (SanctumBattlefieldReward) instanceReward;
                int points1 = sanctumBattlefieldReward.getPoints();
                int npcKills1 = sanctumBattlefieldReward.getNpcKills();
                int rank1 = sanctumBattlefieldReward.getRank();
                writeD(points1);
                writeD(npcKills1);
                writeD(rank1);
				break;
			case 302300000: // Pandaemonium Battlefield
                PandaemoniumBattlefieldReward pandaemoniumBattlefieldReward = (PandaemoniumBattlefieldReward) instanceReward;
                int points2 = pandaemoniumBattlefieldReward.getPoints();
                int npcKills2 = pandaemoniumBattlefieldReward.getNpcKills();
                int rank2 = pandaemoniumBattlefieldReward.getRank();
                writeD(points2);
                writeD(npcKills2);
                writeD(rank2);
				break;
			case 302310000: // Gold Arena
				// TODO
				break;
			case 302320000: // Golden Crucible
				// TODO
				break;
			case 302350000: // Neviwind Canyon
                NeviwindCanyonReward ncr = (NeviwindCanyonReward) instanceReward;
				if (object == null) {
					object = ownerObject; // Player ObjectId
				}
                NeviwindCanyonPlayerReward ncpr = ncr.getPlayerReward(object);
                writeC(type);
                switch (type) {
                case 2: {
                    writeD(0);
                    writeD(ncr.getTime());
                    break;
                    }
                    case 3: {
                        writeD(10);
                        writeD(PlayerStatus);
                        writeD(object);
                        writeD(PlayerRaceId);
                        break;
                    }
                    case 4: {
                        writeD(10);
                        writeD(PlayerStatus);
                        writeD(object);
                        break;
                    }
                    case 5: {
                        writeD((int) ncpr.getParticipation());
                        writeD(ncpr.getRewardExp());
                        writeD(ncpr.getBonusExp());
                        writeD(ncpr.getRewardAp());
                        writeD(ncpr.getBonusAp());
                        writeD(ncpr.getRewardGp());
                        writeD(ncpr.getBonusGp());
                        writeD(ncpr.getCoinIdEternityWar01());
                        writeQ(ncpr.getRewardCount());
                        writeD(ncpr.getBrokenSpinel());
                        writeQ(ncpr.getRewardCount());
                        writeD(ncpr.getCashMinionContract01());
                        writeQ(ncpr.getRewardCount());
                        writeD(ncpr.getIDEternityWarStigma());
                        writeQ(ncpr.getRewardCount());
                        writeC(1);
                        break;
                    }
                    case 6: {
                        int counter = 0;
                        writeD(100);
                        for (Player player : players) {
                            if (player.getRace() != Race.ELYOS) {
                                continue;
                            }
                            writeD(15);
                            writeD(player.getLifeStats().isAlreadyDead() ? 60 : 0);
                            writeD(player.getObjectId());
                            counter++;
                        }
                        if (counter < 24) {
                            writeB(new byte[12 * (24 - counter)]);
                        }
                        writeC(0);
                        writeD(ncr.getPvpKillsByRace(Race.ELYOS).intValue());
                        writeD(ncr.getPointsByRace(Race.ELYOS).intValue());
                        writeD(0);
                        writeD(ncr.getInstanceScoreType() == InstanceScoreType.PREPARING ? 65535 : 1);
                        writeC(0);
                        writeD(ncr.getPvpKillsByRace(Race.ASMODIANS).intValue());
                        writeD(ncr.getPointsByRace(Race.ASMODIANS).intValue());
                        writeD(1);
                        writeD(ncr.getInstanceScoreType() == InstanceScoreType.PREPARING ? 65535 : 1);
                        break;
                    }
                    case 7: {
                        NeviwindCanyonTable(Race.ELYOS);
                        NeviwindCanyonTable(Race.ASMODIANS);
                        break;
                    }
                    case 8: {
                        writeD(object);
                        break;
                    }
                    case 10: {
                        writeC(0);
                        writeD(ncr.getPvpKillsByRace(ncpr.getRace()).intValue());
                        writeD(ncr.getPointsByRace(ncpr.getRace()).intValue());
                        writeD(ncpr.getRace().getRaceId());
                        writeD(object);
                        break;
                    }
                    case 11: {
                        int TeamScore = ncr.getPointsByRace(ncpr.getRace()).intValue();
                        int OppositeTeamScore = ncr.getPointsByRace(ncpr.getRace()).intValue();
                        writeC(0);
                        writeD(ncr.getPvpKillsByRace(ncpr.getRace()).intValue());
                        writeD(TeamScore);
                        writeD(ncpr.getRace().getRaceId());
                        writeD(TeamScore == OppositeTeamScore ? 65535 : 0);
                        break;
                    }
                }
            break;
		}
	}

	private void fillTableWithGroup(Race race) {
		int count = 0;
		DredgionReward dredgionReward = (DredgionReward) instanceReward;
		for (Player player : players) {
			if (!race.equals(player.getRace())) {
				continue;
			}
			InstancePlayerReward playerReward = dredgionReward.getPlayerReward(player.getObjectId());
			DredgionPlayerReward dpr = (DredgionPlayerReward) playerReward;
			writeD(playerReward.getOwner());
			writeD(player.getAbyssRank().getRank().getId());
			writeD(dpr.getPvPKills());
			writeD(dpr.getMonsterKills());
			writeD(dpr.getZoneCaptured());
			writeD(dpr.getPoints());
			if (instanceScoreType.isEndProgress()) {
				boolean winner = race.equals(dredgionReward.getWinningRace());
				writeD((winner ? dredgionReward.getWinnerPoints() : dredgionReward.getLooserPoints()) + (int) (dpr.getPoints() * 1.6f));
				writeD((winner ? dredgionReward.getWinnerPoints() : dredgionReward.getLooserPoints()));
			}
			else {
				writeB(new byte[8]);
			}
			writeC(player.getPlayerClass().getClassId());
			writeC(0);
			writeS(player.getName(), 54);
			count++;
		}
		if (count < 6) {
			writeB(new byte[88 * (6 - count)]);
		}
	}

	private Race getOppositeRace(Race race) {
		return (race.getRaceId() ^ 1) == 0 ? Race.ELYOS : Race.ASMODIANS;
	}

	private void kamarTable(Race race) {
		int count = 0;
		KamarBattlefieldReward kbr = (KamarBattlefieldReward) instanceReward;

		for (Player player : players) {
			if (!race.equals(player.getRace())) {
				continue;
			}
			KamarBattlefieldPlayerReward kbpr = kbr.getPlayerReward(player.getObjectId());
			writeD(player.getObjectId());
			writeC(player.getPlayerClass().getClassId());
			writeC(player.getAbyssRank().getRank().getId());
			writeC(0);// unk
			writeH(0);// unk?
			writeD(kbpr.getPvPKills());
			log.info("Player Points: " + kbpr.getPoints());
			writeD(kbpr.getPoints());
			writeS(player.getName(), 52);
			count++;
		}
		if (count < 12) {
			writeB(new byte[69 * (12 - count)]);
		}
		writeB(new byte[828]);
	}

	private void JormungandTable(Race race) {
		int count = 0;
		JormungandReward jr = (JormungandReward) instanceReward;

		for (Player player : players) {
			if (!race.equals(player.getRace())) {
				continue;
			}
			JormungandPlayerReward jpr = jr.getPlayerReward(player.getObjectId());
			writeD(player.getObjectId());
			writeC(player.getPlayerClass().getClassId());
			writeC(player.getAbyssRank().getRank().getId());
			writeC(0);
			writeH(0);
			writeD(jpr.getPvPKills());
			writeD(jpr.getPoints());
			writeS(player.getName(), 52);
			count++;
		}
		if (count < 12) {
			writeB(new byte[69 * (12 - count)]);
		}
		writeB(new byte[828]);
	}

	private void BalaurMarchingRouteTable(Race race) {
		int count = 0;
		BalaurMarchingRouteReward bmrr = (BalaurMarchingRouteReward) instanceReward;

		for (Player player : players) {
			if (!race.equals(player.getRace())) {
				continue;
			}
			BalaurMarchingRoutePlayerReward bmrpr = bmrr.getPlayerReward(player.getObjectId());
			writeD(player.getObjectId());
			writeC(player.getPlayerClass().getClassId());
			writeC(player.getAbyssRank().getRank().getId());
			writeC(0);
			writeH(0);
			writeD(bmrpr.getPvPKills());
			writeD(bmrpr.getPoints());
			writeS(player.getName(), 52);
			count++;
		}
		if (count < 12) {
			writeB(new byte[69 * (12 - count)]);
		}
		writeB(new byte[828]);
	}

	private void DomeTable(Race race) {
		int count = 0;
		RunatoriumReward rr = (RunatoriumReward) instanceReward;

		for (Player player : players) {
			if (!race.equals(player.getRace())) {
				continue;
			}
			RunatoriumPlayerReward rpr = rr.getPlayerReward(player.getObjectId());
			writeD(player.getObjectId());
			writeC(player.getPlayerClass().getClassId());
			writeC(player.getAbyssRank().getRank().getId());
			writeC(0);// unk
			writeH(0);// unk?
			writeD(rpr.getPvPKills());
			log.info("Player Points: " + rpr.getPoints());
			writeD(rpr.getPoints());
			writeS(player.getName(), 52);
			count++;
		}
		if (count < 12) {
			writeB(new byte[69 * (12 - count)]);
		}
		writeB(new byte[897]);
	}

	private void SteelWallBastionBattlefieldTable(Race race) {
		int count = 0;
		SteelWallBastionBattlefieldReward swbbr = (SteelWallBastionBattlefieldReward) instanceReward;

		for (Player player : players) {
			if (!race.equals(player.getRace())) {
				continue;
			}
			SteelWallBastionBattlefieldPlayerReward swbbpr = swbbr.getPlayerReward(player.getObjectId());
			writeD(player.getObjectId());
			writeC(player.getPlayerClass().getClassId());
			writeC(player.getAbyssRank().getRank().getId());
			writeC(0);
			writeH(0);
			writeD(swbbpr.getPvPKills());
			writeD(swbbpr.getPoints());
			writeS(player.getName(), 52);
			count++;
		}
		if (count < 12) {
			writeB(new byte[69 * (12 - count)]);
		}
		writeB(new byte[897]);
	}

    private void NeviwindCanyonTable(Race race) {
        int count = 0;
        NeviwindCanyonReward ncr = (NeviwindCanyonReward) instanceReward;

        for (Player player : players) {
            if (!race.equals(player.getRace())) {
                continue;
            }
            NeviwindCanyonPlayerReward ncpr = ncr.getPlayerReward(player.getObjectId());
            writeD(player.getObjectId());
            writeC(player.getPlayerClass().getClassId());
            writeC(player.getAbyssRank().getRank().getId());
            writeC(0);
            writeH(0);
            writeD(ncpr.getPvPKills());
            writeD(ncpr.getPoints());
            writeS(player.getName(), 52);
            count++;
        }
        if (count < 12) {
            writeB(new byte[69 * (12 - count)]);
        }
        writeB(new byte[828]);
    }
}
