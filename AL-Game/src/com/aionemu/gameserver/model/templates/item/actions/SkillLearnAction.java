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
package com.aionemu.gameserver.model.templates.item.actions;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.model.PlayerClass;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.services.SkillLearnService;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SkillLearnAction")
public class SkillLearnAction extends AbstractItemAction {

	@XmlAttribute
	protected int skillid;
	@XmlAttribute
	protected int level;
	@XmlAttribute(name = "class")
	protected PlayerClass playerClass;

	@Override
	public boolean canAct(Player player, Item parentItem, Item targetItem) {
		// 1. check player level
		if (player.getCommonData().getLevel() < level) {
			return false;
		}

		PlayerClass pc = player.getCommonData().getPlayerClass();
		if (!validateClass(pc)) {
			return false;
		}

		// 4. check player race and Race.PC_ALL
		Race race = parentItem.getItemTemplate().getRace();
		if (player.getRace() != race && race != Race.PC_ALL) {
			return false;
		}
		// 5. check whether this skill is already learned
		if (player.getSkillList().isSkillPresent(skillid)) {
			return false;
		}

		return true;
	}

	@Override
	public void act(Player player, Item parentItem, Item targetItem) {
		// item animation and message
		ItemTemplate itemTemplate = parentItem.getItemTemplate();
		// PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.USE_ITEM(itemTemplate.getDescription()));
		player.getController().cancelUseItem();
		PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), parentItem.getObjectId(), itemTemplate.getTemplateId()), true);

		// add skill
		SkillLearnService.learnSkillBook(player, skillid);

		// remove book from inventory (assuming its not stackable)
		Item item = player.getInventory().getItemByObjId(parentItem.getObjectId());
		player.getInventory().delete(item);
	}

	private boolean validateClass(PlayerClass pc) {
		boolean result = false;
		// 2. check if current class is second class and book is for starting class
		if (!pc.isStartingClass() && PlayerClass.getStartingClassFor(pc).ordinal() == playerClass.ordinal()) {
			result = true;
		}
		// 3. check player class and SkillClass.ALL
		if (pc == playerClass || playerClass == PlayerClass.ALL) {
			result = true;
		}

		return result;
	}
}
