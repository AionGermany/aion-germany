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
package mysql5;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.DB;
import com.aionemu.commons.database.DatabaseFactory;
import com.aionemu.commons.database.IUStH;
import com.aionemu.gameserver.dao.MySQL5DAOUtils;
import com.aionemu.gameserver.dao.PlayerSettingsDAO;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PlayerSettings;

/**
 * @author ATracer
 */
public class MySQL5PlayerSettingsDAO extends PlayerSettingsDAO {

	private static final Logger log = LoggerFactory.getLogger(MySQL5PlayerSettingsDAO.class);

	/**
	 * TODO 1) analyze possibility to zip settings 2) insert/update instead of replace 0 - uisettings 1 - shortcuts 2 - display 3 - deny
	 */
	@Override
	public void loadSettings(final Player player) {
		final int playerId = player.getObjectId();
		final PlayerSettings playerSettings = new PlayerSettings();
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT * FROM player_settings WHERE player_id = ?");
			statement.setInt(1, playerId);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				int type = resultSet.getInt("settings_type");
				switch (type) {
					case 0:
						playerSettings.setUiSettings(resultSet.getBytes("settings"));
						break;
					case 1:
						playerSettings.setShortcuts(resultSet.getBytes("settings"));
						break;
					case 2:
						playerSettings.setHouseBuddies(resultSet.getBytes("settings"));
						break;
					case -1:
						playerSettings.setDisplay(resultSet.getInt("settings"));
						break;
					case -2:
						playerSettings.setDeny(resultSet.getInt("settings"));
						break;
				}
			}
			resultSet.close();
			statement.close();
		}
		catch (Exception e) {
			log.error("Could not restore PlayerSettings data for player " + playerId + " from DB: " + e.getMessage(), e);
		}
		finally {
			DatabaseFactory.close(con);
		}
		playerSettings.setPersistentState(PersistentState.UPDATED);
		player.setPlayerSettings(playerSettings);
	}

	@Override
	public void saveSettings(final Player player) {
		final int playerId = player.getObjectId();

		PlayerSettings playerSettings = player.getPlayerSettings();
		if (playerSettings.getPersistentState() == PersistentState.UPDATED) {
			return;
		}

		final byte[] uiSettings = playerSettings.getUiSettings();
		final byte[] shortcuts = playerSettings.getShortcuts();
		final byte[] houseBuddies = playerSettings.getHouseBuddies();
		final int display = playerSettings.getDisplay();
		final int deny = playerSettings.getDeny();

		if (uiSettings != null) {
			DB.insertUpdate("REPLACE INTO player_settings values (?, ?, ?)", new IUStH() {

				@Override
				public void handleInsertUpdate(PreparedStatement stmt) throws SQLException {
					stmt.setInt(1, playerId);
					stmt.setInt(2, 0);
					stmt.setBytes(3, uiSettings);
					stmt.execute();
				}
			});
		}

		if (shortcuts != null) {
			DB.insertUpdate("REPLACE INTO player_settings values (?, ?, ?)", new IUStH() {

				@Override
				public void handleInsertUpdate(PreparedStatement stmt) throws SQLException {
					stmt.setInt(1, playerId);
					stmt.setInt(2, 1);
					stmt.setBytes(3, shortcuts);
					stmt.execute();
				}
			});
		}

		if (houseBuddies != null) {
			DB.insertUpdate("REPLACE INTO player_settings values (?, ?, ?)", new IUStH() {

				@Override
				public void handleInsertUpdate(PreparedStatement stmt) throws SQLException {
					stmt.setInt(1, playerId);
					stmt.setInt(2, 2);
					stmt.setBytes(3, houseBuddies);
					stmt.execute();
				}
			});
		}

		DB.insertUpdate("REPLACE INTO player_settings values (?, ?, ?)", new IUStH() {

			@Override
			public void handleInsertUpdate(PreparedStatement stmt) throws SQLException {
				stmt.setInt(1, playerId);
				stmt.setInt(2, -1);
				stmt.setInt(3, display);
				stmt.execute();
			}
		});

		DB.insertUpdate("REPLACE INTO player_settings values (?, ?, ?)", new IUStH() {

			@Override
			public void handleInsertUpdate(PreparedStatement stmt) throws SQLException {
				stmt.setInt(1, playerId);
				stmt.setInt(2, -2);
				stmt.setInt(3, deny);
				stmt.execute();
			}
		});

	}

	@Override
	public boolean supports(String databaseName, int majorVersion, int minorVersion) {
		return MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
	}
}
