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
package com.aionemu.gameserver.configs;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.configs.CommonsConfig;
import com.aionemu.commons.configs.DatabaseConfig;
import com.aionemu.commons.configuration.ConfigurableProcessor;
import com.aionemu.commons.utils.PropertiesUtils;
import com.aionemu.gameserver.configs.administration.AdminConfig;
import com.aionemu.gameserver.configs.administration.DeveloperConfig;
import com.aionemu.gameserver.configs.main.AIConfig;
import com.aionemu.gameserver.configs.main.AutoGroupConfig;
import com.aionemu.gameserver.configs.main.BaseConfig;
import com.aionemu.gameserver.configs.main.CacheConfig;
import com.aionemu.gameserver.configs.main.CleaningConfig;
import com.aionemu.gameserver.configs.main.CompositionConfig;
import com.aionemu.gameserver.configs.main.ConquerorProtectorConfig;
import com.aionemu.gameserver.configs.main.CraftConfig;
import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.configs.main.DropConfig;
import com.aionemu.gameserver.configs.main.DualBoxConfig;
import com.aionemu.gameserver.configs.main.EnchantsConfig;
import com.aionemu.gameserver.configs.main.EventsConfig;
import com.aionemu.gameserver.configs.main.FallDamageConfig;
import com.aionemu.gameserver.configs.main.FastTrackConfig;
import com.aionemu.gameserver.configs.main.GSConfig;
import com.aionemu.gameserver.configs.main.GeoDataConfig;
import com.aionemu.gameserver.configs.main.GroupConfig;
import com.aionemu.gameserver.configs.main.HTMLConfig;
import com.aionemu.gameserver.configs.main.HousingConfig;
import com.aionemu.gameserver.configs.main.LegionConfig;
import com.aionemu.gameserver.configs.main.LoggingConfig;
import com.aionemu.gameserver.configs.main.LunaSystemConfig;
import com.aionemu.gameserver.configs.main.MembershipConfig;
import com.aionemu.gameserver.configs.main.NameConfig;
import com.aionemu.gameserver.configs.main.PeriodicSaveConfig;
import com.aionemu.gameserver.configs.main.PlayerTransferConfig;
import com.aionemu.gameserver.configs.main.PricesConfig;
import com.aionemu.gameserver.configs.main.PunishmentConfig;
import com.aionemu.gameserver.configs.main.RankingConfig;
import com.aionemu.gameserver.configs.main.RateConfig;
import com.aionemu.gameserver.configs.main.SecurityConfig;
import com.aionemu.gameserver.configs.main.ShutdownConfig;
import com.aionemu.gameserver.configs.main.SiegeConfig;
import com.aionemu.gameserver.configs.main.ThreadConfig;
import com.aionemu.gameserver.configs.main.WeddingsConfig;
import com.aionemu.gameserver.configs.main.WorldConfig;
import com.aionemu.gameserver.configs.network.IPConfig;
import com.aionemu.gameserver.configs.network.NetworkConfig;
import com.aionemu.gameserver.utils.Util;

/**
 * @author Nemesiss, SoulKeeper
 */
public class Config {

	protected static final Logger log = LoggerFactory.getLogger(Config.class);

