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

import java.sql.Timestamp;
import java.util.Map;

import com.aionemu.gameserver.model.team.legion.Legion;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author Simple, CoolyT
 */
public class SM_LEGION_INFO extends AionServerPacket {

	/**
	 * Legion information *
	 */
	private Legion legion;

	/**
	 * This constructor will handle legion info
	 *
	 * @param legion
	 */
	public SM_LEGION_INFO(Legion legion) {
		this.legion = legion;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeS(legion.getLegionName());
		writeC(legion.getLegionLevel());
		writeD(legion.getLegionRank());
		writeH(legion.getDeputyPermission());
		writeH(legion.getCenturionPermission());
		writeH(legion.getLegionaryPermission());
		writeH(legion.getVolunteerPermission());
		writeQ(legion.getContributionPoints());
		writeQ(0); // definatly some kind of points ... but what ?!
		writeB(new byte[4]); // UNK but not a spacer ... ?!
		writeD(legion.getTerritory().getId()); // TerritoryId colors the terrritory on map
		writeD(0);
		writeD(0);
		writeS(legion.getLegionDiscription());
		writeC(legion.getLegionJoinType());
		writeH(legion.getMinLevel());
		writeH(-8161);// Unk

		/**
		 * Get Announcements List From DB By Legion *
		 */
		Map<Timestamp, String> announcementList = legion.getAnnouncementList().descendingMap();

		/**
		 * Show max 7 announcements *
		 */
		if (!announcementList.isEmpty()) {
			int i = 0;
			for (Timestamp unixTime : announcementList.keySet()) {
				writeS(announcementList.get(unixTime));
				writeD((int) (unixTime.getTime() / 1000));
				i++;
				if (i >= 7)
					break;

			}
		}
	}
}
