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
package ai.worlds.inggison;

import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.ai.Percentage;
import com.aionemu.gameserver.model.ai.SummonGroup;
import com.aionemu.gameserver.model.gameobjects.player.Player;

import ai.SummonerAI2;

/**
 * @author Luzien, xTz
 */
@AIName("omega")
// 216516
public class OmegaAI2 extends SummonerAI2 {

	@Override
	protected void handleBeforeSpawn(Percentage percent) {
		AI2Actions.useSkill(this, 19189);
		AI2Actions.useSkill(this, 19191);
	}

	@Override
	protected void handleSpawnFinished(SummonGroup summonGroup) {
		if (summonGroup.getNpcId() == 281948) {
			AI2Actions.useSkill(this, 18671);
		}
	}

	@Override
	protected boolean checkBeforeSpawn() {
		boolean hit = false;
		for (Player player : getKnownList().getKnownPlayers().values()) {
			if (isInRange(player, 30)) {
				hit = true;
				break;
			}
		}
		return hit;
	}
}
