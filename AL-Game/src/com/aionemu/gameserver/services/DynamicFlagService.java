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
package com.aionemu.gameserver.services;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_FLAG_INFO;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

public class DynamicFlagService {

    public void onEnterWorld(Player player) {
        if (player.getWorldId() == 210010000) {
            World.getInstance().getWorldMap(player.getWorldId()).getMainWorldMapInstance().doOnAllPlayers(new Visitor<Player>(){

                @Override
                public void visit(Player player) {
                    for (VisibleObject npc : World.getInstance().getNpcs()) {
                        if (npc.getObjectTemplate().getTemplateId() == 651878 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        player.getController().updateZone();
                    }
                }
            });
        }
        if (player.getWorldId() == 210040000) {
            World.getInstance().getWorldMap(player.getWorldId()).getMainWorldMapInstance().doOnAllPlayers(new Visitor<Player>(){

                @Override
                public void visit(Player player) {
                    for (VisibleObject npc : World.getInstance().getNpcs()) {
                        if (npc.getObjectTemplate().getTemplateId() == 212008 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        player.getController().updateZone();
                    }
                }
            });
        }
        if (player.getWorldId() == 210050000) {
            World.getInstance().getWorldMap(player.getWorldId()).getMainWorldMapInstance().doOnAllPlayers(new Visitor<Player>(){

                @Override
                public void visit(Player player) {
                    for (VisibleObject npc : World.getInstance().getNpcs()) {
                        if (npc.getObjectTemplate().getTemplateId() == 832830 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 661657 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 661658 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 661659 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        player.getController().updateZone();
                    }
                }
            });
        }
        if (player.getWorldId() == 220010000) {
            World.getInstance().getWorldMap(player.getWorldId()).getMainWorldMapInstance().doOnAllPlayers(new Visitor<Player>(){

                @Override
                public void visit(Player player) {
                    for (VisibleObject npc : World.getInstance().getNpcs()) {
                        if (npc.getObjectTemplate().getTemplateId() == 651806 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        player.getController().updateZone();
                    }
                }
            });
        }
        if (player.getWorldId() == 220040000) {
            World.getInstance().getWorldMap(player.getWorldId()).getMainWorldMapInstance().doOnAllPlayers(new Visitor<Player>(){

                @Override
                public void visit(Player player) {
                    for (VisibleObject npc : World.getInstance().getNpcs()) {
                        if (npc.getObjectTemplate().getTemplateId() == 652353 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        player.getController().updateZone();
                    }
                }
            });
        }
        if (player.getWorldId() == 220070000) {
            World.getInstance().getWorldMap(player.getWorldId()).getMainWorldMapInstance().doOnAllPlayers(new Visitor<Player>(){

                @Override
                public void visit(Player player) {
                    for (VisibleObject npc : World.getInstance().getNpcs()) {
                        if (npc.getObjectTemplate().getTemplateId() == 832831 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 661796 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 661797 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 661798 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        player.getController().updateZone();
                    }
                }
            });
        }
        if (player.getWorldId() == 400070000) {
            World.getInstance().getWorldMap(player.getWorldId()).getMainWorldMapInstance().doOnAllPlayers(new Visitor<Player>(){

                @Override
                public void visit(Player player) {
                    for (VisibleObject npc : World.getInstance().getNpcs()) {
                        if (npc.getObjectTemplate().getTemplateId() == 835738 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 835739 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 835740 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 835741 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 835742 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 835743 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 835744 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 835745 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 835746 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 835747 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 835748 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 835749 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 835750 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 835751 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 835752 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 835753 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 835754 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 835755 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 835756 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 835757 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 835758 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        player.getController().updateZone();
                    }
                }
            });
        }
        if (player.getWorldId() == 600010000) {
            World.getInstance().getWorldMap(player.getWorldId()).getMainWorldMapInstance().doOnAllPlayers(new Visitor<Player>(){

                @Override
                public void visit(Player player) {
                    for (VisibleObject npc : World.getInstance().getNpcs()) {
                        if (npc.getObjectTemplate().getTemplateId() == 858868 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 858869 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 858870 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 858871 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 858872 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        player.getController().updateZone();
                    }
                }
            });
        }
        if (player.getWorldId() == 600040000) {
            World.getInstance().getWorldMap(player.getWorldId()).getMainWorldMapInstance().doOnAllPlayers(new Visitor<Player>(){

                @Override
                public void visit(Player player) {
                    for (VisibleObject npc : World.getInstance().getNpcs()) {
                        if (npc.getObjectTemplate().getTemplateId() == 218553 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 219311 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        player.getController().updateZone();
                    }
                }
            });
        }
        if (player.getWorldId() == 800030000) {
            World.getInstance().getWorldMap(player.getWorldId()).getMainWorldMapInstance().doOnAllPlayers(new Visitor<Player>(){

                @Override
                public void visit(Player player) {
                    for (VisibleObject npc : World.getInstance().getNpcs()) {
                        if (npc.getObjectTemplate().getTemplateId() == 807067 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 807068 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 807069 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 807070 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 807071 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 807072 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 807073 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 807233 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 838561 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 838562 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 838563 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 838564 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 838565 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 838566 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 838567 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 838568 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 838569 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 838570 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 838571 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 838572 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 838573 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 838574 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 838575 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 838576 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 838577 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 838578 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 838579 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 838580 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 838581 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 838582 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 838583 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 838584 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 838585 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 838586 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 838587 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 838588 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 838589 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 838590 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 838591 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 838592 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 838593 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 838594 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 838595 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 838596 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 838597 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 838598 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 838599 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 839266 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 839595 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        player.getController().updateZone();
                    }
                }
            });
        }
        if (player.getWorldId() == 800040000) {
            World.getInstance().getWorldMap(player.getWorldId()).getMainWorldMapInstance().doOnAllPlayers(new Visitor<Player>(){

                @Override
                public void visit(Player player) {
                    for (VisibleObject npc : World.getInstance().getNpcs()) {
                        if (npc.getObjectTemplate().getTemplateId() == 807220 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 807221 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 839836 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 839837 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 839838 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 839839 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 839840 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 839841 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 839842 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 839843 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        player.getController().updateZone();
                    }
                }
            });
        }
        if (player.getWorldId() == 800050000) {
            World.getInstance().getWorldMap(player.getWorldId()).getMainWorldMapInstance().doOnAllPlayers(new Visitor<Player>(){

                @Override
                public void visit(Player player) {
                    for (VisibleObject npc : World.getInstance().getNpcs()) {
                        if (npc.getObjectTemplate().getTemplateId() == 807243 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 655120 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 655121 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 655122 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 655123 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 655124 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 655240 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        player.getController().updateZone();
                    }
                }
            });
        }
        if (player.getWorldId() == 800060000) {
            World.getInstance().getWorldMap(player.getWorldId()).getMainWorldMapInstance().doOnAllPlayers(new Visitor<Player>(){

                @Override
                public void visit(Player player) {
                    for (VisibleObject npc : World.getInstance().getNpcs()) {
                        if (npc.getObjectTemplate().getTemplateId() == 658554 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 838291 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 838292 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 858116 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 858117 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 858118 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 858119 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 858120 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 858121 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 858122 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 858123 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 858124 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 858125 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        player.getController().updateZone();
                    }
                }
            });
        }
        if (player.getWorldId() == 800070000) {
            World.getInstance().getWorldMap(player.getWorldId()).getMainWorldMapInstance().doOnAllPlayers(new Visitor<Player>(){

                @Override
                public void visit(Player player) {
                    for (VisibleObject npc : World.getInstance().getNpcs()) {
                        if (npc.getObjectTemplate().getTemplateId() == 807389 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        if (npc.getObjectTemplate().getTemplateId() == 807390 && npc.isSpawned()) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                        player.getController().updateZone();
                    }
                }
            });
        }
    }

	public static DynamicFlagService getInstance() {
		return SingletonHolder.instance;
	}

	private static class SingletonHolder {

		protected static final DynamicFlagService instance = new DynamicFlagService();
	}
}

