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
package admincommands;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.PlayerDAO;
import com.aionemu.gameserver.dao.SiegeDAO;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PlayerCommonData;
import com.aionemu.gameserver.model.siege.ArtifactLocation;
import com.aionemu.gameserver.model.siege.FortressLocation;
import com.aionemu.gameserver.model.siege.SiegeLocation;
import com.aionemu.gameserver.model.siege.SiegeModType;
import com.aionemu.gameserver.model.siege.SiegeRace;
import com.aionemu.gameserver.model.team.legion.Legion;
import com.aionemu.gameserver.services.LegionService;
import com.aionemu.gameserver.services.SiegeService;
import com.aionemu.gameserver.services.siegeservice.BalaurAssaultService;
import com.aionemu.gameserver.services.siegeservice.Siege;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;

@SuppressWarnings("rawtypes")
public class SiegeCommand extends AdminCommand {

	private static final String COMMAND_START = "start";
	private static final String COMMAND_STOP = "stop";
	private static final String COMMAND_LIST = "list";
	private static final String COMMAND_LIST_LOCATIONS = "locations";
	private static final String COMMAND_LIST_SIEGES = "sieges";
	private static final String COMMAND_CAPTURE = "capture";
	private static final String COMMAND_ASSAULT = "assault";

	public SiegeCommand() {
		super("siege");
	}

	@Override
	public void execute(Player player, String... params) {

		if (params.length == 0) {
			showHelp(player);
			return;
		}

		if (COMMAND_STOP.equalsIgnoreCase(params[0]) || COMMAND_START.equalsIgnoreCase(params[0])) {
			handleStartStopSiege(player, params);
		}
		else if (COMMAND_LIST.equalsIgnoreCase(params[0])) {
			handleList(player, params);
		}
		else if (COMMAND_LIST_SIEGES.equals(params[0])) {
			listLocations(player);
		}
		else if (COMMAND_CAPTURE.equals(params[0])) {
			capture(player, params);
		}
		else if (COMMAND_ASSAULT.equals(params[0])) {
			assault(player, params);
		}
	}

	protected void handleStartStopSiege(Player player, String... params) {
		if (params.length != 2 || !NumberUtils.isDigits(params[1])) {
			showHelp(player);
			return;
		}

		int siegeLocId = NumberUtils.toInt(params[1]);
		if (!isValidSiegeLocationId(player, siegeLocId)) {
			showHelp(player);
			return;
		}

		if (COMMAND_START.equalsIgnoreCase(params[0])) {
			if (SiegeService.getInstance().isSiegeInProgress(siegeLocId)) {
				PacketSendUtility.sendMessage(player, "Siege Location " + siegeLocId + " is already under siege");
			}
			else {
				PacketSendUtility.sendMessage(player, "Siege Location " + siegeLocId + " - starting siege!");
				SiegeService.getInstance().startSiege(siegeLocId);
			}
		}
		else if (COMMAND_STOP.equalsIgnoreCase(params[0])) {
			if (!SiegeService.getInstance().isSiegeInProgress(siegeLocId)) {
				PacketSendUtility.sendMessage(player, "Siege Location " + siegeLocId + " is not under siege");
			}
			else {
				PacketSendUtility.sendMessage(player, "Siege Location " + siegeLocId + " - stopping siege!");
				SiegeService.getInstance().stopSiege(siegeLocId);
			}
		}
	}

	protected boolean isValidSiegeLocationId(Player player, int fortressId) {

		if (!SiegeService.getInstance().getSiegeLocations().keySet().contains(fortressId)) {
			PacketSendUtility.sendMessage(player, "Id " + fortressId + " is invalid");
			return false;
		}

		return true;
	}

	protected void handleList(Player player, String[] params) {
		if (params.length != 2) {
			showHelp(player);
			return;
		}

		if (COMMAND_LIST_LOCATIONS.equalsIgnoreCase(params[1])) {
			listLocations(player);
		}
		else if (COMMAND_LIST_SIEGES.equalsIgnoreCase(params[1])) {
			listSieges(player);
		}
		else {
			showHelp(player);
		}
	}

	protected void listLocations(Player player) {
		for (FortressLocation f : SiegeService.getInstance().getFortresses().values()) {
			PacketSendUtility.sendMessage(player, "Fortress: " + f.getLocationId() + " belongs to " + f.getRace());
		}
		for (ArtifactLocation a : SiegeService.getInstance().getStandaloneArtifacts().values()) {
			PacketSendUtility.sendMessage(player, "Artifact: " + a.getLocationId() + " belongs to " + a.getRace());
		}
	}

