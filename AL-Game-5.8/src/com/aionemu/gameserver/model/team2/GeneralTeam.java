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
package com.aionemu.gameserver.model.team2;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.model.gameobjects.AionObject;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;

/**
 * @author ATracer
 */
public abstract class GeneralTeam<M extends AionObject, TM extends TeamMember<M>> extends AionObject implements Team<M, TM> {

	private final static Logger log = LoggerFactory.getLogger(GeneralTeam.class);
	protected final Map<Integer, TM> members = new ConcurrentHashMap<Integer, TM>();
	protected final Lock teamLock = new ReentrantLock();
	private TM leader;
	private final MemberTransformFunction<TM, M> TRANSFORM_FUNCTION = new MemberTransformFunction<TM, M>();

	public GeneralTeam(Integer objId) {
		super(objId);
	}

	@Override
	public void onEvent(TeamEvent event) {
		lock();
		try {
			if (event.checkCondition()) {
				event.handleEvent();
			}
			else {
				log.warn("[TEAM2] skipped event: {} group: {}", event, this);
			}
		}
		finally {
			unlock();
		}
	}

	@Override
	public TM getMember(Integer objectId) {
		return members.get(objectId);
	}

	@Override
	public boolean hasMember(Integer objectId) {
		return members.get(objectId) != null;
	}

	@Override
	public void addMember(TM member) {
		Preconditions.checkNotNull(member, "Team member should be not null");
		Preconditions.checkState(members.get(member.getObjectId()) == null, "Team member is already added");
		members.put(member.getObjectId(), member);
	}

	@Override
	public void removeMember(TM member) {
		Preconditions.checkNotNull(member, "Team member should be not null");
		Preconditions.checkState(members.get(member.getObjectId()) != null, "Team member is already removed");
		members.remove(member.getObjectId());
	}

	@Override
	public final void removeMember(Integer objectId) {
		removeMember(members.get(objectId));
	}

	/**
	 * Apply some predicate on all group members<br>
	 * Should be used only to change state of the group or its members
	 */
	public void apply(Predicate<TM> predicate) {
		lock();
		try {
			for (TM member : members.values()) {
				if (!predicate.apply(member)) {
					return;
				}
			}
		}
		finally {
			unlock();
		}
	}

	/**
	 * Apply some predicate on all group member's objects<br>
	 * Should be used only to change state of the group or its members
	 */
	public void applyOnMembers(Predicate<M> predicate) {
		lock();
		try {
			for (TM member : members.values()) {
				if (!predicate.apply(member.getObject())) {
					return;
				}
			}
		}
		finally {
			unlock();
		}
	}

	@Override
	public Collection<TM> filter(Predicate<TM> predicate) {
		return Collections2.filter(members.values(), predicate);
	}

	@Override
	public Collection<M> filterMembers(Predicate<M> predicate) {
		return Collections2.filter(Collections2.transform(members.values(), TRANSFORM_FUNCTION), predicate);
	}

	@Override
	public Collection<M> getMembers() {
		return filterMembers(Predicates.<M> alwaysTrue());
	}

	@Override
	public int size() {
		return members.size();
	}

	@Override
	public final Integer getTeamId() {
		return getObjectId();
	}

	@Override
	public String getName() {
		return GeneralTeam.class.getName();
	}

	public final TM getLeader() {
		return leader;
	}

	public final M getLeaderObject() {
		return leader.getObject();
	}

	public final boolean isLeader(M member) {
		return leader.getObject().getObjectId().equals(member.getObjectId());
	}

	public final void changeLeader(TM member) {
		Preconditions.checkNotNull(leader, "Leader should already be set");
		Preconditions.checkNotNull(member, "New leader should not be null");
		this.leader = member;
	}

	protected final void setLeader(TM member) {
		Preconditions.checkState(leader == null, "Leader should be not initialized");
		Preconditions.checkNotNull(member, "Leader should not be null");
		this.leader = member;
	}

	protected final void lock() {
		teamLock.lock();
	}

	protected final void unlock() {
		teamLock.unlock();
	}

	private static final class MemberTransformFunction<TM extends TeamMember<M>, M> implements Function<TM, M> {

		@Override
		public M apply(TM member) {
			return member.getObject();
		}
	}
}
