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
package com.aionemu.gameserver.services.player;

import static java.lang.System.out;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.commons.services.CronService;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.dao.PlayerLunaShopDAO;
import com.aionemu.gameserver.dao.PlayerWardrobeDAO;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PlayerLunaShop;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.model.templates.luna.LunaConsumeRewardsTemplate;
import com.aionemu.gameserver.model.templates.recipe.LunaComponent;
import com.aionemu.gameserver.model.templates.recipe.LunaComponentElement;
import com.aionemu.gameserver.model.templates.recipe.LunaTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_LUNA_SYSTEM;
import com.aionemu.gameserver.network.aion.serverpackets.SM_LUNA_SYSTEM_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.instance.InstanceService;
import com.aionemu.gameserver.services.item.ItemPacketService;
import com.aionemu.gameserver.services.item.ItemPacketService.ItemUpdateType;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;

public class LunaShopService {

	private Logger log = LoggerFactory.getLogger(LunaShopService.class);
	PlayerWardrobeDAO wDAO = DAOManager.getDAO(PlayerWardrobeDAO.class);
	private boolean dailyGenerated = true;
//	private boolean specialGenerated = true;
	private boolean reciveBonus = false;
	private List<Integer> DailyCraft = new ArrayList<Integer>();
	private List<Integer> SpecialCraft = new ArrayList<Integer>();
//	private List<Integer> armors = new ArrayList<Integer>();
//	private List<Integer> pants = new ArrayList<Integer>();
//	private List<Integer> shoes = new ArrayList<Integer>();
//	private List<Integer> gloves = new ArrayList<Integer>();
//	private List<Integer> shoulders = new ArrayList<Integer>();
//	private List<Integer> weapons = new ArrayList<Integer>();

	public void init() {
		log.info("[LunaSystem] Luna Reset");
		String daily = "0 0 9 1/1 * ? *";
//		String weekly = "0 0 9 ? * WED *";
		if (DailyCraft.size() == 0) {
			generateDailyCraft();
		}
//		if (SpecialCraft.size() == 0) {
//			generateSpecialCraft();
//		}

		CronService.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				dailyGenerated = false;
				generateDailyCraft();
				resetFreeLuna();
			}
		}, daily);

//		CronService.getInstance().schedule(new Runnable() {
//
//			@Override
//			public void run() {
//				specialGenerated = false;
//				generateSpecialCraft();
//			}
//		}, weekly);
	}

//	public void generateSpecialCraft() {
//		if (SpecialCraft.size() > 0) {
//			SpecialCraft.clear();
//		}
//		armors.add(10029);
//		armors.add(10031);
//		armors.add(10033);
//		armors.add(10035);
//		pants.add(10037);
//		pants.add(10039);
//		pants.add(10041);
//		pants.add(10043);
//		shoes.add(10061);
//		shoes.add(10063);
//		shoes.add(10065);
//		shoes.add(10067);
//		gloves.add(10053);
//		gloves.add(10055);
//		gloves.add(10057);
//		gloves.add(10059);
//		shoulders.add(10045);
//		shoulders.add(10047);
//		shoulders.add(10049);
//		shoulders.add(10051);
//		weapons.add(10021);
//		weapons.add(10017);
//		weapons.add(10025);
//		weapons.add(10005);
//		weapons.add(10011);
//		weapons.add(10023);
//		weapons.add(10003);
//		weapons.add(10007);
//		weapons.add(10019);
//		weapons.add(10013);
//		weapons.add(10027);
//		weapons.add(10009);
//		weapons.add(10015);
//		weapons.add(10001);
//		int rnd = Rnd.get(1, 6);
//		switch (rnd) {
//			case 1:
//				SpecialCraft.addAll(weapons);
//				break;
//			case 2:
//				SpecialCraft.addAll(armors);
//				break;
//			case 3:
//				SpecialCraft.addAll(pants);
//				break;
//			case 4:
//				SpecialCraft.addAll(shoes);
//				break;
//			case 5:
//				SpecialCraft.addAll(gloves);
//				break;
//			case 6:
//				SpecialCraft.addAll(shoulders);
//				break;
//		}
//		if (!specialGenerated) {
//			updateSpecialCraft();
//		}
//	}

	public void resetFreeLuna() {
		DAOManager.getDAO(PlayerLunaShopDAO.class).delete();
		updateFreeLuna();
	}

	public void sendSpecialCraft(Player player) {
		PacketSendUtility.sendPacket(player, new SM_LUNA_SYSTEM_INFO(2, 0, SpecialCraft));
	}

