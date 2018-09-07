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
package com.aionemu.gameserver.model.gameobjects.player.motion;

import java.util.HashMap;
import java.util.Map;

import com.aionemu.gameserver.model.IExpirable;
import com.aionemu.gameserver.model.gameobjects.player.Player;

/**
 * @author MrPoke
 */
public class Motion implements IExpirable {

	static final Map<Integer, Integer> motionType = new HashMap<Integer, Integer>();

	static {
		// Ninja Set
		motionType.put(1, 1);
		motionType.put(2, 2);
		motionType.put(3, 3);
		motionType.put(4, 4);

		// Hovering Set
		motionType.put(5, 1);
		motionType.put(6, 2);
		motionType.put(7, 3);
		motionType.put(8, 4);

		//Hovering Set 3-Day Pass (test_add_customize_motion_shop_01)
		motionType.put(9, 1);
		
		// Signboard
		motionType.put(10, 1);

		// Martial Arts Set
		motionType.put(11, 1);
		motionType.put(12, 2);
		motionType.put(13, 3);
		motionType.put(14, 4);

		// Martial Arts Master and Teachings of the Master
		motionType.put(15, 1);
		motionType.put(16, 2);
		motionType.put(17, 3);
		motionType.put(18, 4);

		// Private Store Sign
		motionType.put(19, 1);

		// Hello?
		motionType.put(20, 1);

		// Boxing legend
		motionType.put(21, 1);

		// Boxing champion
		motionType.put(22, 1);

		// Monkey King
		motionType.put(23, 1);
		motionType.put(24, 2);
		motionType.put(26, 3);
		motionType.put(25, 4);
		
		// Test (cash_add_customize_motion_lyn_01)
		motionType.put(27, 1);
		motionType.put(28, 2);
		motionType.put(29, 3);
		motionType.put(30, 4);
		
		// Illuminated Signboard
		motionType.put(31, 1);
		
		// Energy Concentration
		motionType.put(32, 1);
		
		// Fun Outing
		motionType.put(33, 1);
		
		//Ice Skate
		motionType.put(34, 1);
		motionType.put(35, 2);
		motionType.put(36, 3);
		motionType.put(37, 4);
		
		// Spellbinding Rhythm
		motionType.put(38, 1);
	}

	private int id;
	private int deletionTime = 0;
	private boolean active = false;

	/**
	 * @param id
	 * @param deletionTime
	 */
	public Motion(int id, int deletionTime, boolean isActive) {
		this.id = id;
		this.deletionTime = deletionTime;
		this.active = isActive;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	public int getRemainingTime() {
		if (deletionTime == 0) {
			return 0;
		}
		return deletionTime - (int) (System.currentTimeMillis() / 1000);
	}

	/**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active
	 *            the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public int getExpireTime() {
		return deletionTime;
	}

	@Override
	public void expireEnd(Player player) {
		player.getMotions().remove(id);
	}

	@Override
	public void expireMessage(Player player, int time) {
	}

	@Override
	public boolean canExpireNow() {
		return true;
	}
}
