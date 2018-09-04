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
package com.aionemu.gameserver.services;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.joda.time.DateTime;
import org.quartz.CronExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.configs.main.HousingConfig;
import com.aionemu.gameserver.configs.main.LoggingConfig;
import com.aionemu.gameserver.dao.HouseBidsDAO;
import com.aionemu.gameserver.dao.PlayerDAO;
import com.aionemu.gameserver.dao.ServerVariablesDAO;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.TribeClass;
import com.aionemu.gameserver.model.gameobjects.Letter;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PlayerCommonData;
import com.aionemu.gameserver.model.gameobjects.player.PlayerHouseOwnerFlags;
import com.aionemu.gameserver.model.house.House;
import com.aionemu.gameserver.model.house.HouseBidEntry;
import com.aionemu.gameserver.model.house.HouseStatus;
import com.aionemu.gameserver.model.house.PlayerHouseBid;
import com.aionemu.gameserver.model.templates.housing.HouseType;
import com.aionemu.gameserver.model.templates.housing.HousingLand;
import com.aionemu.gameserver.model.templates.housing.Sale;
import com.aionemu.gameserver.network.aion.serverpackets.SM_HOUSE_ACQUIRE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_HOUSE_OWNER_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_RECEIVE_BIDS;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.mail.AuctionResult;
import com.aionemu.gameserver.services.mail.MailFormatter;
import com.aionemu.gameserver.taskmanager.AbstractCronTask;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.WorldMapType;

import javolution.util.FastMap;

/**
 * @author Rolandas
 */
public class HousingBidService extends AbstractCronTask {

	private static final Logger log = LoggerFactory.getLogger("HOUSE_AUCTION_LOG");
	private static final String registerEndExpression = HousingConfig.HOUSE_REGISTER_END;
	private static CronExpression registerDateExpr;
	private static final FastMap<Integer, HouseBidEntry> houseBids;
	private static final FastMap<Integer, HouseBidEntry> playerBids;
	private static final FastMap<Integer, HouseBidEntry> bidsByIndex;
	private static int timeProlonged = 0;
	private static boolean isDataLoaded = false;
	private static HousingBidService instance;

	static {
		houseBids = FastMap.newInstance();
		playerBids = FastMap.newInstance();
		bidsByIndex = FastMap.newInstance();
		try {
			instance = new HousingBidService(HousingConfig.HOUSE_AUCTION_TIME);
		}
		catch (ParseException pe) {
			log.error("[HousingBidService] Error at parsing housing bids: " + pe.getMessage());
		}
	}

	private HousingBidService(String auctionTime) throws ParseException {
		super(auctionTime);
	}

	public static final HousingBidService getInstance() {
		return instance;
	}

	@Override
	protected long getRunDelay() {
		return (long) getSecondsTillAuction() * 1000;
	}

	@Override
	protected String getServerTimeVariable() {
		return "auctionTime";
	}

	@Override
	protected boolean canRunOnInit() {
		return true;
	}

	@Override
	protected void postInit() {
		try {
			registerDateExpr = new CronExpression(registerEndExpression);
		}
		catch (ParseException e) {
			log.error("[HousingBidService] Error with CronExpression: " + e.getMessage());
		}

		ServerVariablesDAO dao = DAOManager.getDAO(ServerVariablesDAO.class);
		timeProlonged = dao.load("auctionProlonged");
	}

	private HousingBidService() throws ParseException {
		super(HousingConfig.HOUSE_AUCTION_TIME);
	}

	public void start() {
		loadBidData();
		if (HousingConfig.FILL_HOUSE_BIDS_AUTO) {
			log.info("[HousingBidService] Auction auto filling is enabled.");
			int added = fillBidData();
			log.info("[HousingBidService] added " + added + " new house bids.");
		}
		int minutes = getMinutesTillAuction();
		String timeString = minutes / 24 / 60 + " days " + minutes / 60 % 24 + " hours and " + minutes % 60 + " minutes";
		log.info("[HousingBidService] Starting Auction in " + timeString);
		isDataLoaded = true;
	}

