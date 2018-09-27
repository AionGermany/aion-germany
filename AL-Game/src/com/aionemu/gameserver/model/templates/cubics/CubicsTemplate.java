/**
 * This file is part of Aion-Lightning <aion-lightning.org>.
 *
 * Aion-Lightning is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * Aion-Lightning is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. * You should have received a copy of the GNU General Public License
 * along with Aion-Lightning. If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.model.templates.cubics;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Phantom_KNA
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CubicsTemplate")
public class CubicsTemplate {

	protected List<StatCoreList> core_list;
	@XmlAttribute(name = "id", required = true)
	private int id;
	@XmlAttribute(name = "name")
	private String name;
	@XmlAttribute(name = "category")
	private int category;
	@XmlAttribute(name = "maxRank")
	private int maxRank;
	@XmlAttribute(name = "quality")
	private QualityCoreType quality;
	@XmlAttribute(name = "itemId")
	private int itemId;

	public CubicsTemplate() {
		this.name = "";
	}

	public List<StatCoreList> getStatLists() {
		if (this.core_list == null) {
			this.core_list = new ArrayList<StatCoreList>();
		}
		return this.core_list;
	}

	public int getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public int getCategory() {
		return this.category;
	}

	public int getMaxRank() {
		return this.maxRank;
	}

	public QualityCoreType getQuality() {
		return this.quality;
	}

	public int getItemIdCubic() {
		return this.itemId;
	}

}