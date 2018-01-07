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
import com.aionemu.gameserver.services.abyss.AbyssPointsService;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.PlayerCommand;

/**
 * @author Maestross
 */
public class cmd_honorsitems extends PlayerCommand {

	public cmd_honorsitems() {
		super("honoritems");
	}

	@Override
	public void execute(Player player, String... params) {
		if (params.length < 1) {
			PacketSendUtility.sendMessage(player, "Syntax: .honoritems <plate | leather | cloth | chain | weapons>");
			PacketSendUtility.sendMessage(player, "Syntax: .honoritems <pprices | lprices | cprices | ccprices | wprices>");
			return;
		}

		if (params[0].equalsIgnoreCase("plate")) {
			plate(player);
		}
		if (params[0].equalsIgnoreCase("leather")) {
			leather(player);
		}
		if (params[0].equalsIgnoreCase("cloth")) {
			cloth(player);
		}
		if (params[0].equalsIgnoreCase("chain")) {
			chain(player);
		}
		if (params[0].equalsIgnoreCase("weapons")) {
			weapons(player);
		}
		if (params[0].equalsIgnoreCase("pprices")) {
			plateInfo(player);
		}
		if (params[0].equalsIgnoreCase("lprices")) {
			leatherInfo(player);
		}
		if (params[0].equalsIgnoreCase("cprices")) {
			clothInfo(player);
		}
		if (params[0].equalsIgnoreCase("ccprices")) {
			chainInfo(player);
		}
		if (params[0].equalsIgnoreCase("wprices")) {
			weaponsInfo(player);
		}
		if (params[0].equalsIgnoreCase("1")) {// Plate Breast
			case1(player);
		}
		if (params[0].equalsIgnoreCase("2")) {// Plate Hands
			case2(player);
		}
		if (params[0].equalsIgnoreCase("3")) {// Plate Shoes
			case3(player);
		}
		if (params[0].equalsIgnoreCase("4")) {// Plate Pants
			case4(player);
		}
		if (params[0].equalsIgnoreCase("5")) {// Plate Shoulders
			case5(player);
		}
		if (params[0].equalsIgnoreCase("6")) {// Leather Breast
			case6(player);
		}
		if (params[0].equalsIgnoreCase("7")) {// Leather Hands
			case7(player);
		}
		if (params[0].equalsIgnoreCase("8")) {// Leather Shoes
			case8(player);
		}
		if (params[0].equalsIgnoreCase("9")) {// Leather Pants
			case9(player);
		}
		if (params[0].equalsIgnoreCase("10")) {// Leather Shoulders
			case10(player);
		}
		if (params[0].equalsIgnoreCase("11")) {// Cloth Breast
			case11(player);
		}
		if (params[0].equalsIgnoreCase("12")) {// Cloth Hands
			case12(player);
		}
		if (params[0].equalsIgnoreCase("13")) {// Cloth Shoes
			case13(player);
		}
		if (params[0].equalsIgnoreCase("14")) {// Cloth Pants
			case14(player);
		}
		if (params[0].equalsIgnoreCase("15")) {// Cloth Shoulders
			case15(player);
		}
		if (params[0].equalsIgnoreCase("16")) {// Chain Breast
			case16(player);
		}
		if (params[0].equalsIgnoreCase("17")) {// Chain Hands
			case17(player);
		}
		if (params[0].equalsIgnoreCase("18")) {// Chain Shoes
			case18(player);
		}
		if (params[0].equalsIgnoreCase("19")) {// Chain Pants
			case19(player);
		}
		if (params[0].equalsIgnoreCase("20")) {// Chain Shoulders
			case20(player);
		}
		if (params[0].equalsIgnoreCase("21")) {// Weapon Sword
			case21(player);
		}
		if (params[0].equalsIgnoreCase("22")) {// Weapon Greatsword
			case22(player);
		}
		if (params[0].equalsIgnoreCase("23")) {// Weapon Longbow
			case23(player);
		}
		if (params[0].equalsIgnoreCase("24")) {// Weapon Dagger
			case24(player);
		}
		if (params[0].equalsIgnoreCase("25")) {// Weapon Orb
			case25(player);
		}
		if (params[0].equalsIgnoreCase("26")) {// Weapon Tome
			case26(player);
		}
		if (params[0].equalsIgnoreCase("27")) {// Weapon Staff
			case27(player);
		}
		if (params[0].equalsIgnoreCase("28")) {// Weapon Mace
			case28(player);
		}
		if (params[0].equalsIgnoreCase("29")) {// Weapon Shield
			case29(player);
		}
		if (params[0].equalsIgnoreCase("30")) {// Weapon Spear
			case30(player);
		}
	}

	private void plateInfo(Player player) {
		PacketSendUtility.sendYellowMessageOnCenter(player, "Armor Plate Prices");
		PacketSendUtility.sendMessage(player, "----------------");
		PacketSendUtility.sendMessage(player, "[item: 110601342] AP: 4329504 Medals: 105");// Plate Breast
		PacketSendUtility.sendMessage(player, "[item: 111601305] AP: 2164752 Medals: 52");// Plate Hands
		PacketSendUtility.sendMessage(player, "[item: 114601291] AP: 2164752 Medals: 52");// Plate Shoes
		PacketSendUtility.sendMessage(player, "[item: 113601294] AP: 3247344 Medals: 78");// Plate Pants
		PacketSendUtility.sendMessage(player, "[item: 112601285] AP: 2164752 Medals: 52");// Plate Shoulders
		PacketSendUtility.sendMessage(player, "----------------");
	}

