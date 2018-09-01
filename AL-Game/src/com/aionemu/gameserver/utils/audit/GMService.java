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
package com.aionemu.gameserver.utils.audit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.aionemu.gameserver.configs.administration.AdminConfig;
import com.aionemu.gameserver.configs.main.MembershipConfig;
import com.aionemu.gameserver.configs.main.WeddingsConfig;
import com.aionemu.gameserver.model.ChatType;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.i18n.CustomMessageId;
import com.aionemu.gameserver.utils.i18n.LanguageHandler;
import com.aionemu.gameserver.world.World;

import javolution.util.FastMap;

/**
 * @author Waii
 */
public class GMService {

	public static final GMService getInstance() {
		return SingletonHolder.instance;
	}

	private Map<Integer, Player> gms = new FastMap<Integer, Player>();
	private boolean announceAny = false;
	private List<Byte> announceList;

	private GMService() {

		announceList = new ArrayList<Byte>();
		announceAny = AdminConfig.ANNOUNCE_LEVEL_LIST.equals("*");
		if (!announceAny) {
			try {
				for (String level : AdminConfig.ANNOUNCE_LEVEL_LIST.split(",")) {
					announceList.add(Byte.parseByte(level));
				}
			}
			catch (Exception e) {
				announceAny = true;
			}
		}
	}

	public void onPlayerLogin(Player player) {
		if (player.isGM()) {
			gms.put(player.getObjectId(), player);
		}
	}

	public void onPlayerLogedOut(Player player) {
		if (player.isGM()) {
			gms.remove(player.getObjectId());
		}
	}

	public Collection<Player> getGMs() {
		return gms.values();
	}

	public void onPlayerAvailable(Player player) {
		if (player.isGM()) {
			gms.put(player.getObjectId(), player);
			String adminTag = "%s";
			StringBuilder sb = new StringBuilder(adminTag);

			if (player.getClientConnection() != null) {

				// * = Premium & VIP Membership
				if (MembershipConfig.PREMIUM_TAG_DISPLAY) {
					switch (player.getClientConnection().getAccount().getMembership()) {
						case 1:
							adminTag = sb.insert(0, MembershipConfig.TAG_PREMIUM.substring(0, 2)).toString();
							break;
						case 2:
							adminTag = sb.insert(0, MembershipConfig.TAG_VIP.substring(0, 2)).toString();
							break;
					}
				}
				// * = Wedding
				if (player.isMarried()) {
					adminTag = sb.insert(0, WeddingsConfig.TAG_WEDDING).toString();
				}
				if (AdminConfig.CUSTOMTAG_ENABLE) {
					if (player.getAccessLevel() == 1) {
						adminTag = AdminConfig.CUSTOMTAG_ACCESS1.replace("%s", sb.toString());
					}
					else if (player.getAccessLevel() == 2) {
						adminTag = AdminConfig.CUSTOMTAG_ACCESS2.replace("%s", sb.toString());
					}
					else if (player.getAccessLevel() == 3) {
						adminTag = AdminConfig.CUSTOMTAG_ACCESS3.replace("%s", sb.toString());
					}
					else if (player.getAccessLevel() == 4) {
						adminTag = AdminConfig.CUSTOMTAG_ACCESS4.replace("%s", sb.toString());
					}
					else if (player.getAccessLevel() == 5) {
						adminTag = AdminConfig.CUSTOMTAG_ACCESS5.replace("%s", sb.toString());
					}
					else if (player.getAccessLevel() == 6) {
						adminTag = AdminConfig.CUSTOMTAG_ACCESS6.replace("%s", sb.toString());
					}
					else if (player.getAccessLevel() == 7) {
						adminTag = AdminConfig.CUSTOMTAG_ACCESS7.replace("%s", sb.toString());
					}
					else if (player.getAccessLevel() == 8) {
						adminTag = AdminConfig.CUSTOMTAG_ACCESS8.replace("%s", sb.toString());
					}
					else if (player.getAccessLevel() == 9) {
						adminTag = AdminConfig.CUSTOMTAG_ACCESS9.replace("%s", sb.toString());
					}
					else if (player.getAccessLevel() == 10) {
						adminTag = AdminConfig.CUSTOMTAG_ACCESS10.replace("%s", sb.toString());
					}
				}
			}

			Iterator<Player> iter = World.getInstance().getPlayersIterator();
			while (iter.hasNext()) {
				PacketSendUtility.sendBrightYellowMessageOnCenter(iter.next(), "Information : " + String.format(adminTag, player.getName()) + LanguageHandler.translate(CustomMessageId.ANNOUNCE_GM_CONNECTION));
			}
		}
	}

