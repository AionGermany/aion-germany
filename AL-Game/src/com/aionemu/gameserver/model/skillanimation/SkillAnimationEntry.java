package com.aionemu.gameserver.model.skillanimation;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.templates.SkillAnimationTemplate;

/**
 * @author Ghostfur (Aion-Unique)
 */
public abstract class SkillAnimationEntry {
	protected final int skinId;
	protected int skillLevel;
	
	SkillAnimationEntry(int skinId, int skillLevel) {
		this.skinId = skinId;
		this.skillLevel = skillLevel;
	}
	
	public final int getSkinId() {
		return skinId;
	}
	
	public final int getSkillLevel() {
		return skillLevel;
	}
	
	public final String getSkillName() {
		return DataManager.SKILL_ANIMATION_DATA.getSkillAnimationTemplate(getSkinId()).getName();
	}
	
	public void setSkillLvl(int skillLevel) {
		this.skillLevel = skillLevel;
	}
	
	public final SkillAnimationTemplate getSkillAnimationTemplate() {
		return DataManager.SKILL_ANIMATION_DATA.getSkillAnimationTemplate(getSkinId());
	}
}
