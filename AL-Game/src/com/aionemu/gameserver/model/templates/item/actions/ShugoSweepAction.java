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
import com.aionemu.gameserver.model.gameobjects.player.PlayerCommonData;
import com.aionemu.gameserver.model.gameobjects.player.PlayerSweep;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SHUGO_SWEEP;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * Created by Ghostfur
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ShugoSweepAction")
public class ShugoSweepAction extends AbstractItemAction {

	@XmlAttribute(name = "type")
	// 1 reset ; 2 gold dice
	protected int type;

	@XmlAttribute(name = "count")
	protected boolean count;

	@Override
	public boolean canAct(Player player, Item parentItem, Item targetItem) {
		if (type == 1) {
			if (getCommonData(player).getResetBoard() != 0) {
				PacketSendUtility.sendMessage(player, "You have already one Reset Board");
				return false;
			}
		}
		return true;
	}

	@Override
	public void act(Player player, Item parentItem, Item targetItem) {
		if (player.getInventory().decreaseByObjectId(parentItem.getObjectId(), 1)) {
			if (type == 1) {
				getCommonData(player).setResetBoard(getCommonData(player).getResetBoard() + 1);
				PacketSendUtility.sendMessage(player, "You have received one Reset Board");
			}
			if (type == 2) {
				getCommonData(player).setGoldenDice(getCommonData(player).getGoldenDice() + 1);
				PacketSendUtility.sendMessage(player, "You have received one Golden Dice");
			}
			PacketSendUtility.sendPacket(player, new SM_SHUGO_SWEEP(getPlayerSweep(player).getBoardId(), getPlayerSweep(player).getStep(), getPlayerSweep(player).getFreeDice(), getCommonData(player).getGoldenDice(), getCommonData(player).getResetBoard(), 0));
		}
	}

	public PlayerCommonData getCommonData(Player player) {
		return player.getCommonData();
	}

	public PlayerSweep getPlayerSweep(Player player) {
		return player.getPlayerShugoSweep();
	}
}
