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

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.PlayerCommand;

/**
 * @author abaton
 */
public class cmd_shop extends PlayerCommand {

	static class Shop {

		class ItemSetNotFoundException extends Exception {

			/**
			 * 
			 */
			private static final long serialVersionUID = -5886677962380429895L;
			final List<String> suggestions;

			public ItemSetNotFoundException(List<String> suggestions) {
				this.suggestions = suggestions;
			}

			@Override
			public String toString() {
				return "ItemSetNotFoundException{" + "suggestions=" + suggestions + '}';
			}
		}

		private int levenshteinDistance(CharSequence lhs, CharSequence rhs, int costReplace, int costDelete, int costInsert) {
			int len0 = lhs.length() + 1;
			int len1 = rhs.length() + 1;

			// the array of distances
			int[] cost = new int[len0];
			int[] newcost = new int[len0];

			// initial cost of skipping prefix in String s0
			for (int i = 0; i < len0; i++)
				cost[i] = i;

			// dynamically computing the array of distances

			// transformation cost for each letter in s1
			for (int j = 1; j < len1; j++) {
				// initial cost of skipping prefix in String s1
				newcost[0] = j;

				// transformation cost for each letter in s0
				for (int i = 1; i < len0; i++) {
					// matching current letters in both strings
					int match = (lhs.charAt(i - 1) == rhs.charAt(j - 1)) ? 0 : costReplace;

					// computing cost for each transformation
					int cost_replace = cost[i - 1] + match;
					int cost_insert = cost[i] + costInsert;
					int cost_delete = newcost[i - 1] + costDelete;

					// keep minimum cost
					newcost[i] = Math.min(Math.min(cost_insert, cost_delete), cost_replace);
				}

				// swap cost/newcost arrays
				int[] swap = cost;
				cost = newcost;
				newcost = swap;
			}

			// the distance is the cost for transforming all letters in both strings
			return cost[len0 - 1];
		}

		static class Item {

			final int id;
			final int count;

			public Item(int id, int count) {
				this.id = id;
				this.count = count;
			}

			@Override
			public String toString() {
				return "Item{" + "id=" + id + ", count=" + count + '}';
			}
		}

		static class ItemSet {

			final List<Item> items;
			final List<Item> trades;
			final String name;
			final String desc;
			final int cost;

			public ItemSet(List<Item> items, List<Item> trades, String name, String desc, int cost) {
				this.items = items;
				this.trades = trades;
				this.name = name;
				this.desc = desc;
				this.cost = cost;
			}

			@Override
			public String toString() {
				return "ItemSet{" + "items=" + items + ", trades=" + trades + ", name='" + name + '\'' + ", desc='" + desc + '\'' + ", cost=" + cost + '}';
			}
		}

		private final String filename;
		private final HashMap<String, ItemSet> itemSets = new HashMap<String, ItemSet>();
		private final List<String> names = new ArrayList<String>();

		public Shop(String filename) throws Exception {
			this.filename = filename;

			load();
		}

		private static List<Item> getItems(NodeList list) {
			ArrayList<Item> result = new ArrayList<Item>(list.getLength());

			for (int i = 0; i < list.getLength(); i++) {
				Node item = list.item(i);

				result.add(new Item(Integer.parseInt(item.getTextContent()), Integer.parseInt(item.getAttributes().getNamedItem("count").getTextContent())));

			}
			return result;
		}

		private void load() throws Exception {
			Element root = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(filename)).getDocumentElement();

			itemSets.clear();

			NodeList sets = root.getElementsByTagName("set");
			for (int i = 0; i < sets.getLength(); i++) {
				Element set = (Element) sets.item(i);

				String name = set.getAttributes().getNamedItem("name").getTextContent();
				String desc = set.getAttributes().getNamedItem("description").getTextContent();
				Integer cost = Integer.parseInt(set.getAttributes().getNamedItem("cost").getTextContent());
				List<Item> items = getItems(set.getElementsByTagName("item"));
				List<Item> trades = getItems(set.getElementsByTagName("trade"));

				names.add(name);
				itemSets.put(name, new ItemSet(items, trades, name, desc, cost));
			}
		}

		public void reload() throws Exception {
			load();
		}

