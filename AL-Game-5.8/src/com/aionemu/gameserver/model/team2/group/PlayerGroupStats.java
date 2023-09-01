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
package com.aionemu.gameserver.model.team2.group;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.google.common.base.Predicate;

/**
 * @author ATracer
 */
public class PlayerGroupStats implements Predicate<Player> {

	private final PlayerGroup group;
	private int minExpPlayerLevel;
	private int maxExpPlayerLevel;
	Player minLevelPlayer;
	Player maxLevelPlayer;

	PlayerGroupStats(PlayerGroup group) {
		this.group = group;
	}

	public void onAddPlayer(PlayerGroupMember member) {
		group.applyOnMembers(this);
		calculateExpLevels();
	}

	public void onRemovePlayer(PlayerGroupMember member) {
		group.applyOnMembers(this);
	}

	private void calculateExpLevels() {
		minExpPlayerLevel = minLevelPlayer.getLevel();
		maxExpPlayerLevel = maxLevelPlayer.getLevel();
		minLevelPlayer = null;
		maxLevelPlayer = null;
	}

	@Override
	public boolean apply(Player player) {
		if (minLevelPlayer == null || maxLevelPlayer == null) {
			minLevelPlayer = player;
			maxLevelPlayer = player;
		}
		else {
			if (player.getCommonData().getExp() < minLevelPlayer.getCommonData().getExp()) {
				minLevelPlayer = player;
			}
			if (!player.isMentor() && player.getCommonData().getExp() > maxLevelPlayer.getCommonData().getExp()) {
				maxLevelPlayer = player;
			}
		}
		return true;
	}

	public int getMinExpPlayerLevel() {
		return minExpPlayerLevel;
	}

	public int getMaxExpPlayerLevel() {
		return maxExpPlayerLevel;
	}
}
