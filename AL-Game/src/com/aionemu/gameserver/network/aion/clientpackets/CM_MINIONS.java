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

import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.MinionAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.item.actions.AbstractItemAction;
import com.aionemu.gameserver.model.templates.item.actions.AdoptMinionAction;
import com.aionemu.gameserver.model.templates.item.actions.ItemActions;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Falke_34, FrozenKiller
 */
public class CM_MINIONS extends AionClientPacket {

	private int actionId;
	private MinionAction action;
	private int ItemObjectId;

	public CM_MINIONS(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	@Override
	protected void readImpl() {
		actionId = readH();
		action = MinionAction.getActionById(actionId);
		switch (action) {
		case ADOPT: {
			ItemObjectId = readD();
			break;
		}
		case RENAME: {

		}
		case DELETE: {

		}
		case LOCK: {

		}
		case SPAWN:
		case DISMISS: {

		}
		case GROWTH: {

		}
		case USE_FUNCTION:
		case STOP_FUNCTION: {

		}
		case CHARGE: {

		}
		case EVOLVE: {

		}
		case COMBINE: {

		}
		case SET_FUNCTION: {

		}
		default:
			break;
		}
	}

	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();
		if (player == null) {
			return;
		}
		switch (action) {
		case ADOPT: {
			if (player.getMinionList().getMinions().size() >= CustomConfig.MAX_MINION_LIST) {
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1404322, new Object[0]));
				return;
			}
			if (player.getMinionList().getMinions().size() >= 200) {
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1404322, new Object[0]));
				return;
			}
			Item item = player.getInventory().getItemByObjId(this.ItemObjectId);
			ItemActions itemActions = item.getItemTemplate().getActions();
			player.getObserveController().notifyItemuseObservers(item);
			for (AbstractItemAction itemAction : itemActions.getItemActions()) {
				if (!(itemAction instanceof AdoptMinionAction))
					continue;
				AdoptMinionAction action = (AdoptMinionAction) itemAction;
				action.act(player, item, item);
			}
			break;
		}
		default:
			break;
		}
	}
}
