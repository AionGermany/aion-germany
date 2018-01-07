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
import java.util.concurrent.atomic.AtomicInteger;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.ai2.manager.WalkManager;
import com.aionemu.gameserver.controllers.effect.PlayerEffectController;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.StaticDoor;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.state.CreatureState;
import com.aionemu.gameserver.model.instance.InstanceScoreType;
import com.aionemu.gameserver.model.instance.instancereward.SteelWallBastionReward;
import com.aionemu.gameserver.model.instance.playerreward.SteelWallBastionPlayerReward;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INSTANCE_SCORE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.abyss.AbyssPointsService;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;

/**
 * @author Alcapwnd
 */
@InstanceID(300540000)
public class SteelWallBastionInstance extends GeneralInstanceHandler {

	private long startTime;
	private Future<?> instanceTimer;
	private boolean isInstanceDestroyed;
	private Map<Integer, StaticDoor> doors;
	private SteelWallBastionReward instanceReward;
	@SuppressWarnings("unused")
	private final AtomicInteger specNpcKilled = new AtomicInteger();
	private List<Npc> npcs = new ArrayList<Npc>();
	private Race spawnRace;
	private int CannonDestroy;
	private int Wave01Begin;
	private int Wave02Begin;
	private int Wave03Begin;
	private int Wave04Begin;
	@SuppressWarnings("unused")
	private Future<?> WaveAssaultFirst;
	private Future<?> AssaultPodAndDrill;
	private Future<?> AssaultBomb;
	private Future<?> cancelSpawnTask;
	private boolean isCancelled;
	private int rank;

	protected SteelWallBastionPlayerReward getPlayerReward(Player player) {
		Integer object = player.getObjectId();
		if (instanceReward.getPlayerReward(object) == null) {
			addPlayerToReward(player);
		}
		return (SteelWallBastionPlayerReward) instanceReward.getPlayerReward(object);
	}

	private void addPlayerToReward(Player player) {
		instanceReward.addPlayerReward(new SteelWallBastionPlayerReward(player.getObjectId()));
	}

	private boolean containPlayer(Integer object) {
		return instanceReward.containPlayer(object);
	}

