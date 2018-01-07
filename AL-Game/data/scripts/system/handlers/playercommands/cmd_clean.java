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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.PlayerCommand;

/**
 * @author Source
 */
public class cmd_clean extends PlayerCommand {

	public cmd_clean() {
		super("clean");
	}

	@Override
	public void execute(Player player, String... params) {
		String msg = "syntax .clean <item ID> or <item @link>";

		if (params.length == 0) {
			onFail(player, msg);
			return;
		}

		int itemId = 0;

		try {
			String item = params[0];
			// Some item links have space before Id
			if (item.equals("[item:")) {
				item = params[1];
				Pattern id = Pattern.compile("(\\d{9})");
				Matcher result = id.matcher(item);
				if (result.find()) {
					itemId = Integer.parseInt(result.group(1));
				}
			}
			else {
				Pattern id = Pattern.compile("\\[item:(\\d{9})");
				Matcher result = id.matcher(item);

				if (result.find()) {
					itemId = Integer.parseInt(result.group(1));
				}
				else {
					itemId = Integer.parseInt(params[0]);
				}
			}
		}
		catch (NumberFormatException e) {
			try {
				String item = params[1];
				// Some item links have space before Id
				if (item.equals("[item:")) {
					item = params[2];
					Pattern id = Pattern.compile("(\\d{9})");
					Matcher result = id.matcher(item);
					if (result.find()) {
						itemId = Integer.parseInt(result.group(1));
					}
				}
				else {
					Pattern id = Pattern.compile("\\[item:(\\d{9})");
					Matcher result = id.matcher(item);

					if (result.find()) {
						itemId = Integer.parseInt(result.group(1));
					}
					else {
						itemId = Integer.parseInt(params[1]);
					}
				}
			}
			catch (NumberFormatException ex) {
				PacketSendUtility.sendMessage(player, "You must give Id or @link to item.");
				return;
			}
			catch (Exception ex2) {
				onFail(player, msg);
				return;
			}
		}

		Storage bag = player.getInventory();
		Item item = bag.getFirstItemByItemId(itemId);
		if (item != null || itemId == 0) {
			bag.decreaseByObjectId(item.getObjectId(), 1);
			PacketSendUtility.sendMessage(player, "Item removed succesfully");
		}
		else {
			PacketSendUtility.sendMessage(player, "You don't have that item");
		}
	}

	@Override
	public void onFail(Player player, String message) {
		PacketSendUtility.sendMessage(player, message);
	}
}
