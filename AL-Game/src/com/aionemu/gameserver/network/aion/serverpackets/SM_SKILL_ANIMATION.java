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
package com.aionemu.gameserver.network.aion.serverpackets;

import java.util.HashMap;
import java.util.Map.Entry;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.skill.PlayerSkillEntry;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author FrozenKiller
 */

public class SM_SKILL_ANIMATION extends AionServerPacket {

	private int action;
	private int skillAnimation;
	private static HashMap<Integer, Integer> skillAnimations = new HashMap<Integer, Integer>();

	public SM_SKILL_ANIMATION(int action) {
		this.action = action;
	}

	public SM_SKILL_ANIMATION(int action, int skillAnimation) {
		this.action = action;
		this.skillAnimation = skillAnimation;
	}

	@Override
	protected void writeImpl(AionConnection con) { // TODO
		final Player player = con.getActivePlayer();
		switch (action) {
			case 0:// Learn
				writeC(0);
				writeH(1);
				writeH(skillAnimation);
				writeD(0);
				writeC(0);
				break;
			case 1:// Initial packet ?
				writeC(1);
				writeH(0);
				break;
			case 2: // Load from DB
				PlayerSkillEntry[] skills = player.getSkillList().getAllSkills();
				for (PlayerSkillEntry entry : skills) {
					if (entry.getSkillAnimation() != 0) {
						skillAnimations.put(entry.getSkillId(), entry.getSkillAnimation());
					}
					else {
						continue;
					}
				}
				writeC(1);
				if (skillAnimations.size() != 0) {
					writeH(skillAnimations.size());// Size
					for (Entry<Integer, Integer> entry : skillAnimations.entrySet()) {
						int skillId = entry.getKey();
						int animation = entry.getValue();
						writeH(animation);
						writeD(skillId);
						writeC(player.getSkillList().getSkillEntry(skillId).getSkillAnimationEnabled());// Default Animation (0 = True, 1 = False)
					}
					break;
				}
				else {
					// System.out.println("NO ANIMATION SKILLS");
					writeH(0);
					writeH(0);
					writeD(0);
					writeC(0);
					break;
				}
		}
		skillAnimations.clear();
	}
}
