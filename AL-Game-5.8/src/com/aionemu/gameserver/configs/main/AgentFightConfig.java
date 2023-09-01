package com.aionemu.gameserver.configs.main;

import com.aionemu.commons.configuration.Property;

/**
 * This file is part of Aion-Lightning <aion-lightning.org>. Aion-Lightning is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Aion-Lightning is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. * You should have received a copy of the GNU General
 * Public License along with Aion-Lightning. If not, see <http://www.gnu.org/licenses/>.
 */
public class AgentFightConfig {

	/**
	 * Agent Fight
	 */
	@Property(key = "gameserver.agent.fight.enable", defaultValue = "true")
	public static boolean AGENT_ENABLE;

	@Property(key = "gameserver.agent.fight.time", defaultValue = "0 0 1 ? * *")
	public static String AGENT_FIGHT_SPAWN_SCHEDULE;

	@Property(key = "gameserver.agent.fight.announce", defaultValue = "50")
	public static int AGENT_FIGHT_SPAWN_ANNOUNCE;

	@Property(key = "gameserver.agent.fight.runtime", defaultValue = "1")
	public static int AGENT_FIGHT_SPAWN_RUNTIME;

	@Property(key = "gameserver.agent.fight.gp", defaultValue = "5000")
	public static int AGENT_FIGHT_GP;

	@Property(key = "gameserver.agent.fight.ammount", defaultValue = "1")
	public static int AGENT_FIGHT_AMMOUNT;

	@Property(key = "gameserver.agent.fight.item", defaultValue = "186000242")
	public static int AGENT_FIGHT_ITEM;
}
