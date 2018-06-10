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
package com.aionemu.gameserver.model.skillanimation;

import com.aionemu.gameserver.model.IExpirable;
import com.aionemu.gameserver.model.templates.SkillAnimationTemplate;
import com.aionemu.gameserver.model.gameobjects.player.Player;

/**
 * @author Ghostfur (Aion-Unique)
 */
public class SkillAnimation implements IExpirable {
	private SkillAnimationTemplate template;
	private int id;
	private int dispearTime;
	private int isActive;
	
	public SkillAnimation(SkillAnimationTemplate template, int id, int dispearTime, int isActive) {
		this.template = template;
		this.id = id;
		this.dispearTime = dispearTime;
		this.isActive = isActive;
	}
	
	public SkillAnimationTemplate getTemplate() {
		return template;
	}
	
	public int getId() {
		return id;
	}
	
	public int getRemainingTime() {
		if (dispearTime == 0) {
			return 0;
		}
		return dispearTime - (int)(System.currentTimeMillis() / 1000L);
	}
	
	public int getIsActive() {
		return isActive;
	}
	
	public int getExpireTime() {
		return dispearTime;
	}
	
	public void expireEnd(Player player) {
		player.getSkillAnimationList().removeSkillAnimation(id);
	}
	
	public void expireMessage(Player player, int time) {}
	
	public boolean canExpireNow() {
		return true;
	}
}