	private int fillBidData() {
		int count = 0;
		List<House> houses = HousingService.getInstance().getCustomHouses();
		while (!houses.isEmpty()) {
			House house = houses.get(Rnd.get(houses.size()));
			houses.remove(house);
			if (house.getOwnerId() != 0 || houseBids.containsKey(house.getObjectId())) {
				continue;
			}
			if (!checkAutoFillingLimits(house.getPlayerRace(), house.getHouseType())) {
				continue;
			}
			addHouseToAuction(house, house.getDefaultAuctionPrice());
			count++;
		}
		return count;
	}

	private boolean checkAutoFillingLimits(Race race, HouseType type) {
		int bidsCount = getBidsCountByType(race, type);
		switch (type) {
			case HOUSE:
				if (bidsCount >= HousingConfig.FILL_AUTO_HOUSES_COUNT) {
					return false;
				}
				break;
			case MANSION:
				if (bidsCount >= HousingConfig.FILL_AUTO_MANSION_COUNT) {
					return false;
				}
				break;
			case ESTATE:
				if (bidsCount >= HousingConfig.FILL_AUTO_ESTATE_COUNT) {
					return false;
				}
				break;
			case PALACE:
				if (bidsCount >= HousingConfig.FILL_AUTO_PALACE_COUNT) {
					return false;
				}
				break;
			default:
				break;
		}
		return true;
	}

	private int getBidsCountByType(Race race, HouseType type) {
		int count = 0;
		for (HouseBidEntry entry : houseBids.values()) {
			HousingLand land = DataManager.HOUSE_DATA.getLand(entry.getLandId());
			Race entryRace = DataManager.NPC_DATA.getNpcTemplate(land.getManagerNpcId()).getTribe() == TribeClass.GENERAL ? Race.ELYOS : Race.ASMODIANS;
			if (entryRace == race && entry.getHouseType() == type) {
				count++;
			}
		}
		return count;
	}

	private void loadBidData() {
		Set<PlayerHouseBid> playerBidData = DAOManager.getDAO(HouseBidsDAO.class).loadBids();

		List<PlayerHouseBid> sortedBids = new ArrayList<PlayerHouseBid>(playerBidData);
		log.info("[HousingBidService] Loaded " + playerBidData.size() + " House Bids");
		Collections.sort(sortedBids);

		FastMap<Integer, House> housesById = FastMap.newInstance();
		for (House house : HousingService.getInstance().getCustomHouses()) {
			housesById.put(house.getObjectId(), house);
		}

		int entryIndex = 1;
		for (PlayerHouseBid playerBid : sortedBids) {
			House house = housesById.get(playerBid.getHouseId());
			if (house == null) {
				log.warn("[HousingBidService] Missing house " + playerBid.getHouseId() + " player " + playerBid.getPlayerId() + " bid.");
				continue;
			}

			HouseBidEntry entry = houseBids.get(house.getObjectId());
			if (entry == null) {
				entry = new HouseBidEntry(house, entryIndex, playerBid.getBidOffer());
				houseBids.put(house.getObjectId(), entry);
				bidsByIndex.put(entryIndex++, entry);
			}
			else if (entry.getBidPrice() < playerBid.getBidOffer()) {
				// find max price
				entry.setBidPrice(playerBid.getBidOffer());
			}

			if (playerBid.getPlayerId() != 0) {
				HouseBidEntry playerEntry = (HouseBidEntry) entry.Clone();
				playerEntry.setBidPrice(playerBid.getBidOffer());
				playerBids.put(playerBid.getPlayerId(), playerEntry);

				entry.setLastBiddingPlayer(playerBid.getPlayerId());
				entry.setLastBidTime(playerBid.getTime().getTime());
				entry.incrementBidCount();
			}
		}

		// check to see if bids were not removed from DB manually
		for (House house : housesById.values()) {
			if (house.getOwnerId() == 0) {
				continue;
			}
			if (house.getStatus() == HouseStatus.SELL_WAIT && !houseBids.containsKey(house.getObjectId())) {
				log.warn("[HousingBidService] House address=" + house.getAddress().getId() + " has status SELL_WAIT but no bid exists. Activated.");
				house.setStatus(HouseStatus.ACTIVE);
				house.setSellStarted(null);
				house.save();
			}
		}
	}