	private void leatherInfo(Player player) {
		PacketSendUtility.sendYellowMessageOnCenter(player, "Leather Armor Prices");
		PacketSendUtility.sendMessage(player, "----------------");
		PacketSendUtility.sendMessage(player, "[item: 110301393] AP: 4329504 Medals: 105");// Leather Breast
		PacketSendUtility.sendMessage(player, "[item: 111301334] AP: 2164752 Medals: 52");// Leather Hands
		PacketSendUtility.sendMessage(player, "[item: 114301393] AP: 2164752 Medals: 52");// Leather Shoes
		PacketSendUtility.sendMessage(player, "[item: 113301358] AP: 3247344 Medals: 78");// Leather Pants
		PacketSendUtility.sendMessage(player, "[item: 112301277] AP: 2164752 Medals: 52");// Leather Shoulders
		PacketSendUtility.sendMessage(player, "----------------");
	}

	private void clothInfo(Player player) {
		PacketSendUtility.sendYellowMessageOnCenter(player, "Cloth Armor prices");
		PacketSendUtility.sendMessage(player, "----------------");
		PacketSendUtility.sendMessage(player, "[item: 110101485] AP: 4329504 Medals: 105");// Cloth Breast
		PacketSendUtility.sendMessage(player, "[item: 111101339] AP: 2164752 Medals: 52");// Cloth Hands
		PacketSendUtility.sendMessage(player, "[item: 114101387] AP: 2164752 Medals: 52");// Cloth Shoes
		PacketSendUtility.sendMessage(player, "[item: 113101356] AP: 3247344 Medals: 78");// Cloth Pants
		PacketSendUtility.sendMessage(player, "[item: 112101296] AP: 2164752 Medals: 52");// Cloth Shoulders
		PacketSendUtility.sendMessage(player, "----------------");
	}

	private void chainInfo(Player player) {
		PacketSendUtility.sendYellowMessageOnCenter(player, "Chain Armor Prices");
		PacketSendUtility.sendMessage(player, "----------------");
		PacketSendUtility.sendMessage(player, "[item: 110501368] AP: 4329504 Medals: 105");// Chain Breast
		PacketSendUtility.sendMessage(player, "[item: 111501326] AP: 2164752 Medals: 52");// Chain Hands
		PacketSendUtility.sendMessage(player, "[item: 114501349] AP: 2164752 Medals: 52");// Chain Shoes
		PacketSendUtility.sendMessage(player, "[item: 113501341] AP: 3247344 Medals: 78");// Chain Pants
		PacketSendUtility.sendMessage(player, "[item: 112501266] AP: 2164752 Medals: 52");// Chain Shoulders
		PacketSendUtility.sendMessage(player, "----------------");
	}

	private void weaponsInfo(Player player) {
		PacketSendUtility.sendYellowMessageOnCenter(player, "Weapon Pprices");
		PacketSendUtility.sendMessage(player, "----------------");
		PacketSendUtility.sendMessage(player, "[item: 100001412] AP: 6494256 Medals: 156");// Weapon Sword
		PacketSendUtility.sendMessage(player, "[item: 100901105] AP: 6494256 Medals: 156");// Weapon Greatsword
		PacketSendUtility.sendMessage(player, "[item: 101701134] AP: 6494256 Medals: 156");// Weapon Longbow
		PacketSendUtility.sendMessage(player, "[item: 100201251] AP: 6494256 Medals: 156");// Weapon Dagger
		PacketSendUtility.sendMessage(player, "[item: 100501097] AP: 6494256 Medals: 156");// Weapon Orb
		PacketSendUtility.sendMessage(player, "[item: 100601153] AP: 6494256 Medals: 156");// Weapon Tome
		PacketSendUtility.sendMessage(player, "[item: 101501123] AP: 6494256 Medals: 156");// Weapon Staff
		PacketSendUtility.sendMessage(player, "[item: 100101089] AP: 6494256 Medals: 156");// Weapon Mace
		PacketSendUtility.sendMessage(player, "[item: 115001462] AP: 4329504 Medals: 105");// Weapon Shield
		PacketSendUtility.sendMessage(player, "[item: 101301042] AP: 6494256 Medals: 156");// Weapon Spear
		PacketSendUtility.sendMessage(player, "----------------");
	}

	private void plate(Player player) {
		PacketSendUtility.sendYellowMessageOnCenter(player, "Plates");
		PacketSendUtility.sendMessage(player, "----------------");
		PacketSendUtility.sendMessage(player, "[item: 110601342] (1)");// Plate Breast
		PacketSendUtility.sendMessage(player, "[item: 111601305] (2)");// Plate Hands
		PacketSendUtility.sendMessage(player, "[item: 114601291] (3)");// Plate Shoes
		PacketSendUtility.sendMessage(player, "[item: 113601294] (4)");// Plate Pants
		PacketSendUtility.sendMessage(player, "[item: 112601285] (5)");// Plate Shoulders
		PacketSendUtility.sendMessage(player, "----------------");
		PacketSendUtility.sendYellowMessageOnCenter(player, "Use now .honoritems and the corresponding ID number (Examplel: .honoritems 1");
	}

