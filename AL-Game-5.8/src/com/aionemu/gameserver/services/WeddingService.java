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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.configs.main.WeddingsConfig;
import com.aionemu.gameserver.dao.WeddingDAO;
import com.aionemu.gameserver.model.Wedding;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;

/**
 * @author synchro2
 */
public class WeddingService {

	private Map<Integer, Wedding> weddings = new HashMap<Integer, Wedding>();

	public static final WeddingService getInstance() {
		return SingletonHolder.instance;
	}

	public void registerOffer(Player partner1, Player partner2, Player priest) {
		if (!canRegister(partner1, partner2)) {
			PacketSendUtility.sendMessage(priest, "One of players already married.");
			return;
		}
		weddings.put(partner1.getObjectId(), new Wedding(partner1, partner2, priest));
		weddings.put(partner2.getObjectId(), new Wedding(partner2, partner1, priest));
	}

	private boolean canRegister(Player partner1, Player partner2) {
		return (getWedding(partner1) == null && getWedding(partner2) == null && !partner1.isMarried() && !partner2.isMarried());
	}

	public void acceptWedding(Player player) {
		Player partner = getPartner(player);
		Wedding playersWedding = getWedding(player);
		Wedding partnersWedding = getWedding(partner);

		playersWedding.setAccept();
		if (partnersWedding.isAccepted()) {
			if (!checkConditions(player, partner)) {
				cleanWedding(player, partner);
			}
			else {
				doWedding(player, partner);
				if (WeddingsConfig.WEDDINGS_GIFT_ENABLE) {
					giveGifts(player, partner);
				}
				if (WeddingsConfig.WEDDINGS_ANNOUNCE) {
					announceWedding(player, partner);
				}
			}
		}
	}

	private void doWedding(Player player, Player partner) {
		DAOManager.getDAO(WeddingDAO.class).storeWedding(player, partner);
		player.setPartnerId(partner.getObjectId());
		partner.setPartnerId(player.getObjectId());
		PacketSendUtility.sendMessage(player, "You had married on " + partner.getName() + ".");
		PacketSendUtility.sendMessage(partner, "You had married on " + player.getName() + ".");
		PacketSendUtility.sendMessage(getPriest(player), "You had married" + player.getName() + " and " + partner.getName() + ".");
		cleanWedding(player, partner);
	}

	public void unDoWedding(Player player, Player partner) {
		DAOManager.getDAO(WeddingDAO.class).deleteWedding(player, partner);
		player.setPartnerId(0);
		partner.setPartnerId(0);
		PacketSendUtility.sendMessage(player, "Wedding canceled.");
		PacketSendUtility.sendMessage(partner, "Wedding canceled.");
	}

