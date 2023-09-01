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
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.DB;
import com.aionemu.commons.database.DatabaseFactory;
import com.aionemu.commons.utils.GenericValidator;
import com.aionemu.gameserver.dao.MySQL5DAOUtils;
import com.aionemu.gameserver.dao.PlayerRegisteredItemsDAO;
import com.aionemu.gameserver.model.gameobjects.AionObject;
import com.aionemu.gameserver.model.gameobjects.HouseDecoration;
import com.aionemu.gameserver.model.gameobjects.HouseObject;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.house.House;
import com.aionemu.gameserver.model.house.HouseRegistry;
import com.aionemu.gameserver.model.templates.housing.HouseType;
import com.aionemu.gameserver.model.templates.housing.PartType;
import com.aionemu.gameserver.services.HousingService;
import com.aionemu.gameserver.services.item.HouseObjectFactory;
import com.aionemu.gameserver.utils.idfactory.IDFactory;
import com.aionemu.gameserver.world.World;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

import javolution.util.FastList;

/**
 * @author Rolandas
 */
public class MySQL5PlayerRegisteredItemsDAO extends PlayerRegisteredItemsDAO {

	private static final Logger log = LoggerFactory.getLogger(MySQL5PlayerRegisteredItemsDAO.class);
	public static final String CLEAN_PLAYER_QUERY = "DELETE FROM `player_registered_items` WHERE `player_id` = ?";
	public static final String SELECT_QUERY = "SELECT * FROM `player_registered_items` WHERE `player_id`=?";
	public static final String INSERT_QUERY = "INSERT INTO `player_registered_items` " + "(`expire_time`,`color`,`color_expires`,`owner_use_count`,`visitor_use_count`,`x`,`y`,`z`,`h`,`area`,`floor`,`player_id`,`item_unique_id`,`item_id`) VALUES " + "(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	public static final String UPDATE_QUERY = "UPDATE `player_registered_items` SET " + "`expire_time`=?,`color`=?,`color_expires`=?,`owner_use_count`=?,`visitor_use_count`=?,`x`=?,`y`=?,`z`=?,`h`=?,`area`=?,`floor`=? " + "WHERE `player_id`=? AND `item_unique_id`=? AND `item_id`=?";
	public static final String DELETE_QUERY = "DELETE FROM `player_registered_items` WHERE `item_unique_id` = ?";
	public static final String RESET_QUERY = "UPDATE `player_registered_items` SET x=0,y=0,z=0,h=0,area='NONE' WHERE `player_id`=? AND `area` != 'DECOR'";
	private static final Predicate<HouseObject<?>> objectsToAddPredicate = new Predicate<HouseObject<?>>() {

		@Override
		public boolean apply(@Nullable HouseObject<?> input) {
			return input != null && (input.getPersistentState() == PersistentState.NEW);
		}
	};
	private static final Predicate<HouseObject<?>> objectsToUpdatePredicate = new Predicate<HouseObject<?>>() {

		@Override
		public boolean apply(@Nullable HouseObject<?> input) {
			return input != null && (input.getPersistentState() == PersistentState.UPDATE_REQUIRED);
		}
	};
	private static final Predicate<HouseObject<?>> objectsToDeletePredicate = new Predicate<HouseObject<?>>() {

		@Override
		public boolean apply(@Nullable HouseObject<?> input) {
			return input != null && PersistentState.DELETED == input.getPersistentState();
		}
	};
	private static final Predicate<HouseDecoration> partsToAddPredicate = new Predicate<HouseDecoration>() {

		@Override
		public boolean apply(@Nullable HouseDecoration input) {
			return input != null && (input.getPersistentState() == PersistentState.NEW);
		}
	};
	private static final Predicate<HouseDecoration> partsToUpdatePredicate = new Predicate<HouseDecoration>() {

		@Override
		public boolean apply(@Nullable HouseDecoration input) {
			return input != null && (input.getPersistentState() == PersistentState.UPDATE_REQUIRED);
		}
	};
	private static final Predicate<HouseDecoration> partsToDeletePredicate = new Predicate<HouseDecoration>() {

		@Override
		public boolean apply(@Nullable HouseDecoration input) {
			return input != null && PersistentState.DELETED == input.getPersistentState();
		}
	};