	@Override
	protected void executeTask() {
		if (!HousingConfig.ENABLE_HOUSE_AUCTIONS) {
			return;
		}

		while (!isDataLoaded) {
			try {
				Thread.sleep(500);
			}
			catch (InterruptedException e) {
				return;
			}
		}

		Map<HouseBidEntry, Integer> winners = new HashMap<HouseBidEntry, Integer>();
		Map<HouseBidEntry, Integer> successSell = new HashMap<HouseBidEntry, Integer>();
		Map<HouseBidEntry, Integer> failedSell = new HashMap<HouseBidEntry, Integer>();

		for (Entry<Integer, HouseBidEntry> playerBid : playerBids.entrySet()) {
			int playerId = playerBid.getKey();
			HouseBidEntry houseBid = getBidByEntryIndex(playerBid.getValue().getEntryIndex());
			House house = HousingService.getInstance().getHouseByAddress(houseBid.getAddress());
			if (playerBid.getValue().getBidPrice() == houseBid.getBidPrice()) {
				// The player can not be top 1 for both bids, no need to check other
				if (house.getOwnerId() == 0) {
					winners.put(houseBid, playerId); // our sold house
				}
				else {
					successSell.put(houseBid, playerId); // player sold house
				}
			}
		}

		for (HouseBidEntry houseBid : houseBids.values()) {
			House house = HousingService.getInstance().getHouseByAddress(houseBid.getAddress());
			if (houseBid.getBidCount() > 0) {
				continue;
			}
			if (house.getOwnerId() != 0) {
				// add only player sold houses
				failedSell.put(houseBid, house.getOwnerId());
			}
		}

		// send mails + messages if players are online
		if (LoggingConfig.LOG_HOUSE_AUCTION) {
			log.info("[HousingBidService] ##### " + winners.size( ) + " Houses sold by Auction System #####");
		}

		// check houses sold by administrators
		for (Entry<HouseBidEntry, Integer> winData : winners.entrySet()) {
			House wonHouse = HousingService.getInstance().getHouseByAddress(winData.getKey().getAddress());
			if (getPlayerData(winData.getValue()) == null) {
				log.warn("[HousingBidService] Missing Player with ID:" + winData.getValue() + " for Housebid on address:" + winData.getKey().getAddress());
				continue;
			}
			AuctionResult result = completeHouseSell(getPlayerData(winData.getValue()), wonHouse);

			if (LoggingConfig.LOG_HOUSE_AUCTION) {
				log.info("[HousingBidService] Address " + wonHouse.getAddress().getId() + " sold for price " + winData.getKey().getBidPrice() + " (bid count: " + winData.getKey().getBidCount() + "; result: " + result + ") to player " + winData.getKey().getLastBiddingPlayer());
			}
		}

		// check houses sold by players
		long time = System.currentTimeMillis();

		if (LoggingConfig.LOG_HOUSE_AUCTION) {
			log.info("[HousingBidService] ##### " + successSell.size() + " Houses auctioned by players #####");
		}
		for (Entry<HouseBidEntry, Integer> sellData : successSell.entrySet()) {
			House soldHouse = HousingService.getInstance().getHouseByAddress(sellData.getKey().getAddress());
			PlayerCommonData buyerPcd = getPlayerData(sellData.getValue());
			PlayerCommonData sellerPcd = getPlayerData(soldHouse.getOwnerId());

			if (buyerPcd.getPlayerObjId() == soldHouse.getOwnerId()) {
				log.warn("[HousingBidService] Selling house to its own owner, cancelling!");
				continue;
			}

			if (sellerPcd.isOnline()) {
				PacketSendUtility.sendPacket(sellerPcd.getPlayer(), SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_AUCTION_SUCCESS(sellData.getKey().getAddress()));
			}
			if (buyerPcd.isOnline()) {
				PacketSendUtility.sendPacket(buyerPcd.getPlayer(), SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_BID_WIN(sellData.getKey().getAddress()));
			}

			long returnKinah = sellData.getKey().getBidPrice() + sellData.getKey().getRefundKinah();
			if (soldHouse.isInGracePeriod()) {
				soldHouse.revokeOwner();
				// activate previously obtained house
				House newHouse = HousingService.getInstance().activateBoughtHouse(sellerPcd.getPlayerObjId());
				if (sellerPcd.isOnline()) {
					PacketSendUtility.sendPacket(sellerPcd.getPlayer(), new SM_HOUSE_ACQUIRE(sellerPcd.getPlayerObjId(), newHouse.getAddress().getId(), true));
					PacketSendUtility.sendPacket(sellerPcd.getPlayer(), new SM_HOUSE_OWNER_INFO(sellerPcd.getPlayer(), newHouse));
					sellerPcd.getPlayer().setHouseRegistry(newHouse.getRegistry());
				}
				MailFormatter.sendHouseAuctionMail(newHouse, sellerPcd, AuctionResult.GRACE_SUCCESS, time, returnKinah);
			}
			else {
				MailFormatter.sendHouseAuctionMail(soldHouse, sellerPcd, AuctionResult.SUCCESS_SALE, time, returnKinah);
				soldHouse.revokeOwner();
			}

			AuctionResult result = completeHouseSell(buyerPcd, soldHouse);

			if (LoggingConfig.LOG_HOUSE_AUCTION) {
				log.info("[HousingBidService] Address " + soldHouse.getAddress().getId() + " sold by player " + sellerPcd.getPlayerObjId() + " for price " + sellData.getKey().getBidPrice() + " (bid count: " + sellData.getKey().getBidCount() + "; result: " + result + ") to player " + sellData.getKey().getLastBiddingPlayer());
			}
		}

		// not sold houses
		for (Entry<HouseBidEntry, Integer> notSoldData : failedSell.entrySet()) {
			HouseBidEntry bidEntry = notSoldData.getKey();
			PlayerCommonData sellerPcd = getPlayerData(notSoldData.getValue());
			House bidHouse = HousingService.getInstance().getHouseByAddress(bidEntry.getAddress());

			if (sellerPcd.isOnline()) {
				PacketSendUtility.sendPacket(sellerPcd.getPlayer(), SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_AUCTION_FAIL(bidHouse.getAddress().getId()));
			}

			AuctionResult result = AuctionResult.FAILED_SALE;
			long compensation = 0;

			if (bidHouse.isInGracePeriod()) {
				long timePassed = (getAuctionStartTime() - bidHouse.getSellStarted().getTime()) / 1000;
				if (timePassed > 7 * 24 * 3600) { // more than one week, i.e. 2 weeks passed
					bidHouse.revokeOwner();
					House activatedHouse = HousingService.getInstance().activateBoughtHouse(sellerPcd.getPlayerObjId());
					if (sellerPcd.isOnline()) {
						if (activatedHouse != null) {
							PacketSendUtility.sendPacket(sellerPcd.getPlayer(), new SM_HOUSE_ACQUIRE(sellerPcd.getPlayerObjId(), activatedHouse.getAddress().getId(), true));
							sellerPcd.getPlayer().setHouseRegistry(activatedHouse.getRegistry());
						}
						PacketSendUtility.sendPacket(sellerPcd.getPlayer(), new SM_HOUSE_OWNER_INFO(sellerPcd.getPlayer(), activatedHouse));
					}

					result = AuctionResult.GRACE_FAIL;
					time = System.currentTimeMillis();
					compensation = bidHouse.getDefaultAuctionPrice();
				}
			}
			else {
				bidHouse.setStatus(HouseStatus.ACTIVE);
				time = bidHouse.getSellStarted().getTime();
			}

			bidHouse.save();
			MailFormatter.sendHouseAuctionMail(bidHouse, sellerPcd, result, time, compensation);
			if (LoggingConfig.LOG_HOUSE_AUCTION) {
				log.info("[HousingBidService] Address " + bidHouse.getAddress().getId() + " not sold for price " + bidEntry.getBidPrice() + " (result: " + result + "; return: " + compensation + " kinah) by player " + sellerPcd.getPlayerObjId());
			}
		}

		List<HouseBidEntry> copy = new ArrayList<HouseBidEntry>();
		copy.addAll(houseBids.values());

		houseBids.clear();
		playerBids.clear();
		bidsByIndex.clear();
		
		// add back auto auctioned houses (with grace period ended) + admin houses not sold
		if (LoggingConfig.LOG_HOUSE_AUCTION) {
			log.info("[HousingBidService] ##### " + (copy.size() - winners.size()) + " Houses added back to auction #####");
		}

		for (HouseBidEntry houseBid : copy) {
			House house = HousingService.getInstance().getHouseByAddress(houseBid.getAddress());
			DAOManager.getDAO(HouseBidsDAO.class).deleteHouseBids(house.getObjectId());
			if (house.getOwnerId() == 0) {
				house.setStatus(HouseStatus.NOSALE);
				addHouseToAuction(house);
				if (LoggingConfig.LOG_HOUSE_AUCTION) {
					log.info("[HousingBidService] Address " + houseBid.getAddress() + " not sold for price " + houseBid.getBidPrice());
				}
			}
		}
	}

