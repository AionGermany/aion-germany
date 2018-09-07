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
package playercommands;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.PlayerCommand;
import com.aionemu.gameserver.utils.i18n.CustomMessageId;
import com.aionemu.gameserver.utils.i18n.LanguageHandler;

/**
 * @author Maestross
 */
public class cmd_medal extends PlayerCommand {

	public cmd_medal() {
		super("medal");
	}

	@Override
	public void execute(Player player, String... params) {
		if (params.length < 1) {
			PacketSendUtility.sendMessage(player, "Syntax: .medal <silver | gold | platinum | mithril>" + LanguageHandler.translate(CustomMessageId.EX_SILVER_INFO) + LanguageHandler.translate(CustomMessageId.EX_GOLD_INFO) + LanguageHandler.translate(CustomMessageId.EX_PLATIN_INFO) + LanguageHandler.translate(CustomMessageId.EX_MITHRIL_INFO));
			return;
		}

		if (params[0].equalsIgnoreCase("silver")) {
			silver_medal(player);
		}
		if (params[0].equalsIgnoreCase("gold")) {
			gold_medal(player);
		}
		if (params[0].equalsIgnoreCase("platinum")) {
			platinum_medal(player);
		}
		if (params[0].equalsIgnoreCase("mithril")) {
			mithril_medal(player);
		}
	}

	private void silver_medal(Player player) {
		Storage bag = player.getInventory();

		long itemsInBag = bag.getItemCountByItemId(186000031);
		int ss = player.getClientConnection().getAccount().getMembership() < 2 ? 5 : 3;
		if (itemsInBag <= 0) {
			return;
		}
		if (itemsInBag >= 1000) {
			return;
		}
		if (itemsInBag < ss) {
			PacketSendUtility.sendYellowMessageOnCenter(player, LanguageHandler.translate(CustomMessageId.NOT_ENOUGH_SILVER));
			return;
		}

		Item item = bag.getFirstItemByItemId(186000031);
		bag.decreaseByObjectId(item.getObjectId(), player.getClientConnection().getAccount().getMembership() < 2 ? 5 : 3);
		ItemService.addItem(player, 186000030, 5);
		PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.EXCHANGE_SILVER));
	}

	private void gold_medal(Player player) {
		Storage bag = player.getInventory();

		long itemsInBag = bag.getItemCountByItemId(186000030);
		int ss = player.getClientConnection().getAccount().getMembership() < 2 ? 5 : 3;
		if (itemsInBag <= 0) {
			return;
		}
		if (itemsInBag >= 1000) {
			return;
		}
		if (itemsInBag < ss) {
			PacketSendUtility.sendYellowMessageOnCenter(player, LanguageHandler.translate(CustomMessageId.NOT_ENOUGH_GOLD));
			return;
		}

		Item item = bag.getFirstItemByItemId(186000030);
		bag.decreaseByObjectId(item.getObjectId(), player.getClientConnection().getAccount().getMembership() < 2 ? 5 : 3);
		ItemService.addItem(player, 186000096, 5);
		PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.EXCHANGE_GOLD));
	}

	private void platinum_medal(Player player) {
		Storage bag = player.getInventory();

		long itemsInBag = bag.getItemCountByItemId(186000096);
		int ss = player.getClientConnection().getAccount().getMembership() < 2 ? 5 : 3;
		if (itemsInBag <= 0) {
			return;
		}
		if (itemsInBag >= 1000) {
			return;
		}
		if (itemsInBag < ss) {
			PacketSendUtility.sendYellowMessageOnCenter(player, LanguageHandler.translate(CustomMessageId.NOT_ENOUGH_PLATIN));
			return;
		}

		Item item = bag.getFirstItemByItemId(186000096);
		bag.decreaseByObjectId(item.getObjectId(), player.getClientConnection().getAccount().getMembership() < 2 ? 5 : 3);
		ItemService.addItem(player, 186000147, 3);
		PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.EXCHANGE_PLATIN));
	}

	private void mithril_medal(Player player) {
		Storage bag = player.getInventory();

		long itemsInBag = bag.getItemCountByItemId(186000147);
		int ss = player.getClientConnection().getAccount().getMembership() < 2 ? 5 : 3;
		if (itemsInBag <= 0) {
			return;
		}
		if (itemsInBag >= 1000) {
			return;
		}
		if (itemsInBag < ss) {
			PacketSendUtility.sendYellowMessageOnCenter(player, LanguageHandler.translate(CustomMessageId.NOT_ENOUGH_MITHRIL));
			return;
		}

		Item item = bag.getFirstItemByItemId(186000147);
		bag.decreaseByObjectId(item.getObjectId(), player.getClientConnection().getAccount().getMembership() < 2 ? 5 : 3);
		ItemService.addItem(player, 186000223, 2);
		PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.EXCHANGE_MITHRIL));
	}
}