	private void leather(Player player) {
		PacketSendUtility.sendYellowMessageOnCenter(player, "Leather");
		PacketSendUtility.sendMessage(player, "----------------");
		PacketSendUtility.sendMessage(player, "[item: 110301393] (6)");// Leather Breast
		PacketSendUtility.sendMessage(player, "[item: 111301334] (7)");// Leather Hands
		PacketSendUtility.sendMessage(player, "[item: 114301393] (8)");// Leather Shoes
		PacketSendUtility.sendMessage(player, "[item: 113301358] (9)");// Leather Pants
		PacketSendUtility.sendMessage(player, "[item: 112301277] (10)");// Leather Shoulders
		PacketSendUtility.sendMessage(player, "----------------");
		PacketSendUtility.sendYellowMessageOnCenter(player, "Use now .honoritems and the corresponding ID number (Examplel: .honoritems 6");
	}

	private void cloth(Player player) {
		PacketSendUtility.sendYellowMessageOnCenter(player, "Cloth");
		PacketSendUtility.sendMessage(player, "----------------");
		PacketSendUtility.sendMessage(player, "[item: 110101485] (11)");// Cloth Breast
		PacketSendUtility.sendMessage(player, "[item: 111101339] (12)");// Cloth Hands
		PacketSendUtility.sendMessage(player, "[item: 114101387] (13)");// Cloth Shoes
		PacketSendUtility.sendMessage(player, "[item: 113101356] (14)");// Cloth Pants
		PacketSendUtility.sendMessage(player, "[item: 112101296] (15)");// Cloth Shoulders
		PacketSendUtility.sendMessage(player, "----------------");
		PacketSendUtility.sendYellowMessageOnCenter(player, "Use now .honoritems and the corresponding ID number (Examplel: .honoritems 11");
	}

	private void chain(Player player) {
		PacketSendUtility.sendYellowMessageOnCenter(player, "Chain");
		PacketSendUtility.sendMessage(player, "----------------");
		PacketSendUtility.sendMessage(player, "[item: 110501368] (16)");// Chain Breast
		PacketSendUtility.sendMessage(player, "[item: 111501326] (17)");// Chain Hands
		PacketSendUtility.sendMessage(player, "[item: 114501349] (18)");// Chain Shoes
		PacketSendUtility.sendMessage(player, "[item: 113501341] (19)");// Chain Pants
		PacketSendUtility.sendMessage(player, "[item: 112501266] (20)");// Chain Shoulders
		PacketSendUtility.sendMessage(player, "----------------");
		PacketSendUtility.sendYellowMessageOnCenter(player, "Use now .honoritems and the corresponding ID number (Examplel: .honoritems 16");
	}

	private void weapons(Player player) {
		PacketSendUtility.sendYellowMessageOnCenter(player, "Weapons");
		PacketSendUtility.sendMessage(player, "----------------");
		PacketSendUtility.sendMessage(player, "[item: 100001412] (21)");// Weapon Sword
		PacketSendUtility.sendMessage(player, "[item: 100901105] (22)");// Weapon Greatsword
		PacketSendUtility.sendMessage(player, "[item: 101701134] (23)");// Weapon Longbow
		PacketSendUtility.sendMessage(player, "[item: 100201251] (24)");// Weapon Dagger
		PacketSendUtility.sendMessage(player, "[item: 100501097] (25)");// Weapon Orb
		PacketSendUtility.sendMessage(player, "[item: 100601153] (26)");// Weapon Tome
		PacketSendUtility.sendMessage(player, "[item: 101501123] (27)");// Weapon Staff
		PacketSendUtility.sendMessage(player, "[item: 100101089] (28)");// Weapon Mace
		PacketSendUtility.sendMessage(player, "[item: 115001462] (29)");// Weapon Shield
		PacketSendUtility.sendMessage(player, "[item: 101301042] (30)");// Weapon Spear
		PacketSendUtility.sendMessage(player, "----------------");
		PacketSendUtility.sendYellowMessageOnCenter(player, "Use now .honoritems and the corresponding ID number (Examplel: .honoritems 21");
	}

	private void case1(Player player) {// Plate Breast
		Storage bag = player.getInventory();
		int count = 105;
		int ap = 4329504;
		int id = 110601342;
		long itemsInBag = bag.getItemCountByItemId(186000223);
		if (player.getAbyssRank().getAp() < ap) {
			PacketSendUtility.sendYellowMessageOnCenter(player, "You do not have enough AP, you only have: " + ap);
			return;
		}
		if (itemsInBag < count) {
			PacketSendUtility.sendYellowMessageOnCenter(player, "You do not have enough medals, you have only: " + count);
			return;
		}
		AbyssPointsService.addAp(player, -ap);
		Item item = bag.getFirstItemByItemId(186000223);
		bag.decreaseByObjectId(item.getObjectId(), count);
		ItemService.addItem(player, id, 1);
		PacketSendUtility.sendMessage(player, "You have successfully received your item!");
	}

