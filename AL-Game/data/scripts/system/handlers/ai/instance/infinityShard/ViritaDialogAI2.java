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

package ai.instance.infinityShard;

import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.services.NpcShoutsService;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/**
 * @author Alcapwnd
 */
@AIName("vritradialog_hyperion")
public class ViritaDialogAI2 extends NpcAI2 {

	@Override
	public void handleSpawned() {
		super.handleSpawned();
		NpcShoutsService.getInstance().sendMsg(getOwner(), 1500761, getObjectId(), 0, 10000);
		NpcShoutsService.getInstance().sendMsg(getOwner(), 1500762, getObjectId(), 0, 15000);
		NpcShoutsService.getInstance().sendMsg(getOwner(), 1500760, getObjectId(), 0, 20000);
		startEventTask(30000);
	}

	private void startEventTask(final int time) {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (!isAlreadyDead()) {
					if (time == 30000) {
						AI2Actions.deleteOwner(ViritaDialogAI2.this);
						spawn(230396, 109.6370f, 140.9392f, 112.1742f, (byte) 12);
						spawn(230396, 130.4754f, 132.7612f, 112.2131f, (byte) 83);
						spawn(230396, 151.7122f, 132.7612f, 112.2131f, (byte) 83);
						spawn(230396, 124.9110f, 162.9065f, 129.2247f, (byte) 64);

						spawn(230397, 106.8842f, 143.6426f, 112.2893f, (byte) 24);
						spawn(230397, 133.4831f, 113.4827f, 128.9372f, (byte) 10);
						spawn(230397, 147.9318f, 136.2378f, 112.1742f, (byte) 60);
						spawn(230397, 127.3380f, 161.2330f, 129.2247f, (byte) 92);
					}
				}
			}

		}, time);
	}
}
