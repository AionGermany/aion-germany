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
package com.aionemu.gameserver.model.team2.alliance.events;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.team2.alliance.PlayerAlliance;
import com.aionemu.gameserver.model.team2.alliance.PlayerAllianceMember;
import com.aionemu.gameserver.model.team2.common.events.AlwaysTrueTeamEvent;
import com.aionemu.gameserver.model.team2.common.legacy.PlayerAllianceEvent;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ALLIANCE_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ALLIANCE_MEMBER_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INSTANCE_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SHOW_BRAND;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.google.common.base.Predicate;

/**
 * @author ATracer
 */
public class PlayerConnectedEvent extends AlwaysTrueTeamEvent implements Predicate<PlayerAllianceMember> {

	private final PlayerAlliance alliance;
	private final Player connected;
	private PlayerAllianceMember connectedMember;

	public PlayerConnectedEvent(PlayerAlliance alliance, Player player) {
		this.alliance = alliance;
		this.connected = player;
	}

	@Override
	public void handleEvent() {
		alliance.removeMember(connected.getObjectId());
		connectedMember = new PlayerAllianceMember(connected);
		alliance.addMember(connectedMember);

		PacketSendUtility.sendPacket(connected, new SM_ALLIANCE_INFO(alliance));
		PacketSendUtility.sendPacket(connected, new SM_ALLIANCE_MEMBER_INFO(connectedMember, PlayerAllianceEvent.RECONNECT));
		PacketSendUtility.sendPacket(connected, new SM_SHOW_BRAND(0, 0));

		alliance.apply(this);
	}

	@Override
	public boolean apply(PlayerAllianceMember member) {
		Player player = member.getObject();
		if (!connected.getObjectId().equals(player.getObjectId())) {
			PacketSendUtility.sendPacket(player, new SM_ALLIANCE_MEMBER_INFO(connectedMember, PlayerAllianceEvent.RECONNECT));
			PacketSendUtility.sendPacket(player, new SM_INSTANCE_INFO(connected, false, alliance));

			PacketSendUtility.sendPacket(connected, new SM_ALLIANCE_MEMBER_INFO(member, PlayerAllianceEvent.RECONNECT));
		}
		return true;
	}
}
