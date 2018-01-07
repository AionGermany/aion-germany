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
package ai.instance.runadium;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MOVE;
import com.aionemu.gameserver.services.NpcShoutsService;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;

import ai.GeneralNpcAI2;

/**
 * @author nightm
 */
@AIName("grendal")
public class GrendalAI2 extends GeneralNpcAI2 {

	protected List<Integer> percents = new ArrayList<Integer>();

	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		checkPercentage(getLifeStats().getHpPercentage());
	}

	private void addPercent() {
		percents.clear();
		Collections.addAll(percents, new Integer[] { 95, 75, 65, 55, 50, 40 });
	}

	private synchronized void checkPercentage(int hpPercentage) {
		for (Integer percent : percents) {
			if (hpPercentage <= percent) {
				switch (percent) {
					case 95:
						break;
					case 75:
						NpcShoutsService.getInstance().sendMsg(getOwner(), 343527, getObjectId(), 0, 100);
						SkillEngine.getInstance().getSkill(getOwner(), 21171, 60, getOwner()).useSkill();
						break;
					case 65:
						NpcShoutsService.getInstance().sendMsg(getOwner(), 343692, getObjectId(), 0, 100);
						SkillEngine.getInstance().getSkill(getOwner(), 21165, 60, getOwner()).useNoAnimationSkill();
						teleport(1);
						FirstWave();
						break;
					case 55:
						SkillEngine.getInstance().getSkill(getOwner(), 21165, 60, getOwner()).useNoAnimationSkill();
						teleport(2);
						break;
					case 50:
						SkillEngine.getInstance().getSkill(getOwner(), 21165, 60, getOwner()).useNoAnimationSkill();
						teleport(3);
						SkillEngine.getInstance().getSkill(getOwner(), 21171, 60, getOwner()).useSkill();
						break;
					case 40:
						NpcShoutsService.getInstance().sendMsg(getOwner(), 343528, getObjectId(), 0, 100);
						SkillEngine.getInstance().getSkill(getOwner(), 21165, 60, getOwner()).useNoAnimationSkill();
						SecondWave();
						break;
				}
				percents.remove(percent);
				break;
			}
		}
	}

	private void teleport(int number) {
		final int var = number;
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				port(var);
			}
		}, 1999);

	}

	private void FirstWave() {

		spawn(284382, 257.02f, 257.78f, 241.78f, (byte) 0);
		spawn(284381, 258.16f, 252.24f, 241.80f, (byte) 0);
		spawn(284380, 255.65f, 263.45f, 241.80f, (byte) 0);

	}

	private void SecondWave() {

		spawn(284429, 240f, 235f, 251f, (byte) 17);
		spawn(284429, 284.75f, 262.83f, 248.70f, (byte) 60);
		spawn(284384, 255.50f, 293.25f, 253.71f, (byte) 91);
		spawn(284429, 232.74f, 263.88f, 248.55f, (byte) 114);
		spawn(284384, 271.20f, 230.53f, 250.85f, (byte) 39);

		AI2Actions.deleteOwner(this);
	}

	private void port(int var) {
		switch (var) {
			case 1:
				World.getInstance().updatePosition(getOwner(), 240f, 235f, 251f, (byte) 18, false);
				PacketSendUtility.broadcastPacket(getOwner(), new SM_MOVE(getOwner()));
				break;
			case 2:
				World.getInstance().updatePosition(getOwner(), 232.74f, 263.88f, 248.55f, (byte) 113, false);
				PacketSendUtility.broadcastPacket(getOwner(), new SM_MOVE(getOwner()));
				break;
			case 3:
				World.getInstance().updatePosition(getOwner(), 256.34f, 257.68f, 241.78f, (byte) 46, false);
				PacketSendUtility.broadcastPacket(getOwner(), new SM_MOVE(getOwner()));
				break;
		}
	}

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		addPercent();
		NpcShoutsService.getInstance().sendMsg(getOwner(), 343526, getObjectId(), 0, 1000);
	}

	@Override
	protected void handleDespawned() {
		super.handleDespawned();
		percents.clear();
	}

	@Override
	protected void handleDied() {
		percents.clear();
		super.handleDied();
	}
}
