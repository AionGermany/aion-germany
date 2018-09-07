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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.configs.administration.AdminConfig;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.actions.PlayerMode;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.state.CreatureState;
import com.aionemu.gameserver.model.templates.zone.ZoneType;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.skillengine.effect.AbnormalState;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author SoulKeeper
 * @author_fix nerolory
 */
public class CM_EMOTION extends AionClientPacket {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(CM_EMOTION.class);
	/**
	 * Emotion number
	 */
	EmotionType emotionType;
	/**
	 * Emotion number
	 */
	int emotion;
	/**
	 * Coordinates of player
	 */
	float x;
	float y;
	float z;
	byte heading;
	int targetObjectId;

	/**
	 * Constructs new client packet instance.
	 *
	 * @param opcode
	 */
	public CM_EMOTION(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	/**
	 * Read data
	 */
	@Override
	protected void readImpl() {
		int et;
		et = readC();
		emotionType = EmotionType.getEmotionTypeById(et);

		switch (emotionType) {
			case SELECT_TARGET:// select target
			case JUMP: // jump
			case SIT: // resting
			case STAND: // end resting
			case LAND_FLYTELEPORT: // fly teleport land
			case FLY: // fly up
			case LAND: // land
			case RIDE:
			case RIDE_END:
			case DIE: // die
			case END_DUEL: // duel end
			case WALK: // walk on
			case RUN: // walk off
			case OPEN_DOOR: // open static doors
			case CLOSE_DOOR: // close static doors
			case POWERSHARD_ON: // powershard on
			case POWERSHARD_OFF: // powershard off
			case ATTACKMODE: // get equip weapon
			case ATTACKMODE2: // get equip weapon
			case NEUTRALMODE: // remove equip weapon
			case NEUTRALMODE2: // remove equip weapon
			case START_SPRINT:
			case END_SPRINT:
			case WINDSTREAM_STRAFE:
				break;
			case EMOTE:
				emotion = readH();
				targetObjectId = readD();
				break;
			case CHAIR_SIT: // sit on chair
			case CHAIR_UP: // stand on chair
				x = readF();
				y = readF();
				z = readF();
				heading = (byte) readC();
				break;
			case START_FLYBOOST_SPEED:
				emotion = readD();
				break;
			case END_FLYBOOST_SPEED:
				break;
			default:
				log.error("Unknown emotion type? 0x" + Integer.toHexString(et/* !!!!! */).toUpperCase());
				break;
		}
	}

	/**
	 * Send emotion packet
	 */
	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();
		if (player.getLifeStats().isAlreadyDead()) {
			return;
		}

		if (player.getEffectController().isAbnormalState(AbnormalState.CANT_MOVE_STATE2) || player.getEffectController().isUnderFear()) {
			return;
		}

		// check for stance
		if (player.getController().isUnderStance()) {
			if (emotionType == EmotionType.SIT || emotionType == EmotionType.JUMP || emotionType == EmotionType.NEUTRALMODE || emotionType == EmotionType.NEUTRALMODE2 || emotionType == EmotionType.ATTACKMODE || emotionType == EmotionType.ATTACKMODE2) {
				player.getController().stopStance();
				// return; // because you cannot jump or sit etc while under stance..
			}
		}

		if (player.getState() == CreatureState.PRIVATE_SHOP.getId() || player.isAttackMode() && (emotionType == EmotionType.CHAIR_SIT || emotionType == EmotionType.JUMP)) {
			return;
		}

		if (player.getState() == CreatureState.LOOTING.getId() && emotionType == EmotionType.NEUTRALMODE) {
			return;
		}

		player.getController().cancelUseItem();
		if (emotionType != EmotionType.SELECT_TARGET) {
			player.getController().cancelCurrentSkill();
		}

		switch (emotionType) {
			case SELECT_TARGET:
				return;
			case SIT:
				if (player.isInState(CreatureState.PRIVATE_SHOP)) {
					return;
				}
				player.setState(CreatureState.RESTING);
				break;
			case STAND:
				player.unsetState(CreatureState.RESTING);
				break;
			case CHAIR_SIT:
				if (!player.isInState(CreatureState.WEAPON_EQUIPPED)) {
					player.setState(CreatureState.CHAIR);
				}
				break;
			case CHAIR_UP:
				player.unsetState(CreatureState.CHAIR);
				break;
			case LAND_FLYTELEPORT:
				player.getController().onFlyTeleportEnd();
				break;
			case FLY:
				if (player.getAccessLevel() < AdminConfig.GM_FLIGHT_FREE) {
					if (!player.isInsideZoneType(ZoneType.FLY)) {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_FLYING_FORBIDDEN_HERE);
						return;
					}
				}
				// If player is under NoFly Effect, show the retail message for it and return
				if (player.isUnderNoFly()) {
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CANT_FLY_NOW_DUE_TO_NOFLY);
					return;
				}
				player.getFlyController().startFly();
				break;
			case LAND:
				player.getFlyController().endFly(false);
				break;
			case ATTACKMODE2:
			case ATTACKMODE:
				player.setAttackMode(true);
				player.setState(CreatureState.WEAPON_EQUIPPED);
				break;
			case NEUTRALMODE2:
			case NEUTRALMODE:
				player.setAttackMode(false);
				player.unsetState(CreatureState.WEAPON_EQUIPPED);
				break;
			case WALK:
				// cannot toggle walk when you flying or gliding
				if (player.getFlyState() > 0) {
					return;
				}
				player.setState(CreatureState.WALKING);
				break;
			case RUN:
				player.unsetState(CreatureState.WALKING);
				break;
			case OPEN_DOOR:
			case CLOSE_DOOR:
				break;
			case POWERSHARD_ON:
				if (!player.getEquipment().isPowerShardEquipped()) {
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_WEAPON_BOOST_NO_BOOSTER_EQUIPED);
					return;
				}
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_WEAPON_BOOST_BOOST_MODE_STARTED);
				player.setState(CreatureState.POWERSHARD);
				break;
			case POWERSHARD_OFF:
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_WEAPON_BOOST_BOOST_MODE_ENDED);
				player.unsetState(CreatureState.POWERSHARD);
				break;
			case START_SPRINT:
				if (!player.isInPlayerMode(PlayerMode.RIDE) || player.getLifeStats().getCurrentFp() < player.ride.getStartFp() || player.isInState(CreatureState.FLYING) || !player.ride.canSprint()) {
					return;
				}
				player.setSprintMode(true);
				player.getLifeStats().triggerFpReduceByCost(player.ride.getCostFp());
				break;
			case END_SPRINT:
				if (!player.isInPlayerMode(PlayerMode.RIDE) || !player.ride.canSprint()) {
					return;
				}
				player.setSprintMode(false);
				player.getLifeStats().triggerFpRestore();
				break;
			case START_FLYBOOST_SPEED:
				break;
			case END_FLYBOOST_SPEED:
				break;
			default:
				break;
		}

		if (player.getEmotions().canUse(emotion)) {
			PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, emotionType, emotion, x, y, z, heading, getTargetObjectId(player)), true);
		}
	}

	/**
	 * @param player
	 * @return
	 */
	private final int getTargetObjectId(Player player) {
		int target = player.getTarget() == null ? 0 : player.getTarget().getObjectId();
		return target != 0 ? target : this.targetObjectId;
	}
}
