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
package ai.worlds.enshar;

import java.util.ArrayList;
import java.util.Collection;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.GameServer;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.ai2.handler.TalkEventHandler;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.world.WorldPosition;

/**
 * @author Falke_34, CoolyT
 */
@AIName("edinerk")
// 805350
public class EdinerkAI extends NpcAI2 {

	Collection<WorldPosition> loc = new ArrayList<WorldPosition>();
	Npc npc = null;

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		npc = getOwner();
		int time = 60; // respawnTime in minutes -- default 60 min.

		loc.clear();
		loc.add(new WorldPosition(220080000, 475.47388f, 2300.761f, 216.45724f, (byte) 96));
		loc.add(new WorldPosition(220080000, 768.74994f, 1292.4216f, 251.5f, (byte) 60));
		loc.add(new WorldPosition(220080000, 1456.0063f, 1744.8394f, 330.36365f, (byte) 98));
		loc.add(new WorldPosition(220080000, 1573.4567f, 142.89934f, 186.81342f, (byte) 60));
		loc.add(new WorldPosition(220080000, 1771.8647f, 2571.6003f, 300.01526f, (byte) 74));
		loc.add(new WorldPosition(220080000, 2357.9692f, 808.9668f, 285.73026f, (byte) 74));
		loc.add(new WorldPosition(220080000, 2660.1885f, 1424.3641f, 334.53787f, (byte) 74));

		GameServer.log.info("EdinerkAI: Spawned ...");
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				int index = Rnd.get(0, 6);
				WorldPosition random = (WorldPosition) loc.toArray()[index];
				spawn(805350, random.getX(), random.getY(), random.getZ(), random.getHeading());
				npc.getController().delete();
				GameServer.log.info("EdinerkAI: Spawned on Pos : " + random.toString());
			}
		}, 1000 * 60 * time);
	}

	@Override
	protected void handleDialogStart(Player player) {
		TalkEventHandler.onTalk(this, player);
	}

	@Override
	protected void handleDialogFinish(Player creature) {
		TalkEventHandler.onFinishTalk(this, creature);
	}
}
