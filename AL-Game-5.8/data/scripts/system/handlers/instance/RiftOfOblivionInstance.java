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
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.ai2.manager.WalkManager;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.instance.InstanceScoreType;
import com.aionemu.gameserver.model.instance.instancereward.RiftOfOblivionReward;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INSTANCE_SCORE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;

/**
 * @author Falke_34
 */
@InstanceID(302100000)
public class RiftOfOblivionInstance extends GeneralInstanceHandler {

	private RiftOfOblivionReward instanceReward;
	private long startTime;
	private Future<?> timerPrepare;
	private Future<?> timerInstance;
	private int startDoorId = 34;
	private int prepareTimerSeconds = 180000; // 3 Minute
	private int instanceTimerSeconds = 1800000; // 30 Minutes

	private int IDTransformSado66An;
	private int IDTransformSado67An;
	private int IDTransformSado68An;
	private int IDTransformSado69An;
	private int IDTransformSado70An;
	private int IDTransformSado71An;
	private int IDTransformSado72An;
	private int IDTransformSado73An;
	private int IDTransformSado74An;
	private int IDTransformSado75An;

	/**
	 * list of npcIds to check after instance timer is done this Npc's must stay in instance
	 */
	private List<Integer> npcCheckList = new ArrayList<Integer>(Arrays.asList(244572, 244573, 244574, 244575, 244576));

	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
		instanceReward = new RiftOfOblivionReward(mapId, instanceId);
		instanceReward.setInstanceScoreType(InstanceScoreType.PREPARING);
	}

	@Override
	public void onEnterInstance(final Player player) {
		startPrepareTimer();
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				IDTransformARoomForm();
				IDTransformBRoomForm();
				IDTransformCRoomForm();
				IDTransformCinemaRoom();
				IDTransformLevelStart(player);
				SkillEngine.getInstance().applyEffectDirectly(4829, player, player, 1800000 * 1); // Ancient High Daeva�s Memory

				// if (player.getRace().equals(Race.ASMODIANS)) {
				// SkillEngine.getInstance().applyEffectDirectly(4829, player, player, 0);
				// }
				// else {
				// SkillEngine.getInstance().applyEffectDirectly(4829, player, player, 0);
				// }
			}
		}, 1000);
	}

	@Override
	public void onInstanceDestroy() {
		if (timerInstance != null) {
			timerInstance.cancel(false);
		}
		if (timerPrepare != null) {
			timerPrepare.cancel(false);
		}
	}

	private void startPrepareTimer() {
		if (timerPrepare == null) {
			timerPrepare = ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					startMainInstanceTimer();
				}
			}, prepareTimerSeconds);
		}

		instance.doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(prepareTimerSeconds, instanceReward, null));
			}
		});
	}

	private void startMainInstanceTimer() {
		// cancel the prepare-timer if is set
		if (!timerPrepare.isDone()) {
			timerPrepare.cancel(false);
		}

		// open the start door
		instance.getDoors().get(startDoorId).setOpen(true);

		// set the current time for later calculation
		startTime = System.currentTimeMillis();

		// set instanceReward state
		instanceReward.setInstanceScoreType(InstanceScoreType.START_PROGRESS);
		sendPacket(0, 0);

		// start main instance timer
		timerInstance = ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				onTimerEnd();
			}
		}, instanceTimerSeconds);
	}

	private void sendPacket(final int nameId, final int point) {
		instance.doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				if (nameId != 0) {
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400237, new DescriptionId(nameId * 2 + 1), point));
				}
				PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(getTime(), instanceReward, null));
			}
		});
	}

	private int getTime() {
		long result = (int) (System.currentTimeMillis() - startTime);

		return instanceTimerSeconds - (int) result;
	}

	private void onTimerEnd() {
		if (!timerInstance.isDone()) {
			timerInstance.cancel(true);
		}

		for (Npc npc : instance.getNpcs()) {
			if (npcCheckList.contains(npc.getNpcId())) {
				continue;
			}
			npc.getController().delete();
		}

		instance.doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				switch (checkRank(instanceReward.getPoints())) {
					case 1:
						instanceReward.setIcyOrbOfMemory(5);
						break;
					case 2:
						instanceReward.setIcyOrbOfMemory(4);
						break;
					case 3:
						instanceReward.setIcyOrbOfMemory(3);
						break;
					case 4:
						instanceReward.setIcyOrbOfMemory(2);
						break;
					case 5:
						instanceReward.setIcyOrbOfMemory(1);
						break;
				}
				instanceReward.setInstanceScoreType(InstanceScoreType.END_PROGRESS);
				sendPacket(0, 0);

				ItemService.addItem(player, 186000448, instanceReward.getIcyOrbOfMemory());

				spawn(834195, 923.7528f, 463.11f, 352.94174f, (byte) 75, 278); // Exit from the Rift of Oblivion
				spawn(834194, 273.74597f, 514.06793f, 352.3227f, (byte) 0, 29); // Orb of Fate
			}
		});
	}

	@Override
	public void onDie(Npc npc) {
		Creature master = npc.getMaster();
		boolean timerEnd = false;
		if (master instanceof Player)
			return;

		int points = 0;
		switch (npc.getObjectTemplate().getTemplateId()) {
			/*** Player Lvl 66 ***/
			case 244454: // Minion of Oblivion
			case 244455:
			case 244456:
			case 244457:
				IDTransformSado66An++;
				if (IDTransformSado66An == 4) {
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							switch (Rnd.get(1, 3)) {
								case 1:
									IDTransformEreshWarp66();
									break;
								case 2:
									IDTransformVritraWarp66();
									break;
								case 3:
									IDTransformTiamatWarp66();
									break;
							}
							deleteNpc(245402);
							spawn(245416, 855.54144f, 465.55255f, 351.57367f, (byte) 0, 54);
						}
					}, 5000);
				}
				else if (IDTransformSado66An == 8) {
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							deleteNpc(245403);
							spawn(245417, 594.41882f, 564.05542f, 352.56454f, (byte) 0, 58);
							spawn(245418, 522.48053f, 573.51971f, 321.80389f, (byte) 0, 55);
						}
					}, 5000);
				}
				else if (IDTransformSado66An == 12) {
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							deleteNpc(245404);
						}
					}, 5000);
				}
				points = 250;
				despawnNpc(npc);
				break;
			/*** Player Lvl 67 ***/
			case 244495: // Minion of Oblivion
			case 244496:
			case 244497:
			case 244498:
				IDTransformSado67An++;
				if (IDTransformSado67An == 4) {
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							switch (Rnd.get(1, 3)) {
								case 1:
									IDTransformEreshWarp67();
									break;
								case 2:
									IDTransformVritraWarp67();
									break;
								case 3:
									IDTransformTiamatWarp67();
									break;
							}
							deleteNpc(245402);
							spawn(245416, 855.54144f, 465.55255f, 351.57367f, (byte) 0, 54);
						}
					}, 5000);
				}
				else if (IDTransformSado67An == 8) {
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							deleteNpc(245403);
							spawn(245417, 594.41882f, 564.05542f, 352.56454f, (byte) 0, 58);
							spawn(245418, 522.48053f, 573.51971f, 321.80389f, (byte) 0, 55);
						}
					}, 5000);
				}
				else if (IDTransformSado67An == 12) {
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							deleteNpc(245404);
						}
					}, 5000);
				}
				points = 250;
				despawnNpc(npc);
				break;
			/*** Player Lvl 68 ***/
			case 244536: // Minion of Oblivion
			case 244537:
			case 244538:
			case 244539:
				IDTransformSado68An++;
				if (IDTransformSado68An == 4) {
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							switch (Rnd.get(1, 3)) {
								case 1:
									IDTransformEreshWarp68();
									break;
								case 2:
									IDTransformVritraWarp68();
									break;
								case 3:
									IDTransformTiamatWarp68();
									break;
							}
							deleteNpc(245402);
							spawn(245416, 855.54144f, 465.55255f, 351.57367f, (byte) 0, 54);
						}
					}, 5000);
				}
				else if (IDTransformSado68An == 8) {
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							deleteNpc(245403);
							spawn(245417, 594.41882f, 564.05542f, 352.56454f, (byte) 0, 58);
							spawn(245418, 522.48053f, 573.51971f, 321.80389f, (byte) 0, 55);
						}
					}, 5000);
				}
				else if (IDTransformSado68An == 12) {
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							deleteNpc(245404);
						}
					}, 5000);
				}
				points = 250;
				despawnNpc(npc);
				break;
			/*** Player Lvl 69 ***/
			case 244577: // Minion of Oblivion
			case 244578:
			case 244579:
			case 244580:
				IDTransformSado69An++;
				if (IDTransformSado69An == 4) {
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							switch (Rnd.get(1, 3)) {
								case 1:
									IDTransformEreshWarp69();
									break;
								case 2:
									IDTransformVritraWarp69();
									break;
								case 3:
									IDTransformTiamatWarp69();
									break;
							}
							deleteNpc(245402);
							spawn(245416, 855.54144f, 465.55255f, 351.57367f, (byte) 0, 54);
						}
					}, 5000);
				}
				else if (IDTransformSado69An == 8) {
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							deleteNpc(245403);
							spawn(245417, 594.41882f, 564.05542f, 352.56454f, (byte) 0, 58);
							spawn(245418, 522.48053f, 573.51971f, 321.80389f, (byte) 0, 55);
						}
					}, 5000);
				}
				else if (IDTransformSado69An == 12) {
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							deleteNpc(245404);
						}
					}, 5000);
				}
				points = 250;
				despawnNpc(npc);
				break;
			/*** Player Lvl 70 ***/
			case 244618: // Minion of Oblivion
			case 244619:
			case 244620:
			case 244621:
				IDTransformSado70An++;
				if (IDTransformSado70An == 4) {
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							switch (Rnd.get(1, 3)) {
								case 1:
									IDTransformEreshWarp70();
									break;
								case 2:
									IDTransformVritraWarp70();
									break;
								case 3:
									IDTransformTiamatWarp70();
									break;
							}
							deleteNpc(245402);
							spawn(245416, 855.54144f, 465.55255f, 351.57367f, (byte) 0, 54);
						}
					}, 5000);
				}
				else if (IDTransformSado70An == 8) {
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							deleteNpc(245403);
							spawn(245417, 594.41882f, 564.05542f, 352.56454f, (byte) 0, 58);
							spawn(245418, 522.48053f, 573.51971f, 321.80389f, (byte) 0, 55);
						}
					}, 5000);
				}
				else if (IDTransformSado70An == 12) {
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							deleteNpc(245404);
						}
					}, 5000);
				}
				points = 250;
				despawnNpc(npc);
				break;
			/*** Player Lvl 71 ***/
			case 244659: // Minion of Oblivion
			case 244660:
			case 244661:
			case 244662:
				IDTransformSado71An++;
				if (IDTransformSado71An == 4) {
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							switch (Rnd.get(1, 3)) {
								case 1:
									IDTransformEreshWarp71();
									break;
								case 2:
									IDTransformVritraWarp71();
									break;
								case 3:
									IDTransformTiamatWarp71();
									break;
							}
							deleteNpc(245402);
							spawn(245416, 855.54144f, 465.55255f, 351.57367f, (byte) 0, 54);
						}
					}, 5000);
				}
				else if (IDTransformSado71An == 8) {
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							deleteNpc(245403);
							spawn(245417, 594.41882f, 564.05542f, 352.56454f, (byte) 0, 58);
							spawn(245418, 522.48053f, 573.51971f, 321.80389f, (byte) 0, 55);
						}
					}, 5000);
				}
				else if (IDTransformSado71An == 12) {
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							deleteNpc(245404);
						}
					}, 5000);
				}
				points = 250;
				despawnNpc(npc);
				break;
			/*** Player Lvl 72 ***/
			case 244700: // Minion of Oblivion
			case 244701:
			case 244702:
			case 244703:
				IDTransformSado72An++;
				if (IDTransformSado72An == 4) {
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							switch (Rnd.get(1, 3)) {
								case 1:
									IDTransformEreshWarp72();
									break;
								case 2:
									IDTransformVritraWarp72();
									break;
								case 3:
									IDTransformTiamatWarp72();
									break;
							}
							deleteNpc(245402);
							spawn(245416, 855.54144f, 465.55255f, 351.57367f, (byte) 0, 54);
						}
					}, 5000);
				}
				else if (IDTransformSado72An == 8) {
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							deleteNpc(245403);
							spawn(245417, 594.41882f, 564.05542f, 352.56454f, (byte) 0, 58);
							spawn(245418, 522.48053f, 573.51971f, 321.80389f, (byte) 0, 55);
						}
					}, 5000);
				}
				else if (IDTransformSado72An == 12) {
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							deleteNpc(245404);
						}
					}, 5000);
				}
				points = 250;
				despawnNpc(npc);
				break;
			/*** Player Lvl 73 ***/
			case 244741: // Minion of Oblivion
			case 244742:
			case 244743:
			case 244744:
				IDTransformSado73An++;
				if (IDTransformSado73An == 4) {
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							switch (Rnd.get(1, 3)) {
								case 1:
									IDTransformEreshWarp73();
									break;
								case 2:
									IDTransformVritraWarp73();
									break;
								case 3:
									IDTransformTiamatWarp73();
									break;
							}
							deleteNpc(245402);
							spawn(245416, 855.54144f, 465.55255f, 351.57367f, (byte) 0, 54);
						}
					}, 5000);
				}
				else if (IDTransformSado73An == 8) {
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							deleteNpc(245403);
							spawn(245417, 594.41882f, 564.05542f, 352.56454f, (byte) 0, 58);
							spawn(245418, 522.48053f, 573.51971f, 321.80389f, (byte) 0, 55);
						}
					}, 5000);
				}
				else if (IDTransformSado73An == 12) {
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							deleteNpc(245404);
						}
					}, 5000);
				}
				points = 250;
				despawnNpc(npc);
				break;
			/*** Player Lvl 74 ***/
			case 244782: // Minion of Oblivion
			case 244783:
			case 244784:
			case 244785:
				IDTransformSado74An++;
				if (IDTransformSado74An == 4) {
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							switch (Rnd.get(1, 3)) {
								case 1:
									IDTransformEreshWarp74();
									break;
								case 2:
									IDTransformVritraWarp74();
									break;
								case 3:
									IDTransformTiamatWarp74();
									break;
							}
							deleteNpc(245402);
							spawn(245416, 855.54144f, 465.55255f, 351.57367f, (byte) 0, 54);
						}
					}, 5000);
				}
				else if (IDTransformSado74An == 8) {
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							deleteNpc(245403);
							spawn(245417, 594.41882f, 564.05542f, 352.56454f, (byte) 0, 58);
							spawn(245418, 522.48053f, 573.51971f, 321.80389f, (byte) 0, 55);
						}
					}, 5000);
				}
				else if (IDTransformSado74An == 12) {
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							deleteNpc(245404);
						}
					}, 5000);
				}
				points = 250;
				despawnNpc(npc);
				break;
			/*** Player Lvl 75-83 ***/
			case 244823: // Minion of Oblivion
			case 244824:
			case 244825:
			case 244826:
				IDTransformSado75An++;
				if (IDTransformSado75An == 4) {
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							switch (Rnd.get(1, 3)) {
								case 1:
									IDTransformEreshWarp75();
									break;
								case 2:
									IDTransformVritraWarp75();
									break;
								case 3:
									IDTransformTiamatWarp75();
									break;
							}
							deleteNpc(245402);
							spawn(245416, 855.54144f, 465.55255f, 351.57367f, (byte) 0, 54);
						}
					}, 5000);
				}
				else if (IDTransformSado75An == 8) {
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							deleteNpc(245403);
							spawn(245417, 594.41882f, 564.05542f, 352.56454f, (byte) 0, 58);
							spawn(245418, 522.48053f, 573.51971f, 321.80389f, (byte) 0, 55);
						}
					}, 5000);
				}
				else if (IDTransformSado75An == 12) {
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							deleteNpc(245404);
						}
					}, 5000);
				}
				points = 250;
				despawnNpc(npc);
				break;
			/*** Player Lvl 66 ***/
			case 244470:
			case 244471:
			case 244472:
			case 244473:
			case 244474:
			case 244475:
			case 244476:
			case 244477:
			case 244478:
			case 244479:
			case 244480:
			case 244481:
				/*** Player Lvl 67 ***/
			case 244511:
			case 244512:
			case 244513:
			case 244514:
			case 244515:
			case 244516:
			case 244517:
			case 244518:
			case 244519:
			case 244520:
			case 244521:
			case 244522:
				/*** Player Lvl 68 ***/
			case 244552:
			case 244553:
			case 244554:
			case 244555:
			case 244556:
			case 244557:
			case 244558:
			case 244559:
			case 244560:
			case 244561:
			case 244562:
			case 244563:
				/*** Player Lvl 69 ***/
			case 244593:
			case 244594:
			case 244595:
			case 244596:
			case 244597:
			case 244598:
			case 244599:
			case 244600:
			case 244601:
			case 244602:
			case 244603:
			case 244604:
				/*** Player Lvl 70 ***/
			case 244634:
			case 244635:
			case 244636:
			case 244637:
			case 244638:
			case 244639:
			case 244640:
			case 244641:
			case 244642:
			case 244643:
			case 244644:
			case 244645:
				/*** Player Lvl 71 ***/
			case 244675:
			case 244676:
			case 244677:
			case 244678:
			case 244679:
			case 244680:
			case 244681:
			case 244682:
			case 244683:
			case 244684:
			case 244685:
			case 244686:
				/*** Player Lvl 72 ***/
			case 244716:
			case 244717:
			case 244718:
			case 244719:
			case 244720:
			case 244721:
			case 244722:
			case 244723:
			case 244724:
			case 244725:
			case 244726:
			case 244727:
				/*** Player Lvl 73 ***/
			case 244757:
			case 244758:
			case 244759:
			case 244760:
			case 244761:
			case 244762:
			case 244763:
			case 244764:
			case 244765:
			case 244766:
			case 244767:
			case 244768:
				/*** Player Lvl 74 ***/
			case 244798:
			case 244799:
			case 244800:
			case 244801:
			case 244802:
			case 244803:
			case 244804:
			case 244805:
			case 244806:
			case 244807:
			case 244808:
			case 244809:
				/*** Player Lvl 75-83 ***/
			case 244839:
			case 244840:
			case 244841:
			case 244842:
			case 244843:
			case 244844:
			case 244845:
			case 244846:
			case 244847:
			case 244848:
			case 244849:
			case 244850:
				points = 250;
				despawnNpc(npc);
				break;
			/*** Player Lvl 66 ***/
			case 245577:
			case 245578:
			case 245579:
			case 245580:
			case 245581:
			case 245582:
			case 245583:
			case 245584:
			case 245585:
			case 245586:
			case 245587:
			case 245588:
			case 245697:
			case 245698:
			case 245699:
				/*** Player Lvl 67 ***/
			case 245589:
			case 245590:
			case 245591:
			case 245592:
			case 245593:
			case 245594:
			case 245595:
			case 245596:
			case 245597:
			case 245598:
			case 245599:
			case 245600:
			case 245703:
			case 245704:
			case 245705:
				/*** Player Lvl 68 ***/
			case 245601:
			case 245602:
			case 245603:
			case 245604:
			case 245605:
			case 245606:
			case 245607:
			case 245608:
			case 245609:
			case 245610:
			case 245611:
			case 245612:
			case 245709:
			case 245710:
			case 245711:
				/*** Player Lvl 69 ***/
			case 245613:
			case 245614:
			case 245615:
			case 245616:
			case 245617:
			case 245618:
			case 245619:
			case 245620:
			case 245621:
			case 245622:
			case 245623:
			case 245624:
			case 245715:
			case 245716:
			case 245717:
				/*** Player Lvl 70 ***/
			case 245625:
			case 245626:
			case 245627:
			case 245628:
			case 245629:
			case 245630:
			case 245631:
			case 245632:
			case 245633:
			case 245634:
			case 245635:
			case 245636:
			case 245721:
			case 245722:
			case 245723:
				/*** Player Lvl 71 ***/
			case 245637:
			case 245638:
			case 245639:
			case 245640:
			case 245641:
			case 245642:
			case 245643:
			case 245644:
			case 245645:
			case 245646:
			case 245647:
			case 245648:
			case 245727:
			case 245728:
			case 245729:
				/*** Player Lvl 72 ***/
			case 245649:
			case 245650:
			case 245651:
			case 245652:
			case 245653:
			case 245654:
			case 245655:
			case 245656:
			case 245657:
			case 245658:
			case 245659:
			case 245660:
			case 245733:
			case 245734:
			case 245735:
				/*** Player Lvl 73 ***/
			case 245661:
			case 245662:
			case 245663:
			case 245664:
			case 245665:
			case 245666:
			case 245667:
			case 245668:
			case 245669:
			case 245670:
			case 245671:
			case 245672:
			case 245739:
			case 245740:
			case 245741:
				/*** Player Lvl 74 ***/
			case 245673:
			case 245674:
			case 245675:
			case 245676:
			case 245677:
			case 245678:
			case 245679:
			case 245680:
			case 245681:
			case 245682:
			case 245683:
			case 245684:
			case 245745:
			case 245746:
			case 245747:
				/*** Player Lvl 75-83 ***/
			case 245685:
			case 245686:
			case 245687:
			case 245688:
			case 245689:
			case 245690:
			case 245691:
			case 245692:
			case 245693:
			case 245694:
			case 245695:
			case 245696:
			case 245751:
			case 245752:
			case 245753:
				points = 250;
				despawnNpc(npc);
				break;
			/*** Player Lvl 66 ***/
			case 244482:
			case 244483:
			case 244484:
				/*** Player Lvl 67 ***/
			case 244523:
			case 244524:
			case 244525:
				/*** Player Lvl 68 ***/
			case 244564:
			case 244565:
			case 244566:
				/*** Player Lvl 69 ***/
			case 244605:
			case 244606:
			case 244607:
				/*** Player Lvl 70 ***/
			case 244646:
			case 244647:
			case 244648:
				/*** Player Lvl 71 ***/
			case 244687:
			case 244688:
			case 244689:
				/*** Player Lvl 72 ***/
			case 244728:
			case 244729:
			case 244730:
				/*** Player Lvl 73 ***/
			case 244769:
			case 244770:
			case 244771:
				/*** Player Lvl 74 ***/
			case 244810:
			case 244811:
			case 244812:
				/*** Player Lvl 75-83 ***/
			case 244851:
			case 244852:
			case 244853:
				points = 550;
				despawnNpc(npc);
				break;
			/*** Player Lvl 66 ***/
			case 244892:
			case 244893:
			case 244894:
				/*** Player Lvl 67 ***/
			case 244923:
			case 244924:
			case 244925:
				/*** Player Lvl 68 ***/
			case 244954:
			case 244955:
			case 244956:
				/*** Player Lvl 69 ***/
			case 244985:
			case 244986:
			case 244987:
				/*** Player Lvl 70 ***/
			case 245016:
			case 245017:
			case 245018:
				/*** Player Lvl 71 ***/
			case 245047:
			case 245048:
			case 245049:
				/*** Player Lvl 72 ***/
			case 245078:
			case 245079:
			case 245080:
				/*** Player Lvl 73 ***/
			case 245109:
			case 245110:
			case 245111:
				/*** Player Lvl 74 ***/
			case 245140:
			case 245141:
			case 245142:
				/*** Player Lvl 75-83 ***/
			case 245171:
			case 245172:
			case 245173:
				points = 550;
				despawnNpc(npc);
				break;
			/*** Player Lvl 66 ***/
			case 244490:
			case 244491:
			case 244492:
			case 244493:
			case 244494:
				/*** Player Lvl 67 ***/
			case 244531:
			case 244532:
			case 244533:
			case 244534:
			case 244535:
				/*** Player Lvl 68 ***/
			case 244572:
			case 244573:
			case 244574:
			case 244575:
			case 244576:
				/*** Player Lvl 69 ***/
			case 244613:
			case 244614:
			case 244615:
			case 244616:
			case 244617:
				/*** Player Lvl 70 ***/
			case 244654:
			case 244655:
			case 244656:
			case 244657:
			case 244658:
				/*** Player Lvl 71 ***/
			case 244695:
			case 244696:
			case 244697:
			case 244698:
			case 244699:
				/*** Player Lvl 72 ***/
			case 244736:
			case 244737:
			case 244738:
			case 244739:
			case 244740:
				/*** Player Lvl 73 ***/
			case 244777:
			case 244778:
			case 244779:
			case 244780:
			case 244781:
				/*** Player Lvl 74 ***/
			case 244818:
			case 244819:
			case 244820:
			case 244821:
			case 244822:
				/*** Player Lvl 75 ***/
			case 244859:
			case 244860:
			case 244861:
			case 244862:
			case 244863:
				points = 1500;
				despawnNpc(npc);
				deleteNpc(245405);
				timerEnd = true;
		}
		if (instanceReward.getInstanceScoreType().isStartProgress()) {
			instanceReward.addPoints(points);
			instanceReward.setRank(checkRank(instanceReward.getPoints()));
			if (timerEnd)
				onTimerEnd();
			sendPacket(npc.getObjectTemplate().getNameId(), points);
		}
	}

	private int checkRank(int totalPoints) {
		int rank = 0;

		if (totalPoints >= 23550) { // S Rank
			rank = 1;
		}
		else if (totalPoints >= 21200) { // A Rank
			rank = 2;
		}
		else if (totalPoints >= 17700) { // B Rank
			rank = 3;
		}
		else if (totalPoints >= 14100) { // C Rank
			rank = 4;
		}
		else if (totalPoints >= 9400) { // D Rank
			rank = 5;
		}
		else if (totalPoints > 0) { // F Rank
			rank = 6;
		}
		else {
			rank = 8;
		}

		return rank;
	}

	@Override
	public void onOpenDoor(int door) {
		if (door == startDoorId) {
			if ((timerPrepare != null) && (!timerPrepare.isDone() || !timerPrepare.isCancelled())) {
				startMainInstanceTimer();
			}
		}
	}

	protected void despawnNpc(Npc npc) {
		if (npc != null) {
			npc.getController().onDelete();
		}
	}

	// Level of the monsters depands on the character's level.
	private void IDTransformLevelStart(Player player) {
		switch (player.getLevel()) {
			/*** Player Lvl 66 ***/
			case 66:
				IDTransformRa66An();
				IDTransformDropDragon66Ae();
				IDTransformDevaGuardFi66An();
				IDTransformDevaGuardAs66An();
				IDTransformDevaGuardWi66An();
				IDTransformDevaGuardRa66An();
				IDTransformDevaStumble66An();
				switch (Rnd.get(1, 3)) {
					case 1:
						IDTransformEreshHigh66An();
						break;
					case 2:
						IDTransformVritraHigh66An();
						break;
					case 3:
						IDTransformTiamatHigh66An();
						break;
				}
				switch (Rnd.get(1, 3)) {
					case 1:
						IDTransformEreshGuard66An();
						break;
					case 2:
						IDTransformVritraGuard66An();
						break;
					case 3:
						IDTransformTiamatGuard66An();
						break;
				}
				switch (Rnd.get(1, 4)) {
					case 1:
						IDTransformBonusMonsterTypeA66An();
						break;
					case 2:
						IDTransformBonusMonsterTypeB66An();
						break;
					case 3:
						IDTransformBonusMonsterTypeC66An();
						break;
					case 4:
						IDTransformBonusMonsterTypeD66An();
						break;
				}
				switch (Rnd.get(1, 3)) {
					case 1:
						IDTransformEreshStumble66An();
						break;
					case 2:
						IDTransformVritraStumble66An();
						break;
					case 3:
						IDTransformTiamatStumble66An();
						break;
				}
				switch (Rnd.get(1, 3)) {
					case 1:
						spawn(244482, 510.39917f, 458.81943f, 322.0f, (byte) 21);
						break;
					case 2:
						spawn(244483, 510.39917f, 458.81943f, 322.0f, (byte) 21);
						break;
					case 3:
						spawn(244484, 510.39917f, 458.81943f, 322.0f, (byte) 21);
						break;
				}
				break;
			/*** Player Lvl 67 ***/
			case 67:
				IDTransformRa67An();
				IDTransformDropDragon67Ae();
				IDTransformDevaGuardFi67An();
				IDTransformDevaGuardAs67An();
				IDTransformDevaGuardWi67An();
				IDTransformDevaGuardRa67An();
				IDTransformDevaStumble67An();
				switch (Rnd.get(1, 3)) {
					case 1:
						IDTransformEreshHigh67An();
						break;
					case 2:
						IDTransformVritraHigh67An();
						break;
					case 3:
						IDTransformTiamatHigh67An();
						break;
				}
				switch (Rnd.get(1, 3)) {
					case 1:
						IDTransformEreshGuard67An();
						break;
					case 2:
						IDTransformVritraGuard67An();
						break;
					case 3:
						IDTransformTiamatGuard67An();
						break;
				}
				switch (Rnd.get(1, 4)) {
					case 1:
						IDTransformBonusMonsterTypeA67An();
						break;
					case 2:
						IDTransformBonusMonsterTypeB67An();
						break;
					case 3:
						IDTransformBonusMonsterTypeC67An();
						break;
					case 4:
						IDTransformBonusMonsterTypeD67An();
						break;
				}
				switch (Rnd.get(1, 3)) {
					case 1:
						IDTransformEreshStumble67An();
						break;
					case 2:
						IDTransformVritraStumble67An();
						break;
					case 3:
						IDTransformTiamatStumble67An();
						break;
				}
				switch (Rnd.get(1, 3)) {
					case 1:
						spawn(244523, 510.39917f, 458.81943f, 322.0f, (byte) 21);
						break;
					case 2:
						spawn(244524, 510.39917f, 458.81943f, 322.0f, (byte) 21);
						break;
					case 3:
						spawn(244525, 510.39917f, 458.81943f, 322.0f, (byte) 21);
						break;
				}
				break;
			/*** Player Lvl 68 ***/
			case 68:
				IDTransformRa68An();
				IDTransformDropDragon68Ae();
				IDTransformDevaGuardFi68An();
				IDTransformDevaGuardAs68An();
				IDTransformDevaGuardWi68An();
				IDTransformDevaGuardRa68An();
				IDTransformDevaStumble68An();
				switch (Rnd.get(1, 3)) {
					case 1:
						IDTransformEreshHigh68An();
						break;
					case 2:
						IDTransformVritraHigh68An();
						break;
					case 3:
						IDTransformTiamatHigh68An();
						break;
				}
				switch (Rnd.get(1, 3)) {
					case 1:
						IDTransformEreshGuard68An();
						break;
					case 2:
						IDTransformVritraGuard68An();
						break;
					case 3:
						IDTransformTiamatGuard68An();
						break;
				}
				switch (Rnd.get(1, 4)) {
					case 1:
						IDTransformBonusMonsterTypeA68An();
						break;
					case 2:
						IDTransformBonusMonsterTypeB68An();
						break;
					case 3:
						IDTransformBonusMonsterTypeC68An();
						break;
					case 4:
						IDTransformBonusMonsterTypeD68An();
						break;
				}
				switch (Rnd.get(1, 3)) {
					case 1:
						IDTransformEreshStumble68An();
						break;
					case 2:
						IDTransformVritraStumble68An();
						break;
					case 3:
						IDTransformTiamatStumble68An();
						break;
				}
				switch (Rnd.get(1, 3)) {
					case 1:
						spawn(244564, 510.39917f, 458.81943f, 322.0f, (byte) 21);
						break;
					case 2:
						spawn(244565, 510.39917f, 458.81943f, 322.0f, (byte) 21);
						break;
					case 3:
						spawn(244566, 510.39917f, 458.81943f, 322.0f, (byte) 21);
						break;
				}
				break;
			/*** Player Lvl 69 ***/
			case 69:
				IDTransformRa69An();
				IDTransformDropDragon69Ae();
				IDTransformDevaGuardFi69An();
				IDTransformDevaGuardAs69An();
				IDTransformDevaGuardWi69An();
				IDTransformDevaGuardRa69An();
				IDTransformDevaStumble69An();
				switch (Rnd.get(1, 3)) {
					case 1:
						IDTransformEreshHigh69An();
						break;
					case 2:
						IDTransformVritraHigh69An();
						break;
					case 3:
						IDTransformTiamatHigh69An();
						break;
				}
				switch (Rnd.get(1, 3)) {
					case 1:
						IDTransformEreshGuard69An();
						break;
					case 2:
						IDTransformVritraGuard69An();
						break;
					case 3:
						IDTransformTiamatGuard69An();
						break;
				}
				switch (Rnd.get(1, 4)) {
					case 1:
						IDTransformBonusMonsterTypeA69An();
						break;
					case 2:
						IDTransformBonusMonsterTypeB69An();
						break;
					case 3:
						IDTransformBonusMonsterTypeC69An();
						break;
					case 4:
						IDTransformBonusMonsterTypeD69An();
						break;
				}
				switch (Rnd.get(1, 3)) {
					case 1:
						IDTransformEreshStumble69An();
						break;
					case 2:
						IDTransformVritraStumble69An();
						break;
					case 3:
						IDTransformTiamatStumble69An();
						break;
				}
				switch (Rnd.get(1, 3)) {
					case 1:
						spawn(244605, 510.39917f, 458.81943f, 322.0f, (byte) 21);
						break;
					case 2:
						spawn(244606, 510.39917f, 458.81943f, 322.0f, (byte) 21);
						break;
					case 3:
						spawn(244607, 510.39917f, 458.81943f, 322.0f, (byte) 21);
						break;
				}
				break;
			/*** Player Lvl 70 ***/
			case 70:
				IDTransformRa70An();
				IDTransformDropDragon70Ae();
				IDTransformDevaGuardFi70An();
				IDTransformDevaGuardAs70An();
				IDTransformDevaGuardWi70An();
				IDTransformDevaGuardRa70An();
				IDTransformDevaStumble70An();
				switch (Rnd.get(1, 3)) {
					case 1:
						IDTransformEreshHigh70An();
						break;
					case 2:
						IDTransformVritraHigh70An();
						break;
					case 3:
						IDTransformTiamatHigh70An();
						break;
				}
				switch (Rnd.get(1, 3)) {
					case 1:
						IDTransformEreshGuard70An();
						break;
					case 2:
						IDTransformVritraGuard70An();
						break;
					case 3:
						IDTransformTiamatGuard70An();
						break;
				}
				switch (Rnd.get(1, 4)) {
					case 1:
						IDTransformBonusMonsterTypeA70An();
						break;
					case 2:
						IDTransformBonusMonsterTypeB70An();
						break;
					case 3:
						IDTransformBonusMonsterTypeC70An();
						break;
					case 4:
						IDTransformBonusMonsterTypeD70An();
						break;
				}
				switch (Rnd.get(1, 3)) {
					case 1:
						IDTransformEreshStumble70An();
						break;
					case 2:
						IDTransformVritraStumble70An();
						break;
					case 3:
						IDTransformTiamatStumble70An();
						break;
				}
				switch (Rnd.get(1, 3)) {
					case 1:
						spawn(244646, 510.39917f, 458.81943f, 322.0f, (byte) 21);
						break;
					case 2:
						spawn(244647, 510.39917f, 458.81943f, 322.0f, (byte) 21);
						break;
					case 3:
						spawn(244648, 510.39917f, 458.81943f, 322.0f, (byte) 21);
						break;
				}
				break;
			/*** Player Lvl 71 ***/
			case 71:
				IDTransformRa71An();
				IDTransformDropDragon71Ae();
				IDTransformDevaGuardFi71An();
				IDTransformDevaGuardAs71An();
				IDTransformDevaGuardWi71An();
				IDTransformDevaGuardRa71An();
				IDTransformDevaStumble71An();
				switch (Rnd.get(1, 3)) {
					case 1:
						IDTransformEreshHigh71An();
						break;
					case 2:
						IDTransformVritraHigh71An();
						break;
					case 3:
						IDTransformTiamatHigh71An();
						break;
				}
				switch (Rnd.get(1, 3)) {
					case 1:
						IDTransformEreshGuard71An();
						break;
					case 2:
						IDTransformVritraGuard71An();
						break;
					case 3:
						IDTransformTiamatGuard71An();
						break;
				}
				switch (Rnd.get(1, 4)) {
					case 1:
						IDTransformBonusMonsterTypeA71An();
						break;
					case 2:
						IDTransformBonusMonsterTypeB71An();
						break;
					case 3:
						IDTransformBonusMonsterTypeC71An();
						break;
					case 4:
						IDTransformBonusMonsterTypeD71An();
						break;
				}
				switch (Rnd.get(1, 3)) {
					case 1:
						IDTransformEreshStumble71An();
						break;
					case 2:
						IDTransformVritraStumble71An();
						break;
					case 3:
						IDTransformTiamatStumble71An();
						break;
				}
				switch (Rnd.get(1, 3)) {
					case 1:
						spawn(244687, 510.39917f, 458.81943f, 322.0f, (byte) 21);
						break;
					case 2:
						spawn(244688, 510.39917f, 458.81943f, 322.0f, (byte) 21);
						break;
					case 3:
						spawn(244689, 510.39917f, 458.81943f, 322.0f, (byte) 21);
						break;
				}
				break;
			/*** Player Lvl 72 ***/
			case 72:
				IDTransformRa72An();
				IDTransformDropDragon72Ae();
				IDTransformDevaGuardFi72An();
				IDTransformDevaGuardAs72An();
				IDTransformDevaGuardWi72An();
				IDTransformDevaGuardRa72An();
				IDTransformDevaStumble72An();
				switch (Rnd.get(1, 3)) {
					case 1:
						IDTransformEreshHigh72An();
						break;
					case 2:
						IDTransformVritraHigh72An();
						break;
					case 3:
						IDTransformTiamatHigh72An();
						break;
				}
				switch (Rnd.get(1, 3)) {
					case 1:
						IDTransformEreshGuard72An();
						break;
					case 2:
						IDTransformVritraGuard72An();
						break;
					case 3:
						IDTransformTiamatGuard72An();
						break;
				}
				switch (Rnd.get(1, 4)) {
					case 1:
						IDTransformBonusMonsterTypeA72An();
						break;
					case 2:
						IDTransformBonusMonsterTypeB72An();
						break;
					case 3:
						IDTransformBonusMonsterTypeC72An();
						break;
					case 4:
						IDTransformBonusMonsterTypeD72An();
						break;
				}
				switch (Rnd.get(1, 3)) {
					case 1:
						IDTransformEreshStumble72An();
						break;
					case 2:
						IDTransformVritraStumble72An();
						break;
					case 3:
						IDTransformTiamatStumble72An();
						break;
				}
				switch (Rnd.get(1, 3)) {
					case 1:
						spawn(244728, 510.39917f, 458.81943f, 322.0f, (byte) 21);
						break;
					case 2:
						spawn(244729, 510.39917f, 458.81943f, 322.0f, (byte) 21);
						break;
					case 3:
						spawn(244730, 510.39917f, 458.81943f, 322.0f, (byte) 21);
						break;
				}
				break;
			/*** Player Lvl 73 ***/
			case 73:
				IDTransformRa73An();
				IDTransformDropDragon73Ae();
				IDTransformDevaGuardFi73An();
				IDTransformDevaGuardAs73An();
				IDTransformDevaGuardWi73An();
				IDTransformDevaGuardRa73An();
				IDTransformDevaStumble73An();
				switch (Rnd.get(1, 3)) {
					case 1:
						IDTransformEreshHigh73An();
						break;
					case 2:
						IDTransformVritraHigh73An();
						break;
					case 3:
						IDTransformTiamatHigh73An();
						break;
				}
				switch (Rnd.get(1, 3)) {
					case 1:
						IDTransformEreshGuard73An();
						break;
					case 2:
						IDTransformVritraGuard73An();
						break;
					case 3:
						IDTransformTiamatGuard73An();
						break;
				}
				switch (Rnd.get(1, 4)) {
					case 1:
						IDTransformBonusMonsterTypeA73An();
						break;
					case 2:
						IDTransformBonusMonsterTypeB73An();
						break;
					case 3:
						IDTransformBonusMonsterTypeC73An();
						break;
					case 4:
						IDTransformBonusMonsterTypeD73An();
						break;
				}
				switch (Rnd.get(1, 3)) {
					case 1:
						IDTransformEreshStumble73An();
						break;
					case 2:
						IDTransformVritraStumble73An();
						break;
					case 3:
						IDTransformTiamatStumble73An();
						break;
				}
				switch (Rnd.get(1, 3)) {
					case 1:
						spawn(244769, 510.39917f, 458.81943f, 322.0f, (byte) 21);
						break;
					case 2:
						spawn(244770, 510.39917f, 458.81943f, 322.0f, (byte) 21);
						break;
					case 3:
						spawn(244771, 510.39917f, 458.81943f, 322.0f, (byte) 21);
						break;
				}
				break;
			/*** Player Lvl 74 ***/
			case 74:
				IDTransformRa74An();
				IDTransformDropDragon74Ae();
				IDTransformDevaGuardFi74An();
				IDTransformDevaGuardAs74An();
				IDTransformDevaGuardWi74An();
				IDTransformDevaGuardRa74An();
				IDTransformDevaStumble74An();
				switch (Rnd.get(1, 3)) {
					case 1:
						IDTransformEreshHigh74An();
						break;
					case 2:
						IDTransformVritraHigh74An();
						break;
					case 3:
						IDTransformTiamatHigh74An();
						break;
				}
				switch (Rnd.get(1, 3)) {
					case 1:
						IDTransformEreshGuard74An();
						break;
					case 2:
						IDTransformVritraGuard74An();
						break;
					case 3:
						IDTransformTiamatGuard74An();
						break;
				}
				switch (Rnd.get(1, 4)) {
					case 1:
						IDTransformBonusMonsterTypeA74An();
						break;
					case 2:
						IDTransformBonusMonsterTypeB74An();
						break;
					case 3:
						IDTransformBonusMonsterTypeC74An();
						break;
					case 4:
						IDTransformBonusMonsterTypeD74An();
						break;
				}
				switch (Rnd.get(1, 3)) {
					case 1:
						IDTransformEreshStumble74An();
						break;
					case 2:
						IDTransformVritraStumble74An();
						break;
					case 3:
						IDTransformTiamatStumble74An();
						break;
				}
				switch (Rnd.get(1, 3)) {
					case 1:
						spawn(244810, 510.39917f, 458.81943f, 322.0f, (byte) 21);
						break;
					case 2:
						spawn(244811, 510.39917f, 458.81943f, 322.0f, (byte) 21);
						break;
					case 3:
						spawn(244812, 510.39917f, 458.81943f, 322.0f, (byte) 21);
						break;
				}
				break;
			/*** Player Lvl 75-83 ***/
			case 75:
			case 76:
			case 77:
			case 78:
			case 79:
			case 80:
			case 81:
			case 82:
			case 83:
				IDTransformRa75An();
				IDTransformDropDragon75Ae();
				IDTransformDevaGuardFi75An();
				IDTransformDevaGuardAs75An();
				IDTransformDevaGuardWi75An();
				IDTransformDevaGuardRa75An();
				IDTransformDevaStumble75An();
				switch (Rnd.get(1, 3)) {
					case 1:
						IDTransformEreshHigh75An();
						break;
					case 2:
						IDTransformVritraHigh75An();
						break;
					case 3:
						IDTransformTiamatHigh75An();
						break;
				}
				switch (Rnd.get(1, 3)) {
					case 1:
						IDTransformEreshGuard75An();
						break;
					case 2:
						IDTransformVritraGuard75An();
						break;
					case 3:
						IDTransformTiamatGuard75An();
						break;
				}
				switch (Rnd.get(1, 4)) {
					case 1:
						IDTransformBonusMonsterTypeA75An();
						break;
					case 2:
						IDTransformBonusMonsterTypeB75An();
						break;
					case 3:
						IDTransformBonusMonsterTypeC75An();
						break;
					case 4:
						IDTransformBonusMonsterTypeD75An();
						break;
				}
				switch (Rnd.get(1, 3)) {
					case 1:
						IDTransformEreshStumble75An();
						break;
					case 2:
						IDTransformVritraStumble75An();
						break;
					case 3:
						IDTransformTiamatStumble75An();
						break;
				}
				switch (Rnd.get(1, 3)) {
					case 1:
						spawn(244851, 510.39917f, 458.81943f, 322.0f, (byte) 21);
						break;
					case 2:
						spawn(244852, 510.39917f, 458.81943f, 322.0f, (byte) 21);
						break;
					case 3:
						spawn(244853, 510.39917f, 458.81943f, 322.0f, (byte) 21);
						break;
				}
				break;
		}
	}

	/*** Room Form ***/
	private void IDTransformARoomForm() {
		switch (Rnd.get(1, 4)) {
			case 1:
				spawn(245869, 758.6982f, 516.9711f, 339.81848f, (byte) 100);
				spawn(245870, 763.16156f, 510.3873f, 339.81848f, (byte) 41);
				spawn(245871, 764.7666f, 516.008f, 339.81848f, (byte) 71);
				spawn(245872, 757.1974f, 511.36588f, 339.81848f, (byte) 11);
				spawn(245419, 760.8957f, 513.91797f, 339.85913f, (byte) 90);
				break;
			case 2:
				spawn(245869, 758.6982f, 516.9711f, 339.81848f, (byte) 100);
				spawn(245870, 763.16156f, 510.3873f, 339.81848f, (byte) 41);
				spawn(245871, 764.7666f, 516.008f, 339.81848f, (byte) 71);
				spawn(245872, 757.1974f, 511.36588f, 339.81848f, (byte) 11);
				spawn(245420, 760.8957f, 513.91797f, 339.85913f, (byte) 90);
				break;
			case 3:
				spawn(245869, 758.6982f, 516.9711f, 339.81848f, (byte) 100);
				spawn(245870, 763.16156f, 510.3873f, 339.81848f, (byte) 41);
				spawn(245871, 764.7666f, 516.008f, 339.81848f, (byte) 71);
				spawn(245872, 757.1974f, 511.36588f, 339.81848f, (byte) 11);
				spawn(245421, 760.8957f, 513.91797f, 339.85913f, (byte) 90);
				break;
			case 4:
				spawn(245869, 758.6982f, 516.9711f, 339.81848f, (byte) 100);
				spawn(245870, 763.16156f, 510.3873f, 339.81848f, (byte) 41);
				spawn(245871, 764.7666f, 516.008f, 339.81848f, (byte) 71);
				spawn(245872, 757.1974f, 511.36588f, 339.81848f, (byte) 11);
				spawn(245422, 760.8957f, 513.91797f, 339.85913f, (byte) 90);
				break;
		}
	}

	private void IDTransformBRoomForm() {
		switch (Rnd.get(1, 4)) {
			case 1:
				spawn(245873, 609.6464f, 607.68384f, 354.69708f, (byte) 90);
				spawn(245874, 609.82184f, 597.31396f, 354.69708f, (byte) 30);
				spawn(245875, 615.1253f, 602.52295f, 354.69708f, (byte) 59);
				spawn(245876, 604.41425f, 602.0602f, 354.69708f, (byte) 1);
				spawn(245423, 610.70374f, 602.6874f, 354.7417f, (byte) 59);
				break;
			case 2:
				spawn(245873, 609.6464f, 607.68384f, 354.69708f, (byte) 90);
				spawn(245874, 609.82184f, 597.31396f, 354.69708f, (byte) 30);
				spawn(245875, 615.1253f, 602.52295f, 354.69708f, (byte) 59);
				spawn(245876, 604.41425f, 602.0602f, 354.69708f, (byte) 1);
				spawn(245424, 610.70374f, 602.6874f, 354.7417f, (byte) 59);
				break;
			case 3:
				spawn(245873, 609.6464f, 607.68384f, 354.69708f, (byte) 90);
				spawn(245874, 609.82184f, 597.31396f, 354.69708f, (byte) 30);
				spawn(245875, 615.1253f, 602.52295f, 354.69708f, (byte) 59);
				spawn(245876, 604.41425f, 602.0602f, 354.69708f, (byte) 1);
				spawn(245425, 610.70374f, 602.6874f, 354.7417f, (byte) 59);
				break;
			case 4:
				spawn(245873, 609.6464f, 607.68384f, 354.69708f, (byte) 90);
				spawn(245874, 609.82184f, 597.31396f, 354.69708f, (byte) 30);
				spawn(245875, 615.1253f, 602.52295f, 354.69708f, (byte) 59);
				spawn(245876, 604.41425f, 602.0602f, 354.69708f, (byte) 1);
				spawn(245426, 610.70374f, 602.6874f, 354.7417f, (byte) 59);
				break;
		}
	}

	private void IDTransformCRoomForm() {
		switch (Rnd.get(1, 4)) {
			case 1:
				spawn(245877, 384.56375f, 515.2426f, 350.37335f, (byte) 91);
				spawn(245878, 382.34543f, 513.1757f, 350.37335f, (byte) 0);
				spawn(245879, 384.6212f, 510.97125f, 350.37335f, (byte) 30);
				spawn(245880, 386.9315f, 513.2064f, 350.37335f, (byte) 60);
				spawn(245427, 384.5316f, 513.82904f, 350.37335f, (byte) 91);
				break;
			case 2:
				spawn(245877, 384.56375f, 515.2426f, 350.37335f, (byte) 91);
				spawn(245878, 382.34543f, 513.1757f, 350.37335f, (byte) 0);
				spawn(245879, 384.6212f, 510.97125f, 350.37335f, (byte) 30);
				spawn(245880, 386.9315f, 513.2064f, 350.37335f, (byte) 60);
				spawn(245428, 384.5316f, 513.82904f, 350.37335f, (byte) 91);
				break;
			case 3:
				spawn(245877, 384.56375f, 515.2426f, 350.37335f, (byte) 91);
				spawn(245878, 382.34543f, 513.1757f, 350.37335f, (byte) 0);
				spawn(245879, 384.6212f, 510.97125f, 350.37335f, (byte) 30);
				spawn(245880, 386.9315f, 513.2064f, 350.37335f, (byte) 60);
				spawn(245429, 384.5316f, 513.82904f, 350.37335f, (byte) 91);
				break;
			case 4:
				spawn(245877, 384.56375f, 515.2426f, 350.37335f, (byte) 91);
				spawn(245878, 382.34543f, 513.1757f, 350.37335f, (byte) 0);
				spawn(245879, 384.6212f, 510.97125f, 350.37335f, (byte) 30);
				spawn(245880, 386.9315f, 513.2064f, 350.37335f, (byte) 60);
				spawn(245430, 384.5316f, 513.82904f, 350.37335f, (byte) 91);
				break;
		}
	}

	private void IDTransformCinemaRoom() {
		spawn(244854, 290.98978f, 519.4039f, 350.77097f, (byte) 109);
		spawn(244854, 310.3636f, 518.8186f, 350.77097f, (byte) 11);
		spawn(244854, 301.2251f, 501.2079f, 350.77097f, (byte) 30);
		spawn(244855, 307.03687f, 509.5404f, 350.77097f, (byte) 49);
		spawn(244856, 297.4365f, 509.63843f, 350.8281f, (byte) 15);
		spawn(244856, 301.3423f, 527.84375f, 350.77097f, (byte) 103);
		spawn(244856, 313.1516f, 503.664f, 350.77097f, (byte) 44);
		spawn(244857, 304.17764f, 518.64935f, 350.8281f, (byte) 81);
		spawn(244857, 287.55588f, 505.04895f, 350.77097f, (byte) 10);
		spawn(245885, 292.31613f, 518.5927f, 350.77097f, (byte) 50);
		spawn(245885, 301.10458f, 502.94583f, 350.77097f, (byte) 90);
		spawn(245885, 311.78598f, 519.7351f, 350.77097f, (byte) 72);
		spawn(245886, 305.58627f, 510.2931f, 350.8281f, (byte) 110);
		spawn(245887, 298.4435f, 511.07806f, 350.8281f, (byte) 76);
		spawn(245887, 311.72922f, 504.33463f, 350.77097f, (byte) 108);
		spawn(245887, 301.57993f, 526.58594f, 350.77097f, (byte) 33);
		spawn(245888, 303.31702f, 517.22076f, 350.8281f, (byte) 19);
		spawn(245888, 289.24152f, 506.13815f, 350.77097f, (byte) 71);
	}

	/*** Player Lvl 66 ***/
	/*** Eresh High ***/
	private void IDTransformEreshHigh66An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformEreshHighFi66An();
				break;
			case 2:
				IDTransformEreshHighAs66An();
				break;
			case 3:
				IDTransformEreshHighWi66An();
				break;
			case 4:
				IDTransformEreshHighPr66An();
				break;
		}
	}

	private void IDTransformEreshHighFi66An() {
		spawn(244470, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244470, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244470, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244470, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244470, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244470, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244470, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244470, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244470, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244470, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244470, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244470, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244470, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244470, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244470, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244470, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244470, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformEreshHighAs66An() {
		spawn(244471, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244471, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244471, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244471, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244471, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244471, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244471, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244471, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244471, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244471, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244471, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244471, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244471, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244471, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244471, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244471, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244471, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformEreshHighWi66An() {
		spawn(244472, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244472, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244472, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244472, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244472, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244472, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244472, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244472, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244472, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244472, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244472, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244472, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244472, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244472, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244472, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244472, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244472, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformEreshHighPr66An() {
		spawn(244473, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244473, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244473, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244473, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244473, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244473, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244473, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244473, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244473, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244473, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244473, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244473, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244473, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244473, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244473, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244473, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244473, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	/*** Vritra High ***/
	private void IDTransformVritraHigh66An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformVritraHighFi66An();
				break;
			case 2:
				IDTransformVritraHighAs66An();
				break;
			case 3:
				IDTransformVritraHighWi66An();
				break;
			case 4:
				IDTransformVritraHighPr66An();
				break;
		}
	}

	private void IDTransformVritraHighFi66An() {
		spawn(244474, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244474, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244474, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244474, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244474, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244474, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244474, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244474, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244474, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244474, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244474, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244474, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244474, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244474, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244474, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244474, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244474, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformVritraHighAs66An() {
		spawn(244475, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244475, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244475, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244475, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244475, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244475, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244475, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244475, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244475, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244475, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244475, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244475, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244475, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244475, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244475, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244475, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244475, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformVritraHighWi66An() {
		spawn(244476, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244476, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244476, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244476, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244476, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244476, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244476, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244476, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244476, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244476, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244476, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244476, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244476, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244476, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244476, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244476, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244476, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformVritraHighPr66An() {
		spawn(244477, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244477, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244477, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244477, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244477, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244477, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244477, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244477, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244477, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244477, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244477, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244477, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244477, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244477, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244477, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244477, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244477, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	/*** Tiamat High ***/
	private void IDTransformTiamatHigh66An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformTiamatHighFi66An();
				break;
			case 2:
				IDTransformTiamatHighAs66An();
				break;
			case 3:
				IDTransformTiamatHighWi66An();
				break;
			case 4:
				IDTransformTiamatHighPr66An();
				break;
		}
	}

	private void IDTransformTiamatHighFi66An() {
		spawn(244478, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244478, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244478, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244478, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244478, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244478, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244478, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244478, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244478, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244478, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244478, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244478, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244478, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244478, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244478, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244478, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244478, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformTiamatHighAs66An() {
		spawn(244479, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244479, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244479, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244479, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244479, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244479, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244479, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244479, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244479, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244479, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244479, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244479, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244479, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244479, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244479, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244479, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244479, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformTiamatHighWi66An() {
		spawn(244480, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244480, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244480, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244480, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244480, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244480, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244480, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244480, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244480, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244480, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244480, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244480, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244480, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244480, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244480, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244480, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244480, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformTiamatHighPr66An() {
		spawn(244481, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244481, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244481, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244481, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244481, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244481, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244481, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244481, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244481, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244481, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244481, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244481, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244481, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244481, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244481, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244481, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244481, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	/*** Eresh Guard ***/
	private void IDTransformEreshGuard66An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformEreshGuardFi66An();
				break;
			case 2:
				IDTransformEreshGuardAs66An();
				break;
			case 3:
				IDTransformEreshGuardWi66An();
				break;
			case 4:
				IDTransformEreshGuardPr66An();
				break;
		}
	}

	private void IDTransformEreshGuardFi66An() {
		spawn(244458, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244458, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244458, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244458, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244458, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244458, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244458, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244458, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244458, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244458, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244458, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244458, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244458, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244458, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244458, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244458, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244458, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244458, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244458, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244458, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244458, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244458, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244458, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244458, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244458, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244458, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244458, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244458, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244458, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244458, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244458, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244458, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244458, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244458, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244458, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244458, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244458, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244458, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244458, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244458, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244458, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformEreshGuardAs66An() {
		spawn(244459, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244459, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244459, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244459, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244459, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244459, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244459, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244459, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244459, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244459, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244459, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244459, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244459, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244459, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244459, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244459, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244459, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244459, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244459, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244459, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244459, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244459, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244459, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244459, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244459, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244459, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244459, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244459, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244459, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244459, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244459, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244459, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244459, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244459, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244459, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244459, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244459, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244459, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244459, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244459, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244459, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformEreshGuardWi66An() {
		spawn(244460, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244460, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244460, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244460, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244460, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244460, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244460, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244460, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244460, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244460, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244460, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244460, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244460, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244460, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244460, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244460, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244460, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244460, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244460, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244460, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244460, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244460, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244460, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244460, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244460, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244460, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244460, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244460, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244460, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244460, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244460, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244460, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244460, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244460, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244460, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244460, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244460, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244460, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244460, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244460, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244460, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformEreshGuardPr66An() {
		spawn(244461, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244461, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244461, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244461, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244461, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244461, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244461, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244461, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244461, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244461, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244461, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244461, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244461, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244461, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244461, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244461, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244461, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244461, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244461, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244461, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244461, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244461, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244461, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244461, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244461, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244461, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244461, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244461, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244461, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244461, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244461, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244461, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244461, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244461, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244461, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244461, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244461, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244461, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244461, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244461, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244461, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	/*** Vritra Guard ***/
	private void IDTransformVritraGuard66An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformVritraGuardFi66An();
				break;
			case 2:
				IDTransformVritraGuardAs66An();
				break;
			case 3:
				IDTransformVritraGuardWi66An();
				break;
			case 4:
				IDTransformVritraGuardPr66An();
				break;
		}
	}

	private void IDTransformVritraGuardFi66An() {
		spawn(244462, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244462, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244462, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244462, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244462, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244462, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244462, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244462, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244462, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244462, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244462, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244462, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244462, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244462, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244462, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244462, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244462, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244462, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244462, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244462, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244462, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244462, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244462, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244462, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244462, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244462, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244462, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244462, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244462, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244462, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244462, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244462, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244462, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244462, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244462, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244462, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244462, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244462, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244462, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244462, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244462, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformVritraGuardAs66An() {
		spawn(244463, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244463, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244463, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244463, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244463, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244463, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244463, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244463, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244463, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244463, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244463, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244463, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244463, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244463, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244463, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244463, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244463, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244463, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244463, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244463, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244463, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244463, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244463, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244463, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244463, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244463, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244463, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244463, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244463, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244463, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244463, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244463, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244463, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244463, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244463, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244463, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244463, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244463, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244463, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244463, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244463, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformVritraGuardWi66An() {
		spawn(244464, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244464, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244464, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244464, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244464, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244464, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244464, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244464, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244464, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244464, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244464, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244464, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244464, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244464, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244464, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244464, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244464, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244464, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244464, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244464, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244464, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244464, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244464, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244464, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244464, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244464, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244464, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244464, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244464, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244464, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244464, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244464, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244464, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244464, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244464, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244464, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244464, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244464, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244464, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244464, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244464, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformVritraGuardPr66An() {
		spawn(244465, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244465, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244465, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244465, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244465, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244465, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244465, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244465, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244465, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244465, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244465, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244465, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244465, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244465, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244465, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244465, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244465, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244465, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244465, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244465, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244465, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244465, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244465, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244465, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244465, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244465, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244465, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244465, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244465, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244465, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244465, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244465, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244465, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244465, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244465, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244465, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244465, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244465, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244465, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244465, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244465, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	/*** Tiamat Guard ***/
	private void IDTransformTiamatGuard66An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformTiamatGuardFi66An();
				break;
			case 2:
				IDTransformTiamatGuardAs66An();
				break;
			case 3:
				IDTransformTiamatGuardWi66An();
				break;
			case 4:
				IDTransformTiamatGuardPr66An();
				break;
		}
	}

	private void IDTransformTiamatGuardFi66An() {
		spawn(244466, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244466, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244466, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244466, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244466, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244466, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244466, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244466, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244466, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244466, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244466, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244466, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244466, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244466, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244466, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244466, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244466, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244466, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244466, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244466, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244466, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244466, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244466, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244466, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244466, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244466, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244466, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244466, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244466, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244466, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244466, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244466, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244466, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244466, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244466, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244466, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244466, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244466, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244466, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244466, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244466, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformTiamatGuardAs66An() {
		spawn(244467, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244467, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244467, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244467, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244467, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244467, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244467, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244467, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244467, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244467, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244467, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244467, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244467, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244467, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244467, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244467, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244467, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244467, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244467, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244467, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244467, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244467, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244467, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244467, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244467, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244467, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244467, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244467, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244467, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244467, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244467, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244467, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244467, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244467, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244467, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244467, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244467, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244467, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244467, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244467, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244467, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformTiamatGuardWi66An() {
		spawn(244468, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244468, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244468, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244468, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244468, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244468, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244468, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244468, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244468, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244468, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244468, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244468, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244468, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244468, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244468, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244468, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244468, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244468, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244468, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244468, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244468, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244468, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244468, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244468, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244468, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244468, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244468, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244468, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244468, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244468, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244468, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244468, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244468, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244468, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244468, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244468, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244468, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244468, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244468, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244468, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244468, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformTiamatGuardPr66An() {
		spawn(244469, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244469, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244469, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244469, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244469, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244469, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244469, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244469, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244469, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244469, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244469, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244469, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244469, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244469, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244469, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244469, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244469, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244469, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244469, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244469, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244469, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244469, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244469, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244469, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244469, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244469, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244469, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244469, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244469, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244469, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244469, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244469, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244469, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244469, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244469, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244469, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244469, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244469, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244469, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244469, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244469, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	/*** Deva Guard ***/
	private void IDTransformDevaGuardFi66An() {
		spawn(244485, 543.9115f, 491.4599f, 322.04422f, (byte) 91);
		spawn(244485, 547.9682f, 490.34494f, 321.50793f, (byte) 90);
		spawn(244485, 534.53534f, 485.35455f, 322.0f, (byte) 89);
		spawn(244485, 541.57935f, 487.14307f, 322.0f, (byte) 89);
		spawn(244485, 516.4117f, 585.5329f, 321.97427f, (byte) 90);
		spawn(244485, 499.82422f, 585.81537f, 322.02252f, (byte) 92);
		spawn(244485, 501.61078f, 555.0141f, 321.84106f, (byte) 12);
	}

	private void IDTransformDevaGuardAs66An() {
		spawn(244486, 855.63855f, 484.74774f, 349.08722f, (byte) 45);
		spawn(244486, 852.21844f, 482.80756f, 349.41986f, (byte) 44);
		spawn(244486, 864.0118f, 492.74384f, 349.56552f, (byte) 46);
		spawn(244486, 508.17993f, 587.8108f, 322.56973f, (byte) 90);
		spawn(244486, 526.8684f, 493.7475f, 322.0f, (byte) 91);
		spawn(244486, 537.37225f, 508.5786f, 322.0f, (byte) 90);
		spawn(244486, 512.8187f, 590.4575f, 322.56216f, (byte) 90);
		spawn(244486, 504.50745f, 590.3867f, 322.56216f, (byte) 90);
		spawn(244486, 482.3006f, 496.94232f, 342.22174f, (byte) 27);
		spawn(244486, 594.4832f, 673.60126f, 350.85538f, (byte) 54);
		spawn(244486, 634.298f, 511.2995f, 339.61545f, (byte) 61);
		spawn(244486, 530.982f, 493.62543f, 322.0f, (byte) 90);
		spawn(244486, 641.9954f, 514.2143f, 339.61542f, (byte) 61);
		spawn(244486, 505.45636f, 584.90186f, 322.0f, (byte) 90);
		spawn(244486, 510.66394f, 584.80176f, 322.0f, (byte) 90);
		spawn(244486, 859.2291f, 490.53555f, 348.98047f, (byte) 45);
		spawn(244486, 604.1567f, 635.69995f, 352.53815f, (byte) 30);
		spawn(244486, 467.0f, 476.0f, 345.70047f, (byte) 32);
		spawn(244486, 536.2914f, 488.65588f, 322.0f, (byte) 90);
		spawn(244486, 639.6124f, 525.6597f, 339.61542f, (byte) 62);
		spawn(244486, 885.47394f, 464.9668f, 351.0f, (byte) 66);
		spawn(244486, 642.88293f, 520.17224f, 339.61542f, (byte) 60);
		spawn(244486, 546.91925f, 510.66574f, 321.94254f, (byte) 91);
		spawn(244486, 527.7633f, 502.87167f, 321.6398f, (byte) 91);
		spawn(244486, 885.5619f, 456.29126f, 351.02737f, (byte) 55);
	}

	private void IDTransformDevaGuardWi66An() {
		spawn(244487, 513.0236f, 593.86285f, 322.56216f, (byte) 90);
		spawn(244487, 501.3256f, 593.69885f, 322.56216f, (byte) 90);
		spawn(244487, 633.75323f, 516.50934f, 339.61545f, (byte) 61);
		spawn(244487, 544.93945f, 499.1886f, 322.0f, (byte) 91);
		spawn(244487, 530.8947f, 502.38684f, 321.86185f, (byte) 91);
		spawn(244487, 634.5065f, 522.0903f, 339.61545f, (byte) 53);
		spawn(244487, 885.382f, 460.77805f, 351.0f, (byte) 60);
		spawn(244487, 612.8048f, 643.80194f, 352.46414f, (byte) 30);
		spawn(244487, 538.00024f, 495.57394f, 322.0f, (byte) 90);
		spawn(244487, 526.9178f, 486.92227f, 321.87527f, (byte) 85);
	}

	private void IDTransformDevaGuardRa66An() {
		spawn(244488, 529.87f, 545.5564f, 321.85876f, (byte) 95);
		spawn(244488, 526.1612f, 545.1311f, 321.58078f, (byte) 88);
		spawn(244488, 546.40234f, 523.6142f, 321.97485f, (byte) 87);
		spawn(244488, 549.9647f, 524.9884f, 321.9978f, (byte) 95);
		spawn(244488, 520.9293f, 586.19006f, 321.94193f, (byte) 95);
		spawn(244488, 495.259f, 586.9457f, 322.0659f, (byte) 81);
		spawn(244488, 505.0709f, 552.7453f, 322.0f, (byte) 28);
	}

	/*** Eresh Stumble ***/
	private void IDTransformEreshStumble66An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformEreshStumbleFi66An();
				break;
			case 2:
				IDTransformEreshStumbleAs66An();
				break;
			case 3:
				IDTransformEreshStumbleWi66An();
				break;
			case 4:
				IDTransformEreshStumblePr66An();
				break;
		}
	}

	private void IDTransformEreshStumbleFi66An() {
		spawn(244864, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(244864, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(244864, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformEreshStumbleAs66An() {
		spawn(244865, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(244865, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(244865, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformEreshStumbleWi66An() {
		spawn(244866, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(244866, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(244866, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformEreshStumblePr66An() {
		spawn(244867, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(244867, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(244867, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	/*** Vritra Stumble ***/
	private void IDTransformVritraStumble66An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformVritraStumbleFi66An();
				break;
			case 2:
				IDTransformVritraStumbleAs66An();
				break;
			case 3:
				IDTransformVritraStumbleWi66An();
				break;
			case 4:
				IDTransformVritraStumblePr66An();
				break;
		}
	}

	private void IDTransformVritraStumbleFi66An() {
		spawn(244868, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(244868, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(244868, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformVritraStumbleAs66An() {
		spawn(244869, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(244869, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(244869, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformVritraStumbleWi66An() {
		spawn(244870, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(244870, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(244870, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformVritraStumblePr66An() {
		spawn(244871, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(244871, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(244871, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	/*** Tiamat Stumble ***/
	private void IDTransformTiamatStumble66An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformTiamatStumbleFi66An();
				break;
			case 2:
				IDTransformTiamatStumbleAs66An();
				break;
			case 3:
				IDTransformTiamatStumbleWi66An();
				break;
			case 4:
				IDTransformTiamatStumblePr66An();
				break;
		}
	}

	private void IDTransformTiamatStumbleFi66An() {
		spawn(244872, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(244872, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(244872, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformTiamatStumbleAs66An() {
		spawn(244873, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(244873, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(244873, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformTiamatStumbleWi66An() {
		spawn(244874, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(244874, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(244874, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformTiamatStumblePr66An() {
		spawn(244875, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(244875, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(244875, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	/*** Deva Stumble ***/
	private void IDTransformDevaStumble66An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformDevaStumbleFi66An();
				break;
			case 2:
				IDTransformDevaStumbleAs66An();
				break;
			case 3:
				IDTransformDevaStumbleWi66An();
				break;
			case 4:
				IDTransformDevaStumblePr66An();
				break;
		}
	}

	private void IDTransformDevaStumbleFi66An() {
		spawn(244876, 677.0f, 516.0f, 338.24844f, (byte) 43);
		spawn(244876, 505.28098f, 648.23944f, 317.08282f, (byte) 113);
		spawn(244876, 507.24606f, 628.40027f, 318.4649f, (byte) 61);
		spawn(244876, 504.39532f, 615.5569f, 320.3689f, (byte) 61);
		spawn(244876, 504.10117f, 601.42255f, 322.4643f, (byte) 60);
		spawn(244876, 513.7057f, 643.4998f, 317.10632f, (byte) 11);
		spawn(244876, 511.02466f, 614.0008f, 320.5996f, (byte) 0);
		spawn(244876, 503.81223f, 640.00806f, 317.11395f, (byte) 40);
		spawn(244876, 531.7686f, 642.5042f, 317.08282f, (byte) 112);
		spawn(244876, 522.0014f, 648.79095f, 317.08282f, (byte) 55);
	}

	private void IDTransformDevaStumbleAs66An() {
		spawn(244877, 677.0f, 516.0f, 338.24844f, (byte) 43);
		spawn(244877, 505.28098f, 648.23944f, 317.08282f, (byte) 113);
		spawn(244877, 507.24606f, 628.40027f, 318.4649f, (byte) 61);
		spawn(244877, 504.39532f, 615.5569f, 320.3689f, (byte) 61);
		spawn(244877, 504.10117f, 601.42255f, 322.4643f, (byte) 60);
		spawn(244877, 513.7057f, 643.4998f, 317.10632f, (byte) 11);
		spawn(244877, 511.02466f, 614.0008f, 320.5996f, (byte) 0);
		spawn(244877, 503.81223f, 640.00806f, 317.11395f, (byte) 40);
		spawn(244877, 531.7686f, 642.5042f, 317.08282f, (byte) 112);
		spawn(244877, 522.0014f, 648.79095f, 317.08282f, (byte) 55);
	}

	private void IDTransformDevaStumbleWi66An() {
		spawn(244878, 677.0f, 516.0f, 338.24844f, (byte) 43);
		spawn(244878, 505.28098f, 648.23944f, 317.08282f, (byte) 113);
		spawn(244878, 507.24606f, 628.40027f, 318.4649f, (byte) 61);
		spawn(244878, 504.39532f, 615.5569f, 320.3689f, (byte) 61);
		spawn(244878, 504.10117f, 601.42255f, 322.4643f, (byte) 60);
		spawn(244878, 513.7057f, 643.4998f, 317.10632f, (byte) 11);
		spawn(244878, 511.02466f, 614.0008f, 320.5996f, (byte) 0);
		spawn(244878, 503.81223f, 640.00806f, 317.11395f, (byte) 40);
		spawn(244878, 531.7686f, 642.5042f, 317.08282f, (byte) 112);
		spawn(244878, 522.0014f, 648.79095f, 317.08282f, (byte) 55);
	}

	private void IDTransformDevaStumblePr66An() {
		spawn(244879, 677.0f, 516.0f, 338.24844f, (byte) 43);
		spawn(244879, 505.28098f, 648.23944f, 317.08282f, (byte) 113);
		spawn(244879, 507.24606f, 628.40027f, 318.4649f, (byte) 61);
		spawn(244879, 504.39532f, 615.5569f, 320.3689f, (byte) 61);
		spawn(244879, 504.10117f, 601.42255f, 322.4643f, (byte) 60);
		spawn(244879, 513.7057f, 643.4998f, 317.10632f, (byte) 11);
		spawn(244879, 511.02466f, 614.0008f, 320.5996f, (byte) 0);
		spawn(244879, 503.81223f, 640.00806f, 317.11395f, (byte) 40);
		spawn(244879, 531.7686f, 642.5042f, 317.08282f, (byte) 112);
		spawn(244879, 522.0014f, 648.79095f, 317.08282f, (byte) 55);
	}

	/*** Drop Dragon ***/
	private void IDTransformDropDragon66Ae() {
		switch (Rnd.get(1, 3)) {
			case 1:
				spawn(244892, 622.6814f, 551.9144f, 346.06897f, (byte) 105);
				break;
			case 2:
				spawn(244893, 622.6814f, 551.9144f, 346.06897f, (byte) 105);
				break;
			case 3:
				spawn(244894, 622.6814f, 551.9144f, 346.06897f, (byte) 105);
				break;
		}
		switch (Rnd.get(1, 3)) {
			case 1:
				spawn(244892, 607.8992f, 674.06934f, 352.29062f, (byte) 90);
				break;
			case 2:
				spawn(244893, 607.8992f, 674.06934f, 352.29062f, (byte) 90);
				break;
			case 3:
				spawn(244894, 607.8992f, 674.06934f, 352.29062f, (byte) 90);
				break;
		}
	}

	/*** Bonus Monster ***/
	private void IDTransformBonusMonsterTypeA66An() {
		spawn(246200, 761.54095f, 562.17f, 341.0512f, (byte) 90);
		spawn(246200, 476.94467f, 549.22363f, 345.6048f, (byte) 90);
		spawn(246200, 609.2811f, 707.6352f, 355.10846f, (byte) 93);
	}

	private void IDTransformBonusMonsterTypeB66An() {
		spawn(246201, 761.54095f, 562.17f, 341.0512f, (byte) 90);
		spawn(246201, 476.94467f, 549.22363f, 345.6048f, (byte) 90);
		spawn(246201, 609.2811f, 707.6352f, 355.10846f, (byte) 93);
	}

	private void IDTransformBonusMonsterTypeC66An() {
		spawn(246202, 761.54095f, 562.17f, 341.0512f, (byte) 90);
		spawn(246202, 476.94467f, 549.22363f, 345.6048f, (byte) 90);
		spawn(246202, 609.2811f, 707.6352f, 355.10846f, (byte) 93);
	}

	private void IDTransformBonusMonsterTypeD66An() {
		spawn(246203, 761.54095f, 562.17f, 341.0512f, (byte) 90);
		spawn(246203, 476.94467f, 549.22363f, 345.6048f, (byte) 90);
		spawn(246203, 609.2811f, 707.6352f, 355.10846f, (byte) 93);
	}

	/*** Eresh Warp ***/
	private void IDTransformEreshWarp66() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformEreshWarpFi66An();
				break;
			case 2:
				IDTransformEreshWarpAs66An();
				break;
			case 3:
				IDTransformEreshWarpWi66An();
				break;
			case 4:
				IDTransformEreshWarpPr66An();
				break;
		}
	}

	private void IDTransformEreshWarpFi66An() {
		sp(245577, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245577, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245577, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245577, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245577, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245577, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245577, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245577, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245577, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245577, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245577, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245577, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245577, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245577, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245577, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245577, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245577, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245577, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245577, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformEreshWarpAs66An() {
		sp(245578, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245578, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245578, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245578, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245578, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245578, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245578, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245578, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245578, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245578, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245578, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245578, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245578, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245578, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245578, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245578, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245578, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245578, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245578, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformEreshWarpWi66An() {
		sp(245579, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245579, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245579, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245579, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245579, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245579, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245579, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245579, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245579, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245579, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245579, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245579, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245579, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245579, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245579, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245579, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245579, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245579, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245579, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformEreshWarpPr66An() {
		sp(245580, 725.0f, 515.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245580, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245580, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245580, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245580, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245580, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245580, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245580, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245580, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245580, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245580, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245580, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245580, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245580, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245580, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245580, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245580, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245580, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245580, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	/*** Vritra Warp ***/
	private void IDTransformVritraWarp66() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformVritraWarpFi66An();
				break;
			case 2:
				IDTransformVritraWarpAs66An();
				break;
			case 3:
				IDTransformVritraWarpWi66An();
				break;
			case 4:
				IDTransformVritraWarpPr66An();
				break;
		}
	}

	private void IDTransformVritraWarpFi66An() {
		sp(245581, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245581, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245581, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245581, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245581, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245581, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245581, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245581, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245581, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245581, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245581, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245581, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245581, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245581, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245581, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245581, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245581, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245581, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245581, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformVritraWarpAs66An() {
		sp(245582, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245582, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245582, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245582, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245582, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245582, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245582, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245582, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245582, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245582, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245582, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245582, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245582, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245582, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245582, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245582, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245582, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245582, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245582, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformVritraWarpWi66An() {
		sp(245583, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245583, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245583, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245583, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245583, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245583, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245583, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245583, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245583, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245583, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245583, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245583, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245583, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245583, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245583, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245583, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245583, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245583, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245583, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformVritraWarpPr66An() {
		sp(245584, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245584, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245584, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245584, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245584, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245584, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245584, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245584, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245584, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245584, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245584, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245584, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245584, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245584, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245584, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245584, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245584, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245584, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245584, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	/*** Tiamat Warp ***/
	private void IDTransformTiamatWarp66() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformTiamatWarpFi66An();
				break;
			case 2:
				IDTransformTiamatWarpAs66An();
				break;
			case 3:
				IDTransformTiamatWarpWi66An();
				break;
			case 4:
				IDTransformTiamatWarpPr66An();
				break;
		}
	}

	private void IDTransformTiamatWarpFi66An() {
		sp(245585, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245585, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245585, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245585, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245585, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245585, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245585, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245585, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245585, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245585, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245585, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245585, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245585, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245585, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245585, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245585, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245585, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245585, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245585, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformTiamatWarpAs66An() {
		sp(245586, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245586, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245586, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245586, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245586, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245586, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245586, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245586, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245586, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245586, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245586, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245586, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245586, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245586, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245586, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245586, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245586, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245586, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245586, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformTiamatWarpWi66An() {
		sp(245587, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245587, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245587, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245587, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245587, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245587, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245587, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245587, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245587, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245587, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245587, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245587, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245587, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245587, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245587, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245587, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245587, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245587, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245587, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformTiamatWarpPr66An() {
		sp(245588, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245588, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245588, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245588, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245588, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245588, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245588, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245588, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245588, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245588, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245588, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245588, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245588, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245588, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245588, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245588, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245588, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245588, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245588, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	/*** Ranger ***/
	private void IDTransformRa66An() {
		switch (Rnd.get(1, 3)) {
			case 1:
				IDTransformEreshRa66An();
				break;
			case 2:
				IDTransformVritraRa66An();
				break;
			case 3:
				IDTransformTiamatRa66An();
				break;
		}
	}

	/*** Eresh Ranger ***/
	private void IDTransformEreshRa66An() {
		spawn(245697, 543.0933f, 557.8653f, 322.0f, (byte) 70);
		spawn(245697, 550.4571f, 546.09937f, 322.0f, (byte) 61);
		spawn(245697, 550.2217f, 539.29755f, 322.0f, (byte) 59);
		spawn(245697, 543.0761f, 534.39087f, 322.0f, (byte) 43);
		spawn(245697, 534.18945f, 534.63385f, 321.875f, (byte) 21);
		spawn(245697, 522.56396f, 467.8957f, 322.0f, (byte) 37);
		spawn(245697, 527.70734f, 475.95486f, 322.0f, (byte) 7);
		spawn(245697, 509.6404f, 476.19836f, 322.0f, (byte) 118);
		spawn(245697, 501.61966f, 467.60513f, 322.0f, (byte) 5);
		spawn(245697, 503.2415f, 446.9365f, 321.8276f, (byte) 18);
		spawn(245697, 542.8608f, 472.11404f, 322.0f, (byte) 51);
		spawn(245697, 539.9602f, 467.72766f, 322.0f, (byte) 50);
		spawn(245697, 516.02167f, 453.723f, 321.875f, (byte) 60);
		spawn(245697, 466.30823f, 509.6083f, 342.1901f, (byte) 0);
		spawn(245697, 466.1356f, 516.46765f, 342.1901f, (byte) 0);
		spawn(245697, 468.7195f, 513.0958f, 342.1901f, (byte) 0);
		spawn(245697, 446.91077f, 509.62943f, 342.2713f, (byte) 1);
		spawn(245697, 446.86057f, 517.2843f, 342.27173f, (byte) 0);
		spawn(245697, 449.71277f, 513.23883f, 342.24753f, (byte) 0);
		spawn(245697, 427.54434f, 509.3018f, 342.43564f, (byte) 0);
		spawn(245697, 427.4629f, 517.19116f, 342.43634f, (byte) 0);
		spawn(245697, 430.6331f, 513.1085f, 342.40942f, (byte) 0);
		spawn(245697, 483.40283f, 475.9793f, 345.70047f, (byte) 81);
		spawn(245697, 469.33865f, 454.01733f, 345.70047f, (byte) 4);
		spawn(245697, 482.78824f, 526.4342f, 342.234f, (byte) 78);
		spawn(245697, 502.18372f, 508.00098f, 339.63123f, (byte) 39);
		spawn(245697, 497.84283f, 521.77655f, 339.63815f, (byte) 77);
		spawn(245697, 508.98663f, 527.061f, 339.63126f, (byte) 76);
		spawn(245697, 502.5523f, 649.1053f, 317.08282f, (byte) 109);
		spawn(245697, 522.4196f, 650.3398f, 317.08282f, (byte) 99);
		spawn(245697, 540.84265f, 652.0684f, 316.16275f, (byte) 100);
		spawn(245697, 540.5331f, 637.4856f, 316.17746f, (byte) 17);
		spawn(245697, 514.6148f, 641.9673f, 317.10098f, (byte) 33);
		spawn(245697, 539.03076f, 536.819f, 322.0f, (byte) 30);
	}

	/*** Vritra Ranger ***/
	private void IDTransformVritraRa66An() {
		spawn(245698, 543.0933f, 557.8653f, 322.0f, (byte) 70);
		spawn(245698, 550.4571f, 546.09937f, 322.0f, (byte) 61);
		spawn(245698, 550.2217f, 539.29755f, 322.0f, (byte) 59);
		spawn(245698, 543.0761f, 534.39087f, 322.0f, (byte) 43);
		spawn(245698, 534.18945f, 534.63385f, 321.875f, (byte) 21);
		spawn(245698, 522.56396f, 467.8957f, 322.0f, (byte) 37);
		spawn(245698, 527.70734f, 475.95486f, 322.0f, (byte) 7);
		spawn(245698, 509.6404f, 476.19836f, 322.0f, (byte) 118);
		spawn(245698, 501.61966f, 467.60513f, 322.0f, (byte) 5);
		spawn(245698, 503.2415f, 446.9365f, 321.8276f, (byte) 18);
		spawn(245698, 542.8608f, 472.11404f, 322.0f, (byte) 51);
		spawn(245698, 539.9602f, 467.72766f, 322.0f, (byte) 50);
		spawn(245698, 516.02167f, 453.723f, 321.875f, (byte) 60);
		spawn(245698, 466.30823f, 509.6083f, 342.1901f, (byte) 0);
		spawn(245698, 466.1356f, 516.46765f, 342.1901f, (byte) 0);
		spawn(245698, 468.7195f, 513.0958f, 342.1901f, (byte) 0);
		spawn(245698, 446.91077f, 509.62943f, 342.2713f, (byte) 1);
		spawn(245698, 446.86057f, 517.2843f, 342.27173f, (byte) 0);
		spawn(245698, 449.71277f, 513.23883f, 342.24753f, (byte) 0);
		spawn(245698, 427.54434f, 509.3018f, 342.43564f, (byte) 0);
		spawn(245698, 427.4629f, 517.19116f, 342.43634f, (byte) 0);
		spawn(245698, 430.6331f, 513.1085f, 342.40942f, (byte) 0);
		spawn(245698, 483.40283f, 475.9793f, 345.70047f, (byte) 81);
		spawn(245698, 469.33865f, 454.01733f, 345.70047f, (byte) 4);
		spawn(245698, 482.78824f, 526.4342f, 342.234f, (byte) 78);
		spawn(245698, 502.18372f, 508.00098f, 339.63123f, (byte) 39);
		spawn(245698, 497.84283f, 521.77655f, 339.63815f, (byte) 77);
		spawn(245698, 508.98663f, 527.061f, 339.63126f, (byte) 76);
		spawn(245698, 502.5523f, 649.1053f, 317.08282f, (byte) 109);
		spawn(245698, 522.4196f, 650.3398f, 317.08282f, (byte) 99);
		spawn(245698, 540.84265f, 652.0684f, 316.16275f, (byte) 100);
		spawn(245698, 540.5331f, 637.4856f, 316.17746f, (byte) 17);
		spawn(245698, 514.6148f, 641.9673f, 317.10098f, (byte) 33);
		spawn(245698, 539.03076f, 536.819f, 322.0f, (byte) 30);
	}

	/*** Tiamat Ranger ***/
	private void IDTransformTiamatRa66An() {
		spawn(245699, 543.0933f, 557.8653f, 322.0f, (byte) 70);
		spawn(245699, 550.4571f, 546.09937f, 322.0f, (byte) 61);
		spawn(245699, 550.2217f, 539.29755f, 322.0f, (byte) 59);
		spawn(245699, 543.0761f, 534.39087f, 322.0f, (byte) 43);
		spawn(245699, 534.18945f, 534.63385f, 321.875f, (byte) 21);
		spawn(245699, 522.56396f, 467.8957f, 322.0f, (byte) 37);
		spawn(245699, 527.70734f, 475.95486f, 322.0f, (byte) 7);
		spawn(245699, 509.6404f, 476.19836f, 322.0f, (byte) 118);
		spawn(245699, 501.61966f, 467.60513f, 322.0f, (byte) 5);
		spawn(245699, 503.2415f, 446.9365f, 321.8276f, (byte) 18);
		spawn(245699, 542.8608f, 472.11404f, 322.0f, (byte) 51);
		spawn(245699, 539.9602f, 467.72766f, 322.0f, (byte) 50);
		spawn(245699, 516.02167f, 453.723f, 321.875f, (byte) 60);
		spawn(245699, 466.30823f, 509.6083f, 342.1901f, (byte) 0);
		spawn(245699, 466.1356f, 516.46765f, 342.1901f, (byte) 0);
		spawn(245699, 468.7195f, 513.0958f, 342.1901f, (byte) 0);
		spawn(245699, 446.91077f, 509.62943f, 342.2713f, (byte) 1);
		spawn(245699, 446.86057f, 517.2843f, 342.27173f, (byte) 0);
		spawn(245699, 449.71277f, 513.23883f, 342.24753f, (byte) 0);
		spawn(245699, 427.54434f, 509.3018f, 342.43564f, (byte) 0);
		spawn(245699, 427.4629f, 517.19116f, 342.43634f, (byte) 0);
		spawn(245699, 430.6331f, 513.1085f, 342.40942f, (byte) 0);
		spawn(245699, 483.40283f, 475.9793f, 345.70047f, (byte) 81);
		spawn(245699, 469.33865f, 454.01733f, 345.70047f, (byte) 4);
		spawn(245699, 482.78824f, 526.4342f, 342.234f, (byte) 78);
		spawn(245699, 502.18372f, 508.00098f, 339.63123f, (byte) 39);
		spawn(245699, 497.84283f, 521.77655f, 339.63815f, (byte) 77);
		spawn(245699, 508.98663f, 527.061f, 339.63126f, (byte) 76);
		spawn(245699, 502.5523f, 649.1053f, 317.08282f, (byte) 109);
		spawn(245699, 522.4196f, 650.3398f, 317.08282f, (byte) 99);
		spawn(245699, 540.84265f, 652.0684f, 316.16275f, (byte) 100);
		spawn(245699, 540.5331f, 637.4856f, 316.17746f, (byte) 17);
		spawn(245699, 514.6148f, 641.9673f, 317.10098f, (byte) 33);
		spawn(245699, 539.03076f, 536.819f, 322.0f, (byte) 30);
	}

	/*** Player Lvl 67 ***/
	/*** Eresh High ***/
	private void IDTransformEreshHigh67An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformEreshHighFi67An();
				break;
			case 2:
				IDTransformEreshHighAs67An();
				break;
			case 3:
				IDTransformEreshHighWi67An();
				break;
			case 4:
				IDTransformEreshHighPr67An();
				break;
		}
	}

	private void IDTransformEreshHighFi67An() {
		spawn(244511, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244511, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244511, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244511, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244511, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244511, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244511, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244511, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244511, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244511, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244511, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244511, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244511, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244511, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244511, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244511, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244511, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformEreshHighAs67An() {
		spawn(244512, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244512, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244512, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244512, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244512, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244512, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244512, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244512, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244512, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244512, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244512, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244512, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244512, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244512, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244512, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244512, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244512, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformEreshHighWi67An() {
		spawn(244513, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244513, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244513, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244513, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244513, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244513, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244513, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244513, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244513, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244513, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244513, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244513, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244513, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244513, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244513, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244513, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244513, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformEreshHighPr67An() {
		spawn(244514, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244514, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244514, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244514, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244514, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244514, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244514, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244514, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244514, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244514, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244514, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244514, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244514, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244514, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244514, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244514, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244514, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	/*** Vritra High ***/
	private void IDTransformVritraHigh67An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformVritraHighFi67An();
				break;
			case 2:
				IDTransformVritraHighAs67An();
				break;
			case 3:
				IDTransformVritraHighWi67An();
				break;
			case 4:
				IDTransformVritraHighPr67An();
				break;
		}
	}

	private void IDTransformVritraHighFi67An() {
		spawn(244515, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244515, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244515, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244515, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244515, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244515, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244515, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244515, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244515, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244515, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244515, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244515, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244515, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244515, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244515, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244515, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244515, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformVritraHighAs67An() {
		spawn(244516, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244516, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244516, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244516, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244516, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244516, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244516, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244516, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244516, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244516, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244516, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244516, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244516, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244516, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244516, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244516, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244516, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformVritraHighWi67An() {
		spawn(244517, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244517, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244517, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244517, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244517, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244517, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244517, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244517, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244517, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244517, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244517, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244517, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244517, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244517, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244517, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244517, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244517, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformVritraHighPr67An() {
		spawn(244518, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244518, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244518, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244518, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244518, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244518, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244518, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244518, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244518, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244518, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244518, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244518, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244518, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244518, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244518, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244518, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244518, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	/*** Tiamat High ***/
	private void IDTransformTiamatHigh67An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformTiamatHighFi67An();
				break;
			case 2:
				IDTransformTiamatHighAs67An();
				break;
			case 3:
				IDTransformTiamatHighWi67An();
				break;
			case 4:
				IDTransformTiamatHighPr67An();
				break;
		}
	}

	private void IDTransformTiamatHighFi67An() {
		spawn(244519, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244519, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244519, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244519, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244519, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244519, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244519, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244519, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244519, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244519, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244519, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244519, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244519, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244519, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244519, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244519, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244519, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformTiamatHighAs67An() {
		spawn(244520, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244520, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244520, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244520, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244520, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244520, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244520, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244520, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244520, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244520, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244520, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244520, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244520, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244520, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244520, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244520, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244520, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformTiamatHighWi67An() {
		spawn(244521, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244521, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244521, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244521, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244521, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244521, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244521, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244521, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244521, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244521, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244521, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244521, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244521, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244521, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244521, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244521, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244521, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformTiamatHighPr67An() {
		spawn(244522, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244522, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244522, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244522, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244522, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244522, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244522, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244522, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244522, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244522, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244522, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244522, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244522, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244522, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244522, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244522, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244522, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	/*** Eresh Guard ***/
	private void IDTransformEreshGuard67An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformEreshGuardFi67An();
				break;
			case 2:
				IDTransformEreshGuardAs67An();
				break;
			case 3:
				IDTransformEreshGuardWi67An();
				break;
			case 4:
				IDTransformEreshGuardPr67An();
				break;
		}
	}

	private void IDTransformEreshGuardFi67An() {
		spawn(244499, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244499, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244499, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244499, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244499, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244499, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244499, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244499, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244499, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244499, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244499, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244499, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244499, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244499, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244499, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244499, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244499, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244499, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244499, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244499, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244499, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244499, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244499, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244499, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244499, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244499, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244499, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244499, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244499, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244499, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244499, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244499, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244499, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244499, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244499, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244499, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244499, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244499, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244499, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244499, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244499, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformEreshGuardAs67An() {
		spawn(244500, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244500, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244500, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244500, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244500, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244500, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244500, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244500, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244500, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244500, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244500, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244500, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244500, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244500, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244500, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244500, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244500, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244500, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244500, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244500, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244500, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244500, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244500, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244500, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244500, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244500, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244500, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244500, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244500, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244500, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244500, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244500, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244500, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244500, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244500, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244500, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244500, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244500, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244500, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244500, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244500, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformEreshGuardWi67An() {
		spawn(244501, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244501, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244501, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244501, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244501, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244501, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244501, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244501, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244501, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244501, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244501, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244501, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244501, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244501, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244501, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244501, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244501, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244501, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244501, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244501, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244501, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244501, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244501, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244501, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244501, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244501, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244501, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244501, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244501, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244501, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244501, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244501, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244501, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244501, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244501, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244501, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244501, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244501, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244501, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244501, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244501, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformEreshGuardPr67An() {
		spawn(244502, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244502, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244502, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244502, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244502, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244502, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244502, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244502, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244502, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244502, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244502, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244502, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244502, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244502, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244502, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244502, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244502, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244502, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244502, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244502, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244502, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244502, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244502, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244502, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244502, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244502, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244502, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244502, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244502, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244502, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244502, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244502, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244502, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244502, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244502, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244502, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244502, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244502, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244502, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244502, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244502, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	/*** Vritra Guard ***/
	private void IDTransformVritraGuard67An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformVritraGuardFi67An();
				break;
			case 2:
				IDTransformVritraGuardAs67An();
				break;
			case 3:
				IDTransformVritraGuardWi67An();
				break;
			case 4:
				IDTransformVritraGuardPr67An();
				break;
		}
	}

	private void IDTransformVritraGuardFi67An() {
		spawn(244503, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244503, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244503, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244503, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244503, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244503, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244503, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244503, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244503, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244503, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244503, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244503, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244503, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244503, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244503, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244503, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244503, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244503, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244503, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244503, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244503, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244503, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244503, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244503, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244503, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244503, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244503, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244503, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244503, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244503, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244503, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244503, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244503, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244503, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244503, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244503, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244503, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244503, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244503, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244503, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244503, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformVritraGuardAs67An() {
		spawn(244504, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244504, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244504, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244504, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244504, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244504, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244504, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244504, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244504, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244504, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244504, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244504, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244504, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244504, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244504, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244504, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244504, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244504, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244504, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244504, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244504, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244504, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244504, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244504, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244504, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244504, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244504, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244504, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244504, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244504, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244504, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244504, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244504, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244504, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244504, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244504, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244504, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244504, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244504, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244504, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244504, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformVritraGuardWi67An() {
		spawn(244505, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244505, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244505, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244505, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244505, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244505, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244505, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244505, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244505, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244505, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244505, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244505, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244505, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244505, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244505, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244505, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244505, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244505, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244505, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244505, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244505, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244505, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244505, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244505, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244505, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244505, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244505, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244505, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244505, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244505, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244505, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244505, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244505, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244505, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244505, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244505, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244505, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244505, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244505, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244505, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244505, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformVritraGuardPr67An() {
		spawn(244506, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244506, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244506, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244506, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244506, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244506, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244506, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244506, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244506, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244506, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244506, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244506, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244506, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244506, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244506, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244506, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244506, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244506, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244506, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244506, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244506, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244506, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244506, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244506, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244506, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244506, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244506, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244506, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244506, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244506, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244506, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244506, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244506, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244506, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244506, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244506, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244506, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244506, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244506, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244506, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244506, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	/*** Tiamat Guard ***/
	private void IDTransformTiamatGuard67An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformTiamatGuardFi67An();
				break;
			case 2:
				IDTransformTiamatGuardAs67An();
				break;
			case 3:
				IDTransformTiamatGuardWi67An();
				break;
			case 4:
				IDTransformTiamatGuardPr67An();
				break;
		}
	}

	private void IDTransformTiamatGuardFi67An() {
		spawn(244507, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244507, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244507, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244507, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244507, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244507, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244507, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244507, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244507, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244507, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244507, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244507, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244507, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244507, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244507, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244507, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244507, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244507, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244507, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244507, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244507, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244507, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244507, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244507, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244507, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244507, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244507, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244507, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244507, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244507, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244507, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244507, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244507, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244507, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244507, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244507, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244507, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244507, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244507, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244507, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244507, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformTiamatGuardAs67An() {
		spawn(244508, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244508, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244508, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244508, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244508, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244508, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244508, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244508, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244508, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244508, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244508, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244508, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244508, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244508, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244508, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244508, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244508, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244508, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244508, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244508, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244508, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244508, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244508, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244508, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244508, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244508, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244508, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244508, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244508, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244508, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244508, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244508, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244508, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244508, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244508, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244508, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244508, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244508, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244508, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244508, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244508, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformTiamatGuardWi67An() {
		spawn(244509, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244509, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244509, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244509, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244509, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244509, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244509, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244509, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244509, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244509, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244509, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244509, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244509, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244509, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244509, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244509, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244509, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244509, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244509, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244509, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244509, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244509, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244509, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244509, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244509, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244509, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244509, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244509, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244509, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244509, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244509, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244509, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244509, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244509, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244509, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244509, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244509, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244509, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244509, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244509, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244509, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformTiamatGuardPr67An() {
		spawn(244510, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244510, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244510, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244510, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244510, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244510, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244510, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244510, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244510, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244510, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244510, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244510, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244510, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244510, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244510, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244510, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244510, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244510, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244510, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244510, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244510, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244510, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244510, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244510, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244510, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244510, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244510, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244510, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244510, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244510, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244510, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244510, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244510, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244510, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244510, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244510, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244510, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244510, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244510, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244510, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244510, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	/*** Deva Guard ***/
	private void IDTransformDevaGuardFi67An() {
		spawn(244526, 543.9115f, 491.4599f, 322.04422f, (byte) 91);
		spawn(244526, 547.9682f, 490.34494f, 321.50793f, (byte) 90);
		spawn(244526, 534.53534f, 485.35455f, 322.0f, (byte) 89);
		spawn(244526, 541.57935f, 487.14307f, 322.0f, (byte) 89);
		spawn(244526, 516.4117f, 585.5329f, 321.97427f, (byte) 90);
		spawn(244526, 499.82422f, 585.81537f, 322.02252f, (byte) 92);
		spawn(244526, 501.61078f, 555.0141f, 321.84106f, (byte) 12);
	}

	private void IDTransformDevaGuardAs67An() {
		spawn(244527, 855.63855f, 484.74774f, 349.08722f, (byte) 45);
		spawn(244527, 852.21844f, 482.80756f, 349.41986f, (byte) 44);
		spawn(244527, 864.0118f, 492.74384f, 349.56552f, (byte) 46);
		spawn(244527, 508.17993f, 587.8108f, 322.56973f, (byte) 90);
		spawn(244527, 526.8684f, 493.7475f, 322.0f, (byte) 91);
		spawn(244527, 537.37225f, 508.5786f, 322.0f, (byte) 90);
		spawn(244527, 512.8187f, 590.4575f, 322.56216f, (byte) 90);
		spawn(244527, 504.50745f, 590.3867f, 322.56216f, (byte) 90);
		spawn(244527, 482.3006f, 496.94232f, 342.22174f, (byte) 27);
		spawn(244527, 594.4832f, 673.60126f, 350.85538f, (byte) 54);
		spawn(244527, 634.298f, 511.2995f, 339.61545f, (byte) 61);
		spawn(244527, 530.982f, 493.62543f, 322.0f, (byte) 90);
		spawn(244527, 641.9954f, 514.2143f, 339.61542f, (byte) 61);
		spawn(244527, 505.45636f, 584.90186f, 322.0f, (byte) 90);
		spawn(244527, 510.66394f, 584.80176f, 322.0f, (byte) 90);
		spawn(244527, 859.2291f, 490.53555f, 348.98047f, (byte) 45);
		spawn(244527, 604.1567f, 635.69995f, 352.53815f, (byte) 30);
		spawn(244527, 467.0f, 476.0f, 345.70047f, (byte) 32);
		spawn(244527, 536.2914f, 488.65588f, 322.0f, (byte) 90);
		spawn(244527, 639.6124f, 525.6597f, 339.61542f, (byte) 62);
		spawn(244527, 885.47394f, 464.9668f, 351.0f, (byte) 66);
		spawn(244527, 642.88293f, 520.17224f, 339.61542f, (byte) 60);
		spawn(244527, 546.91925f, 510.66574f, 321.94254f, (byte) 91);
		spawn(244527, 527.7633f, 502.87167f, 321.6398f, (byte) 91);
		spawn(244527, 885.5619f, 456.29126f, 351.02737f, (byte) 55);
	}

	private void IDTransformDevaGuardWi67An() {
		spawn(244528, 513.0236f, 593.86285f, 322.56216f, (byte) 90);
		spawn(244528, 501.3256f, 593.69885f, 322.56216f, (byte) 90);
		spawn(244528, 633.75323f, 516.50934f, 339.61545f, (byte) 61);
		spawn(244528, 544.93945f, 499.1886f, 322.0f, (byte) 91);
		spawn(244528, 530.8947f, 502.38684f, 321.86185f, (byte) 91);
		spawn(244528, 634.5065f, 522.0903f, 339.61545f, (byte) 53);
		spawn(244528, 885.382f, 460.77805f, 351.0f, (byte) 60);
		spawn(244528, 612.8048f, 643.80194f, 352.46414f, (byte) 30);
		spawn(244528, 538.00024f, 495.57394f, 322.0f, (byte) 90);
		spawn(244528, 526.9178f, 486.92227f, 321.87527f, (byte) 85);
	}

	private void IDTransformDevaGuardRa67An() {
		spawn(244529, 529.87f, 545.5564f, 321.85876f, (byte) 95);
		spawn(244529, 526.1612f, 545.1311f, 321.58078f, (byte) 88);
		spawn(244529, 546.40234f, 523.6142f, 321.97485f, (byte) 87);
		spawn(244529, 549.9647f, 524.9884f, 321.9978f, (byte) 95);
		spawn(244529, 520.9293f, 586.19006f, 321.94193f, (byte) 95);
		spawn(244529, 495.259f, 586.9457f, 322.0659f, (byte) 81);
		spawn(244529, 505.0709f, 552.7453f, 322.0f, (byte) 28);
	}

	/*** Eresh Stumble ***/
	private void IDTransformEreshStumble67An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformEreshStumbleFi67An();
				break;
			case 2:
				IDTransformEreshStumbleAs67An();
				break;
			case 3:
				IDTransformEreshStumbleWi67An();
				break;
			case 4:
				IDTransformEreshStumblePr67An();
				break;
		}
	}

	private void IDTransformEreshStumbleFi67An() {
		spawn(244895, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(244895, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(244895, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformEreshStumbleAs67An() {
		spawn(244896, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(244896, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(244896, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformEreshStumbleWi67An() {
		spawn(244897, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(244897, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(244897, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformEreshStumblePr67An() {
		spawn(244898, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(244898, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(244898, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	/*** Vritra Stumble ***/
	private void IDTransformVritraStumble67An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformVritraStumbleFi67An();
				break;
			case 2:
				IDTransformVritraStumbleAs67An();
				break;
			case 3:
				IDTransformVritraStumbleWi67An();
				break;
			case 4:
				IDTransformVritraStumblePr67An();
				break;
		}
	}

	private void IDTransformVritraStumbleFi67An() {
		spawn(244899, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(244899, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(244899, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformVritraStumbleAs67An() {
		spawn(244900, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(244900, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(244900, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformVritraStumbleWi67An() {
		spawn(244901, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(244901, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(244901, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformVritraStumblePr67An() {
		spawn(244902, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(244902, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(244902, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	/*** Tiamat Stumble ***/
	private void IDTransformTiamatStumble67An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformTiamatStumbleFi67An();
				break;
			case 2:
				IDTransformTiamatStumbleAs67An();
				break;
			case 3:
				IDTransformTiamatStumbleWi67An();
				break;
			case 4:
				IDTransformTiamatStumblePr67An();
				break;
		}
	}

	private void IDTransformTiamatStumbleFi67An() {
		spawn(244903, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(244903, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(244903, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformTiamatStumbleAs67An() {
		spawn(244904, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(244904, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(244904, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformTiamatStumbleWi67An() {
		spawn(244905, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(244905, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(244905, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformTiamatStumblePr67An() {
		spawn(244906, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(244906, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(244906, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	/*** Deva Stumble ***/
	private void IDTransformDevaStumble67An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformDevaStumbleFi67An();
				break;
			case 2:
				IDTransformDevaStumbleAs67An();
				break;
			case 3:
				IDTransformDevaStumbleWi67An();
				break;
			case 4:
				IDTransformDevaStumblePr67An();
				break;
		}
	}

	private void IDTransformDevaStumbleFi67An() {
		spawn(244907, 677.0f, 516.0f, 338.24844f, (byte) 43);
		spawn(244907, 505.28098f, 648.23944f, 317.08282f, (byte) 113);
		spawn(244907, 507.24606f, 628.40027f, 318.4649f, (byte) 61);
		spawn(244907, 504.39532f, 615.5569f, 320.3689f, (byte) 61);
		spawn(244907, 504.10117f, 601.42255f, 322.4643f, (byte) 60);
		spawn(244907, 513.7057f, 643.4998f, 317.10632f, (byte) 11);
		spawn(244907, 511.02466f, 614.0008f, 320.5996f, (byte) 0);
		spawn(244907, 503.81223f, 640.00806f, 317.11395f, (byte) 40);
		spawn(244907, 531.7686f, 642.5042f, 317.08282f, (byte) 112);
		spawn(244907, 522.0014f, 648.79095f, 317.08282f, (byte) 55);
	}

	private void IDTransformDevaStumbleAs67An() {
		spawn(244908, 677.0f, 516.0f, 338.24844f, (byte) 43);
		spawn(244908, 505.28098f, 648.23944f, 317.08282f, (byte) 113);
		spawn(244908, 507.24606f, 628.40027f, 318.4649f, (byte) 61);
		spawn(244908, 504.39532f, 615.5569f, 320.3689f, (byte) 61);
		spawn(244908, 504.10117f, 601.42255f, 322.4643f, (byte) 60);
		spawn(244908, 513.7057f, 643.4998f, 317.10632f, (byte) 11);
		spawn(244908, 511.02466f, 614.0008f, 320.5996f, (byte) 0);
		spawn(244908, 503.81223f, 640.00806f, 317.11395f, (byte) 40);
		spawn(244908, 531.7686f, 642.5042f, 317.08282f, (byte) 112);
		spawn(244908, 522.0014f, 648.79095f, 317.08282f, (byte) 55);
	}

	private void IDTransformDevaStumbleWi67An() {
		spawn(244909, 677.0f, 516.0f, 338.24844f, (byte) 43);
		spawn(244909, 505.28098f, 648.23944f, 317.08282f, (byte) 113);
		spawn(244909, 507.24606f, 628.40027f, 318.4649f, (byte) 61);
		spawn(244909, 504.39532f, 615.5569f, 320.3689f, (byte) 61);
		spawn(244909, 504.10117f, 601.42255f, 322.4643f, (byte) 60);
		spawn(244909, 513.7057f, 643.4998f, 317.10632f, (byte) 11);
		spawn(244909, 511.02466f, 614.0008f, 320.5996f, (byte) 0);
		spawn(244909, 503.81223f, 640.00806f, 317.11395f, (byte) 40);
		spawn(244909, 531.7686f, 642.5042f, 317.08282f, (byte) 112);
		spawn(244909, 522.0014f, 648.79095f, 317.08282f, (byte) 55);
	}

	private void IDTransformDevaStumblePr67An() {
		spawn(244910, 677.0f, 516.0f, 338.24844f, (byte) 43);
		spawn(244910, 505.28098f, 648.23944f, 317.08282f, (byte) 113);
		spawn(244910, 507.24606f, 628.40027f, 318.4649f, (byte) 61);
		spawn(244910, 504.39532f, 615.5569f, 320.3689f, (byte) 61);
		spawn(244910, 504.10117f, 601.42255f, 322.4643f, (byte) 60);
		spawn(244910, 513.7057f, 643.4998f, 317.10632f, (byte) 11);
		spawn(244910, 511.02466f, 614.0008f, 320.5996f, (byte) 0);
		spawn(244910, 503.81223f, 640.00806f, 317.11395f, (byte) 40);
		spawn(244910, 531.7686f, 642.5042f, 317.08282f, (byte) 112);
		spawn(244910, 522.0014f, 648.79095f, 317.08282f, (byte) 55);
	}

	/*** Drop Dragon ***/
	private void IDTransformDropDragon67Ae() {
		switch (Rnd.get(1, 3)) {
			case 1:
				spawn(244923, 622.6814f, 551.9144f, 346.06897f, (byte) 105);
				break;
			case 2:
				spawn(244924, 622.6814f, 551.9144f, 346.06897f, (byte) 105);
				break;
			case 3:
				spawn(244925, 622.6814f, 551.9144f, 346.06897f, (byte) 105);
				break;
		}
		switch (Rnd.get(1, 3)) {
			case 1:
				spawn(244923, 607.8992f, 674.06934f, 352.29062f, (byte) 90);
				break;
			case 2:
				spawn(244924, 607.8992f, 674.06934f, 352.29062f, (byte) 90);
				break;
			case 3:
				spawn(244925, 607.8992f, 674.06934f, 352.29062f, (byte) 90);
				break;
		}
	}

	/*** Bonus Monster ***/
	private void IDTransformBonusMonsterTypeA67An() {
		spawn(246204, 761.54095f, 562.17f, 341.0512f, (byte) 90);
		spawn(246204, 476.94467f, 549.22363f, 345.6048f, (byte) 90);
		spawn(246204, 609.2811f, 707.6352f, 355.10846f, (byte) 93);
	}

	private void IDTransformBonusMonsterTypeB67An() {
		spawn(246205, 761.54095f, 562.17f, 341.0512f, (byte) 90);
		spawn(246205, 476.94467f, 549.22363f, 345.6048f, (byte) 90);
		spawn(246205, 609.2811f, 707.6352f, 355.10846f, (byte) 93);
	}

	private void IDTransformBonusMonsterTypeC67An() {
		spawn(246206, 761.54095f, 562.17f, 341.0512f, (byte) 90);
		spawn(246206, 476.94467f, 549.22363f, 345.6048f, (byte) 90);
		spawn(246206, 609.2811f, 707.6352f, 355.10846f, (byte) 93);
	}

	private void IDTransformBonusMonsterTypeD67An() {
		spawn(246207, 761.54095f, 562.17f, 341.0512f, (byte) 90);
		spawn(246207, 476.94467f, 549.22363f, 345.6048f, (byte) 90);
		spawn(246207, 609.2811f, 707.6352f, 355.10846f, (byte) 93);
	}

	/*** Eresh Warp ***/
	private void IDTransformEreshWarp67() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformEreshWarpFi67An();
				break;
			case 2:
				IDTransformEreshWarpAs67An();
				break;
			case 3:
				IDTransformEreshWarpWi67An();
				break;
			case 4:
				IDTransformEreshWarpPr67An();
				break;
		}
	}

	private void IDTransformEreshWarpFi67An() {
		sp(245589, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245589, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245589, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245589, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245589, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245589, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245589, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245589, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245589, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245589, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245589, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245589, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245589, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245589, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245589, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245589, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245589, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245589, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245589, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformEreshWarpAs67An() {
		sp(245590, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245590, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245590, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245590, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245590, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245590, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245590, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245590, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245590, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245590, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245590, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245590, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245590, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245590, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245590, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245590, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245590, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245590, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245590, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformEreshWarpWi67An() {
		sp(245591, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245591, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245591, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245591, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245591, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245591, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245591, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245591, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245591, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245591, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245591, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245591, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245591, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245591, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245591, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245591, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245591, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245591, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245591, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformEreshWarpPr67An() {
		sp(245592, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245592, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245592, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245592, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245592, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245592, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245592, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245592, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245592, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245592, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245592, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245592, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245592, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245592, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245592, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245592, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245592, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245592, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245592, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	/*** Vritra Warp ***/
	private void IDTransformVritraWarp67() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformVritraWarpFi67An();
				break;
			case 2:
				IDTransformVritraWarpAs67An();
				break;
			case 3:
				IDTransformVritraWarpWi67An();
				break;
			case 4:
				IDTransformVritraWarpPr67An();
				break;
		}
	}

	private void IDTransformVritraWarpFi67An() {
		sp(245593, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245593, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245593, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245593, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245593, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245593, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245593, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245593, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245593, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245593, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245593, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245593, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245593, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245593, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245593, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245593, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245593, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245593, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245593, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformVritraWarpAs67An() {
		sp(245594, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245594, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245594, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245594, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245594, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245594, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245594, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245594, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245594, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245594, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245594, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245594, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245594, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245594, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245594, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245594, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245594, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245594, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245594, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformVritraWarpWi67An() {
		sp(245595, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245595, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245595, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245595, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245595, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245595, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245595, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245595, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245595, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245595, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245595, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245595, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245595, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245595, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245595, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245595, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245595, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245595, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245595, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformVritraWarpPr67An() {
		sp(245596, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245596, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245596, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245596, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245596, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245596, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245596, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245596, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245596, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245596, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245596, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245596, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245596, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245596, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245596, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245596, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245596, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245596, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245596, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	/*** Tiamat Warp ***/
	private void IDTransformTiamatWarp67() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformTiamatWarpFi67An();
				break;
			case 2:
				IDTransformTiamatWarpAs67An();
				break;
			case 3:
				IDTransformTiamatWarpWi67An();
				break;
			case 4:
				IDTransformTiamatWarpPr67An();
				break;
		}
	}

	private void IDTransformTiamatWarpFi67An() {
		sp(245597, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245597, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245597, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245597, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245597, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245597, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245597, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245597, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245597, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245597, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245597, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245597, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245597, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245597, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245597, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245597, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245597, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245597, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245597, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformTiamatWarpAs67An() {
		sp(245598, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245598, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245598, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245598, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245598, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245598, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245598, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245598, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245598, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245598, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245598, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245598, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245598, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245598, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245598, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245598, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245598, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245598, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245598, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformTiamatWarpWi67An() {
		sp(245599, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245599, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245599, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245599, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245599, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245599, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245599, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245599, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245599, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245599, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245599, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245599, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245599, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245599, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245599, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245599, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245599, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245599, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245599, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformTiamatWarpPr67An() {
		sp(245600, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245600, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245600, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245600, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245600, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245600, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245600, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245600, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245600, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245600, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245600, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245600, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245600, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245600, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245600, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245600, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245600, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245600, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245600, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	/*** Ranger ***/
	private void IDTransformRa67An() {
		switch (Rnd.get(1, 3)) {
			case 1:
				IDTransformEreshRa67An();
				break;
			case 2:
				IDTransformVritraRa67An();
				break;
			case 3:
				IDTransformTiamatRa67An();
				break;
		}
	}

	/*** Eresh Ranger ***/
	private void IDTransformEreshRa67An() {
		spawn(245703, 543.0933f, 557.8653f, 322.0f, (byte) 70);
		spawn(245703, 550.4571f, 546.09937f, 322.0f, (byte) 61);
		spawn(245703, 550.2217f, 539.29755f, 322.0f, (byte) 59);
		spawn(245703, 543.0761f, 534.39087f, 322.0f, (byte) 43);
		spawn(245703, 534.18945f, 534.63385f, 321.875f, (byte) 21);
		spawn(245703, 522.56396f, 467.8957f, 322.0f, (byte) 37);
		spawn(245703, 527.70734f, 475.95486f, 322.0f, (byte) 7);
		spawn(245703, 509.6404f, 476.19836f, 322.0f, (byte) 118);
		spawn(245703, 501.61966f, 467.60513f, 322.0f, (byte) 5);
		spawn(245703, 503.2415f, 446.9365f, 321.8276f, (byte) 18);
		spawn(245703, 542.8608f, 472.11404f, 322.0f, (byte) 51);
		spawn(245703, 539.9602f, 467.72766f, 322.0f, (byte) 50);
		spawn(245703, 516.02167f, 453.723f, 321.875f, (byte) 60);
		spawn(245703, 466.30823f, 509.6083f, 342.1901f, (byte) 0);
		spawn(245703, 466.1356f, 516.46765f, 342.1901f, (byte) 0);
		spawn(245703, 468.7195f, 513.0958f, 342.1901f, (byte) 0);
		spawn(245703, 446.91077f, 509.62943f, 342.2713f, (byte) 1);
		spawn(245703, 446.86057f, 517.2843f, 342.27173f, (byte) 0);
		spawn(245703, 449.71277f, 513.23883f, 342.24753f, (byte) 0);
		spawn(245703, 427.54434f, 509.3018f, 342.43564f, (byte) 0);
		spawn(245703, 427.4629f, 517.19116f, 342.43634f, (byte) 0);
		spawn(245703, 430.6331f, 513.1085f, 342.40942f, (byte) 0);
		spawn(245703, 483.40283f, 475.9793f, 345.70047f, (byte) 81);
		spawn(245703, 469.33865f, 454.01733f, 345.70047f, (byte) 4);
		spawn(245703, 482.78824f, 526.4342f, 342.234f, (byte) 78);
		spawn(245703, 502.18372f, 508.00098f, 339.63123f, (byte) 39);
		spawn(245703, 497.84283f, 521.77655f, 339.63815f, (byte) 77);
		spawn(245703, 508.98663f, 527.061f, 339.63126f, (byte) 76);
		spawn(245703, 502.5523f, 649.1053f, 317.08282f, (byte) 109);
		spawn(245703, 522.4196f, 650.3398f, 317.08282f, (byte) 99);
		spawn(245703, 540.84265f, 652.0684f, 316.16275f, (byte) 100);
		spawn(245703, 540.5331f, 637.4856f, 316.17746f, (byte) 17);
		spawn(245703, 514.6148f, 641.9673f, 317.10098f, (byte) 33);
		spawn(245703, 539.03076f, 536.819f, 322.0f, (byte) 30);
	}

	/*** Vritra Ranger ***/
	private void IDTransformVritraRa67An() {
		spawn(245704, 543.0933f, 557.8653f, 322.0f, (byte) 70);
		spawn(245704, 550.4571f, 546.09937f, 322.0f, (byte) 61);
		spawn(245704, 550.2217f, 539.29755f, 322.0f, (byte) 59);
		spawn(245704, 543.0761f, 534.39087f, 322.0f, (byte) 43);
		spawn(245704, 534.18945f, 534.63385f, 321.875f, (byte) 21);
		spawn(245704, 522.56396f, 467.8957f, 322.0f, (byte) 37);
		spawn(245704, 527.70734f, 475.95486f, 322.0f, (byte) 7);
		spawn(245704, 509.6404f, 476.19836f, 322.0f, (byte) 118);
		spawn(245704, 501.61966f, 467.60513f, 322.0f, (byte) 5);
		spawn(245704, 503.2415f, 446.9365f, 321.8276f, (byte) 18);
		spawn(245704, 542.8608f, 472.11404f, 322.0f, (byte) 51);
		spawn(245704, 539.9602f, 467.72766f, 322.0f, (byte) 50);
		spawn(245704, 516.02167f, 453.723f, 321.875f, (byte) 60);
		spawn(245704, 466.30823f, 509.6083f, 342.1901f, (byte) 0);
		spawn(245704, 466.1356f, 516.46765f, 342.1901f, (byte) 0);
		spawn(245704, 468.7195f, 513.0958f, 342.1901f, (byte) 0);
		spawn(245704, 446.91077f, 509.62943f, 342.2713f, (byte) 1);
		spawn(245704, 446.86057f, 517.2843f, 342.27173f, (byte) 0);
		spawn(245704, 449.71277f, 513.23883f, 342.24753f, (byte) 0);
		spawn(245704, 427.54434f, 509.3018f, 342.43564f, (byte) 0);
		spawn(245704, 427.4629f, 517.19116f, 342.43634f, (byte) 0);
		spawn(245704, 430.6331f, 513.1085f, 342.40942f, (byte) 0);
		spawn(245704, 483.40283f, 475.9793f, 345.70047f, (byte) 81);
		spawn(245704, 469.33865f, 454.01733f, 345.70047f, (byte) 4);
		spawn(245704, 482.78824f, 526.4342f, 342.234f, (byte) 78);
		spawn(245704, 502.18372f, 508.00098f, 339.63123f, (byte) 39);
		spawn(245704, 497.84283f, 521.77655f, 339.63815f, (byte) 77);
		spawn(245704, 508.98663f, 527.061f, 339.63126f, (byte) 76);
		spawn(245704, 502.5523f, 649.1053f, 317.08282f, (byte) 109);
		spawn(245704, 522.4196f, 650.3398f, 317.08282f, (byte) 99);
		spawn(245704, 540.84265f, 652.0684f, 316.16275f, (byte) 100);
		spawn(245704, 540.5331f, 637.4856f, 316.17746f, (byte) 17);
		spawn(245704, 514.6148f, 641.9673f, 317.10098f, (byte) 33);
		spawn(245704, 539.03076f, 536.819f, 322.0f, (byte) 30);
	}

	/*** Tiamat Ranger ***/
	private void IDTransformTiamatRa67An() {
		spawn(245705, 543.0933f, 557.8653f, 322.0f, (byte) 70);
		spawn(245705, 550.4571f, 546.09937f, 322.0f, (byte) 61);
		spawn(245705, 550.2217f, 539.29755f, 322.0f, (byte) 59);
		spawn(245705, 543.0761f, 534.39087f, 322.0f, (byte) 43);
		spawn(245705, 534.18945f, 534.63385f, 321.875f, (byte) 21);
		spawn(245705, 522.56396f, 467.8957f, 322.0f, (byte) 37);
		spawn(245705, 527.70734f, 475.95486f, 322.0f, (byte) 7);
		spawn(245705, 509.6404f, 476.19836f, 322.0f, (byte) 118);
		spawn(245705, 501.61966f, 467.60513f, 322.0f, (byte) 5);
		spawn(245705, 503.2415f, 446.9365f, 321.8276f, (byte) 18);
		spawn(245705, 542.8608f, 472.11404f, 322.0f, (byte) 51);
		spawn(245705, 539.9602f, 467.72766f, 322.0f, (byte) 50);
		spawn(245705, 516.02167f, 453.723f, 321.875f, (byte) 60);
		spawn(245705, 466.30823f, 509.6083f, 342.1901f, (byte) 0);
		spawn(245705, 466.1356f, 516.46765f, 342.1901f, (byte) 0);
		spawn(245705, 468.7195f, 513.0958f, 342.1901f, (byte) 0);
		spawn(245705, 446.91077f, 509.62943f, 342.2713f, (byte) 1);
		spawn(245705, 446.86057f, 517.2843f, 342.27173f, (byte) 0);
		spawn(245705, 449.71277f, 513.23883f, 342.24753f, (byte) 0);
		spawn(245705, 427.54434f, 509.3018f, 342.43564f, (byte) 0);
		spawn(245705, 427.4629f, 517.19116f, 342.43634f, (byte) 0);
		spawn(245705, 430.6331f, 513.1085f, 342.40942f, (byte) 0);
		spawn(245705, 483.40283f, 475.9793f, 345.70047f, (byte) 81);
		spawn(245705, 469.33865f, 454.01733f, 345.70047f, (byte) 4);
		spawn(245705, 482.78824f, 526.4342f, 342.234f, (byte) 78);
		spawn(245705, 502.18372f, 508.00098f, 339.63123f, (byte) 39);
		spawn(245705, 497.84283f, 521.77655f, 339.63815f, (byte) 77);
		spawn(245705, 508.98663f, 527.061f, 339.63126f, (byte) 76);
		spawn(245705, 502.5523f, 649.1053f, 317.08282f, (byte) 109);
		spawn(245705, 522.4196f, 650.3398f, 317.08282f, (byte) 99);
		spawn(245705, 540.84265f, 652.0684f, 316.16275f, (byte) 100);
		spawn(245705, 540.5331f, 637.4856f, 316.17746f, (byte) 17);
		spawn(245705, 514.6148f, 641.9673f, 317.10098f, (byte) 33);
		spawn(245705, 539.03076f, 536.819f, 322.0f, (byte) 30);
	}

	/*** Player Lvl 68 ***/
	/*** Eresh High ***/
	private void IDTransformEreshHigh68An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformEreshHighFi68An();
				break;
			case 2:
				IDTransformEreshHighAs68An();
				break;
			case 3:
				IDTransformEreshHighWi68An();
				break;
			case 4:
				IDTransformEreshHighPr68An();
				break;
		}
	}

	private void IDTransformEreshHighFi68An() {
		spawn(244552, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244552, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244552, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244552, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244552, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244552, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244552, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244552, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244552, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244552, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244552, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244552, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244552, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244552, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244552, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244552, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244552, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformEreshHighAs68An() {
		spawn(244553, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244553, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244553, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244553, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244553, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244553, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244553, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244553, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244553, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244553, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244553, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244553, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244553, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244553, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244553, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244553, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244553, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformEreshHighWi68An() {
		spawn(244554, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244554, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244554, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244554, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244554, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244554, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244554, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244554, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244554, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244554, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244554, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244554, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244554, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244554, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244554, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244554, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244554, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformEreshHighPr68An() {
		spawn(244555, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244555, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244555, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244555, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244555, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244555, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244555, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244555, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244555, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244555, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244555, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244555, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244555, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244555, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244555, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244555, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244555, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	/*** Vritra High ***/
	private void IDTransformVritraHigh68An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformVritraHighFi68An();
				break;
			case 2:
				IDTransformVritraHighAs68An();
				break;
			case 3:
				IDTransformVritraHighWi68An();
				break;
			case 4:
				IDTransformVritraHighPr68An();
				break;
		}
	}

	private void IDTransformVritraHighFi68An() {
		spawn(244556, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244556, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244556, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244556, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244556, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244556, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244556, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244556, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244556, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244556, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244556, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244556, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244556, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244556, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244556, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244556, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244556, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformVritraHighAs68An() {
		spawn(244557, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244557, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244557, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244557, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244557, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244557, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244557, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244557, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244557, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244557, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244557, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244557, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244557, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244557, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244557, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244557, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244557, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformVritraHighWi68An() {
		spawn(244558, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244558, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244558, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244558, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244558, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244558, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244558, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244558, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244558, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244558, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244558, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244558, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244558, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244558, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244558, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244558, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244558, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformVritraHighPr68An() {
		spawn(244559, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244559, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244559, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244559, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244559, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244559, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244559, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244559, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244559, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244559, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244559, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244559, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244559, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244559, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244559, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244559, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244559, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	/*** Tiamat High ***/
	private void IDTransformTiamatHigh68An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformTiamatHighFi68An();
				break;
			case 2:
				IDTransformTiamatHighAs68An();
				break;
			case 3:
				IDTransformTiamatHighWi68An();
				break;
			case 4:
				IDTransformTiamatHighPr68An();
				break;
		}
	}

	private void IDTransformTiamatHighFi68An() {
		spawn(244560, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244560, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244560, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244560, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244560, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244560, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244560, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244560, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244560, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244560, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244560, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244560, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244560, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244560, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244560, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244560, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244560, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformTiamatHighAs68An() {
		spawn(244561, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244561, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244561, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244561, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244561, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244561, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244561, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244561, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244561, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244561, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244561, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244561, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244561, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244561, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244561, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244561, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244561, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformTiamatHighWi68An() {
		spawn(244562, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244562, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244562, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244562, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244562, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244562, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244562, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244562, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244562, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244562, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244562, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244562, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244562, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244562, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244562, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244562, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244562, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformTiamatHighPr68An() {
		spawn(244563, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244563, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244563, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244563, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244563, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244563, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244563, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244563, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244563, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244563, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244563, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244563, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244563, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244563, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244563, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244563, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244563, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	/*** Eresh Guard ***/
	private void IDTransformEreshGuard68An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformEreshGuardFi68An();
				break;
			case 2:
				IDTransformEreshGuardAs68An();
				break;
			case 3:
				IDTransformEreshGuardWi68An();
				break;
			case 4:
				IDTransformEreshGuardPr68An();
				break;
		}
	}

	private void IDTransformEreshGuardFi68An() {
		spawn(244540, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244540, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244540, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244540, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244540, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244540, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244540, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244540, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244540, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244540, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244540, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244540, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244540, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244540, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244540, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244540, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244540, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244540, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244540, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244540, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244540, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244540, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244540, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244540, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244540, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244540, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244540, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244540, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244540, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244540, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244540, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244540, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244540, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244540, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244540, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244540, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244540, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244540, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244540, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244540, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244540, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformEreshGuardAs68An() {
		spawn(244541, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244541, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244541, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244541, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244541, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244541, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244541, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244541, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244541, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244541, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244541, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244541, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244541, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244541, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244541, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244541, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244541, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244541, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244541, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244541, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244541, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244541, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244541, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244541, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244541, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244541, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244541, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244541, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244541, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244541, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244541, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244541, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244541, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244541, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244541, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244541, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244541, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244541, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244541, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244541, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244541, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformEreshGuardWi68An() {
		spawn(244542, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244542, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244542, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244542, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244542, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244542, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244542, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244542, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244542, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244542, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244542, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244542, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244542, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244542, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244542, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244542, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244542, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244542, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244542, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244542, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244542, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244542, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244542, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244542, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244542, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244542, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244542, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244542, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244542, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244542, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244542, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244542, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244542, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244542, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244542, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244542, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244542, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244542, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244542, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244542, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244542, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformEreshGuardPr68An() {
		spawn(244543, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244543, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244543, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244543, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244543, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244543, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244543, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244543, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244543, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244543, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244543, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244543, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244543, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244543, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244543, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244543, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244543, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244543, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244543, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244543, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244543, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244543, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244543, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244543, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244543, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244543, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244543, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244543, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244543, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244543, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244543, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244543, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244543, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244543, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244543, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244543, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244543, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244543, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244543, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244543, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244543, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	/*** Vritra Guard ***/
	private void IDTransformVritraGuard68An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformVritraGuardFi68An();
				break;
			case 2:
				IDTransformVritraGuardAs68An();
				break;
			case 3:
				IDTransformVritraGuardWi68An();
				break;
			case 4:
				IDTransformVritraGuardPr68An();
				break;
		}
	}

	private void IDTransformVritraGuardFi68An() {
		spawn(244544, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244544, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244544, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244544, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244544, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244544, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244544, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244544, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244544, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244544, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244544, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244544, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244544, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244544, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244544, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244544, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244544, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244544, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244544, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244544, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244544, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244544, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244544, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244544, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244544, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244544, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244544, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244544, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244544, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244544, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244544, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244544, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244544, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244544, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244544, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244544, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244544, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244544, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244544, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244544, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244544, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformVritraGuardAs68An() {
		spawn(244545, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244545, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244545, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244545, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244545, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244545, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244545, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244545, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244545, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244545, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244545, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244545, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244545, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244545, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244545, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244545, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244545, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244545, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244545, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244545, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244545, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244545, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244545, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244545, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244545, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244545, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244545, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244545, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244545, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244545, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244545, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244545, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244545, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244545, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244545, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244545, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244545, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244545, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244545, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244545, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244545, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformVritraGuardWi68An() {
		spawn(244546, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244546, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244546, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244546, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244546, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244546, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244546, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244546, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244546, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244546, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244546, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244546, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244546, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244546, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244546, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244546, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244546, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244546, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244546, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244546, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244546, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244546, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244546, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244546, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244546, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244546, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244546, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244546, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244546, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244546, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244546, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244546, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244546, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244546, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244546, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244546, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244546, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244546, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244546, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244546, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244546, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformVritraGuardPr68An() {
		spawn(244547, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244547, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244547, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244547, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244547, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244547, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244547, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244547, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244547, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244547, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244547, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244547, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244547, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244547, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244547, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244547, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244547, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244547, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244547, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244547, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244547, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244547, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244547, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244547, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244547, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244547, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244547, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244547, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244547, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244547, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244547, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244547, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244547, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244547, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244547, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244547, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244547, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244547, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244547, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244547, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244547, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	/*** Tiamat Guard ***/
	private void IDTransformTiamatGuard68An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformTiamatGuardFi68An();
				break;
			case 2:
				IDTransformTiamatGuardAs68An();
				break;
			case 3:
				IDTransformTiamatGuardWi68An();
				break;
			case 4:
				IDTransformTiamatGuardPr68An();
				break;
		}
	}

	private void IDTransformTiamatGuardFi68An() {
		spawn(244548, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244548, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244548, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244548, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244548, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244548, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244548, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244548, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244548, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244548, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244548, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244548, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244548, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244548, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244548, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244548, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244548, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244548, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244548, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244548, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244548, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244548, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244548, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244548, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244548, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244548, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244548, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244548, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244548, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244548, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244548, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244548, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244548, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244548, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244548, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244548, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244548, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244548, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244548, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244548, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244548, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformTiamatGuardAs68An() {
		spawn(244549, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244549, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244549, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244549, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244549, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244549, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244549, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244549, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244549, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244549, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244549, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244549, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244549, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244549, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244549, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244549, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244549, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244549, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244549, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244549, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244549, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244549, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244549, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244549, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244549, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244549, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244549, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244549, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244549, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244549, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244549, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244549, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244549, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244549, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244549, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244549, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244549, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244549, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244549, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244549, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244549, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformTiamatGuardWi68An() {
		spawn(244550, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244550, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244550, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244550, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244550, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244550, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244550, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244550, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244550, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244550, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244550, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244550, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244550, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244550, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244550, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244550, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244550, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244550, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244550, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244550, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244550, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244550, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244550, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244550, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244550, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244550, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244550, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244550, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244550, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244550, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244550, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244550, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244550, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244550, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244550, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244550, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244550, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244550, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244550, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244550, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244550, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformTiamatGuardPr68An() {
		spawn(244551, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244551, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244551, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244551, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244551, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244551, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244551, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244551, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244551, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244551, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244551, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244551, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244551, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244551, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244551, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244551, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244551, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244551, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244551, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244551, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244551, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244551, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244551, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244551, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244551, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244551, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244551, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244551, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244551, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244551, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244551, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244551, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244551, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244551, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244551, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244551, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244551, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244551, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244551, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244551, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244551, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	/*** Deva Guard ***/
	private void IDTransformDevaGuardFi68An() {
		spawn(244567, 543.9115f, 491.4599f, 322.04422f, (byte) 91);
		spawn(244567, 547.9682f, 490.34494f, 321.50793f, (byte) 90);
		spawn(244567, 534.53534f, 485.35455f, 322.0f, (byte) 89);
		spawn(244567, 541.57935f, 487.14307f, 322.0f, (byte) 89);
		spawn(244567, 516.4117f, 585.5329f, 321.97427f, (byte) 90);
		spawn(244567, 499.82422f, 585.81537f, 322.02252f, (byte) 92);
		spawn(244567, 501.61078f, 555.0141f, 321.84106f, (byte) 12);
	}

	private void IDTransformDevaGuardAs68An() {
		spawn(244568, 855.63855f, 484.74774f, 349.08722f, (byte) 45);
		spawn(244568, 852.21844f, 482.80756f, 349.41986f, (byte) 44);
		spawn(244568, 864.0118f, 492.74384f, 349.56552f, (byte) 46);
		spawn(244568, 508.17993f, 587.8108f, 322.56973f, (byte) 90);
		spawn(244568, 526.8684f, 493.7475f, 322.0f, (byte) 91);
		spawn(244568, 537.37225f, 508.5786f, 322.0f, (byte) 90);
		spawn(244568, 512.8187f, 590.4575f, 322.56216f, (byte) 90);
		spawn(244568, 504.50745f, 590.3867f, 322.56216f, (byte) 90);
		spawn(244568, 482.3006f, 496.94232f, 342.22174f, (byte) 27);
		spawn(244568, 594.4832f, 673.60126f, 350.85538f, (byte) 54);
		spawn(244568, 634.298f, 511.2995f, 339.61545f, (byte) 61);
		spawn(244568, 530.982f, 493.62543f, 322.0f, (byte) 90);
		spawn(244568, 641.9954f, 514.2143f, 339.61542f, (byte) 61);
		spawn(244568, 505.45636f, 584.90186f, 322.0f, (byte) 90);
		spawn(244568, 510.66394f, 584.80176f, 322.0f, (byte) 90);
		spawn(244568, 859.2291f, 490.53555f, 348.98047f, (byte) 45);
		spawn(244568, 604.1567f, 635.69995f, 352.53815f, (byte) 30);
		spawn(244568, 467.0f, 476.0f, 345.70047f, (byte) 32);
		spawn(244568, 536.2914f, 488.65588f, 322.0f, (byte) 90);
		spawn(244568, 639.6124f, 525.6597f, 339.61542f, (byte) 62);
		spawn(244568, 885.47394f, 464.9668f, 351.0f, (byte) 66);
		spawn(244568, 642.88293f, 520.17224f, 339.61542f, (byte) 60);
		spawn(244568, 546.91925f, 510.66574f, 321.94254f, (byte) 91);
		spawn(244568, 527.7633f, 502.87167f, 321.6398f, (byte) 91);
		spawn(244568, 885.5619f, 456.29126f, 351.02737f, (byte) 55);
	}

	private void IDTransformDevaGuardWi68An() {
		spawn(244569, 513.0236f, 593.86285f, 322.56216f, (byte) 90);
		spawn(244569, 501.3256f, 593.69885f, 322.56216f, (byte) 90);
		spawn(244569, 633.75323f, 516.50934f, 339.61545f, (byte) 61);
		spawn(244569, 544.93945f, 499.1886f, 322.0f, (byte) 91);
		spawn(244569, 530.8947f, 502.38684f, 321.86185f, (byte) 91);
		spawn(244569, 634.5065f, 522.0903f, 339.61545f, (byte) 53);
		spawn(244569, 885.382f, 460.77805f, 351.0f, (byte) 60);
		spawn(244569, 612.8048f, 643.80194f, 352.46414f, (byte) 30);
		spawn(244569, 538.00024f, 495.57394f, 322.0f, (byte) 90);
		spawn(244569, 526.9178f, 486.92227f, 321.87527f, (byte) 85);
	}

	private void IDTransformDevaGuardRa68An() {
		spawn(244570, 529.87f, 545.5564f, 321.85876f, (byte) 95);
		spawn(244570, 526.1612f, 545.1311f, 321.58078f, (byte) 88);
		spawn(244570, 546.40234f, 523.6142f, 321.97485f, (byte) 87);
		spawn(244570, 549.9647f, 524.9884f, 321.9978f, (byte) 95);
		spawn(244570, 520.9293f, 586.19006f, 321.94193f, (byte) 95);
		spawn(244570, 495.259f, 586.9457f, 322.0659f, (byte) 81);
		spawn(244570, 505.0709f, 552.7453f, 322.0f, (byte) 28);
	}

	/*** Eresh Stumble ***/
	private void IDTransformEreshStumble68An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformEreshStumbleFi68An();
				break;
			case 2:
				IDTransformEreshStumbleAs68An();
				break;
			case 3:
				IDTransformEreshStumbleWi68An();
				break;
			case 4:
				IDTransformEreshStumblePr68An();
				break;
		}
	}

	private void IDTransformEreshStumbleFi68An() {
		spawn(244926, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(244926, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(244926, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformEreshStumbleAs68An() {
		spawn(244927, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(244927, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(244927, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformEreshStumbleWi68An() {
		spawn(244928, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(244928, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(244928, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformEreshStumblePr68An() {
		spawn(244929, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(244929, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(244929, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	/*** Vritra Stumble ***/
	private void IDTransformVritraStumble68An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformVritraStumbleFi68An();
				break;
			case 2:
				IDTransformVritraStumbleAs68An();
				break;
			case 3:
				IDTransformVritraStumbleWi68An();
				break;
			case 4:
				IDTransformVritraStumblePr68An();
				break;
		}
	}

	private void IDTransformVritraStumbleFi68An() {
		spawn(244930, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(244930, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(244930, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformVritraStumbleAs68An() {
		spawn(244931, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(244931, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(244931, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformVritraStumbleWi68An() {
		spawn(244932, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(244932, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(244932, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformVritraStumblePr68An() {
		spawn(244933, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(244933, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(244933, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	/*** Tiamat Stumble ***/
	private void IDTransformTiamatStumble68An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformTiamatStumbleFi68An();
				break;
			case 2:
				IDTransformTiamatStumbleAs68An();
				break;
			case 3:
				IDTransformTiamatStumbleWi68An();
				break;
			case 4:
				IDTransformTiamatStumblePr68An();
				break;
		}
	}

	private void IDTransformTiamatStumbleFi68An() {
		spawn(244934, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(244934, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(244934, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformTiamatStumbleAs68An() {
		spawn(244935, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(244935, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(244935, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformTiamatStumbleWi68An() {
		spawn(244936, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(244936, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(244936, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformTiamatStumblePr68An() {
		spawn(244937, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(244937, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(244937, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	/*** Deva Stumble ***/
	private void IDTransformDevaStumble68An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformDevaStumbleFi68An();
				break;
			case 2:
				IDTransformDevaStumbleAs68An();
				break;
			case 3:
				IDTransformDevaStumbleWi68An();
				break;
			case 4:
				IDTransformDevaStumblePr68An();
				break;
		}
	}

	private void IDTransformDevaStumbleFi68An() {
		spawn(244938, 677.0f, 516.0f, 338.24844f, (byte) 43);
		spawn(244938, 505.28098f, 648.23944f, 317.08282f, (byte) 113);
		spawn(244938, 507.24606f, 628.40027f, 318.4649f, (byte) 61);
		spawn(244938, 504.39532f, 615.5569f, 320.3689f, (byte) 61);
		spawn(244938, 504.10117f, 601.42255f, 322.4643f, (byte) 60);
		spawn(244938, 513.7057f, 643.4998f, 317.10632f, (byte) 11);
		spawn(244938, 511.02466f, 614.0008f, 320.5996f, (byte) 0);
		spawn(244938, 503.81223f, 640.00806f, 317.11395f, (byte) 40);
		spawn(244938, 531.7686f, 642.5042f, 317.08282f, (byte) 112);
		spawn(244938, 522.0014f, 648.79095f, 317.08282f, (byte) 55);
	}

	private void IDTransformDevaStumbleAs68An() {
		spawn(244939, 677.0f, 516.0f, 338.24844f, (byte) 43);
		spawn(244939, 505.28098f, 648.23944f, 317.08282f, (byte) 113);
		spawn(244939, 507.24606f, 628.40027f, 318.4649f, (byte) 61);
		spawn(244939, 504.39532f, 615.5569f, 320.3689f, (byte) 61);
		spawn(244939, 504.10117f, 601.42255f, 322.4643f, (byte) 60);
		spawn(244939, 513.7057f, 643.4998f, 317.10632f, (byte) 11);
		spawn(244939, 511.02466f, 614.0008f, 320.5996f, (byte) 0);
		spawn(244939, 503.81223f, 640.00806f, 317.11395f, (byte) 40);
		spawn(244939, 531.7686f, 642.5042f, 317.08282f, (byte) 112);
		spawn(244939, 522.0014f, 648.79095f, 317.08282f, (byte) 55);
	}

	private void IDTransformDevaStumbleWi68An() {
		spawn(244940, 677.0f, 516.0f, 338.24844f, (byte) 43);
		spawn(244940, 505.28098f, 648.23944f, 317.08282f, (byte) 113);
		spawn(244940, 507.24606f, 628.40027f, 318.4649f, (byte) 61);
		spawn(244940, 504.39532f, 615.5569f, 320.3689f, (byte) 61);
		spawn(244940, 504.10117f, 601.42255f, 322.4643f, (byte) 60);
		spawn(244940, 513.7057f, 643.4998f, 317.10632f, (byte) 11);
		spawn(244940, 511.02466f, 614.0008f, 320.5996f, (byte) 0);
		spawn(244940, 503.81223f, 640.00806f, 317.11395f, (byte) 40);
		spawn(244940, 531.7686f, 642.5042f, 317.08282f, (byte) 112);
		spawn(244940, 522.0014f, 648.79095f, 317.08282f, (byte) 55);
	}

	private void IDTransformDevaStumblePr68An() {
		spawn(244941, 677.0f, 516.0f, 338.24844f, (byte) 43);
		spawn(244941, 505.28098f, 648.23944f, 317.08282f, (byte) 113);
		spawn(244941, 507.24606f, 628.40027f, 318.4649f, (byte) 61);
		spawn(244941, 504.39532f, 615.5569f, 320.3689f, (byte) 61);
		spawn(244941, 504.10117f, 601.42255f, 322.4643f, (byte) 60);
		spawn(244941, 513.7057f, 643.4998f, 317.10632f, (byte) 11);
		spawn(244941, 511.02466f, 614.0008f, 320.5996f, (byte) 0);
		spawn(244941, 503.81223f, 640.00806f, 317.11395f, (byte) 40);
		spawn(244941, 531.7686f, 642.5042f, 317.08282f, (byte) 112);
		spawn(244941, 522.0014f, 648.79095f, 317.08282f, (byte) 55);
	}

	/*** Drop Dragon ***/
	private void IDTransformDropDragon68Ae() {
		switch (Rnd.get(1, 3)) {
			case 1:
				spawn(244954, 622.6814f, 551.9144f, 346.06897f, (byte) 105);
				break;
			case 2:
				spawn(244955, 622.6814f, 551.9144f, 346.06897f, (byte) 105);
				break;
			case 3:
				spawn(244956, 622.6814f, 551.9144f, 346.06897f, (byte) 105);
				break;
		}
		switch (Rnd.get(1, 3)) {
			case 1:
				spawn(244954, 607.8992f, 674.06934f, 352.29062f, (byte) 90);
				break;
			case 2:
				spawn(244955, 607.8992f, 674.06934f, 352.29062f, (byte) 90);
				break;
			case 3:
				spawn(244956, 607.8992f, 674.06934f, 352.29062f, (byte) 90);
				break;
		}
	}

	/*** Bonus Monster ***/
	private void IDTransformBonusMonsterTypeA68An() {
		spawn(246208, 761.54095f, 562.17f, 341.0512f, (byte) 90);
		spawn(246208, 476.94467f, 549.22363f, 345.6048f, (byte) 90);
		spawn(246208, 609.2811f, 707.6352f, 355.10846f, (byte) 93);
	}

	private void IDTransformBonusMonsterTypeB68An() {
		spawn(246209, 761.54095f, 562.17f, 341.0512f, (byte) 90);
		spawn(246209, 476.94467f, 549.22363f, 345.6048f, (byte) 90);
		spawn(246209, 609.2811f, 707.6352f, 355.10846f, (byte) 93);
	}

	private void IDTransformBonusMonsterTypeC68An() {
		spawn(246210, 761.54095f, 562.17f, 341.0512f, (byte) 90);
		spawn(246210, 476.94467f, 549.22363f, 345.6048f, (byte) 90);
		spawn(246210, 609.2811f, 707.6352f, 355.10846f, (byte) 93);
	}

	private void IDTransformBonusMonsterTypeD68An() {
		spawn(246211, 761.54095f, 562.17f, 341.0512f, (byte) 90);
		spawn(246211, 476.94467f, 549.22363f, 345.6048f, (byte) 90);
		spawn(246211, 609.2811f, 707.6352f, 355.10846f, (byte) 93);
	}

	/*** Eresh Warp ***/
	private void IDTransformEreshWarp68() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformEreshWarpFi68An();
				break;
			case 2:
				IDTransformEreshWarpAs68An();
				break;
			case 3:
				IDTransformEreshWarpWi68An();
				break;
			case 4:
				IDTransformEreshWarpPr68An();
				break;
		}
	}

	private void IDTransformEreshWarpFi68An() {
		sp(245601, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245601, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245601, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245601, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245601, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245601, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245601, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245601, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245601, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245601, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245601, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245601, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245601, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245601, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245601, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245601, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245601, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245601, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245601, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformEreshWarpAs68An() {
		sp(245602, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245602, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245602, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245602, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245602, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245602, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245602, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245602, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245602, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245602, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245602, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245602, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245602, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245602, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245602, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245602, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245602, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245602, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245602, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformEreshWarpWi68An() {
		sp(245603, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245603, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245603, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245603, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245603, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245603, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245603, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245603, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245603, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245603, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245603, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245603, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245603, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245603, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245603, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245603, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245603, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245603, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245603, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformEreshWarpPr68An() {
		sp(245604, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245604, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245604, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245604, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245604, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245604, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245604, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245604, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245604, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245604, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245604, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245604, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245604, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245604, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245604, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245604, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245604, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245604, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245604, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	/*** Vritra Warp ***/
	private void IDTransformVritraWarp68() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformVritraWarpFi68An();
				break;
			case 2:
				IDTransformVritraWarpAs68An();
				break;
			case 3:
				IDTransformVritraWarpWi68An();
				break;
			case 4:
				IDTransformVritraWarpPr68An();
				break;
		}
	}

	private void IDTransformVritraWarpFi68An() {
		sp(245605, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245605, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245605, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245605, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245605, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245605, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245605, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245605, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245605, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245605, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245605, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245605, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245605, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245605, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245605, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245605, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245605, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245605, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245605, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformVritraWarpAs68An() {
		sp(245606, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245606, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245606, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245606, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245606, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245606, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245606, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245606, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245606, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245606, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245606, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245606, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245606, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245606, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245606, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245606, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245606, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245606, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245606, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformVritraWarpWi68An() {
		sp(245607, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245607, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245607, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245607, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245607, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245607, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245607, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245607, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245607, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245607, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245607, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245607, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245607, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245607, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245607, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245607, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245607, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245607, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245607, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformVritraWarpPr68An() {
		sp(245608, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245608, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245608, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245608, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245608, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245608, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245608, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245608, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245608, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245608, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245608, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245608, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245608, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245608, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245608, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245608, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245608, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245608, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245608, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	/*** Tiamat Warp ***/
	private void IDTransformTiamatWarp68() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformTiamatWarpFi68An();
				break;
			case 2:
				IDTransformTiamatWarpAs68An();
				break;
			case 3:
				IDTransformTiamatWarpWi68An();
				break;
			case 4:
				IDTransformTiamatWarpPr68An();
				break;
		}
	}

	private void IDTransformTiamatWarpFi68An() {
		sp(245609, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245609, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245609, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245609, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245609, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245609, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245609, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245609, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245609, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245609, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245609, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245609, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245609, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245609, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245609, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245609, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245609, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245609, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245609, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformTiamatWarpAs68An() {
		sp(245610, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245610, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245610, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245610, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245610, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245610, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245610, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245610, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245610, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245610, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245610, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245610, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245610, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245610, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245610, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245610, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245610, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245610, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245610, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformTiamatWarpWi68An() {
		sp(245611, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245611, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245611, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245611, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245611, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245611, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245611, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245611, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245611, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245611, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245611, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245611, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245611, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245611, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245611, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245611, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245611, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245611, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245611, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformTiamatWarpPr68An() {
		sp(245612, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245612, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245612, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245612, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245612, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245612, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245612, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245612, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245612, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245612, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245612, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245612, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245612, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245612, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245612, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245612, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245612, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245612, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245612, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	/*** Ranger ***/
	private void IDTransformRa68An() {
		switch (Rnd.get(1, 3)) {
			case 1:
				IDTransformEreshRa68An();
				break;
			case 2:
				IDTransformVritraRa68An();
				break;
			case 3:
				IDTransformTiamatRa68An();
				break;
		}
	}

	/*** Eresh Ranger ***/
	private void IDTransformEreshRa68An() {
		spawn(245709, 543.0933f, 557.8653f, 322.0f, (byte) 70);
		spawn(245709, 550.4571f, 546.09937f, 322.0f, (byte) 61);
		spawn(245709, 550.2217f, 539.29755f, 322.0f, (byte) 59);
		spawn(245709, 543.0761f, 534.39087f, 322.0f, (byte) 43);
		spawn(245709, 534.18945f, 534.63385f, 321.875f, (byte) 21);
		spawn(245709, 522.56396f, 467.8957f, 322.0f, (byte) 37);
		spawn(245709, 527.70734f, 475.95486f, 322.0f, (byte) 7);
		spawn(245709, 509.6404f, 476.19836f, 322.0f, (byte) 118);
		spawn(245709, 501.61966f, 467.60513f, 322.0f, (byte) 5);
		spawn(245709, 503.2415f, 446.9365f, 321.8276f, (byte) 18);
		spawn(245709, 542.8608f, 472.11404f, 322.0f, (byte) 51);
		spawn(245709, 539.9602f, 467.72766f, 322.0f, (byte) 50);
		spawn(245709, 516.02167f, 453.723f, 321.875f, (byte) 60);
		spawn(245709, 466.30823f, 509.6083f, 342.1901f, (byte) 0);
		spawn(245709, 466.1356f, 516.46765f, 342.1901f, (byte) 0);
		spawn(245709, 468.7195f, 513.0958f, 342.1901f, (byte) 0);
		spawn(245709, 446.91077f, 509.62943f, 342.2713f, (byte) 1);
		spawn(245709, 446.86057f, 517.2843f, 342.27173f, (byte) 0);
		spawn(245709, 449.71277f, 513.23883f, 342.24753f, (byte) 0);
		spawn(245709, 427.54434f, 509.3018f, 342.43564f, (byte) 0);
		spawn(245709, 427.4629f, 517.19116f, 342.43634f, (byte) 0);
		spawn(245709, 430.6331f, 513.1085f, 342.40942f, (byte) 0);
		spawn(245709, 483.40283f, 475.9793f, 345.70047f, (byte) 81);
		spawn(245709, 469.33865f, 454.01733f, 345.70047f, (byte) 4);
		spawn(245709, 482.78824f, 526.4342f, 342.234f, (byte) 78);
		spawn(245709, 502.18372f, 508.00098f, 339.63123f, (byte) 39);
		spawn(245709, 497.84283f, 521.77655f, 339.63815f, (byte) 77);
		spawn(245709, 508.98663f, 527.061f, 339.63126f, (byte) 76);
		spawn(245709, 502.5523f, 649.1053f, 317.08282f, (byte) 109);
		spawn(245709, 522.4196f, 650.3398f, 317.08282f, (byte) 99);
		spawn(245709, 540.84265f, 652.0684f, 316.16275f, (byte) 100);
		spawn(245709, 540.5331f, 637.4856f, 316.17746f, (byte) 17);
		spawn(245709, 514.6148f, 641.9673f, 317.10098f, (byte) 33);
		spawn(245709, 539.03076f, 536.819f, 322.0f, (byte) 30);
	}

	/*** Vritra Ranger ***/
	private void IDTransformVritraRa68An() {
		spawn(245710, 543.0933f, 557.8653f, 322.0f, (byte) 70);
		spawn(245710, 550.4571f, 546.09937f, 322.0f, (byte) 61);
		spawn(245710, 550.2217f, 539.29755f, 322.0f, (byte) 59);
		spawn(245710, 543.0761f, 534.39087f, 322.0f, (byte) 43);
		spawn(245710, 534.18945f, 534.63385f, 321.875f, (byte) 21);
		spawn(245710, 522.56396f, 467.8957f, 322.0f, (byte) 37);
		spawn(245710, 527.70734f, 475.95486f, 322.0f, (byte) 7);
		spawn(245710, 509.6404f, 476.19836f, 322.0f, (byte) 118);
		spawn(245710, 501.61966f, 467.60513f, 322.0f, (byte) 5);
		spawn(245710, 503.2415f, 446.9365f, 321.8276f, (byte) 18);
		spawn(245710, 542.8608f, 472.11404f, 322.0f, (byte) 51);
		spawn(245710, 539.9602f, 467.72766f, 322.0f, (byte) 50);
		spawn(245710, 516.02167f, 453.723f, 321.875f, (byte) 60);
		spawn(245710, 466.30823f, 509.6083f, 342.1901f, (byte) 0);
		spawn(245710, 466.1356f, 516.46765f, 342.1901f, (byte) 0);
		spawn(245710, 468.7195f, 513.0958f, 342.1901f, (byte) 0);
		spawn(245710, 446.91077f, 509.62943f, 342.2713f, (byte) 1);
		spawn(245710, 446.86057f, 517.2843f, 342.27173f, (byte) 0);
		spawn(245710, 449.71277f, 513.23883f, 342.24753f, (byte) 0);
		spawn(245710, 427.54434f, 509.3018f, 342.43564f, (byte) 0);
		spawn(245710, 427.4629f, 517.19116f, 342.43634f, (byte) 0);
		spawn(245710, 430.6331f, 513.1085f, 342.40942f, (byte) 0);
		spawn(245710, 483.40283f, 475.9793f, 345.70047f, (byte) 81);
		spawn(245710, 469.33865f, 454.01733f, 345.70047f, (byte) 4);
		spawn(245710, 482.78824f, 526.4342f, 342.234f, (byte) 78);
		spawn(245710, 502.18372f, 508.00098f, 339.63123f, (byte) 39);
		spawn(245710, 497.84283f, 521.77655f, 339.63815f, (byte) 77);
		spawn(245710, 508.98663f, 527.061f, 339.63126f, (byte) 76);
		spawn(245710, 502.5523f, 649.1053f, 317.08282f, (byte) 109);
		spawn(245710, 522.4196f, 650.3398f, 317.08282f, (byte) 99);
		spawn(245710, 540.84265f, 652.0684f, 316.16275f, (byte) 100);
		spawn(245710, 540.5331f, 637.4856f, 316.17746f, (byte) 17);
		spawn(245710, 514.6148f, 641.9673f, 317.10098f, (byte) 33);
		spawn(245710, 539.03076f, 536.819f, 322.0f, (byte) 30);
	}

	/*** Tiamat Ranger ***/
	private void IDTransformTiamatRa68An() {
		spawn(245711, 543.0933f, 557.8653f, 322.0f, (byte) 70);
		spawn(245711, 550.4571f, 546.09937f, 322.0f, (byte) 61);
		spawn(245711, 550.2217f, 539.29755f, 322.0f, (byte) 59);
		spawn(245711, 543.0761f, 534.39087f, 322.0f, (byte) 43);
		spawn(245711, 534.18945f, 534.63385f, 321.875f, (byte) 21);
		spawn(245711, 522.56396f, 467.8957f, 322.0f, (byte) 37);
		spawn(245711, 527.70734f, 475.95486f, 322.0f, (byte) 7);
		spawn(245711, 509.6404f, 476.19836f, 322.0f, (byte) 118);
		spawn(245711, 501.61966f, 467.60513f, 322.0f, (byte) 5);
		spawn(245711, 503.2415f, 446.9365f, 321.8276f, (byte) 18);
		spawn(245711, 542.8608f, 472.11404f, 322.0f, (byte) 51);
		spawn(245711, 539.9602f, 467.72766f, 322.0f, (byte) 50);
		spawn(245711, 516.02167f, 453.723f, 321.875f, (byte) 60);
		spawn(245711, 466.30823f, 509.6083f, 342.1901f, (byte) 0);
		spawn(245711, 466.1356f, 516.46765f, 342.1901f, (byte) 0);
		spawn(245711, 468.7195f, 513.0958f, 342.1901f, (byte) 0);
		spawn(245711, 446.91077f, 509.62943f, 342.2713f, (byte) 1);
		spawn(245711, 446.86057f, 517.2843f, 342.27173f, (byte) 0);
		spawn(245711, 449.71277f, 513.23883f, 342.24753f, (byte) 0);
		spawn(245711, 427.54434f, 509.3018f, 342.43564f, (byte) 0);
		spawn(245711, 427.4629f, 517.19116f, 342.43634f, (byte) 0);
		spawn(245711, 430.6331f, 513.1085f, 342.40942f, (byte) 0);
		spawn(245711, 483.40283f, 475.9793f, 345.70047f, (byte) 81);
		spawn(245711, 469.33865f, 454.01733f, 345.70047f, (byte) 4);
		spawn(245711, 482.78824f, 526.4342f, 342.234f, (byte) 78);
		spawn(245711, 502.18372f, 508.00098f, 339.63123f, (byte) 39);
		spawn(245711, 497.84283f, 521.77655f, 339.63815f, (byte) 77);
		spawn(245711, 508.98663f, 527.061f, 339.63126f, (byte) 76);
		spawn(245711, 502.5523f, 649.1053f, 317.08282f, (byte) 109);
		spawn(245711, 522.4196f, 650.3398f, 317.08282f, (byte) 99);
		spawn(245711, 540.84265f, 652.0684f, 316.16275f, (byte) 100);
		spawn(245711, 540.5331f, 637.4856f, 316.17746f, (byte) 17);
		spawn(245711, 514.6148f, 641.9673f, 317.10098f, (byte) 33);
		spawn(245711, 539.03076f, 536.819f, 322.0f, (byte) 30);
	}

	/*** Player Lvl 69 ***/
	/*** Eresh High ***/
	private void IDTransformEreshHigh69An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformEreshHighFi69An();
				break;
			case 2:
				IDTransformEreshHighAs69An();
				break;
			case 3:
				IDTransformEreshHighWi69An();
				break;
			case 4:
				IDTransformEreshHighPr69An();
				break;
		}
	}

	private void IDTransformEreshHighFi69An() {
		spawn(244593, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244593, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244593, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244593, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244593, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244593, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244593, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244593, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244593, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244593, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244593, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244593, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244593, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244593, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244593, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244593, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244593, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformEreshHighAs69An() {
		spawn(244594, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244594, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244594, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244594, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244594, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244594, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244594, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244594, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244594, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244594, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244594, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244594, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244594, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244594, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244594, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244594, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244594, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformEreshHighWi69An() {
		spawn(244595, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244595, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244595, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244595, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244595, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244595, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244595, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244595, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244595, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244595, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244595, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244595, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244595, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244595, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244595, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244595, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244595, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformEreshHighPr69An() {
		spawn(244596, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244596, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244596, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244596, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244596, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244596, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244596, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244596, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244596, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244596, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244596, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244596, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244596, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244596, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244596, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244596, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244596, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	/*** Vritra High ***/
	private void IDTransformVritraHigh69An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformVritraHighFi69An();
				break;
			case 2:
				IDTransformVritraHighAs69An();
				break;
			case 3:
				IDTransformVritraHighWi69An();
				break;
			case 4:
				IDTransformVritraHighPr69An();
				break;
		}
	}

	private void IDTransformVritraHighFi69An() {
		spawn(244597, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244597, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244597, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244597, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244597, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244597, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244597, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244597, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244597, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244597, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244597, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244597, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244597, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244597, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244597, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244597, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244597, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformVritraHighAs69An() {
		spawn(244598, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244598, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244598, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244598, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244598, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244598, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244598, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244598, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244598, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244598, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244598, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244598, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244598, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244598, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244598, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244598, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244598, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformVritraHighWi69An() {
		spawn(244599, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244599, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244599, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244599, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244599, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244599, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244599, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244599, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244599, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244599, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244599, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244599, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244599, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244599, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244599, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244599, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244599, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformVritraHighPr69An() {
		spawn(244600, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244600, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244600, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244600, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244600, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244600, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244600, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244600, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244600, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244600, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244600, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244600, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244600, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244600, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244600, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244600, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244600, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	/*** Tiamat High ***/
	private void IDTransformTiamatHigh69An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformTiamatHighFi69An();
				break;
			case 2:
				IDTransformTiamatHighAs69An();
				break;
			case 3:
				IDTransformTiamatHighWi69An();
				break;
			case 4:
				IDTransformTiamatHighPr69An();
				break;
		}
	}

	private void IDTransformTiamatHighFi69An() {
		spawn(244601, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244601, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244601, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244601, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244601, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244601, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244601, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244601, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244601, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244601, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244601, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244601, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244601, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244601, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244601, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244601, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244601, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformTiamatHighAs69An() {
		spawn(244602, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244602, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244602, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244602, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244602, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244602, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244602, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244602, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244602, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244602, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244602, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244602, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244602, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244602, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244602, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244602, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244602, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformTiamatHighWi69An() {
		spawn(244603, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244603, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244603, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244603, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244603, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244603, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244603, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244603, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244603, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244603, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244603, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244603, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244603, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244603, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244603, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244603, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244603, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformTiamatHighPr69An() {
		spawn(244604, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244604, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244604, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244604, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244604, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244604, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244604, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244604, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244604, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244604, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244604, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244604, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244604, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244604, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244604, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244604, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244604, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	/*** Eresh Guard ***/
	private void IDTransformEreshGuard69An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformEreshGuardFi69An();
				break;
			case 2:
				IDTransformEreshGuardAs69An();
				break;
			case 3:
				IDTransformEreshGuardWi69An();
				break;
			case 4:
				IDTransformEreshGuardPr69An();
				break;
		}
	}

	private void IDTransformEreshGuardFi69An() {
		spawn(244581, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244581, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244581, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244581, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244581, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244581, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244581, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244581, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244581, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244581, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244581, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244581, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244581, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244581, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244581, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244581, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244581, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244581, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244581, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244581, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244581, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244581, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244581, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244581, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244581, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244581, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244581, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244581, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244581, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244581, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244581, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244581, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244581, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244581, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244581, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244581, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244581, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244581, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244581, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244581, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244581, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformEreshGuardAs69An() {
		spawn(244582, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244582, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244582, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244582, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244582, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244582, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244582, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244582, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244582, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244582, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244582, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244582, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244582, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244582, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244582, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244582, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244582, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244582, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244582, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244582, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244582, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244582, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244582, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244582, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244582, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244582, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244582, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244582, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244582, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244582, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244582, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244582, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244582, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244582, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244582, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244582, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244582, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244582, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244582, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244582, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244582, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformEreshGuardWi69An() {
		spawn(244583, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244583, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244583, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244583, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244583, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244583, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244583, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244583, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244583, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244583, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244583, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244583, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244583, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244583, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244583, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244583, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244583, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244583, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244583, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244583, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244583, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244583, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244583, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244583, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244583, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244583, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244583, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244583, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244583, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244583, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244583, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244583, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244583, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244583, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244583, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244583, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244583, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244583, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244583, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244583, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244583, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformEreshGuardPr69An() {
		spawn(244584, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244584, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244584, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244584, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244584, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244584, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244584, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244584, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244584, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244584, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244584, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244584, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244584, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244584, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244584, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244584, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244584, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244584, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244584, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244584, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244584, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244584, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244584, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244584, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244584, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244584, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244584, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244584, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244584, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244584, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244584, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244584, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244584, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244584, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244584, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244584, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244584, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244584, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244584, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244584, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244584, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	/*** Vritra Guard ***/
	private void IDTransformVritraGuard69An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformVritraGuardFi69An();
				break;
			case 2:
				IDTransformVritraGuardAs69An();
				break;
			case 3:
				IDTransformVritraGuardWi69An();
				break;
			case 4:
				IDTransformVritraGuardPr69An();
				break;
		}
	}

	private void IDTransformVritraGuardFi69An() {
		spawn(244585, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244585, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244585, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244585, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244585, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244585, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244585, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244585, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244585, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244585, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244585, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244585, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244585, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244585, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244585, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244585, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244585, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244585, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244585, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244585, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244585, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244585, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244585, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244585, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244585, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244585, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244585, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244585, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244585, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244585, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244585, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244585, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244585, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244585, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244585, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244585, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244585, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244585, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244585, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244585, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244585, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformVritraGuardAs69An() {
		spawn(244586, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244586, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244586, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244586, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244586, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244586, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244586, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244586, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244586, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244586, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244586, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244586, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244586, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244586, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244586, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244586, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244586, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244586, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244586, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244586, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244586, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244586, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244586, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244586, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244586, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244586, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244586, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244586, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244586, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244586, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244586, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244586, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244586, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244586, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244586, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244586, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244586, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244586, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244586, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244586, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244586, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformVritraGuardWi69An() {
		spawn(244587, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244587, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244587, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244587, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244587, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244587, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244587, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244587, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244587, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244587, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244587, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244587, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244587, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244587, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244587, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244587, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244587, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244587, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244587, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244587, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244587, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244587, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244587, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244587, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244587, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244587, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244587, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244587, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244587, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244587, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244587, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244587, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244587, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244587, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244587, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244587, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244587, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244587, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244587, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244587, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244587, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformVritraGuardPr69An() {
		spawn(244588, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244588, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244588, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244588, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244588, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244588, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244588, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244588, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244588, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244588, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244588, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244588, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244588, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244588, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244588, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244588, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244588, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244588, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244588, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244588, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244588, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244588, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244588, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244588, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244588, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244588, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244588, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244588, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244588, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244588, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244588, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244588, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244588, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244588, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244588, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244588, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244588, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244588, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244588, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244588, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244588, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	/*** Tiamat Guard ***/
	private void IDTransformTiamatGuard69An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformTiamatGuardFi69An();
				break;
			case 2:
				IDTransformTiamatGuardAs69An();
				break;
			case 3:
				IDTransformTiamatGuardWi69An();
				break;
			case 4:
				IDTransformTiamatGuardPr69An();
				break;
		}
	}

	private void IDTransformTiamatGuardFi69An() {
		spawn(244589, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244589, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244589, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244589, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244589, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244589, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244589, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244589, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244589, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244589, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244589, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244589, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244589, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244589, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244589, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244589, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244589, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244589, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244589, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244589, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244589, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244589, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244589, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244589, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244589, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244589, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244589, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244589, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244589, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244589, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244589, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244589, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244589, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244589, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244589, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244589, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244589, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244589, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244589, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244589, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244589, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformTiamatGuardAs69An() {
		spawn(244590, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244590, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244590, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244590, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244590, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244590, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244590, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244590, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244590, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244590, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244590, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244590, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244590, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244590, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244590, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244590, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244590, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244590, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244590, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244590, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244590, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244590, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244590, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244590, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244590, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244590, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244590, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244590, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244590, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244590, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244590, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244590, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244590, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244590, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244590, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244590, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244590, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244590, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244590, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244590, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244590, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformTiamatGuardWi69An() {
		spawn(244591, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244591, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244591, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244591, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244591, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244591, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244591, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244591, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244591, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244591, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244591, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244591, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244591, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244591, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244591, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244591, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244591, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244591, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244591, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244591, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244591, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244591, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244591, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244591, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244591, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244591, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244591, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244591, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244591, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244591, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244591, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244591, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244591, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244591, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244591, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244591, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244591, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244591, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244591, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244591, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244591, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformTiamatGuardPr69An() {
		spawn(244592, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244592, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244592, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244592, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244592, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244592, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244592, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244592, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244592, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244592, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244592, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244592, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244592, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244592, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244592, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244592, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244592, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244592, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244592, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244592, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244592, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244592, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244592, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244592, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244592, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244592, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244592, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244592, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244592, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244592, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244592, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244592, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244592, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244592, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244592, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244592, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244592, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244592, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244592, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244592, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244592, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	/*** Deva Guard ***/
	private void IDTransformDevaGuardFi69An() {
		spawn(244608, 543.9115f, 491.4599f, 322.04422f, (byte) 91);
		spawn(244608, 547.9682f, 490.34494f, 321.50793f, (byte) 90);
		spawn(244608, 534.53534f, 485.35455f, 322.0f, (byte) 89);
		spawn(244608, 541.57935f, 487.14307f, 322.0f, (byte) 89);
		spawn(244608, 516.4117f, 585.5329f, 321.97427f, (byte) 90);
		spawn(244608, 499.82422f, 585.81537f, 322.02252f, (byte) 92);
		spawn(244608, 501.61078f, 555.0141f, 321.84106f, (byte) 12);
	}

	private void IDTransformDevaGuardAs69An() {
		spawn(244609, 855.63855f, 484.74774f, 349.08722f, (byte) 45);
		spawn(244609, 852.21844f, 482.80756f, 349.41986f, (byte) 44);
		spawn(244609, 864.0118f, 492.74384f, 349.56552f, (byte) 46);
		spawn(244609, 508.17993f, 587.8108f, 322.56973f, (byte) 90);
		spawn(244609, 526.8684f, 493.7475f, 322.0f, (byte) 91);
		spawn(244609, 537.37225f, 508.5786f, 322.0f, (byte) 90);
		spawn(244609, 512.8187f, 590.4575f, 322.56216f, (byte) 90);
		spawn(244609, 504.50745f, 590.3867f, 322.56216f, (byte) 90);
		spawn(244609, 482.3006f, 496.94232f, 342.22174f, (byte) 27);
		spawn(244609, 594.4832f, 673.60126f, 350.85538f, (byte) 54);
		spawn(244609, 634.298f, 511.2995f, 339.61545f, (byte) 61);
		spawn(244609, 530.982f, 493.62543f, 322.0f, (byte) 90);
		spawn(244609, 641.9954f, 514.2143f, 339.61542f, (byte) 61);
		spawn(244609, 505.45636f, 584.90186f, 322.0f, (byte) 90);
		spawn(244609, 510.66394f, 584.80176f, 322.0f, (byte) 90);
		spawn(244609, 859.2291f, 490.53555f, 348.98047f, (byte) 45);
		spawn(244609, 604.1567f, 635.69995f, 352.53815f, (byte) 30);
		spawn(244609, 467.0f, 476.0f, 345.70047f, (byte) 32);
		spawn(244609, 536.2914f, 488.65588f, 322.0f, (byte) 90);
		spawn(244609, 639.6124f, 525.6597f, 339.61542f, (byte) 62);
		spawn(244609, 885.47394f, 464.9668f, 351.0f, (byte) 66);
		spawn(244609, 642.88293f, 520.17224f, 339.61542f, (byte) 60);
		spawn(244609, 546.91925f, 510.66574f, 321.94254f, (byte) 91);
		spawn(244609, 527.7633f, 502.87167f, 321.6398f, (byte) 91);
		spawn(244609, 885.5619f, 456.29126f, 351.02737f, (byte) 55);
	}

	private void IDTransformDevaGuardWi69An() {
		spawn(244610, 513.0236f, 593.86285f, 322.56216f, (byte) 90);
		spawn(244610, 501.3256f, 593.69885f, 322.56216f, (byte) 90);
		spawn(244610, 633.75323f, 516.50934f, 339.61545f, (byte) 61);
		spawn(244610, 544.93945f, 499.1886f, 322.0f, (byte) 91);
		spawn(244610, 530.8947f, 502.38684f, 321.86185f, (byte) 91);
		spawn(244610, 634.5065f, 522.0903f, 339.61545f, (byte) 53);
		spawn(244610, 885.382f, 460.77805f, 351.0f, (byte) 60);
		spawn(244610, 612.8048f, 643.80194f, 352.46414f, (byte) 30);
		spawn(244610, 538.00024f, 495.57394f, 322.0f, (byte) 90);
		spawn(244610, 526.9178f, 486.92227f, 321.87527f, (byte) 85);
	}

	private void IDTransformDevaGuardRa69An() {
		spawn(244611, 529.87f, 545.5564f, 321.85876f, (byte) 95);
		spawn(244611, 526.1612f, 545.1311f, 321.58078f, (byte) 88);
		spawn(244611, 546.40234f, 523.6142f, 321.97485f, (byte) 87);
		spawn(244611, 549.9647f, 524.9884f, 321.9978f, (byte) 95);
		spawn(244611, 520.9293f, 586.19006f, 321.94193f, (byte) 95);
		spawn(244611, 495.259f, 586.9457f, 322.0659f, (byte) 81);
		spawn(244611, 505.0709f, 552.7453f, 322.0f, (byte) 28);
	}

	/*** Eresh Stumble ***/
	private void IDTransformEreshStumble69An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformEreshStumbleFi69An();
				break;
			case 2:
				IDTransformEreshStumbleAs69An();
				break;
			case 3:
				IDTransformEreshStumbleWi69An();
				break;
			case 4:
				IDTransformEreshStumblePr69An();
				break;
		}
	}

	private void IDTransformEreshStumbleFi69An() {
		spawn(244957, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(244957, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(244957, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformEreshStumbleAs69An() {
		spawn(244958, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(244958, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(244958, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformEreshStumbleWi69An() {
		spawn(244959, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(244959, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(244959, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformEreshStumblePr69An() {
		spawn(244960, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(244960, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(244960, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	/*** Vritra Stumble ***/
	private void IDTransformVritraStumble69An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformVritraStumbleFi69An();
				break;
			case 2:
				IDTransformVritraStumbleAs69An();
				break;
			case 3:
				IDTransformVritraStumbleWi69An();
				break;
			case 4:
				IDTransformVritraStumblePr69An();
				break;
		}
	}

	private void IDTransformVritraStumbleFi69An() {
		spawn(244961, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(244961, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(244961, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformVritraStumbleAs69An() {
		spawn(244962, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(244962, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(244962, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformVritraStumbleWi69An() {
		spawn(244963, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(244963, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(244963, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformVritraStumblePr69An() {
		spawn(244964, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(244964, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(244964, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	/*** Tiamat Stumble ***/
	private void IDTransformTiamatStumble69An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformTiamatStumbleFi69An();
				break;
			case 2:
				IDTransformTiamatStumbleAs69An();
				break;
			case 3:
				IDTransformTiamatStumbleWi69An();
				break;
			case 4:
				IDTransformTiamatStumblePr69An();
				break;
		}
	}

	private void IDTransformTiamatStumbleFi69An() {
		spawn(244965, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(244965, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(244965, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformTiamatStumbleAs69An() {
		spawn(244966, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(244966, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(244966, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformTiamatStumbleWi69An() {
		spawn(244967, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(244967, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(244967, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformTiamatStumblePr69An() {
		spawn(244968, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(244968, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(244968, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	/*** Deva Stumble ***/
	private void IDTransformDevaStumble69An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformDevaStumbleFi69An();
				break;
			case 2:
				IDTransformDevaStumbleAs69An();
				break;
			case 3:
				IDTransformDevaStumbleWi69An();
				break;
			case 4:
				IDTransformDevaStumblePr69An();
				break;
		}
	}

	private void IDTransformDevaStumbleFi69An() {
		spawn(244969, 677.0f, 516.0f, 338.24844f, (byte) 43);
		spawn(244969, 505.28098f, 648.23944f, 317.08282f, (byte) 113);
		spawn(244969, 507.24606f, 628.40027f, 318.4649f, (byte) 61);
		spawn(244969, 504.39532f, 615.5569f, 320.3689f, (byte) 61);
		spawn(244969, 504.10117f, 601.42255f, 322.4643f, (byte) 60);
		spawn(244969, 513.7057f, 643.4998f, 317.10632f, (byte) 11);
		spawn(244969, 511.02466f, 614.0008f, 320.5996f, (byte) 0);
		spawn(244969, 503.81223f, 640.00806f, 317.11395f, (byte) 40);
		spawn(244969, 531.7686f, 642.5042f, 317.08282f, (byte) 112);
		spawn(244969, 522.0014f, 648.79095f, 317.08282f, (byte) 55);
	}

	private void IDTransformDevaStumbleAs69An() {
		spawn(244970, 677.0f, 516.0f, 338.24844f, (byte) 43);
		spawn(244970, 505.28098f, 648.23944f, 317.08282f, (byte) 113);
		spawn(244970, 507.24606f, 628.40027f, 318.4649f, (byte) 61);
		spawn(244970, 504.39532f, 615.5569f, 320.3689f, (byte) 61);
		spawn(244970, 504.10117f, 601.42255f, 322.4643f, (byte) 60);
		spawn(244970, 513.7057f, 643.4998f, 317.10632f, (byte) 11);
		spawn(244970, 511.02466f, 614.0008f, 320.5996f, (byte) 0);
		spawn(244970, 503.81223f, 640.00806f, 317.11395f, (byte) 40);
		spawn(244970, 531.7686f, 642.5042f, 317.08282f, (byte) 112);
		spawn(244970, 522.0014f, 648.79095f, 317.08282f, (byte) 55);
	}

	private void IDTransformDevaStumbleWi69An() {
		spawn(244971, 677.0f, 516.0f, 338.24844f, (byte) 43);
		spawn(244971, 505.28098f, 648.23944f, 317.08282f, (byte) 113);
		spawn(244971, 507.24606f, 628.40027f, 318.4649f, (byte) 61);
		spawn(244971, 504.39532f, 615.5569f, 320.3689f, (byte) 61);
		spawn(244971, 504.10117f, 601.42255f, 322.4643f, (byte) 60);
		spawn(244971, 513.7057f, 643.4998f, 317.10632f, (byte) 11);
		spawn(244971, 511.02466f, 614.0008f, 320.5996f, (byte) 0);
		spawn(244971, 503.81223f, 640.00806f, 317.11395f, (byte) 40);
		spawn(244971, 531.7686f, 642.5042f, 317.08282f, (byte) 112);
		spawn(244971, 522.0014f, 648.79095f, 317.08282f, (byte) 55);
	}

	private void IDTransformDevaStumblePr69An() {
		spawn(244972, 677.0f, 516.0f, 338.24844f, (byte) 43);
		spawn(244972, 505.28098f, 648.23944f, 317.08282f, (byte) 113);
		spawn(244972, 507.24606f, 628.40027f, 318.4649f, (byte) 61);
		spawn(244972, 504.39532f, 615.5569f, 320.3689f, (byte) 61);
		spawn(244972, 504.10117f, 601.42255f, 322.4643f, (byte) 60);
		spawn(244972, 513.7057f, 643.4998f, 317.10632f, (byte) 11);
		spawn(244972, 511.02466f, 614.0008f, 320.5996f, (byte) 0);
		spawn(244972, 503.81223f, 640.00806f, 317.11395f, (byte) 40);
		spawn(244972, 531.7686f, 642.5042f, 317.08282f, (byte) 112);
		spawn(244972, 522.0014f, 648.79095f, 317.08282f, (byte) 55);
	}

	/*** Drop Dragon ***/
	private void IDTransformDropDragon69Ae() {
		switch (Rnd.get(1, 3)) {
			case 1:
				spawn(244985, 622.6814f, 551.9144f, 346.06897f, (byte) 105);
				break;
			case 2:
				spawn(244986, 622.6814f, 551.9144f, 346.06897f, (byte) 105);
				break;
			case 3:
				spawn(244987, 622.6814f, 551.9144f, 346.06897f, (byte) 105);
				break;
		}
		switch (Rnd.get(1, 3)) {
			case 1:
				spawn(244985, 607.8992f, 674.06934f, 352.29062f, (byte) 90);
				break;
			case 2:
				spawn(244986, 607.8992f, 674.06934f, 352.29062f, (byte) 90);
				break;
			case 3:
				spawn(244987, 607.8992f, 674.06934f, 352.29062f, (byte) 90);
				break;
		}
	}

	/*** Bonus Monster ***/
	private void IDTransformBonusMonsterTypeA69An() {
		spawn(246212, 761.54095f, 562.17f, 341.0512f, (byte) 90);
		spawn(246212, 476.94467f, 549.22363f, 345.6048f, (byte) 90);
		spawn(246212, 609.2811f, 707.6352f, 355.10846f, (byte) 93);
	}

	private void IDTransformBonusMonsterTypeB69An() {
		spawn(246213, 761.54095f, 562.17f, 341.0512f, (byte) 90);
		spawn(246213, 476.94467f, 549.22363f, 345.6048f, (byte) 90);
		spawn(246213, 609.2811f, 707.6352f, 355.10846f, (byte) 93);
	}

	private void IDTransformBonusMonsterTypeC69An() {
		spawn(246214, 761.54095f, 562.17f, 341.0512f, (byte) 90);
		spawn(246214, 476.94467f, 549.22363f, 345.6048f, (byte) 90);
		spawn(246214, 609.2811f, 707.6352f, 355.10846f, (byte) 93);
	}

	private void IDTransformBonusMonsterTypeD69An() {
		spawn(246215, 761.54095f, 562.17f, 341.0512f, (byte) 90);
		spawn(246215, 476.94467f, 549.22363f, 345.6048f, (byte) 90);
		spawn(246215, 609.2811f, 707.6352f, 355.10846f, (byte) 93);
	}

	/*** Eresh Warp ***/
	private void IDTransformEreshWarp69() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformEreshWarpFi69An();
				break;
			case 2:
				IDTransformEreshWarpAs69An();
				break;
			case 3:
				IDTransformEreshWarpWi69An();
				break;
			case 4:
				IDTransformEreshWarpPr69An();
				break;
		}
	}

	private void IDTransformEreshWarpFi69An() {
		sp(245613, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245613, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245613, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245613, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245613, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245613, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245613, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245613, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245613, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245613, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245613, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245613, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245613, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245613, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245613, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245613, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245613, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245613, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245613, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformEreshWarpAs69An() {
		sp(245614, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245614, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245614, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245614, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245614, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245614, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245614, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245614, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245614, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245614, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245614, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245614, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245614, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245614, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245614, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245614, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245614, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245614, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245614, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformEreshWarpWi69An() {
		sp(245615, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245615, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245615, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245615, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245615, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245615, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245615, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245615, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245615, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245615, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245615, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245615, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245615, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245615, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245615, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245615, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245615, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245615, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245615, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformEreshWarpPr69An() {
		sp(245616, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245616, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245616, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245616, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245616, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245616, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245616, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245616, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245616, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245616, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245616, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245616, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245616, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245616, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245616, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245616, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245616, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245616, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245616, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	/*** Vritra Warp ***/
	private void IDTransformVritraWarp69() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformVritraWarpFi69An();
				break;
			case 2:
				IDTransformVritraWarpAs69An();
				break;
			case 3:
				IDTransformVritraWarpWi69An();
				break;
			case 4:
				IDTransformVritraWarpPr69An();
				break;
		}
	}

	private void IDTransformVritraWarpFi69An() {
		sp(245617, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245617, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245617, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245617, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245617, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245617, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245617, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245617, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245617, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245617, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245617, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245617, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245617, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245617, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245617, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245617, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245617, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245617, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245617, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformVritraWarpAs69An() {
		sp(245618, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245618, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245618, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245618, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245618, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245618, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245618, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245618, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245618, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245618, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245618, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245618, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245618, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245618, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245618, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245618, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245618, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245618, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245618, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformVritraWarpWi69An() {
		sp(245619, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245619, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245619, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245619, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245619, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245619, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245619, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245619, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245619, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245619, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245619, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245619, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245619, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245619, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245619, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245619, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245619, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245619, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245619, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformVritraWarpPr69An() {
		sp(245620, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245620, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245620, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245620, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245620, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245620, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245620, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245620, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245620, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245620, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245620, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245620, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245620, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245620, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245620, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245620, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245620, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245620, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245620, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	/*** Tiamat Warp ***/
	private void IDTransformTiamatWarp69() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformTiamatWarpFi69An();
				break;
			case 2:
				IDTransformTiamatWarpAs69An();
				break;
			case 3:
				IDTransformTiamatWarpWi69An();
				break;
			case 4:
				IDTransformTiamatWarpPr69An();
				break;
		}
	}

	private void IDTransformTiamatWarpFi69An() {
		sp(245621, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245621, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245621, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245621, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245621, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245621, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245621, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245621, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245621, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245621, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245621, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245621, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245621, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245621, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245621, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245621, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245621, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245621, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245621, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformTiamatWarpAs69An() {
		sp(245622, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245622, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245622, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245622, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245622, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245622, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245622, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245622, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245622, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245622, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245622, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245622, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245622, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245622, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245622, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245622, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245622, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245622, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245622, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformTiamatWarpWi69An() {
		sp(245623, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245623, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245623, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245623, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245623, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245623, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245623, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245623, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245623, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245623, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245623, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245623, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245623, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245623, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245623, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245623, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245623, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245623, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245623, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformTiamatWarpPr69An() {
		sp(245624, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245624, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245624, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245624, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245624, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245624, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245624, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245624, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245624, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245624, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245624, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245624, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245624, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245624, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245624, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245624, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245624, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245624, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245624, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	/*** Ranger ***/
	private void IDTransformRa69An() {
		switch (Rnd.get(1, 3)) {
			case 1:
				IDTransformEreshRa69An();
				break;
			case 2:
				IDTransformVritraRa69An();
				break;
			case 3:
				IDTransformTiamatRa69An();
				break;
		}
	}

	/*** Eresh Ranger ***/
	private void IDTransformEreshRa69An() {
		spawn(245715, 543.0933f, 557.8653f, 322.0f, (byte) 70);
		spawn(245715, 550.4571f, 546.09937f, 322.0f, (byte) 61);
		spawn(245715, 550.2217f, 539.29755f, 322.0f, (byte) 59);
		spawn(245715, 543.0761f, 534.39087f, 322.0f, (byte) 43);
		spawn(245715, 534.18945f, 534.63385f, 321.875f, (byte) 21);
		spawn(245715, 522.56396f, 467.8957f, 322.0f, (byte) 37);
		spawn(245715, 527.70734f, 475.95486f, 322.0f, (byte) 7);
		spawn(245715, 509.6404f, 476.19836f, 322.0f, (byte) 118);
		spawn(245715, 501.61966f, 467.60513f, 322.0f, (byte) 5);
		spawn(245715, 503.2415f, 446.9365f, 321.8276f, (byte) 18);
		spawn(245715, 542.8608f, 472.11404f, 322.0f, (byte) 51);
		spawn(245715, 539.9602f, 467.72766f, 322.0f, (byte) 50);
		spawn(245715, 516.02167f, 453.723f, 321.875f, (byte) 60);
		spawn(245715, 466.30823f, 509.6083f, 342.1901f, (byte) 0);
		spawn(245715, 466.1356f, 516.46765f, 342.1901f, (byte) 0);
		spawn(245715, 468.7195f, 513.0958f, 342.1901f, (byte) 0);
		spawn(245715, 446.91077f, 509.62943f, 342.2713f, (byte) 1);
		spawn(245715, 446.86057f, 517.2843f, 342.27173f, (byte) 0);
		spawn(245715, 449.71277f, 513.23883f, 342.24753f, (byte) 0);
		spawn(245715, 427.54434f, 509.3018f, 342.43564f, (byte) 0);
		spawn(245715, 427.4629f, 517.19116f, 342.43634f, (byte) 0);
		spawn(245715, 430.6331f, 513.1085f, 342.40942f, (byte) 0);
		spawn(245715, 483.40283f, 475.9793f, 345.70047f, (byte) 81);
		spawn(245715, 469.33865f, 454.01733f, 345.70047f, (byte) 4);
		spawn(245715, 482.78824f, 526.4342f, 342.234f, (byte) 78);
		spawn(245715, 502.18372f, 508.00098f, 339.63123f, (byte) 39);
		spawn(245715, 497.84283f, 521.77655f, 339.63815f, (byte) 77);
		spawn(245715, 508.98663f, 527.061f, 339.63126f, (byte) 76);
		spawn(245715, 502.5523f, 649.1053f, 317.08282f, (byte) 109);
		spawn(245715, 522.4196f, 650.3398f, 317.08282f, (byte) 99);
		spawn(245715, 540.84265f, 652.0684f, 316.16275f, (byte) 100);
		spawn(245715, 540.5331f, 637.4856f, 316.17746f, (byte) 17);
		spawn(245715, 514.6148f, 641.9673f, 317.10098f, (byte) 33);
		spawn(245715, 539.03076f, 536.819f, 322.0f, (byte) 30);
	}

	/*** Vritra Ranger ***/
	private void IDTransformVritraRa69An() {
		spawn(245716, 543.0933f, 557.8653f, 322.0f, (byte) 70);
		spawn(245716, 550.4571f, 546.09937f, 322.0f, (byte) 61);
		spawn(245716, 550.2217f, 539.29755f, 322.0f, (byte) 59);
		spawn(245716, 543.0761f, 534.39087f, 322.0f, (byte) 43);
		spawn(245716, 534.18945f, 534.63385f, 321.875f, (byte) 21);
		spawn(245716, 522.56396f, 467.8957f, 322.0f, (byte) 37);
		spawn(245716, 527.70734f, 475.95486f, 322.0f, (byte) 7);
		spawn(245716, 509.6404f, 476.19836f, 322.0f, (byte) 118);
		spawn(245716, 501.61966f, 467.60513f, 322.0f, (byte) 5);
		spawn(245716, 503.2415f, 446.9365f, 321.8276f, (byte) 18);
		spawn(245716, 542.8608f, 472.11404f, 322.0f, (byte) 51);
		spawn(245716, 539.9602f, 467.72766f, 322.0f, (byte) 50);
		spawn(245716, 516.02167f, 453.723f, 321.875f, (byte) 60);
		spawn(245716, 466.30823f, 509.6083f, 342.1901f, (byte) 0);
		spawn(245716, 466.1356f, 516.46765f, 342.1901f, (byte) 0);
		spawn(245716, 468.7195f, 513.0958f, 342.1901f, (byte) 0);
		spawn(245716, 446.91077f, 509.62943f, 342.2713f, (byte) 1);
		spawn(245716, 446.86057f, 517.2843f, 342.27173f, (byte) 0);
		spawn(245716, 449.71277f, 513.23883f, 342.24753f, (byte) 0);
		spawn(245716, 427.54434f, 509.3018f, 342.43564f, (byte) 0);
		spawn(245716, 427.4629f, 517.19116f, 342.43634f, (byte) 0);
		spawn(245716, 430.6331f, 513.1085f, 342.40942f, (byte) 0);
		spawn(245716, 483.40283f, 475.9793f, 345.70047f, (byte) 81);
		spawn(245716, 469.33865f, 454.01733f, 345.70047f, (byte) 4);
		spawn(245716, 482.78824f, 526.4342f, 342.234f, (byte) 78);
		spawn(245716, 502.18372f, 508.00098f, 339.63123f, (byte) 39);
		spawn(245716, 497.84283f, 521.77655f, 339.63815f, (byte) 77);
		spawn(245716, 508.98663f, 527.061f, 339.63126f, (byte) 76);
		spawn(245716, 502.5523f, 649.1053f, 317.08282f, (byte) 109);
		spawn(245716, 522.4196f, 650.3398f, 317.08282f, (byte) 99);
		spawn(245716, 540.84265f, 652.0684f, 316.16275f, (byte) 100);
		spawn(245716, 540.5331f, 637.4856f, 316.17746f, (byte) 17);
		spawn(245716, 514.6148f, 641.9673f, 317.10098f, (byte) 33);
		spawn(245716, 539.03076f, 536.819f, 322.0f, (byte) 30);
	}

	/*** Tiamat Ranger ***/
	private void IDTransformTiamatRa69An() {
		spawn(245717, 543.0933f, 557.8653f, 322.0f, (byte) 70);
		spawn(245717, 550.4571f, 546.09937f, 322.0f, (byte) 61);
		spawn(245717, 550.2217f, 539.29755f, 322.0f, (byte) 59);
		spawn(245717, 543.0761f, 534.39087f, 322.0f, (byte) 43);
		spawn(245717, 534.18945f, 534.63385f, 321.875f, (byte) 21);
		spawn(245717, 522.56396f, 467.8957f, 322.0f, (byte) 37);
		spawn(245717, 527.70734f, 475.95486f, 322.0f, (byte) 7);
		spawn(245717, 509.6404f, 476.19836f, 322.0f, (byte) 118);
		spawn(245717, 501.61966f, 467.60513f, 322.0f, (byte) 5);
		spawn(245717, 503.2415f, 446.9365f, 321.8276f, (byte) 18);
		spawn(245717, 542.8608f, 472.11404f, 322.0f, (byte) 51);
		spawn(245717, 539.9602f, 467.72766f, 322.0f, (byte) 50);
		spawn(245717, 516.02167f, 453.723f, 321.875f, (byte) 60);
		spawn(245717, 466.30823f, 509.6083f, 342.1901f, (byte) 0);
		spawn(245717, 466.1356f, 516.46765f, 342.1901f, (byte) 0);
		spawn(245717, 468.7195f, 513.0958f, 342.1901f, (byte) 0);
		spawn(245717, 446.91077f, 509.62943f, 342.2713f, (byte) 1);
		spawn(245717, 446.86057f, 517.2843f, 342.27173f, (byte) 0);
		spawn(245717, 449.71277f, 513.23883f, 342.24753f, (byte) 0);
		spawn(245717, 427.54434f, 509.3018f, 342.43564f, (byte) 0);
		spawn(245717, 427.4629f, 517.19116f, 342.43634f, (byte) 0);
		spawn(245717, 430.6331f, 513.1085f, 342.40942f, (byte) 0);
		spawn(245717, 483.40283f, 475.9793f, 345.70047f, (byte) 81);
		spawn(245717, 469.33865f, 454.01733f, 345.70047f, (byte) 4);
		spawn(245717, 482.78824f, 526.4342f, 342.234f, (byte) 78);
		spawn(245717, 502.18372f, 508.00098f, 339.63123f, (byte) 39);
		spawn(245717, 497.84283f, 521.77655f, 339.63815f, (byte) 77);
		spawn(245717, 508.98663f, 527.061f, 339.63126f, (byte) 76);
		spawn(245717, 502.5523f, 649.1053f, 317.08282f, (byte) 109);
		spawn(245717, 522.4196f, 650.3398f, 317.08282f, (byte) 99);
		spawn(245717, 540.84265f, 652.0684f, 316.16275f, (byte) 100);
		spawn(245717, 540.5331f, 637.4856f, 316.17746f, (byte) 17);
		spawn(245717, 514.6148f, 641.9673f, 317.10098f, (byte) 33);
		spawn(245717, 539.03076f, 536.819f, 322.0f, (byte) 30);
	}

	/*** Player Lvl 70 ***/
	/*** Eresh High ***/
	private void IDTransformEreshHigh70An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformEreshHighFi70An();
				break;
			case 2:
				IDTransformEreshHighAs70An();
				break;
			case 3:
				IDTransformEreshHighWi70An();
				break;
			case 4:
				IDTransformEreshHighPr70An();
				break;
		}
	}

	private void IDTransformEreshHighFi70An() {
		spawn(244634, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244634, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244634, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244634, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244634, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244634, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244634, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244634, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244634, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244634, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244634, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244634, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244634, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244634, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244634, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244634, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244634, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformEreshHighAs70An() {
		spawn(244635, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244635, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244635, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244635, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244635, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244635, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244635, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244635, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244635, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244635, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244635, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244635, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244635, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244635, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244635, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244635, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244635, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformEreshHighWi70An() {
		spawn(244636, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244636, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244636, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244636, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244636, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244636, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244636, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244636, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244636, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244636, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244636, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244636, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244636, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244636, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244636, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244636, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244636, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformEreshHighPr70An() {
		spawn(244637, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244637, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244637, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244637, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244637, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244637, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244637, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244637, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244637, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244637, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244637, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244637, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244637, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244637, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244637, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244637, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244637, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	/*** Vritra High ***/
	private void IDTransformVritraHigh70An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformVritraHighFi70An();
				break;
			case 2:
				IDTransformVritraHighAs70An();
				break;
			case 3:
				IDTransformVritraHighWi70An();
				break;
			case 4:
				IDTransformVritraHighPr70An();
				break;
		}
	}

	private void IDTransformVritraHighFi70An() {
		spawn(244638, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244638, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244638, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244638, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244638, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244638, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244638, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244638, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244638, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244638, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244638, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244638, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244638, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244638, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244638, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244638, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244638, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformVritraHighAs70An() {
		spawn(244639, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244639, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244639, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244639, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244639, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244639, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244639, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244639, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244639, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244639, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244639, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244639, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244639, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244639, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244639, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244639, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244639, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformVritraHighWi70An() {
		spawn(244640, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244640, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244640, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244640, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244640, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244640, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244640, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244640, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244640, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244640, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244640, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244640, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244640, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244640, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244640, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244640, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244640, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformVritraHighPr70An() {
		spawn(244641, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244641, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244641, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244641, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244641, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244641, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244641, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244641, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244641, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244641, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244641, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244641, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244641, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244641, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244641, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244641, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244641, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	/*** Tiamat High ***/
	private void IDTransformTiamatHigh70An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformTiamatHighFi70An();
				break;
			case 2:
				IDTransformTiamatHighAs70An();
				break;
			case 3:
				IDTransformTiamatHighWi70An();
				break;
			case 4:
				IDTransformTiamatHighPr70An();
				break;
		}
	}

	private void IDTransformTiamatHighFi70An() {
		spawn(244642, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244642, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244642, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244642, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244642, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244642, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244642, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244642, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244642, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244642, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244642, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244642, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244642, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244642, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244642, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244642, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244642, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformTiamatHighAs70An() {
		spawn(244643, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244643, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244643, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244643, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244643, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244643, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244643, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244643, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244643, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244643, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244643, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244643, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244643, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244643, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244643, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244643, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244643, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformTiamatHighWi70An() {
		spawn(244644, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244644, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244644, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244644, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244644, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244644, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244644, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244644, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244644, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244644, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244644, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244644, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244644, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244644, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244644, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244644, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244644, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformTiamatHighPr70An() {
		spawn(244645, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244645, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244645, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244645, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244645, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244645, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244645, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244645, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244645, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244645, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244645, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244645, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244645, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244645, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244645, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244645, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244645, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	/*** Eresh Guard ***/
	private void IDTransformEreshGuard70An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformEreshGuardFi70An();
				break;
			case 2:
				IDTransformEreshGuardAs70An();
				break;
			case 3:
				IDTransformEreshGuardWi70An();
				break;
			case 4:
				IDTransformEreshGuardPr70An();
				break;
		}
	}

	private void IDTransformEreshGuardFi70An() {
		spawn(244622, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244622, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244622, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244622, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244622, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244622, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244622, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244622, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244622, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244622, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244622, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244622, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244622, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244622, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244622, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244622, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244622, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244622, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244622, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244622, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244622, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244622, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244622, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244622, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244622, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244622, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244622, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244622, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244622, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244622, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244622, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244622, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244622, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244622, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244622, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244622, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244622, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244622, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244622, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244622, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244622, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformEreshGuardAs70An() {
		spawn(244623, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244623, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244623, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244623, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244623, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244623, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244623, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244623, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244623, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244623, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244623, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244623, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244623, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244623, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244623, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244623, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244623, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244623, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244623, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244623, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244623, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244623, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244623, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244623, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244623, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244623, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244623, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244623, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244623, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244623, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244623, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244623, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244623, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244623, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244623, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244623, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244623, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244623, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244623, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244623, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244623, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformEreshGuardWi70An() {
		spawn(244624, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244624, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244624, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244624, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244624, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244624, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244624, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244624, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244624, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244624, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244624, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244624, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244624, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244624, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244624, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244624, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244624, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244624, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244624, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244624, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244624, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244624, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244624, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244624, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244624, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244624, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244624, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244624, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244624, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244624, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244624, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244624, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244624, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244624, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244624, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244624, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244624, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244624, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244624, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244624, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244624, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformEreshGuardPr70An() {
		spawn(244625, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244625, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244625, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244625, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244625, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244625, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244625, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244625, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244625, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244625, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244625, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244625, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244625, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244625, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244625, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244625, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244625, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244625, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244625, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244625, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244625, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244625, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244625, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244625, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244625, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244625, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244625, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244625, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244625, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244625, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244625, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244625, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244625, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244625, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244625, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244625, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244625, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244625, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244625, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244625, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244625, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	/*** Vritra Guard ***/
	private void IDTransformVritraGuard70An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformVritraGuardFi70An();
				break;
			case 2:
				IDTransformVritraGuardAs70An();
				break;
			case 3:
				IDTransformVritraGuardWi70An();
				break;
			case 4:
				IDTransformVritraGuardPr70An();
				break;
		}
	}

	private void IDTransformVritraGuardFi70An() {
		spawn(244626, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244626, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244626, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244626, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244626, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244626, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244626, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244626, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244626, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244626, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244626, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244626, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244626, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244626, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244626, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244626, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244626, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244626, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244626, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244626, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244626, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244626, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244626, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244626, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244626, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244626, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244626, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244626, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244626, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244626, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244626, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244626, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244626, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244626, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244626, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244626, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244626, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244626, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244626, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244626, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244626, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformVritraGuardAs70An() {
		spawn(244627, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244627, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244627, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244627, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244627, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244627, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244627, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244627, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244627, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244627, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244627, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244627, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244627, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244627, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244627, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244627, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244627, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244627, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244627, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244627, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244627, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244627, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244627, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244627, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244627, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244627, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244627, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244627, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244627, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244627, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244627, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244627, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244627, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244627, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244627, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244627, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244627, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244627, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244627, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244627, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244627, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformVritraGuardWi70An() {
		spawn(244628, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244628, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244628, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244628, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244628, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244628, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244628, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244628, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244628, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244628, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244628, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244628, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244628, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244628, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244628, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244628, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244628, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244628, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244628, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244628, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244628, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244628, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244628, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244628, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244628, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244628, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244628, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244628, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244628, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244628, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244628, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244628, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244628, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244628, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244628, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244628, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244628, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244628, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244628, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244628, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244628, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformVritraGuardPr70An() {
		spawn(244629, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244629, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244629, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244629, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244629, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244629, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244629, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244629, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244629, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244629, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244629, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244629, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244629, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244629, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244629, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244629, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244629, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244629, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244629, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244629, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244629, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244629, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244629, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244629, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244629, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244629, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244629, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244629, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244629, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244629, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244629, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244629, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244629, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244629, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244629, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244629, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244629, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244629, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244629, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244629, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244629, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	/*** Tiamat Guard ***/
	private void IDTransformTiamatGuard70An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformTiamatGuardFi70An();
				break;
			case 2:
				IDTransformTiamatGuardAs70An();
				break;
			case 3:
				IDTransformTiamatGuardWi70An();
				break;
			case 4:
				IDTransformTiamatGuardPr70An();
				break;
		}
	}

	private void IDTransformTiamatGuardFi70An() {
		spawn(244630, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244630, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244630, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244630, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244630, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244630, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244630, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244630, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244630, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244630, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244630, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244630, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244630, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244630, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244630, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244630, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244630, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244630, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244630, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244630, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244630, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244630, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244630, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244630, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244630, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244630, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244630, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244630, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244630, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244630, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244630, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244630, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244630, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244630, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244630, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244630, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244630, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244630, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244630, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244630, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244630, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformTiamatGuardAs70An() {
		spawn(244631, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244631, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244631, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244631, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244631, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244631, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244631, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244631, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244631, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244631, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244631, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244631, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244631, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244631, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244631, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244631, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244631, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244631, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244631, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244631, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244631, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244631, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244631, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244631, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244631, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244631, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244631, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244631, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244631, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244631, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244631, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244631, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244631, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244631, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244631, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244631, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244631, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244631, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244631, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244631, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244631, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformTiamatGuardWi70An() {
		spawn(244632, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244632, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244632, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244632, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244632, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244632, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244632, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244632, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244632, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244632, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244632, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244632, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244632, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244632, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244632, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244632, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244632, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244632, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244632, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244632, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244632, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244632, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244632, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244632, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244632, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244632, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244632, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244632, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244632, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244632, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244632, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244632, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244632, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244632, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244632, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244632, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244632, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244632, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244632, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244632, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244632, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformTiamatGuardPr70An() {
		spawn(244633, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244633, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244633, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244633, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244633, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244633, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244633, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244633, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244633, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244633, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244633, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244633, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244633, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244633, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244633, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244633, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244633, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244633, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244633, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244633, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244633, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244633, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244633, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244633, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244633, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244633, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244633, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244633, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244633, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244633, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244633, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244633, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244633, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244633, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244633, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244633, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244633, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244633, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244633, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244633, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244633, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	/*** Deva Guard ***/
	private void IDTransformDevaGuardFi70An() {
		spawn(244649, 543.9115f, 491.4599f, 322.04422f, (byte) 91);
		spawn(244649, 547.9682f, 490.34494f, 321.50793f, (byte) 90);
		spawn(244649, 534.53534f, 485.35455f, 322.0f, (byte) 89);
		spawn(244649, 541.57935f, 487.14307f, 322.0f, (byte) 89);
		spawn(244649, 516.4117f, 585.5329f, 321.97427f, (byte) 90);
		spawn(244649, 499.82422f, 585.81537f, 322.02252f, (byte) 92);
		spawn(244649, 501.61078f, 555.0141f, 321.84106f, (byte) 12);
	}

	private void IDTransformDevaGuardAs70An() {
		spawn(244650, 855.63855f, 484.74774f, 349.08722f, (byte) 45);
		spawn(244650, 852.21844f, 482.80756f, 349.41986f, (byte) 44);
		spawn(244650, 864.0118f, 492.74384f, 349.56552f, (byte) 46);
		spawn(244650, 508.17993f, 587.8108f, 322.56973f, (byte) 90);
		spawn(244650, 526.8684f, 493.7475f, 322.0f, (byte) 91);
		spawn(244650, 537.37225f, 508.5786f, 322.0f, (byte) 90);
		spawn(244650, 512.8187f, 590.4575f, 322.56216f, (byte) 90);
		spawn(244650, 504.50745f, 590.3867f, 322.56216f, (byte) 90);
		spawn(244650, 482.3006f, 496.94232f, 342.22174f, (byte) 27);
		spawn(244650, 594.4832f, 673.60126f, 350.85538f, (byte) 54);
		spawn(244650, 634.298f, 511.2995f, 339.61545f, (byte) 61);
		spawn(244650, 530.982f, 493.62543f, 322.0f, (byte) 90);
		spawn(244650, 641.9954f, 514.2143f, 339.61542f, (byte) 61);
		spawn(244650, 505.45636f, 584.90186f, 322.0f, (byte) 90);
		spawn(244650, 510.66394f, 584.80176f, 322.0f, (byte) 90);
		spawn(244650, 859.2291f, 490.53555f, 348.98047f, (byte) 45);
		spawn(244650, 604.1567f, 635.69995f, 352.53815f, (byte) 30);
		spawn(244650, 467.0f, 476.0f, 345.70047f, (byte) 32);
		spawn(244650, 536.2914f, 488.65588f, 322.0f, (byte) 90);
		spawn(244650, 639.6124f, 525.6597f, 339.61542f, (byte) 62);
		spawn(244650, 885.47394f, 464.9668f, 351.0f, (byte) 66);
		spawn(244650, 642.88293f, 520.17224f, 339.61542f, (byte) 60);
		spawn(244650, 546.91925f, 510.66574f, 321.94254f, (byte) 91);
		spawn(244650, 527.7633f, 502.87167f, 321.6398f, (byte) 91);
		spawn(244650, 885.5619f, 456.29126f, 351.02737f, (byte) 55);
	}

	private void IDTransformDevaGuardWi70An() {
		spawn(244651, 513.0236f, 593.86285f, 322.56216f, (byte) 90);
		spawn(244651, 501.3256f, 593.69885f, 322.56216f, (byte) 90);
		spawn(244651, 633.75323f, 516.50934f, 339.61545f, (byte) 61);
		spawn(244651, 544.93945f, 499.1886f, 322.0f, (byte) 91);
		spawn(244651, 530.8947f, 502.38684f, 321.86185f, (byte) 91);
		spawn(244651, 634.5065f, 522.0903f, 339.61545f, (byte) 53);
		spawn(244651, 885.382f, 460.77805f, 351.0f, (byte) 60);
		spawn(244651, 612.8048f, 643.80194f, 352.46414f, (byte) 30);
		spawn(244651, 538.00024f, 495.57394f, 322.0f, (byte) 90);
		spawn(244651, 526.9178f, 486.92227f, 321.87527f, (byte) 85);
	}

	private void IDTransformDevaGuardRa70An() {
		spawn(244652, 529.87f, 545.5564f, 321.85876f, (byte) 95);
		spawn(244652, 526.1612f, 545.1311f, 321.58078f, (byte) 88);
		spawn(244652, 546.40234f, 523.6142f, 321.97485f, (byte) 87);
		spawn(244652, 549.9647f, 524.9884f, 321.9978f, (byte) 95);
		spawn(244652, 520.9293f, 586.19006f, 321.94193f, (byte) 95);
		spawn(244652, 495.259f, 586.9457f, 322.0659f, (byte) 81);
		spawn(244652, 505.0709f, 552.7453f, 322.0f, (byte) 28);
	}

	/*** Eresh Stumble ***/
	private void IDTransformEreshStumble70An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformEreshStumbleFi70An();
				break;
			case 2:
				IDTransformEreshStumbleAs70An();
				break;
			case 3:
				IDTransformEreshStumbleWi70An();
				break;
			case 4:
				IDTransformEreshStumblePr70An();
				break;
		}
	}

	private void IDTransformEreshStumbleFi70An() {
		spawn(244988, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(244988, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(244988, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformEreshStumbleAs70An() {
		spawn(244989, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(244989, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(244989, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformEreshStumbleWi70An() {
		spawn(244990, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(244990, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(244990, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformEreshStumblePr70An() {
		spawn(244991, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(244991, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(244991, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	/*** Vritra Stumble ***/
	private void IDTransformVritraStumble70An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformVritraStumbleFi70An();
				break;
			case 2:
				IDTransformVritraStumbleAs70An();
				break;
			case 3:
				IDTransformVritraStumbleWi70An();
				break;
			case 4:
				IDTransformVritraStumblePr70An();
				break;
		}
	}

	private void IDTransformVritraStumbleFi70An() {
		spawn(244992, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(244992, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(244992, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformVritraStumbleAs70An() {
		spawn(244993, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(244993, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(244993, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformVritraStumbleWi70An() {
		spawn(244994, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(244994, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(244994, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformVritraStumblePr70An() {
		spawn(244995, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(244995, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(244995, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	/*** Tiamat Stumble ***/
	private void IDTransformTiamatStumble70An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformTiamatStumbleFi70An();
				break;
			case 2:
				IDTransformTiamatStumbleAs70An();
				break;
			case 3:
				IDTransformTiamatStumbleWi70An();
				break;
			case 4:
				IDTransformTiamatStumblePr70An();
				break;
		}
	}

	private void IDTransformTiamatStumbleFi70An() {
		spawn(244996, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(244996, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(244996, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformTiamatStumbleAs70An() {
		spawn(244997, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(244997, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(244997, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformTiamatStumbleWi70An() {
		spawn(244998, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(244998, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(244998, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformTiamatStumblePr70An() {
		spawn(244999, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(244999, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(244999, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	/*** Deva Stumble ***/
	private void IDTransformDevaStumble70An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformDevaStumbleFi70An();
				break;
			case 2:
				IDTransformDevaStumbleAs70An();
				break;
			case 3:
				IDTransformDevaStumbleWi70An();
				break;
			case 4:
				IDTransformDevaStumblePr70An();
				break;
		}
	}

	private void IDTransformDevaStumbleFi70An() {
		spawn(245000, 677.0f, 516.0f, 338.24844f, (byte) 43);
		spawn(245000, 505.28098f, 648.23944f, 317.08282f, (byte) 113);
		spawn(245000, 507.24606f, 628.40027f, 318.4649f, (byte) 61);
		spawn(245000, 504.39532f, 615.5569f, 320.3689f, (byte) 61);
		spawn(245000, 504.10117f, 601.42255f, 322.4643f, (byte) 60);
		spawn(245000, 513.7057f, 643.4998f, 317.10632f, (byte) 11);
		spawn(245000, 511.02466f, 614.0008f, 320.5996f, (byte) 0);
		spawn(245000, 503.81223f, 640.00806f, 317.11395f, (byte) 40);
		spawn(245000, 531.7686f, 642.5042f, 317.08282f, (byte) 112);
		spawn(245000, 522.0014f, 648.79095f, 317.08282f, (byte) 55);
	}

	private void IDTransformDevaStumbleAs70An() {
		spawn(245001, 677.0f, 516.0f, 338.24844f, (byte) 43);
		spawn(245001, 505.28098f, 648.23944f, 317.08282f, (byte) 113);
		spawn(245001, 507.24606f, 628.40027f, 318.4649f, (byte) 61);
		spawn(245001, 504.39532f, 615.5569f, 320.3689f, (byte) 61);
		spawn(245001, 504.10117f, 601.42255f, 322.4643f, (byte) 60);
		spawn(245001, 513.7057f, 643.4998f, 317.10632f, (byte) 11);
		spawn(245001, 511.02466f, 614.0008f, 320.5996f, (byte) 0);
		spawn(245001, 503.81223f, 640.00806f, 317.11395f, (byte) 40);
		spawn(245001, 531.7686f, 642.5042f, 317.08282f, (byte) 112);
		spawn(245001, 522.0014f, 648.79095f, 317.08282f, (byte) 55);
	}

	private void IDTransformDevaStumbleWi70An() {
		spawn(245002, 677.0f, 516.0f, 338.24844f, (byte) 43);
		spawn(245002, 505.28098f, 648.23944f, 317.08282f, (byte) 113);
		spawn(245002, 507.24606f, 628.40027f, 318.4649f, (byte) 61);
		spawn(245002, 504.39532f, 615.5569f, 320.3689f, (byte) 61);
		spawn(245002, 504.10117f, 601.42255f, 322.4643f, (byte) 60);
		spawn(245002, 513.7057f, 643.4998f, 317.10632f, (byte) 11);
		spawn(245002, 511.02466f, 614.0008f, 320.5996f, (byte) 0);
		spawn(245002, 503.81223f, 640.00806f, 317.11395f, (byte) 40);
		spawn(245002, 531.7686f, 642.5042f, 317.08282f, (byte) 112);
		spawn(245002, 522.0014f, 648.79095f, 317.08282f, (byte) 55);
	}

	private void IDTransformDevaStumblePr70An() {
		spawn(245003, 677.0f, 516.0f, 338.24844f, (byte) 43);
		spawn(245003, 505.28098f, 648.23944f, 317.08282f, (byte) 113);
		spawn(245003, 507.24606f, 628.40027f, 318.4649f, (byte) 61);
		spawn(245003, 504.39532f, 615.5569f, 320.3689f, (byte) 61);
		spawn(245003, 504.10117f, 601.42255f, 322.4643f, (byte) 60);
		spawn(245003, 513.7057f, 643.4998f, 317.10632f, (byte) 11);
		spawn(245003, 511.02466f, 614.0008f, 320.5996f, (byte) 0);
		spawn(245003, 503.81223f, 640.00806f, 317.11395f, (byte) 40);
		spawn(245003, 531.7686f, 642.5042f, 317.08282f, (byte) 112);
		spawn(245003, 522.0014f, 648.79095f, 317.08282f, (byte) 55);
	}

	/*** Drop Dragon ***/
	private void IDTransformDropDragon70Ae() {
		switch (Rnd.get(1, 3)) {
			case 1:
				spawn(245016, 622.6814f, 551.9144f, 346.06897f, (byte) 105);
				break;
			case 2:
				spawn(245017, 622.6814f, 551.9144f, 346.06897f, (byte) 105);
				break;
			case 3:
				spawn(245018, 622.6814f, 551.9144f, 346.06897f, (byte) 105);
				break;
		}
		switch (Rnd.get(1, 3)) {
			case 1:
				spawn(245016, 607.8992f, 674.06934f, 352.29062f, (byte) 90);
				break;
			case 2:
				spawn(245017, 607.8992f, 674.06934f, 352.29062f, (byte) 90);
				break;
			case 3:
				spawn(245018, 607.8992f, 674.06934f, 352.29062f, (byte) 90);
				break;
		}
	}

	/*** Bonus Monster ***/
	private void IDTransformBonusMonsterTypeA70An() {
		spawn(246216, 761.54095f, 562.17f, 341.0512f, (byte) 90);
		spawn(246216, 476.94467f, 549.22363f, 345.6048f, (byte) 90);
		spawn(246216, 609.2811f, 707.6352f, 355.10846f, (byte) 93);
	}

	private void IDTransformBonusMonsterTypeB70An() {
		spawn(246217, 761.54095f, 562.17f, 341.0512f, (byte) 90);
		spawn(246217, 476.94467f, 549.22363f, 345.6048f, (byte) 90);
		spawn(246217, 609.2811f, 707.6352f, 355.10846f, (byte) 93);
	}

	private void IDTransformBonusMonsterTypeC70An() {
		spawn(246218, 761.54095f, 562.17f, 341.0512f, (byte) 90);
		spawn(246218, 476.94467f, 549.22363f, 345.6048f, (byte) 90);
		spawn(246218, 609.2811f, 707.6352f, 355.10846f, (byte) 93);
	}

	private void IDTransformBonusMonsterTypeD70An() {
		spawn(246219, 761.54095f, 562.17f, 341.0512f, (byte) 90);
		spawn(246219, 476.94467f, 549.22363f, 345.6048f, (byte) 90);
		spawn(246219, 609.2811f, 707.6352f, 355.10846f, (byte) 93);
	}

	/*** Eresh Warp ***/
	private void IDTransformEreshWarp70() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformEreshWarpFi70An();
				break;
			case 2:
				IDTransformEreshWarpAs70An();
				break;
			case 3:
				IDTransformEreshWarpWi70An();
				break;
			case 4:
				IDTransformEreshWarpPr70An();
				break;
		}
	}

	private void IDTransformEreshWarpFi70An() {
		sp(245625, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245625, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245625, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245625, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245625, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245625, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245625, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245625, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245625, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245625, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245625, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245625, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245625, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245625, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245625, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245625, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245625, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245625, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245625, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformEreshWarpAs70An() {
		sp(245626, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245626, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245626, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245626, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245626, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245626, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245626, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245626, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245626, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245626, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245626, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245626, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245626, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245626, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245626, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245626, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245626, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245626, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245626, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformEreshWarpWi70An() {
		sp(245627, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245627, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245627, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245627, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245627, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245627, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245627, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245627, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245627, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245627, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245627, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245627, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245627, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245627, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245627, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245627, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245627, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245627, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245627, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformEreshWarpPr70An() {
		sp(245628, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245628, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245628, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245628, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245628, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245628, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245628, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245628, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245628, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245628, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245628, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245628, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245628, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245628, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245628, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245628, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245628, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245628, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245628, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	/*** Vritra Warp ***/
	private void IDTransformVritraWarp70() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformVritraWarpFi70An();
				break;
			case 2:
				IDTransformVritraWarpAs70An();
				break;
			case 3:
				IDTransformVritraWarpWi70An();
				break;
			case 4:
				IDTransformVritraWarpPr70An();
				break;
		}
	}

	private void IDTransformVritraWarpFi70An() {
		sp(245629, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245629, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245629, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245629, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245629, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245629, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245629, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245629, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245629, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245629, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245629, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245629, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245629, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245629, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245629, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245629, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245629, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245629, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245629, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformVritraWarpAs70An() {
		sp(245630, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245630, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245630, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245630, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245630, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245630, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245630, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245630, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245630, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245630, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245630, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245630, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245630, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245630, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245630, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245630, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245630, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245630, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245630, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformVritraWarpWi70An() {
		sp(245631, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245631, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245631, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245631, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245631, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245631, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245631, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245631, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245631, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245631, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245631, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245631, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245631, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245631, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245631, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245631, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245631, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245631, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245631, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformVritraWarpPr70An() {
		sp(245632, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245632, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245632, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245632, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245632, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245632, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245632, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245632, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245632, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245632, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245632, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245632, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245632, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245632, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245632, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245632, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245632, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245632, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245632, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	/*** Tiamat Warp ***/
	private void IDTransformTiamatWarp70() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformTiamatWarpFi70An();
				break;
			case 2:
				IDTransformTiamatWarpAs70An();
				break;
			case 3:
				IDTransformTiamatWarpWi70An();
				break;
			case 4:
				IDTransformTiamatWarpPr70An();
				break;
		}
	}

	private void IDTransformTiamatWarpFi70An() {
		sp(245633, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245633, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245633, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245633, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245633, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245633, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245633, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245633, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245633, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245633, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245633, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245633, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245633, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245633, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245633, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245633, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245633, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245633, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245633, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformTiamatWarpAs70An() {
		sp(245634, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245634, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245634, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245634, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245634, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245634, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245634, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245634, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245634, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245634, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245634, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245634, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245634, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245634, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245634, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245634, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245634, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245634, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245634, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformTiamatWarpWi70An() {
		sp(245635, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245635, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245635, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245635, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245635, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245635, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245635, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245635, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245635, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245635, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245635, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245635, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245635, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245635, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245635, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245635, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245635, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245635, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245635, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformTiamatWarpPr70An() {
		sp(245636, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245636, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245636, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245636, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245636, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245636, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245636, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245636, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245636, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245636, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245636, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245636, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245636, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245636, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245636, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245636, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245636, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245636, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245636, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	/*** Ranger ***/
	private void IDTransformRa70An() {
		switch (Rnd.get(1, 3)) {
			case 1:
				IDTransformEreshRa70An();
				break;
			case 2:
				IDTransformVritraRa70An();
				break;
			case 3:
				IDTransformTiamatRa70An();
				break;
		}
	}

	/*** Eresh Ranger ***/
	private void IDTransformEreshRa70An() {
		spawn(245721, 543.0933f, 557.8653f, 322.0f, (byte) 70);
		spawn(245721, 550.4571f, 546.09937f, 322.0f, (byte) 61);
		spawn(245721, 550.2217f, 539.29755f, 322.0f, (byte) 59);
		spawn(245721, 543.0761f, 534.39087f, 322.0f, (byte) 43);
		spawn(245721, 534.18945f, 534.63385f, 321.875f, (byte) 21);
		spawn(245721, 522.56396f, 467.8957f, 322.0f, (byte) 37);
		spawn(245721, 527.70734f, 475.95486f, 322.0f, (byte) 7);
		spawn(245721, 509.6404f, 476.19836f, 322.0f, (byte) 118);
		spawn(245721, 501.61966f, 467.60513f, 322.0f, (byte) 5);
		spawn(245721, 503.2415f, 446.9365f, 321.8276f, (byte) 18);
		spawn(245721, 542.8608f, 472.11404f, 322.0f, (byte) 51);
		spawn(245721, 539.9602f, 467.72766f, 322.0f, (byte) 50);
		spawn(245721, 516.02167f, 453.723f, 321.875f, (byte) 60);
		spawn(245721, 466.30823f, 509.6083f, 342.1901f, (byte) 0);
		spawn(245721, 466.1356f, 516.46765f, 342.1901f, (byte) 0);
		spawn(245721, 468.7195f, 513.0958f, 342.1901f, (byte) 0);
		spawn(245721, 446.91077f, 509.62943f, 342.2713f, (byte) 1);
		spawn(245721, 446.86057f, 517.2843f, 342.27173f, (byte) 0);
		spawn(245721, 449.71277f, 513.23883f, 342.24753f, (byte) 0);
		spawn(245721, 427.54434f, 509.3018f, 342.43564f, (byte) 0);
		spawn(245721, 427.4629f, 517.19116f, 342.43634f, (byte) 0);
		spawn(245721, 430.6331f, 513.1085f, 342.40942f, (byte) 0);
		spawn(245721, 483.40283f, 475.9793f, 345.70047f, (byte) 81);
		spawn(245721, 469.33865f, 454.01733f, 345.70047f, (byte) 4);
		spawn(245721, 482.78824f, 526.4342f, 342.234f, (byte) 78);
		spawn(245721, 502.18372f, 508.00098f, 339.63123f, (byte) 39);
		spawn(245721, 497.84283f, 521.77655f, 339.63815f, (byte) 77);
		spawn(245721, 508.98663f, 527.061f, 339.63126f, (byte) 76);
		spawn(245721, 502.5523f, 649.1053f, 317.08282f, (byte) 109);
		spawn(245721, 522.4196f, 650.3398f, 317.08282f, (byte) 99);
		spawn(245721, 540.84265f, 652.0684f, 316.16275f, (byte) 100);
		spawn(245721, 540.5331f, 637.4856f, 316.17746f, (byte) 17);
		spawn(245721, 514.6148f, 641.9673f, 317.10098f, (byte) 33);
		spawn(245721, 539.03076f, 536.819f, 322.0f, (byte) 30);
	}

	/*** Vritra Ranger ***/
	private void IDTransformVritraRa70An() {
		spawn(245722, 543.0933f, 557.8653f, 322.0f, (byte) 70);
		spawn(245722, 550.4571f, 546.09937f, 322.0f, (byte) 61);
		spawn(245722, 550.2217f, 539.29755f, 322.0f, (byte) 59);
		spawn(245722, 543.0761f, 534.39087f, 322.0f, (byte) 43);
		spawn(245722, 534.18945f, 534.63385f, 321.875f, (byte) 21);
		spawn(245722, 522.56396f, 467.8957f, 322.0f, (byte) 37);
		spawn(245722, 527.70734f, 475.95486f, 322.0f, (byte) 7);
		spawn(245722, 509.6404f, 476.19836f, 322.0f, (byte) 118);
		spawn(245722, 501.61966f, 467.60513f, 322.0f, (byte) 5);
		spawn(245722, 503.2415f, 446.9365f, 321.8276f, (byte) 18);
		spawn(245722, 542.8608f, 472.11404f, 322.0f, (byte) 51);
		spawn(245722, 539.9602f, 467.72766f, 322.0f, (byte) 50);
		spawn(245722, 516.02167f, 453.723f, 321.875f, (byte) 60);
		spawn(245722, 466.30823f, 509.6083f, 342.1901f, (byte) 0);
		spawn(245722, 466.1356f, 516.46765f, 342.1901f, (byte) 0);
		spawn(245722, 468.7195f, 513.0958f, 342.1901f, (byte) 0);
		spawn(245722, 446.91077f, 509.62943f, 342.2713f, (byte) 1);
		spawn(245722, 446.86057f, 517.2843f, 342.27173f, (byte) 0);
		spawn(245722, 449.71277f, 513.23883f, 342.24753f, (byte) 0);
		spawn(245722, 427.54434f, 509.3018f, 342.43564f, (byte) 0);
		spawn(245722, 427.4629f, 517.19116f, 342.43634f, (byte) 0);
		spawn(245722, 430.6331f, 513.1085f, 342.40942f, (byte) 0);
		spawn(245722, 483.40283f, 475.9793f, 345.70047f, (byte) 81);
		spawn(245722, 469.33865f, 454.01733f, 345.70047f, (byte) 4);
		spawn(245722, 482.78824f, 526.4342f, 342.234f, (byte) 78);
		spawn(245722, 502.18372f, 508.00098f, 339.63123f, (byte) 39);
		spawn(245722, 497.84283f, 521.77655f, 339.63815f, (byte) 77);
		spawn(245722, 508.98663f, 527.061f, 339.63126f, (byte) 76);
		spawn(245722, 502.5523f, 649.1053f, 317.08282f, (byte) 109);
		spawn(245722, 522.4196f, 650.3398f, 317.08282f, (byte) 99);
		spawn(245722, 540.84265f, 652.0684f, 316.16275f, (byte) 100);
		spawn(245722, 540.5331f, 637.4856f, 316.17746f, (byte) 17);
		spawn(245722, 514.6148f, 641.9673f, 317.10098f, (byte) 33);
		spawn(245722, 539.03076f, 536.819f, 322.0f, (byte) 30);
	}

	/*** Tiamat Ranger ***/
	private void IDTransformTiamatRa70An() {
		spawn(245723, 543.0933f, 557.8653f, 322.0f, (byte) 70);
		spawn(245723, 550.4571f, 546.09937f, 322.0f, (byte) 61);
		spawn(245723, 550.2217f, 539.29755f, 322.0f, (byte) 59);
		spawn(245723, 543.0761f, 534.39087f, 322.0f, (byte) 43);
		spawn(245723, 534.18945f, 534.63385f, 321.875f, (byte) 21);
		spawn(245723, 522.56396f, 467.8957f, 322.0f, (byte) 37);
		spawn(245723, 527.70734f, 475.95486f, 322.0f, (byte) 7);
		spawn(245723, 509.6404f, 476.19836f, 322.0f, (byte) 118);
		spawn(245723, 501.61966f, 467.60513f, 322.0f, (byte) 5);
		spawn(245723, 503.2415f, 446.9365f, 321.8276f, (byte) 18);
		spawn(245723, 542.8608f, 472.11404f, 322.0f, (byte) 51);
		spawn(245723, 539.9602f, 467.72766f, 322.0f, (byte) 50);
		spawn(245723, 516.02167f, 453.723f, 321.875f, (byte) 60);
		spawn(245723, 466.30823f, 509.6083f, 342.1901f, (byte) 0);
		spawn(245723, 466.1356f, 516.46765f, 342.1901f, (byte) 0);
		spawn(245723, 468.7195f, 513.0958f, 342.1901f, (byte) 0);
		spawn(245723, 446.91077f, 509.62943f, 342.2713f, (byte) 1);
		spawn(245723, 446.86057f, 517.2843f, 342.27173f, (byte) 0);
		spawn(245723, 449.71277f, 513.23883f, 342.24753f, (byte) 0);
		spawn(245723, 427.54434f, 509.3018f, 342.43564f, (byte) 0);
		spawn(245723, 427.4629f, 517.19116f, 342.43634f, (byte) 0);
		spawn(245723, 430.6331f, 513.1085f, 342.40942f, (byte) 0);
		spawn(245723, 483.40283f, 475.9793f, 345.70047f, (byte) 81);
		spawn(245723, 469.33865f, 454.01733f, 345.70047f, (byte) 4);
		spawn(245723, 482.78824f, 526.4342f, 342.234f, (byte) 78);
		spawn(245723, 502.18372f, 508.00098f, 339.63123f, (byte) 39);
		spawn(245723, 497.84283f, 521.77655f, 339.63815f, (byte) 77);
		spawn(245723, 508.98663f, 527.061f, 339.63126f, (byte) 76);
		spawn(245723, 502.5523f, 649.1053f, 317.08282f, (byte) 109);
		spawn(245723, 522.4196f, 650.3398f, 317.08282f, (byte) 99);
		spawn(245723, 540.84265f, 652.0684f, 316.16275f, (byte) 100);
		spawn(245723, 540.5331f, 637.4856f, 316.17746f, (byte) 17);
		spawn(245723, 514.6148f, 641.9673f, 317.10098f, (byte) 33);
		spawn(245723, 539.03076f, 536.819f, 322.0f, (byte) 30);
	}

	/*** Player Lvl 71 ***/
	/*** Eresh High ***/
	private void IDTransformEreshHigh71An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformEreshHighFi71An();
				break;
			case 2:
				IDTransformEreshHighAs71An();
				break;
			case 3:
				IDTransformEreshHighWi71An();
				break;
			case 4:
				IDTransformEreshHighPr71An();
				break;
		}
	}

	private void IDTransformEreshHighFi71An() {
		spawn(244675, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244675, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244675, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244675, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244675, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244675, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244675, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244675, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244675, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244675, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244675, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244675, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244675, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244675, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244675, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244675, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244675, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformEreshHighAs71An() {
		spawn(244676, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244676, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244676, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244676, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244676, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244676, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244676, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244676, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244676, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244676, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244676, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244676, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244676, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244676, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244676, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244676, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244676, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformEreshHighWi71An() {
		spawn(244677, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244677, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244677, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244677, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244677, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244677, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244677, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244677, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244677, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244677, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244677, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244677, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244677, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244677, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244677, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244677, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244677, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformEreshHighPr71An() {
		spawn(244678, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244678, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244678, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244678, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244678, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244678, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244678, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244678, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244678, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244678, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244678, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244678, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244678, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244678, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244678, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244678, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244678, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	/*** Vritra High ***/
	private void IDTransformVritraHigh71An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformVritraHighFi71An();
				break;
			case 2:
				IDTransformVritraHighAs71An();
				break;
			case 3:
				IDTransformVritraHighWi71An();
				break;
			case 4:
				IDTransformVritraHighPr71An();
				break;
		}
	}

	private void IDTransformVritraHighFi71An() {
		spawn(244679, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244679, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244679, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244679, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244679, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244679, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244679, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244679, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244679, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244679, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244679, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244679, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244679, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244679, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244679, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244679, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244679, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformVritraHighAs71An() {
		spawn(244680, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244680, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244680, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244680, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244680, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244680, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244680, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244680, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244680, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244680, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244680, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244680, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244680, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244680, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244680, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244680, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244680, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformVritraHighWi71An() {
		spawn(244681, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244681, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244681, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244681, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244681, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244681, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244681, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244681, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244681, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244681, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244681, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244681, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244681, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244681, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244681, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244681, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244681, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformVritraHighPr71An() {
		spawn(244682, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244682, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244682, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244682, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244682, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244682, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244682, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244682, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244682, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244682, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244682, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244682, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244682, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244682, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244682, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244682, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244682, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	/*** Tiamat High ***/
	private void IDTransformTiamatHigh71An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformTiamatHighFi71An();
				break;
			case 2:
				IDTransformTiamatHighAs71An();
				break;
			case 3:
				IDTransformTiamatHighWi71An();
				break;
			case 4:
				IDTransformTiamatHighPr71An();
				break;
		}
	}

	private void IDTransformTiamatHighFi71An() {
		spawn(244683, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244683, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244683, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244683, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244683, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244683, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244683, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244683, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244683, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244683, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244683, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244683, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244683, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244683, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244683, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244683, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244683, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformTiamatHighAs71An() {
		spawn(244684, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244684, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244684, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244684, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244684, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244684, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244684, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244684, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244684, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244684, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244684, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244684, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244684, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244684, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244684, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244684, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244684, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformTiamatHighWi71An() {
		spawn(244685, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244685, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244685, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244685, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244685, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244685, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244685, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244685, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244685, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244685, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244685, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244685, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244685, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244685, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244685, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244685, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244685, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformTiamatHighPr71An() {
		spawn(244686, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244686, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244686, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244686, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244686, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244686, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244686, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244686, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244686, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244686, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244686, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244686, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244686, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244686, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244686, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244686, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244686, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	/*** Eresh Guard ***/
	private void IDTransformEreshGuard71An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformEreshGuardFi71An();
				break;
			case 2:
				IDTransformEreshGuardAs71An();
				break;
			case 3:
				IDTransformEreshGuardWi71An();
				break;
			case 4:
				IDTransformEreshGuardPr71An();
				break;
		}
	}

	private void IDTransformEreshGuardFi71An() {
		spawn(244663, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244663, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244663, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244663, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244663, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244663, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244663, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244663, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244663, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244663, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244663, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244663, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244663, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244663, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244663, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244663, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244663, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244663, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244663, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244663, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244663, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244663, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244663, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244663, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244663, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244663, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244663, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244663, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244663, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244663, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244663, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244663, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244663, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244663, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244663, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244663, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244663, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244663, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244663, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244663, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244663, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformEreshGuardAs71An() {
		spawn(244664, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244664, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244664, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244664, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244664, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244664, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244664, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244664, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244664, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244664, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244664, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244664, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244664, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244664, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244664, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244664, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244664, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244664, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244664, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244664, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244664, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244664, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244664, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244664, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244664, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244664, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244664, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244664, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244664, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244664, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244664, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244664, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244664, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244664, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244664, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244664, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244664, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244664, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244664, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244664, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244664, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformEreshGuardWi71An() {
		spawn(244665, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244665, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244665, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244665, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244665, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244665, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244665, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244665, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244665, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244665, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244665, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244665, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244665, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244665, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244665, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244665, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244665, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244665, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244665, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244665, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244665, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244665, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244665, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244665, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244665, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244665, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244665, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244665, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244665, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244665, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244665, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244665, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244665, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244665, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244665, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244665, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244665, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244665, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244665, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244665, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244665, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformEreshGuardPr71An() {
		spawn(244666, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244666, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244666, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244666, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244666, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244666, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244666, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244666, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244666, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244666, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244666, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244666, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244666, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244666, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244666, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244666, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244666, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244666, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244666, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244666, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244666, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244666, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244666, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244666, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244666, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244666, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244666, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244666, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244666, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244666, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244666, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244666, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244666, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244666, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244666, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244666, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244666, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244666, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244666, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244666, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244666, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	/*** Vritra Guard ***/
	private void IDTransformVritraGuard71An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformVritraGuardFi71An();
				break;
			case 2:
				IDTransformVritraGuardAs71An();
				break;
			case 3:
				IDTransformVritraGuardWi71An();
				break;
			case 4:
				IDTransformVritraGuardPr71An();
				break;
		}
	}

	private void IDTransformVritraGuardFi71An() {
		spawn(244667, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244667, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244667, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244667, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244667, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244667, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244667, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244667, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244667, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244667, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244667, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244667, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244667, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244667, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244667, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244667, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244667, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244667, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244667, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244667, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244667, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244667, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244667, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244667, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244667, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244667, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244667, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244667, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244667, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244667, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244667, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244667, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244667, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244667, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244667, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244667, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244667, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244667, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244667, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244667, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244667, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformVritraGuardAs71An() {
		spawn(244668, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244668, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244668, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244668, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244668, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244668, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244668, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244668, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244668, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244668, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244668, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244668, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244668, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244668, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244668, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244668, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244668, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244668, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244668, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244668, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244668, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244668, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244668, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244668, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244668, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244668, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244668, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244668, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244668, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244668, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244668, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244668, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244668, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244668, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244668, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244668, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244668, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244668, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244668, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244668, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244668, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformVritraGuardWi71An() {
		spawn(244669, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244669, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244669, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244669, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244669, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244669, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244669, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244669, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244669, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244669, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244669, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244669, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244669, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244669, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244669, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244669, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244669, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244669, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244669, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244669, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244669, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244669, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244669, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244669, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244669, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244669, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244669, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244669, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244669, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244669, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244669, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244669, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244669, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244669, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244669, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244669, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244669, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244669, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244669, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244669, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244669, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformVritraGuardPr71An() {
		spawn(244670, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244670, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244670, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244670, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244670, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244670, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244670, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244670, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244670, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244670, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244670, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244670, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244670, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244670, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244670, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244670, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244670, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244670, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244670, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244670, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244670, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244670, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244670, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244670, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244670, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244670, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244670, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244670, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244670, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244670, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244670, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244670, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244670, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244670, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244670, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244670, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244670, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244670, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244670, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244670, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244670, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	/*** Tiamat Guard ***/
	private void IDTransformTiamatGuard71An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformTiamatGuardFi71An();
				break;
			case 2:
				IDTransformTiamatGuardAs71An();
				break;
			case 3:
				IDTransformTiamatGuardWi71An();
				break;
			case 4:
				IDTransformTiamatGuardPr71An();
				break;
		}
	}

	private void IDTransformTiamatGuardFi71An() {
		spawn(244671, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244671, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244671, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244671, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244671, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244671, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244671, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244671, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244671, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244671, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244671, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244671, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244671, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244671, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244671, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244671, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244671, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244671, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244671, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244671, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244671, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244671, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244671, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244671, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244671, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244671, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244671, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244671, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244671, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244671, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244671, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244671, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244671, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244671, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244671, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244671, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244671, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244671, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244671, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244671, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244671, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformTiamatGuardAs71An() {
		spawn(244672, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244672, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244672, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244672, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244672, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244672, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244672, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244672, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244672, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244672, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244672, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244672, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244672, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244672, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244672, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244672, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244672, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244672, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244672, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244672, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244672, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244672, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244672, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244672, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244672, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244672, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244672, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244672, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244672, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244672, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244672, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244672, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244672, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244672, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244672, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244672, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244672, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244672, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244672, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244672, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244672, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformTiamatGuardWi71An() {
		spawn(244673, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244673, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244673, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244673, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244673, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244673, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244673, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244673, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244673, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244673, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244673, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244673, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244673, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244673, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244673, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244673, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244673, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244673, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244673, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244673, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244673, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244673, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244673, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244673, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244673, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244673, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244673, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244673, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244673, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244673, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244673, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244673, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244673, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244673, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244673, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244673, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244673, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244673, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244673, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244673, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244673, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformTiamatGuardPr71An() {
		spawn(244674, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244674, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244674, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244674, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244674, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244674, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244674, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244674, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244674, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244674, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244674, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244674, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244674, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244674, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244674, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244674, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244674, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244674, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244674, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244674, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244674, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244674, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244674, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244674, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244674, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244674, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244674, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244674, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244674, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244674, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244674, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244674, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244674, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244674, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244674, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244674, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244674, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244674, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244674, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244674, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244674, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	/*** Deva Guard ***/
	private void IDTransformDevaGuardFi71An() {
		spawn(244690, 543.9115f, 491.4599f, 322.04422f, (byte) 91);
		spawn(244690, 547.9682f, 490.34494f, 321.50793f, (byte) 90);
		spawn(244690, 534.53534f, 485.35455f, 322.0f, (byte) 89);
		spawn(244690, 541.57935f, 487.14307f, 322.0f, (byte) 89);
		spawn(244690, 516.4117f, 585.5329f, 321.97427f, (byte) 90);
		spawn(244690, 499.82422f, 585.81537f, 322.02252f, (byte) 92);
		spawn(244690, 501.61078f, 555.0141f, 321.84106f, (byte) 12);
	}

	private void IDTransformDevaGuardAs71An() {
		spawn(244691, 855.63855f, 484.74774f, 349.08722f, (byte) 45);
		spawn(244691, 852.21844f, 482.80756f, 349.41986f, (byte) 44);
		spawn(244691, 864.0118f, 492.74384f, 349.56552f, (byte) 46);
		spawn(244691, 508.17993f, 587.8108f, 322.56973f, (byte) 90);
		spawn(244691, 526.8684f, 493.7475f, 322.0f, (byte) 91);
		spawn(244691, 537.37225f, 508.5786f, 322.0f, (byte) 90);
		spawn(244691, 512.8187f, 590.4575f, 322.56216f, (byte) 90);
		spawn(244691, 504.50745f, 590.3867f, 322.56216f, (byte) 90);
		spawn(244691, 482.3006f, 496.94232f, 342.22174f, (byte) 27);
		spawn(244691, 594.4832f, 673.60126f, 350.85538f, (byte) 54);
		spawn(244691, 634.298f, 511.2995f, 339.61545f, (byte) 61);
		spawn(244691, 530.982f, 493.62543f, 322.0f, (byte) 90);
		spawn(244691, 641.9954f, 514.2143f, 339.61542f, (byte) 61);
		spawn(244691, 505.45636f, 584.90186f, 322.0f, (byte) 90);
		spawn(244691, 510.66394f, 584.80176f, 322.0f, (byte) 90);
		spawn(244691, 859.2291f, 490.53555f, 348.98047f, (byte) 45);
		spawn(244691, 604.1567f, 635.69995f, 352.53815f, (byte) 30);
		spawn(244691, 467.0f, 476.0f, 345.70047f, (byte) 32);
		spawn(244691, 536.2914f, 488.65588f, 322.0f, (byte) 90);
		spawn(244691, 639.6124f, 525.6597f, 339.61542f, (byte) 62);
		spawn(244691, 885.47394f, 464.9668f, 351.0f, (byte) 66);
		spawn(244691, 642.88293f, 520.17224f, 339.61542f, (byte) 60);
		spawn(244691, 546.91925f, 510.66574f, 321.94254f, (byte) 91);
		spawn(244691, 527.7633f, 502.87167f, 321.6398f, (byte) 91);
		spawn(244691, 885.5619f, 456.29126f, 351.02737f, (byte) 55);
	}

	private void IDTransformDevaGuardWi71An() {
		spawn(244692, 513.0236f, 593.86285f, 322.56216f, (byte) 90);
		spawn(244692, 501.3256f, 593.69885f, 322.56216f, (byte) 90);
		spawn(244692, 633.75323f, 516.50934f, 339.61545f, (byte) 61);
		spawn(244692, 544.93945f, 499.1886f, 322.0f, (byte) 91);
		spawn(244692, 530.8947f, 502.38684f, 321.86185f, (byte) 91);
		spawn(244692, 634.5065f, 522.0903f, 339.61545f, (byte) 53);
		spawn(244692, 885.382f, 460.77805f, 351.0f, (byte) 60);
		spawn(244692, 612.8048f, 643.80194f, 352.46414f, (byte) 30);
		spawn(244692, 538.00024f, 495.57394f, 322.0f, (byte) 90);
		spawn(244692, 526.9178f, 486.92227f, 321.87527f, (byte) 85);
	}

	private void IDTransformDevaGuardRa71An() {
		spawn(244693, 529.87f, 545.5564f, 321.85876f, (byte) 95);
		spawn(244693, 526.1612f, 545.1311f, 321.58078f, (byte) 88);
		spawn(244693, 546.40234f, 523.6142f, 321.97485f, (byte) 87);
		spawn(244693, 549.9647f, 524.9884f, 321.9978f, (byte) 95);
		spawn(244693, 520.9293f, 586.19006f, 321.94193f, (byte) 95);
		spawn(244693, 495.259f, 586.9457f, 322.0659f, (byte) 81);
		spawn(244693, 505.0709f, 552.7453f, 322.0f, (byte) 28);
	}

	/*** Eresh Stumble ***/
	private void IDTransformEreshStumble71An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformEreshStumbleFi71An();
				break;
			case 2:
				IDTransformEreshStumbleAs71An();
				break;
			case 3:
				IDTransformEreshStumbleWi71An();
				break;
			case 4:
				IDTransformEreshStumblePr71An();
				break;
		}
	}

	private void IDTransformEreshStumbleFi71An() {
		spawn(245019, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(245019, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(245019, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformEreshStumbleAs71An() {
		spawn(245020, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(245020, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(245020, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformEreshStumbleWi71An() {
		spawn(245021, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(245021, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(245021, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformEreshStumblePr71An() {
		spawn(245022, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(245022, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(245022, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	/*** Vritra Stumble ***/
	private void IDTransformVritraStumble71An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformVritraStumbleFi71An();
				break;
			case 2:
				IDTransformVritraStumbleAs71An();
				break;
			case 3:
				IDTransformVritraStumbleWi71An();
				break;
			case 4:
				IDTransformVritraStumblePr71An();
				break;
		}
	}

	private void IDTransformVritraStumbleFi71An() {
		spawn(245023, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(245023, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(245023, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformVritraStumbleAs71An() {
		spawn(245024, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(245024, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(245024, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformVritraStumbleWi71An() {
		spawn(245025, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(245025, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(245025, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformVritraStumblePr71An() {
		spawn(245026, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(245026, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(245026, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	/*** Tiamat Stumble ***/
	private void IDTransformTiamatStumble71An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformTiamatStumbleFi71An();
				break;
			case 2:
				IDTransformTiamatStumbleAs71An();
				break;
			case 3:
				IDTransformTiamatStumbleWi71An();
				break;
			case 4:
				IDTransformTiamatStumblePr71An();
				break;
		}
	}

	private void IDTransformTiamatStumbleFi71An() {
		spawn(245027, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(245027, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(245027, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformTiamatStumbleAs71An() {
		spawn(245028, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(245028, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(245028, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformTiamatStumbleWi71An() {
		spawn(245029, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(245029, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(245029, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformTiamatStumblePr71An() {
		spawn(245030, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(245030, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(245030, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	/*** Deva Stumble ***/
	private void IDTransformDevaStumble71An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformDevaStumbleFi71An();
				break;
			case 2:
				IDTransformDevaStumbleAs71An();
				break;
			case 3:
				IDTransformDevaStumbleWi71An();
				break;
			case 4:
				IDTransformDevaStumblePr71An();
				break;
		}
	}

	private void IDTransformDevaStumbleFi71An() {
		spawn(245031, 677.0f, 516.0f, 338.24844f, (byte) 43);
		spawn(245031, 505.28098f, 648.23944f, 317.08282f, (byte) 113);
		spawn(245031, 507.24606f, 628.40027f, 318.4649f, (byte) 61);
		spawn(245031, 504.39532f, 615.5569f, 320.3689f, (byte) 61);
		spawn(245031, 504.10117f, 601.42255f, 322.4643f, (byte) 60);
		spawn(245031, 513.7057f, 643.4998f, 317.10632f, (byte) 11);
		spawn(245031, 511.02466f, 614.0008f, 320.5996f, (byte) 0);
		spawn(245031, 503.81223f, 640.00806f, 317.11395f, (byte) 40);
		spawn(245031, 531.7686f, 642.5042f, 317.08282f, (byte) 112);
		spawn(245031, 522.0014f, 648.79095f, 317.08282f, (byte) 55);
	}

	private void IDTransformDevaStumbleAs71An() {
		spawn(245032, 677.0f, 516.0f, 338.24844f, (byte) 43);
		spawn(245032, 505.28098f, 648.23944f, 317.08282f, (byte) 113);
		spawn(245032, 507.24606f, 628.40027f, 318.4649f, (byte) 61);
		spawn(245032, 504.39532f, 615.5569f, 320.3689f, (byte) 61);
		spawn(245032, 504.10117f, 601.42255f, 322.4643f, (byte) 60);
		spawn(245032, 513.7057f, 643.4998f, 317.10632f, (byte) 11);
		spawn(245032, 511.02466f, 614.0008f, 320.5996f, (byte) 0);
		spawn(245032, 503.81223f, 640.00806f, 317.11395f, (byte) 40);
		spawn(245032, 531.7686f, 642.5042f, 317.08282f, (byte) 112);
		spawn(245032, 522.0014f, 648.79095f, 317.08282f, (byte) 55);
	}

	private void IDTransformDevaStumbleWi71An() {
		spawn(245033, 677.0f, 516.0f, 338.24844f, (byte) 43);
		spawn(245033, 505.28098f, 648.23944f, 317.08282f, (byte) 113);
		spawn(245033, 507.24606f, 628.40027f, 318.4649f, (byte) 61);
		spawn(245033, 504.39532f, 615.5569f, 320.3689f, (byte) 61);
		spawn(245033, 504.10117f, 601.42255f, 322.4643f, (byte) 60);
		spawn(245033, 513.7057f, 643.4998f, 317.10632f, (byte) 11);
		spawn(245033, 511.02466f, 614.0008f, 320.5996f, (byte) 0);
		spawn(245033, 503.81223f, 640.00806f, 317.11395f, (byte) 40);
		spawn(245033, 531.7686f, 642.5042f, 317.08282f, (byte) 112);
		spawn(245033, 522.0014f, 648.79095f, 317.08282f, (byte) 55);
	}

	private void IDTransformDevaStumblePr71An() {
		spawn(245034, 677.0f, 516.0f, 338.24844f, (byte) 43);
		spawn(245034, 505.28098f, 648.23944f, 317.08282f, (byte) 113);
		spawn(245034, 507.24606f, 628.40027f, 318.4649f, (byte) 61);
		spawn(245034, 504.39532f, 615.5569f, 320.3689f, (byte) 61);
		spawn(245034, 504.10117f, 601.42255f, 322.4643f, (byte) 60);
		spawn(245034, 513.7057f, 643.4998f, 317.10632f, (byte) 11);
		spawn(245034, 511.02466f, 614.0008f, 320.5996f, (byte) 0);
		spawn(245034, 503.81223f, 640.00806f, 317.11395f, (byte) 40);
		spawn(245034, 531.7686f, 642.5042f, 317.08282f, (byte) 112);
		spawn(245034, 522.0014f, 648.79095f, 317.08282f, (byte) 55);
	}

	/*** Drop Dragon ***/
	private void IDTransformDropDragon71Ae() {
		switch (Rnd.get(1, 3)) {
			case 1:
				spawn(245047, 622.6814f, 551.9144f, 346.06897f, (byte) 105);
				break;
			case 2:
				spawn(245048, 622.6814f, 551.9144f, 346.06897f, (byte) 105);
				break;
			case 3:
				spawn(245049, 622.6814f, 551.9144f, 346.06897f, (byte) 105);
				break;
		}
		switch (Rnd.get(1, 3)) {
			case 1:
				spawn(245047, 607.8992f, 674.06934f, 352.29062f, (byte) 90);
				break;
			case 2:
				spawn(245048, 607.8992f, 674.06934f, 352.29062f, (byte) 90);
				break;
			case 3:
				spawn(245049, 607.8992f, 674.06934f, 352.29062f, (byte) 90);
				break;
		}
	}

	/*** Bonus Monster ***/
	private void IDTransformBonusMonsterTypeA71An() {
		spawn(246220, 761.54095f, 562.17f, 341.0512f, (byte) 90);
		spawn(246220, 476.94467f, 549.22363f, 345.6048f, (byte) 90);
		spawn(246220, 609.2811f, 707.6352f, 355.10846f, (byte) 93);
	}

	private void IDTransformBonusMonsterTypeB71An() {
		spawn(246221, 761.54095f, 562.17f, 341.0512f, (byte) 90);
		spawn(246221, 476.94467f, 549.22363f, 345.6048f, (byte) 90);
		spawn(246221, 609.2811f, 707.6352f, 355.10846f, (byte) 93);
	}

	private void IDTransformBonusMonsterTypeC71An() {
		spawn(246222, 761.54095f, 562.17f, 341.0512f, (byte) 90);
		spawn(246222, 476.94467f, 549.22363f, 345.6048f, (byte) 90);
		spawn(246222, 609.2811f, 707.6352f, 355.10846f, (byte) 93);
	}

	private void IDTransformBonusMonsterTypeD71An() {
		spawn(246223, 761.54095f, 562.17f, 341.0512f, (byte) 90);
		spawn(246223, 476.94467f, 549.22363f, 345.6048f, (byte) 90);
		spawn(246223, 609.2811f, 707.6352f, 355.10846f, (byte) 93);
	}

	/*** Eresh Warp ***/
	private void IDTransformEreshWarp71() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformEreshWarpFi71An();
				break;
			case 2:
				IDTransformEreshWarpAs71An();
				break;
			case 3:
				IDTransformEreshWarpWi71An();
				break;
			case 4:
				IDTransformEreshWarpPr71An();
				break;
		}
	}

	private void IDTransformEreshWarpFi71An() {
		sp(245637, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245637, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245637, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245637, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245637, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245637, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245637, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245637, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245637, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245637, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245637, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245637, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245637, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245637, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245637, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245637, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245637, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245637, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245637, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformEreshWarpAs71An() {
		sp(245638, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245638, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245638, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245638, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245638, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245638, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245638, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245638, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245638, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245638, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245638, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245638, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245638, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245638, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245638, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245638, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245638, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245638, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245638, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformEreshWarpWi71An() {
		sp(245639, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245639, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245639, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245639, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245639, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245639, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245639, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245639, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245639, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245639, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245639, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245639, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245639, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245639, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245639, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245639, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245639, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245639, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245639, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformEreshWarpPr71An() {
		sp(245640, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245640, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245640, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245640, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245640, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245640, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245640, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245640, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245640, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245640, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245640, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245640, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245640, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245640, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245640, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245640, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245640, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245640, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245640, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	/*** Vritra Warp ***/
	private void IDTransformVritraWarp71() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformVritraWarpFi71An();
				break;
			case 2:
				IDTransformVritraWarpAs71An();
				break;
			case 3:
				IDTransformVritraWarpWi71An();
				break;
			case 4:
				IDTransformVritraWarpPr71An();
				break;
		}
	}

	private void IDTransformVritraWarpFi71An() {
		sp(245641, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245641, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245641, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245641, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245641, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245641, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245641, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245641, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245641, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245641, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245641, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245641, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245641, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245641, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245641, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245641, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245641, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245641, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245641, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformVritraWarpAs71An() {
		sp(245642, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245642, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245642, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245642, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245642, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245642, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245642, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245642, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245642, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245642, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245642, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245642, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245642, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245642, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245642, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245642, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245642, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245642, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245642, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformVritraWarpWi71An() {
		sp(245643, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245643, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245643, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245643, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245643, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245643, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245643, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245643, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245643, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245643, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245643, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245643, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245643, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245643, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245643, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245643, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245643, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245643, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245643, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformVritraWarpPr71An() {
		sp(245644, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245644, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245644, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245644, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245644, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245644, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245644, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245644, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245644, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245644, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245644, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245644, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245644, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245644, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245644, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245644, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245644, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245644, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245644, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	/*** Tiamat Warp ***/
	private void IDTransformTiamatWarp71() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformTiamatWarpFi71An();
				break;
			case 2:
				IDTransformTiamatWarpAs71An();
				break;
			case 3:
				IDTransformTiamatWarpWi71An();
				break;
			case 4:
				IDTransformTiamatWarpPr71An();
				break;
		}
	}

	private void IDTransformTiamatWarpFi71An() {
		sp(245645, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245645, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245645, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245645, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245645, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245645, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245645, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245645, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245645, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245645, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245645, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245645, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245645, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245645, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245645, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245645, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245645, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245645, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245645, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformTiamatWarpAs71An() {
		sp(245646, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245646, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245646, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245646, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245646, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245646, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245646, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245646, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245646, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245646, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245646, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245646, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245646, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245646, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245646, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245646, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245646, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245646, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245646, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformTiamatWarpWi71An() {
		sp(245647, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245647, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245647, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245647, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245647, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245647, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245647, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245647, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245647, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245647, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245647, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245647, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245647, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245647, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245647, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245647, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245647, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245647, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245647, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformTiamatWarpPr71An() {
		sp(245648, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245648, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245648, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245648, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245648, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245648, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245648, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245648, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245648, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245648, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245648, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245648, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245648, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245648, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245648, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245648, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245648, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245648, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245648, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	/*** Ranger ***/
	private void IDTransformRa71An() {
		switch (Rnd.get(1, 3)) {
			case 1:
				IDTransformEreshRa71An();
				break;
			case 2:
				IDTransformVritraRa71An();
				break;
			case 3:
				IDTransformTiamatRa71An();
				break;
		}
	}

	/*** Eresh Ranger ***/
	private void IDTransformEreshRa71An() {
		spawn(245727, 543.0933f, 557.8653f, 322.0f, (byte) 70);
		spawn(245727, 550.4571f, 546.09937f, 322.0f, (byte) 61);
		spawn(245727, 550.2217f, 539.29755f, 322.0f, (byte) 59);
		spawn(245727, 543.0761f, 534.39087f, 322.0f, (byte) 43);
		spawn(245727, 534.18945f, 534.63385f, 321.875f, (byte) 21);
		spawn(245727, 522.56396f, 467.8957f, 322.0f, (byte) 37);
		spawn(245727, 527.70734f, 475.95486f, 322.0f, (byte) 7);
		spawn(245727, 509.6404f, 476.19836f, 322.0f, (byte) 118);
		spawn(245727, 501.61966f, 467.60513f, 322.0f, (byte) 5);
		spawn(245727, 503.2415f, 446.9365f, 321.8276f, (byte) 18);
		spawn(245727, 542.8608f, 472.11404f, 322.0f, (byte) 51);
		spawn(245727, 539.9602f, 467.72766f, 322.0f, (byte) 50);
		spawn(245727, 516.02167f, 453.723f, 321.875f, (byte) 60);
		spawn(245727, 466.30823f, 509.6083f, 342.1901f, (byte) 0);
		spawn(245727, 466.1356f, 516.46765f, 342.1901f, (byte) 0);
		spawn(245727, 468.7195f, 513.0958f, 342.1901f, (byte) 0);
		spawn(245727, 446.91077f, 509.62943f, 342.2713f, (byte) 1);
		spawn(245727, 446.86057f, 517.2843f, 342.27173f, (byte) 0);
		spawn(245727, 449.71277f, 513.23883f, 342.24753f, (byte) 0);
		spawn(245727, 427.54434f, 509.3018f, 342.43564f, (byte) 0);
		spawn(245727, 427.4629f, 517.19116f, 342.43634f, (byte) 0);
		spawn(245727, 430.6331f, 513.1085f, 342.40942f, (byte) 0);
		spawn(245727, 483.40283f, 475.9793f, 345.70047f, (byte) 81);
		spawn(245727, 469.33865f, 454.01733f, 345.70047f, (byte) 4);
		spawn(245727, 482.78824f, 526.4342f, 342.234f, (byte) 78);
		spawn(245727, 502.18372f, 508.00098f, 339.63123f, (byte) 39);
		spawn(245727, 497.84283f, 521.77655f, 339.63815f, (byte) 77);
		spawn(245727, 508.98663f, 527.061f, 339.63126f, (byte) 76);
		spawn(245727, 502.5523f, 649.1053f, 317.08282f, (byte) 109);
		spawn(245727, 522.4196f, 650.3398f, 317.08282f, (byte) 99);
		spawn(245727, 540.84265f, 652.0684f, 316.16275f, (byte) 100);
		spawn(245727, 540.5331f, 637.4856f, 316.17746f, (byte) 17);
		spawn(245727, 514.6148f, 641.9673f, 317.10098f, (byte) 33);
		spawn(245727, 539.03076f, 536.819f, 322.0f, (byte) 30);
	}

	/*** Vritra Ranger ***/
	private void IDTransformVritraRa71An() {
		spawn(245728, 543.0933f, 557.8653f, 322.0f, (byte) 70);
		spawn(245728, 550.4571f, 546.09937f, 322.0f, (byte) 61);
		spawn(245728, 550.2217f, 539.29755f, 322.0f, (byte) 59);
		spawn(245728, 543.0761f, 534.39087f, 322.0f, (byte) 43);
		spawn(245728, 534.18945f, 534.63385f, 321.875f, (byte) 21);
		spawn(245728, 522.56396f, 467.8957f, 322.0f, (byte) 37);
		spawn(245728, 527.70734f, 475.95486f, 322.0f, (byte) 7);
		spawn(245728, 509.6404f, 476.19836f, 322.0f, (byte) 118);
		spawn(245728, 501.61966f, 467.60513f, 322.0f, (byte) 5);
		spawn(245728, 503.2415f, 446.9365f, 321.8276f, (byte) 18);
		spawn(245728, 542.8608f, 472.11404f, 322.0f, (byte) 51);
		spawn(245728, 539.9602f, 467.72766f, 322.0f, (byte) 50);
		spawn(245728, 516.02167f, 453.723f, 321.875f, (byte) 60);
		spawn(245728, 466.30823f, 509.6083f, 342.1901f, (byte) 0);
		spawn(245728, 466.1356f, 516.46765f, 342.1901f, (byte) 0);
		spawn(245728, 468.7195f, 513.0958f, 342.1901f, (byte) 0);
		spawn(245728, 446.91077f, 509.62943f, 342.2713f, (byte) 1);
		spawn(245728, 446.86057f, 517.2843f, 342.27173f, (byte) 0);
		spawn(245728, 449.71277f, 513.23883f, 342.24753f, (byte) 0);
		spawn(245728, 427.54434f, 509.3018f, 342.43564f, (byte) 0);
		spawn(245728, 427.4629f, 517.19116f, 342.43634f, (byte) 0);
		spawn(245728, 430.6331f, 513.1085f, 342.40942f, (byte) 0);
		spawn(245728, 483.40283f, 475.9793f, 345.70047f, (byte) 81);
		spawn(245728, 469.33865f, 454.01733f, 345.70047f, (byte) 4);
		spawn(245728, 482.78824f, 526.4342f, 342.234f, (byte) 78);
		spawn(245728, 502.18372f, 508.00098f, 339.63123f, (byte) 39);
		spawn(245728, 497.84283f, 521.77655f, 339.63815f, (byte) 77);
		spawn(245728, 508.98663f, 527.061f, 339.63126f, (byte) 76);
		spawn(245728, 502.5523f, 649.1053f, 317.08282f, (byte) 109);
		spawn(245728, 522.4196f, 650.3398f, 317.08282f, (byte) 99);
		spawn(245728, 540.84265f, 652.0684f, 316.16275f, (byte) 100);
		spawn(245728, 540.5331f, 637.4856f, 316.17746f, (byte) 17);
		spawn(245728, 514.6148f, 641.9673f, 317.10098f, (byte) 33);
		spawn(245728, 539.03076f, 536.819f, 322.0f, (byte) 30);
	}

	/*** Tiamat Ranger ***/
	private void IDTransformTiamatRa71An() {
		spawn(245729, 543.0933f, 557.8653f, 322.0f, (byte) 70);
		spawn(245729, 550.4571f, 546.09937f, 322.0f, (byte) 61);
		spawn(245729, 550.2217f, 539.29755f, 322.0f, (byte) 59);
		spawn(245729, 543.0761f, 534.39087f, 322.0f, (byte) 43);
		spawn(245729, 534.18945f, 534.63385f, 321.875f, (byte) 21);
		spawn(245729, 522.56396f, 467.8957f, 322.0f, (byte) 37);
		spawn(245729, 527.70734f, 475.95486f, 322.0f, (byte) 7);
		spawn(245729, 509.6404f, 476.19836f, 322.0f, (byte) 118);
		spawn(245729, 501.61966f, 467.60513f, 322.0f, (byte) 5);
		spawn(245729, 503.2415f, 446.9365f, 321.8276f, (byte) 18);
		spawn(245729, 542.8608f, 472.11404f, 322.0f, (byte) 51);
		spawn(245729, 539.9602f, 467.72766f, 322.0f, (byte) 50);
		spawn(245729, 516.02167f, 453.723f, 321.875f, (byte) 60);
		spawn(245729, 466.30823f, 509.6083f, 342.1901f, (byte) 0);
		spawn(245729, 466.1356f, 516.46765f, 342.1901f, (byte) 0);
		spawn(245729, 468.7195f, 513.0958f, 342.1901f, (byte) 0);
		spawn(245729, 446.91077f, 509.62943f, 342.2713f, (byte) 1);
		spawn(245729, 446.86057f, 517.2843f, 342.27173f, (byte) 0);
		spawn(245729, 449.71277f, 513.23883f, 342.24753f, (byte) 0);
		spawn(245729, 427.54434f, 509.3018f, 342.43564f, (byte) 0);
		spawn(245729, 427.4629f, 517.19116f, 342.43634f, (byte) 0);
		spawn(245729, 430.6331f, 513.1085f, 342.40942f, (byte) 0);
		spawn(245729, 483.40283f, 475.9793f, 345.70047f, (byte) 81);
		spawn(245729, 469.33865f, 454.01733f, 345.70047f, (byte) 4);
		spawn(245729, 482.78824f, 526.4342f, 342.234f, (byte) 78);
		spawn(245729, 502.18372f, 508.00098f, 339.63123f, (byte) 39);
		spawn(245729, 497.84283f, 521.77655f, 339.63815f, (byte) 77);
		spawn(245729, 508.98663f, 527.061f, 339.63126f, (byte) 76);
		spawn(245729, 502.5523f, 649.1053f, 317.08282f, (byte) 109);
		spawn(245729, 522.4196f, 650.3398f, 317.08282f, (byte) 99);
		spawn(245729, 540.84265f, 652.0684f, 316.16275f, (byte) 100);
		spawn(245729, 540.5331f, 637.4856f, 316.17746f, (byte) 17);
		spawn(245729, 514.6148f, 641.9673f, 317.10098f, (byte) 33);
		spawn(245729, 539.03076f, 536.819f, 322.0f, (byte) 30);
	}

	/*** Player Lvl 72 ***/
	/*** Eresh High ***/
	private void IDTransformEreshHigh72An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformEreshHighFi72An();
				break;
			case 2:
				IDTransformEreshHighAs72An();
				break;
			case 3:
				IDTransformEreshHighWi72An();
				break;
			case 4:
				IDTransformEreshHighPr72An();
				break;
		}
	}

	private void IDTransformEreshHighFi72An() {
		spawn(244716, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244716, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244716, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244716, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244716, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244716, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244716, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244716, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244716, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244716, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244716, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244716, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244716, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244716, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244716, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244716, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244716, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformEreshHighAs72An() {
		spawn(244717, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244717, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244717, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244717, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244717, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244717, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244717, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244717, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244717, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244717, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244717, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244717, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244717, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244717, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244717, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244717, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244717, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformEreshHighWi72An() {
		spawn(244718, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244718, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244718, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244718, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244718, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244718, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244718, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244718, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244718, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244718, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244718, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244718, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244718, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244718, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244718, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244718, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244718, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformEreshHighPr72An() {
		spawn(244719, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244719, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244719, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244719, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244719, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244719, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244719, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244719, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244719, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244719, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244719, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244719, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244719, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244719, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244719, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244719, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244719, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	/*** Vritra High ***/
	private void IDTransformVritraHigh72An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformVritraHighFi72An();
				break;
			case 2:
				IDTransformVritraHighAs72An();
				break;
			case 3:
				IDTransformVritraHighWi72An();
				break;
			case 4:
				IDTransformVritraHighPr72An();
				break;
		}
	}

	private void IDTransformVritraHighFi72An() {
		spawn(244720, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244720, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244720, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244720, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244720, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244720, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244720, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244720, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244720, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244720, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244720, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244720, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244720, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244720, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244720, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244720, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244720, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformVritraHighAs72An() {
		spawn(244721, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244721, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244721, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244721, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244721, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244721, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244721, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244721, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244721, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244721, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244721, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244721, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244721, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244721, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244721, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244721, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244721, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformVritraHighWi72An() {
		spawn(244722, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244722, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244722, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244722, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244722, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244722, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244722, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244722, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244722, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244722, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244722, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244722, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244722, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244722, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244722, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244722, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244722, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformVritraHighPr72An() {
		spawn(244723, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244723, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244723, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244723, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244723, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244723, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244723, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244723, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244723, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244723, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244723, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244723, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244723, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244723, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244723, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244723, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244723, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	/*** Tiamat High ***/
	private void IDTransformTiamatHigh72An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformTiamatHighFi72An();
				break;
			case 2:
				IDTransformTiamatHighAs72An();
				break;
			case 3:
				IDTransformTiamatHighWi72An();
				break;
			case 4:
				IDTransformTiamatHighPr72An();
				break;
		}
	}

	private void IDTransformTiamatHighFi72An() {
		spawn(244724, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244724, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244724, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244724, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244724, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244724, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244724, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244724, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244724, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244724, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244724, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244724, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244724, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244724, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244724, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244724, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244724, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformTiamatHighAs72An() {
		spawn(244725, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244725, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244725, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244725, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244725, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244725, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244725, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244725, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244725, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244725, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244725, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244725, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244725, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244725, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244725, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244725, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244725, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformTiamatHighWi72An() {
		spawn(244726, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244726, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244726, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244726, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244726, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244726, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244726, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244726, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244726, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244726, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244726, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244726, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244726, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244726, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244726, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244726, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244726, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformTiamatHighPr72An() {
		spawn(244727, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244727, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244727, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244727, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244727, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244727, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244727, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244727, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244727, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244727, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244727, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244727, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244727, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244727, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244727, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244727, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244727, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	/*** Eresh Guard ***/
	private void IDTransformEreshGuard72An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformEreshGuardFi72An();
				break;
			case 2:
				IDTransformEreshGuardAs72An();
				break;
			case 3:
				IDTransformEreshGuardWi72An();
				break;
			case 4:
				IDTransformEreshGuardPr72An();
				break;
		}
	}

	private void IDTransformEreshGuardFi72An() {
		spawn(244704, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244704, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244704, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244704, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244704, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244704, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244704, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244704, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244704, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244704, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244704, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244704, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244704, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244704, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244704, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244704, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244704, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244704, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244704, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244704, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244704, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244704, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244704, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244704, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244704, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244704, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244704, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244704, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244704, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244704, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244704, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244704, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244704, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244704, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244704, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244704, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244704, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244704, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244704, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244704, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244704, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformEreshGuardAs72An() {
		spawn(244705, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244705, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244705, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244705, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244705, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244705, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244705, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244705, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244705, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244705, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244705, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244705, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244705, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244705, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244705, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244705, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244705, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244705, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244705, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244705, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244705, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244705, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244705, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244705, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244705, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244705, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244705, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244705, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244705, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244705, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244705, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244705, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244705, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244705, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244705, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244705, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244705, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244705, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244705, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244705, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244705, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformEreshGuardWi72An() {
		spawn(244706, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244706, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244706, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244706, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244706, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244706, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244706, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244706, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244706, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244706, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244706, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244706, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244706, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244706, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244706, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244706, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244706, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244706, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244706, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244706, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244706, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244706, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244706, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244706, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244706, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244706, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244706, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244706, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244706, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244706, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244706, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244706, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244706, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244706, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244706, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244706, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244706, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244706, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244706, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244706, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244706, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformEreshGuardPr72An() {
		spawn(244707, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244707, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244707, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244707, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244707, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244707, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244707, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244707, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244707, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244707, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244707, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244707, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244707, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244707, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244707, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244707, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244707, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244707, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244707, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244707, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244707, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244707, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244707, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244707, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244707, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244707, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244707, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244707, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244707, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244707, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244707, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244707, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244707, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244707, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244707, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244707, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244707, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244707, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244707, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244707, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244707, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	/*** Vritra Guard ***/
	private void IDTransformVritraGuard72An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformVritraGuardFi72An();
				break;
			case 2:
				IDTransformVritraGuardAs72An();
				break;
			case 3:
				IDTransformVritraGuardWi72An();
				break;
			case 4:
				IDTransformVritraGuardPr72An();
				break;
		}
	}

	private void IDTransformVritraGuardFi72An() {
		spawn(244708, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244708, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244708, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244708, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244708, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244708, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244708, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244708, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244708, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244708, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244708, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244708, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244708, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244708, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244708, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244708, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244708, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244708, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244708, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244708, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244708, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244708, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244708, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244708, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244708, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244708, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244708, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244708, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244708, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244708, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244708, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244708, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244708, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244708, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244708, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244708, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244708, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244708, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244708, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244708, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244708, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformVritraGuardAs72An() {
		spawn(244709, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244709, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244709, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244709, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244709, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244709, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244709, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244709, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244709, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244709, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244709, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244709, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244709, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244709, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244709, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244709, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244709, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244709, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244709, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244709, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244709, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244709, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244709, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244709, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244709, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244709, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244709, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244709, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244709, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244709, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244709, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244709, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244709, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244709, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244709, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244709, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244709, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244709, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244709, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244709, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244709, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformVritraGuardWi72An() {
		spawn(244710, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244710, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244710, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244710, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244710, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244710, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244710, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244710, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244710, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244710, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244710, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244710, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244710, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244710, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244710, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244710, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244710, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244710, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244710, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244710, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244710, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244710, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244710, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244710, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244710, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244710, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244710, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244710, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244710, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244710, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244710, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244710, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244710, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244710, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244710, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244710, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244710, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244710, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244710, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244710, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244710, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformVritraGuardPr72An() {
		spawn(244711, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244711, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244711, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244711, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244711, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244711, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244711, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244711, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244711, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244711, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244711, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244711, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244711, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244711, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244711, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244711, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244711, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244711, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244711, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244711, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244711, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244711, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244711, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244711, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244711, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244711, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244711, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244711, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244711, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244711, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244711, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244711, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244711, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244711, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244711, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244711, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244711, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244711, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244711, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244711, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244711, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	/*** Tiamat Guard ***/
	private void IDTransformTiamatGuard72An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformTiamatGuardFi72An();
				break;
			case 2:
				IDTransformTiamatGuardAs72An();
				break;
			case 3:
				IDTransformTiamatGuardWi72An();
				break;
			case 4:
				IDTransformTiamatGuardPr72An();
				break;
		}
	}

	private void IDTransformTiamatGuardFi72An() {
		spawn(244712, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244712, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244712, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244712, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244712, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244712, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244712, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244712, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244712, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244712, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244712, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244712, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244712, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244712, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244712, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244712, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244712, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244712, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244712, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244712, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244712, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244712, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244712, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244712, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244712, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244712, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244712, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244712, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244712, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244712, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244712, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244712, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244712, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244712, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244712, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244712, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244712, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244712, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244712, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244712, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244712, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformTiamatGuardAs72An() {
		spawn(244713, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244713, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244713, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244713, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244713, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244713, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244713, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244713, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244713, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244713, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244713, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244713, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244713, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244713, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244713, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244713, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244713, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244713, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244713, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244713, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244713, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244713, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244713, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244713, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244713, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244713, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244713, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244713, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244713, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244713, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244713, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244713, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244713, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244713, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244713, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244713, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244713, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244713, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244713, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244713, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244713, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformTiamatGuardWi72An() {
		spawn(244714, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244714, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244714, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244714, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244714, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244714, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244714, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244714, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244714, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244714, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244714, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244714, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244714, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244714, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244714, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244714, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244714, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244714, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244714, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244714, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244714, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244714, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244714, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244714, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244714, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244714, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244714, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244714, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244714, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244714, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244714, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244714, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244714, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244714, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244714, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244714, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244714, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244714, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244714, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244714, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244714, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformTiamatGuardPr72An() {
		spawn(244715, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244715, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244715, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244715, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244715, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244715, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244715, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244715, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244715, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244715, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244715, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244715, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244715, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244715, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244715, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244715, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244715, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244715, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244715, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244715, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244715, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244715, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244715, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244715, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244715, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244715, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244715, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244715, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244715, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244715, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244715, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244715, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244715, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244715, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244715, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244715, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244715, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244715, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244715, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244715, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244715, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	/*** Deva Guard ***/
	private void IDTransformDevaGuardFi72An() {
		spawn(244731, 543.9115f, 491.4599f, 322.04422f, (byte) 91);
		spawn(244731, 547.9682f, 490.34494f, 321.50793f, (byte) 90);
		spawn(244731, 534.53534f, 485.35455f, 322.0f, (byte) 89);
		spawn(244731, 541.57935f, 487.14307f, 322.0f, (byte) 89);
		spawn(244731, 516.4117f, 585.5329f, 321.97427f, (byte) 90);
		spawn(244731, 499.82422f, 585.81537f, 322.02252f, (byte) 92);
		spawn(244731, 501.61078f, 555.0141f, 321.84106f, (byte) 12);
	}

	private void IDTransformDevaGuardAs72An() {
		spawn(244732, 855.63855f, 484.74774f, 349.08722f, (byte) 45);
		spawn(244732, 852.21844f, 482.80756f, 349.41986f, (byte) 44);
		spawn(244732, 864.0118f, 492.74384f, 349.56552f, (byte) 46);
		spawn(244732, 508.17993f, 587.8108f, 322.56973f, (byte) 90);
		spawn(244732, 526.8684f, 493.7475f, 322.0f, (byte) 91);
		spawn(244732, 537.37225f, 508.5786f, 322.0f, (byte) 90);
		spawn(244732, 512.8187f, 590.4575f, 322.56216f, (byte) 90);
		spawn(244732, 504.50745f, 590.3867f, 322.56216f, (byte) 90);
		spawn(244732, 482.3006f, 496.94232f, 342.22174f, (byte) 27);
		spawn(244732, 594.4832f, 673.60126f, 350.85538f, (byte) 54);
		spawn(244732, 634.298f, 511.2995f, 339.61545f, (byte) 61);
		spawn(244732, 530.982f, 493.62543f, 322.0f, (byte) 90);
		spawn(244732, 641.9954f, 514.2143f, 339.61542f, (byte) 61);
		spawn(244732, 505.45636f, 584.90186f, 322.0f, (byte) 90);
		spawn(244732, 510.66394f, 584.80176f, 322.0f, (byte) 90);
		spawn(244732, 859.2291f, 490.53555f, 348.98047f, (byte) 45);
		spawn(244732, 604.1567f, 635.69995f, 352.53815f, (byte) 30);
		spawn(244732, 467.0f, 476.0f, 345.70047f, (byte) 32);
		spawn(244732, 536.2914f, 488.65588f, 322.0f, (byte) 90);
		spawn(244732, 639.6124f, 525.6597f, 339.61542f, (byte) 62);
		spawn(244732, 885.47394f, 464.9668f, 351.0f, (byte) 66);
		spawn(244732, 642.88293f, 520.17224f, 339.61542f, (byte) 60);
		spawn(244732, 546.91925f, 510.66574f, 321.94254f, (byte) 91);
		spawn(244732, 527.7633f, 502.87167f, 321.6398f, (byte) 91);
		spawn(244732, 885.5619f, 456.29126f, 351.02737f, (byte) 55);
	}

	private void IDTransformDevaGuardWi72An() {
		spawn(244733, 513.0236f, 593.86285f, 322.56216f, (byte) 90);
		spawn(244733, 501.3256f, 593.69885f, 322.56216f, (byte) 90);
		spawn(244733, 633.75323f, 516.50934f, 339.61545f, (byte) 61);
		spawn(244733, 544.93945f, 499.1886f, 322.0f, (byte) 91);
		spawn(244733, 530.8947f, 502.38684f, 321.86185f, (byte) 91);
		spawn(244733, 634.5065f, 522.0903f, 339.61545f, (byte) 53);
		spawn(244733, 885.382f, 460.77805f, 351.0f, (byte) 60);
		spawn(244733, 612.8048f, 643.80194f, 352.46414f, (byte) 30);
		spawn(244733, 538.00024f, 495.57394f, 322.0f, (byte) 90);
		spawn(244733, 526.9178f, 486.92227f, 321.87527f, (byte) 85);
	}

	private void IDTransformDevaGuardRa72An() {
		spawn(244734, 529.87f, 545.5564f, 321.85876f, (byte) 95);
		spawn(244734, 526.1612f, 545.1311f, 321.58078f, (byte) 88);
		spawn(244734, 546.40234f, 523.6142f, 321.97485f, (byte) 87);
		spawn(244734, 549.9647f, 524.9884f, 321.9978f, (byte) 95);
		spawn(244734, 520.9293f, 586.19006f, 321.94193f, (byte) 95);
		spawn(244734, 495.259f, 586.9457f, 322.0659f, (byte) 81);
		spawn(244734, 505.0709f, 552.7453f, 322.0f, (byte) 28);
	}

	/*** Eresh Stumble ***/
	private void IDTransformEreshStumble72An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformEreshStumbleFi72An();
				break;
			case 2:
				IDTransformEreshStumbleAs72An();
				break;
			case 3:
				IDTransformEreshStumbleWi72An();
				break;
			case 4:
				IDTransformEreshStumblePr72An();
				break;
		}
	}

	private void IDTransformEreshStumbleFi72An() {
		spawn(245050, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(245050, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(245050, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformEreshStumbleAs72An() {
		spawn(245051, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(245051, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(245051, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformEreshStumbleWi72An() {
		spawn(245052, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(245052, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(245052, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformEreshStumblePr72An() {
		spawn(245053, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(245053, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(245053, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	/*** Vritra Stumble ***/
	private void IDTransformVritraStumble72An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformVritraStumbleFi72An();
				break;
			case 2:
				IDTransformVritraStumbleAs72An();
				break;
			case 3:
				IDTransformVritraStumbleWi72An();
				break;
			case 4:
				IDTransformVritraStumblePr72An();
				break;
		}
	}

	private void IDTransformVritraStumbleFi72An() {
		spawn(245054, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(245054, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(245054, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformVritraStumbleAs72An() {
		spawn(245055, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(245055, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(245055, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformVritraStumbleWi72An() {
		spawn(245056, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(245056, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(245056, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformVritraStumblePr72An() {
		spawn(245057, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(245057, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(245057, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	/*** Tiamat Stumble ***/
	private void IDTransformTiamatStumble72An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformTiamatStumbleFi72An();
				break;
			case 2:
				IDTransformTiamatStumbleAs72An();
				break;
			case 3:
				IDTransformTiamatStumbleWi72An();
				break;
			case 4:
				IDTransformTiamatStumblePr72An();
				break;
		}
	}

	private void IDTransformTiamatStumbleFi72An() {
		spawn(245058, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(245058, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(245058, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformTiamatStumbleAs72An() {
		spawn(245059, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(245059, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(245059, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformTiamatStumbleWi72An() {
		spawn(245060, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(245060, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(245060, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformTiamatStumblePr72An() {
		spawn(245061, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(245061, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(245061, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	/*** Deva Stumble ***/
	private void IDTransformDevaStumble72An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformDevaStumbleFi72An();
				break;
			case 2:
				IDTransformDevaStumbleAs72An();
				break;
			case 3:
				IDTransformDevaStumbleWi72An();
				break;
			case 4:
				IDTransformDevaStumblePr72An();
				break;
		}
	}

	private void IDTransformDevaStumbleFi72An() {
		spawn(245062, 677.0f, 516.0f, 338.24844f, (byte) 43);
		spawn(245062, 505.28098f, 648.23944f, 317.08282f, (byte) 113);
		spawn(245062, 507.24606f, 628.40027f, 318.4649f, (byte) 61);
		spawn(245062, 504.39532f, 615.5569f, 320.3689f, (byte) 61);
		spawn(245062, 504.10117f, 601.42255f, 322.4643f, (byte) 60);
		spawn(245062, 513.7057f, 643.4998f, 317.10632f, (byte) 11);
		spawn(245062, 511.02466f, 614.0008f, 320.5996f, (byte) 0);
		spawn(245062, 503.81223f, 640.00806f, 317.11395f, (byte) 40);
		spawn(245062, 531.7686f, 642.5042f, 317.08282f, (byte) 112);
		spawn(245062, 522.0014f, 648.79095f, 317.08282f, (byte) 55);
	}

	private void IDTransformDevaStumbleAs72An() {
		spawn(245063, 677.0f, 516.0f, 338.24844f, (byte) 43);
		spawn(245063, 505.28098f, 648.23944f, 317.08282f, (byte) 113);
		spawn(245063, 507.24606f, 628.40027f, 318.4649f, (byte) 61);
		spawn(245063, 504.39532f, 615.5569f, 320.3689f, (byte) 61);
		spawn(245063, 504.10117f, 601.42255f, 322.4643f, (byte) 60);
		spawn(245063, 513.7057f, 643.4998f, 317.10632f, (byte) 11);
		spawn(245063, 511.02466f, 614.0008f, 320.5996f, (byte) 0);
		spawn(245063, 503.81223f, 640.00806f, 317.11395f, (byte) 40);
		spawn(245063, 531.7686f, 642.5042f, 317.08282f, (byte) 112);
		spawn(245063, 522.0014f, 648.79095f, 317.08282f, (byte) 55);
	}

	private void IDTransformDevaStumbleWi72An() {
		spawn(245064, 677.0f, 516.0f, 338.24844f, (byte) 43);
		spawn(245064, 505.28098f, 648.23944f, 317.08282f, (byte) 113);
		spawn(245064, 507.24606f, 628.40027f, 318.4649f, (byte) 61);
		spawn(245064, 504.39532f, 615.5569f, 320.3689f, (byte) 61);
		spawn(245064, 504.10117f, 601.42255f, 322.4643f, (byte) 60);
		spawn(245064, 513.7057f, 643.4998f, 317.10632f, (byte) 11);
		spawn(245064, 511.02466f, 614.0008f, 320.5996f, (byte) 0);
		spawn(245064, 503.81223f, 640.00806f, 317.11395f, (byte) 40);
		spawn(245064, 531.7686f, 642.5042f, 317.08282f, (byte) 112);
		spawn(245064, 522.0014f, 648.79095f, 317.08282f, (byte) 55);
	}

	private void IDTransformDevaStumblePr72An() {
		spawn(245065, 677.0f, 516.0f, 338.24844f, (byte) 43);
		spawn(245065, 505.28098f, 648.23944f, 317.08282f, (byte) 113);
		spawn(245065, 507.24606f, 628.40027f, 318.4649f, (byte) 61);
		spawn(245065, 504.39532f, 615.5569f, 320.3689f, (byte) 61);
		spawn(245065, 504.10117f, 601.42255f, 322.4643f, (byte) 60);
		spawn(245065, 513.7057f, 643.4998f, 317.10632f, (byte) 11);
		spawn(245065, 511.02466f, 614.0008f, 320.5996f, (byte) 0);
		spawn(245065, 503.81223f, 640.00806f, 317.11395f, (byte) 40);
		spawn(245065, 531.7686f, 642.5042f, 317.08282f, (byte) 112);
		spawn(245065, 522.0014f, 648.79095f, 317.08282f, (byte) 55);
	}

	/*** Drop Dragon ***/
	private void IDTransformDropDragon72Ae() {
		switch (Rnd.get(1, 3)) {
			case 1:
				spawn(245078, 622.6814f, 551.9144f, 346.06897f, (byte) 105);
				break;
			case 2:
				spawn(245079, 622.6814f, 551.9144f, 346.06897f, (byte) 105);
				break;
			case 3:
				spawn(245080, 622.6814f, 551.9144f, 346.06897f, (byte) 105);
				break;
		}
		switch (Rnd.get(1, 3)) {
			case 1:
				spawn(245078, 607.8992f, 674.06934f, 352.29062f, (byte) 90);
				break;
			case 2:
				spawn(245079, 607.8992f, 674.06934f, 352.29062f, (byte) 90);
				break;
			case 3:
				spawn(245080, 607.8992f, 674.06934f, 352.29062f, (byte) 90);
				break;
		}
	}

	/*** Bonus Monster ***/
	private void IDTransformBonusMonsterTypeA72An() {
		spawn(246224, 761.54095f, 562.17f, 341.0512f, (byte) 90);
		spawn(246224, 476.94467f, 549.22363f, 345.6048f, (byte) 90);
		spawn(246224, 609.2811f, 707.6352f, 355.10846f, (byte) 93);
	}

	private void IDTransformBonusMonsterTypeB72An() {
		spawn(246225, 761.54095f, 562.17f, 341.0512f, (byte) 90);
		spawn(246225, 476.94467f, 549.22363f, 345.6048f, (byte) 90);
		spawn(246225, 609.2811f, 707.6352f, 355.10846f, (byte) 93);
	}

	private void IDTransformBonusMonsterTypeC72An() {
		spawn(246226, 761.54095f, 562.17f, 341.0512f, (byte) 90);
		spawn(246226, 476.94467f, 549.22363f, 345.6048f, (byte) 90);
		spawn(246226, 609.2811f, 707.6352f, 355.10846f, (byte) 93);
	}

	private void IDTransformBonusMonsterTypeD72An() {
		spawn(246227, 761.54095f, 562.17f, 341.0512f, (byte) 90);
		spawn(246227, 476.94467f, 549.22363f, 345.6048f, (byte) 90);
		spawn(246227, 609.2811f, 707.6352f, 355.10846f, (byte) 93);
	}

	/*** Eresh Warp ***/
	private void IDTransformEreshWarp72() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformEreshWarpFi72An();
				break;
			case 2:
				IDTransformEreshWarpAs72An();
				break;
			case 3:
				IDTransformEreshWarpWi72An();
				break;
			case 4:
				IDTransformEreshWarpPr72An();
				break;
		}
	}

	private void IDTransformEreshWarpFi72An() {
		sp(245649, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245649, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245649, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245649, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245649, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245649, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245649, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245649, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245649, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245649, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245649, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245649, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245649, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245649, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245649, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245649, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245649, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245649, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245649, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformEreshWarpAs72An() {
		sp(245650, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245650, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245650, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245650, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245650, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245650, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245650, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245650, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245650, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245650, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245650, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245650, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245650, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245650, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245650, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245650, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245650, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245650, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245650, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformEreshWarpWi72An() {
		sp(245651, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245651, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245651, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245651, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245651, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245651, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245651, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245651, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245651, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245651, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245651, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245651, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245651, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245651, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245651, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245651, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245651, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245651, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245651, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformEreshWarpPr72An() {
		sp(245652, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245652, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245652, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245652, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245652, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245652, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245652, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245652, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245652, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245652, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245652, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245652, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245652, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245652, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245652, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245652, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245652, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245652, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245652, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	/*** Vritra Warp ***/
	private void IDTransformVritraWarp72() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformVritraWarpFi72An();
				break;
			case 2:
				IDTransformVritraWarpAs72An();
				break;
			case 3:
				IDTransformVritraWarpWi72An();
				break;
			case 4:
				IDTransformVritraWarpPr72An();
				break;
		}
	}

	private void IDTransformVritraWarpFi72An() {
		sp(245653, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245653, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245653, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245653, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245653, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245653, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245653, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245653, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245653, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245653, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245653, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245653, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245653, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245653, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245653, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245653, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245653, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245653, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245653, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformVritraWarpAs72An() {
		sp(245654, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245654, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245654, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245654, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245654, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245654, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245654, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245654, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245654, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245654, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245654, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245654, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245654, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245654, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245654, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245654, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245654, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245654, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245654, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformVritraWarpWi72An() {
		sp(245655, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245655, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245655, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245655, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245655, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245655, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245655, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245655, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245655, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245655, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245655, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245655, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245655, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245655, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245655, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245655, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245655, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245655, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245655, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformVritraWarpPr72An() {
		sp(245656, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245656, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245656, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245656, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245656, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245656, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245656, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245656, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245656, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245656, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245656, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245656, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245656, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245656, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245656, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245656, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245656, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245656, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245656, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	/*** Tiamat Warp ***/
	private void IDTransformTiamatWarp72() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformTiamatWarpFi72An();
				break;
			case 2:
				IDTransformTiamatWarpAs72An();
				break;
			case 3:
				IDTransformTiamatWarpWi72An();
				break;
			case 4:
				IDTransformTiamatWarpPr72An();
				break;
		}
	}

	private void IDTransformTiamatWarpFi72An() {
		sp(245657, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245657, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245657, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245657, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245657, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245657, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245657, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245657, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245657, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245657, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245657, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245657, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245657, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245657, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245657, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245657, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245657, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245657, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245657, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformTiamatWarpAs72An() {
		sp(245658, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245658, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245658, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245658, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245658, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245658, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245658, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245658, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245658, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245658, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245658, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245658, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245658, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245658, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245658, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245658, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245658, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245658, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245658, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformTiamatWarpWi72An() {
		sp(245659, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245659, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245659, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245659, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245659, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245659, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245659, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245659, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245659, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245659, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245659, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245659, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245659, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245659, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245659, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245659, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245659, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245659, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245659, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformTiamatWarpPr72An() {
		sp(245660, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245660, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245660, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245660, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245660, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245660, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245660, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245660, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245660, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245660, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245660, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245660, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245660, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245660, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245660, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245660, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245660, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245660, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245660, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	/*** Ranger ***/
	private void IDTransformRa72An() {
		switch (Rnd.get(1, 3)) {
			case 1:
				IDTransformEreshRa72An();
				break;
			case 2:
				IDTransformVritraRa72An();
				break;
			case 3:
				IDTransformTiamatRa72An();
				break;
		}
	}

	/*** Eresh Ranger ***/
	private void IDTransformEreshRa72An() {
		spawn(245733, 543.0933f, 557.8653f, 322.0f, (byte) 70);
		spawn(245733, 550.4571f, 546.09937f, 322.0f, (byte) 61);
		spawn(245733, 550.2217f, 539.29755f, 322.0f, (byte) 59);
		spawn(245733, 543.0761f, 534.39087f, 322.0f, (byte) 43);
		spawn(245733, 534.18945f, 534.63385f, 321.875f, (byte) 21);
		spawn(245733, 522.56396f, 467.8957f, 322.0f, (byte) 37);
		spawn(245733, 527.70734f, 475.95486f, 322.0f, (byte) 7);
		spawn(245733, 509.6404f, 476.19836f, 322.0f, (byte) 118);
		spawn(245733, 501.61966f, 467.60513f, 322.0f, (byte) 5);
		spawn(245733, 503.2415f, 446.9365f, 321.8276f, (byte) 18);
		spawn(245733, 542.8608f, 472.11404f, 322.0f, (byte) 51);
		spawn(245733, 539.9602f, 467.72766f, 322.0f, (byte) 50);
		spawn(245733, 516.02167f, 453.723f, 321.875f, (byte) 60);
		spawn(245733, 466.30823f, 509.6083f, 342.1901f, (byte) 0);
		spawn(245733, 466.1356f, 516.46765f, 342.1901f, (byte) 0);
		spawn(245733, 468.7195f, 513.0958f, 342.1901f, (byte) 0);
		spawn(245733, 446.91077f, 509.62943f, 342.2713f, (byte) 1);
		spawn(245733, 446.86057f, 517.2843f, 342.27173f, (byte) 0);
		spawn(245733, 449.71277f, 513.23883f, 342.24753f, (byte) 0);
		spawn(245733, 427.54434f, 509.3018f, 342.43564f, (byte) 0);
		spawn(245733, 427.4629f, 517.19116f, 342.43634f, (byte) 0);
		spawn(245733, 430.6331f, 513.1085f, 342.40942f, (byte) 0);
		spawn(245733, 483.40283f, 475.9793f, 345.70047f, (byte) 81);
		spawn(245733, 469.33865f, 454.01733f, 345.70047f, (byte) 4);
		spawn(245733, 482.78824f, 526.4342f, 342.234f, (byte) 78);
		spawn(245733, 502.18372f, 508.00098f, 339.63123f, (byte) 39);
		spawn(245733, 497.84283f, 521.77655f, 339.63815f, (byte) 77);
		spawn(245733, 508.98663f, 527.061f, 339.63126f, (byte) 76);
		spawn(245733, 502.5523f, 649.1053f, 317.08282f, (byte) 109);
		spawn(245733, 522.4196f, 650.3398f, 317.08282f, (byte) 99);
		spawn(245733, 540.84265f, 652.0684f, 316.16275f, (byte) 100);
		spawn(245733, 540.5331f, 637.4856f, 316.17746f, (byte) 17);
		spawn(245733, 514.6148f, 641.9673f, 317.10098f, (byte) 33);
		spawn(245733, 539.03076f, 536.819f, 322.0f, (byte) 30);
	}

	/*** Vritra Ranger ***/
	private void IDTransformVritraRa72An() {
		spawn(245734, 543.0933f, 557.8653f, 322.0f, (byte) 70);
		spawn(245734, 550.4571f, 546.09937f, 322.0f, (byte) 61);
		spawn(245734, 550.2217f, 539.29755f, 322.0f, (byte) 59);
		spawn(245734, 543.0761f, 534.39087f, 322.0f, (byte) 43);
		spawn(245734, 534.18945f, 534.63385f, 321.875f, (byte) 21);
		spawn(245734, 522.56396f, 467.8957f, 322.0f, (byte) 37);
		spawn(245734, 527.70734f, 475.95486f, 322.0f, (byte) 7);
		spawn(245734, 509.6404f, 476.19836f, 322.0f, (byte) 118);
		spawn(245734, 501.61966f, 467.60513f, 322.0f, (byte) 5);
		spawn(245734, 503.2415f, 446.9365f, 321.8276f, (byte) 18);
		spawn(245734, 542.8608f, 472.11404f, 322.0f, (byte) 51);
		spawn(245734, 539.9602f, 467.72766f, 322.0f, (byte) 50);
		spawn(245734, 516.02167f, 453.723f, 321.875f, (byte) 60);
		spawn(245734, 466.30823f, 509.6083f, 342.1901f, (byte) 0);
		spawn(245734, 466.1356f, 516.46765f, 342.1901f, (byte) 0);
		spawn(245734, 468.7195f, 513.0958f, 342.1901f, (byte) 0);
		spawn(245734, 446.91077f, 509.62943f, 342.2713f, (byte) 1);
		spawn(245734, 446.86057f, 517.2843f, 342.27173f, (byte) 0);
		spawn(245734, 449.71277f, 513.23883f, 342.24753f, (byte) 0);
		spawn(245734, 427.54434f, 509.3018f, 342.43564f, (byte) 0);
		spawn(245734, 427.4629f, 517.19116f, 342.43634f, (byte) 0);
		spawn(245734, 430.6331f, 513.1085f, 342.40942f, (byte) 0);
		spawn(245734, 483.40283f, 475.9793f, 345.70047f, (byte) 81);
		spawn(245734, 469.33865f, 454.01733f, 345.70047f, (byte) 4);
		spawn(245734, 482.78824f, 526.4342f, 342.234f, (byte) 78);
		spawn(245734, 502.18372f, 508.00098f, 339.63123f, (byte) 39);
		spawn(245734, 497.84283f, 521.77655f, 339.63815f, (byte) 77);
		spawn(245734, 508.98663f, 527.061f, 339.63126f, (byte) 76);
		spawn(245734, 502.5523f, 649.1053f, 317.08282f, (byte) 109);
		spawn(245734, 522.4196f, 650.3398f, 317.08282f, (byte) 99);
		spawn(245734, 540.84265f, 652.0684f, 316.16275f, (byte) 100);
		spawn(245734, 540.5331f, 637.4856f, 316.17746f, (byte) 17);
		spawn(245734, 514.6148f, 641.9673f, 317.10098f, (byte) 33);
		spawn(245734, 539.03076f, 536.819f, 322.0f, (byte) 30);
	}

	/*** Tiamat Ranger ***/
	private void IDTransformTiamatRa72An() {
		spawn(245735, 543.0933f, 557.8653f, 322.0f, (byte) 70);
		spawn(245735, 550.4571f, 546.09937f, 322.0f, (byte) 61);
		spawn(245735, 550.2217f, 539.29755f, 322.0f, (byte) 59);
		spawn(245735, 543.0761f, 534.39087f, 322.0f, (byte) 43);
		spawn(245735, 534.18945f, 534.63385f, 321.875f, (byte) 21);
		spawn(245735, 522.56396f, 467.8957f, 322.0f, (byte) 37);
		spawn(245735, 527.70734f, 475.95486f, 322.0f, (byte) 7);
		spawn(245735, 509.6404f, 476.19836f, 322.0f, (byte) 118);
		spawn(245735, 501.61966f, 467.60513f, 322.0f, (byte) 5);
		spawn(245735, 503.2415f, 446.9365f, 321.8276f, (byte) 18);
		spawn(245735, 542.8608f, 472.11404f, 322.0f, (byte) 51);
		spawn(245735, 539.9602f, 467.72766f, 322.0f, (byte) 50);
		spawn(245735, 516.02167f, 453.723f, 321.875f, (byte) 60);
		spawn(245735, 466.30823f, 509.6083f, 342.1901f, (byte) 0);
		spawn(245735, 466.1356f, 516.46765f, 342.1901f, (byte) 0);
		spawn(245735, 468.7195f, 513.0958f, 342.1901f, (byte) 0);
		spawn(245735, 446.91077f, 509.62943f, 342.2713f, (byte) 1);
		spawn(245735, 446.86057f, 517.2843f, 342.27173f, (byte) 0);
		spawn(245735, 449.71277f, 513.23883f, 342.24753f, (byte) 0);
		spawn(245735, 427.54434f, 509.3018f, 342.43564f, (byte) 0);
		spawn(245735, 427.4629f, 517.19116f, 342.43634f, (byte) 0);
		spawn(245735, 430.6331f, 513.1085f, 342.40942f, (byte) 0);
		spawn(245735, 483.40283f, 475.9793f, 345.70047f, (byte) 81);
		spawn(245735, 469.33865f, 454.01733f, 345.70047f, (byte) 4);
		spawn(245735, 482.78824f, 526.4342f, 342.234f, (byte) 78);
		spawn(245735, 502.18372f, 508.00098f, 339.63123f, (byte) 39);
		spawn(245735, 497.84283f, 521.77655f, 339.63815f, (byte) 77);
		spawn(245735, 508.98663f, 527.061f, 339.63126f, (byte) 76);
		spawn(245735, 502.5523f, 649.1053f, 317.08282f, (byte) 109);
		spawn(245735, 522.4196f, 650.3398f, 317.08282f, (byte) 99);
		spawn(245735, 540.84265f, 652.0684f, 316.16275f, (byte) 100);
		spawn(245735, 540.5331f, 637.4856f, 316.17746f, (byte) 17);
		spawn(245735, 514.6148f, 641.9673f, 317.10098f, (byte) 33);
		spawn(245735, 539.03076f, 536.819f, 322.0f, (byte) 30);
	}

	/*** Player Lvl 73 ***/
	/*** Eresh High ***/
	private void IDTransformEreshHigh73An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformEreshHighFi73An();
				break;
			case 2:
				IDTransformEreshHighAs73An();
				break;
			case 3:
				IDTransformEreshHighWi73An();
				break;
			case 4:
				IDTransformEreshHighPr73An();
				break;
		}
	}

	private void IDTransformEreshHighFi73An() {
		spawn(244757, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244757, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244757, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244757, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244757, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244757, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244757, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244757, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244757, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244757, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244757, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244757, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244757, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244757, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244757, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244757, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244757, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformEreshHighAs73An() {
		spawn(244758, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244758, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244758, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244758, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244758, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244758, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244758, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244758, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244758, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244758, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244758, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244758, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244758, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244758, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244758, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244758, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244758, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformEreshHighWi73An() {
		spawn(244759, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244759, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244759, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244759, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244759, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244759, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244759, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244759, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244759, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244759, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244759, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244759, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244759, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244759, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244759, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244759, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244759, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformEreshHighPr73An() {
		spawn(244760, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244760, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244760, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244760, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244760, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244760, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244760, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244760, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244760, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244760, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244760, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244760, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244760, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244760, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244760, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244760, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244760, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	/*** Vritra High ***/
	private void IDTransformVritraHigh73An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformVritraHighFi73An();
				break;
			case 2:
				IDTransformVritraHighAs73An();
				break;
			case 3:
				IDTransformVritraHighWi73An();
				break;
			case 4:
				IDTransformVritraHighPr73An();
				break;
		}
	}

	private void IDTransformVritraHighFi73An() {
		spawn(244761, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244761, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244761, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244761, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244761, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244761, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244761, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244761, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244761, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244761, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244761, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244761, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244761, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244761, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244761, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244761, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244761, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformVritraHighAs73An() {
		spawn(244762, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244762, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244762, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244762, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244762, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244762, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244762, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244762, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244762, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244762, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244762, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244762, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244762, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244762, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244762, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244762, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244762, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformVritraHighWi73An() {
		spawn(244763, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244763, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244763, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244763, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244763, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244763, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244763, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244763, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244763, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244763, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244763, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244763, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244763, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244763, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244763, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244763, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244763, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformVritraHighPr73An() {
		spawn(244764, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244764, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244764, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244764, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244764, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244764, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244764, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244764, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244764, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244764, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244764, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244764, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244764, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244764, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244764, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244764, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244764, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	/*** Tiamat High ***/
	private void IDTransformTiamatHigh73An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformTiamatHighFi73An();
				break;
			case 2:
				IDTransformTiamatHighAs73An();
				break;
			case 3:
				IDTransformTiamatHighWi73An();
				break;
			case 4:
				IDTransformTiamatHighPr73An();
				break;
		}
	}

	private void IDTransformTiamatHighFi73An() {
		spawn(244765, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244765, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244765, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244765, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244765, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244765, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244765, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244765, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244765, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244765, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244765, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244765, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244765, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244765, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244765, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244765, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244765, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformTiamatHighAs73An() {
		spawn(244766, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244766, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244766, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244766, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244766, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244766, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244766, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244766, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244766, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244766, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244766, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244766, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244766, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244766, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244766, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244766, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244766, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformTiamatHighWi73An() {
		spawn(244767, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244767, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244767, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244767, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244767, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244767, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244767, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244767, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244767, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244767, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244767, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244767, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244767, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244767, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244767, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244767, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244767, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformTiamatHighPr73An() {
		spawn(244768, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244768, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244768, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244768, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244768, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244768, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244768, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244768, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244768, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244768, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244768, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244768, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244768, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244768, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244768, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244768, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244768, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	/*** Eresh Guard ***/
	private void IDTransformEreshGuard73An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformEreshGuardFi73An();
				break;
			case 2:
				IDTransformEreshGuardAs73An();
				break;
			case 3:
				IDTransformEreshGuardWi73An();
				break;
			case 4:
				IDTransformEreshGuardPr73An();
				break;
		}
	}

	private void IDTransformEreshGuardFi73An() {
		spawn(244745, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244745, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244745, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244745, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244745, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244745, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244745, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244745, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244745, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244745, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244745, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244745, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244745, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244745, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244745, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244745, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244745, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244745, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244745, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244745, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244745, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244745, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244745, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244745, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244745, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244745, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244745, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244745, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244745, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244745, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244745, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244745, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244745, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244745, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244745, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244745, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244745, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244745, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244745, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244745, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244745, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformEreshGuardAs73An() {
		spawn(244746, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244746, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244746, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244746, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244746, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244746, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244746, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244746, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244746, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244746, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244746, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244746, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244746, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244746, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244746, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244746, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244746, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244746, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244746, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244746, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244746, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244746, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244746, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244746, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244746, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244746, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244746, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244746, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244746, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244746, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244746, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244746, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244746, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244746, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244746, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244746, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244746, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244746, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244746, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244746, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244746, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformEreshGuardWi73An() {
		spawn(244747, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244747, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244747, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244747, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244747, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244747, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244747, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244747, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244747, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244747, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244747, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244747, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244747, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244747, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244747, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244747, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244747, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244747, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244747, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244747, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244747, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244747, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244747, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244747, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244747, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244747, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244747, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244747, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244747, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244747, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244747, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244747, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244747, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244747, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244747, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244747, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244747, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244747, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244747, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244747, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244747, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformEreshGuardPr73An() {
		spawn(244748, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244748, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244748, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244748, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244748, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244748, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244748, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244748, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244748, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244748, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244748, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244748, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244748, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244748, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244748, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244748, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244748, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244748, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244748, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244748, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244748, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244748, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244748, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244748, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244748, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244748, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244748, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244748, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244748, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244748, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244748, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244748, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244748, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244748, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244748, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244748, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244748, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244748, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244748, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244748, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244748, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	/*** Vritra Guard ***/
	private void IDTransformVritraGuard73An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformVritraGuardFi73An();
				break;
			case 2:
				IDTransformVritraGuardAs73An();
				break;
			case 3:
				IDTransformVritraGuardWi73An();
				break;
			case 4:
				IDTransformVritraGuardPr73An();
				break;
		}
	}

	private void IDTransformVritraGuardFi73An() {
		spawn(244749, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244749, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244749, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244749, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244749, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244749, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244749, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244749, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244749, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244749, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244749, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244749, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244749, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244749, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244749, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244749, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244749, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244749, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244749, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244749, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244749, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244749, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244749, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244749, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244749, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244749, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244749, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244749, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244749, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244749, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244749, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244749, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244749, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244749, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244749, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244749, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244749, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244749, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244749, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244749, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244749, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformVritraGuardAs73An() {
		spawn(244750, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244750, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244750, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244750, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244750, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244750, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244750, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244750, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244750, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244750, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244750, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244750, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244750, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244750, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244750, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244750, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244750, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244750, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244750, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244750, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244750, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244750, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244750, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244750, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244750, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244750, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244750, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244750, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244750, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244750, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244750, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244750, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244750, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244750, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244750, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244750, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244750, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244750, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244750, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244750, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244750, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformVritraGuardWi73An() {
		spawn(244751, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244751, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244751, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244751, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244751, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244751, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244751, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244751, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244751, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244751, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244751, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244751, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244751, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244751, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244751, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244751, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244751, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244751, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244751, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244751, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244751, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244751, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244751, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244751, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244751, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244751, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244751, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244751, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244751, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244751, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244751, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244751, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244751, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244751, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244751, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244751, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244751, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244751, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244751, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244751, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244751, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformVritraGuardPr73An() {
		spawn(244752, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244752, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244752, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244752, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244752, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244752, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244752, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244752, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244752, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244752, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244752, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244752, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244752, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244752, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244752, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244752, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244752, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244752, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244752, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244752, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244752, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244752, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244752, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244752, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244752, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244752, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244752, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244752, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244752, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244752, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244752, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244752, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244752, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244752, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244752, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244752, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244752, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244752, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244752, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244752, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244752, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	/*** Tiamat Guard ***/
	private void IDTransformTiamatGuard73An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformTiamatGuardFi73An();
				break;
			case 2:
				IDTransformTiamatGuardAs73An();
				break;
			case 3:
				IDTransformTiamatGuardWi73An();
				break;
			case 4:
				IDTransformTiamatGuardPr73An();
				break;
		}
	}

	private void IDTransformTiamatGuardFi73An() {
		spawn(244753, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244753, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244753, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244753, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244753, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244753, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244753, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244753, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244753, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244753, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244753, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244753, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244753, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244753, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244753, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244753, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244753, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244753, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244753, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244753, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244753, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244753, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244753, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244753, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244753, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244753, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244753, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244753, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244753, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244753, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244753, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244753, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244753, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244753, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244753, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244753, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244753, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244753, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244753, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244753, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244753, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformTiamatGuardAs73An() {
		spawn(244754, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244754, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244754, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244754, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244754, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244754, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244754, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244754, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244754, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244754, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244754, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244754, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244754, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244754, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244754, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244754, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244754, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244754, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244754, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244754, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244754, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244754, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244754, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244754, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244754, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244754, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244754, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244754, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244754, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244754, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244754, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244754, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244754, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244754, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244754, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244754, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244754, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244754, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244754, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244754, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244754, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformTiamatGuardWi73An() {
		spawn(244755, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244755, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244755, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244755, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244755, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244755, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244755, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244755, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244755, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244755, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244755, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244755, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244755, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244755, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244755, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244755, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244755, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244755, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244755, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244755, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244755, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244755, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244755, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244755, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244755, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244755, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244755, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244755, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244755, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244755, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244755, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244755, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244755, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244755, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244755, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244755, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244755, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244755, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244755, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244755, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244755, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformTiamatGuardPr73An() {
		spawn(244756, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244756, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244756, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244756, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244756, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244756, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244756, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244756, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244756, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244756, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244756, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244756, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244756, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244756, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244756, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244756, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244756, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244756, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244756, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244756, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244756, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244756, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244756, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244756, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244756, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244756, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244756, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244756, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244756, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244756, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244756, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244756, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244756, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244756, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244756, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244756, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244756, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244756, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244756, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244756, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244756, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	/*** Deva Guard ***/
	private void IDTransformDevaGuardFi73An() {
		spawn(244772, 543.9115f, 491.4599f, 322.04422f, (byte) 91);
		spawn(244772, 547.9682f, 490.34494f, 321.50793f, (byte) 90);
		spawn(244772, 534.53534f, 485.35455f, 322.0f, (byte) 89);
		spawn(244772, 541.57935f, 487.14307f, 322.0f, (byte) 89);
		spawn(244772, 516.4117f, 585.5329f, 321.97427f, (byte) 90);
		spawn(244772, 499.82422f, 585.81537f, 322.02252f, (byte) 92);
		spawn(244772, 501.61078f, 555.0141f, 321.84106f, (byte) 12);
	}

	private void IDTransformDevaGuardAs73An() {
		spawn(244773, 855.63855f, 484.74774f, 349.08722f, (byte) 45);
		spawn(244773, 852.21844f, 482.80756f, 349.41986f, (byte) 44);
		spawn(244773, 864.0118f, 492.74384f, 349.56552f, (byte) 46);
		spawn(244773, 508.17993f, 587.8108f, 322.56973f, (byte) 90);
		spawn(244773, 526.8684f, 493.7475f, 322.0f, (byte) 91);
		spawn(244773, 537.37225f, 508.5786f, 322.0f, (byte) 90);
		spawn(244773, 512.8187f, 590.4575f, 322.56216f, (byte) 90);
		spawn(244773, 504.50745f, 590.3867f, 322.56216f, (byte) 90);
		spawn(244773, 482.3006f, 496.94232f, 342.22174f, (byte) 27);
		spawn(244773, 594.4832f, 673.60126f, 350.85538f, (byte) 54);
		spawn(244773, 634.298f, 511.2995f, 339.61545f, (byte) 61);
		spawn(244773, 530.982f, 493.62543f, 322.0f, (byte) 90);
		spawn(244773, 641.9954f, 514.2143f, 339.61542f, (byte) 61);
		spawn(244773, 505.45636f, 584.90186f, 322.0f, (byte) 90);
		spawn(244773, 510.66394f, 584.80176f, 322.0f, (byte) 90);
		spawn(244773, 859.2291f, 490.53555f, 348.98047f, (byte) 45);
		spawn(244773, 604.1567f, 635.69995f, 352.53815f, (byte) 30);
		spawn(244773, 467.0f, 476.0f, 345.70047f, (byte) 32);
		spawn(244773, 536.2914f, 488.65588f, 322.0f, (byte) 90);
		spawn(244773, 639.6124f, 525.6597f, 339.61542f, (byte) 62);
		spawn(244773, 885.47394f, 464.9668f, 351.0f, (byte) 66);
		spawn(244773, 642.88293f, 520.17224f, 339.61542f, (byte) 60);
		spawn(244773, 546.91925f, 510.66574f, 321.94254f, (byte) 91);
		spawn(244773, 527.7633f, 502.87167f, 321.6398f, (byte) 91);
		spawn(244773, 885.5619f, 456.29126f, 351.02737f, (byte) 55);
	}

	private void IDTransformDevaGuardWi73An() {
		spawn(244774, 513.0236f, 593.86285f, 322.56216f, (byte) 90);
		spawn(244774, 501.3256f, 593.69885f, 322.56216f, (byte) 90);
		spawn(244774, 633.75323f, 516.50934f, 339.61545f, (byte) 61);
		spawn(244774, 544.93945f, 499.1886f, 322.0f, (byte) 91);
		spawn(244774, 530.8947f, 502.38684f, 321.86185f, (byte) 91);
		spawn(244774, 634.5065f, 522.0903f, 339.61545f, (byte) 53);
		spawn(244774, 885.382f, 460.77805f, 351.0f, (byte) 60);
		spawn(244774, 612.8048f, 643.80194f, 352.46414f, (byte) 30);
		spawn(244774, 538.00024f, 495.57394f, 322.0f, (byte) 90);
		spawn(244774, 526.9178f, 486.92227f, 321.87527f, (byte) 85);
	}

	private void IDTransformDevaGuardRa73An() {
		spawn(244775, 529.87f, 545.5564f, 321.85876f, (byte) 95);
		spawn(244775, 526.1612f, 545.1311f, 321.58078f, (byte) 88);
		spawn(244775, 546.40234f, 523.6142f, 321.97485f, (byte) 87);
		spawn(244775, 549.9647f, 524.9884f, 321.9978f, (byte) 95);
		spawn(244775, 520.9293f, 586.19006f, 321.94193f, (byte) 95);
		spawn(244775, 495.259f, 586.9457f, 322.0659f, (byte) 81);
		spawn(244775, 505.0709f, 552.7453f, 322.0f, (byte) 28);
	}

	/*** Eresh Stumble ***/
	private void IDTransformEreshStumble73An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformEreshStumbleFi73An();
				break;
			case 2:
				IDTransformEreshStumbleAs73An();
				break;
			case 3:
				IDTransformEreshStumbleWi73An();
				break;
			case 4:
				IDTransformEreshStumblePr73An();
				break;
		}
	}

	private void IDTransformEreshStumbleFi73An() {
		spawn(245081, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(245081, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(245081, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformEreshStumbleAs73An() {
		spawn(245082, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(245082, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(245082, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformEreshStumbleWi73An() {
		spawn(245083, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(245083, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(245083, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformEreshStumblePr73An() {
		spawn(245084, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(245084, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(245084, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	/*** Vritra Stumble ***/
	private void IDTransformVritraStumble73An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformVritraStumbleFi73An();
				break;
			case 2:
				IDTransformVritraStumbleAs73An();
				break;
			case 3:
				IDTransformVritraStumbleWi73An();
				break;
			case 4:
				IDTransformVritraStumblePr73An();
				break;
		}
	}

	private void IDTransformVritraStumbleFi73An() {
		spawn(245085, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(245085, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(245085, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformVritraStumbleAs73An() {
		spawn(245086, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(245086, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(245086, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformVritraStumbleWi73An() {
		spawn(245087, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(245087, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(245087, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformVritraStumblePr73An() {
		spawn(245088, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(245088, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(245088, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	/*** Tiamat Stumble ***/
	private void IDTransformTiamatStumble73An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformTiamatStumbleFi73An();
				break;
			case 2:
				IDTransformTiamatStumbleAs73An();
				break;
			case 3:
				IDTransformTiamatStumbleWi73An();
				break;
			case 4:
				IDTransformTiamatStumblePr73An();
				break;
		}
	}

	private void IDTransformTiamatStumbleFi73An() {
		spawn(245089, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(245089, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(245089, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformTiamatStumbleAs73An() {
		spawn(245090, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(245090, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(245090, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformTiamatStumbleWi73An() {
		spawn(245091, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(245091, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(245091, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformTiamatStumblePr73An() {
		spawn(245092, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(245092, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(245092, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	/*** Deva Stumble ***/
	private void IDTransformDevaStumble73An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformDevaStumbleFi73An();
				break;
			case 2:
				IDTransformDevaStumbleAs73An();
				break;
			case 3:
				IDTransformDevaStumbleWi73An();
				break;
			case 4:
				IDTransformDevaStumblePr73An();
				break;
		}
	}

	private void IDTransformDevaStumbleFi73An() {
		spawn(245093, 677.0f, 516.0f, 338.24844f, (byte) 43);
		spawn(245093, 505.28098f, 648.23944f, 317.08282f, (byte) 113);
		spawn(245093, 507.24606f, 628.40027f, 318.4649f, (byte) 61);
		spawn(245093, 504.39532f, 615.5569f, 320.3689f, (byte) 61);
		spawn(245093, 504.10117f, 601.42255f, 322.4643f, (byte) 60);
		spawn(245093, 513.7057f, 643.4998f, 317.10632f, (byte) 11);
		spawn(245093, 511.02466f, 614.0008f, 320.5996f, (byte) 0);
		spawn(245093, 503.81223f, 640.00806f, 317.11395f, (byte) 40);
		spawn(245093, 531.7686f, 642.5042f, 317.08282f, (byte) 112);
		spawn(245093, 522.0014f, 648.79095f, 317.08282f, (byte) 55);
	}

	private void IDTransformDevaStumbleAs73An() {
		spawn(245094, 677.0f, 516.0f, 338.24844f, (byte) 43);
		spawn(245094, 505.28098f, 648.23944f, 317.08282f, (byte) 113);
		spawn(245094, 507.24606f, 628.40027f, 318.4649f, (byte) 61);
		spawn(245094, 504.39532f, 615.5569f, 320.3689f, (byte) 61);
		spawn(245094, 504.10117f, 601.42255f, 322.4643f, (byte) 60);
		spawn(245094, 513.7057f, 643.4998f, 317.10632f, (byte) 11);
		spawn(245094, 511.02466f, 614.0008f, 320.5996f, (byte) 0);
		spawn(245094, 503.81223f, 640.00806f, 317.11395f, (byte) 40);
		spawn(245094, 531.7686f, 642.5042f, 317.08282f, (byte) 112);
		spawn(245094, 522.0014f, 648.79095f, 317.08282f, (byte) 55);
	}

	private void IDTransformDevaStumbleWi73An() {
		spawn(245095, 677.0f, 516.0f, 338.24844f, (byte) 43);
		spawn(245095, 505.28098f, 648.23944f, 317.08282f, (byte) 113);
		spawn(245095, 507.24606f, 628.40027f, 318.4649f, (byte) 61);
		spawn(245095, 504.39532f, 615.5569f, 320.3689f, (byte) 61);
		spawn(245095, 504.10117f, 601.42255f, 322.4643f, (byte) 60);
		spawn(245095, 513.7057f, 643.4998f, 317.10632f, (byte) 11);
		spawn(245095, 511.02466f, 614.0008f, 320.5996f, (byte) 0);
		spawn(245095, 503.81223f, 640.00806f, 317.11395f, (byte) 40);
		spawn(245095, 531.7686f, 642.5042f, 317.08282f, (byte) 112);
		spawn(245095, 522.0014f, 648.79095f, 317.08282f, (byte) 55);
	}

	private void IDTransformDevaStumblePr73An() {
		spawn(245096, 677.0f, 516.0f, 338.24844f, (byte) 43);
		spawn(245096, 505.28098f, 648.23944f, 317.08282f, (byte) 113);
		spawn(245096, 507.24606f, 628.40027f, 318.4649f, (byte) 61);
		spawn(245096, 504.39532f, 615.5569f, 320.3689f, (byte) 61);
		spawn(245096, 504.10117f, 601.42255f, 322.4643f, (byte) 60);
		spawn(245096, 513.7057f, 643.4998f, 317.10632f, (byte) 11);
		spawn(245096, 511.02466f, 614.0008f, 320.5996f, (byte) 0);
		spawn(245096, 503.81223f, 640.00806f, 317.11395f, (byte) 40);
		spawn(245096, 531.7686f, 642.5042f, 317.08282f, (byte) 112);
		spawn(245096, 522.0014f, 648.79095f, 317.08282f, (byte) 55);
	}

	/*** Drop Dragon ***/
	private void IDTransformDropDragon73Ae() {
		switch (Rnd.get(1, 3)) {
			case 1:
				spawn(245109, 622.6814f, 551.9144f, 346.06897f, (byte) 105);
				break;
			case 2:
				spawn(245110, 622.6814f, 551.9144f, 346.06897f, (byte) 105);
				break;
			case 3:
				spawn(245111, 622.6814f, 551.9144f, 346.06897f, (byte) 105);
				break;
		}
		switch (Rnd.get(1, 3)) {
			case 1:
				spawn(245109, 607.8992f, 674.06934f, 352.29062f, (byte) 90);
				break;
			case 2:
				spawn(245110, 607.8992f, 674.06934f, 352.29062f, (byte) 90);
				break;
			case 3:
				spawn(245111, 607.8992f, 674.06934f, 352.29062f, (byte) 90);
				break;
		}
	}

	/*** Bonus Monster ***/
	private void IDTransformBonusMonsterTypeA73An() {
		spawn(246228, 761.54095f, 562.17f, 341.0512f, (byte) 90);
		spawn(246228, 476.94467f, 549.22363f, 345.6048f, (byte) 90);
		spawn(246228, 609.2811f, 707.6352f, 355.10846f, (byte) 93);
	}

	private void IDTransformBonusMonsterTypeB73An() {
		spawn(246229, 761.54095f, 562.17f, 341.0512f, (byte) 90);
		spawn(246229, 476.94467f, 549.22363f, 345.6048f, (byte) 90);
		spawn(246229, 609.2811f, 707.6352f, 355.10846f, (byte) 93);
	}

	private void IDTransformBonusMonsterTypeC73An() {
		spawn(246230, 761.54095f, 562.17f, 341.0512f, (byte) 90);
		spawn(246230, 476.94467f, 549.22363f, 345.6048f, (byte) 90);
		spawn(246230, 609.2811f, 707.6352f, 355.10846f, (byte) 93);
	}

	private void IDTransformBonusMonsterTypeD73An() {
		spawn(246231, 761.54095f, 562.17f, 341.0512f, (byte) 90);
		spawn(246231, 476.94467f, 549.22363f, 345.6048f, (byte) 90);
		spawn(246231, 609.2811f, 707.6352f, 355.10846f, (byte) 93);
	}

	/*** Eresh Warp ***/
	private void IDTransformEreshWarp73() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformEreshWarpFi73An();
				break;
			case 2:
				IDTransformEreshWarpAs73An();
				break;
			case 3:
				IDTransformEreshWarpWi73An();
				break;
			case 4:
				IDTransformEreshWarpPr73An();
				break;
		}
	}

	private void IDTransformEreshWarpFi73An() {
		sp(245661, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245661, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245661, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245661, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245661, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245661, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245661, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245661, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245661, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245661, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245661, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245661, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245661, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245661, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245661, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245661, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245661, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245661, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245661, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformEreshWarpAs73An() {
		sp(245662, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245662, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245662, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245662, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245662, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245662, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245662, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245662, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245662, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245662, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245662, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245662, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245662, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245662, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245662, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245662, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245662, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245662, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245662, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformEreshWarpWi73An() {
		sp(245663, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245663, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245663, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245663, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245663, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245663, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245663, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245663, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245663, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245663, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245663, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245663, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245663, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245663, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245663, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245663, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245663, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245663, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245663, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformEreshWarpPr73An() {
		sp(245664, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245664, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245664, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245664, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245664, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245664, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245664, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245664, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245664, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245664, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245664, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245664, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245664, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245664, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245664, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245664, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245664, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245664, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245664, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	/*** Vritra Warp ***/
	private void IDTransformVritraWarp73() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformVritraWarpFi73An();
				break;
			case 2:
				IDTransformVritraWarpAs73An();
				break;
			case 3:
				IDTransformVritraWarpWi73An();
				break;
			case 4:
				IDTransformVritraWarpPr73An();
				break;
		}
	}

	private void IDTransformVritraWarpFi73An() {
		sp(245665, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245665, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245665, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245665, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245665, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245665, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245665, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245665, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245665, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245665, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245665, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245665, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245665, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245665, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245665, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245665, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245665, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245665, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245665, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformVritraWarpAs73An() {
		sp(245666, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245666, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245666, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245666, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245666, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245666, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245666, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245666, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245666, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245666, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245666, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245666, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245666, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245666, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245666, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245666, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245666, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245666, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245666, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformVritraWarpWi73An() {
		sp(245667, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245667, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245667, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245667, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245667, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245667, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245667, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245667, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245667, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245667, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245667, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245667, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245667, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245667, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245667, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245667, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245667, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245667, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245667, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformVritraWarpPr73An() {
		sp(245668, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245668, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245668, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245668, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245668, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245668, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245668, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245668, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245668, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245668, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245668, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245668, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245668, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245668, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245668, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245668, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245668, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245668, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245668, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	/*** Tiamat Warp ***/
	private void IDTransformTiamatWarp73() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformTiamatWarpFi73An();
				break;
			case 2:
				IDTransformTiamatWarpAs73An();
				break;
			case 3:
				IDTransformTiamatWarpWi73An();
				break;
			case 4:
				IDTransformTiamatWarpPr73An();
				break;
		}
	}

	private void IDTransformTiamatWarpFi73An() {
		sp(245669, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245669, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245669, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245669, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245669, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245669, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245669, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245669, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245669, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245669, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245669, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245669, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245669, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245669, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245669, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245669, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245669, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245669, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245669, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformTiamatWarpAs73An() {
		sp(245670, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245670, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245670, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245670, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245670, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245670, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245670, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245670, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245670, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245670, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245670, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245670, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245670, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245670, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245670, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245670, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245670, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245670, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245670, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformTiamatWarpWi73An() {
		sp(245671, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245671, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245671, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245671, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245671, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245671, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245671, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245671, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245671, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245671, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245671, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245671, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245671, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245671, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245671, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245671, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245671, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245671, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245671, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformTiamatWarpPr73An() {
		sp(245672, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245672, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245672, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245672, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245672, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245672, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245672, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245672, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245672, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245672, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245672, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245672, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245672, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245672, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245672, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245672, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245672, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245672, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245672, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	/*** Ranger ***/
	private void IDTransformRa73An() {
		switch (Rnd.get(1, 3)) {
			case 1:
				IDTransformEreshRa73An();
				break;
			case 2:
				IDTransformVritraRa73An();
				break;
			case 3:
				IDTransformTiamatRa73An();
				break;
		}
	}

	/*** Eresh Ranger ***/
	private void IDTransformEreshRa73An() {
		spawn(245739, 543.0933f, 557.8653f, 322.0f, (byte) 70);
		spawn(245739, 550.4571f, 546.09937f, 322.0f, (byte) 61);
		spawn(245739, 550.2217f, 539.29755f, 322.0f, (byte) 59);
		spawn(245739, 543.0761f, 534.39087f, 322.0f, (byte) 43);
		spawn(245739, 534.18945f, 534.63385f, 321.875f, (byte) 21);
		spawn(245739, 522.56396f, 467.8957f, 322.0f, (byte) 37);
		spawn(245739, 527.70734f, 475.95486f, 322.0f, (byte) 7);
		spawn(245739, 509.6404f, 476.19836f, 322.0f, (byte) 118);
		spawn(245739, 501.61966f, 467.60513f, 322.0f, (byte) 5);
		spawn(245739, 503.2415f, 446.9365f, 321.8276f, (byte) 18);
		spawn(245739, 542.8608f, 472.11404f, 322.0f, (byte) 51);
		spawn(245739, 539.9602f, 467.72766f, 322.0f, (byte) 50);
		spawn(245739, 516.02167f, 453.723f, 321.875f, (byte) 60);
		spawn(245739, 466.30823f, 509.6083f, 342.1901f, (byte) 0);
		spawn(245739, 466.1356f, 516.46765f, 342.1901f, (byte) 0);
		spawn(245739, 468.7195f, 513.0958f, 342.1901f, (byte) 0);
		spawn(245739, 446.91077f, 509.62943f, 342.2713f, (byte) 1);
		spawn(245739, 446.86057f, 517.2843f, 342.27173f, (byte) 0);
		spawn(245739, 449.71277f, 513.23883f, 342.24753f, (byte) 0);
		spawn(245739, 427.54434f, 509.3018f, 342.43564f, (byte) 0);
		spawn(245739, 427.4629f, 517.19116f, 342.43634f, (byte) 0);
		spawn(245739, 430.6331f, 513.1085f, 342.40942f, (byte) 0);
		spawn(245739, 483.40283f, 475.9793f, 345.70047f, (byte) 81);
		spawn(245739, 469.33865f, 454.01733f, 345.70047f, (byte) 4);
		spawn(245739, 482.78824f, 526.4342f, 342.234f, (byte) 78);
		spawn(245739, 502.18372f, 508.00098f, 339.63123f, (byte) 39);
		spawn(245739, 497.84283f, 521.77655f, 339.63815f, (byte) 77);
		spawn(245739, 508.98663f, 527.061f, 339.63126f, (byte) 76);
		spawn(245739, 502.5523f, 649.1053f, 317.08282f, (byte) 109);
		spawn(245739, 522.4196f, 650.3398f, 317.08282f, (byte) 99);
		spawn(245739, 540.84265f, 652.0684f, 316.16275f, (byte) 100);
		spawn(245739, 540.5331f, 637.4856f, 316.17746f, (byte) 17);
		spawn(245739, 514.6148f, 641.9673f, 317.10098f, (byte) 33);
		spawn(245739, 539.03076f, 536.819f, 322.0f, (byte) 30);
	}

	/*** Vritra Ranger ***/
	private void IDTransformVritraRa73An() {
		spawn(245740, 543.0933f, 557.8653f, 322.0f, (byte) 70);
		spawn(245740, 550.4571f, 546.09937f, 322.0f, (byte) 61);
		spawn(245740, 550.2217f, 539.29755f, 322.0f, (byte) 59);
		spawn(245740, 543.0761f, 534.39087f, 322.0f, (byte) 43);
		spawn(245740, 534.18945f, 534.63385f, 321.875f, (byte) 21);
		spawn(245740, 522.56396f, 467.8957f, 322.0f, (byte) 37);
		spawn(245740, 527.70734f, 475.95486f, 322.0f, (byte) 7);
		spawn(245740, 509.6404f, 476.19836f, 322.0f, (byte) 118);
		spawn(245740, 501.61966f, 467.60513f, 322.0f, (byte) 5);
		spawn(245740, 503.2415f, 446.9365f, 321.8276f, (byte) 18);
		spawn(245740, 542.8608f, 472.11404f, 322.0f, (byte) 51);
		spawn(245740, 539.9602f, 467.72766f, 322.0f, (byte) 50);
		spawn(245740, 516.02167f, 453.723f, 321.875f, (byte) 60);
		spawn(245740, 466.30823f, 509.6083f, 342.1901f, (byte) 0);
		spawn(245740, 466.1356f, 516.46765f, 342.1901f, (byte) 0);
		spawn(245740, 468.7195f, 513.0958f, 342.1901f, (byte) 0);
		spawn(245740, 446.91077f, 509.62943f, 342.2713f, (byte) 1);
		spawn(245740, 446.86057f, 517.2843f, 342.27173f, (byte) 0);
		spawn(245740, 449.71277f, 513.23883f, 342.24753f, (byte) 0);
		spawn(245740, 427.54434f, 509.3018f, 342.43564f, (byte) 0);
		spawn(245740, 427.4629f, 517.19116f, 342.43634f, (byte) 0);
		spawn(245740, 430.6331f, 513.1085f, 342.40942f, (byte) 0);
		spawn(245740, 483.40283f, 475.9793f, 345.70047f, (byte) 81);
		spawn(245740, 469.33865f, 454.01733f, 345.70047f, (byte) 4);
		spawn(245740, 482.78824f, 526.4342f, 342.234f, (byte) 78);
		spawn(245740, 502.18372f, 508.00098f, 339.63123f, (byte) 39);
		spawn(245740, 497.84283f, 521.77655f, 339.63815f, (byte) 77);
		spawn(245740, 508.98663f, 527.061f, 339.63126f, (byte) 76);
		spawn(245740, 502.5523f, 649.1053f, 317.08282f, (byte) 109);
		spawn(245740, 522.4196f, 650.3398f, 317.08282f, (byte) 99);
		spawn(245740, 540.84265f, 652.0684f, 316.16275f, (byte) 100);
		spawn(245740, 540.5331f, 637.4856f, 316.17746f, (byte) 17);
		spawn(245740, 514.6148f, 641.9673f, 317.10098f, (byte) 33);
		spawn(245740, 539.03076f, 536.819f, 322.0f, (byte) 30);
	}

	/*** Tiamat Ranger ***/
	private void IDTransformTiamatRa73An() {
		spawn(245741, 543.0933f, 557.8653f, 322.0f, (byte) 70);
		spawn(245741, 550.4571f, 546.09937f, 322.0f, (byte) 61);
		spawn(245741, 550.2217f, 539.29755f, 322.0f, (byte) 59);
		spawn(245741, 543.0761f, 534.39087f, 322.0f, (byte) 43);
		spawn(245741, 534.18945f, 534.63385f, 321.875f, (byte) 21);
		spawn(245741, 522.56396f, 467.8957f, 322.0f, (byte) 37);
		spawn(245741, 527.70734f, 475.95486f, 322.0f, (byte) 7);
		spawn(245741, 509.6404f, 476.19836f, 322.0f, (byte) 118);
		spawn(245741, 501.61966f, 467.60513f, 322.0f, (byte) 5);
		spawn(245741, 503.2415f, 446.9365f, 321.8276f, (byte) 18);
		spawn(245741, 542.8608f, 472.11404f, 322.0f, (byte) 51);
		spawn(245741, 539.9602f, 467.72766f, 322.0f, (byte) 50);
		spawn(245741, 516.02167f, 453.723f, 321.875f, (byte) 60);
		spawn(245741, 466.30823f, 509.6083f, 342.1901f, (byte) 0);
		spawn(245741, 466.1356f, 516.46765f, 342.1901f, (byte) 0);
		spawn(245741, 468.7195f, 513.0958f, 342.1901f, (byte) 0);
		spawn(245741, 446.91077f, 509.62943f, 342.2713f, (byte) 1);
		spawn(245741, 446.86057f, 517.2843f, 342.27173f, (byte) 0);
		spawn(245741, 449.71277f, 513.23883f, 342.24753f, (byte) 0);
		spawn(245741, 427.54434f, 509.3018f, 342.43564f, (byte) 0);
		spawn(245741, 427.4629f, 517.19116f, 342.43634f, (byte) 0);
		spawn(245741, 430.6331f, 513.1085f, 342.40942f, (byte) 0);
		spawn(245741, 483.40283f, 475.9793f, 345.70047f, (byte) 81);
		spawn(245741, 469.33865f, 454.01733f, 345.70047f, (byte) 4);
		spawn(245741, 482.78824f, 526.4342f, 342.234f, (byte) 78);
		spawn(245741, 502.18372f, 508.00098f, 339.63123f, (byte) 39);
		spawn(245741, 497.84283f, 521.77655f, 339.63815f, (byte) 77);
		spawn(245741, 508.98663f, 527.061f, 339.63126f, (byte) 76);
		spawn(245741, 502.5523f, 649.1053f, 317.08282f, (byte) 109);
		spawn(245741, 522.4196f, 650.3398f, 317.08282f, (byte) 99);
		spawn(245741, 540.84265f, 652.0684f, 316.16275f, (byte) 100);
		spawn(245741, 540.5331f, 637.4856f, 316.17746f, (byte) 17);
		spawn(245741, 514.6148f, 641.9673f, 317.10098f, (byte) 33);
		spawn(245741, 539.03076f, 536.819f, 322.0f, (byte) 30);
	}

	/*** Player Lvl 74 ***/
	/*** Eresh High ***/
	private void IDTransformEreshHigh74An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformEreshHighFi74An();
				break;
			case 2:
				IDTransformEreshHighAs74An();
				break;
			case 3:
				IDTransformEreshHighWi74An();
				break;
			case 4:
				IDTransformEreshHighPr74An();
				break;
		}
	}

	private void IDTransformEreshHighFi74An() {
		spawn(244798, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244798, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244798, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244798, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244798, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244798, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244798, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244798, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244798, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244798, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244798, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244798, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244798, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244798, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244798, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244798, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244798, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformEreshHighAs74An() {
		spawn(244799, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244799, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244799, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244799, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244799, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244799, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244799, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244799, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244799, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244799, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244799, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244799, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244799, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244799, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244799, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244799, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244799, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformEreshHighWi74An() {
		spawn(244800, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244800, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244800, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244800, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244800, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244800, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244800, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244800, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244800, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244800, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244800, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244800, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244800, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244800, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244800, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244800, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244800, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformEreshHighPr74An() {
		spawn(244801, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244801, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244801, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244801, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244801, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244801, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244801, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244801, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244801, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244801, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244801, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244801, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244801, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244801, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244801, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244801, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244801, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	/*** Vritra High ***/
	private void IDTransformVritraHigh74An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformVritraHighFi74An();
				break;
			case 2:
				IDTransformVritraHighAs74An();
				break;
			case 3:
				IDTransformVritraHighWi74An();
				break;
			case 4:
				IDTransformVritraHighPr74An();
				break;
		}
	}

	private void IDTransformVritraHighFi74An() {
		spawn(244802, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244802, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244802, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244802, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244802, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244802, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244802, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244802, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244802, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244802, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244802, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244802, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244802, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244802, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244802, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244802, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244802, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformVritraHighAs74An() {
		spawn(244803, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244803, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244803, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244803, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244803, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244803, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244803, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244803, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244803, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244803, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244803, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244803, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244803, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244803, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244803, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244803, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244803, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformVritraHighWi74An() {
		spawn(244804, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244804, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244804, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244804, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244804, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244804, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244804, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244804, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244804, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244804, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244804, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244804, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244804, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244804, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244804, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244804, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244804, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformVritraHighPr74An() {
		spawn(244805, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244805, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244805, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244805, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244805, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244805, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244805, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244805, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244805, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244805, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244805, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244805, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244805, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244805, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244805, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244805, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244805, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	/*** Tiamat High ***/
	private void IDTransformTiamatHigh74An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformTiamatHighFi74An();
				break;
			case 2:
				IDTransformTiamatHighAs74An();
				break;
			case 3:
				IDTransformTiamatHighWi74An();
				break;
			case 4:
				IDTransformTiamatHighPr74An();
				break;
		}
	}

	private void IDTransformTiamatHighFi74An() {
		spawn(244806, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244806, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244806, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244806, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244806, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244806, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244806, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244806, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244806, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244806, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244806, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244806, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244806, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244806, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244806, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244806, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244806, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformTiamatHighAs74An() {
		spawn(244807, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244807, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244807, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244807, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244807, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244807, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244807, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244807, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244807, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244807, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244807, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244807, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244807, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244807, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244807, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244807, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244807, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformTiamatHighWi74An() {
		spawn(244808, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244808, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244808, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244808, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244808, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244808, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244808, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244808, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244808, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244808, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244808, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244808, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244808, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244808, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244808, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244808, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244808, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformTiamatHighPr74An() {
		spawn(244809, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244809, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244809, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244809, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244809, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244809, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244809, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244809, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244809, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244809, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244809, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244809, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244809, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244809, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244809, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244809, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244809, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	/*** Eresh Guard ***/
	private void IDTransformEreshGuard74An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformEreshGuardFi74An();
				break;
			case 2:
				IDTransformEreshGuardAs74An();
				break;
			case 3:
				IDTransformEreshGuardWi74An();
				break;
			case 4:
				IDTransformEreshGuardPr74An();
				break;
		}
	}

	private void IDTransformEreshGuardFi74An() {
		spawn(244786, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244786, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244786, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244786, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244786, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244786, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244786, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244786, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244786, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244786, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244786, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244786, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244786, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244786, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244786, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244786, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244786, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244786, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244786, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244786, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244786, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244786, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244786, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244786, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244786, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244786, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244786, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244786, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244786, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244786, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244786, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244786, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244786, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244786, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244786, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244786, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244786, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244786, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244786, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244786, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244786, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformEreshGuardAs74An() {
		spawn(244787, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244787, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244787, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244787, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244787, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244787, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244787, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244787, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244787, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244787, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244787, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244787, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244787, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244787, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244787, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244787, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244787, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244787, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244787, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244787, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244787, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244787, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244787, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244787, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244787, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244787, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244787, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244787, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244787, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244787, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244787, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244787, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244787, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244787, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244787, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244787, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244787, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244787, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244787, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244787, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244787, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformEreshGuardWi74An() {
		spawn(244788, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244788, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244788, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244788, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244788, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244788, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244788, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244788, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244788, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244788, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244788, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244788, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244788, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244788, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244788, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244788, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244788, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244788, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244788, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244788, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244788, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244788, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244788, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244788, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244788, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244788, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244788, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244788, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244788, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244788, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244788, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244788, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244788, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244788, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244788, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244788, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244788, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244788, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244788, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244788, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244788, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformEreshGuardPr74An() {
		spawn(244789, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244789, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244789, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244789, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244789, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244789, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244789, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244789, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244789, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244789, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244789, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244789, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244789, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244789, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244789, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244789, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244789, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244789, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244789, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244789, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244789, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244789, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244789, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244789, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244789, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244789, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244789, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244789, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244789, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244789, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244789, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244789, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244789, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244789, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244789, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244789, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244789, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244789, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244789, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244789, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244789, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	/*** Vritra Guard ***/
	private void IDTransformVritraGuard74An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformVritraGuardFi74An();
				break;
			case 2:
				IDTransformVritraGuardAs74An();
				break;
			case 3:
				IDTransformVritraGuardWi74An();
				break;
			case 4:
				IDTransformVritraGuardPr74An();
				break;
		}
	}

	private void IDTransformVritraGuardFi74An() {
		spawn(244790, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244790, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244790, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244790, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244790, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244790, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244790, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244790, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244790, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244790, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244790, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244790, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244790, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244790, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244790, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244790, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244790, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244790, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244790, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244790, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244790, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244790, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244790, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244790, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244790, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244790, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244790, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244790, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244790, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244790, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244790, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244790, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244790, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244790, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244790, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244790, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244790, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244790, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244790, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244790, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244790, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformVritraGuardAs74An() {
		spawn(244791, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244791, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244791, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244791, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244791, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244791, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244791, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244791, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244791, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244791, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244791, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244791, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244791, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244791, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244791, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244791, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244791, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244791, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244791, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244791, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244791, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244791, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244791, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244791, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244791, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244791, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244791, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244791, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244791, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244791, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244791, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244791, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244791, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244791, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244791, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244791, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244791, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244791, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244791, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244791, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244791, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformVritraGuardWi74An() {
		spawn(244792, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244792, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244792, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244792, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244792, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244792, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244792, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244792, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244792, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244792, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244792, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244792, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244792, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244792, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244792, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244792, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244792, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244792, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244792, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244792, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244792, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244792, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244792, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244792, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244792, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244792, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244792, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244792, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244792, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244792, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244792, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244792, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244792, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244792, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244792, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244792, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244792, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244792, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244792, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244792, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244792, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformVritraGuardPr74An() {
		spawn(244793, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244793, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244793, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244793, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244793, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244793, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244793, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244793, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244793, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244793, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244793, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244793, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244793, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244793, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244793, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244793, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244793, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244793, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244793, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244793, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244793, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244793, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244793, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244793, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244793, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244793, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244793, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244793, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244793, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244793, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244793, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244793, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244793, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244793, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244793, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244793, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244793, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244793, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244793, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244793, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244793, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	/*** Tiamat Guard ***/
	private void IDTransformTiamatGuard74An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformTiamatGuardFi74An();
				break;
			case 2:
				IDTransformTiamatGuardAs74An();
				break;
			case 3:
				IDTransformTiamatGuardWi74An();
				break;
			case 4:
				IDTransformTiamatGuardPr74An();
				break;
		}
	}

	private void IDTransformTiamatGuardFi74An() {
		spawn(244794, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244794, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244794, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244794, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244794, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244794, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244794, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244794, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244794, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244794, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244794, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244794, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244794, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244794, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244794, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244794, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244794, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244794, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244794, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244794, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244794, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244794, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244794, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244794, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244794, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244794, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244794, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244794, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244794, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244794, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244794, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244794, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244794, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244794, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244794, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244794, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244794, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244794, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244794, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244794, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244794, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformTiamatGuardAs74An() {
		spawn(244795, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244795, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244795, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244795, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244795, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244795, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244795, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244795, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244795, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244795, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244795, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244795, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244795, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244795, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244795, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244795, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244795, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244795, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244795, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244795, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244795, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244795, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244795, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244795, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244795, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244795, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244795, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244795, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244795, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244795, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244795, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244795, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244795, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244795, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244795, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244795, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244795, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244795, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244795, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244795, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244795, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformTiamatGuardWi74An() {
		spawn(244796, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244796, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244796, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244796, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244796, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244796, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244796, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244796, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244796, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244796, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244796, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244796, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244796, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244796, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244796, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244796, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244796, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244796, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244796, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244796, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244796, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244796, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244796, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244796, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244796, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244796, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244796, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244796, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244796, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244796, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244796, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244796, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244796, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244796, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244796, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244796, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244796, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244796, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244796, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244796, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244796, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformTiamatGuardPr74An() {
		spawn(244797, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244797, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244797, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244797, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244797, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244797, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244797, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244797, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244797, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244797, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244797, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244797, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244797, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244797, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244797, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244797, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244797, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244797, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244797, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244797, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244797, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244797, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244797, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244797, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244797, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244797, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244797, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244797, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244797, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244797, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244797, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244797, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244797, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244797, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244797, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244797, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244797, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244797, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244797, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244797, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244797, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	/*** Deva Guard ***/
	private void IDTransformDevaGuardFi74An() {
		spawn(244813, 543.9115f, 491.4599f, 322.04422f, (byte) 91);
		spawn(244813, 547.9682f, 490.34494f, 321.50793f, (byte) 90);
		spawn(244813, 534.53534f, 485.35455f, 322.0f, (byte) 89);
		spawn(244813, 541.57935f, 487.14307f, 322.0f, (byte) 89);
		spawn(244813, 516.4117f, 585.5329f, 321.97427f, (byte) 90);
		spawn(244813, 499.82422f, 585.81537f, 322.02252f, (byte) 92);
		spawn(244813, 501.61078f, 555.0141f, 321.84106f, (byte) 12);
	}

	private void IDTransformDevaGuardAs74An() {
		spawn(244814, 855.63855f, 484.74774f, 349.08722f, (byte) 45);
		spawn(244814, 852.21844f, 482.80756f, 349.41986f, (byte) 44);
		spawn(244814, 864.0118f, 492.74384f, 349.56552f, (byte) 46);
		spawn(244814, 508.17993f, 587.8108f, 322.56973f, (byte) 90);
		spawn(244814, 526.8684f, 493.7475f, 322.0f, (byte) 91);
		spawn(244814, 537.37225f, 508.5786f, 322.0f, (byte) 90);
		spawn(244814, 512.8187f, 590.4575f, 322.56216f, (byte) 90);
		spawn(244814, 504.50745f, 590.3867f, 322.56216f, (byte) 90);
		spawn(244814, 482.3006f, 496.94232f, 342.22174f, (byte) 27);
		spawn(244814, 594.4832f, 673.60126f, 350.85538f, (byte) 54);
		spawn(244814, 634.298f, 511.2995f, 339.61545f, (byte) 61);
		spawn(244814, 530.982f, 493.62543f, 322.0f, (byte) 90);
		spawn(244814, 641.9954f, 514.2143f, 339.61542f, (byte) 61);
		spawn(244814, 505.45636f, 584.90186f, 322.0f, (byte) 90);
		spawn(244814, 510.66394f, 584.80176f, 322.0f, (byte) 90);
		spawn(244814, 859.2291f, 490.53555f, 348.98047f, (byte) 45);
		spawn(244814, 604.1567f, 635.69995f, 352.53815f, (byte) 30);
		spawn(244814, 467.0f, 476.0f, 345.70047f, (byte) 32);
		spawn(244814, 536.2914f, 488.65588f, 322.0f, (byte) 90);
		spawn(244814, 639.6124f, 525.6597f, 339.61542f, (byte) 62);
		spawn(244814, 885.47394f, 464.9668f, 351.0f, (byte) 66);
		spawn(244814, 642.88293f, 520.17224f, 339.61542f, (byte) 60);
		spawn(244814, 546.91925f, 510.66574f, 321.94254f, (byte) 91);
		spawn(244814, 527.7633f, 502.87167f, 321.6398f, (byte) 91);
		spawn(244814, 885.5619f, 456.29126f, 351.02737f, (byte) 55);
	}

	private void IDTransformDevaGuardWi74An() {
		spawn(244815, 513.0236f, 593.86285f, 322.56216f, (byte) 90);
		spawn(244815, 501.3256f, 593.69885f, 322.56216f, (byte) 90);
		spawn(244815, 633.75323f, 516.50934f, 339.61545f, (byte) 61);
		spawn(244815, 544.93945f, 499.1886f, 322.0f, (byte) 91);
		spawn(244815, 530.8947f, 502.38684f, 321.86185f, (byte) 91);
		spawn(244815, 634.5065f, 522.0903f, 339.61545f, (byte) 53);
		spawn(244815, 885.382f, 460.77805f, 351.0f, (byte) 60);
		spawn(244815, 612.8048f, 643.80194f, 352.46414f, (byte) 30);
		spawn(244815, 538.00024f, 495.57394f, 322.0f, (byte) 90);
		spawn(244815, 526.9178f, 486.92227f, 321.87527f, (byte) 85);
	}

	private void IDTransformDevaGuardRa74An() {
		spawn(244816, 529.87f, 545.5564f, 321.85876f, (byte) 95);
		spawn(244816, 526.1612f, 545.1311f, 321.58078f, (byte) 88);
		spawn(244816, 546.40234f, 523.6142f, 321.97485f, (byte) 87);
		spawn(244816, 549.9647f, 524.9884f, 321.9978f, (byte) 95);
		spawn(244816, 520.9293f, 586.19006f, 321.94193f, (byte) 95);
		spawn(244816, 495.259f, 586.9457f, 322.0659f, (byte) 81);
		spawn(244816, 505.0709f, 552.7453f, 322.0f, (byte) 28);
	}

	/*** Eresh Stumble ***/
	private void IDTransformEreshStumble74An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformEreshStumbleFi74An();
				break;
			case 2:
				IDTransformEreshStumbleAs74An();
				break;
			case 3:
				IDTransformEreshStumbleWi74An();
				break;
			case 4:
				IDTransformEreshStumblePr74An();
				break;
		}
	}

	private void IDTransformEreshStumbleFi74An() {
		spawn(245112, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(245112, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(245112, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformEreshStumbleAs74An() {
		spawn(245113, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(245113, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(245113, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformEreshStumbleWi74An() {
		spawn(245114, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(245114, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(245114, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformEreshStumblePr74An() {
		spawn(245115, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(245115, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(245115, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	/*** Vritra Stumble ***/
	private void IDTransformVritraStumble74An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformVritraStumbleFi74An();
				break;
			case 2:
				IDTransformVritraStumbleAs74An();
				break;
			case 3:
				IDTransformVritraStumbleWi74An();
				break;
			case 4:
				IDTransformVritraStumblePr74An();
				break;
		}
	}

	private void IDTransformVritraStumbleFi74An() {
		spawn(245116, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(245116, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(245116, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformVritraStumbleAs74An() {
		spawn(245117, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(245117, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(245117, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformVritraStumbleWi74An() {
		spawn(245118, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(245118, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(245118, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformVritraStumblePr74An() {
		spawn(245119, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(245119, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(245119, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	/*** Tiamat Stumble ***/
	private void IDTransformTiamatStumble74An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformTiamatStumbleFi74An();
				break;
			case 2:
				IDTransformTiamatStumbleAs74An();
				break;
			case 3:
				IDTransformTiamatStumbleWi74An();
				break;
			case 4:
				IDTransformTiamatStumblePr74An();
				break;
		}
	}

	private void IDTransformTiamatStumbleFi74An() {
		spawn(245120, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(245120, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(245120, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformTiamatStumbleAs74An() {
		spawn(245121, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(245121, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(245121, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformTiamatStumbleWi74An() {
		spawn(245122, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(245122, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(245122, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformTiamatStumblePr74An() {
		spawn(245123, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(245123, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(245123, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	/*** Deva Stumble ***/
	private void IDTransformDevaStumble74An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformDevaStumbleFi74An();
				break;
			case 2:
				IDTransformDevaStumbleAs74An();
				break;
			case 3:
				IDTransformDevaStumbleWi74An();
				break;
			case 4:
				IDTransformDevaStumblePr74An();
				break;
		}
	}

	private void IDTransformDevaStumbleFi74An() {
		spawn(245124, 677.0f, 516.0f, 338.24844f, (byte) 43);
		spawn(245124, 505.28098f, 648.23944f, 317.08282f, (byte) 113);
		spawn(245124, 507.24606f, 628.40027f, 318.4649f, (byte) 61);
		spawn(245124, 504.39532f, 615.5569f, 320.3689f, (byte) 61);
		spawn(245124, 504.10117f, 601.42255f, 322.4643f, (byte) 60);
		spawn(245124, 513.7057f, 643.4998f, 317.10632f, (byte) 11);
		spawn(245124, 511.02466f, 614.0008f, 320.5996f, (byte) 0);
		spawn(245124, 503.81223f, 640.00806f, 317.11395f, (byte) 40);
		spawn(245124, 531.7686f, 642.5042f, 317.08282f, (byte) 112);
		spawn(245124, 522.0014f, 648.79095f, 317.08282f, (byte) 55);
	}

	private void IDTransformDevaStumbleAs74An() {
		spawn(245125, 677.0f, 516.0f, 338.24844f, (byte) 43);
		spawn(245125, 505.28098f, 648.23944f, 317.08282f, (byte) 113);
		spawn(245125, 507.24606f, 628.40027f, 318.4649f, (byte) 61);
		spawn(245125, 504.39532f, 615.5569f, 320.3689f, (byte) 61);
		spawn(245125, 504.10117f, 601.42255f, 322.4643f, (byte) 60);
		spawn(245125, 513.7057f, 643.4998f, 317.10632f, (byte) 11);
		spawn(245125, 511.02466f, 614.0008f, 320.5996f, (byte) 0);
		spawn(245125, 503.81223f, 640.00806f, 317.11395f, (byte) 40);
		spawn(245125, 531.7686f, 642.5042f, 317.08282f, (byte) 112);
		spawn(245125, 522.0014f, 648.79095f, 317.08282f, (byte) 55);
	}

	private void IDTransformDevaStumbleWi74An() {
		spawn(245126, 677.0f, 516.0f, 338.24844f, (byte) 43);
		spawn(245126, 505.28098f, 648.23944f, 317.08282f, (byte) 113);
		spawn(245126, 507.24606f, 628.40027f, 318.4649f, (byte) 61);
		spawn(245126, 504.39532f, 615.5569f, 320.3689f, (byte) 61);
		spawn(245126, 504.10117f, 601.42255f, 322.4643f, (byte) 60);
		spawn(245126, 513.7057f, 643.4998f, 317.10632f, (byte) 11);
		spawn(245126, 511.02466f, 614.0008f, 320.5996f, (byte) 0);
		spawn(245126, 503.81223f, 640.00806f, 317.11395f, (byte) 40);
		spawn(245126, 531.7686f, 642.5042f, 317.08282f, (byte) 112);
		spawn(245126, 522.0014f, 648.79095f, 317.08282f, (byte) 55);
	}

	private void IDTransformDevaStumblePr74An() {
		spawn(245127, 677.0f, 516.0f, 338.24844f, (byte) 43);
		spawn(245127, 505.28098f, 648.23944f, 317.08282f, (byte) 113);
		spawn(245127, 507.24606f, 628.40027f, 318.4649f, (byte) 61);
		spawn(245127, 504.39532f, 615.5569f, 320.3689f, (byte) 61);
		spawn(245127, 504.10117f, 601.42255f, 322.4643f, (byte) 60);
		spawn(245127, 513.7057f, 643.4998f, 317.10632f, (byte) 11);
		spawn(245127, 511.02466f, 614.0008f, 320.5996f, (byte) 0);
		spawn(245127, 503.81223f, 640.00806f, 317.11395f, (byte) 40);
		spawn(245127, 531.7686f, 642.5042f, 317.08282f, (byte) 112);
		spawn(245127, 522.0014f, 648.79095f, 317.08282f, (byte) 55);
	}

	/*** Drop Dragon ***/
	private void IDTransformDropDragon74Ae() {
		switch (Rnd.get(1, 3)) {
			case 1:
				spawn(245140, 622.6814f, 551.9144f, 346.06897f, (byte) 105);
				break;
			case 2:
				spawn(245141, 622.6814f, 551.9144f, 346.06897f, (byte) 105);
				break;
			case 3:
				spawn(245142, 622.6814f, 551.9144f, 346.06897f, (byte) 105);
				break;
		}
		switch (Rnd.get(1, 3)) {
			case 1:
				spawn(245140, 607.8992f, 674.06934f, 352.29062f, (byte) 90);
				break;
			case 2:
				spawn(245141, 607.8992f, 674.06934f, 352.29062f, (byte) 90);
				break;
			case 3:
				spawn(245142, 607.8992f, 674.06934f, 352.29062f, (byte) 90);
				break;
		}
	}

	/*** Bonus Monster ***/
	private void IDTransformBonusMonsterTypeA74An() {
		spawn(246232, 761.54095f, 562.17f, 341.0512f, (byte) 90);
		spawn(246232, 476.94467f, 549.22363f, 345.6048f, (byte) 90);
		spawn(246232, 609.2811f, 707.6352f, 355.10846f, (byte) 93);
	}

	private void IDTransformBonusMonsterTypeB74An() {
		spawn(246233, 761.54095f, 562.17f, 341.0512f, (byte) 90);
		spawn(246233, 476.94467f, 549.22363f, 345.6048f, (byte) 90);
		spawn(246233, 609.2811f, 707.6352f, 355.10846f, (byte) 93);
	}

	private void IDTransformBonusMonsterTypeC74An() {
		spawn(246234, 761.54095f, 562.17f, 341.0512f, (byte) 90);
		spawn(246234, 476.94467f, 549.22363f, 345.6048f, (byte) 90);
		spawn(246234, 609.2811f, 707.6352f, 355.10846f, (byte) 93);
	}

	private void IDTransformBonusMonsterTypeD74An() {
		spawn(246235, 761.54095f, 562.17f, 341.0512f, (byte) 90);
		spawn(246235, 476.94467f, 549.22363f, 345.6048f, (byte) 90);
		spawn(246235, 609.2811f, 707.6352f, 355.10846f, (byte) 93);
	}

	/*** Eresh Warp ***/
	private void IDTransformEreshWarp74() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformEreshWarpFi74An();
				break;
			case 2:
				IDTransformEreshWarpAs74An();
				break;
			case 3:
				IDTransformEreshWarpWi74An();
				break;
			case 4:
				IDTransformEreshWarpPr74An();
				break;
		}
	}

	private void IDTransformEreshWarpFi74An() {
		sp(245673, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245673, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245673, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245673, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245673, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245673, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245673, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245673, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245673, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245673, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245673, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245673, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245673, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245673, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245673, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245673, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245673, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245673, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245673, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformEreshWarpAs74An() {
		sp(245674, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245674, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245674, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245674, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245674, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245674, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245674, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245674, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245674, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245674, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245674, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245674, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245674, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245674, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245674, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245674, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245674, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245674, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245674, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformEreshWarpWi74An() {
		sp(245675, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245675, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245675, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245675, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245675, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245675, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245675, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245675, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245675, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245675, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245675, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245675, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245675, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245675, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245675, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245675, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245675, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245675, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245675, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformEreshWarpPr74An() {
		sp(245676, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245676, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245676, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245676, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245676, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245676, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245676, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245676, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245676, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245676, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245676, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245676, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245676, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245676, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245676, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245676, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245676, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245676, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245676, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	/*** Vritra Warp ***/
	private void IDTransformVritraWarp74() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformVritraWarpFi74An();
				break;
			case 2:
				IDTransformVritraWarpAs74An();
				break;
			case 3:
				IDTransformVritraWarpWi74An();
				break;
			case 4:
				IDTransformVritraWarpPr74An();
				break;
		}
	}

	private void IDTransformVritraWarpFi74An() {
		sp(245677, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245677, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245677, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245677, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245677, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245677, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245677, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245677, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245677, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245677, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245677, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245677, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245677, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245677, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245677, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245677, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245677, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245677, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245677, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformVritraWarpAs74An() {
		sp(245678, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245678, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245678, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245678, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245678, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245678, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245678, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245678, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245678, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245678, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245678, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245678, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245678, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245678, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245678, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245678, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245678, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245678, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245678, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformVritraWarpWi74An() {
		sp(245679, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245679, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245679, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245679, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245679, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245679, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245679, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245679, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245679, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245679, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245679, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245679, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245679, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245679, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245679, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245679, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245679, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245679, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245679, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformVritraWarpPr74An() {
		sp(245680, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245680, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245680, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245680, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245680, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245680, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245680, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245680, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245680, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245680, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245680, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245680, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245680, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245680, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245680, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245680, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245680, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245680, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245680, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	/*** Tiamat Warp ***/
	private void IDTransformTiamatWarp74() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformTiamatWarpFi74An();
				break;
			case 2:
				IDTransformTiamatWarpAs74An();
				break;
			case 3:
				IDTransformTiamatWarpWi74An();
				break;
			case 4:
				IDTransformTiamatWarpPr74An();
				break;
		}
	}

	private void IDTransformTiamatWarpFi74An() {
		sp(245681, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245681, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245681, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245681, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245681, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245681, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245681, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245681, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245681, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245681, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245681, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245681, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245681, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245681, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245681, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245681, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245681, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245681, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245681, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformTiamatWarpAs74An() {
		sp(245682, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245682, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245682, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245682, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245682, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245682, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245682, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245682, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245682, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245682, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245682, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245682, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245682, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245682, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245682, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245682, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245682, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245682, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245682, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformTiamatWarpWi74An() {
		sp(245683, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245683, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245683, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245683, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245683, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245683, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245683, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245683, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245683, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245683, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245683, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245683, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245683, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245683, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245683, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245683, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245683, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245683, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245683, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformTiamatWarpPr74An() {
		sp(245684, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245684, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245684, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245684, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245684, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245684, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245684, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245684, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245684, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245684, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245684, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245684, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245684, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245684, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245684, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245684, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245684, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245684, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245684, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	/*** Ranger ***/
	private void IDTransformRa74An() {
		switch (Rnd.get(1, 3)) {
			case 1:
				IDTransformEreshRa74An();
				break;
			case 2:
				IDTransformVritraRa74An();
				break;
			case 3:
				IDTransformTiamatRa74An();
				break;
		}
	}

	/*** Eresh Ranger ***/
	private void IDTransformEreshRa74An() {
		spawn(245745, 543.0933f, 557.8653f, 322.0f, (byte) 70);
		spawn(245745, 550.4571f, 546.09937f, 322.0f, (byte) 61);
		spawn(245745, 550.2217f, 539.29755f, 322.0f, (byte) 59);
		spawn(245745, 543.0761f, 534.39087f, 322.0f, (byte) 43);
		spawn(245745, 534.18945f, 534.63385f, 321.875f, (byte) 21);
		spawn(245745, 522.56396f, 467.8957f, 322.0f, (byte) 37);
		spawn(245745, 527.70734f, 475.95486f, 322.0f, (byte) 7);
		spawn(245745, 509.6404f, 476.19836f, 322.0f, (byte) 118);
		spawn(245745, 501.61966f, 467.60513f, 322.0f, (byte) 5);
		spawn(245745, 503.2415f, 446.9365f, 321.8276f, (byte) 18);
		spawn(245745, 542.8608f, 472.11404f, 322.0f, (byte) 51);
		spawn(245745, 539.9602f, 467.72766f, 322.0f, (byte) 50);
		spawn(245745, 516.02167f, 453.723f, 321.875f, (byte) 60);
		spawn(245745, 466.30823f, 509.6083f, 342.1901f, (byte) 0);
		spawn(245745, 466.1356f, 516.46765f, 342.1901f, (byte) 0);
		spawn(245745, 468.7195f, 513.0958f, 342.1901f, (byte) 0);
		spawn(245745, 446.91077f, 509.62943f, 342.2713f, (byte) 1);
		spawn(245745, 446.86057f, 517.2843f, 342.27173f, (byte) 0);
		spawn(245745, 449.71277f, 513.23883f, 342.24753f, (byte) 0);
		spawn(245745, 427.54434f, 509.3018f, 342.43564f, (byte) 0);
		spawn(245745, 427.4629f, 517.19116f, 342.43634f, (byte) 0);
		spawn(245745, 430.6331f, 513.1085f, 342.40942f, (byte) 0);
		spawn(245745, 483.40283f, 475.9793f, 345.70047f, (byte) 81);
		spawn(245745, 469.33865f, 454.01733f, 345.70047f, (byte) 4);
		spawn(245745, 482.78824f, 526.4342f, 342.234f, (byte) 78);
		spawn(245745, 502.18372f, 508.00098f, 339.63123f, (byte) 39);
		spawn(245745, 497.84283f, 521.77655f, 339.63815f, (byte) 77);
		spawn(245745, 508.98663f, 527.061f, 339.63126f, (byte) 76);
		spawn(245745, 502.5523f, 649.1053f, 317.08282f, (byte) 109);
		spawn(245745, 522.4196f, 650.3398f, 317.08282f, (byte) 99);
		spawn(245745, 540.84265f, 652.0684f, 316.16275f, (byte) 100);
		spawn(245745, 540.5331f, 637.4856f, 316.17746f, (byte) 17);
		spawn(245745, 514.6148f, 641.9673f, 317.10098f, (byte) 33);
		spawn(245745, 539.03076f, 536.819f, 322.0f, (byte) 30);
	}

	/*** Vritra Ranger ***/
	private void IDTransformVritraRa74An() {
		spawn(245746, 543.0933f, 557.8653f, 322.0f, (byte) 70);
		spawn(245746, 550.4571f, 546.09937f, 322.0f, (byte) 61);
		spawn(245746, 550.2217f, 539.29755f, 322.0f, (byte) 59);
		spawn(245746, 543.0761f, 534.39087f, 322.0f, (byte) 43);
		spawn(245746, 534.18945f, 534.63385f, 321.875f, (byte) 21);
		spawn(245746, 522.56396f, 467.8957f, 322.0f, (byte) 37);
		spawn(245746, 527.70734f, 475.95486f, 322.0f, (byte) 7);
		spawn(245746, 509.6404f, 476.19836f, 322.0f, (byte) 118);
		spawn(245746, 501.61966f, 467.60513f, 322.0f, (byte) 5);
		spawn(245746, 503.2415f, 446.9365f, 321.8276f, (byte) 18);
		spawn(245746, 542.8608f, 472.11404f, 322.0f, (byte) 51);
		spawn(245746, 539.9602f, 467.72766f, 322.0f, (byte) 50);
		spawn(245746, 516.02167f, 453.723f, 321.875f, (byte) 60);
		spawn(245746, 466.30823f, 509.6083f, 342.1901f, (byte) 0);
		spawn(245746, 466.1356f, 516.46765f, 342.1901f, (byte) 0);
		spawn(245746, 468.7195f, 513.0958f, 342.1901f, (byte) 0);
		spawn(245746, 446.91077f, 509.62943f, 342.2713f, (byte) 1);
		spawn(245746, 446.86057f, 517.2843f, 342.27173f, (byte) 0);
		spawn(245746, 449.71277f, 513.23883f, 342.24753f, (byte) 0);
		spawn(245746, 427.54434f, 509.3018f, 342.43564f, (byte) 0);
		spawn(245746, 427.4629f, 517.19116f, 342.43634f, (byte) 0);
		spawn(245746, 430.6331f, 513.1085f, 342.40942f, (byte) 0);
		spawn(245746, 483.40283f, 475.9793f, 345.70047f, (byte) 81);
		spawn(245746, 469.33865f, 454.01733f, 345.70047f, (byte) 4);
		spawn(245746, 482.78824f, 526.4342f, 342.234f, (byte) 78);
		spawn(245746, 502.18372f, 508.00098f, 339.63123f, (byte) 39);
		spawn(245746, 497.84283f, 521.77655f, 339.63815f, (byte) 77);
		spawn(245746, 508.98663f, 527.061f, 339.63126f, (byte) 76);
		spawn(245746, 502.5523f, 649.1053f, 317.08282f, (byte) 109);
		spawn(245746, 522.4196f, 650.3398f, 317.08282f, (byte) 99);
		spawn(245746, 540.84265f, 652.0684f, 316.16275f, (byte) 100);
		spawn(245746, 540.5331f, 637.4856f, 316.17746f, (byte) 17);
		spawn(245746, 514.6148f, 641.9673f, 317.10098f, (byte) 33);
		spawn(245746, 539.03076f, 536.819f, 322.0f, (byte) 30);
	}

	/*** Tiamat Ranger ***/
	private void IDTransformTiamatRa74An() {
		spawn(245747, 543.0933f, 557.8653f, 322.0f, (byte) 70);
		spawn(245747, 550.4571f, 546.09937f, 322.0f, (byte) 61);
		spawn(245747, 550.2217f, 539.29755f, 322.0f, (byte) 59);
		spawn(245747, 543.0761f, 534.39087f, 322.0f, (byte) 43);
		spawn(245747, 534.18945f, 534.63385f, 321.875f, (byte) 21);
		spawn(245747, 522.56396f, 467.8957f, 322.0f, (byte) 37);
		spawn(245747, 527.70734f, 475.95486f, 322.0f, (byte) 7);
		spawn(245747, 509.6404f, 476.19836f, 322.0f, (byte) 118);
		spawn(245747, 501.61966f, 467.60513f, 322.0f, (byte) 5);
		spawn(245747, 503.2415f, 446.9365f, 321.8276f, (byte) 18);
		spawn(245747, 542.8608f, 472.11404f, 322.0f, (byte) 51);
		spawn(245747, 539.9602f, 467.72766f, 322.0f, (byte) 50);
		spawn(245747, 516.02167f, 453.723f, 321.875f, (byte) 60);
		spawn(245747, 466.30823f, 509.6083f, 342.1901f, (byte) 0);
		spawn(245747, 466.1356f, 516.46765f, 342.1901f, (byte) 0);
		spawn(245747, 468.7195f, 513.0958f, 342.1901f, (byte) 0);
		spawn(245747, 446.91077f, 509.62943f, 342.2713f, (byte) 1);
		spawn(245747, 446.86057f, 517.2843f, 342.27173f, (byte) 0);
		spawn(245747, 449.71277f, 513.23883f, 342.24753f, (byte) 0);
		spawn(245747, 427.54434f, 509.3018f, 342.43564f, (byte) 0);
		spawn(245747, 427.4629f, 517.19116f, 342.43634f, (byte) 0);
		spawn(245747, 430.6331f, 513.1085f, 342.40942f, (byte) 0);
		spawn(245747, 483.40283f, 475.9793f, 345.70047f, (byte) 81);
		spawn(245747, 469.33865f, 454.01733f, 345.70047f, (byte) 4);
		spawn(245747, 482.78824f, 526.4342f, 342.234f, (byte) 78);
		spawn(245747, 502.18372f, 508.00098f, 339.63123f, (byte) 39);
		spawn(245747, 497.84283f, 521.77655f, 339.63815f, (byte) 77);
		spawn(245747, 508.98663f, 527.061f, 339.63126f, (byte) 76);
		spawn(245747, 502.5523f, 649.1053f, 317.08282f, (byte) 109);
		spawn(245747, 522.4196f, 650.3398f, 317.08282f, (byte) 99);
		spawn(245747, 540.84265f, 652.0684f, 316.16275f, (byte) 100);
		spawn(245747, 540.5331f, 637.4856f, 316.17746f, (byte) 17);
		spawn(245747, 514.6148f, 641.9673f, 317.10098f, (byte) 33);
		spawn(245747, 539.03076f, 536.819f, 322.0f, (byte) 30);
	}

	/*** Player Lvl 75-83 ***/
	/*** Eresh High ***/
	private void IDTransformEreshHigh75An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformEreshHighFi75An();
				break;
			case 2:
				IDTransformEreshHighAs75An();
				break;
			case 3:
				IDTransformEreshHighWi75An();
				break;
			case 4:
				IDTransformEreshHighPr75An();
				break;
		}
	}

	private void IDTransformEreshHighFi75An() {
		spawn(244839, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244839, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244839, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244839, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244839, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244839, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244839, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244839, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244839, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244839, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244839, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244839, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244839, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244839, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244839, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244839, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244839, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformEreshHighAs75An() {
		spawn(244840, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244840, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244840, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244840, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244840, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244840, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244840, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244840, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244840, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244840, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244840, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244840, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244840, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244840, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244840, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244840, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244840, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformEreshHighWi75An() {
		spawn(244841, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244841, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244841, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244841, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244841, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244841, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244841, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244841, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244841, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244841, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244841, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244841, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244841, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244841, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244841, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244841, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244841, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformEreshHighPr75An() {
		spawn(244842, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244842, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244842, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244842, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244842, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244842, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244842, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244842, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244842, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244842, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244842, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244842, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244842, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244842, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244842, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244842, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244842, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	/*** Vritra High ***/
	private void IDTransformVritraHigh75An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformVritraHighFi75An();
				break;
			case 2:
				IDTransformVritraHighAs75An();
				break;
			case 3:
				IDTransformVritraHighWi75An();
				break;
			case 4:
				IDTransformVritraHighPr75An();
				break;
		}
	}

	private void IDTransformVritraHighFi75An() {
		spawn(244843, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244843, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244843, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244843, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244843, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244843, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244843, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244843, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244843, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244843, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244843, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244843, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244843, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244843, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244843, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244843, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244843, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformVritraHighAs75An() {
		spawn(244844, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244844, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244844, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244844, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244844, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244844, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244844, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244844, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244844, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244844, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244844, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244844, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244844, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244844, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244844, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244844, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244844, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformVritraHighWi75An() {
		spawn(244845, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244845, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244845, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244845, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244845, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244845, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244845, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244845, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244845, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244845, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244845, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244845, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244845, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244845, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244845, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244845, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244845, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformVritraHighPr75An() {
		spawn(244846, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244846, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244846, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244846, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244846, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244846, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244846, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244846, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244846, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244846, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244846, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244846, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244846, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244846, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244846, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244846, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244846, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	/*** Tiamat High ***/
	private void IDTransformTiamatHigh75An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformTiamatHighFi75An();
				break;
			case 2:
				IDTransformTiamatHighAs75An();
				break;
			case 3:
				IDTransformTiamatHighWi75An();
				break;
			case 4:
				IDTransformTiamatHighPr75An();
				break;
		}
	}

	private void IDTransformTiamatHighFi75An() {
		spawn(244847, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244847, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244847, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244847, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244847, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244847, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244847, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244847, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244847, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244847, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244847, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244847, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244847, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244847, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244847, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244847, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244847, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformTiamatHighAs75An() {
		spawn(244848, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244848, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244848, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244848, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244848, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244848, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244848, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244848, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244848, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244848, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244848, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244848, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244848, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244848, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244848, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244848, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244848, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformTiamatHighWi75An() {
		spawn(244849, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244849, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244849, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244849, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244849, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244849, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244849, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244849, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244849, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244849, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244849, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244849, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244849, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244849, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244849, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244849, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244849, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	private void IDTransformTiamatHighPr75An() {
		spawn(244850, 587.9679f, 620.0452f, 331.7278f, (byte) 15);
		spawn(244850, 574.8497f, 668.79987f, 306.13416f, (byte) 78);
		spawn(244850, 579.0743f, 665.09393f, 306.088f, (byte) 77);
		spawn(244850, 577.0f, 667.0f, 306.1446f, (byte) 77);
		spawn(244850, 592.84674f, 627.5482f, 331.72772f, (byte) 30);
		spawn(244850, 575.2141f, 664.8451f, 306.0879f, (byte) 78);
		spawn(244850, 590.92944f, 620.8617f, 331.72787f, (byte) 21);
		spawn(244850, 587.0f, 626.0f, 331.80182f, (byte) 21);
		spawn(244850, 559.017f, 619.6899f, 326.24957f, (byte) 1);
		spawn(244850, 584.7248f, 623.8249f, 331.80182f, (byte) 10);
		spawn(244850, 553.0f, 618.0f, 326.2498f, (byte) 10);
		spawn(244850, 578.6354f, 669.4132f, 306.0881f, (byte) 79);
		spawn(244850, 552.9948f, 622.4288f, 326.2497f, (byte) 0);
		spawn(244850, 557.20746f, 616.5142f, 326.2497f, (byte) 0);
		spawn(244850, 558.10596f, 623.51215f, 326.3014f, (byte) 116);
		spawn(244850, 467.57324f, 477.38986f, 345.70047f, (byte) 84);
		spawn(244850, 482.65543f, 498.34192f, 342.22174f, (byte) 87);
	}

	/*** Eresh Guard ***/
	private void IDTransformEreshGuard75An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformEreshGuardFi75An();
				break;
			case 2:
				IDTransformEreshGuardAs75An();
				break;
			case 3:
				IDTransformEreshGuardWi75An();
				break;
			case 4:
				IDTransformEreshGuardPr75An();
				break;
		}
	}

	private void IDTransformEreshGuardFi75An() {
		spawn(244827, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244827, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244827, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244827, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244827, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244827, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244827, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244827, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244827, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244827, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244827, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244827, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244827, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244827, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244827, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244827, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244827, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244827, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244827, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244827, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244827, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244827, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244827, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244827, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244827, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244827, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244827, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244827, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244827, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244827, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244827, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244827, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244827, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244827, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244827, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244827, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244827, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244827, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244827, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244827, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244827, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformEreshGuardAs75An() {
		spawn(244828, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244828, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244828, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244828, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244828, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244828, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244828, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244828, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244828, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244828, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244828, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244828, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244828, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244828, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244828, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244828, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244828, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244828, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244828, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244828, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244828, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244828, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244828, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244828, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244828, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244828, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244828, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244828, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244828, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244828, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244828, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244828, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244828, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244828, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244828, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244828, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244828, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244828, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244828, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244828, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244828, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformEreshGuardWi75An() {
		spawn(244829, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244829, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244829, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244829, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244829, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244829, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244829, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244829, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244829, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244829, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244829, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244829, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244829, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244829, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244829, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244829, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244829, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244829, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244829, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244829, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244829, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244829, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244829, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244829, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244829, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244829, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244829, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244829, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244829, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244829, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244829, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244829, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244829, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244829, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244829, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244829, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244829, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244829, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244829, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244829, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244829, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformEreshGuardPr75An() {
		spawn(244830, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244830, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244830, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244830, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244830, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244830, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244830, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244830, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244830, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244830, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244830, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244830, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244830, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244830, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244830, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244830, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244830, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244830, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244830, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244830, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244830, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244830, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244830, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244830, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244830, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244830, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244830, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244830, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244830, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244830, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244830, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244830, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244830, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244830, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244830, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244830, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244830, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244830, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244830, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244830, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244830, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	/*** Vritra Guard ***/
	private void IDTransformVritraGuard75An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformVritraGuardFi75An();
				break;
			case 2:
				IDTransformVritraGuardAs75An();
				break;
			case 3:
				IDTransformVritraGuardWi75An();
				break;
			case 4:
				IDTransformVritraGuardPr75An();
				break;
		}
	}

	private void IDTransformVritraGuardFi75An() {
		spawn(244831, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244831, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244831, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244831, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244831, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244831, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244831, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244831, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244831, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244831, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244831, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244831, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244831, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244831, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244831, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244831, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244831, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244831, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244831, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244831, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244831, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244831, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244831, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244831, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244831, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244831, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244831, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244831, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244831, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244831, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244831, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244831, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244831, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244831, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244831, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244831, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244831, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244831, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244831, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244831, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244831, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformVritraGuardAs75An() {
		spawn(244832, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244832, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244832, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244832, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244832, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244832, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244832, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244832, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244832, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244832, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244832, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244832, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244832, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244832, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244832, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244832, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244832, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244832, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244832, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244832, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244832, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244832, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244832, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244832, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244832, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244832, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244832, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244832, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244832, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244832, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244832, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244832, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244832, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244832, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244832, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244832, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244832, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244832, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244832, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244832, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244832, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformVritraGuardWi75An() {
		spawn(244833, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244833, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244833, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244833, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244833, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244833, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244833, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244833, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244833, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244833, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244833, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244833, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244833, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244833, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244833, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244833, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244833, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244833, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244833, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244833, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244833, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244833, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244833, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244833, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244833, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244833, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244833, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244833, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244833, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244833, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244833, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244833, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244833, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244833, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244833, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244833, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244833, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244833, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244833, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244833, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244833, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformVritraGuardPr75An() {
		spawn(244834, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244834, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244834, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244834, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244834, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244834, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244834, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244834, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244834, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244834, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244834, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244834, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244834, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244834, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244834, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244834, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244834, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244834, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244834, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244834, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244834, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244834, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244834, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244834, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244834, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244834, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244834, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244834, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244834, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244834, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244834, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244834, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244834, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244834, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244834, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244834, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244834, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244834, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244834, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244834, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244834, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	/*** Tiamat Guard ***/
	private void IDTransformTiamatGuard75An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformTiamatGuardFi75An();
				break;
			case 2:
				IDTransformTiamatGuardAs75An();
				break;
			case 3:
				IDTransformTiamatGuardWi75An();
				break;
			case 4:
				IDTransformTiamatGuardPr75An();
				break;
		}
	}

	private void IDTransformTiamatGuardFi75An() {
		spawn(244835, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244835, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244835, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244835, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244835, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244835, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244835, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244835, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244835, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244835, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244835, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244835, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244835, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244835, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244835, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244835, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244835, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244835, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244835, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244835, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244835, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244835, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244835, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244835, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244835, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244835, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244835, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244835, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244835, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244835, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244835, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244835, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244835, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244835, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244835, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244835, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244835, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244835, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244835, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244835, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244835, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformTiamatGuardAs75An() {
		spawn(244836, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244836, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244836, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244836, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244836, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244836, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244836, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244836, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244836, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244836, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244836, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244836, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244836, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244836, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244836, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244836, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244836, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244836, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244836, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244836, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244836, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244836, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244836, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244836, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244836, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244836, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244836, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244836, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244836, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244836, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244836, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244836, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244836, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244836, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244836, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244836, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244836, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244836, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244836, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244836, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244836, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformTiamatGuardWi75An() {
		spawn(244837, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244837, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244837, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244837, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244837, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244837, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244837, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244837, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244837, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244837, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244837, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244837, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244837, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244837, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244837, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244837, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244837, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244837, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244837, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244837, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244837, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244837, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244837, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244837, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244837, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244837, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244837, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244837, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244837, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244837, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244837, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244837, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244837, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244837, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244837, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244837, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244837, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244837, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244837, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244837, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244837, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	private void IDTransformTiamatGuardPr75An() {
		spawn(244838, 796.0f, 502.0f, 340.625f, (byte) 0);
		spawn(244838, 854.7336f, 485.80002f, 348.83334f, (byte) 102);
		spawn(244838, 799.1453f, 507.0006f, 340.83063f, (byte) 119);
		spawn(244838, 796.0f, 518.0f, 340.0f, (byte) 119);
		spawn(244838, 862.7976f, 493.89987f, 349.12473f, (byte) 98);
		spawn(244838, 608.63324f, 559.0327f, 352.1944f, (byte) 112);
		spawn(244838, 593.5001f, 674.01624f, 350.85538f, (byte) 0);
		spawn(244838, 537.9779f, 494.51947f, 322.0f, (byte) 30);
		spawn(244838, 851.19464f, 483.77264f, 348.9846f, (byte) 103);
		spawn(244838, 536.33435f, 487.47772f, 322.0f, (byte) 30);
		spawn(244838, 604.0555f, 636.776f, 352.5063f, (byte) 94);
		spawn(244838, 530.9891f, 501.1546f, 321.87363f, (byte) 31);
		spawn(244838, 799.2242f, 521.97736f, 340.47952f, (byte) 118);
		spawn(244838, 530.97894f, 492.33932f, 322.0f, (byte) 31);
		spawn(244838, 858.3547f, 491.6155f, 348.8165f, (byte) 104);
		spawn(244838, 607.52203f, 555.99097f, 352.1944f, (byte) 119);
		spawn(244838, 544.9966f, 497.9999f, 322.0f, (byte) 30);
		spawn(244838, 527.03064f, 492.47556f, 322.0f, (byte) 31);
		spawn(244838, 640.6056f, 514.0086f, 339.61542f, (byte) 1);
		spawn(244838, 632.79755f, 516.58496f, 339.61548f, (byte) 1);
		spawn(244838, 633.14124f, 511.15356f, 339.61548f, (byte) 2);
		spawn(244838, 632.80096f, 522.824f, 339.61545f, (byte) 113);
		spawn(244838, 534.67413f, 483.98718f, 322.0f, (byte) 30);
		spawn(244838, 637.7931f, 525.1838f, 339.61545f, (byte) 107);
		spawn(244838, 537.44684f, 507.10223f, 322.0f, (byte) 30);
		spawn(244838, 790.6567f, 503.19193f, 339.64206f, (byte) 2);
		spawn(244838, 612.65625f, 645.03973f, 352.49344f, (byte) 91);
		spawn(244838, 606.4433f, 558.1034f, 352.1944f, (byte) 113);
		spawn(244838, 794.0673f, 521.59106f, 339.63763f, (byte) 115);
		spawn(244838, 790.0854f, 507.5921f, 339.28082f, (byte) 3);
		spawn(244838, 609.1728f, 561.4424f, 352.20877f, (byte) 108);
		spawn(244838, 546.9983f, 509.095f, 321.93762f, (byte) 30);
		spawn(244838, 528.00977f, 501.40976f, 321.6244f, (byte) 30);
		spawn(244838, 791.13544f, 525.8589f, 339.43713f, (byte) 0);
		spawn(244838, 526.723f, 485.72604f, 321.79556f, (byte) 31);
		spawn(244838, 548.0f, 489.0f, 321.5f, (byte) 31);
		spawn(244838, 792.1176f, 505.79953f, 339.52722f, (byte) 3);
		spawn(244838, 541.7361f, 485.9917f, 322.0f, (byte) 30);
		spawn(244838, 543.99554f, 490.13123f, 322.0f, (byte) 30);
		spawn(244838, 791.39966f, 518.47125f, 339.42496f, (byte) 0);
		spawn(244838, 641.4397f, 520.0093f, 339.61542f, (byte) 119);
	}

	/*** Deva Guard ***/
	private void IDTransformDevaGuardFi75An() {
		spawn(244854, 543.9115f, 491.4599f, 322.04422f, (byte) 91);
		spawn(244854, 547.9682f, 490.34494f, 321.50793f, (byte) 90);
		spawn(244854, 534.53534f, 485.35455f, 322.0f, (byte) 89);
		spawn(244854, 541.57935f, 487.14307f, 322.0f, (byte) 89);
		spawn(244854, 516.4117f, 585.5329f, 321.97427f, (byte) 90);
		spawn(244854, 499.82422f, 585.81537f, 322.02252f, (byte) 92);
		spawn(244854, 501.61078f, 555.0141f, 321.84106f, (byte) 12);
	}

	private void IDTransformDevaGuardAs75An() {
		spawn(244855, 855.63855f, 484.74774f, 349.08722f, (byte) 45);
		spawn(244855, 852.21844f, 482.80756f, 349.41986f, (byte) 44);
		spawn(244855, 864.0118f, 492.74384f, 349.56552f, (byte) 46);
		spawn(244855, 508.17993f, 587.8108f, 322.56973f, (byte) 90);
		spawn(244855, 526.8684f, 493.7475f, 322.0f, (byte) 91);
		spawn(244855, 537.37225f, 508.5786f, 322.0f, (byte) 90);
		spawn(244855, 512.8187f, 590.4575f, 322.56216f, (byte) 90);
		spawn(244855, 504.50745f, 590.3867f, 322.56216f, (byte) 90);
		spawn(244855, 482.3006f, 496.94232f, 342.22174f, (byte) 27);
		spawn(244855, 594.4832f, 673.60126f, 350.85538f, (byte) 54);
		spawn(244855, 634.298f, 511.2995f, 339.61545f, (byte) 61);
		spawn(244855, 530.982f, 493.62543f, 322.0f, (byte) 90);
		spawn(244855, 641.9954f, 514.2143f, 339.61542f, (byte) 61);
		spawn(244855, 505.45636f, 584.90186f, 322.0f, (byte) 90);
		spawn(244855, 510.66394f, 584.80176f, 322.0f, (byte) 90);
		spawn(244855, 859.2291f, 490.53555f, 348.98047f, (byte) 45);
		spawn(244855, 604.1567f, 635.69995f, 352.53815f, (byte) 30);
		spawn(244855, 467.0f, 476.0f, 345.70047f, (byte) 32);
		spawn(244855, 536.2914f, 488.65588f, 322.0f, (byte) 90);
		spawn(244855, 639.6124f, 525.6597f, 339.61542f, (byte) 62);
		spawn(244855, 885.47394f, 464.9668f, 351.0f, (byte) 66);
		spawn(244855, 642.88293f, 520.17224f, 339.61542f, (byte) 60);
		spawn(244855, 546.91925f, 510.66574f, 321.94254f, (byte) 91);
		spawn(244855, 527.7633f, 502.87167f, 321.6398f, (byte) 91);
		spawn(244855, 885.5619f, 456.29126f, 351.02737f, (byte) 55);
	}

	private void IDTransformDevaGuardWi75An() {
		spawn(244856, 513.0236f, 593.86285f, 322.56216f, (byte) 90);
		spawn(244856, 501.3256f, 593.69885f, 322.56216f, (byte) 90);
		spawn(244856, 633.75323f, 516.50934f, 339.61545f, (byte) 61);
		spawn(244856, 544.93945f, 499.1886f, 322.0f, (byte) 91);
		spawn(244856, 530.8947f, 502.38684f, 321.86185f, (byte) 91);
		spawn(244856, 634.5065f, 522.0903f, 339.61545f, (byte) 53);
		spawn(244856, 885.382f, 460.77805f, 351.0f, (byte) 60);
		spawn(244856, 612.8048f, 643.80194f, 352.46414f, (byte) 30);
		spawn(244856, 538.00024f, 495.57394f, 322.0f, (byte) 90);
		spawn(244856, 526.9178f, 486.92227f, 321.87527f, (byte) 85);
	}

	private void IDTransformDevaGuardRa75An() {
		spawn(244857, 529.87f, 545.5564f, 321.85876f, (byte) 95);
		spawn(244857, 526.1612f, 545.1311f, 321.58078f, (byte) 88);
		spawn(244857, 546.40234f, 523.6142f, 321.97485f, (byte) 87);
		spawn(244857, 549.9647f, 524.9884f, 321.9978f, (byte) 95);
		spawn(244857, 520.9293f, 586.19006f, 321.94193f, (byte) 95);
		spawn(244857, 495.259f, 586.9457f, 322.0659f, (byte) 81);
		spawn(244857, 505.0709f, 552.7453f, 322.0f, (byte) 28);
	}

	/*** Eresh Stumble ***/
	private void IDTransformEreshStumble75An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformEreshStumbleFi75An();
				break;
			case 2:
				IDTransformEreshStumbleAs75An();
				break;
			case 3:
				IDTransformEreshStumbleWi75An();
				break;
			case 4:
				IDTransformEreshStumblePr75An();
				break;
		}
	}

	private void IDTransformEreshStumbleFi75An() {
		spawn(245143, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(245143, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(245143, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformEreshStumbleAs75An() {
		spawn(245144, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(245144, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(245144, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformEreshStumbleWi75An() {
		spawn(245145, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(245145, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(245145, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformEreshStumblePr75An() {
		spawn(245146, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(245146, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(245146, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	/*** Vritra Stumble ***/
	private void IDTransformVritraStumble75An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformVritraStumbleFi75An();
				break;
			case 2:
				IDTransformVritraStumbleAs75An();
				break;
			case 3:
				IDTransformVritraStumbleWi75An();
				break;
			case 4:
				IDTransformVritraStumblePr75An();
				break;
		}
	}

	private void IDTransformVritraStumbleFi75An() {
		spawn(245147, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(245147, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(245147, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformVritraStumbleAs75An() {
		spawn(245148, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(245148, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(245148, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformVritraStumbleWi75An() {
		spawn(245149, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(245149, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(245149, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformVritraStumblePr75An() {
		spawn(245150, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(245150, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(245150, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	/*** Tiamat Stumble ***/
	private void IDTransformTiamatStumble75An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformTiamatStumbleFi75An();
				break;
			case 2:
				IDTransformTiamatStumbleAs75An();
				break;
			case 3:
				IDTransformTiamatStumbleWi75An();
				break;
			case 4:
				IDTransformTiamatStumblePr75An();
				break;
		}
	}

	private void IDTransformTiamatStumbleFi75An() {
		spawn(245151, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(245151, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(245151, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformTiamatStumbleAs75An() {
		spawn(245152, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(245152, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(245152, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformTiamatStumbleWi75An() {
		spawn(245153, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(245153, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(245153, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	private void IDTransformTiamatStumblePr75An() {
		spawn(245154, 856.45654f, 530.1012f, 346.1631f, (byte) 86);
		spawn(245154, 504.76083f, 513.9777f, 339.63126f, (byte) 61);
		spawn(245154, 809.2487f, 476.4447f, 340.87885f, (byte) 29);
	}

	/*** Deva Stumble ***/
	private void IDTransformDevaStumble75An() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformDevaStumbleFi75An();
				break;
			case 2:
				IDTransformDevaStumbleAs75An();
				break;
			case 3:
				IDTransformDevaStumbleWi75An();
				break;
			case 4:
				IDTransformDevaStumblePr75An();
				break;
		}
	}

	private void IDTransformDevaStumbleFi75An() {
		spawn(245155, 677.0f, 516.0f, 338.24844f, (byte) 43);
		spawn(245155, 505.28098f, 648.23944f, 317.08282f, (byte) 113);
		spawn(245155, 507.24606f, 628.40027f, 318.4649f, (byte) 61);
		spawn(245155, 504.39532f, 615.5569f, 320.3689f, (byte) 61);
		spawn(245155, 504.10117f, 601.42255f, 322.4643f, (byte) 60);
		spawn(245155, 513.7057f, 643.4998f, 317.10632f, (byte) 11);
		spawn(245155, 511.02466f, 614.0008f, 320.5996f, (byte) 0);
		spawn(245155, 503.81223f, 640.00806f, 317.11395f, (byte) 40);
		spawn(245155, 531.7686f, 642.5042f, 317.08282f, (byte) 112);
		spawn(245155, 522.0014f, 648.79095f, 317.08282f, (byte) 55);
	}

	private void IDTransformDevaStumbleAs75An() {
		spawn(245156, 677.0f, 516.0f, 338.24844f, (byte) 43);
		spawn(245156, 505.28098f, 648.23944f, 317.08282f, (byte) 113);
		spawn(245156, 507.24606f, 628.40027f, 318.4649f, (byte) 61);
		spawn(245156, 504.39532f, 615.5569f, 320.3689f, (byte) 61);
		spawn(245156, 504.10117f, 601.42255f, 322.4643f, (byte) 60);
		spawn(245156, 513.7057f, 643.4998f, 317.10632f, (byte) 11);
		spawn(245156, 511.02466f, 614.0008f, 320.5996f, (byte) 0);
		spawn(245156, 503.81223f, 640.00806f, 317.11395f, (byte) 40);
		spawn(245156, 531.7686f, 642.5042f, 317.08282f, (byte) 112);
		spawn(245156, 522.0014f, 648.79095f, 317.08282f, (byte) 55);
	}

	private void IDTransformDevaStumbleWi75An() {
		spawn(245157, 677.0f, 516.0f, 338.24844f, (byte) 43);
		spawn(245157, 505.28098f, 648.23944f, 317.08282f, (byte) 113);
		spawn(245157, 507.24606f, 628.40027f, 318.4649f, (byte) 61);
		spawn(245157, 504.39532f, 615.5569f, 320.3689f, (byte) 61);
		spawn(245157, 504.10117f, 601.42255f, 322.4643f, (byte) 60);
		spawn(245157, 513.7057f, 643.4998f, 317.10632f, (byte) 11);
		spawn(245157, 511.02466f, 614.0008f, 320.5996f, (byte) 0);
		spawn(245157, 503.81223f, 640.00806f, 317.11395f, (byte) 40);
		spawn(245157, 531.7686f, 642.5042f, 317.08282f, (byte) 112);
		spawn(245157, 522.0014f, 648.79095f, 317.08282f, (byte) 55);
	}

	private void IDTransformDevaStumblePr75An() {
		spawn(245158, 677.0f, 516.0f, 338.24844f, (byte) 43);
		spawn(245158, 505.28098f, 648.23944f, 317.08282f, (byte) 113);
		spawn(245158, 507.24606f, 628.40027f, 318.4649f, (byte) 61);
		spawn(245158, 504.39532f, 615.5569f, 320.3689f, (byte) 61);
		spawn(245158, 504.10117f, 601.42255f, 322.4643f, (byte) 60);
		spawn(245158, 513.7057f, 643.4998f, 317.10632f, (byte) 11);
		spawn(245158, 511.02466f, 614.0008f, 320.5996f, (byte) 0);
		spawn(245158, 503.81223f, 640.00806f, 317.11395f, (byte) 40);
		spawn(245158, 531.7686f, 642.5042f, 317.08282f, (byte) 112);
		spawn(245158, 522.0014f, 648.79095f, 317.08282f, (byte) 55);
	}

	/*** Drop Dragon ***/
	private void IDTransformDropDragon75Ae() {
		switch (Rnd.get(1, 3)) {
			case 1:
				spawn(245171, 622.6814f, 551.9144f, 346.06897f, (byte) 105);
				break;
			case 2:
				spawn(245172, 622.6814f, 551.9144f, 346.06897f, (byte) 105);
				break;
			case 3:
				spawn(245173, 622.6814f, 551.9144f, 346.06897f, (byte) 105);
				break;
		}
		switch (Rnd.get(1, 3)) {
			case 1:
				spawn(245171, 607.8992f, 674.06934f, 352.29062f, (byte) 90);
				break;
			case 2:
				spawn(245172, 607.8992f, 674.06934f, 352.29062f, (byte) 90);
				break;
			case 3:
				spawn(245173, 607.8992f, 674.06934f, 352.29062f, (byte) 90);
				break;
		}
	}

	/*** Bonus Monster ***/
	private void IDTransformBonusMonsterTypeA75An() {
		spawn(246236, 761.54095f, 562.17f, 341.0512f, (byte) 90);
		spawn(246236, 476.94467f, 549.22363f, 345.6048f, (byte) 90);
		spawn(246236, 609.2811f, 707.6352f, 355.10846f, (byte) 93);
	}

	private void IDTransformBonusMonsterTypeB75An() {
		spawn(246237, 761.54095f, 562.17f, 341.0512f, (byte) 90);
		spawn(246237, 476.94467f, 549.22363f, 345.6048f, (byte) 90);
		spawn(246237, 609.2811f, 707.6352f, 355.10846f, (byte) 93);
	}

	private void IDTransformBonusMonsterTypeC75An() {
		spawn(246238, 761.54095f, 562.17f, 341.0512f, (byte) 90);
		spawn(246238, 476.94467f, 549.22363f, 345.6048f, (byte) 90);
		spawn(246238, 609.2811f, 707.6352f, 355.10846f, (byte) 93);
	}

	private void IDTransformBonusMonsterTypeD75An() {
		spawn(246239, 761.54095f, 562.17f, 341.0512f, (byte) 90);
		spawn(246239, 476.94467f, 549.22363f, 345.6048f, (byte) 90);
		spawn(246239, 609.2811f, 707.6352f, 355.10846f, (byte) 93);
	}

	/*** Eresh Warp ***/
	private void IDTransformEreshWarp75() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformEreshWarpFi75An();
				break;
			case 2:
				IDTransformEreshWarpAs75An();
				break;
			case 3:
				IDTransformEreshWarpWi75An();
				break;
			case 4:
				IDTransformEreshWarpPr75An();
				break;
		}
	}

	private void IDTransformEreshWarpFi75An() {
		sp(245685, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245685, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245685, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245685, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245685, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245685, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245685, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245685, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245685, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245685, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245685, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245685, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245685, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245685, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245685, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245685, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245685, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245685, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245685, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformEreshWarpAs75An() {
		sp(245686, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245686, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245686, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245686, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245686, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245686, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245686, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245686, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245686, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245686, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245686, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245686, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245686, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245686, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245686, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245686, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245686, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245686, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245686, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformEreshWarpWi75An() {
		sp(245687, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245687, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245687, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245687, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245687, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245687, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245687, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245687, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245687, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245687, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245687, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245687, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245687, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245687, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245687, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245687, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245687, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245687, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245687, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformEreshWarpPr75An() {
		sp(245688, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245688, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245688, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245688, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245688, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245688, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245688, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245688, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245688, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245688, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245688, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245688, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245688, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245688, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245688, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245688, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245688, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245688, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245688, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	/*** Vritra Warp ***/
	private void IDTransformVritraWarp75() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformVritraWarpFi75An();
				break;
			case 2:
				IDTransformVritraWarpAs75An();
				break;
			case 3:
				IDTransformVritraWarpWi75An();
				break;
			case 4:
				IDTransformVritraWarpPr75An();
				break;
		}
	}

	private void IDTransformVritraWarpFi75An() {
		sp(245689, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245689, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245689, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245689, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245689, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245689, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245689, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245689, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245689, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245689, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245689, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245689, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245689, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245689, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245689, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245689, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245689, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245689, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245689, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformVritraWarpAs75An() {
		sp(245690, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245690, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245690, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245690, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245690, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245690, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245690, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245690, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245690, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245690, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245690, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245690, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245690, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245690, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245690, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245690, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245690, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245690, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245690, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformVritraWarpWi75An() {
		sp(245691, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245691, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245691, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245691, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245691, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245691, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245691, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245691, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245691, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245691, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245691, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245691, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245691, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245691, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245691, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245691, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245691, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245691, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245691, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformVritraWarpPr75An() {
		sp(245692, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245692, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245692, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245692, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245692, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245692, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245692, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245692, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245692, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245692, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245692, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245692, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245692, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245692, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245692, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245692, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245692, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245692, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245692, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	/*** Tiamat Warp ***/
	private void IDTransformTiamatWarp75() {
		switch (Rnd.get(1, 4)) {
			case 1:
				IDTransformTiamatWarpFi75An();
				break;
			case 2:
				IDTransformTiamatWarpAs75An();
				break;
			case 3:
				IDTransformTiamatWarpWi75An();
				break;
			case 4:
				IDTransformTiamatWarpPr75An();
				break;
		}
	}

	private void IDTransformTiamatWarpFi75An() {
		sp(245693, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245693, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245693, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245693, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245693, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245693, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245693, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245693, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245693, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245693, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245693, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245693, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245693, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245693, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245693, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245693, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245693, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245693, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245693, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformTiamatWarpAs75An() {
		sp(245694, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245694, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245694, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245694, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245694, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245694, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245694, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245694, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245694, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245694, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245694, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245694, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245694, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245694, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245694, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245694, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245694, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245694, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245694, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformTiamatWarpWi75An() {
		sp(245695, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245695, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245695, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245695, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245695, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245695, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245695, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245695, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245695, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245695, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245695, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245695, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245695, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245695, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245695, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245695, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245695, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245695, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245695, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	private void IDTransformTiamatWarpPr75An() {
		sp(245696, 725.0f, 515.0f, 338.24844f, (byte) 1, 0, 0, null);
		sp(245696, 722.0f, 513.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245696, 722.0f, 517.3f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245696, 725.0f, 502.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245696, 722.7f, 499.3f, 338.37372f, (byte) 1, 2000, 0, null);
		sp(245696, 722.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245696, 703.0f, 514.0f, 338.24844f, (byte) 4, 2000, 0, null);
		sp(245696, 700.0f, 516.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245696, 697.0f, 515.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245696, 703.0f, 503.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245696, 699.0f, 506.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245696, 696.0f, 504.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245696, 672.0f, 523.0f, 338.24844f, (byte) 1, 2000, 0, null);
		sp(245696, 669.0f, 520.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245696, 668.1f, 525.2f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245696, 664.9f, 520.7f, 338.24844f, (byte) 115, 2000, 0, null);
		sp(245696, 674.0f, 509.6f, 338.24844f, (byte) 0, 2000, 0, null);
		sp(245696, 671.0f, 512.0f, 338.24844f, (byte) 2, 2000, 0, null);
		sp(245696, 667.2f, 510.0f, 338.24844f, (byte) 1, 2000, 0, null);
	}

	/*** Ranger ***/
	private void IDTransformRa75An() {
		switch (Rnd.get(1, 3)) {
			case 1:
				IDTransformEreshRa75An();
				break;
			case 2:
				IDTransformVritraRa75An();
				break;
			case 3:
				IDTransformTiamatRa75An();
				break;
		}
	}

	/*** Eresh Ranger ***/
	private void IDTransformEreshRa75An() {
		spawn(245751, 543.0933f, 557.8653f, 322.0f, (byte) 70);
		spawn(245751, 550.4571f, 546.09937f, 322.0f, (byte) 61);
		spawn(245751, 550.2217f, 539.29755f, 322.0f, (byte) 59);
		spawn(245751, 543.0761f, 534.39087f, 322.0f, (byte) 43);
		spawn(245751, 534.18945f, 534.63385f, 321.875f, (byte) 21);
		spawn(245751, 522.56396f, 467.8957f, 322.0f, (byte) 37);
		spawn(245751, 527.70734f, 475.95486f, 322.0f, (byte) 7);
		spawn(245751, 509.6404f, 476.19836f, 322.0f, (byte) 118);
		spawn(245751, 501.61966f, 467.60513f, 322.0f, (byte) 5);
		spawn(245751, 503.2415f, 446.9365f, 321.8276f, (byte) 18);
		spawn(245751, 542.8608f, 472.11404f, 322.0f, (byte) 51);
		spawn(245751, 539.9602f, 467.72766f, 322.0f, (byte) 50);
		spawn(245751, 516.02167f, 453.723f, 321.875f, (byte) 60);
		spawn(245751, 466.30823f, 509.6083f, 342.1901f, (byte) 0);
		spawn(245751, 466.1356f, 516.46765f, 342.1901f, (byte) 0);
		spawn(245751, 468.7195f, 513.0958f, 342.1901f, (byte) 0);
		spawn(245751, 446.91077f, 509.62943f, 342.2713f, (byte) 1);
		spawn(245751, 446.86057f, 517.2843f, 342.27173f, (byte) 0);
		spawn(245751, 449.71277f, 513.23883f, 342.24753f, (byte) 0);
		spawn(245751, 427.54434f, 509.3018f, 342.43564f, (byte) 0);
		spawn(245751, 427.4629f, 517.19116f, 342.43634f, (byte) 0);
		spawn(245751, 430.6331f, 513.1085f, 342.40942f, (byte) 0);
		spawn(245751, 483.40283f, 475.9793f, 345.70047f, (byte) 81);
		spawn(245751, 469.33865f, 454.01733f, 345.70047f, (byte) 4);
		spawn(245751, 482.78824f, 526.4342f, 342.234f, (byte) 78);
		spawn(245751, 502.18372f, 508.00098f, 339.63123f, (byte) 39);
		spawn(245751, 497.84283f, 521.77655f, 339.63815f, (byte) 77);
		spawn(245751, 508.98663f, 527.061f, 339.63126f, (byte) 76);
		spawn(245751, 502.5523f, 649.1053f, 317.08282f, (byte) 109);
		spawn(245751, 522.4196f, 650.3398f, 317.08282f, (byte) 99);
		spawn(245751, 540.84265f, 652.0684f, 316.16275f, (byte) 100);
		spawn(245751, 540.5331f, 637.4856f, 316.17746f, (byte) 17);
		spawn(245751, 514.6148f, 641.9673f, 317.10098f, (byte) 33);
		spawn(245751, 539.03076f, 536.819f, 322.0f, (byte) 30);
	}

	/*** Vritra Ranger ***/
	private void IDTransformVritraRa75An() {
		spawn(245752, 543.0933f, 557.8653f, 322.0f, (byte) 70);
		spawn(245752, 550.4571f, 546.09937f, 322.0f, (byte) 61);
		spawn(245752, 550.2217f, 539.29755f, 322.0f, (byte) 59);
		spawn(245752, 543.0761f, 534.39087f, 322.0f, (byte) 43);
		spawn(245752, 534.18945f, 534.63385f, 321.875f, (byte) 21);
		spawn(245752, 522.56396f, 467.8957f, 322.0f, (byte) 37);
		spawn(245752, 527.70734f, 475.95486f, 322.0f, (byte) 7);
		spawn(245752, 509.6404f, 476.19836f, 322.0f, (byte) 118);
		spawn(245752, 501.61966f, 467.60513f, 322.0f, (byte) 5);
		spawn(245752, 503.2415f, 446.9365f, 321.8276f, (byte) 18);
		spawn(245752, 542.8608f, 472.11404f, 322.0f, (byte) 51);
		spawn(245752, 539.9602f, 467.72766f, 322.0f, (byte) 50);
		spawn(245752, 516.02167f, 453.723f, 321.875f, (byte) 60);
		spawn(245752, 466.30823f, 509.6083f, 342.1901f, (byte) 0);
		spawn(245752, 466.1356f, 516.46765f, 342.1901f, (byte) 0);
		spawn(245752, 468.7195f, 513.0958f, 342.1901f, (byte) 0);
		spawn(245752, 446.91077f, 509.62943f, 342.2713f, (byte) 1);
		spawn(245752, 446.86057f, 517.2843f, 342.27173f, (byte) 0);
		spawn(245752, 449.71277f, 513.23883f, 342.24753f, (byte) 0);
		spawn(245752, 427.54434f, 509.3018f, 342.43564f, (byte) 0);
		spawn(245752, 427.4629f, 517.19116f, 342.43634f, (byte) 0);
		spawn(245752, 430.6331f, 513.1085f, 342.40942f, (byte) 0);
		spawn(245752, 483.40283f, 475.9793f, 345.70047f, (byte) 81);
		spawn(245752, 469.33865f, 454.01733f, 345.70047f, (byte) 4);
		spawn(245752, 482.78824f, 526.4342f, 342.234f, (byte) 78);
		spawn(245752, 502.18372f, 508.00098f, 339.63123f, (byte) 39);
		spawn(245752, 497.84283f, 521.77655f, 339.63815f, (byte) 77);
		spawn(245752, 508.98663f, 527.061f, 339.63126f, (byte) 76);
		spawn(245752, 502.5523f, 649.1053f, 317.08282f, (byte) 109);
		spawn(245752, 522.4196f, 650.3398f, 317.08282f, (byte) 99);
		spawn(245752, 540.84265f, 652.0684f, 316.16275f, (byte) 100);
		spawn(245752, 540.5331f, 637.4856f, 316.17746f, (byte) 17);
		spawn(245752, 514.6148f, 641.9673f, 317.10098f, (byte) 33);
		spawn(245752, 539.03076f, 536.819f, 322.0f, (byte) 30);
	}

	/*** Tiamat Ranger ***/
	private void IDTransformTiamatRa75An() {
		spawn(245753, 543.0933f, 557.8653f, 322.0f, (byte) 70);
		spawn(245753, 550.4571f, 546.09937f, 322.0f, (byte) 61);
		spawn(245753, 550.2217f, 539.29755f, 322.0f, (byte) 59);
		spawn(245753, 543.0761f, 534.39087f, 322.0f, (byte) 43);
		spawn(245753, 534.18945f, 534.63385f, 321.875f, (byte) 21);
		spawn(245753, 522.56396f, 467.8957f, 322.0f, (byte) 37);
		spawn(245753, 527.70734f, 475.95486f, 322.0f, (byte) 7);
		spawn(245753, 509.6404f, 476.19836f, 322.0f, (byte) 118);
		spawn(245753, 501.61966f, 467.60513f, 322.0f, (byte) 5);
		spawn(245753, 503.2415f, 446.9365f, 321.8276f, (byte) 18);
		spawn(245753, 542.8608f, 472.11404f, 322.0f, (byte) 51);
		spawn(245753, 539.9602f, 467.72766f, 322.0f, (byte) 50);
		spawn(245753, 516.02167f, 453.723f, 321.875f, (byte) 60);
		spawn(245753, 466.30823f, 509.6083f, 342.1901f, (byte) 0);
		spawn(245753, 466.1356f, 516.46765f, 342.1901f, (byte) 0);
		spawn(245753, 468.7195f, 513.0958f, 342.1901f, (byte) 0);
		spawn(245753, 446.91077f, 509.62943f, 342.2713f, (byte) 1);
		spawn(245753, 446.86057f, 517.2843f, 342.27173f, (byte) 0);
		spawn(245753, 449.71277f, 513.23883f, 342.24753f, (byte) 0);
		spawn(245753, 427.54434f, 509.3018f, 342.43564f, (byte) 0);
		spawn(245753, 427.4629f, 517.19116f, 342.43634f, (byte) 0);
		spawn(245753, 430.6331f, 513.1085f, 342.40942f, (byte) 0);
		spawn(245753, 483.40283f, 475.9793f, 345.70047f, (byte) 81);
		spawn(245753, 469.33865f, 454.01733f, 345.70047f, (byte) 4);
		spawn(245753, 482.78824f, 526.4342f, 342.234f, (byte) 78);
		spawn(245753, 502.18372f, 508.00098f, 339.63123f, (byte) 39);
		spawn(245753, 497.84283f, 521.77655f, 339.63815f, (byte) 77);
		spawn(245753, 508.98663f, 527.061f, 339.63126f, (byte) 76);
		spawn(245753, 502.5523f, 649.1053f, 317.08282f, (byte) 109);
		spawn(245753, 522.4196f, 650.3398f, 317.08282f, (byte) 99);
		spawn(245753, 540.84265f, 652.0684f, 316.16275f, (byte) 100);
		spawn(245753, 540.5331f, 637.4856f, 316.17746f, (byte) 17);
		spawn(245753, 514.6148f, 641.9673f, 317.10098f, (byte) 33);
		spawn(245753, 539.03076f, 536.819f, 322.0f, (byte) 30);
	}

	private void deleteNpc(int npcId) {
		if (getNpc(npcId) != null) {
			getNpc(npcId).getController().onDelete();
		}
	}

	protected void sp(final int npcId, final float x, final float y, final float z, final byte h, final int time) {
		sp(npcId, x, y, z, h, 0, time, 0, null);
	}

	protected void sp(final int npcId, final float x, final float y, final float z, final byte h, final int time, final int msg, final Race race) {
		sp(npcId, x, y, z, h, 0, time, msg, race);
	}

	protected void sp(final int npcId, final float x, final float y, final float z, final byte h, final int staticId, final int time, final int msg, final Race race) {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawn(npcId, x, y, z, h, staticId);
				if (msg > 0) {
					sendMsgByRace(msg, race, 0);
				}
			}
		}, time);
	}

	protected void sp(final int npcId, final float x, final float y, final float z, final byte h, final int time, final String walkerId) {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				Npc npc = (Npc) spawn(npcId, x, y, z, h);
				npc.getSpawn().setWalkerId(walkerId);
				WalkManager.startWalking((NpcAI2) npc.getAi2());
			}
		}, time);
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
	public void onLeaveInstance(Player player) {
		player.getEffectController().removeEffect(4808); // IDTransform_Highdeva_fire_G2
		player.getEffectController().removeEffect(4813); // IDTransform_Highdeva_water_G2
		player.getEffectController().removeEffect(4818); // IDTransform_Highdeva_earth_G2
		player.getEffectController().removeEffect(4824); // IDTransform_Highdeva_wind_G2

		player.getEffectController().removeEffect(4831); // IDTransform_Base_Form
		player.getEffectController().removeEffect(4834); // IDTransform_Form_Dispel_Water
		player.getEffectController().removeEffect(4835); // IDTransform_Form_Dispel_Earth
		player.getEffectController().removeEffect(4836); // IDTransform_Form_Dispel_Wind
	}

	@Override
	public void onExitInstance(Player player) {
		TeleportService2.moveToInstanceExit(player, mapId, player.getRace());
	}

	@Override
	public void onPlayerLogOut(Player player) {
		TeleportService2.moveToInstanceExit(player, mapId, player.getRace());
	}

	@Override
	public boolean onDie(final Player player, Creature lastAttacker) {
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);
		PacketSendUtility.sendPacket(player, new SM_DIE(player.haveSelfRezEffect(), player.haveSelfRezItem(), 0, 8));
		return true;
	}
}
