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
import com.aionemu.gameserver.model.account.CharacterBanInfo;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.PunishmentService.PunishmentType;

/**
 * @author lord_rex
 */
public abstract class PlayerPunishmentsDAO implements DAO {

	@Override
	public final String getClassName() {
		return PlayerPunishmentsDAO.class.getName();
	}

	public abstract void loadPlayerPunishments(final Player player, final PunishmentType punishmentType);

	public abstract void storePlayerPunishments(final Player player, final PunishmentType punishmentType);

	public abstract void punishPlayer(final int playerId, final PunishmentType punishmentType, final long expireTime, final String reason);

	public abstract void punishPlayer(final Player player, final PunishmentType punishmentType, final String reason);

	public abstract void unpunishPlayer(final int playerId, final PunishmentType punishmentType);

	public abstract CharacterBanInfo getCharBanInfo(final int playerId);
}
