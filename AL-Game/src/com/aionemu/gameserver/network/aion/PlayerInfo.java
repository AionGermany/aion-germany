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
package com.aionemu.gameserver.network.aion;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.model.account.PlayerAccountData;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.PlayerAppearance;
import com.aionemu.gameserver.model.gameobjects.player.PlayerCommonData;
import com.aionemu.gameserver.model.items.GodStone;
import com.aionemu.gameserver.model.items.ItemSlot;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;

/**
 * @author AEJTester
 * @author Nemesiss
 * @author Niato
 */
public abstract class PlayerInfo extends AionServerPacket {

	private static Logger log = LoggerFactory.getLogger(PlayerInfo.class);

	protected PlayerInfo() {
	}

	protected void writePlayerInfo(PlayerAccountData accPlData) {
		PlayerCommonData pbd = accPlData.getPlayerCommonData();
		final int raceId = pbd.getRace().getRaceId();
		final int genderId = pbd.getGender().getGenderId();
		final PlayerAppearance playerAppearance = accPlData.getAppereance();
		writeD(pbd.getPlayerObjId());
		writeS(pbd.getName(), 52);
		writeD(genderId);
		writeD(raceId);
		writeD(pbd.getPlayerClass().getClassId());
		writeD(playerAppearance.getVoice());
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
		writeC(0x06);// UNK 6
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
		writeC(playerAppearance.getArmLength());
		writeC(playerAppearance.getLegLength());
		writeC(playerAppearance.getShoulders());
		writeC(playerAppearance.getFaceShape());
		writeC(playerAppearance.getPupilSize());
		writeC(playerAppearance.getUpperTorso());
		writeC(playerAppearance.getForeArmThickness());
		writeC(playerAppearance.getHandSpan());
		writeC(playerAppearance.getCalfThickness());
		writeC(0x00);// always 0 may be acessLevel
		writeC(0x00);// always 0
		writeC(0x00);// always 0
		writeF(playerAppearance.getHeight());
		writeD(genderId == 0 ? 100000 : 100001);// 100000 = Male 100001 = Female
		writeD(pbd.getPosition().getMapId());// mapid for preloading map
		writeF(pbd.getPosition().getX());
		writeF(pbd.getPosition().getY());
		writeF(pbd.getPosition().getZ());
		writeD(pbd.getPosition().getHeading());
		writeH(pbd.getLevel()); // lvl confirmed
		writeH(0);
		writeD(pbd.getTitleId());
		if (accPlData.isLegionMember()) {
			writeD(accPlData.getLegion().getLegionId());
			writeS(accPlData.getLegion().getLegionName(), 82);
		}
		else {
			writeB(new byte[86]);
		}

		writeH(accPlData.isLegionMember() ? 0x01 : 0x00);// is in legion?
		writeD((int) (pbd.getLastOnline().getTime() / 1000));// last online

		int itemsDataSize = 0;
		// TODO figure out this part when fully equipped
		List<Item> items = accPlData.getEquipment();

		for (Item item : items) {
			if (itemsDataSize >= 208) {
				break;
			}

			ItemTemplate itemTemplate = item.getItemTemplate();
			if (itemTemplate == null) {
				log.warn("Missing item. PlayerId: " + pbd.getPlayerObjId() + " ItemId: " + item.getObjectId());
				continue;
			}

			if (ItemSlot.isDisplaySlot(item.getEquipmentSlot())) {
				if (item.getEquipmentSlot() == ItemSlot.SUB_HAND.getSlotIdMask()
						|| item.getEquipmentSlot() == ItemSlot.EARRINGS_LEFT.getSlotIdMask()
						|| item.getEquipmentSlot() == ItemSlot.RING_LEFT.getSlotIdMask()) {
					writeC(0x02);
				} else {
					writeC(0x01);
				}
				writeD(item.getItemSkinTemplate().getTemplateId());
				GodStone godStone = item.getGodStone();
				writeD((godStone != null) ? godStone.getItemId() : 0);
				writeD(item.getItemColor());
				itemsDataSize += 13;
			}
		}

		byte[] stupidNc = new byte[208 - itemsDataSize];
		writeB(stupidNc);
	}
}
