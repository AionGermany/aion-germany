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

import java.util.List;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.templates.decomposable.SelectItem;
import com.aionemu.gameserver.model.templates.decomposable.SelectItems;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author Alcapwnd
 */
public class SM_SELECT_ITEM_LIST extends AionServerPacket {

	private int uniqueItemId;
	private List<SelectItem> selsetitems;

	public SM_SELECT_ITEM_LIST(SelectItems selsetitem, int uniqueItemId) {
		this.uniqueItemId = uniqueItemId;
		this.selsetitems = selsetitem.getItems();
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeD(this.uniqueItemId);
		writeD(0);
		writeC(this.selsetitems.size());
		for (int i = 0; i < this.selsetitems.size(); i++) {
			writeC(i);
			SelectItem rt = this.selsetitems.get(i);
			ItemTemplate itemTemplate = DataManager.ITEM_DATA.getItemTemplate(rt.getSelectItemId());
			writeD(rt.getSelectItemId());
			writeD(rt.getCount());
			writeC(itemTemplate.getOptionSlotBonus() > 0 ? 255 : 0);
			writeC(itemTemplate.getMaxEnchantBonus() > 0 ? 255 : 0);
			if ((itemTemplate.isArmor()) || (itemTemplate.isWeapon())) {
				writeC(-1);
			}
			else {
				writeC(0);
			}
			if ((itemTemplate.isCloth()) || (itemTemplate.getOptionSlotBonus() > 0) || (itemTemplate.getMaxEnchantBonus() > 0)) {
				writeC(1);
			}
			else {
				writeC(0);
			}
		}
	}
}
