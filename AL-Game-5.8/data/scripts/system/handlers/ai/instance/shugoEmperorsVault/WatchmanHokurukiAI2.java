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
package ai.instance.shugoEmperorsVault;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.gameobjects.Creature;

/**
 * @author Lyras
 */
@AIName("watchman_hokuruki")
// 235634
public class WatchmanHokurukiAI2 extends NpcAI2 {

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
						spawn(235633, 476.70721f, 638.32092f, 395.44031f, (byte) 0);

						spawn(235649, 471.98087f, 641.28455f, 395.53012f, (byte) 0);
						spawn(235649, 473.05136f, 635.12988f, 395.09039f, (byte) 0);
						break;
					case 60:
						spawn(235633, 486.04926f, 650.40784f, 395.60233f, (byte) 90);

						spawn(235649, 482.76154f, 653.60107f, 395.63751f, (byte) 90);
						spawn(235649, 489.51031f, 653.80121f, 395.63751f, (byte) 90);
						break;
					case 40:
						spawn(235633, 486.41211f, 628.32477f, 395.59641f, (byte) 30);

						spawn(235649, 484.06662f, 625.17377f, 395.63159f, (byte) 30);
						spawn(235649, 489.72015f, 626.16064f, 395.63159f, (byte) 30);
						break;
					case 20:
						spawn(235633, 496.9855f, 638.52502f, 395.60233f, (byte) 60);

						spawn(235649, 499.11783f, 635.36615f, 395.63751f, (byte) 60);
						spawn(235649, 500.64038f, 640.77563f, 395.63751f, (byte) 60);
						break;
				}
				percents.remove(percent);
				break;
			}
		}
	}
}
