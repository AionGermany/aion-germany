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

import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.HouseDecoration;
import com.aionemu.gameserver.model.gameobjects.HouseObject;
import com.aionemu.gameserver.model.gameobjects.HousingAction;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.house.House;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.model.templates.item.actions.DecorateAction;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_HOUSE_EDIT;
import com.aionemu.gameserver.network.aion.serverpackets.SM_HOUSE_REGISTRY;
import com.aionemu.gameserver.questEngine.QuestEngine;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.services.HousingService;
import com.aionemu.gameserver.services.item.HouseObjectFactory;
import com.aionemu.gameserver.services.item.ItemPacketService.ItemDeleteType;
import com.aionemu.gameserver.utils.audit.AuditLogger;
import com.aionemu.gameserver.utils.idfactory.IDFactory;

/**
 * @author Rolandas
 * @author_update Ever'
 */
public class CM_HOUSE_EDIT extends AionClientPacket {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(CM_HOUSE_EDIT.class);
	private HousingAction action;
	private int actionId;
	int itemObjectId;
	float x, y, z;
	int rotation;
	int buildingId;

	public CM_HOUSE_EDIT(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	@Override
	protected void readImpl() {
		actionId = readC();
		action = HousingAction.getActionTypeById(actionId);
		if (action == HousingAction.ADD_ITEM || action == HousingAction.DELETE_ITEM || action == HousingAction.DESPAWN_OBJECT) {
			itemObjectId = readD();
		}
		else if (action == HousingAction.SPAWN_OBJECT || action == HousingAction.MOVE_OBJECT) {
			itemObjectId = readD();
			x = readF();
			y = readF();
			z = readF();
			rotation = readH();
		}
		else if (action == HousingAction.CHANGE_APPEARANCE) {
			buildingId = readD();
		}
		else if (action == HousingAction.ENTER_DECORATION || action == HousingAction.EXIT_DECORATION) {
			// NOTHING TO READ
		}
		else {
			log.error("Unknown housing action type? 0x" + Integer.toHexString(actionId).toUpperCase());
		}
	}

	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();
		if (player == null) {
			return;
		}

		if (action == HousingAction.ENTER_DECORATION) { // Enter Decoration mode
			sendPacket(new SM_HOUSE_EDIT(actionId));
			sendPacket(new SM_HOUSE_REGISTRY(actionId));
			sendPacket(new SM_HOUSE_REGISTRY(actionId + 1));
		}
		else if (action == HousingAction.EXIT_DECORATION) { // Exit Decoration mode
			sendPacket(new SM_HOUSE_EDIT(actionId));
		}
		else if (action == HousingAction.ADD_ITEM) { // Add item
			Item item = player.getInventory().getItemByObjId(itemObjectId);
			if (item == null) {
				return;
			}

			ItemTemplate template = item.getItemTemplate();
			player.getInventory().delete(item, ItemDeleteType.REGISTER);

			DecorateAction decorateAction = template.getActions().getDecorateAction();
			if (decorateAction != null) {
				HouseDecoration decor = new HouseDecoration(IDFactory.getInstance().nextId(), decorateAction.getTemplateId());
				player.getHouseRegistry().putCustomPart(decor);
				sendPacket(new SM_HOUSE_EDIT(actionId, 2, decor.getObjectId()));
			}
			else {
				House house = player.getHouseRegistry().getOwner();
				HouseObject<?> obj = HouseObjectFactory.createNew(house, template);
				player.getHouseRegistry().putObject(obj);
				sendPacket(new SM_HOUSE_EDIT(actionId, 1, obj.getObjectId()));
			}
		}
		else if (action == HousingAction.DELETE_ITEM) { // Delete item
			player.getHouseRegistry().removeObject(itemObjectId);
			sendPacket(new SM_HOUSE_EDIT(actionId, 1, itemObjectId));
			sendPacket(new SM_HOUSE_EDIT(4, 1, itemObjectId));
		}
		else if (action == HousingAction.SPAWN_OBJECT) { // spawn object
			HouseObject<?> obj = player.getHouseRegistry().getObjectByObjId(itemObjectId);
			if (obj == null) {
				return;
			}
			obj.setX(x);
			obj.setY(y);
			obj.setZ(z);
			obj.setRotation(rotation);
			sendPacket(new SM_HOUSE_EDIT(actionId, itemObjectId, x, y, z, rotation));
			obj.spawn();
			player.getHouseRegistry().setPersistentState(PersistentState.UPDATE_REQUIRED);
			sendPacket(new SM_HOUSE_EDIT(4, 1, itemObjectId));
			QuestEngine.getInstance().onHouseItemUseEvent(new QuestEnv(null, player, 0, 0));
		}
		else if (action == HousingAction.MOVE_OBJECT) { // move object
			HouseObject<?> obj = player.getHouseRegistry().getObjectByObjId(itemObjectId);
			if (obj == null) {
				return;
			}
			sendPacket(new SM_HOUSE_EDIT(actionId + 1, 0, itemObjectId));
			obj.getController().onDelete();
			obj.setX(x);
			obj.setY(y);
			obj.setZ(z);
			obj.setRotation(rotation);
			if (obj.getPersistentState() == PersistentState.UPDATE_REQUIRED) {
				player.getHouseRegistry().setPersistentState(PersistentState.UPDATE_REQUIRED);
			}
			sendPacket(new SM_HOUSE_EDIT(actionId - 1, itemObjectId, x, y, z, rotation));
			obj.spawn();
		}
		else if (action == HousingAction.DESPAWN_OBJECT) { // despawn object
			HouseObject<?> obj = player.getHouseRegistry().getObjectByObjId(itemObjectId);
			if (obj == null) {
				return;
			}
			sendPacket(new SM_HOUSE_EDIT(actionId, 0, itemObjectId));
			obj.getController().onDelete();
			obj.removeFromHouse();
			obj.clearKnownlist();
			player.getHouseRegistry().setPersistentState(PersistentState.UPDATE_REQUIRED);
			sendPacket(new SM_HOUSE_EDIT(3, 1, itemObjectId)); // place it back
		}
		else if (action == HousingAction.ENTER_RENOVATION) { // enter renovation mode
			sendPacket(new SM_HOUSE_EDIT(14));
		}
		else if (action == HousingAction.EXIT_RENOVATION) { // exit renovation mode
			sendPacket(new SM_HOUSE_EDIT(15));
		}
		else if (action == HousingAction.CHANGE_APPEARANCE) { // update appearence
			House house = player.getHouseRegistry().getOwner();
			if (!removeRenovationCoupon(player, house)) {
				AuditLogger.info(player, "Try house renovation without coupon");
				return;
			}
			HousingService.getInstance().switchHouseBuilding(house, buildingId);
			player.setHouseRegistry(house.getRegistry());
			house.getController().updateAppearance();
		}
	}

	private boolean removeRenovationCoupon(Player player, House house) {
		int typeId = house.getHouseType().getId();
		if (typeId == 0) {
			return false; // studio
		}
		int itemId = (player.getRace().equals(Race.ELYOS) ? 169661004 : 169661008) - typeId;
		if (player.getInventory().getItemCountByItemId(itemId) > 0) {
			return player.getInventory().decreaseByItemId(itemId, 1);
		}
		return false;
	}
}
