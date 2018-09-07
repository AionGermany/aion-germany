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

import java.lang.reflect.Field;

import com.aionemu.gameserver.configs.administration.CommandsConfig;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.ManaStone;
import com.aionemu.gameserver.model.stats.listeners.ItemEquipmentListener;
import com.aionemu.gameserver.model.templates.item.GodstoneInfo;
import com.aionemu.gameserver.model.templates.item.ItemCategory;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.model.templates.item.ItemType;
//import com.aionemu.gameserver.model.items.ItemSlot;
import com.aionemu.gameserver.network.aion.serverpackets.SM_STATS_INFO;
import com.aionemu.gameserver.services.EnchantService;
import com.aionemu.gameserver.services.item.ItemPacketService;
import com.aionemu.gameserver.services.item.ItemSocketService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.Util;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;
import com.aionemu.gameserver.world.World;

/**
 * @author Tago modified by Wakizashi
 */
public class Equip extends AdminCommand {

	public Equip() {
		super("equip");
	}

	@Override
	public void execute(Player admin, String... params) {
		if (params.length != 0) {
			int i = 0;
			if ("help".startsWith(params[i])) {
				if (params[i + 1] == null) {
					showHelp(admin);
				}
				else if ("socket".startsWith(params[i + 1])) {
					showHelpSocket(admin);
				}
				else if ("enchant".startsWith(params[i + 1])) {
					showHelpEnchant(admin);
				}
				else if ("plume".startsWith(params[i + 1])) {
					showHelpplume(admin);
				}
				else if ("godstone".startsWith(params[i + 1])) {
					showHelpGodstone(admin);
				}
				return;
			}
			Player player = null;
			player = World.getInstance().findPlayer(Util.convertName(params[i]));
			if (player == null) {
				VisibleObject target = admin.getTarget();
				if (target instanceof Player) {
					player = (Player) target;
				}
				else {
					player = admin;
				}
			}
			else {
				i++;
			}
			if ("socket".startsWith(params[i])) {
				if (admin.getAccessLevel() < CommandsConfig.EQUIP) {
					PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
					return;
				}
				int manastone = 167000551;
				int quant = 0;
				try {
					manastone = params[i + 1] == null ? manastone : Integer.parseInt(params[i + 1]);
					quant = params[i + 2] == null ? quant : Integer.parseInt(params[i + 2]);
				}
				catch (Exception ex2) {
					showHelpSocket(admin);
					return;
				}
				socket(admin, player, manastone, quant);
				return;
			}
			if ("enchant".startsWith(params[i])) {
				if (admin.getAccessLevel() < CommandsConfig.EQUIP) {
					PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
					return;
				}
				int enchant = 0;
				try {
					enchant = params[i + 1] == null ? enchant : Integer.parseInt(params[i + 1]);
				}
				catch (Exception ex) {
					showHelpEnchant(admin);
					return;
				}
				enchant(admin, player, enchant);
				return;
			}
			if ("plume".startsWith(params[i])) {
				if (admin.getAccessLevel() < CommandsConfig.EQUIP) {
					PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
					return;
				}
				int plume = 0;
				try {
					plume = params[i + 1] == null ? plume : Integer.parseInt(params[i + 1]);
				}
				catch (Exception ex) {
					showHelpplume(admin);
					return;
				}
				plume(admin, player, plume);
				return;
			}
			if ("godstone".startsWith(params[i])) {
				if (admin.getAccessLevel() < CommandsConfig.EQUIP) {
					PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
					return;
				}
				int godstone = 100;
				try {
					godstone = params[i + 1] == null ? godstone : Integer.parseInt(params[i + 1]);
				}
				catch (Exception ex) {
					showHelpGodstone(admin);
					return;
				}
				godstone(admin, player, godstone);
				return;
			}
			if ("amply".startsWith(params[i])) {
				if (admin.getAccessLevel() < CommandsConfig.EQUIP) {
					PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
					return;
				}
				int itemId = 0;
				try {
					itemId = params[i + 1] == null ? itemId : Integer.parseInt(params[i + 1]);
				}
				catch (Exception ex) {
					showHelpGodstone(admin);
					return;
				}
				amply(admin, player, itemId);
				return;
			}
		}
		showHelp(admin);
	}

