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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INSTANCE_INFO;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author nrg
 */
public class CM_INSTANCE_INFO extends AionClientPacket {

	private static Logger log = LoggerFactory.getLogger(CM_INSTANCE_INFO.class);
	@SuppressWarnings("unused")
	private int unk1, unk2;

	public CM_INSTANCE_INFO(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl() {
		unk1 = readD();
		unk2 = readC(); // team?
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl() {
		if (unk2 == 1 && !getConnection().getActivePlayer().isInTeam()) {
			log.debug("Received CM_INSTANCE_INFO with teamdata request but player has no team!");
		}
		if (unk2 == 1) {
			Player player = getConnection().getActivePlayer();
			if (player.isInAlliance2()) {
				boolean answer = true;
				for (Player p : player.getPlayerAlliance2().getMembers()) {
					if (answer) {
						PacketSendUtility.sendPacket(p, new SM_INSTANCE_INFO(p, true, p.getCurrentTeam()));
						answer = false;
					}
					else {
						PacketSendUtility.sendPacket(p, new SM_INSTANCE_INFO(p, false, p.getCurrentTeam()));
					}
				}
			}
			else if (player.isInGroup2()) {
				boolean answer = true;
				for (Player p : player.getPlayerGroup2().getMembers()) {
					if (answer) {
						PacketSendUtility.sendPacket(p, new SM_INSTANCE_INFO(p, true, p.getCurrentTeam()));
						answer = false;
					}
					else {
						PacketSendUtility.sendPacket(p, new SM_INSTANCE_INFO(p, false, p.getCurrentTeam()));
					}
				}
			}
		}
		else
			sendPacket(new SM_INSTANCE_INFO(getConnection().getActivePlayer(), true, getConnection().getActivePlayer().getCurrentTeam()));
	}
}
