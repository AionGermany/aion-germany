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

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.zone.ZoneType;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;
import com.aionemu.gameserver.world.zone.ZoneInstance;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author ATracer
 */
public class Zone extends AdminCommand {

	public Zone() {
		super("zone");
	}

	@Override
	public void execute(Player admin, String... params) {
		Creature target;
		if (admin.getTarget() == null || !(admin.getTarget() instanceof Creature)) {
			target = admin;
		}
		else {
			target = (Creature) admin.getTarget();
		}
		if (params.length == 0) {
			List<ZoneInstance> zones = target.getPosition().getMapRegion().getZones(target);
			if (zones.isEmpty()) {
				PacketSendUtility.sendMessage(admin, target.getName() + " are out of any zone");
			}
			else {
				PacketSendUtility.sendMessage(admin, target.getName() + " are in zone: ");
				PacketSendUtility.sendMessage(admin, "Registered zones:");
				if (admin.isInsideZoneType(ZoneType.DAMAGE)) {
					PacketSendUtility.sendMessage(admin, "DAMAGE");
				}
				if (admin.isInsideZoneType(ZoneType.FLY)) {
					PacketSendUtility.sendMessage(admin, "FLY");
				}
				if (admin.isInsideZoneType(ZoneType.PVP)) {
					PacketSendUtility.sendMessage(admin, "PVP");
				}
				if (admin.isInsideZoneType(ZoneType.SIEGE)) {
					PacketSendUtility.sendMessage(admin, "CASTLE");
				}
				if (admin.isInsideZoneType(ZoneType.WATER)) {
					PacketSendUtility.sendMessage(admin, "WATER");
				}
				for (ZoneInstance zone : zones) {
					PacketSendUtility.sendMessage(admin, zone.getAreaTemplate().getZoneName().name());
					PacketSendUtility.sendMessage(admin, "Fly: " + zone.canFly() + "; Glide: " + zone.canGlide());
					PacketSendUtility.sendMessage(admin, "Ride: " + zone.canRide() + "; Fly-ride: " + zone.canFlyRide());
					PacketSendUtility.sendMessage(admin, "Kisk: " + zone.canPutKisk() + "; Racall: " + zone.canRecall());
					PacketSendUtility.sendMessage(admin, "Same race duels: " + zone.isSameRaceDuelsAllowed() + "; Other race duels: " + zone.isOtherRaceDuelsAllowed());
					PacketSendUtility.sendMessage(admin, "PvP: " + zone.isPvpAllowed());
				}
			}
		}
		else if ("?".equalsIgnoreCase(params[0])) {
			onFail(admin, null);
		}
		else if ("refresh".equalsIgnoreCase(params[0])) {
			admin.revalidateZones();
		}
		else if ("inside".equalsIgnoreCase(params[0])) {
			try {
				ZoneName name = ZoneName.get(params[1]);
				PacketSendUtility.sendMessage(admin, "isInsideZone: " + admin.isInsideZone(name));
			}
			catch (Exception e) {
				PacketSendUtility.sendMessage(admin, "Zone name missing!");
				PacketSendUtility.sendMessage(admin, "Syntax: //zone inside <zone name> ");
			}
		}
	}

	@Override
	public void onFail(Player player, String message) {
		PacketSendUtility.sendMessage(player, "Syntax: //zone refresh | inside");
	}
}
