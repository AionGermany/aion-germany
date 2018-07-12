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
package com.aionemu.gameserver.dataholders;

import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.aionemu.gameserver.model.templates.item.ItemCustomSetTemplate;

import gnu.trove.map.hash.TIntObjectHashMap;

@XmlRootElement(name = "item_custom_sets")
@XmlAccessorType(XmlAccessType.FIELD)
public class ItemCustomSetData {

    @XmlElement(name = "item_custom_set")
    private List<ItemCustomSetTemplate> itemCustomSetTemplateList;
    @XmlTransient
    private TIntObjectHashMap<ItemCustomSetTemplate> itemCustomSetTemplateTIntObjectHashMap = new TIntObjectHashMap<>();

    void afterUnmarshal(final Unmarshaller unmarshaller, final Object o) {
        for (ItemCustomSetTemplate itemCustomSetTemplate : itemCustomSetTemplateList) {
            itemCustomSetTemplateTIntObjectHashMap.put(itemCustomSetTemplate.getItem_id(), itemCustomSetTemplate);
        }
        itemCustomSetTemplateList.clear();
        itemCustomSetTemplateList = null;
    }

    public int size() {
        return itemCustomSetTemplateTIntObjectHashMap.size();
    }

    public ItemCustomSetTemplate getItemCustomSetTemplate(int itemId) {
        return itemCustomSetTemplateTIntObjectHashMap.get(itemId);
    }
}
