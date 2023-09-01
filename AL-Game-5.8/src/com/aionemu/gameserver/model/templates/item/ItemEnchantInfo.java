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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EnchantInfo")
public class ItemEnchantInfo {

	@XmlAttribute(name = "rnd_enchant")
	private int rnd_enchant;
	@XmlAttribute(name = "wake_level")
	private int wake_level;
	@XmlAttribute(name = "waken_id")
	private int waken_id;

	public int getAwakeLevel() {
		return this.wake_level;
	}

	public int getAwakenId() {
		return this.waken_id;
	}

	public int getRndEnchantLevel() {
		return this.rnd_enchant;
	}
}
