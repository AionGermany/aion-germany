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
package ai.instance.theobomosLab;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.ai2.manager.WalkManager;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;

import ai.AggressiveNpcAI2;

/**
 * @author Ritsu
 */
@AIName("triroan")
public class UnstableTriroanAI2 extends AggressiveNpcAI2 {

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

	private synchronized void checkPercentage(int hpPercentage) {
		if (hpPercentage > 99 && percents.size() < 10) {
			addPercent();
		}

		for (Integer percent : percents) {
			if (hpPercentage <= percent) {
				switch (percent) {
					case 99:
						SkillEngine.getInstance().getSkill(getOwner(), 16699, 1, getOwner()).useSkill();
						break;
					case 90:
						spawnFire();
						break;
					case 80:
						spawnWater();
						break;
					case 70:
						spawnEarth();
						break;
					case 60:
						spawnWind();
						break;
					case 50:
						spawnFire();
						break;
					case 40:
						spawnFire();
						spawnWater();
						break;
					case 30:
						spawnEarth();
						spawnWind();
						break;
					case 20:
						spawnWind();
						spawnFire();
						break;
					case 10:
						spawnWater();
						spawnEarth();
						break;
					case 5:
						spawnWind();
						spawnFire();
						spawnWater();
						spawnEarth();
						break;
				}
				percents.remove(percent);
				break;
			}
		}
	}

	private void spawnFire() {
		startWalk((Npc) spawn(280975, 601.966f, 488.853f, 196.019f, (byte) 0), "3101100002");
	}

	private void spawnWater() {
		startWalk((Npc) spawn(280976, 601.966f, 488.853f, 196.019f, (byte) 0), "3101100003");
	}

	private void spawnEarth() {
		startWalk((Npc) spawn(280977, 601.966f, 488.853f, 196.019f, (byte) 0), "3101100004");
	}

	private void spawnWind() {
		startWalk((Npc) spawn(280978, 601.966f, 488.853f, 196.019f, (byte) 0), "3101100005");
	}

	private void startWalk(Npc npc, String walkId) {
		npc.getSpawn().setWalkerId(walkId);
		WalkManager.startWalking((NpcAI2) npc.getAi2());
		npc.setState(1);
		PacketSendUtility.broadcastPacket(npc, new SM_EMOTION(npc, EmotionType.START_EMOTE2, 0, npc.getObjectId()));
	}

	private void addPercent() {
		percents.clear();
		Collections.addAll(percents, new Integer[] { 99, 90, 80, 70, 60, 50, 40, 30, 20, 10, 5 });
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
}
