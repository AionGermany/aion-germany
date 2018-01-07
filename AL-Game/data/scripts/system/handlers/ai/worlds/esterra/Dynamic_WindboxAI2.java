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
package ai.worlds.esterra;

import java.util.concurrent.atomic.AtomicBoolean;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.controllers.effect.PlayerEffectController;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.MathUtil;

import ai.AggressiveNpcAI2;

@AIName("dynamic_windbox")
public class Dynamic_WindboxAI2 extends AggressiveNpcAI2 {

	@Override
	public void think() {
	}

	private AtomicBoolean startedEvent = new AtomicBoolean(false);

	@Override
	protected void handleCreatureMoved(Creature creature) {
		if (creature instanceof Player) {
			final Player player = (Player) creature;
			PlayerEffectController effectController = player.getEffectController();
			if (MathUtil.getDistance(getOwner(), player) <= 5) {
				if (startedEvent.compareAndSet(false, true)) {
					switch (Rnd.get(1, 5)) {
						case 1:
							effectController.removeEffect(22883);
							effectController.removeEffect(22884);
							effectController.removeEffect(22885);
							effectController.removeEffect(22886);
							SkillEngine.getInstance().getSkill(player, 22882, 1, player).useNoAnimationSkill(); // Boost Attack Power
							break;
						case 2:
							effectController.removeEffect(22884);
							effectController.removeEffect(22885);
							effectController.removeEffect(22886);
							effectController.removeEffect(22882);
							SkillEngine.getInstance().getSkill(player, 22883, 1, player).useNoAnimationSkill(); // Movement Speed Increase
							break;
						case 3:
							effectController.removeEffect(22885);
							effectController.removeEffect(22886);
							effectController.removeEffect(22882);
							effectController.removeEffect(22883);
							SkillEngine.getInstance().getSkill(player, 22884, 1, player).useNoAnimationSkill(); // Attack Speed Increased
							break;
						case 4:
							effectController.removeEffect(22886);
							effectController.removeEffect(22882);
							effectController.removeEffect(22883);
							effectController.removeEffect(22884);
							SkillEngine.getInstance().getSkill(player, 22885, 1, player).useNoAnimationSkill(); // Boost Defense
							break;
						case 5:
							effectController.removeEffect(22882);
							effectController.removeEffect(22883);
							effectController.removeEffect(22884);
							effectController.removeEffect(22885);
							SkillEngine.getInstance().getSkill(player, 22886, 1, player).useNoAnimationSkill(); // Casting Time Reduced
							break;
					}
					AI2Actions.deleteOwner(Dynamic_WindboxAI2.this);
					AI2Actions.scheduleRespawn(this);
				}
			}
		}
	}

	@Override
	public boolean isMoveSupported() {
		return false;
	}
}
