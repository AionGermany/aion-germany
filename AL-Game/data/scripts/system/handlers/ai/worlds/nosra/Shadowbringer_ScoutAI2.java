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
package ai.worlds.nosra;

import java.util.List;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.services.NpcShoutsService;
import com.aionemu.gameserver.world.WorldPosition;

@AIName("shadowbringer_scout")
public class Shadowbringer_ScoutAI2 extends NpcAI2 {

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		startLifeTask();
		startRiftEscapeTask();
		startRiftEscapeClosed();
		// Am I still alive ?
		sendMsg(1501533, getObjectId(), false, 5000);
		// And I thought all was lost.
		sendMsg(1501532, getObjectId(), false, 8000);
	}

	private void startRiftEscapeTask() {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawnRiftEscape(701131);
			}
		}, 2500);
	}

	private void startLifeTask() {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				AI2Actions.deleteOwner(Shadowbringer_ScoutAI2.this);
			}
		}, 10000);
	}

	private void startRiftEscapeClosed() {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				despawnNpc(701131);
			}
		}, 12500);
	}

	private void spawnRiftEscape(int npcId) {
		rndSpawnInRange(npcId, Rnd.get(1, 3));
	}

	private Npc rndSpawnInRange(int npcId, float distance) {
		float direction = Rnd.get(0, 199) / 100f;
		float x1 = (float) (Math.cos(Math.PI * direction) * distance);
		float y1 = (float) (Math.sin(Math.PI * direction) * distance);
		WorldPosition p = getPosition();
		return (Npc) spawn(npcId, p.getX() + x1, p.getY() + y1, p.getZ(), (byte) 0);
	}

	private void despawnNpc(int npcId) {
		if (getPosition().getWorldMapInstance().getNpcs(npcId) != null) {
			List<Npc> npcs = getPosition().getWorldMapInstance().getNpcs(npcId);
			for (Npc npc : npcs) {
				npc.getController().onDelete();
			}
		}
	}

	private void sendMsg(int msg, int Obj, boolean isShout, int time) {
		NpcShoutsService.getInstance().sendMsg(getPosition().getWorldMapInstance(), msg, Obj, isShout, 0, time);
	}
}
