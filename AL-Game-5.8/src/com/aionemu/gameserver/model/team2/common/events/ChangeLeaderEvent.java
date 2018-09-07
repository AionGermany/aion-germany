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
package com.aionemu.gameserver.model.team2.common.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.team2.TemporaryPlayerTeam;
import com.aionemu.gameserver.model.team2.group.events.ChangeGroupLeaderEvent;

/**
 * @author ATracer
 */
public abstract class ChangeLeaderEvent<T extends TemporaryPlayerTeam<?>> extends AbstractTeamPlayerEvent<T> {

	private static final Logger log = LoggerFactory.getLogger(ChangeGroupLeaderEvent.class);

	public ChangeLeaderEvent(T team, Player eventPlayer) {
		super(team, eventPlayer);
	}

	/**
	 * New leader either is null or should be online
	 */
	@Override
	public boolean checkCondition() {
		return eventPlayer == null || eventPlayer.isOnline();
	}

	@Override
	public boolean apply(Player player) {
		if (!player.getObjectId().equals(team.getLeader().getObjectId()) && player.isOnline()) {
			changeLeaderTo(player);
			return false;
		}
		return true;
	}

	/**
	 * @param oldLeader
	 */
	protected void checkLeaderChanged(Player oldLeader) {
		if (team.isLeader(oldLeader)) {
			log.info("TEAM2: leader is not changed, total: {}, online: {}", team.size(), team.onlineMembers());
		}
	}

	protected abstract void changeLeaderTo(Player player);
}
