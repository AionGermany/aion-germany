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

import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.actions.PlayerMode;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.state.CreatureState;
import com.aionemu.gameserver.model.templates.windstreams.WindstreamPath;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_TRANSFORM;
import com.aionemu.gameserver.network.aion.serverpackets.SM_WINDSTREAM;
import com.aionemu.gameserver.questEngine.QuestEngine;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.utils.PacketSendUtility;

public class CM_WINDSTREAM extends AionClientPacket {

	private final Logger log = LoggerFactory.getLogger(CM_WINDSTREAM.class);
	int teleportId;
	int distance;
	int state;

	public CM_WINDSTREAM(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl() {
		teleportId = readD();
		distance = readD();
		state = readD();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();
		switch (state) {
			case 0:
			case 7:
			case 8:
				if (state == 0) {
					player.unsetPlayerMode(PlayerMode.RIDE);
				}
				else if (state == 7) { // start boost
					PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.WINDSTREAM_START_BOOST, 0, 0), true);
				}
				else if (state == 8) { // end boost
					PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.WINDSTREAM_END_BOOST, 0, 0), true);
				}
				PacketSendUtility.sendPacket(player, new SM_WINDSTREAM(state, 1));
				break;
			case 1:
				if (player.isInPlayerMode(PlayerMode.WINDSTREAM)) {
					return;
				}
				if (player.isInState(CreatureState.GLIDING) || player.isInState(CreatureState.FLYING)) {
					player.setPlayerMode(PlayerMode.WINDSTREAM, new WindstreamPath(teleportId, distance));
					player.unsetState(CreatureState.ACTIVE);
					player.unsetState(CreatureState.GLIDING);
					player.setState(CreatureState.FLYING);
					PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.WINDSTREAM, teleportId, distance), true);
					player.getLifeStats().triggerFpRestore();
					QuestEngine.getInstance().onEnterWindStream(new QuestEnv(null, player, 0, 0), teleportId);
				}
				break;
			case 2:
			case 3:
			case 4:
				if (!player.isInPlayerMode(PlayerMode.WINDSTREAM)) {
					return;
				}
				player.unsetState(CreatureState.FLYING);
				player.setState(CreatureState.ACTIVE);
				if (state == 2) {
					player.setState(CreatureState.GLIDING);
					player.getLifeStats().triggerFpReduce();
				}
				if (state == 4 || player.isTransformed()) {
					player.setState(CreatureState.GLIDING);
					player.getLifeStats().triggerFpReduce();

					PacketSendUtility.broadcastPacketAndReceive(player, new SM_TRANSFORM(player, player.getTransformedModelId(), true, player.getTransformedItemId()));
					PacketSendUtility.broadcastPacketAndReceive(player, new SM_TRANSFORM(player, true));
					player.getEffectController().updatePlayerEffectIcons();
				}
				PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, state == 2 ? EmotionType.WINDSTREAM_END : EmotionType.WINDSTREAM_EXIT, 0, 0), true);
				player.getGameStats().updateStatsAndSpeedVisually();
				PacketSendUtility.sendPacket(player, new SM_WINDSTREAM(state, 1));
				player.unsetPlayerMode(PlayerMode.WINDSTREAM);
				break;
			default:
				log.error("Unknown Windstream state #" + state + " was found!");
		}
	}
}
