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
package com.aionemu.gameserver.services.player;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SECURITY_TOKEN;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author xXMashUpXx
 */
public class PlayerSecurityTokenService {

	private final Logger log = LoggerFactory.getLogger(PlayerSecurityTokenService.class);
	String token;

	public void generateToken(Player player) {
		if (player == null) {
			log.warn("[SecurityToken] Player don't exist O.o");
			return;
		}

		if (!"".equals(player.getPlayerAccount().getSecurityToken())) {
			log.warn("[SecurityToken] Player with already exist token should'nt get another one!");
			return;
		}

		MD5(player.getName() + "GH58" + player.getRace().toString() + "8HHGZTU");

		player.getPlayerAccount().setSecurityToken(token);
		sendToken(player, player.getPlayerAccount().getSecurityToken());
	}

	public void sendToken(Player player, String token) {
		if (player == null)
			return;
		PacketSendUtility.sendPacket(player, new SM_SECURITY_TOKEN(token));
	}

	public String MD5(String md5) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] array = md.digest(md5.getBytes());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < array.length; ++i) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
			}
			return token = sb.toString();
		}
		catch (NoSuchAlgorithmException e) {
			log.warn("[SecurityToken] Error to generate token for player!");
		}
		return null;
	}

	public static final PlayerSecurityTokenService getInstance() {
		return SingletonHolder.instance;
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder {

		protected static final PlayerSecurityTokenService instance = new PlayerSecurityTokenService();
	}
}
