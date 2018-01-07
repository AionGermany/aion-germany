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
package ai.siege;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Npc;

/**
 * @author ATracer, Source
 */
@AIName("siege_protector")
public class SiegeProtectorNpcAI2 extends SiegeNpcAI2 {

	// spawn for quest
	@Override
	protected void handleDied() {
		super.handleDied();
		int npc = getOwner().getNpcId();
		switch (npc) {
			case 259614:
				spawn(701237, getOwner().getX(), getOwner().getY(), getOwner().getZ(), (byte) 0);
				despawnClaw();
				break;
		}
	}

	private void despawnClaw() {
		final Npc claw = getPosition().getWorldMapInstance().getNpc(701237);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				claw.getController().onDelete();
			}
		}, 60000 * 5);
	}
}
