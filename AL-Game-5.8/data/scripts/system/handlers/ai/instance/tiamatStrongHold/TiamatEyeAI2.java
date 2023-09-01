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
package ai.instance.tiamatStrongHold;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.services.NpcShoutsService;

/**
 * @author Cheatkiller
 */
@AIName("tiamateye")
public class TiamatEyeAI2 extends NpcAI2 {

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		int owner = getOwner().getNpcId();
		switch (owner) {
			case 283177:
				NpcShoutsService.getInstance().sendMsg(getOwner(), 1500679, getOwner().getObjectId(), 0, 2000);
				break;
			case 283178:
				NpcShoutsService.getInstance().sendMsg(getOwner(), 1500680, getOwner().getObjectId(), 0, 2000);
				break;
			case 283179:
				NpcShoutsService.getInstance().sendMsg(getOwner(), 1500681, getOwner().getObjectId(), 0, 2000);
				break;
			case 283180:
				NpcShoutsService.getInstance().sendMsg(getOwner(), 1500682, getOwner().getObjectId(), 0, 2000);
				break;
		}
		despawn();
	}

	private void despawn() {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (!isAlreadyDead()) {
					AI2Actions.deleteOwner(TiamatEyeAI2.this);
				}
			}
		}, 5000);
	}
}
