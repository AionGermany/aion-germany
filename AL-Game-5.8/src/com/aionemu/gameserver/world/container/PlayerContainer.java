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
package com.aionemu.gameserver.world.container;

import java.util.Collection;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.world.exceptions.DuplicateAionObjectException;
import com.aionemu.gameserver.world.knownlist.Visitor;

import javolution.util.FastMap;

/**
 * Container for storing Players by objectId and name.
 *
 * @author -Nemesiss-
 */
public class PlayerContainer implements Iterable<Player> {

	private static final Logger log = LoggerFactory.getLogger(PlayerContainer.class);
	/**
	 * Map<ObjectId,Player>
	 */
	private final FastMap<Integer, Player> playersById = new FastMap<Integer, Player>().shared();
	/**
	 * Map<Name,Player>
	 */
	private final FastMap<String, Player> playersByName = new FastMap<String, Player>().shared();

	/**
	 * Add Player to this Container.
	 *
	 * @param player
	 */
	public void add(Player player) {
		if (playersById.put(player.getObjectId(), player) != null) {
			throw new DuplicateAionObjectException();
		}
		if (playersByName.put(player.getName(), player) != null) {
			throw new DuplicateAionObjectException();
		}
	}

	/**
	 * Remove Player from this Container.
	 *
	 * @param player
	 */
	public void remove(Player player) {
		playersById.remove(player.getObjectId());
		playersByName.remove(player.getName());
	}

	/**
	 * Get Player object by objectId.
	 *
	 * @param objectId
	 *            - ObjectId of player.
	 * @return Player with given ojectId or null if Player with given objectId is not logged.
	 */
	public Player get(int objectId) {
		return playersById.get(objectId);
	}

	/**
	 * Get Player object by name.
	 *
	 * @param name
	 *            - name of player
	 * @return Player with given name or null if Player with given name is not logged.
	 */
	public Player get(String name) {
		return playersByName.get(name);
	}

	@Override
	public Iterator<Player> iterator() {
		return playersById.values().iterator();
	}

	/**
	 * @param visitor
	 */
	public void doOnAllPlayers(Visitor<Player> visitor) {
		try {
			for (FastMap.Entry<Integer, Player> e = playersById.head(), mapEnd = playersById.tail(); (e = e.getNext()) != mapEnd;) {
				Player player = null;
				try {
					player = e.getValue();
				}
				catch (Exception ex) {
					player = null;
				}
				if (player != null) {
					if (player.getRace().isPlayerRace())
						visitor.visit(player);
					else
						continue;
				}
			}
		}
		catch (Exception ex) {
			log.error("Exception when running visitor on all players" + ex);
		}
	}

	public Collection<Player> getAllPlayers() {
		return playersById.values();
	}
}
