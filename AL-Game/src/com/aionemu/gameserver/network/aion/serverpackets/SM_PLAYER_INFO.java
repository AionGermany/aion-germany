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
package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.configs.administration.AdminConfig;
import com.aionemu.gameserver.configs.main.MembershipConfig;
import com.aionemu.gameserver.configs.main.WeddingsConfig;
import com.aionemu.gameserver.dao.PlayerDAO;
import com.aionemu.gameserver.model.Gender;
import com.aionemu.gameserver.model.actions.PlayerMode;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PlayerAppearance;
import com.aionemu.gameserver.model.gameobjects.player.PlayerCommonData;
import com.aionemu.gameserver.model.gameobjects.player.PlayerConquererProtectorData;
import com.aionemu.gameserver.model.items.GodStone;
import com.aionemu.gameserver.model.items.ItemSlot;
import com.aionemu.gameserver.model.team.legion.LegionEmblemType;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

import javolution.util.FastList;

/**
 * This packet is displaying visible players.
 *
 * @author -Nemesiss-, Avol, srx47 modified cura
 * @modified -Enomine- -Artur-, Alcapwnd
 */
public class SM_PLAYER_INFO extends AionServerPacket {

	/**
	 * Visible player
	 */
	private final Player player;
	private boolean enemy;

