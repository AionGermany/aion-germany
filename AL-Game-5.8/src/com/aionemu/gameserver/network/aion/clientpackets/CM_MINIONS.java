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
import com.aionemu.gameserver.services.toypet.PetSpawnService;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Falke_34, FrozenKiller
 */
public class CM_MINIONS extends AionClientPacket {

	private int actionId;
	private String minionName;
	private int objectId;
	private int itemObjectId;
	@SuppressWarnings("unused")
	private boolean isSpawned; //Should be in DB (TODO)
	private int charge;
	private int autoCharge;
	private int functId;
	private int subSwitch;
	private int minionObjectId;
	private int dopingItemId;
	private int targetSlot;
	private int destinationSlot;
	private int unk;
	private ArrayList<Integer> MaterialObjIds = new ArrayList<Integer>();
	private int lock = 0;
	
	public CM_MINIONS(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	@Override
	protected void readImpl() {
		actionId = readH();
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
			case 5: // unsummon
				minionObjectId = readD(); // Minion Unique ID
				break;
			case 6: // ascension
				MaterialObjIds.clear();
				objectId = readD(); // Minion Unique ID
				for (int i = 0; i < 10; i++) {
					MaterialObjIds.add(readD());
				}
				break;
			case 7: //evolution
				objectId = readD();
				break;
			case 8://combination
				MaterialObjIds.clear();
				for (int i = 0; i < 4; i++) {
					MaterialObjIds.add(readD());
				}
				break;
			case 9: // TODO (MinionFunction Scrolls etc)
				subSwitch = readD(); //0, 1
				System.out.println("SubSwitch: "+subSwitch);
				switch (subSwitch) {
	                case 0: {
	                    functId = readD();
	                    switch(functId) {
		                    case 0:{//add item
		                    	minionObjectId = readD();
		                        dopingItemId = readD();
		                        targetSlot = readD();
		                        System.out.println("subSwitch: "+subSwitch+" functId: "+functId+" minionObjectId :"+minionObjectId+"\ndopingItemId :"+dopingItemId+" dopingItemId :"+dopingItemId+"\ntargetSlot :"+targetSlot+" targetSlot2 :"+destinationSlot+" unk :"+unk);
		                        break;
		                    }
		                    case 1:{
		                    	minionObjectId = readD();
		                        targetSlot = readD();
		                        unk = readD();
		                        System.out.println("subSwitch: "+subSwitch+" functId: "+functId+" minionObjectId :"+minionObjectId+"\ndopingItemId :"+dopingItemId+" dopingItemId :"+dopingItemId+"\ntargetSlot :"+targetSlot+" targetSlot2 :"+destinationSlot+" unk :"+unk);
		                        break;
		                    }
		                    case 2:{
		                        minionObjectId = readD();
		                        targetSlot = readD();
		                        destinationSlot = readD();
		                        System.out.println("subSwitch: "+subSwitch+" functId: "+functId+" minionObjectId :"+minionObjectId+"\ndopingItemId :"+dopingItemId+" dopingItemId :"+dopingItemId+"\ntargetSlot :"+targetSlot+" targetSlot2 :"+destinationSlot+" unk :"+unk);
		                        break;
		                    }
		                    case 3:{//BUFF ON
		                        minionObjectId = readD();
		                        dopingItemId = readD();
		                        targetSlot = readD();
		                        System.out.println("subSwitch: "+subSwitch+" functId: "+functId+" minionObjectId :"+minionObjectId+"\ndopingItemId :"+dopingItemId+" dopingItemId :"+dopingItemId+"\ntargetSlot :"+targetSlot+" targetSlot2 :"+destinationSlot+" unk :"+unk);
		                        break;
		                    }
		                    case 4:{
		                        minionObjectId = readD();
		                        System.out.println("subSwitch: "+subSwitch+" functId: "+functId+" minionObjectId :"+minionObjectId+"\ndopingItemId :"+dopingItemId+" dopingItemId :"+dopingItemId+"\ntargetSlot :"+targetSlot+" targetSlot2 :"+destinationSlot+" unk :"+unk);
		                        break;
		                    }
	                    }
	                    break;
	                }
	                case 1: {//Auto Loot
	                    minionObjectId = readD();
	                    break;
	                }
				}
				break;
			case 10: //Nothing to read (Falke Log 5.6_Minion_Function)
				break;
			case 11: // charge
				charge = readC(); // Charge 1 = true / 0 = false ? 
				autoCharge = readC(); // Auto Recharge on/off Todo
				break;
			case 12:
				readC(); // Auto Function on/off
				break;
            case 13:
            	readD();
            	readC();
            	readH();
            	break;
            case 14: // BUFF ON
            	readC(); // 20?
            	readC();
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
		switch (actionId) {
			case 0:
				MinionService.getInstance().addMinion(player, itemObjectId);
				break;
			case 1:
				MinionService.getInstance().deleteMinion(player, objectId, false);
				break;
			case 2:
				if (NameRestrictionService.isForbiddenWord(minionName)) {
					PacketSendUtility.sendMessage(player, "You are trying to use a forbidden name. Choose another one!");
				}
				else {
					MinionService.getInstance().renameMinion(player, objectId, minionName);
				}
				break;				
			case 3:
				MinionService.getInstance().lockMinion(player, objectId, lock);
				break;
			case 4:
				if (player.getPet() != null) {
					PetSpawnService.dismissPet(player, true);
				}
				MinionService.getInstance().spawnMinion(player, minionObjectId);
				break;
			case 5:
				MinionService.getInstance().despawnMinion(player, minionObjectId);
				break;
			case 6:
				MinionService.getInstance().growthUpMinion(player, objectId, MaterialObjIds);
				break;
			case 7:
				MinionService.getInstance().evolutionUpMinion(player, objectId);
				break;
			case 8:
				MinionService.getInstance().CombinationMinion(player, MaterialObjIds);
				break;
			case 9: // TODO
				switch(subSwitch) {
					case 0: {
						switch(functId) {
							case 0: { //Add Item
								System.out.println("ITEM_ADD");
			                    MinionService.getInstance().addMinionFunctionItems(player, functId, minionObjectId, dopingItemId, targetSlot, destinationSlot);  // Scrolls etc
								break;
							}
							case 2: {
								System.out.println("XD");
								MinionService.getInstance().relocateDoping(player, minionObjectId, targetSlot, destinationSlot);
								break;
							}
							case 3: {
								System.out.println("BUFF_ON");
								 MinionService.getInstance().buffPlayer(player, minionObjectId, dopingItemId, targetSlot); //Buff
								break;
							}
						}
						break;
					}
					case 1: {
						System.out.println("AUTOLOOT_ACTIVATION_DEACTIVATION");
	                	MinionService.getInstance().activateLoot(player, true);
						break;
					}
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
			case 12:
				break;
			case 13:
				break;
			case 14:
				break;
		default:
			break;
		}
	}
}
