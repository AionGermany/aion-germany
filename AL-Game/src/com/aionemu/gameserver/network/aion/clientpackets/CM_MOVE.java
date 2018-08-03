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

import com.aionemu.gameserver.configs.main.SecurityConfig;
import com.aionemu.gameserver.controllers.movement.MovementMask;
import com.aionemu.gameserver.controllers.movement.PlayerMoveController;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.state.CreatureVisualState;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MOVE;
import com.aionemu.gameserver.services.antihack.AntiHackService;
import com.aionemu.gameserver.skillengine.effect.AbnormalState;
import com.aionemu.gameserver.taskmanager.tasks.TeamMoveUpdater;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

/**
 * Packet about player movement.
 *
 * @author -Nemesiss-
 */
public class CM_MOVE extends AionClientPacket {

	private byte type;
	private byte heading;
	private float x;
	private float y;
	private float z;
	private float x2;
	private float y2;
	private float z2;
	private float vehicleX;
	private float vehicleY;
	private float vehicleZ;
	private float vectorX;
	private float vectorY;
	private float vectorZ;
	private byte glideFlag;
	private int unk1;
	private int unk2;
	
	public CM_MOVE(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	@Override
	protected void readImpl() {
		Player player = getConnection().getActivePlayer();

		if (player == null || !player.isSpawned()) {
			return;
		}

		x = readF();
		y = readF();
		z = readF();

		heading = (byte) readC();
		type = (byte) readC();

		if ((type & MovementMask.STARTMOVE) == MovementMask.STARTMOVE) {
			if ((type & MovementMask.MOUSE) == 0) {
				vectorX = readF();
				vectorY = readF();
				vectorZ = readF();
				x2 = vectorX + x;
				y2 = vectorY + y;
				z2 = vectorZ + z;
			}
			else {
				x2 = readF();
				y2 = readF();
				z2 = readF();
			}
		}
		if ((type & MovementMask.GLIDE) == MovementMask.GLIDE) {
			glideFlag = (byte) readC();
		}
		if ((type & MovementMask.VEHICLE) == MovementMask.VEHICLE) {
			unk1 = readD();
			unk2 = readD();
			vehicleX = readF();
			vehicleY = readF();
			vehicleZ = readF();
		}
	}

	@Override
	protected void runImpl() {
		final Player player = getConnection().getActivePlayer();
		// packet was not read correctly
		if (player.getLifeStats().isAlreadyDead()) {
			return;
		}

		if (player.getEffectController().isAbnormalState(AbnormalState.CANT_MOVE_STATE) || player.getEffectController().isUnderFear()) {
			return;
		}

		PlayerMoveController m = player.getMoveController();
		m.movementMask = type;

		// Admin Teleportation
		if (player.getAdminTeleportation() && ((type & MovementMask.STARTMOVE) == MovementMask.STARTMOVE) && ((type & MovementMask.MOUSE) == MovementMask.MOUSE)) {
			m.setNewDirection(x2, y2, z2);
			World.getInstance().updatePosition(player, x2, y2, z2, heading);
			PacketSendUtility.broadcastPacketAndReceive(player, new SM_MOVE(player));
		}

		float speed = player.getGameStats().getMovementSpeedFloat();
		if ((type & MovementMask.GLIDE) == MovementMask.GLIDE) {
			m.glideFlag = glideFlag;
			player.getFlyController().switchToGliding();
		}
		else {
			player.getFlyController().onStopGliding(false);
		}

		if (type == 0) {
			player.getController().onStopMove();
			player.getFlyController().onStopGliding(false);
		}
		else if ((type & MovementMask.STARTMOVE) == MovementMask.STARTMOVE) {
			if ((type & MovementMask.MOUSE) == 0) {
				speed = player.getGameStats().getMovementSpeedFloat();
				m.vectorX = vectorX;
				m.vectorY = vectorY;
				m.vectorZ = vectorZ;
			}
			player.getMoveController().setNewDirection(x2, y2, z2, heading);
			player.getController().onStartMove();
		}
		else {
			player.getController().onMove();
			if ((type & MovementMask.MOUSE) == 0) {
				speed = player.getGameStats().getMovementSpeedFloat();
				player.getMoveController().setNewDirection(x + m.vectorX * speed * 1.5f, y + m.vectorY * speed * 1.5f, z + m.vectorZ * speed * 1.5f, heading);
			}
		}

		if ((type & MovementMask.VEHICLE) == MovementMask.VEHICLE) {
			m.unk1 = unk1;
			m.unk2 = unk2;
			m.vehicleX = vehicleX;
			m.vehicleY = vehicleY;
			m.vehicleZ = vehicleZ;
		}

		if (!AntiHackService.canMove(player, x, y, z, speed, type)) {
			return;
		}

		World.getInstance().updatePosition(player, x, y, z, heading);
		m.updateLastMove();

		if (player.isInGroup2() || player.isInAlliance2()) {
			TeamMoveUpdater.getInstance().startTask(player);
		}

		if ((type & MovementMask.STARTMOVE) == MovementMask.STARTMOVE || type == 0) {
			player.getKnownList().doOnAllPlayers(new Visitor<Player>() {

				@Override
				public void visit(Player observer) {
					if (observer.isOnline()) {
						if (SecurityConfig.INVIS && (!observer.canSee(player) || player.isInVisualState(CreatureVisualState.BLINKING))) {
							return;
						}

						PacketSendUtility.sendPacket(observer, new SM_MOVE(player));
					}
				}
			});
		}

		if ((type & MovementMask.FALL) == MovementMask.FALL) {
			m.updateFalling(z);
		}
		else {
			m.stopFalling();
		}

		if (type != 0 && player.isProtectionActive()) {
			player.getController().stopProtectionActiveTask();
		}
	}

	@Override
	public String toString() {
		return "CM_MOVE [type=" + type + ", heading=" + heading + ", x=" + x + ", y=" + y + ", z=" + z + ", x2=" + x2 + ", y2=" + y2 + ", z2=" + z2 + ", vehicleX=" + vehicleX + ", vehicleY=" + vehicleY + ", vehicleZ=" + vehicleZ + ", vectorX=" + vectorX + ", vectorY=" + vectorY + ", vectorZ=" + vectorZ + ", glideFlag=" + glideFlag + ", unk1=" + unk1 + ", unk2=" + unk2 + "]";
	}
}
