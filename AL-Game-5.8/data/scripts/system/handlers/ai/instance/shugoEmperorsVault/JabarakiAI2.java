/**
 * This file is part of Aion-Lightning <aion-lightning.org>.
 *
 *  Aion-Lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Aion-Lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY); without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details. *
 *  You should have received a copy of the GNU General Public License
 *  along with Aion-Lightning.
 *  If not, see <http://www.gnu.org/licenses/>.
 */
package ai.instance.shugoEmperorsVault;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.gameobjects.Creature;

/**
 * @author Lyras
 */
@AIName("jabaraki")
// 235660
public class JabarakiAI2 extends NpcAI2 {

	protected List<Integer> percents = new ArrayList<Integer>();

	@Override
	protected void handleSpawned() {
		addPercent();
		super.handleSpawned();
	}

	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		checkPercentage(getLifeStats().getHpPercentage());
	}

	@Override
	protected void handleBackHome() {
		addPercent();
		super.handleBackHome();
	}

	@Override
	protected void handleDespawned() {
		percents.clear();
		super.handleDespawned();
	}

	@Override
	protected void handleDied() {
		percents.clear();
		super.handleDied();
	}

	private void addPercent() {
		percents.clear();
		Collections.addAll(percents, new Integer[] { 80, 60, 40, 20 });
	}

	private synchronized void checkPercentage(int hpPercent) {
		for (Integer percent : percents) {
			if (hpPercent <= percent) {
				switch (percent) {
					case 80:
						spawn(235631, 535.65863f, 388.60391f, 395.31137f, (byte) 10);
						spawn(235631, 535.67999f, 390.5246f, 395.57455f, (byte) 10);
						spawn(235631, 536.12079f, 388.2316f, 395.57455f, (byte) 10);
						spawn(235631, 536.57025f, 388.2316f, 395.57455f, (byte) 10);
						spawn(235631, 537.66498f, 406.50635f, 397.15497f, (byte) 100);
						spawn(235631, 538.54395f, 408.26468f, 395.15497f, (byte) 100);
						spawn(235631, 539.38446f, 409.99432f, 395.15497f, (byte) 100);
						spawn(235631, 539.89008f, 406.91602f, 395.31137f, (byte) 100);
						spawn(235631, 540.08575f, 407.01358f, 395.37427f, (byte) 100);
						spawn(235631, 560.40338f, 380.38724f, 395.89749f, (byte) 53);
						spawn(235631, 561.14966f, 381.51852f, 395.92307f, (byte) 53);
						spawn(235631, 561.50793f, 382.16434f, 395.72421f, (byte) 53);
						spawn(235631, 562.49835f, 384.02011f, 395.89749f, (byte) 53);
						spawn(235631, 569.03955f, 403.24002f, 397.11578f, (byte) 83);
						break;
					case 60:
						spawn(235630, 534.84564f, 390.16638f, 395.39029f, (byte) 10);
						spawn(235630, 535.26465f, 388.06601f, 395.3147f, (byte) 10);
						spawn(235630, 535.91302f, 386.66516f, 395.3147f, (byte) 10);
						spawn(235630, 536.91217f, 406.94269f, 395.8096f, (byte) 100);
						spawn(235630, 537.59412f, 387.37759f, 395.98294f, (byte) 10);
						spawn(235630, 537.94232f, 408.823f, 395.66263f, (byte) 100);
						spawn(235630, 538.98499f, 410.2489f, 395.4133f, (byte) 100);
						spawn(235630, 539.70984f, 405.74088f, 395.98294f, (byte) 100);
						spawn(235630, 541.19086f, 407.9996f, 395.98294f, (byte) 100);
						spawn(235630, 560.13489f, 380.47229f, 395.43802f, (byte) 53);
						spawn(235630, 561.0506f, 382.24615f, 395.43802f, (byte) 53);
						spawn(235630, 562.04718f, 384.19226f, 395.43802f, (byte) 53);
						break;
					case 40:
						spawn(235629, 536.89331f, 390.32019f, 395.62619f, (byte) 10);
						spawn(235629, 537.54303f, 387.63019f, 395.80844f, (byte) 10);
						spawn(235629, 539.75153f, 405.13397f, 395.72488f, (byte) 100);
						spawn(235629, 541.25134f, 407.52338f, 395.72488f, (byte) 100);
						spawn(235629, 543.70764f, 378.43268f, 395.78214f, (byte) 107);
						spawn(235629, 551.19135f, 386.90134f, 395.54523f, (byte) 90);
						spawn(235629, 557.44055f, 378.48816f, 395.63525f, (byte) 73);
						break;
					case 20:
						spawn(235631, 569.93604f, 400.95404f, 397.11578f, (byte) 83);
						spawn(235631, 570.47168f, 398.48041f, 398.11578f, (byte) 83);
						spawn(235631, 570.87695f, 402.05118f, 397.24573f, (byte) 83);
						spawn(235630, 569.03223f, 403.38486f, 396.88416f, (byte) 83);
						spawn(235630, 569.88995f, 401.1925f, 396.88416f, (byte) 83);
						spawn(235630, 570.51416f, 399.25363f, 395.88416f, (byte) 83);
						spawn(235629, 561.05634f, 381.99451f, 395.63406f, (byte) 53);
						spawn(235629, 562.23022f, 383.69931f, 395.67001f, (byte) 53);
						break;
				}
				percents.remove(percent);
				break;
			}
		}
	}
}
