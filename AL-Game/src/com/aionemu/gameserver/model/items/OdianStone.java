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
package com.aionemu.gameserver.model.items;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;

public class OdianStone extends ItemStone {

    @SuppressWarnings("unused")
	private final int odianSkill;
    @SuppressWarnings("unused")
	private final int odianSkillLevel;
    private final ItemTemplate odianItem;

    public OdianStone(int itemObjId, int itemId, PersistentState persistentState) {
        super(itemObjId, itemId, 0, persistentState);

        ItemTemplate itemTemplate;
        odianItem = itemTemplate = DataManager.ITEM_DATA.getItemTemplate(itemId);
        odianSkill = itemTemplate.getOdianSkillId();
        odianSkillLevel = itemTemplate.getOdianSkillLevel();
    }

    public void onEquip(Player player) {
        if (this.odianItem == null) {
            return;
        }
    }
}
