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

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.fame.PlayerFame;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAYER_FAME;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.player.PlayerFameService;
import com.aionemu.gameserver.utils.PacketSendUtility;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name= "FameAddExpAction")
public class FameAddExpAction extends AbstractItemAction {

    @XmlAttribute(name= "value")
    protected Integer value;

    public FameAddExpAction(Integer value) {
        this.value = value;
    }

    public Integer getRate() {
        return value;
    }

    public void setRate(Integer value) {
        this.value = value;
    }

    public FameAddExpAction() {
    }

    @Override
    public boolean canAct(Player player, Item parentItem, Item targetItem) {
        if (parentItem == null) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_ITEM_COLOR_ERROR);
            return false;
        }
        for (PlayerFame playerFieldFame : player.getPlayerFame().values()) {
            if (player.getWorldId() != playerFieldFame.getFameEnum().getWorldId() || playerFieldFame.getLevel() != 9) continue;
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_POPUP_ADDFEXP_USE_ITEM_FULL);
            return false;
        }
        return true;
    }

    @Override
    public void act(Player player, Item parentItem, Item targetItem) {
        if (player.getInventory().decreaseByObjectId(parentItem.getObjectId(), 1)) {
            PlayerFameService.getInstance().addFameExp(player, value.intValue());
            player.getObserveController().notifyItemuseObservers(parentItem);
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_GET_FEXP(value.intValue()));
            ItemTemplate itemTemplate = parentItem.getItemTemplate();
            PacketSendUtility.sendPacket(player, new SM_PLAYER_FAME(player));
            PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), parentItem.getObjectId(), itemTemplate.getTemplateId()), true);
        }
    }
}
