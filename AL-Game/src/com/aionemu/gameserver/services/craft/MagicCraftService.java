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
package com.aionemu.gameserver.services.craft;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.configs.main.LoggingConfig;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.StaticObject;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.RewardType;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.model.templates.recipe.Component;
import com.aionemu.gameserver.model.templates.recipe.RecipeTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.services.item.ItemService.ItemUpdatePredicate;
import com.aionemu.gameserver.skillengine.task.MagicCraftTask;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Falke_34, FrozenKiller
 */

// ExP & Find a good Crit rate

public class MagicCraftService {

	private static final Logger log = LoggerFactory.getLogger("MAGIC_CRAFT_LOG");

	public static void startMagicCraft(Player player, int recipeId, int craftType) {

		RecipeTemplate recipeTemplate = DataManager.RECIPE_DATA.getRecipeTemplateById(recipeId);
		VisibleObject target = player.getKnownList().getObject(player.getObjectId());
		ItemTemplate itemTemplate = DataManager.ITEM_DATA.getItemTemplate(recipeTemplate.getProductid());

		if (!checkMagicCraft(player, recipeTemplate, itemTemplate)) {
			sendCancelMagicCraft(player);
			return;
		}
		player.setCraftingTask(new MagicCraftTask(player, (StaticObject) target, recipeTemplate));
		player.getCraftingTask().start();
	}

	private static boolean checkMagicCraft(Player player, RecipeTemplate recipeTemplate, ItemTemplate itemTemplate) {

		if (recipeTemplate == null) {
			return false;
		}
		if (itemTemplate == null) {
			return false;
		}
		if (player.getCraftingTask() != null && player.getCraftingTask().isInProgress()) {
			return false;
		}
		return true;
	}

	public static void finishMagicCrafting(final Player player, RecipeTemplate recipetemplate, int critCount, int bonus) {
		int xpReward = ((2 * (recipetemplate.getSkillpoint() + 100) * (recipetemplate.getSkillpoint() + 100) + 60)); // should be more or less Exp ?
		xpReward = xpReward + (xpReward * bonus / 100); // bonus

		if (player.getInventory().getFreeSlots() == 0) {
			sendCancelMagicCraft(player);
			return;
		}

		for (Component items : recipetemplate.getComponent()) {
			player.getInventory().decreaseByItemId(items.getItemid(), items.getQuantity());
		}
		int critVal = (Rnd.get(10000));

		int productItemId = (recipetemplate.getComboProductSize() > 0 && critVal > 9800) ? recipetemplate.getComboProduct(1) : recipetemplate.getProductid();

		ItemService.addItem(player, productItemId, recipetemplate.getQuantity(), new ItemUpdatePredicate() {

			@Override
			public boolean changeItem(Item item) {
				if (item.getItemTemplate().isWeapon() || item.getItemTemplate().isArmor()) {
					item.setItemCreator(player.getName());
				}
				return true;
			}
		});

		ItemTemplate itemTemplate = DataManager.ITEM_DATA.getItemTemplate(productItemId);
		if (LoggingConfig.LOG_CRAFT) {
			log.info(((recipetemplate.getComboProductSize() != null && critVal > 9800) ? "[CRAFT][Critical] ID/Count" : "[MAGIC_CRAFT][Normal] Added ID/Count") + (LoggingConfig.ENABLE_ADVANCED_LOGGING ? "/Item Name - " + productItemId + "/" + recipetemplate.getQuantity() + "/" + itemTemplate.getName() : " - " + productItemId + "/" + recipetemplate.getQuantity()) + " to player: " + player.getName());
		}

		int gainedCraftExp = (int) RewardType.CRAFTING.calcReward(player, xpReward);

		if (player.getSkillList().addSkillXp(player, recipetemplate.getSkillid(), gainedCraftExp, recipetemplate.getSkillpoint())) {
			player.getCommonData().addExp(xpReward, RewardType.CRAFTING);
		}
		else {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_DONT_GET_PRODUCTION_EXP(new DescriptionId(DataManager.SKILL_DATA.getSkillTemplate(recipetemplate.getSkillid()).getNameId())));
		}
	}

	public static void sendCancelMagicCraft(Player player) {
		if (player.getCraftingTask().isInProgress()) {
			player.getCraftingTask().abort();
		}
	}
}