		public ItemSet findItemSet(String name) throws ItemSetNotFoundException {
			ItemSet set = itemSets.get(name);

			if (set == null) {

				TreeMap<Integer, String> lvs = new TreeMap<Integer, String>();

				for (String id : names) {
					lvs.put(levenshteinDistance(name, id, 2, 1, 1), id);
				}

				ArrayList<String> suggestions = new ArrayList<String>();

				Map.Entry<Integer, String> it;
				while ((it = lvs.pollFirstEntry()) != null && suggestions.size() < 3) {
					if (it.getKey() < 8)
						suggestions.add(it.getValue());
				}

				Collections.reverse(suggestions);

				throw new ItemSetNotFoundException(suggestions);
			}

			return set;
		}

		public HashMap<String, ItemSet> getSets() {
			return itemSets;
		}
	}

	public cmd_shop() {
		super("shop");
	}

	private void handleList(Player player, Shop shop) {
		PacketSendUtility.sendMessage(player, "Available sets:");
		for (Shop.ItemSet set : shop.getSets().values()) {
			PacketSendUtility.sendMessage(player, set.name + ": " + set.desc);
		}
	}

	private void handleShow(Player player, Shop shop, String name) {
		try {
			Shop.ItemSet itemSet = shop.findItemSet(name);
			PacketSendUtility.sendMessage(player, itemSet.cost + "Kinah");

			for (Shop.Item item : itemSet.items) {
				PacketSendUtility.sendMessage(player, "Item: " + item.count + "x [item:" + item.id + ";ver6;;;;]");
			}

			for (Shop.Item item : itemSet.trades) {
				PacketSendUtility.sendMessage(player, "Trade in: " + item.count + "x [item:" + item.id + ";ver6;;;;]");
			}
		}
		catch (Shop.ItemSetNotFoundException e) {
			PacketSendUtility.sendMessage(player, "I don't know any set named: " + name + ". Did you mean one of: " + e.suggestions);
		}
	}

	private void handleBuy(Player player, Shop shop, String name) {
		try {
			Shop.ItemSet itemSet = shop.findItemSet(name);

			Storage i = player.getInventory();

			boolean fail = false;

			if (i.getKinah() < itemSet.cost) {
				fail = true;
				PacketSendUtility.sendMessage(player, "You don't have enough Kinah, you need: " + itemSet.cost);
			}

			for (Shop.Item item : itemSet.trades) {
				if (i.getItemCountByItemId(item.id) < item.count) {
					fail = true;
					PacketSendUtility.sendMessage(player, "You don't have enough [item:" + item.id + ";ver6;;;;] you need: " + item.count);
				}
			}

			if (fail)
				return;

			player.getInventory().decreaseKinah(itemSet.cost);

			for (Shop.Item item : itemSet.trades) {
				i.decreaseByItemId(item.id, item.count);
			}

			for (Shop.Item item : itemSet.items) {
				ItemService.addItem(player, item.id, item.count);
			}
		}
		catch (Shop.ItemSetNotFoundException e) {
			PacketSendUtility.sendMessage(player, "I don't know any set named: " + name + ". Did you mean one of: " + e.suggestions);
		}
	}

	@Override
	public void execute(final Player player, String... params) {
		if (params.length < 1) {
			showHelp(player);
			return;
		}

		String cmd = params[0];

		Shop shop;
		try {
			shop = new Shop("data/static_data/webshop/shop.xml");
		}
		catch (Exception e) {
			PacketSendUtility.sendMessage(player, "Error: Could not load shop data");
			return;
		}

		if ("list".equals(cmd)) {
			handleList(player, shop);
			return;
		}
		if ("buy".equals(cmd)) {
			handleBuy(player, shop, params[1]);
			return;
		}
		if ("show".equals(cmd)) {
			handleShow(player, shop, params[1]);
			return;
		}

		PacketSendUtility.sendMessage(player, "syntax: .shop list|buy NAME|show NAME");
	}

	@Override
	public void onFail(Player player, String message) {
		PacketSendUtility.sendMessage(player, "syntax: .shop list|buy NAME|show NAME");
	}

	protected void showHelp(Player player) {
		PacketSendUtility.sendMessage(player, "syntax: .shop list|buy NAME|show NAME");
	}
}
