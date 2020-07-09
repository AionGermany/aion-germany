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
			PreparedStatement stmt = con.prepareStatement("INSERT INTO transformations(player_id, transformation_id, name, grade, count) VALUES(?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE `player_id` = VALUES (`player_id`), `count` = VALUES(`count`)");
			stmt.setInt(1, transformationCommonData.getMasterObjectId());
			stmt.setInt(2, transformationCommonData.getTransformationId());
			stmt.setString(3, transformationCommonData.getName());
			stmt.setString(4, transformationCommonData.getTransformationGrade());
			stmt.setInt(5, transformationCommonData.getCount());
			stmt.execute();
			stmt.close();
		} catch (Exception e) {
			log.error("Error inserting new transformation #" + transformationCommonData.getTransformationId() + "[" + transformationCommonData.getName() + "]", e);
		} finally {
			DatabaseFactory.close(con);
		}
	}

	@Override
	public void removeTransformation(Player player, int transformationId) {
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("DELETE FROM transformations WHERE player_id = ? AND transformation_id = ?");
			stmt.setInt(1, player.getObjectId());
			stmt.setInt(2, transformationId);
			stmt.execute();
			stmt.close();
		} catch (Exception e) {
			log.error("Error removing transformation #" + transformationId, e);
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
				TransformationCommonData transformationCommonData = new TransformationCommonData(rs.getInt("transformation_id"), player.getObjectId(), rs.getString("name"), rs.getString("grade"), rs.getInt("count"));
				transformationCommonData.setName(rs.getString("name"));
				transformationCommonData.setGrade(rs.getString("grade"));
				transformationCommonData.setCount(rs.getInt("count"));
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

	@Override
	public void updateTransformation(TransformationCommonData transformationCommonData) {
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("UPDATE transformations SET count = ? WHERE player_id = ? AND transformation_id = ?");
			stmt.setInt(1, transformationCommonData.getCount());
			stmt.setInt(2, transformationCommonData.getMasterObjectId());
			stmt.setInt(3, transformationCommonData.getTransformationId());
			stmt.execute();
			stmt.close();
		}
		catch (Exception e) {
			log.error("Error update transformations #" + transformationCommonData.getTransformationId(), e);
		}
		finally {
			DatabaseFactory.close(con);
		}
	}
	
	@Override
	public int getCount(int playerObjId, int transformationId) {
		Connection con = null;
		int transformCount = 0;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM transformations WHERE player_id = ? AND transformation_id = ? ");
			stmt.setInt(1, playerObjId);
			stmt.setInt(2, transformationId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				transformCount = rs.getInt("count");
			}
			stmt.close();
		} catch (Exception e) {
			log.error("Error getting TransformationId count for " + playerObjId, e);
		} finally {
			DatabaseFactory.close(con);
		}
		return transformCount;
	}

	@Override
	public boolean supports(String databaseName, int majorVersion, int minorVersion) {
		return MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
	}
}