	private void socket(Player admin, Player player, int manastone, int quant) {
		if (manastone != 0 && (manastone < 167000000 || manastone > 168000000)) {
			PacketSendUtility.sendMessage(admin, "You are suposed to give the item id for a" + " Manastone or 0 to remove all manastones.");
			return;
		}
		for (Item targetItem : player.getEquipment().getEquippedItemsWithoutStigma()) {
			if (isUpgradeble(targetItem)) {
				if (manastone == 0) {
					ItemEquipmentListener.removeStoneStats(targetItem.getItemStones(), player.getGameStats());
					ItemSocketService.removeAllManastone(player, targetItem);
				}
				else {
					int counter = quant <= 0 ? getMaxSlots(targetItem) : quant;
					while (targetItem.getItemStones().size() < getMaxSlots(targetItem) && counter >= 0) {
						ManaStone manaStone = ItemSocketService.addManaStone(targetItem, manastone);
						ItemEquipmentListener.addStoneStats(targetItem, manaStone, player.getGameStats());
						counter--;

					}
				}
				PacketSendUtility.sendPacket(player, new SM_STATS_INFO(player));
				ItemPacketService.updateItemAfterInfoChange(player, targetItem);
				targetItem.setPersistentState(PersistentState.UPDATE_REQUIRED);
			}

		}
		if (manastone == 0) {
			if (player == admin) {
				PacketSendUtility.sendMessage(player, "All Manastones removed from all equipped Items");
			}
			else {
				PacketSendUtility.sendMessage(admin, "All Manastones removed from all equipped Items by the Player " + player.getName());
				PacketSendUtility.sendMessage(player, "Admin " + admin.getName() + " removed all manastones from all your equipped Items");
			}
		}
		else {
			if (player == admin) {
				PacketSendUtility.sendMessage(player, quant + "x [item: " + manastone + "] were added to free slots on all equipped items");
			}
			else {
				PacketSendUtility.sendMessage(admin, quant + "x [item: " + manastone + "] were added to free slots on all equipped items by the Player " + player.getName());
				PacketSendUtility.sendMessage(player, "Admin " + admin.getName() + " added " + quant + "x [item: " + manastone + "] to free slots on all your equipped items");
			}
		}
	}

	private void godstone(Player admin, Player player, int godstone) {
		Item targetItem = player.getEquipment().getMainHandWeapon();
		if (godstone > 100000000) {
			ItemTemplate itemTemplate = DataManager.ITEM_DATA.getItemTemplate(godstone);
			GodstoneInfo godstoneInfo = itemTemplate.getGodstoneInfo();
			if (godstoneInfo == null) {
				PacketSendUtility.sendMessage(admin, "Wrong GodStone ItemID");
				return;
			}
			targetItem.addGodStone(godstone);
			PacketSendUtility.sendPacket(player, new SM_STATS_INFO(player));
			ItemPacketService.updateItemAfterInfoChange(player, targetItem);
			targetItem.setPersistentState(PersistentState.UPDATE_REQUIRED);
			if (player == admin) {
				PacketSendUtility.sendMessage(player, "[Item: " + godstone + "] socketed to your equipped MainHandWeapon [Item: " + targetItem.getItemId() + "]");
			}
			else {
				PacketSendUtility.sendMessage(admin, "[Item: " + godstone + "] socketed to the Player " + player.getName() + "equipped MainHandWeapon [Item: " + targetItem.getItemId() + "]");
				PacketSendUtility.sendMessage(player, "Admin " + admin.getName() + " socketed [Item: " + godstone + "]  to you equipped MainHandWeapon [Item: " + targetItem.getItemId() + "]");
			}
			targetItem.getGodStone().onEquip(player);
		}
		else if (targetItem.getGodStone() != null) {
			targetItem.getGodStone().onUnEquip(player);
			try {
				if (godstone <= 100) {
					godstone *= 10;
				}
				if (godstone > 1000) {
					godstone = 1000;
				}
				Class<?> gs = targetItem.getGodStone().getClass();
				Field probability = gs.getDeclaredField("probability");
				Field probabilityLeft = gs.getDeclaredField("probability");
				probability.setAccessible(true);
				probabilityLeft.setAccessible(true);
				probability.setInt(targetItem.getGodStone(), godstone);
				probabilityLeft.setInt(targetItem.getGodStone(), godstone);
			}
			catch (Exception ex2) {
				PacketSendUtility.sendMessage(admin, "Occurs an error.");
				return;
			}
			targetItem.getGodStone().onEquip(player);
			if (player == admin) {
				PacketSendUtility.sendMessage(player, "Your godstone on your MainHandWeapon will now activate around " + (godstone / 10) + " percent of the time.");
			}
			else {
				PacketSendUtility.sendMessage(admin, "Player " + player.getName() + " godstone on MainHandWeapon will now activate around " + godstone + " percent of the time.");
				PacketSendUtility.sendMessage(player, "Admin " + admin.getName() + " blessed your godstone on your MainHandWeapon to now activate around " + godstone + " percent of the time.");
			}
		}
	}

