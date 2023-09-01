/**
 * This file is part of Aion-Lightning <aion-lightning.org>.
 *
 *  Aion-Lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Aion-Lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY); without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details. *
 *  You should have received a copy of the GNU General Public License
 *  along with Aion-Lightning.
 *  If not, see <http://www.gnu.org/licenses/>.
 */
package ai.instance.holyTower;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ai.AggressiveNpcAI2;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Creature;

/**
 * @author Falke_34
 */
@AIName("heroes_3rd_wave_door") // 248442
public class Heroes_3rd_Wave_DoorAI2 extends AggressiveNpcAI2 {

	protected List<Integer> percents = new ArrayList<Integer>();

	@Override
	protected void handleSpawned() {
		addPercent();
		super.handleSpawned();
	}

	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		checkPercentage(getLifeStats().getHpPercentage());
	}

	@Override
	protected void handleBackHome() {
		addPercent();
		super.handleBackHome();
	}

	@Override
	protected void handleDespawned() {
		percents.clear();
		super.handleDespawned();
	}

	@Override
	protected void handleDied() {
		percents.clear();
		super.handleDied();
	}

	private void addPercent() {
		percents.clear();
		Collections.addAll(percents, new Integer[] { 80, 60, 40, 20 });
	}

	private synchronized void checkPercentage(int hpPercent) {
		for (Integer percent : percents) {
			if (hpPercent <= percent) {
				switch (percent) {
					case 80:
                        spawn(248023, getOwner().getX() - 2, getOwner().getY() - 2, getOwner().getZ(), getOwner().getHeading()); // Chirurg
						spawn(248015, getOwner().getX() - 2, getOwner().getY() - 2, getOwner().getZ(), getOwner().getHeading()); // Warrior
						break;
					case 60:
						spawn(248016, getOwner().getX() - 2, getOwner().getY() - 2, getOwner().getZ(), getOwner().getHeading()); // Assassin
						break;
					case 40:
                        spawn(248015, getOwner().getX() - 2, getOwner().getY() - 2, getOwner().getZ(), getOwner().getHeading()); // Warrior
						spawn(248018, getOwner().getX() - 2, getOwner().getY() - 2, getOwner().getZ(), getOwner().getHeading()); // Medicus
						break;
					case 20:
						spawn(248015, getOwner().getX() - 2, getOwner().getY() - 2, getOwner().getZ(), getOwner().getHeading()); // Warrior
						break;
				}
				percents.remove(percent);
				break;
			}
		}
	}
}
