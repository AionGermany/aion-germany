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
package ai.instance.raksangruins;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;

import ai.AggressiveNpcAI2;

/**
 * @author Kill3r
 */
@AIName("diplito")
// 236303
public class instructorDiplitoAI2 extends AggressiveNpcAI2 {

	protected List<Integer> percents = new ArrayList<Integer>();
	private boolean used = false;

	private void addPercents() {
		percents.clear();
		Collections.addAll(percents, new Integer[] { 40 });
	}

	private synchronized void checkhpPercentage(int hpPercentage) {
		for (Integer percent : percents) {
			if (hpPercentage <= percent) {
				switch (percent) {
					case 40:
						summonAdd();
						used = true;
						break;
				}
			}
		}
	}

	private void summonAdd() {
		if (!used) {
			spawn(855908, getPosition().getX(), getPosition().getY(), getPosition().getZ(), getPosition().getHeading());
		}
	}

	@Override
	protected void handleSpawned() {
		addPercents();
		super.handleSpawned();
	}

	@Override
	protected void handleDied() {
		super.handleDied();
		used = false;
		despawnNpc(getPosition().getWorldMapInstance().getNpc(855908));
	}

	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		checkhpPercentage(getLifeStats().getHpPercentage());
	}

	protected void despawnNpc(Npc npc) {
		if (npc != null) {
			npc.getController().onDelete();
		}
	}

}