	private void enchant(Player admin, Player player, int enchant) {
		for (Item targetItem : player.getEquipment().getEquippedItemsWithoutStigma()) {
			if (isUpgradeble(targetItem)) {
				if (targetItem.getEnchantLevel() == enchant) {
					continue;
				}
				if (enchant > 50) {
					enchant = 50;
				}
				if (enchant < 0) {
					enchant = 0;
				}

				if (targetItem.getItemTemplate().isBracelet()) {
					if (enchant > 10) {
						targetItem.setOptionalSocket(3);
						targetItem.setAuthorize(10);
					}
					else {
						targetItem.setAuthorize(enchant);
					}
				}
				else {
					targetItem.setEnchantLevel(enchant);
				}
				if (targetItem.isEquipped()) {
					player.getGameStats().updateStatsVisually();
				}
				ItemPacketService.updateItemAfterInfoChange(player, targetItem);

				if (targetItem.isEquipped()) {
					player.getEquipment().setPersistentState(PersistentState.UPDATE_REQUIRED);
				}
			}
		}
		if (player == admin) {
			PacketSendUtility.sendMessage(player, "All equipped items were enchanted to level " + enchant);
		}
		else {
			PacketSendUtility.sendMessage(admin, "All equipped items by the Player " + player.getName() + " were enchanted to " + enchant);
			PacketSendUtility.sendMessage(player, "Admin " + admin.getName() + " enchanted all your equipped items to level " + enchant);
		}

	}

	private void plume(Player admin, Player player, int plume) {
		for (Item targetItem : player.getEquipment().getEquippedItems()) {
			if (targetItem.getItemTemplate().isPlume()) {
				if (plume > 20) {
					plume = 20;
				}
				if (plume < 0) {
					plume = 0;
				}
				targetItem.setAuthorize(plume);
				if (targetItem.isEquipped()) {
					player.getGameStats().updateStatsVisually();
				}
				ItemPacketService.updateItemAfterInfoChange(player, targetItem);
			}
		}
		if (player == admin) {
			PacketSendUtility.sendMessage(player, "equipped plume were enchanted to level " + plume);
		}
		else {
			PacketSendUtility.sendMessage(admin, "equipped plume by the Player " + player.getName() + " were enchanted to " + plume);
			PacketSendUtility.sendMessage(player, "Admin " + admin.getName() + " enchanted your equipped plume to level " + plume);
		}
	}

	private void amply(Player admin, Player player, int itemId) {
		boolean success = false;
		for (Item targetItem : player.getEquipment().getEquippedItemsWithoutStigma()) {
			if (targetItem.getItemId() != itemId)
				continue;

			EnchantService.amplifyItemCommand(player, targetItem);
			success = true;
		}
		if (success) {
			if (player == admin) {
				PacketSendUtility.sendMessage(player, "Item: " + itemId + " got amplified successfully!");
			}
			else {
				PacketSendUtility.sendMessage(admin, "Item: " + itemId + " from player " + player.getName() + " got amplified successfully!");
				PacketSendUtility.sendMessage(player, "Admin " + admin.getName() + " amplified your item: " + itemId + " successfully!");
			}
		}
		else {
			if (player == admin) {
				PacketSendUtility.sendMessage(player, "Item: " + itemId + " not successfully amplified!");
			}
			else {
				PacketSendUtility.sendMessage(player, "Item: " + itemId + " not successfully amplified!");

			}
		}
	}

