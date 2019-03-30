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
package com.aionemu.packetsamurai.utils.collector.data.npcTemplates;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlType(name= "Race")
@XmlEnum
public enum Race {

    ELYOS,
    ASMODIANS,
    LYCAN,
    CONSTRUCT,
    CARRIER,
    DRAKAN,
    LIZARDMAN,
    TELEPORTER,
    NAGA,
    BROWNIE,
    KRALL,
    SHULACK,
    BARRIER,
    PC_LIGHT_CASTLE_DOOR,
    PC_DARK_CASTLE_DOOR,
    DRAGON_CASTLE_DOOR,
    GCHIEF_LIGHT,
    GCHIEF_DARK,
    DRAGON,
    OUTSIDER,
    RATMAN,
    DEMIHUMANOID,
    UNDEAD,
    BEAST,
    MAGICALMONSTER,
    ELEMENTAL,
    LIVINGWATER,
    NONE,
    PC_ALL,
    DEFORM,
    NEUT,
    GHENCHMAN_LIGHT,
    GHENCHMAN_DARK,
    EVENT_TOWER_DARK,
    EVENT_TOWER_LIGHT,
    GOBLIN,
    TRICODARK,
    NPC,
    LIGHT,
    DARK,
    WORLD_EVENT_DEFTOWER,
    ORC,
    DRAGONET,
    SIEGEDRAKAN,
    GCHIEF_DRAGON,
	WORLD_EVENT_BONFIRE,
	BATTLEGROUND_LI,
	BATTLEGROUND_DA,
	TYPE_A,
	TYPE_B,
	TYPE_C,
	TYPE_D,
	GINSENGS,
	EVENT_YEAR;

    public String value() {
        return name();
    }
  
    public static Race fromValue(String v) {
        return valueOf(v);
    }
}