	@Override
	protected void postRun() {
		ServerVariablesDAO dao = DAOManager.getDAO(ServerVariablesDAO.class);
		timeProlonged = 0;
		dao.store("auctionProlonged", timeProlonged);
	}

	/**
	 * Milliseconds of auction start time
	 *
	 * @return one week ago before the auction time
	 */
	public long getAuctionStartTime() {
		return (long) (getRunTime() - 7 * 24 * 3600) * 1000;
	}

	public int getSecondsTillAuction() {
		int left = (int) (getRunTime() - System.currentTimeMillis() / 1000);
		left += timeProlonged * 60;
		if (left < 0) {
			return 0;
		}
		return left;
	}

	public int getMinutesTillAuction() {
		return getSecondsTillAuction() / 60;
	}

	public boolean isBiddingAllowed() {
		DateTime now = DateTime.now();
		DateTime auctionEnd = new DateTime(((long) getRunTime() + timeProlonged * 60) * 1000);
		return !(now.getDayOfWeek() == auctionEnd.getDayOfWeek() && auctionEnd.minusDays(1).isAfterNow());
	}

	public boolean isRegisteringAllowed() {
		DateTime now = DateTime.now();
		DateTime registerEnd = new DateTime(registerDateExpr.getTimeAfter(now.toDate()));
		DateTime auctionEnd = new DateTime(((long) getRunTime() + timeProlonged * 60) * 1000);
		return !(now.getDayOfWeek() == registerEnd.getDayOfWeek() && now.getHourOfDay() >= registerEnd.getHourOfDay() || (now.getDayOfWeek() == auctionEnd.getDayOfWeek() && now.getHourOfDay() <= auctionEnd.getHourOfDay()));
	}

