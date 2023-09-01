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
import com.aionemu.gameserver.services.craft.MagicCraftService;

/**
 * @author Falke_34, FrozenKiller
 */

public class CM_MAGIC_CRAFT extends AionClientPacket {

	private int action;
	@SuppressWarnings("unused")
	private int targetTemplateId;
	private int recipeId;
	@SuppressWarnings("unused")
	private int targetObjId;
	private int materialsCount;
	private int craftType;

	public CM_MAGIC_CRAFT(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	@Override
	protected void readImpl() {
		action = readC();
		switch (action) {
			case 0: // Cancel MagicCraft
				targetTemplateId = readD();
				recipeId = readD();
				targetObjId = readD();
				materialsCount = readH();
				craftType = readC();
				break;
			case 1: // Start MagicCraft
				targetTemplateId = readD(); // TODO
				recipeId = readD();
				targetObjId = readD(); // TODO
				materialsCount = readH();
				craftType = readC(); // TODO
				for (int i = 0; i < materialsCount; i++) {
					readD(); // materialId
					readQ(); // materialCount
				}
		}
	}

	@Override
	protected void runImpl() {
		final Player player = getConnection().getActivePlayer();

		if (player == null || !player.isSpawned()) {
			return;
		}
		if (player.getController().isInShutdownProgress()) {
			return;
		}

		switch (action) {
			case 0: { // cancel
				MagicCraftService.sendCancelMagicCraft(player); // TODO (NullPointer)
				break;
			}
			case 1: { // start
				MagicCraftService.startMagicCraft(player, recipeId, craftType);
				break;
			}
		}
	}
}
