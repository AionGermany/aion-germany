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
package instance;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.skillengine.SkillEngine;

/**
 * @author
 */
@InstanceID(301630000)
public class HellPassInstance extends GeneralInstanceHandler {

	@Override
	public void onEnterInstance(final Player player) {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (player.getRace().equals(Race.ASMODIANS)) {
					SkillEngine.getInstance().applyEffectDirectly(21346, player, player, 0);
				}
				else {
					SkillEngine.getInstance().applyEffectDirectly(21345, player, player, 0);
				}
			}
		}, 1000);
	}

	@Override
	public void onLeaveInstance(Player player) {
	}

	@Override
	public void onPlayerLogOut(Player player) {
	}
}
