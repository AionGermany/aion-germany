/**
 * This file is part of Aion Eternity Core <Ver:4.9>.
 *
 * Aion Eternity Core is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * Aion Eternity Core is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Aion Eternity Core. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.aionemu.gameserver.dataholders;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.skill.PlayerSkillEntry;
import com.aionemu.gameserver.model.templates.HiddenStigmasTemplate;

/**
 * @author Cain
 */
@XmlRootElement(name = "hidden_stigma_tree")
@XmlAccessorType(XmlAccessType.FIELD)
public class HiddenStigmaTreeData {

	@XmlElement(name = "playerclass", type = HiddenStigmasTemplate.class)
	private List<HiddenStigmasTemplate> hiddenStigmasByClass;

	public int size() {
		return hiddenStigmasByClass.size();
	}

	public List<HiddenStigmasTemplate> getHiddenStigmasByClass() {
		return hiddenStigmasByClass;
	}

	public int getHiddenStigmaSkill(Player player) {
		for (HiddenStigmasTemplate hst : hiddenStigmasByClass) {
			if (hst.getClassname().equals(player.getPlayerClass().name())) {
				int maxAvailHiddenStigmaLvl = player.getSkillList().getMaxAvailHiddenStigmaLvl();
				int regularHiddenStigmaSkillId = hst.getRegularStigmaSkillId(player.getRace(), maxAvailHiddenStigmaLvl);
				for (HiddenStigmasTemplate.HiddenStigmaTemplate oneStigma : hst.getHiddenStigmas()) {
					boolean haveRequiredSkill = false;
					int customVariants = 0;
					for (PlayerSkillEntry pse : player.getSkillList().getStigmaSkills()) {
						if (oneStigma.getRequiredId() == null)
							continue;

						if (oneStigma.getRequiredId().equals(pse.getSkillTemplate().getStack()))
							haveRequiredSkill = true;

						for (String customStack : oneStigma.getCustomStacks()) {
							if (customStack.equals(pse.getSkillTemplate().getStack()))
								customVariants++;
						}
						if (haveRequiredSkill && customVariants > 1) {
							return DataManager.SKILL_TREE_DATA.getStigmaTree().get(player.getRace()).get(oneStigma.getId()).get(maxAvailHiddenStigmaLvl);
						}
					}

				}
				return regularHiddenStigmaSkillId;
			}
		}
		return 0;
	}
}
