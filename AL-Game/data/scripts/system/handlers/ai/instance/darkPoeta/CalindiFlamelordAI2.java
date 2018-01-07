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
package ai.instance.darkPoeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.manager.EmoteManager;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.services.NpcShoutsService;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.world.WorldPosition;

import ai.AggressiveNpcAI2;

/**
 * @author Ritsu
 */
@AIName("calindiflamelord")
public class CalindiFlamelordAI2 extends AggressiveNpcAI2 {

	private List<Integer> percents = new ArrayList<Integer>();
	private AtomicBoolean isStart = new AtomicBoolean(false);

	@Override
	protected void handleSpawned() {
		addPercent();
		super.handleSpawned();
	}

	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		checkPercentage(getLifeStats().getHpPercentage());
		if (isStart.compareAndSet(false, true)) {
			checkTimer();
		}

	}

	private synchronized void checkPercentage(int hpPercentage) {
		for (Integer percent : percents) {
			if (hpPercentage <= percent) {
				if (percent == 60) {
					EmoteManager.emoteStopAttacking(getOwner());
					SkillEngine.getInstance().getSkill(getOwner(), 18233, 50, getOwner()).useSkill();
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							sp(281267);
						}
					}, 3000);
				}
				else {
					EmoteManager.emoteStopAttacking(getOwner());
					SkillEngine.getInstance().getSkill(getOwner(), 18233, 50, getOwner()).useSkill();
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							sp(281268);
							sp(281268);
						}
					}, 3000);
				}
				percents.remove(percent);
				break;
			}
		}
	}

	private void sp(int npcId) {
		if (npcId == 281267) {
			spawn(npcId, 1191.2714f, 1220.5795f, 144.2901f, (byte) 36);
			spawn(npcId, 1188.3695f, 1257.1322f, 139.66028f, (byte) 80);
			spawn(npcId, 1177.1423f, 1253.9136f, 140.58705f, (byte) 97);
			spawn(npcId, 1163.5889f, 1231.9149f, 145.40042f, (byte) 118);
		}
		else {
			float direction = Rnd.get(0, 199) / 100f;
			int distance = Rnd.get(0, 2);
			float x1 = (float) (Math.cos(Math.PI * direction) * distance);
			float y1 = (float) (Math.sin(Math.PI * direction) * distance);
			WorldPosition p = getPosition();
			spawn(npcId, p.getX() + x1, p.getY() + y1, p.getZ(), p.getHeading());
		}
	}

	private void checkTimer() {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (!isAlreadyDead()) {
					EmoteManager.emoteStopAttacking(getOwner());
					NpcShoutsService.getInstance().sendMsg(getOwner(), 1400259);
					SkillEngine.getInstance().getSkill(getOwner(), 19679, 50, getTarget()).useSkill();
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							if (!isAlreadyDead()) {
								getOwner().getController().onDelete();
								NpcShoutsService.getInstance().sendMsg(getOwner(), 1400260);
							}
						}
					}, 2000);
				}
			}
		}, 600000);
	}

	private void addPercent() {
		percents.clear();
		Collections.addAll(percents, new Integer[] { 60, 30 });
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
