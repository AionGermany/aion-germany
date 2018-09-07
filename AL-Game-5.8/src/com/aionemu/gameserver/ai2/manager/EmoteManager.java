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
package com.aionemu.gameserver.ai2.manager;

import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.state.CreatureState;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author ATracer
 */
public class EmoteManager {

	/**
	 * Npc starts attacking from idle state
	 *
	 * @param owner
	 */
	public static final void emoteStartAttacking(Npc owner) {
		Creature target = (Creature) owner.getTarget();
		owner.unsetState(CreatureState.WALKING);
		if (!owner.isInState(CreatureState.WEAPON_EQUIPPED)) {
			owner.setState(CreatureState.WEAPON_EQUIPPED);
			PacketSendUtility.broadcastPacket(owner, new SM_EMOTION(owner, EmotionType.START_EMOTE2, 0, target.getObjectId()));
			PacketSendUtility.broadcastPacket(owner, new SM_EMOTION(owner, EmotionType.ATTACKMODE, 0, target.getObjectId()));
		}
	}

	/**
	 * Npc stops attacking
	 *
	 * @param owner
	 */
	public static final void emoteStopAttacking(Npc owner) {
		owner.unsetState(CreatureState.WEAPON_EQUIPPED);
		if (owner.getTarget() != null && owner.getTarget() instanceof Player) {
			PacketSendUtility.sendPacket((Player) owner.getTarget(), SM_SYSTEM_MESSAGE.STR_UI_COMBAT_NPC_RETURN(owner.getObjectTemplate().getNameId()));
		}
	}

	/**
	 * Npc starts following other creature
	 *
	 * @param owner
	 */
	public static final void emoteStartFollowing(Npc owner) {
		owner.unsetState(CreatureState.WALKING);
		PacketSendUtility.broadcastPacket(owner, new SM_EMOTION(owner, EmotionType.START_EMOTE2, 0, 0));
		PacketSendUtility.broadcastPacket(owner, new SM_EMOTION(owner, EmotionType.NEUTRALMODE, 0, 0));
	}

	/**
	 * Npc starts walking (either random or path)
	 *
	 * @param owner
	 */
	public static final void emoteStartWalking(Npc owner) {
		owner.setState(CreatureState.WALKING);
		PacketSendUtility.broadcastPacket(owner, new SM_EMOTION(owner, EmotionType.WALK));
	}

	/**
	 * Npc stops walking
	 *
	 * @param owner
	 */
	public static final void emoteStopWalking(Npc owner) {
		owner.unsetState(CreatureState.WALKING);
	}

	/**
	 * Npc starts returning to spawn location
	 *
	 * @param owner
	 */
	public static final void emoteStartReturning(Npc owner) {
		PacketSendUtility.broadcastPacket(owner, new SM_EMOTION(owner, EmotionType.START_EMOTE2, 0, 0));
		PacketSendUtility.broadcastPacket(owner, new SM_EMOTION(owner, EmotionType.NEUTRALMODE, 0, 0));
	}

	/**
	 * Npc starts idling
	 *
	 * @param owner
	 */
	public static final void emoteStartIdling(Npc owner) {
		owner.setState(CreatureState.WALKING);
		PacketSendUtility.broadcastPacket(owner, new SM_EMOTION(owner, EmotionType.START_EMOTE2, 0, 0));
		PacketSendUtility.broadcastPacket(owner, new SM_EMOTION(owner, EmotionType.NEUTRALMODE, 0, 0));
	}

	/**
	 * Npc starts dancing iu 1
	 * 
	 * @param owner
	 * @value you and me
	 */
	public static final void emoteStartDancing1(Npc owner) {
		owner.unsetState(CreatureState.NPC_IDLE);
		owner.setState(CreatureState.ACTIVE);
		PacketSendUtility.broadcastPacket(owner, new SM_EMOTION(owner, EmotionType.EMOTE, 134, 0));
	}

	/**
	 * Npc starts dancing iu 2
	 * 
	 * @param owner
	 * @vlaue good day
	 */
	public static final void emoteStartDancing2(Npc owner) {
		owner.unsetState(CreatureState.NPC_IDLE);
		owner.setState(CreatureState.ACTIVE);
		PacketSendUtility.broadcastPacket(owner, new SM_EMOTION(owner, EmotionType.EMOTE, 133, 0));
	}

	/**
	 * Npc starts dancing iu 3
	 * 
	 * @param owner
	 * @value theme song
	 */
	public static final void emoteStartDancing3(Npc owner) {
		owner.unsetState(CreatureState.NPC_IDLE);
		owner.setState(CreatureState.ACTIVE);
		PacketSendUtility.broadcastPacket(owner, new SM_EMOTION(owner, EmotionType.EMOTE, 142, 0));
	}

	/**
	 * Npc starts dancing iu 4
	 * 
	 * @param owner
	 * @value new song
	 */
	public static final void emoteStartDancing4(Npc owner) {
		owner.unsetState(CreatureState.NPC_IDLE);
		owner.setState(CreatureState.ACTIVE);
		PacketSendUtility.broadcastPacket(owner, new SM_EMOTION(owner, EmotionType.EMOTE, 19, 0));
	}

	/**
	 * Npc starts singing iu
	 * 
	 * @param owner
	 */
	public static final void emoteStartSinging(Npc owner) {
		owner.unsetState(CreatureState.NPC_IDLE);
		owner.setState(CreatureState.ACTIVE);
		PacketSendUtility.broadcastPacket(owner, new SM_EMOTION(owner, EmotionType.EMOTE, 113, 0));
	}
}