//	private void updateSpecialCraft() {
//		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
//
//			@Override
//			public void visit(Player player) {
//				PacketSendUtility.sendPacket(player, new SM_LUNA_SYSTEM_INFO(2, 0, SpecialCraft));
//			}
//		});
//	}

	private void updateFreeLuna() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				PlayerLunaShop pls = new PlayerLunaShop(true, true, true);
				pls.setPersistentState(PersistentState.UPDATE_REQUIRED);
				player.setPlayerLunaShop(pls);
				DAOManager.getDAO(PlayerLunaShopDAO.class).store(player);
			}
		});
	}

	public void generateDailyCraft() {
		if (DailyCraft.size() > 0) {
			DailyCraft.clear();
		}
		for (int i = 0; i < 5; i++) {
			int templateId = Rnd.get(30000, 37016);
			DailyCraft.add(templateId);
		}
		if (!dailyGenerated) {
			updateDailyCraft();
		}
	}

	public void sendDailyCraft(Player player) {
		PacketSendUtility.sendPacket(player, new SM_LUNA_SYSTEM_INFO(2, 1, DailyCraft));
	}

	private void updateDailyCraft() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				PacketSendUtility.sendPacket(player, new SM_LUNA_SYSTEM_INFO(2, 1, DailyCraft));
				dailyGenerated = true;
			}
		});
	}

	public void lunaPointController(Player player, int point) {
		player.setLunaAccount(point);
		PacketSendUtility.sendPacket(player, new SM_LUNA_SYSTEM_INFO(0, player.getLunaAccount()));
	}

	public void muniKeysController(Player player, int keys) {
		player.setMuniKeys(keys);
		PacketSendUtility.sendPacket(player, new SM_LUNA_SYSTEM_INFO(4));
	}

	public void onLogin(Player player) {
		if (player.getPlayerLunaShop() == null) {
			PlayerLunaShop pls = new PlayerLunaShop(true, true, true);
			pls.setPersistentState(PersistentState.UPDATE_REQUIRED);
			player.setPlayerLunaShop(pls);
			DAOManager.getDAO(PlayerLunaShopDAO.class).add(player.getObjectId(), pls.isFreeUnderpath(), pls.isFreeFactory(), pls.isFreeChest());
		}

//		PacketSendUtility.sendPacket(player, new SM_LUNA_SYSTEM_INFO(6));
		PacketSendUtility.sendPacket(player, new SM_LUNA_SYSTEM_INFO(7));
		sendSpecialCraft(player);
		sendDailyCraft(player);
		for (int i = 0; i < 9; i++) {
			PacketSendUtility.sendPacket(player, new SM_LUNA_SYSTEM_INFO(8, i, 0));
		}
		PacketSendUtility.sendPacket(player, new SM_LUNA_SYSTEM_INFO(0, player.getLunaAccount()));
		PacketSendUtility.sendPacket(player, new SM_LUNA_SYSTEM_INFO(5));
		PacketSendUtility.sendPacket(player, new SM_LUNA_SYSTEM_INFO(4, player.getMuniKeys()));
		PacketSendUtility.sendPacket(player, new SM_LUNA_SYSTEM_INFO(9, 0));
		
		if (!player.getPlayerLunaShop().isFreeUnderpath()) {
			PacketSendUtility.sendPacket(player, new SM_LUNA_SYSTEM_INFO(1, 1, 45));
		}
		if (!player.getPlayerLunaShop().isFreeFactory()) {
			PacketSendUtility.sendPacket(player, new SM_LUNA_SYSTEM_INFO(1, 1, 47));
		}
	}

	public void specialDesign(Player player, int recipeId) {
		LunaTemplate recipe = DataManager.LUNA_DATA.getLunaTemplateById(recipeId + (int) player.getLunaAccount());
//		System.out.println("Recipe ID: " + recipe.getId());
		int product_id = recipe.getProductid();
//		System.out.println("Produkt ID: " + recipe.getProductid());
		int quantity = recipe.getQuantity();
//		System.out.println("Quantity : " + recipe.getQuantity());
		ItemTemplate item = DataManager.ITEM_DATA.getItemTemplate(product_id);
//		System.out.println("Item Id : " + item.getTemplateId());
		boolean isSuccess = isSuccess(player, recipeId);
		if (isSuccess) {
			for (LunaComponent lc : recipe.getLunaComponent()) {
				for (LunaComponentElement a : lc.getComponents()) {
					if (!player.getInventory().decreaseByItemId(a.getItemid(), a.getQuantity())) {
						System.out.println("!!! Possible item hack CHEATER(?) !!!");
						PacketSendUtility.sendPacket(player, new SM_LUNA_SYSTEM(2, item, 1));
						PacketSendUtility.sendPacket(player, new SM_LUNA_SYSTEM(3, product_id, quantity, false));
						return;
					}
				}
			}
			PacketSendUtility.sendPacket(player, new SM_LUNA_SYSTEM(2, item, 0));
			PacketSendUtility.sendPacket(player, new SM_LUNA_SYSTEM(3, product_id, quantity, true));
			ItemService.addItem(player, product_id, quantity);
		}
		else {
			for (LunaComponent lc : recipe.getLunaComponent()) {
				for (LunaComponentElement a : lc.getComponents()) {
					if (!player.getInventory().decreaseByItemId(a.getItemid(), a.getQuantity())) {
						System.out.println("!!! Possible item hack CHEATER(?) !!!");
						PacketSendUtility.sendPacket(player, new SM_LUNA_SYSTEM(2, item, 1));
						PacketSendUtility.sendPacket(player, new SM_LUNA_SYSTEM(3, product_id, quantity, false));
						return;
					}
				}
			}
			PacketSendUtility.sendPacket(player, new SM_LUNA_SYSTEM(2, item, 1));
			PacketSendUtility.sendPacket(player, new SM_LUNA_SYSTEM(3, product_id, quantity, false));
		}
	}

	public void craftBox(Player player) {
		int itemId = 188055460;
		ItemTemplate item = DataManager.ITEM_DATA.getItemTemplate(itemId);
		if (player.getPlayerLunaShop().isFreeChest()) {
			PacketSendUtility.sendPacket(player, new SM_LUNA_SYSTEM_INFO(5));
			PacketSendUtility.sendPacket(player, new SM_LUNA_SYSTEM(3, itemId, 1, true));
			PacketSendUtility.sendPacket(player, new SM_LUNA_SYSTEM_INFO(0));
			PacketSendUtility.sendPacket(player, new SM_LUNA_SYSTEM_INFO(1, 1, 1));
			ItemService.addItem(player, itemId, 1); // Luna Material Chest
			player.getPlayerLunaShop().setFreeChest(false);
		}
		else {
			out.println("TODO!");
			// player.setLunaAccount(player.getLunaAccount() - 5);
			PacketSendUtility.sendPacket(player, new SM_LUNA_SYSTEM(3, item, 0));
		}
	}

	private boolean isSuccess(Player player, int recipeId) {
		LunaTemplate recipe = DataManager.LUNA_DATA.getLunaTemplateById(recipeId);
		boolean result = false;
		float random = Rnd.get(1, 100);
		if (recipe.getRate() == 100) {
			result = true;
		}
		else if (recipe.getRate() < 100) {
			if (random <= recipe.getRate()) {
				result = true;
			}
			else {
				result = false;
			}
		}
		return result;
	}

	public void buyMaterials(Player player, int itemId, long count) {
		ItemTemplate itemTemplate = DataManager.ITEM_DATA.getItemTemplate(itemId);
		int lunaPrice = itemTemplate.getLunaPrice();
		long price = count * lunaPrice;
		ItemService.addItem(player, itemId, count);
		player.setLunaAccount((player.getLunaAccount() - price));
		PacketSendUtility.sendPacket(player, new SM_LUNA_SYSTEM_INFO(0, player.getLunaAccount()));
		PacketSendUtility.sendPacket(player, new SM_LUNA_SYSTEM(4, player.getMuniKeys()));
	}

	public void dorinerkWardrobeLoad(Player player) {
		int size = DAOManager.getDAO(PlayerWardrobeDAO.class).getItemSize(player.getObjectId());
		PacketSendUtility.sendPacket(player, new SM_LUNA_SYSTEM(8, player.getWardrobeSlot(), size));
	}

	public void dorinerkWardrobeAct(Player player, int applySlot, int itemObjId) {
		int itemId = player.getInventory().getItemByObjId(itemObjId).getItemId();
		int itemOnDB = DAOManager.getDAO(PlayerWardrobeDAO.class).getWardrobeItemBySlot(player.getObjectId(), applySlot);
		if (itemOnDB != 0) {
			DAOManager.getDAO(PlayerWardrobeDAO.class).delete(player.getObjectId(), itemOnDB);
			player.setLunaAccount(player.getLunaAccount() - 10);
			player.getWardrobe().addItem(player, itemId, applySlot, 0);
			PacketSendUtility.sendPacket(player, new SM_LUNA_SYSTEM_INFO(0, player.getLunaAccount()));
		}
		else {
			player.getWardrobe().addItem(player, itemId, applySlot, 0);
		}
		player.getInventory().decreaseByObjectId(itemObjId, 1);
		PacketSendUtility.sendPacket(player, new SM_LUNA_SYSTEM(10, 0x00, applySlot, itemId, 1));
	}

	public void dorinerkWardrobeModifyAppearance(Player player, int applySlot, int itemObjId) {
		int itemId = DAOManager.getDAO(PlayerWardrobeDAO.class).getWardrobeItemBySlot(player.getObjectId(), applySlot);
		int reskinCount = DAOManager.getDAO(PlayerWardrobeDAO.class).getReskinCountBySlot(player.getObjectId(), applySlot);
		ItemTemplate it = DataManager.ITEM_DATA.getItemTemplate(itemId);
		Storage inventory = player.getInventory();
		Item keepItem = inventory.getItemByObjId(itemObjId);
		if (reskinCount != 0) {
			DAOManager.getDAO(PlayerWardrobeDAO.class).setReskinCountBySlot(player.getObjectId(), applySlot, reskinCount + 1);
			player.setLunaAccount(player.getLunaAccount() - 15);
			keepItem.setItemSkinTemplate(it);
			if (!keepItem.getItemTemplate().isItemDyePermitted()) {
				keepItem.setItemColor(0);
			}
			keepItem.setLunaReskin(true);
			PacketSendUtility.sendPacket(player, new SM_LUNA_SYSTEM_INFO(0, player.getLunaAccount()));
		}
		else {
			DAOManager.getDAO(PlayerWardrobeDAO.class).setReskinCountBySlot(player.getObjectId(), applySlot, reskinCount + 1);
			keepItem.setItemSkinTemplate(it);
			if (!keepItem.getItemTemplate().isItemDyePermitted()) {
				keepItem.setItemColor(0);
			}
			keepItem.setLunaReskin(true);
		}
		ItemPacketService.updateItemAfterInfoChange(player, keepItem, ItemUpdateType.STATS_CHANGE);
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CHANGE_ITEM_SKIN_SUCCEED(new DescriptionId(keepItem.getItemTemplate().getNameId())));
		PacketSendUtility.sendPacket(player, new SM_LUNA_SYSTEM(11, applySlot));
	}

	public void dorinerkWardrobeExtendSlots(Player player) {
		int currentSlot = player.getWardrobeSlot();
		int size = DAOManager.getDAO(PlayerWardrobeDAO.class).getItemSize(player.getObjectId());
		player.setWardrobeSlot(currentSlot + 1);
		player.setLunaAccount(player.getLunaAccount() - wardrobePrice(currentSlot + 1));
		PacketSendUtility.sendPacket(player, new SM_LUNA_SYSTEM(9, player.getWardrobeSlot(), size));
		PacketSendUtility.sendPacket(player, new SM_LUNA_SYSTEM_INFO(0, player.getLunaAccount()));
		PacketSendUtility.sendPacket(player, new SM_LUNA_SYSTEM_INFO(5, player.getLunaAccount()));
		PacketSendUtility.sendPacket(player, new SM_LUNA_SYSTEM_INFO(4, player.getMuniKeys()));
	}

	public void takiAdventure(Player player, int indun_id) {
		PacketSendUtility.sendPacket(player, new SM_LUNA_SYSTEM(14, indun_id));
	}

	public void takiAdventureTeleport(Player player, int indun_unk, int indun_id) {
		if (indun_id == 1) {
			if (player.getPlayerLunaShop().isFreeUnderpath()) {
				WorldMapInstance contaminatedUnderpath = InstanceService.getNextAvailableInstance(301630000);
				InstanceService.registerPlayerWithInstance(contaminatedUnderpath, player);
				TeleportService2.teleportTo(player, 301630000, contaminatedUnderpath.getInstanceId(), 230f, 169f, 164f, (byte) 60);
				player.getPlayerLunaShop().setLunaShopByObjId(player.getObjectId());
				PacketSendUtility.sendPacket(player, new SM_LUNA_SYSTEM_INFO(0));
				PacketSendUtility.sendPacket(player, new SM_LUNA_SYSTEM_INFO(1, 1, 45));
				PacketSendUtility.sendPacket(player, new SM_LUNA_SYSTEM(0, 0));
				player.getPlayerLunaShop().setFreeUnderpath(false);
			}
			else {
				WorldMapInstance contaminatedUnderpath = InstanceService.getNextAvailableInstance(301630000);
				InstanceService.registerPlayerWithInstance(contaminatedUnderpath, player);
				TeleportService2.teleportTo(player, 301630000, contaminatedUnderpath.getInstanceId(), 230f, 169f, 164f, (byte) 60);
				player.setLunaAccount(player.getLunaAccount() - 89);
				PacketSendUtility.sendPacket(player, new SM_LUNA_SYSTEM_INFO(0));
				PacketSendUtility.sendPacket(player, new SM_LUNA_SYSTEM_INFO(1, 1, 45));
				PacketSendUtility.sendPacket(player, new SM_LUNA_SYSTEM(0, 0));
			}
		}
		if (indun_id == 2) {
			if (player.getPlayerLunaShop().isFreeFactory()) {
				WorldMapInstance secretMunitionsFactory = InstanceService.getNextAvailableInstance(301640000);
				InstanceService.registerPlayerWithInstance(secretMunitionsFactory, player);
				TeleportService2.teleportTo(player, 301640000, secretMunitionsFactory.getInstanceId(), 400.3279f, 290.5061f, 198.64015f, (byte) 60);
				player.getPlayerLunaShop().setLunaShopByObjId(player.getObjectId());
				PacketSendUtility.sendPacket(player, new SM_LUNA_SYSTEM_INFO(0));
				PacketSendUtility.sendPacket(player, new SM_LUNA_SYSTEM_INFO(1, 1, 47));
				PacketSendUtility.sendPacket(player, new SM_LUNA_SYSTEM(0, 0));
				player.getPlayerLunaShop().setFreeFactory(false);
			}
			else {
				WorldMapInstance secretMunitionsFactory = InstanceService.getNextAvailableInstance(301640000);
				InstanceService.registerPlayerWithInstance(secretMunitionsFactory, player);
				TeleportService2.teleportTo(player, 301640000, secretMunitionsFactory.getInstanceId(), 400.3279f, 290.5061f, 198.64015f, (byte) 60);
				player.setLunaAccount(player.getLunaAccount() - 59);
				PacketSendUtility.sendPacket(player, new SM_LUNA_SYSTEM_INFO(0));
				PacketSendUtility.sendPacket(player, new SM_LUNA_SYSTEM_INFO(1, 1, 47));
				PacketSendUtility.sendPacket(player, new SM_LUNA_SYSTEM(0, 0));
			}
		}
	}

	public void teleport(Player player, int action, int teleportId) {
		switch (action) {
			case 6:
				PacketSendUtility.sendMessage(player, "teleportId : " + teleportId);
				PacketSendUtility.sendPacket(player, new SM_LUNA_SYSTEM(6));
				break;
			case 7:
				PacketSendUtility.sendMessage(player, "teleportId : " + teleportId);
				PacketSendUtility.sendPacket(player, new SM_LUNA_SYSTEM(7));
				break;
		}
	}

	public void munirunerksTreasureChamber(final Player player) {
		HashMap<Integer, Long> hm = new HashMap<Integer, Long>();
		hm.put(188054633, (long) 1); // [Event] Special Head Executor Weapon Box
		hm.put(188054634, (long) 1); // [Event] Special Head Executor Armor Box
		hm.put(166030013, (long) 1); // [Event] Tempering Solution
		hm.put(166020003, (long) 1); // [Event] Omega Enchantment Stone
		hm.put(188054122, (long) 1); // Major Stigma Bundle
		hm.put(188055183, (long) 1); // Major Felicitous Socketing Box (Mythic)
		hm.put(188054287, (long) 1); // Greater Stigma Bundle
		hm.put(188054462, (long) 1); // Illusion Godstone Bundle
		hm.put(188052639, (long) 1); // [Event] Heroic Godstone Bundle
		hm.put(169405339, (long) 10); // Pallasite Crystal
		hm.put(164000076, (long) 10); // Greater Running Scroll
		hm.put(164000134, (long) 10); // Greater Awakening Scroll
		hm.put(166000196, (long) 3); // Enchantment Stone
		hm.put(186000242, (long) 2); // Ceramium Medal
		hm.put(186000051, (long) 2); // Major Ancient Crown
		hm.put(188055168, (long) 10); // [Event] Blood Medal Box
		hm.put(188054283, (long) 30); // Blood Mark Box
		hm.put(188054463, (long) 1); // [Event] Fabled Godstone Bundle
		hm.put(188053002, (long) 1); // [Event] Noble Composite Manastone Bundle
		hm.put(188100335, (long) 2000); // Enchantment Stone Dust
		hm.put(164000073, (long) 10); // Greater Courage Scroll
		hm.put(160002497, (long) 1); // Fresh Oily Plucar Dragon Salad
		hm.put(160002499, (long) 1); // Fresh Oily Plucar Dragon Soup

		if (player.getMuniKeys() > 0) {
			player.setMuniKeys(player.getMuniKeys() - 1);
		}
		else {
			player.setLunaAccount(player.getLunaAccount() - 19);
			player.setLunaConsumePoint(player.getLunaConsumePoint() + 25);
			switch (player.getLunaConsumePoint()) {
				case 25:
					reciveBonus = true;
					player.setLunaConsumeCount(1);
					break;
				case 50:
					reciveBonus = true;
					player.setLunaConsumeCount(2);
					break;
				case 100:
					reciveBonus = true;
					player.setLunaConsumeCount(3);
					muniKeysController(player, player.getMuniKeys() + 1);
					break;
				case 150:
					reciveBonus = true;
					player.setLunaConsumeCount(4);
					muniKeysController(player, player.getMuniKeys() + 1);
					break;
				case 300:
					reciveBonus = true;
					player.setLunaConsumeCount(5);
					muniKeysController(player, player.getMuniKeys() + 2);
					break;
				case 500:
					reciveBonus = true;
					player.setLunaConsumeCount(6);
					muniKeysController(player, player.getMuniKeys() + 2);
					break;
				case 1000:
					reciveBonus = true;
					player.setLunaConsumeCount(7);
					muniKeysController(player, player.getMuniKeys() + 3);
					break;
				default:
					reciveBonus = false;
					break;
			}
			if (reciveBonus) {
				LunaConsumeRewardsTemplate lt = DataManager.LUNA_CONSUME_REWARDS_DATA.getLunaConsumeRewardsId(player.getLunaConsumeCount());
				ItemService.addItem(player, lt.getCreateItemId(), lt.getCreateItemCount());
			}
		}

		final HashMap<Integer, Long> mt = new HashMap<Integer, Long>();
		for (int i = 0; i < 3; i++) {
			Object[] crunchifyKeys = hm.keySet().toArray();
			Object key = crunchifyKeys[new Random().nextInt(crunchifyKeys.length)];
			mt.put((int) key, (long) hm.get(key));
		}
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				for (Map.Entry<Integer, Long> e : mt.entrySet()) {
					ItemService.addItem(player, e.getKey(), e.getValue());
					ItemTemplate t = DataManager.ITEM_DATA.getItemTemplate(e.getKey());
					if (e.getValue() == 1) {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LUNA_REWARD_GOTCHA_ITEM(t.getNameId()));
					}
					else if (e.getValue() > 1) {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LUNA_REWARD_GOTCHA_ITEM_MULTI(e.getValue(), t.getNameId()));
					}
				}
				PacketSendUtility.sendPacket(player, new SM_LUNA_SYSTEM(mt));
			}
		}, 1);
		PacketSendUtility.sendPacket(player, new SM_LUNA_SYSTEM_INFO(5));
		PacketSendUtility.sendPacket(player, new SM_LUNA_SYSTEM_INFO(4, player.getMuniKeys()));
		PacketSendUtility.sendPacket(player, new SM_LUNA_SYSTEM_INFO(0, player.getLunaAccount()));
		// As you spend Luna, you can earn keys to open Munirunerks Treasure Chest.
		// If you do not have any keys, you can spend 3 Luna to open a chest immediately.
		// The Luna you spend on opening chests will also count towards your Luna Rewards!
	}

	public void onLogout(Player player) {
		PlayerLunaShop pls = player.getPlayerLunaShop();
		pls.setPersistentState(PersistentState.UPDATE_REQUIRED);
		DAOManager.getDAO(PlayerLunaShopDAO.class).store(player);
	}

	private int wardrobePrice(int WardrobeSlot) { // Done
		switch (WardrobeSlot) {
			case 1:
			case 2:
			case 3:
			case 4:
				return 69;
			case 5:
			case 6:
			case 7:
			case 8:
				return 99;
		}
		return 0;
	}
	
	public void diceGame(Player player) { // TODO Golden Dice + Golden Price fix..
		int random = Rnd.get(1, 1000);
		if (random >= 100 && random <= 400) {
			player.setLunaDiceGame(1, false);
		} else if (random >= 450 && random <= 749) {
			player.setLunaDiceGame(2, false);
		} else if (random >= 750 && random <= 849) {
			player.setLunaDiceGame(3, false);
		} else if (random >= 850 && random <= 900) {
			player.setLunaDiceGame(4, false);
		} else if (random >= 950 && random <= 1000) {
			player.setLunaDiceGame(5, false);
		}
		int diceTry = player.getLunaDiceGameTry();
		
		if (diceTry < 1) {
			player.setLunaDiceGameTry(player.getLunaDiceGameTry() + 1);
			player.setLunaConsumePoint(player.getLunaConsumePoint() + lunaDicePrice(diceTry));
			player.setLunaAccount((player.getLunaAccount() - lunaDicePrice(diceTry)));
			PacketSendUtility.sendPacket(player, new SM_LUNA_SYSTEM_INFO(5));
			PacketSendUtility.sendPacket(player, new SM_LUNA_SYSTEM_INFO(0));
			PacketSendUtility.sendPacket(player, new SM_LUNA_SYSTEM_INFO(1, 1, 78));
			PacketSendUtility.sendPacket(player, new SM_LUNA_SYSTEM(15));
		} else {
			player.setLunaDiceGameTry(player.getLunaDiceGameTry() + 1);
			player.setLunaConsumePoint(player.getLunaConsumePoint() + lunaDicePrice(diceTry));
			player.setLunaAccount((player.getLunaAccount() - lunaDicePrice(diceTry)));
			PacketSendUtility.sendPacket(player, new SM_LUNA_SYSTEM_INFO(0));
			PacketSendUtility.sendPacket(player, new SM_LUNA_SYSTEM_INFO(1, 1, 79));
			PacketSendUtility.sendPacket(player, new SM_LUNA_SYSTEM(15));
			PacketSendUtility.sendPacket(player, new SM_LUNA_SYSTEM_INFO(5));
			PacketSendUtility.sendPacket(player, new SM_LUNA_SYSTEM_INFO(4));
		}
		System.out.println("Random: " + random);
		System.out.println("Try: " + player.getLunaDiceGameTry());
		System.out.println("Consum: " + player.getLunaConsumePoint());
	}
	
	public void diceGameReward(Player player) { //TODO
		ItemService.addItem(player, 162001014, 4);
		PacketSendUtility.sendPacket(player, new SM_LUNA_SYSTEM(16, 162001014, 4));
		player.setLunaDiceGame(0, true);
		player.setLunaDiceGameTry(0);
		PacketSendUtility.sendPacket(player, new SM_LUNA_SYSTEM_INFO(1, 1, 78));
	}
	
	public int lunaDicePrice(int diceTry) { // Done
		switch (diceTry) {
			case 0:
				return 20;
			case 1:
				return 22;
			case 2:
				return 24;
			case 3:
				return 26;
			case 4:
			case 5:
				return 30;
			case 6:
				return 32;
			case 7:
				return 36;
			case 8:
			case 9:
				return 38;
			case 10:
				return 40;
			default:
				return 40;
		}
	}

	public static LunaShopService getInstance() {
		return NewSingletonHolder.INSTANCE;
	}

	private static class NewSingletonHolder {

		private static final LunaShopService INSTANCE = new LunaShopService();
	}
}
