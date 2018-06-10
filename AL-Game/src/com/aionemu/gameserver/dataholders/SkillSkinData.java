/*
 * =====================================================================================*
 * This file is part of Aion-Unique (Aion-Unique Home Software Development)             *
 * Aion-Unique Development is a closed Aion Project that use Old Aion Project Base      *
 * Like Aion-Lightning, Aion-Engine, Aion-Core, Aion-Extreme, Aion-NextGen, ArchSoft,   *
 * Aion-Ger, U3J, Encom And other Aion project, All Credit Content                      *
 * That they make is belong to them/Copyright is belong to them. And All new Content    *
 * that Aion-Unique make the copyright is belong to Aion-Unique                         *
 * You may have agreement with Aion-Unique Development, before use this Engine/Source   *
 * You have agree with all of Term of Services agreement with Aion-Unique Development   *
 * =====================================================================================*
 */
package com.aionemu.gameserver.dataholders;

import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.aionemu.gameserver.model.templates.SkillSkinTemplate;

@XmlRootElement(name="skill_skins")
@XmlAccessorType(XmlAccessType.FIELD)
public class SkillSkinData {
	@XmlElement(name="skill_skin")
	private List<SkillSkinTemplate> sst;
	private TIntObjectHashMap<SkillSkinTemplate> skillskins;
  
	void afterUnmarshal(Unmarshaller u, Object parent) {
		skillskins = new TIntObjectHashMap<SkillSkinTemplate>();
		for (SkillSkinTemplate st : sst) {
			skillskins.put(st.getId(), st);
		}
		sst = null;
	}
  
	public SkillSkinTemplate getSkillSkinTemplate(int skinId) {
		return skillskins.get(skinId);
	}
  
	public int size() {
		return skillskins.size();
	}
}
