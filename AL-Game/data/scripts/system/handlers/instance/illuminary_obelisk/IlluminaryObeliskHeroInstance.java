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
 */
@InstanceID(301370000)
public class IlluminaryObeliskHeroInstance extends GeneralInstanceHandler {

	private long startTime;
	private int beritraKilled;
	private Future<?> TWDTask;
	private Future<?> instanceTimer;
	private Map<Integer, StaticDoor> doors;
	protected boolean isInstanceDestroyed = false;
	private List<Integer> movies = new ArrayList<Integer>();
	boolean moviePlayed = false;
	private boolean isStartTimer = false;

	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
		doors = instance.getDoors();
	}

	@Override
	public void onEnterInstance(Player player) {
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
	}

	@Override
	public void handleUseItemFinish(Player player, Npc npc) {
		switch (npc.getNpcId()) {
			case 702009: // Danuar Cannon.
				despawnNpc(npc);
				SkillEngine.getInstance().getSkill(npc, 21511, 60, player).useNoAnimationSkill();
				break;
		}
	}

	@Override
	public void onDie(Npc npc) {
		// Player player = npc.getAggroList().getMostPlayerDamage();
		switch (npc.getObjectTemplate().getTemplateId()) {
			case 284851: // Beritra Ranger.
			case 284853: // Beritra Songweaver.
			case 284855: // Beritra Immobilizer.
			case 284857: // Beritra Healer.
				beritraKilled++;
				if (beritraKilled == 1) {
				}
				else if (beritraKilled == 2) {
				}
				else if (beritraKilled == 3) {
				}
				else if (beritraKilled == 4) {
					spawnShieldControlRoomTeleporter();
				}
				despawnNpc(npc);
				break;
			case 702010: // Eastern Shield Generator.
				despawnNpc(npc);
				deleteNpc(233723);
				deleteNpc(233724);
				deleteNpc(233725);
				deleteNpc(233734);
				deleteNpc(233735);
				deleteNpc(233857);
				deleteNpc(284841);
				deleteNpc(284842);
				deleteNpc(284843);
				deleteNpc(284850);
				deleteNpc(284851);
				deleteNpc(702218);
				deleteNpc(702219);
				deleteNpc(702220);
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
					}
				}, 10000);
				break;
			case 702011: // Western Shield Generator.
				despawnNpc(npc);
				deleteNpc(233726);
				deleteNpc(233727);
				deleteNpc(233728);
				deleteNpc(233736);
				deleteNpc(233737);
				deleteNpc(233858);
				deleteNpc(284844);
				deleteNpc(284845);
				deleteNpc(284846);
				deleteNpc(284852);
				deleteNpc(284853);
				deleteNpc(702221);
				deleteNpc(702222);
				deleteNpc(702223);
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
					}
				}, 10000);
				break;
			case 702012: // Southern Shield Generator.
				despawnNpc(npc);
				deleteNpc(233729);
				deleteNpc(233730);
				deleteNpc(233731);
				deleteNpc(233738);
				deleteNpc(233739);
				deleteNpc(233880);
				deleteNpc(284847);
				deleteNpc(284848);
				deleteNpc(284849);
				deleteNpc(284854);
				deleteNpc(284855);
				deleteNpc(702224);
				deleteNpc(702225);
				deleteNpc(702226);
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
					}
				}, 10000);
				break;
			case 702013: // Northern Shield Generator.
				despawnNpc(npc);
				deleteNpc(233720);
				deleteNpc(233721);
				deleteNpc(233722);
				deleteNpc(233732);
				deleteNpc(233733);
				deleteNpc(233881);
				deleteNpc(284838);
				deleteNpc(284839);
				deleteNpc(284840);
				deleteNpc(284856);
				deleteNpc(284857);
				deleteNpc(702227);
				deleteNpc(702228);
				deleteNpc(702229);
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
					}
				}, 10000);
				break;
			/****************************
			 * Eastern Shield Generator *
			 ****************************/
			case 233723:
			case 233724:
			case 233725:
				despawnNpc(npc);
				if (getNpcs(233723).isEmpty() && getNpcs(233724).isEmpty() && getNpcs(233725).isEmpty()) {
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							sendMsg(1402194);
							spawn(702218, 255.56438f, 297.59488f, 321.39154f, (byte) 29);
						}
					}, 2000);
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							startWaveEasternShieldGenerator2();
						}
					}, 8000);
				}
				break;
			case 284841:
			case 284842:
			case 284843:
				despawnNpc(npc);
				if (getNpcs(284841).isEmpty() && getNpcs(284842).isEmpty() && getNpcs(284843).isEmpty()) {
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							sendMsg(1402194);
							sendMsg(1402198);
							spawn(702219, 255.56438f, 297.59488f, 321.39154f, (byte) 29);
						}
					}, 2000);
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							startWaveEasternShieldGenerator3();
						}
					}, 8000);
				}
				break;
			case 233734:
			case 233735:
			case 233857:
			case 284850:
				despawnNpc(npc);
				if (getNpcs(233734).isEmpty() && getNpcs(233735).isEmpty() && getNpcs(233857).isEmpty() && getNpcs(284850).isEmpty()) {
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							sendMsg(1402194);
							spawn(702220, 255.56438f, 297.59488f, 321.39154f, (byte) 29);
						}
					}, 2000);
				}
				break;
			/****************************
			 * Western Shield Generator *
			 ****************************/
			case 233726:
			case 233727:
			case 233728:
				despawnNpc(npc);
				if (getNpcs(233726).isEmpty() && getNpcs(233727).isEmpty() && getNpcs(233728).isEmpty()) {
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							sendMsg(1402195);
							spawn(702221, 255.38777f, 212.00926f, 321.37292f, (byte) 90);
						}
					}, 2000);
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							startWaveWesternShieldGenerator2();
						}
					}, 8000);
				}
				break;
			case 284844:
			case 284845:
			case 284846:
				despawnNpc(npc);
				if (getNpcs(284844).isEmpty() && getNpcs(284845).isEmpty() && getNpcs(284846).isEmpty()) {
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							sendMsg(1402195);
							sendMsg(1402199);
							spawn(702222, 255.38777f, 212.00926f, 321.37292f, (byte) 90);
						}
					}, 2000);
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							startWaveWesternShieldGenerator3();
						}
					}, 8000);
				}
				break;
			case 233736:
			case 233737:
			case 233858:
			case 284852:
				despawnNpc(npc);
				if (getNpcs(233736).isEmpty() && getNpcs(233737).isEmpty() && getNpcs(233858).isEmpty() && getNpcs(284852).isEmpty()) {
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							sendMsg(1402195);
							spawn(702223, 255.38777f, 212.00926f, 321.37292f, (byte) 90);
						}
					}, 2000);
				}
				break;
			/*****************************
			 * Southern Shield Generator *
			 *****************************/
			case 233729:
			case 233730:
			case 233731:
				despawnNpc(npc);
				if (getNpcs(233729).isEmpty() && getNpcs(233730).isEmpty() && getNpcs(233731).isEmpty()) {
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							sendMsg(1402197);
							spawn(702224, 298.13452f, 254.48087f, 295.93027f, (byte) 119);
						}
					}, 2000);
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							startWaveSouthernShieldGenerator2();
						}
					}, 8000);
				}
				break;
			case 284847:
			case 284848:
			case 284849:
				despawnNpc(npc);
				if (getNpcs(284847).isEmpty() && getNpcs(284848).isEmpty() && getNpcs(284849).isEmpty()) {
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							sendMsg(1402196);
							sendMsg(1402200);
							spawn(702225, 298.13452f, 254.48087f, 295.93027f, (byte) 119);
						}
					}, 2000);
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							startWaveSouthernShieldGenerator3();
						}
					}, 8000);
				}
				break;
			case 233738:
			case 233739:
			case 233880:
			case 284854:
				despawnNpc(npc);
				if (getNpcs(233738).isEmpty() && getNpcs(233739).isEmpty() && getNpcs(233880).isEmpty() && getNpcs(284854).isEmpty()) {
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							sendMsg(1402196);
							spawn(702226, 298.13452f, 254.48087f, 295.93027f, (byte) 119);
						}
					}, 2000);
				}
				break;
			/*****************************
			 * Northern Shield Generator *
			 *****************************/
			case 233720:
			case 233721:
			case 233722:
				despawnNpc(npc);
				if (getNpcs(233720).isEmpty() && getNpcs(233721).isEmpty() && getNpcs(233722).isEmpty()) {
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							sendMsg(1402197);
							spawn(702227, 212.96484f, 254.4526f, 295.90784f, (byte) 60);
						}
					}, 2000);
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							startWaveNorthernShieldGenerator2();
						}
					}, 8000);
				}
				break;
			case 284838:
			case 284839:
			case 284840:
				despawnNpc(npc);
				if (getNpcs(284838).isEmpty() && getNpcs(284839).isEmpty() && getNpcs(284840).isEmpty()) {
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							sendMsg(1402197);
							sendMsg(1402201);
							spawn(702228, 212.96484f, 254.4526f, 295.90784f, (byte) 60);
						}
					}, 2000);
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							startWaveNorthernShieldGenerator3();
						}
					}, 8000);
				}
				break;
			case 233732:
			case 233733:
			case 233881:
			case 284856:
				despawnNpc(npc);
				if (getNpcs(233732).isEmpty() && getNpcs(233733).isEmpty() && getNpcs(233881).isEmpty() && getNpcs(284856).isEmpty()) {
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							sendMsg(1402197);
							spawn(702229, 212.96484f, 254.4526f, 295.90784f, (byte) 60);
						}
					}, 2000);
				}
				break;
			/**
			 * Final Boss.
			 */
			case 234686: // Test Weapon Dynatoum.
				TWDTask.cancel(true);
				sendMsg("\u041f\u043e\u0437\u0434\u0440\u0430\u0432\u043b\u044f\u0435\u043c! \u0412\u044b \u0441\u043f\u0440\u0430\u0432\u0438\u043b\u0438\u0441\u044c \u0441 \u0414\u0430\u0439\u043d\u0430\u0442\u0443\u043c\u043e\u043c!");
				spawn(730905, 240.2449f, 254.58f, 455.12012f, (byte) 0); // Illuminary Obelisk Exit.
				break;
		}
	}

	/**
	 * You have about 6 minutes to finish the boss, so all party members must be ready before activating the seal.
	 */
	@Override
	public void onEnterZone(Player player, ZoneInstance zone) {
		if (zone.getAreaTemplate().getZoneName() == ZoneName.get("SHIELD_GENERATION_HUB_301370000")) {
			instance.doOnAllPlayers(new Visitor<Player>() {

				@Override
				public void visit(final Player player) {
					if (player.isOnline()) {
						if (isStartTimer) {
							long time = System.currentTimeMillis() - startTime;
							if (time < 360000) {
								PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 360 - (int) time / 1000));
							}
						}
						if (!isStartTimer) {
							isStartTimer = true;
							System.currentTimeMillis();
							PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 360)); // 6 Minutes.
						}
						TWDTask = ThreadPoolManager.getInstance().schedule(new Runnable() {

							@Override
							public void run() {
								if (getNpc(234686) != null) {
									despawnNpc(getNpc(234686));
									spawn(730905, 240.2449f, 254.58f, 455.12012f, (byte) 0); // Illuminary Obelisk Exit.
									PacketSendUtility.sendMessage(player, "Time is ower, Instance Failed");
								}
							}
						}, 361000); // 6 Minutes.
					}
				}
			});
		}
		if (zone.getAreaTemplate().getZoneName() == ZoneName.get("INFERNAL_ILLUMINARY_OBELISK_301370000")) {
			if (!moviePlayed) {
				switch (player.getRace()) {
					case ELYOS:
						sendMovie(894);
						break;
					case ASMODIANS:
						sendMovie(895);
						break;
					default:
						break;
				}
			}
		}
	}

	private void removeEffects(Player player) {
		PlayerEffectController effectController = player.getEffectController();
		effectController.removeEffect(0);
	}

	private void startWaveEasternShieldGenerator2() {
		sp(284842, 252.68709f, 333.483f, 325.59268f, (byte) 90, 1000, "EasternShieldGenerator1");
		sp(284841, 255.74022f, 333.2762f, 325.49332f, (byte) 90, 1000, "EasternShieldGenerator2");
		sp(284843, 258.72256f, 333.27713f, 325.58722f, (byte) 90, 1000, "EasternShieldGenerator3");
	}

	private void startWaveEasternShieldGenerator3() {
		sp(233734, 252.68709f, 333.483f, 325.59268f, (byte) 90, 6000, "");
		sp(233735, 255.74022f, 333.2762f, 325.49332f, (byte) 90, 6000, "EasternShieldGenerator2");
		sp(284850, 258.72256f, 333.27713f, 325.58722f, (byte) 90, 6000, "EasternShieldGenerator3");
		sp(233857, 252.68709f, 333.483f, 325.59268f, (byte) 90, 23000, "EasternShieldGenerator1");
		sp(284851, 255.74022f, 333.2762f, 325.49332f, (byte) 90, 23000, "EasternShieldGenerator2");
		sp(233857, 258.72256f, 333.27713f, 325.58722f, (byte) 90, 23000, "EasternShieldGenerator3");
	}

	/****************************
	 * Western Shield Generator *
	 ****************************/
	private void startWaveWesternShieldGenerator2() {
		sp(284844, 258.37912f, 176.03621f, 325.59268f, (byte) 30, 1000, "WesternShieldGenerator1");
		sp(284845, 255.55922f, 176.17963f, 325.49332f, (byte) 29, 1000, "WesternShieldGenerator2");
		sp(284846, 252.49738f, 176.27466f, 325.52942f, (byte) 29, 1000, "WesternShieldGenerator3");
	}

	private void startWaveWesternShieldGenerator3() {
		sp(233736, 258.37912f, 176.03621f, 325.59268f, (byte) 30, 6000, "WesternShieldGenerator1");
		sp(233737, 255.55922f, 176.17963f, 325.49332f, (byte) 29, 6000, "WesternShieldGenerator2");
		sp(284852, 252.49738f, 176.27466f, 325.52942f, (byte) 29, 6000, "WesternShieldGenerator3");
		sp(233858, 258.37912f, 176.03621f, 325.59268f, (byte) 30, 23000, "WesternShieldGenerator1");
		sp(284853, 255.55922f, 176.17963f, 325.49332f, (byte) 29, 23000, "WesternShieldGenerator2");
		sp(233858, 252.49738f, 176.27466f, 325.52942f, (byte) 29, 23000, "WesternShieldGenerator3");
	}

	/*****************************
	 * Southern Shield Generator *
	 *****************************/
	private void startWaveSouthernShieldGenerator2() {
		sp(284847, 337.93338f, 257.88702f, 292.43845f, (byte) 60, 1000, "SouthernShieldGenerator1");
		sp(284848, 338.05304f, 254.6424f, 292.3325f, (byte) 60, 1000, "SouthernShieldGenerator2");
		sp(284849, 338.13315f, 251.34738f, 292.48932f, (byte) 59, 1000, "SouthernShieldGenerator3");
	}

	private void startWaveSouthernShieldGenerator3() {
		sp(233738, 337.93338f, 257.88702f, 292.43845f, (byte) 60, 6000, "SouthernShieldGenerator1");
		sp(233739, 338.05304f, 254.6424f, 292.3325f, (byte) 60, 6000, "SouthernShieldGenerator2");
		sp(284854, 338.13315f, 251.34738f, 292.48932f, (byte) 59, 6000, "SouthernShieldGenerator3");
		sp(233880, 337.93338f, 257.88702f, 292.43845f, (byte) 60, 23000, "SouthernShieldGenerator1");
		sp(284855, 338.05304f, 254.6424f, 292.3325f, (byte) 60, 23000, "SouthernShieldGenerator2");
		sp(233880, 338.13315f, 251.34738f, 292.48932f, (byte) 59, 23000, "SouthernShieldGenerator3");
	}

	/*****************************
	 * Northern Shield Generator *
	 *****************************/
	private void startWaveNorthernShieldGenerator2() {
		sp(284838, 174.50981f, 251.38982f, 292.43088f, (byte) 0, 1000, "NorthernShieldGenerator1");
		sp(284839, 174.9973f, 254.4739f, 292.3325f, (byte) 0, 1000, "NorthernShieldGenerator2");
		sp(284840, 174.84029f, 257.80832f, 292.4389f, (byte) 0, 1000, "NorthernShieldGenerator3");
	}

	private void startWaveNorthernShieldGenerator3() {
		sp(233732, 174.50981f, 251.38982f, 292.43088f, (byte) 0, 6000, "NorthernShieldGenerator1");
		sp(233733, 174.9973f, 254.4739f, 292.3325f, (byte) 0, 6000, "NorthernShieldGenerator2");
		sp(284856, 174.84029f, 257.80832f, 292.4389f, (byte) 0, 6000, "NorthernShieldGenerator3");
		sp(233881, 174.50981f, 251.38982f, 292.43088f, (byte) 0, 23000, "NorthernShieldGenerator1");
		sp(284857, 174.9973f, 254.4739f, 292.3325f, (byte) 0, 23000, "NorthernShieldGenerator2");
		sp(233881, 174.84029f, 257.80832f, 292.4389f, (byte) 0, 23000, "NorthernShieldGenerator3");
	}

	/**********************************
	 * Shield Control Room Teleporter *
	 **********************************/
	private void spawnShieldControlRoomTeleporter() {
		deleteNpc(702010);
		deleteNpc(702011);
		deleteNpc(702012);
		deleteNpc(702013);
		spawn(730886, 255.47392f, 293.56177f, 321.18497f, (byte) 89);
		spawn(730886, 255.55742f, 216.03549f, 321.21344f, (byte) 30);
		spawn(730886, 294.20718f, 254.60352f, 295.7729f, (byte) 60);
		spawn(730886, 216.97739f, 254.4616f, 295.77353f, (byte) 0);
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

	private void sendMovie(final int movie) {
		if (!movies.contains(movie)) {
			instance.doOnAllPlayers(new Visitor<Player>() {

				@Override
				public void visit(final Player player) {
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							if (!isInstanceDestroyed) {
								movies.add(movie);
								PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, movie));
								moviePlayed = true;
							}
						}
					}, 9000);
				}
			});
		}
	}

	@Override
	public void onPlayerLogOut(Player player) {
		removeEffects(player);
	}

	@Override
	public void onLeaveInstance(Player player) {
		removeEffects(player);
	}

	@Override
	public void onInstanceDestroy() {
		isInstanceDestroyed = true;
		doors.clear();
		movies.clear();
		moviePlayed = false;

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
		// PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 360 - (int) time / 1000));
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);
		PacketSendUtility.sendPacket(player, new SM_DIE(player.haveSelfRezEffect(), player.haveSelfRezItem(), 0, 8));
		return true;
	}
}
