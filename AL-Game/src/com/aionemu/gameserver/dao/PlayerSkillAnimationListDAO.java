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
import com.aionemu.gameserver.model.skillanimation.SkillAnimation;
import com.aionemu.gameserver.model.skillanimation.SkillAnimationList;
import com.aionemu.gameserver.model.gameobjects.player.Player;

/**
 * @author Ghostfur (Aion-Unique)
 */
public abstract class PlayerSkillAnimationListDAO implements DAO {
	public final String getClassName() {
		return PlayerSkillAnimationListDAO.class.getName();
	}
	
	public abstract SkillAnimationList loadSkillAnimationList(int paramInt);
	
	public abstract boolean storeSkillAnimations(Player paramPlayer, SkillAnimation paramSkillAnimation);
	
	public abstract boolean removeSkillAnimation(int paramInt1, int paramInt2);
	
	public abstract boolean setActive(int paramInt1, int paramInt2);
	
	public abstract boolean setDeactive(int paramInt1, int paramInt2);
}