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
package com.aionemu.gameserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.DatabaseFactory;
import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.commons.network.NioServer;
import com.aionemu.commons.network.ServerCfg;
import com.aionemu.commons.services.CronService;
import com.aionemu.commons.utils.AEInfos;
import com.aionemu.gameserver.ai2.AI2Engine;
import com.aionemu.gameserver.ai2.manager.LookManager;
import com.aionemu.gameserver.cache.HTMLCache;
import com.aionemu.gameserver.configs.Config;
import com.aionemu.gameserver.configs.main.AIConfig;
import com.aionemu.gameserver.configs.main.AutoGroupConfig;
import com.aionemu.gameserver.configs.main.BaseConfig;
import com.aionemu.gameserver.configs.main.ConquerorProtectorConfig;
import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.configs.main.EventsConfig;
import com.aionemu.gameserver.configs.main.GSConfig;
import com.aionemu.gameserver.configs.main.MembershipConfig;
import com.aionemu.gameserver.configs.main.PanesterraConfig;
import com.aionemu.gameserver.configs.main.SiegeConfig;
import com.aionemu.gameserver.configs.main.ThreadConfig;
import com.aionemu.gameserver.configs.main.WeddingsConfig;
import com.aionemu.gameserver.configs.network.NetworkConfig;
import com.aionemu.gameserver.dao.PlayerDAO;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.instance.InstanceEngine;
import com.aionemu.gameserver.model.GameEngine;
import com.aionemu.gameserver.model.house.MaintenanceTask;
import com.aionemu.gameserver.model.siege.Influence;
import com.aionemu.gameserver.network.BannedHDDManager;
import com.aionemu.gameserver.network.BannedMacManager;
import com.aionemu.gameserver.network.NetworkBannedManager;
import com.aionemu.gameserver.network.aion.GameConnectionFactoryImpl;
import com.aionemu.gameserver.network.chatserver.ChatServer;
import com.aionemu.gameserver.network.loginserver.LoginServer;
import com.aionemu.gameserver.questEngine.QuestEngine;
import com.aionemu.gameserver.services.AbyssLandingService;
import com.aionemu.gameserver.services.AbyssLandingSpecialService;
import com.aionemu.gameserver.services.AdminService;
import com.aionemu.gameserver.services.AgentFightService;
import com.aionemu.gameserver.services.AnnouncementService;
import com.aionemu.gameserver.services.BaseService;
import com.aionemu.gameserver.services.BeritraService;
import com.aionemu.gameserver.services.BrokerService;
import com.aionemu.gameserver.services.ChallengeTaskService;
import com.aionemu.gameserver.services.DatabaseCleaningService;
import com.aionemu.gameserver.services.DebugService;
import com.aionemu.gameserver.services.DiflodoxService;
import com.aionemu.gameserver.services.DiflonaxService;
import com.aionemu.gameserver.services.DisputeLandService;
import com.aionemu.gameserver.services.ExchangeService;
import com.aionemu.gameserver.services.FlyRingService;
import com.aionemu.gameserver.services.GameTimeService;
import com.aionemu.gameserver.services.HousingBidService;
import com.aionemu.gameserver.services.LimitedItemTradeService;
import com.aionemu.gameserver.services.MoltenusService;
import com.aionemu.gameserver.services.NpcShoutsService;
import com.aionemu.gameserver.services.PeriodicSaveService;
import com.aionemu.gameserver.services.RestartService;
import com.aionemu.gameserver.services.RiftService;
import com.aionemu.gameserver.services.RoadService;
import com.aionemu.gameserver.services.RvrService;
import com.aionemu.gameserver.services.ShieldService;
import com.aionemu.gameserver.services.SiegeService;
import com.aionemu.gameserver.services.StigmaService;
import com.aionemu.gameserver.services.SupportService;
import com.aionemu.gameserver.services.SvsService;
import com.aionemu.gameserver.services.TownService;
import com.aionemu.gameserver.services.VortexService;
import com.aionemu.gameserver.services.WeatherService;
import com.aionemu.gameserver.services.WeddingService;
import com.aionemu.gameserver.services.WorldBuffService;
import com.aionemu.gameserver.services.abyss.AbyssRankUpdateService;
import com.aionemu.gameserver.services.abysslandingservice.LandingUpdateService;
import com.aionemu.gameserver.services.conquerer_protector.ConquerorsService;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.services.dynamic_world.AdmaPortalSpawnService;
import com.aionemu.gameserver.services.dynamic_world.LaboratoryPortalSpawnService;
import com.aionemu.gameserver.services.dynamic_world.TowerEntranceService;
import com.aionemu.gameserver.services.events.AtreianPassportService;
import com.aionemu.gameserver.services.events.BoostEventService;
import com.aionemu.gameserver.services.events.EventService;
import com.aionemu.gameserver.services.events.EventWindowService;
import com.aionemu.gameserver.services.events.ShugoSweepService;
import com.aionemu.gameserver.services.gc.GarbageCollector;
import com.aionemu.gameserver.services.instance.BalaurMarchingRouteService;
import com.aionemu.gameserver.services.instance.DredgionService;
import com.aionemu.gameserver.services.instance.GoldenCrucibleService;
import com.aionemu.gameserver.services.instance.InstanceService;
import com.aionemu.gameserver.services.instance.JormungandService;
import com.aionemu.gameserver.services.instance.KamarBattlefieldService;
import com.aionemu.gameserver.services.instance.PandaemoniumBattlefieldService;
import com.aionemu.gameserver.services.instance.RunatoriumRuinsService;
import com.aionemu.gameserver.services.instance.RunatoriumService;
import com.aionemu.gameserver.services.instance.SanctumBattlefieldService;
import com.aionemu.gameserver.services.instance.SteelWallBastionBattlefieldService;
import com.aionemu.gameserver.services.player.FatigueService;
import com.aionemu.gameserver.services.player.LunaShopService;
import com.aionemu.gameserver.services.player.PlayerEventService;
import com.aionemu.gameserver.services.player.PlayerLimitService;
import com.aionemu.gameserver.services.reward.OnlineBonus;
import com.aionemu.gameserver.services.reward.RewardService;
import com.aionemu.gameserver.services.teleport.HotspotTeleportService;
import com.aionemu.gameserver.services.territory.TerritoryService;
import com.aionemu.gameserver.services.toypet.MinionService;
import com.aionemu.gameserver.services.transfers.PlayerTransferService;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.spawnengine.TemporarySpawnEngine;
import com.aionemu.gameserver.taskmanager.fromdb.TaskFromDBManager;
import com.aionemu.gameserver.taskmanager.tasks.PacketBroadcaster;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.utils.ThreadUncaughtExceptionHandler;
import com.aionemu.gameserver.utils.Util;
import com.aionemu.gameserver.utils.ZCXInfo;
import com.aionemu.gameserver.utils.chathandlers.ChatProcessor;
import com.aionemu.gameserver.utils.cron.ThreadPoolManagerRunnableRunner;
import com.aionemu.gameserver.utils.gametime.DateTimeUtil;
import com.aionemu.gameserver.utils.gametime.GameTimeManager;
import com.aionemu.gameserver.utils.i18n.LanguageHandler;
import com.aionemu.gameserver.utils.idfactory.IDFactory;
import com.aionemu.gameserver.utils.javaagent.JavaAgentUtils;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.geo.GeoService;
import com.aionemu.gameserver.world.zone.ZoneService;

