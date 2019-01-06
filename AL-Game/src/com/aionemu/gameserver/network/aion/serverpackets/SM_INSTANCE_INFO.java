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

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PortalCooldownList;
import com.aionemu.gameserver.model.team2.TemporaryPlayerTeam;
import com.aionemu.gameserver.model.templates.InstanceCooltime;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

import javolution.util.FastMap;

/**
 * @author nrg
 */
public class SM_INSTANCE_INFO extends AionServerPacket {

	private Player player;
	private boolean isAnswer;
	private int cooldownId;
	private int worldId;
	private TemporaryPlayerTeam<?> playerTeam;

	public SM_INSTANCE_INFO(Player player, boolean isAnswer, TemporaryPlayerTeam<?> playerTeam) {
		this.player = player;
		this.isAnswer = isAnswer;
		this.playerTeam = playerTeam;
		this.worldId = 0;
		this.cooldownId = 0;
	}

	public SM_INSTANCE_INFO(Player player, int instanceId) {
		this.player = player;
		this.isAnswer = false;
		this.playerTeam = null;
		this.worldId = instanceId;
		this.cooldownId = DataManager.INSTANCE_COOLTIME_DATA.getInstanceCooltimeByWorldId(instanceId) != null ? DataManager.INSTANCE_COOLTIME_DATA.getInstanceCooltimeByWorldId(instanceId).getId() : 0;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		boolean hasTeam = playerTeam != null;
		writeC(!isAnswer ? 2 : hasTeam ? 1 : 0);
		writeC(cooldownId);
		writeD(0);
		writeH(1);
		if (cooldownId == 0) {
			writeD(player.getObjectId());
			writeH(DataManager.INSTANCE_COOLTIME_DATA.size());
			PortalCooldownList cooldownList = player.getPortalCooldownList();
			for (FastMap.Entry<Integer, InstanceCooltime> e = DataManager.INSTANCE_COOLTIME_DATA.getAllInstances().head(), end = DataManager.INSTANCE_COOLTIME_DATA.getAllInstances().tail(); (e = e.getNext()) != end;) {
				writeD(e.getValue().getId());
				writeD(0x0);
				if (cooldownList.getPortalCooldown(e.getValue().getWorldId()) == 0) {
					writeD(0x0);
				}
				else {
					writeD((int) (cooldownList.getPortalCooldown(e.getValue().getWorldId()) - System.currentTimeMillis()) / 1000);
				}
				writeD(DataManager.INSTANCE_COOLTIME_DATA.getInstanceEntranceCountByWorldId(e.getKey()));
				writeD(cooldownList.getPortalCooldownItem(e.getValue().getWorldId()) != null ? cooldownList.getPortalCooldownItem(e.getValue().getWorldId()).getEntryCount() * -1 : 0);
				writeD(0); // 4.9
				writeD(0); // 4.9
				writeD(0); // Unk 5.1
				writeD(0); // Unk 6.x
				writeC(1); // activated
			}
			writeS(player.getName());
		}
		else {
			writeD(player.getObjectId());
			writeH(player.getPortalCooldownList().size()); // TEST
			for (int i = 0; i < player.getPortalCooldownList().size(); i++) {
				writeD(cooldownId); // InstanceID
				writeD(0);
				writeD(0);
				writeD(DataManager.INSTANCE_COOLTIME_DATA.getInstanceEntranceCountByWorldId(worldId)); // Max Entry's
				writeD(player.getPortalCooldownList().getPortalCooldownItem(worldId) != null ? player.getPortalCooldownList().getPortalCooldownItem(worldId).getEntryCount() * -1 : 0); // Used Entry's
																																														// (negative -)
				writeD(0); // 4.9
				writeD(0); // 4.9
				writeD(0); // Unk 5.1
				writeD(0); // Unk 6.x
				writeC(1); // activated
			}
			writeS(player.getName());
		}
	}
}