	/**
	 * Initialize all configs in com.aionemu.gameserver.configs package
	 */
	public static void load() {
		try {
			Properties myProps = null;
			try {
				log.info("[Config] Loading: mygs.properties");
				myProps = PropertiesUtils.load("./config/mygs.properties");
			}
			catch (Exception e) {
				log.info("[Config] No override properties found");
			}

			// Administration
			Util.printSsSection("ADMINISTRATION");
			String administration = "./config/administration";

			Properties[] adminProps = PropertiesUtils.loadAllFromDirectory(administration);
			PropertiesUtils.overrideProperties(adminProps, myProps);

			ConfigurableProcessor.process(AdminConfig.class, adminProps);
			log.info("[Config] Loading: " + administration + "/admin.properties");

			ConfigurableProcessor.process(DeveloperConfig.class, adminProps);
			log.info("[Config] Loading: " + administration + "/developer.properties");

			// Main
			Util.printSsSection("MAIN");
			String main = "./config/main";

			Properties[] mainProps = PropertiesUtils.loadAllFromDirectory(main);
			PropertiesUtils.overrideProperties(mainProps, myProps);

			ConfigurableProcessor.process(AIConfig.class, mainProps);
			log.info("[Config] Loading: " + main + "/ai.properties");

			ConfigurableProcessor.process(AutoGroupConfig.class, mainProps);
			log.info("[Config] Loading: " + main + "/autogroup.properties");

			ConfigurableProcessor.process(BaseConfig.class, mainProps);
			log.info("[Config] Loading: " + main + "/base.properties");

			ConfigurableProcessor.process(CompositionConfig.class, mainProps);
			log.info("[Config] Loading: " + main + "/composition.properties");

			ConfigurableProcessor.process(ConquerorProtectorConfig.class, mainProps);
			log.info("[Config] Loading: " + main + "/conqueror.properties");

			ConfigurableProcessor.process(DualBoxConfig.class, mainProps);
			log.info("[Config] Loading: " + main + "/DualBoxConfig.properties");

			ConfigurableProcessor.process(CommonsConfig.class, mainProps);
			log.info("[Config] Loading: " + main + "/commons.properties");

			ConfigurableProcessor.process(CacheConfig.class, mainProps);
			log.info("[Config] Loading: " + main + "/cache.properties");

			ConfigurableProcessor.process(CleaningConfig.class, mainProps);
			log.info("[Config] Loading: " + main + "/cleaning.properties");

			ConfigurableProcessor.process(CompositionConfig.class, mainProps);
			log.info("[Config] Loading: " + main + "/composition.properties");

			ConfigurableProcessor.process(CraftConfig.class, mainProps);
			log.info("[Config] Loading: " + main + "/craft.properties");

			ConfigurableProcessor.process(CustomConfig.class, mainProps);
			log.info("[Config] Loading: " + main + "/custom.properties");

			ConfigurableProcessor.process(DropConfig.class, mainProps);
			log.info("[Config] Loading: " + main + "/drop.properties");

			ConfigurableProcessor.process(EnchantsConfig.class, mainProps);
			log.info("[Config] Loading: " + main + "/enchants.properties");

			ConfigurableProcessor.process(EventsConfig.class, mainProps);
			log.info("[Config] Loading: " + main + "/events.properties");

			ConfigurableProcessor.process(FallDamageConfig.class, mainProps);
			log.info("[Config] Loading: " + main + "/falldamage.properties");

			ConfigurableProcessor.process(FastTrackConfig.class, mainProps);
			log.info("[Config] Loading: " + main + "/fasttrack.properties");

			ConfigurableProcessor.process(GSConfig.class, mainProps);
			log.info("[Config] Loading: " + main + "/gameserver.properties");

			ConfigurableProcessor.process(GeoDataConfig.class, mainProps);
			log.info("[Config] Loading: " + main + "/geodata.properties");

			ConfigurableProcessor.process(GroupConfig.class, mainProps);
			log.info("[Config] Loading: " + main + "/group.properties");

			ConfigurableProcessor.process(HousingConfig.class, mainProps);
			log.info("[Config] Loading: " + main + "/housing.properties");

			ConfigurableProcessor.process(HTMLConfig.class, mainProps);
			log.info("[Config] Loading: " + main + "/html.properties");

			ConfigurableProcessor.process(LegionConfig.class, mainProps);
			log.info("[Config] Loading: " + main + "/legion.properties");

			ConfigurableProcessor.process(LoggingConfig.class, mainProps);
			log.info("[Config] Loading: " + main + "/logging.properties");

			ConfigurableProcessor.process(LunaSystemConfig.class, mainProps);
			log.info("[Config] Loading: " + main + "/lunasystem.properties");

			ConfigurableProcessor.process(MembershipConfig.class, mainProps);
			log.info("[Config] Loading: " + main + "/membership.properties");

			ConfigurableProcessor.process(NameConfig.class, mainProps);
			log.info("[Config] Loading: " + main + "/name.properties");

			ConfigurableProcessor.process(PeriodicSaveConfig.class, mainProps);
			log.info("[Config] Loading: " + main + "/periodicsave.properties");

			ConfigurableProcessor.process(PlayerTransferConfig.class, mainProps);
			log.info("[Config] Loading: " + main + "/playertransfer.properties");

			ConfigurableProcessor.process(PricesConfig.class, mainProps);
			log.info("[Config] Loading: " + main + "/prices.properties");

			ConfigurableProcessor.process(PunishmentConfig.class, mainProps);
			log.info("[Config] Loading: " + main + "/punishment.properties");

			ConfigurableProcessor.process(RankingConfig.class, mainProps);
			log.info("[Config] Loading: " + main + "/ranking.properties");

			ConfigurableProcessor.process(RateConfig.class, mainProps);
			log.info("[Config] Loading: " + main + "/rates.properties");

			ConfigurableProcessor.process(SecurityConfig.class, mainProps);
			log.info("[Config] Loading: " + main + "/security.properties");

			ConfigurableProcessor.process(ShutdownConfig.class, mainProps);
			log.info("[Config] Loading: " + main + "/shutdown.properties");

			ConfigurableProcessor.process(SiegeConfig.class, mainProps);
			log.info("[Config] Loading: " + main + "/siege.properties");

			ConfigurableProcessor.process(ThreadConfig.class, mainProps);
			log.info("[Config] Loading: " + main + "/thread.properties");

			ConfigurableProcessor.process(WeddingsConfig.class, mainProps);
			log.info("[Config] Loading: " + main + "/weddings.properties");

			ConfigurableProcessor.process(WorldConfig.class, mainProps);
			log.info("[Config] Loading: " + main + "/world.properties");

			// Network
			Util.printSsSection("NETWORK");
			String network = "./config/network";

			Properties[] networkProps = PropertiesUtils.loadAllFromDirectory(network);
			PropertiesUtils.overrideProperties(networkProps, myProps);

			log.info("[Config] Loading: " + network + "/database.properties");
			ConfigurableProcessor.process(DatabaseConfig.class, networkProps);

			log.info("[Config] Loading: " + network + "/network.properties");
			ConfigurableProcessor.process(NetworkConfig.class, networkProps);

		}
		catch (Exception e) {
			log.error("[Config] Can't load gameserver configuration: ", e);
			throw new Error("[Config] Can't load gameserver configuration: ", e);
		}

		IPConfig.load();
	}

