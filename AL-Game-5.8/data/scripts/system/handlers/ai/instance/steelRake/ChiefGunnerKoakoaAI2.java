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
package ai.instance.steelRake;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.ai.Percentage;
import com.aionemu.gameserver.utils.ThreadPoolManager;

import ai.SummonerAI2;

/**
 * @author xTz
 */
@AIName("gunnerkoakoa")
public class ChiefGunnerKoakoaAI2 extends SummonerAI2 {

	@Override
	protected void handleIndividualSpawnedSummons(Percentage percent) {
		if (getEffectController().hasAbnormalEffect(18552)) {
			checkAbnormalEffect();
		}
		randomSpawn(Rnd.get(1, 3));
	}

	private void checkAbnormalEffect() {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				getEffectController().removeEffect(18552);
				// to do remove pause
			}
		}, 21000);
	}

	private void randomSpawn(int i) {
		// to do pause boss
		spawn(281212, 757.39746f, 508.70383f, 1012.30084f, (byte) 0);
		switch (i) {
			case 1:
				spawn(281212, 726.1167f, 503.28836f, 1012.6846f, (byte) 0);
				spawn(281212, 736.4446f, 505.3141f, 1012.1576f, (byte) 0);
				spawn(281212, 746.9261f, 503.50122f, 1012.68335f, (byte) 0);
				spawn(281212, 728.9705f, 492.59402f, 1012.68335f, (byte) 0);
				spawn(281212, 739.9526f, 491.54123f, 1011.692f, (byte) 0);
				spawn(281212, 749.754f, 491.74677f, 1011.8663f, (byte) 0);
				spawn(281212, 756.9996f, 500.01736f, 1011.692f, (byte) 0);
				spawn(281213, 736.9722f, 514.6446f, 1011.8599f, (byte) 0);
				spawn(281213, 747.5162f, 514.51715f, 1011.692f, (byte) 0);
				spawn(281213, 726.8303f, 514.5155f, 1012.6845f, (byte) 0);
				spawn(281213, 727.9019f, 524.578f, 1012.68365f, (byte) 0);
				spawn(281213, 738.52844f, 525.0482f, 1011.692f, (byte) 0);
				spawn(281213, 758.3127f, 520.59143f, 1011.692f, (byte) 0);
				spawn(281213, 748.7474f, 525.84f, 1011.859f, (byte) 0);
				break;
			case 2:
				spawn(281213, 726.1167f, 503.28836f, 1012.6846f, (byte) 0);
				spawn(281213, 736.4446f, 505.3141f, 1012.1576f, (byte) 0);
				spawn(281212, 746.9261f, 503.50122f, 1012.68335f, (byte) 0);
				spawn(281213, 728.9705f, 492.59402f, 1012.68335f, (byte) 0);
				spawn(281213, 739.9526f, 491.54123f, 1011.692f, (byte) 0);
				spawn(281212, 749.754f, 491.74677f, 1011.8663f, (byte) 0);
				spawn(281212, 756.9996f, 500.01736f, 1011.692f, (byte) 0);
				spawn(281212, 736.9722f, 514.6446f, 1011.8599f, (byte) 0);
				spawn(281213, 747.5162f, 514.51715f, 1011.692f, (byte) 0);
				spawn(281212, 726.8303f, 514.5155f, 1012.6845f, (byte) 0);
				spawn(281212, 727.9019f, 524.578f, 1012.68365f, (byte) 0);
				spawn(281212, 738.52844f, 525.0482f, 1011.692f, (byte) 0);
				spawn(281213, 758.3127f, 520.59143f, 1011.692f, (byte) 0);
				spawn(281213, 748.7474f, 525.84f, 1011.859f, (byte) 0);
				break;
			case 3:
				spawn(281212, 726.1167f, 503.28836f, 1012.6846f, (byte) 0);
				spawn(281212, 736.4446f, 505.3141f, 1012.1576f, (byte) 0);
				spawn(281213, 746.9261f, 503.50122f, 1012.68335f, (byte) 0);
				spawn(281212, 728.9705f, 492.59402f, 1012.68335f, (byte) 0);
				spawn(281212, 739.9526f, 491.54123f, 1011.692f, (byte) 0);
				spawn(281213, 749.754f, 491.74677f, 1011.8663f, (byte) 0);
				spawn(281213, 756.9996f, 500.01736f, 1011.692f, (byte) 0);
				spawn(281213, 736.9722f, 514.6446f, 1011.8599f, (byte) 0);
				spawn(281212, 747.5162f, 514.51715f, 1011.692f, (byte) 0);
				spawn(281213, 726.8303f, 514.5155f, 1012.6845f, (byte) 0);
				spawn(281213, 727.9019f, 524.578f, 1012.68365f, (byte) 0);
				spawn(281213, 738.52844f, 525.0482f, 1011.692f, (byte) 0);
				spawn(281212, 758.3127f, 520.59143f, 1011.692f, (byte) 0);
				spawn(281212, 748.7474f, 525.84f, 1011.859f, (byte) 0);
				break;
		}
	}
}