	private void case2(Player player) {// Plate Hands
		Storage bag = player.getInventory();
		int count = 52;
		int ap = 2164752;
		int id = 111601305;
		long itemsInBag = bag.getItemCountByItemId(186000223);
		if (player.getAbyssRank().getAp() < ap) {
			PacketSendUtility.sendYellowMessageOnCenter(player, "You do not have enough AP, you only have: " + ap);
			return;
		}
		if (itemsInBag < count) {
			PacketSendUtility.sendYellowMessageOnCenter(player, "You do not have enough medals, you have only: " + count);
			return;
		}
		AbyssPointsService.addAp(player, -ap);
		Item item = bag.getFirstItemByItemId(186000223);
		bag.decreaseByObjectId(item.getObjectId(), count);
		ItemService.addItem(player, id, 1);
		PacketSendUtility.sendMessage(player, "You have successfully received your item!");
	}

	private void case3(Player player) {// Plate Shoes
		Storage bag = player.getInventory();
		int count = 52;
		int ap = 2164752;
		int id = 114601291;
		long itemsInBag = bag.getItemCountByItemId(186000223);
		if (player.getAbyssRank().getAp() < ap) {
			PacketSendUtility.sendYellowMessageOnCenter(player, "You do not have enough AP, you only have: " + ap);
			return;
		}
		if (itemsInBag < count) {
			PacketSendUtility.sendYellowMessageOnCenter(player, "You do not have enough medals, you have only: " + count);
			return;
		}
		AbyssPointsService.addAp(player, -ap);
		Item item = bag.getFirstItemByItemId(186000223);
		bag.decreaseByObjectId(item.getObjectId(), count);
		ItemService.addItem(player, id, 1);
		PacketSendUtility.sendMessage(player, "You have successfully received your item!");
	}

	private void case4(Player player) {// Plate Pants
		Storage bag = player.getInventory();
		int count = 78;
		int ap = 3247344;
		int id = 113601294;
		long itemsInBag = bag.getItemCountByItemId(186000223);
		if (player.getAbyssRank().getAp() < ap) {
			PacketSendUtility.sendYellowMessageOnCenter(player, "You do not have enough AP, you only have: " + ap);
			return;
		}
		if (itemsInBag < count) {
			PacketSendUtility.sendYellowMessageOnCenter(player, "You do not have enough medals, you have only: " + count);
			return;
		}
		AbyssPointsService.addAp(player, -ap);
		Item item = bag.getFirstItemByItemId(186000223);
		bag.decreaseByObjectId(item.getObjectId(), count);
		ItemService.addItem(player, id, 1);
		PacketSendUtility.sendMessage(player, "You have successfully received your item!");
	}

	private void case5(Player player) {// Plate Shoulders
		Storage bag = player.getInventory();
		int count = 52;
		int ap = 2164752;
		int id = 112601285;
		long itemsInBag = bag.getItemCountByItemId(186000223);
		if (player.getAbyssRank().getAp() < ap) {
			PacketSendUtility.sendYellowMessageOnCenter(player, "You do not have enough AP, you only have: " + ap);
			return;
		}
		if (itemsInBag < count) {
			PacketSendUtility.sendYellowMessageOnCenter(player, "You do not have enough medals, you have only: " + count);
			return;
		}
		AbyssPointsService.addAp(player, -ap);
		Item item = bag.getFirstItemByItemId(186000223);
		bag.decreaseByObjectId(item.getObjectId(), count);
		ItemService.addItem(player, id, 1);
		PacketSendUtility.sendMessage(player, "You have successfully received your item!");
	}

	private void case6(Player player) {// Leather Breast
		Storage bag = player.getInventory();
		int count = 105;
		int ap = 4329504;
		int id = 110301393;
		long itemsInBag = bag.getItemCountByItemId(186000223);
		if (player.getAbyssRank().getAp() < ap) {
			PacketSendUtility.sendYellowMessageOnCenter(player, "You do not have enough AP, you only have: " + ap);
			return;
		}
		if (itemsInBag < count) {
			PacketSendUtility.sendYellowMessageOnCenter(player, "You do not have enough medals, you have only: " + count);
			return;
		}
		AbyssPointsService.addAp(player, -ap);
		Item item = bag.getFirstItemByItemId(186000223);
		bag.decreaseByObjectId(item.getObjectId(), count);
		ItemService.addItem(player, id, 1);
		PacketSendUtility.sendMessage(player, "You have successfully received your item!");
	}

	private void case7(Player player) {// Leather Hands
		Storage bag = player.getInventory();
		int count = 52;
		int ap = 2164752;
		int id = 111301334;
		long itemsInBag = bag.getItemCountByItemId(186000223);
		if (player.getAbyssRank().getAp() < ap) {
			PacketSendUtility.sendYellowMessageOnCenter(player, "You do not have enough AP, you only have: " + ap);
			return;
		}
		if (itemsInBag < count) {
			PacketSendUtility.sendYellowMessageOnCenter(player, "You do not have enough medals, you have only: " + count);
			return;
		}
		AbyssPointsService.addAp(player, -ap);
		Item item = bag.getFirstItemByItemId(186000223);
		bag.decreaseByObjectId(item.getObjectId(), count);
		ItemService.addItem(player, id, 1);
		PacketSendUtility.sendMessage(player, "You have successfully received your item!");
	}

