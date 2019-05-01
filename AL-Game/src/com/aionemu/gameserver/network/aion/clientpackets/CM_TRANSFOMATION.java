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

import java.util.ArrayList;
import java.util.List;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.TransformationAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.item.actions.AbstractItemAction;
import com.aionemu.gameserver.model.templates.item.actions.AdoptTransformationAction;
import com.aionemu.gameserver.model.templates.item.actions.ItemActions;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.services.TransformationService;

/**
 * @author Falke_34, FrozenKiller
 */
public class CM_TRANSFOMATION extends AionClientPacket {

	private int actionId;
	private TransformationAction action;
	private int ItemObjectId;
	private int transformId;
	private int itemObjId;
	List<Integer> material = new ArrayList<Integer>();
	private int Upgradeslot;
	private int Upgradeslot2;
	private int Upgradeslot3;
	private int Upgradeslot4;
	private int Upgradeslot5;
	private int Upgradeslot6;

	public CM_TRANSFOMATION(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	@Override
	protected void readImpl() {
		actionId = readH();
		action = TransformationAction.getActionById(actionId);
		switch (action) {
		case ADOPT:
			ItemObjectId = readD();
			break;
		case TRANSFORM: // Transform
			transformId = readD();
			itemObjId = readD(); // 8487
			break;
		case COMBINE:
			material.clear();
			Upgradeslot = readD();
			Upgradeslot2 = readD();
			Upgradeslot3 = readD();
			Upgradeslot4 = readD();
			Upgradeslot5 = readD();
			Upgradeslot6 = readD();
			material.add(Upgradeslot);
			material.add(Upgradeslot2);
			material.add(Upgradeslot3);
			material.add(Upgradeslot4);
			material.add(Upgradeslot5);
			material.add(Upgradeslot6);
			break;
		case USEBONUS: // Collection
			readD(); // Id?
			readC(); // 0 or 1 
			break;
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
				Item item = player.getInventory().getItemByObjId(this.ItemObjectId);
				ItemActions itemActions = item.getItemTemplate().getActions();
				player.getObserveController().notifyItemuseObservers(item);
				for (AbstractItemAction itemAction : itemActions.getItemActions()) {
					if (itemAction instanceof AdoptTransformationAction) {
						AdoptTransformationAction action = (AdoptTransformationAction) itemAction;
						action.act(player, item, item);
					}
				}
				break;
			}
			case TRANSFORM: {
				TransformationService.getInstance().transform(player, transformId, itemObjId);
				break;
			}
			case COMBINE: {
				TransformationService.getInstance().CombinationTransformation(player, material);
				break;
			}
			default:
				break;
		}
	}
}
