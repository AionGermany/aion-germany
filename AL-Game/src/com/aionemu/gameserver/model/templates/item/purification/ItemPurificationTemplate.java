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
package com.aionemu.gameserver.model.templates.item.purification;

import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.aionemu.gameserver.model.stats.calc.StatOwner;

/**
 * @author Ranastic
 * @rework Navyan
 */
@XmlRootElement(name = "ItemPurification")
@XmlAccessorType(XmlAccessType.FIELD)
public class ItemPurificationTemplate implements StatOwner {

	protected List<PurificationResultItem> purification_result_item;
	@XmlAttribute(name = "base_item")
	private int purification_base_item_id;

	/**
	 * @param u
	 * @param parent
	 */
	void afterUnmarshal(Unmarshaller u, Object parent) {
	}

	/**
	 * @return the purification_result_item
	 */
	public List<PurificationResultItem> getPurification_result_item() {
		return purification_result_item;
	}

	/**
	 * @return the purification_base_item_id
	 */
	public int getPurification_base_item_id() {
		return purification_base_item_id;
	}
}
