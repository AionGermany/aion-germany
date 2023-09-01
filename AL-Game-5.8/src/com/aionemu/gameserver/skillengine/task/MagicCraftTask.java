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

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.StaticObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.recipe.RecipeTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MAGIC_CRAFT;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MAGIC_CRAFT_ANIMATION;
import com.aionemu.gameserver.services.craft.MagicCraftService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/**
 * @author FrozenKiller
 */

public class MagicCraftTask extends CraftingTask {

	/**
	 * MagicCraftTask
	 *
	 * @param requestor
	 * @param responder
	 * @param recipeTemplates
	 */
	public MagicCraftTask(Player requestor, StaticObject responder, RecipeTemplate recipeTemplates) {
		super(requestor, responder, recipeTemplates, 0, 0);
		this.maxSuccessValue = 2000000;
		this.maxFailureValue = 2000000;
	}

	@Override
	public void start() {
		onInteractionStart();
		task = ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (!validateParticipants()) {
					stop(true);
				}
				boolean stopTask = onSuccessFinish();
				if (stopTask) {
					stop(false);
				}
			}
		}, 4000);
	}

	@Override
	protected void analyzeInteraction() {
	}

	@Override
	protected boolean onSuccessFinish() {
		PacketSendUtility.sendPacket(requestor, new SM_MAGIC_CRAFT_ANIMATION(requestor.getObjectId(), 2));
		PacketSendUtility.sendPacket(requestor, new SM_MAGIC_CRAFT(2, recipeTemplate));
		MagicCraftService.finishMagicCrafting(requestor, recipeTemplate, 0, 0);
		return true;
	}

	@Override
	protected void onInteractionFinish() {
		requestor.setCraftingTask(null);
	}

	@Override
	protected void sendInteractionUpdate() {
	}

	@Override
	protected void onInteractionStart() {
		this.itemTemplate = DataManager.ITEM_DATA.getItemTemplate(recipeTemplate.getProductid());
		PacketSendUtility.sendPacket(requestor, new SM_MAGIC_CRAFT(0, recipeTemplate));
		PacketSendUtility.sendPacket(requestor, new SM_MAGIC_CRAFT_ANIMATION(requestor.getObjectId(), 0));
	}

	@Override
	protected void onInteractionAbort() {
		PacketSendUtility.sendPacket(requestor, new SM_MAGIC_CRAFT_ANIMATION(requestor.getObjectId(), 1));
		PacketSendUtility.sendPacket(requestor, new SM_MAGIC_CRAFT(1, 0));
		requestor.setCraftingTask(null);
	}
}
