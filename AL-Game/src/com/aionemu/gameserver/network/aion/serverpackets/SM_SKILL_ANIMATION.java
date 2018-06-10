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

import com.aionemu.gameserver.model.skinskill.SkillSkin;
import com.aionemu.gameserver.model.skinskill.SkillSkinList;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author Ghostfur (Aion-Unique)
 */
public class SM_SKILL_ANIMATION extends AionServerPacket {
	private SkillSkinList skillSkinList;
	private int action;
	@SuppressWarnings("unused")
	private int titleId;
	@SuppressWarnings("unused")
	private int bonusTitleId;
	@SuppressWarnings("unused")
	private int playerObjId;

	public SM_SKILL_ANIMATION(Player player) {
		action = 1;
		skillSkinList = player.getSkillSkinList();
	}

	protected void writeImpl(AionConnection con) {
		writeC(action);
		switch (action) {
			case 0:
				writeH(skillSkinList.size());
				for (SkillSkin skillSkin : skillSkinList.getSkillSkins()) {
					writeH(skillSkin.getId());
					writeD(skillSkin.getExpireTime());
					writeC(skillSkin.getIsActive());
				}
				break;
			case 1:
				if (skillSkinList != null) {
					writeH(skillSkinList.size());
					for (SkillSkin skillSkin : skillSkinList.getSkillSkins()) {
						writeH(skillSkin.getId());
						writeD(skillSkin.getExpireTime());
						writeC(skillSkin.getIsActive());
					}
				}
				break;
		}
	}
}
