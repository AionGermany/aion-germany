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
package ai.instance.dredgion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;

import ai.AggressiveNpcAI2;

/**
 * @author Falke_34
 */
@AIName("captain_ashunatal") // 243953
public class CaptainAshunatalAI2 extends AggressiveNpcAI2 {

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
		Npc npc = getPosition().getWorldMapInstance().getNpc(243953); // Captain Ashunatal

		Npc add1 = getPosition().getWorldMapInstance().getNpc(243956); // Explosion Shadow
		Npc add2 = getPosition().getWorldMapInstance().getNpc(243957); // Foul Shadow
		Npc add3 = getPosition().getWorldMapInstance().getNpc(243958); // Disruption Shadow
		Npc add4 = getPosition().getWorldMapInstance().getNpc(243959); // Disruption Shadow
		if (npc != null) {
			add1.getController().onDelete();
			add2.getController().onDelete();
			add3.getController().onDelete();
			add4.getController().onDelete();
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
		Collections.addAll(percents, new Integer[] { 90, 80, 70, 60, 50, 40, 30, 20, 10 });
	}

	private synchronized void checkPercentage(int hpPercent) {
		for (Integer percent : percents) {
			if (hpPercent <= percent) {
				switch (percent) {
					case 90:
						spawn(243956, getOwner().getX() - 2, getOwner().getY() - 2, getOwner().getZ(), getOwner().getHeading());
						spawn(243956, getOwner().getX() - 2, getOwner().getY() - 2, getOwner().getZ(), getOwner().getHeading());
						break;
					case 80:
						spawn(243957, getOwner().getX() - 2, getOwner().getY() - 2, getOwner().getZ(), getOwner().getHeading());
						break;
					case 70:
						spawn(243958, getOwner().getX() - 2, getOwner().getY() - 2, getOwner().getZ(), getOwner().getHeading());
						spawn(243958, getOwner().getX() - 2, getOwner().getY() - 2, getOwner().getZ(), getOwner().getHeading());
						spawn(243958, getOwner().getX() - 2, getOwner().getY() - 2, getOwner().getZ(), getOwner().getHeading());
						break;
					case 60:
						spawn(243959, getOwner().getX() - 2, getOwner().getY() - 2, getOwner().getZ(), getOwner().getHeading());
						spawn(243959, getOwner().getX() - 2, getOwner().getY() - 2, getOwner().getZ(), getOwner().getHeading());
						break;
					case 50:
						spawn(243957, getOwner().getX() - 2, getOwner().getY() - 2, getOwner().getZ(), getOwner().getHeading());
						break;
					case 40:
						spawn(243956, getOwner().getX() - 2, getOwner().getY() - 2, getOwner().getZ(), getOwner().getHeading());
						spawn(243956, getOwner().getX() - 2, getOwner().getY() - 2, getOwner().getZ(), getOwner().getHeading());
						break;
					case 30:
						spawn(243959, getOwner().getX() - 2, getOwner().getY() - 2, getOwner().getZ(), getOwner().getHeading());
						spawn(243959, getOwner().getX() - 2, getOwner().getY() - 2, getOwner().getZ(), getOwner().getHeading());
						break;
					case 20:
						spawn(243958, getOwner().getX() - 2, getOwner().getY() - 2, getOwner().getZ(), getOwner().getHeading());
						spawn(243958, getOwner().getX() - 2, getOwner().getY() - 2, getOwner().getZ(), getOwner().getHeading());
						spawn(243958, getOwner().getX() - 2, getOwner().getY() - 2, getOwner().getZ(), getOwner().getHeading());
						break;
					case 10:
						spawn(243956, getOwner().getX() - 2, getOwner().getY() - 2, getOwner().getZ(), getOwner().getHeading());
						spawn(243956, getOwner().getX() - 2, getOwner().getY() - 2, getOwner().getZ(), getOwner().getHeading());
						break;
				}
				percents.remove(percent);
				break;
			}
		}
	}
}
