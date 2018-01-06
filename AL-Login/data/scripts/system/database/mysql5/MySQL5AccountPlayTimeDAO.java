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

import com.aionemu.commons.database.DB;
import com.aionemu.loginserver.dao.AccountPlayTimeDAO;
import com.aionemu.loginserver.model.AccountTime;

/**
 * @author Antraxx
 */
public class MySQL5AccountPlayTimeDAO extends AccountPlayTimeDAO {

    @Override
    public boolean update(final Integer accountId, final AccountTime accountTime) {
        String sql = "INSERT INTO account_playtime (`account_id`,`accumulated_online`) VALUES (" + accountId + ", " + accountTime.getAccumulatedOnlineTime() + ") "
                + "ON DUPLICATE KEY UPDATE `accumulated_online` = `accumulated_online` + " + accountTime.getAccumulatedOnlineTime();
        return DB.insertUpdate(sql);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(String database, int majorVersion, int minorVersion) {
        return MySQL5DAOUtils.supports(database, majorVersion, minorVersion);
    }

}
