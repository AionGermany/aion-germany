package com.aionemu.gameserver.model.skinskill;

import com.aionemu.gameserver.model.IExpirable;
import com.aionemu.gameserver.model.templates.SkillSkinTemplate;
import com.aionemu.gameserver.model.gameobjects.player.Player;

/**
 * @author Ghostfur (Aion-Unique)
 */
public class SkillSkin implements IExpirable {
	private SkillSkinTemplate template;
	private int id;
	private int dispearTime;
	private int isActive;
	
	public SkillSkin(SkillSkinTemplate template, int id, int dispearTime, int isActive) {
		this.template = template;
		this.id = id;
		this.dispearTime = dispearTime;
		this.isActive = isActive;
	}
	
	public SkillSkinTemplate getTemplate() {
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
		player.getSkillSkinList().removeSkillSkin(id);
	}
	
	public void expireMessage(Player player, int time) {}
	
	public boolean canExpireNow() {
		return true;
	}
}