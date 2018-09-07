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

import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.model.templates.npcskill.NpcSkillTemplates;

import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * @author ATracer
 */
@XmlRootElement(name = "npc_skill_templates")
@XmlAccessorType(XmlAccessType.FIELD)
public class NpcSkillData {

	@XmlElement(name = "npcskills")
	private List<NpcSkillTemplates> npcSkills;
	/**
	 * A map containing all npc skill templates
	 */
	private TIntObjectHashMap<NpcSkillTemplates> npcSkillData = new TIntObjectHashMap<NpcSkillTemplates>();

	void afterUnmarshal(Unmarshaller u, Object parent) {
		for (NpcSkillTemplates npcSkill : npcSkills) {
			npcSkillData.put(npcSkill.getNpcId(), npcSkill);

			if (npcSkill.getNpcSkills() == null) {
				LoggerFactory.getLogger(NpcSkillData.class).error("NO SKILL");
			}
		}

	}

	public int size() {
		return npcSkillData.size();
	}

	public NpcSkillTemplates getNpcSkillList(int id) {
		return npcSkillData.get(id);
	}
}
