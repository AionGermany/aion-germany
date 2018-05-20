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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.commons.services.CronService;
import com.aionemu.gameserver.GameServer;
import com.aionemu.gameserver.configs.main.SiegeConfig;
import com.aionemu.gameserver.configs.schedule.SiegeSchedule;
import com.aionemu.gameserver.dao.SiegeDAO;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.siege.SiegeNpc;
import com.aionemu.gameserver.model.siege.ArtifactLocation;
import com.aionemu.gameserver.model.siege.FortressLocation;
import com.aionemu.gameserver.model.siege.Influence;
import com.aionemu.gameserver.model.siege.SiegeLocation;
import com.aionemu.gameserver.model.siege.SiegeModType;
import com.aionemu.gameserver.model.siege.SiegeRace;
import com.aionemu.gameserver.model.siege.SourceLocation;
import com.aionemu.gameserver.model.stats.container.NpcLifeStats;
import com.aionemu.gameserver.model.templates.npc.NpcRating;
import com.aionemu.gameserver.model.templates.npc.NpcTemplate;
import com.aionemu.gameserver.model.templates.spawns.SpawnGroup2;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.siegespawns.SiegeSpawnTemplate;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ABYSS_ARTIFACT_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_FORTRESS_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_FORTRESS_STATUS;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INFLUENCE_RATIO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SHIELD_EFFECT;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SIEGE_LOCATION_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_UNK_127;
import com.aionemu.gameserver.services.siegeservice.ArtifactSiege;
import com.aionemu.gameserver.services.siegeservice.FortressSiege;
import com.aionemu.gameserver.services.siegeservice.Siege;
import com.aionemu.gameserver.services.siegeservice.SiegeException;
import com.aionemu.gameserver.services.siegeservice.SiegeStartRunnable;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import javolution.util.FastMap;

/**
 * 3.0 siege update (https://docs.google.com/document/d/1HVOw8-w9AlRp4ci0ei4iAzNaSKzAHj_xORu-qIQJFmc/edit#)
 *
 * @author SoulKeeper, Source
 */
public class SiegeService {

	/**
	 * Just a logger
	 */
	private static final Logger log = LoggerFactory.getLogger("SIEGE_LOG");
	/**
	 * We should broadcast fortress status every hour Actually only influence packet must be sent, but that doesn't matter
	 */
	private static final String SIEGE_LOCATION_STATUS_BROADCAST_SCHEDULE = "0 0 * ? * *";
	/**
	 * Singleton that is loaded on the class initialization. Guys, we really do not SingletonHolder classes
	 */
	private static final SiegeService instance = new SiegeService();
	/**
	 * Map that holds fortressId to Siege. We can easily know what fortresses is under siege ATM :)
	 */
	private final Map<Integer, Siege<?>> activeSieges = new FastMap<Integer, Siege<?>>().shared();
	/**
	 * Object that holds siege schedule.<br>
	 * And maybe other useful information (in future).
	 */
	private SiegeSchedule siegeSchedule;
	// Player list on RVR Event.
	private List<Player> rvrPlayersOnEvent = new ArrayList<Player>();

	/**
	 * Returns the single instance of siege service
	 *
	 * @return siege service instance
	 */
	public static SiegeService getInstance() {
		return instance;
	}

	private Map<Integer, ArtifactLocation> artifacts;
	private Map<Integer, FortressLocation> fortresses;
	private Map<Integer, SiegeLocation> locations;

	/**
	 * Initializer. Should be called once.
	 */
	public void initSiegeLocations() {
		if (SiegeConfig.SIEGE_ENABLED) {

			if (siegeSchedule != null) {
				log.error("[SiegeService] SiegeService should not be initialized two times!");
				return;
			}

			// initialize current siege locations
			artifacts = DataManager.SIEGE_LOCATION_DATA.getArtifacts();
			fortresses = DataManager.SIEGE_LOCATION_DATA.getFortress();
			locations = DataManager.SIEGE_LOCATION_DATA.getSiegeLocations();
			DAOManager.getDAO(SiegeDAO.class).loadSiegeLocations(locations);
			log.info("[SiegeService] Loaded " + locations.size() + " siege locations");
		}
		else {
			artifacts = Collections.emptyMap();
			fortresses = Collections.emptyMap();
			locations = Collections.emptyMap();
			log.info("Sieges are disabled in config.");
		}
	}

