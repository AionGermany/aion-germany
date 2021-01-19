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

import java.util.Map;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.services.player.LumielTransformService;

import javolution.util.FastMap;

public class CM_LUMIEL_TRANSFORM extends AionClientPacket {

	private int actionId;
	private int lumielId;
	private int matCount;
	private Player player;
	int objectId;
	long count;
	private Map<Integer, Long> matrials = new FastMap<Integer, Long>();

	public CM_LUMIEL_TRANSFORM(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	protected void readImpl() {
		player = (getConnection()).getActivePlayer();
		actionId = readH();
		switch (actionId) {
			case 1:
				break;
			case 2:
				lumielId = readD();
				matCount = readH();
				for (int i = 0; i < matCount; ++i) {
					objectId = readD();
					count = readQ();
					matrials.put(objectId, count);
				}	
				break;
			case 3:
				lumielId = readD();
				break;
			case 4:
				lumielId = readD();
		}
	}

	protected void runImpl() {
		switch (actionId) {
			case 1:
				LumielTransformService.getInstance().sendLumielPacket(player);
				break;
			case 2:
				LumielTransformService.getInstance().onRewardPoints(player, lumielId, matrials);
				break;
			case 3:
				LumielTransformService.getInstance().onGenerateReward(player, lumielId);
				break;
			case 4:
				LumielTransformService.getInstance().onRewardPlayer(player, lumielId);
		}
	}
}
