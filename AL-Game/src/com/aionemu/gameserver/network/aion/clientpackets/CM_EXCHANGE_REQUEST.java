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

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.DeniedStatus;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.RequestResponseHandler;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.SystemMessageId;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.ExchangeService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;

/**
 * @author -Avol-
 */
public class CM_EXCHANGE_REQUEST extends AionClientPacket {

	public Integer targetObjectId;
	private static final Logger log = LoggerFactory.getLogger(CM_EXCHANGE_REQUEST.class);

	public CM_EXCHANGE_REQUEST(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	@Override
	protected void readImpl() {
		targetObjectId = readD();
	}

	@Override
	protected void runImpl() {
		final Player activePlayer = getConnection().getActivePlayer();
		final Player targetPlayer = World.getInstance().findPlayer(targetObjectId);

		if (targetPlayer == null) {
			log.warn("CM_EXCHANGE_REQUEST null target from {} to {}", activePlayer.getObjectId(), targetObjectId);
			return;
		}
		/**
		 * check if not trading with yourself.
		 */
		if (!activePlayer.equals(targetPlayer)) {
			/**
			 * check distance between players.
			 */
			if (activePlayer.getKnownList().getObject(targetPlayer.getObjectId()) == null) {
				log.info("[AUDIT] Player " + activePlayer.getName() + " tried trade with player (" + targetPlayer.getName() + ") not from knownlist.");
				return;
			}
			if (!activePlayer.getRace().equals(targetPlayer.getRace())) {
				log.info("[AUDIT] Player " + activePlayer.getName() + " tried trade with player (" + targetPlayer.getName() + ") another race.");
				return;
			}
			/**
			 * check if trade partner exists or is he/she a player.
			 */
			if (targetPlayer != null) {
				if (targetPlayer.getPlayerSettings().isInDeniedStatus(DeniedStatus.TRADE)) {
					sendPacket(SM_SYSTEM_MESSAGE.STR_MSG_REJECTED_TRADE(targetPlayer.getName()));
					return;
				} 
				if (targetPlayer.getInventory().isFull()) {
					//You cannot trade with the target as the target is carrying too many items.
					PacketSendUtility.sendPacket(activePlayer, SM_SYSTEM_MESSAGE.STR_PARTNER_TOO_HEAVY_TO_EXCHANGE);
					return;
				} 
				if (activePlayer.getInventory().isFull()) {
					//You cannot trade with the target as you are carrying too many items.
					PacketSendUtility.sendPacket(activePlayer, SM_SYSTEM_MESSAGE.STR_EXCHANGE_CANT_EXCHANGE_HEAVY_TO_ADD_EXCHANGE_ITEM);
					return;
				}
				sendPacket(SM_SYSTEM_MESSAGE.STR_EXCHANGE_ASKED_EXCHANGE_TO_HIM(targetPlayer.getName()));
				RequestResponseHandler responseHandler = new RequestResponseHandler(activePlayer) {

					@Override
					public void acceptRequest(Creature requester, Player responder) {
						ExchangeService.getInstance().registerExchange(activePlayer, targetPlayer);
					}

					@Override
					public void denyRequest(Creature requester, Player responder) {
						PacketSendUtility.sendPacket(activePlayer, new SM_SYSTEM_MESSAGE(SystemMessageId.EXCHANGE_HE_REJECTED_EXCHANGE, targetPlayer.getName()));
					}
				};

				boolean requested = targetPlayer.getResponseRequester().putRequest(SM_QUESTION_WINDOW.STR_EXCHANGE_DO_YOU_ACCEPT_EXCHANGE, responseHandler);
				if (requested) {
					PacketSendUtility.sendPacket(targetPlayer, new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_EXCHANGE_DO_YOU_ACCEPT_EXCHANGE, 0, 0, activePlayer.getName()));
				}
			}
		}
		else {
			// TODO: send message, cannot trade with yourself.
		}
	}
}
