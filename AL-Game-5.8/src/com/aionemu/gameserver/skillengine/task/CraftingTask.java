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
package com.aionemu.gameserver.skillengine.task;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.configs.main.CraftConfig;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.StaticObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.house.House;
import com.aionemu.gameserver.model.templates.item.ItemCategory;
import com.aionemu.gameserver.model.templates.item.ItemQuality;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.model.templates.recipe.RecipeTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_CRAFT_ANIMATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_CRAFT_UPDATE;
import com.aionemu.gameserver.services.craft.CraftService;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Mr. Poke
 * @author synchro2
 * @author Antraxx
 * @author Kamikaze
 * @Reworked Kill3r
 */
public class CraftingTask extends AbstractCraftTask {

	protected RecipeTemplate recipeTemplate;
	protected ItemTemplate itemTemplate;
	protected ItemTemplate itemTemplateReal;
	protected int critCount;
	protected boolean crit = false;
	protected boolean purpleCrit = false;
	protected int maxCritCount;
	private int bonus;

	/**
	 * Constructor
	 *
	 * @param requestor
	 * @param responder
	 * @param recipeTemplate
	 * @param skillLvlDiff
	 * @param bonus
	 */
	public CraftingTask(Player requestor, StaticObject responder, RecipeTemplate recipeTemplate, int skillLvlDiff, int bonus) {
		super(requestor, responder, skillLvlDiff);
		this.recipeTemplate = recipeTemplate;
		this.maxCritCount = recipeTemplate.getComboProductSize();
		this.bonus = bonus;
	}

	private void craftSetup() {
		this.itemQuality = this.itemTemplateReal.getItemQuality();
		// if (this.itemTemplateReal.getCategory() == ItemCategory.QUEST) {
		// this.itemQuality = ItemQuality.COMMON;
		// }
		currentSuccessValue = 0;
		currentFailureValue = 0;
		maxSuccessValue = (int) Math.round((this.itemQuality.getQualityId() + 3) * 3.5) * 5;
		maxFailureValue = (int) Math.round((this.itemQuality.getQualityId() + 3) * 5.25) * 5;
	}

	/**
	 * Perform interaction calculation
	 */
	@Override
	protected void analyzeInteraction() {
		int critVal = Rnd.get(55000) / (skillLvlDiff + 1);
		if (critVal < CraftConfig.CRAFT_CHANCE_BLUECRIT) {
			critType = CraftCritType.BLUE;
		}
		else if ((critVal < CraftConfig.CRAFT_CHANCE_INSTANT) && (this.itemQuality.getQualityId() < ItemQuality.EPIC.getQualityId())) {
			critType = CraftCritType.INSTANT;
			currentSuccessValue = maxSuccessValue;
			return;
		}

		if (CraftConfig.CRAFT_CHECKTASK) {
			if (this.task == null) {
				return;
			}
		}

		double mod = Math.sqrt((double) skillLvlDiff / 450f) * 100f + Rnd.nextGaussian() * 10f;
		mod -= (double) this.itemQuality.getQualityId() / 2;
		if (mod < 0) {
			currentFailureValue -= (int) mod;
		}
		else {
			currentSuccessValue += (int) mod;
		}

		if (currentSuccessValue >= maxSuccessValue) {
			currentSuccessValue = maxSuccessValue;
		}
		else if (currentFailureValue >= maxFailureValue) {
			currentFailureValue = maxFailureValue;
		}
	}

	@Override
	protected void onFailureFinish() {
		PacketSendUtility.sendPacket(requestor, new SM_CRAFT_UPDATE(recipeTemplate.getSkillid(), itemTemplate, currentSuccessValue, currentFailureValue, 6));
		PacketSendUtility.broadcastPacket(requestor, new SM_CRAFT_ANIMATION(requestor.getObjectId(), responder.getObjectId(), 0, 3), true);
	}

