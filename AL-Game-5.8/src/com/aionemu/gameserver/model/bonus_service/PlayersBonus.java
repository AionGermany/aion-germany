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
import com.aionemu.gameserver.model.templates.bonus_service.PlayersBonusPenaltyAttr;
import com.aionemu.gameserver.model.templates.bonus_service.PlayersBonusServiceAttr;
import com.aionemu.gameserver.skillengine.change.Func;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

/**
 * Created by Ace on 31/07/2016.
 */
public class PlayersBonus implements StatOwner {

	private List<IStatFunction> functions = new ArrayList<IStatFunction>();
	private PlayersBonusServiceAttr playersServiceBonusattr;

	public PlayersBonus(int buffId) {
		playersServiceBonusattr = DataManager.PLAYERS_BONUS_DATA.getInstanceBonusattr(buffId);
	}

	public void applyEffect(Player player, int buffId) {
		if (playersServiceBonusattr == null) {
			return;
		}
		for (PlayersBonusPenaltyAttr playersBonusPenaltyAttr : playersServiceBonusattr.getPenaltyAttr()) {
			if (playersBonusPenaltyAttr.getFunc().equals(Func.PERCENT)) {
				functions.add(new StatRateFunction(playersBonusPenaltyAttr.getStat(), playersBonusPenaltyAttr.getValue(), true));
			}
			else {
				functions.add(new StatAddFunction(playersBonusPenaltyAttr.getStat(), playersBonusPenaltyAttr.getValue(), true));
			}
		}
		player.getGameStats().addEffect(this, functions);
	}

	public void endEffect(Player player, int buffId) {
		functions.clear();
		player.setPlayersBonusId(1);
		player.getGameStats().endEffect(this);
	}
}
