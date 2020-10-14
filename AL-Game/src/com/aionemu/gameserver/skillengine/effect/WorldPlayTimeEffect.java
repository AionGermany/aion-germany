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

import javax.xml.bind.annotation.XmlAttribute;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_WORLD_PLAYTIME;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.utils.PacketSendUtility;

public class WorldPlayTimeEffect extends EffectTemplate {

	@XmlAttribute(required = true)
	protected int points;

	@Override
	public void applyEffect(Effect effect) {
		Player player;
		if (effect.getEffected() instanceof Player && (player = (Player) effect.getEffected()).getCommonData().getWorldPlayTime() < 300) {
			player.getCommonData().setWorldPlayTime(player.getCommonData().getWorldPlayTime() + points);
			PacketSendUtility.sendPacket(player, new SM_WORLD_PLAYTIME(player));
		}
	}
}
