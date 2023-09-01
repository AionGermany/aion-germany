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
package ai.instance.nightmareCircus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.NpcShoutsService;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.WorldPosition;

import ai.AggressiveNpcAI2;

@AIName("ringmaster_rukibuki")
public class RingmasterRukibukiAI2 extends AggressiveNpcAI2 {

	private final List<Integer> percents = new ArrayList<Integer>();
	private boolean canThink = true;
	private Future<?> thinkTask;

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		addPercent();
		sendMsg(1500983);
	}

	@Override
	public boolean canThink() {
		return canThink;
	}

	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		checkPercentage(getLifeStats().getHpPercentage());
	}

	private synchronized void checkPercentage(int hpPercentage) {
		for (Integer percent : percents) {
			if (hpPercentage <= percent) {
				percents.remove(percent);
				canThink = false;
				thinkTask = ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						int npcId = 233151;
						if (Rnd.get(100) < 50) {
							npcId = 233162;
						}
						sp(npcId);
						canThink = true;
						Creature creature = getAggroList().getMostHated();
						if (creature == null || creature.getLifeStats().isAlreadyDead() || !getOwner().canSee(creature)) {
							setStateIfNot(AIState.FIGHT);
							think();
						}
						else {
							getMoveController().abortMove();
							getOwner().setTarget(creature);
							getOwner().getGameStats().renewLastAttackTime();
							getOwner().getGameStats().renewLastAttackedTime();
							getOwner().getGameStats().renewLastChangeTargetTime();
							getOwner().getGameStats().renewLastSkillTime();
							setStateIfNot(AIState.FIGHT);
							handleMoveValidate();
						}
					}
				}, 2000);
				break;
			}
		}
	}

	private void sp(int npcId) {
		if (npcId == 233151) {
			Npc mamut1 = (Npc) spawn(npcId, 521.58502f, 510.16528f, 199.59279f, (byte) 30);
			Npc mamut2 = (Npc) spawn(npcId, 521.71558f, 505.26691f, 199.50775f, (byte) 30);
			Npc mamut3 = (Npc) spawn(npcId, 523.37469f, 621.13623f, 208.05113f, (byte) 90);
			Npc mamut4 = (Npc) spawn(npcId, 523.67548f, 616.54547f, 208.05113f, (byte) 90);
			List<Player> players = getPosition().getWorldMapInstance().getPlayersInside();
			if (players != null && !players.isEmpty()) {
				mamut1.getAggroList().addHate(players.get(Rnd.get(0, (players.size() - 1))), 500);
				mamut2.getAggroList().addHate(players.get(Rnd.get(0, (players.size() - 1))), 500);
				mamut3.getAggroList().addHate(players.get(Rnd.get(0, (players.size() - 1))), 500);
				mamut4.getAggroList().addHate(players.get(Rnd.get(0, (players.size() - 1))), 500);
			}
			sendMsg(1500992);
			return;
		}
		float direction = Rnd.get(0, 199) / 100f;
		int distance = Rnd.get(1, 2);
		float x1 = (float) (Math.cos(Math.PI * direction) * distance);
		float y1 = (float) (Math.sin(Math.PI * direction) * distance);
		WorldPosition p = getPosition();
		Npc npc = (Npc) spawn(npcId, p.getX() + x1, p.getY() + y1, p.getZ(), p.getHeading());
		NpcShoutsService.getInstance().sendMsg(npc, 1500991, npc.getObjectId(), 0, 0);
		sendMsg(1500991);
	}

	private void addPercent() {
		percents.clear();
		Collections.addAll(percents, new Integer[] { 85, 65, 55, 45, 30, 15 });
	}

	private void cancelThinkTask() {
		if (thinkTask != null && !thinkTask.isDone()) {
			thinkTask.cancel(true);
		}
	}

	@Override
	protected void handleDespawned() {
		cancelThinkTask();
		super.handleDespawned();
	}

	private void removeHelpers() {
		WorldPosition p = getPosition();
		if (p != null) {
			WorldMapInstance instance = p.getWorldMapInstance();
			if (instance != null) {
				deleteNpcs(instance.getNpcs(233162));
				deleteNpcs(instance.getNpcs(233151));
			}
		}
	}

	private void deleteNpcs(List<Npc> npcs) {
		for (Npc npc : npcs) {
			if (npc != null) {
				npc.getController().onDelete();
			}
		}
	}

	@Override
	protected void handleDied() {
		removeHelpers();
		cancelThinkTask();
		super.handleDied();
	}

	@Override
	protected void handleBackHome() {
		canThink = true;
		addPercent();
		cancelThinkTask();
		removeHelpers();
		super.handleBackHome();
	}

	private void sendMsg(int msg) {
		NpcShoutsService.getInstance().sendMsg(getOwner(), msg, getObjectId(), 0, 0);
	}
}
