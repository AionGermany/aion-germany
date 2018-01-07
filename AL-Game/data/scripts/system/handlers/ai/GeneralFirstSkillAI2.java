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
package ai;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.skillengine.SkillEngine;

/**
 * @author Ritsu
 */
@AIName("general_first_skill")
public class GeneralFirstSkillAI2 extends GeneralNpcAI2 {

	@Override
	protected void handleBackHome() {
		super.handleBackHome();
		if (getSkillList().getUseInSpawnedSkill() != null) {
			int skillId = getSkillList().getUseInSpawnedSkill().getSkillId();
			int skillLevel = getSkillList().getSkillLevel(skillId);
			SkillEngine.getInstance().getSkill(getOwner(), skillId, skillLevel, getOwner()).useSkill();
		}
	}

	@Override
	protected void handleRespawned() {
		super.handleRespawned();
		if (getSkillList().getUseInSpawnedSkill() != null) {
			int skillId = getSkillList().getUseInSpawnedSkill().getSkillId();
			int skillLevel = getSkillList().getSkillLevel(skillId);
			SkillEngine.getInstance().getSkill(getOwner(), skillId, skillLevel, getOwner()).useSkill();
		}
	}

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		if (getSkillList().getUseInSpawnedSkill() != null) {
			int skillId = getSkillList().getUseInSpawnedSkill().getSkillId();
			int skillLevel = getSkillList().getSkillLevel(skillId);
			SkillEngine.getInstance().getSkill(getOwner(), skillId, skillLevel, getOwner()).useSkill();
		}
	}
}
