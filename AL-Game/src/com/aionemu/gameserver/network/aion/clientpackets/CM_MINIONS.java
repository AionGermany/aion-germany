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

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.services.NameRestrictionService;
import com.aionemu.gameserver.services.toypet.MinionService;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Falke_34, FrozenKiller
 */
public class CM_MINIONS extends AionClientPacket {

	private int actionId;
	private String minionName;
	private int objectId;
	private int itemObjectId;
	private boolean isSpawned; //Should be in DB (TODO)
	private int charge;
	private int autoCharge;
	private int itemId;
	private int itemSlot;
	private int subSwitch;
	private int value;
	private int value1;
	private int value2;
	private int value3;
	private ArrayList<Integer> ids = new ArrayList<Integer>(); //new
	private int lock = 0;//new
	
	public CM_MINIONS(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	@Override
	protected void readImpl() {
		actionId = readH();
		System.out.println("ActionId 1: " + actionId);
		switch (actionId) {
			case 0: // add
				itemObjectId = readD(); // Item UniqueId (Minion Contract)
				break;
			case 1: // delete
				objectId = readD();
				break;
			case 2: // rename
				objectId = readD(); // Minion Unique ID
				minionName = readS(); // Name
				break;
			case 3: // locked
                objectId = readD(); // Minion Unique ID
                lock = readC(); // lock/unlock Todo
                break;
			case 4: // summon
				objectId = readD(); // Minion Unique ID
				break;
			case 5: // unsummon
				objectId = readD(); // Minion Unique ID
				break;
			case 6: // ascension new
				objectId = readD();
				for (int i = 0; i < 10; i++) {
					int a = readD();
					ids.add(a);
				}
				break;
			case 7: //evolution new
				objectId = readD();
				break;
			case 8://combination new
				for (int i = 0; i < 4; i++) {
					int a = readD();
					System.out.println("ActionId 8: " + i + " Index   Ertek: " + a);
					ids.add(a);
				}
				break;
			case 9: // TODO (MinionFunction Scrolls etc)
				subSwitch = readD(); //0, 1
				value = readD(); // 0, 3 or Minion Unique ID / subSwitch (1) = Minion Unique ID
				value1 = readD(); //subSwitch + value (0) =  Minion Unique ID 
				value2 = readD(); //subSwitch + value (0) = ItemId
				value3 = readD(); //subSwitch + value (0) = ItemSlot
				break;
			case 10: //Nothing to read (Falke Log 5.6_Minion_Function)
				break;
			case 11: // charge
				charge = readC(); // Charge 1 = true / 0 = false ? 
				autoCharge = readC(); // Auto Recharge on/off Todo
				break;
			case 12:
				readH(); // TODO AutoExtension MiolFunktion
				break;
            case 13:
            	readD();
            	readC();
            	readH();
            	break;
		}
	}

	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();
		if (player == null) {
			return;
		}
		System.out.println("ActionId 2: " + actionId);
		switch (actionId) {
			case 0:
				MinionService.getInstance().addMinion(player, itemObjectId); // TODO
				break;
			case 1:
				MinionService.getInstance().deleteMinion(player, objectId);
				break;
			case 2:
				if (NameRestrictionService.isForbiddenWord(minionName)) {
					PacketSendUtility.sendMessage(player, "You are trying to use a forbidden name. Choose another one!");
				}
				else {
					MinionService.getInstance().renameMinion(player, objectId, minionName);
				}
				break;				
			case 3://lock
				MinionService.getInstance().lockMinion(player, objectId, lock);
				break;
			case 4:
				MinionService.getInstance().spawnMinion(player, objectId);
				break;
			case 5:
				MinionService.getInstance().despawnMinion(player, objectId);
				break;
			case 6:
				//new
				MinionService.getInstance().growthUpMinion(player, ids, objectId);
				break;
			case 7:
				//new
				MinionService.getInstance().evolutionUpMinion(player, objectId);
				break;
			case 8:
				//new
				MinionService.getInstance().CombinationMinion(player, ids);
				break;
			case 9: // TODO
				if (subSwitch == 0 && value == 0) {
					MinionService.getInstance().addMinionFunctionItems(player, 0, value, value1, value2, value3); // Scrolls etc
				} else if (subSwitch == 1 && value > 3) {
					//Send SystemMessage 1400876 (Loot Deaktivated)
					MinionService.getInstance().deaktivateLoot(player, 1, value, value1, value2, value3); //Loot Deaktivated
					return;
				} else {
					MinionService.getInstance().buffPlayer(player, 768, value, value1, value2, value3); //Buff
				}
				break;
			case 10: // MinionFunction (Activate)
				MinionService.getInstance().activateMinionFunction(player);
				break;
			case 11:
				MinionService.getInstance().addMinionSkillPoints(player, charge == 1 ? true : false, autoCharge == 1 ? true : false);
				// TODO
				//MinionService.getInstance().chargeMinion(player, todo2 == 1 ? true : false);
				break;
			case 13:
				break;
		}
	}
}
