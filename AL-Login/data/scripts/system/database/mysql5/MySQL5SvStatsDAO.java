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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.aionemu.loginserver.dao.SvStatsDAO;
import com.aionemu.commons.database.DatabaseFactory;

public class MySQL5SvStatsDAO extends SvStatsDAO {
        private static final Logger log = LoggerFactory.getLogger(MySQL5SvStatsDAO.class);
        
        public static final String INSERT_QUERY_1 = "UPDATE `svstats` SET status=?, current=?, max=? WHERE server=?";
        public static final String INSERT_QUERY_2 = "UPDATE `svstats` SET status=?, current=? WHERE server=?";
        public static final String INSERT_QUERY_3 = "UPDATE `svstats` SET status=?, current=?";
        
        @Override
        public void update_SvStats_Online(int server, int status, int current, int max) {
                Connection con = null;
                try
                {
                        con = DatabaseFactory.getConnection();
                        PreparedStatement stmt = con.prepareStatement(INSERT_QUERY_1);
                        stmt.setInt(1, status);
                        stmt.setInt(2, current);
                        stmt.setInt(3, max);
                        stmt.setInt(4, server);
                        stmt.execute();
                        stmt.close();
                }
                catch (Exception e)
                {
                        log.error("Cannot update SvStats", e);
                }
                finally
                {
                        DatabaseFactory.close(con);
                }
        }
        
        @Override
        public void update_SvStats_Offline(int server, int status, int current) {
                Connection con = null;
                try
                {
                        con = DatabaseFactory.getConnection();
                        PreparedStatement stmt = con.prepareStatement(INSERT_QUERY_2);
                        stmt.setInt(1, status);
                        stmt.setInt(2, current);
                        stmt.setInt(3, server);
                        stmt.execute();
                        stmt.close();
                }
                catch (Exception e)
                {
                        log.error("Cannot update SvStats", e);
                }
                finally
                {
                        DatabaseFactory.close(con);
                }
        }
        
        @Override
        public void update_SvStats_All_Offline(int status, int current) {
                Connection con = null;
                try
                {
                        con = DatabaseFactory.getConnection();
                        PreparedStatement stmt = con.prepareStatement(INSERT_QUERY_3);
                        stmt.setInt(1, status);
                        stmt.setInt(2, current);
                        stmt.execute();
                        stmt.close();
                }
                catch (Exception e)
                {
                        log.error("Cannot update SvStats", e);
                }
                finally
                {
                        DatabaseFactory.close(con);
                }
        }

        @Override
        public boolean supports(String database, int majorVersion, int minorVersion) {
                return MySQL5DAOUtils.supports(database, majorVersion, minorVersion);
        }
}