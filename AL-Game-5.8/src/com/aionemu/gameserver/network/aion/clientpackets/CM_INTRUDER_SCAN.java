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

import com.aionemu.gameserver.GameServer;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.services.conquerer_protector.ConquerorsService;
import com.aionemu.gameserver.services.territory.TerritoryService;

/**
 * @author Falke_34
 */
public class CM_INTRUDER_SCAN extends AionClientPacket {

	int type = 0;

	public CM_INTRUDER_SCAN(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	@Override
	protected void readImpl() {
		type = readC(); // 0 is Protector IntruderScan / 1 is automatic TerretoryScan
	}

	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();
		GameServer.log.info("CM_INTRUDER_SCAN: Type : " + type);
		switch (type) {
			case 0: // IntruderRadar
				ConquerorsService.getInstance().scanForIntruders(player);
				break;
			case 1: // Automatic TerritoryScan
				TerritoryService.getInstance().scanForIntruders(player);
				break;
		}
	}
}