	@Override
	protected boolean onSuccessFinish() {
		if (this.checkCrit() && recipeTemplate.getComboProduct(critCount) != null) {
			if (purpleCrit) {
				critCount++;
			}
			craftSetup();
			PacketSendUtility.sendPacket(requestor, new SM_CRAFT_UPDATE(recipeTemplate.getSkillid(), itemTemplateReal, maxSuccessValue, maxFailureValue, 3));
			return false;
		}
		else {
			if (critCount > 0 && this.checkCrit()) {
				PacketSendUtility.broadcastPacket(requestor, new SM_CRAFT_ANIMATION(requestor.getObjectId(), responder.getObjectId(), 0, 2), true);
				PacketSendUtility.sendPacket(requestor, new SM_CRAFT_UPDATE(recipeTemplate.getSkillid(), itemTemplateReal, currentSuccessValue, currentFailureValue, 5));
				CraftService.finishCrafting(requestor, recipeTemplate, critCount, bonus);
				return true;
			}
			PacketSendUtility.broadcastPacket(requestor, new SM_CRAFT_ANIMATION(requestor.getObjectId(), responder.getObjectId(), 0, 2), true);
			PacketSendUtility.sendPacket(requestor, new SM_CRAFT_UPDATE(recipeTemplate.getSkillid(), itemTemplateReal, currentSuccessValue, currentFailureValue, 5));
			CraftService.finishCrafting(requestor, recipeTemplate, critCount, bonus);
			return true;
		}
	}

	@Override
	protected void sendInteractionUpdate() {
		PacketSendUtility.sendPacket(requestor, new SM_CRAFT_UPDATE(recipeTemplate.getSkillid(), itemTemplate, currentSuccessValue, currentFailureValue, this.critType.getPacketId()));
		if (this.critType == CraftCritType.PURPLE) {
			this.critType = CraftCritType.NONE;
		}
	}

	@Override
	protected void onInteractionAbort() {
		PacketSendUtility.sendPacket(requestor, new SM_CRAFT_UPDATE(recipeTemplate.getSkillid(), itemTemplate, 0, 0, 4));
		PacketSendUtility.broadcastPacket(requestor, new SM_CRAFT_ANIMATION(requestor.getObjectId(), responder.getObjectId(), 0, 2), true);
		requestor.setCraftingTask(null);
	}

	@Override
	protected void onInteractionFinish() {
		requestor.setCraftingTask(null);
	}

	@Override
	protected void onInteractionStart() {
		this.itemTemplate = DataManager.ITEM_DATA.getItemTemplate(recipeTemplate.getProductid());
		this.itemTemplateReal = this.itemTemplate;
		craftSetup();

		if ((this.recipeTemplate.getMaxProductionCount() != null) && (this.itemTemplateReal.getCategory() == ItemCategory.QUEST)) {
			this.requestor.getRecipeList().deleteRecipe(this.requestor, this.recipeTemplate.getId());
		}

		int chance = requestor.getRates().getCraftCritRate();
		if (maxCritCount > 0) {
			if (critCount > 0 && maxCritCount > 1) {
				chance = requestor.getRates().getComboCritRate();
				House house = requestor.getActiveHouse();
				if (house != null) {
					switch (house.getHouseType()) {
						case ESTATE:
						case MANSION:
						case HOUSE:
						case STUDIO:
						case PALACE:
							chance += 5;
							break;
						default:
							break;
					}
				}
			}
			if ((critCount < maxCritCount) && (Rnd.get(100) < chance)) {
				critCount++;
				crit = true;
			}
			// Only Double Proc happens , if maxCritCount is 2.
			if ((critCount > 0 && critCount <= maxCritCount && maxCritCount != 1) && (Rnd.get(100) < chance)) {
				purpleCrit = true;
			}
		}

		PacketSendUtility.sendPacket(requestor, new SM_CRAFT_UPDATE(recipeTemplate.getSkillid(), itemTemplate, maxSuccessValue, maxFailureValue, 0));
		this.onInteraction();
		PacketSendUtility.broadcastPacket(requestor, new SM_CRAFT_ANIMATION(requestor.getObjectId(), responder.getObjectId(), recipeTemplate.getSkillid(), 0), true);
		PacketSendUtility.broadcastPacket(requestor, new SM_CRAFT_ANIMATION(requestor.getObjectId(), responder.getObjectId(), recipeTemplate.getSkillid(), 1), true);
	}

	@Override
	protected boolean onInteraction() {
		if (currentSuccessValue == maxSuccessValue) {
			return onSuccessFinish();
		}
		if (currentFailureValue == maxFailureValue) {
			onFailureFinish();
			return true;
		}
		analyzeInteraction();
		sendInteractionUpdate();
		return false;
	}

	private boolean checkCrit() {
		if (crit) {
			crit = false;
			this.itemTemplateReal = DataManager.ITEM_DATA.getItemTemplate(recipeTemplate.getComboProduct(critCount));
			return true;
		}
		if (purpleCrit) {
			purpleCrit = false;
			this.itemTemplateReal = DataManager.ITEM_DATA.getItemTemplate(recipeTemplate.getComboProduct(critCount));
			return true;
		}
		return false;
	}
}
