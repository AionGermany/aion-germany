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
import com.aionemu.gameserver.services.NameRestrictionService;
import com.aionemu.gameserver.services.toypet.MinionService;
import com.aionemu.gameserver.services.toypet.PetSpawnService;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Falke_34, FrozenKiller
 */
public class CM_MINIONS extends AionClientPacket {

	private int actionId;
	private MinionAction action;
	private int ItemObjectId;
	private int minionObjId;
	private int lock;
	private int charge;
	private int autoCharge;
	private String rename = "";
	List<Integer> material = new ArrayList<Integer>();
	private int Upgradeslot;
	private int Upgradeslot2;
	private int Upgradeslot3;
	private int Upgradeslot4;
	private int growthtarget;
	private int growthtarget2;
	private int growthtarget3;
	private int growthtarget4;
	private int growthtarget5;
	private int growthtarget6;
	private int growthtarget7;
	private int growthtarget8;
	private int growthtarget9;
	private int growthtarget10;

	public CM_MINIONS(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	@Override
	protected void readImpl() {
		actionId = readH();
		action = MinionAction.getActionById(actionId);
		Player player = getConnection().getActivePlayer();
		switch (action) {
		case ADOPT:
			ItemObjectId = readD();
			break;
		case RENAME:
			minionObjId = readD();
			rename = readS();
			break;
		case DELETE:
			minionObjId = readD();
			break;
		case LOCK:
			minionObjId = readD();
			lock = readC();
			break;
		case SPAWN:
			minionObjId = readD();
			break;
		case DISMISS:
			minionObjId = readD();
			break;
		case GROWTH:
			material.clear();
			minionObjId = readD();
			growthtarget = readD();
			growthtarget2 = readD();
			growthtarget3 = readD();
			growthtarget4 = readD();
			growthtarget5 = readD();
			growthtarget6 = readD();
			growthtarget7 = readD();
			growthtarget8 = readD();
			growthtarget9 = readD();
			growthtarget10 = readD();
			material.add(growthtarget);
			material.add(growthtarget2);
			material.add(growthtarget3);
			material.add(growthtarget4);
			material.add(growthtarget5);
			material.add(growthtarget6);
			material.add(growthtarget7);
			material.add(growthtarget8);
			material.add(growthtarget9);
			material.add(growthtarget10);
			break;
		case USE_FUNCTION:
			break;
		case STOP_FUNCTION:
			break;
		case CHARGE:
			charge = readC(); // Charge 1 = true / 0 = false ? 
			autoCharge = readC(); // Auto Recharge on/off Todo
			break;
		case EVOLVE:
			minionObjId = readD();
			break;
		case COMBINE:
			material.clear();
			Upgradeslot = readD();
			Upgradeslot2 = readD();
			Upgradeslot3 = readD();
			Upgradeslot4 = readD();
			material.add(Upgradeslot);
			material.add(Upgradeslot2);
			material.add(Upgradeslot3);
			material.add(Upgradeslot4);
			break;
		case SET_FUNCTION:
			MinionService.getInstance().toggleAutoLoot(player, false);
			break;
		case FUNCTION_RENEW:
			readC();
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
		System.out.println("Action: " + action);
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
		case DELETE: {
			MinionService.getInstance().deleteMinion(player, minionObjId, true);
			break;
		}
		case RENAME: {
			if (NameRestrictionService.isForbiddenWord(rename)) {
				PacketSendUtility.sendMessage(player, "You are trying to use a forbidden name. Choose another one!");
				return;
			}
			MinionService.getInstance().renameMinion(player, minionObjId, rename);
			break;
		}
		case LOCK: {
			MinionService.getInstance().lockMinion(player, minionObjId, lock);
			break;
		}
		case SPAWN: {
			if (player.getPet() != null) {
				PetSpawnService.dismissPet(player, true);
			}
			MinionService.getInstance().spawnMinion(player, minionObjId);
			break;
		}
		case DISMISS: {
			MinionService.getInstance().despawnMinion(player, minionObjId);
			break;
		}
		case GROWTH: {
			MinionService.getInstance().growthUpMinion(player, minionObjId, material);
			break;
		}
		case EVOLVE: {
			MinionService.getInstance().evolutionUpMinion(player, minionObjId);
			break;
		}
		case COMBINE: {
			MinionService.getInstance().CombinationMinion(player, material);
			break;
		}
		case CHARGE: {
			MinionService.getInstance().chargeSkillPoint(player, charge == 1 ? true : false, autoCharge == 1 ? true : false);
			break;
		}
		case USE_FUNCTION: {
			MinionService.getInstance().activateMinionFunction(player);
			break;
		}
		case STOP_FUNCTION: {
			MinionService.getInstance().activateMinionFunction(player);
			break;
		}
		default:
			break;
		}
	}
}