	private PlayerCommonData getPlayerData(int objectId) {
		Player player = World.getInstance().findPlayer(objectId);
		if (player == null) {
			return DAOManager.getDAO(PlayerDAO.class).loadPlayerCommonData(objectId);
		}
		return player.getCommonData();
	}

	public AuctionResult completeHouseSell(PlayerCommonData winner, House obtainedHouse) {
		House winnerHouse = HousingService.getInstance().getPlayerStudio(winner.getPlayerObjId());
		AuctionResult result = AuctionResult.WIN_BID;
		long time = System.currentTimeMillis();
		if (winnerHouse != null) {
			winnerHouse.revokeOwner();
		}
		else {
			int address = HousingService.getInstance().getPlayerAddress(winner.getPlayerObjId());
			if (address > 0) { // old house exists
				winnerHouse = HousingService.getInstance().getHouseByAddress(address);
				// grace period start, sell time remains from this auction
				winnerHouse.setSellStarted(new Timestamp(getAuctionStartTime()));
				// make the new house inactive until the old one is sold
				obtainedHouse.setStatus(HouseStatus.INACTIVE);
				result = AuctionResult.GRACE_START;
				// time = new DateTime((long) (getRunTime() * 1000)).plusWeeks(2).getMillis();
				time = new DateTime(getRunTime() * 1000L).plusWeeks(2).getMillis();
			}
		}
		obtainedHouse.setOwnerId(winner.getPlayerObjId());
		if (result == AuctionResult.WIN_BID) {
			obtainedHouse.setAcquiredTime(new Timestamp(System.currentTimeMillis()));
			obtainedHouse.setStatus(HouseStatus.ACTIVE);
			obtainedHouse.setFeePaid(true);
			obtainedHouse.setNextPay(null);
			obtainedHouse.setSellStarted(null);
			obtainedHouse.reloadHouseRegistry();
			obtainedHouse.save();
		}
		if (winner.isOnline()) {
			if (result == AuctionResult.WIN_BID) {
				winner.getPlayer().setHouseRegistry(obtainedHouse.getRegistry());
				winner.getPlayer().setBuildingOwnerState(PlayerHouseOwnerFlags.HOUSE_OWNER.getId());
				PacketSendUtility.sendPacket(winner.getPlayer(), new SM_HOUSE_ACQUIRE(winner.getPlayerObjId(), obtainedHouse.getAddress().getId(), true));
				PacketSendUtility.sendPacket(winner.getPlayer(), new SM_HOUSE_OWNER_INFO(winner.getPlayer(), obtainedHouse));
			}
			PacketSendUtility.sendPacket(winner.getPlayer(), SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_BID_WIN(obtainedHouse.getAddress().getId()));
		}
		MailFormatter.sendHouseAuctionMail(obtainedHouse, winner, result, time, 0);
		obtainedHouse.getController().kickVisitors(null, true, true);
		return result;
	}

