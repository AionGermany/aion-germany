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
package com.aionemu.gameserver.model.templates.gather;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.aionemu.gameserver.model.templates.VisibleObjectTemplate;

/**
 * @author ATracer, KID
 */
@XmlRootElement(name = "gatherable_template")
@XmlAccessorType(XmlAccessType.FIELD)
public class GatherableTemplate extends VisibleObjectTemplate {

	@XmlElement(required = true)
	protected Materials materials;
	@XmlElement(required = true)
	protected ExMaterials exmaterials;
	@XmlAttribute
	protected int id;
	@XmlAttribute
	protected String name;
	@XmlAttribute
	protected int nameId;
	@XmlAttribute
	protected String sourceType;
	@XmlAttribute
	protected int harvestCount;
	@XmlAttribute
	protected int skillLevel;
	@XmlAttribute
	protected int harvestSkill;
	@XmlAttribute
	protected int successAdj;
	@XmlAttribute
	protected int failureAdj;
	@XmlAttribute
	protected int aerialAdj;
	@XmlAttribute
	protected int captcha;
	@XmlAttribute
	protected int lvlLimit;
	@XmlAttribute
	protected int reqItem;
	@XmlAttribute
	protected int reqItemNameId;
	@XmlAttribute
	protected int checkType;
	@XmlAttribute
	protected int eraseValue;

	/**
	 * Gets the value of the materials property.
	 *
	 * @return possible object is {@link Materials }
	 */
	public Materials getMaterials() {
		return materials;
	}

	public ExMaterials getExtraMaterials() {
		return exmaterials;
	}

	/**
	 * Gets the value of the id property.
	 */
	@Override
	public int getTemplateId() {
		return id;
	}

	/**
	 * Gets the value of the aerialAdj property.
	 *
	 * @return possible object is {@link Integer }
	 */
	public int getAerialAdj() {
		return aerialAdj;
	}

	/**
	 * Gets the value of the failureAdj property.
	 *
	 * @return possible object is {@link Integer }
	 */
	public int getFailureAdj() {
		return failureAdj;
	}

	/**
	 * Gets the value of the successAdj property.
	 *
	 * @return possible object is {@link Integer }
	 */
	public int getSuccessAdj() {
		return successAdj;
	}

	/**
	 * Gets the value of the harvestSkill property.
	 *
	 * @return possible object is {@link Integer }
	 */
	public int getHarvestSkill() {
		return harvestSkill;
	}

	/**
	 * Gets the value of the skillLevel property.
	 *
	 * @return possible object is {@link Integer }
	 */
	public int getSkillLevel() {
		return skillLevel;
	}

	/**
	 * Gets the value of the harvestCount property.
	 *
	 * @return possible object is {@link Integer }
	 */
	public int getHarvestCount() {
		return harvestCount;
	}

	/**
	 * Gets the value of the sourceType property.
	 *
	 * @return possible object is {@link String }
	 */
	public String getSourceType() {
		return sourceType;
	}

	/**
	 * Gets the value of the name property.
	 *
	 * @return possible object is {@link String }
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * @return the nameId
	 */
	@Override
	public int getNameId() {
		return nameId;
	}

	public int getCaptchaRate() {
		return captcha;
	}

	public int getLevelLimit() {
		return lvlLimit;
	}

	public int getRequiredItemId() {
		return reqItem;
	}

	public int getRequiredItemNameId() {
		return reqItemNameId * 2 + 1;
	}

	public int getCheckType() {
		return checkType;
	}

	public int getEraseValue() {
		return eraseValue;
	}
}
