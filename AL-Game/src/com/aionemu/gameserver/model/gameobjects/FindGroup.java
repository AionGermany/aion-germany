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
package com.aionemu.gameserver.model.gameobjects;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.team2.TemporaryPlayerTeam;
import com.aionemu.gameserver.model.team2.alliance.PlayerAlliance;
import com.aionemu.gameserver.model.team2.group.PlayerGroup;

/**
 * Find Group
 *
 * @author MrPoke
 * @modified teenwolf
 */
public class FindGroup {

	private AionObject object;
	private String message;
	private int groupType, minMembers, instanceId;
	private int lastUpdate = (int) (System.currentTimeMillis() / 1000);

	public FindGroup(AionObject object, String message, int groupType) {
		this.object = object;
		this.message = message;
		this.groupType = groupType;
	}

	public String getMessage() {
		return message;
	}

	public int getGroupType() {
		return groupType;
	}

	public int getObjectId() {
		return object.getObjectId();
	}

	public int getInstanceId() {
		return instanceId;
	}

	public int getMinMembers() {
		return minMembers;
	}

	public int getClassId() {
		if (object instanceof Player) {
			return ((Player) (object)).getPlayerClass().getClassId();
		}
		else if (object instanceof PlayerAlliance) {
			((PlayerAlliance) (object)).getLeaderObject().getCommonData().getPlayerClass();
		}
		else if (object instanceof PlayerGroup) {
			((PlayerGroup) object).getLeaderObject().getPlayerClass();
		}
		return 0;
	}

	public int getMinLevel() {
		if (object instanceof Player) {
			return ((Player) (object)).getLevel();
		}
		else if (object instanceof PlayerAlliance) {
			int minLvl = 99;
			for (Player member : ((PlayerAlliance) (object)).getMembers()) {
				int memberLvl = member.getCommonData().getLevel();
				if (memberLvl < minLvl) {
					minLvl = memberLvl;
				}
			}
			return minLvl;
		}
		else if (object instanceof PlayerGroup) {
			return ((PlayerGroup) object).getMinExpPlayerLevel();
		}
		else if (object instanceof TemporaryPlayerTeam) {
			return ((TemporaryPlayerTeam<?>) object).getMinExpPlayerLevel();
		}
		return 1;
	}

	public int getMaxLevel() {
		if (object instanceof Player) {
			return ((Player) (object)).getLevel();
		}
		else if (object instanceof PlayerAlliance) {
			int maxLvl = 1;
			for (Player member : ((PlayerAlliance) (object)).getMembers()) {
				int memberLvl = member.getCommonData().getLevel();
				if (memberLvl > maxLvl) {
					maxLvl = memberLvl;
				}
			}
			return maxLvl;
		}
		else if (object instanceof PlayerGroup) {
			return ((PlayerGroup) object).getMaxExpPlayerLevel();
		}
		else if (object instanceof TemporaryPlayerTeam) {
			return ((TemporaryPlayerTeam<?>) object).getMaxExpPlayerLevel();
		}
		return 1;
	}

	public int getUnk() {
		if (object instanceof Player) {
			return 65557;
		}
		else {
			return 0;
		}
	}

	/**
	 * @return the lastUpdate
	 */
	public int getLastUpdate() {
		return lastUpdate;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		if (object instanceof Player) {
			return ((Player) object).getName();
		}
		else if (object instanceof PlayerAlliance) {
			return ((PlayerAlliance) object).getLeaderObject().getCommonData().getName();
		}
		else if (object instanceof PlayerGroup) {
			return ((PlayerGroup) object).getLeaderObject().getName();
		}
		return "";
	}

	public int getSize() {
		if (object instanceof Player) {
			return 1;
		}
		else if (object instanceof PlayerAlliance) {
			return ((PlayerAlliance) object).size();
		}
		else if (object instanceof PlayerGroup) {
			return ((PlayerGroup) object).size();
		}
		return 1;
	}

	public void setMessage(String message) {
		lastUpdate = (int) (System.currentTimeMillis() / 1000);
		this.message = message;
	}

	public void setGroupType(int groupType) {
		this.groupType = groupType;
	}
}
