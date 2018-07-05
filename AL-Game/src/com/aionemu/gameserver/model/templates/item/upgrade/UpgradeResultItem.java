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
package com.aionemu.gameserver.model.templates.item.upgrade;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Ranastic
 * @rework Navyan
 */
@XmlRootElement(name = "UpgradeResultItem")
@XmlAccessorType(XmlAccessType.FIELD)
public class UpgradeResultItem {

	@XmlAttribute(name = "item_id")
	private int item_id;
	@XmlAttribute(name = "name")
	private String name;
	@XmlAttribute(name = "check_enchant_count")
	private int check_enchant_count;

	private RequiredMaterials required_materials;
	private NeedAbyssPoint abyss_point_needed;
	private NeedKinah kinah_needed;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the check_enchant_count
	 */
	public int getCheck_enchant_count() {
		return check_enchant_count;
	}

	/**
	 * @return the item_id
	 */
	public int getItem_id() {
		return item_id;
	}

	/**
	 * @return the required_materials
	 */
	public RequiredMaterials getUpgrade_materials() {
		return required_materials;
	}

	/**
	 * @return the abyss_point_needed
	 */
	public NeedAbyssPoint getNeed_abyss_point() {
		return abyss_point_needed;
	}

	/**
	 * @return the kinah_needed
	 */
	public NeedKinah getNeed_kinah() {
		return kinah_needed;
	}
}
