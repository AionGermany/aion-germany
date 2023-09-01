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

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.controllers.effect.PlayerEffectController;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
//import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUEST_ACTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;

/**
 * @author Alcapwnd
 * @reworked Himiko
 */

@InstanceID(301340000)
public class LinkgateFoundryInstance extends GeneralInstanceHandler {

	private long startTime;
	protected boolean isInstanceDestroyed = false;
	boolean moviePlayed = false;
	private boolean isStartTimer = false;
	private boolean isElyos = false;
	private int mainTime = 0;
	public WorldMapInstance LinkgateFoundry;
	private List<Integer> movies = new ArrayList<Integer>();

	@Override
	public void onEnterInstance(Player player) {
		super.onInstanceCreate(instance);
		if (player.getRace() == Race.ELYOS) {
			isElyos = true;
			// sendMovie(player, 899);
			spawn(206361, 348.00464f, 252.13882f, 311.36136f, (byte) 10); // Ketesivius
			// spawn(855087, 226.8177f, 256.8576f, 312.37897f, (byte) 0); // For NPC 702339 -TODO-
		}
		else {
			isElyos = false;
			// sendMovie(player, 900);
			spawn(206362, 348.00464f, 252.13882f, 311.36136f, (byte) 10); // Aitu
			// spawn(855088, 226.8177f, 256.8576f, 312.37897f, (byte) 0); // For NPC 702339 -TODO-
		}
	}

	@Override
	public void onDie(Npc npc) {
		switch (npc.getNpcId()) {
			case 233898:
			case 234990:
			case 234991:
				if (isElyos) {
					spawn(702338, 225.60893f, 259.64015f, 312.62775f, (byte) 60);
				}
				else {
					spawn(702389, 225.60893f, 259.64015f, 312.62775f, (byte) 60);
				}
				break;
		}
	}

	@Override
	public void handleUseItemFinish(Player player, Npc npc) {
		switch (npc.getNpcId()) {
			case 234193:
				despawnNpc(npc);
				break;
			case 702590:
				TeleportService2.teleportTo(player, 301270000, 268.21646f, 321.59653f, 270.8809f, (byte) 60, TeleportAnimation.BEAM_ANIMATION);
				break;
			case 804629:
				TeleportService2.teleportTo(player, 301270000, 227.27151f, 263.95963f, 312.57867f, (byte) 115);
				break;
			case 804578:
				despawnNpc(npc);
				if (player.isOnline()) {
					LinkgateFoundry = instance;
					mainTime = 20;
					ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

						@Override
						public void run() {
							CheckMainTime();
						}
					}, 60 * 1000 * 1, 60 * 1000 * 1);
					if (isStartTimer) {
						long time = System.currentTimeMillis() - startTime;
						if (time < 1200000) {
							PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 1200 - (int) time / 1000));
						}
					}
					if (!isStartTimer) {
						isStartTimer = true;
						System.currentTimeMillis();
						PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 1200)); // 20 Minutes.
						PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1402453));
					}
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							if (getNpc(233887) != null) {
								despawnNpcs(instance.getNpcs(233887));
							}
							if (getNpc(233888) != null) {
								despawnNpcs(instance.getNpcs(233888));
							}
							if (getNpc(233889) != null) {
								despawnNpcs(instance.getNpcs(233889));
							}
							if (getNpc(233890) != null) {
								despawnNpcs(instance.getNpcs(233890));
							}
							if (getNpc(233891) != null) {
								despawnNpcs(instance.getNpcs(233891));
							}
							if (getNpc(233892) != null) {
								despawnNpcs(instance.getNpcs(233892));
							}
							if (getNpc(233893) != null) {
								despawnNpcs(instance.getNpcs(233893));
							}
							if (getNpc(233894) != null) {
								despawnNpcs(instance.getNpcs(233894));
							}
							if (getNpc(233895) != null) {
								despawnNpcs(instance.getNpcs(233895));
							}
							if (getNpc(233896) != null) {
								despawnNpcs(instance.getNpcs(233896));
							}
							if (getNpc(233897) != null) {
								despawnNpcs(instance.getNpcs(233897));
							}
						}
					}, 1201000);
				}
		}
	}

	private void CheckMainTime() {
		if (mainTime > 0) {
			mainTime -= 1;
			sendRemainTime(mainTime);
		}
	}

	private void sendRemainTime(int remainingTime) {
		switch (remainingTime) {
			case 15:
				sendMsg(1402454);
				break;
			case 10:
				sendMsg(1402455);
				break;
			case 5:
				sendMsg(1402456);
				break;
			case 3:
				sendMsg(1402457);
				break;
			case 1:
				sendMsg(1402458);
				break;
			case 0:
				sendMsg(1402461);
				break;
		}
	}

	private void removeEffects(Player player) {
		PlayerEffectController effectController = player.getEffectController();
		effectController.removeEffect(0);
	}

	// TODO ?
	// private void sendMovie(Player player, int movie) {
	// if (!movies.contains(movie)) {
	// movies.add(movie);
	// PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, movie));
	// }
	// }

	@Override
	public void onPlayerLogOut(Player player) {
		TeleportService2.moveToInstanceExit(player, mapId, player.getRace());
		removeEffects(player);
	}

	@Override
	public void onLeaveInstance(Player player) {
		PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 0)); // cancel timer
		removeEffects(player);
		onInstanceDestroy();
	}

	@Override
	public void onInstanceDestroy() {
		isInstanceDestroyed = true;
		movies.clear();
	}

	@Override
	public void onExitInstance(Player player) {
		TeleportService2.moveToInstanceExit(player, mapId, player.getRace());
		onInstanceDestroy();
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

	@Override
	protected Npc getNpc(int npcId) {
		if (!isInstanceDestroyed) {
			return instance.getNpc(npcId);
		}
		return null;
	}

	protected List<Npc> getNpcs(int npcId) {
		if (!isInstanceDestroyed) {
			return instance.getNpcs(npcId);
		}
		return null;
	}

	@Override
	public boolean onReviveEvent(Player player) {
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
		PlayerReviveService.revive(player, 100, 100, false, 0);
		player.getGameStats().updateStatsAndSpeedVisually();
		return TeleportService2.teleportTo(player, mapId, instanceId, 364.14f, 259.84f, 311.4f, (byte) 60);
	}

	@Override
	public boolean onDie(final Player player, Creature lastAttacker) {
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);
		PacketSendUtility.sendPacket(player, new SM_DIE(player.haveSelfRezEffect(), player.haveSelfRezItem(), 0, 8));
		return true;
	}
}
