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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.actions.CreatureActions;
import com.aionemu.gameserver.model.drop.DropItem;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.instance.InstanceScoreType;
import com.aionemu.gameserver.model.instance.StageType;
import com.aionemu.gameserver.model.instance.playerreward.CruciblePlayerReward;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INSTANCE_SCORE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INSTANCE_STAGE_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;

/**
 * @author xTz
 * @modified Luzien
 */
@InstanceID(300300000)
public class EmpyreanCrucibleInstance extends CrucibleInstance {

	private List<Npc> npcs = new ArrayList<Npc>();
	private List<EmpyreanStage> emperyanStage = new ArrayList<EmpyreanStage>();
	private byte stage;
	private boolean isDoneStage4 = false;
	private boolean isDoneStage6Round2 = false;
	private boolean isDoneStage6Round1 = false;

	private class EmpyreanStage {

		private List<Npc> npcs = new ArrayList<Npc>();

		public EmpyreanStage(List<Npc> npcs) {
			this.npcs = npcs;
		}

		private boolean containNpc() {
			for (Npc npc : npcs) {
				if (instance.getNpcs().contains(npc)) {
					return true;
				}
			}
			return false;
		}
	}

	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
		stage = 0;
		sp(799567, 345.25107f, 349.40176f, 96.09097f, (byte) 0);
		sp(799573, 384.51f, 352.61078f, 96.747635f, (byte) 83);
	}

	@Override
	public void onEnterInstance(Player player) {
		boolean isNew = !instanceReward.containPlayer(player.getObjectId());
		super.onEnterInstance(player);
		if (isNew && stage > 0) {
			moveToReadyRoom(player); // send player to team and wait for end of the stage
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400963));
		}
		CruciblePlayerReward playerReward = getPlayerReward(player.getObjectId());
		if (playerReward.isPlayerLeave()) {
			onExitInstance(player);
			return;
		}
		else if (playerReward.isRewarded()) {
			doReward(player);
		}
		PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(instanceReward));
		PacketSendUtility.sendPacket(player, new SM_INSTANCE_STAGE_INFO(2, stageType.getId(), stageType.getType()));

	}

	private void sendPacket(final int points, final int nameId) {
		instance.doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				if (player.isOnline()) {
					CruciblePlayerReward playerReward = getPlayerReward(player.getObjectId());
					if (nameId != 0) {
						PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400237, new DescriptionId(nameId * 2 + 1), points));
					}
					if (!playerReward.isRewarded()) {
						playerReward.addPoints(points);
					}
					PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(instanceReward));
				}
			}
		});
	}

	private void sendEventPacket(final StageType type, final int time) {
		this.stageType = type;
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				instance.doOnAllPlayers(new Visitor<Player>() {

					@Override
					public void visit(Player player) {
						PacketSendUtility.sendPacket(player, new SM_INSTANCE_STAGE_INFO(2, type.getId(), type.getType()));
					}
				});
			}
		}, time);
	}

	@Override
	public void onDie(Npc npc) {
		if (npcs.contains(npc)) {
			npcs.remove(npc);
		}
		EmpyreanStage es = getEmpyreanStage(npc);
		int point = 0;
		switch (npc.getNpcId()) {
			case 217511:
			case 217512:
			case 217513:
			case 217514:
				point += 55;
				break;
			case 217518:
				point += 110;
				break;
			case 217515:
			case 217516:
			case 217517:
				point += 120;
				break;
			case 217522:
				point += 250;
				break;
			case 217560:
			case 217561:
				point += 260;
				break;
			case 217557:
			case 217559:
			case 217558:
				point += 280;
				break;
			case 217519:
			case 217520:
			case 217521:
			case 217504:
			case 217507:
			case 217508:
			case 217547:
			case 217548:
			case 217549:
			case 217550:
			case 217545:
			case 217569:
				point += 300;
				break;
			case 217565:
				point += 350;
				break;
			case 217562:
				point += 360;
				break;
			case 217502:
			case 217505:
			case 217563:
			case 217566:
				point += 400;
				break;
			case 217654:
				point += 455;
				break;
			case 217523:
			case 217524:
			case 217525:
			case 217526:
				point += 475;
				break;
			case 217568:
				point += 480;
				break;
			case 217503:
			case 217489:
			case 217653:
				point += 500;
				break;
			case 217486:
			case 217564:
				point += 600;
				break;
			case 217652:
				point += 685;
				break;
			case 217570:
				point += 700;
				break;
			case 217492:
				point += 1100;
				break;
			case 217487:
				point += 1200;
				break;
			case 217491:
				point += 1300;
				break;
			case 217509:
				point += 1500;
				break;
			case 217552:
				point += 1900;
				break;
			case 217553:
				point += 2000;
				break;
			case 217567:
				point += 2100;
				break;
			case 217554:
				point += 2400;
				break;
			case 217578:
			case 217582:
				point += 2900;
				break;
			case 217579:
			case 217583:
				point += 3100;
				break;
			case 217527:
			case 217528:
				point += 3250;
				break;
			case 217572:
				point += 3370;
				break;
			case 217585:
				point += 3400;
				break;
			case 217580:
			case 217584:
				point += 3800;
				break;
			case 217588:
				point += 4000;
				break;
			case 217590:
				point += 4200;
				break;
			case 217597: // TODO: confirm
			case 217596:
				point += 4250;
				break;
			case 217493:
				point += 5000;
				break;
			case 217510:
			case 217501:
				point += 5500;
				break;
			case 217591:
				point += 6000;
				break;
			case 217594:
				point += 6200;
				break;
			case 217595:
				point += 7300;
				break;
			case 217556:
				point += 7800;
				break;
			case 217573:
				point += 8900;
				break;
			case 217587:
				point += 11500;
				break;
			case 217593:
				point += 17000;
				break;
		}

		if (point != 0) {
			sendPacket(point, npc.getObjectTemplate().getNameId());
		}
		switch (npc.getNpcId()) {
			case 217486:
				despawnNpc(npc);
				if (stageType == StageType.START_STAGE_1_ROUND_1 && getNpc(217489) == null) {
					startStage1Round2();
				}
				break;
			case 217489:
				despawnNpc(npc);
				if (stageType == StageType.START_STAGE_1_ROUND_1 && getNpc(217486) == null) {
					startStage1Round2();
				}
				break;
			case 217492:
				despawnNpc(npc);
				if (stageType == StageType.START_STAGE_1_ROUND_2) {
					startStage1Round3();
				}
				break;
			case 217487:
				despawnNpc(npc);
				if (stageType == StageType.START_STAGE_1_ROUND_3) {
					startStage1Round4();
				}
				break;
			case 217491:
				despawnNpc(npc);
				if (stageType == StageType.START_STAGE_1_ROUND_4) {
					startStage1Round5();
				}
				break;
			case 217493:
				sendEventPacket(StageType.PASS_GROUP_STAGE_1, 0);
				sp(217756, 342.65106f, 357.4013f, 96.09094f, (byte) 0);
				sp(217756, 349.07376f, 357.5898f, 96.090965f, (byte) 0);
				sp(217756, 345.69272f, 359.40958f, 96.09094f, (byte) 0);
				sp(217756, 342.59192f, 353.73386f, 96.090965f, (byte) 0);
				sp(217756, 342.6043f, 360.8932f, 96.09093f, (byte) 0);
				sp(217756, 345.69318f, 355.56677f, 96.09094f, (byte) 0);
				sp(799568, 345.25f, 349.24f, 96.09097f, (byte) 0);
				despawnNpc(npc);
				break;
			case 217502:
				despawnNpc(npc);
				switch (stageType) {
					case START_STAGE_2_ROUND_1:
						if (getNpc(217503) == null && getNpc(217504) == null) {
							startStage2Round2();
						}
						break;
					case START_STAGE_2_ROUND_2:
						if (getNpc(217508) == null && getNpc(217507) == null && getNpc(217504) == null) {
							startStage2Round3();
						}
						break;
					default:
						break;
				}
				break;
			case 217503:
				despawnNpc(npc);
				if (stageType == StageType.START_STAGE_2_ROUND_1 && getNpc(217502) == null && getNpc(217504) == null) {
					startStage2Round2();
				}
				break;
			case 217504:
				despawnNpc(npc);
				switch (stageType) {
					case START_STAGE_2_ROUND_1:
						if (getNpc(217502) == null && getNpc(217503) == null) {
							startStage2Round2();
						}
						break;
					case START_STAGE_2_ROUND_2:
						if (getNpc(217502) == null && getNpc(217507) == null && getNpc(217508) == null) {
							startStage2Round3();
						}
						break;
					default:
						break;
				}
				break;
			case 217507:
				despawnNpc(npc);
				switch (stageType) {
					case START_STAGE_2_ROUND_2:
						if (getNpc(217502) == null && getNpc(217504) == null && getNpc(217508) == null) {
							startStage2Round3();
						}
						break;
					default:
						break;
				}
				if (es != null && !es.containNpc()) {
					startStage2Round5();
				}
				break;
			case 217508:
				despawnNpc(npc);
				if (es != null) {
					return;
				}
				switch (stageType) {
					case START_STAGE_2_ROUND_2:
						if (getNpc(217502) == null && getNpc(217504) == null && getNpc(217507) == null) {
							startStage2Round3();
						}
						break;
					case START_STAGE_2_ROUND_4:
						if (getNpc(217505) == null) {
							startStage4Round4_1();
						}
						break;
					default:
						break;
				}
				break;
			case 217509:
				despawnNpc(npc);
				sendEventPacket(StageType.START_STAGE_2_ROUND_4, 2000);
				sp(217505, 341.95056f, 334.77692f, 96.09093f, (byte) 0, 6000);
				sp(217508, 344.17813f, 334.42462f, 96.090935f, (byte) 0, 6000);
				break;
			case 217505:
				despawnNpc(npc);
				if (getNpc(217508) == null) {
					startStage4Round4_1();
				}
				break;
			case 217506:
				despawnNpc(npc);
				if (es != null && !es.containNpc()) {
					startStage2Round5();
				}
				break;
			case 217510:
			case 217501:
				despawnNpc(npc);
				sendEventPacket(StageType.PASS_GROUP_STAGE_2, 0);
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						sp(217737, 334.49496f, 349.2322f, 96.090935f, (byte) 0);
						sendEventPacket(StageType.START_BONUS_STAGE_2, 0);

						ThreadPoolManager.getInstance().schedule(new Runnable() {

							@Override
							public void run() {
								if (getNpc(217737) != null) {
									sendMsg(1400979);
									ThreadPoolManager.getInstance().schedule(new Runnable() {

										@Override
										public void run() {
											if (getNpc(217737) != null) {
												despawnNpc(getNpc(217737));
												sp(799569, 345.25f, 349.24f, 96.09097f, (byte) 0);
											}
										}
									}, 30000);
								}
							}
						}, 30000);
					}
				}, 8000);
				break;
			case 217737:
				despawnNpc(npc);
				sp(799569, 345.25f, 349.24f, 96.09097f, (byte) 0);
				break;
			case 217511:
			case 217512:
			case 217513:
			case 217514:
				despawnNpc(npc);
				if (getNpcs(217511).isEmpty() && getNpcs(217512).isEmpty() && getNpcs(217513).isEmpty() && getNpcs(217514).isEmpty()) {
					sendEventPacket(StageType.START_STAGE_3_ROUND_2, 2000);
					sp(217515, 336.32092f, 345.0251f, 96.090935f, (byte) 0, 6000);
					sp(217516, 347.16144f, 361.89084f, 96.09093f, (byte) 0, 6000);
					sp(217518, 352.77557f, 360.97845f, 96.09091f, (byte) 0, 6000);
					sp(217518, 340.2231f, 351.10208f, 96.09098f, (byte) 0, 6000);
					sp(217517, 354.132f, 337.14255f, 96.09089f, (byte) 0, 6000);
					sp(217517, 353.7888f, 354.4324f, 96.091064f, (byte) 0, 6000);
					sp(217516, 350.0108f, 342.09482f, 96.090935f, (byte) 0, 6000);
					sp(217515, 349.16327f, 335.63864f, 96.09095f, (byte) 0, 6000);
					sp(217517, 341.23633f, 344.55603f, 96.09096f, (byte) 0, 6000);
					sp(217518, 354.66513f, 343.31537f, 96.091095f, (byte) 0, 6000);
					sp(217516, 334.60898f, 352.01447f, 96.09095f, (byte) 0, 6000);
					sp(217515, 348.87338f, 354.90146f, 96.09096f, (byte) 0, 6000);
				}
				break;
			case 217515:
			case 217516:
			case 217517:
			case 217518:
				despawnNpc(npc);
				if (getNpcs(217515).isEmpty() && getNpcs(217516).isEmpty() && getNpcs(217517).isEmpty() && getNpcs(217518).isEmpty()) {
					sendEventPacket(StageType.START_STAGE_3_ROUND_3, 2000);
					sp(217519, 351.08026f, 341.61298f, 96.090935f, (byte) 0, 6000);
					sp(217521, 333.4532f, 354.7357f, 96.09094f, (byte) 0, 6000);
					sp(217522, 342.1805f, 360.534f, 96.09092f, (byte) 0, 6000);
					sp(217520, 334.2686f, 342.60797f, 96.09091f, (byte) 0, 6000);
					sp(217522, 350.34537f, 356.18558f, 96.09094f, (byte) 0, 6000);
					sp(217520, 343.7485f, 336.2869f, 96.09092f, (byte) 0, 6000);
				}
				break;
			case 217519:
			case 217520:
			case 217521:
			case 217522:
				despawnNpc(npc);
				if (getNpcs(217519).isEmpty() && getNpcs(217520).isEmpty() && getNpcs(217521).isEmpty() && getNpcs(217522).isEmpty()) {
					sendEventPacket(StageType.START_STAGE_3_ROUND_4, 2000);
					sp(217524, 349.66446f, 341.4752f, 96.090965f, (byte) 0, 6000);
					sp(217525, 338.32742f, 356.29636f, 96.090935f, (byte) 0, 6000);
					sp(217526, 349.31473f, 358.43762f, 96.09096f, (byte) 0, 6000);
					sp(217523, 338.73138f, 342.35876f, 96.09094f, (byte) 0, 6000);
				}
				break;
			case 217523:
			case 217524:
			case 217525:
			case 217526:
				despawnNpc(npc);
				if (getNpcs(217523).isEmpty() && getNpcs(217524).isEmpty() && getNpcs(217525).isEmpty() && getNpcs(217526).isEmpty()) {
					sendEventPacket(StageType.START_STAGE_3_ROUND_5, 2000);
					sp(217527, 335.37524f, 346.34567f, 96.09094f, (byte) 0, 6000);
					sp(217528, 335.36105f, 353.16922f, 96.09094f, (byte) 0, 6000);
				}
				break;
			case 217527:
			case 217528:
				despawnNpc(npc);
				if (getNpcs(217527).isEmpty() && getNpcs(217528).isEmpty()) {
					sendEventPacket(StageType.START_BONUS_STAGE_3, 7000);
					sp(217744, 342.45215f, 349.339f, 96.09096f, (byte) 0, 7000);
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							startBonusStage3();
						}
					}, 39000);
				}
				break;
			case 217557:
			case 217559:
			case 217562:
				despawnNpc(npc);
				switch (stageType) {
					case START_STAGE_4_ROUND_1:
						if (getNpcs(217557).isEmpty() && getNpcs(217559).isEmpty()) {
							sp(217558, 330.27792f, 339.2779f, 96.09093f, (byte) 6);
							sp(217558, 328.08972f, 346.3553f, 96.090904f, (byte) 1);
						}
						break;
					case START_STAGE_4_ROUND_2:
						if (es != null && !es.containNpc()) {
							startStage4Round3();
						}
						break;
					default:
						break;
				}
				break;
			case 217558:
			case 217561:
				despawnNpc(npc);
				switch (stageType) {
					case START_STAGE_4_ROUND_1:
						if (getNpcs(217558).isEmpty()) {
							sendEventPacket(StageType.START_STAGE_4_ROUND_2, 2000);
							sp(217559, 330.53665f, 349.23523f, 96.09093f, (byte) 0, 6000);
							sp(217562, 334.89508f, 363.78442f, 96.090904f, (byte) 105, 6000);
							sp(217560, 334.61942f, 334.80353f, 96.090904f, (byte) 15, 6000);
							ThreadPoolManager.getInstance().schedule(new Runnable() {

								@Override
								public void run() {
									List<Npc> round = new ArrayList<Npc>();
									round.add(sp(217557, 357.24625f, 338.30093f, 96.09104f, (byte) 65));
									round.add(sp(217558, 357.20663f, 359.28714f, 96.091064f, (byte) 75));
									round.add(sp(217561, 365.109f, 349.1218f, 96.09114f, (byte) 60));
									emperyanStage.add(new EmpyreanStage(round));
								}
							}, 47000);

						}
						break;
					case START_STAGE_4_ROUND_2:
						if (es != null && !es.containNpc()) {
							startStage4Round3();
						}
						break;
					default:
						break;
				}
				break;
			case 217563:
			case 217566:
				despawnNpc(npc);
				if (es != null && !es.containNpc()) {
					sendEventPacket(StageType.START_STAGE_4_ROUND_4, 2000);
					sp(217567, 345.73895f, 349.49786f, 96.09097f, (byte) 0, 6000);
				}
				break;
			case 217564:
			case 217565:
			case 217560:
			case 217745:
			case 217746:
			case 217747:
			case 217748:
			case 205413:
			case 205414:
			case 217576:
			case 217577:
				despawnNpc(npc);
				break;
			case 217567:
				despawnNpc(npc);
				sendEventPacket(StageType.START_STAGE_4_ROUND_5, 2000);
				sp(217653, 327.76917f, 349.26215f, 96.09092f, (byte) 0, 6000);
				sp(217651, 364.8972f, 349.25653f, 96.09114f, (byte) 60, 18000);
				sp(217652, 361.1795f, 339.99252f, 96.09112f, (byte) 50, 35000);
				sp(217653, 354.4119f, 333.6749f, 96.09091f, (byte) 40, 54000);
				sp(217651, 331.61502f, 358.4374f, 96.09091f, (byte) 110, 69000);
				sp(217652, 338.38858f, 364.91507f, 96.090904f, (byte) 100, 83000);
				sp(217651, 346.39847f, 368.19427f, 96.090904f, (byte) 90, 99000);
				sp(217652, 353.92606f, 364.92636f, 96.090904f, (byte) 80, 110000);
				sp(217653, 361.13452f, 358.90424f, 96.091156f, (byte) 65, 130000);
				sp(217652, 346.34402f, 329.9449f, 96.09091f, (byte) 30, 142000);
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						sp(217653, 331.53894f, 339.8832f, 96.09091f, (byte) 10);
						isDoneStage4 = true;
					}
				}, 174000);
				break;
			case 217651:
			case 217652:
			case 217653:
				despawnNpc(npc);
				if (isDoneStage4 && getNpcs(217651).isEmpty() && getNpcs(217652).isEmpty() && getNpcs(217653).isEmpty()) {
					sendEventPacket(StageType.PASS_GROUP_STAGE_4, 0);
					sp(217749, 340.59f, 349.32166f, 96.09096f, (byte) 0, 6000);
					sendEventPacket(StageType.START_BONUS_STAGE_4, 6000);
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							startBonusStage4();
						}
					}, 33000);
				}
				break;
			case 217547:
			case 217548:
			case 217549:
				despawnNpc(npc);
				if (getNpcs(217547).isEmpty() && getNpcs(217548).isEmpty() && getNpcs(217549).isEmpty()) {
					sp(217550, 1266.293f, 778.3254f, 358.60574f, (byte) 30, 4000);
					sp(217545, 1254.261f, 778.3817f, 358.6056f, (byte) 30, 4000);
				}
				break;
			case 217550:
			case 217545:
				despawnNpc(npc);
				if (getNpcs(217550).isEmpty() && getNpcs(217545).isEmpty()) {
					sendEventPacket(StageType.START_STAGE_5_ROUND_2, 2000);
					sp(217552, 1246.0197f, 788.8341f, 358.60556f, (byte) 11, 6000);
				}
				break;
			case 217552:
				despawnNpc(npc);
				sendEventPacket(StageType.START_STAGE_5_ROUND_3, 2000);
				sp(700527, 1253.3123f, 789.38385f, 358.60562f, (byte) 119);
				sp(700528, 1260.2015f, 800.14886f, 358.6056f, (byte) 16);
				sp(217553, 1259.4706f, 812.30505f, 358.6056f, (byte) 30, 6000);
				break;
			case 217553:
				despawnNpc(npc);
				despawnNpcs(getNpcs(700527));
				despawnNpcs(getNpcs(700528));
				sp(281111, 1246.4855f, 796.90735f, 358.6056f, (byte) 0);
				sp(281110, 1259.5508f, 784.5548f, 358.60562f, (byte) 0);
				sp(281112, 1276.6561f, 812.5499f, 358.60565f, (byte) 0);
				sp(281322, 1243.2113f, 813.0927f, 358.60565f, (byte) 0);
				sp(281109, 1272.9266f, 797.1055f, 358.60562f, (byte) 0);
				sp(281113, 1275.894f, 780.51544f, 358.60565f, (byte) 0);
				sp(281108, 1260.003f, 810.555f, 358.6056f, (byte) 0);
				sp(281114, 1244.3293f, 780.4284f, 358.60562f, (byte) 0);
				sendEventPacket(StageType.START_STAGE_5_ROUND_4, 2000);
				sp(217554, 1243.1877f, 796.79553f, 358.6056f, (byte) 0, 6000);
				break;
			case 217554:
				despawnNpc(npc);
				despawnNpc(getNpc(281108));
				despawnNpc(getNpc(281109));
				despawnNpc(getNpc(281110));
				despawnNpc(getNpc(281111));
				despawnNpc(getNpc(281112));
				despawnNpc(getNpc(281113));
				despawnNpc(getNpc(281114));
				despawnNpc(getNpc(281322));
				sendEventPacket(StageType.START_STAGE_5_ROUND_5, 2000);
				sp(217556, 1259.8387f, 785.6266f, 358.60562f, (byte) 30, 6000);
				sp(281191, 1261.8f, 804.5f, 358.7f, (byte) 0, 6000);
				sp(281192, 1267.6f, 793.9f, 358.7f, (byte) 0, 6000);
				sp(281193, 1257.4f, 787.9f, 358.7f, (byte) 0, 6000);
				sp(281194, 1251.3f, 798.6f, 358.7f, (byte) 0, 6000);
				break;
			case 217556:
				despawnNpc(npc);
				despawnNpc(getNpc(281191));
				despawnNpc(getNpc(281192));
				despawnNpc(getNpc(281193));
				despawnNpc(getNpc(281194));
				sendEventPacket(StageType.PASS_GROUP_STAGE_5, 0);
				sp(205339, 1260.1465f, 795.07495f, 358.60562f, (byte) 30);
				break;
			case 217568:
				sp(205413, npc.getX(), npc.getY(), npc.getZ(), npc.getHeading());
				despawnNpc(npc);
				if (isDoneStage6Round1 && getNpcs(217568).isEmpty()) {
					sendEventPacket(StageType.START_STAGE_6_ROUND_2, 2000);
					sp(217570, 1629.4642f, 154.8044f, 126f, (byte) 30, 6000);
					sp(217569, 1643.7776f, 161.63562f, 126f, (byte) 46, 6000);
					sp(217569, 1639.7843f, 142.09268f, 126f, (byte) 40, 6000);
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							sp(217569, 1614.6377f, 164.04999f, 126.00113f, (byte) 3);
							sp(217569, 1625.8965f, 135.62509f, 126f, (byte) 30);
							isDoneStage6Round2 = true;
						}
					}, 12000);
				}
				break;
			case 217570:
				for (int i = 0; i < 5; i++) {
					sp(211984, npc.getX() + Rnd.get(-2, 2), npc.getY() + Rnd.get(-2, 2), npc.getZ(), npc.getHeading());
				}
			case 217569:
				sp(205414, npc.getX(), npc.getY(), npc.getZ(), npc.getHeading());
				despawnNpc(npc);
				if (stageType == StageType.START_STAGE_6_ROUND_2 && isDoneStage6Round2 && getNpcs(217569).isEmpty() && getNpcs(217570).isEmpty()) {
					sendEventPacket(StageType.START_STAGE_6_ROUND_3, 2000);
					sp(217572, 1629.5837f, 138.38435f, 126f, (byte) 30, 9000);
					sp(217569, 1635.01535f, 150.01535f, 126f, (byte) 45, 6000);
					sp(217569, 1638.3817f, 152.84074f, 126f, (byte) 45, 6000);
				}
				break;
			case 217572:
				despawnNpc(npc);
				sp(217573, 1634.7891f, 141.99077f, 126f, (byte) 0);
				sendEventPacket(StageType.START_STAGE_6_ROUND_4, 2000);
				break;
			case 217573:
				despawnNpc(npc);
				sendEventPacket(StageType.PASS_GROUP_STAGE_6, 0);
				// sendMsg(1400977);
				// sendMsg(1400977);
				// TODO: 2 boxes ready room!
				sp(217750, 1624.1908f, 155.16148f, 126f, (byte) 0, 8000);
				sendEventPacket(StageType.START_BONUS_STAGE_6, 8000);
				break;
			case 217750:
				sp(205340, 1625.08f, 159.15f, 126f, (byte) 0);
				break;
			case 217582:
			case 217578:
				sendEventPacket(StageType.START_STAGE_7_ROUND_2, 2000);
				sp(217579, 1794.81f, 779.53925f, 469.35016f, (byte) 40, 6000);
				// sp(217583, 1794.81f, 779.53925f, 469.35016f, (byte) 40, 6000);
				break;
			case 217579:
			case 217583:
				sendEventPacket(StageType.START_STAGE_7_ROUND_3, 2000);
				sp(217580, 1775.6254f, 811.43225f, 469.35022f, (byte) 100, 6000);
				// sp(217584, 1775.6254f, 811.43225f, 469.35022f, (byte) 100, 6000);
				break;
			case 217580:
			case 217584:
				sendEventPacket(StageType.START_STAGE_7_ROUND_4, 2000);
				sp(217581, 1775.716f, 779.630f, 469.564f, (byte) 20, 6000);
				// sp(217585, 1775.716f, 779.630f, 469.564f, (byte) 20, 6000);
				break;
			case 217581:
			case 217585:
				sendEventPacket(StageType.START_STAGE_7_ROUND_5, 2000);
				sp(217586, 1773.194f, 796.537f, 469.350f, (byte) 0, 6000);
				// sp(217587, 1773.194f, 796.537f, 469.350f, (byte) 0, 6000);
				break;
			case 217586:
			case 217587:
				sendEventPacket(StageType.PASS_GROUP_STAGE_7, 0);
				sp(217753, 1782.881f, 800.114f, 469.420f, (byte) 0, 2000);
				sendMsg(1400983);
				sp(205341, 1781.610f, 796.920f, 469.350f, (byte) 0, 2000);
				break;
			case 217588:
				sendEventPacket(StageType.START_STAGE_8_ROUND_2, 2000);
				sp(217590, 1764.377f, 1761.510f, 303.695f, (byte) 0, 6000);
				break;
			case 217590:
				sendEventPacket(StageType.START_STAGE_8_ROUND_3, 2000);
				sp(217591, 1776.946f, 1749.255f, 303.696f, (byte) 30, 6000);
				break;
			case 217591:
				sendEventPacket(StageType.START_STAGE_8_ROUND_4, 2000);
				sp(217592, 1790.693f, 1761.911f, 303.877f, (byte) 60, 6000);
				break;
			case 217592:
				sendEventPacket(StageType.START_STAGE_8_ROUND_5, 2000);
				sp(280790, npc.getX(), npc.getY(), npc.getZ(), npc.getHeading());
				sp(217593, 1777.065f, 1763.706f, 303.695f, (byte) 90, 6000);
				break;
			case 217593:
				sendEventPacket(StageType.PASS_GROUP_STAGE_8, 0);
				sp(205342, 1776.757f, 1764.624f, 303.695f, (byte) 90);
				break;
			case 217594:
				sendEventPacket(StageType.START_STAGE_9_ROUND_2, 2000);
				sp(217595, 1322.311f, 1741.508f, 316.349f, (byte) 65, 6000);
				break;
			case 217595:
				sendEventPacket(StageType.START_STAGE_9_ROUND_3, 2000);
				sp(217596, 1308.038f, 1729.718f, 315.996f, (byte) 36, 6000);
				sp(217597, 1302.290f, 1745.471f, 316.092f, (byte) 96, 6000);
				break;
			case 217596:
			case 217597:
				Npc counterpart = getNpc(npc.getNpcId() == 217596 ? 217597 : 217596);
				if (counterpart != null && !CreatureActions.isAlreadyDead(counterpart)) {
					SkillEngine.getInstance().getSkill(counterpart, 19624, 10, counterpart).useNoAnimationSkill();
				}
				despawnNpc(npc);
				if (getNpcs(217596).isEmpty() && getNpcs(217597).isEmpty()) {
					// sendEventPacket(StageType.START_STAGE_9_ROUND_4, 2000);
					rewardGroup();// Finish for now, TODO: continue
				}
		}
	}

	private void startBonusStage3() {
		sp(217742, 360.76f, 349.42f, 96.1f, (byte) 0);
		sp(217743, 346.27f, 363.35f, 96.1f, (byte) 11);
		sp(217740, 332.12f, 349.22f, 96.1f, (byte) 0);
		sp(217741, 346.42f, 335.1f, 96.1f, (byte) 87);

		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				sendMsg(1401010); // 30 sec
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						sendMsg(1401011); // 10 sec
						ThreadPoolManager.getInstance().schedule(new Runnable() {

							@Override
							public void run() {
								sendMsg(1401012); // 5 sec
								ThreadPoolManager.getInstance().schedule(new Runnable() {

									@Override
									public void run() {
										despawnNpc(getNpc(217740));
										despawnNpc(getNpc(217741));
										despawnNpc(getNpc(217742));
										despawnNpc(getNpc(217743));
										sp(205331, 345.25f, 349.24f, 96.09097f, (byte) 0);
										sp(217735, 378.9331f, 346.74878f, 96.74762f, (byte) 0);
										sendMsg(1400976); // A Ticket Box appeared
										despawnNpc(getNpc(217744));
									}
								}, 5000);
							}
						}, 5000);
					}
				}, 20000);
			}
		}, 30000);
	}

	private void startBonusStage4() {
		sp(217778, 346.2f, 366.85f, 96.55f, (byte) 1);
		sp(217747, 346.2204f, 367.52002f, 96.090904f, (byte) 60, 3000);
		sp(217747, 346.2204f, 367.52002f, 96.090904f, (byte) 60, 6000);
		sp(217748, 346.65222f, 366.3634f, 96.09092f, (byte) 60, 9000);
		sp(217748, 346.65222f, 366.3634f, 96.09092f, (byte) 60, 12000);
		sp(217748, 345.7578f, 366.7986f, 96.09094f, (byte) 60, 15000);
		sp(217748, 345.863f, 367.471f, 96.09094f, (byte) 60, 18000);
		sp(217747, 346.9996f, 366.3978f, 96.09092f, (byte) 60, 21000);
		sp(217746, 345.7872f, 366.3056f, 96.09092f, (byte) 60, 24000);
		sp(217745, 346.4504f, 367.8004f, 96.09094f, (byte) 60, 27000);
		sp(217747, 346.75043f, 367.467f, 96.09094f, (byte) 60, 30000);
		sp(217747, 346.535f, 367.3128f, 96.090904f, (byte) 60, 33000);
		sp(217747, 345.8452f, 367.2468f, 96.09092f, (byte) 60, 36000);
		sp(217746, 345.428f, 366.2954f, 96.09093f, (byte) 60, 39000);
		sp(217747, 346.71082f, 366.7156f, 96.090904f, (byte) 60, 42000);
		sp(217747, 346.38782f, 366.1606f, 96.09093f, (byte) 60, 45000);
		sp(217747, 345.36002f, 366.0284f, 96.09093f, (byte) 60, 48000);
		sp(217747, 345.5378f, 366.1876f, 96.09092f, (byte) 60, 51000);
		sp(217747, 346.5176f, 365.8592f, 96.09092f, (byte) 60, 54000);
		sp(217745, 345.8434f, 367.8082f, 96.090904f, (byte) 60, 57000);
		sp(217747, 345.297f, 366.3014f, 96.09093f, (byte) 60, 60000);
		// 30 sec
		sp(217747, 346.0346f, 367.2426f, 96.090904f, (byte) 60, 63000);
		sp(217747, 345.52863f, 366.62622f, 96.09092f, (byte) 60, 66000);
		sp(217745, 345.80862f, 366.9388f, 96.09092f, (byte) 60, 69000);
		sp(217747, 346.393f, 366.9766f, 96.090904f, (byte) 60, 72000);
		sp(217746, 345.5726f, 366.3462f, 96.09092f, (byte) 60, 75000);
		sp(217745, 345.2004f, 366.36902f, 96.09092f, (byte) 60, 78000);
		sp(217746, 346.2528f, 365.9208f, 96.09093f, (byte) 60, 81000);
		sp(217747, 346.0686f, 366.9096f, 96.090904f, (byte) 60, 84000);
		// 10 sec
		sp(217746, 345.4606f, 367.14862f, 96.090904f, (byte) 60, 87000);
		sp(217747, 345.8016f, 367.7212f, 96.090904f, (byte) 60, 90000);
		sp(217747, 347.1144f, 365.875f, 96.09092f, (byte) 60, 93000);
		// 5 sec
		sp(217747, 345.3226f, 367.7414f, 96.0909f, (byte) 60, 96000);
		sp(217747, 345.4836f, 367.3886f, 96.090904f, (byte) 60, 99000);
		sp(217747, 345.80862f, 366.0682f, 96.09092f, (byte) 60, 102000);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				despawnNpcs(getNpcs(217745));
				despawnNpcs(getNpcs(217746));
				despawnNpcs(getNpcs(217747));
				despawnNpcs(getNpcs(217748));
				despawnNpc(getNpc(217749));
				despawnNpc(getNpc(217778));
				sp(205338, 345.25f, 349.24f, 96.09097f, (byte) 0);
			}
		}, 102000);
	}

	private void startStage4Round4_1() {
		List<Npc> round = new ArrayList<Npc>();
		round.add(sp(217508, 334.06754f, 339.84393f, 96.09091f, (byte) 0));
		emperyanStage.add(new EmpyreanStage(round));
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				List<Npc> round1 = new ArrayList<Npc>();
				round1.add(sp(217506, 342.12405f, 364.4922f, 96.09093f, (byte) 0));
				round1.add(sp(217507, 344.4953f, 365.14444f, 96.09092f, (byte) 0));
				emperyanStage.add(new EmpyreanStage(round1));
			}
		}, 5000);
	}

	private void startStage4Round3() {
		sendEventPacket(StageType.START_STAGE_4_ROUND_3, 2000);
		sp(217563, 339.70975f, 333.54272f, 96.090904f, (byte) 20, 6000);
		sp(217564, 342.92892f, 333.43994f, 96.09092f, (byte) 18, 6000);
		sp(217565, 341.55396f, 330.70847f, 96.09093f, (byte) 23, 16000);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				List<Npc> round = new ArrayList<Npc>();
				round.add(sp(217566, 362.87164f, 357.87164f, 96.091125f, (byte) 73));
				round.add(sp(217563, 359.1135f, 359.6953f, 96.091125f, (byte) 80));
				emperyanStage.add(new EmpyreanStage(round));
			}
		}, 43000);
	}

	private void startStage2Round2() {
		sendEventPacket(StageType.START_STAGE_2_ROUND_2, 2000);
		sp(217502, 328.78433f, 348.77353f, 96.09092f, (byte) 0, 6000);
		sp(217508, 329.01874f, 343.79257f, 96.09092f, (byte) 0, 6000);
		sp(217507, 329.2849f, 355.2314f, 96.090935f, (byte) 0, 6000);
		sp(217504, 328.90808f, 351.6184f, 96.09092f, (byte) 0, 6000);
	}

	private void startStage2Round3() {
		sendEventPacket(StageType.START_STAGE_2_ROUND_3, 2000);
		sp(217509, 332.24298f, 349.49286f, 96.090935f, (byte) 0, 6000);
	}

	private void startStage2Round5() {
		sendEventPacket(StageType.START_STAGE_2_ROUND_5, 2000);
		sp(Rnd.get(1, 2) == 1 ? 217510 : 217501, 332.0035f, 349.55893f, 96.09093f, (byte) 0, 6000);
	}

	private void rewardGroup() {
		for (Player p : instance.getPlayersInside()) {
			doReward(p);
		}
	}

	@Override
	public void doReward(Player player) {
		CruciblePlayerReward playerReward = getPlayerReward(player.getObjectId());
		if (playerReward == null) {
			return;
		}
		float reward = 0.05f * playerReward.getPoints() + 500;
		if (!playerReward.isRewarded()) {
			playerReward.setRewarded();
			playerReward.setInsignia((int) reward);
			ItemService.addItem(player, 186000130, (int) reward);
		}
		else {
			TeleportService2.teleportTo(player, mapId, player.getInstanceId(), 381.41684f, 346.78162f, 96.74763f, (byte) 43);
		}

		PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(instanceReward, InstanceScoreType.END_PROGRESS));
	}

	@Override
	public void onInstanceDestroy() {
		super.onInstanceDestroy();
		npcs.clear();
		emperyanStage.clear();
	}

	@Override
	public boolean onDie(final Player player, Creature lastAttacker) {
		super.onDie(player, lastAttacker);
		getPlayerReward(player.getObjectId()).setPlayerDefeated(true);

		boolean defeat = true;
		for (Player p : instance.getPlayersInside()) {
			if (!getPlayerReward(p.getObjectId()).isPlayerDefeated()) {
				defeat = false;
				break;
			}
		}

		if (defeat) {
			rewardGroup();
		}

		return true;
	}

	@Override
	public boolean onReviveEvent(final Player player) {
		super.onReviveEvent(player);
		moveToReadyRoom(player);
		instance.doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player p) {
				if (player.getObjectId() == p.getObjectId()) {
					PacketSendUtility.sendPacket(p, new SM_SYSTEM_MESSAGE(1400932));
				}
				else {
					PacketSendUtility.sendPacket(p, new SM_SYSTEM_MESSAGE(1400933, player.getName()));
				}
			}
		});
		return true;
	}

	private EmpyreanStage getEmpyreanStage(Npc npc) {
		for (EmpyreanStage es : emperyanStage) {
			if (es.npcs.contains(npc)) {
				return es;
			}
		}
		return null;
	}

	private boolean isSpawn(List<Integer> round) {
		for (Npc n : npcs) {
			if (round.contains(n.getNpcId())) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void onChangeStage(final StageType type) {
		switch (type) {
			case START_STAGE_1_ELEVATOR:
				sendEventPacket(type, 0);
				sendEventPacket(StageType.START_STAGE_1_ROUND_1, 5000);
				stage = 1;
				final List<Integer> round = new ArrayList<Integer>();
				round.add(217486);
				round.add(217489);
				sp(217486, 327.73657f, 347.96228f, 96.09092f, (byte) 0, 9000);
				sp(217489, 327.81943f, 350.948f, 96.09093f, (byte) 0, 9000);
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						if (!isSpawn(round)) {
							startStage1Round2();
						}
						round.clear();
					}
				}, 62000);
				break;
			case START_STAGE_2_ELEVATOR:
				sendEventPacket(type, 0);
				despawnNpcs(getNpcs(217756));
				stage = 2;
				sendEventPacket(StageType.START_STAGE_2_ROUND_1, 5000);
				sp(217503, 325.71194f, 352.81027f, 96.09092f, (byte) 0, 9000);
				sp(217502, 325.78696f, 346.07263f, 96.090904f, (byte) 0, 9000);
				sp(217504, 325.06122f, 349.4784f, 96.090904f, (byte) 0, 9000);
				break;
			case START_STAGE_3_ELEVATOR:
				sendEventPacket(type, 0);
				sendEventPacket(StageType.START_STAGE_3_ROUND_1, 5000);
				stage = 3;
				sp(217512, 344.23056f, 347.89594f, 96.09096f, (byte) 0, 9000);
				sp(217513, 341.09082f, 337.95187f, 96.09097f, (byte) 0, 9000);
				sp(217512, 342.06656f, 361.16135f, 96.090935f, (byte) 0, 9000);
				sp(217511, 356.75006f, 335.27487f, 96.09096f, (byte) 0, 9000);
				sp(217514, 345.4355f, 365.05215f, 96.09093f, (byte) 0, 9000);
				sp(217512, 352.8222f, 358.33463f, 96.09092f, (byte) 0, 9000);
				sp(217513, 342.32755f, 365.00473f, 96.09093f, (byte) 0, 9000);
				sp(217514, 356.19113f, 362.22543f, 96.090965f, (byte) 0, 9000);
				sp(217511, 344.25127f, 334.1194f, 96.090935f, (byte) 0, 9000);
				sp(217511, 344.07086f, 346.8839f, 96.09092f, (byte) 0, 9000);
				sp(217514, 334.01746f, 350.76382f, 96.090935f, (byte) 0, 9000);
				sp(217513, 344.49155f, 351.73932f, 96.09093f, (byte) 0, 9000);
				sp(217513, 353.0832f, 362.178f, 96.09092f, (byte) 0, 9000);
				sp(217511, 356.24454f, 358.34552f, 96.09103f, (byte) 0, 9000);
				sp(217512, 330.64853f, 346.87302f, 96.09091f, (byte) 0, 9000);
				sp(217512, 353.32773f, 335.26398f, 96.09092f, (byte) 0, 9000);
				sp(217514, 356.69666f, 339.1548f, 96.09103f, (byte) 0, 9000);
				sp(217511, 347.6529f, 347.90683f, 96.09098f, (byte) 0, 9000);
				sp(217514, 347.5995f, 351.78674f, 96.09099f, (byte) 0, 9000);
				sp(217512, 340.82983f, 334.1085f, 96.09093f, (byte) 0, 9000);
				sp(217514, 344.19876f, 337.9993f, 96.09094f, (byte) 0, 9000);
				sp(217513, 353.5887f, 339.10763f, 96.09092f, (byte) 0, 9000);
				sp(217511, 345.4889f, 361.17224f, 96.090935f, (byte) 0, 9000);
				sp(217513, 330.90952f, 350.7164f, 96.09093f, (byte) 0, 9000);
				break;
			case START_STAGE_4_ELEVATOR:
				sendEventPacket(type, 0);
				despawnNpc(getNpc(217735));
				sendEventPacket(StageType.START_STAGE_4_ROUND_1, 5000);
				stage = 4;
				sp(217557, 328.88104f, 349.55392f, 96.090904f, (byte) 0, 9000);
				sp(217559, 328.38922f, 342.39066f, 96.09091f, (byte) 5, 9000);
				sp(217557, 333.17947f, 336.4504f, 96.090904f, (byte) 8, 9000);
				break;
			case START_STAGE_5_ROUND_1:
				sendEventPacket(type, 2000);
				sp(217549, 1263.1987f, 778.4129f, 358.6056f, (byte) 30, 6000);
				sp(217548, 1260.1381f, 778.84644f, 358.60562f, (byte) 30, 6000);
				sp(217547, 1257.3065f, 778.35016f, 358.60562f, (byte) 30, 6000);
				break;
			case START_STAGE_6_ROUND_1:
				sendEventPacket(type, 2000);
				sp(217568, 1636.7102f, 166.87984f, 126f, (byte) 60, 6000);
				sp(217568, 1619.4432f, 153.83188f, 126f, (byte) 60, 6000);
				sp(217568, 1636.6416f, 164.15344f, 126f, (byte) 60, 6000);
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						sp(217568, 1638.7107f, 165.40533f, 126f, (byte) 60);
						sp(217568, 1638.6783f, 162.67389f, 126f, (byte) 60);
						isDoneStage6Round1 = true;
					}
				}, 12000);
				break;
			case START_STAGE_6_ROUND_5:
				sendEventPacket(type, 0);
				break;
			case START_STAGE_7_ROUND_1:
				sendEventPacket(type, 2000);
				sp(217578, 1784.848f, 806.7728f, 469.82202f, (byte) 0, 6000); // elyos
				// sp(217582, 1794.908f, 811.9936f, 469.3501f, (byte) 80, 6000); //asmo, need sniff
				break;
			case START_STAGE_8_ROUND_1:
				sendEventPacket(type, 2000);
				sp(217588, 1776.578f, 1773.231f, 303.695f, (byte) 90, 6000);
				break;
			case START_STAGE_9_ROUND_1:
				sendEventPacket(type, 2000);
				sp(217594, 1274.890f, 1730.676f, 318.194f, (byte) 3, 6000);
				break;
			case START_STAGE_5:
				stage = 5;
				sp(205426, 1256.2872f, 834.28986f, 358.60565f, (byte) 103);
				sp(205332, 1260.1292f, 795.06964f, 358.60562f, (byte) 30);
				teleport(1260.15f, 812.34f, 358.6056f, (byte) 90);
				sendEventPacket(type, 1000);
				break;
			case START_STAGE_6:
				stage = 6;
				sp(205427, 1594.4756f, 145.26898f, 128.67778f, (byte) 16);
				sp(205333, 1625.1771f, 159.15244f, 126f, (byte) 70);
				teleport(1616.0248f, 154.43837f, 126f, (byte) 10);
				sendEventPacket(type, 1000);
				break;
			case START_STAGE_7:
				stage = 7;
				sp(205428, 1820.39f, 800.81805f, 470.1394f, (byte) 86);
				sp(205334, 1781.6106f, 796.9224f, 469.35016f, (byte) 0);
				teleport(1793.9233f, 796.92f, 469.36542f, (byte) 60);
				sendEventPacket(type, 1000);
				break;
			case START_STAGE_8:
				stage = 8;
				sp(205335, 1776.759f, 1764.705f, 303.695f, (byte) 90);
				sp(205429, 1780.103f, 1723.458f, 304.039f, (byte) 53);
				teleport(1776.4169f, 1749.9952f, 303.69553f, (byte) 0); // get retail
				sendEventPacket(type, 1000);
				break;
			case START_STAGE_9:
				stage = 9;
				sp(205430, 1359.375f, 1758.057f, 319.625f, (byte) 90);
				sp(205336, 1309.309f, 1732.540f, 315.782f, (byte) 7);
				teleport(1320.4513f, 1738.4838f, 316.1746f, (byte) 66);
				sendEventPacket(type, 1000);
				break;
			default:
				break;
		}
	}

	private void startStage1Round2() {
		sendEventPacket(StageType.START_STAGE_1_ROUND_2, 2000);
		final List<Integer> round = new ArrayList<Integer>();
		round.add(217492);
		sp(217492, 332.7714f, 358.48206f, 96.09092f, (byte) 106, 6000);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (!isSpawn(round)) {
					startStage1Round3();
				}
				round.clear();
			}
		}, 59000);
	}

	private void startStage1Round3() {
		sendEventPacket(StageType.START_STAGE_1_ROUND_3, 2000);
		final List<Integer> round = new ArrayList<Integer>();
		round.add(217487);
		sp(217487, 334.844f, 339.92618f, 96.09094f, (byte) 18, 6000);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (!isSpawn(round)) {
					startStage1Round4();
				}
				round.clear();
			}
		}, 63000);
	}

	private void startStage1Round4() {
		sendEventPacket(StageType.START_STAGE_1_ROUND_4, 2000);
		final List<Integer> round = new ArrayList<Integer>();
		round.add(217491);
		sp(217491, 341.03156f, 361.04315f, 96.09093f, (byte) 90, 6000);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (!isSpawn(round)) {
					startStage1Round5();
				}
				round.clear();
			}
		}, 167000);
	}

	private void startStage1Round5() {
		sendEventPacket(StageType.START_STAGE_1_ROUND_5, 2000);
		sp(217493, 332.093f, 349.36847f, 96.090935f, (byte) 0, 8000);
	}

	private void teleport(float x, float y, float z, byte h) {
		for (Player playerInside : instance.getPlayersInside()) {
			if (playerInside.isOnline()) {
				if (!getPlayerReward(playerInside.getObjectId()).isPlayerDefeated()) {
					teleport(playerInside, x, y, z, h);
				}
				else {
					moveToReadyRoom(playerInside);
				}
			}
		}
	}

	private void moveToReadyRoom(Player player) {
		switch (stage) {
			case 1:
			case 2:
			case 3:
			case 4:
				teleport(player, 381.41684f, 346.78162f, 96.74763f, (byte) 43);
				break;
			case 5:
				teleport(player, 1260.9495f, 832.87317f, 358.60562f, (byte) 92);
				break;
			case 6:
				teleport(player, 1592.8813f, 149.78166f, 128.81355f, (byte) 117);
				break;
			case 7:
				teleport(player, 1820.8805f, 795.80914f, 470.18304f, (byte) 51);
				break;
			case 8:
				teleport(player, 1780.103f, 1723.458f, 304.039f, (byte) 53); // get retail
				break;
			case 9:
				teleport(player, 1359.5046f, 1751.7952f, 319.59406f, (byte) 30);
				break;
			case 10:
				// todo
				break;
		}
	}

	@Override
	public void onLeaveInstance(Player player) {
		CruciblePlayerReward reward = getPlayerReward(player.getObjectId());
		if (reward != null) {
			reward.setPlayerLeave();
		}
	}

	@Override
	public void onExitInstance(Player player) {
		TeleportService2.moveToInstanceExit(player, mapId, player.getRace());
	}

	@Override
	public void onStopTraining(Player player) {
		doReward(player);
	}

	private void sp(final int npcId, final float x, final float y, final float z, final byte h, final int time) {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (!isInstanceDestroyed) {
					npcs.add((Npc) spawn(npcId, x, y, z, h));
				}
			}
		}, time);
	}

	private Npc sp(int npcId, float x, float y, float z, byte h) {
		Npc npc = null;
		if (!isInstanceDestroyed) {
			npc = (Npc) spawn(npcId, x, y, z, h);
			npcs.add(npc);
		}
		return npc;
	}

	@Override
	public void handleUseItemFinish(Player player, Npc npc) {
		switch (npc.getNpcId()) {
			case 217756:
			case 217735:
				ItemService.addItem(player, 186000124, 1);
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1401009));
				CreatureActions.delete(npc);
				break;
		}
	}

	@Override
	public void onDropRegistered(Npc npc) {
		Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		int npcId = npc.getNpcId();
		int itemId = 0;
		switch (npcId) {
			case 217740:
			case 217741:
			case 217743:
			case 217742:
				dropItems.clear();
				itemId = 162000108;
				if (Rnd.get(1, 100) < 6) {
					switch (npc.getNpcId()) {
						case 217740:
							itemId = 125002593;
							break;
						case 217741:
							itemId = 125002595;
							break;
						case 217742:
							itemId = 125002592;
							break;
						case 217743:
							itemId = 125002594;
							break;
					}
				}
				break;
			case 217750:
				dropItems.clear();
				itemId = 162000109;
				if (Rnd.get(1, 100) < 6) {
					switch (Rnd.get(1, 9)) {
						case 1:
							itemId = 101700911;
							break;
						case 2:
							itemId = 100201003;
							break;
						case 3:
							itemId = 100900869;
							break;
						case 4:
							itemId = 100100874;
							break;
						case 5:
							itemId = 100500886;
							break;
						case 6:
							itemId = 101300836;
							break;
						case 7:
							itemId = 100600944;
							break;
						case 8:
							itemId = 100001135;
							break;
						case 9:
							itemId = 101500895;
							break;
					}
					break;
				}
			case 205341: // skillbooks
				if (Rnd.get(1, 100) < 51) {
					Race race = instance.getRegisteredGroup().getRace();
					switch (Rnd.get(1, 6)) {
						case 1:
							itemId = 169500935;
							break;
						case 2:
							itemId = 169500934;
							break;
						case 3:
							itemId = 169500932;
							break;
						case 4:
							itemId = race.equals(Race.ELYOS) ? 169500947 : 169500951;
							break;
						case 5:
							itemId = race.equals(Race.ELYOS) ? 169500939 : 169500943;
							break;
						case 6:
							itemId = race.equals(Race.ELYOS) ? 169500927 : 169500931;
							break;
						case 7:
							itemId = race.equals(Race.ELYOS) ? 169500919 : 169500923;
							break;
					}
					break;
				}
		}
		if (itemId != 0) {
			dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, itemId, 1));
		}
	}

	@Override
	public void onPlayerLogOut(Player player) {
		TeleportService2.moveToInstanceExit(player, mapId, player.getRace());
	}
}