	/**
	 * Constructs new <tt>SM_PLAYER_INFO </tt> packet
	 *
	 * @param player
	 *            actual player.
	 * @param enemy
	 */
	public SM_PLAYER_INFO(Player player, boolean enemy) {
		this.player = player;
		this.enemy = enemy;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con) {
		Player activePlayer = con.getActivePlayer();
		if (activePlayer == null || player == null) {
			return;
		}
		PlayerCommonData pcd = player.getCommonData();
		final int raceId;
		if (player.getAdminNeutral() > 1 || activePlayer.getAdminNeutral() > 1) {
			raceId = activePlayer.getRace().getRaceId();
		}
		else if (activePlayer.isEnemy(player)) {
			raceId = (activePlayer.getRace().getRaceId() == 0 ? 1 : 0);
		}
		else {
			raceId = player.getRace().getRaceId();
		}

		final int genderId = pcd.getGender().getGenderId();
		final PlayerAppearance playerAppearance = player.getPlayerAppearance();

		writeF(player.getX());// x
		writeF(player.getY());// y
		writeF(player.getZ());// z
		writeD(player.getObjectId());
		/**
		 * A3 female asmodian A2 male asmodian A1 female elyos A0 male elyos
		 */
		writeD(pcd.getTemplateId());
		writeD(player.getRobotId()); // 4.5
		/**
		 * Transformed state - send transformed model id Regular state - send player model id (from common data)
		 */
		int model = player.getTransformModel().getModelId();

		writeD(model != 0 ? model : pcd.getTemplateId());
		writeC(0x00); // new 2.0 Packet --- probably pet info?
		writeB(new byte[19]);// TODO (Changed from 11 to 19 on 5.6)
		writeD(player.getTransformModel().getType().getId());
		writeC(enemy ? 0x00 : 0x26);

		writeC(raceId); // race
		writeC(pcd.getPlayerClass().getClassId());
		writeC(genderId); // sex
		writeH(player.getState());

		writeB(new byte[8]);

		writeC(player.getHeading());

		// * = Several Custom Tags = * modified by Voidstar and Himiko

		String nameFormat = "%s";
		StringBuilder sb = new StringBuilder(nameFormat);
		if (player.getClientConnection() != null) {

			// * = Premium & VIP Membership
			if (MembershipConfig.PREMIUM_TAG_DISPLAY) {
				switch (player.getClientConnection().getAccount().getMembership()) {
					case 1:
						nameFormat = sb.replace(0, sb.length(), MembershipConfig.TAG_PREMIUM).toString();
						break;
					case 2:
						nameFormat = sb.replace(0, sb.length(), MembershipConfig.TAG_VIP).toString();
						break;
				}
			}
			if (player.isMarried()) {
				String partnerName = DAOManager.getDAO(PlayerDAO.class).getPlayerNameByObjId(player.getPartnerId());
				String tag = WeddingsConfig.TAG_WEDDING; 
	            nameFormat += " " + tag + " " + partnerName;
			}
			// * = Server Staff Access Level
			if (AdminConfig.CUSTOMTAG_ENABLE && player.isGmMode()) {
				switch (player.getClientConnection().getAccount().getAccessLevel()) {
					case 1:
						nameFormat = AdminConfig.CUSTOMTAG_ACCESS1.replace("%s", sb.toString());
						break;
					case 2:
						nameFormat = AdminConfig.CUSTOMTAG_ACCESS2.replace("%s", sb.toString());
						break;
					case 3:
						nameFormat = AdminConfig.CUSTOMTAG_ACCESS3.replace("%s", sb.toString());
						break;
					case 4:
						nameFormat = AdminConfig.CUSTOMTAG_ACCESS4.replace("%s", sb.toString());
						break;
					case 5:
						nameFormat = AdminConfig.CUSTOMTAG_ACCESS5.replace("%s", sb.toString());
						break;
					case 6:
						nameFormat = AdminConfig.CUSTOMTAG_ACCESS6.replace("%s", sb.toString());
						break;
					case 7:
						nameFormat = AdminConfig.CUSTOMTAG_ACCESS7.replace("%s", sb.toString());
						break;
					case 8:
						nameFormat = AdminConfig.CUSTOMTAG_ACCESS8.replace("%s", sb.toString());
						break;
					case 9:
						nameFormat = AdminConfig.CUSTOMTAG_ACCESS9.replace("%s", sb.toString());
						break;
					case 10:
						nameFormat = AdminConfig.CUSTOMTAG_ACCESS10.replace("%s", sb.toString());
						break;
				}
			}
		}

		writeS(String.format(nameFormat, player.getName()));

		writeH(pcd.getTitleId());
		writeH(player.getCommonData().isHaveMentorFlag() ? 1 : 0);

		writeH(player.getCastingSkillId());

		if (player.isLegionMember()) {
			writeD(player.getLegion().getLegionId());
			writeC(player.getLegion().getLegionEmblem().getEmblemId());
			writeC(player.getLegion().getLegionEmblem().getEmblemType().getValue());
			writeC(player.getLegion().getLegionEmblem().getEmblemType() == LegionEmblemType.DEFAULT ? 0x00 : 0xFF);
			writeC(player.getLegion().getLegionEmblem().getColor_r());
			writeC(player.getLegion().getLegionEmblem().getColor_g());
			writeC(player.getLegion().getLegionEmblem().getColor_b());
			writeS(player.getLegion().getLegionName());
		}
		else {
			writeB(new byte[12]);
		}
		int maxHp = player.getLifeStats().getMaxHp();
		int currHp = player.getLifeStats().getCurrentHp();
		writeC(100 * currHp / maxHp);// %hp
		writeH(pcd.getDp());// current dp
		writeC(0x00);// unk (0x00)

		int mask = 0;

		FastList<Item> items = player.getEquipment().getEquippedForApparence();
		for (Item item : items) {
			if (item.getItemTemplate().isTwoHandWeapon()) {
				ItemSlot[] slots = ItemSlot.getSlotsFor(item.getEquipmentSlot());
				mask |= slots[0].getSlotIdMask();
			}
			else {
				mask |= item.getEquipmentSlot();
			}
		}

		writeD(mask); // DBS size

		for (Item item : items) {
			writeD(item.getItemSkinTemplate().getTemplateId());
			GodStone godStone = item.getGodStone();
			writeD(godStone != null ? godStone.getItemId() : 0);
			writeD(item.getItemColor());
			if (item.getItemTemplate().isAccessory()) {
				if (item.getItemTemplate().isPlume()) {
					float authorize = item.getAuthorize() / 5;
					if (item.getAuthorize() >= 5) {
						authorize = authorize > 2.0F ? 2.0F : authorize;
						writeD((int) authorize << 3);
					}
					else {
						writeD(0);
					}
				}
				else if (item.getItemTemplate().isBracelet()) {
					if (item.getAuthorize() >= 5 && item.getAuthorize() < 10) {
						writeD(96);
					}
					else if (item.getAuthorize() >= 10) {
						writeD(160);
					}
					else {
						writeD(32);
					}
				}
				else {
					writeD(item.getAuthorize() >= 5 ? 2 : 0);
				}
			}
			else if ((item.getItemTemplate().isWeapon()) || (item.getItemTemplate().isTwoHandWeapon())) {
				writeD(item.getEnchantLevel() == 15 ? 2 : item.getEnchantLevel() >= 20 ? 4 : 0);
			}
			else {
				writeD(0);
			}
		}

		writeD(playerAppearance.getSkinRGB());
		writeD(playerAppearance.getHairRGB());
		writeD(playerAppearance.getEyeRGB()); // TODO LEFT EYE
		writeD(playerAppearance.getLipRGB());
		writeC(playerAppearance.getFace());
		writeC(playerAppearance.getHair());
		writeC(playerAppearance.getDeco());
		writeC(playerAppearance.getTattoo());
		writeC(playerAppearance.getFaceContour());
		writeC(playerAppearance.getExpression());
		writeC(playerAppearance.getPupilShape());
		writeC(playerAppearance.getRemoveMane());
		writeD(playerAppearance.getRightEyeRGB());
		writeC(playerAppearance.getEyeLashShape());
		if (player.getGender() == Gender.FEMALE) {
			writeC(6);
		}
		else {
			writeC(5);
		}
		writeC(playerAppearance.getJawLine());
		writeC(playerAppearance.getForehead());
		writeC(playerAppearance.getEyeHeight());
		writeC(playerAppearance.getEyeSpace());
		writeC(playerAppearance.getEyeWidth());
		writeC(playerAppearance.getEyeSize());
		writeC(playerAppearance.getEyeShape());
		writeC(playerAppearance.getEyeAngle());
		writeC(playerAppearance.getBrowHeight());
		writeC(playerAppearance.getBrowAngle());
		writeC(playerAppearance.getBrowShape());
		writeC(playerAppearance.getNose());
		writeC(playerAppearance.getNoseBridge());
		writeC(playerAppearance.getNoseWidth());
		writeC(playerAppearance.getNoseTip());
		writeC(playerAppearance.getCheek());
		writeC(playerAppearance.getLipHeight());
		writeC(playerAppearance.getMouthSize());
		writeC(playerAppearance.getLipSize());
		writeC(playerAppearance.getSmile());
		writeC(playerAppearance.getLipShape());
		writeC(playerAppearance.getJawHeigh());
		writeC(playerAppearance.getChinJut());
		writeC(playerAppearance.getEarShape());
		writeC(playerAppearance.getHeadSize());
		// 1.5.x 0x00, shoulderSize, armLength, legLength (BYTE) after HeadSize

		writeC(playerAppearance.getNeck());
		writeC(playerAppearance.getNeckLength());
		writeC(playerAppearance.getShoulderSize()); // shoulderSize
		writeC(playerAppearance.getTorso());
		writeC(playerAppearance.getChest());
		writeC(playerAppearance.getWaist());
		writeC(playerAppearance.getHips());
		writeC(playerAppearance.getArmThickness());
		writeC(playerAppearance.getHandSize());
		writeC(playerAppearance.getLegThickness());
		writeC(playerAppearance.getFootSize());
		writeC(playerAppearance.getFacialRate());
		writeC(0);// unk;
		writeC(playerAppearance.getArmLength()); // armLength
		writeC(playerAppearance.getLegLength()); // legLength
		writeC(playerAppearance.getShoulders());
		writeC(playerAppearance.getFaceShape());
		writeC(playerAppearance.getPupilSize());
		writeC(playerAppearance.getUpperTorso());
		writeC(playerAppearance.getForeArmThickness());
		writeC(playerAppearance.getHandSpan());
		writeC(playerAppearance.getCalfThickness());
		writeC(playerAppearance.getVoice());
		writeF(playerAppearance.getHeight());
		writeF(0.25f); // scale
		writeF(2.0f); // gravity or slide surface o_O
		writeF(player.getGameStats().getMovementSpeedFloat()); // move speed
		writeH(player.getGameStats().getAttackSpeed().getBase());
		writeH(player.getGameStats().getAttackSpeed().getCurrent());
		writeC(player.getPortAnimation());

		writeS(player.hasStore() ? player.getStore().getStoreMessage() : "");// private store message

		/**
		 * Movement
		 */
		writeF(0);
		writeF(0);
		writeF(0);
		writeF(player.getX());// x
		writeF(player.getY());// y
		writeF(player.getZ());// z
		writeC(0x00); // move type ?
		if (player.isUsingFlyTeleport()) {
			writeD(player.getFlightTeleportId());
			writeD(player.getFlightDistance());
		}
		else if (player.isInPlayerMode(PlayerMode.WINDSTREAM)) {
			writeD(player.windstreamPath.teleportId);
			writeD(player.windstreamPath.distance);
		}
		// ?
		writeC(player.getVisualState()); // visualState
		writeS(player.getCommonData().getNote()); // note show in right down windows if your target on player
		writeH(player.getLevel()); // [level]
		writeH(player.getPlayerSettings().getDisplay()); // unk - 0x04
		writeH(player.getPlayerSettings().getDeny()); // unk - 0x00
		writeH(player.getAbyssRank().getRank().getId()); // abyss rank

		writeH(0x00); // unk - 0x01
		writeD(0x08); // unk 5.4
		writeD(player.getTarget() == null ? 0 : player.getTarget().getObjectId()); // target status
		writeC(0); // suspect id
		writeD(player.getBonusTime().isBonus() ? 1 : 0); // Abbey Return Bonus 1 - true, 0 - false
		writeC(player.isMentor() ? 1 : 0);
		writeD(player.getHouseOwnerId()); // 3.0
		writeD(player.getBonusTime().getStatus().getId()); // Abbey Return Buff ID 1 -Normal, 2 - New, 3 Return
		writeD(0x00);// unk 0x00 4.7 //TODO need to figure out
		writeC(raceId == 0 ? 3 : 5); // language asmo:3 ely:5
		/**
		 * === Conqueror === 0x01 = Conquerers Will Lvl 1 (Buff you get for killing enemies from their home map) 0x02 = Furious Conquerers Will Lvl 2 (Buff you get for killing enemies from their home
		 * map) 0x03 = Berserk Conquerers Will Lvl 3 (Buff you get for killing enemies from their home map) === Protector == 0x01 = Protector Lvl 1 0x02 = Protector Lvl 2 0x03 = Protector Lvl 3
		 */
		PlayerConquererProtectorData pcdd = player.getConquerorProtectorData();
		writeC(pcdd.getConquerorBuffLevel());
		writeC(pcdd.getProtectorBuffLevel());
		switch (player.getAbyssRank().getRank()) {
			case STAR1_OFFICER:
			case STAR2_OFFICER:
			case STAR3_OFFICER:
			case STAR4_OFFICER:
			case STAR5_OFFICER:
		        writeC(1); // 4.9
		        break;
			case GENERAL:
			case GREAT_GENERAL:
		        writeC(2); // 4.9
		        break;
			case COMMANDER:
		        writeC(3); // 4.9
		        break;
			case SUPREME_COMMANDER:
		        writeC(4); // 4.9
		        break;
			default:
		        writeC(0); // 4.9
		        break;
		    }
		writeD(0x00); // 5.4 found value 1000,2000 and 0
	}
}
