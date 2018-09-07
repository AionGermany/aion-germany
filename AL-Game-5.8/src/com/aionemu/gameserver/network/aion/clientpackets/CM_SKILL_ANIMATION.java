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
package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;

/**
 * @author FrozenKiller
 * @reworked Ghostfur (Aion-Unique)
 */
public class CM_SKILL_ANIMATION extends AionClientPacket {

	private int skillId;
	private int skillSkinId;

	public CM_SKILL_ANIMATION(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	protected void readImpl() {
		skillId = readH();
		skillSkinId = readH();
	}

	protected void runImpl() {
		Player player = getConnection().getActivePlayer();
		if (skillSkinId > 0) {
			player.getSkillSkinList().setActive(skillSkinId);
		}
		else {
			player.getSkillSkinList().setDeactive(skillId);
		}
	}
}
