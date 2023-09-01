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

import java.util.List;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.house.House;
import com.aionemu.gameserver.model.house.HouseBidEntry;
import com.aionemu.gameserver.model.house.HouseStatus;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.services.HousingBidService;

/**
 * @author Rolandas
 */
public class SM_HOUSE_BIDS extends AionServerPacket {

	private boolean isFirst;
	private boolean isLast;
	private HouseBidEntry playerBid;
	private List<HouseBidEntry> houseBids;

	public SM_HOUSE_BIDS(boolean isFirstPacket, boolean isLastPacket, HouseBidEntry playerBid, List<HouseBidEntry> houseBids) {
		isFirst = isFirstPacket;
		isLast = isLastPacket;
		this.playerBid = playerBid;
		this.houseBids = houseBids;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		Player player = con.getActivePlayer();
		int secondsTillAuction = HousingBidService.getInstance().getSecondsTillAuction();

		writeC(isFirst ? 1 : 0);
		writeC(isLast ? 1 : 0);

		if (playerBid == null) {
			writeD(0);
			writeQ(0);
		}
		else {
			writeD(playerBid.getEntryIndex());
			writeQ(playerBid.getBidPrice());
		}

		List<House> playerHouses = player.getHouses();
		House sellHouse = null;
		for (House house : playerHouses) {
			if (house.getStatus() == HouseStatus.SELL_WAIT) {
				sellHouse = house;
				break;
			}
		}

		HouseBidEntry sellData = null;
		if (sellHouse != null) {
			sellData = HousingBidService.getInstance().getHouseBid(sellHouse.getObjectId());
			writeD(sellData.getEntryIndex());
			writeQ(sellData.getBidPrice());
		}
		else {
			writeD(0);
			writeQ(0);
		}

		writeH(houseBids.size());
		for (int n = 0; n < houseBids.size(); n++) {
			HouseBidEntry entry = houseBids.get(n);
			writeD(entry.getEntryIndex());
			writeD(entry.getLandId());
			writeD(entry.getAddress());
			writeD(entry.getBuildingId());
			if (sellData != null && entry.getEntryIndex() == sellData.getEntryIndex()) {
				writeD(0);
			}
			else if (HousingBidService.canBidHouse(player, entry.getMapId(), entry.getLandId())) {
				writeD(entry.getHouseType().getId());
			}
			else {
				writeD(0);
			}
			writeQ(entry.getBidPrice());
			writeQ(entry.getUnk2());
			writeD(entry.getBidCount());
			writeD(secondsTillAuction);
		}
	}
}