	private void case8(Player player) {// Leather Shoes
		Storage bag = player.getInventory();
		int count = 52;
		int ap = 2164752;
		int id = 114301393;
		long itemsInBag = bag.getItemCountByItemId(186000223);
		if (player.getAbyssRank().getAp() < ap) {
			PacketSendUtility.sendYellowMessageOnCenter(player, "You do not have enough AP, you only have: " + ap);
			return;
		}
		if (itemsInBag < count) {
			PacketSendUtility.sendYellowMessageOnCenter(player, "You do not have enough medals, you have only: " + count);
			return;
		}
		AbyssPointsService.addAp(player, -ap);
		Item item = bag.getFirstItemByItemId(186000223);
		bag.decreaseByObjectId(item.getObjectId(), count);
		ItemService.addItem(player, id, 1);
		PacketSendUtility.sendMessage(player, "You have successfully received your item!");
	}

	private void case9(Player player) {// Leather Pants
		Storage bag = player.getInventory();
		int count = 78;
		int ap = 3247344;
		int id = 113301358;
		long itemsInBag = bag.getItemCountByItemId(186000223);
		if (player.getAbyssRank().getAp() < ap) {
			PacketSendUtility.sendYellowMessageOnCenter(player, "You do not have enough AP, you only have: " + ap);
			return;
		}
		if (itemsInBag < count) {
			PacketSendUtility.sendYellowMessageOnCenter(player, "You do not have enough medals, you have only: " + count);
			return;
		}
		AbyssPointsService.addAp(player, -ap);
		Item item = bag.getFirstItemByItemId(186000223);
		bag.decreaseByObjectId(item.getObjectId(), count);
		ItemService.addItem(player, id, 1);
		PacketSendUtility.sendMessage(player, "You have successfully received your item!");
	}

	private void case10(Player player) {// Leather Shoulders
		Storage bag = player.getInventory();
		int count = 52;
		int ap = 2164752;
		int id = 112301277;
		long itemsInBag = bag.getItemCountByItemId(186000223);
		if (player.getAbyssRank().getAp() < ap) {
			PacketSendUtility.sendYellowMessageOnCenter(player, "You do not have enough AP, you only have: " + ap);
			return;
		}
		if (itemsInBag < count) {
			PacketSendUtility.sendYellowMessageOnCenter(player, "You do not have enough medals, you have only: " + count);
			return;
		}
		AbyssPointsService.addAp(player, -ap);
		Item item = bag.getFirstItemByItemId(186000223);
		bag.decreaseByObjectId(item.getObjectId(), count);
		ItemService.addItem(player, id, 1);
		PacketSendUtility.sendMessage(player, "You have successfully received your item!");
	}

	private void case11(Player player) {// Cloth Breast
		Storage bag = player.getInventory();
		int count = 105;
		int ap = 4329504;
		int id = 110101485;
		long itemsInBag = bag.getItemCountByItemId(186000223);
		if (player.getAbyssRank().getAp() < ap) {
			PacketSendUtility.sendYellowMessageOnCenter(player, "You do not have enough AP, you only have: " + ap);
			return;
		}
		if (itemsInBag < count) {
			PacketSendUtility.sendYellowMessageOnCenter(player, "You do not have enough medals, you have only: " + count);
			return;
		}
		AbyssPointsService.addAp(player, -ap);
		Item item = bag.getFirstItemByItemId(186000223);
		bag.decreaseByObjectId(item.getObjectId(), count);
		ItemService.addItem(player, id, 1);
		PacketSendUtility.sendMessage(player, "You have successfully received your item!");
	}

	private void case12(Player player) {// Cloth Hands
		Storage bag = player.getInventory();
		int count = 52;
		int ap = 2164752;
		int id = 111101339;
		long itemsInBag = bag.getItemCountByItemId(186000223);
		if (player.getAbyssRank().getAp() < ap) {
			PacketSendUtility.sendYellowMessageOnCenter(player, "You do not have enough AP, you only have: " + ap);
			return;
		}
		if (itemsInBag < count) {
			PacketSendUtility.sendYellowMessageOnCenter(player, "You do not have enough medals, you have only: " + count);
			return;
		}
		AbyssPointsService.addAp(player, -ap);
		Item item = bag.getFirstItemByItemId(186000223);
		bag.decreaseByObjectId(item.getObjectId(), count);
		ItemService.addItem(player, id, 1);
		PacketSendUtility.sendMessage(player, "You have successfully received your item!");
	}

	private void case13(Player player) {// Cloth Shoes
		Storage bag = player.getInventory();
		int count = 52;
		int ap = 2164752;
		int id = 114101387;
		long itemsInBag = bag.getItemCountByItemId(186000223);
		if (player.getAbyssRank().getAp() < ap) {
			PacketSendUtility.sendYellowMessageOnCenter(player, "You do not have enough AP, you only have: " + ap);
			return;
		}
		if (itemsInBag < count) {
			PacketSendUtility.sendYellowMessageOnCenter(player, "You do not have enough medals, you have only: " + count);
			return;
		}
		AbyssPointsService.addAp(player, -ap);
		Item item = bag.getFirstItemByItemId(186000223);
		bag.decreaseByObjectId(item.getObjectId(), count);
		ItemService.addItem(player, id, 1);
		PacketSendUtility.sendMessage(player, "You have successfully received your item!");
	}

