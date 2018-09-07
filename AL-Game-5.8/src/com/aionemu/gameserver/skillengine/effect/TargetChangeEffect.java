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

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_TARGET_SELECTED;
import com.aionemu.gameserver.network.aion.serverpackets.SM_TARGET_UPDATE;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Kill3r
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TargetChangeEffect")
public class TargetChangeEffect extends EffectTemplate {

	@Override
	public void applyEffect(Effect effect) {
	}

	@Override
	public void startEffect(Effect effect) {
		Creature effected = effect.getEffected();
		if (effected instanceof Player) {
			if (effect.getSkillId() == 2992 || effect.getSkillId() == 3018 || effect.getSkillId() == 2511 || effect.getSkillId() == 2952 || effect.getSkillId() == 22099 || effect.getSkillId() == 22099 || effect.getSkillId() == 22100 || effect.getSkillId() == 22101 || effect.getSkillId() == 22102 || effect.getSkillId() == 22103 || effect.getSkillId() == 22104 || effect.getSkillId() == 22557 || effect.getSkillId() == 22558 || effect.getSkillId() == 22559 || effect.getSkillId() == 22560 || effect.getSkillId() == 22561 || effect.getSkillId() == 22562 || effect.getSkillId() == 22563 || effect.getSkillId() == 22564 || effect.getSkillId() == 22565 || effect.getSkillId() == 22566 || effect.getSkillId() == 22567 || effect.getSkillId() == 22568 || effect.getSkillId() == 22569 || effect.getSkillId() == 22570 || effect.getSkillId() == 22571 || effect.getSkillId() == 22572 || effect.getSkillId() == 22573 || effect.getSkillId() == 22574 || effect.getSkillId() == 22575 || effect.getSkillId() == 22576 || effect.getSkillId() == 22577 || effect.getSkillId() == 22578 || effect.getSkillId() == 22579 || effect.getSkillId() == 22580 || effect.getSkillId() == 22581 || effect.getSkillId() == 22582
				|| effect.getSkillId() == 22583 || effect.getSkillId() == 22584 || effect.getSkillId() == 22585 || effect.getSkillId() == 22586 || effect.getSkillId() == 22587 || effect.getSkillId() == 22588 || effect.getSkillId() == 22589 || effect.getSkillId() == 22590 || effect.getSkillId() == 22591 || effect.getSkillId() == 22592 || effect.getSkillId() == 22593 || effect.getSkillId() == 22594 || effect.getSkillId() == 22595 || effect.getSkillId() == 22596 || effect.getSkillId() == 22597 || effect.getSkillId() == 22598 || effect.getSkillId() == 22599 || effect.getSkillId() == 22600 || effect.getSkillId() == 22601 || effect.getSkillId() == 22666 || effect.getSkillId() == 22665) {
				int random = Rnd.get(0, 1000);
				if (random < 250) {
					if (effect.getEffector() != null) {
						effected.setTarget(effect.getEffector());
						PacketSendUtility.sendPacket((Player) effected, new SM_TARGET_SELECTED((Player) effected));
						PacketSendUtility.broadcastPacket(effected, new SM_TARGET_UPDATE((Player) effected));
					}
				}
			}
			else {
				effected.setTarget(null);
				PacketSendUtility.sendPacket((Player) effected, new SM_TARGET_SELECTED(null));
				PacketSendUtility.broadcastPacket(effected, new SM_TARGET_UPDATE((Player) effected));
			}
		}
	}
}
