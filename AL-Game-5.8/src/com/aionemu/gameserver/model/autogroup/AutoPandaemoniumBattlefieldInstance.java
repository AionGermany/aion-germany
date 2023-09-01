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
package com.aionemu.gameserver.model.autogroup;

import java.util.List;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.instance.instancereward.PandaemoniumBattlefieldReward;
import com.aionemu.gameserver.model.team2.TeamType;
import com.aionemu.gameserver.model.team2.group.PlayerGroupService;
import com.aionemu.gameserver.network.aion.serverpackets.SM_AUTO_GROUP;
import com.aionemu.gameserver.services.AutoGroupService;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Falke_34
 */
public class AutoPandaemoniumBattlefieldInstance extends AutoInstance {

	@Override
	public AGQuestion addPlayer(Player player, SearchInstance searchInstance) {
		super.writeLock();
		try {
			if (!satisfyTime(searchInstance) || players.size() >= agt.getPlayerSize()) {
				return AGQuestion.FAILED;
			}
			players.put(player.getObjectId(), new AGPlayer(player));
			return (instance != null) ? AGQuestion.ADDED : ((players.size() == agt.getPlayerSize()) ? AGQuestion.READY : AGQuestion.ADDED);
		} finally {
			super.writeUnlock();
		}
	}

	@Override
	public void onEnterInstance(Player player) {
		super.onEnterInstance(player);
		List<Player> playersInside = instance.getPlayersInside();
		if (playersInside.size() == 1 && !playersInside.get(0).isInGroup2()) {
			int intValue = PlayerGroupService.createGroup(playersInside.get(0), player, TeamType.AUTO_GROUP).getObjectId();
			if (!instance.isRegistered(intValue)) {
				instance.register(intValue);
			}
		} else if (!playersInside.isEmpty() && playersInside.get(0).isInGroup2()) {
			PlayerGroupService.addPlayer(playersInside.get(0).getPlayerGroup2(), player);
		}
		Integer objectId = player.getObjectId();
		if (!instance.isRegistered(objectId)) {
			instance.register(objectId);
		}
	}

	@Override
	public void onPressEnter(Player player) {
		super.onPressEnter(player);
		if (this.agt.isPandaemoniumBattlefield()) {
			this.players.remove(player.getObjectId());
			PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 5));
			if (players.isEmpty()) {
				AutoGroupService.getInstance().unRegisterInstance(Integer.valueOf(instance.getInstanceId()));
			}
			return;
		}
		((PandaemoniumBattlefieldReward) instance.getInstanceHandler().getInstanceReward()).portToPosition(player);
		instance.register(player.getObjectId());
	}

	@Override
	public void onLeaveInstance(Player player) {
		super.unregister(player);
		PlayerGroupService.removePlayer(player);
	}
}