	public boolean addHouseToAuction(House house) {
		return addHouseToAuction(house, house.getDefaultAuctionPrice());
	}

	public boolean addHouseToAuction(House house, long initialPrice) {
		if (house.getStatus() == HouseStatus.SELL_WAIT) {
			return false;
		}
		house.setStatus(HouseStatus.SELL_WAIT);
		int maxIndex = 1;
		HouseBidEntry bidEntry = null;

		synchronized (bidsByIndex) {
			for (Integer index : bidsByIndex.keySet()) {
				if (index > maxIndex) {
					maxIndex = index;
				}
			}
			bidEntry = new HouseBidEntry(house, ++maxIndex, initialPrice);
			bidsByIndex.putEntry(maxIndex, bidEntry);
		}

		synchronized (houseBids) {
			houseBids.put(house.getObjectId(), bidEntry);
		}

		Timestamp time = new Timestamp(System.currentTimeMillis());
		if (house.getSellStarted() == null) { // dont overwrite grace start time
			house.setSellStarted(time);
		}
		house.save();
		return DAOManager.getDAO(HouseBidsDAO.class).addBid(0, house.getObjectId(), initialPrice, time);
	}

	/**
	 * Remove house from auction. Currently from admin command, both for player and non-player houses. Returns kinah for the selling player and the last bidder if such exist
	 */
	public boolean removeHouseFromAuction(House house, boolean noSale) {
		if (house.getStatus() != HouseStatus.SELL_WAIT) {
			return false;
		}

		HouseBidEntry bidEntry = null;
		HouseBidEntry playerBid = null;
		Integer lastPlayer = null;

		synchronized (houseBids) {
			bidEntry = houseBids.remove(house.getObjectId());
			if (bidEntry == null) {
				return false;
			}
			lastPlayer = bidEntry.getLastBiddingPlayer();
			playerBid = playerBids.remove(lastPlayer);
		}

		synchronized (bidsByIndex) {
			bidsByIndex.remove(bidEntry.getEntryIndex());
		}

		PlayerCommonData pcd = null;
		if (house.getOwnerId() != 0) {
			// player put this house himself, refund everything
			if (house.isInGracePeriod()) {
				house.setSellStarted(null);
			}
			pcd = getPlayerData(house.getOwnerId());
			MailFormatter.sendHouseAuctionMail(house, pcd, AuctionResult.CANCELED_BID, System.currentTimeMillis(), bidEntry.getBidPrice() + bidEntry.getRefundKinah());
			house.setStatus(HouseStatus.ACTIVE);
		}
		else {
			house.setStatus(noSale ? HouseStatus.NOSALE : HouseStatus.ACTIVE);
		}
		if (lastPlayer != null) {
			// return bid price only to the last bidder
			pcd = getPlayerData(lastPlayer);
			MailFormatter.sendHouseAuctionMail(house, pcd, AuctionResult.CANCELED_BID, System.currentTimeMillis(), playerBid.getBidPrice());
		}

		DAOManager.getDAO(HouseBidsDAO.class).deleteHouseBids(house.getObjectId());
		house.save();

		return true;
	}