import ch.lambdaj.Lambda;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;

/**
 * <tt>GameServer </tt> is the main class of the application and represents the whole game server.<br>
 * This class is also an entry point with main() method.
 *
 * @author -Nemesiss-
 * @author SoulKeeper
 * @author cura
 * @author Alcapwnd - reworked and removed the trash
 */
public class GameServer {

	public static final Logger log = LoggerFactory.getLogger(GameServer.class);

	private static void initalizeLoggger() {
		new File("./log/backup/").mkdirs();
		File[] files = new File("log").listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".log");
			}
		});

		if (files != null && files.length > 0) {
			byte[] buf = new byte[1024];
			try {
				String outFilename = "./log/backup/" + new SimpleDateFormat("yyyy-MM-dd HHmmss").format(new Date()) + ".zip";
				ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outFilename));
				out.setMethod(ZipOutputStream.DEFLATED);
				out.setLevel(Deflater.BEST_COMPRESSION);

				for (File logFile : files) {
					FileInputStream in = new FileInputStream(logFile);
					out.putNextEntry(new ZipEntry(logFile.getName()));
					int len;
					while ((len = in.read(buf)) > 0) {
						out.write(buf, 0, len);
					}
					out.closeEntry();
					in.close();
					logFile.delete();
				}
				out.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		try {
			JoranConfigurator configurator = new JoranConfigurator();
			configurator.setContext(lc);
			lc.reset();
			configurator.doConfigure("config/slf4j-logback.xml");
		}
		catch (JoranException je) {
			throw new RuntimeException("[LoggerFactory] Failed to configure loggers, shutting down...", je);
		}
	}

	/**
	 * Launching method for GameServer
	 *
	 * @param args
	 *            arguments, not used
	 */
	public static void main(String[] args) {
		long start = System.currentTimeMillis();

		Lambda.enableJitting(true);
		final GameEngine[] parallelEngines = new GameEngine[] { QuestEngine.getInstance(), InstanceEngine.getInstance(), AI2Engine.getInstance(), ChatProcessor.getInstance() };

		final CountDownLatch progressLatch = new CountDownLatch(parallelEngines.length);
		initalizeLoggger();
		initUtilityServicesAndConfig();
		Util.printSection(" ### StaticData ### ");
		DataManager.getInstance();
		DataManager.SKILL_TREE_DATA.setStigmaTree();
		StigmaService.reparseHiddenStigmas();
		Util.printSection(" ### IDFactory ### ");
		IDFactory.getInstance();
		Util.printSection(" ### World ### ");
		ZoneService.getInstance().load(null);
		System.gc();
		World.getInstance();
		Util.printSection(" ### Luna System ### ");
		LunaShopService.getInstance().init();
		Util.printSection(" ### Minion System ### ");
		MinionService.getInstance().init();
		Util.printSection(" ### Events Window System ### ");
		EventWindowService.getInstance().initialize();
		Util.printSsSection(" ### Shugo Sweep initialization ### ");
		ShugoSweepService.getInstance().initShugoSweep();
		Util.printSection(" ### GeoData ### ");
		GeoService.getInstance().initializeGeo();
		DropRegistrationService.getInstance();
		GameServer gs = new GameServer();
		DAOManager.getDAO(PlayerDAO.class).setPlayersOffline(false);

		Util.printSection(" ### Engines ### ");
		for (int i = 0; i < parallelEngines.length; i++) {
			final int index = i;
			ThreadPoolManager.getInstance().execute(new Runnable() {

				@Override
				public void run() {
					parallelEngines[index].load(progressLatch);
				}
			});
		}

		try {
			progressLatch.await();
		}
		catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		// This is loading only siege location data
		// No Siege schedule or spawns
		Util.printSection(" ### Siege Location Data ### ");
		BaseService.getInstance().initBaseLocations();
		BeritraService.getInstance().initBeritraLocations();
		RvrService.getInstance().initRvrLocations();
		SvsService.getInstance().initSvsLocations();
		SiegeService.getInstance().initSiegeLocations();
		VortexService.getInstance().initVortexLocations();
		RiftService.getInstance().initRiftLocations();
		Util.printSection(" ### Spawns ### ");
		SpawnEngine.spawnAll();
		if (EventsConfig.EVENT_ENABLED) {
			PlayerEventService.getInstance();
		}
		
		if (EventsConfig.ENABLE_EVENT_SERVICE) {
			EventService.getInstance().start();
		}
		if (EventsConfig.ENABLE_ATREIAN_PASSPORT) {
			AtreianPassportService.getInstance().onStart();
		}
		
		RiftService.getInstance().initRifts();
		TemporarySpawnEngine.spawnAll();

		Util.printSection(" ### Sieges ### ");
		// Init Sieges... It's separated due to spawn engine.
		// It should not spawn siege NPCs
		if (SiegeConfig.SIEGE_ENABLED) {
			ShieldService.getInstance().spawnAll();
		}
		SiegeService.getInstance().initSieges();
		AgentFightService.getInstance().initAgentFight();
		MoltenusService.getInstance().initMoltenus();
		DiflodoxService.getInstance().initDiflodox(); // 4.9
		DiflonaxService.getInstance().initDiflonax(); // 4.9
		AbyssLandingService.getInstance().initLandingLocations();
		LandingUpdateService.getInstance().initResetQuestPoints();
		LandingUpdateService.getInstance().initResetAbyssLandingPoints();
		AbyssLandingSpecialService.getInstance().initLandingSpecialLocations();
		DisputeLandService.getInstance().initDisputeLand();
		TowerEntranceService.getInstance().startTowerEntrance();
		Util.printSsSection("Bases");
		if (BaseConfig.BASE_ENABLED) {
			BaseService.getInstance().initBases();
		}
		else {
			BaseService.getInstance().basesDisabled();
		}
		Util.printSsSection("RvR");
		if (PanesterraConfig.SVS_ENABLED) {
			RvrService.getInstance().initRvr();
		}
		else {
			RvrService.getInstance().RvrDisabled();
		}
		Util.printSsSection("Panesterra");
		if (PanesterraConfig.SVS_ENABLED) {
			SvsService.getInstance().initSvs();
		}
		else {
			SvsService.getInstance().SvsDisabled();
		}
		Util.printSection(" ### Cleaning ### ");
		DatabaseCleaningService.getInstance();
		EventService.getInstance().startCronCleanBase();
		Util.printSection(" ### TaskManagers ### ");
		PacketBroadcaster.getInstance();
		PeriodicSaveService.getInstance();
		TaskFromDBManager.getInstance();
		LaboratoryPortalSpawnService.getInstance().startLaboratory();
		AdmaPortalSpawnService.getInstance().startAdma();
		Util.printSection(" ### Services ### ");
		if (EventsConfig.ENABLE_BOOST_EVENTS) {
			BoostEventService.getInstance().onStart();
		}
		Util.printSsSection("Atreian Passport");
		AtreianPassportService.getInstance().onStart();
		Util.printSsSection("HTML");
		HTMLCache.getInstance();
		if (CustomConfig.ENABLE_REWARD_SERVICE) {
			RewardService.getInstance();
		}
		if (WeddingsConfig.WEDDINGS_ENABLE) {
			WeddingService.getInstance();
		}
		Util.printSsSection("Sheduled Services");
		LimitedItemTradeService.getInstance().start();
		if (AutoGroupConfig.AUTO_GROUP_ENABLE && AutoGroupConfig.DREDGION2_ENABLE)
			DredgionService.getInstance().start();
		if (AutoGroupConfig.AUTO_GROUP_ENABLE && AutoGroupConfig.KAMAR_ENABLE)
			KamarBattlefieldService.getInstance().start();
		if (AutoGroupConfig.AUTO_GROUP_ENABLE && AutoGroupConfig.JORMUNGAND_ENABLE)
			JormungandService.getInstance().start();
		if (AutoGroupConfig.AUTO_GROUP_ENABLE && AutoGroupConfig.STEELWALL_ENABLE)
			SteelWallBastionBattlefieldService.getInstance().start();
		if (AutoGroupConfig.AUTO_GROUP_ENABLE && AutoGroupConfig.RUNATORIUM_ENABLE)
			RunatoriumService.getInstance().start();
		if (AutoGroupConfig.AUTO_GROUP_ENABLE && AutoGroupConfig.BALAURMARCHING_ENABLE)
			BalaurMarchingRouteService.getInstance().start();
		if (AutoGroupConfig.AUTO_GROUP_ENABLE && AutoGroupConfig.RUNATORIUMRUINS_ENABLE)
			RunatoriumRuinsService.getInstance().start();
		if (AutoGroupConfig.AUTO_GROUP_ENABLE && AutoGroupConfig.GOLDENCRUCIBLE_ENABLE)
			GoldenCrucibleService.getInstance().start();
		if (AutoGroupConfig.AUTO_GROUP_ENABLE && AutoGroupConfig.SANCTUMBATTLEFIELD_ENABLE)
			SanctumBattlefieldService.getInstance().start();
		if (AutoGroupConfig.AUTO_GROUP_ENABLE && AutoGroupConfig.PANDAEMONIUMBATTLEFIELD_ENABLE)
			PandaemoniumBattlefieldService.getInstance().start();
		if (ConquerorProtectorConfig.ENABLE_GUARDIAN_PVP)
			ConquerorsService.getInstance().initConquerorPvPSystem();
		AbyssRankUpdateService.getInstance().scheduleUpdate();
		/**
		 * Schedules Garbage Collector to be launched at the specified time to be optimized unused memory. (Avoids OutOfMemoryException)
		 */
		GarbageCollector.getInstance().start();
		Util.printSsSection("Other Services");
		WorldBuffService.getInstance();
		// PetitionService.getInstance();
		if (AIConfig.SHOUTS_ENABLE) {
			NpcShoutsService.getInstance();
		}
		if (CustomConfig.LIMITS_ENABLED) {
			PlayerLimitService.getInstance().scheduleUpdate();
		}
		GameTimeManager.startClock();
		GameTimeService.getInstance();
		AnnouncementService.getInstance();
		DebugService.getInstance();
		WeatherService.getInstance();
		BrokerService.getInstance();
		Influence.getInstance();
		ExchangeService.getInstance();
		FatigueService.getInstance();
		InstanceService.load();
		FlyRingService.getInstance();
		LanguageHandler.getInstance();
		RoadService.getInstance();
		AdminService.getInstance();
		PlayerTransferService.getInstance();
		Util.printSection(" ### Housing ### ");
		HousingBidService.getInstance().start();
		MaintenanceTask.getInstance();
		TownService.getInstance();
		ChallengeTaskService.getInstance();
		Util.printSection(" ### Customs ### ");
		LookManager.getInstance().onStart();
		SupportService.getInstance();
		HotspotTeleportService.getInstance();
		TerritoryService.getInstance().init();
		if (MembershipConfig.ONLINE_BONUS_ENABLE)
			OnlineBonus.getInstance();
		RestartService.getInstance();

		Util.printSection(" ### System ### ");
		System.gc();
		AEInfos.printAllInfos();
		System.out.println("");
		log.info("[GameServer] GameServer started in " + (System.currentTimeMillis() - start) / 1000 + " seconds.");

		Util.printSection(" ### Credits ### ");
		try {
			ZCXInfo.getInfo();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		gs.startServers();
		Runtime.getRuntime().addShutdownHook(ShutdownHook.getInstance());

		ZCXInfo.checkForRatioLimitation();
		onStartup();

	}

	/**
	 * Starts servers for connection with aion client and login\chat server.
	 */
	private void startServers() {
		Util.printSection(" ### Network ### ");
		NioServer nioServer = new NioServer(NetworkConfig.NIO_READ_WRITE_THREADS, new ServerCfg(NetworkConfig.GAME_BIND_ADDRESS, NetworkConfig.GAME_PORT, "Game Connections", new GameConnectionFactoryImpl()));
		BannedMacManager.getInstance();
		BannedHDDManager.getInstance();
		NetworkBannedManager.getInstance();
		LoginServer ls = LoginServer.getInstance();
		ChatServer cs = ChatServer.getInstance();

		ls.setNioServer(nioServer);
		cs.setNioServer(nioServer);

		// Nio must go first
		nioServer.connect();
		System.out.println("");
		ls.connect();

		if (GSConfig.ENABLE_CHAT_SERVER) {
			cs.connect();
		}

		Util.printSection(" ### Misc ###");
	}

	/**
	 * Initialize all helper services, that are not directly related to aion gs, which includes:
	 * <ul>
	 * <li>Logging</li>
	 * <li>Database factory</li>
	 * <li>Thread pool</li>
	 * </ul>
	 * This method also initializes {@link Config}
	 */
	private static void initUtilityServicesAndConfig() {
		// Set default uncaught exception handler
		Thread.setDefaultUncaughtExceptionHandler(new ThreadUncaughtExceptionHandler());

		// make sure that callback code was initialized
		if (JavaAgentUtils.isConfigured()) {
			log.info("[GameServer] JavaAgent [Callback Support] is configured.");
		}

		// Initialize cron service
		CronService.initSingleton(ThreadPoolManagerRunnableRunner.class);

		Util.printSection(" ### Config ### ");
		// init config
		Config.load();
		// DateTime zone override from configs
		DateTimeUtil.init();
		// Second should be database factory
		Util.printSection(" ### DataBase ### ");
		DatabaseFactory.init();
		// Initialize DAOs
		DAOManager.init();
		// Initialize thread pools
		Util.printSection(" ### Threads ### ");
		ThreadConfig.load();
		ThreadPoolManager.getInstance();
	}

	private static Set<StartupHook> startUpHooks = new HashSet<StartupHook>();

	public synchronized static void addStartupHook(StartupHook hook) {
		if (startUpHooks != null) {
			startUpHooks.add(hook);
		}
		else {
			hook.onStartup();
		}
	}

	private synchronized static void onStartup() {
		final Set<StartupHook> startupHooks = startUpHooks;

		startUpHooks = null;

		for (StartupHook hook : startupHooks) {
			hook.onStartup();
		}
	}

	public interface StartupHook {

		public void onStartup();
	}
}