	/**
	 * Reload all configs in com.aionemu.gameserver.configs package
	 */
	public static void reload() {
		try {
			Properties myProps = null;
			try {
				log.info("[Config] Loading: mygs.properties");
				myProps = PropertiesUtils.load("./config/mygs.properties");
			}
			catch (Exception e) {
				log.info("[Config] No override properties found");
			}

			// Administration
			String administration = "./config/administration";

			Properties[] adminProps = PropertiesUtils.loadAllFromDirectory(administration);
			PropertiesUtils.overrideProperties(adminProps, myProps);

			ConfigurableProcessor.process(AdminConfig.class, adminProps);
			log.info("[Config] Reload: " + administration + "/admin.properties");

			ConfigurableProcessor.process(DeveloperConfig.class, adminProps);
			log.info("[Config] Reload: " + administration + "/developer.properties");

			// Main
			String main = "./config/main";

			Properties[] mainProps = PropertiesUtils.loadAllFromDirectory(main);
			PropertiesUtils.overrideProperties(mainProps, myProps);

			ConfigurableProcessor.process(AIConfig.class, mainProps);
			log.info("[Config] Reload: " + main + "/ai.properties");

			ConfigurableProcessor.process(AutoGroupConfig.class, mainProps);
			log.info("[Config] Reload: " + main + "/autogroup.properties");

			ConfigurableProcessor.process(BaseConfig.class, mainProps);
			log.info("[Config] Reload: " + main + "/base.properties");

			ConfigurableProcessor.process(CompositionConfig.class, mainProps);
			log.info("[Config] Reload: " + main + "/composition.properties");

			ConfigurableProcessor.process(ConquerorProtectorConfig.class, mainProps);
			log.info("[Config] Reload: " + main + "/conqueror.properties");

			ConfigurableProcessor.process(DualBoxConfig.class, mainProps);
			log.info("[Config] Reload: " + main + "/DualBoxConfig.properties");

			ConfigurableProcessor.process(CommonsConfig.class, mainProps);
			log.info("[Config] Reload: " + main + "/commons.properties");

			ConfigurableProcessor.process(CacheConfig.class, mainProps);
			log.info("[Config] Reload: " + main + "/cache.properties");

			ConfigurableProcessor.process(CompositionConfig.class, mainProps);
			log.info("[Config] Reload: " + main + "/composition.properties");

			ConfigurableProcessor.process(CraftConfig.class, mainProps);
			log.info("[Config] Reload: " + main + "/craft.properties");

			ConfigurableProcessor.process(CustomConfig.class, mainProps);
			log.info("[Config] Reload: " + main + "/custom.properties");

			ConfigurableProcessor.process(DropConfig.class, mainProps);
			log.info("[Config] Reload: " + main + "/drop.properties");

			ConfigurableProcessor.process(EnchantsConfig.class, mainProps);
			log.info("[Config] Reload: " + main + "/enchants.properties");

			ConfigurableProcessor.process(EventsConfig.class, mainProps);
			log.info("[Config] Reload: " + main + "/events.properties");

			ConfigurableProcessor.process(FallDamageConfig.class, mainProps);
			log.info("[Config] Reload: " + main + "/falldamage.properties");

			ConfigurableProcessor.process(FastTrackConfig.class, mainProps);
			log.info("[Config] Reload: " + main + "/fasttrack.properties");

			ConfigurableProcessor.process(GSConfig.class, mainProps);
			log.info("[Config] Reload: " + main + "/gameserver.properties");

			ConfigurableProcessor.process(GeoDataConfig.class, mainProps);
			log.info("[Config] Reload: " + main + "/geodata.properties");

			ConfigurableProcessor.process(GroupConfig.class, mainProps);
			log.info("[Config] Reload: " + main + "/group.properties");

			ConfigurableProcessor.process(HousingConfig.class, mainProps);
			log.info("[Config] Reload: " + main + "/housing.properties");

			ConfigurableProcessor.process(HTMLConfig.class, mainProps);
			log.info("[Config] Reload: " + main + "/html.properties");

			ConfigurableProcessor.process(LegionConfig.class, mainProps);
			log.info("[Config] Reload: " + main + "/legion.properties");

			ConfigurableProcessor.process(LoggingConfig.class, mainProps);
			log.info("[Config] Reload: " + main + "/logging.properties");

			ConfigurableProcessor.process(LunaSystemConfig.class, mainProps);
			log.info("[Config] Reload: " + main + "/lunasystem.properties");

			ConfigurableProcessor.process(MembershipConfig.class, mainProps);
			log.info("[Config] Reload: " + main + "/membership.properties");

			ConfigurableProcessor.process(NameConfig.class, mainProps);
			log.info("[Config] Reload: " + main + "/name.properties");

			ConfigurableProcessor.process(PeriodicSaveConfig.class, mainProps);
			log.info("[Config] Reload: " + main + "/periodicsave.properties");

			ConfigurableProcessor.process(PlayerTransferConfig.class, mainProps);
			log.info("[Config] Reload: " + main + "/playertransfer.properties");

			ConfigurableProcessor.process(PricesConfig.class, mainProps);
			log.info("[Config] Reload: " + main + "/prices.properties");

			ConfigurableProcessor.process(PunishmentConfig.class, mainProps);
			log.info("[Config] Reload: " + main + "/punishment.properties");

			ConfigurableProcessor.process(RankingConfig.class, mainProps);
			log.info("[Config] Reload: " + main + "/ranking.properties");

			ConfigurableProcessor.process(RateConfig.class, mainProps);
			log.info("[Config] Reload: " + main + "/rates.properties");

			ConfigurableProcessor.process(SecurityConfig.class, mainProps);
			log.info("[Config] Reload: " + main + "/security.properties");

			ConfigurableProcessor.process(ShutdownConfig.class, mainProps);
			log.info("[Config] Reload: " + main + "/shutdown.properties");

			ConfigurableProcessor.process(SiegeConfig.class, mainProps);
			log.info("[Config] Reload: " + main + "/siege.properties");

			ConfigurableProcessor.process(ThreadConfig.class, mainProps);
			log.info("[Config] Reload: " + main + "/thread.properties");

			ConfigurableProcessor.process(WeddingsConfig.class, mainProps);
			log.info("[Config] Reload: " + main + "/weddings.properties");

			ConfigurableProcessor.process(WorldConfig.class, mainProps);
			log.info("[Config] Reload: " + main + "/world.properties");
		}
		catch (Exception e) {
			log.error("Can't reload configuration: ", e);
			throw new Error("Can't reload configuration: ", e);
		}
	}
}
