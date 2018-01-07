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
package ai.instance.ophidanBridge;

import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.NpcShoutsService;
import com.aionemu.gameserver.world.WorldMapInstance;

import ai.ActionItemNpcAI2;

/**
 * @author DeathMagnestic
 * @rework Eloann
 */
@AIName("ophidan_bridge")
public class OphidanBridgeAI2 extends ActionItemNpcAI2 {

	@Override
	public void handleUseItemFinish(Player player) {
		WorldMapInstance instance = getPosition().getWorldMapInstance();
		if (instance != null) {
			instance.getDoors().get(47).setOpen(true);
			NpcShoutsService.getInstance().sendMsg(getOwner(), 1401879);
			AI2Actions.deleteOwner(this);
		}
	}
}
