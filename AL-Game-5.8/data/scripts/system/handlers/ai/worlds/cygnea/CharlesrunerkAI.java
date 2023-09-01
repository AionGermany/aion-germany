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
package ai.worlds.cygnea;

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
@AIName("charlesrunerk")
// 805338
public class CharlesrunerkAI extends NpcAI2 {

	Collection<WorldPosition> loc = new ArrayList<WorldPosition>();
	Npc npc = null;

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		npc = getOwner();
		int time = 60; // respawnTime in minutes -- default 60 min.

		loc.clear();
		loc.add(new WorldPosition(210070000, 480.80933f, 1918.559f, 466.5252f, (byte) 15));
		loc.add(new WorldPosition(210070000, 1120.8098f, 2921.0552f, 291.25f, (byte) 90));
		loc.add(new WorldPosition(210070000, 1181.7f, 1558.1765f, 465.75f, (byte) 105));
		loc.add(new WorldPosition(210070000, 1398.9495f, 639.26959f, 581.25f, (byte) 44));
		loc.add(new WorldPosition(210070000, 2092.6746f, 2840.4351f, 322.962f, (byte) 15));
		loc.add(new WorldPosition(210070000, 2400.4988f, 1524.213f, 439.5217f, (byte) 60));
		loc.add(new WorldPosition(210070000, 2826.0615f, 766.99139f, 564.83984f, (byte) 15));

		GameServer.log.info("CharlesrunerkAI: Spawned ...");
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				int index = Rnd.get(0, 6);
				WorldPosition random = (WorldPosition) loc.toArray()[index];
				spawn(805338, random.getX(), random.getY(), random.getZ(), random.getHeading());
				npc.getController().delete();
				GameServer.log.info("CharlesrunerkAI: Spawned on Pos : " + random.toString());
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
