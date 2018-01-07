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
import com.aionemu.gameserver.dao.PlayerAppearanceDAO;
import com.aionemu.gameserver.model.gameobjects.player.PlayerAppearance;

/**
 * @author SoulKeeper, AEJTester, srx47
 */
public class MySQL5PlayerAppearanceDAO extends PlayerAppearanceDAO {

	private static final Logger log = LoggerFactory.getLogger(PlayerAppearanceDAO.class);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PlayerAppearance load(final int playerId) {
		Connection con = null;
		final PlayerAppearance pa = new PlayerAppearance();
		try {
			con = DatabaseFactory.getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT * FROM player_appearance WHERE player_id = ?");
			statement.setInt(1, playerId);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				pa.setVoice(resultSet.getInt("voice"));
				pa.setSkinRGB(resultSet.getInt("skin_rgb"));
				pa.setHairRGB(resultSet.getInt("hair_rgb"));
				pa.setEyeRGB(resultSet.getInt("eye_rgb"));
				pa.setLipRGB(resultSet.getInt("lip_rgb"));
				pa.setFace(resultSet.getInt("face"));
				pa.setHair(resultSet.getInt("hair"));
				pa.setDeco(resultSet.getInt("deco"));
				pa.setTattoo(resultSet.getInt("tattoo"));
				pa.setFaceContour(resultSet.getInt("face_contour"));
				pa.setExpression(resultSet.getInt("expression"));
				pa.setPupilShape(resultSet.getInt("pupil_shape"));
				pa.setRemoveMane(resultSet.getInt("remove_mane"));
				pa.setRightEyeRGB(resultSet.getInt("right_eye_rgb"));
				pa.setEyeLashShape(resultSet.getInt("eye_lash_shape"));
				pa.setJawLine(resultSet.getInt("jaw_line"));
				pa.setForehead(resultSet.getInt("forehead"));
				pa.setEyeHeight(resultSet.getInt("eye_height"));
				pa.setEyeSpace(resultSet.getInt("eye_space"));
				pa.setEyeWidth(resultSet.getInt("eye_width"));
				pa.setEyeSize(resultSet.getInt("eye_size"));
				pa.setEyeShape(resultSet.getInt("eye_shape"));
				pa.setEyeAngle(resultSet.getInt("eye_angle"));
				pa.setBrowHeight(resultSet.getInt("brow_height"));
				pa.setBrowAngle(resultSet.getInt("brow_angle"));
				pa.setBrowShape(resultSet.getInt("brow_shape"));
				pa.setNose(resultSet.getInt("nose"));
				pa.setNoseBridge(resultSet.getInt("nose_bridge"));
				pa.setNoseWidth(resultSet.getInt("nose_width"));
				pa.setNoseTip(resultSet.getInt("nose_tip"));
				pa.setCheek(resultSet.getInt("cheek"));
				pa.setLipHeight(resultSet.getInt("lip_height"));
				pa.setMouthSize(resultSet.getInt("mouth_size"));
				pa.setLipSize(resultSet.getInt("lip_size"));
				pa.setSmile(resultSet.getInt("smile"));
				pa.setLipShape(resultSet.getInt("lip_shape"));
				pa.setJawHeigh(resultSet.getInt("jaw_height"));
				pa.setChinJut(resultSet.getInt("chin_jut"));
				pa.setEarShape(resultSet.getInt("ear_shape"));
				pa.setHeadSize(resultSet.getInt("head_size"));
				pa.setNeck(resultSet.getInt("neck"));
				pa.setNeckLength(resultSet.getInt("neck_length"));
				pa.setShoulderSize(resultSet.getInt("shoulder_size"));
				pa.setTorso(resultSet.getInt("torso"));
				pa.setChest(resultSet.getInt("chest"));
				pa.setWaist(resultSet.getInt("waist"));
				pa.setHips(resultSet.getInt("hips"));
				pa.setArmThickness(resultSet.getInt("arm_thickness"));
				pa.setHandSize(resultSet.getInt("hand_size"));
				pa.setLegThickness(resultSet.getInt("leg_thickness"));
				pa.setFacialRate(resultSet.getInt("facial_rate"));
				pa.setFootSize(resultSet.getInt("foot_size"));
				pa.setArmLength(resultSet.getInt("arm_length"));
				pa.setLegLength(resultSet.getInt("leg_length"));
				pa.setShoulders(resultSet.getInt("shoulders"));
				pa.setFaceShape(resultSet.getInt("face_shape"));
				pa.setPupilSize(resultSet.getInt("pupil_size"));
				pa.setUpperTorso(resultSet.getInt("upper_torso"));
				pa.setForeArmThickness(resultSet.getInt("fore_arm_thickness"));
				pa.setHandSpan(resultSet.getInt("hand_span"));
				pa.setCalfThickness(resultSet.getInt("calf_thickness"));
				pa.setHeight(resultSet.getFloat("height"));
			}
			resultSet.close();
			statement.close();
		}
		catch (Exception e) {
			log.error("Could not restore PlayerAppearance data for player " + playerId + " from DB: " + e.getMessage(), e);
			return null;
		}
		finally {
			DatabaseFactory.close(con);
		}
		return pa;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean store(final int id, final PlayerAppearance pa) {

		return DB.insertUpdate("REPLACE INTO player_appearance (" + "player_id, voice, skin_rgb, hair_rgb, eye_rgb, lip_rgb, face, hair, deco, tattoo, face_contour, expression, pupil_shape, remove_mane, right_eye_rgb, eye_lash_shape," + "jaw_line, forehead, eye_height, eye_space, eye_width, eye_size, eye_shape, eye_angle, brow_height, brow_angle, brow_shape, nose, nose_bridge, nose_width, nose_tip," + "cheek, lip_height, mouth_size, lip_size, smile, lip_shape, jaw_height, chin_jut, ear_shape, head_size, neck, neck_length, shoulder_size, torso, chest, waist, hips," + "arm_thickness, hand_size, leg_thickness, facial_rate, foot_size, arm_length, leg_length, shoulders, face_shape, pupil_size, upper_torso, fore_arm_thickness, hand_span," + "calf_thickness, height)" + " VALUES " + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?" + ")", new IUStH() {

			@Override
			public void handleInsertUpdate(PreparedStatement ps) throws SQLException {
				log.debug("[DAO: MySQL5PlayerAppearanceDAO] storing appereance " + id);
				ps.setInt(1, id);
				ps.setInt(2, pa.getVoice());
				ps.setInt(3, pa.getSkinRGB());
				ps.setInt(4, pa.getHairRGB());
				ps.setInt(5, pa.getEyeRGB());
				ps.setInt(6, pa.getLipRGB());
				ps.setInt(7, pa.getFace());
				ps.setInt(8, pa.getHair());
				ps.setInt(9, pa.getDeco());
				ps.setInt(10, pa.getTattoo());
				ps.setInt(11, pa.getFaceContour());
				ps.setInt(12, pa.getExpression());
				ps.setInt(13, pa.getPupilShape());
				ps.setInt(14, pa.getRemoveMane());
				ps.setInt(15, pa.getRightEyeRGB());
				ps.setInt(16, pa.getEyeLashShape());
				ps.setInt(17, pa.getJawLine());
				ps.setInt(18, pa.getForehead());
				ps.setInt(19, pa.getEyeHeight());
				ps.setInt(20, pa.getEyeSpace());
				ps.setInt(21, pa.getEyeWidth());
				ps.setInt(22, pa.getEyeSize());
				ps.setInt(23, pa.getEyeShape());
				ps.setInt(24, pa.getEyeAngle());
				ps.setInt(25, pa.getBrowHeight());
				ps.setInt(26, pa.getBrowAngle());
				ps.setInt(27, pa.getBrowShape());
				ps.setInt(28, pa.getNose());
				ps.setInt(29, pa.getNoseBridge());
				ps.setInt(30, pa.getNoseWidth());
				ps.setInt(31, pa.getNoseTip());
				ps.setInt(32, pa.getCheek());
				ps.setInt(33, pa.getLipHeight());
				ps.setInt(34, pa.getMouthSize());
				ps.setInt(35, pa.getLipSize());
				ps.setInt(36, pa.getSmile());
				ps.setInt(37, pa.getLipShape());
				ps.setInt(38, pa.getJawHeigh());
				ps.setInt(39, pa.getChinJut());
				ps.setInt(40, pa.getEarShape());
				ps.setInt(41, pa.getHeadSize());
				ps.setInt(42, pa.getNeck());
				ps.setInt(43, pa.getNeckLength());
				ps.setInt(44, pa.getShoulderSize());
				ps.setInt(45, pa.getTorso());
				ps.setInt(46, pa.getChest());
				ps.setInt(47, pa.getWaist());
				ps.setInt(48, pa.getHips());
				ps.setInt(49, pa.getArmThickness());
				ps.setInt(50, pa.getHandSize());
				ps.setInt(51, pa.getLegThickness());
				ps.setInt(52, pa.getFacialRate());
				ps.setInt(53, pa.getFootSize());
				ps.setInt(54, pa.getArmLength());
				ps.setInt(55, pa.getLegLength());
				ps.setInt(56, pa.getShoulders());
				ps.setInt(57, pa.getFaceShape());
				ps.setInt(58, pa.getPupilSize());
				ps.setInt(59, pa.getUpperTorso());
				ps.setInt(60, pa.getForeArmThickness());
				ps.setInt(61, pa.getHandSpan());
				ps.setInt(62, pa.getCalfThickness());
				ps.setFloat(63, pa.getHeight());
				ps.execute();
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean supports(String s, int i, int i1) {
		return MySQL5DAOUtils.supports(s, i, i1);
	}
}
