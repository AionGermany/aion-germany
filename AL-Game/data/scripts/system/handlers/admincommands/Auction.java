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
package admincommands;

import java.util.ArrayList;
import java.util.List;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.house.House;
import com.aionemu.gameserver.model.house.HouseBidEntry;
import com.aionemu.gameserver.model.house.HouseStatus;
import com.aionemu.gameserver.model.templates.housing.HouseType;
import com.aionemu.gameserver.services.HousingBidService;
import com.aionemu.gameserver.services.HousingService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;
import com.aionemu.gameserver.world.zone.ZoneName;

import javolution.util.FastList;

/**
 * @author Rolandas
 * @modified Luzien
 */
public class Auction extends AdminCommand {

	public Auction() {
		super("auction");
	}

	@Override
	public void execute(Player admin, String... params) {
		if (params.length < 1) {
			onFail(admin, null);
			return;
		}

		if ("remove".equals(params[0])) {
			if (params.length < 2) {
				onFail(admin, null);
				return;
			}
			String param = params[1].toUpperCase();
			List<House> housesToRemove = new ArrayList<House>();

			if ("HOUSE".equals(param.split("_")[0])) {
				House house = HousingService.getInstance().getHouseByName(params[1].toUpperCase());
				if (house == null || house.getStatus() != HouseStatus.SELL_WAIT) {
					PacketSendUtility.sendMessage(admin, "No such house!");
				}
				housesToRemove.add(house);
			}
			else {
				ZoneName zoneName = ZoneName.get(params[1]);
				if (zoneName.name().equals(ZoneName.NONE)) {
					PacketSendUtility.sendMessage(admin, "No such zone!");
					return;
				}
				for (House house : HousingService.getInstance().getCustomHouses()) {
					if (house.getStatus() != HouseStatus.SELL_WAIT) {
						continue;
					}
					float x = house.getX();
					float y = house.getY();
					float z = house.getZ();
					if (house.getPosition().getMapRegion().isInsideZone(zoneName, x, y, z)) {
						housesToRemove.add(house);
					}
				}
			}

			if (housesToRemove.size() == 0) {
				PacketSendUtility.sendMessage(admin, "Nothing to remove!");
				return;
			}

			boolean noSale = false;
			if (params.length == 3) {
				if (!"nosale".equals(params[2])) {
					onFail(admin, null);
					return;
				}
				noSale = true;
			}

			for (House house : housesToRemove) {
				if (HousingBidService.getInstance().removeHouseFromAuction(house, noSale)) {
					PacketSendUtility.sendMessage(admin, "Succesfully removed house " + house.getName());
				}
				else {
					PacketSendUtility.sendMessage(admin, "Failed to remove house " + house.getName());
				}
			}
		}
		else if ("add".equals(params[0])) {

			if (params.length < 3 || params.length > 4) {
				onFail(admin, null);
				return;
			}

			ZoneName zoneName = ZoneName.get(params[1]);
			if (zoneName.name().equals(ZoneName.NONE)) {
				PacketSendUtility.sendMessage(admin, "No such zone!");
				return;
			}

			HouseType houseType = null;
			try {
				houseType = HouseType.fromValue(params[2].toUpperCase());
			}
			catch (Exception e) {
			}

			if (houseType == null) {
				PacketSendUtility.sendMessage(admin, "No such house type!");
				return;
			}

			long bidPrice = 0;
			if (params.length == 4) {
				try {
					bidPrice = Long.parseLong(params[3]);
					if (bidPrice <= 0) {
						throw new IllegalArgumentException();
					}
				}
				catch (Exception e) {
					PacketSendUtility.sendMessage(admin, "Only positive numbers for the bid price!");
					return;
				}
			}

			boolean found = false;
			int counter = 0;

			for (House house : HousingService.getInstance().getCustomHouses()) {
				if (house.getOwnerId() != 0 || house.getHouseType() != houseType) {
					continue;
				}
				if (house.getStatus() == HouseStatus.INACTIVE) {
					continue;
				}
				if (house.getStatus() == HouseStatus.SELL_WAIT) {
					// check to see if the bid entry exists
					HouseBidEntry entry = HousingBidService.getInstance().getHouseBid(house.getObjectId());
					if (entry == null) {
						// reset status
						house.setStatus(HouseStatus.ACTIVE);
					}
					else {
						continue;
					}
				}
				float x = house.getX();
				float y = house.getY();
				float z = house.getZ();
				if (house.getPosition().getMapRegion().isInsideZone(zoneName, x, y, z)) {
					found = true;
					long price = bidPrice > 0 ? bidPrice : house.getDefaultAuctionPrice();
					if (HousingBidService.getInstance().addHouseToAuction(house, price)) {
						house.save();
						counter++;
					}
				}
			}

			if (found) {
				PacketSendUtility.sendMessage(admin, "Added " + counter + " houses of type " + houseType);
			}
			else {
				PacketSendUtility.sendMessage(admin, "No houses, all are occupied or already in auction!");
			}
		}
		else if ("addrandom".equals(params[0])) {
			if (params.length < 4 || params.length > 5) {
				onFail(admin, null);
				return;
			}

			String param = params[1].toUpperCase();
			Race race;
			if ("ALL".equals(param) || "PC_ALL".equals(param)) {
				race = Race.PC_ALL;
			}
			else if ("ELYOS".equals(param)) {
				race = Race.ELYOS;
			}
			else if ("ASMODIANS".equals(param)) {
				race = Race.ASMODIANS;
			}
			else {
				PacketSendUtility.sendMessage(admin, "Race not found! Use ALL | ELYOS | ASMODIANS!");
				return;
			}

			HouseType houseType = null;
			try {
				houseType = HouseType.fromValue(params[2].toUpperCase());
			}
			catch (Exception e) {
			}

			if (houseType == null) {
				PacketSendUtility.sendMessage(admin, "No such house type!");
				return;
			}

			int count = 0;
			try {
				count = Integer.parseInt(params[3]);
				if (count <= 0) {
					throw new IllegalArgumentException();
				}
			}
			catch (Exception e) {
				PacketSendUtility.sendMessage(admin, "Invalid count. Only positive numbers!");
				return;
			}
			long bidPrice = 0;
			if (params.length == 5) {
				try {
					bidPrice = Long.parseLong(params[4]);
					if (bidPrice <= 0) {
						throw new IllegalArgumentException();
					}
				}
				catch (Exception e) {
					PacketSendUtility.sendMessage(admin, "Only positive numbers for the bid price!");
					return;
				}
			}

			int counter = 0;
			FastList<House> houses = HousingService.getInstance().getCustomHouses();
			while (!houses.isEmpty() && counter < count) {
				House house = houses.get(Rnd.get(houses.size()));
				houses.remove(house);
				if (house.getOwnerId() != 0 || house.getHouseType() != houseType) {
					continue;
				}
				if (race != Race.PC_ALL) {
					int mapId = house.getAddress().getMapId();
					if (race.equals(Race.ELYOS)) {
						if (mapId != 210050000 && mapId != 700010000 && mapId != 210040000) {
							continue;
						}
					}
					else if (race.equals(Race.ASMODIANS)) {
						if (mapId != 710010000 && mapId != 220040000 && mapId != 220070000) {
							continue;
						}
					}
				}
				if (house.getStatus() == HouseStatus.INACTIVE) {
					continue;
				}
				if (house.getStatus() == HouseStatus.SELL_WAIT) {
					// check to see if the bid entry exists
					HouseBidEntry entry = HousingBidService.getInstance().getHouseBid(house.getObjectId());
					if (entry == null) {
						// reset status
						house.setStatus(HouseStatus.ACTIVE);
					}
					else {
						continue;
					}
				}

				long price = bidPrice > 0 ? bidPrice : house.getDefaultAuctionPrice();
				if (HousingBidService.getInstance().addHouseToAuction(house, price)) {
					house.save();
					counter++;
				}
			}

			if (counter > 0) {
				PacketSendUtility.sendMessage(admin, "Added " + counter + " houses of type " + houseType);
			}
			else {
				PacketSendUtility.sendMessage(admin, "No houses, all are occupied or already in auction!");
			}

		}
		else {
			onFail(admin, null);
		}

	}

	@Override
	public void onFail(Player player, String message) {
		PacketSendUtility.sendMessage(player, "syntax:\n" + " //auction add <zone_name> <house_type> [initial_bid]\n" + " //auction remove <HOUSE_id|zone_name> [nosale]\n" + " //auction addrandom <race> <house_type> <count> [initial_bid]\n" + "   zone_name = from zones xml files\n" + "   house_type = house, mansion, estate, palace\n" + "   initial_bid = initial bid price (if omitted, default is used)");
	}
}
