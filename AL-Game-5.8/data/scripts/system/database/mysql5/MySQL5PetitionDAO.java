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
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.DatabaseFactory;
import com.aionemu.gameserver.dao.MySQL5DAOUtils;
import com.aionemu.gameserver.dao.PetitionDAO;
import com.aionemu.gameserver.model.Petition;
import com.aionemu.gameserver.model.PetitionStatus;

/**
 * @author zdead
 */
public class MySQL5PetitionDAO extends PetitionDAO {

	private static final Logger log = LoggerFactory.getLogger(MySQL5PetitionDAO.class);

	@Override
	public synchronized int getNextAvailableId() {
		Connection con = null;
		int result = 0;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT MAX(id) as nextid FROM petitions");
			ResultSet rset = stmt.executeQuery();
			rset.next();
			result = rset.getInt("nextid") + 1;
			stmt.close();
		}
		catch (Exception e) {
			log.error("Cannot get next available petition id", e);
			return 0;
		}
		finally {
			DatabaseFactory.close(con);
		}
		return result;
	}

	@Override
	public Petition getPetitionById(int petitionId) {
		String query = "SELECT * FROM petitions WHERE id = ?";
		Connection con = null;
		Petition result = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setInt(1, petitionId);
			ResultSet rset = stmt.executeQuery();
			if (!rset.next()) {
				return null;
			}

			String statusValue = rset.getString("status");
			PetitionStatus status;
			if (statusValue.equals("PENDING")) {
				status = PetitionStatus.PENDING;
			}
			else if (statusValue.equals("IN_PROGRESS")) {
				status = PetitionStatus.IN_PROGRESS;
			}
			else {
				status = PetitionStatus.PENDING;
			}

			result = new Petition(rset.getInt("id"), rset.getInt("player_id"), rset.getInt("type"), rset.getString("title"), rset.getString("message"), rset.getString("add_data"), status.getElementId());

			stmt.close();
		}
		catch (Exception e) {
			log.error("Cannot get petition #" + petitionId, e);
		}
		finally {
			DatabaseFactory.close(con);
		}
		return result;
	}

	@Override
	public Set<Petition> getPetitions() {
		String query = "SELECT * FROM petitions WHERE status = 'PENDING' OR status = 'IN_PROGRESS' ORDER BY id ASC";
		Connection con = null;
		Set<Petition> results = new HashSet<Petition>();
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(query);
			ResultSet rset = stmt.executeQuery();
			while (rset.next()) {
				String statusValue = rset.getString("status");
				PetitionStatus status;
				if (statusValue.equals("PENDING")) {
					status = PetitionStatus.PENDING;
				}
				else if (statusValue.equals("IN_PROGRESS")) {
					status = PetitionStatus.IN_PROGRESS;
				}
				else {
					status = PetitionStatus.PENDING;
				}

				Petition p = new Petition(rset.getInt("id"), rset.getInt("player_id"), rset.getInt("type"), rset.getString("title"), rset.getString("message"), rset.getString("add_data"), status.getElementId());
				results.add(p);
			}
			stmt.close();
		}
		catch (Exception e) {
			log.error("Cannot get next available petition id", e);
			return null;
		}
		finally {
			DatabaseFactory.close(con);
		}
		return results;
	}

	@Override
	public void deletePetition(int playerObjId) {
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("DELETE FROM petitions WHERE player_id = ? AND (status = 'PENDING' OR status='IN_PROGRESS')");
			stmt.setInt(1, playerObjId);
			stmt.execute();
			stmt.close();
		}
		catch (Exception e) {
			log.error("Cannot delete petition", e);
		}
		finally {
			DatabaseFactory.close(con);
		}
	}

	@Override
	public void insertPetition(Petition petition) {
		Connection con = null;
		String query = "INSERT INTO petitions (id, player_id, type, title, message, add_data, time, status) VALUES(?,?,?,?,?,?,?,?)";
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setInt(1, petition.getPetitionId());
			stmt.setInt(2, petition.getPlayerObjId());
			stmt.setInt(3, petition.getPetitionType().getElementId());
			stmt.setString(4, petition.getTitle());
			stmt.setString(5, petition.getContentText());
			stmt.setString(6, petition.getAdditionalData());
			stmt.setLong(7, new Date().getTime() / 1000);
			stmt.setString(8, petition.getStatus().toString());
			stmt.execute();
			stmt.close();
		}
		catch (Exception e) {
			log.error("Cannot insert petition", e);
		}
		finally {
			DatabaseFactory.close(con);
		}
	}

	@Override
	public void setReplied(int petitionId) {
		Connection con = null;
		String query = "UPDATE petitions SET status = 'REPLIED' WHERE id = ?";
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setInt(1, petitionId);
			stmt.execute();
			stmt.close();
		}
		catch (Exception e) {
			log.error("Cannot set petition replied", e);
		}
		finally {
			DatabaseFactory.close(con);
		}
	}

	@Override
	public boolean supports(String s, int i, int i1) {
		return MySQL5DAOUtils.supports(s, i, i1);
	}
}
