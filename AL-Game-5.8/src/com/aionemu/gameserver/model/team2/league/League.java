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
package com.aionemu.gameserver.model.team2.league;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.team2.GeneralTeam;
import com.aionemu.gameserver.model.team2.alliance.PlayerAlliance;
import com.aionemu.gameserver.model.team2.alliance.PlayerAllianceMember;
import com.aionemu.gameserver.model.team2.common.legacy.LootGroupRules;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.utils.idfactory.IDFactory;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;

/**
 * @author ATracer
 */
public class League extends GeneralTeam<PlayerAlliance, LeagueMember> {

	private LootGroupRules lootGroupRules = new LootGroupRules();
	private static final LeagueMemberComparator MEMBER_COMPARATOR = new LeagueMemberComparator();

	public League(LeagueMember leader) {
		super(IDFactory.getInstance().nextId());
		initializeTeam(leader);
	}

	protected final void initializeTeam(LeagueMember leader) {
		setLeader(leader);
	}

	@Override
	public Collection<PlayerAlliance> getOnlineMembers() {
		return getMembers();
	}

	@Override
	public void addMember(LeagueMember member) {
		super.addMember(member);
		member.getObject().setLeague(this);
	}

	@Override
	public void removeMember(LeagueMember member) {
		super.removeMember(member);
		member.getObject().setLeague(null);
	}

	@Override
	public void sendPacket(AionServerPacket packet) {
		for (PlayerAlliance alliance : getMembers()) {
			alliance.sendPacket(packet);
		}
	}

	@Override
	public void sendPacket(AionServerPacket packet, Predicate<PlayerAlliance> predicate) {
		for (PlayerAlliance alliance : getMembers()) {
			if (predicate.apply(alliance)) {
				alliance.sendPacket(packet, Predicates.<Player> alwaysTrue());
			}
		}
	}

	@Override
	public int onlineMembers() {
		return getMembers().size();
	}

	@Override
	public Race getRace() {
		return getLeaderObject().getRace();
	}

	@Override
	public boolean isFull() {
		return size() == 8;
	}

	public LootGroupRules getLootGroupRules() {
		return lootGroupRules;
	}

	public void setLootGroupRules(LootGroupRules lootGroupRules) {
		this.lootGroupRules = lootGroupRules;
	}

	/**
	 * @return sorted alliances by position
	 */
	public Collection<LeagueMember> getSortedMembers() {
		ArrayList<LeagueMember> newArrayList = Lists.newArrayList(members.values());
		Collections.sort(newArrayList, MEMBER_COMPARATOR);
		return newArrayList;
	}

	/**
	 * Search for player member in all alliances
	 *
	 * @return player object
	 */
	public Player getPlayerMember(Integer playerObjId) {
		for (PlayerAlliance member : getMembers()) {
			PlayerAllianceMember playerMember = member.getMember(playerObjId);
			if (playerMember != null) {
				return playerMember.getObject();
			}
		}
		return null;
	}

	static class LeagueMemberComparator implements Comparator<LeagueMember> {

		@Override
		public int compare(LeagueMember o1, LeagueMember o2) {
			return o1.getLeaguePosition() > o2.getLeaguePosition() ? 1 : -1;
		}
	}
}
