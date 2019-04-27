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
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.DatabaseFactory;
import com.aionemu.gameserver.dao.MySQL5DAOUtils;
import com.aionemu.gameserver.dao.TransformationsDAO;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.TransformationCommonData;

/**
 * @author Falke_34
 */
public class MySQL5TransformationsDAO extends TransformationsDAO {

	private static final Logger log = LoggerFactory.getLogger(MySQL5TransformationsDAO.class);

	@Override
	public void insertTransformation(TransformationCommonData transformationCommonData) {
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("INSERT INTO transformations(player_id, object_id, transformation_id, name, grade) VALUES(?, ?, ?, ?, ?)");
			stmt.setInt(1, transformationCommonData.getMasterObjectId());
			stmt.setInt(2, transformationCommonData.getObjectId());
			stmt.setInt(3, transformationCommonData.getTransformationId());
			stmt.setString(4, transformationCommonData.getName());
			stmt.setString(5, transformationCommonData.getTransformationGrade());
			stmt.execute();
			stmt.close();
		} catch (Exception e) {
			log.error("Error inserting new transformation #" + transformationCommonData.getTransformationId() + "[" + transformationCommonData.getName() + "]", e);
		} finally {
			DatabaseFactory.close(con);
		}
	}

	@Override
	public void removeTransformation(Player player, int transformationObjectId) {
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("DELETE FROM transformations WHERE player_id = ? AND transformation_id = ?");
			stmt.setInt(1, player.getObjectId());
			stmt.setInt(2, transformationObjectId);
			stmt.execute();
			stmt.close();
		} catch (Exception e) {
			log.error("Error removing transformation #" + transformationObjectId, e);
		} finally {
			DatabaseFactory.close(con);
		}
	}

	@Override
	public List<TransformationCommonData> getTransformations(Player player) {
		List<TransformationCommonData> transformations = new ArrayList<TransformationCommonData>();
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM transformations WHERE player_id = ?");
			stmt.setInt(1, player.getObjectId());
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				TransformationCommonData transformationCommonData = new TransformationCommonData(rs.getInt("transformation_id"), player.getObjectId(), rs.getString("name"), rs.getString("grade"));
				transformationCommonData.setName(rs.getString("name"));
				transformationCommonData.setGrade(rs.getString("grade"));
				transformations.add(transformationCommonData);
			}
			stmt.close();
		} catch (Exception e) {
			log.error("Error getting transformations for " + player.getObjectId(), e);
		} finally {
			DatabaseFactory.close(con);
		}
		return transformations;
	}

	public boolean Transformations(int playerid, int transformationObjId) {
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM transformations WHERE player_id = ? ");
			stmt.setInt(1, playerid);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				if (rs.getInt("transformation_id") == transformationObjId)
					return true;
			}
			stmt.close();
		} catch (Exception e) {
			log.error("Error getting transformations for " + playerid, e);
		} finally {
			DatabaseFactory.close(con);
		}
		return false;
	}

	@Override
	public boolean supports(String databaseName, int majorVersion, int minorVersion) {
		return MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
	}
}
