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
package ai.instance.fireTempleMemory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Creature;

import ai.AggressiveNpcAI2;

/**
 * @author Falke_34
 */
@AIName("strong_tough_sipus")
// 244096
public class StrongToughSipusAI2 extends AggressiveNpcAI2 {

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
		Collections.addAll(percents, new Integer[] { 90 });
		Collections.addAll(percents, new Integer[] { 70 });
		Collections.addAll(percents, new Integer[] { 50 });
		Collections.addAll(percents, new Integer[] { 40 });
		Collections.addAll(percents, new Integer[] { 30 });
		Collections.addAll(percents, new Integer[] { 10 });
	}

	private synchronized void checkPercentage(int hpPercent) {
		for (Integer percent : percents) {
			if (hpPercent <= percent) {
				switch (percent) {
					case 90:
						spawn(244089, 294.90015f, 205.86647f, 119.36517f, (byte) 40);
						spawn(244089, 292.42633f, 197.40533f, 119.36552f, (byte) 40);
						spawn(244089, 302.09448f, 200.031f, 119.36517f, (byte) 40);
						break;
					case 70:
						spawn(244088, 285.06448f, 212.93443f, 119.61173f, (byte) 40);
						spawn(244088, 300.6554f, 215.35086f, 119.65028f, (byte) 40);
						spawn(244088, 311.44366f, 203.59665f, 119.01606f, (byte) 40);
						spawn(244088, 309.38092f, 190.3146f, 118.56891f, (byte) 40);
						spawn(244088, 293.37613f, 183.59897f, 118.79105f, (byte) 40);
						spawn(244088, 280.81534f, 196.67685f, 119.56473f, (byte) 40);
						break;
					case 50:
						spawn(244088, 285.06448f, 212.93443f, 119.61173f, (byte) 40);
						spawn(244088, 300.6554f, 215.35086f, 119.65028f, (byte) 40);
						spawn(244088, 311.44366f, 203.59665f, 119.01606f, (byte) 40);
						spawn(244088, 309.38092f, 190.3146f, 118.56891f, (byte) 40);
						spawn(244088, 293.37613f, 183.59897f, 118.79105f, (byte) 40);
						spawn(244088, 280.81534f, 196.67685f, 119.56473f, (byte) 40);
						break;
					case 40:
						spawn(244089, 294.90015f, 205.86647f, 119.36517f, (byte) 40);
						spawn(244089, 292.42633f, 197.40533f, 119.36552f, (byte) 40);
						spawn(244089, 302.09448f, 200.031f, 119.36517f, (byte) 40);
						break;
					case 30:
						spawn(244088, 285.06448f, 212.93443f, 119.61173f, (byte) 40);
						spawn(244088, 300.6554f, 215.35086f, 119.65028f, (byte) 40);
						spawn(244088, 311.44366f, 203.59665f, 119.01606f, (byte) 40);
						spawn(244088, 309.38092f, 190.3146f, 118.56891f, (byte) 40);
						spawn(244088, 293.37613f, 183.59897f, 118.79105f, (byte) 40);
						spawn(244088, 280.81534f, 196.67685f, 119.56473f, (byte) 40);
						break;
					case 10:
						spawn(244088, 285.06448f, 212.93443f, 119.61173f, (byte) 40);
						spawn(244088, 300.6554f, 215.35086f, 119.65028f, (byte) 40);
						spawn(244088, 311.44366f, 203.59665f, 119.01606f, (byte) 40);
						spawn(244088, 309.38092f, 190.3146f, 118.56891f, (byte) 40);
						spawn(244088, 293.37613f, 183.59897f, 118.79105f, (byte) 40);
						spawn(244088, 280.81534f, 196.67685f, 119.56473f, (byte) 40);
						break;
				}
				percents.remove(percent);
				break;
			}
		}
	}
}
