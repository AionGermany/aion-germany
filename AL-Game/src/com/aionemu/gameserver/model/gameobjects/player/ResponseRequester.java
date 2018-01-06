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
package com.aionemu.gameserver.model.gameobjects.player;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages the asking of and responding to <tt>SM_QUESTION_WINDOW</tt>
 *
 * @author Ben
 */
public class ResponseRequester {

	private Player player;
	private HashMap<Integer, RequestResponseHandler> map = new HashMap<Integer, RequestResponseHandler>();
	private static Logger log = LoggerFactory.getLogger(ResponseRequester.class);

	public ResponseRequester(Player player) {
		this.player = player;
	}

	/**
	 * Adds this handler to this messageID, returns false if there already exists one
	 *
	 * @param messageId
	 *            ID of the request message
	 * @return true or false
	 */
	public synchronized boolean putRequest(int messageId, RequestResponseHandler handler) {
		if (map.containsKey(messageId)) {
			return false;
		}

		map.put(messageId, handler);
		return true;
	}

	/**
	 * Responds to the given message ID with the given response Returns success
	 *
	 * @param messageId
	 * @param response
	 * @return Success
	 */
	public synchronized boolean respond(int messageId, int response) {
		RequestResponseHandler handler = map.get(messageId);
		if (handler != null) {
			map.remove(messageId);
			log.debug("RequestResponseHandler triggered for response code " + messageId + " from " + player.getName());
			handler.handle(player, response);
			return true;
		}
		return false;
	}

	/**
	 * Automatically responds 0 to all requests, passing the given player as the responder
	 */
	public synchronized void denyAll() {
		for (RequestResponseHandler handler : map.values()) {
			handler.handle(player, 0);
		}

		map.clear();
	}
}