	private void case14(Player player) {// Cloth Pants
		Storage bag = player.getInventory();
		int count = 78;
		int ap = 3247344;
		int id = 113101356;
		long itemsInBag = bag.getItemCountByItemId(186000223);
		if (player.getAbyssRank().getAp() < ap) {
			PacketSendUtility.sendYellowMessageOnCenter(player, "You do not have enough AP, you only have: " + ap);
			return;
		}
		if (itemsInBag < count) {
			PacketSendUtility.sendYellowMessageOnCenter(player, "You do not have enough medals, you have only: " + count);
			return;
		}
		AbyssPointsService.addAp(player, -ap);
		Item item = bag.getFirstItemByItemId(186000223);
		bag.decreaseByObjectId(item.getObjectId(), count);
		ItemService.addItem(player, id, 1);
		PacketSendUtility.sendMessage(player, "You have successfully received your item!");
	}

	private void case15(Player player) {// Cloth Shoulders
		Storage bag = player.getInventory();
		int count = 52;
		int ap = 2164752;
		int id = 112101296;
		long itemsInBag = bag.getItemCountByItemId(186000223);
		if (player.getAbyssRank().getAp() < ap) {
			PacketSendUtility.sendYellowMessageOnCenter(player, "You do not have enough AP, you only have: " + ap);
			return;
		}
		if (itemsInBag < count) {
			PacketSendUtility.sendYellowMessageOnCenter(player, "You do not have enough medals, you have only: " + count);
			return;
		}
		AbyssPointsService.addAp(player, -ap);
		Item item = bag.getFirstItemByItemId(186000223);
		bag.decreaseByObjectId(item.getObjectId(), count);
		ItemService.addItem(player, id, 1);
		PacketSendUtility.sendMessage(player, "You have successfully received your item!");
	}

	private void case16(Player player) {// Chain Breast
		Storage bag = player.getInventory();
		int count = 105;
		int ap = 4329504;
		int id = 110501368;
		long itemsInBag = bag.getItemCountByItemId(186000223);
		if (player.getAbyssRank().getAp() < ap) {
			PacketSendUtility.sendYellowMessageOnCenter(player, "You do not have enough AP, you only have: " + ap);
			return;
		}
		if (itemsInBag < count) {
			PacketSendUtility.sendYellowMessageOnCenter(player, "You do not have enough medals, you have only: " + count);
			return;
		}
		AbyssPointsService.addAp(player, -ap);
		Item item = bag.getFirstItemByItemId(186000223);
		bag.decreaseByObjectId(item.getObjectId(), count);
		ItemService.addItem(player, id, 1);
		PacketSendUtility.sendMessage(player, "You have successfully received your item!");
	}

	private void case17(Player player) {// Chain Hands
		Storage bag = player.getInventory();
		int count = 52;
		int ap = 2164752;
		int id = 111501326;
		long itemsInBag = bag.getItemCountByItemId(186000223);
		if (player.getAbyssRank().getAp() < ap) {
			PacketSendUtility.sendYellowMessageOnCenter(player, "You do not have enough AP, you only have: " + ap);
			return;
		}
		if (itemsInBag < count) {
			PacketSendUtility.sendYellowMessageOnCenter(player, "You do not have enough medals, you have only: " + count);
			return;
		}
		AbyssPointsService.addAp(player, -ap);
		Item item = bag.getFirstItemByItemId(186000223);
		bag.decreaseByObjectId(item.getObjectId(), count);
		ItemService.addItem(player, id, 1);
		PacketSendUtility.sendMessage(player, "You have successfully received your item!");
	}

	private void case18(Player player) {// Chain Shoes
		Storage bag = player.getInventory();
		int count = 52;
		int ap = 2164752;
		int id = 114501349;
		long itemsInBag = bag.getItemCountByItemId(186000223);
		if (player.getAbyssRank().getAp() < ap) {
			PacketSendUtility.sendYellowMessageOnCenter(player, "You do not have enough AP, you only have: " + ap);
			return;
		}
		if (itemsInBag < count) {
			PacketSendUtility.sendYellowMessageOnCenter(player, "You do not have enough medals, you have only: " + count);
			return;
		}
		AbyssPointsService.addAp(player, -ap);
		Item item = bag.getFirstItemByItemId(186000223);
		bag.decreaseByObjectId(item.getObjectId(), count);
		ItemService.addItem(player, id, 1);
		PacketSendUtility.sendMessage(player, "You have successfully received your item!");
	}

	private void case19(Player player) {// Chain Pants
		Storage bag = player.getInventory();
		int count = 78;
		int ap = 3247344;
		int id = 113501341;
		long itemsInBag = bag.getItemCountByItemId(186000223);
		if (player.getAbyssRank().getAp() < ap) {
			PacketSendUtility.sendYellowMessageOnCenter(player, "You do not have enough AP, you only have: " + ap);
			return;
		}
		if (itemsInBag < count) {
			PacketSendUtility.sendYellowMessageOnCenter(player, "You do not have enough medals, you have only: " + count);
			return;
		}
		AbyssPointsService.addAp(player, -ap);
		Item item = bag.getFirstItemByItemId(186000223);
		bag.decreaseByObjectId(item.getObjectId(), count);
		ItemService.addItem(player, id, 1);
		PacketSendUtility.sendMessage(player, "You have successfully received your item!");
	}

