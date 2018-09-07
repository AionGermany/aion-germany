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

package com.aionemu.gameserver.model.npcdrops;

import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Falke_34
 */
@XmlRootElement(name = "npc_drop")
@XmlAccessorType(XmlAccessType.NONE)
public class XmlNpcDrops {

	@XmlElement(name = "drop_group")
	protected List<XmlDropGroup> dropGroup;
	@XmlAttribute(name = "npc_id", required = true)
	protected int npcId;

	public List<XmlDropGroup> getDropGroup() {
		if (this.dropGroup == null) {
			return Collections.emptyList();
		}
		return this.dropGroup;
	}

	public int getNpcId() {
		return this.npcId;
	}
}