	public synchronized void placeBid(Player player, int entryIndex, long bidOffer) {
		// prevent this earlier (problem are house signs which allow bidding)
		if ((player.getBuildingOwnerStates() & PlayerHouseOwnerFlags.BIDDING_ALLOWED.getId()) == 0) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_CANT_OWN_NOT_COMPLETE_QUEST(player.getRace() == Race.ELYOS ? 18802 : 28802));
			return;
		}
		int minutesLeft = getMinutesTillAuction();
		if (minutesLeft == 0) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_CANT_BID_TIMEOUT);
			return;
		}

		HouseBidEntry entry = getBidByEntryIndex(entryIndex);
		if (entry == null) {
			return;
		}

		if (player.getInventory().getKinah() < bidOffer) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_NOT_ENOUGH_MONEY);
			return;
		}

		// Server side check for own house. Client side exists, but to be sure
		House bidHouse = HousingService.getInstance().getHouseByAddress(entry.getAddress());
		if (player.getObjectId() == bidHouse.getOwnerId()) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_CANT_BID_MY_HOUSE);
			return;
		}

		int playerAddress = HousingService.getInstance().getPlayerAddress(player.getObjectId());
		House playerHouse = HousingService.getInstance().getHouseByAddress(playerAddress);
		if (playerHouse != null && playerHouse.isInGracePeriod()) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_CANT_BID_GRACE_HOUSE);
			return;
		}

		int minLevel = getMinBidLevel(player, entry.getMapId(), entry.getLandId());
		if (minLevel > player.getLevel()) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_CANT_BID_LOW_LEVEL(minLevel));
			return;
		}

		if (playerHouse != null && !playerHouse.isFeePaid() && HousingConfig.ENABLE_HOUSE_PAY) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_CANT_BID_OVERDUE);
			return;
		}

		if (bidOffer - entry.getBidPrice() >= entry.getBidPrice() * HousingConfig.HOUSE_AUCTION_BID_LIMIT / 100f) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_CANT_BID_EXCESS_ACCOUNT);
			return;
		}

		HouseBidEntry currentBid = playerBids.get(player.getObjectId());
		if (currentBid != null) {
			if (entry.getLastBiddingPlayer() == player.getObjectId()) {
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_CANT_BID_SUCC_BID_HOUSE);
				return;
			}
			HouseBidEntry houseBid = getBidByEntryIndex(currentBid.getEntryIndex());
			if (houseBid.getBidPrice() == currentBid.getBidPrice()) {
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_CANT_BID_OTHER_HOUSE);
				return;
			}
		}

		if (minutesLeft < 5 && timeProlonged < 30) {
			timeProlonged += 5;
			// save time
			ThreadPoolManager.getInstance().execute(new Runnable() {

				@Override
				public void run() {
					DAOManager.getDAO(ServerVariablesDAO.class).store("auctionProlonged", timeProlonged);
				}
			});
		}
		else if (!isBiddingAllowed()) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_CANT_BID_TIMEOUT);
			return;
		}

		if (bidOffer > entry.getBidPrice() || entry.getBidCount() == 0) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_BID_SUCCESS(entry.getAddress()));

			Timestamp time = new Timestamp(System.currentTimeMillis());

			player.getInventory().decreaseKinah(bidOffer);

			int previousPlayer = entry.getLastBiddingPlayer();

			if (previousPlayer > 0) {
				PlayerCommonData prevPcd = getPlayerData(previousPlayer);
				if (prevPcd.isOnline()) {
					PacketSendUtility.sendPacket(prevPcd.getPlayer(), SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_BID_CANCEL);
				}
				MailFormatter.sendHouseAuctionMail(bidHouse, prevPcd, AuctionResult.FAILED_BID, time.getTime(), entry.getBidPrice());
			}

			entry.incrementBidCount();
			entry.setLastBiddingPlayer(player.getObjectId());
			entry.setLastBidTime(time.getTime());
			entry.setBidPrice(bidOffer);

			HouseBidEntry playerBid = (HouseBidEntry) entry.Clone();
			playerBids.put(player.getObjectId(), playerBid);

			DAOManager.getDAO(HouseBidsDAO.class).addBid(player.getObjectId(), bidHouse.getObjectId(), bidOffer, time);

			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_PRICE_CHANGE(bidOffer));
			PacketSendUtility.sendPacket(player, new SM_RECEIVE_BIDS(0));
		}
	}

	/**
	 * Notify about new auction results or maintenance checks but only once (based on system mail checks and login time)
	 */
	public void onPlayerLogin(Player player) {
		if (player.getMailbox() == null) {
			return;
		}

		List<Letter> letters = player.getMailbox().getNewSystemLetters("$$HS_AUCTION_MAIL");
		boolean needsRefresh = false;

		for (Letter letter : letters) {
			String[] titleParts = letter.getTitle().split(",");
			String[] bodyParts = letter.getMessage().split(",");
			AuctionResult result = AuctionResult.getResultFromId(Integer.parseInt(titleParts[0]));
			if (result == AuctionResult.FAILED_BID) {
				needsRefresh = true;
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_BID_CANCEL);
			}
			else if (result == AuctionResult.WIN_BID || result == AuctionResult.GRACE_START) {
				needsRefresh = true;
				int address = Integer.parseInt(bodyParts[1]);
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_BID_WIN(address));
			}
			else if (result == AuctionResult.FAILED_SALE) {
				needsRefresh = true;
				int address = Integer.parseInt(bodyParts[1]);
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_AUCTION_FAIL(address));
			}
			else if (result == AuctionResult.SUCCESS_SALE || result == AuctionResult.GRACE_SUCCESS) {
				needsRefresh = true;
				int address = Integer.parseInt(bodyParts[1]);
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_AUCTION_SUCCESS(address));
			}
		}

		if (needsRefresh) {
			PacketSendUtility.sendPacket(player, new SM_RECEIVE_BIDS(0));
		}

		letters = player.getMailbox().getNewSystemLetters("$$HS_OVERDUE_");
		for (Letter letter : letters) {
			if (letter.getSenderName().endsWith("FINAL") || letter.getSenderName().endsWith("3RD")) {
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_SEQUESTRATE);
			}
			else {
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_OVERDUE);
			}
		}
	}

	public HouseBidEntry getHouseBid(int houseObjectId) {
		synchronized (houseBids) {
			return houseBids.get(houseObjectId);
		}
	}

	public List<HouseBidEntry> getHouseBidEntries(Race playerRace) {
		synchronized (houseBids) {
			List<HouseBidEntry> bids = new ArrayList<HouseBidEntry>();
			for (HouseBidEntry bid : houseBids.values()) {
				HousingLand land = DataManager.HOUSE_DATA.getLand(bid.getLandId());
				boolean isEly = DataManager.NPC_DATA.getNpcTemplate(land.getManagerNpcId()).getTribe() == TribeClass.GENERAL;
				if (isEly && playerRace == Race.ELYOS) {
					bids.add(bid);
				}
				else if (!isEly && playerRace == Race.ASMODIANS) {
					bids.add(bid);
				}
			}
			return bids;
		}
	}

	public HouseBidEntry getLastPlayerBid(int playerId) {
		return playerBids.get(playerId);
	}

	public HouseBidEntry getBidByEntryIndex(int index) {
		synchronized (bidsByIndex) {
			return bidsByIndex.get(index);
		}
	}

	private static int getMinBidLevel(Player player, int mapId, int landId) {
		HousingLand land = DataManager.HOUSE_DATA.getLand(landId);
		Sale saleOptions = land.getSaleOptions();
		// all types are the same for the land, take the first
		HouseType houseType = HouseType.fromValue(land.getBuildings().get(0).getSize());

		if (player.getRace() == Race.ELYOS) {
			if (mapId == WorldMapType.HEIRON.getId() || mapId == WorldMapType.INGGISON.getId()) {
				return saleOptions.getMinLevel();
			}
		}
		else if (mapId == WorldMapType.BELUSLAN.getId() || mapId == WorldMapType.GELKMAROS.getId()) {
			return saleOptions.getMinLevel();
		}

		switch (houseType) {
			case HOUSE:
				if (HousingConfig.HOUSE_MIN_BID_LEVEL > 0) {
					return HousingConfig.HOUSE_MIN_BID_LEVEL;
				}
				break;
			case MANSION:
				if (HousingConfig.MANSION_MIN_BID_LEVEL > 0) {
					return HousingConfig.MANSION_MIN_BID_LEVEL;
				}
				break;
			case ESTATE:
				if (HousingConfig.ESTATE_MIN_BID_LEVEL > 0) {
					return HousingConfig.ESTATE_MIN_BID_LEVEL;
				}
				break;
			case PALACE:
				if (HousingConfig.PALACE_MIN_BID_LEVEL > 0) {
					return HousingConfig.PALACE_MIN_BID_LEVEL;
				}
				break;
			default:
				break;
		}

		return saleOptions.getMinLevel();
	}

	public static boolean canBidHouse(Player player, int mapId, int landId) {
		return player.getLevel() >= getMinBidLevel(player, mapId, landId);
	}
}
