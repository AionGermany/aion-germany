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
package instance.pvparenas;

import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.flyring.FlyRing;
import com.aionemu.gameserver.model.gameobjects.Gatherable;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.instance.playerreward.PvPArenaPlayerReward;
import com.aionemu.gameserver.model.templates.flyring.FlyRingTemplate;
import com.aionemu.gameserver.model.utils3d.Point3D;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;

/**
 * @author xTz
 */
@InstanceID(300420000)
public class ChaosTrainingGroundsInstance extends PvPArenaInstance {

	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		killBonus = 1000;
		deathFine = -125;
		super.onInstanceCreate(instance);
	}

	@Override
	public void onGather(Player player, Gatherable gatherable) {
		if (!instanceReward.isStartProgress()) {
			return;
		}
		getPlayerReward(player.getObjectId()).addPoints(1250);
		sendPacket();
		int nameId = gatherable.getObjectTemplate().getNameId();
		DescriptionId name = new DescriptionId(nameId * 2 + 1);
		PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400237, name, 1250));
	}

	@Override
	protected void spawnRings() {
		FlyRing f1 = new FlyRing(new FlyRingTemplate("PVP_ARENA_1", mapId, new Point3D(674.66974, 1792.8499, 149.77501), new Point3D(674.66974, 1792.8499, 155.77501), new Point3D(678.83636, 1788.5325, 149.77501), 6), instanceId);
		f1.spawn();
		FlyRing f2 = new FlyRing(new FlyRingTemplate("PVP_ARENA_2", mapId, new Point3D(688.30615, 1769.7937, 149.88556), new Point3D(688.30615, 1769.7937, 155.88556), new Point3D(689.42096, 1763.8982, 149.88556), 6), instanceId);
		f2.spawn();
		FlyRing f3 = new FlyRing(new FlyRingTemplate("PVP_ARENA_3", mapId, new Point3D(664.2252, 1761.671, 170.95732), new Point3D(664.2252, 1761.671, 176.95732), new Point3D(669.2843, 1764.8967, 170.95732), 6), instanceId);
		f3.spawn();
		FlyRing fv1 = new FlyRing(new FlyRingTemplate("PVP_ARENA_VOID_1", mapId, new Point3D(690.28625, 1753.8561, 192.07726), new Point3D(690.28625, 1753.8561, 198.07726), new Point3D(689.4365, 1747.9165, 192.07726), 6), instanceId);
		fv1.spawn();
		FlyRing fv2 = new FlyRing(new FlyRingTemplate("PVP_ARENA_VOID_2", mapId, new Point3D(690.1935, 1797.0029, 203.79236), new Point3D(690.1935, 1797.0029, 209.79236), new Point3D(692.8295, 1802.3928, 203.79236), 6), instanceId);
		fv2.spawn();
		FlyRing fv3 = new FlyRing(new FlyRingTemplate("PVP_ARENA_VOID_3", mapId, new Point3D(659.2784, 1766.0273, 207.25465), new Point3D(659.2784, 1766.0273, 213.25465), new Point3D(665.2619, 1766.4718, 207.25465), 6), instanceId);
		fv3.spawn();
	}

	@Override
	public boolean onPassFlyingRing(Player player, String flyingRing) {
		PvPArenaPlayerReward playerReward = getPlayerReward(player.getObjectId());
		if (playerReward == null || !instanceReward.isStartProgress()) {
			return false;
		}
		Npc npc;
		if (flyingRing.equals("PVP_ARENA_1")) {
			npc = getNpc(674.841f, 1793.065f, 150.964f);
			if (npc != null && npc.isSpawned()) {
				npc.getController().scheduleRespawn();
				npc.getController().onDelete();
				sendSystemMsg(player, npc, 250);
				sendPacket();
			}
		}
		else if (flyingRing.equals("PVP_ARENA_2")) {
			npc = getNpc(688.410f, 1769.611f, 150.964f);
			if (npc != null && npc.isSpawned()) {
				npc.getController().scheduleRespawn();
				npc.getController().onDelete();
				playerReward.addPoints(250);
				sendSystemMsg(player, npc, 250);
				sendPacket();
			}
		}
		else if (flyingRing.equals("PVP_ARENA_3")) {
			npc = getNpc(664.160f, 1761.933f, 171.504f);
			if (npc != null && npc.isSpawned()) {
				npc.getController().scheduleRespawn();
				npc.getController().onDelete();
				playerReward.addPoints(250);
				sendSystemMsg(player, npc, 250);
				sendPacket();
			}
		}
		else if (flyingRing.equals("PVP_ARENA_VOID_1")) {
			npc = getNpc(693.061f, 1752.479f, 186.750f);
			if (npc != null && npc.isSpawned()) {
				useSkill(npc, player, 20059, 1);
				npc.getController().scheduleRespawn();
				npc.getController().onDelete();
			}
		}
		else if (flyingRing.equals("PVP_ARENA_VOID_2")) {
			npc = getNpc(688.061f, 1798.229f, 198.500f);
			if (npc != null && npc.isSpawned()) {
				useSkill(npc, player, 20059, 1);
				npc.getController().scheduleRespawn();
				npc.getController().onDelete();
			}
		}
		else if (flyingRing.equals("PVP_ARENA_VOID_3")) {
			npc = getNpc(659.311f, 1768.979f, 201.500f);
			if (npc != null && npc.isSpawned()) {
				useSkill(npc, player, 20059, 1);
				npc.getController().scheduleRespawn();
				npc.getController().onDelete();
			}
		}
		return false;
	}
}
