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

import com.aionemu.gameserver.configs.main.GeoDataConfig;
import com.aionemu.gameserver.configs.main.HousingConfig;
import com.aionemu.gameserver.geoEngine.collision.CollisionIntention;
import com.aionemu.gameserver.geoEngine.math.Vector3f;
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.house.House;
import com.aionemu.gameserver.model.house.HousePermissions;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.HousingService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.geo.GeoService;

/**
 * @author Rolandas
 */
public class CM_HOUSE_OPEN_DOOR extends AionClientPacket {

	int address;
	boolean leave = false;

	public CM_HOUSE_OPEN_DOOR(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	@Override
	protected void readImpl() {
		address = readD();
		if (readC() != 0) {
			leave = true;
		}
	}

	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();
		if (player == null) {
			return;
		}

		if (player.getAccessLevel() >= 3 && HousingConfig.ENABLE_SHOW_HOUSE_DOORID) {
			PacketSendUtility.sendMessage(player, "House door id: " + address);
		}

		House house = HousingService.getInstance().getHouseByAddress(address);
		if (house == null) {
			return;
		}

		if (leave) {
			if (house.getAddress().getExitMapId() != null) {
				TeleportService2.teleportTo(player, house.getAddress().getExitMapId(), house.getAddress().getExitX(), house.getAddress().getExitY(), house.getAddress().getExitZ(), (byte) 0, TeleportAnimation.BEAM_ANIMATION);
			}
			else {
				if (GeoDataConfig.GEO_ENABLE) {
					Npc sign = house.getCurrentSign();
					byte flags = (byte) (CollisionIntention.PHYSICAL.getId() | CollisionIntention.DOOR.getId());
					Vector3f colSign = GeoService.getInstance().getClosestCollision(sign, player.getX(), player.getY(), player.getZ() + 2, false, flags);
					Vector3f colWall = GeoService.getInstance().getClosestCollision(player, colSign.getX(), colSign.getY(), colSign.getZ(), true, flags);
					double radian = Math.toRadians(MathUtil.calculateAngleFrom(player.getX(), player.getY(), colWall.x, colWall.y));
					float x = (float) (Math.cos(radian) * 0.1);
					float y = (float) (Math.sin(radian) * 0.1);
					TeleportService2.teleportTo(player, house.getWorldId(), colWall.getX() + x, colWall.getY() + y, player.getZ(), (byte) 0, TeleportAnimation.BEAM_ANIMATION);
				}
				else {
					double radian = Math.toRadians(MathUtil.convertHeadingToDegree(player.getHeading()));
					float x = (float) (Math.cos(radian) * 6);
					float y = (float) (Math.sin(radian) * 6);
					TeleportService2.teleportTo(player, house.getWorldId(), player.getX() + x, player.getY() + y, player.getZ(), (byte) 0, TeleportAnimation.BEAM_ANIMATION);
				}
			}
		}
		else {
			if (house.getOwnerId() != player.getObjectId()) {
				boolean allowed = false;
				if (house.getDoorState() == HousePermissions.DOOR_OPENED_FRIENDS) {
					allowed = player.getFriendList().getFriend(house.getOwnerId()) != null || (player.getLegion() != null && player.getLegion().isMember(house.getOwnerId()));
				}
				if (!allowed) {
					if (player.getAccessLevel() < HousingConfig.ENTER_HOUSE_ACCESSLEVEL) {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_CANT_ENTER_NO_RIGHT2);
						return;
					}
				}
			}
			double radian = Math.toRadians(MathUtil.convertHeadingToDegree(player.getHeading()));
			float x = (float) (Math.cos(radian) * 6);
			float y = (float) (Math.sin(radian) * 6);
			TeleportService2.teleportTo(player, house.getWorldId(), player.getX() + x, player.getY() + y, house.getAddress().getZ(), (byte) 0, TeleportAnimation.BEAM_ANIMATION);
		}
	}
}