	public void initSieges() {
		if (!SiegeConfig.SIEGE_ENABLED) {
			return;
		}

		GameServer.log.info("[SiegeService] started ...");

		// despawn all NPCs spawned by spawn engine.
		// Siege spawns should be controlled by siege service
		for (Integer i : getSiegeLocations().keySet()) {
			deSpawnNpcs(i);
		}

		// spawn fortress common npcs
		for (FortressLocation f : getFortresses().values()) {
			spawnNpcs(f.getLocationId(), f.getRace(), SiegeModType.PEACE);
		}

		// spawn artifacts
		for (ArtifactLocation a : getStandaloneArtifacts().values()) {
			spawnNpcs(a.getLocationId(), a.getRace(), SiegeModType.PEACE);
		}

		// initialize siege schedule
		siegeSchedule = SiegeSchedule.load();

		// Schedule fortresses sieges protector spawn
		for (final SiegeSchedule.Fortress f : siegeSchedule.getFortressesList()) {
			for (String siegeTime : f.getSiegeTimes()) {
				CronService.getInstance().schedule(new SiegeStartRunnable(f.getId()), siegeTime);
				log.debug("[SiegeService] Scheduled siege of fortressID " + f.getId() + " based on cron expression: " + siegeTime);
			}
		}

		// Start siege of artifacts
		for (ArtifactLocation artifact : artifacts.values()) {
			if (artifact.isStandAlone()) {
				log.debug("[SiegeService] Starting siege of artifact #" + artifact.getLocationId());
				startSiege(artifact.getLocationId());
			}
			else {
				log.debug("[SiegeService] Artifact #" + artifact.getLocationId() + " siege was not started, it belongs to fortress");
			}
		}

		// We should set valid next state for fortress on startup
		// no need to broadcast state here, no players @ server ATM
		updateFortressNextState();

		// Schedule siege status broadcast (every hour)
		CronService.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				updateFortressNextState();
				World.getInstance().doOnAllPlayers(new Visitor<Player>() {

					@Override
					public void visit(Player player) {
						for (FortressLocation fortress : getFortresses().values()) {
							PacketSendUtility.sendPacket(player, new SM_FORTRESS_INFO(fortress.getLocationId(), false));
						}

						PacketSendUtility.sendPacket(player, new SM_FORTRESS_STATUS());

						for (FortressLocation fortress : getFortresses().values()) {
							PacketSendUtility.sendPacket(player, new SM_FORTRESS_INFO(fortress.getLocationId(), true));
						}
					}
				});
			}
		}, SIEGE_LOCATION_STATUS_BROADCAST_SCHEDULE);
		log.debug("[SiegeService] Broadcasting Siege Location status based on expression: " + SIEGE_LOCATION_STATUS_BROADCAST_SCHEDULE);
	}

	public void checkSiegeStart(final int locationId) {
		startSiege(locationId);
	}

	public void startSiege(final int siegeLocationId) {
		log.debug("[SiegeService] Starting siege of siege location: " + siegeLocationId);

		// Siege should not be started two times. Never.
		Siege<?> siege;
		synchronized (this) {
			if (activeSieges.containsKey(siegeLocationId)) {
				log.error("[SiegeService] Attempt to start siege twice for siege location: " + siegeLocationId);
				return;
			}
			siege = newSiege(siegeLocationId);
			activeSieges.put(siegeLocationId, siege);
		}

		siege.startSiege();

		// certain sieges are endless
		// should end only manually on siege boss death
		if (siege.isEndless()) {
			return;
		}

		// schedule siege end
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				stopSiege(siegeLocationId);
			}
		}, siege.getSiegeLocation().getSiegeDuration() * 1000);
	}

	public void stopSiege(int siegeLocationId) {
		log.debug("[SiegeService] Stopping siege of siege location: " + siegeLocationId);

		// Just a check here...
		// If fortresses was captured in 99% the siege timer will return here
		// without concurrent race
		if (!isSiegeInProgress(siegeLocationId)) {
			log.debug("[SiegeService] Siege of siege location " + siegeLocationId + " is not in progress, it was captured earlier?");
			return;
		}

		// We need synchronization here for that 1% of cases :)
		// It may happen that fortresses siege is stopping in the same time by 2 different threads
		// 1 is for killing the boss
		// 2 is for the schedule
		// it might happen that siege will be stopping by other thread, but in such case siege object will be null
		Siege<?> siege;
		synchronized (this) {
			siege = activeSieges.remove(siegeLocationId);
		}
		if (siege == null || siege.isFinished()) {
			return;
		}

		siege.stopSiege();
	}

	/**
	 * Updates next state for fortresses
	 */
	protected void updateFortressNextState() {
		// get current hour and add 1 hour
		Calendar currentHourPlus1 = Calendar.getInstance();
		currentHourPlus1.set(Calendar.MINUTE, 0);
		currentHourPlus1.set(Calendar.SECOND, 0);
		currentHourPlus1.set(Calendar.MILLISECOND, 0);
		currentHourPlus1.add(Calendar.HOUR, 1);

		// filter fortress siege start runnables
		Map<Runnable, JobDetail> siegeStartRunables = CronService.getInstance().getRunnables();
		siegeStartRunables = Maps.filterKeys(siegeStartRunables, new Predicate<Runnable>() {

			@Override
			public boolean apply(@Nullable Runnable runnable) {
				return runnable instanceof SiegeStartRunnable;
			}
		});

		// Create map FortressId-To-AllTriggers
		Map<Integer, List<Trigger>> siegeIdToStartTriggers = Maps.newHashMap();
		for (Map.Entry<Runnable, JobDetail> entry : siegeStartRunables.entrySet()) {
			SiegeStartRunnable fssr = (SiegeStartRunnable) entry.getKey();

			List<Trigger> storage = siegeIdToStartTriggers.get(fssr.getLocationId());
			if (storage == null) {
				storage = Lists.newArrayList();
				siegeIdToStartTriggers.put(fssr.getLocationId(), storage);
			}
			storage.addAll(CronService.getInstance().getJobTriggers(entry.getValue()));
		}

		// update each fortress next state
		for (Map.Entry<Integer, List<Trigger>> entry : siegeIdToStartTriggers.entrySet()) {
			List<Date> nextFireDates = Lists.newArrayListWithCapacity(entry.getValue().size());
			for (Trigger trigger : entry.getValue()) {
				nextFireDates.add(trigger.getNextFireTime());
			}
			Collections.sort(nextFireDates);

			// clear non-required times
			Date nextSiegeDate = nextFireDates.get(0);
			Calendar siegeStartHour = Calendar.getInstance();
			siegeStartHour.setTime(nextSiegeDate);
			siegeStartHour.set(Calendar.MINUTE, 0);
			siegeStartHour.set(Calendar.SECOND, 0);
			siegeStartHour.set(Calendar.MILLISECOND, 0);

			// update fortress state that will be valid in 1 h
			SiegeLocation fortress = getSiegeLocation(entry.getKey());
			// check if siege duration is > than 1 Hour
			Calendar siegeCalendar = Calendar.getInstance();
			siegeCalendar.set(Calendar.MINUTE, 0);
			siegeCalendar.set(Calendar.SECOND, 0);
			siegeCalendar.set(Calendar.MILLISECOND, 0);
			siegeCalendar.add(Calendar.HOUR, 0);
			siegeCalendar.add(Calendar.SECOND, getRemainingSiegeTimeInSeconds(fortress.getLocationId()));

			if (fortress instanceof SourceLocation) {
				siegeStartHour.add(Calendar.HOUR, 1);
			}

			if (currentHourPlus1.getTimeInMillis() == siegeStartHour.getTimeInMillis() || siegeCalendar.getTimeInMillis() > currentHourPlus1.getTimeInMillis()) {
				fortress.setNextState(SiegeLocation.STATE_VULNERABLE);
			}
			else {
				fortress.setNextState(SiegeLocation.STATE_INVULNERABLE);
			}
		}
	}

	/**
	 * @return seconds before hour end
	 */
	public int getSecondsBeforeHourEnd() {
		Calendar c = Calendar.getInstance();
		int minutesAsSeconds = c.get(Calendar.MINUTE) * 60;
		int seconds = c.get(Calendar.SECOND);
		return 3600 - (minutesAsSeconds + seconds);
	}

	/**
	 * TODO: Check if it's valid
	 * <p/>
	 * If siege duration is endless - will return -1
	 *
	 * @param siegeLocationId
	 *            Scheduled siege end time
	 * @return remaining seconds in current hour
	 */
	public int getRemainingSiegeTimeInSeconds(int siegeLocationId) {
		Siege<?> siege = getSiege(siegeLocationId);
		if (siege == null || siege.isFinished())
			return 0;

		if (!siege.isStarted())
			return siege.getSiegeLocation().getSiegeDuration();
		// endless siege
		if (siege.getSiegeLocation().getSiegeDuration() == -1)
			return -1;

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.SECOND, siege.getSiegeLocation().getSiegeDuration());

		int result = (int) ((calendar.getTimeInMillis() - System.currentTimeMillis()) / 1000);
		return (result > 0) ? result : 0;
	}

	public Siege<?> getSiege(SiegeLocation loc) {
		return activeSieges.get(loc.getLocationId());
	}

	public Siege<?> getSiege(Integer siegeLocationId) {
		return activeSieges.get(siegeLocationId);
	}

	public boolean isSiegeInProgress(int fortressId) {
		return activeSieges.containsKey(fortressId);
	}

	public Map<Integer, FortressLocation> getFortresses() {
		return fortresses;
	}

	public FortressLocation getFortress(int id) {
		return fortresses.get(id);
	}

	public Map<Integer, ArtifactLocation> getArtifacts() {
		return artifacts;
	}

	public ArtifactLocation getArtifact(int id) {
		return getArtifacts().get(id);
	}

	public Map<Integer, ArtifactLocation> getStandaloneArtifacts() {
		return Maps.filterValues(artifacts, new Predicate<ArtifactLocation>() {

			@Override
			public boolean apply(@Nullable ArtifactLocation input) {
				return input != null && input.isStandAlone();
			}
		});
	}

	public Map<Integer, ArtifactLocation> getFortressArtifacts() {
		return Maps.filterValues(artifacts, new Predicate<ArtifactLocation>() {

			@Override
			public boolean apply(@Nullable ArtifactLocation input) {
				return input != null && input.getOwningFortress() != null;
			}
		});
	}

	public Map<Integer, SiegeLocation> getSiegeLocations() {
		return locations;
	}

	public SiegeLocation getSiegeLocation(int id) {
		return locations.get(id);
	}

	public Map<Integer, SiegeLocation> getSiegeLocations(int worldId) {
		Map<Integer, SiegeLocation> mapLocations = new FastMap<Integer, SiegeLocation>();
		for (SiegeLocation location : getSiegeLocations().values()) {
			if (location.getWorldId() == worldId) {
				mapLocations.put(location.getLocationId(), location);
			}
		}

		return mapLocations;
	}

	protected Siege<?> newSiege(int siegeLocationId) {
		if (fortresses.containsKey(siegeLocationId)) {
			return new FortressSiege(fortresses.get(siegeLocationId));
		}
		else if (artifacts.containsKey(siegeLocationId)) {
			return new ArtifactSiege(artifacts.get(siegeLocationId));
		}
		else {
			throw new SiegeException("[SiegeService] Unknown siege handler for siege location: " + siegeLocationId);
		}
	}

	public void cleanLegionId(int legionId) {
		for (SiegeLocation loc : this.getSiegeLocations().values()) {
			if (loc.getLegionId() == legionId) {
				loc.setLegionId(0);
				break;
			}
		}
	}

	public void spawnNpcs(int siegeLocationId, SiegeRace race, SiegeModType type) {
		List<SpawnGroup2> siegeSpawns = DataManager.SPAWNS_DATA2.getSiegeSpawnsByLocId(siegeLocationId);
		if (siegeSpawns == null) {
			return;
		}
		for (SpawnGroup2 group : siegeSpawns) {
			for (SpawnTemplate template : group.getSpawnTemplates()) {
				SiegeSpawnTemplate siegetemplate = (SiegeSpawnTemplate) template;
				if (siegetemplate.getSiegeRace().equals(race) && siegetemplate.getSiegeModType().equals(type)) {
					Npc npc = (Npc) SpawnEngine.spawnObject(siegetemplate, 1);
					if (SiegeConfig.SIEGE_HEALTH_MOD_ENABLED) {
						NpcTemplate templ = npc.getObjectTemplate();
						if (templ.getRating().equals(NpcRating.LEGENDARY)) {
							NpcLifeStats life = npc.getLifeStats();
							int maxHpPercent = (int) (life.getMaxHp() * SiegeConfig.SIEGE_HEALTH_MULTIPLIER);
							templ.getStatsTemplate().setMaxHp(maxHpPercent);
							life.setCurrentHpPercent(100);
						}
					}
				}
			}
		}
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				player.getController().updateNearbyQuests();
			}
		});
	}

	public void deSpawnNpcs(int siegeLocationId) {
		Collection<SiegeNpc> siegeNpcs = World.getInstance().getLocalSiegeNpcs(siegeLocationId);
		for (SiegeNpc npc : siegeNpcs) {
			npc.getController().onDelete();
		}
	}

	public boolean isSiegeNpcInActiveSiege(Npc npc) {
		if (npc instanceof SiegeNpc) {
			FortressLocation fort = getFortress(((SiegeNpc) npc).getSiegeId());
			if (fort != null) {
				if (fort.isVulnerable()) {
					return true;
				}
				else if (fort.getNextState() == 1) {
					return npc.getSpawn().getRespawnTime() >= getSecondsBeforeHourEnd();
				}
			}
		}
		return false;
	}

	public void broadcastUpdate() {
		broadcast(new SM_SIEGE_LOCATION_INFO(), null);
	}

	public void broadcastUpdate(SiegeLocation loc) {
		Influence.getInstance().recalculateInfluence();
		broadcast(new SM_SIEGE_LOCATION_INFO(loc), new SM_INFLUENCE_RATIO());
	}

	public void broadcast(final AionServerPacket pkt1, final AionServerPacket pkt2) {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				fortressBuffRemove(player);
				fortressBuffApply(player);
				if (pkt1 != null) {
					PacketSendUtility.sendPacket(player, pkt1);
				}
				if (pkt2 != null) {
					PacketSendUtility.sendPacket(player, pkt2);
				}
			}
		});
	}

	public void broadcastUpdate(SiegeLocation loc, DescriptionId nameId) {
		SM_SIEGE_LOCATION_INFO pkt = new SM_SIEGE_LOCATION_INFO(loc);
		SM_SYSTEM_MESSAGE info = loc.getLegionId() == 0 ? new SM_SYSTEM_MESSAGE(1301039, loc.getRace().getDescriptionId(), nameId) : new SM_SYSTEM_MESSAGE(1301038, LegionService.getInstance().getLegion(loc.getLegionId()).getLegionName(), nameId);
		broadcast(pkt, info, loc.getRace());
	}

	private void broadcast(final AionServerPacket pkt, final AionServerPacket info, final SiegeRace race) {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				fortressBuffRemove(player);
				fortressBuffApply(player);
				if (player.getRace().getRaceId() == race.getRaceId()) {
					PacketSendUtility.sendPacket(player, info);
				}
				PacketSendUtility.sendPacket(player, pkt);
			}
		});
	}

	public void onPlayerLogin(final Player player) {
		// not on login
		// PacketSendUtility.sendPacket(player, new SM_ABYSS_ARTIFACT_INFO(getSiegeLocations().values()));
		// PacketSendUtility.sendPacket(player, new SM_ABYSS_ARTIFACT_INFO2(getSiegeLocations().values()));

		// Chk login when teleporter is dead
		// for (FortressLocation loc : getFortresses().values()) {
		// // remove teleportation to dead teleporters
		// if (!loc.isCanTeleport(player))
		// PacketSendUtility.sendPacket(player, new SM_FORTRESS_INFO(loc.getLocationId(), false));
		// }
		// First part will be sent to all
		if (SiegeConfig.SIEGE_ENABLED) {
			PacketSendUtility.sendPacket(player, new SM_INFLUENCE_RATIO());
			PacketSendUtility.sendPacket(player, new SM_SIEGE_LOCATION_INFO());
			PacketSendUtility.sendPacket(player, new SM_UNK_127());
		}
	}

	public void onEnterSiegeWorld(Player player) {
		// Second part only for siege world
		FastMap<Integer, SiegeLocation> worldLocations = new FastMap<Integer, SiegeLocation>();
		FastMap<Integer, ArtifactLocation> worldArtifacts = new FastMap<Integer, ArtifactLocation>();

		for (SiegeLocation location : getSiegeLocations().values()) {
			if (location.getWorldId() == player.getWorldId()) {
				worldLocations.put(location.getLocationId(), location);
			}
		}

		for (ArtifactLocation artifact : getArtifacts().values()) {
			if (artifact.getWorldId() == player.getWorldId()) {
				worldArtifacts.put(artifact.getLocationId(), artifact);
			}
		}

		PacketSendUtility.sendPacket(player, new SM_SHIELD_EFFECT(worldLocations.values()));
		PacketSendUtility.sendPacket(player, new SM_ABYSS_ARTIFACT_INFO(worldArtifacts.values()));
	}

	public void fortressBuffApply(Player player) {
		if (player.isInSiegeWorld()) {
			SiegeLocation location7011 = getSiegeLocation(7011);
			SiegeLocation location10111 = getSiegeLocation(10111);
			SiegeLocation location10211 = getSiegeLocation(10211);
			SiegeLocation location10311 = getSiegeLocation(10311);
			SiegeLocation location10411 = getSiegeLocation(10411);
			if (player.getWorldId() == 400020000) {
				if (location10111.getRace() == SiegeRace.getByRace(player.getRace())) {
					// Gab01's Commendation
					SkillEngine.getInstance().applyEffectDirectly(12160, player, player, 0);
				}
				else if (location10111.getRace() != SiegeRace.getByRace(player.getRace()) && location10111.getRace() != SiegeRace.BALAUR) {
					// Gab01's Encouragement
					SkillEngine.getInstance().applyEffectDirectly(12161, player, player, 0);
				}
			}
			else if (player.getWorldId() == 400040000) {
				if (location10211.getRace() == SiegeRace.getByRace(player.getRace())) {
					// Gab02's Commendation
					SkillEngine.getInstance().applyEffectDirectly(12162, player, player, 0);
				}
				else if (location10211.getRace() != SiegeRace.getByRace(player.getRace()) && location10211.getRace() != SiegeRace.BALAUR) {
					// Gab02's Encouragement
					SkillEngine.getInstance().applyEffectDirectly(12163, player, player, 0);
				}
			}
			else if (player.getWorldId() == 400050000) {
				if (location10311.getRace() == SiegeRace.getByRace(player.getRace())) {
					// Gab03's Commendation
					SkillEngine.getInstance().applyEffectDirectly(12164, player, player, 0);
				}
				else if (location10311.getRace() != SiegeRace.getByRace(player.getRace()) && location10311.getRace() != SiegeRace.BALAUR) {
					// Gab03's Encouragement
					SkillEngine.getInstance().applyEffectDirectly(12165, player, player, 0);
				}
			}
			else if (player.getWorldId() == 400060000) {
				if (location10411.getRace() == SiegeRace.getByRace(player.getRace())) {
					// Gab04's Commendation
					SkillEngine.getInstance().applyEffectDirectly(12166, player, player, 0);
				}
				else if (location10411.getRace() != SiegeRace.getByRace(player.getRace()) && location10411.getRace() != SiegeRace.BALAUR) {
					// Gab04's Encouragement
					SkillEngine.getInstance().applyEffectDirectly(12167, player, player, 0);
				}
			}
			else if (player.getWorldId() == 600090000) {
				if (location7011.getRace() == SiegeRace.getByRace(player.getRace())) {
					// Kaldor's Commendation
					SkillEngine.getInstance().applyEffectDirectly(12168, player, player, 0);
				}
				else if (location7011.getRace() != SiegeRace.getByRace(player.getRace()) && location7011.getRace() != SiegeRace.BALAUR) {
					// Kaldor's Encouragement
					SkillEngine.getInstance().applyEffectDirectly(12169, player, player, 0);
				}
			}
		}
	}

	public void fortressBuffRemove(Player player) {
		if (player.getEffectController().hasAbnormalEffect(12161)) {
			player.getEffectController().removeEffect(12161);
		}
		else if (player.getEffectController().hasAbnormalEffect(12162)) {
			player.getEffectController().removeEffect(12163);
		}
		else if (player.getEffectController().hasAbnormalEffect(12163)) {
			player.getEffectController().removeEffect(12163);
		}
		else if (player.getEffectController().hasAbnormalEffect(12164)) {
			player.getEffectController().removeEffect(12164);
		}
		else if (player.getEffectController().hasAbnormalEffect(12165)) {
			player.getEffectController().removeEffect(12165);
		}
		else if (player.getEffectController().hasAbnormalEffect(12166)) {
			player.getEffectController().removeEffect(12166);
		}
		else if (player.getEffectController().hasAbnormalEffect(12167)) {
			player.getEffectController().removeEffect(12167);
		}
		else if (player.getEffectController().hasAbnormalEffect(12168)) {
			player.getEffectController().removeEffect(12168);
		}
		else if (player.getEffectController().hasAbnormalEffect(12169)) {
			player.getEffectController().removeEffect(12169);
		}
	}

	public int getFortressId(int locId) {
		switch (locId) {
            case 49:
            case 61:
                return 1011; // Divine Fortress
			case 36:
			case 54:
				return 1131; // Siel's Western Fortress
			case 37:
			case 55:
				return 1132; // Siel's Eastern Fortress
			case 39:
			case 56:
				return 1141; // Sulfur Archipelago
			case 45:
			case 57:
			case 72:
			case 75:
				return 1221; // Krotan Refuge
			case 46:
			case 58:
			case 73:
			case 76:
				return 1231; // Kysis Fortress
			case 47:
			case 59:
			case 74:
			case 77:
				return 1241; // Miren Fortress

			case 102:
				return 7011; // Anoha Fortress

			case 103:
				return 10111; // Belus Fortress
			case 104:
				return 10211; // Aspida Fortress
			case 105:
				return 10311; // Atanaos Fortress
			case 106:
				return 10411; // Disillon Fortress
		}
		return 0;
	}

	// return RVR Event players list
	public List<Player> getRvrPlayersOnEvent() {
		return rvrPlayersOnEvent;
	}

	// check if player is in RVR event list, if not the player is added.
	public void checkRvrPlayerOnEvent(Player player) {
		if (player != null && !rvrPlayersOnEvent.contains(player)) {
			rvrPlayersOnEvent.add(player);
		}
	}

	// clear RVR event players list
	public void clearRvrPlayersOnEvent() {
		rvrPlayersOnEvent = new ArrayList<Player>();
	}
}