	@Override
	public int[] getUsedIDs() {
		PreparedStatement statement = DB.prepareStatement("SELECT item_unique_id FROM player_registered_items WHERE item_unique_id <> 0", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

		try {
			ResultSet rs = statement.executeQuery();
			rs.last();
			int count = rs.getRow();
			rs.beforeFirst();
			int[] ids = new int[count];
			for (int i = 0; i < count; i++) {
				rs.next();
				ids[i] = rs.getInt(1);
			}
			return ids;
		}
		catch (SQLException e) {
			log.error("Can't get list of id's from player_registered_items table", e);
		}
		finally {
			DB.close(statement);
		}

		return new int[0];
	}

	@Override
	public void loadRegistry(int playerId) {
		House house = HousingService.getInstance().getPlayerStudio(playerId);
		if (house == null) {
			int address = HousingService.getInstance().getPlayerAddress(playerId);
			house = HousingService.getInstance().getHouseByAddress(address);
		}
		HouseRegistry registry = house.getRegistry();
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = DatabaseFactory.getConnection();
			stmt = con.prepareStatement(SELECT_QUERY);
			stmt.setInt(1, playerId);
			ResultSet rset = stmt.executeQuery();
			HashMap<PartType, List<HouseDecoration>> usedParts = new HashMap<PartType, List<HouseDecoration>>();
			while (rset.next()) {
				String area = rset.getString("area");
				if ("DECOR".equals(area)) {
					HouseDecoration dec = createDecoration(rset);
					registry.putCustomPart(dec);
					if (dec.isUsed()) {
						if (house.getHouseType() != HouseType.PALACE && dec.getFloor() > 0) {
							dec.setFloor(0);
						}
						List<HouseDecoration> usedForType = usedParts.get(dec.getTemplate().getType());
						if (usedForType == null) {
							usedForType = new ArrayList<HouseDecoration>();
							usedParts.put(dec.getTemplate().getType(), usedForType);
						}
						usedForType.add(dec);
					}
					dec.setPersistentState(PersistentState.UPDATED);
				}
				else {
					HouseObject<?> obj = constructObject(registry, house, rset);
					registry.putObject(obj);
					obj.setPersistentState(PersistentState.UPDATED);
				}
			}
			for (PartType partType : PartType.values()) {
				if (usedParts.containsKey(partType)) {
					for (HouseDecoration usedDeco : usedParts.get(partType)) {
						registry.setPartInUse(usedDeco, usedDeco.getFloor());
					}
					continue;
				}
				int floorCount = 1;
				if (house.getHouseType() == HouseType.PALACE && (partType == PartType.INFLOOR_ANY || partType == PartType.INWALL_ANY)) {
					floorCount = 6;
				}
				for (int i = 0; i < floorCount; i++) {
					HouseDecoration def = registry.getDefaultPartByType(partType, i);
					if (def != null) {
						registry.setPartInUse(def, i);
					}
				}
			}
			registry.setPersistentState(PersistentState.UPDATED);
			rset.close();
		}
		catch (Exception e) {
			log.error("Could not restore house registry data for player: " + playerId + " from DB: " + e.getMessage(), e);
		}
		finally {
			DatabaseFactory.close(stmt, con);
		}
	}

	private HouseObject<?> constructObject(final HouseRegistry registry, House house, ResultSet rset) throws SQLException, IllegalAccessException {
		int itemUniqueId = rset.getInt("item_unique_id");
		VisibleObject visObj = World.getInstance().findVisibleObject(itemUniqueId);
		HouseObject<?> obj = null;
		if (visObj != null) {
			if (visObj instanceof HouseObject<?>) {
				obj = (HouseObject<?>) visObj;
			}
			else {
				throw new IllegalAccessException("Someone stole my house object id : " + itemUniqueId);
			}
		}
		else {
			obj = registry.getObjectByObjId(itemUniqueId);
			if (obj == null) {
				obj = HouseObjectFactory.createNew(house, itemUniqueId, rset.getInt("item_id"));
			}
		}
		obj.setOwnerUsedCount(rset.getInt("owner_use_count"));
		obj.setVisitorUsedCount(rset.getInt("visitor_use_count"));
		obj.setX(rset.getFloat("x"));
		obj.setY(rset.getFloat("y"));
		obj.setZ(rset.getFloat("z"));
		obj.setHeading((byte) rset.getInt("h"));
		obj.setColor(rset.getInt("color"));
		obj.setColorExpireEnd(rset.getInt("color_expires"));
		if (obj.getObjectTemplate().getUseDays() > 0) {
			obj.setExpireTime(rset.getInt("expire_time"));
		}
		return obj;
	}