	public void onPlayerUnavailable(Player player) {
		gms.remove(player.getObjectId());
		String adminTag = "%s";
		StringBuilder sb = new StringBuilder(adminTag);

		if (player.getClientConnection() != null) {

			// * = Premium & VIP Membership
			if (MembershipConfig.PREMIUM_TAG_DISPLAY) {
				switch (player.getClientConnection().getAccount().getMembership()) {
					case 1:
						adminTag = sb.insert(0, MembershipConfig.TAG_PREMIUM.substring(0, 2)).toString();
						break;
					case 2:
						adminTag = sb.insert(0, MembershipConfig.TAG_VIP.substring(0, 2)).toString();
						break;
				}
			}
			// * = Wedding
			if (player.isMarried()) {
				adminTag = sb.insert(0, WeddingsConfig.TAG_WEDDING).toString();
			}

			if (AdminConfig.CUSTOMTAG_ENABLE) {
				if (player.getAccessLevel() == 1) {
					adminTag = AdminConfig.CUSTOMTAG_ACCESS1.replace("%s", sb.toString());
				}
				else if (player.getAccessLevel() == 2) {
					adminTag = AdminConfig.CUSTOMTAG_ACCESS2.replace("%s", sb.toString());
				}
				else if (player.getAccessLevel() == 3) {
					adminTag = AdminConfig.CUSTOMTAG_ACCESS3.replace("%s", sb.toString());
				}
				else if (player.getAccessLevel() == 4) {
					adminTag = AdminConfig.CUSTOMTAG_ACCESS4.replace("%s", sb.toString());
				}
				else if (player.getAccessLevel() == 5) {
					adminTag = AdminConfig.CUSTOMTAG_ACCESS5.replace("%s", sb.toString());
				}
				else if (player.getAccessLevel() == 6) {
					adminTag = AdminConfig.CUSTOMTAG_ACCESS6.replace("%s", sb.toString());
				}
				else if (player.getAccessLevel() == 7) {
					adminTag = AdminConfig.CUSTOMTAG_ACCESS7.replace("%s", sb.toString());
				}
				else if (player.getAccessLevel() == 8) {
					adminTag = AdminConfig.CUSTOMTAG_ACCESS8.replace("%s", sb.toString());
				}
				else if (player.getAccessLevel() == 9) {
					adminTag = AdminConfig.CUSTOMTAG_ACCESS9.replace("%s", sb.toString());
				}
				else if (player.getAccessLevel() == 10) {
					adminTag = AdminConfig.CUSTOMTAG_ACCESS10.replace("%s", sb.toString());
				}
			}
		}

		Iterator<Player> iter = World.getInstance().getPlayersIterator();
		while (iter.hasNext()) {
			PacketSendUtility.sendBrightYellowMessageOnCenter(iter.next(), "Information : " + String.format(adminTag, player.getName()) + LanguageHandler.translate(CustomMessageId.ANNOUNCE_GM_DECONNECTION));
		}
	}

	public void broadcastMesage(String message) {
		SM_MESSAGE packet = new SM_MESSAGE(0, null, message, ChatType.YELLOW);
		for (Player player : gms.values()) {
			PacketSendUtility.sendPacket(player, packet);
		}
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder {

		protected static final GMService instance = new GMService();
	}
}
