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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.DatabaseFactory;
import com.aionemu.gameserver.dao.MySQL5DAOUtils;
import com.aionemu.gameserver.dao.PlayerEquipmentSettingDAO;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.equipmentsetting.EquipmentSetting;
import com.aionemu.gameserver.model.gameobjects.player.equipmentsetting.EquipmentSettingList;

/**
 * @author Falke_34
 */

public class MySQL5PlayerEquipmentSettingDAO extends PlayerEquipmentSettingDAO {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(PlayerEquipmentSettingDAO.class);
	public static final String INSERT_QUERY = "INSERT INTO `player_equipment_setting` (`player_id`, `slot`, `display`, `m_hand`, `s_hand`, `helmet`, `torso`, `glove`, `boots`, `earrings_left`, `earrings_right`, `ring_left`, `ring_right`, `necklace`, `shoulder`, `pants`, `powershard_left`, `powershard_right`, `wings`, `waist`, `m_off_hand`, `s_off_hand`, `plume`, `bracelet`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE display = values(display), m_hand = values(m_hand), s_hand=values(s_hand), helmet=values(helmet), torso=values(torso), glove=values(glove), boots=values(boots), earrings_left=values(earrings_left), earrings_right=values(earrings_right), ring_left=values(ring_left), ring_right=values(ring_right), necklace=values(necklace), shoulder=values(shoulder), pants=values(pants), powershard_left=values(powershard_left), powershard_right=values(powershard_right), wings=values(wings), waist=values(waist), m_off_hand=values(m_off_hand), s_off_hand=values(s_off_hand),plume=values(plume), bracelet=values(bracelet)";
	public static final String SELECT_QUERY = "SELECT *  FROM `player_equipment_setting` WHERE `player_id`=?";

	public void loadEquipmentSetting(Player player) {
		EquipmentSettingList equipmentSettingList = new EquipmentSettingList(player);
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(SELECT_QUERY);
			stmt.setInt(1, player.getObjectId());
			ResultSet rset = stmt.executeQuery();
			while (rset.next()) {
				int slot = rset.getInt("slot");
				int display = rset.getInt("display");
				int mHand = rset.getInt("m_hand");
				int sHand = rset.getInt("s_hand");
				int helmet = rset.getInt("helmet");
				int torso = rset.getInt("torso");
				int glove = rset.getInt("glove");
				int boots = rset.getInt("boots");
				int earringsLeft = rset.getInt("earrings_left");
				int earringsRight = rset.getInt("earrings_right");
				int ringLeft = rset.getInt("ring_left");
				int ringRight = rset.getInt("ring_right");
				int necklace = rset.getInt("necklace");
				int shoulder = rset.getInt("shoulder");
				int pants = rset.getInt("pants");
				int powershardLeft = rset.getInt("powershard_left");
				int powershardRight = rset.getInt("powershard_right");
				int wings = rset.getInt("wings");
				int waist = rset.getInt("waist");
				int mOffHand = rset.getInt("m_off_hand");
				int sOffHand = rset.getInt("s_off_hand");
				int plume = rset.getInt("plume");
				int bracelet = rset.getInt("bracelet");
				equipmentSettingList.add(slot, display, mHand, sHand, helmet, torso, glove, boots, earringsLeft, earringsRight, ringLeft,
					ringRight, necklace, shoulder, pants, powershardLeft, powershardRight, wings, waist, mOffHand, sOffHand, plume,
					bracelet, false);
			}
			rset.close();
			stmt.close();
		}
		catch (Exception e) {
			log.error("Could not restore equipment setting for playerObjId: " + player.getObjectId() + " from DB: " + e.getMessage(), e);
		}
		finally {
			DatabaseFactory.close(con);
		}
		player.setEquipmentSettingList(equipmentSettingList);
	}

	@Override
	public void insertEquipmentSetting(Player player, EquipmentSetting equipmentSetting) {
		Connection con = null;
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(INSERT_QUERY);
			stmt.setInt(1, player.getObjectId());
			stmt.setInt(2, equipmentSetting.getSlot());
			stmt.setInt(3, equipmentSetting.getDisplay());
			stmt.setInt(4, equipmentSetting.getmHand());
			stmt.setInt(5, equipmentSetting.getsHand());
			stmt.setInt(6, equipmentSetting.getHelmet());
			stmt.setInt(7, equipmentSetting.getTorso());
			stmt.setInt(8, equipmentSetting.getGlove());
			stmt.setInt(9, equipmentSetting.getBoots());
			stmt.setInt(10, equipmentSetting.getEarringsLeft());
			stmt.setInt(11, equipmentSetting.getEarringsRight());
			stmt.setInt(12, equipmentSetting.getRingLeft());
			stmt.setInt(13, equipmentSetting.getRingRight());
			stmt.setInt(14, equipmentSetting.getNecklace());
			stmt.setInt(15, equipmentSetting.getShoulder());
			stmt.setInt(16, equipmentSetting.getPants());
			stmt.setInt(17, equipmentSetting.getPowershardLeft());
			stmt.setInt(18, equipmentSetting.getPowershardRight());
			stmt.setInt(19, equipmentSetting.getWings());
			stmt.setInt(20, equipmentSetting.getWaist());
			stmt.setInt(21, equipmentSetting.getmOffHand());
			stmt.setInt(22, equipmentSetting.getsOffHand());
			stmt.setInt(23, equipmentSetting.getPlume());
			stmt.setInt(24, equipmentSetting.getBracelet());
			stmt.execute();
			stmt.close();
		}
		catch (Exception e) {
			log.error("Could not store equipment setting for player " + player.getObjectId() + " from DB: " + e.getMessage(), e);
		}
		finally {
			DatabaseFactory.close(con);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean supports(String databaseName, int majorVersion, int minorVersion) {
		return MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
	}
}
