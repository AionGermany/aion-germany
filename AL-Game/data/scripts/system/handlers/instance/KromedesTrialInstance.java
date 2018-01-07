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

import java.util.ArrayList;
import java.util.List;

import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAYER_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_TRANSFORM;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.WorldMap;
import com.aionemu.gameserver.world.zone.ZoneInstance;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author xTz, Gigi
 */
@InstanceID(300230000)
public class KromedesTrialInstance extends GeneralInstanceHandler {

	private int skillId;
	private List<Integer> movies = new ArrayList<Integer>();
	private boolean isSpawned = false;

	@Override
	public void onEnterInstance(Player player) {
		if (movies.contains(453)) {
			return;
		}
		skillId = player.getRace() == Race.ASMODIANS ? 19270 : 19220;
		sendMovie(player, 453);
	}

	@Override
	public void onLeaveInstance(Player player) {
		player.getEffectController().removeEffect(skillId);
		removeItems(player);
	}

	private void removeItems(Player player) {
		Storage storage = player.getInventory();
		storage.decreaseByItemId(185000109, 1);
		storage.decreaseByItemId(185000098, 1);
		storage.decreaseByItemId(185000099, 1);
		storage.decreaseByItemId(185000100, 1);
		storage.decreaseByItemId(185000101, 1);
		storage.decreaseByItemId(185000102, 1);
	}

	@Override
	public void onPlayerLogOut(Player player) {
		player.setTransformed(false);
		PacketSendUtility.broadcastPacketAndReceive(player, new SM_TRANSFORM(player, false));
	}

	@Override
	public void onPlayMovieEnd(Player player, int movieId) {
		Storage storage = player.getInventory();
		switch (movieId) {
			case 453:
				SkillEngine.getInstance().applyEffectDirectly(skillId, player, player, 0);
				break;
			case 454:
				Npc npc1 = getNpc(730308);
				if (npc1 != null && MathUtil.isIn3dRange(player, npc1, 20)) {
					storage.decreaseByItemId(185000109, storage.getItemCountByItemId(185000109));
					TeleportService2.teleportTo(player, mapId, 687.56116f, 681.68225f, 200.28648f, (byte) 30);
				}
				break;
		}
	}

	@Override
	public void onEnterZone(Player player, ZoneInstance zone) {
		if (zone.getAreaTemplate().getZoneName() == ZoneName.get("MANOR_ENTRANCE_300230000")) {
			sendMovie(player, 462);
		}
		else if (zone.getAreaTemplate().getZoneName() == ZoneName.get("KALIGA_TREASURY_300230000")) {
			{
				if (!isSpawned) {
					isSpawned = true;
					Npc npc1 = getNpc(217002);
					Npc npc2 = getNpc(217000);
					Npc npc3 = getNpc(216982);
					if (isDead(npc1) && isDead(npc2) && isDead(npc3)) {
						spawn(217005, 669.214f, 774.387f, 216.88f, (byte) 60);
						spawn(217001, 663.8805f, 779.1967f, 216.26213f, (byte) 60);
						spawn(217003, 663.0468f, 774.6116f, 216.26215f, (byte) 60);
						spawn(217004, 663.0468f, 770.03815f, 216.26212f, (byte) 60);
					}
					else {
						spawn(217006, 669.214f, 774.387f, 216.88f, (byte) 60);
					}
				}
			}
		}
	}

	private boolean isDead(Npc npc) {
		return (npc == null || npc.getLifeStats().isAlreadyDead());
	}

	private void sendMovie(Player player, int movie) {
		if (!movies.contains(movie)) {
			movies.add(movie);
			PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, movie));
		}
	}

	@Override
	public void onInstanceDestroy() {
		movies.clear();
	}

	@Override
	public boolean onDie(final Player player, Creature lastAttacker) {
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);
		PacketSendUtility.sendPacket(player, new SM_DIE(player.haveSelfRezEffect(), player.haveSelfRezItem(), 0, 8));
		return true;
	}

	@Override
	public boolean onReviveEvent(Player player) {
		WorldMap map = World.getInstance().getWorldMap(player.getWorldId());
		if (map == null) {
			PlayerReviveService.bindRevive(player);
			return true;
		}
		PlayerReviveService.revive(player, 25, 25, true, 0);
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
		player.getGameStats().updateStatsAndSpeedVisually();
		PacketSendUtility.sendPacket(player, new SM_PLAYER_INFO(player, false));
		PacketSendUtility.sendPacket(player, new SM_MOTION(player.getObjectId(), player.getMotions().getActiveMotions()));
		TeleportService2.teleportTo(player, player.getWorldId(), 248, 244, 189);
		SkillEngine.getInstance().applyEffectDirectly(skillId, player, player, 0);
		player.unsetResPosState();
		return true;
	}
}