	@Override
	public void onDie(Npc npc) {
		int rewardetPoints = 0;
		int npcId = npc.getNpcId();
		Player player = npc.getAggroList().getMostPlayerDamage();
		switch (npc.getObjectTemplate().getTemplateId()) {
			case 831330: // Eternal Bastion Bomb.
				SkillEngine.getInstance().getSkill(npc, 21272, 60, npc).useSkill();
				despawnNpc(npc);
				break;
			case 230782: // The Eternal Bastion Barricade.
				despawnNpc(npc);
				break;
			case 233313:
				despawnNpc(npc);
				rewardetPoints = 20;
				break;
			case 231115:
			case 231116:
			case 233309:
				despawnNpc(npc);
				rewardetPoints = 33;
				break;
			case 233312:
			case 233314:
			case 233315:
				despawnNpc(npc);
				rewardetPoints = 36;
				break;
			case 231117:
			case 231118:
			case 231119:
			case 231120:
			case 231123:
			case 231124:
			case 231125:
			case 231126:
			case 231127:
			case 231128:
			case 233310:
			case 233311:
				despawnNpc(npc);
				rewardetPoints = 42;
				break;
			case 231149:
			case 231181:
				despawnNpc(npc);
				rewardetPoints = 266;
				break;
			case 230784:
			case 230785:
			case 231137:
			case 231138:
			case 231143:
			case 231144:
			case 231148:
			case 231151:
			case 231152:
			case 231153:
			case 231154:
			case 231155:
			case 231156:
			case 231157:
			case 231179:
				despawnNpc(npc);
				rewardetPoints = 334;
				break;
			case 230744:
			case 230745:
			case 230746: // Pashid Assault Tribuni Sentry.
			case 230749:
			case 230753:
			case 230754:
			case 230756:
			case 230757:
			case 231131:
			case 231132:
			case 231177: // Deathbringer Tariksha.
				rewardetPoints = 1002;
				break;
			case 231168:
			case 231169:
			case 231170:
			case 231171:
			case 231172:
			case 231173:
			case 231174:
			case 231175:
			case 231176:
			case 231178:
			case 231180:
				rewardetPoints = 1880;
				sendMsg(1401940); // MSG TD Notice 06.
				break;
			case 231133:
				rewardetPoints = 13424;
				break;
			// Died Guards
			case 209554:
			case 209555:
			case 209556:
			case 209557:
				instanceReward.addPoints(-900);
				npc.getController().onDelete();
				sendMsg(1401939); // MSG TD Main Wave 06.
				break;
			// Destroy Gate
			case 831335:
			case 831333:
				instanceReward.addPoints(-1350);
				npc.getController().onDelete();
				break;

		}
		if (!isInstanceDestroyed && instanceReward.getInstanceScoreType().isStartProgress()) {
			instanceReward.addNpcKill();
			instanceReward.addPoints(rewardetPoints);
			if (player != null) {
				sendSystemMsg(player, npc, rewardetPoints);
			}
			sendPacket(rewardetPoints);
		}

		switch (npcId) {
			case 231141: // Assault Drill
			case 231163: // Assault Drill
			case 231164: // Assault Drill
			case 231165: // Assault Drill
			case 231166: // Assault Drill
			case 231167: // Assault Drill
			case 231140: // Assault Pod
			case 231156: // Assault Pod
			case 231157: // Assault Pod
			case 231158: // Assault Pod
			case 231159: // Assault Pod
			case 231160: // Assault Pod
			case 231161: // Assault Pod
			case 231162: // Assault Pod
			case 231136: // Danuar Turret
			case 231137: // Danuar Turret
			case 231138: // Danuar Turret
			case 231139: // Danuar Turret
				CannonDestroy++;
				if (CannonDestroy == 1) {
					sendMsg(1401820); // First Destroy msg.
				}
				else if (CannonDestroy == 2) {
					sendMsg(1401821);
				}
				else if (CannonDestroy == 3) {
					sendMsg(1401821);
				}
				else if (CannonDestroy == 4) {
					sendMsg(1401821);
				}
				else if (CannonDestroy == 5) {
					sendMsg(1401821);
				}
				else if (CannonDestroy == 6) {
					sendMsg(1401821);
				}
				else if (CannonDestroy == 7) {
					sendMsg(1401821);
				}
				else if (CannonDestroy == 8) {
					sendMsg(1401821);
				}
				else if (CannonDestroy == 9) {
					sendMsg(1401821);
				}
				else if (CannonDestroy == 10) {
					sendMsg(1401821);
				}
				else if (CannonDestroy == 11) {
					sendMsg(1401821);
				}
				else if (CannonDestroy == 12) {
					sendMsg(1401821);
				}
				else if (CannonDestroy == 13) {
					sendMsg(1401821);
				}
				else if (CannonDestroy == 14) {
					sendMsg(1401821);
				}
				else if (CannonDestroy == 15) {
					sendMsg(1401821);
				}
				else if (CannonDestroy == 16) {
					sendMsg(1401821);
				}
				else if (CannonDestroy == 17) {
					sendMsg(1401821);
				}
				else if (CannonDestroy == 18) {
					sendMsg(1401821);
				}
				else if (CannonDestroy == 19) {
					sendMsg(1401821);
				}
				else if (CannonDestroy == 20) {
					sendMsg(1401821);
				}
				despawnNpc(npc);
				rewardetPoints = 500;
				break;
		}

		switch (npcId) {
			// 1 Wave Begin
			case 231169: // Commander.
			case 231168: // Commander.
			case 231170: // Commander.
				Wave01Begin++;
				if (Wave01Begin == 1) {
				}
				if (Wave01Begin == 2) {
				}
				if (Wave01Begin == 3) {
					sendMsg(1401816); // MSG Main Wave 02
					startAssaultBombTimer();
					// Uderground
					spawn(231171, 656.05f, 212.84f, 223.95f, (byte) 74); // Commander Matuk
					// Left Flang
					spawn(231172, 607.57f, 405.07f, 224.09f, (byte) 92); // Commander Badut
					spawn(233313, 609.18f, 407.78f, 224.12f, (byte) 92); // Guard
					spawn(233313, 605.69f, 407.75f, 224.01f, (byte) 92); // Guard
					// Right FLang
					spawn(231173, 652.82f, 461.09f, 225.62f, (byte) 98); // Commander Kastu
					spawn(233313, 659.12f, 457.89f, 225.76f, (byte) 98); // Guard
					spawn(233313, 650.74f, 454.19f, 225.86f, (byte) 98); // Guard
					spawn(233315, 652.53f, 470.91f, 225.70f, (byte) 98); // Guard
					spawn(233315, 645.05f, 467.01f, 225.68f, (byte) 98); // Guard
				}
				despawnNpc(npc);
				break;
			// 2 Wave Begin
			case 231171: // Commander.
			case 231172: // Commander.
			case 231173: // Commander.
				Wave02Begin++;
				if (Wave02Begin == 1) {
				}
				if (Wave02Begin == 2) {
				}
				if (Wave02Begin == 3) {
					sendMsg(1401817); // MSG Main Wave 03
					// Top
					spawn(231176, 758.98f, 393.70f, 243.35f, (byte) 47); // Commander Nitra
					// Left Flang
					spawn(231175, 584.28f, 371.35f, 225.35f, (byte) 113); // Commander Kaimdu
					spawn(233313, 588.92f, 364.62f, 224.98f, (byte) 113); // Guard
					spawn(233313, 592.30f, 373.44f, 224.06f, (byte) 113); // Guard
					spawn(233315, 577.68f, 377.08f, 225.97f, (byte) 113); // Guard
					spawn(233315, 575.73f, 368.95f, 226.28f, (byte) 113); // Guard
					// Right FLang
					spawn(231174, 652.82f, 461.09f, 225.62f, (byte) 98); // Commander Murat
					spawn(233313, 659.12f, 457.89f, 225.76f, (byte) 98); // Guard
					spawn(233313, 650.74f, 454.19f, 225.86f, (byte) 98); // Guard
					spawn(233315, 652.53f, 470.91f, 225.70f, (byte) 98); // Guard
					spawn(233315, 645.05f, 467.01f, 225.68f, (byte) 98); // Guard
				}
				despawnNpc(npc);
				break;
			// 3 Wave Begin
			case 231175: // Commander.
			case 231174: // Commander.
			case 231176: // Commander.
				Wave03Begin++;
				if (Wave03Begin == 1) {
				}
				if (Wave03Begin == 2) {
				}
				if (Wave03Begin == 3) {
					sendMsg(1401818); // MSG Main Wave 04
					AssaultBombCancelTask();
					AssaultPodAndDrillCancelTask();
					spawn(231143, 613.27f, 262.20f, 228.26f, (byte) 3); // Pashid Siege Tower.
					spawn(231152, 608.41f, 303.55f, 228.29f, (byte) 113); // Pashid Siege Tower.
					spawn(231153, 626.25f, 351.28f, 226.01f, (byte) 113); // Pashid Siege Tower.
					spawn(231154, 672.08f, 416.16f, 228.01f, (byte) 75); // Pashid Siege Tower.
					spawn(231155, 633.40f, 395.50f, 226.62f, (byte) 13); // Pashid Siege Tower.
				}
				despawnNpc(npc);
				break;
			// 4 Wave Begin
			case 231143: // Pashid Siege Tower.
			case 231152: // Pashid Siege Tower.
			case 231153: // Pashid Siege Tower.
			case 231154: // Pashid Siege Tower.
			case 231155: // Pashid Siege Tower.
				Wave04Begin++;
				if (Wave04Begin == 1) {
					sendMsg(1401821); // Destroy msg.
				}
				if (Wave04Begin == 2) {
					sendMsg(1401821); // Destroy msg.
				}
				if (Wave04Begin == 3) {
					sendMsg(1401821); // Destroy msg.
				}
				if (Wave04Begin == 4) {
					sendMsg(1401821); // Destroy msg.
				}
				if (Wave04Begin == 5) { // 5 Wave Begin
					sendMsg(1401819); // MSG Main Wave 05
					spawn(231130, 744.22f, 293.07f, 233.69f, (byte) 42); // General Fly Fashid.
					spawn(231131, 712.75f, 289.28f, 249.28f, (byte) 1); // Guard Fly Fashid.
					spawn(231131, 747.78f, 323.02f, 249.28f, (byte) 88); // Guard Fly Fashid.
				}
				despawnNpc(npc);
				break;
			case 284321: // Signal Tower 1.
			case 231148: // Signal Tower 2.
				sendMsg(1401821);
				despawnNpc(npc);
				rewardetPoints = 500;
				break;
			case 231130: // Boss Rank S.
				stopInstance(player);
				if (checkRank(instanceReward.getPoints()) == 1) {
					spawn(701913, 744.22f, 293.07f, 233.69f, (byte) 42); // Chest Rank S.
				}
				else if (checkRank(instanceReward.getPoints()) == 2) {
					spawn(701914, 744.22f, 293.07f, 233.69f, (byte) 42); // Chest Rank A.
				}
				else if (checkRank(instanceReward.getPoints()) == 3) {
					spawn(701915, 744.22f, 293.07f, 233.69f, (byte) 42); // Chest Rank B.
				}
				else if (checkRank(instanceReward.getPoints()) == 4) {
					spawn(701916, 744.22f, 293.07f, 233.69f, (byte) 42); // Chest Rank C.
				}
				else if (checkRank(instanceReward.getPoints()) == 5) {
					spawn(701917, 744.22f, 293.07f, 233.69f, (byte) 42); // Chest Rank D.
				}
				final int bastion_exit = spawnRace == Race.ASMODIANS ? 730872 : 730871;
				spawn(bastion_exit, 767.10693f, 264.60303f, 233.49748f, (byte) 43); // The Eternal Bastion Exit.
				break;
			case 209516: // Kill Commander Elyos
			case 209517: // Kill Commander Asmodians
				instanceReward.addPoints(-90000);
				// stopInstanceTask(); // Fail Bastion
				stopInstance(player);
				npc.getController().onDelete();
				final int bastion_exit2 = spawnRace == Race.ASMODIANS ? 730872 : 730871;
				spawn(bastion_exit2, 767.10693f, 264.60303f, 233.49748f, (byte) 43); // The Eternal Bastion Exit.

				break;
		}
	}

