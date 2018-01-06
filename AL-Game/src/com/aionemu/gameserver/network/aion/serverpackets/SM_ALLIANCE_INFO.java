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
package com.aionemu.gameserver.network.aion.serverpackets;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.team2.alliance.PlayerAlliance;
import com.aionemu.gameserver.model.team2.common.legacy.LootGroupRules;
import com.aionemu.gameserver.model.team2.league.LeagueMember;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author Sarynth, xTz
 */
public class SM_ALLIANCE_INFO extends AionServerPacket {

	private LootGroupRules lootRules;
	private PlayerAlliance alliance;
	private int leaderid;
	private int groupid;
	private final int messageId;
	private final String message;
	public static final int FORCE_PROMOTE_MANAGER = 1300984;
	public static final int FORCE_DEMOTE_MANAGER = 1300985;
	public static final int UNION_ENTER = 1400560;
	public static final int UNION_LEAVE = 1400572;
	public static final int UNION_BAN_HIM = 1400574;
	public static final int UNION_BAN_ME = 1400576;

	public SM_ALLIANCE_INFO(PlayerAlliance alliance) {
		this(alliance, 0, StringUtils.EMPTY);
	}

	public SM_ALLIANCE_INFO(PlayerAlliance alliance, int messageId, String message) {
		this.alliance = alliance;
		groupid = alliance.getObjectId();
		leaderid = alliance.getLeader().getObjectId();
		lootRules = alliance.getLootGroupRules();
		this.messageId = messageId;
		this.message = message;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		Player player = con.getActivePlayer();
		writeH(alliance.groupSize());
		writeD(groupid);
		writeD(leaderid);
		writeD(player.getWorldId()); // 4.9 mapid
		Collection<Integer> ids = alliance.getViceCaptainIds();
		for (Integer id : ids) {
			writeD(id);
		}
		for (int i = 0; i < 4 - ids.size(); i++) {
			writeD(0);
		}
		writeD(lootRules.getLootRule().getId());
		writeD(lootRules.getMisc());
		writeD(lootRules.getCommonItemAbove());
		writeD(lootRules.getSuperiorItemAbove());
		writeD(lootRules.getHeroicItemAbove());
		writeD(lootRules.getFabledItemAbove());
		writeD(lootRules.getEthernalItemAbove());
		writeD(lootRules.getAutodistribution().getId());
		writeD(0x02);
		writeC(0x00);
		writeD(alliance.getTeamType().getType());// Need to update 3F to FF ?
		writeD(alliance.getTeamType().getSubType()); // 3.5
		writeD(alliance.isInLeague() ? alliance.getLeague().getTeamId() : 0);
		for (int a = 0; a < 4; a++) {
			writeD(a); // group num
			writeD(1000 + a); // group id
		}
		writeD(messageId); // System message ID
		writeS(messageId != 0 ? message : StringUtils.EMPTY); // System message
		if (alliance.isInLeague()) {
			//TODO LootRules !!
			lootRules = alliance.getLeague().getLootGroupRules();
			writeH(alliance.getLeague().size());
			writeD(lootRules.getLootRule().getId()); // loot rule type - 0 freeforall, 1 roundrobin, 2 leader
			writeD(lootRules.getAutodistribution().getId()); // autoDistribution - 0 or 1
			writeD(lootRules.getCommonItemAbove()); // this.common_item_above); - 0 normal 2 roll 3 bid
			writeD(lootRules.getSuperiorItemAbove()); // this.superior_item_above); - 0 normal 2 roll 3 bid
			writeD(lootRules.getHeroicItemAbove()); // this.heroic_item_above); - 0 normal 2 roll 3 bid
			writeD(lootRules.getFabledItemAbove()); // this.fabled_item_above); - 0 normal 2 roll 3 bid
			writeD(lootRules.getEthernalItemAbove()); // this.ethernal_item_above); - 0 normal 2 roll 3 bid
			writeD(2); // this.over_ethernal); - 0 normal 2 roll 3 bid
			writeD(2); // this.over_over_ethernal); - 0 normal 2 roll 3 bid
			writeD(226); // Todo check if it is always 226
			writeD(4); 
			writeC(0); 
			for (LeagueMember leagueMember : alliance.getLeague().getSortedMembers()) {
				writeD(leagueMember.getLeaguePosition());
				writeD(leagueMember.getObjectId());
				writeD(leagueMember.getObject().size());
				writeS(leagueMember.getObject().getLeaderObject().getName());
				writeD(leagueMember.getObject().getLeaderObject().getWorldId()); // TODO Looks like some ObjId and not mapId
				writeD(leagueMember.getObject().getObjectId());
			}
		}
	}
}
