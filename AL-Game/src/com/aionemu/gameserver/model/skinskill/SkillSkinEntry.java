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
package com.aionemu.gameserver.model.skinskill;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.templates.SkillSkinTemplate;

/**
 * @author Ghostfur (Aion-Unique)
 */
public abstract class SkillSkinEntry {

	protected final int skinId;
	protected int skillLevel;

	SkillSkinEntry(int skinId, int skillLevel) {
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
		return DataManager.SKILL_SKIN_DATA.getSkillSkinTemplate(getSkinId()).getName();
	}

	public void setSkillLvl(int skillLevel) {
		this.skillLevel = skillLevel;
	}

	public final SkillSkinTemplate getSkillSkinTemplate() {
		return DataManager.SKILL_SKIN_DATA.getSkillSkinTemplate(getSkinId());
	}
}