	private void case20(Player player) {// Chain Shoulders
		Storage bag = player.getInventory();
		int count = 52;
		int ap = 2164752;
		int id = 112501266;
		long itemsInBag = bag.getItemCountByItemId(186000223);
		if (player.getAbyssRank().getAp() < ap) {
			PacketSendUtility.sendYellowMessageOnCenter(player, "You do not have enough AP, you only have: " + ap);
			return;
		}
		if (itemsInBag < count) {
			PacketSendUtility.sendYellowMessageOnCenter(player, "You do not have enough medals, you have only: " + count);
			return;
		}
		AbyssPointsService.addAp(player, -ap);
		Item item = bag.getFirstItemByItemId(186000223);
		bag.decreaseByObjectId(item.getObjectId(), count);
		ItemService.addItem(player, id, 1);
		PacketSendUtility.sendMessage(player, "You have successfully received your item!");
	}

	private void case21(Player player) {// Weapon Sword
		Storage bag = player.getInventory();
		int count = 156;
		int ap = 6494256;
		int id = 100001412;
		long itemsInBag = bag.getItemCountByItemId(186000223);
		if (player.getAbyssRank().getAp() < ap) {
			PacketSendUtility.sendYellowMessageOnCenter(player, "You do not have enough AP, you only have: " + ap);
			return;
		}
		if (itemsInBag < count) {
			PacketSendUtility.sendYellowMessageOnCenter(player, "You do not have enough medals, you have only: " + count);
			return;
		}
		AbyssPointsService.addAp(player, -ap);
		Item item = bag.getFirstItemByItemId(186000223);
		bag.decreaseByObjectId(item.getObjectId(), count);
		ItemService.addItem(player, id, 1);
		PacketSendUtility.sendMessage(player, "You have successfully received your item!");
	}

	private void case22(Player player) {// Weapon Greatsword
		Storage bag = player.getInventory();
		int count = 156;
		int ap = 6494256;
		int id = 100901105;
		long itemsInBag = bag.getItemCountByItemId(186000223);
		if (player.getAbyssRank().getAp() < ap) {
			PacketSendUtility.sendYellowMessageOnCenter(player, "You do not have enough AP, you only have: " + ap);
			return;
		}
		if (itemsInBag < count) {
			PacketSendUtility.sendYellowMessageOnCenter(player, "You do not have enough medals, you have only: " + count);
			return;
		}
		AbyssPointsService.addAp(player, -ap);
		Item item = bag.getFirstItemByItemId(186000223);
		bag.decreaseByObjectId(item.getObjectId(), count);
		ItemService.addItem(player, id, 1);
		PacketSendUtility.sendMessage(player, "You have successfully received your item!");
	}

	private void case23(Player player) {// Weapon Longbow
		Storage bag = player.getInventory();
		int count = 156;
		int ap = 6494256;
		int id = 101701134;
		long itemsInBag = bag.getItemCountByItemId(186000223);
		if (player.getAbyssRank().getAp() < ap) {
			PacketSendUtility.sendYellowMessageOnCenter(player, "You do not have enough AP, you only have: " + ap);
			return;
		}
		if (itemsInBag < count) {
			PacketSendUtility.sendYellowMessageOnCenter(player, "You do not have enough medals, you have only: " + count);
			return;
		}
		AbyssPointsService.addAp(player, -ap);
		Item item = bag.getFirstItemByItemId(186000223);
		bag.decreaseByObjectId(item.getObjectId(), count);
		ItemService.addItem(player, id, 1);
		PacketSendUtility.sendMessage(player, "You have successfully received your item!");
	}

	private void case24(Player player) {// Weapon Dagger
		Storage bag = player.getInventory();
		int count = 156;
		int ap = 6494256;
		int id = 100201251;
		long itemsInBag = bag.getItemCountByItemId(186000223);
		if (player.getAbyssRank().getAp() < ap) {
			PacketSendUtility.sendYellowMessageOnCenter(player, "You do not have enough AP, you only have: " + ap);
			return;
		}
		if (itemsInBag < count) {
			PacketSendUtility.sendYellowMessageOnCenter(player, "You do not have enough medals, you have only: " + count);
			return;
		}
		AbyssPointsService.addAp(player, -ap);
		Item item = bag.getFirstItemByItemId(186000223);
		bag.decreaseByObjectId(item.getObjectId(), count);
		ItemService.addItem(player, id, 1);
		PacketSendUtility.sendMessage(player, "You have successfully received your item!");
	}

