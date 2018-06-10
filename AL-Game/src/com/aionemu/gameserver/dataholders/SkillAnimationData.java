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
package com.aionemu.gameserver.dataholders;

import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.aionemu.gameserver.model.templates.SkillAnimationTemplate;

/**
 * @author Ghostfur (Aion-Unique)
 */
@XmlRootElement(name="skill_animations")
@XmlAccessorType(XmlAccessType.FIELD)
public class SkillAnimationData {
	@XmlElement(name="skill_animation")
	private List<SkillAnimationTemplate> sst;
	private TIntObjectHashMap<SkillAnimationTemplate> skillanimations;
  
	void afterUnmarshal(Unmarshaller u, Object parent) {
		skillanimations = new TIntObjectHashMap<SkillAnimationTemplate>();
		for (SkillAnimationTemplate st : sst) {
			skillanimations.put(st.getId(), st);
		}
		sst = null;
	}
  
	public SkillAnimationTemplate getSkillAnimationTemplate(int skinId) {
		return skillanimations.get(skinId);
	}
  
	public int size() {
		return skillanimations.size();
	}
}
