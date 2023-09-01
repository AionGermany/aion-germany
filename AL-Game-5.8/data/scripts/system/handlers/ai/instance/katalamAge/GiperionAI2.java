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
package ai.instance.katalamAge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;

import ai.AggressiveNpcAI2;

/**
 * @author nightm
 */
@AIName("giperion")
public class GiperionAI2 extends AggressiveNpcAI2 {

	protected List<Integer> percents = new ArrayList<Integer>();
	protected List<Integer> npcIds = new ArrayList<Integer>();

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		addPercent();
		allNpcIds();
	}

	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		checkPercentage(getLifeStats().getHpPercentage());
	}

	@Override
	protected void handleCreatureAggro(Creature creature) {
		super.handleCreatureAggro(creature);
	}

	@Override
	protected void handleDied() {
		super.handleDied();
		for (Integer npcid : npcIds)
			despawnNpcs(npcid);
	}

	private synchronized void checkPercentage(int hpPercentage) {
		for (Integer percent : percents) {
			if (hpPercentage <= percent) {
				switch (percent) {
					case 75:
					case 50:
						spawnEquipment1();
						break;
					case 25:
						spawnEquipment2();
						break;
				}
				percents.remove(percent);
				break;
			}
		}
	}

	private void spawnEquipment1() {
		spawn(231092, 126.5471f, 154.479614f, 131.471161f, (byte) 90);
		spawn(231093, 126.5471f, 154.47961f, 131.47116f, (byte) 90);
		spawn(231094, 146.724548f, 139.122665f, 132.68515f, (byte) 60);
		spawn(231095, 129.413055f, 121.347656f, 131.471161f, (byte) 30);
		spawn(231096, 151.4244f, 143.7524f, 124.588257f, (byte) 0);
		spawn(231096, 149.757f, 145.6058f, 124.388809f, (byte) 0);
		spawn(233296, 148.959991f, 140.7144f, 124.69268f, (byte) 0);
		spawn(233295, 149.564392f, 140.6568f, 124.6927f, (byte) 0);
		spawn(231099, 107.2858f, 129.659f, 124.404213f, (byte) 0);
		spawn(233301, 109.5848f, 127.3776f, 124.083214f, (byte) 0);
		spawn(233298, 107.5332f, 129.1126f, 124.336235f, (byte) 0);
		spawn(231100, 108.2658f, 129.8578f, 124.382507f, (byte) 0);
		spawn(231101, 107.325f, 128.6048f, 124.294525f, (byte) 0);
		spawn(233291, 107.6864f, 127.911804f, 124.212212f, (byte) 0);
		spawn(231103, 117.326118f, 143.281509f, 112.174316f, (byte) 0);
		spawn(231103, 130.397385f, 128.7875f, 112.123581f, (byte) 0);
		spawn(231103, 141.98439f, 134.141968f, 112.174294f, (byte) 0);
		spawn(231103, 128.517883f, 146.873154f, 112.174294f, (byte) 0);
	}

	private void spawnEquipment2() {
		spawn(231092, 126.5471f, 154.479614f, 131.471161f, (byte) 90);
		spawn(231093, 126.5471f, 154.47961f, 131.47116f, (byte) 90);
		spawn(231094, 146.724548f, 139.122665f, 132.68515f, (byte) 60);
		spawn(231095, 129.413055f, 121.347656f, 131.471161f, (byte) 30);
		spawn(231096, 151.4244f, 143.7524f, 124.588257f, (byte) 0);
		spawn(231096, 149.757f, 145.6058f, 124.388809f, (byte) 0);
		spawn(233288, 148.959991f, 140.7144f, 124.69268f, (byte) 0);
		spawn(233289, 149.564392f, 140.6568f, 124.6927f, (byte) 0);
		spawn(233291, 109.5848f, 127.3776f, 124.083214f, (byte) 0);
		spawn(231099, 107.2858f, 129.659f, 124.404213f, (byte) 0);
		spawn(233298, 107.5332f, 129.1126f, 124.336235f, (byte) 0);
		spawn(231100, 108.2658f, 129.8578f, 124.382507f, (byte) 0);
		spawn(231101, 107.325f, 128.6048f, 124.294525f, (byte) 0);
	}

	private void despawnNpcs(int npcId) {
		List<Npc> npcs = getPosition().getWorldMapInstance().getNpcs(npcId);
		for (Npc npc : npcs) {
			if (npc != null) {
				npc.getController().onDelete();
			}
		}
	}

	private void allNpcIds() {
		npcIds.clear();
		Collections.addAll(npcIds, new Integer[] { 231092, 231093, 231094, 231095, 231096, 231099, 231100, 231101, 233296, 233295, 233298, 233291, 233289 });
	}

	private void addPercent() {
		percents.clear();
		Collections.addAll(percents, new Integer[] { 75, 50, 25 });
	}
}
