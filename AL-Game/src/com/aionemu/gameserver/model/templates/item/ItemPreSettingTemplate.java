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
package com.aionemu.gameserver.model.templates.item;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by xjplay@yahoo.com on 2015/5/4.
 */
@XmlType(name="ItemPreSettingTemplate")
@XmlAccessorType(XmlAccessType.FIELD)
public class ItemPreSettingTemplate {
    @XmlAttribute(name = "item_id")
    private Integer item_id;
    @XmlAttribute(name = "name")
    private String name;
    @XmlAttribute(name = "enchant_level")
    private int enchant_level;
    @XmlElement(name = "mana_stone")
    private List<Integer> mana_stone;

    public Integer getItem_id() {
        return item_id;
    }

    public String getName() {
        return name;
    }

    public int getEnchant_level() {
        return enchant_level;
    }

    public List<Integer> getMana_stone() {
        return mana_stone;
    }
}
