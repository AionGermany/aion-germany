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
package instance;

import java.util.List;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.flyring.FlyRing;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.flyring.FlyRingTemplate;
import com.aionemu.gameserver.model.utils3d.Point3D;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUEST_ACTION;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;

/**
 * @author xTz, Bobobear
 */
@InstanceID(300700000)
public class TheHexwayInstance extends GeneralInstanceHandler {

	private boolean isStartTimer = false;
	private long startTime;
	private boolean isInstanceDestroyed = false;

	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
		spawnRings();
	}

	private void spawnRings() {
		FlyRing f1 = new FlyRing(new FlyRingTemplate("HEXWAY_WING_1", mapId, new Point3D(576.2102, 585.4146, 353.90677), new Point3D(576.2102, 585.4146, 359.90677), new Point3D(575.18384, 596.36664, 353.90677), 10), instanceId);
		f1.spawn();
	}

	@Override
	public boolean onPassFlyingRing(Player player, String flyingRing) {
		if (flyingRing.equals("HEXWAY_WING_1")) {
			if (!isStartTimer) {
				isStartTimer = true;
				startTime = System.currentTimeMillis();
				PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 900));
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						sendMsg(1400244);
						despawnNpcs(getNpcs(701664));
						despawnNpcs(getNpcs(701662));
						despawnNpcs(getNpcs(701663));
					}
				}, 900000);
			}
		}
		return false;
	}

	@Override
	public void onEnterInstance(Player player) {
		if (isStartTimer) {
			long time = System.currentTimeMillis() - startTime;
			if (time < 900000) {
				PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 900 - (int) time / 1000));
			}
		}

	}

	private List<Npc> getNpcs(int npcId) {
		if (!isInstanceDestroyed) {
			return instance.getNpcs(npcId);
		}
		return null;
	}

	private void despawnNpcs(List<Npc> npcs) {
		for (Npc npc : npcs) {
			npc.getController().onDelete();
		}
	}

	@Override
	public void onInstanceDestroy() {
		isInstanceDestroyed = true;
	}

	@Override
	public boolean onDie(final Player player, Creature lastAttacker) {
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);

		PacketSendUtility.sendPacket(player, new SM_DIE(player.haveSelfRezEffect(), player.haveSelfRezItem(), 0, 8));
		return true;
	}

	@Override
	public void onPlayerLogOut(Player player) {
		TeleportService2.moveToInstanceExit(player, mapId, player.getRace());
	}
}