	private boolean checkConditions(Player player, Player partner) {
		if (player.isMarried() || partner.isMarried()) {
			PacketSendUtility.sendMessage(player, "One of players already married.");
			PacketSendUtility.sendMessage(partner, "One of players already married.");
			PacketSendUtility.sendMessage(getPriest(player), "One of players already married.");
		}
		if (WeddingsConfig.WEDDINGS_SUIT_ENABLE) {
			String[] suits = WeddingsConfig.WEDDINGS_SUITS.split(",");
			boolean success1 = false;
			boolean success2 = false;
			try {
				for (String suit : suits) {
					int suitId = Integer.parseInt(suit);
					if (!player.getEquipment().getEquippedItemsByItemId(suitId).isEmpty()) {
						success1 = true;
					}
					if (!partner.getEquipment().getEquippedItemsByItemId(suitId).isEmpty()) {
						success2 = true;
					}
				}
			}
			catch (NumberFormatException e) {
				e.printStackTrace();
			}
			finally {
				if (!success1 || !success2) {
					PacketSendUtility.sendMessage(player, "One of players not have required suit.");
					PacketSendUtility.sendMessage(partner, "One of players not equip required suit.");
					PacketSendUtility.sendMessage(getPriest(player), "One of players not equip required suit.");
					return false;
				}
			}
		}

		if (player.getKnownList().getObject(partner.getObjectId()) == null) {
			PacketSendUtility.sendMessage(player, "You should see spouse.");
			PacketSendUtility.sendMessage(partner, "You should see spouse.");
			PacketSendUtility.sendMessage(getPriest(player), "Players not see each other.");
			return false;
		}

		if (!player.havePermission(WeddingsConfig.WEDDINGS_MEMBERSHIP) || !partner.havePermission(WeddingsConfig.WEDDINGS_MEMBERSHIP)) {
			PacketSendUtility.sendMessage(player, "One of players not have required membership.");
			PacketSendUtility.sendMessage(partner, "One of players not have required membership.");
			PacketSendUtility.sendMessage(getPriest(player), "One of players not have required membership.");
			return false;
		}

		if (!WeddingsConfig.WEDDINGS_SAME_SEX && player.getCommonData().getGender().equals(partner.getCommonData().getGender())) {
			PacketSendUtility.sendMessage(player, "Same-sex weddings prohibited.");
			PacketSendUtility.sendMessage(partner, "Same-sex weddings prohibited.");
			PacketSendUtility.sendMessage(getPriest(player), "Same-sex weddings prohibited.");
			return false;
		}

		if (!WeddingsConfig.WEDDINGS_DIFF_RACES && !player.getCommonData().getRace().equals(partner.getCommonData().getRace())) {
			PacketSendUtility.sendMessage(player, "Weddings between different races prohibited.");
			PacketSendUtility.sendMessage(partner, "Weddings between different races prohibited.");
			PacketSendUtility.sendMessage(getPriest(player), "Weddings between different races prohibited.");
			return false;
		}

		if (WeddingsConfig.WEDDINGS_KINAH != 0) {
			if (!player.getInventory().tryDecreaseKinah(WeddingsConfig.WEDDINGS_KINAH) || !partner.getInventory().tryDecreaseKinah(WeddingsConfig.WEDDINGS_KINAH)) {
				PacketSendUtility.sendMessage(player, "One of players not have required kinah count.");
				PacketSendUtility.sendMessage(partner, "One of players not have required kinah count.");
				PacketSendUtility.sendMessage(getPriest(player), "One of players not have required kinah count.");
				return false;
			}
		}
		return true;
	}

	private void giveGifts(Player player, Player partner) {
		ItemService.addItem(player, WeddingsConfig.WEDDINGS_GIFT, 1);
		ItemService.addItem(partner, WeddingsConfig.WEDDINGS_GIFT, 1);
	}

	private void announceWedding(Player player, Player partner) {
		String message = player.getName() + " and " + partner.getName() + " now married.";
		Iterator<Player> iter = World.getInstance().getPlayersIterator();
		while (iter.hasNext()) {
			PacketSendUtility.sendBrightYellowMessage(iter.next(), message);
		}
	}

	public void cancelWedding(Player player) {
		PacketSendUtility.sendMessage(player, "Wedding canceled.");
		PacketSendUtility.sendMessage(getPartner(player), "Player " + player.getName() + " declined from a wedding.");
		PacketSendUtility.sendMessage(getPriest(player), "Player " + player.getName() + " declined from a wedding.");
		cleanWedding(player, getPartner(player));
	}

	private void cleanWedding(Player player, Player partner) {
		weddings.remove(player.getObjectId());
		weddings.remove(partner.getObjectId());
	}

	public Wedding getWedding(Player player) {
		return weddings.get(player.getObjectId());
	}

	private Player getPartner(Player player) {
		Wedding wedding = weddings.get(player.getObjectId());
		return wedding.getPartner();
	}

	private Player getPriest(Player player) {
		Wedding wedding = weddings.get(player.getObjectId());
		return wedding.getPriest();
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder {

		protected static final WeddingService instance = new WeddingService();
	}
}
