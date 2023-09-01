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
package ai.worlds.conquest;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.spawnengine.ConquestSpawnManager;
import com.aionemu.gameserver.world.WorldPosition;

import ai.AggressiveNpcAI2;
import javolution.util.FastList;

/**
 * @author Falke_34, CoolyT
 */
@AIName("conquest_npc")
public class Conquest_NpcAI2 extends AggressiveNpcAI2 {

	@Override
	protected void handleDied() {

		WorldPosition p = getPosition();
		int cId = getNpcId();
		int npcId = 0;

		FastList<Integer> npcs = new FastList<Integer>();
		FastList<Integer> portals = new FastList<Integer>();

		npcs.add(856175); // Pawrunerk
		npcs.add(856176); // Chitrunerk
		npcs.add(856177); // Rapirunerk
		npcs.add(856178); // Dandrunerk

		if ((cId >= 236331 && cId <= 236334) || (cId >= 236359 && cId <= 236362)) // Shugo Monster
		{
			// Inggison Portals
			portals.add(833018); // Secret Portal
			portals.add(833019); // Questionable Portal

			int index = Rnd.get(0, 1);
			npcId = portals.get(index);
		}
		else if ((cId >= 236387 && cId <= 236390) || (cId >= 236415 && cId <= 236418)) // Owl Monster
		{
			// Gelkmaros Portals
			portals.add(833021); // Secret Portal
			portals.add(833022); // Questionable Portal

			int index = Rnd.get(0, 1);
			npcId = portals.get(index);
		}
		else // it wasn't a Shugo or a Owl Monster
		{
			int chance = Rnd.get(100);
			if (chance <= 30) // 30% Chance that a Buff Shugo appears.
			{
				int index = Rnd.get(0, npcs.size() - 1);
				npcId = npcs.get(index);
			}
		}

		if (npcId <= 0) {
			reSpawn();
			return;
		}

		final VisibleObject obj = spawn(npcId, p.getX(), p.getY(), p.getZ(), (byte) 0);

		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (obj != null && obj.isSpawned())
					obj.getController().delete();
			}
		}, 120000); // 2 Minutes.
		reSpawn();
	}

	private void reSpawn() {
		SpawnTemplate st = getSpawnTemplate();
		WorldPosition spawnPos = new WorldPosition(st.getWorldId());
		spawnPos.setXYZH(st.getX(), st.getY(), st.getZ(), st.getHeading());
		ConquestSpawnManager.spawnByLoc(spawnPos);
		super.handleDied();
	}
}
