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
package instance.crucible;

import java.util.List;

import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.instance.StageType;
import com.aionemu.gameserver.model.instance.instancereward.InstanceReward;
import com.aionemu.gameserver.model.instance.playerreward.CruciblePlayerReward;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author xTz
 */
@SuppressWarnings("rawtypes")
public class CrucibleInstance extends GeneralInstanceHandler {

	protected boolean isInstanceDestroyed = false;
	protected StageType stageType = StageType.DEFAULT;
	protected InstanceReward instanceReward;

	@Override
	public void onEnterInstance(Player player) {
		if (!instanceReward.containPlayer(player.getObjectId())) {
			addPlayerReward(player);
		}
	}

	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
		instanceReward = new InstanceReward(mapId, instanceId);
	}

	@SuppressWarnings("unchecked")
	protected void addPlayerReward(Player player) {
		instanceReward.addPlayerReward(new CruciblePlayerReward(player.getObjectId()));
	}

	protected CruciblePlayerReward getPlayerReward(Integer object) {
		return (CruciblePlayerReward) instanceReward.getPlayerReward(object);
	}

	@Override
	public InstanceReward<?> getInstanceReward() {
		return instanceReward;
	}

	protected List<Npc> getNpcs(int npcId) {
		if (!isInstanceDestroyed) {
			return instance.getNpcs(npcId);
		}
		return null;
	}

	protected boolean isInZone(ZoneName zone, Player player) {
		return player.isInsideZone(zone);
	}

	protected void sendMsg(int msg, int Obj, int color) {
		sendMsg(msg, Obj, false, color);
	}

	@Override
	public boolean onDie(Player player, Creature lastAttacker) {
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);

		PacketSendUtility.sendPacket(player, new SM_DIE(false, false, 0, 8));
		return true;
	}

	protected void despawnNpc(Npc npc) {
		if (npc != null) {
			npc.getController().onDelete();
		}
	}

	protected void despawnNpcs(List<Npc> npcs) {
		for (Npc npc : npcs) {
			npc.getController().onDelete();
		}
	}

	protected void teleport(Player player, float x, float y, float z, byte h) {
		TeleportService2.teleportTo(player, mapId, instanceId, x, y, z, h);
	}

	@Override
	public StageType getStage() {
		return stageType;
	}

	@Override
	public boolean onReviveEvent(Player player) {
		PlayerReviveService.revive(player, 100, 100, false, 0);
		player.getGameStats().updateStatsAndSpeedVisually();
		return true;
	}

	@Override
	public void onInstanceDestroy() {
		isInstanceDestroyed = true;
		instanceReward.clear();
	}
}
