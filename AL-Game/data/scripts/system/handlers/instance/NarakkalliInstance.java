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
import java.util.Map;
import java.util.concurrent.Future;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.flyring.FlyRing;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.StaticDoor;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.flyring.FlyRingTemplate;
import com.aionemu.gameserver.model.utils3d.Point3D;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUEST_ACTION;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;

import javolution.util.FastList;

/**
 * @author Falke_34
 */

@InstanceID(302340000)
public class NarakkalliInstance extends GeneralInstanceHandler {

	private List<Integer> movies = new ArrayList<Integer>();
    private Map<Integer, StaticDoor> doors;
    private boolean isStartTimer = false;
    private final FastList<Future<?>> narakkalliTask = FastList.newInstance();

	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
		doors = instance.getDoors();
	}

	@Override
	public void onEnterInstance(Player player) {
        spawnNarakkalliRings();
	}

	@Override
	public void onDie(Npc npc) {
		switch (npc.getNpcId()) {
			case 246941:
                doors.get(224).setOpen(true);
				break;
			case 246944:
			case 246945:
				Npc guard1 = instance.getNpc(246944);
				Npc guard2 = instance.getNpc(246945);
				if (isDead(guard1) && isDead(guard2)) {
                    doors.get(225).setOpen(true);
				}
				break;
		}
	}

	@Override
	public boolean onPassFlyingRing(Player player, String flyingRing) {
		if (flyingRing.equals("CUSTOM_BRIDGE_OF_REINCARNATION_302340000")) {
			if (!isStartTimer) {
				isStartTimer = true;
				System.currentTimeMillis();
				instance.doOnAllPlayers(new Visitor<Player>() {

					@Override
					public void visit(Player player) {
						if (player.isOnline()) {
							startNarakkalliTimer();
							sendMovie(player, 957);
							PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 600));
						}
					}
				});
			}
		}
		return false;
	}

	private void spawnNarakkalliRings() {
		FlyRing f1 = new FlyRing(new FlyRingTemplate("CUSTOM_BRIDGE_OF_REINCARNATION_302340000", mapId, new Point3D(1164.909, 1152.0735, 491.07047), new Point3D(1164.909, 1152.0735, 497.07047), new Point3D(1170.8926, 1152.518, 491.07047), 10), instanceId);
		f1.spawn();
	}

	protected void startNarakkalliTimer() {
		narakkalliTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				instance.doOnAllPlayers(new Visitor<Player>() {

					@Override
					public void visit(Player player) {
						onExitInstance(player);
					}
				});
				onInstanceDestroy();
			}
		}, 900000)); // 15 Minutes
	}

	protected void stopNarakkalliTimer(Player player) {
		stopNarakkalliTask();
	}

	private void stopNarakkalliTask() {
		for (FastList.Node<Future<?>> n = narakkalliTask.head(), end = narakkalliTask.tail(); (n = n.getNext()) != end;) {
			if (n.getValue() != null) {
				n.getValue().cancel(true);
			}
		}
	}

	@Override
	public void onPlayMovieEnd(Player player, int movieId) {
		switch (movieId) {
			case 957:
				TeleportService2.teleportTo(player, mapId, 1183.2251f, 729.6886f, 433.22552f, (byte) 90);
				//add Skill-Buff to all Players : ID = 22682
				SkillEngine.getInstance().applyEffectDirectly(22682, player, player, 0);
				
				//Jetzt ist man im K�fig, welcher verschlossen ist.
				//NPC 806583 kommt die Treppe hoch bis vor das Tor und zerst�rt dieses.
				break;
		}
	}

	private void sendMovie(Player player, int movie) {
		if (!movies.contains(movie)) {
			movies.add(movie);
			PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, movie));
		}
	}

	private boolean isDead(Npc npc) {
		return (npc == null || npc.getLifeStats().isAlreadyDead());
	}

	@Override
	public void onInstanceDestroy() {
        stopNarakkalliTask();
		movies.clear();
		doors.clear();
	}

	@Override
	public void onLeaveInstance(Player player) {
        player.getEffectController().removeEffect(22682);
	}

	@Override
	public void onExitInstance(Player player) {
		TeleportService2.moveToInstanceExit(player, mapId, player.getRace());
	}

	@Override
	public boolean onDie(final Player player, Creature lastAttacker) {
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);
		PacketSendUtility.sendPacket(player, new SM_DIE(player.haveSelfRezEffect(), player.haveSelfRezItem(), 0, 8));
		return true;
	}
}
