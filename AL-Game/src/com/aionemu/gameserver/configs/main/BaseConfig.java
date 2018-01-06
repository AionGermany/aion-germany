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

package com.aionemu.gameserver.configs.main;

import com.aionemu.commons.configuration.Property;

/**
 * @Author Lyras
 */
public class BaseConfig {

	/**
	 * Base Configs
	 */
	@Property(key = "gameserver.base.enable", defaultValue = "true")
	public static boolean BASE_ENABLED;

	@Property(key = "gameserver.base.assault.min.delay", defaultValue = "15") // 15 = 15min
	public static int ASSAULT_MIN_DELAY;

	@Property(key = "gameserver.base.assault.min.delay", defaultValue = "20") // 20 = 20min
	public static int ASSAULT_MAX_DELAY;

	@Property(key = "gameserver.base.boss.spawn.min.delay", defaultValue = "30") // 30 = 30min
	public static int BOSS_SPAWN_MIN_DELAY;

	@Property(key = "gameserver.base.boss.spawn.min.delay", defaultValue = "240") // 240 = 4 hours
	public static int BOSS_SPAWN_MAX_DELAY;

	@Property(key = "gameserver.base.rewards.enable", defaultValue = "true")
	public static boolean ENABLE_BASE_REWARDS;
}
