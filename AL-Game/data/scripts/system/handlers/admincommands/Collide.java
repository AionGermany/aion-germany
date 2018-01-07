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

import java.util.Iterator;

import com.aionemu.gameserver.geoEngine.collision.CollisionIntention;
import com.aionemu.gameserver.geoEngine.collision.CollisionResult;
import com.aionemu.gameserver.geoEngine.collision.CollisionResults;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;
import com.aionemu.gameserver.world.geo.GeoService;

/**
 * @author Rolandas
 */
public class Collide extends AdminCommand {

	public Collide() {
		super("collide");
	}

	@Override
	public void execute(Player admin, String... params) {
		VisibleObject target = admin.getTarget();
		if (target == null) {
			PacketSendUtility.sendMessage(admin, "Must select a target!");
			return;
		}
		if (params.length > 1 || params.length == 1 && !"me".equalsIgnoreCase(params[0])) {
			onFail(admin, null);
			return;
		}

		final byte intentions = (CollisionIntention.ALL.getId());
		float x = target.getX();
		float y = target.getY();
		float z = target.getZ();
		float targetX, targetY, targetZ;

		if (params.length == 0) {
			targetX = x;
			targetY = y;
			targetZ = z - 10;
		}
		else {
			targetX = admin.getX();
			targetY = admin.getY();
			targetZ = admin.getZ() + admin.getObjectTemplate().getBoundRadius().getUpper() / 2;
		}

		if (params.length == 1) {
			PacketSendUtility.sendMessage(admin, "From target direction:");
		}
		PacketSendUtility.sendMessage(admin, "Target: X=" + x + "; Y=" + y + "; Z=" + z);

		CollisionResults results = GeoService.getInstance().getCollisions(target, targetX, targetY, targetZ, false, intentions);
		CollisionResult closest = results.getClosestCollision();

		if (results.size() == 0) {
			PacketSendUtility.sendMessage(admin, "Hm... Nothing collidable?");
			if (params.length == 0) {
				return;
			}
			else {
				closest = null;
			}
		}
		else {
			Iterator<CollisionResult> iter = results.iterator();
			int count = 0;
			int closestId = 0;
			String description = "";
			while (iter.hasNext()) {
				count++;
				CollisionResult result = iter.next();
				if (result.equals(closest)) {
					closestId = count;
				}
				if (result.getGeometry() == null) {
					description += count + ". " + result.getContactPoint().toString() + "\n";
				}
				else {
					if (result.getGeometry().getName() == null) {
						description += count + ". " + result.getContactPoint().toString() + "; parent=" + result.getGeometry().getParent().getName() + "\n";
					}
					else {
						description += count + ". " + result.getContactPoint().toString() + "; name=" + result.getGeometry().getName() + "\n";
					}
				}
			}
			description += "-----------------------\nClosest: " + closestId + ". Distance: " + closest.getDistance();
			PacketSendUtility.sendMessage(admin, description);
		}

		CollisionResult closestOpposite = null;

		if (params.length == 1) {
			PacketSendUtility.sendMessage(admin, "From opposite direction:");
			PacketSendUtility.sendMessage(admin, "Admin: X=" + admin.getX() + "; Y=" + admin.getY() + "; Z=" + admin.getZ());

			results = GeoService.getInstance().getCollisions(admin, target.getX(), target.getY(), target.getZ() + target.getObjectTemplate().getBoundRadius().getUpper() / 2, false, intentions);
			closestOpposite = results.getClosestCollision();

			if (results.size() == 0) {
				PacketSendUtility.sendMessage(admin, "Hm... Nothing collidable?");
				closestOpposite = null;
			}
			else {
				Iterator<CollisionResult> iter2 = results.iterator();
				int count = 0;
				int closestId = 0;
				String description = "";

				while (iter2.hasNext()) {
					count++;
					CollisionResult result = iter2.next();
					if (result.equals(closestOpposite)) {
						closestId = count;
					}
					if (result.getGeometry() == null) {
						description += count + ". " + result.getContactPoint().toString() + "\n";
					}
					else {
						if (result.getGeometry().getName() == null) {
							description += count + ". " + result.getContactPoint().toString() + "; parent=" + result.getGeometry().getParent().getName() + "\n";
						}
						else {
							description += count + ". " + result.getContactPoint().toString() + "; name=" + result.getGeometry().getName() + "\n";
						}
					}
				}
				description += "-----------------------\nClosest: " + closestId + ". Distance: " + closestOpposite.getDistance();
				PacketSendUtility.sendMessage(admin, description);
			}
		}

		if (params.length == 0 && closest.getContactPoint().z + 0.5f < target.getZ()) {
			PacketSendUtility.sendMessage(admin, "Below actual Z!");
		}
		else {
			if (closest != null) {
				SpawnTemplate spawn = SpawnEngine.addNewSpawn(admin.getWorldId(), 200000, closest.getContactPoint().x, closest.getContactPoint().y, closest.getContactPoint().z, (byte) 0, 0);
				SpawnEngine.spawnObject(spawn, admin.getInstanceId());
			}
			if (closestOpposite != null) {
				SpawnTemplate spawn = SpawnEngine.addNewSpawn(admin.getWorldId(), 200000, closestOpposite.getContactPoint().x, closestOpposite.getContactPoint().y, closestOpposite.getContactPoint().z, (byte) 0, 0);
				SpawnEngine.spawnObject(spawn, admin.getInstanceId());
			}
		}
	}

	@Override
	public void onFail(Player player, String message) {
		String syntax = "Syntax: //collide [me]";
		PacketSendUtility.sendMessage(player, syntax);
	}
}
