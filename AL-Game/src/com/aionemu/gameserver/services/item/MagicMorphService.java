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
package com.aionemu.gameserver.services.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.controllers.observer.ItemUseObserver;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MAGIC_MORPH_RESULT;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Falke34, FrozenKiller
 */

public class MagicMorphService {

	public static void startMagicMorph(final Player player, final int upgradedItemObjectId, final List<Integer> ItemsList) {

		final Item firstItem = player.getInventory().getItemByObjId(upgradedItemObjectId);
		final List<Integer> list = new ArrayList<Integer>();
		int ItemsCount = 0;

		if (firstItem == null) {
			return;
		}

		/*
		 * Check if item is in bag
		 */
		for (Integer ItemObjId : ItemsList) {
			if (player.getInventory().getItemByObjId(ItemObjId).getItemCount() > 0) {
				ItemsCount++;
			}
		}

		if (ItemsCount < ItemsList.size()) {
			return;
		}

		/*
		 * Get a random Item
		 */
		for (ItemTemplate template : DataManager.ITEM_DATA.getItemData().valueCollection()) {
			if (template.getCategory() == firstItem.getItemTemplate().getCategory()) {
				if (template.getLevel() > 65) {
					if (!template.getName().contains("test_") && !template.getName().contains("Test_") && !template.getName().contains("5.0_") && !template.getName().contains("5.0 ") && !template.getName().contains("Test ") && !template.getName().contains("test ") && !template.getName().contains("You_") && !template.getName().contains("NPC ")) {
						switch (ItemsList.size()) {
							case 1:
								if (template.getArmorType() == firstItem.getItemTemplate().getArmorType()) {
									if (template.getLevel() <= Rnd.get(66, 68)) {
										list.add(template.getTemplateId());
									}
								}
								break;
							case 2:
								if (template.getArmorType() == firstItem.getItemTemplate().getArmorType()) {
									if (template.getLevel() <= Rnd.get(67, 69)) {
										list.add(template.getTemplateId());
									}
								}
								break;
							case 3:
								if (template.getArmorType() == firstItem.getItemTemplate().getArmorType()) {
									if (template.getLevel() <= Rnd.get(68, 70)) {
										list.add(template.getTemplateId());
									}
								}
							case 4:
								if (template.getItemQuality() == firstItem.getItemTemplate().getItemQuality()) {
									if (template.getArmorType() == firstItem.getItemTemplate().getArmorType()) {
										if (template.getLevel() <= Rnd.get(70, 72)) {
											list.add(template.getTemplateId());
										}
									}
								}
								break;
							case 5:
								if (template.getItemQuality() == firstItem.getItemTemplate().getItemQuality()) {
									if (template.getArmorType() == firstItem.getItemTemplate().getArmorType()) {
										if (template.getLevel() <= Rnd.get(71, 73)) {
											list.add(template.getTemplateId());
										}
									}
								}
								break;
							case 6:
								if (template.getItemQuality() == firstItem.getItemTemplate().getItemQuality()) {
									if (template.getArmorType() == firstItem.getItemTemplate().getArmorType()) {
										if (template.getLevel() <= Rnd.get(72, 74)) {
											list.add(template.getTemplateId());
										}
									}
								}
								break;
						}
					}
				}
			}
		}

		PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), 0, upgradedItemObjectId, firstItem.getItemId(), 4000, 23), true);
		final ItemUseObserver observer = new ItemUseObserver() {

			@Override
			public void abort() {
				player.getController().cancelTask(TaskId.ITEM_USE);
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1403619)); // Magic Morph cancel
				PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), 0, upgradedItemObjectId, firstItem.getItemId(), 0, 25), true);
				player.getObserveController().removeObserver(this);
				ItemsList.clear();
				list.clear();
			}
		};

		player.getObserveController().attach(observer);
		player.getController().addTask(TaskId.ITEM_USE, ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				final int random = list.get(new Random().nextInt(list.size()));
				final int description = DataManager.ITEM_DATA.getItemTemplate(random).getNameId();
				for (Integer ItemObjId : ItemsList) {
					player.getInventory().decreaseByObjectId(ItemObjId, 1);
				}
				player.getInventory().decreaseByObjectId(upgradedItemObjectId, 1);
				player.getObserveController().removeObserver(observer);
				PacketSendUtility.sendPacket(player, new SM_MAGIC_MORPH_RESULT(firstItem.getItemId(), firstItem.getObjectId()));
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1403620, new DescriptionId(description)));
				PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), 0, upgradedItemObjectId, firstItem.getItemId(), 0, 24), true);
				ItemService.addItem(player, random, 1);
				ItemsList.clear();
				list.clear();
			}
		}, 4000));
	}

	public static MagicMorphService getInstance() {
		return NewSingletonHolder.INSTANCE;
	}

	private static class NewSingletonHolder {

		private static final MagicMorphService INSTANCE = new MagicMorphService();
	}
}
