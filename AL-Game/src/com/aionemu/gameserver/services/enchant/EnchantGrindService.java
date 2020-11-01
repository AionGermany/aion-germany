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
package com.aionemu.gameserver.services.enchant;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.item.ItemEnchantChance;
import com.aionemu.gameserver.model.templates.item.ItemEnchantChanceList;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.item.ItemPacketService;
import com.aionemu.gameserver.utils.PacketSendUtility;

public class EnchantGrindService {

    public static boolean enchantGrindItem(Player player, Item parentItem, Item targetItem) {
        boolean result = false;
        float random = (float)Rnd.get((int)1, (int)1000) / 10.0f;
        int chanceId = 21;
        ItemEnchantChance eItem = DataManager.ITEM_ENCHANT_CHANCES_DATA.getChanceById(chanceId);
        ItemEnchantChanceList eData = eItem.getChancesById(targetItem.getEnchantOrAuthorizeLevel());
        result = player.isGM() ? true : random <= (float)eData.getChance();
        return result;
    }

    public static void enchantGrindItemAct(Player player, Item parentItem, Item targetItem, int currentEnchant, boolean result) {
        int EnchantKinah = EnchantService.EnchantKinah(targetItem);
        currentEnchant = targetItem.getEnchantOrAuthorizeLevel();
        if (!player.getInventory().decreaseByObjectId(parentItem.getObjectId(), 1L)) {
            return;
        }
        if (player.getInventory().getKinah() >= (long)EnchantKinah) {
            player.getInventory().decreaseKinah(EnchantKinah);
        }
        if (player.getInventory().getKinah() < (long)EnchantKinah) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_NOT_ENOUGH_MONEY);
            return;
        }
        if (result) {
            ++currentEnchant;
        } 
        else {
            targetItem.setContaminated(true);
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_ITEM_ENCHANT_GRIND_CANT_STATUS(new DescriptionId(targetItem.getNameId())));
        }
        targetItem.setEnchantOrAuthorizeLevel(currentEnchant);
        if (targetItem.isEquipped()) {
            player.getGameStats().updateStatsVisually();
        }
        ItemPacketService.updateItemAfterInfoChange(player, targetItem, ItemPacketService.ItemUpdateType.STATS_CHANGE);
        if (targetItem.isEquipped()) {
            player.getEquipment().setPersistentState(PersistentState.UPDATE_REQUIRED);
        } 
        else {
            player.getInventory().setPersistentState(PersistentState.UPDATE_REQUIRED);
        }
        if (result) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_ITEM_ENCHANT_GRIND_SUCCEEDED(new DescriptionId(targetItem.getNameId()), targetItem.getEnchantOrAuthorizeLevel()));
        } 
        else {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_ITEM_ENCHANT_GRIND_FAIL(new DescriptionId(targetItem.getNameId())));
        }
    }
}
