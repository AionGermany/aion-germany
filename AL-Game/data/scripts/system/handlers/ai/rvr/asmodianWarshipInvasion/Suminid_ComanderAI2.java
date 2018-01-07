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
package ai.rvr.asmodianWarshipInvasion;

import java.util.concurrent.atomic.AtomicBoolean;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.NpcShoutsService;
import com.aionemu.gameserver.services.RvrService;
import com.aionemu.gameserver.utils.MathUtil;

import ai.AggressiveNpcAI2;

@AIName("suminid_comander") // 240765
public class Suminid_ComanderAI2 extends AggressiveNpcAI2 {

	private AtomicBoolean startedEvent = new AtomicBoolean(false);

	@Override
	protected void handleCreatureMoved(Creature creature) {
		if (creature instanceof Player) {
			final Player player = (Player) creature;
			if (MathUtil.getDistance(getOwner(), player) <= 15) {
				if (startedEvent.compareAndSet(false, true)) {
					// Wretches!! Your resistance shall be futile.
					sendMsg(1501534, getObjectId(), false, 3000);
					// Let's show these cowardly Elyos the might of the Asmodians!
					sendMsg(1501535, getObjectId(), false, 9000);
					// Don't give up! The will of Empyrean Lord Azphel is with us.
					sendMsg(1501536, getObjectId(), false, 15000);
					// Empyrean Lord Azphel! Please give me strength.
					sendMsg(1501540, getObjectId(), false, 21000);
				}
			}
		}
	}

	@Override
	protected void handleDied() {
		RvrService.getInstance().stopRvr(3);
		spawn(833766, getOwner().getX(), getOwner().getY(), getOwner().getZ(), (byte) 0); // Dimensional Vortex
		super.handleDied();
	}

	private void sendMsg(int msg, int Obj, boolean isShout, int time) {
		NpcShoutsService.getInstance().sendMsg(getPosition().getWorldMapInstance(), msg, Obj, isShout, 0, time);
	}
}
