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
package com.aionemu.gameserver.controllers.observer;

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.MathUtil;

/**
 * @author xTz
 */
public abstract class GaleCycloneObserver extends ActionObserver {

	private Player player;
	private Creature creature;
	private double oldRange;

	public GaleCycloneObserver(Player player, Creature creature) {
		super(ObserverType.MOVE);
		this.player = player;
		this.creature = creature;
		oldRange = MathUtil.getDistance(player, creature);
	}

	@Override
	public void moved() {
		double newRange = MathUtil.getDistance(player, creature);
		if (creature == null || creature.getLifeStats().isAlreadyDead()) {
			if (player != null) {
				player.getObserveController().removeObserver(this);
			}
			return;
		}
		if (oldRange > 12 && newRange <= 12) {
			onMove();
		}
		oldRange = newRange;
	}

	public abstract void onMove();
}
