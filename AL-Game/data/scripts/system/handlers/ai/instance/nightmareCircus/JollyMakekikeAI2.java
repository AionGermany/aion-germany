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
import javolution.util.FastList;

@AIName("jolly_makekike")
public class JollyMakekikeAI2 extends AggressiveNpcAI2 {

	private final List<Integer> percents = new ArrayList<Integer>();
	private boolean canThink = true;
	private Future<?> thinkTask;
	private final FastList<Npc> helpers = FastList.newInstance();
	private final FastList<Npc> servant = FastList.newInstance();
	private int lastSpawn;

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		addPercent();
		sp(831348);
		lastSpawn = 831348;
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
						sp(Rnd.get(831348, 831349));

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
		removeHelpers();
		Npc ui = getUi();
		if (ui != null) {
			int amount = 12;
			float x1;
			float y1;
			float interval = (float) (Math.PI * 2.0f / (amount / 2));
			int h = ui.getHeading() - 60;
			if (h < 0) {
				h = 120 + h;
			}
			for (int i = 0; amount > i; i++) {
				x1 = (float) (Math.cos(interval * i) * 17);
				y1 = (float) (Math.sin(interval * i) * 17);
				helpers.add((Npc) spawn(npcId, ui.getX() + x1, ui.getY() + y1, 199.50775f, (byte) h));
			}
		}

		thinkTask = ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				removeHelpers();
			}
		}, 15000);

		lastSpawn = npcId;
		sendMsg(1500998);
	}

	private Npc getUi() {
		WorldPosition p = getPosition();
		if (p != null) {
			WorldMapInstance instance = p.getWorldMapInstance();
			if (instance != null) {
				Npc npc = instance.getNpc(831573);
				if (npc == null) {
					npc = instance.getNpc(831741);
				}
				return npc;
			}
		}
		return null;
	}

	private void addPercent() {
		percents.clear();
		Collections.addAll(percents, new Integer[] { 65, 45, 30 });
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
		for (FastList.Node<Npc> n = helpers.head(), end = helpers.tail(); (n = n.getNext()) != end;) {
			Npc obj = n.getValue();
			if (obj == null) {
				continue;
			}
			if (obj.isSpawned() && !obj.getLifeStats().isAlreadyDead()) {
				obj.getController().onDelete();
				if (!getOwner().getLifeStats().isAlreadyDead()) {
					Npc serv = null;
					if (obj.getNpcId() == 831348) {
						serv = (Npc) spawn(233152, obj.getX(), obj.getY(), obj.getZ(), obj.getHeading());
					}
					else if (obj.getNpcId() == 831349) {
						serv = (Npc) spawn(233148, obj.getX(), obj.getY(), obj.getZ(), obj.getHeading());
					}
					servant.add(serv);
					NpcShoutsService.getInstance().sendMsg(serv, 1501001, serv.getObjectId(), 0, 0);
					List<Player> players = getPosition().getWorldMapInstance().getPlayersInside();
					if (players != null && !players.isEmpty()) {
						serv.getAggroList().addHate(players.get(Rnd.get(0, (players.size() - 1))), 500);
					}
				}
			}
		}
		if (lastSpawn == 831348) {
			sendMsg(1500999);
		}
		else {
			sendMsg(1501000);
		}
	}

	private void removeServant() {
		for (FastList.Node<Npc> n = servant.head(), end = servant.tail(); (n = n.getNext()) != end;) {
			Npc obj = n.getValue();
			if (obj == null) {
				continue;
			}
			if (obj.isSpawned() && !obj.getLifeStats().isAlreadyDead()) {
				obj.getController().onDelete();
			}
		}
	}

	@Override
	protected void handleDied() {
		removeHelpers();
		removeServant();
		cancelThinkTask();
		super.handleDied();
	}

	@Override
	protected void handleBackHome() {
		canThink = true;
		sendMsg(1500994);
		addPercent();
		cancelThinkTask();
		removeHelpers();
		removeServant();
		super.handleBackHome();
	}

	private void sendMsg(int msg) {
		NpcShoutsService.getInstance().sendMsg(getOwner(), msg, getObjectId(), 0, 0);
	}

	public void ownerSkillUse(int skillId) {
		switch (skillId) {
			case 21345:
				sendMsg(1500990);
				break;
		}
	}
}
