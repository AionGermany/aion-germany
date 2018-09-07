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
package com.aionemu.gameserver.model.templates.housing;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.dataholders.DataManager;
import com.mysql.jdbc.StringUtils;

/**
 * @author Rolandas
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "parts" })
@XmlRootElement(name = "building")
public class Building {

	private Parts parts;
	@XmlAttribute(name = "default")
	protected boolean isDefault;
	@XmlAttribute(name = "parts_match")
	protected String partsMatch;
	@XmlAttribute
	protected String size;
	@XmlAttribute
	protected BuildingType type;
	@XmlAttribute(required = true)
	protected int id;

	public boolean isDefault() {
		return isDefault;
	}

	@XmlTransient
	Map<PartType, Integer> partsByType = new HashMap<PartType, Integer>();

	/**
	 * @param u
	 * @param parent
	 */
	void afterUnmarshal(Unmarshaller u, Object parent) {
		if (parts == null) {
			return;
		}
		if (parts.getDoor() != 0) {
			partsByType.put(PartType.DOOR, parts.getDoor());
		}
		if (parts.getFence() != null) {
			partsByType.put(PartType.FENCE, parts.getFence());
		}
		if (parts.getFrame() != null) {
			partsByType.put(PartType.FRAME, parts.getFrame());
		}
		if (parts.getGarden() != null) {
			partsByType.put(PartType.GARDEN, parts.getGarden());
		}
		if (parts.getInfloor() != 0) {
			partsByType.put(PartType.INFLOOR_ANY, parts.getInfloor());
		}
		if (parts.getInwall() != 0) {
			partsByType.put(PartType.INWALL_ANY, parts.getInwall());
		}
		if (parts.getOutwall() != null) {
			partsByType.put(PartType.OUTWALL, parts.getOutwall());
		}
		if (parts.getRoof() != null) {
			partsByType.put(PartType.ROOF, parts.getRoof());
		}
	}

	// All methods for DataManager call are just to ensure integrity
	// if called from housing land templates, because it only has id and isDefault
	// for the buildings. Buildings template has full info though, except isDefault
	// value for the land.
	public String getPartsMatchTag() {
		if (StringUtils.isNullOrEmpty(partsMatch)) {
			return DataManager.HOUSE_BUILDING_DATA.getBuilding(id).getPartsMatchTag();
		}
		return partsMatch;
	}

	public String getSize() {
		if (StringUtils.isNullOrEmpty(size)) {
			return DataManager.HOUSE_BUILDING_DATA.getBuilding(id).getSize();
		}
		return size;
	}

	public BuildingType getType() {
		if (type == null) {
			return DataManager.HOUSE_BUILDING_DATA.getBuilding(id).getType();
		}
		return type;
	}

	public int getId() {
		return id;
	}

	public Integer getDefaultPartId(PartType partType) {
		return DataManager.HOUSE_BUILDING_DATA.getBuilding(id).partsByType.get(partType);
	}
}
