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
package com.aionemu.gameserver.network.chatserver;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.network.chatserver.ChatServerConnection.State;

/**
 * @author ATracer
 */
public class CsPacketHandler {

	/**
	 * logger for this class
	 */
	private static final Logger log = LoggerFactory.getLogger(CsPacketHandler.class);
	private Map<State, Map<Integer, CsClientPacket>> packetPrototypes = new HashMap<State, Map<Integer, CsClientPacket>>();

	/**
	 * Reads one packet from given ByteBuffer
	 *
	 * @param data
	 * @param client
	 * @return GsClientPacket object from binary data
	 */
	public CsClientPacket handle(ByteBuffer data, ChatServerConnection client) {
		State state = client.getState();
		int id = data.get() & 0xff;

		return getPacket(state, id, data, client);
	}

	/**
	 * @param packetPrototype
	 * @param states
	 */
	public void addPacketPrototype(CsClientPacket packetPrototype, State... states) {
		for (State state : states) {
			Map<Integer, CsClientPacket> pm = packetPrototypes.get(state);
			if (pm == null) {
				pm = new HashMap<Integer, CsClientPacket>();
				packetPrototypes.put(state, pm);
			}
			pm.put(packetPrototype.getOpcode(), packetPrototype);
		}
	}

	/**
	 * @param state
	 * @param id
	 * @param buf
	 * @param con
	 * @return
	 */
	private CsClientPacket getPacket(State state, int id, ByteBuffer buf, ChatServerConnection con) {
		CsClientPacket prototype = null;

		Map<Integer, CsClientPacket> pm = packetPrototypes.get(state);
		if (pm != null) {
			prototype = pm.get(id);
		}

		if (prototype == null) {
			unknownPacket(state, id);
			return null;
		}

		CsClientPacket res = prototype.clonePacket();
		res.setBuffer(buf);
		res.setConnection(con);

		return res;
	}

	/**
	 * Logs unknown packet.
	 *
	 * @param state
	 * @param id
	 */
	private void unknownPacket(State state, int id) {
		log.warn(String.format("Unknown packet recived from Chat Server: 0x%02X state=%s", id, state.toString()));
	}
}
