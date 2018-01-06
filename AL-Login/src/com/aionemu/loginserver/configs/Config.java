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


package com.aionemu.loginserver.configs;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.configs.CommonsConfig;
import com.aionemu.commons.configs.DatabaseConfig;
import com.aionemu.commons.configuration.ConfigurableProcessor;
import com.aionemu.commons.configuration.Property;
import com.aionemu.commons.utils.PropertiesUtils;

/**
 * @author -Nemesiss-
 * @author SoulKeeper
 */
public class Config {

    /**
     * Logger for this class.
     */
    protected static final Logger log = LoggerFactory.getLogger(Config.class);
    @Property(key = "accounts.charset", defaultValue = "ISO8859_2")
    public static String ACCOUNT_CHARSET;
    @Property(key = "network.fastreconnection.time", defaultValue = "10")
    public static int FAST_RECONNECTION_TIME;
    /**
     * Login Server port
     */
    @Property(key = "loginserver.network.client.port", defaultValue = "2106")
    public static int LOGIN_PORT;
    /**
     * Login Server bind ip
     */
    @Property(key = "loginserver.network.client.host", defaultValue = "localhost")
    public static String LOGIN_BIND_ADDRESS;
    /**
     * Login Server port
     */
    @Property(key = "loginserver.network.gameserver.port", defaultValue = "9014")
    public static int GAME_PORT;
    /**
     * Login Server bind ip
     */
    @Property(key = "loginserver.network.gameserver.host", defaultValue = "*")
    public static String GAME_BIND_ADDRESS;
    /**
     * Number of trys of login before ban
     */
    @Property(key = "loginserver.network.client.logintrybeforeban", defaultValue = "5")
    public static int LOGIN_TRY_BEFORE_BAN;
    /**
     * Ban time in minutes
     */
    @Property(key = "loginserver.network.client.bantimeforbruteforcing", defaultValue = "15")
    public static int WRONG_LOGIN_BAN_TIME;
    /**
     * Number of Threads that will handle io read (>= 0)
     */
    @Property(key = "loginserver.network.nio.threads.read", defaultValue = "0")
    public static int NIO_READ_THREADS;
    /**
     * Number of Threads that will handle io write (>= 0)
     */
    @Property(key = "loginserver.network.nio.threads.write", defaultValue = "0")
    public static int NIO_WRITE_THREADS;
    /**
     * Should server automaticly create accounts for users or not?
     */
    @Property(key = "loginserver.accounts.autocreate", defaultValue = "true")
    public static boolean ACCOUNT_AUTO_CREATION;
    /**
     * Set the server on maintenance mod
     */
    @Property(key = "loginserver.server.maintenance", defaultValue = "false")
    public static boolean MAINTENANCE_MOD;
    /**
     * Set GM level for maintenance mod
     */
    @Property(key = "loginserver.server.maintenance.gmlevel", defaultValue = "3")
    public static int MAINTENANCE_MOD_GMLEVEL;
    /**
     * Enable\disable flood protector from 1 ip on account login
     */
    @Property(key = "loginserver.server.floodprotector", defaultValue = "true")
    public static boolean ENABLE_FLOOD_PROTECTION;
    /**
     * Enable\disable flood protector from 1 ip on account login
     */
    @Property(key = "loginserver.server.bruteforceprotector", defaultValue = "true")
    public static boolean ENABLE_BRUTEFORCE_PROTECTION;
    @Property(key = "loginserver.server.pingpong", defaultValue = "true")
    public static boolean ENABLE_PINGPONG;
    @Property(key = "loginserver.server.pingpong.delay", defaultValue = "3000")
    public static int PINGPONG_DELAY;
    @Property(key = "loginserver.excluded.ips", defaultValue = "")
    public static String EXCLUDED_IP;

    /**
     * Load configs from files.
     */
    public static void load() {
        try {
            Properties myProps = null;
            try {
                log.info("Loading: myls.properties");
                myProps = PropertiesUtils.load("./config/myls.properties");
            } catch (Exception e) {
                log.info("No override properties found");
            }

            String network = "./config/network";
            Properties[] props = PropertiesUtils.loadAllFromDirectory(network);
            PropertiesUtils.overrideProperties(props, myProps);
            log.info("Loading: " + network + "/network.properties");
            ConfigurableProcessor.process(Config.class, props);
            log.info("Loading: " + network + "/svstats.properties");
			ConfigurableProcessor.process(SvStatsConfig.class, props);
            log.info("Loading: " + network + "/commons.properties");
            ConfigurableProcessor.process(CommonsConfig.class, props);
            log.info("Loading: " + network + "/database.properties");
            ConfigurableProcessor.process(DatabaseConfig.class, props);

        } catch (Exception e) {
            log.error("Can't load loginserver configuration", e);
            throw new Error("Can't load loginserver configuration", e);
        }
    }
}
