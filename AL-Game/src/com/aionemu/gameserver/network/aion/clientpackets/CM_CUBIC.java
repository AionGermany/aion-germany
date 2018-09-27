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
import com.aionemu.gameserver.services.player.PlayerCubicService;

/**
 * @author Falke_34
 * @rework Phantom_KNA
 */
public class CM_CUBIC extends AionClientPacket {

    private int cubicId;
    
	public CM_CUBIC(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	@Override
    protected void readImpl() {
        cubicId = readD(); // Id Cube
        GameServer.log.info("ActionID: " + cubicId + ""); //For Debug 
    }

	@Override
    protected void runImpl() {
        Player player = getConnection().getActivePlayer();
        if (player == null) {
            return;
        }
        PlayerCubicService.getInstance().registerCubic(player, cubicId);
    }
}
