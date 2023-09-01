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
package com.aionemu.gameserver.skillengine.properties;

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.Summon;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.skillengine.model.DispelCategoryType;
import com.aionemu.gameserver.skillengine.model.Skill;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author ATracer
 */
public class FirstTargetProperty {

	/**
	 * @param skill
	 * @param properties
	 * @return
	 */
	public static final boolean set(Skill skill, Properties properties) {

		FirstTargetAttribute value = properties.getFirstTarget();
		skill.setFirstTargetAttribute(value);
		switch (value) {
			case ME:
				skill.setFirstTargetRangeCheck(false);
				skill.setFirstTarget(skill.getEffector());
				break;
			case TARGETORME:
				boolean changeTargetToMe = false;
				if (skill.getFirstTarget() == null) {
					skill.setFirstTarget(skill.getEffector());
				}
				else if (skill.getFirstTarget().isAttackableNpc()) {
					Player playerEffector = (Player) skill.getEffector();
					if (skill.getFirstTarget().isEnemy(playerEffector)) {
						changeTargetToMe = true;
					}
				}
				else if ((skill.getFirstTarget() instanceof Player) && (skill.getEffector() instanceof Player)) {
					Player playerEffected = (Player) skill.getFirstTarget();
					Player playerEffector = (Player) skill.getEffector();
					if (!playerEffected.getRace().equals(playerEffector.getRace()) || playerEffected.isEnemy(playerEffector)) {
						changeTargetToMe = true;
					}
				}
				else if (skill.getFirstTarget() instanceof Npc) {
					Npc npcEffected = (Npc) skill.getFirstTarget();
					Player playerEffector = (Player) skill.getEffector();
					if (npcEffected.isEnemy(playerEffector)) {
						changeTargetToMe = true;
					}
				}
				else if ((skill.getFirstTarget() instanceof Summon) && (skill.getEffector() instanceof Player)) {
					Summon summon = (Summon) skill.getFirstTarget();
					Player playerEffected = summon.getMaster();
					Player playerEffector = (Player) skill.getEffector();
					if (playerEffected.isEnemy(playerEffector)) {
						changeTargetToMe = true;
					}
				}
				if (changeTargetToMe) {
					if (skill.getEffector() instanceof Player) {
						PacketSendUtility.sendPacket((Player) skill.getEffector(), SM_SYSTEM_MESSAGE.STR_SKILL_AUTO_CHANGE_TARGET_TO_MY);
					}
					skill.setFirstTarget(skill.getEffector());
				}
				break;
			case TARGET:
				if ((skill.getSkillId() <= 8217) || (skill.getSkillId() >= 9180)) { // 5.1
					if ((skill.getSkillTemplate().getDispelCategory() != DispelCategoryType.NPC_BUFF) && (skill.getSkillTemplate().getDispelCategory() != DispelCategoryType.NPC_DEBUFF_PHYSICAL)) {
						if (((skill.getFirstTarget() == null) || (skill.getFirstTarget().equals(skill.getEffector()))) && ((skill.getEffector() instanceof Player))) {
							if (skill.getSkillTemplate().getProperties().getTargetType() == TargetRangeAttribute.AREA) {
								return skill.getFirstTarget() != null;
							}
							TargetRelationAttribute relation = skill.getSkillTemplate().getProperties().getTargetRelation();
							if (relation != TargetRelationAttribute.ALL) {
								PacketSendUtility.sendPacket((Player) skill.getEffector(), SM_SYSTEM_MESSAGE.STR_SKILL_TARGET_IS_NOT_VALID);
								return false;
							}
						}
					}
				}
				break;
			case MYPET:
				Creature effector = skill.getEffector();
				if (effector instanceof Player) {
					Summon summon = ((Player) effector).getSummon();
					if (summon != null) {
						skill.setFirstTarget(summon);
					}
					else {
						return false;
					}
				}
				else {
					return false;
				}
				break;
			case MYMASTER:
				Creature peteffector = skill.getEffector();
				if (peteffector instanceof Summon) {
					Player player = ((Summon) peteffector).getMaster();
					if (player != null) {
						skill.setFirstTarget(player);
					}
					else {
						return false;
					}
				}
				else {
					return false;
				}
				break;
			case PASSIVE:
				skill.setFirstTarget(skill.getEffector());
				break;
			case TARGET_MYPARTY_NONVISIBLE:
				Creature effected = skill.getFirstTarget();
				if (effected == null || skill.getEffector() == null) {
					return false;
				}
				if (!(effected instanceof Player) || !(skill.getEffector() instanceof Player) || !((Player) skill.getEffector()).isInGroup2()) {
					return false;
				}
				boolean myParty = false;
				for (Player member : ((Player) skill.getEffector()).getPlayerGroup2().getMembers()) {
					if (member == skill.getEffector()) {
						continue;
					}
					if (member == effected) {
						myParty = true;
						break;
					}
				}
				if (!myParty) {
					return false;
				}

				skill.setFirstTargetRangeCheck(false);
				break;
			case POINT:
				skill.setFirstTarget(skill.getEffector());
				skill.setFirstTargetRangeCheck(false);
				return true;
			default:
				break;
		}

		if (skill.getFirstTarget() != null) {
			skill.getEffectedList().add(skill.getFirstTarget());
		}
		return true;
	}
}