	private HouseDecoration createDecoration(ResultSet rset) throws SQLException {
		int itemUniqueId = rset.getInt("item_unique_id");
		int itemId = rset.getInt("item_Id");
		byte floor = rset.getByte("floor");
		HouseDecoration decor = new HouseDecoration(itemUniqueId, itemId, floor);
		decor.setUsed(rset.getInt("owner_use_count") > 0);
		return decor;
	}

	@Override
	public boolean store(HouseRegistry registry, int playerId) {
		FastList<HouseObject<?>> objects = registry.getObjects();
		FastList<HouseDecoration> decors = registry.getAllParts();
		Collection<HouseObject<?>> objectsToAdd = Collections2.filter(objects, objectsToAddPredicate);
		Collection<HouseObject<?>> objectsToUpdate = Collections2.filter(objects, objectsToUpdatePredicate);
		Collection<HouseObject<?>> objectsToDelete = Collections2.filter(objects, objectsToDeletePredicate);
		Collection<HouseDecoration> partsToAdd = Collections2.filter(decors, partsToAddPredicate);
		Collection<HouseDecoration> partsToUpdate = Collections2.filter(decors, partsToUpdatePredicate);
		Collection<HouseDecoration> partsToDelete = Collections2.filter(decors, partsToDeletePredicate);

		boolean objectDeleteResult = false;
		boolean partsDeleteResult = false;

		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			con.setAutoCommit(false);
			objectDeleteResult = deleteObjects(con, objectsToDelete);
			partsDeleteResult = deleteParts(con, partsToDelete);
			storeObjects(con, objectsToUpdate, playerId, false);
			storeParts(con, partsToUpdate, playerId, false);
			storeObjects(con, objectsToAdd, playerId, true);
			storeParts(con, partsToAdd, playerId, true);
			registry.setPersistentState(PersistentState.UPDATED);
		}
		catch (SQLException e) {
			log.error("Can't open connection to save player inventory: " + playerId);
		}
		finally {
			DatabaseFactory.close(con);
		}

		for (HouseObject<?> obj : objects) {
			if (obj.getPersistentState() == PersistentState.DELETED) {
				registry.discardObject(obj.getObjectId());
			}
			else {
				obj.setPersistentState(PersistentState.UPDATED);
			}
		}

		for (HouseDecoration decor : decors) {
			if (decor.getPersistentState() == PersistentState.DELETED) {
				registry.discardPart(decor);
			}
			else {
				decor.setPersistentState(PersistentState.UPDATED);
			}
		}

		if (!objectsToDelete.isEmpty() && objectDeleteResult) {
			Collection<Integer> idIterator = Collections2.transform(objectsToDelete, AionObject.OBJECT_TO_ID_TRANSFORMER);
			IDFactory.getInstance().releaseIds(idIterator);
		}

		if (!partsToDelete.isEmpty() && partsDeleteResult) {
			for (HouseDecoration part : partsToDelete) {
				if (part.getObjectId() != 0) {
					IDFactory.getInstance().releaseId(part.getObjectId());
				}
			}
		}

