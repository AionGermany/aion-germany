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
package ai.worlds.cygnea;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;

import ai.AggressiveNpcAI2;

/**
 * @author Falke_34
 * @rework FrozenKiller
 */
@AIName("fierce_sandmane_tigric")
// 235973
public class FierceSandmaneTigricAI2 extends AggressiveNpcAI2 {

	protected List<Integer> percents = new ArrayList<Integer>();

	@Override
	protected void handleSpawned() {
		addPercent();
		super.handleSpawned();
	}

	@Override
	protected void handleAttack(Creature creature) {
		checkPercentage(getLifeStats().getHpPercentage());
		super.handleAttack(creature);
	}

	@Override
	protected void handleBackHome() {
		addPercent();
		super.handleBackHome();
	}

	@Override
	protected void handleDied() {
		Npc npc = getPosition().getWorldMapInstance().getNpc(235973);
		Npc add = getPosition().getWorldMapInstance().getNpc(235974);
		if (npc != null) {
			add.getController().onDelete();
			percents.clear();
			super.handleDied();
		}
		else {
			percents.clear();
			super.handleDied();
		}
	}

	private void addPercent() {
		percents.clear();
		Collections.addAll(percents, new Integer[] { 50 });
	}

	private synchronized void checkPercentage(int hpPercent) {
		for (Integer percent : percents) {
			if (hpPercent <= percent) {
				switch (percent) {
					case 50:
						spawn(235974, getOwner().getX() - 2, getOwner().getY() - 2, getOwner().getZ(), getOwner().getHeading()); // Cloned Seagric
						break;
				}
				percents.remove(percent);
				break;
			}
		}
	}
}