	private void case25(Player player) {// Weapon Orb
		Storage bag = player.getInventory();
		int count = 156;
		int ap = 6494256;
		int id = 100501097;
		long itemsInBag = bag.getItemCountByItemId(186000223);
		if (player.getAbyssRank().getAp() < ap) {
			PacketSendUtility.sendYellowMessageOnCenter(player, "You do not have enough AP, you only have: " + ap);
			return;
		}
		if (itemsInBag < count) {
			PacketSendUtility.sendYellowMessageOnCenter(player, "You do not have enough medals, you have only: " + count);
			return;
		}
		AbyssPointsService.addAp(player, -ap);
		Item item = bag.getFirstItemByItemId(186000223);
		bag.decreaseByObjectId(item.getObjectId(), count);
		ItemService.addItem(player, id, 1);
		PacketSendUtility.sendMessage(player, "You have successfully received your item!");
	}

	private void case26(Player player) {// Weapon Tome
		Storage bag = player.getInventory();
		int count = 156;
		int ap = 6494256;
		int id = 100601153;
		long itemsInBag = bag.getItemCountByItemId(186000223);
		if (player.getAbyssRank().getAp() < ap) {
			PacketSendUtility.sendYellowMessageOnCenter(player, "You do not have enough AP, you only have: " + ap);
			return;
		}
		if (itemsInBag < count) {
			PacketSendUtility.sendYellowMessageOnCenter(player, "You do not have enough medals, you have only: " + count);
			return;
		}
		AbyssPointsService.addAp(player, -ap);
		Item item = bag.getFirstItemByItemId(186000223);
		bag.decreaseByObjectId(item.getObjectId(), count);
		ItemService.addItem(player, id, 1);
		PacketSendUtility.sendMessage(player, "You have successfully received your item!");
	}

	private void case27(Player player) {// Weapon Staff
		Storage bag = player.getInventory();
		int count = 156;
		int ap = 6494256;
		int id = 101501123;
		long itemsInBag = bag.getItemCountByItemId(186000223);
		if (player.getAbyssRank().getAp() < ap) {
			PacketSendUtility.sendYellowMessageOnCenter(player, "You do not have enough AP, you only have: " + ap);
			return;
		}
		if (itemsInBag < count) {
			PacketSendUtility.sendYellowMessageOnCenter(player, "You do not have enough medals, you have only: " + count);
			return;
		}
		AbyssPointsService.addAp(player, -ap);
		Item item = bag.getFirstItemByItemId(186000223);
		bag.decreaseByObjectId(item.getObjectId(), count);
		ItemService.addItem(player, id, 1);
		PacketSendUtility.sendMessage(player, "You have successfully received your item!");
	}

	private void case28(Player player) {// Weapon Mace
		Storage bag = player.getInventory();
		int count = 156;
		int ap = 6494256;
		int id = 100101089;
		long itemsInBag = bag.getItemCountByItemId(186000223);
		if (player.getAbyssRank().getAp() < ap) {
			PacketSendUtility.sendYellowMessageOnCenter(player, "You do not have enough AP, you only have: " + ap);
			return;
		}
		if (itemsInBag < count) {
			PacketSendUtility.sendYellowMessageOnCenter(player, "You do not have enough medals, you have only: " + count);
			return;
		}
		AbyssPointsService.addAp(player, -ap);
		Item item = bag.getFirstItemByItemId(186000223);
		bag.decreaseByObjectId(item.getObjectId(), count);
		ItemService.addItem(player, id, 1);
		PacketSendUtility.sendMessage(player, "You have successfully received your item!");
	}

	private void case29(Player player) {// Weapon Shield
		Storage bag = player.getInventory();
		int count = 105;
		int ap = 4329504;
		int id = 115001462;
		long itemsInBag = bag.getItemCountByItemId(186000223);
		if (player.getAbyssRank().getAp() < ap) {
			PacketSendUtility.sendYellowMessageOnCenter(player, "You do not have enough AP, you only have: " + ap);
			return;
		}
		if (itemsInBag < count) {
			PacketSendUtility.sendYellowMessageOnCenter(player, "You do not have enough medals, you have only: " + count);
			return;
		}
		AbyssPointsService.addAp(player, -ap);
		Item item = bag.getFirstItemByItemId(186000223);
		bag.decreaseByObjectId(item.getObjectId(), count);
		ItemService.addItem(player, id, 1);
		PacketSendUtility.sendMessage(player, "You have successfully received your item!");
	}

	private void case30(Player player) {// Weapon Spear
		Storage bag = player.getInventory();
		int count = 156;
		int ap = 6494256;
		int id = 101301042;
		long itemsInBag = bag.getItemCountByItemId(186000223);
		if (player.getAbyssRank().getAp() < ap) {
			PacketSendUtility.sendYellowMessageOnCenter(player, "You do not have enough AP, you only have: " + ap);
			return;
		}
		if (itemsInBag < count) {
			PacketSendUtility.sendYellowMessageOnCenter(player, "You do not have enough medals, you have only: " + count);
			return;
		}
		AbyssPointsService.addAp(player, -ap);
		Item item = bag.getFirstItemByItemId(186000223);
		bag.decreaseByObjectId(item.getObjectId(), count);
		ItemService.addItem(player, id, 1);
		PacketSendUtility.sendMessage(player, "You have successfully received your item!");
	}

	@Override
	public void onFail(Player player, String message) {
		PacketSendUtility.sendMessage(player, "Syntax: .honoritems <plate | leather | cloth | chain | weapons>");
	}
}
