package com.aionemu.gameserver.model.skinskill;

import com.aionemu.gameserver.model.IExpirable;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.SkillSkinTemplate;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Ghostfur (Aion-Unique)
 */
public class SkillSkin implements IExpirable {

	private SkillSkinTemplate template;
	private int id;
	private int dispearTime = 0;
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
		return dispearTime - (int) (System.currentTimeMillis() / 1000L);
	}

	public int getIsActive() {
		return isActive;
	}

	@Override
	public int getExpireTime() {
		return dispearTime;
	}

	@Override
	public void expireEnd(Player player) {
		player.getSkillSkinList().removeSkillSkin(id);
	}

	@Override
	public void expireMessage(Player player, int time) {
		PacketSendUtility.sendBrightYellowMessageOnCenter(player, "Skill Animation Expired"); //For testing should be removed later if all works 100%
	}

	@Override
	public boolean canExpireNow() {
		return true;
	}
}
