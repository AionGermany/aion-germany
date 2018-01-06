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
package com.aionemu.gameserver.services.conquerer_protector;

import java.util.ArrayList;
import java.util.List;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.stats.calc.StatOwner;
import com.aionemu.gameserver.model.stats.calc.functions.IStatFunction;
import com.aionemu.gameserver.model.stats.calc.functions.StatAddFunction;
import com.aionemu.gameserver.model.stats.container.StatEnum;

/**
 * @author CoolyT
 */
public class ConquerorBuff implements StatOwner {

	private List<IStatFunction> functions = new ArrayList<IStatFunction>();

	public void applyEffect(Player player, int buffLevel) {
		int addvalue = buffLevel * 10; // BuffLevel 1 = 10; BuffLevel 2 = 20; Bufflevel 3 = 30; regarding client Xml ......
		if (buffLevel == 0) {
			if (hasBuff())
				player.getGameStats().endEffect(this);
			return;
		}

		if (hasBuff())
			endEffect(player);

		functions.add(new StatAddFunction(StatEnum.PVP_ATTACK_RATIO, addvalue, true));
		player.getGameStats().addEffect(this, functions);
	}

	public boolean hasBuff() {
		return !functions.isEmpty();
	}

	public void endEffect(Player player) {
		functions.clear();
		player.getGameStats().endEffect(this);
	}

}
