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

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.team.legion.LegionHistory;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_LEGION_TABS;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Simple, xTz
 */
public class CM_LEGION_TABS extends AionClientPacket {

	private static final Logger log = LoggerFactory.getLogger(CM_LEGION_TABS.class);
	private int page;
	private int tab;

	public CM_LEGION_TABS(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	@Override
	protected void readImpl() {
		page = readD();
		tab = readC();
	}

	@Override
	protected void runImpl() {
		Player activePlayer = getConnection().getActivePlayer();

		if (activePlayer.getLegion() != null) {

			/**
			 * Max page is 16 for legion history
			 */
			if (page > 16) {
				return;
			}

			switch (tab) {
				/**
				 * History Tab
				 */
				case 0: // legion history
				case 2: // legion WH history
					Collection<LegionHistory> history = activePlayer.getLegion().getLegionHistoryByTabId(tab);
					/**
					 * If history size is less than page*8 return
					 */
					if (history.size() < page * 8) {
						return;
					}
					if (!history.isEmpty()) {
						PacketSendUtility.sendPacket(activePlayer, new SM_LEGION_TABS(history, page, tab));
					}
					break;
				/**
				 * Reward Tab
				 */
				case 1:
					// TODO Reward Tab Page
					break;
			}
		}
		else {
			log.warn("Player " + activePlayer.getName() + " was requested null legion");
		}
	}
}
