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

import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.google.common.base.Predicate;

/**
 * @author ATracer
 */
public interface Team<M, TM extends TeamMember<M>> {

	Integer getTeamId();

	TM getMember(Integer objectId);

	boolean hasMember(Integer objectId);

	void addMember(TM member);

	void removeMember(TM member);

	void removeMember(Integer objectId);

	Collection<M> getMembers();

	Collection<M> getOnlineMembers();

	void onEvent(TeamEvent event);

	Collection<TM> filter(Predicate<TM> predicate);

	Collection<M> filterMembers(Predicate<M> predicate);

	void sendPacket(AionServerPacket packet);

	void sendPacket(AionServerPacket packet, Predicate<M> predicate);

	int onlineMembers();

	Race getRace();

	int size();

	boolean isFull();
}
