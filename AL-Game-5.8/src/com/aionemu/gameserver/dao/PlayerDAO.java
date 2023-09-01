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
package com.aionemu.gameserver.dao;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.aionemu.gameserver.model.Race;
//import com.aionemu.gameserver.model.account.Account;
import com.aionemu.gameserver.model.account.PlayerAccountData;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PlayerCommonData;
import com.aionemu.gameserver.model.team.legion.LegionJoinRequestState;

/**
 * Class that is responsible for storing/loading player data
 *
 * @author SoulKeeper, Saelya
 * @author cura
 */
public abstract class PlayerDAO implements IDFactoryAwareDAO {

	/**
	 * Returns true if name is used, false in other case
	 *
	 * @param name
	 *            name to check
	 * @return true if name is used, false in other case
	 */
	public abstract boolean isNameUsed(String name);

	/**
	 * Returns player name by player object id
	 *
	 * @param playerObjectIds
	 *            player object ids to get name
	 * @return map ObjectID-To-Name
	 */
	public abstract Map<Integer, String> getPlayerNames(Collection<Integer> playerObjectIds);

	public abstract void changePlayerId(Player player, int accountId);

	/**
	 * Stores player to db
	 *
	 * @param player
	 */
	public abstract void storePlayer(Player player);

	/**
	 * This method is used to store only newly created characters
	 *
	 * @param pcd
	 *            player to save in database
	 * @return true if every things went ok.
	 */
	public abstract boolean saveNewPlayer(PlayerCommonData pcd, int accountId, String accountName);

	public abstract PlayerCommonData loadPlayerCommonData(int playerObjId);

	/**
	 * Removes player and all related data (Done by CASCADE DELETION)
	 *
	 * @param playerId
	 *            player to delete
	 */
	public abstract void deletePlayer(int playerId);

	public abstract void updateDeletionTime(int objectId, Timestamp deletionDate);

	public abstract void storeCreationTime(int objectId, Timestamp creationDate);

	/**
	 * Loads creation and deletion time from database, for particular player and sets these values in given <tt>PlayerAccountData</tt> object.
	 *
	 * @param acData
	 */
	public abstract void setCreationDeletionTime(PlayerAccountData acData);

	/**
	 * Returns a list of objectId of players that are on the account with given accountId
	 *
	 * @param accountId
	 * @return List<Integer>
	 */
	public abstract List<Integer> getPlayerOidsOnAccount(int accountId);

	/**
	 * Stores the last online time
	 *
	 * @param objectId
	 *            Object ID of player to store
	 * @param lastOnline
	 *            Last online time of player to store
	 */
	public abstract void storeLastOnlineTime(final int objectId, final Timestamp lastOnline);

	/**
	 * Store online or offline player status
	 *
	 * @param player
	 * @param online
	 */
	public abstract void onlinePlayer(final Player player, final boolean online);

	/**
	 * Set all players offline status
	 *
	 * @param online
	 */
	public abstract void setPlayersOffline(final boolean online);

	/**
	 * get commondata by name for MailService
	 *
	 * @param name
	 * @return
	 */
	public abstract PlayerCommonData loadPlayerCommonDataByName(String name);

	/**
	 * Returns Player's Account ID
	 *
	 * @param name
	 * @return
	 */
	public abstract int getAccountIdByName(final String name);

	/**
	 * Identifier name for all PlayerDAO classes
	 *
	 * @return PlayerDAO.class.getName()
	 */
	public abstract String getPlayerNameByObjId(final int playerObjId);

	/**
	 * get playerId by name
	 *
	 * @param playerName
	 * @return
	 */
	public abstract int getPlayerIdByName(final String playerName);

	public abstract void storePlayerName(PlayerCommonData recipientCommonData);

	/**
	 * Return account characters count
	 *
	 * @param accountId
	 * @return
	 */
	public abstract int getCharacterCountOnAccount(final int accountId);

	/**
	 * Get characters count for a given Race
	 *
	 * @param race
	 * @return the number of characters for race
	 */
	public abstract int getCharacterCountForRace(Race race);

	/**
	 * Return online characters count
	 *
	 * @param none
	 * @return
	 */
	public abstract int getOnlinePlayerCount();

	/**
	 * Returns a Set of objectId of accounts that are inactive for more than dayOfInactivity days
	 *
	 * @param daysOfInactivity
	 *            Number of days a char needs to be inactive
	 * @param limitation
	 *            Maximum number of chars deleted in one run
	 * @return List of IDs
	 */
	public abstract Set<Integer> getInactiveAccounts(final int daysOfInactivity, int limitation);

	public abstract void setPlayerLastTransferTime(final int playerId, final long time);

	public abstract boolean updateBonusTime(final int playerObjId);

	@Override
	public final String getClassName() {
		return PlayerDAO.class.getName();
	}

	public abstract Timestamp getCharacterCreationDateId(final int obj);

	public abstract void updateLegionJoinRequestState(int playerId, LegionJoinRequestState state);

	public abstract void clearJoinRequest(final int playerId);

	public abstract void getJoinRequestState(Player player);
}
