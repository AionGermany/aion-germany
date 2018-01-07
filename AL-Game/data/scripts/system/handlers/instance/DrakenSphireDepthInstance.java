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

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.ai2.manager.WalkManager;
import com.aionemu.gameserver.controllers.effect.PlayerEffectController;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.drop.DropItem;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.StaticDoor;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.state.CreatureState;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.NpcShoutsService;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;

/**
 * @author Blackfire
 */

@InstanceID(301390000)
public class DrakenSphireDepthInstance extends GeneralInstanceHandler {

	private Npc boss1;
	private Npc boss2;
	private int firstBoss = 0;
	private int secondBoss = 0;
	private int race = 0;
	private int score = 0;
	private int beritraTim = 8;
	private boolean fail1;
	private float x = 0;
	private float y = 0;
	private float z = 0;
	private float h = 0;
	private boolean isStart;
	private boolean isRunning;
	private boolean firstStage;
	private boolean secondStage;
	private boolean beritraRun;
	private boolean stageFinal;
	private Future<?> wave1;
	private Future<?> wave2;
	private Future<?> wave3;
	private Future<?> wave4;
	private Future<?> wave5;
	private Future<?> wave6;
	private Future<?> wave7;
	private Future<?> wave8;
	private Future<?> wave9;
	private Future<?> periodicSpawn1;
	private Future<?> periodicSpawn2;
	private Future<?> periodicSpawn3;
	private Future<?> periodicSpawn4;
	private Future<?> periodicSpawn5;
	private Future<?> periodicSpawn6;
	private Future<?> periodicSpawn7;
	private Future<?> isEnd;
	private Future<?> pTimer;
	private Future<?> pChecker;
	private Future<?> bChecker;
	// private Future<?> cmdCheck;
	private Future<?> stageTimer;
	private Future<?> FlameTimer;
	private Future<?> FlameChecker;
	private Future<?> OrissanChecker;
	private Future<?> FirstStageChecker;
	private Future<?> SecondStageChecker;
	private Future<?> beritraTimers;
	private Future<?> insMsg;
	private Future<?> squad;
	private Future<?> bSummon;
	private Map<Integer, StaticDoor> doors;
	protected boolean isInstanceDestroyed = false;
	private List<Integer> movies = new ArrayList<Integer>();

	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
		doors = instance.getDoors();
	}

	@Override
	public void onDropRegistered(Npc npc) {
		Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		int npcId = npc.getNpcId();
		// int index = dropItems.size() + 1;
		switch (npcId) {
			case 236223: // Crossroads Choice Key.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000219, 1));
				break;
		}
	}

	@Override
	public void onEnterInstance(final Player player) {
		super.onInstanceCreate(instance);
		if (!isStart) {
			isStart = true;
			spawn(236227, 531.57263f, 212.31601f, 1681.8224f, (byte) 60);
			spawn(236228, 531.5915f, 151.92694f, 1681.8224f, (byte) 60);
			boss1 = getNpc(236227); // Lava Protector
			boss2 = getNpc(236228); // Heatvent Protector
			SkillEngine.getInstance().getSkill(boss1, 21641, 60, boss1).useSkill();
			SkillEngine.getInstance().getSkill(boss2, 21642, 60, boss2).useSkill();
			FlameChecker = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

				@Override
				public void run() {
					switch (player.getRace()) {
						case ELYOS:
							race = 1;
							twinBoss(player, boss1);
							twinBoss(player, boss2);
							break;
						case ASMODIANS:
							race = 2;
							twinBoss(player, boss1);
							twinBoss(player, boss2);
							break;
						default:
							break;
					}
				}
			}, 1000, 1000);

			FirstStageChecker = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

				@Override
				public void run() {
					Npc boss3 = getNpc(236229); // Orissan
					OrissanBoss(player, boss3);
				}
			}, 1000, 1000);

			SecondStageChecker = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

				@Override
				public void run() {
					if (secondStage) {
						instance.doOnAllPlayers(new Visitor<Player>() {

							@Override
							public void visit(Player object) {
								float pos = object.getY();
								if (pos > 844.23615) {
									waveStage();
									SecondStageChecker.cancel(true);
								}
							}
						});
					}
				}
			}, 1000, 1000);
		}
	}

	private void twinBoss(final Player player, Npc boss) {
		if (boss.getLifeStats().getHpPercentage() < 100) {
			if (FlameTimer == null) {
				FlameChecker.cancel(true);
				FlameTimer = ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						switch (player.getRace()) {
							case ELYOS:
								flameBossFailE();
								break;
							case ASMODIANS:
								flameBossFailA();
								break;
							default:
								break;
						}
					}
				}, 60000 * 5);
			}
		}
	}

	private void waveStage() {
		if (race == 1) {
			final Npc Guard = getNpc(209741);
			NpcShoutsService.getInstance().sendMsg(Guard, 1501318, Guard.getObjectId(), 0, 1000);
			wavePrepare(Guard);
		}
		if (race == 2) {
			final Npc Guard = getNpc(209806);
			NpcShoutsService.getInstance().sendMsg(Guard, 1501318, Guard.getObjectId(), 0, 1000);
			wavePrepare(Guard);
		}
	}

	private void wavePrepare(final Npc Guard) {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				SkillEngine.getInstance().getSkill(Guard, 20839, 60, Guard).useSkill();
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						bodyGuard();
						NpcShoutsService.getInstance().sendMsg(Guard, 1501319, Guard.getObjectId(), 0, 1000);
						ThreadPoolManager.getInstance().schedule(new Runnable() {

							@Override
							public void run() {
								bgRespawn();
								ThreadPoolManager.getInstance().schedule(new Runnable() {

									@Override
									public void run() {
										if (race == 1) {
											spawn(702719, 636.0202f, 892.09717f, 1600.5283f, (byte) 31); // Empyrean Lord's Cannon Ely
											startWave();
										}
										if (race == 2) {
											spawn(702720, 636.0202f, 892.09717f, 1600.5283f, (byte) 31); // Empyrean Lord's Cannon Asmo
											startWave();
										}
									}
								}, 5000);
							}
						}, 25000);
					}
				}, 3200);
			}
		}, 10000);
	}

	private void startWave() {
		score = firstBoss + secondBoss;
		spawn(731581, 635.38892f, 784.05261f, 1596.7184f, (byte) 30, 548);// wave start
		sp(236204, 635.63995f, 798.1231f, 1597.4143f, (byte) 29, 1000, "WAVEG11", true);
		sp(236205, 632.6976f, 796.1035f, 1597.4716f, (byte) 29, 1000, "WAVEG12", true);
		sp(236206, 637.5611f, 796.17017f, 1597.533f, (byte) 29, 1000, "WAVEG13", true);
		sp(236207, 634.8776f, 794.55853f, 1597.2604f, (byte) 29, 1000, "WAVEG11", true);
		if (score == 1) { // Start 3 waves only
			wave1 = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

				@Override
				public void run() {
					sp(236216, 635.63995f, 798.1231f, 1597.4143f, (byte) 29, 1000, "WAVEG11", true);
					sp(236205, 632.6976f, 796.1035f, 1597.4716f, (byte) 29, 1000, "WAVEG12", true);
					sp(236206, 637.5611f, 796.17017f, 1597.533f, (byte) 29, 1000, "WAVEG13", true);
					sp(236207, 634.8776f, 794.55853f, 1597.2604f, (byte) 29, 1000, "WAVEG11", true);
				}
			}, 60000, 60000);
			wave2 = ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					openDoor(267);
					spawn(731581, 578.54736f, 819.26752f, 1609.4229f, (byte) 13, 84);
					sp(236208, 587.562f, 830.03326f, 1608.1837f, (byte) 13, 1000, "WAVEG21", true);
					sp(236209, 584.42096f, 829.65265f, 1608.572f, (byte) 13, 1000, "WAVEG22", true);
					sp(236210, 587.26807f, 826.73224f, 1608.74f, (byte) 13, 1000, "WAVEG23", true);
					sp(236211, 585.0757f, 826.7777f, 1608.7599f, (byte) 13, 1000, "WAVEG21", true);
					periodicSpawn1 = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

						@Override
						public void run() {
							sp(236217, 587.562f, 830.03326f, 1608.1837f, (byte) 13, 1000, "WAVEG21", true);
							sp(236209, 584.42096f, 829.65265f, 1608.572f, (byte) 13, 1000, "WAVEG22", true);
							sp(236210, 587.26807f, 826.73224f, 1608.74f, (byte) 13, 1000, "WAVEG23", true);
							sp(236211, 585.0757f, 826.7777f, 1608.7599f, (byte) 13, 1000, "WAVEG21", true);
						}
					}, 120000, 120000);
				}
			}, 90000);
			wave3 = ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					openDoor(271);
					spawn(731581, 690.48749f, 822.40784f, 1609.5748f, (byte) 47, 405);
					sp(236212, 683.4146f, 829.0708f, 1608.3843f, (byte) 47, 1000, "WAVEG31", true);
					sp(236213, 682.9121f, 826.08295f, 1608.974f, (byte) 47, 1000, "WAVEG32", true);
					sp(236214, 685.7962f, 829.507f, 1608.9707f, (byte) 47, 1000, "WAVEG33", true);
					sp(236215, 685.98883f, 827.13403f, 1609.2852f, (byte) 47, 1000, "WAVEG31", true);
					periodicSpawn2 = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

						@Override
						public void run() {
							sp(236218, 683.4146f, 829.0708f, 1608.3843f, (byte) 47, 1000, "WAVEG31", true);
							sp(236213, 682.9121f, 826.08295f, 1608.974f, (byte) 47, 1000, "WAVEG32", true);
							sp(236214, 685.7962f, 829.507f, 1608.9707f, (byte) 47, 1000, "WAVEG33", true);
							sp(236215, 685.98883f, 827.13403f, 1609.2852f, (byte) 47, 1000, "WAVEG31", true);
						}
					}, 120000, 120000);
				}
			}, 30000 * 4);
			wave4 = ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					sp(236243, 635.63995f, 798.1231f, 1597.4143f, (byte) 29, 1000, "WAVEG11", true);
					sp(236205, 632.6976f, 796.1035f, 1597.4716f, (byte) 29, 1000, "WAVEG12", true);
					sp(236206, 637.5611f, 796.17017f, 1597.533f, (byte) 29, 1000, "WAVEG13", true);
					sp(236207, 634.8776f, 794.55853f, 1597.2604f, (byte) 29, 1000, "WAVEG11", true);
					wave1.cancel(true);
					periodicSpawn1.cancel(true);
					periodicSpawn2.cancel(true);
					periodicSpawn3.cancel(true);
					periodicSpawn4.cancel(true);
					periodicSpawn5.cancel(true);
					periodicSpawn6.cancel(true);
					periodicSpawn7.cancel(true);
				}
			}, 60000 * 4);
		}
		if (score == 0) { // Full waves
			wave1 = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

				@Override
				public void run() {
					sp(236216, 635.63995f, 798.1231f, 1597.4143f, (byte) 29, 1000, "WAVEG11", true);
					sp(236205, 632.6976f, 796.1035f, 1597.4716f, (byte) 29, 1000, "WAVEG12", true);
					sp(236206, 637.5611f, 796.17017f, 1597.533f, (byte) 29, 1000, "WAVEG13", true);
					sp(236207, 634.8776f, 794.55853f, 1597.2604f, (byte) 29, 1000, "WAVEG11", true);
				}
			}, 60000, 60000);
			wave2 = ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					openDoor(267);
					spawn(731581, 578.54736f, 819.26752f, 1609.4229f, (byte) 13, 84);
					sp(236208, 587.562f, 830.03326f, 1608.1837f, (byte) 13, 1000, "WAVEG21", true);
					sp(236209, 584.42096f, 829.65265f, 1608.572f, (byte) 13, 1000, "WAVEG22", true);
					sp(236210, 587.26807f, 826.73224f, 1608.74f, (byte) 13, 1000, "WAVEG23", true);
					sp(236211, 585.0757f, 826.7777f, 1608.7599f, (byte) 13, 1000, "WAVEG21", true);
					periodicSpawn1 = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

						@Override
						public void run() {
							sp(236217, 587.562f, 830.03326f, 1608.1837f, (byte) 13, 1000, "WAVEG21", true);
							sp(236209, 584.42096f, 829.65265f, 1608.572f, (byte) 13, 1000, "WAVEG22", true);
							sp(236210, 587.26807f, 826.73224f, 1608.74f, (byte) 13, 1000, "WAVEG23", true);
							sp(236211, 585.0757f, 826.7777f, 1608.7599f, (byte) 13, 1000, "WAVEG21", true);
						}
					}, 120000, 120000);
				}
			}, 90000);
			wave3 = ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					openDoor(271);
					spawn(731581, 690.48749f, 822.40784f, 1609.5748f, (byte) 47, 405);
					sp(236212, 683.4146f, 829.0708f, 1608.3843f, (byte) 47, 1000, "WAVEG31", true);
					sp(236213, 682.9121f, 826.08295f, 1608.974f, (byte) 47, 1000, "WAVEG32", true);
					sp(236214, 685.7962f, 829.507f, 1608.9707f, (byte) 47, 1000, "WAVEG33", true);
					sp(236215, 685.98883f, 827.13403f, 1609.2852f, (byte) 47, 1000, "WAVEG31", true);
					periodicSpawn2 = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

						@Override
						public void run() {
							sp(236218, 683.4146f, 829.0708f, 1608.3843f, (byte) 47, 1000, "WAVEG31", true);
							sp(236213, 682.9121f, 826.08295f, 1608.974f, (byte) 47, 1000, "WAVEG32", true);
							sp(236214, 685.7962f, 829.507f, 1608.9707f, (byte) 47, 1000, "WAVEG33", true);
							sp(236215, 685.98883f, 827.13403f, 1609.2852f, (byte) 47, 1000, "WAVEG31", true);
						}
					}, 120000, 120000);
				}
			}, 30000 * 4);
			wave4 = ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					openDoor(7);
					spawn(731581, 570.69678f, 877.50848f, 1599.8043f, (byte) 119, 401);
					sp(236212, 581.4584f, 877.7429f, 1600.8345f, (byte) 119, 1000, "WAVEG41", true);
					sp(236213, 579.1328f, 879.878f, 1600.7639f, (byte) 119, 1000, "WAVEG42", true);
					sp(236214, 578.9766f, 875.8135f, 1601.0686f, (byte) 119, 1000, "WAVEG43", true);
					sp(236215, 576.76953f, 877.85016f, 1600.878f, (byte) 119, 1000, "WAVEG41", true);
					periodicSpawn3 = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

						@Override
						public void run() {
							sp(236218, 683.4146f, 829.0708f, 1608.3843f, (byte) 47, 1000, "WAVEG41", true);
							sp(236213, 682.9121f, 826.08295f, 1608.974f, (byte) 47, 1000, "WAVEG42", true);
							sp(236214, 685.7962f, 829.507f, 1608.9707f, (byte) 47, 1000, "WAVEG43", true);
							sp(236215, 685.98883f, 827.13403f, 1609.2852f, (byte) 47, 1000, "WAVEG41", true);
						}
					}, 120000, 120000);
				}
			}, 60000 * 3);
			wave5 = ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					openDoor(310);
					spawn(731581, 707.3374f, 876.80182f, 1603.6915f, (byte) 60, 398);
					sp(236212, 687.50635f, 877.7851f, 1602.8757f, (byte) 60, 1000, "WAVEG51", true);
					sp(236213, 668.9602f, 875.3703f, 1603.049f, (byte) 60, 1000, "WAVEG52", true);
					sp(236214, 688.99817f, 879.7412f, 1603.0151f, (byte) 60, 1000, "WAVEG53", true);
					sp(236215, 691.3017f, 878.0749f, 1603.2634f, (byte) 60, 1000, "WAVEG51", true);
					periodicSpawn4 = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

						@Override
						public void run() {
							sp(236212, 687.50635f, 877.7851f, 1602.8757f, (byte) 60, 1000, "WAVEG51", true);
							sp(236213, 668.9602f, 875.3703f, 1603.049f, (byte) 60, 1000, "WAVEG52", true);
							sp(236214, 688.99817f, 879.7412f, 1603.0151f, (byte) 60, 1000, "WAVEG53", true);
							sp(236215, 691.3017f, 878.0749f, 1603.2634f, (byte) 60, 1000, "WAVEG51", true);
						}
					}, 120000, 120000);
				}
			}, 60000 * 4);
			wave6 = ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					openDoor(312);
					spawn(731581, 694.00665f, 935.9696f, 1618.0875f, (byte) 74, 399);
					sp(236212, 682.9437f, 924.86115f, 1615.049f, (byte) 74, 1000, "WAVEG71", true);
					sp(236213, 685.1894f, 925.1698f, 1615.67f, (byte) 74, 1000, "WAVEG72", true);
					sp(236214, 682.7655f, 927.7005f, 1615.7504f, (byte) 74, 1000, "WAVEG73", true);
					sp(236215, 685.0388f, 927.2815f, 1616.081f, (byte) 74, 1000, "WAVEG71", true);
					periodicSpawn5 = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

						@Override
						public void run() {
							sp(236212, 682.9437f, 924.86115f, 1615.049f, (byte) 74, 1000, "WAVEG71", true);
							sp(236213, 685.1894f, 925.1698f, 1615.67f, (byte) 74, 1000, "WAVEG72", true);
							sp(236214, 682.7655f, 927.7005f, 1615.7504f, (byte) 74, 1000, "WAVEG73", true);
							sp(236215, 685.0388f, 927.2815f, 1616.081f, (byte) 74, 1000, "WAVEG71", true);
						}
					}, 120000, 120000);
				}
			}, 60000 * 5);
			wave7 = ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					openDoor(210);
					spawn(731581, 572.9444f, 940.03577f, 1620.0454f, (byte) 104, 407);
					sp(236212, 582.92267f, 930.0109f, 1617.5431f, (byte) 104, 1000, "WAVEG81", true);
					sp(236213, 582.5963f, 932.8494f, 1618.3359f, (byte) 104, 1000, "WAVEG82", true);
					sp(236214, 579.66345f, 930.0382f, 1618.3992f, (byte) 104, 1000, "WAVEG83", true);
					sp(236215, 579.4339f, 933.2825f, 1619.0638f, (byte) 104, 1000, "WAVEG81", true);
					periodicSpawn6 = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

						@Override
						public void run() {
							sp(236212, 582.92267f, 930.0109f, 1617.5431f, (byte) 104, 1000, "WAVEG81", true);
							sp(236213, 582.5963f, 932.8494f, 1618.3359f, (byte) 104, 1000, "WAVEG82", true);
							sp(236214, 579.66345f, 930.0382f, 1618.3992f, (byte) 104, 1000, "WAVEG83", true);
							sp(236215, 579.4339f, 933.2825f, 1619.0638f, (byte) 104, 1000, "WAVEG81", true);
						}
					}, 120000, 120000);
				}
			}, 60000 * 6);
			wave9 = ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					sp(236243, 635.63995f, 798.1231f, 1597.4143f, (byte) 29, 1000, "WAVEG11", true);
					sp(236205, 632.6976f, 796.1035f, 1597.4716f, (byte) 29, 1000, "WAVEG12", true);
					sp(236206, 637.5611f, 796.17017f, 1597.533f, (byte) 29, 1000, "WAVEG13", true);
					sp(236207, 634.8776f, 794.55853f, 1597.2604f, (byte) 29, 1000, "WAVEG11", true);
					wave1.cancel(true);
					periodicSpawn1.cancel(true);
					periodicSpawn2.cancel(true);
					periodicSpawn3.cancel(true);
					periodicSpawn4.cancel(true);
					periodicSpawn5.cancel(true);
					periodicSpawn6.cancel(true);
					periodicSpawn7.cancel(true);
				}
			}, 60000 * 10);
		}

	}

	private void OrissanBoss(final Player player, final Npc boss) {
		if (boss.getLifeStats().getHpPercentage() < 100) {
			if (firstStage == true) {
				FirstStageChecker.cancel(true);
				SkillEngine.getInstance().getSkill(boss, 21885, 60, boss).useSkill();
				stageTimer = ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						x = boss.getX();
						y = boss.getY();
						z = boss.getZ();
						h = boss.getHeading();
						despawnNpc(boss);
						spawn(236230, x, y, z, (byte) h); // Immortal
						final Npc Immortal = getNpc(236230);
						SkillEngine.getInstance().getSkill(Immortal, 21634, 60, Immortal).useSkill();
						stageTimer = ThreadPoolManager.getInstance().schedule(new Runnable() {

							@Override
							public void run() {
								SkillEngine.getInstance().getSkill(Immortal, 21885, 60, Immortal).useSkill();
								stageTimer = ThreadPoolManager.getInstance().schedule(new Runnable() {

									@Override
									public void run() {
										final Npc Immortal = getNpc(236230);
										x = Immortal.getX();
										y = Immortal.getY();
										z = Immortal.getZ();
										h = Immortal.getHeading();
										despawnNpc(Immortal);
										spawn(236231, x, y, z, (byte) h); // Exhausted
										stageTimer = ThreadPoolManager.getInstance().schedule(new Runnable() {

											@Override
											public void run() {
												final Npc second = getNpc(236231);
												SkillEngine.getInstance().getSkill(second, 21885, 60, second).useSkill();
												stageTimer = ThreadPoolManager.getInstance().schedule(new Runnable() {

													@Override
													public void run() {
														x = second.getX();
														y = second.getY();
														z = second.getZ();
														h = second.getHeading();
														despawnNpc(second);
														spawn(236230, x, y, z, (byte) h); // Immortal
														final Npc Immortal = getNpc(236230);
														SkillEngine.getInstance().getSkill(Immortal, 21634, 60, Immortal).useSkill();
														stageTimer = ThreadPoolManager.getInstance().schedule(new Runnable() {

															@Override
															public void run() {
																SkillEngine.getInstance().getSkill(Immortal, 21885, 60, Immortal).useSkill();
																stageTimer = ThreadPoolManager.getInstance().schedule(new Runnable() {

																	@Override
																	public void run() {
																		final Npc Immortal = getNpc(236230);
																		x = Immortal.getX();
																		y = Immortal.getY();
																		z = Immortal.getZ();
																		h = Immortal.getHeading();
																		despawnNpc(Immortal);
																		spawn(236231, x, y, z, (byte) h); // Exhausted
																		stageTimer = ThreadPoolManager.getInstance().schedule(new Runnable() {

																			@Override
																			public void run() {
																				final Npc fourth = getNpc(236231);
																				SkillEngine.getInstance().getSkill(fourth, 21885, 60, fourth).useSkill();
																				stageTimer = ThreadPoolManager.getInstance().schedule(new Runnable() {

																					@Override
																					public void run() {
																						x = fourth.getX();
																						y = fourth.getY();
																						z = fourth.getZ();
																						h = fourth.getHeading();
																						despawnNpc(fourth);
																						spawn(236230, x, y, z, (byte) h); // Immortal
																						final Npc Immortal = getNpc(236230);
																						SkillEngine.getInstance().getSkill(Immortal, 21634, 60, Immortal).useSkill();
																						stageTimer = ThreadPoolManager.getInstance().schedule(new Runnable() {

																							@Override
																							public void run() {
																								final Npc Immortal = getNpc(236230);
																								SkillEngine.getInstance().getSkill(Immortal, 21885, 60, Immortal).useSkill();
																								stageTimer = ThreadPoolManager.getInstance().schedule(new Runnable() {

																									@Override
																									public void run() {
																										final Npc Immortal = getNpc(236230);
																										x = Immortal.getX();
																										y = Immortal.getY();
																										z = Immortal.getZ();
																										h = Immortal.getHeading();
																										despawnNpc(Immortal);
																										spawn(236231, x, y, z, (byte) h); // Exhausted
																										stageTimer = ThreadPoolManager.getInstance().schedule(new Runnable() {

																											@Override
																											public void run() {
																												final Npc sixth = getNpc(236231);
																												SkillEngine.getInstance().getSkill(sixth, 21885, 60, sixth).useSkill();
																												stageTimer = ThreadPoolManager.getInstance().schedule(new Runnable() {

																													@Override
																													public void run() {
																														x = sixth.getX();
																														y = sixth.getY();
																														z = sixth.getZ();
																														h = sixth.getHeading();
																														despawnNpc(sixth);
																														spawn(236230, x, y, z, (byte) h); // Immortal
																														final Npc Immortal = getNpc(236230);
																														SkillEngine.getInstance().getSkill(Immortal, 21634, 60, Immortal).useSkill();
																														stageTimer = ThreadPoolManager.getInstance().schedule(new Runnable() {

																															@Override
																															public void run() {
																																SkillEngine.getInstance().getSkill(Immortal, 21885, 60, Immortal).useSkill();
																																stageTimer = ThreadPoolManager.getInstance().schedule(new Runnable() {

																																	@Override
																																	public void run() {
																																		final Npc Immortal = getNpc(236230);
																																		x = Immortal.getX();
																																		y = Immortal.getY();
																																		z = Immortal.getZ();
																																		h = Immortal.getHeading();
																																		despawnNpc(Immortal);
																																		spawn(236231, x, y, z, (byte) h); // Exhausted
																																		stageTimer = ThreadPoolManager.getInstance().schedule(new Runnable() {

																																			@Override
																																			public void run() {
																																				switch (player.getRace()) {
																																					case ELYOS:
																																						orissanBossFailE(player);
																																						break;
																																					case ASMODIANS:
																																						orissanBossFailA(player);
																																						break;
																																					default:
																																						break;
																																				}
																																			}
																																		}, 76000 * 3);
																																	}
																																}, 10000);
																															}
																														}, 30000 * 3);
																													}
																												}, 10000);
																											}
																										}, 76000 * 3);
																									}
																								}, 10000);
																							}
																						}, 30000 * 3);
																					}
																				}, 10000);
																			}
																		}, 76000 * 3);
																	}
																}, 10000);
															}
														}, 30000 * 3);
													}
												}, 10000);
											}
										}, 60000 * 2);
									}
								}, 10000);
							}
						}, 90000);
					}
				}, 10000);
			}
		}
	}

	public void doorDestroyed2() {
		if (race == 1) {
			deleteNpc(209681);
			deleteNpc(209683);
			deleteNpc(209684);
			ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					spawn(209681, 809.0107f, 596.524f, 1700.7549f, (byte) 32);
					spawn(209683, 806.947f, 591.7435f, 1701.0449f, (byte) 32);
					spawn(209684, 811.6698f, 592.4256f, 1701.0449f, (byte) 32);
					final Npc talk1 = getNpc(209681);
					NpcShoutsService.getInstance().sendMsg(talk1, 1501310, talk1.getObjectId(), 0, 1000);
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							Npc talk2 = getNpc(209684);
							Npc door = getNpc(702696);
							if (firstBoss + secondBoss == 1) {
								NpcShoutsService.getInstance().sendMsg(talk2, 1501315, talk2.getObjectId(), 0, 1000);
								SkillEngine.getInstance().getSkill(talk1, 20840, 60, door).useSkill();
								door.getController().die();
								NpcShoutsService.getInstance().sendMsg(talk1, 1501311, talk1.getObjectId(), 0, 1000);
							}
							if (firstBoss + secondBoss == 2) {
								NpcShoutsService.getInstance().sendMsg(talk2, 1501316, talk2.getObjectId(), 0, 1000);
								SkillEngine.getInstance().getSkill(talk1, 20840, 60, door).useSkill();
								door.getController().die();
								NpcShoutsService.getInstance().sendMsg(talk1, 1501311, talk1.getObjectId(), 0, 1000);
							}
							if (firstBoss + secondBoss == 0) {
								SkillEngine.getInstance().getSkill(talk1, 20840, 60, door).useSkill();
								door.getController().die();
								NpcShoutsService.getInstance().sendMsg(talk1, 1501311, talk1.getObjectId(), 0, 1000);
							}
						}
					}, 5000);
				}
			}, 30000);
		}
		if (race == 2) {
			deleteNpc(209746);
			deleteNpc(209748);
			deleteNpc(209750);
			ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					spawn(209746, 809.0107f, 596.524f, 1700.7549f, (byte) 32);
					spawn(209748, 806.947f, 591.7435f, 1701.0449f, (byte) 32);
					spawn(209750, 811.6698f, 592.4256f, 1701.0449f, (byte) 32);
					final Npc talk1 = getNpc(209746);
					NpcShoutsService.getInstance().sendMsg(talk1, 1501310, talk1.getObjectId(), 0, 1000);
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							Npc talk2 = getNpc(209748);
							Npc door = getNpc(702696);
							if (firstBoss + secondBoss == 1) {
								NpcShoutsService.getInstance().sendMsg(talk2, 1501315, talk2.getObjectId(), 0, 1000);
								SkillEngine.getInstance().getSkill(talk1, 20840, 60, door).useSkill();
								door.getController().die();
								NpcShoutsService.getInstance().sendMsg(talk1, 1501311, talk1.getObjectId(), 0, 1000);
							}
							if (firstBoss + secondBoss == 2) {
								NpcShoutsService.getInstance().sendMsg(talk2, 1501316, talk2.getObjectId(), 0, 1000);
								SkillEngine.getInstance().getSkill(talk1, 20840, 60, door).useSkill();
								door.getController().die();
								NpcShoutsService.getInstance().sendMsg(talk1, 1501311, talk1.getObjectId(), 0, 1000);
							}
							if (firstBoss + secondBoss == 0) {
								SkillEngine.getInstance().getSkill(talk1, 20840, 60, door).useSkill();
								door.getController().die();
								NpcShoutsService.getInstance().sendMsg(talk1, 1501311, talk1.getObjectId(), 0, 1000);
							}
						}
					}, 5000);
				}
			}, 5000);
		}
	}

	public void doorDestroyed() {
		if (!isRunning) {
			isRunning = true;
			if (race == 1) {
				FlameTimer.cancel(true);
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						spawn(209681, 589.06866f, 181.19467f, 1683.7301f, (byte) 0);
						spawn(209683, 585.4429f, 183.71507f, 1683.7301f, (byte) 0);
						spawn(209684, 585.73883f, 178.0035f, 1683.7301f, (byte) 0);
						final Npc talk1 = getNpc(209681);
						NpcShoutsService.getInstance().sendMsg(talk1, 1501310, talk1.getObjectId(), 0, 1000);
						ThreadPoolManager.getInstance().schedule(new Runnable() {

							@Override
							public void run() {
								Npc talk2 = getNpc(209684);
								Npc door = getNpc(702695);
								if (firstBoss > 0) {
									NpcShoutsService.getInstance().sendMsg(talk2, 1501315, talk2.getObjectId(), 0, 1000);
									SkillEngine.getInstance().getSkill(talk1, 20840, 60, door).useSkill();
									door.getController().die();
									NpcShoutsService.getInstance().sendMsg(talk1, 1501311, talk1.getObjectId(), 0, 1000);
								}
								else {
									SkillEngine.getInstance().getSkill(talk1, 20840, 60, door).useSkill();
									door.getController().die();
									NpcShoutsService.getInstance().sendMsg(talk1, 1501311, talk1.getObjectId(), 0, 1000);
								}
							}
						}, 5000);
					}
				}, 30000);
			}
			if (race == 2) {
				FlameTimer.cancel(true);
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						spawn(209746, 589.06866f, 181.19467f, 1683.7301f, (byte) 0);
						spawn(209748, 585.4429f, 183.71507f, 1683.7301f, (byte) 0);
						spawn(209750, 585.73883f, 178.0035f, 1683.7301f, (byte) 0);
						final Npc talk1 = getNpc(209746);
						NpcShoutsService.getInstance().sendMsg(talk1, 1501310, talk1.getObjectId(), 0, 1000);
						ThreadPoolManager.getInstance().schedule(new Runnable() {

							@Override
							public void run() {
								Npc talk2 = getNpc(209748);
								Npc door = getNpc(702695);
								if (firstBoss > 0) {
									NpcShoutsService.getInstance().sendMsg(talk2, 1501315, talk2.getObjectId(), 0, 1000);
									SkillEngine.getInstance().getSkill(talk1, 20840, 60, door).useSkill();
									door.getController().die();
									NpcShoutsService.getInstance().sendMsg(talk1, 1501311, talk1.getObjectId(), 0, 1000);
								}
								else {
									SkillEngine.getInstance().getSkill(talk1, 20840, 60, door).useSkill();
									door.getController().die();
									NpcShoutsService.getInstance().sendMsg(talk1, 1501311, talk1.getObjectId(), 0, 1000);
								}
							}
						}, 5000);
					}
				}, 5000);
			}
		}
	}

	public void protectorChecker() {
		pChecker = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				if (score == 2) {
					Npc beritra1 = getNpc(236244);
					if (beritra1.getLifeStats().getHpPercentage() < 100) {
						calculateTimer();
						beritraTimer();
						beritraSummon();
						stageFinal = true;
						pChecker.cancel(true);
					}
				}
				if (score == 1) {
					Npc beritra1 = getNpc(236245);
					if (beritra1.getLifeStats().getHpPercentage() < 100) {
						protectorTimer(beritra1);
						calculateTimer();
						beritraTimer();
						beritraSummon();
						stageFinal = true;
						pChecker.cancel(true);
					}
				}
				if (score == 0) {
					Npc beritra1 = getNpc(236246);
					if (beritra1.getLifeStats().getHpPercentage() < 100) {
						protectorTimer(beritra1);
						calculateTimer();
						checkBuff();
						beritraSummon();
						stageFinal = true;
						pChecker.cancel(true);
					}
				}
			}
		}, 1000, 1000);
	}

	public void protectorTimer(final Npc beritra) {
		pTimer = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				Npc dragon = getNpc(236247);
				Npc beritraHum = getNpc(236246);
				if (getNpcs(855460).isEmpty() && getNpcs(855461).isEmpty() && getNpcs(855462).isEmpty() && getNpcs(855463).isEmpty() && getNpcs(855464).isEmpty() && getNpcs(855465).isEmpty()) {
					if (beritra.getEffectController().isAbnormalPresentBySkillId(21610) || beritra.getEffectController().isAbnormalPresentBySkillId(21611) || beritra.getEffectController().isAbnormalPresentBySkillId(21612) || beritraHum.getEffectController().isAbnormalPresentBySkillId(21612) || beritraHum.getEffectController().isAbnormalPresentBySkillId(21612) || beritraHum.getEffectController().isAbnormalPresentBySkillId(21612) || dragon.getEffectController().isAbnormalPresentBySkillId(21610) || dragon.getEffectController().isAbnormalPresentBySkillId(21611) || dragon.getEffectController().isAbnormalPresentBySkillId(21612) || dragon.getEffectController().isAbnormalPresentBySkillId(21618)) {
						switch (Rnd.get(1, 6)) {
							case 1:
								sendMsg(1402728);
								spawn(855460, 128.19576f, 461.10153f, 1754.587f, (byte) 11);
								break;
							case 2:
								sendMsg(1402728);
								spawn(855461, 177.99277f, 457.63803f, 1759.8959f, (byte) 39);
								break;
							case 3:
								sendMsg(1402728);
								spawn(855462, 208.01146f, 496.2286f, 1754.5236f, (byte) 53);
								break;
							case 4:
								sendMsg(1402728);
								spawn(855463, 208.86754f, 542.6027f, 1754.6102f, (byte) 68);
								break;
							case 5:
								sendMsg(1402728);
								spawn(855464, 176.51666f, 580.03455f, 1760.0494f, (byte) 82);
								break;
							case 6:
								sendMsg(1402728);
								spawn(855465, 127.19859f, 574.9473f, 1754.6764f, (byte) 99);
								break;
						}
					}
				}
			}
		}, 60000, 60000);
	}

	public void checkBuff() {
		if (bChecker == null) {
			bChecker = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

				@Override
				public void run() {
					if (score == 0) {
						Npc bBuff = getNpc(236246);
						if (!bBuff.getEffectController().isAbnormalPresentBySkillId(21610) && !bBuff.getEffectController().isAbnormalPresentBySkillId(21611) && !bBuff.getEffectController().isAbnormalPresentBySkillId(21612) && bBuff.getLifeStats().getHpPercentage() != 0) {
							despawnNpc(bBuff);
							beritraTrue();
							bChecker.cancel(true);
						}
					}
				}
			}, 1000, 1000);
		}
	}

	public void beritraTrue() {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawn(702699, 151.98242f, 518.67438f, 1749.6335f, (byte) 0, 383);
				spawn(236247, 124.09473f, 519.9342f, 1749.8306f, (byte) 0);
				beritraTimer();
			}
		}, 5000);
	}

	public void beritraSummon() {
		if (bSummon == null) {
			bSummon = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

				@Override
				public void run() {
					for (int s = 0; s < 4; s++) {
						deleteNpc(855444);
					}
					spawn(855444, 148.32216f, 527.70935f, 1749.4484f, (byte) 96);
					spawn(855444, 159.82544f, 521.0148f, 1749.4779f, (byte) 64);
					spawn(855444, 154.69583f, 509.9405f, 1749.4435f, (byte) 35);
					spawn(855444, 144.39413f, 515.2097f, 1749.4745f, (byte) 7);
				}
			}, 90000, 90000);
		}
	}

	public void beritraTimer() {
		notification();
		beritraTimers = ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				beritraRun = true;
				bSummon.cancel(true);
				for (int s = 0; s < 4; s++) {
					deleteNpc(855444);
				}
				for (int s = 0; s < 4; s++) {
					deleteNpc(855444);
				}
				for (int s = 0; s < 4; s++) {
					deleteNpc(209707);
				}
				for (int s = 0; s < 4; s++) {
					deleteNpc(209754);
				}
				if (race == 1) {
					if (score == 0) {
						final Npc remove = getNpc(236247);
						insMsg.cancel(true);
						ThreadPoolManager.getInstance().schedule(new Runnable() {

							@Override
							public void run() {
								remove.getController().die();
								spawn(731548, 139.90425f, 518.69543f, 1749.5311f, (byte) 0);
								spawn(209680, 144.02014f, 519.71625f, 1749.4772f, (byte) 0);
								spawn(209680, 144.15436f, 516.6729f, 1749.4772f, (byte) 0);
								spawn(209741, 145.75145f, 518.8291f, 1749.4772f, (byte) 0);
								ThreadPoolManager.getInstance().schedule(new Runnable() {

									@Override
									public void run() {
										Npc shout = getNpc(209741);
										NpcShoutsService.getInstance().sendMsg(shout, 1501326, shout.getObjectId(), 0, 1000);
										stopInstanceTask();
									}
								}, 3000);
							}
						}, 3000);
					}
					if (score == 1) {
						final Npc remove = getNpc(236246);
						despawnNpc(remove);
						insMsg.cancel(true);
						spawn(731548, 139.90425f, 518.69543f, 1749.5311f, (byte) 0);
						spawn(209680, 144.02014f, 519.71625f, 1749.4772f, (byte) 0);
						spawn(209680, 144.15436f, 516.6729f, 1749.4772f, (byte) 0);
						spawn(209741, 145.75145f, 518.8291f, 1749.4772f, (byte) 0);
						ThreadPoolManager.getInstance().schedule(new Runnable() {

							@Override
							public void run() {
								Npc shout = getNpc(209741);
								NpcShoutsService.getInstance().sendMsg(shout, 1501326, shout.getObjectId(), 0, 1000);
								stopInstanceTask();
							}
						}, 3000);
					}
					if (score == 2) {
						final Npc remove = getNpc(236245);
						despawnNpc(remove);
						insMsg.cancel(true);
						spawn(731548, 139.90425f, 518.69543f, 1749.5311f, (byte) 0);
						spawn(209680, 144.02014f, 519.71625f, 1749.4772f, (byte) 0);
						spawn(209680, 144.15436f, 516.6729f, 1749.4772f, (byte) 0);
						spawn(209741, 145.75145f, 518.8291f, 1749.4772f, (byte) 0);
						ThreadPoolManager.getInstance().schedule(new Runnable() {

							@Override
							public void run() {
								Npc shout = getNpc(209741);
								NpcShoutsService.getInstance().sendMsg(shout, 1501326, shout.getObjectId(), 0, 1000);
								stopInstanceTask();
							}
						}, 3000);
					}
				}
				if (race == 2) {
					if (score == 0) {
						final Npc remove = getNpc(236247);
						insMsg.cancel(true);
						ThreadPoolManager.getInstance().schedule(new Runnable() {

							@Override
							public void run() {
								remove.getController().die();
								spawn(731548, 139.90425f, 518.69543f, 1749.5311f, (byte) 0);
								spawn(209745, 144.02014f, 519.71625f, 1749.4772f, (byte) 0);
								spawn(209745, 144.15436f, 516.6729f, 1749.4772f, (byte) 0);
								spawn(209806, 145.75145f, 518.8291f, 1749.4772f, (byte) 0);
								ThreadPoolManager.getInstance().schedule(new Runnable() {

									@Override
									public void run() {
										Npc shout = getNpc(209806);
										NpcShoutsService.getInstance().sendMsg(shout, 1501326, shout.getObjectId(), 0, 1000);
										stopInstanceTask();
									}
								}, 3000);
							}
						}, 3000);
					}
					if (score == 1) {
						final Npc remove = getNpc(236246);
						despawnNpc(remove);
						insMsg.cancel(true);
						spawn(731548, 139.90425f, 518.69543f, 1749.5311f, (byte) 0);
						spawn(209745, 144.02014f, 519.71625f, 1749.4772f, (byte) 0);
						spawn(209745, 144.15436f, 516.6729f, 1749.4772f, (byte) 0);
						spawn(209806, 145.75145f, 518.8291f, 1749.4772f, (byte) 0);
						ThreadPoolManager.getInstance().schedule(new Runnable() {

							@Override
							public void run() {
								Npc shout = getNpc(209806);
								NpcShoutsService.getInstance().sendMsg(shout, 1501326, shout.getObjectId(), 0, 1000);
								stopInstanceTask();
							}
						}, 3000);
					}
					if (score == 2) {
						final Npc remove = getNpc(236245);
						despawnNpc(remove);
						insMsg.cancel(true);
						spawn(731548, 139.90425f, 518.69543f, 1749.5311f, (byte) 0);
						spawn(209745, 144.02014f, 519.71625f, 1749.4772f, (byte) 0);
						spawn(209745, 144.15436f, 516.6729f, 1749.4772f, (byte) 0);
						spawn(209806, 145.75145f, 518.8291f, 1749.4772f, (byte) 0);
						ThreadPoolManager.getInstance().schedule(new Runnable() {

							@Override
							public void run() {
								Npc shout = getNpc(209806);
								NpcShoutsService.getInstance().sendMsg(shout, 1501326, shout.getObjectId(), 0, 1000);
								stopInstanceTask();
							}
						}, 3000);
					}
				}
			}
		}, 60000 * beritraTim);

	}

	public void notification() {
		final int noticeEnd = beritraTim - 1;
		sendMsg(1402722);
		rushSquad();
		insMsg = ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				sendMsg(1402725);
				if (race == 1) {
					for (int s = 0; s < 4; s++) {
						deleteNpc(209680);
					}
				}
				if (race == 2) {
					for (int s = 0; s < 4; s++) {
						deleteNpc(209745);
					}
				}
			}
		}, 60000 * noticeEnd);
	}

	public void rushSquad() {
		if (beritraTim == 11) {
			squad = ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					if (race == 1) {
						sendMsg(1402727);
						spawn(209707, 144.41025f, 519.1675f, 1749.4811f, (byte) 67);
						spawn(209707, 145.18846f, 516.9105f, 1749.4856f, (byte) 67);
						spawn(209707, 146.23433f, 514.5892f, 1749.4856f, (byte) 67);
						spawn(209707, 147.34212f, 511.9703f, 1749.4772f, (byte) 67);
						squad = ThreadPoolManager.getInstance().schedule(new Runnable() {

							@Override
							public void run() {
								for (int s = 0; s < 4; s++) {
									deleteNpc(209707);
								}
								sendMsg(1402727);
								spawn(209707, 144.41025f, 519.1675f, 1749.4811f, (byte) 67);
								spawn(209707, 145.18846f, 516.9105f, 1749.4856f, (byte) 67);
								spawn(209707, 146.23433f, 514.5892f, 1749.4856f, (byte) 67);
								spawn(209707, 147.34212f, 511.9703f, 1749.4772f, (byte) 67);
								squad = ThreadPoolManager.getInstance().schedule(new Runnable() {

									@Override
									public void run() {
										for (int s = 0; s < 4; s++) {
											deleteNpc(209707);
										}
										sendMsg(1402727);
										spawn(209707, 144.41025f, 519.1675f, 1749.4811f, (byte) 67);
										spawn(209707, 145.18846f, 516.9105f, 1749.4856f, (byte) 67);
										spawn(209707, 146.23433f, 514.5892f, 1749.4856f, (byte) 67);
										spawn(209707, 147.34212f, 511.9703f, 1749.4772f, (byte) 67);
										squad = ThreadPoolManager.getInstance().schedule(new Runnable() {

											@Override
											public void run() {
												for (int s = 0; s < 4; s++) {
													deleteNpc(209707);
												}
												sendMsg(1402727);
												spawn(209707, 144.41025f, 519.1675f, 1749.4811f, (byte) 67);
												spawn(209707, 145.18846f, 516.9105f, 1749.4856f, (byte) 67);
												spawn(209707, 146.23433f, 514.5892f, 1749.4856f, (byte) 67);
												spawn(209707, 147.34212f, 511.9703f, 1749.4772f, (byte) 67);
												squad = ThreadPoolManager.getInstance().schedule(new Runnable() {

													@Override
													public void run() {
														for (int s = 0; s < 4; s++) {
															deleteNpc(209707);
														}
														sendMsg(1402727);
														spawn(209707, 144.41025f, 519.1675f, 1749.4811f, (byte) 67);
														spawn(209707, 145.18846f, 516.9105f, 1749.4856f, (byte) 67);
														spawn(209707, 146.23433f, 514.5892f, 1749.4856f, (byte) 67);
														spawn(209707, 147.34212f, 511.9703f, 1749.4772f, (byte) 67);
													}
												}, 60000 * 2);
											}
										}, 60000 * 2);
									}
								}, 60000 * 2);
							}
						}, 60000 * 2);
					}
					if (race == 2) {
						sendMsg(1402727);
						spawn(209754, 144.41025f, 519.1675f, 1749.4811f, (byte) 67);
						spawn(209754, 145.18846f, 516.9105f, 1749.4856f, (byte) 67);
						spawn(209754, 146.23433f, 514.5892f, 1749.4856f, (byte) 67);
						spawn(209754, 147.34212f, 511.9703f, 1749.4772f, (byte) 67);
						squad = ThreadPoolManager.getInstance().schedule(new Runnable() {

							@Override
							public void run() {
								for (int s = 0; s < 4; s++) {
									deleteNpc(209754);
								}
								sendMsg(1402727);
								spawn(209754, 144.41025f, 519.1675f, 1749.4811f, (byte) 67);
								spawn(209754, 145.18846f, 516.9105f, 1749.4856f, (byte) 67);
								spawn(209754, 146.23433f, 514.5892f, 1749.4856f, (byte) 67);
								spawn(209754, 147.34212f, 511.9703f, 1749.4772f, (byte) 67);
								squad = ThreadPoolManager.getInstance().schedule(new Runnable() {

									@Override
									public void run() {
										for (int s = 0; s < 4; s++) {
											deleteNpc(209754);
										}
										sendMsg(1402727);
										spawn(209754, 144.41025f, 519.1675f, 1749.4811f, (byte) 67);
										spawn(209754, 145.18846f, 516.9105f, 1749.4856f, (byte) 67);
										spawn(209754, 146.23433f, 514.5892f, 1749.4856f, (byte) 67);
										spawn(209754, 147.34212f, 511.9703f, 1749.4772f, (byte) 67);
										squad = ThreadPoolManager.getInstance().schedule(new Runnable() {

											@Override
											public void run() {
												for (int s = 0; s < 4; s++) {
													deleteNpc(209754);
												}
												sendMsg(1402727);
												spawn(209754, 144.41025f, 519.1675f, 1749.4811f, (byte) 67);
												spawn(209754, 145.18846f, 516.9105f, 1749.4856f, (byte) 67);
												spawn(209754, 146.23433f, 514.5892f, 1749.4856f, (byte) 67);
												spawn(209754, 147.34212f, 511.9703f, 1749.4772f, (byte) 67);
												squad = ThreadPoolManager.getInstance().schedule(new Runnable() {

													@Override
													public void run() {
														for (int s = 0; s < 4; s++) {
															deleteNpc(209754);
														}
														sendMsg(1402727);
														spawn(209754, 144.41025f, 519.1675f, 1749.4811f, (byte) 67);
														spawn(209754, 145.18846f, 516.9105f, 1749.4856f, (byte) 67);
														spawn(209754, 146.23433f, 514.5892f, 1749.4856f, (byte) 67);
														spawn(209754, 147.34212f, 511.9703f, 1749.4772f, (byte) 67);
													}
												}, 60000 * 2);
											}
										}, 60000 * 2);
									}
								}, 60000 * 2);
							}
						}, 60000 * 2);
					}
				}
			}, 60000 * 2);
		}
		if (beritraTim == 10) {
			squad = ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					if (race == 1) {
						sendMsg(1402727);
						spawn(209707, 144.41025f, 519.1675f, 1749.4811f, (byte) 67);
						spawn(209707, 145.18846f, 516.9105f, 1749.4856f, (byte) 67);
						spawn(209707, 146.23433f, 514.5892f, 1749.4856f, (byte) 67);
						spawn(209707, 147.34212f, 511.9703f, 1749.4772f, (byte) 67);
						squad = ThreadPoolManager.getInstance().schedule(new Runnable() {

							@Override
							public void run() {
								for (int s = 0; s < 4; s++) {
									deleteNpc(209707);
								}
								sendMsg(1402727);
								spawn(209707, 144.41025f, 519.1675f, 1749.4811f, (byte) 67);
								spawn(209707, 145.18846f, 516.9105f, 1749.4856f, (byte) 67);
								spawn(209707, 146.23433f, 514.5892f, 1749.4856f, (byte) 67);
								spawn(209707, 147.34212f, 511.9703f, 1749.4772f, (byte) 67);
								squad = ThreadPoolManager.getInstance().schedule(new Runnable() {

									@Override
									public void run() {
										for (int s = 0; s < 4; s++) {
											deleteNpc(209707);
										}
										sendMsg(1402727);
										spawn(209707, 144.41025f, 519.1675f, 1749.4811f, (byte) 67);
										spawn(209707, 145.18846f, 516.9105f, 1749.4856f, (byte) 67);
										spawn(209707, 146.23433f, 514.5892f, 1749.4856f, (byte) 67);
										spawn(209707, 147.34212f, 511.9703f, 1749.4772f, (byte) 67);
										squad = ThreadPoolManager.getInstance().schedule(new Runnable() {

											@Override
											public void run() {
												for (int s = 0; s < 4; s++) {
													deleteNpc(209707);
												}
												sendMsg(1402727);
												spawn(209707, 144.41025f, 519.1675f, 1749.4811f, (byte) 67);
												spawn(209707, 145.18846f, 516.9105f, 1749.4856f, (byte) 67);
												spawn(209707, 146.23433f, 514.5892f, 1749.4856f, (byte) 67);
												spawn(209707, 147.34212f, 511.9703f, 1749.4772f, (byte) 67);
											}
										}, 60000 * 2);
									}
								}, 60000 * 2);
							}
						}, 60000 * 2);
					}
					if (race == 2) {
						sendMsg(1402727);
						spawn(209754, 144.41025f, 519.1675f, 1749.4811f, (byte) 67);
						spawn(209754, 145.18846f, 516.9105f, 1749.4856f, (byte) 67);
						spawn(209754, 146.23433f, 514.5892f, 1749.4856f, (byte) 67);
						spawn(209754, 147.34212f, 511.9703f, 1749.4772f, (byte) 67);
						squad = ThreadPoolManager.getInstance().schedule(new Runnable() {

							@Override
							public void run() {
								for (int s = 0; s < 4; s++) {
									deleteNpc(209754);
								}
								sendMsg(1402727);
								spawn(209754, 144.41025f, 519.1675f, 1749.4811f, (byte) 67);
								spawn(209754, 145.18846f, 516.9105f, 1749.4856f, (byte) 67);
								spawn(209754, 146.23433f, 514.5892f, 1749.4856f, (byte) 67);
								spawn(209754, 147.34212f, 511.9703f, 1749.4772f, (byte) 67);
								squad = ThreadPoolManager.getInstance().schedule(new Runnable() {

									@Override
									public void run() {
										for (int s = 0; s < 4; s++) {
											deleteNpc(209754);
										}
										sendMsg(1402727);
										spawn(209754, 144.41025f, 519.1675f, 1749.4811f, (byte) 67);
										spawn(209754, 145.18846f, 516.9105f, 1749.4856f, (byte) 67);
										spawn(209754, 146.23433f, 514.5892f, 1749.4856f, (byte) 67);
										spawn(209754, 147.34212f, 511.9703f, 1749.4772f, (byte) 67);
										squad = ThreadPoolManager.getInstance().schedule(new Runnable() {

											@Override
											public void run() {
												for (int s = 0; s < 4; s++) {
													deleteNpc(209754);
												}
												sendMsg(1402727);
												spawn(209754, 144.41025f, 519.1675f, 1749.4811f, (byte) 67);
												spawn(209754, 145.18846f, 516.9105f, 1749.4856f, (byte) 67);
												spawn(209754, 146.23433f, 514.5892f, 1749.4856f, (byte) 67);
												spawn(209754, 147.34212f, 511.9703f, 1749.4772f, (byte) 67);

											}
										}, 60000 * 2);
									}
								}, 60000 * 2);
							}
						}, 60000 * 2);
					}
				}
			}, 60000 * 2);
		}
		if (beritraTim == 9) {
			squad = ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					if (race == 1) {
						sendMsg(1402727);
						spawn(209707, 144.41025f, 519.1675f, 1749.4811f, (byte) 67);
						spawn(209707, 145.18846f, 516.9105f, 1749.4856f, (byte) 67);
						spawn(209707, 146.23433f, 514.5892f, 1749.4856f, (byte) 67);
						spawn(209707, 147.34212f, 511.9703f, 1749.4772f, (byte) 67);
						squad = ThreadPoolManager.getInstance().schedule(new Runnable() {

							@Override
							public void run() {
								for (int s = 0; s < 4; s++) {
									deleteNpc(209707);
								}
								sendMsg(1402727);
								spawn(209707, 144.41025f, 519.1675f, 1749.4811f, (byte) 67);
								spawn(209707, 145.18846f, 516.9105f, 1749.4856f, (byte) 67);
								spawn(209707, 146.23433f, 514.5892f, 1749.4856f, (byte) 67);
								spawn(209707, 147.34212f, 511.9703f, 1749.4772f, (byte) 67);
								squad = ThreadPoolManager.getInstance().schedule(new Runnable() {

									@Override
									public void run() {
										for (int s = 0; s < 4; s++) {
											deleteNpc(209707);
										}
										sendMsg(1402727);
										spawn(209707, 144.41025f, 519.1675f, 1749.4811f, (byte) 67);
										spawn(209707, 145.18846f, 516.9105f, 1749.4856f, (byte) 67);
										spawn(209707, 146.23433f, 514.5892f, 1749.4856f, (byte) 67);
										spawn(209707, 147.34212f, 511.9703f, 1749.4772f, (byte) 67);
									}
								}, 60000 * 2);
							}
						}, 60000 * 2);
					}
					if (race == 2) {
						sendMsg(1402727);
						spawn(209754, 144.41025f, 519.1675f, 1749.4811f, (byte) 67);
						spawn(209754, 145.18846f, 516.9105f, 1749.4856f, (byte) 67);
						spawn(209754, 146.23433f, 514.5892f, 1749.4856f, (byte) 67);
						spawn(209754, 147.34212f, 511.9703f, 1749.4772f, (byte) 67);
						squad = ThreadPoolManager.getInstance().schedule(new Runnable() {

							@Override
							public void run() {
								for (int s = 0; s < 4; s++) {
									deleteNpc(209754);
								}
								sendMsg(1402727);
								spawn(209754, 144.41025f, 519.1675f, 1749.4811f, (byte) 67);
								spawn(209754, 145.18846f, 516.9105f, 1749.4856f, (byte) 67);
								spawn(209754, 146.23433f, 514.5892f, 1749.4856f, (byte) 67);
								spawn(209754, 147.34212f, 511.9703f, 1749.4772f, (byte) 67);
								squad = ThreadPoolManager.getInstance().schedule(new Runnable() {

									@Override
									public void run() {
										for (int s = 0; s < 4; s++) {
											deleteNpc(209754);
										}
										sendMsg(1402727);
										spawn(209754, 144.41025f, 519.1675f, 1749.4811f, (byte) 67);
										spawn(209754, 145.18846f, 516.9105f, 1749.4856f, (byte) 67);
										spawn(209754, 146.23433f, 514.5892f, 1749.4856f, (byte) 67);
										spawn(209754, 147.34212f, 511.9703f, 1749.4772f, (byte) 67);
									}
								}, 60000 * 2);
							}
						}, 60000 * 2);
					}
				}
			}, 60000 * 2);
		}

	}

	public void calculateTimer() {
		if (beritraTim == 8 || beritraTim == 7) {
			beritraTim = 11;
		}
		if (beritraTim == 6 || beritraTim == 5) {
			beritraTim = 10;
		}
		if (beritraTim == 4 || beritraTim == 3) {
			beritraTim = 9;
		}
		if (beritraTim < 3) {
			beritraTim = 8;
		}
	}

	@Override
	public void onDie(final Npc npc) {
		final Player player = npc.getAggroList().getMostPlayerDamage();
		switch (npc.getObjectTemplate().getTemplateId()) {
			/****************************
			 * Lava Protector *
			 ****************************/
			case 236227:
			case 236225:
				despawnNpc(npc);
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						if (getNpcs(236228).isEmpty() && getNpcs(236226).isEmpty() && !fail1) {
							deleteNpc(236228);
							deleteNpc(236226);
							firstStage = true;
							doorDestroyed();
						}
						else {
							if (!fail1) {
								deleteNpc(236227);
								deleteNpc(236225);
								deleteNpc(236228);
								deleteNpc(236226);
								spawn(236225, 531.57263f, 212.31601f, 1681.8224f, (byte) 60);
								spawn(236226, 531.5915f, 151.92694f, 1681.8224f, (byte) 60);
								boss1 = getNpc(236225);
								boss2 = getNpc(236226);
								SkillEngine.getInstance().getSkill(boss1, 21641, 60, boss1).useSkill();
								SkillEngine.getInstance().getSkill(boss2, 21642, 60, boss2).useSkill();
							}
						}
					}
				}, 5000);
				break;
			/****************************
			 * Heatvent Protector *
			 ****************************/
			case 236228:
			case 236226:
				despawnNpc(npc);
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						if (getNpcs(236227).isEmpty() && getNpcs(236225).isEmpty() && !fail1) {
							deleteNpc(236227);
							deleteNpc(236225);
							firstStage = true;
							doorDestroyed();
						}
						else {
							if (!fail1) {
								deleteNpc(236227);
								deleteNpc(236225);
								deleteNpc(236228);
								deleteNpc(236226);
								spawn(236225, 531.57263f, 212.31601f, 1681.8224f, (byte) 60);
								spawn(236226, 531.5915f, 151.92694f, 1681.8224f, (byte) 60);
								boss1 = getNpc(236225);
								boss2 = getNpc(236226);
								SkillEngine.getInstance().getSkill(boss1, 21641, 60, boss1).useSkill();
								SkillEngine.getInstance().getSkill(boss2, 21642, 60, boss2).useSkill();
							}
						}
					}
				}, 5000);
				break;
			case 236230:
				x = npc.getX();
				y = npc.getY();
				z = npc.getZ();
				h = npc.getHeading();
				despawnNpc(npc);
				spawn(236230, x, y, z, (byte) h);
				Npc Immortal1 = getNpc(236230);
				SkillEngine.getInstance().getSkill(Immortal1, 21634, 60, Immortal1).useSkill();
				break;
			case 236231:
				if (race == 1) {
					spawn(209741, 635.8158f, 883.06915f, 1600.7712f, (byte) 91); // Masionel
					doorDestroyed2();
					instance.doOnAllPlayers(new Visitor<Player>() {

						@Override
						public void visit(Player object) {
							PacketSendUtility.sendPacket(object, new SM_PLAY_MOVIE(0, 914));
						}
					});
					secondStage = true;
					stageTimer.cancel(true);
				}
				if (race == 2) {
					spawn(209806, 635.8158f, 883.06915f, 1600.7712f, (byte) 91); // Parsia
					doorDestroyed2();
					instance.doOnAllPlayers(new Visitor<Player>() {

						@Override
						public void visit(Player object) {
							PacketSendUtility.sendPacket(object, new SM_PLAY_MOVIE(0, 914));
						}
					});
					secondStage = true;
					stageTimer.cancel(true);
				}
				break;
			case 702719:
			case 702720:
				spawn(731548, 635.679f, 901.1033f, 1600.4779f, (byte) 93);
				stopInstanceTask();
				break;
			case 236243:
				endWaves();
				break;
			case 236204:
				despawnNpc(npc);
				if (score == 2) {
					endWaves();
				}
				break;
			case 855444:
			case 236205:
			case 236206:
			case 236207:
			case 236208:
			case 236209:
			case 236210:
			case 236211:
			case 236212:
			case 236213:
			case 236214:
			case 236215:
			case 236216:
			case 236217:
			case 236218:
			case 209697:
			case 209755:
			case 209701:
			case 209757:
				despawnNpc(npc);
				break;
			case 236224:
				openDoor(378);
				break;
			case 236661:
				openDoor(376);
				break;
			case 236662:
				openDoor(375);
				break;
			case 236244:// Beritra Easy
				despawnNpc(npc);
				bSummon.cancel(true);
				stopInstanceTask();
				for (int s = 0; s < 4; s++) {
					deleteNpc(855444);
				}
				for (int s = 0; s < 4; s++) {
					deleteNpc(209707);
				}
				for (int s = 0; s < 4; s++) {
					deleteNpc(209754);
				}
				spawn(833013, 152.2761f, 518.70667f, 1749.5945f, (byte) 118);
				spawn(731548, 139.90425f, 518.69543f, 1749.5311f, (byte) 0);
				if (race == 1) {
					spawn(209680, 144.02014f, 519.71625f, 1749.4772f, (byte) 0);
					spawn(209680, 144.15436f, 516.6729f, 1749.4772f, (byte) 0);
					spawn(209741, 145.75145f, 518.8291f, 1749.4772f, (byte) 0);
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							Npc shout = getNpc(209741);
							NpcShoutsService.getInstance().sendMsg(shout, 1501327, shout.getObjectId(), 0, 1000);
						}
					}, 3000);
				}
				if (race == 2) {
					spawn(209745, 144.02014f, 519.71625f, 1749.4772f, (byte) 0);
					spawn(209745, 144.15436f, 516.6729f, 1749.4772f, (byte) 0);
					spawn(209806, 145.75145f, 518.8291f, 1749.4772f, (byte) 0);
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							Npc shout = getNpc(209806);
							NpcShoutsService.getInstance().sendMsg(shout, 1501327, shout.getObjectId(), 0, 1000);
						}
					}, 3000);
				}
				break;
			case 236245:// Beritra Normal
				despawnNpc(npc);
				for (int s = 0; s < 4; s++) {
					deleteNpc(855444);
				}
				for (int s = 0; s < 4; s++) {
					deleteNpc(209707);
				}
				for (int s = 0; s < 4; s++) {
					deleteNpc(209754);
				}
				bSummon.cancel(true);
				stopInstanceTask();
				spawn(833014, 152.2761f, 518.70667f, 1749.5945f, (byte) 118);
				spawn(731548, 139.90425f, 518.69543f, 1749.5311f, (byte) 0);
				if (race == 1) {
					spawn(209680, 144.02014f, 519.71625f, 1749.4772f, (byte) 0);
					spawn(209680, 144.15436f, 516.6729f, 1749.4772f, (byte) 0);
					spawn(209741, 145.75145f, 518.8291f, 1749.4772f, (byte) 0);
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							Npc shout = getNpc(209741);
							NpcShoutsService.getInstance().sendMsg(shout, 1501327, shout.getObjectId(), 0, 1000);
						}
					}, 3000);
				}
				if (race == 2) {
					spawn(209745, 144.02014f, 519.71625f, 1749.4772f, (byte) 0);
					spawn(209745, 144.15436f, 516.6729f, 1749.4772f, (byte) 0);
					spawn(209806, 145.75145f, 518.8291f, 1749.4772f, (byte) 0);
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							Npc shout = getNpc(209806);
							NpcShoutsService.getInstance().sendMsg(shout, 1501327, shout.getObjectId(), 0, 1000);
						}
					}, 3000);
				}
				break;
			case 236246: // Beritra Hard Humanoid
				despawnNpc(npc);
				spawn(236246, 152.2761f, 518.70667f, 1749.5945f, (byte) 118);
				Npc beritraH2 = getNpc(236246);
				SkillEngine.getInstance().getSkill(beritraH2, 21610, 60, beritraH2).useSkill();
				SkillEngine.getInstance().getSkill(beritraH2, 21611, 60, beritraH2).useSkill();
				SkillEngine.getInstance().getSkill(beritraH2, 21612, 60, beritraH2).useSkill();
				break;
			case 236247: // Beritra Dragon
				for (int s = 0; s < 4; s++) {
					deleteNpc(855444);
				}
				for (int s = 0; s < 4; s++) {
					deleteNpc(209707);
				}
				for (int s = 0; s < 4; s++) {
					deleteNpc(209754);
				}
				bSummon.cancel(true);
				if (!beritraRun) {
					instance.doOnAllPlayers(new Visitor<Player>() {

						@Override
						public void visit(Player object) {
							PacketSendUtility.sendPacket(object, new SM_PLAY_MOVIE(0, 916));
						}
					});
					stopInstanceTask();
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							despawnNpc(npc);
							spawn(833015, 152.2761f, 518.70667f, 1749.5945f, (byte) 118);
							spawn(731548, 139.90425f, 518.69543f, 1749.5311f, (byte) 0);
							if (race == 1) {
								spawn(209680, 144.02014f, 519.71625f, 1749.4772f, (byte) 0);
								spawn(209680, 144.15436f, 516.6729f, 1749.4772f, (byte) 0);
								spawn(209741, 145.75145f, 518.8291f, 1749.4772f, (byte) 0);
								ThreadPoolManager.getInstance().schedule(new Runnable() {

									@Override
									public void run() {
										Npc shout = getNpc(209741);
										NpcShoutsService.getInstance().sendMsg(shout, 1501327, shout.getObjectId(), 0, 1000);
									}
								}, 3000);
							}
							if (race == 2) {
								spawn(209745, 144.02014f, 519.71625f, 1749.4772f, (byte) 0);
								spawn(209745, 144.15436f, 516.6729f, 1749.4772f, (byte) 0);
								spawn(209806, 145.75145f, 518.8291f, 1749.4772f, (byte) 0);
								ThreadPoolManager.getInstance().schedule(new Runnable() {

									@Override
									public void run() {
										Npc shout = getNpc(209806);
										NpcShoutsService.getInstance().sendMsg(shout, 1501327, shout.getObjectId(), 0, 1000);
									}
								}, 3000);
							}
						}
					}, 5000);
				}
				else {
					despawnNpc(npc);
				}
				break;
			case 209707:
			case 209754:
				despawnNpc(npc);
				if (!stageFinal) {
					beritraTim--;
				}
				break;
			case 855460:
			case 855461:
			case 855462:
			case 855463:
			case 855464:
			case 855465:
				SkillEngine.getInstance().getSkill(npc, 21625, 60, player).useSkill();
				sendMsg(1402889);
				despawnNpc(npc);
				break;
		}
	}

	/**
	 * *************************** Twin Boss Failed **************************
	 */
	private void flameBossFailE() {
		fail1 = true;
		spawn(209690, 531.7336f, 206.20775f, 1681.8224f, (byte) 31);
		spawn(209692, 536.65045f, 212.83272f, 1681.8224f, (byte) 65);
		spawn(209697, 531.29895f, 146.5562f, 1681.8224f, (byte) 29);
		spawn(209701, 525.39154f, 151.95476f, 1681.8224f, (byte) 2);

		spawn(209711, 530.9033f, 157.50938f, 1681.8225f, (byte) 92);
		spawn(209715, 536.66895f, 151.98724f, 1681.8224f, (byte) 59);
		spawn(209719, 525.0131f, 212.35306f, 1681.8224f, (byte) 1);
		spawn(209721, 530.95233f, 218.09294f, 1681.8224f, (byte) 92);

		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				Npc guardL = getNpc(209690);
				Npc guardH = getNpc(209701);
				Npc lava = getNpc(236227);
				Npc fountLava = getNpc(236225);
				Npc heat = getNpc(236228);
				Npc fountheat = getNpc(236226);
				SkillEngine.getInstance().getSkill(guardL, 20840, 60, lava).useSkill();
				SkillEngine.getInstance().getSkill(guardL, 20840, 60, fountLava).useSkill();
				SkillEngine.getInstance().getSkill(guardH, 20840, 60, heat).useSkill();
				SkillEngine.getInstance().getSkill(guardH, 20840, 60, fountheat).useSkill();
				deleteNpc(236227);
				deleteNpc(236225);
				deleteNpc(236228);
				deleteNpc(236226);
				firstBoss = 1;
				firstStage = true;
				doorDestroyed();
				Npc guard1 = getNpc(209690);
				Npc guard2 = getNpc(209692);
				Npc guard3 = getNpc(209697);
				Npc guard4 = getNpc(209701);

				Npc guard5 = getNpc(209711);
				Npc guard6 = getNpc(209715);
				Npc guard7 = getNpc(209719);
				Npc guard8 = getNpc(209721);

				guard1.getController().die();
				guard2.getController().die();
				guard3.getController().die();
				guard4.getController().die();
				guard5.getController().die();
				guard6.getController().die();
				guard7.getController().die();
				guard8.getController().die();
			}
		}, 10000);
	}

	private void flameBossFailA() {
		fail1 = true;
		spawn(209746, 531.7336f, 206.20775f, 1681.8224f, (byte) 31);
		spawn(209755, 536.65045f, 212.83272f, 1681.8224f, (byte) 65);
		spawn(209757, 531.29895f, 146.5562f, 1681.8224f, (byte) 29);
		spawn(209762, 525.39154f, 151.95476f, 1681.8224f, (byte) 2);

		spawn(209766, 530.9033f, 157.50938f, 1681.8225f, (byte) 92);
		spawn(209776, 536.66895f, 151.98724f, 1681.8224f, (byte) 59);
		spawn(209780, 525.0131f, 212.35306f, 1681.8224f, (byte) 1);
		spawn(209784, 530.95233f, 218.09294f, 1681.8224f, (byte) 92);

		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				Npc guardL = getNpc(209755);
				Npc guardH = getNpc(209766);
				Npc lava = getNpc(236227);
				Npc fountLava = getNpc(236225);
				Npc heat = getNpc(236228);
				Npc fountheat = getNpc(236226);
				SkillEngine.getInstance().getSkill(guardL, 20840, 60, lava).useSkill();
				SkillEngine.getInstance().getSkill(guardL, 20840, 60, fountLava).useSkill();
				SkillEngine.getInstance().getSkill(guardH, 20840, 60, heat).useSkill();
				SkillEngine.getInstance().getSkill(guardH, 20840, 60, fountheat).useSkill();
				deleteNpc(236227);
				deleteNpc(236225);
				deleteNpc(236228);
				deleteNpc(236226);
				firstBoss = 1;
				firstStage = true;
				doorDestroyed();
				Npc guard1 = getNpc(209746);
				Npc guard2 = getNpc(209755);
				Npc guard3 = getNpc(209757);
				Npc guard4 = getNpc(209762);

				Npc guard5 = getNpc(209766);
				Npc guard6 = getNpc(209776);
				Npc guard7 = getNpc(209780);
				Npc guard8 = getNpc(209784);

				guard1.getController().die();
				guard2.getController().die();
				guard3.getController().die();
				guard4.getController().die();
				guard5.getController().die();
				guard6.getController().die();
				guard7.getController().die();
				guard8.getController().die();
			}
		}, 10000);
	}

	/**
	 * *************************** Orissan Boss Failed **************************
	 */
	private void orissanBossFailE(final Player player) {
		spawn(209690, 818.6932f, 571.14154f, 1701.0443f, (byte) 92);
		spawn(209692, 807.80396f, 571.79126f, 1701.0443f, (byte) 103);
		spawn(209697, 805.8648f, 563.9165f, 1701.0442f, (byte) 9);
		spawn(209701, 815.5379f, 562.5722f, 1701.0441f, (byte) 41);

		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				Npc orissan = getNpc(236231);
				orissan.getController().die();
				secondBoss = 1;
				Npc guard1 = getNpc(209690);
				Npc guard2 = getNpc(209692);
				Npc guard3 = getNpc(209697);
				Npc guard4 = getNpc(209701);

				guard1.getController().die();
				guard2.getController().die();
				guard3.getController().die();
				guard4.getController().die();

			}
		}, 10000);
	}

	private void orissanBossFailA(final Player player) {
		spawn(209746, 818.6932f, 571.14154f, 1701.0443f, (byte) 92);
		spawn(209755, 807.80396f, 571.79126f, 1701.0443f, (byte) 103);
		spawn(209757, 805.8648f, 563.9165f, 1701.0442f, (byte) 9);
		spawn(209762, 815.5379f, 562.5722f, 1701.0441f, (byte) 41);

		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				Npc orissan = getNpc(236231);
				orissan.getController().die();
				secondBoss = 1;
				Npc guard1 = getNpc(209746);
				Npc guard2 = getNpc(209755);
				Npc guard3 = getNpc(209757);
				Npc guard4 = getNpc(209762);

				guard1.getController().die();
				guard2.getController().die();
				guard3.getController().die();
				guard4.getController().die();

			}
		}, 10000);
	}

	/**
	 * *************************** Wave's Bodyguard **************************
	 */
	private void bodyGuard() {
		if (race == 1) {
			sp(209680, 638.04425f, 874.08044f, 1600.8944f, (byte) 28, 1000, "GUARD1", false);
			sp(209680, 639.54205f, 873.6535f, 1600.9027f, (byte) 28, 1000, "GUARD2", false);
			sp(209680, 640.8667f, 873.54987f, 1600.9072f, (byte) 28, 1000, "GUARD3", false);
			sp(209680, 642.6332f, 873.41156f, 1600.903f, (byte) 28, 1000, "GUARD4", false);

			sp(209680, 635.89276f, 874.06696f, 1600.8887f, (byte) 28, 1000, "GUARD5", false);
			sp(209680, 634.5438f, 874.1542f, 1600.8843f, (byte) 28, 1000, "GUARD6", false);
			sp(209680, 633.2586f, 874.2546f, 1600.8798f, (byte) 28, 1000, "GUARD7", false);
			sp(209680, 631.68494f, 874.3776f, 1600.8743f, (byte) 28, 1000, "GUARD8", false);
		}
		if (race == 2) {
			sp(209745, 638.04425f, 874.08044f, 1600.8944f, (byte) 28, 1000, "GUARD1", false);
			sp(209745, 639.54205f, 873.6535f, 1600.9027f, (byte) 28, 1000, "GUARD2", false);
			sp(209745, 640.8667f, 873.54987f, 1600.9072f, (byte) 28, 1000, "GUARD3", false);
			sp(209745, 642.6332f, 873.41156f, 1600.903f, (byte) 28, 1000, "GUARD4", false);

			sp(209745, 635.89276f, 874.06696f, 1600.8887f, (byte) 28, 1000, "GUARD5", false);
			sp(209745, 634.5438f, 874.1542f, 1600.8843f, (byte) 28, 1000, "GUARD6", false);
			sp(209745, 633.2586f, 874.2546f, 1600.8798f, (byte) 28, 1000, "GUARD7", false);
			sp(209745, 631.68494f, 874.3776f, 1600.8743f, (byte) 28, 1000, "GUARD8", false);
		}
	}

	private void bgRespawn() {
		if (race == 1) {
			for (int s = 0; s < 8; s++) {
				deleteNpc(209680);
			}

			ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					spawn(209707, 636.9694f, 884.0824f, 1600.7594f, (byte) 89);
					spawn(209707, 637.5881f, 886.68195f, 1600.7238f, (byte) 89);
					spawn(209707, 638.4884f, 890.34015f, 1600.6006f, (byte) 89);
					spawn(209707, 639.6865f, 895.2082f, 1600.4141f, (byte) 89);

					spawn(209707, 634.6396f, 884.38196f, 1600.7498f, (byte) 89);
					spawn(209707, 634.17944f, 886.869f, 1600.7133f, (byte) 89);
					spawn(209707, 633.2802f, 890.5802f, 1600.5966f, (byte) 89);
					spawn(209707, 632.02594f, 896.08276f, 1600.5029f, (byte) 89);
				}
			}, 1000);

		}
		if (race == 2) {
			for (int s = 0; s < 8; s++) {
				deleteNpc(209745);
			}

			ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					spawn(209754, 636.9694f, 884.0824f, 1600.7594f, (byte) 89);
					spawn(209754, 637.5881f, 886.68195f, 1600.7238f, (byte) 89);
					spawn(209754, 638.4884f, 890.34015f, 1600.6006f, (byte) 89);
					spawn(209754, 639.6865f, 895.2082f, 1600.4141f, (byte) 89);

					spawn(209754, 634.6396f, 884.38196f, 1600.7498f, (byte) 89);
					spawn(209754, 634.17944f, 886.869f, 1600.7133f, (byte) 89);
					spawn(209754, 633.2802f, 890.5802f, 1600.5966f, (byte) 89);
					spawn(209754, 632.02594f, 896.08276f, 1600.5029f, (byte) 89);

				}
			}, 1000);

		}
	}

	public void endWaves() {
		final Npc cannon1 = getNpc(702720);
		final Npc cannon2 = getNpc(702719);
		final Npc berDoor = getNpc(702697);
		isEnd = ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (race == 1) {
					Npc GuardE = getNpc(209741);
					NpcShoutsService.getInstance().sendMsg(GuardE, 1501320, GuardE.getObjectId(), 0, 1000);
					SkillEngine.getInstance().getSkill(cannon2, 20838, 60, berDoor).useSkill();
					if (score == 0) {
						spawn(236246, 152.2761f, 518.70667f, 1749.5945f, (byte) 118);
						Npc beritraH1 = getNpc(236246);
						protectorChecker();
						SkillEngine.getInstance().getSkill(beritraH1, 21610, 60, beritraH1).useSkill();
						SkillEngine.getInstance().getSkill(beritraH1, 21611, 60, beritraH1).useSkill();
						SkillEngine.getInstance().getSkill(beritraH1, 21612, 60, beritraH1).useSkill();
						cleanUp();
					}
					if (score == 1) {
						spawn(236245, 152.2761f, 518.70667f, 1749.5945f, (byte) 118);
						Npc beritraM1 = getNpc(236245);
						SkillEngine.getInstance().getSkill(beritraM1, 21610, 60, beritraM1).useSkill();
						SkillEngine.getInstance().getSkill(beritraM1, 21611, 60, beritraM1).useSkill();
						SkillEngine.getInstance().getSkill(beritraM1, 21612, 60, beritraM1).useSkill();
						protectorChecker();
						cleanUp();
					}
					if (score == 2) {
						spawn(236244, 152.2761f, 518.70667f, 1749.5945f, (byte) 118);
						protectorChecker();
						cleanUp();
					}
				}
				if (race == 2) {
					Npc GuardA = getNpc(209806);
					NpcShoutsService.getInstance().sendMsg(GuardA, 1501320, GuardA.getObjectId(), 0, 1000);
					SkillEngine.getInstance().getSkill(cannon1, 20838, 60, berDoor).useSkill();
					if (score == 0) {
						spawn(236246, 152.2761f, 518.70667f, 1749.5945f, (byte) 118);
						Npc beritraH2 = getNpc(236246);
						protectorChecker();
						SkillEngine.getInstance().getSkill(beritraH2, 21610, 60, beritraH2).useSkill();
						SkillEngine.getInstance().getSkill(beritraH2, 21611, 60, beritraH2).useSkill();
						SkillEngine.getInstance().getSkill(beritraH2, 21612, 60, beritraH2).useSkill();
						cleanUp();
					}
					if (score == 1) {
						spawn(236245, 152.2761f, 518.70667f, 1749.5945f, (byte) 118);
						protectorChecker();
						Npc beritraM2 = getNpc(236245);
						SkillEngine.getInstance().getSkill(beritraM2, 21610, 60, beritraM2).useSkill();
						SkillEngine.getInstance().getSkill(beritraM2, 21611, 60, beritraM2).useSkill();
						SkillEngine.getInstance().getSkill(beritraM2, 21612, 60, beritraM2).useSkill();
						cleanUp();
					}
					if (score == 2) {
						spawn(236244, 152.2761f, 518.70667f, 1749.5945f, (byte) 118);
						cleanUp();
					}
				}
			}
		}, 30000);
	}

	public void cleanUp() {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (race == 1) {
					deleteNpc(209741);
					deleteNpc(702719);
					for (int s = 0; s < 8; s++) {
						deleteNpc(209707);
					}
				}
				if (race == 2) {
					deleteNpc(209806);
					deleteNpc(702720);
					for (int s = 0; s < 8; s++) {
						deleteNpc(209754);
					}
				}
			}
		}, 20000);

	}

	private void removeEffects(Player player) {
		PlayerEffectController effectController = player.getEffectController();
		effectController.removeEffect(0);
	}

	public void removeItems(Player player) {
		Storage storage = player.getInventory();
		storage.decreaseByItemId(185000219, storage.getItemCountByItemId(185000219));
	}

	private void stopInstanceTask() {
		if (insMsg != null) {
			insMsg.cancel(true);
		}
		if (bSummon != null) {
			bSummon.cancel(true);
		}
		if (squad != null) {
			squad.cancel(true);
		}
		if (beritraTimers != null) {
			beritraTimers.cancel(true);
		}
		if (isEnd != null) {
			isEnd.cancel(true);
		}
		if (pTimer != null) {
			pTimer.cancel(true);
		}
		if (pChecker != null) {
			pChecker.cancel(true);
		}
		if (bChecker != null) {
			bChecker.cancel(true);
		}
		if (wave1 != null) {
			wave1.cancel(true);
		}
		if (wave2 != null) {
			wave2.cancel(true);
		}
		if (wave3 != null) {
			wave3.cancel(true);
		}
		if (wave4 != null) {
			wave4.cancel(true);
		}
		if (wave5 != null) {
			wave5.cancel(true);
		}
		if (wave6 != null) {
			wave6.cancel(true);
		}
		if (wave7 != null) {
			wave7.cancel(true);
		}
		if (wave8 != null) {
			wave8.cancel(true);
		}
		if (wave9 != null) {
			wave9.cancel(true);
		}
		if (periodicSpawn1 != null) {
			periodicSpawn1.cancel(true);
		}
		if (periodicSpawn2 != null) {
			periodicSpawn2.cancel(true);
		}
		if (periodicSpawn3 != null) {
			periodicSpawn3.cancel(true);
		}
		if (periodicSpawn4 != null) {
			periodicSpawn4.cancel(true);
		}
		if (periodicSpawn5 != null) {
			periodicSpawn5.cancel(true);
		}
		if (periodicSpawn6 != null) {
			periodicSpawn6.cancel(true);
		}
		if (periodicSpawn7 != null) {
			periodicSpawn7.cancel(true);
		}
		if (FlameChecker != null) {
			FlameChecker.cancel(true);
		}
		if (OrissanChecker != null) {
			OrissanChecker.cancel(true);
		}
		if (SecondStageChecker != null) {
			SecondStageChecker.cancel(true);
		}
		if (FirstStageChecker != null) {
			FirstStageChecker.cancel(true);
		}
		if (FlameTimer != null) {
			FlameTimer.cancel(true);
		}
	}

	protected void sp(final int npcId, final float x, final float y, final float z, final byte h, final int time, final String walkerId, final boolean running) {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (!isInstanceDestroyed) {
					Npc npc = (Npc) spawn(npcId, x, y, z, h);
					npc.getSpawn().setWalkerId(walkerId);
					WalkManager.startWalking((NpcAI2) npc.getAi2());
					if (running) {
						npc.setState(1);
					}
					else {
						npc.setState(CreatureState.WALKING);
					}
					PacketSendUtility.broadcastPacket(npc, new SM_EMOTION(npc, EmotionType.START_EMOTE2, 0, npc.getObjectId()));

				}
			}
		}, time);
	}

	protected void openDoor(int doorId) {
		StaticDoor door = doors.get(doorId);
		if (door != null) {
			door.setOpen(true);
		}
	}

	// private void sendMsg(final String str) {
	// instance.doOnAllPlayers(new Visitor<Player>() {
	// @Override
	// public void visit(Player player) {
	// PacketSendUtility.sendMessage(player, str);
	// }
	// });
	// }

	@Override
	public void onLeaveInstance(Player player) {
		removeItems(player);
		removeEffects(player);
		stopInstanceTask();
	}

	@Override
	public void onInstanceDestroy() {
		isInstanceDestroyed = true;
		doors.clear();
		movies.clear();
		stopInstanceTask();
	}

	@Override
	public void onPlayerLogOut(Player player) {
		removeItems(player);
		removeEffects(player);
		switch (player.getRace()) {
			case ELYOS:
				TeleportService2.teleportTo(player, 210070000, 77, 2758, 229);
				break;
			case ASMODIANS:
				TeleportService2.teleportTo(player, 210080000, 2991, 375, 309);
				break;
			default:
				break;
		}
	}

	@Override
	public void onExitInstance(Player player) {
		switch (player.getRace()) {
			case ELYOS:
				TeleportService2.teleportTo(player, 210070000, 77, 2758, 229);
				break;
			case ASMODIANS:
				TeleportService2.teleportTo(player, 210080000, 2991, 375, 309);
				break;
			default:
				break;
		}

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
		return TeleportService2.teleportTo(player, 210070000, 301390000, 320.5971f, 183.0153f, 1687.2552f, (byte) 0);
	}

	@Override
	public boolean onDie(final Player player, Creature lastAttacker) {
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);
		PacketSendUtility.sendPacket(player, new SM_DIE(player.haveSelfRezEffect(), player.haveSelfRezItem(), 0, 8));
		return true;
	}
}
