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
package com.aionemu.gameserver.services;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;
import static org.hamcrest.Matchers.equalTo;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.configs.main.LoggingConfig;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.instance.InstanceEngine;
import com.aionemu.gameserver.model.autogroup.AGPlayer;
import com.aionemu.gameserver.model.autogroup.AGQuestion;
import com.aionemu.gameserver.model.autogroup.AutoGroupType;
import com.aionemu.gameserver.model.autogroup.AutoInstance;
import com.aionemu.gameserver.model.autogroup.EntryRequestType;
import com.aionemu.gameserver.model.autogroup.LookingForParty;
import com.aionemu.gameserver.model.autogroup.SearchInstance;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.team2.alliance.PlayerAllianceService;
import com.aionemu.gameserver.model.team2.group.PlayerGroup;
import com.aionemu.gameserver.model.team2.group.PlayerGroupService;
import com.aionemu.gameserver.model.templates.InstanceCooltime;
import com.aionemu.gameserver.network.aion.serverpackets.SM_AUTO_GROUP;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.instance.BalaurMarchingRouteService;
import com.aionemu.gameserver.services.instance.DredgionService;
import com.aionemu.gameserver.services.instance.GoldenCrucibleService;
import com.aionemu.gameserver.services.instance.InstanceService;
import com.aionemu.gameserver.services.instance.JormungandService;
import com.aionemu.gameserver.services.instance.KamarBattlefieldService;
import com.aionemu.gameserver.services.instance.PandaemoniumBattlefieldService;
import com.aionemu.gameserver.services.instance.PvPArenaService;
import com.aionemu.gameserver.services.instance.RunatoriumRuinsService;
import com.aionemu.gameserver.services.instance.RunatoriumService;
import com.aionemu.gameserver.services.instance.SanctumBattlefieldService;
import com.aionemu.gameserver.services.instance.SteelWallBastionBattlefieldService;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.WorldMap;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.WorldMapInstanceFactory;

import javolution.util.FastList;
import javolution.util.FastMap;

/**
 * @author xTz
 * @Reworked Eloann v4.5
 * @author GiGatR00n v4.7.5.x
 */
public class AutoGroupService {

	private static final Logger log = LoggerFactory.getLogger("AUTOGROUP_LOG");
	private FastMap<Integer, LookingForParty> searchers = new FastMap<Integer, LookingForParty>().shared();
	private FastMap<Integer, AutoInstance> autoInstances = new FastMap<Integer, AutoInstance>().shared();
	private Collection<Integer> penaltys = new FastList<Integer>().shared();
	private Lock lock = new ReentrantLock();

	private AutoGroupService() {
	}