	@Override
	public void handleUseItemFinish(Player player, Npc npc) {
		switch (npc.getNpcId()) {
			case 701625: // Eternal Bastion Turet Elyos.
				spawn(231160, 707.61f, 260.72f, 253.43f, (byte) 38); // Pashid Assault Pod.
				spawn(230744, 703.81f, 264.90f, 253.43f, (byte) 43); // Pashid Assault officer.
				spawn(230744, 705.67f, 270.07f, 253.43f, (byte) 43); // Pashid Assault officer.
				spawn(230745, 704.81f, 262.90f, 253.43f, (byte) 43); // Pashid Assault Commander.
				despawnNpc(npc);
				break;
			case 701922: // Eternal Bastion Turet Asmodians.
				spawn(231160, 707.61f, 260.72f, 253.43f, (byte) 38); // Pashid Assault Pod.
				spawn(230744, 703.81f, 264.90f, 253.43f, (byte) 43); // Pashid Assault officer.
				spawn(230744, 705.67f, 270.07f, 253.43f, (byte) 43); // Pashid Assault officer.
				spawn(230745, 704.81f, 262.90f, 253.43f, (byte) 43); // Pashid Assault Commander.
				despawnNpc(npc);
				break;
		}
	}

	private void SpawnRace() {
		// Commander.
		final int comander = spawnRace == Race.ASMODIANS ? 209517 : 209516;
		spawn(comander, 744.4769f, 293.416f, 233.75148f, (byte) 44);
		// Guard.
		final int guard1 = spawnRace == Race.ASMODIANS ? 209556 : 209554;
		final int guard2 = spawnRace == Race.ASMODIANS ? 209556 : 209554;
		final int guard3 = spawnRace == Race.ASMODIANS ? 209556 : 209554;
		final int guard4 = spawnRace == Race.ASMODIANS ? 209556 : 209554;
		final int guard5 = spawnRace == Race.ASMODIANS ? 209557 : 209555;
		final int guard6 = spawnRace == Race.ASMODIANS ? 209557 : 209555;
		final int guard7 = spawnRace == Race.ASMODIANS ? 209557 : 209555;
		final int guard8 = spawnRace == Race.ASMODIANS ? 209557 : 209555;
		final int guard9 = spawnRace == Race.ASMODIANS ? 209557 : 209555;
		final int guard10 = spawnRace == Race.ASMODIANS ? 209557 : 209555;
		spawn(guard1, 719.28f, 417.82f, 231.04f, (byte) 41);
		spawn(guard2, 725.68f, 420.94f, 231.00f, (byte) 40);
		spawn(guard3, 598.93f, 331.60f, 226.14f, (byte) 85);
		spawn(guard4, 607.60f, 327.75f, 225.84f, (byte) 84);
		spawn(guard5, 714.31f, 425.46f, 230.23f, (byte) 40);
		spawn(guard6, 720.82f, 428.14f, 230.10f, (byte) 38);
		spawn(guard7, 608.80f, 351.30f, 225.08f, (byte) 35);
		spawn(guard8, 601.31f, 349.28f, 225.33f, (byte) 34);
		spawn(guard9, 689.28f, 342.57f, 228.67f, (byte) 103);
		spawn(guard10, 694.25f, 336.73f, 228.67f, (byte) 43);
		// Defence Weapon.
		final int defenceWeapon1 = spawnRace == Race.ASMODIANS ? 701610 : 701596;
		final int defenceWeapon2 = spawnRace == Race.ASMODIANS ? 701611 : 701597;
		final int defenceWeapon3 = spawnRace == Race.ASMODIANS ? 701612 : 701598;
		final int defenceWeapon4 = spawnRace == Race.ASMODIANS ? 701613 : 701599;
		final int defenceWeapon5 = spawnRace == Race.ASMODIANS ? 701614 : 701600;
		final int defenceWeapon6 = spawnRace == Race.ASMODIANS ? 701615 : 701601;
		final int defenceWeapon7 = spawnRace == Race.ASMODIANS ? 701616 : 701602;
		final int defenceWeapon8 = spawnRace == Race.ASMODIANS ? 701617 : 701603;
		final int defenceWeapon9 = spawnRace == Race.ASMODIANS ? 701618 : 701604;
		final int defenceWeapon10 = spawnRace == Race.ASMODIANS ? 701619 : 701605;
		final int defenceWeapon11 = spawnRace == Race.ASMODIANS ? 701620 : 701606;
		final int defenceWeapon12 = spawnRace == Race.ASMODIANS ? 701621 : 701607;
		final int defenceWeapon13 = spawnRace == Race.ASMODIANS ? 701922 : 701625;
		spawn(defenceWeapon1, 617.95416f, 248.32031f, 235.74449f, (byte) 63);
		spawn(defenceWeapon2, 613.11914f, 275.30057f, 235.74294f, (byte) 64);
		spawn(defenceWeapon3, 616.4774f, 313.85846f, 235.74289f, (byte) 52);
		spawn(defenceWeapon4, 625.97675f, 339.55414f, 235.7432f, (byte) 54);
		spawn(defenceWeapon5, 651.3247f, 373.3068f, 238.60867f, (byte) 44);
		spawn(defenceWeapon6, 678.08124f, 396.04736f, 238.63474f, (byte) 43);
		spawn(defenceWeapon7, 710.27765f, 409.9322f, 241.02042f, (byte) 31);
		spawn(defenceWeapon8, 737.3579f, 413.3636f, 241.02278f, (byte) 33);
		spawn(defenceWeapon9, 772.7887f, 410.0723f, 241.02089f, (byte) 6);
		spawn(defenceWeapon10, 798.2277f, 400.5876f, 241.02304f, (byte) 38);
		spawn(defenceWeapon11, 709.54443f, 313.67133f, 254.21622f, (byte) 103);
		spawn(defenceWeapon12, 726.6982f, 328.01038f, 254.21628f, (byte) 103);
		spawn(defenceWeapon13, 640.8445f, 412.9476f, 243.93938f, (byte) 103);
	}

