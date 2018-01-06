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

import javax.xml.bind.annotation.XmlAttribute;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * Created by Alex on 23.12.2014.
 */
public class ExpAction extends AbstractItemAction {

	@XmlAttribute
	protected int cost;

	@XmlAttribute(name = "percent")
	protected boolean isPercent;

	@Override
	public boolean canAct(Player player, Item parentItem, Item targetItem) {
		return cost > 0;
	}

	@Override
	public void act(Player player, Item parentItem, Item targetItem) {
		long exp = player.getCommonData().getExpNeed();
		long expPercent = isPercent ? (exp * cost / 100) : cost;
		if (player.getInventory().decreaseByObjectId(parentItem.getObjectId(), 1)) {
			player.getCommonData().setExp(player.getCommonData().getExp() + expPercent);
			player.getObserveController().notifyItemuseObservers(parentItem);
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_GET_EXP2(expPercent));
			ItemTemplate itemTemplate = parentItem.getItemTemplate();
			PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), parentItem.getObjectId(), itemTemplate.getTemplateId()), true);
			// PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_USE_ITEM(new DescriptionId(itemTemplate.getNameId())));
		}
	}
}
