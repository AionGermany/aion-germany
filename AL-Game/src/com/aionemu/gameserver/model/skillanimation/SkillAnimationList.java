package com.aionemu.gameserver.model.skillanimation;

import java.util.Collection;

import javolution.util.FastMap;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.PlayerSkillAnimationListDAO;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.templates.SkillAnimationTemplate;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SKILL_ANIMATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.skillengine.model.SkillTemplate;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Ghostfur (Aion-Unique)
 */
public class SkillAnimationList {
	private final FastMap<Integer, SkillAnimation> skillanimations;
	private Player owner;

	public SkillAnimationList() {
		skillanimations = new FastMap<Integer, SkillAnimation>();
		owner = null;
	}

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public boolean contains(int skinId) {
		return skillanimations.containsKey(skinId);
	}

	public void addEntry(int skinId, int remaining, int active) {
		SkillAnimationTemplate ss = DataManager.SKILL_ANIMATION_DATA.getSkillAnimationTemplate(skinId);
		if (ss == null) {
			throw new IllegalArgumentException("Invalid skill animation id " + skinId);
		}
		skillanimations.put(skinId, new SkillAnimation(ss, skinId, remaining, active));
	}

	public boolean addSkillSkin(int skinId, int time) {
		SkillAnimationTemplate ss = DataManager.SKILL_ANIMATION_DATA.getSkillAnimationTemplate(skinId);
		if (ss == null) {
			throw new IllegalArgumentException("Invalid skin id " + skinId);
		} if (owner != null) {
			SkillAnimation entry = new SkillAnimation(ss, skinId, time, 0);
			if (!skillanimations.containsKey(skinId)) {
				skillanimations.put(skinId, entry);
				DAOManager.getDAO(PlayerSkillAnimationListDAO.class).storeSkillAnimations(owner, entry);
			} else {
				PacketSendUtility.sendPacket(owner, SM_SYSTEM_MESSAGE.STR_MSG_COSTUME_SKILL_ALREADY_HAS_COSTUME);
				return false;
			}
			PacketSendUtility.sendPacket(owner, SM_SYSTEM_MESSAGE.STR_MSG_GET_ITEM(ss.getName()));
			PacketSendUtility.sendPacket(owner, new SM_SKILL_ANIMATION(owner));
			return true;
		}
		return false;
	}

	public void removeSkillAnimation(int skinId) {
		if (!skillanimations.containsKey(skinId)) {
			return;
		}
		skillanimations.remove(skinId);
		PacketSendUtility.sendPacket(owner, new SM_SKILL_ANIMATION(owner));
		DAOManager.getDAO(PlayerSkillAnimationListDAO.class).removeSkillAnimation(owner.getObjectId(), skinId);
	}

	public void setActive(int skinId) {
		DAOManager.getDAO(PlayerSkillAnimationListDAO.class).setActive(owner.getObjectId(), skinId);
		owner.setSkillAnimationList(DAOManager.getDAO(PlayerSkillAnimationListDAO.class).loadSkillAnimationList(owner.getObjectId()));
		PacketSendUtility.sendPacket(owner, new SM_SKILL_ANIMATION(owner));
	}

	public void setDeactive(int skillId) {
		int skinIdToremove = 0;
		SkillTemplate skillGroup = DataManager.SKILL_DATA.getSkillTemplate(skillId);
		if (this.owner.getSkillAnimationList() != null) {
			for (SkillAnimation skillAnimation : owner.getSkillAnimationList().getSkillAnimation()) {
				if (skillAnimation.getTemplate() != null) {
					if (skillAnimation.getTemplate().getSkillGroup().equalsIgnoreCase(skillGroup.getSkillGroup()) && skillAnimation.getIsActive() == 1) {
						skinIdToremove = skillAnimation.getId();
						break;
					}
				}
			}
		}
		DAOManager.getDAO(PlayerSkillAnimationListDAO.class).setDeactive(owner.getObjectId(), skinIdToremove);
		owner.setSkillAnimationList(DAOManager.getDAO(PlayerSkillAnimationListDAO.class).loadSkillAnimationList(owner.getObjectId()));
		PacketSendUtility.sendPacket(owner, new SM_SKILL_ANIMATION(owner));
	}

	public int getSkinId(int SkillId) {
		int skinid = 0;
		if (SkillId == 0 || getOwner().getSkillAnimationList() == null || getOwner() == null) {
			return 0;
		}
		for (SkillAnimation skillSkin : getOwner().getSkillAnimationList().getSkillAnimation()) {
			if (DataManager.SKILL_DATA.getSkillTemplate(SkillId).getSkillGroup() != null) {
				if (skillSkin.getTemplate().getSkillGroup().equalsIgnoreCase(DataManager.SKILL_DATA.getSkillTemplate(SkillId).getSkillGroup()) && skillSkin.getIsActive() == 1) {
					skinid = skillSkin.getId();
				}
			}
		}
		return skinid;
	}

	public int size() {
		return skillanimations.size();
	}

	public Collection<SkillAnimation> getSkillAnimation() {
		return skillanimations.values();
	}
}
