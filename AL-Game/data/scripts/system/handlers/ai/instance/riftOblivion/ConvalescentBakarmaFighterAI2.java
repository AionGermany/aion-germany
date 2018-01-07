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
package ai.instance.riftOblivion;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.world.WorldPosition;

/**
 * @author Falke_34
 */
@AIName("convalescent_bakarma_fighter") // 244930
public class ConvalescentBakarmaFighterAI2 extends NpcAI2 {

	@Override
	protected void handleDied() {
		final WorldPosition p = getPosition();
		if (p != null) {
			switch (Rnd.get(1, 2)) {
				case 1:
					spawn(244955, p.getX(), p.getY(), p.getZ(), (byte) 0);
					break;
				case 2:
					break;
				default:
					break;
			}
		}
		super.handleDied();
		AI2Actions.deleteOwner(this);
		AI2Actions.scheduleRespawn(this);
	}
}
