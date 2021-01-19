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
package com.aionemu.gameserver.model.templates.transform_book;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "transform_collection_template")
@XmlAccessorType(XmlAccessType.NONE)
public class TransformCollectionTemplate {

	@XmlElement(name = "physical_bonus")
	protected CollectionAttr physicalAttr;
	@XmlElement(name = "magical_bonus")
	protected CollectionAttr magicalAttr;
	@XmlElement(name = "required")
	protected CollectionRequired required;
	@XmlAttribute(name = "id", required = true)
	private int id;
	@XmlAttribute(name = "need_count")
	private int needCount;
	@XmlAttribute(name = "reward_skill")
	private int rewarSkill;

	public int getId() {
		return id;
	}

	public CollectionAttr getMagicalAttr() {
		return magicalAttr;
	}

	public CollectionAttr getPhysicalAttr() {
		return physicalAttr;
	}

	public CollectionRequired getRequired() {
		return required;
	}

	public int getNeedCount() {
		return needCount;
	}

	public int getRewarSkill() {
		return rewarSkill;
	}
}
