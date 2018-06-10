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
package ai.instance.museumKnowledge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldPosition;

import ai.AggressiveNpcAI2;

/**
 * @author Rinzler
 */
@AIName("IDEternity_03_Dimension_Boss_01") //246440
public class IDEternity_03_Dimension_Boss_01AI2 extends AggressiveNpcAI2 {

	private List<Integer> percents = new ArrayList<Integer>();

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		addPercent();
	}

	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		checkPercentage(getLifeStats().getHpPercentage());
	}

	private void checkPercentage(int hpPercentage) {
		for (Integer percent : percents) {
			if (hpPercentage <= percent) {
				switch (percent) {
					case 30:
						spawnBossPortal();
						break;
					case 20:
						spawnBossSum();
						break;
					case 10:
						spawnBossSum();
						break;
				}
				percents.remove(percent);
				break;
			}
		}
	}

	private void addPercent() {
		percents.clear();
		Collections.addAll(percents, new Integer[] { 30 });
	}

	private void spawnBossPortal() {
		//
		PacketSendUtility.npcSendPacketTime(getOwner(), SM_SYSTEM_MESSAGE.STR_MSG_IDEternity_03_Dimension_02, 0);
		spawn(246937, 229.87758f, 1049.37230f, 706.75494f, (byte) 84);
		spawn(246442, 226.18071f, 1048.9526f, 706.75494f, (byte) 86);
		spawn(246442, 231.16983f, 1047.3529f, 706.75494f, (byte) 83);
		//
		spawn(247024, 207.51686f, 1045.44860f, 706.75494f, (byte) 103);
		spawn(246442, 211.03645f, 1045.8074f, 706.75494f, (byte) 98);
		spawn(246442, 205.73178f, 1041.6572f, 706.75494f, (byte) 105);
		//
		spawn(247025, 237.62363f, 1005.49835f, 706.75494f, (byte) 42);
		spawn(246442, 239.03406f, 1008.0827f, 706.75494f, (byte) 45);
		spawn(246442, 234.82101f, 1004.9394f, 706.75494f, (byte) 42);
	}

	private void spawnBossSum() {
		spawn(246442, 222.63515f, 1036.9862f, 706.75494f, (byte) 61);
		spawn(246442, 222.79411f, 1014.1347f, 706.75494f, (byte) 60);
		spawn(246442, 230.22252f, 1025.4879f, 706.75494f, (byte) 60);
		spawn(246442, 214.86880f, 1025.4816f, 706.75494f, (byte) 0);
		spawn(246442, 202.80700f, 1025.3514f, 706.75494f, (byte) 0);
	}

	@Override
	protected void handleDied() {
		final WorldPosition p = getPosition();
		if (p != null) {
			deleteNpcs(p.getWorldMapInstance().getNpcs(246442)); // IDEternity_03_Dimension_Boss_Sum.
			deleteNpcs(p.getWorldMapInstance().getNpcs(246724)); // IDEternity_03_Dimension_Boss_Portal_02.
			deleteNpcs(p.getWorldMapInstance().getNpcs(246937)); // IDEternity_03_Dimension_Boss_Portal_03.
			deleteNpcs(p.getWorldMapInstance().getNpcs(247024)); // IDEternity_03_Dimension_Boss_Portal_04.
			deleteNpcs(p.getWorldMapInstance().getNpcs(247025)); // IDEternity_03_Dimension_Boss_Portal_05.
		}
		super.handleDied();
	}

	@Override
	protected void handleBackHome() {
		final WorldPosition p = getPosition();
		if (p != null) {
			deleteNpcs(p.getWorldMapInstance().getNpcs(246442)); // IDEternity_03_Dimension_Boss_Sum.
			deleteNpcs(p.getWorldMapInstance().getNpcs(246937)); // IDEternity_03_Dimension_Boss_Portal_03.
			deleteNpcs(p.getWorldMapInstance().getNpcs(247024)); // IDEternity_03_Dimension_Boss_Portal_04.
			deleteNpcs(p.getWorldMapInstance().getNpcs(247025)); // IDEternity_03_Dimension_Boss_Portal_05.
		}
		addPercent();
		super.handleBackHome();
	}

	private void deleteNpcs(List<Npc> npcs) {
		for (Npc npc : npcs) {
			if (npc != null) {
				npc.getController().onDelete();
			}
		}
	}
}
