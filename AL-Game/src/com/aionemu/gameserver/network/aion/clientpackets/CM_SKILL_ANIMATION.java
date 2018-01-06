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
package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;

/**
 * @author FrozenKiller
 */
public class CM_SKILL_ANIMATION extends AionClientPacket {

	private int skillId;
	private int skillAnimationId;
	private int fixedSkillId = 0;

	public CM_SKILL_ANIMATION(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	@Override
	protected void readImpl() {
		skillId = readH();
		// System.out.println("SkillId: " + skillId);
		skillAnimationId = readH();
		// System.out.println("AnimationsId: " + skillAnimationId);
		if (skillId == 2607) {
			fixedSkillId = 2606;
		}
		else if (skillId == 2276) {
			fixedSkillId = 2274;
		}
	}

	@Override
	protected void runImpl() {
		final Player player = getConnection().getActivePlayer();

		if (fixedSkillId != 0) {
			if (player.getSkillList().getSkillEntry(fixedSkillId).getSkillAnimation() == 0) {
				player.getSkillList().getSkillEntry(fixedSkillId).setSkillAnimationEnabled(1);
				player.getSkillList().getSkillEntry(fixedSkillId).setSkillAnimation(skillAnimationId);
			}
			else if (player.getSkillList().getSkillEntry(fixedSkillId).getSkillAnimation() > 0) {
				if (skillAnimationId > 0) {
					player.getSkillList().getSkillEntry(fixedSkillId).setSkillAnimationEnabled(1);
				}
				else {
					player.getSkillList().getSkillEntry(fixedSkillId).setSkillAnimationEnabled(0);
				}
			}
		}
		else {
			if (player.getSkillList().getSkillEntry(skillId).getSkillAnimation() == 0) {
				player.getSkillList().getSkillEntry(skillId).setSkillAnimationEnabled(1);
				player.getSkillList().getSkillEntry(skillId).setSkillAnimation(skillAnimationId);
			}
			else if (player.getSkillList().getSkillEntry(skillId).getSkillAnimation() > 0) {
				if (skillAnimationId > 0) {
					player.getSkillList().getSkillEntry(skillId).setSkillAnimationEnabled(1);
				}
				else {
					player.getSkillList().getSkillEntry(skillId).setSkillAnimationEnabled(0);
				}
			}
		}
		fixedSkillId = 0;
	}
}
