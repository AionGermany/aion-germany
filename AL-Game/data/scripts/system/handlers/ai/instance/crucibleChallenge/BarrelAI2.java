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
package ai.instance.crucibleChallenge;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;

/**
 * @author xTz
 */
@AIName("barrel")
public class BarrelAI2 extends NpcAI2 {

	@Override
	protected void handleDied() {
		super.handleDied();
		int npcId = 0;
		switch (getNpcId()) {
			case 218560:
				npcId = 218561;
				break;
			case 217840:
				npcId = 217841;
				break;
		}
		float direction = Rnd.get(0, 199) / 100f;
		float x1 = (float) (Math.cos(Math.PI * direction) * 4);
		float y1 = (float) (Math.sin(Math.PI * direction) * 4);
		spawn(npcId, getPosition().getX() + x1, getPosition().getY() + y1, getPosition().getZ(), (byte) 0);
		AI2Actions.deleteOwner(this);
	}
}
