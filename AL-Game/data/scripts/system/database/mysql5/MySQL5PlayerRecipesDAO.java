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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

import com.aionemu.commons.database.DB;
import com.aionemu.commons.database.IUStH;
import com.aionemu.commons.database.ParamReadStH;
import com.aionemu.gameserver.dao.MySQL5DAOUtils;
import com.aionemu.gameserver.dao.PlayerRecipesDAO;
import com.aionemu.gameserver.model.gameobjects.player.RecipeList;

/**
 * @author lord_rex
 */
public class MySQL5PlayerRecipesDAO extends PlayerRecipesDAO {

	private static final String SELECT_QUERY = "SELECT `recipe_id` FROM player_recipes WHERE `player_id`=?";
	private static final String ADD_QUERY = "INSERT INTO player_recipes (`player_id`, `recipe_id`) VALUES (?, ?)";
	private static final String DELETE_QUERY = "DELETE FROM player_recipes WHERE `player_id`=? AND `recipe_id`=?";

	@Override
	public RecipeList load(final int playerId) {
		final HashSet<Integer> recipeList = new HashSet<Integer>();
		DB.select(SELECT_QUERY, new ParamReadStH() {

			@Override
			public void setParams(PreparedStatement ps) throws SQLException {
				ps.setInt(1, playerId);
			}

			@Override
			public void handleRead(ResultSet rs) throws SQLException {
				while (rs.next()) {
					recipeList.add(rs.getInt("recipe_id"));
				}
			}
		});
		return new RecipeList(recipeList);
	}

	@Override
	public boolean addRecipe(final int playerId, final int recipeId) {
		return DB.insertUpdate(ADD_QUERY, new IUStH() {

			@Override
			public void handleInsertUpdate(PreparedStatement ps) throws SQLException {
				ps.setInt(1, playerId);
				ps.setInt(2, recipeId);
				ps.execute();
			}
		});
	}

	@Override
	public boolean delRecipe(final int playerId, final int recipeId) {
		return DB.insertUpdate(DELETE_QUERY, new IUStH() {

			@Override
			public void handleInsertUpdate(PreparedStatement ps) throws SQLException {
				ps.setInt(1, playerId);
				ps.setInt(2, recipeId);
				ps.execute();
			}
		});
	}

	@Override
	public boolean supports(String s, int i, int i1) {
		return MySQL5DAOUtils.supports(s, i, i1);
	}
}
