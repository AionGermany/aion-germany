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

import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.RequestResponseHandler;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.PlayerCommand;

/**
 * @author Wakizashi, Imaginary
 * @revork Alex
 * @rework Eloann
 */
public class cmd_reskin2 extends PlayerCommand {

	public cmd_reskin2() {
		super("reskinvip");
	}

	@Override
	public void execute(Player admin, String... params) {
		if (params.length != 2) {
			onFail(admin, null);
			return;
		}

		if (admin.getClientConnection().getAccount().getMembership() < 2 && !admin.isGM()) {
			PacketSendUtility.sendYellowMessageOnCenter(admin, "This command is available only to VIP!");
			return;
		}

		Player target = admin;
		VisibleObject creature = admin.getTarget();
		if (admin.getTarget() instanceof Player && admin.isGM()) {
			target = (Player) creature;
		}
		int oldItemId = 0;
		int newItemId = 0;
		try {
			String item = params[0];
			if (item.equals("[item:")) {
				item = params[1];
				Pattern id = Pattern.compile("(\\d{9})");
				Matcher result = id.matcher(item);
				if (result.find()) {
					oldItemId = Integer.parseInt(result.group(1));
				}
			}
			else {
				Pattern id = Pattern.compile("\\[item:(\\d{9})");
				Matcher result = id.matcher(item);

				if (result.find()) {
					oldItemId = Integer.parseInt(result.group(1));
				}
				else {
					oldItemId = Integer.parseInt(params[0]);
				}
			}
			try {
				String items = params[1];
				if (items.equals("[item:")) {
					items = params[2];
					Pattern id = Pattern.compile("(\\d{9})");
					Matcher result = id.matcher(items);
					if (result.find()) {
						newItemId = Integer.parseInt(result.group(1));
					}
				}
				else {
					Pattern id = Pattern.compile("\\[item:(\\d{9})");
					Matcher result = id.matcher(items);

					if (result.find()) {
						newItemId = Integer.parseInt(result.group(1));
					}
					else {
						newItemId = Integer.parseInt(params[1]);
					}
				}
			}
			catch (NumberFormatException ex) {
				PacketSendUtility.sendMessage(admin, "1 " + (admin.isGM() ? ex : ""));
				return;
			}
			catch (Exception ex2) {
				PacketSendUtility.sendMessage(admin, "2 " + (admin.isGM() ? ex2 : ""));
				return;
			}
		}
		catch (NumberFormatException ex) {
			PacketSendUtility.sendMessage(admin, "3 " + (admin.isGM() ? ex : ""));
			return;
		}
		catch (Exception ex2) {
			PacketSendUtility.sendMessage(admin, "4 " + (admin.isGM() ? ex2 : ""));
			return;
		}
		if (DataManager.ITEM_DATA.getItemTemplate(newItemId) == null) {
			PacketSendUtility.sendMessage(admin, "Item id is incorrect: " + newItemId);
			return;
		}
		if (!admin.isGM()) {
			target = admin;
		}

		int tollPrice = 50;
		List<Item> items = target.getInventory().getItemsByItemId(oldItemId);
		List<Item> itemnew = target.getInventory().getItemsByItemId(newItemId);
		if (oldItemId == newItemId) {
			PacketSendUtility.sendMessage(admin, "You cannot reskin the same item :D");
			return;
		}

		// Change the appearance of any item. Gun on the mace, sword, shield and so on
		if (DataManager.ITEM_DATA.getItemTemplate(oldItemId).getItemSlot() != DataManager.ITEM_DATA.getItemTemplate(newItemId).getItemSlot()) {
			PacketSendUtility.sendMessage(admin, "You can't :D");
			return;
		}

		if (itemnew.isEmpty() && !admin.isGM()) {
			reskin(target, tollPrice, newItemId, items);
			return;
		}
		if (items.isEmpty()) {
			if (admin.isGM()) {
				PacketSendUtility.sendMessage(admin, "Old itemID character taken to the Target is not found in the inventory.");
				return;
			}
			else {
				PacketSendUtility.sendMessage(admin, "Old itemID Not Found in inventory.");
				return;
			}
		}
		Iterator<Item> iter = items.iterator();
		Item item = iter.next();
		if (!admin.isGM() && !itemnew.isEmpty()) {
			item.setItemSkinTemplate(DataManager.ITEM_DATA.getItemTemplate(newItemId));
			PacketSendUtility.sendMessage(admin, "Skin successfully modified!");
			admin.getInventory().decreaseByItemId(newItemId, 1);
		}
		else {
			item.setItemSkinTemplate(DataManager.ITEM_DATA.getItemTemplate(newItemId));
			PacketSendUtility.sendMessage(admin, "Skin successfully modified!");
		}
	}

	public void reskin(final Player admin, final int toll, final int itemId, final List<Item> items) {
		final long tolls = admin.getClientConnection().getAccount().getToll();
		RequestResponseHandler responseHandler = new RequestResponseHandler(admin) {

			@Override
			public void acceptRequest(Creature p2, Player p) {
				if (tolls < toll) {
					PacketSendUtility.sendMessage(admin, "You don't have enought Vote Points (" + tolls + "). You need : " + toll + " Vote Points.");
					return;
				}
				p.getClientConnection().getAccount().setToll(tolls - toll);
				Iterator<Item> iter = items.iterator();
				Item item = iter.next();
				item.setItemSkinTemplate(DataManager.ITEM_DATA.getItemTemplate(itemId));
				PacketSendUtility.sendMessage(admin, "Skin successfully changed!");
				PacketSendUtility.sendMessage(p, "For changing the skin, you have use " + toll + " Vote Points!");
			}

			@Override
			public void denyRequest(Creature p2, Player p) {
			}
		};
		boolean requested = admin.getResponseRequester().putRequest(902247, responseHandler);
		if (requested) {
			PacketSendUtility.sendPacket(admin, new SM_QUESTION_WINDOW(902247, 0, 0, "In your inventory, there is no New ItemId. To change the look, for which you have not, you need to" + toll + " Vote Points. On your account, you have :" + tolls + ". Want to reskin the item ?"));
		}
	}

	@Override
	public void onFail(Player admin, String message) {
		PacketSendUtility.sendMessage(admin, "syntax //reskinvip <Link@ | Old Item ID> <Link@ | New Item ID>");
	}
}
