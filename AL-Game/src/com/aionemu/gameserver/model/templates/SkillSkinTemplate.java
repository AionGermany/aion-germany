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
package com.aionemu.gameserver.model.templates;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Ghostfur (Aion-Unique)
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="skill_skin")
public class SkillSkinTemplate {

	@XmlAttribute(name="id", required=true)
	private int id;
	@XmlAttribute(name="name", required=true)
	private String name;
	@XmlAttribute(name="skill_group", required=true)
	private String skillgroup;
	@XmlAttribute(name="motion_name", required=true)
	private String motionName;
	@XmlAttribute(name="ammo_speed", required=true)
	private int ammoSpeed;
	  
	public int getId() {
		return id;
	}
	  
	public String getName() {
		return name;
	}
	  
	public String getSkillGroup() {
		return skillgroup;
	}
	  
	public String getMotionName() {
		return motionName;
	}
	  
	public int getAmmoSpeed() {
		return ammoSpeed;
	}
}