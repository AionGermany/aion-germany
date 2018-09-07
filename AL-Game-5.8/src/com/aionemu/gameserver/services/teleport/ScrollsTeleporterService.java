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
package com.aionemu.gameserver.services.teleport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.configs.main.ReturnScrollsConfig;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.portal.PortalLoc;

/**
 * @author GiGatR00n
 */
public class ScrollsTeleporterService {

	private static final Logger log = LoggerFactory.getLogger(ScrollsTeleporterService.class);

	/**
	 * @param player
	 * @param LocId
	 * @param worldId
	 */
	public static void ScrollTeleprter(Player player, int LocId, int worldId) {

		PortalLoc loc = DataManager.PORTAL_LOC_DATA.getPortalLoc(LocId);

		if (loc == null) {
			log.warn("No Portal location for locId" + LocId);
			return;
		}

		if (ReturnScrollsConfig.TELEPORT_ANIMATION == 0) {
			TeleportService2.teleportTo(player, worldId, loc.getX(), loc.getY(), loc.getZ(), player.getHeading(), TeleportAnimation.NO_ANIMATION);
		}
		else if (ReturnScrollsConfig.TELEPORT_ANIMATION == 1) {
			TeleportService2.teleportTo(player, worldId, loc.getX(), loc.getY(), loc.getZ(), player.getHeading(), TeleportAnimation.BEAM_ANIMATION);
		}
		else if (ReturnScrollsConfig.TELEPORT_ANIMATION == 2) {
			TeleportService2.teleportTo(player, worldId, loc.getX(), loc.getY(), loc.getZ(), player.getHeading(), TeleportAnimation.JUMP_ANIMATION);
		}
		else if (ReturnScrollsConfig.TELEPORT_ANIMATION == 3) {
			TeleportService2.teleportTo(player, worldId, loc.getX(), loc.getY(), loc.getZ(), player.getHeading(), TeleportAnimation.JUMP_ANIMATION_2);
		}
		else if (ReturnScrollsConfig.TELEPORT_ANIMATION == 4) {
			TeleportService2.teleportTo(player, worldId, loc.getX(), loc.getY(), loc.getZ(), player.getHeading(), TeleportAnimation.JUMP_ANIMATION_3);
		}
		else {
			TeleportService2.teleportTo(player, worldId, loc.getX(), loc.getY(), loc.getZ(), player.getHeading(), TeleportAnimation.NO_ANIMATION);
		}
	}

	/**
	 * @param worldId
	 * @param race
	 */
	public static int getScrollLocIdbyWorldId(int worldId, Race race) {

		switch (worldId) {

			case 600050000: // Kaisinel's Beacon (Elyos Katalam) | Danuar Spire (Asmo Katalam)
				return (race == Race.ELYOS ? 6000502 : 6000503);
			case 600070000: // Idian Depths
				return (race == Race.ELYOS ? 6000700 : 6000701);
			case 400010000: // Teminon Fortress (Elyos) | Primum Fortress (Asmo)
				return (race == Race.ELYOS ? 4000100 : 4000101);
			case 700010000: // Oriel (Elyos) | Pernon (Asmo)
			case 710010000:
				return (race == Race.ELYOS ? 7000101 : 7100100);
			case 110070000: // Kaisinel Academy (Elyos) | Marchutan Priory (Asmo)
			case 120080000:
				return (race == Race.ELYOS ? 1100702 : 1200800);
			case 600100000: // Levinshor Gerha (Elyos) | Levinshor Gerha (Asmo)
				return (race == Race.ELYOS ? 6001007 : 6001008);
			case 600060000: // Pandarunerk's Delve (Elyos Danarina) | Pandarunerk (Asmo Danarina)
				return 6000603;
		}
		return 0;
	}
}
