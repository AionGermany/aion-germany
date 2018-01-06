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
package com.aionemu.gameserver.model.bonus_service;

import java.util.ArrayList;
import java.util.List;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.stats.calc.StatOwner;
import com.aionemu.gameserver.model.stats.calc.functions.IStatFunction;
import com.aionemu.gameserver.model.stats.calc.functions.StatAddFunction;
import com.aionemu.gameserver.model.stats.calc.functions.StatRateFunction;
import com.aionemu.gameserver.model.templates.bonus_service.BonusPenaltyAttr;
import com.aionemu.gameserver.model.templates.bonus_service.BonusServiceAttr;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ICON_INFO;
import com.aionemu.gameserver.skillengine.change.Func;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * Created by Ace on 31/07/2016.
 */
public class ServiceBuff implements StatOwner {

	private List<IStatFunction> functions = new ArrayList<IStatFunction>();
	private BonusServiceAttr serviceBonusAttr;

	public ServiceBuff(int buffId) {
		serviceBonusAttr = DataManager.SERVICE_BUFF_DATA.getInstanceBonusattr(buffId);
	}

	public void applyEffect(Player player, int buffId) {
		if (serviceBonusAttr == null) {
			return;
		}
		for (BonusPenaltyAttr bonusPenaltyAttr : serviceBonusAttr.getPenaltyAttr()) {
			if (bonusPenaltyAttr.getFunc().equals(Func.PERCENT)) {
				functions.add(new StatRateFunction(bonusPenaltyAttr.getStat(), bonusPenaltyAttr.getValue(), true));
			}
			else {
				functions.add(new StatAddFunction(bonusPenaltyAttr.getStat(), bonusPenaltyAttr.getValue(), true));
			}
		}
		player.setBonus(true);
		player.getGameStats().addEffect(this, functions);
		PacketSendUtility.sendPacket(player, new SM_ICON_INFO(buffId, true));
	}

	public void endEffect(Player player, int buffId) {
		functions.clear();
		player.setBonus(false);
		player.getGameStats().endEffect(this);
		PacketSendUtility.sendPacket(player, new SM_ICON_INFO(buffId, false));
	}
}