	public void startLooking(Player player, int instanceMaskId, EntryRequestType ert) {
		AutoGroupType agt = AutoGroupType.getAGTByMaskId(instanceMaskId);
		if (agt == null) {
			return;
		}
		if (!canEnter(player, ert, agt)) {
			return;
		}
		Integer obj = player.getObjectId();
		LookingForParty lfp = searchers.get(obj);
		if (penaltys.contains(obj)) {
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400181, agt.getInstanceMapId()));
			return;
		}
		if (lfp == null) {
			searchers.put(obj, new LookingForParty(player, instanceMaskId, ert));
		}
		else if (lfp.hasPenalty() || lfp.isRegistredInstance(instanceMaskId)) {
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400181, agt.getInstanceMapId()));
			return;
		}
		else {
			lfp.addInstanceMaskId(instanceMaskId, ert);
		}

		if (ert.isGroupEntry()) {
			for (Player member : player.getPlayerGroup2().getOnlineMembers()) {
				if (agt.isDredgion()) {
					PacketSendUtility.sendPacket(member, new SM_AUTO_GROUP(instanceMaskId, 6, true));
				}
				else if (agt.isKamar()) {
					PacketSendUtility.sendPacket(member, new SM_AUTO_GROUP(instanceMaskId, 6, true));
				}
				else if (agt.isJormungand()) {
					PacketSendUtility.sendPacket(member, new SM_AUTO_GROUP(instanceMaskId, 6, true));
				}
				else if (agt.isSteelWall()) {
					PacketSendUtility.sendPacket(member, new SM_AUTO_GROUP(instanceMaskId, 6, true));
				}
				else if (agt.isRunatorium()) {
					PacketSendUtility.sendPacket(member, new SM_AUTO_GROUP(instanceMaskId, 6, true));
				}
				else if (agt.isBalaurMarching()) {
					PacketSendUtility.sendPacket(member, new SM_AUTO_GROUP(instanceMaskId, 6, true));
				}
				else if (agt.isRunatoriumRuins()) {
					PacketSendUtility.sendPacket(member, new SM_AUTO_GROUP(instanceMaskId, 6, true));
				}
				else if (agt.isGoldenCrucible()) {
					PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6, true));
				}
				else if (agt.isSanctumBattlefield()) {
					PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6, true));
				}
				else if (agt.isPandaemoniumBattlefield()) {
					PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6, true));
				}
				PacketSendUtility.sendPacket(member, new SM_SYSTEM_MESSAGE(1400194, agt.getInstanceMapId()));
				PacketSendUtility.sendPacket(member, new SM_AUTO_GROUP(instanceMaskId, 1, ert.getId(), player.getName()));
			}
		}
		else {
			if (agt.isDredgion()) {
				PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6, true));
			}
			else if (agt.isKamar()) {
				PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6, true));
			}
			else if (agt.isJormungand()) {
				PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6, true));
			}
			else if (agt.isSteelWall()) {
				PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6, true));
			}
			else if (agt.isRunatorium()) {
				PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6, true));
			}
			else if (agt.isBalaurMarching()) {
				PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6, true));
			}
			else if (agt.isRunatoriumRuins()) {
				PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6, true));
			}
			else if (agt.isGoldenCrucible()) {
				PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6, true));
			}
			else if (agt.isSanctumBattlefield()) {
				PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6, true));
			}
			else if (agt.isPandaemoniumBattlefield()) {
				PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6, true));
			}
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400194, agt.getInstanceMapId()));
			PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 1, ert.getId(), player.getName()));
		}
		if (LoggingConfig.LOG_AUTOGROUP) {
			log.info("[AUTOGROUPSERVICE] > Register playerName: " + player.getName() + " class: " + player.getPlayerClass() + " race: " + player.getRace());
			log.info("[AUTOGROUPSERVICE] > Register instanceMaskId: " + instanceMaskId + " type: " + ert);
		}
		startSort(ert, instanceMaskId, true);
	}

	public synchronized void pressEnter(Player player, int instanceMaskId) {
		AutoInstance instance = getAutoInstance(player, instanceMaskId);
		if (instance == null || instance.players.get(player.getObjectId()).isPressedEnter()) {
			return;
		}
		if (player.isInGroup2()) {
			PlayerGroupService.removePlayer(player);
		}
		if (player.isInAlliance2()) {
			PlayerAllianceService.removePlayer(player);
		}
		instance.onPressEnter(player);
		PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 5));
	}

	public void onEnterInstance(Player player) {
		if (player.isInInstance()) {
			setAutoGroupEntryIcon(player, true);
			Integer obj = player.getObjectId();
			AutoInstance autoInstance = autoInstances.get(player.getInstanceId());
			if (autoInstance != null && autoInstance.players.containsKey(obj)) {
				autoInstance.onEnterInstance(player);
				player.setUseAutoGroup(1);
			}
		}
	}

	public void unregisterLooking(Player player, int instanceMaskId) {
		if (LoggingConfig.LOG_AUTOGROUP) {
			log.info("[AUTOGROUPSERVICE] > unregisterLooking instanceMaskId: " + instanceMaskId + " player: " + player.getName());
		}
		Integer obj = player.getObjectId();
		LookingForParty lfp = searchers.get(obj);
		SearchInstance si;
		if (lfp != null) {
			lfp.setPenaltyTime();
			si = lfp.getSearchInstance(instanceMaskId);
			if (si != null) {
				if (lfp.unregisterInstance(instanceMaskId) == 0) {
					searchers.remove(obj);
					startPenalty(obj);
				}
				getInstance().unRegisterSearchInstance(player, si);
			}
		}
	}

	public void cancelEnter(Player player, int instanceMaskId) {
		AutoInstance autoInstance = getAutoInstance(player, instanceMaskId);
		if (autoInstance != null) {
			Integer obj = player.getObjectId();
			if (!autoInstance.players.get(obj).isInInstance()) {
				autoInstance.unregister(player);
				if (!searchers.containsKey(obj)) {
					startPenalty(obj);
				}
				if (autoInstance.agt.hasRegisterQuick()) {
					startSort(EntryRequestType.QUICK_GROUP_ENTRY, instanceMaskId, false);
				}
				if (autoInstance.players.isEmpty()) {
					WorldMapInstance instance = autoInstance.instance;
					autoInstance = autoInstances.remove(instance.getInstanceId());
					InstanceService.destroyInstance(instance);
					autoInstance.clear();
					player.setUseAutoGroup(0);
				}
			}
			if (autoInstance.agt.isDredgion() && DredgionService.getInstance().isDredgionAvailable() && KamarBattlefieldService.getInstance().isKamarAvailable() && JormungandService.getInstance().isJormungandAvailable() && SteelWallBastionBattlefieldService.getInstance().isSteelWallAvailable() && RunatoriumService.getInstance().isRunatoriumAvailable() && BalaurMarchingRouteService.getInstance().isBalaurMarchingAvailable() && RunatoriumRuinsService.getInstance().isRunatoriumRuinsAvailable() && GoldenCrucibleService.getInstance().isGoldenCrucibleAvailable() && SanctumBattlefieldService.getInstance().isSanctumBattlefieldAvailable() && PandaemoniumBattlefieldService.getInstance().isPandaemoniumBattlefieldAvailable()) {
				PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6));
			}
			if (autoInstance.agt.isPvpArena() || autoInstance.agt.isPvPSoloArena()) {
				if (player.getInventory().decreaseByItemId(186000135, 1)) {
					PacketSendUtility.sendMessage(player, "[Anti-Abuse] You have refuse to enter, you have lost an arena ticket");
					return;
				}
			}
			PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 2));
		}
	}

	public void onPlayerLogin(Player player) {
		if (DredgionService.getInstance().canPlayerJoin(player)) {
			PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(DredgionService.getInstance().getInstanceMaskId(player), 6));
		}
		else if (KamarBattlefieldService.getInstance().canPlayerJoin(player)) {
			PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(KamarBattlefieldService.maskId, 6));
		}
		else if (JormungandService.getInstance().canPlayerJoin(player)) {
			PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(JormungandService.maskId, 6));
		}
		else if (SteelWallBastionBattlefieldService.getInstance().canPlayerJoin(player)) {
			PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(SteelWallBastionBattlefieldService.maskId, 6));
		}
		else if (RunatoriumService.getInstance().canPlayerJoin(player)) {
			PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(RunatoriumService.maskId, 6));
		}
		else if (BalaurMarchingRouteService.getInstance().canPlayerJoin(player)) {
			PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(BalaurMarchingRouteService.maskId, 6));
		}
		else if (RunatoriumRuinsService.getInstance().canPlayerJoin(player)) {
			PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(RunatoriumRuinsService.maskId, 6));
		}
		else if (GoldenCrucibleService.getInstance().canPlayerJoin(player)) {
			PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(GoldenCrucibleService.maskId, 6));
		}
		else if (SanctumBattlefieldService.getInstance().canPlayerJoin(player)) {
			PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(SanctumBattlefieldService.maskId, 6));
		}
		else if (PandaemoniumBattlefieldService.getInstance().canPlayerJoin(player)) {
			PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(PandaemoniumBattlefieldService.maskId, 6));
		}
		Integer obj = player.getObjectId();
		LookingForParty lfp = searchers.get(obj);
		if (lfp != null) {
			for (SearchInstance searchInstance : lfp.getSearchInstances()) {
				if (searchInstance.getEntryRequestType().isGroupEntry() && !player.isInGroup2()) {
					int instanceMaskId = searchInstance.getInstanceMaskId();
					lfp.unregisterInstance(instanceMaskId);
					if (searchInstance.isDredgion() && DredgionService.getInstance().isDredgionAvailable()) {
						PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6));
					}
					else if (searchInstance.isKamar() && KamarBattlefieldService.getInstance().isKamarAvailable()) {
						PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6));
					}
					else if (searchInstance.isJormungand() && JormungandService.getInstance().isJormungandAvailable()) {
						PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6));
					}
					else if (searchInstance.isBastion() && SteelWallBastionBattlefieldService.getInstance().isSteelWallAvailable()) {
						PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6));
					}
					else if (searchInstance.isRunatorium() && RunatoriumService.getInstance().isRunatoriumAvailable()) {
						PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6));
					}
					else if (searchInstance.isBalaurMarching() && BalaurMarchingRouteService.getInstance().isBalaurMarchingAvailable()) {
						PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6));
					}
					else if (searchInstance.isRunatoriumRuins() && RunatoriumRuinsService.getInstance().isRunatoriumRuinsAvailable()) {
						PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6));
					}
					else if (searchInstance.isGoldenCrusible() && GoldenCrucibleService.getInstance().isGoldenCrucibleAvailable()) {
						PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6));
					}
					else if (searchInstance.isSanctumBattlefield() && SanctumBattlefieldService.getInstance().isSanctumBattlefieldAvailable()) {
						PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6));
					}
					else if (searchInstance.isPandaemoniumBattlefield() && PandaemoniumBattlefieldService.getInstance().isPandaemoniumBattlefieldAvailable()) {
						PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6));
					}
					PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 2));
					continue;
				}
				PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(searchInstance.getInstanceMaskId(), 8, searchInstance.getRemainingTime() + searchInstance.getEntryRequestType().getId(), player.getName()));
				if (searchInstance.isDredgion()) {
					PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(searchInstance.getInstanceMaskId(), 6, true));
				}
				else if (searchInstance.isKamar()) {
					PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(searchInstance.getInstanceMaskId(), 6, true));
				}
				else if (searchInstance.isJormungand()) {
					PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(searchInstance.getInstanceMaskId(), 6, true));
				}
				else if (searchInstance.isBastion()) {
					PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(searchInstance.getInstanceMaskId(), 6, true));
				}
				else if (searchInstance.isRunatorium()) {
					PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(searchInstance.getInstanceMaskId(), 6, true));
				}
				else if (searchInstance.isBalaurMarching()) {
					PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(searchInstance.getInstanceMaskId(), 6, true));
				}
				else if (searchInstance.isRunatoriumRuins()) {
					PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(searchInstance.getInstanceMaskId(), 6, true));
				}
				else if (searchInstance.isGoldenCrusible()) {
					PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(searchInstance.getInstanceMaskId(), 6, true));
				}
				else if (searchInstance.isSanctumBattlefield()) {
					PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(searchInstance.getInstanceMaskId(), 6, true));
				}
				else if (searchInstance.isPandaemoniumBattlefield()) {
					PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(searchInstance.getInstanceMaskId(), 6, true));
				}
			}
			if (lfp.getSearchInstances().isEmpty()) {
				searchers.remove(obj);
				return;
			}
			lfp.setPlayer(player);
			for (SearchInstance si : lfp.getSearchInstances()) {
				startSort(si.getEntryRequestType(), si.getInstanceMaskId(), true);
			}
		}
	}

	public void onPlayerLogOut(Player player) {
		Integer obj = player.getObjectId();
		int instanceId = player.getInstanceId();
		LookingForParty lfp = searchers.get(obj);
		if (lfp != null) {
			lfp.setPlayer(null);
			if (lfp.isOnStartEnterTask()) {
				for (AutoInstance autoInstance : autoInstances.values()) {
					if (autoInstance.players.containsKey(obj) && !autoInstance.players.get(obj).isInInstance()) {
						cancelEnter(player, autoInstance.agt.getInstanceMaskId());
					}
				}
			}
		}

		if (player.isInInstance()) {
			AutoInstance autoInstance = autoInstances.get(instanceId);
			if (autoInstance != null && autoInstance.players.containsKey(obj)) {
				WorldMapInstance instance = autoInstance.instance;
				if (instance != null) {
					autoInstance.players.get(obj).setOnline(false);
					if (select(autoInstance.players, having(on(AGPlayer.class).isOnline(), equalTo(true))).isEmpty()) {
						autoInstance = autoInstances.remove(instanceId);
						InstanceService.destroyInstance(instance);
						autoInstance.clear();
						player.setUseAutoGroup(0);
					}
				}
			}
		}
	}

	public void onLeaveInstance(Player player) {
		if (player.isInInstance()) {
			Integer obj = player.getObjectId();
			int instanceId = player.getInstanceId();
			AutoInstance autoInstance = autoInstances.get(instanceId);
			if (autoInstance != null && autoInstance.players.containsKey(obj)) {
				autoInstance.onLeaveInstance(player);
				if (select(autoInstance.players, having(on(AGPlayer.class).isOnline(), equalTo(true))).isEmpty()) {
					WorldMapInstance instance = autoInstance.instance;
					autoInstances.remove(instanceId);
					if (instance != null) {
						InstanceService.destroyInstance(instance);
						player.setUseAutoGroup(0);
					}
				}
				else if (autoInstance.agt.hasRegisterQuick()) {
					startSort(EntryRequestType.QUICK_GROUP_ENTRY, autoInstance.agt.getInstanceMaskId(), false);
				}
			}
		}
	}

	private void startSort(EntryRequestType ert, Integer instanceMaskId, boolean checkNewGroup) {
		lock.lock();
		try {
			Collection<Player> players = new HashSet<Player>();
			if (ert.isQuickGroupEntry()) {
				for (LookingForParty lfp : searchers.values()) {
					if (lfp.getPlayer() == null || lfp.isOnStartEnterTask()) {
						continue;
					}
					for (AutoInstance autoInstance : autoInstances.values()) {
						int searchMaskId = autoInstance.agt.getInstanceMaskId();
						SearchInstance searchInstance = lfp.getSearchInstance(searchMaskId);
						if (searchInstance != null && searchInstance.getEntryRequestType().isQuickGroupEntry()) {
							Player owner = lfp.getPlayer();
							if (autoInstance.addPlayer(owner, searchInstance).isAdded()) {
								lfp.setStartEnterTime();
								if (lfp.unregisterInstance(searchMaskId) == 0) {
									players.add(owner);
								}
								PacketSendUtility.sendPacket(lfp.getPlayer(), new SM_AUTO_GROUP(searchMaskId, 4));
							}
						}
					}
				}
				for (Player p : players) {
					searchers.remove(p.getObjectId());
				}
				players.clear();
			}
			if (checkNewGroup) {
				AutoGroupType agt = AutoGroupType.getAGTByMaskId(instanceMaskId);
				AutoInstance autoInstance = agt.getAutoInstance();
				autoInstance.initialize(instanceMaskId);
				boolean canCreate = false;
				Iterator<LookingForParty> iter = searchers.values().iterator();
				LookingForParty lfp;
				while (iter.hasNext()) {
					lfp = iter.next();
					if (lfp.getPlayer() == null || lfp.isOnStartEnterTask()) {
						continue;
					}
					SearchInstance searchInstance = lfp.getSearchInstance(instanceMaskId);
					if (searchInstance != null) {
						if (searchInstance.getEntryRequestType().isGroupEntry()) {
							if (!lfp.getPlayer().isInGroup2()) {
								if (lfp.unregisterInstance(instanceMaskId) == 0) {
									iter.remove();
								}
								continue;
							}
						}
						AGQuestion question = autoInstance.addPlayer(lfp.getPlayer(), searchInstance);
						if (!question.isFailed()) {
							if (searchInstance.getEntryRequestType().isGroupEntry()) {
								for (Player member : lfp.getPlayer().getPlayerGroup2().getOnlineMembers()) {
									if (searchInstance.getMembers().contains(member.getObjectId())) {
										players.add(member);
									}
								}
							}
							else {
								players.add(lfp.getPlayer());
							}
						}
						if (question.isReady()) {
							canCreate = true;
							break;
						}
					}
				}
				if (canCreate) {
					WorldMapInstance instance = createInstance(agt.getInstanceMapId(), agt.getDifficultId());
					autoInstance.onInstanceCreate(instance);
					autoInstances.put(instance.getInstanceId(), autoInstance);
					for (Player player : players) {
						Integer obj = player.getObjectId();
						lfp = searchers.get(obj);
						if (lfp != null) {
							lfp.setStartEnterTime();
							if (lfp.unregisterInstance(autoInstance.agt.getInstanceMaskId()) == 0) {
								searchers.remove(obj);
							}
						}
						PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 4));
					}
				}
				else {
					autoInstance.clear();
				}
				players.clear();
			}
		}
		finally {
			lock.unlock();
		}
	}

	private boolean canEnter(Player player, EntryRequestType ert, AutoGroupType agt) {
		int mapId = agt.getInstanceMapId();
		int instanceMaskId = agt.getInstanceMaskId();
		if (!agt.hasLevelPermit(player.getLevel())) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANT_INSTANCE_ENTER_LEVEL);
			return false;
		}

		if (agt.isDredgion() && !DredgionService.getInstance().isDredgionAvailable()) {
			return false;
		}
		else if ((agt.isPvPFFAArena() || agt.isPvPSoloArena() || agt.isHarmonyArena() || agt.isGloryArena()) && !PvPArenaService.isPvPArenaAvailable(player, agt)) {
			return false;
		}
		else if (agt.isKamar() && !KamarBattlefieldService.getInstance().isKamarAvailable()) {
			return false;
		}
		else if (agt.isJormungand() && !JormungandService.getInstance().isJormungandAvailable()) {
			return false;
		}
		else if (agt.isSteelWall() && !SteelWallBastionBattlefieldService.getInstance().isSteelWallAvailable()) {
			return false;
		}
		else if (agt.isRunatorium() && !RunatoriumService.getInstance().isRunatoriumAvailable()) {
			return false;
		}
		else if (agt.isBalaurMarching() && !BalaurMarchingRouteService.getInstance().isBalaurMarchingAvailable()) {
			return false;
		}
		else if (agt.isRunatoriumRuins() && !RunatoriumRuinsService.getInstance().isRunatoriumRuinsAvailable()) {
			return false;
		}
		else if (agt.isGoldenCrucible() && !GoldenCrucibleService.getInstance().isGoldenCrucibleAvailable()) {
			return false;
		}
		else if (agt.isSanctumBattlefield() && !SanctumBattlefieldService.getInstance().isSanctumBattlefieldAvailable()) {
			return false;
		}
		else if (agt.isPandaemoniumBattlefield() && !PandaemoniumBattlefieldService.getInstance().isPandaemoniumBattlefieldAvailable()) {
			return false;
		}
		else if (hasCoolDown(player, mapId)) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANNOT_MAKE_INSTANCE_COOL_TIME);
			return false;
		}
		switch (ert) {
			case NEW_GROUP_ENTRY:
				if (!agt.hasRegisterNew()) {
					return false;
				}
				break;
			case QUICK_GROUP_ENTRY:
				if (!agt.hasRegisterQuick()) {
					return false;
				}
				break;
			case GROUP_ENTRY:
				if (!agt.hasRegisterGroup()) {
					return false;
				}
				PlayerGroup group = player.getPlayerGroup2();
				if (group == null || !group.isLeader(player)) {
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANT_INSTANCE_NOT_LEADER);
					return false;
				}
				if (agt.isHarmonyArena() || agt.isTrainingHarmonyArena()) {
					if (group.getOnlineMembers().size() > 3) {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANT_INSTANCE_TOO_MANY_MEMBERS(3, Integer.toString(mapId)));
						return false;
					}
				}
				for (Player member : group.getMembers()) {
					if (group.getLeaderObject().equals(member)) {
						continue;
					}
					LookingForParty lfp = searchers.get(member.getObjectId());
					if (lfp != null && lfp.isRegistredInstance(instanceMaskId)) {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANT_INSTANCE_ENTER_MEMBER(member.getName()));
						return false;
					}
					if (agt.isHarmonyArena() && !PvPArenaService.checkItem(member, agt)) {
						PacketSendUtility.sendPacket(member, new SM_SYSTEM_MESSAGE(1400219, agt.getInstanceMapId()));
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANT_INSTANCE_ENTER_MEMBER(member.getName()));
						return false;
					}
					if (agt.isDredgion() && DredgionService.getInstance().hasCoolDown(member)) {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANT_INSTANCE_ENTER_MEMBER(member.getName()));
						return false;
					}
					else if (agt.isKamar() && KamarBattlefieldService.getInstance().hasCoolDown(member)) {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANT_INSTANCE_ENTER_MEMBER(member.getName()));
						return false;
					}
					else if (agt.isJormungand() && JormungandService.getInstance().hasCoolDown(member)) {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANT_INSTANCE_ENTER_MEMBER(member.getName()));
						return false;
					}
					else if (agt.isSteelWall() && SteelWallBastionBattlefieldService.getInstance().hasCoolDown(member)) {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANT_INSTANCE_ENTER_MEMBER(member.getName()));
						return false;
					}
					else if (agt.isRunatorium() && RunatoriumService.getInstance().hasCoolDown(member)) {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANT_INSTANCE_ENTER_MEMBER(member.getName()));
						return false;
					}
					else if (agt.isBalaurMarching() && BalaurMarchingRouteService.getInstance().hasCoolDown(member)) {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANT_INSTANCE_ENTER_MEMBER(member.getName()));
						return false;
					}
					else if (agt.isRunatoriumRuins() && RunatoriumRuinsService.getInstance().hasCoolDown(member)) {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANT_INSTANCE_ENTER_MEMBER(member.getName()));
						return false;
					}
					else if (agt.isGoldenCrucible() && GoldenCrucibleService.getInstance().hasCoolDown(member)) {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANT_INSTANCE_ENTER_MEMBER(member.getName()));
						return false;
					}
					else if (agt.isSanctumBattlefield() && SanctumBattlefieldService.getInstance().hasCoolDown(member)) {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANT_INSTANCE_ENTER_MEMBER(member.getName()));
						return false;
					}
					else if (agt.isPandaemoniumBattlefield() && PandaemoniumBattlefieldService.getInstance().hasCoolDown(member)) {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANT_INSTANCE_ENTER_MEMBER(member.getName()));
						return false;
					}
					else if (hasCoolDown(member, mapId)) {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANT_INSTANCE_ENTER_MEMBER(member.getName()));
						return false;
					}
					if (!agt.hasLevelPermit(member.getLevel())) {
						PacketSendUtility.sendPacket(member, SM_SYSTEM_MESSAGE.STR_MSG_CANT_INSTANCE_ENTER_LEVEL);
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANT_INSTANCE_ENTER_MEMBER(member.getName()));
						return false;
					}
				}
				break;
		}
		return true;
	}

	private AutoInstance getAutoInstance(Player player, int instanceMaskId) {
		for (AutoInstance autoInstance : autoInstances.values()) {
			if (autoInstance.agt.getInstanceMaskId() == instanceMaskId && autoInstance.players.containsKey(player.getObjectId())) {
				return autoInstance;
			}
		}
		return null;
	}

	private boolean hasCoolDown(Player player, int worldId) {
		int instanceCooldownRate = InstanceService.getInstanceRate(player, worldId);
		int useDelay = 0;
		int instanceCooldown = 0;
		InstanceCooltime clt = DataManager.INSTANCE_COOLTIME_DATA.getInstanceCooltimeByWorldId(worldId);
		if (clt != null) {
			instanceCooldown = clt.getEntCoolTime();
		}
		if (instanceCooldownRate > 0) {
			useDelay = instanceCooldown / instanceCooldownRate;
		}
		return player.getPortalCooldownList().isPortalUseDisabled(worldId) && useDelay > 0;
	}

	private WorldMapInstance createInstance(int worldId, byte difficultId) {
		WorldMap map = World.getInstance().getWorldMap(worldId);
		int nextInstanceId = map.getNextInstanceId();
		WorldMapInstance worldMapInstance = WorldMapInstanceFactory.createWorldMapInstance(map, nextInstanceId);
		map.addInstance(nextInstanceId, worldMapInstance);
		SpawnEngine.spawnInstance(worldId, worldMapInstance.getInstanceId(), difficultId);
		InstanceEngine.getInstance().onInstanceCreate(worldMapInstance);
		return worldMapInstance;
	}

	private void startPenalty(final Integer obj) {
		if (penaltys.contains(obj)) {
			penaltys.remove(obj);
		}
		penaltys.add(obj);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (penaltys.contains(obj)) {
					penaltys.remove(obj);
				}
			}
		}, 10000);
	}

	public void unRegisterInstance(int instanceMaskId) {
		for (LookingForParty lfp : searchers.values()) {
			if (lfp.isRegistredInstance(instanceMaskId)) {
				if (lfp.getPlayer() != null) {
					getInstance().unregisterLooking(lfp.getPlayer(), instanceMaskId);
				}
				else {
					getInstance().unRegisterSearchInstance(null, lfp.getSearchInstance(instanceMaskId));
					if (lfp.unregisterInstance(instanceMaskId) == 0) {
						searchers.values().remove(lfp);
					}
				}
			}
		}
	}

	private void unRegisterSearchInstance(Player player, SearchInstance si) {
		int instanceMaskId = si.getInstanceMaskId();
		if (si.getEntryRequestType().isGroupEntry() && si.getMembers() != null) {
			for (Integer obj : si.getMembers()) {
				Player member = World.getInstance().findPlayer(obj);
				if (member != null) {
					if (si.isDredgion() && DredgionService.getInstance().isDredgionAvailable()) {
						PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6));
					}
					else if (si.isKamar() && KamarBattlefieldService.getInstance().isKamarAvailable()) {
						PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6));
					}
					else if (si.isJormungand() && JormungandService.getInstance().isJormungandAvailable()) {
						PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6));
					}
					else if (si.isBastion() && SteelWallBastionBattlefieldService.getInstance().isSteelWallAvailable()) {
						PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6));
					}
					else if (si.isRunatorium() && RunatoriumService.getInstance().isRunatoriumAvailable()) {
						PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6));
					}
					else if (si.isBalaurMarching() && BalaurMarchingRouteService.getInstance().isBalaurMarchingAvailable()) {
						PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6));
					}
					else if (si.isRunatoriumRuins() && RunatoriumRuinsService.getInstance().isRunatoriumRuinsAvailable()) {
						PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6));
					}
					else if (si.isGoldenCrusible() && GoldenCrucibleService.getInstance().isGoldenCrucibleAvailable()) {
						PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6));
					}
					else if (si.isSanctumBattlefield() && SanctumBattlefieldService.getInstance().isSanctumBattlefieldAvailable()) {
						PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6));
					}
					else if (si.isPandaemoniumBattlefield() && PandaemoniumBattlefieldService.getInstance().isPandaemoniumBattlefieldAvailable()) {
						PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6));
					}
					PacketSendUtility.sendPacket(member, new SM_AUTO_GROUP(instanceMaskId, 2));
				}
			}
		}
		if (player != null) {
			if (si.isDredgion() && DredgionService.getInstance().isDredgionAvailable()) {
				PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6));
			}
			else if (si.isKamar() && KamarBattlefieldService.getInstance().isKamarAvailable()) {
				PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6));
			}
			else if (si.isJormungand() && JormungandService.getInstance().isJormungandAvailable()) {
				PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6));
			}
			else if (si.isBastion() && SteelWallBastionBattlefieldService.getInstance().isSteelWallAvailable()) {
				PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6));
			}
			else if (si.isRunatorium() && RunatoriumService.getInstance().isRunatoriumAvailable()) {
				PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6));
			}
			else if (si.isBalaurMarching() && BalaurMarchingRouteService.getInstance().isBalaurMarchingAvailable()) {
				PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6));
			}
			else if (si.isRunatoriumRuins() && RunatoriumRuinsService.getInstance().isRunatoriumRuinsAvailable()) {
				PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6));
			}
			else if (si.isGoldenCrusible() && GoldenCrucibleService.getInstance().isGoldenCrucibleAvailable()) {
				PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6));
			}
			else if (si.isSanctumBattlefield() && SanctumBattlefieldService.getInstance().isSanctumBattlefieldAvailable()) {
				PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6));
			}
			else if (si.isPandaemoniumBattlefield() && PandaemoniumBattlefieldService.getInstance().isPandaemoniumBattlefieldAvailable()) {
				PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6));
			}
			PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 2));
		}
	}

	public void unRegisterInstance(Integer instanceId) {
		AutoInstance autoInstance = autoInstances.remove(instanceId);
		if (autoInstance != null) {
			WorldMapInstance instance = autoInstance.instance;
			if (instance != null) {
				InstanceService.destroyInstance(instance);
			}
			autoInstance.clear();
		}
	}

	/**
	 * When the player enters any instance, the system should automatically remove the AutoGroup Entry Icon
	 */
	private void setAutoGroupEntryIcon(Player player, Boolean removeit) {
		// Dredgion, ChantraDredgion,TerathDredgion,
		PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(1, 6, removeit));
		PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(2, 6, removeit));
		PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(3, 6, removeit));
		// Kamar BattleField
		PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(107, 6, removeit));
		// Jormungand Marching Route
		PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(108, 6, removeit));
		// Steel Wall Bastion Battlefield
		PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(109, 6, removeit));
		// Runatorium
		PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(111, 6, removeit));
		// Ashunatal Dredgion
		PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(121, 6, removeit));
		// Balaur Marching Route
		PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(122, 6, removeit));
		// Runatorium Ruins
		PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(123, 6, removeit));
		// Golden Crusible
		PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(125, 6, removeit));
		// Sanctum Battlefield
		PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(416, 6, removeit));
		// Pandaemonium Battlefield
		PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(417, 6, removeit));
	}

	public boolean isAutoInstance(int instanceId) {
		return autoInstances.containsKey(instanceId);
	}

	public static AutoGroupService getInstance() {
		return NewSingletonHolder.INSTANCE;
	}

	private static class NewSingletonHolder {

		private static final AutoGroupService INSTANCE = new AutoGroupService();
	}
}
