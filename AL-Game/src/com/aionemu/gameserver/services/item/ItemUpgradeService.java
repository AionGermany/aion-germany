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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.item.upgrade.ItemUpgradeTemplate;
import com.aionemu.gameserver.model.templates.item.upgrade.SubMaterialItem;
import com.aionemu.gameserver.model.templates.item.upgrade.UpgradeResultItem;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.abyss.AbyssPointsService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.audit.AuditLogger;

import javolution.util.FastMap;

/**
 * @author Ranastic
 */
public class ItemUpgradeService {

	private static final Logger log = LoggerFactory.getLogger(ItemUpgradeService.class);

	public static boolean decreaseMaterial(Player player, Item baseItem, int resultItemId) {
		FastMap<Integer, UpgradeResultItem> resultItemMap = DataManager.ITEM_UPGRADE_DATA.getResultItemMap(baseItem.getItemId());

		UpgradeResultItem resultItem = resultItemMap.get(resultItemId);
		for (SubMaterialItem item : resultItem.getUpgrade_materials().getSubMaterialItem()) {
			if (!player.getInventory().decreaseByItemId(item.getId(), item.getCount())) {
				AuditLogger.info(player, "try item upgrade without sub material");
				return false;
			}
		}

		if (resultItem.getNeed_abyss_point() != null) {
			AbyssPointsService.setAp(player, -resultItem.getNeed_abyss_point().getCount());
		}

		if (resultItem.getNeed_kinah() != null) {
			player.getInventory().decreaseKinah(-resultItem.getNeed_kinah().getCount());
		}

		player.getInventory().decreaseByObjectId(baseItem.getObjectId(), 1);

		return true;
	}

	public static boolean checkItemUpgrade(Player player, Item baseItem, int resultItemId) {
		ItemUpgradeTemplate itemUpgradeTemplate = DataManager.ITEM_UPGRADE_DATA.getItemUpgradeTemplate(baseItem.getItemId());
		if (itemUpgradeTemplate == null) {
			log.warn(resultItemId + " item's upgrade template is null");
			return false;
		}

		FastMap<Integer, UpgradeResultItem> resultItemMap = DataManager.ITEM_UPGRADE_DATA.getResultItemMap(baseItem.getItemId());

		if (!resultItemMap.containsKey(resultItemId)) {
			AuditLogger.info(player, resultItemId + " item's baseItem and resultItem is not matched (possible client modify)");
			return false;
		}

		UpgradeResultItem resultItem = resultItemMap.get(resultItemId);
		Item resultItemName = ItemService.newItem(resultItemId, 1, null, 0, 0, 0);

		if (baseItem.getEnchantLevel() < resultItem.getCheck_enchant_count()) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REGISTER_ITEM_MSG_UPGRADE_CANNOT(new DescriptionId(baseItem.getNameId())));
			return false;
		}

		for (SubMaterialItem sub : resultItem.getUpgrade_materials().getSubMaterialItem()) {
			if (player.getInventory().getItemCountByItemId(sub.getId()) < sub.getCount()) {
				// sub Metarial is not enough
				return false;
			}
		}

		if (resultItem.getNeed_abyss_point() != null) {
			if (player.getAbyssRank().getAp() < resultItem.getNeed_abyss_point().getCount()) {
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REGISTER_ITEM_MSG_UPGRADE_CANNOT_NEED_AP);
				return false;
			}
		}
		if (resultItem.getNeed_kinah() != null) {
			if (player.getInventory().getKinah() < resultItem.getNeed_kinah().getCount()) {
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REGISTER_ITEM_MSG_UPGRADE_CANNOT_NEED_QINA);
				return false;
			}
		}
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_ITEM_UPGRADE_MSG_UPGRADE_SUCCESS(new DescriptionId(baseItem.getNameId()), new DescriptionId(resultItemName.getNameId())));
		ItemService.releaseItemId(resultItemName);
		return true;
	}
}
