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

import java.util.List;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.world.WeatherTable;
import com.aionemu.gameserver.model.templates.zone.ZoneClassName;
import com.aionemu.gameserver.services.WeatherService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;
import com.aionemu.gameserver.world.WorldMapType;
import com.aionemu.gameserver.world.zone.ZoneInstance;

/**
 * Admin command allowing to change weathers of the world.
 *
 * @author Kwazar
 */
public class Weather extends AdminCommand {

	public Weather() {
		super("weather");
	}

	@Override
	public void execute(Player admin, String... params) {
		String regionName = null;

		if (params.length == 0) {
			int weatherCode = -1;
			List<ZoneInstance> zones = admin.getActiveRegion().getZones(admin);
			for (ZoneInstance regionZone : zones) {
				if (regionZone.getZoneTemplate().getZoneType() == ZoneClassName.WEATHER) {
					int weatherZoneId = DataManager.ZONE_DATA.getWeatherZoneId(regionZone.getZoneTemplate());
					weatherCode = WeatherService.getInstance().getWeatherCode(admin.getWorldId(), weatherZoneId);
					regionName = regionZone.getZoneTemplate().getXmlName();
					break;
				}
			}
			if (weatherCode == -1) {
				PacketSendUtility.sendMessage(admin, "No weather.");
			}
			else {
				PacketSendUtility.sendMessage(admin, "Weather code for region " + regionName + " is " + weatherCode);
			}
			return;
		}

		if (params.length > 2) {
			onFail(admin, null);
			return;
		}

		int weatherType = -1;
		regionName = new String(params[0]);

		if (params.length == 2) {
			try {
				weatherType = Integer.parseInt(params[1]);
			}
			catch (NumberFormatException e) {
				PacketSendUtility.sendMessage(admin, "weather type parameter need to be an integer [0-12].");
				return;
			}
		}

		if (regionName.equals("reset")) {
			WeatherService.getInstance().resetWeather();
			return;
		}

		// Retrieving regionId by name
		WorldMapType region = null;
		for (WorldMapType worldMapType : WorldMapType.values()) {
			if (worldMapType.name().toLowerCase().equals(regionName.toLowerCase())) {
				region = worldMapType;
				break;
			}
		}

		if (region != null) {
			if (weatherType > -1 && weatherType < 13) {
				WeatherTable table = DataManager.MAP_WEATHER_DATA.getWeather(region.getId());
				if (table == null || table.getZoneCount() == 0) {
					PacketSendUtility.sendMessage(admin, "Region has no weather defined");
					return;
				}
				/*
				 * if (table.getWeatherCount() < weatherType) { PacketSendUtility.sendMessage(admin, "Region has no such weather value; max is=" + table.getWeatherCount()); return; }
				 */
				WeatherService.getInstance().changeRegionWeather(region.getId(), weatherType);
			}
			else {
				PacketSendUtility.sendMessage(admin, "Weather type must be between 0 and 12");
				return;
			}
		}
		else {
			PacketSendUtility.sendMessage(admin, "Region " + regionName + " not found");
			return;
		}
	}

	@Override
	public void onFail(Player player, String message) {
		PacketSendUtility.sendMessage(player, "syntax //weather <regionName(poeta, ishalgen, etc ...)> <value(0->12)> OR //weather reset");
	}
}
