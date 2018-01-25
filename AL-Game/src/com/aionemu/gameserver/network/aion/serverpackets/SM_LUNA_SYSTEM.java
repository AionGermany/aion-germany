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

import java.util.HashMap;
import java.util.Map;

import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.dorinerk_wardrobe.PlayerWardrobeEntry;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.utils.PacketSendUtility;

public class SM_LUNA_SYSTEM extends AionServerPacket {

	private int actionId;
	private int unk1;
	private int slotSize;
	private int fail;
	private ItemTemplate item;
	// Karunerk's Workshop
	private int craftItemId;
	private int craftItemCount;
	// Taki's Adventure
	private int indun_id;
	// Munirunerk's Treasure
	private HashMap<Integer, Long> munirunerk_treasure;

	private int isApply;
	private int applySlot;
	private int itemId;
	private int itemSize;
	private boolean success;
	private long itemCount;

	public SM_LUNA_SYSTEM(int actionId) {
		this.actionId = actionId;
	}

	// Karunerk's Workshop
	public SM_LUNA_SYSTEM(int actionId, int craftItemId, int craftItemCount, boolean success) {
		this.actionId = actionId;
		this.craftItemId = craftItemId;
		this.craftItemCount = craftItemCount;
		this.success = success;
	}

	// Taki's Adventure
	public SM_LUNA_SYSTEM(int actionId, int indun_id) {
		this.actionId = actionId;
		this.indun_id = indun_id;
	}

	// Munirunerk's Treasure
	public SM_LUNA_SYSTEM(HashMap<Integer, Long> munirunerk_treasure) {
		this.actionId = 12;
		this.munirunerk_treasure = munirunerk_treasure;
	}

	// Dorinerk's Wardrobe
	public SM_LUNA_SYSTEM(int actionId, int isApply, int applySlot, int itemId, int unk1) {
		this.actionId = actionId;
		this.isApply = isApply;
		this.applySlot = applySlot;
		this.itemId = itemId;
		this.unk1 = unk1;
	}

	public SM_LUNA_SYSTEM(int actionId, int slotSize, int itemSize) {
		this.actionId = actionId;
		this.slotSize = slotSize;
		this.itemSize = itemSize;
	}

	public SM_LUNA_SYSTEM(int actionId, ItemTemplate item, int fail) {
		this.actionId = actionId;
		this.item = item;
		this.fail = fail;
	}
	
	public SM_LUNA_SYSTEM(int actionId, int itemId, long itemCount) {
		this.actionId = actionId;
		this.itemId = itemId;
		this.itemCount = itemCount;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		Player player = con.getActivePlayer();
		writeC(actionId);
		switch (actionId) {
			case 0:
				writeC(0);
				writeD(indun_id);
				break;
			case 2:
				writeC(fail);
				switch (fail) {
					case 0:
						PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1330049, new DescriptionId(item.getNameId())));// Success
						break;
					case 1:
						PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1330050, new DescriptionId(item.getNameId())));// Fail
						break;
				}
				break;
			case 3:
				writeC(success ? 0 : 1);// Success = 0 Fail = 1
				writeH(1);// unk 0x01
				writeD(craftItemId);// productid
				writeQ(craftItemCount);// quantity
				break;
			case 4:
				writeC(0);
				break;
			case 5:
				writeC(0);
				writeC(0);
				writeC(0);
				break;
			case 6:
				writeD(53);
				break;
			case 7:
				writeD(55);
				break;
			case 8:// dorinerk's wardrobe
				writeC(0x00);
				writeC(slotSize);
				writeH(itemSize);
				for (int i = 0; i < itemSize; i++) {
					for (PlayerWardrobeEntry ce : player.getWardrobe().getAllWardrobe()) {
						writeC(ce.getSlot());
						writeD(ce.getItemId());
						writeD(0x00);
						writeD(0x01);
					}
				}
				break;
			case 9:
				writeC(0x00);
				writeC(slotSize); // Also possible = writeH(slotSize * 256)
				break;
			case 10:
				writeC(isApply);
				writeC(applySlot);
				writeD(itemId);
				writeD(unk1);
				break;
			case 11:
				writeC(0x00);
				writeC(indun_id);
				writeD(0x01);
				break;
			case 12:// open chest
				writeC(0);// unk
				writeH(3);// size always 3
				for (Map.Entry<Integer, Long> e : munirunerk_treasure.entrySet()) {
					writeD(e.getKey());
					writeQ(e.getValue());
				}
				break;
			case 14:
				writeC(1); // free enter = 1
				writeD(indun_id);
				break;
			case 15: // TODO Golden Dice
				int dice = player.getLunaDiceGame();
				writeC(0);
				writeC(dice);
				writeC(0);
				writeC(0);
				break;
			case 16: // TODO Display Bug
				writeC(0);
				writeH(1);
				writeD(itemId); // ItemId
				writeQ(itemCount); // Item Count
				break;
		}
	}
}
