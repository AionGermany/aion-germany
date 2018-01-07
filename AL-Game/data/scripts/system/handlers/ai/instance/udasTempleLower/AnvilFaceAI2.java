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
package ai.instance.udasTempleLower;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.services.NpcShoutsService;

import ai.AggressiveNpcAI2;

/**
 * Anvilface BossScript
 *
 * @author Antraxx
 */
@AIName("anvilface")
public class AnvilFaceAI2 extends AggressiveNpcAI2 {

	private AtomicBoolean isHome = new AtomicBoolean(true);
	protected List<Integer> percents = new ArrayList<Integer>();

	private void addPercent() {
		percents.clear();
		Collections.addAll(percents, new Integer[] { 50, 25 });
	}

	private synchronized void checkPercentage(int hpPercentage) {
		for (Integer percent : percents) {
			if (hpPercentage <= percent) {
				switch (percent) {
					case 50:
						break;
					case 25:
						break;
				}
				percents.remove(percent);
				break;
			}
		}
	}

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		addPercent();
	}

	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		if (isHome.compareAndSet(true, false)) {
			NpcShoutsService.getInstance().sendMsg(getOwner(), 1500036, getObjectId(), 0, 0);
		}
		checkPercentage(getLifeStats().getHpPercentage());
	}

	@Override
	protected void handleBackHome() {
		addPercent();
		super.handleBackHome();
		isHome.set(true);
	}

	@Override
	protected void handleDied() {
		super.handleDied();
		percents.clear();
		NpcShoutsService.getInstance().sendMsg(getOwner(), 1500037, getObjectId(), 0, 0);
	}
}
