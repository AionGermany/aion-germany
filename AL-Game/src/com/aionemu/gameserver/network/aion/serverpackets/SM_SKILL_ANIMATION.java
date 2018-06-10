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

import com.aionemu.gameserver.model.skillanimation.SkillAnimation;
import com.aionemu.gameserver.model.skillanimation.SkillAnimationList;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author Ghostfur (Aion-Unique)
 */
public class SM_SKILL_ANIMATION extends AionServerPacket {
	private SkillAnimationList skillAnimationList;
	private int action;
	private int titleId;
	private int bonusTitleId;
	private int playerObjId;

	public SM_SKILL_ANIMATION(Player player) {
		action = 1;
		skillAnimationList = player.getSkillAnimationList();
	}

	protected void writeImpl(AionConnection con) {
		writeC(action);
		switch (action) {
			case 0:
				writeH(skillAnimationList.size());
				for (SkillAnimation skillanimation : skillAnimationList.getSkillAnimation()) {
					writeH(skillanimation.getId());
					writeD(skillanimation.getExpireTime());
					writeC(skillanimation.getIsActive());
				}
				break;
			case 1:
				if (skillAnimationList != null) {
					writeH(skillAnimationList.size());
					for (SkillAnimation skillAnimation : skillAnimationList.getSkillAnimation()) {
						writeH(skillAnimation.getId());
						writeD(skillAnimation.getExpireTime());
						writeC(skillAnimation.getIsActive());
					}
				}
				break;
		}
	}
}
