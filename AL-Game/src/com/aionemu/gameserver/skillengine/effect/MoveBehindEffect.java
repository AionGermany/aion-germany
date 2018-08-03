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
package com.aionemu.gameserver.skillengine.effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.geoEngine.collision.CollisionIntention;
import com.aionemu.gameserver.geoEngine.math.Vector3f;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_TARGET_UPDATE;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.skillengine.model.DashStatus;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.skillengine.model.Skill;
import com.aionemu.gameserver.skillengine.model.SkillMoveType;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.geo.GeoService;

/**
 * @author Sarynth, modified Bobobear
 * @author FrozenKiller
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MoveBehindEffect")
public class MoveBehindEffect extends EffectTemplate {

	@Override
	public void applyEffect(Effect effect) {
		Player effector = (Player) effect.getEffector();
		// Deselect targets
		PacketSendUtility.sendPacket(effector, new SM_TARGET_UPDATE(effector));
		// Move Effector to Effected
		Skill skill = effect.getSkill();
		World.getInstance().updatePosition(effector, skill.getX(), skill.getY(), skill.getZ(), skill.getH());
	}

	@Override
	public void calculate(Effect effect) {
		final Player effector = (Player) effect.getEffector();
		final Creature effected = effect.getEffected();
		final ItemTemplate itemTemplate = effect.getItemTemplate();
		
		effect.addSucessEffect(this);
		effect.setSkillMoveType(SkillMoveType.MOVEBEHIND);
		effect.setDashStatus(DashStatus.MOVEBEHIND);

		double radian = Math.toRadians(MathUtil.convertHeadingToDegree(effected.getHeading()));
		float x1 = (float) (Math.cos(Math.PI + radian) * 1.3F);
		float y1 = (float) (Math.sin(Math.PI + radian) * 1.3F);
		byte intentions = (byte) (CollisionIntention.PHYSICAL.getId() | CollisionIntention.DOOR.getId());
		Vector3f closestCollision = GeoService.getInstance().getClosestCollision(effector, effected.getX() + x1, effected.getY() + y1, effected.getZ(), false, intentions);

		// stop moving
		effected.getMoveController().abortMove();
		
		if (itemTemplate != null) {
			SkillEngine.getInstance().getSkill(effector, effect.getSkillId(), 1, effected).useNoAnimationSkill();
		}
		
		// set new world position for effector
		effect.getSkill().setTargetPosition(closestCollision.getX(), closestCollision.getY(), closestCollision.getZ(), effected.getHeading());
	}
}