	protected void listSieges(Player player) {
		for (Integer i : SiegeService.getInstance().getSiegeLocations().keySet()) {
			Siege s = SiegeService.getInstance().getSiege(i);
			if (s != null) {
				int secondsLeft = SiegeService.getInstance().getRemainingSiegeTimeInSeconds(i);
				String minSec = secondsLeft / 60 + "m ";
				minSec += secondsLeft % 60 + "s";
				PacketSendUtility.sendMessage(player, "Location: " + i + ": " + minSec + " left.");
			}
		}
	}

	protected void capture(Player player, String[] params) {
		if (params.length < 3 || !NumberUtils.isNumber(params[1])) {
			showHelp(player);
			return;
		}

		int siegeLocationId = NumberUtils.toInt(params[1]);
		if (!SiegeService.getInstance().getSiegeLocations().keySet().contains(siegeLocationId)) {
			PacketSendUtility.sendMessage(player, "Invalid Siege Location Id: " + siegeLocationId);
			return;
		}

		// check if params2 is siege race
		SiegeRace sr = null;
		try {
			sr = SiegeRace.valueOf(params[2].toUpperCase());
		}
		catch (IllegalArgumentException e) {
			// ignore
		}

		// try to find legion by name
		Legion legion = null;
		if (sr == null) {

			try {
				int legionId = Integer.valueOf(params[2]);
				legion = LegionService.getInstance().getLegion(legionId);
			}
			catch (NumberFormatException e) {
				String legionName = "";
				for (int i = 2; i < params.length; i++) {
					legionName += " " + params[i];
				}
				legion = LegionService.getInstance().getLegion(legionName.trim());
			}

			if (legion != null) {
				int legionBGeneral = LegionService.getInstance().getLegionBGeneral(legion.getLegionId());
				if (legionBGeneral != 0) {
					PlayerCommonData BGeneral = DAOManager.getDAO(PlayerDAO.class).loadPlayerCommonData(legionBGeneral);
					sr = SiegeRace.getByRace(BGeneral.getRace());
				}
			}
		}

		// check if can capture
		if (legion == null && sr == null) {
			PacketSendUtility.sendMessage(player, params[2] + " is not valid siege race or legion name");
			return;
		}

		// capture
		SiegeLocation loc = SiegeService.getInstance().getSiegeLocation(siegeLocationId);
		Siege s = SiegeService.getInstance().getSiege(siegeLocationId);
		if (s != null) {
			s.getSiegeCounter().addRaceDamage(sr, s.getBoss().getLifeStats().getMaxHp() + 1);
			s.setBossKilled(true);
			SiegeService.getInstance().stopSiege(siegeLocationId);
			loc.setLegionId(legion != null ? legion.getLegionId() : 0);
		}
		else {
			SiegeService.getInstance().deSpawnNpcs(siegeLocationId);
			loc.setVulnerable(false);
			loc.setUnderShield(false);
			loc.setRace(sr);
			loc.setLegionId(legion != null ? legion.getLegionId() : 0);
			SiegeService.getInstance().spawnNpcs(siegeLocationId, sr, SiegeModType.PEACE);
			DAOManager.getDAO(SiegeDAO.class).updateSiegeLocation(loc);
		}
		SiegeService.getInstance().broadcastUpdate(loc);
	}

	protected void assault(Player player, String[] params) {
		if (params.length < 2 || (!NumberUtils.isNumber(params[1]) && !NumberUtils.isNumber(params[2]))) {
			showHelp(player);
			return;
		}

		int siegeLocationId = NumberUtils.toInt(params[1]);
		int delay = NumberUtils.toInt(params[2]);
		if (!SiegeService.getInstance().getSiegeLocations().keySet().contains(siegeLocationId)) {
			PacketSendUtility.sendMessage(player, "Invalid Siege Location Id: " + siegeLocationId);
			return;
		}

		BalaurAssaultService.getInstance().startAssault(player, siegeLocationId, delay);
	}

	protected void showHelp(Player player) {
		PacketSendUtility.sendMessage(player, "AdminCommand //siege Help\n" + "//siege start|stop <LocationId>\n" + "//siege list locations|sieges\n" + "//siege capture <LocationId> <siegeRaceName(ELYOS,ASMODIANS,BALAUR)|legionName|legionId>\n" + "//siege assault <LocationId> <delaySec>");

		java.util.Set<Integer> fortressIds = SiegeService.getInstance().getFortresses().keySet();
		java.util.Set<Integer> artifactIds = SiegeService.getInstance().getStandaloneArtifacts().keySet();
		PacketSendUtility.sendMessage(player, "Fortress: " + StringUtils.join(fortressIds, ", "));
		PacketSendUtility.sendMessage(player, "Artifacts: " + StringUtils.join(artifactIds, ", "));
	}
}
