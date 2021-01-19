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
package com.aionemu.gameserver.model.account;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.stats.calc.StatOwner;
import com.aionemu.gameserver.model.stats.calc.functions.IStatFunction;
import com.aionemu.gameserver.model.stats.calc.functions.StatRateFunction;
import com.aionemu.gameserver.model.templates.transform_book.CollectionAttr;
import com.aionemu.gameserver.model.templates.transform_book.TransformCollectionTemplate;

public class TransformCollectionBuff implements StatOwner {

	private TransformCollectionTemplate template;
	private List<IStatFunction> functions = new ArrayList<IStatFunction>();
	Logger log = LoggerFactory.getLogger(TransformCollectionBuff.class);

	public void apply(Player player, TransformCollectionTemplate template) {
		if (template == null) {
			return;
		}
		CollectionAttr attribute = null;
		if (player.isMagicalTypeClass()) {
			attribute = template.getMagicalAttr();
		} else {
			attribute = template.getPhysicalAttr();
		}
		functions.add(new StatRateFunction(attribute.getName(), attribute.getValue(), true));
		player.setBonus(true);
		player.getGameStats().addEffect(this, functions);
	}

	public void end(Player player) {
		functions.clear();
		player.setBonus(false);
		player.getGameStats().endEffect(this);
	}
}
