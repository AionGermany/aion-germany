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
package com.aionemu.gameserver.dao;

import com.aionemu.commons.database.dao.DAO;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.skinskill.SkillSkin;
import com.aionemu.gameserver.model.skinskill.SkillSkinList;

/**
 * @author Ghostfur (Aion-Unique)
 */
public abstract class PlayerSkillSkinListDAO implements DAO {

	public final String getClassName() {
		return PlayerSkillSkinListDAO.class.getName();
	}

	public abstract SkillSkinList loadSkillSkinList(int paramInt);

	public abstract boolean storeSkillSkins(Player paramPlayer, SkillSkin paramSkillSkin);

	public abstract boolean removeSkillSkin(int paramInt1, int paramInt2);

	public abstract boolean setActive(int paramInt1, int paramInt2);

	public abstract boolean setDeactive(int paramInt1, int paramInt2);
}
