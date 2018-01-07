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

import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.actions.CreatureActions;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.world.WorldMapInstance;

import ai.ShifterAI2;

/**
 * @author xTz
 */
@AIName("feeding_mantutu")
public class FeedingMantutuAI2 extends ShifterAI2 {

	@Override
	protected void handleDialogStart(Player player) {
		WorldMapInstance instance = getPosition().getWorldMapInstance();
		if (instance.getNpc(281128) == null && instance.getNpc(281129) == null) {
			super.handleDialogStart(player);
		}
	}

	@Override
	protected void handleUseItemFinish(Player player) {
		super.handleUseItemFinish(player);
		Npc boss = getPosition().getWorldMapInstance().getNpc(219033);
		if (boss != null && boss.isSpawned() && !CreatureActions.isAlreadyDead(boss)) {
			Npc npc = null;
			switch (getNpcId()) {
				case 701387: // water supply
					npc = (Npc) spawn(281129, 712.042f, 490.5559f, 939.7027f, (byte) 0);
					break;
				case 701386: // feed supply
					npc = (Npc) spawn(281128, 714.62634f, 504.4552f, 939.60675f, (byte) 0);
					break;
			}
			boss.getAi2().onCustomEvent(1, npc);
			AI2Actions.deleteOwner(this);
		}
	}
}