	/**
	 * Verify if the item is enchantble and/or socketble
	 *
	 * @param item
	 */
	public static boolean isUpgradeble(Item item) {
		if (item.getItemTemplate().isNoEnchant()) {
			return false;
		}
		if (item.getItemTemplate().isWeapon()) {
			return true;
		}
		if (item.getItemTemplate().getCategory() == ItemCategory.STIGMA) {
			return false;
		}
		if (item.getItemTemplate().isArmor()) {
			int at = item.getItemTemplate().getItemSlot();
			if (at == 1 || /* Main Hand */at == 2 || /* Sub Hand */at == 8 || /* Jacket */at == 16 || /* Gloves */at == 32 || /* Boots */at == 2048 || /* Shoulder */at == 4096 || /* Pants */at == 131072
				|| /*
					 * Main Off Hand
					 */at == 262144) /*
									 * Sub Off Hand
									 */ {
				return true;
			}
		}
		if (item.getItemTemplate().isBracelet()) {
			return true;
		}
		return false;

	}

	/**
	 * Returns the max number of manastones that can be socketed
	 *
	 * @param item
	 */
	public static int getMaxSlots(Item item) {
		int slots = 0;
		switch (item.getItemTemplate().getItemQuality()) {
			case COMMON:
			case JUNK:
				slots = 1;
				break;
			case RARE:
				slots = 2;
				break;
			case LEGEND:
				slots = 3;
				break;
			case UNIQUE:
				slots = 4;
				break;
			case EPIC:
				slots = 5;
				break;
			case MYTHIC:
				slots = 5;
				break;
			default:
				slots = 0;
				break;
		}
		if (item.getItemTemplate().getItemType() == ItemType.DRACONIC) {
			slots += 1;
		}
		if (item.getItemTemplate().getItemType() == ItemType.ABYSS) {
			slots += 2;
		}
		return slots;

	}

	private void showHelp(Player admin) {
		PacketSendUtility.sendMessage(admin, "[Help: Equip Command]\n" + "  Use //equip help <socket|enchant|plume|godstone> for more details on the command.\n" + "  Notice: This command uses smart matching. You may abbreviate most commands.\n" + "  For example: (//equip so 167000551 5) will match to (//equip socket 167000551 5)");
	}

	private void showHelpEnchant(Player admin) {
		PacketSendUtility.sendMessage(admin, "Syntax:  //equip [playerName] enchant [EnchantLevel = 0]\n" + "  This command Enchants all items equipped up to 255.\n" + "  Notice: You can ommit parameters between [], especially playerName.\n" + "  Target: Named player, then targeted player, only then self.\n" + "  Default Value: EnchantLevel is 0.");
	}

	private void showHelpplume(Player admin) {
		PacketSendUtility.sendMessage(admin, "Syntax:  //equip [playerName] plume [EnchantLevel = 0]\n" + "  This command Enchant the  equipped plume up to 20.\n" + "  Notice: You can ommit parameters between [], especially playerName.\n" + "  Target: Named player, then targeted player, only then self.\n" + "  Default Value: EnchantLevel is 0.");
	}

	private void showHelpSocket(Player admin) {
		PacketSendUtility.sendMessage(admin, "Syntax:  //equip [playerName] socket [ManastoneID = 167000551] [Quantity = 0]\n" + "  This command Sockets all free slots on equipped items, with the given manastone id.\n" + "  Use ManastoneID = 0 to remove all Manastones. Quantity = 0 means to fill all free slots.\n" + "  Notice: You can ommit parameters between [], especially playerName.\n" + "  Target: Named player, then targeted player, only then self.\n" + "  Default Value: ManastoneID is 167000551, Quantity is 0 meaning fill all slots.");
	}

	private void showHelpGodstone(Player admin) {
		PacketSendUtility.sendMessage(admin, "Syntax:  //equip [playerName] godstone [rate = 100|GodStoneID]\n" + "  This command changes the godstone activation rate to the given number(0-100).\n" + "  Give a GodStone ItemID and it will be socketed on you Main Hand Weapon.\n" + "  Notice: You can ommit parameters between [], especially playerName.\n" + "  Target: Named player, then targeted player, only then self.\n" + "  Default Value: Rate is 100 witch is the default action .");
	}

	@Override
	public void onFail(Player player, String message) {
		showHelp(player);
	}
}
