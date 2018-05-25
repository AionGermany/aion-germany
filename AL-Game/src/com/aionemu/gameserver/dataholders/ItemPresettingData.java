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

import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.aionemu.gameserver.model.templates.item.ItemPreSettingTemplate;

@XmlRootElement(name = "item_presettings")
@XmlAccessorType(XmlAccessType.FIELD)
public class ItemPresettingData {

    @XmlElement(name = "item_presetting")
    private List<ItemPreSettingTemplate> itemPreSettingTemplateList;
    @XmlTransient
    private TIntObjectHashMap<ItemPreSettingTemplate> itemPreSettingTemplateTIntObjectHashMap = new TIntObjectHashMap<>();

    void afterUnmarshal(final Unmarshaller unmarshaller, final Object o) {
        for (ItemPreSettingTemplate itemPreSettingTemplate : itemPreSettingTemplateList) {
            itemPreSettingTemplateTIntObjectHashMap.put(itemPreSettingTemplate.getItem_id(), itemPreSettingTemplate);
        }
        itemPreSettingTemplateList.clear();
        itemPreSettingTemplateList = null;
    }

    public int size() {
        return itemPreSettingTemplateTIntObjectHashMap.size();
    }

    public ItemPreSettingTemplate getItemPreSettingTemplate(int itemId) {
        return itemPreSettingTemplateTIntObjectHashMap.get(itemId);
    }
}
