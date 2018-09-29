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

import java.util.List;

import com.aionemu.gameserver.dao.MySQL5DAOUtils;
import com.aionemu.gameserver.dao.PlayerTransformationsDAO;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.TransformationCommonData;

/**
 * @author Falke_34
 */
public class MySQL5PlayerTransformationsDAO extends PlayerTransformationsDAO {

	public static final String INSERT_QUERY = "INSERT INTO `player_transformations` (`account_id`, `item_id`) VALUES (?,?)";
	public static final String SELECT_QUERY = "SELECT * FROM `player_transformations` WHERE `account_id`=?";
	public static final String DELETE_QUERY = "DELETE FROM `player_transformations` WHERE `account_id`=?";

	@Override
	public void insertTransformation(TransformationCommonData transformationCommonData) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removePlayerTransformation(Player player, int transformationId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<TransformationCommonData> getPlayerTransformations(Player player) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public boolean supports(String databaseName, int majorVersion, int minorVersion) {
		return MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
	}
}
