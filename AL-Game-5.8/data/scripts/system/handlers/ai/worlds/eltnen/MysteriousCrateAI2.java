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
package ai.worlds.eltnen;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.world.WorldPosition;

@AIName("mysterious_crate")
// 211801
public class MysteriousCrateAI2 extends NpcAI2 {

	@Override
	protected void handleDied() {
		final WorldPosition p = getPosition();
		if (p != null) {
			switch (Rnd.get(1, 7)) {
				case 1:
					spawn(211793, p.getX(), p.getY(), p.getZ(), (byte) 0); // MuMu Mon
					break;
				case 2:
					spawn(211794, p.getX(), p.getY(), p.getZ(), (byte) 0); // MuMu Zoo
					break;
				case 3:
					spawn(211795, p.getX(), p.getY(), p.getZ(), (byte) 0); // Cursed Camu
					break;
				case 4:
					spawn(211796, p.getX(), p.getY(), p.getZ(), (byte) 0); // Cursed Miku
					break;
				case 5:
					spawn(211797, p.getX(), p.getY(), p.getZ(), (byte) 0); // Cursed Muku
					break;
				case 6:
					spawn(211798, p.getX(), p.getY(), p.getZ(), (byte) 0); // Arrogant Amurru
					break;
				case 7:
					spawn(211800, p.getX(), p.getY(), p.getZ(), (byte) 0); // Chaos Dracus
					break;
			}
		}
		super.handleDied();
		AI2Actions.deleteOwner(this);
		AI2Actions.scheduleRespawn(this);
	}
}
