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
package com.aionemu.gameserver.network.aion.serverpackets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.teleport.TeleportLocation;
import com.aionemu.gameserver.model.templates.teleport.TeleporterTemplate;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;

import javolution.util.FastList;

/**
 * @author alexa026 , orz
 */
public class SM_TELEPORT_MAP extends AionServerPacket {

	private int targetObjectId;
	private Player player;
	private TeleporterTemplate teleport;
	public Npc npc;
	private static final Logger log = LoggerFactory.getLogger(SM_TELEPORT_MAP.class);
	private static final FastList<Integer> disableTeleportNpcs = new FastList<Integer>();

	public SM_TELEPORT_MAP(Player player, int targetObjectId, TeleporterTemplate teleport) {
		this.player = player;
		this.targetObjectId = targetObjectId;
		this.npc = (Npc) World.getInstance().findVisibleObject(targetObjectId);
		this.teleport = teleport;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		for (String s : CustomConfig.DISABLE_TELEPORTER_NPCS.split(",")) {
			disableTeleportNpcs.add(Integer.parseInt(s));
		}

		if (teleport != null && teleport.getTeleportId() != 0) {
			writeD(targetObjectId);
			writeH(teleport.getTeleportId());
			if (disableTeleportNpcs.contains(npc.getNpcId())) {
				for (Integer npcId : disableTeleportNpcs) {
					if (npc.getNpcId() == npcId) {
						writeH(teleport.getTeleLocIdData().getTelelocations().size());
						for (TeleportLocation locationid : teleport.getTeleLocIdData().getTelelocations()) {
							writeD(locationid.getLocId());
						}
					}
					else {
						continue;
					}
				}
			}
			else {
				writeH(0);
			}
		}
		else {
			PacketSendUtility.sendMessage(player, "Missing info at npc_teleporter.xml with npcid: " + npc.getNpcId());
			log.info(String.format("Missing teleport info with npcid: %d", npc.getNpcId()));
		}
		disableTeleportNpcs.clear();
	}
}
