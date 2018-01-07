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
import java.util.Set;
import java.util.concurrent.Future;

import javolution.util.FastList;

import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.ai2.manager.WalkManager;
import com.aionemu.gameserver.controllers.effect.PlayerEffectController;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.drop.DropItem;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.StaticDoor;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUEST_ACTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;

/**
 * @author
 */
@InstanceID(302330000)
public class KumukiHideoutInstance extends GeneralInstanceHandler {

	private int poppySaved;
	private long instanceTime;
	private boolean isInstanceDestroyed;
	private Map<Integer, StaticDoor> doors;
	private List<Npc> Poppy = new ArrayList<Npc>();
	private List<Integer> movies = new ArrayList<Integer>();
	private final FastList<Future<?>> kumukiCaveTask = FastList.newInstance();

	public void onDropRegistered(Npc npc) {
		Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		int npcId = npc.getNpcId();
		int index = dropItems.size() + 1;
		switch (npcId) {
			case 246294: // Key Chest
			case 246327: // Key Chest
			case 246328: // Suspicious Box
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000295, 1)); // Iron Fence Key
				break;
			case 246381: // Supplies Box
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 164002390, 1)); // Shabby Kumuki Transformation Scroll
				break;
			case 246377: // Kumuki Crate
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188056897, 1)); // Hansel's Gift Bundle
				break;
			case 246379: // Golden Treasure Chest
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188056994, 1)); // Golden Treasure Chest
				break;
		}
	}

	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
		doors = instance.getDoors();
	}

	@Override
	public void onEnterInstance(Player player) {
		if (movies.contains(951)) {
			return;
		}
		sendMovie(player, 951);
		Poppy.add((Npc) spawn(246279, 200.82152f, 307.74332f, 142.84671f, (byte) 0)); // First Poppy
		Poppy.add((Npc) spawn(246280, 202.79213f, 331.99738f, 142.84671f, (byte) 0)); // Second Poppy
		Poppy.add((Npc) spawn(246281, 243.97449f, 307.46100f, 142.84671f, (byte) 61)); // Third Poppy
		Poppy.add((Npc) spawn(246282, 243.63004f, 332.50742f, 142.84671f, (byte) 61)); // Fourth Poppy
	}

	protected void startInstanceTask() {
		instanceTime = System.currentTimeMillis();
		kumukiCaveTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				startKumukiCaveTimer();
				// You entered a strange Kumuki Cave
				sendMsgByRace(1403995, Race.PC_ALL, 0);
				// The rescue operation for Poppy is beginning
				sendMsgByRace(1403996, Race.PC_ALL, 10000);
				// Find all 4 keys before the Kumukis' dinner time starts and rescue the Poppies
				sendMsgByRace(1404020, Race.PC_ALL, 15000);
				// The desperate Kumuki Slaughterer is going after Poppy
				sendMsgByRace(1403991, Race.PC_ALL, 220000);
				instance.doOnAllPlayers(new Visitor<Player>() {

					@Override
					public void visit(Player player) {
						if (player.isOnline()) {
							PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 900)); // 15 Minutes
						}
					}
				});
			}
		}, 10000));
		kumukiCaveTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				// The first Poppy was caught and turned into barbecue. There are 3 Poppies left
				sendMsg(1404016);
				// The Kumukis turned Poppy into barbecue and ate him
				sendMsg(1403993);
				Poppy.get(0).getController().onDelete();
			}
		}, 225000));
		kumukiCaveTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				// The second Poppy was caught and turned into barbecue. There are 2 Poppies left
				sendMsg(1404017);
				// The Kumukis turned Poppy into barbecue and ate him
				sendMsg(1403993);
				Poppy.get(1).getController().onDelete();
				// Gretel has appeared. Ask her for help if you want to get to the Kumuki base faster
				sendMsg(1404023);
				sp(835130, 142.37743f, 19.93851f, 144.2455f, (byte) 5, 0, 0, null);
			}
		}, 450000));
		kumukiCaveTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				// The third Poppy was caught and turned into barbecue. There is 1 Poppy left
				sendMsg(1404018);
				// The Kumukis turned Poppy into barbecue and ate him
				sendMsg(1403993);
				Poppy.get(2).getController().onDelete();
			}
		}, 675000));
		kumukiCaveTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				instance.doOnAllPlayers(new Visitor<Player>() {

					@Override
					public void visit(Player player) {
						stopInstance1(player);
						// The Kumukis turned all Poppies into barbecue
						sendMsg(1404019);
						Poppy.get(3).getController().onDelete();
					}
				});
			}
		}, 900000)); // 15 Minutes.
	}

	private void startKumukiCaveTimer() {
		// 15 minutes until dinner time for the Kumukis
		sendMsgByRace(1404013, Race.PC_ALL, 20000);
		// 10 minutes until dinner time for the Kumukis
		this.sendMessage(1404007, 5 * 60 * 1000);
		// The Kumukis' dinner time is approaching
		this.sendMessage(1403992, 8 * 60 * 1000);
		// 5 minutes until dinner time for the Kumukis
		this.sendMessage(1404008, 10 * 60 * 1000);
		// 3 minutes until dinner time for the Kumukis
		this.sendMessage(1404009, 12 * 60 * 1000);
		// 2 minutes until dinner time for the Kumukis
		this.sendMessage(1404010, 13 * 60 * 1000);
		// 1 minute until dinner time for the Kumukis
		this.sendMessage(1404011, 14 * 60 * 1000);
	}

	@Override
	public void onDie(Npc npc) {
		Player player = npc.getAggroList().getMostPlayerDamage();
		switch (npc.getObjectTemplate().getTemplateId()) {
			case 246293: // Nutritious Ginseng
                despawnNpc(npc);
				break;
			case 246326: // Nutritious Ginseng
				despawnNpc(npc);
				break;
			case 246298: // Gatekeeper Nukaki
				despawnNpc(npc);
				// The Kumuki Seeker has appeared. Use the Fear Grenade to overpower him
				sendMsgByRace(1404043, Race.PC_ALL, 0);
				// The Kumuki Slaughterers have appeared. Use the Fart Grenade to overpower them
				sendMsgByRace(1404044, Race.PC_ALL, 10000);
				spawn(246305, 223.39684f, 288.30096f, 143.59119f, (byte) 30); // Cook Bakaki
				break;
			case 246305: // Cook Bakaki
				despawnNpc(npc);
				stopInstance2(player);
				if (poppySaved == 0) {
					spawn(835057, 223.93062f, 337.54870f, 142.43079f, (byte) 90); // Kumuki Cave Exit
				}
				else if (poppySaved == 1) {
					spawn(835057, 223.93062f, 337.54870f, 142.43079f, (byte) 90); // Kumuki Cave Exit
					spawn(246379, 224.45757f, 291.19736f, 145.50471f, (byte) 0); // Golden Treasure Chest
				}
				else if (poppySaved == 2) {
					spawn(835057, 223.93062f, 337.54870f, 142.43079f, (byte) 90); // Kumuki Cave Exit
					spawn(246379, 222.14935f, 291.14789f, 145.89087f, (byte) 0); // Golden Treasure Chest
					spawn(246379, 224.45757f, 291.19736f, 145.50471f, (byte) 0); // Golden Treasure Chest
				}
				else if (poppySaved == 3) {
					spawn(835057, 223.93062f, 337.54870f, 142.43079f, (byte) 90); // Kumuki Cave Exit
					spawn(246379, 222.14935f, 291.14789f, 145.89087f, (byte) 0); // Golden Treasure Chest
					spawn(246379, 223.33165f, 288.37637f, 145.89087f, (byte) 0); // Golden Treasure Chest
					spawn(246379, 224.45757f, 291.19736f, 145.50471f, (byte) 0); // Golden Treasure Chest
				}
				else if (poppySaved == 4) {
					spawn(835057, 223.93062f, 337.54870f, 142.43079f, (byte) 90); // Kumuki Cave Exit
					spawn(246377, 223.25879f, 293.23462f, 143.59119f, (byte) 30); // Kumuki Crate
					spawn(246379, 225.70757f, 288.44736f, 145.89087f, (byte) 0); // Golden Treasure Chest
					spawn(246379, 220.98096f, 288.33063f, 145.89087f, (byte) 0); // Golden Treasure Chest
					spawn(246379, 222.14935f, 291.14789f, 145.89087f, (byte) 0); // Golden Treasure Chest
					spawn(246379, 223.33165f, 288.37637f, 145.89087f, (byte) 0); // Golden Treasure Chest
					spawn(246379, 224.45757f, 291.19736f, 145.50471f, (byte) 0); // Golden Treasure Chest
				}
				instance.doOnAllPlayers(new Visitor<Player>() {

					@Override
					public void visit(Player player) {
						if (player.isOnline()) {
							PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 0));
						}
					}
				});
				break;
		}
	}

	@Override
	public void handleUseItemFinish(Player player, Npc npc) {
		switch (npc.getNpcId()) {
			case 703424: // Locked Iron Fence
				if (player.getInventory().decreaseByItemId(185000295, 1)) { // Iron Fence Key
					poppySaved++;
					despawnNpc(npc);
				}
				else {
					// Key required
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1403686));
				}
				break;
			case 703425: // 2. Door Activator
				doors.get(3).setOpen(true);
				break;
			case 703426: // 3. Door Activator
				doors.get(7).setOpen(true);
				break;
			case 703427: // Door Activator
				doors.get(2).setOpen(true);
				break;
			case 703428: // 1. Door Activator
				doors.get(19).setOpen(true);
				startInstanceTask();
				SkillEngine.getInstance().applyEffectDirectly(17619, player, player, 900000 * 1); // Shabby Kumuki Transformation
				break;
			case 835026: // Suspicious Wagon
				SkillEngine.getInstance().getSkill(npc, 16973, 60, player).useNoAnimationSkill(); // Riding A Wagon.
				break;
			case 835028: // Suspicious Basket
				SkillEngine.getInstance().getSkill(npc, 16974, 60, player).useNoAnimationSkill(); // In Basket Camouflage
				break;
			case 835071: // Suspicious Ginseng Snack
				SkillEngine.getInstance().applyEffectDirectly(17623, player, player, 4000 * 1); // Ginseng Transformation
				break;
		}
	}

	protected void stopInstance1(Player player) {
		stopInstanceTask();
		onInstanceDestroy();
		sendMsg("You have not been able to save all <Poppy>");
		onExitInstance(player);
	}

	protected void stopInstance2(Player player) {
		stopInstanceTask();
		onInstanceDestroy();
		sendMsg("You managed to save all <Poppy>");
	}

	private void deleteNpc(int npcId) {
		if (getNpc(npcId) != null) {
			getNpc(npcId).getController().onDelete();
		}
	}

	private void despawnNpc(Npc npc) {
		if (npc != null) {
			npc.getController().onDelete();
		}
	}

	private void stopInstanceTask() {
		for (FastList.Node<Future<?>> n = kumukiCaveTask.head(), end = kumukiCaveTask.tail(); (n = n.getNext()) != end;) {
			if (n.getValue() != null) {
				n.getValue().cancel(true);
			}
		}
	}

	protected void sp(final int npcId, final float x, final float y, final float z, final byte h, final int time) {
		sp(npcId, x, y, z, h, 0, time, 0, null);
	}

	protected void sp(final int npcId, final float x, final float y, final float z, final byte h, final int time, final int msg,
		final Race race) {
		sp(npcId, x, y, z, h, 0, time, msg, race);
	}

	protected void sp(final int npcId, final float x, final float y, final float z, final byte h, final int entityId, final int time,
		final int msg, final Race race) {
		kumukiCaveTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (!isInstanceDestroyed) {
					spawn(npcId, x, y, z, h, entityId);
					if (msg > 0) {
						sendMsgByRace(msg, race, 0);
					}
				}
			}
		}, time));
	}

	protected void sp(final int npcId, final float x, final float y, final float z, final byte h, final int time, final String walkerId) {
		kumukiCaveTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (!isInstanceDestroyed) {
					Npc npc = (Npc) spawn(npcId, x, y, z, h);
					npc.getSpawn().setWalkerId(walkerId);
					WalkManager.startWalking((NpcAI2) npc.getAi2());
				}
			}
		}, time));
	}

	private void sendMsg(final String str) {
		instance.doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				PacketSendUtility.sendWhiteMessageOnCenter(player, str);
			}
		});
	}

	private void sendMessage(final int msgId, long delay) {
		if (delay == 0) {
			this.sendMsg(msgId);
		}
		else {
			ThreadPoolManager.getInstance().schedule(new Runnable() {

				public void run() {
					sendMsg(msgId);
				}
			}, delay);
		}
	}

	protected void sendMsgByRace(final int msg, final Race race, int time) {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				instance.doOnAllPlayers(new Visitor<Player>() {

					@Override
					public void visit(Player player) {
						if (player.getRace().equals(race) || race.equals(Race.PC_ALL)) {
							PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(msg));
						}
					}
				});
			}
		}, time);
	}

	@Override
	public boolean onReviveEvent(Player player) {
		player.getGameStats().updateStatsAndSpeedVisually();
		PlayerReviveService.revive(player, 100, 100, false, 0);
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
		PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_INSTANT_DUNGEON_RESURRECT, 0, 0));
		return TeleportService2.teleportTo(player, mapId, instanceId, 172.00f, 20.00f, 144.22f, (byte) 0);
	}

	@Override
	public boolean onDie(final Player player, Creature lastAttacker) {
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);
		PacketSendUtility.sendPacket(player, new SM_DIE(player.haveSelfRezEffect(), player.haveSelfRezItem(), 0, 8));
		return true;
	}

	@Override
	public void onLeaveInstance(Player player) {
		removeItems(player);
		removeEffects(player);
	}

	@Override
	public void onPlayerLogOut(Player player) {
		removeItems(player);
		removeEffects(player);
	}

	private void sendMovie(Player player, int movie) {
		if (!movies.contains(movie)) {
			movies.add(movie);
			PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, movie));
		}
	}

	public void onExitInstance(Player player) {
		TeleportService2.moveToInstanceExit(player, mapId, player.getRace());
	}

	private int getTime() {
		long result = System.currentTimeMillis() - instanceTime;
		if (result < 10000) {
			return (int) (10000 - result);
		}
		else if (result < 900000) { // 15 Minutes
			return (int) (900000 - (result - 10000));
		}
		return 0;
	}

	private void removeItems(Player player) {
		Storage storage = player.getInventory();
		storage.decreaseByItemId(185000295, storage.getItemCountByItemId(185000295)); // Iron Fence Key
		storage.decreaseByItemId(185000296, storage.getItemCountByItemId(185000296)); // Kumuki Crate Key
		storage.decreaseByItemId(186000459, storage.getItemCountByItemId(186000459)); // Golden Treasure Chest Key
		storage.decreaseByItemId(164002390, storage.getItemCountByItemId(164002390)); // Shabby Kumuki Transformation Scroll
	}

	private void removeEffects(Player player) {
		PlayerEffectController effectController = player.getEffectController();
		effectController.removeEffect(16973); // Riding A Wagon
		effectController.removeEffect(16974); // In Basket Camouflage
		effectController.removeEffect(17619); // Shabby Kumuki Transformation
		effectController.removeEffect(17623); // Ginseng Transformation
	}

	@Override
	public void onInstanceDestroy() {
		isInstanceDestroyed = true;
		stopInstanceTask();
		movies.clear();
		doors.clear();
	}
}