	private void startAssaultPodTimer() {

		WaveAssaultFirst = ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				sendMsg(1401815); // MSG Main Wave 01
				spawn(231140, 745.09f, 302.52f, 233.75f, (byte) 88); // Pashid Assault Pod.
				spawn(231141, 735.23f, 293.94f, 233.75f, (byte) 118); // Pashid Assault Drill.
			}
		}, 5000);

		AssaultPodAndDrill = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				int rnd = Rnd.get(1, 5);
				switch (rnd) {
					case 1:
						spawn(231156, 771.80f, 358.20f, 230.96f, (byte) 27); // Pashid Assault Pod.
						spawn(231163, 755.40f, 363.19f, 230.96f, (byte) 43); // Pashid Assault Drill.
						TimerDespawn(98000);
						break;
					case 2:
						spawn(231157, 729.01f, 358.72f, 230.94f, (byte) 31); // Pashid Assault Pod.
						spawn(231164, 735.94f, 372.56f, 230.94f, (byte) 73); // Pashid Assault Drill.
						TimerDespawn(98000);
						break;
					case 3:
						spawn(231158, 677.17f, 313.80f, 225.69f, (byte) 56); // Pashid Assault Pod.
						spawn(231165, 673.63f, 302.47f, 225.69f, (byte) 57); // Pashid Assault Drill.
						TimerDespawn(98000);
						break;
					case 4:
						spawn(231159, 659.70f, 257.36f, 225.69f, (byte) 3); // Pashid Assault Pod.
						spawn(231166, 653.85f, 266.61f, 225.69f, (byte) 118); // Pashid Assault Drill.
						TimerDespawn(98000);
						break;
					case 5:
						spawn(231160, 764.36f, 392.54f, 243.35f, (byte) 22); // Pashid Assault Pod.
						spawn(231167, 630.05f, 305.34f, 238.07f, (byte) 22); // Pashid Assault Drill.
						TimerDespawn(98000);
						break;
				}
			}
		}, 30000, 100000);
	}

	private void startAssaultBombTimer() {

		AssaultBomb = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				spawn(231142, 603.76f, 169.68f, 216.06f, (byte) 77, 1000, "bomber1_300540000", false);
				spawn(231142, 798.99f, 473.36f, 225.47f, (byte) 77, 1200, "bomber2_300540000", false);
				spawn(231142, 603.76f, 169.68f, 216.06f, (byte) 77, 1300, "bomber1_300540000", false);
				spawn(231142, 798.99f, 473.36f, 225.47f, (byte) 77, 1400, "bomber2_300540000", false);
				spawn(231142, 603.76f, 169.68f, 216.06f, (byte) 77, 1500, "bomber1_300540000", false);
			}
		}, 50000, 200000);
	}

	private void AssaultPodAndDrillCancelTask() {
		if (AssaultPodAndDrill != null && !AssaultPodAndDrill.isCancelled()) {
			AssaultPodAndDrill.cancel(true);
		}
	}

	private void AssaultBombCancelTask() {
		if (AssaultBomb != null && !AssaultBomb.isCancelled()) {
			AssaultBomb.cancel(true);
		}
	}

	private void TimerDespawn(final int time) {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (time == 98000) {
					// Pashid Pod and Drill
					despawnNpc(getNpc(231156));
					despawnNpc(getNpc(231163));
					despawnNpc(getNpc(231157));
					despawnNpc(getNpc(231164));
					despawnNpc(getNpc(231158));
					despawnNpc(getNpc(231165));
					despawnNpc(getNpc(231159));
					despawnNpc(getNpc(231166));
					despawnNpc(getNpc(231160));
					despawnNpc(getNpc(231167));
					// Pashid Guards
					despawnNpc(getNpc(231105));
					despawnNpc(getNpc(231105));
					despawnNpc(getNpc(231106));
					despawnNpc(getNpc(231106));
					despawnNpc(getNpc(231107));
					despawnNpc(getNpc(231107));
					despawnNpc(getNpc(231108));
					despawnNpc(getNpc(231108));
				}
			}
		}, time);
	}

	private void removeEffects(Player player) {
		PlayerEffectController effectController = player.getEffectController();
		effectController.removeEffect(21065);
		effectController.removeEffect(21066);
		effectController.removeEffect(21138);
		effectController.removeEffect(21139);
		effectController.removeEffect(21141);
	}

	@Override
	public void onLeaveInstance(Player player) {
		removeEffects(player);
	}

	private int getTime() {
		long result = System.currentTimeMillis() - startTime;
		if (result < 180000) {
			return (int) (180000 - result);
		}
		else if (result < 1800000) {
			return (int) (1800000 - (result - 180000));
		}
		return 0;
	}

	protected void sendSystemMsg(Player player, Creature creature, int rewardPoints) {
		int nameId = creature.getObjectTemplate().getNameId();
		DescriptionId name = new DescriptionId(nameId * 2 + 1);
		PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400237, nameId == 0 ? creature.getName() : name, rewardPoints));
	}

	/**
	 * @param point
	 */
	private void sendPacket(final int point) {
		instance.doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(getTime(), instanceReward, null));
			}
		});
	}

	private int checkRank(int totalPoints) {
		if (totalPoints > 91999) { // Rank S.
			rank = 1;
		}
		else if (totalPoints > 83999) { // Rank A.
			rank = 2;
		}
		else if (totalPoints > 75999) { // Rank B.
			rank = 3;
		}
		else if (totalPoints > 49999) { // Rank C.
			rank = 4;
		}
		else if (totalPoints > 9999) { // Rank D.
			rank = 5;
		}
		else if (totalPoints >= 9999) { // Rank F.
			rank = 8;
		}
		else {
			rank = 8;
		}
		return rank;
	}

	/**
	 * @param player
	 */
	protected void stopInstance(Player player) {
		stopInstanceTask();
		AssaultPodAndDrillCancelTask();
		AssaultBombCancelTask();
		if (!instance.getNpc(231130).getLifeStats().isAlreadyDead()) {
			instanceReward.setRank(6);
		}
		else {
			instanceReward.setRank(checkRank(instanceReward.getPoints()));
		}

		instanceReward.setInstanceScoreType(InstanceScoreType.END_PROGRESS);
		rewardGroup();
		sendPacket(0);
	}

	private void stopInstanceTask() {
		if (instanceTimer != null) {
			instanceTimer.cancel(true);
		}
	}

	private void rewardGroup() {
		for (Player p : instance.getPlayersInside()) {
			doReward(p);
		}
	}

	@Override
	public void doReward(Player player) {

		switch (rank) {
			case 1: // S
				instanceReward.setBasicAP(35000);
				instanceReward.setCeramiumMedal(4);
				instanceReward.setPowerfulBundleWater(1);
				instanceReward.setPowerfulBundleEssence(1);
				break;
			case 2: // A
				instanceReward.setBasicAP(25000);
				instanceReward.setCeramiumMedal(2);
				instanceReward.setPowerfulBundleEssence(1);
				instanceReward.setLargeBundleWater(1);
				break;
			case 3: // B
				instanceReward.setBasicAP(15000);
				instanceReward.setCeramiumMedal(1);
				instanceReward.setLargeBundleEssence(1);
				instanceReward.setSmallBundleWater(1);
				break;
			case 4: // C
				instanceReward.setBasicAP(11000);
				instanceReward.setSmallBundleWater(1);
				break;
			case 5: // D
				instanceReward.setBasicAP(7000);
				break;
			case 6: // F
				break;
		}
		AbyssPointsService.addAp(player, instanceReward.getBasicAP());
		ItemService.addItem(player, 186000242, instanceReward.getCeramiumMedal());
		ItemService.addItem(player, 188052596, instanceReward.getPowerfulBundleWater());
		ItemService.addItem(player, 188052594, instanceReward.getPowerfulBundleEssence());
		ItemService.addItem(player, 188052597, instanceReward.getLargeBundleWater());
		ItemService.addItem(player, 188052595, instanceReward.getLargeBundleEssence());
		ItemService.addItem(player, 188052598, instanceReward.getSmallBundleWater());
	}

	@Override
	public void onEnterInstance(final Player player) {

		if (!containPlayer(player.getObjectId())) {
			addPlayerToReward(player);
		}
		if (instanceTimer == null) {
			startTime = System.currentTimeMillis();
			instanceTimer = ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					openDoor(311);
					if (spawnRace == null) {
						spawnRace = player.getRace();
						SpawnRace();
						startAssaultPodTimer();
					}
					instanceReward.setInstanceScoreType(InstanceScoreType.START_PROGRESS);
					sendPacket(0);
				}
			}, 180000);
			instanceTimer = ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					stopInstance(player);
				}

			}, 1980000);
		}
		sendPacket(0);
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
	public void onInstanceDestroy() {
		if (instanceTimer != null) {
			instanceTimer.cancel(false);
		}
		cancelTask();
		isInstanceDestroyed = true;
		AssaultPodAndDrillCancelTask();
		AssaultBombCancelTask();
		doors.clear();
	}

	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
		instanceReward = new SteelWallBastionReward(mapId, instanceId);
		instanceReward.setInstanceScoreType(InstanceScoreType.PREPARING);
		instanceReward.addPoints(20000);
		doors = instance.getDoors();
		int rnd = Rnd.get(1, 2);
		switch (rnd) {
			case 1:
				spawn(230746, 552.5082f, 414.074f, 222.75688f, (byte) 17); // Pashid Assault Tribuni Sentry.
				spawn(231177, 820.55133f, 606.02814f, 239.70607f, (byte) 20); // Deathbringer Tariksha.
				break;
			case 2:
				spawn(231177, 552.5082f, 414.074f, 222.75688f, (byte) 17); // Deathbringer Tariksha.
				spawn(230746, 820.55133f, 606.02814f, 239.70607f, (byte) 20); // Pashid Assault Tribuni Sentry.
				break;
		}
	}

	protected void openDoor(int doorId) {
		StaticDoor door = doors.get(doorId);
		if (door != null) {
			door.setOpen(true);
		}
	}

	private void cancelTask() {
		if (cancelSpawnTask != null && !cancelSpawnTask.isCancelled()) {
			cancelSpawnTask.cancel(true);
		}
	}

	private void spawn(final int npcId, final float x, final float y, final float z, final byte h, final int time, final String walkerId, final boolean isRun) {

		cancelSpawnTask = ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (!isInstanceDestroyed && isCancelled == false) {
					Npc npc = (Npc) spawn(npcId, x, y, z, h);
					npcs.add(npc);
					npc.getSpawn().setWalkerId(walkerId);
					WalkManager.startWalking((NpcAI2) npc.getAi2());
					if (isRun) {
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

	@Override
	public boolean onDie(final Player player, Creature lastAttacker) {
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);
		PacketSendUtility.sendPacket(player, new SM_DIE(player.haveSelfRezEffect(), player.haveSelfRezItem(), 0, 8));
		return true;
	}

	@Override
	public void onExitInstance(Player player) {
		TeleportService2.moveToInstanceExit(player, mapId, player.getRace());
	}
}
