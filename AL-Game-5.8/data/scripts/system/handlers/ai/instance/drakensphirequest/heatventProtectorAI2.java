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
package ai.instance.drakensphirequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Creature;

import ai.AggressiveNpcAI2;

/**
 * @author Falke_34
 */
@AIName("heatvent_protector_quest")
// 237229
public class heatventProtectorAI2 extends AggressiveNpcAI2 {

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
		Collections.addAll(percents, new Integer[] { 40 });
	}

	private synchronized void checkPercentage(int hpPercent) {
		for (Integer percent : percents) {
			if (hpPercent <= percent) {
				switch (percent) {
					case 40:
						spawn(856549, 531.1f, 161.0f, 1682.0f, (byte) 0);
						spawn(856549, 540.0f, 141.9f, 1682.0f, (byte) 0);
						spawn(856549, 524.2f, 142.6f, 1682.0f, (byte) 0);
						break;
				}
				percents.remove(percent);
				break;
			}
		}
	}
}
