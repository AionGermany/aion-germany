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
package com.aionemu.gameserver.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.GameServer;
import com.aionemu.gameserver.GameServer.StartupHook;
import com.aionemu.gameserver.configs.main.GSConfig;
import com.aionemu.gameserver.dao.PlayerDAO;
import com.aionemu.gameserver.model.Race;

/**
 * @author Alcapwnd
 */
public class ZCXInfo {

	private static String VERSION = null;
	private static String MINOR = null;
	private static String COPYRIGHT = null;
	private static final Logger log = LoggerFactory.getLogger(ZCXInfo.class);
	private static int ELYOS_COUNT = 0;
	private static int ASMOS_COUNT = 0;
	private static double ELYOS_RATIO = 0.0;
	private static double ASMOS_RATIO = 0.0;
	private static final ReentrantLock lock = new ReentrantLock();

	public static void getInfo() throws IOException {
		readInfo();
		System.out.println("");
		System.out.println("      .o.        o8o                          .oooooo.");
		System.out.println("     .888.       `\"'                         d8P'  `Y8b");
		System.out.println("    .8\"888.     oooo   .ooooo.  ooo. .oo.   888            .ooooo.  oooo d8b");
		System.out.println("   .8' `888.    `888  d88' `88b `888P\"Y88b  888           d88' `88b `888\"\"8P ");
		System.out.println("  .88ooo8888.    888  888   888  888   888  888     ooooo 888ooo888  888");
		System.out.println(" .8'     `888.   888  888   888  888   888  `88.    .88'  888    .o  888");
		System.out.println(" o88o     o8888o o888o `Y8bod8P' o888o o888o  `Y8bood8P'   `Y8bod8P' d888b");
		System.out.println("");
		System.out.println("######## This is an AionGerman-Core - based on an AionLightning Source ########");
		System.out.println("");
		System.out.println("");
		System.out.println("\t\t\tThanks to all who helped this project!");
		System.out.println("\t\t\tMajor Patch: " + getVersion());
		System.out.println("\t\t\tMinor Patch: " + getMinor());
		System.out.println("\t\t\tCopyright: " + getCopyright());
		System.out.println("");
	}

	public static void readInfo() throws IOException {
		FileReader fr = null;
		try {
			fr = new FileReader("./config/info.txt");
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		BufferedReader br = new BufferedReader(fr);

		setVersion(br.readLine());
		setMinor(br.readLine());
		setCopyright(br.readLine());
		br.close();
	}

	public static void setVersion(String version) {
		VERSION = version;
	}

	public static void setMinor(String minor) {
		MINOR = minor;
	}

	public static void setCopyright(String copyright) {
		COPYRIGHT = copyright;
	}

	public static String getVersion() {
		return VERSION;
	}

	public static String getMinor() {
		return MINOR;
	}

	public static String getCopyright() {
		return COPYRIGHT;
	}

	/**
	 * Adding the ratio limit in this class. on that way the main class dont got trashed with things like this
	 */
	public static void checkForRatioLimitation() {
		if (GSConfig.ENABLE_RATIO_LIMITATION) {
			GameServer.addStartupHook(new StartupHook() {

				@Override
				public void onStartup() {
					lock.lock();
					try {
						ASMOS_COUNT = DAOManager.getDAO(PlayerDAO.class).getCharacterCountForRace(Race.ASMODIANS);
						ELYOS_COUNT = DAOManager.getDAO(PlayerDAO.class).getCharacterCountForRace(Race.ELYOS);
						computeRatios();
					}
					catch (Exception e) {
						log.error("[Error] Something went wrong on checking ratio limitation");
						e.printStackTrace();
					}
					finally {
						lock.unlock();
					}
					displayRatios(false);
				}
			});
		}
	}

	public static void updateRatio(Race race, int i) {
		lock.lock();
		try {
			switch (race) {
				case ASMODIANS:
					ASMOS_COUNT += i;
					break;
				case ELYOS:
					ELYOS_COUNT += i;
					break;
				default:
					break;
			}

			computeRatios();
		}
		catch (Exception e) {
			log.error("[Error] Cant update ratio limits");
			e.printStackTrace();
		}
		finally {
			lock.unlock();
		}

		displayRatios(true);
	}

	private static void computeRatios() {
		if ((ASMOS_COUNT <= GSConfig.RATIO_MIN_CHARACTERS_COUNT) && (ELYOS_COUNT <= GSConfig.RATIO_MIN_CHARACTERS_COUNT)) {
			ASMOS_RATIO = ELYOS_RATIO = 50.0;
		}
		else {
			ASMOS_RATIO = ASMOS_COUNT * 100.0 / (ASMOS_COUNT + ELYOS_COUNT);
			ELYOS_RATIO = ELYOS_COUNT * 100.0 / (ASMOS_COUNT + ELYOS_COUNT);
		}
	}

	private static void displayRatios(boolean updated) {
		GameServer.log.info("[GameServer] Actual Factions Ratio " + (updated ? "updated " : "") + ": Elyos " + String.format("%.1f", ELYOS_RATIO) + " % - Asmodians " + String.format("%.1f", ASMOS_RATIO) + " %");
	}

	public static double getRatiosFor(Race race) {
		switch (race) {
			case ASMODIANS:
				return ASMOS_RATIO;
			case ELYOS:
				return ELYOS_RATIO;
			default:
				return 0.0;
		}
	}

	public static int getCountFor(Race race) {
		switch (race) {
			case ASMODIANS:
				return ASMOS_COUNT;
			case ELYOS:
				return ELYOS_COUNT;
			default:
				return 0;
		}
	}
}
