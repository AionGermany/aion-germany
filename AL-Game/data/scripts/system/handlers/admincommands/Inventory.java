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

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;

/**
 * @author FrozenKiller
 */

public class Inventory extends AdminCommand {
	
	private List<Item> deleteItems = new ArrayList<Item>();

	public Inventory() {
		super("inventory");
	}
	
	@Override
	public void execute(Player admin, String... params) {
		if (params.length < 1) {
			onFail(admin, null);
			return;
		}
		Player receiver = (Player) admin.getTarget();
		
		if (receiver == null) {
			PacketSendUtility.sendMessage(admin, "You should Target an Player or your self first !");
			return;
		}
		
		if (params[0].equalsIgnoreCase("clean")) {
			List<Item> list = receiver.getInventory().getItems();
			for (Item item : list) {
				if (!item.isEquipped()) {
					deleteItems.add(item);
				}
			}
			for (Item delItem : deleteItems) {
				receiver.getInventory().delete(delItem);
			}
			if (admin != receiver) {
				PacketSendUtility.sendMessage(receiver, "An Admin deleted all your Unequiped Items");
			} else {
				PacketSendUtility.sendMessage(admin, "You deleted all your Unequiped Items");
			}
			deleteItems.clear();
			list.clear();
		}
	}

	@Override
	public void onFail(Player player, String message) {
		PacketSendUtility.sendMessage(player, "This command deletes all Unequiped Items from Inventory");
		PacketSendUtility.sendMessage(player, "<usage //inventory | clean");
	}
}