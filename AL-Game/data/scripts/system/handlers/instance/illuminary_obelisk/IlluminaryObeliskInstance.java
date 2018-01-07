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
package instance.illuminary_obelisk;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.ai2.manager.WalkManager;
import com.aionemu.gameserver.controllers.effect.PlayerEffectController;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.StaticDoor;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUEST_ACTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;
import com.aionemu.gameserver.world.zone.ZoneInstance;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author Alcapwnd
 * @rework Blackfire
 */
@InstanceID(301230000)
public class IlluminaryObeliskInstance extends GeneralInstanceHandler {

	@SuppressWarnings("unused")
	private long startTime;
	@SuppressWarnings("unused")
	private int beritraKilled;
	private boolean isStop;
	private boolean isStop1;
	private boolean isEnd;
	private Future<?> TWDTask;
	private Future<?> TWD1Task;
	private Future<?> TWD2Task;
	private Future<?> GENTask;
	private Future<?> CNT1Task;
	private Future<?> CNT2Task;
	private Future<?> CNT3Task;
	private Future<?> CNT4Task;
	private Future<?> CNT5Task;
	private Future<?> CNT6Task;
	private Future<?> CNT7Task;
	private Future<?> SP1Task;
	private Future<?> SP2Task;
	private Future<?> SP3Task;
	private Future<?> SP4Task;
	private Future<?> CHKTask;
	private Future<?> instanceTimer;
	private Map<Integer, StaticDoor> doors;
	protected boolean isInstanceDestroyed = false;
	private List<Integer> movies = new ArrayList<Integer>();

	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
		doors = instance.getDoors();
	}

	@Override
	public void onEnterInstance(final Player player) {
		super.onInstanceCreate(instance);
		if (instanceTimer == null) {
			startTime = System.currentTimeMillis();
			instanceTimer = ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					sendMsg(1402193);
					openFirstDoors();
				}
			}, 60000);
		}
		switch (player.getRace()) {
			case ELYOS:
				sendMovie(player, 894);
				break;
			case ASMODIANS:
				sendMovie(player, 895);
				break;
			default:
				break;
		}
	}

	/**
	 * Defense Cannons: Each Shield Unit has a defense cannon that can be used. This cannons do powerful wide area damage attacks. In order to use them you need to have Bomb items. When a shield is
	 * charged completely a cannon will spawn to help in the defense of the area. Determining a person to use the cannon and positioning before the mobs come is a recommended. Bombs to use the cannons
	 * appear in chests around the instance in a different place every time, collect them too.
	 */
	@Override
	public void handleUseItemFinish(Player player, Npc npc) {
		switch (npc.getNpcId()) {
			case 702009: // Danuar Cannon.
			case 702021: // Danuar Cannon.
			case 702022: // Danuar Cannon.
			case 702023: // Danuar Cannon.
				despawnNpc(npc);
				SkillEngine.getInstance().getSkill(npc, 21511, 60, player).useNoAnimationSkill();
				break;
		}
	}

	/**
	 * If a "Shield" is destroyed, you must start again from the 1st phase You can heal the shield with a restoration skill.
	 */
	@Override
	public void onDie(Npc npc) {
		Player player = npc.getAggroList().getMostPlayerDamage();
		switch (npc.getObjectTemplate().getTemplateId()) {
			case 702010: // Eastern Shield Generator.
				despawnNpc(npc);
				deleteNpc(233720);
				deleteNpc(233721);
				deleteNpc(233728);
				deleteNpc(233729);
				deleteNpc(233730);
				deleteNpc(233732);
				deleteNpc(233733);
				deleteNpc(233734);
				deleteNpc(233735);
				deleteNpc(233736);
				deleteNpc(233737);
				deleteNpc(233738);
				deleteNpc(233739);
				deleteNpc(702014);
				deleteNpc(702218);
				deleteNpc(702219);
				deleteNpc(702220);
				isEnd = true;
				SP1Task.cancel(true);
				instance.doOnAllPlayers(new Visitor<Player>() {

					@Override
					public void visit(Player player) {
						PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1402139));
					}
				});
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						spawn(702010, 255.47392f, 293.56177f, 321.18497f, (byte) 89); // Eastern Shield Generator.
						isEnd = false;
					}
				}, 10000);
				break;
			case 702011: // Western Shield Generator.
				despawnNpc(npc);
				deleteNpc(233722);
				deleteNpc(233723);
				deleteNpc(233728);
				deleteNpc(233729);
				deleteNpc(233730);
				deleteNpc(233732);
				deleteNpc(233733);
				deleteNpc(233734);
				deleteNpc(233735);
				deleteNpc(233736);
				deleteNpc(233737);
				deleteNpc(233738);
				deleteNpc(233739);
				deleteNpc(702015);
				deleteNpc(702221);
				deleteNpc(702222);
				deleteNpc(702223);
				SP2Task.cancel(true);
				isEnd = true;
				instance.doOnAllPlayers(new Visitor<Player>() {

					@Override
					public void visit(Player player) {
						PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1402140));
					}
				});
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						spawn(702011, 255.55742f, 216.03549f, 321.21344f, (byte) 30); // Western Shield Generator.
						isEnd = false;
					}
				}, 10000);
				break;
			case 702012: // Southern Shield Generator.
				despawnNpc(npc);
				deleteNpc(233724);
				deleteNpc(233725);
				deleteNpc(233728);
				deleteNpc(233729);
				deleteNpc(233730);
				deleteNpc(233732);
				deleteNpc(233733);
				deleteNpc(233734);
				deleteNpc(233735);
				deleteNpc(233736);
				deleteNpc(233737);
				deleteNpc(233738);
				deleteNpc(233739);
				deleteNpc(702016);
				deleteNpc(702224);
				deleteNpc(702225);
				deleteNpc(702226);
				SP3Task.cancel(true);
				isEnd = true;
				instance.doOnAllPlayers(new Visitor<Player>() {

					@Override
					public void visit(Player player) {
						PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1402141));
					}
				});
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						spawn(702012, 294.20718f, 254.60352f, 295.7729f, (byte) 60); // Southern Shield Generator.
						isEnd = false;
					}
				}, 10000);
				break;
			case 702013: // Northern Shield Generator.
				despawnNpc(npc);
				deleteNpc(233726);
				deleteNpc(233727);
				deleteNpc(233728);
				deleteNpc(233729);
				deleteNpc(233730);
				deleteNpc(233732);
				deleteNpc(233733);
				deleteNpc(233734);
				deleteNpc(233735);
				deleteNpc(233736);
				deleteNpc(233737);
				deleteNpc(233738);
				deleteNpc(233739);
				deleteNpc(702016);
				deleteNpc(702227);
				deleteNpc(702228);
				deleteNpc(702229);
				SP4Task.cancel(true);
				isEnd = true;
				instance.doOnAllPlayers(new Visitor<Player>() {

					@Override
					public void visit(Player player) {
						PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1402142));
					}
				});
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						spawn(702013, 216.97739f, 254.4616f, 295.77353f, (byte) 0); // Northern Shield Generator.
						isEnd = false;
					}
				}, 10000);
				break;
			/****************************
			 * Eastern Shield Generator *
			 ****************************/
			case 233720:
			case 233721:
			case 233881:
				despawnNpc(npc);
				if (getNpcs(233720).isEmpty() && getNpcs(233721).isEmpty() && getNpcs(233881).isEmpty()) {
					spawncheckE();
				}
				break;
			case 233728:
			case 233729:
			case 233730:
			case 233731:
			case 233732:
			case 233733:
			case 233734:
			case 233735:
			case 233736:
			case 233737:
			case 233738:
			case 233739:
				despawnNpc(npc);
				break;
			/****************************
			 * Western Shield Generator *
			 ****************************/
			case 233722:
			case 233723:
			case 233882:
				despawnNpc(npc);
				if (getNpcs(233722).isEmpty() && getNpcs(233723).isEmpty() && getNpcs(233882).isEmpty()) {
					spawncheckW();
				}
				break;

			/*****************************
			 * Southern Shield Generator *
			 *****************************/
			case 233724:
			case 233725:
			case 233883:
				despawnNpc(npc);
				if (getNpcs(233724).isEmpty() && getNpcs(233725).isEmpty() && getNpcs(233883).isEmpty()) {
					spawncheckS();
				}
				break;

			/*****************************
			 * Northern Shield Generator *
			 *****************************/
			case 233726:
			case 233727:
			case 233857:
				despawnNpc(npc);
				if (getNpcs(233726).isEmpty() && getNpcs(233727).isEmpty() && getNpcs(233857).isEmpty()) {
					spawncheckN();
				}
				break;

			/**
			 * Final Boss.
			 */
			case 233740: // Test Weapon Dynatoum.
				TWDTask.cancel(true);
				TWD1Task.cancel(true);
				TWD2Task.cancel(true);
				PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 0));
				sendMsg("[Congratulation]: you finish <ILLUMINARY OBELISK>");
				spawn(702018, 258.888f, 251.14882f, 455.12198f, (byte) 105); // Supply Box.
				spawn(730905, 255.5597f, 254.49713f, 455.12012f, (byte) 104); // Illuminary Obelisk Exit.
				break;
		}
	}

	/**
	 * You have about 6 minutes to finish the boss, so all party members must be ready before activating the seal.
	 */
	@Override
	public void onEnterZone(Player player, ZoneInstance zone) {
		if (zone.getAreaTemplate().getZoneName() == ZoneName.get("SHIELD_GENERATION_HUB_301230000")) {
			if (!isStop) {
				isStop = true;
				sendMsg(1402143);
				TWD1Task = ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						instance.doOnAllPlayers(new Visitor<Player>() {

							@Override
							public void visit(Player player) {
								sendMsg(1402144);
							}
						});
					}
				}, 60000); // 1 Minutes.

				TWD2Task = ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						instance.doOnAllPlayers(new Visitor<Player>() {

							@Override
							public void visit(Player player) {
								sendMsg(1402145);
							}
						});
					}
				}, 300000); // 5 Minutes.

				instance.doOnAllPlayers(new Visitor<Player>() {

					@Override
					public void visit(Player player) {
						if (player.isOnline()) {
							PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 360)); // 6 Minutes.
						}
					}
				});
				TWDTask = ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						instance.doOnAllPlayers(new Visitor<Player>() {

							@Override
							public void visit(Player player) {
								sendMsg(1402146);
								onExitInstance(player);
							}
						});
						onInstanceDestroy();
					}
				}, 360000); // 6 Minutes.

			}
		}
		else {
			if (zone.getAreaTemplate().getZoneName() == ZoneName.get("SZ_SECTOR_A_301230000") || zone.getAreaTemplate().getZoneName() == ZoneName.get("SZ_SECTOR_B_301230000") || zone.getAreaTemplate().getZoneName() == ZoneName.get("SZ_SECTOR_C_301230000") || zone.getAreaTemplate().getZoneName() == ZoneName.get("SZ_SECTOR_D_301230000")) {
				if (!isStop1) {
					chargecheck();
					sendMsg(1402129); // 30 msg.
					countdownmsg();
					isStop1 = true;
					GENTask = ThreadPoolManager.getInstance().schedule(new Runnable() {

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
					}, 1830000); // 30 Minutes and 30 second.
				}
			}
		}
	}

	/**
	 * *************************** Eastern Shield Generator Periodic Spawn * **************************
	 */
	private void startWaveEasternShieldGenerator1() {
		sp(233720, 252.68709f, 333.483f, 325.59268f, (byte) 90, 1000, "EasternShieldGenerator1");
		sp(233721, 255.74022f, 333.2762f, 325.49332f, (byte) 90, 1000, "EasternShieldGenerator2");
		sp(233881, 258.72256f, 333.27713f, 325.58722f, (byte) 90, 6000, "EasternShieldGenerator3");
	}

	/**
	 * *************************** Western Shield Generator Periodic Spawn * **************************
	 */
	private void startWaveWesternShieldGenerator1() {
		sp(233722, 258.37912f, 176.03621f, 325.59268f, (byte) 30, 1000, "WesternShieldGenerator1");
		sp(233723, 255.55922f, 176.17963f, 325.49332f, (byte) 29, 1000, "WesternShieldGenerator2");
		sp(233882, 252.49738f, 176.27466f, 325.52942f, (byte) 29, 1000, "WesternShieldGenerator3");
	}

	/**
	 * *************************** Southern Shield Generator Periodic Spawn * **************************
	 */
	private void startWaveSouthernShieldGenerator1() {
		sp(233724, 337.93338f, 257.88702f, 292.43845f, (byte) 60, 1000, "SouthernShieldGenerator1");
		sp(233725, 338.05304f, 254.6424f, 292.3325f, (byte) 60, 1000, "SouthernShieldGenerator2");
		sp(233883, 338.13315f, 251.34738f, 292.48932f, (byte) 59, 1000, "SouthernShieldGenerator3");
	}

	/**
	 * *************************** Northern Shield Generator Periodic Spawn * **************************
	 */

	private void startWaveNorthernShieldGenerator1() {
		sp(233726, 174.50981f, 251.38982f, 292.43088f, (byte) 0, 1000, "NorthernShieldGenerator1");
		sp(233727, 174.9973f, 254.4739f, 292.3325f, (byte) 0, 1000, "NorthernShieldGenerator2");
		sp(233857, 174.84029f, 257.80832f, 292.4389f, (byte) 0, 1000, "NorthernShieldGenerator3");
	}

	/**
	 * Starting periodic enemy spawn until all generator is fully charged East.
	 */
	public void spawncheckE() {
		if (!isEnd) {
			SP1Task = ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					startWaveEasternShieldGenerator1();
				}
			}, 30000 * 3); // spawn every 1 and a half minutes.
		}
	}

	/**
	 * Starting periodic enemy spawn until all generator is fully charged Western.
	 */
	public void spawncheckW() {
		if (!isEnd) {
			SP2Task = ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					startWaveWesternShieldGenerator1();
				}
			}, 30000 * 3); // spawn every 1 and a half minutes.
		}
	}

	/**
	 * Starting periodic enemy spawn until all generator is fully charged South.
	 */
	public void spawncheckS() {
		if (!isEnd) {
			SP3Task = ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					startWaveSouthernShieldGenerator1();
				}
			}, 30000 * 3); // spawn every 1 and a half minutes.
		}
	}

	/**
	 * Starting periodic enemy spawn until all generator is fully charged North.
	 */
	public void spawncheckN() {
		if (!isEnd) {
			SP4Task = ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					startWaveNorthernShieldGenerator1();
				}
			}, 30000 * 3); // spawn every 1 and a half minutes.
		}
	}

	/**
	 * Starting periodic enemy spawn until all generator is fully charged.
	 */
	public void chargecheck() {
		CHKTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				if (!getNpcs(702220).isEmpty() && !getNpcs(702223).isEmpty() && !getNpcs(702226).isEmpty() && !getNpcs(702229).isEmpty()) {
					spawnShieldControlRoomTeleporter();
					CHKTask.cancel(true);
				}
			}
		}, 20000, 20000);
	}

	public void countdownmsg() {
		CNT1Task = ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				sendMsg(1402130);
			}
		}, 60000 * 5); // 25 Minutes.

		CNT2Task = ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				sendMsg(1402131);
			}
		}, 60000 * 10); // 20 Minutes.

		CNT3Task = ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				sendMsg(1402132);
			}
		}, 60000 * 15); // 15 Minutes.

		CNT4Task = ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				sendMsg(1402133);
			}
		}, 60000 * 20); // 10 Minutes.

		CNT5Task = ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				sendMsg(1402134);
			}
		}, 60000 * 25); // 5 Minutes.

		CNT6Task = ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				sendMsg(1402235);
			}
		}, 60000 * 29); // 4 Minutes.

		CNT7Task = ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				sendMsg(1402236);
			}
		}, 60000 * 30); // 5 Minutes.
	}

	private void removeEffects(Player player) {
		PlayerEffectController effectController = player.getEffectController();
		effectController.removeEffect(0);
	}

	public void removeItems(Player player) {
		Storage storage = player.getInventory();
		storage.decreaseByItemId(164000289, storage.getItemCountByItemId(164000289));
		storage.decreaseByItemId(164000290, storage.getItemCountByItemId(164000290));
	}

	private void stopInstanceTask() {
		if (instanceTimer != null) {
			instanceTimer.cancel(true);
		}
	}

	/**
	 * ******************************* Shield Control Room Teleporter * ********************************
	 */
	private void spawnShieldControlRoomTeleporter() {
		sendMsg(1402202);
		deleteNpc(702010);
		deleteNpc(702011);
		deleteNpc(702012);
		deleteNpc(702013);
		spawn(730886, 255.47392f, 293.56177f, 321.18497f, (byte) 89);
		spawn(730886, 255.55742f, 216.03549f, 321.21344f, (byte) 30);
		spawn(730886, 294.20718f, 254.60352f, 295.7729f, (byte) 60);
		spawn(730886, 216.97739f, 254.4616f, 295.77353f, (byte) 0);
		GENTask.cancel(true);
		CNT1Task.cancel(true);
		CNT2Task.cancel(true);
		CNT3Task.cancel(true);
		CNT4Task.cancel(true);
		CNT5Task.cancel(true);
		CNT6Task.cancel(true);
		CNT7Task.cancel(true);
		SP1Task.cancel(true);
		SP2Task.cancel(true);
		SP3Task.cancel(true);
		SP4Task.cancel(true);
		isEnd = true;

	}

	protected void sp(final int npcId, final float x, final float y, final float z, final byte h, final int time, final String walkerId) {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (!isInstanceDestroyed) {
					Npc npc = (Npc) spawn(npcId, x, y, z, h);
					npc.getSpawn().setWalkerId(walkerId);
					WalkManager.startWalking((NpcAI2) npc.getAi2());
				}
			}
		}, time);
	}

	protected void openFirstDoors() {
		openDoor(129);
	}

	protected void openDoor(int doorId) {
		StaticDoor door = doors.get(doorId);
		if (door != null) {
			door.setOpen(true);
		}
	}

	private void sendMsg(final String str) {
		instance.doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				PacketSendUtility.sendMessage(player, str);
			}
		});
	}

	private void sendMovie(Player player, int movie) {
		if (!movies.contains(movie)) {
			movies.add(movie);
			PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, movie));
		}
	}

	@Override
	public void onPlayerLogOut(Player player) {
		removeItems(player);
		removeEffects(player);
		TeleportService2.teleportTo(player, 600060000, 1050, 1842, 366);
	}

	@Override
	public void onLeaveInstance(Player player) {
		removeItems(player);
		removeEffects(player);
		stopInstanceTask();
		PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 0)); // cancel timer
	}

	@Override
	public void onInstanceDestroy() {
		isInstanceDestroyed = true;
		doors.clear();
		movies.clear();
		stopInstanceTask();
	}

	@Override
	public void onExitInstance(Player player) {
		TeleportService2.moveToInstanceExit(player, mapId, player.getRace());
	}

	private void deleteNpc(int npcId) {
		if (getNpc(npcId) != null) {
			getNpc(npcId).getController().onDelete();
		}
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
		return TeleportService2.teleportTo(player, mapId, instanceId, 321.6408f, 323.9414f, 405.50763f, (byte) 75);
	}

	@Override
	public boolean onDie(final Player player, Creature lastAttacker) {
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);
		PacketSendUtility.sendPacket(player, new SM_DIE(player.haveSelfRezEffect(), player.haveSelfRezItem(), 0, 8));
		return true;
	}
}
