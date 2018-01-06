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
package com.aionemu.gameserver.model.templates.arcadeupgrade;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Raziel
 */
@XmlType(name = "ArcadeTabItemList")
public class ArcadeTabItemList {

	@XmlAttribute(name = "item_id")
	protected int item_id;
	@XmlAttribute(name = "normalcount")
	protected int normalcount;
	@XmlAttribute(name = "frenzycount")
	protected int frenzycount;

	public final int getItemId() {
		return item_id;
	}

	public final int getNormalCount() {
		return normalcount;
	}

	public final int getFrenzyCount() {
		return frenzycount;
	}
}
