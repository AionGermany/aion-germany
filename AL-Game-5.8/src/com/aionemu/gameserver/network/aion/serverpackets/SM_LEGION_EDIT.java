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

import com.aionemu.gameserver.model.team.legion.Legion;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author Simple
 */
public class SM_LEGION_EDIT extends AionServerPacket {

	private int type;
	private Legion legion;
	private int unixTime;
	private String announcement;

	public SM_LEGION_EDIT(int type) {
		this.type = type;
	}

	public SM_LEGION_EDIT(int type, Legion legion) {
		this.type = type;
		this.legion = legion;
	}

	public SM_LEGION_EDIT(int type, int unixTime) {
		this.type = type;
		this.unixTime = unixTime;
	}

	public SM_LEGION_EDIT(int type, int unixTime, String announcement) {
		this.type = type;
		this.announcement = announcement;
		this.unixTime = unixTime;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeC(type);
		switch (type) {
			/**
			 * Change Legion Level *
			 */
			case 0x00:
				writeC(legion.getLegionLevel());
				break;
			/**
			 * Change Legion Rank *
			 */
			case 0x01:
				writeD(legion.getLegionRank());
				break;
			/**
			 * Change Legion Permissions *
			 */
			case 0x02:
				writeH(legion.getDeputyPermission());
				writeH(legion.getCenturionPermission());
				writeH(legion.getLegionaryPermission());
				writeH(legion.getVolunteerPermission());
				break;
			/**
			 * Change Legion Contributions *
			 */
			case 0x03:
				writeQ(legion.getContributionPoints()); // get Contributions
				break;
			case 0x04:
				writeQ(legion.getLegionWarehouse().getKinah());
				break;
			/**
			 * Change Legion Announcement *
			 */
			case 0x05:
				writeS(announcement);
				writeD(unixTime);
				break;
			/**
			 * Disband Legion *
			 */
			case 0x06:
				writeD(unixTime);
				break;
			/**
			 * Recover Legion *
			 */
			case 0x07:
				break;
			/**
			 * Refresh Legion Announcement? *
			 */
			case 0x08:
				break;

			case 0x10:
				break;
			case 0x0C:
				writeS(legion.getLegionDiscription());
				break;

			case 0x0D:
				writeC(legion.getLegionJoinType());
				break;

			case 0x0E:
				writeH(legion.getMinLevel());
				break;

		}
	}
}
