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

import com.aionemu.gameserver.model.gameobjects.MinionAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.MinionService;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Falke_34, FrozenKiller
 */
public class CM_MINIONS extends AionClientPacket {

	private ArrayList<Integer> sacrificeMinions = new ArrayList<Integer>();
	private int actionId;
	private MinionAction action;
	private int subAction;
	private int itemObjId;
	private int minionObjId;
	private int unk2;
	private int lock;
	private int isAuto;
	private int minionToEvolve;
	private int minionToGrowth;
	private int conbine1;
	private int conbine2;
	private int conbine3;
	private int conbine4;
	private int activateLoot;
	private int dopingItemId;
	private int dopingAction;
	private int targetSlot;
	private int destinationSlot;
	private String name;

	public CM_MINIONS(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	protected void readImpl() {
		actionId = readH();
		action = MinionAction.getActionById(actionId);
		switch (action) {
		case ADOPT: {
			itemObjId = readD();
			break;
		}
		case DISMISS: {
			minionObjId = readD();
			break;
		}
		case RENAME: {
			minionObjId = readD();
			name = readS();
			break;
		}
		case LOCK: {
			minionObjId = readD();
			lock = readC();
			break;
		}
		case SUMMON:
		case UNSUMMON: {
			minionObjId = readD();
			break;
		}
		case GROWTH: {
			sacrificeMinions.clear();
			minionToGrowth = readD();
			for (int i = 0; i < 10; ++i) {
				sacrificeMinions.add(readD());
			}
			break;
		}
		case EVOLVE: {
			minionToEvolve = readD();
			break;
		}
		case COMBINE: {
			conbine1 = readD();
			conbine2 = readD();
			conbine3 = readD();
			conbine4 = readD();
			break;
		}
		case FUNCTION_SETTING: {
			switch (subAction = readD()) {
			case 1: {
				minionObjId = readD();
				activateLoot = readC();
				readD();
				readD();
				break;
			}
			case 0: {
				switch (dopingAction) {
				case 0: {
					minionObjId = readD();
					dopingItemId = readD();
					targetSlot = readD();
					break;
				}
				case 1: {
					minionObjId = readD();
					dopingItemId = readD();
					targetSlot = readD();
					break;
				}
				case 2: {
					minionObjId = readD();
					dopingItemId = readD();
					targetSlot = readD();
					destinationSlot = readD();
					break;
				}
				case 3: {
					minionObjId = readD();
					dopingItemId = readD();
					targetSlot = readD();
					break;
				}
				}
				break;
			}
			}
		}
		case ENERGY_RECHARGE: {
			unk2 = readC();
			isAuto = readC();
			break;
		}
		case AUTO_FUNCTION: {
			isAuto = readC();
			break;
		}
		case UNK: {
			readD();
			readC();
			readH();
			break;
		}
		case BUFFING: {
			readC();
			readC();
			readC();
			break;
		}
		default:
			break;
		}

	}

	protected void runImpl() {
        Player player = getConnection().getActivePlayer();
            switch (action) {
                case ADOPT: {
                    if (player.getMinionList().getMinions().size() >= 30) {
                        PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1404322));
                        break;
                    }
                    MinionService.getInstance().makeMinion(player, itemObjId);
                    break;
                }
                case DISMISS: {
                    MinionService.getInstance().dismissMinion(player, minionObjId);
                    break;
                }
                case RENAME: {
                    MinionService.getInstance().renameMinion(player, minionObjId, name);
                    break;
                }
                case LOCK: {
                    MinionService.getInstance().lockMinion(player, minionObjId, lock);
                    break;
                }
                case SUMMON: {
                    MinionService.getInstance().spawnMinion(player, minionObjId);
                    break;
                }
                case UNSUMMON: {
                    MinionService.getInstance().despawnMinion(player, minionObjId);
                    break;
                }
                case GROWTH: {
                    MinionService.getInstance().growthUpMinion(player, minionToGrowth, sacrificeMinions);
                    break;
                }
                case EVOLVE: {
                    MinionService.getInstance().EvolveMinion(player, minionToEvolve);
                    break;
                }
                case COMBINE: {
                    MinionService.getInstance().CombineMinion(player, conbine1, conbine2, conbine3, conbine4);
                    break;
                }
                case ENERGY_RECHARGE: {
                    MinionService.getInstance().EnergyRecharge(player, isAuto);
                    break;
                }
                case AUTO_FUNCTION: {
                    MinionService.getInstance().activateMinionFunction(player);
                    break;
                }
                case FUNCTION_SETTING: {
                    switch (subAction) {
                        case 1: {
                            MinionService.getInstance().activateLoot(player, minionObjId, activateLoot != 0);
                            break;
                        }
                        case 0: {
                            switch (dopingAction) {
                                case 0: {}
                                case 2: {
                                    break;
                                }
                            }
                            break;
                        }
                    }
                    break;
                }
			default:
				break;
            }
        }
}