		return true;
	}

	private boolean storeObjects(Connection con, Collection<HouseObject<?>> objects, int playerId, boolean isNew) {

		if (GenericValidator.isBlankOrNull(objects)) {
			return true;
		}

		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(isNew ? INSERT_QUERY : UPDATE_QUERY);

			for (HouseObject<?> obj : objects) {
				if (obj.getExpireTime() > 0) {
					stmt.setInt(1, obj.getExpireTime());
				}
				else {
					stmt.setNull(1, Types.INTEGER);
				}

				if (obj.getColor() == null) {
					stmt.setNull(2, Types.INTEGER);
				}
				else {
					stmt.setInt(2, obj.getColor());
				}

				stmt.setInt(3, obj.getColorExpireEnd());
				stmt.setInt(4, obj.getOwnerUsedCount());
				stmt.setInt(5, obj.getVisitorUsedCount());
				stmt.setFloat(6, obj.getX());
				stmt.setFloat(7, obj.getY());
				stmt.setFloat(8, obj.getZ());
				stmt.setInt(9, obj.getHeading());
				if (obj.getX() > 0 || obj.getY() > 0 || obj.getZ() > 0) {
					stmt.setString(10, obj.getPlaceArea().toString());
				}
				else {
					stmt.setString(10, "NONE");
				}
				stmt.setByte(11, (byte) 0);
				stmt.setInt(12, playerId);
				stmt.setInt(13, obj.getObjectId());
				stmt.setInt(14, obj.getObjectTemplate().getTemplateId());
				stmt.addBatch();
			}

			stmt.executeBatch();
			con.commit();
		}
		catch (Exception e) {
			log.error("Failed to execute house object update batch", e);
			return false;
		}
		finally {
			DatabaseFactory.close(stmt);
		}
		return true;
	}

	private boolean storeParts(Connection con, Collection<HouseDecoration> parts, int playerId, boolean isNew) {

		if (GenericValidator.isBlankOrNull(parts)) {
			return true;
		}

		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(isNew ? INSERT_QUERY : UPDATE_QUERY);
			for (HouseDecoration part : parts) {
				stmt.setNull(1, Types.INTEGER);
				stmt.setNull(2, Types.INTEGER);
				stmt.setInt(3, 0);
				stmt.setInt(4, part.isUsed() ? 1 : 0);
				stmt.setInt(5, 0);
				stmt.setFloat(6, 0);
				stmt.setFloat(7, 0);
				stmt.setFloat(8, 0);
				stmt.setInt(9, 0);
				stmt.setString(10, "DECOR");
				stmt.setByte(11, part.getFloor());
				stmt.setInt(12, playerId);
				stmt.setInt(13, part.getObjectId());
				stmt.setInt(14, part.getTemplate().getId());
				stmt.addBatch();
			}

			stmt.executeBatch();
			con.commit();
		}
		catch (Exception e) {
			log.error("Failed to execute house parts update batch", e);
			return false;
		}
		finally {
			DatabaseFactory.close(stmt);
		}
		return true;
	}

	private boolean deleteObjects(Connection con, Collection<HouseObject<?>> objects) {
		if (GenericValidator.isBlankOrNull(objects)) {
			return true;
		}

		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(DELETE_QUERY);
			for (HouseObject<?> obj : objects) {
				stmt.setInt(1, obj.getObjectId());
				stmt.addBatch();
			}

			stmt.executeBatch();
			con.commit();
		}
		catch (Exception e) {
			log.error("Failed to execute delete batch", e);
			return false;
		}
		finally {
			DatabaseFactory.close(stmt);
		}
		return true;
	}

	private boolean deleteParts(Connection con, Collection<HouseDecoration> parts) {
		if (GenericValidator.isBlankOrNull(parts)) {
			return true;
		}

		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(DELETE_QUERY);
			for (HouseDecoration part : parts) {
				stmt.setInt(1, part.getObjectId());
				stmt.addBatch();
			}

			stmt.executeBatch();
			con.commit();
		}
		catch (Exception e) {
			log.error("Failed to execute delete batch", e);
			return false;
		}
		finally {
			DatabaseFactory.close(stmt);
		}
		return true;
	}

	@Override
	public boolean deletePlayerItems(int playerId) {
		Connection con = null;
		try {
			log.info("Deleting player items");
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(CLEAN_PLAYER_QUERY);
			stmt.setInt(1, playerId);
			stmt.execute();
			stmt.close();
		}
		catch (Exception e) {
			log.error("Error in deleting all player registered items. PlayerObjId: " + playerId, e);
			return false;
		}
		finally {
			DatabaseFactory.close(con);
		}
		return true;
	}

	@Override
	public void resetRegistry(int playerId) {
		Connection con = null;
		try {
			log.info("resetting player items: " + playerId);
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(RESET_QUERY);
			stmt.setInt(1, playerId);
			stmt.execute();
			stmt.close();
		}
		catch (Exception e) {
			log.error("Error in resetting  player registered items. PlayerObjId: " + playerId, e);
		}
		finally {
			DatabaseFactory.close(con);
		}
	}

	@Override
	public boolean supports(String databaseName, int majorVersion, int minorVersion) {
		return MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
	}
}
