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

import java.util.Set;

import com.aionemu.gameserver.controllers.effect.PlayerEffectController;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.drop.DropItem;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;

import javolution.util.FastMap;

@InstanceID(301660000)
public class KrobanBaseInstance extends GeneralInstanceHandler {

	private Race spawnRace;
	protected boolean isInstanceDestroyed = false;
	@SuppressWarnings("unused")
	private FastMap<Integer, VisibleObject> objects = new FastMap<Integer, VisibleObject>();

	@Override
	public void onEnterInstance(final Player player) {
		super.onInstanceCreate(instance);
		// Lieutenant Anuhart is guarding the iron fence, burning any enemies who approach
		sendMsgByRace(1403414, Race.PC_ALL, 10000);
		// We must destroy that iron fence before Lieutenant Anuhart appears!
		sendMsgByRace(1403415, Race.PC_ALL, 30000);
		// Use the Thorn Tentacle Traps and skills to reduce Lieutenant Anuhart’s movement speed
		sendMsgByRace(1403416, Race.PC_ALL, 50000);
		// Anuhart sends the command! The Device Maintenance Soldiers are activating the Balaur Explosives Stockpile!
		sendMsgByRace(1403420, Race.PC_ALL, 70000);
		// The Spore Road Post Monitoring Device can deal massive ammounts of damage with its magic cannons
		sendMsgByRace(1403426, Race.PC_ALL, 90000);
		// You can now steal and board the Spore Road Balaur Aether Cannon
		sendMsgByRace(1403427, Race.PC_ALL, 110000);
		if (spawnRace == null) {
			spawnRace = player.getRace();
			SpawnKrobanBaseRace();
		}
	}

	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
		// Lieutenant Anuhart will appear shortly
		sendMsgByRace(1403417, Race.PC_ALL, 20000);
		// Lieutenant Anuhart approaches
		sendMsgByRace(1403418, Race.PC_ALL, 120000);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawn(243682, 284.08746f, 1001.5276f, 112.69383f, (byte) 90); // Lieutenant Anuhart
			}
		}, 120000);
	}

	@Override
	public void onDropRegistered(Npc npc) {
		Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		int npcId = npc.getNpcId();
		int index = dropItems.size() + 1;
		switch (npcId) {
			case 703372: // Tahabata's Treasure Chest
			case 703373: // Kroban's Treasure Chest
				for (Player player : instance.getPlayersInside()) {
					if (player.isOnline()) {
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188055385, 1)); // Kroban’s Treasure
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188055389, 1)); // Kroban's Illusion Godstone Bundle
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188055390, 1)); // Kroban’s Conditioning Bundle
					}
				}
				break;
			case 833862: // Supply Box
				for (Player player : instance.getPlayersInside()) {
					if (player.isOnline()) {
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 164002346, 2)); // Thorn Tentacle Trap
					}
				}
				break;
		}
	}

	private void removeItems(Player player) {
		Storage storage = player.getInventory();
		storage.decreaseByItemId(164002346, storage.getItemCountByItemId(164002346)); // Thorn Tentacle Trap
	}

	@Override
	public void onDie(Npc npc) {
		Player player = npc.getAggroList().getMostPlayerDamage();
		switch (npc.getObjectTemplate().getTemplateId()) {
			case 703293: // Barricade on Darkspore Road
				switch (player.getRace()) {
					case ELYOS:
						despawnNpc(npc);
						deleteNpc(833843);
						// A teleport device for Kroban’s Burning Base was created
						sendMsgByRace(1403559, Race.PC_ALL, 0);
						// The Dark Spore Road has turned into a sea of flames
						sendMsgByRace(1403421, Race.PC_ALL, 20000);
						spawn(833854, 299.90918f, 916.87146f, 105.55609f, (byte) 105); // Animar
						ThreadPoolManager.getInstance().schedule(new Runnable() {

							@Override
							public void run() {
								seaOfFlames1();
							}
						}, 20000);
						break;
					case ASMODIANS:
						despawnNpc(npc);
						deleteNpc(833844);
						// A teleport device for Kroban’s Burning Base was created
						sendMsgByRace(1403559, Race.PC_ALL, 0);
						// The Dark Spore Road has turned into a sea of flames
						sendMsgByRace(1403421, Race.PC_ALL, 20000);
						spawn(833858, 299.90918f, 916.87146f, 105.55609f, (byte) 105);; // Kantil
						ThreadPoolManager.getInstance().schedule(new Runnable() {

							@Override
							public void run() {
								seaOfFlames1();
							}
						}, 20000);
						break;
					default:
						break;
				}
				break;
			case 703296: // Barricade on Darkspore Road
				switch (player.getRace()) {
					case ELYOS:
						despawnNpc(npc);
						deleteNpc(833854);
						// A teleport device for Kroban’s Burning Base was created
						sendMsgByRace(1403559, Race.PC_ALL, 0);
						// The Dark Spore Road has turned into a sea of flames
						sendMsgByRace(1403421, Race.PC_ALL, 20000);
						spawn(833855, 299.90918f, 916.87146f, 105.55609f, (byte) 105); // Animar
						ThreadPoolManager.getInstance().schedule(new Runnable() {

							@Override
							public void run() {
								seaOfFlames2();
							}
						}, 20000);
						break;
					case ASMODIANS:
						despawnNpc(npc);
						deleteNpc(833858);
						// A teleport device for Kroban’s Burning Base was created
						sendMsgByRace(1403559, Race.PC_ALL, 0);
						// The Dark Spore Road has turned into a sea of flames
						sendMsgByRace(1403421, Race.PC_ALL, 20000);
						spawn(833859, 299.90918f, 916.87146f, 105.55609f, (byte) 105); // Kantil
						ThreadPoolManager.getInstance().schedule(new Runnable() {

							@Override
							public void run() {
								seaOfFlames2();
							}
						}, 20000);
						break;
					default:
						break;
				}
				break;
			case 703297: // Barricade at Timola Mine
				switch (player.getRace()) {
					case ELYOS:
						despawnNpc(npc);
						deleteNpc(833856);
						deleteNpc(243682); // Adjutant Anuhart
						// A teleport device for Kroban’s Burning Base was created
						sendMsgByRace(1403559, Race.PC_ALL, 0);
						// Lieutenant Anuhart has given up the pursuit and has disappeared
						sendMsgByRace(1403444, Race.PC_ALL, 10000);
						// The Dark Spore Road has turned into a sea of flames
						sendMsgByRace(1403421, Race.PC_ALL, 20000);
						spawn(833857, 299.90918f, 916.87146f, 105.55609f, (byte) 105); // Animar
						ThreadPoolManager.getInstance().schedule(new Runnable() {

							@Override
							public void run() {
								seaOfFlames4();
							}
						}, 20000);
						break;
					case ASMODIANS:
						despawnNpc(npc);
						deleteNpc(833860);
						deleteNpc(243682); // Adjutant Anuhart
						// A teleport device for Kroban’s Burning Base was created
						sendMsgByRace(1403559, Race.PC_ALL, 0);
						// Lieutenant Anuhart has given up the pursuit and has disappeared
						sendMsgByRace(1403444, Race.PC_ALL, 10000);
						// The Dark Spore Road has turned into a sea of flames
						sendMsgByRace(1403421, Race.PC_ALL, 20000);
						spawn(833861, 299.90918f, 916.87146f, 105.55609f, (byte) 105); // Kantil
						ThreadPoolManager.getInstance().schedule(new Runnable() {

							@Override
							public void run() {
								seaOfFlames4();
							}
						}, 20000);
						break;
					default:
						break;
				}
				break;
			case 243683: // Brigade General Tahabata
				despawnNpc(npc);
				spawn(833853, 232.04787f, 332.55054f, 132.31018f, (byte) 48); // Kroban Base Teleporter
				spawn(703372, npc.getX(), npc.getY(), npc.getZ(), (byte) 0); // Tahabata's Treasure Chest
				break;
			case 243684: // Artefact Guardian Kroban
				despawnNpc(npc);
				spawn(703373, npc.getX(), npc.getY(), npc.getZ(), (byte) 0); // Kroban's Treasure Chest
				spawn(833852, 1178.5111f, 1221.9156f, 145.448f, (byte) 25); // Kroban Base Exit
				break;
		}
	}

	@Override
	public void handleUseItemFinish(Player player, Npc npc) {
		switch (npc.getNpcId()) {
			case 703302: // Controllable Aether Cannon
				despawnNpc(npc);
				SkillEngine.getInstance().getSkill(npc, 21805, 1, player).useNoAnimationSkill();
				break;
			case 703303: // Controllable Aether Cannon
				despawnNpc(npc);
				SkillEngine.getInstance().getSkill(npc, 21805, 1, player).useNoAnimationSkill();
				break;
			case 703304: // Controllable Aether Cannon
				despawnNpc(npc);
				SkillEngine.getInstance().getSkill(npc, 21805, 1, player).useNoAnimationSkill();
				break;
			case 703306: // Controllable Aether Cannon
				despawnNpc(npc);
				SkillEngine.getInstance().getSkill(npc, 21806, 1, player).useNoAnimationSkill();
				break;
			case 703307: // Controllable Aether Cannon
				despawnNpc(npc);
				SkillEngine.getInstance().getSkill(npc, 21806, 1, player).useNoAnimationSkill();
				break;
			case 703308: // Controllable Aether Cannon
				despawnNpc(npc);
				SkillEngine.getInstance().getSkill(npc, 21806, 1, player).useNoAnimationSkill();
				break;
		}
	}

	private void seaOfFlames1() {
		spawn(243952, 288.4514f, 928.72723f, 104.85776f, (byte) 46);
		spawn(243952, 279.38016f, 937.39996f, 103.04628f, (byte) 46);
		spawn(243952, 265.761f, 941.2696f, 100.87882f, (byte) 57);
		spawn(243952, 276.25858f, 951.63635f, 102.90076f, (byte) 22);
		spawn(243952, 280.44476f, 960.348f, 105.21805f, (byte) 24);
		spawn(243952, 283.551f, 976.65814f, 107.817276f, (byte) 28);
		spawn(243952, 296.20764f, 972.11475f, 108.71277f, (byte) 6);
		spawn(243952, 249.14285f, 940.15906f, 100.311485f, (byte) 66);
		spawn(243952, 237.40253f, 937.65735f, 100.21266f, (byte) 72);
		spawn(243952, 227.70697f, 930.4123f, 100.64331f, (byte) 72);
		spawn(243952, 215.41777f, 924.5182f, 101.101166f, (byte) 67);
		spawn(243952, 235.47546f, 952.07434f, 100.90778f, (byte) 34);
		spawn(243952, 233.19786f, 965.635f, 101.27732f, (byte) 34);
		spawn(243952, 230.34238f, 979.22107f, 101.2714f, (byte) 34);
		spawn(243952, 266.37198f, 947.8039f, 101.30749f, (byte) 41);
		spawn(243952, 274.3631f, 943.519f, 102.41302f, (byte) 25);
		spawn(243952, 286.14658f, 952.9042f, 105.45899f, (byte) 24);
		spawn(243952, 232.53708f, 922.10785f, 100.631744f, (byte) 72);
		spawn(243952, 213.09212f, 929.8637f, 101.84944f, (byte) 67);
		spawn(243952, 220.48915f, 934.1818f, 101.43886f, (byte) 71);
		spawn(243952, 249.16116f, 933.94244f, 100.65594f, (byte) 5);
		spawn(243952, 273.93295f, 968.2992f, 106.8435f, (byte) 24);
		spawn(243952, 289.82785f, 961.0512f, 106.32006f, (byte) 24);
		spawn(243952, 245.5708f, 947.28143f, 100.25f, (byte) 51);
		spawn(243952, 229.63419f, 942.6026f, 100.897865f, (byte) 82);
		spawn(243952, 225.56412f, 917.89966f, 100.625f, (byte) 42);
		spawn(243952, 208.67413f, 920.91785f, 101.0f, (byte) 69);
	}

	private void seaOfFlames2() {
		spawn(243952, 200.54776f, 917.40173f, 101.25316f, (byte) 70);
		spawn(243952, 193.78343f, 913.6624f, 101.80573f, (byte) 71);
		spawn(243952, 188.10773f, 902.0539f, 102.6149f, (byte) 84);
		spawn(243952, 182.6066f, 908.3191f, 102.81715f, (byte) 95);
		spawn(243952, 199.4097f, 907.1282f, 102.036896f, (byte) 11);
		spawn(243952, 188.88881f, 920.0784f, 102.19445f, (byte) 92);
		spawn(243952, 189.4067f, 890.8901f, 102.83792f, (byte) 81);
		spawn(243952, 179.44818f, 895.79974f, 102.784485f, (byte) 81);
		spawn(243952, 179.18698f, 880.4128f, 102.22498f, (byte) 84);
		spawn(243952, 184.71758f, 878.56274f, 102.62487f, (byte) 84);
		spawn(243952, 173.92123f, 881.3484f, 102.26477f, (byte) 84);
		spawn(243952, 169.6674f, 864.5626f, 102.29954f, (byte) 111);
		spawn(243952, 186.97244f, 862.8932f, 101.84981f, (byte) 103);
		spawn(243952, 174.35266f, 851.2367f, 100.92271f, (byte) 76);
		spawn(243952, 180.85861f, 858.6011f, 101.3589f, (byte) 38);
		spawn(243952, 180.1472f, 870.70605f, 101.80333f, (byte) 29);
		spawn(243952, 186.05641f, 847.46484f, 101.0f, (byte) 112);
		spawn(243952, 192.72598f, 856.798f, 101.19949f, (byte) 112);
		spawn(243952, 197.5028f, 841.2088f, 100.86728f, (byte) 112);
		spawn(243952, 199.82468f, 846.6691f, 100.5f, (byte) 112);
		spawn(243952, 202.92197f, 854.25867f, 101.46866f, (byte) 112);
		spawn(243952, 213.85631f, 848.5403f, 101.12609f, (byte) 111);
		spawn(243952, 210.09431f, 839.92267f, 100.75377f, (byte) 111);
		spawn(243952, 207.19005f, 833.4048f, 101.63844f, (byte) 111);
		spawn(243952, 219.79854f, 833.68677f, 100.914154f, (byte) 106);
		spawn(243952, 216.45387f, 829.68085f, 101.51153f, (byte) 106);
		spawn(243952, 225.2346f, 839.9147f, 100.5f, (byte) 106);
		spawn(243952, 227.83961f, 826.6211f, 101.81613f, (byte) 102);
		spawn(243952, 221.9174f, 821.7345f, 102.25f, (byte) 102);
		spawn(243952, 233.17325f, 830.8455f, 102.47116f, (byte) 102);
		spawn(243952, 170.6162f, 872.9767f, 102.35902f, (byte) 58);
		spawn(243952, 189.08449f, 886.616f, 103.02928f, (byte) 103);
		spawn(243952, 168.39723f, 858.9887f, 102.01214f, (byte) 67);
		spawn(243952, 234.03479f, 819.4615f, 102.66083f, (byte) 108);
		spawn(243952, 229.91005f, 813.43536f, 102.980865f, (byte) 108);
		spawn(243952, 239.50185f, 827.1065f, 103.31273f, (byte) 108);
		spawn(243952, 246.5085f, 822.3815f, 104.06356f, (byte) 17);
		spawn(243952, 250.29172f, 829.32245f, 104.54234f, (byte) 15);
		spawn(243952, 245.88243f, 834.963f, 104.51007f, (byte) 15);
		spawn(243952, 256.85223f, 834.6816f, 105.46806f, (byte) 15);
		spawn(243952, 252.11853f, 841.50055f, 105.09378f, (byte) 15);
		spawn(243952, 237.96567f, 832.0443f, 103.62687f, (byte) 35);
		spawn(243952, 265.78772f, 840.43f, 105.75f, (byte) 13);
		spawn(243952, 261.97916f, 844.772f, 105.5f, (byte) 13);
		spawn(243952, 259.08527f, 850.34686f, 105.82885f, (byte) 13);
		spawn(243952, 262.93536f, 855.9512f, 105.75f, (byte) 13);
		spawn(243952, 272.07178f, 841.5997f, 105.92953f, (byte) 16);
		spawn(243952, 239.46321f, 815.37286f, 103.21645f, (byte) 98);
		spawn(243952, 245.68912f, 812.66394f, 104.04728f, (byte) 98);
		spawn(243952, 239.7661f, 809.18805f, 103.30075f, (byte) 98);
		spawn(243952, 233.3807f, 805.32886f, 103.875f, (byte) 98);
		spawn(243952, 225.96242f, 817.8285f, 102.52144f, (byte) 105);
		spawn(243952, 247.60118f, 809.0195f, 104.350075f, (byte) 96);
		spawn(243952, 249.66855f, 796.65015f, 103.89492f, (byte) 96);
		spawn(243952, 241.42249f, 793.6862f, 103.51648f, (byte) 96);
		spawn(243952, 233.88933f, 789.94586f, 104.0481f, (byte) 96);
		spawn(243952, 241.24127f, 799.97925f, 103.577576f, (byte) 96);
		spawn(243952, 232.66953f, 798.86865f, 104.0f, (byte) 96);
		spawn(243952, 231.91185f, 811.13556f, 103.358055f, (byte) 95);
		spawn(243952, 249.0608f, 803.61206f, 104.3826f, (byte) 78);
		spawn(243952, 250.11339f, 793.6662f, 103.47245f, (byte) 93);
		spawn(243952, 243.41373f, 790.9393f, 103.220345f, (byte) 93);
		spawn(243952, 237.5806f, 787.14343f, 103.5251f, (byte) 93);
	}

	@SuppressWarnings("unused")
	private void seaOfFlames3() {
		// Hurry and destroy the iron fence to get to the Timolia Abandoned Mine before Lieutenant Anuhart returns.
		sendMsgByRace(1403442, Race.PC_ALL, 30000);
		spawn(243952, 246.878f, 778.5127f, 102.625f, (byte) 95);
		spawn(243952, 242.62125f, 770.5272f, 102.73825f, (byte) 87);
		spawn(243952, 249.14203f, 767.3709f, 101.764305f, (byte) 87);
		spawn(243952, 255.4003f, 766.3358f, 101.75f, (byte) 87);
		spawn(243952, 254.45058f, 775.9949f, 102.5962f, (byte) 35);
		spawn(243952, 240.64401f, 755.2177f, 101.53586f, (byte) 89);
		spawn(243952, 246.74704f, 753.7678f, 100.98549f, (byte) 89);
		spawn(243952, 253.24356f, 753.7627f, 102.07651f, (byte) 89);
		spawn(243952, 256.89877f, 749.0964f, 102.611435f, (byte) 89);
		spawn(243952, 256.90857f, 742.2647f, 101.936905f, (byte) 89);
		spawn(243952, 250.2639f, 738.3609f, 101.07989f, (byte) 89);
		spawn(243952, 243.27936f, 741.8768f, 101.17774f, (byte) 24);
		spawn(243952, 238.36769f, 743.38477f, 101.47702f, (byte) 24);
		spawn(243952, 233.32945f, 744.9314f, 101.89203f, (byte) 24);
		spawn(243952, 234.73366f, 749.447f, 101.98744f, (byte) 24);
		spawn(243952, 238.97853f, 762.5245f, 102.76245f, (byte) 24);
		spawn(243952, 258.90338f, 762.53503f, 102.95282f, (byte) 61);
		spawn(243952, 257.89435f, 752.9006f, 102.86839f, (byte) 16);
		spawn(243952, 246.74129f, 731.59033f, 101.60427f, (byte) 89);
		spawn(243952, 243.13234f, 725.97363f, 101.87335f, (byte) 89);
		spawn(243952, 236.74059f, 722.0256f, 102.325516f, (byte) 89);
		spawn(243952, 227.39397f, 733.66095f, 102.146194f, (byte) 45);
		spawn(243952, 229.6429f, 738.06305f, 101.897316f, (byte) 45);
		spawn(243952, 236.4439f, 732.3632f, 101.75f, (byte) 18);
		spawn(243952, 231.97156f, 716.46484f, 103.31867f, (byte) 78);
		spawn(243952, 225.52414f, 720.5951f, 103.05061f, (byte) 78);
		spawn(243952, 218.93622f, 724.917f, 102.75797f, (byte) 78);
		spawn(243952, 230.58815f, 714.29694f, 103.58788f, (byte) 12);
		spawn(243952, 222.72044f, 735.15594f, 102.75941f, (byte) 19);
		spawn(243952, 213.01143f, 717.6321f, 103.375f, (byte) 19);
		spawn(243952, 219.98059f, 713.63727f, 103.646454f, (byte) 19);
		spawn(243952, 225.97305f, 708.2649f, 104.35339f, (byte) 19);
		spawn(243952, 207.02214f, 710.5011f, 103.87376f, (byte) 79);
		spawn(243952, 212.03679f, 706.41656f, 103.47397f, (byte) 79);
		spawn(243952, 217.37967f, 701.3361f, 103.96123f, (byte) 79);
		spawn(243952, 223.62659f, 705.52136f, 104.4599f, (byte) 16);
		spawn(243952, 200.3668f, 699.1141f, 104.43122f, (byte) 82);
		spawn(243952, 205.1539f, 695.29926f, 103.85576f, (byte) 82);
		spawn(243952, 210.84637f, 691.3745f, 104.50445f, (byte) 82);
		spawn(243952, 192.0856f, 691.96027f, 103.74751f, (byte) 42);
		spawn(243952, 192.26933f, 680.1935f, 102.85817f, (byte) 73);
		spawn(243952, 198.6242f, 683.271f, 103.32289f, (byte) 27);
		spawn(243952, 204.65051f, 680.6512f, 103.49701f, (byte) 27);
		spawn(243952, 184.18658f, 672.9726f, 102.99176f, (byte) 70);
		spawn(243952, 182.61078f, 677.9752f, 102.204445f, (byte) 41);
		spawn(243952, 180.2808f, 662.3416f, 103.4215f, (byte) 82);
		spawn(243952, 205.6189f, 674.8743f, 103.279724f, (byte) 28);
		spawn(243952, 204.71663f, 664.49884f, 103.15646f, (byte) 28);
		spawn(243952, 203.80707f, 654.03723f, 102.47821f, (byte) 28);
		spawn(243952, 209.46384f, 654.1784f, 103.28562f, (byte) 28);
		spawn(243952, 193.85135f, 655.60876f, 102.24071f, (byte) 28);
		spawn(243952, 194.6016f, 664.81226f, 102.17673f, (byte) 28);
		spawn(243952, 195.3209f, 673.08545f, 102.334885f, (byte) 28);
		spawn(243952, 186.25066f, 657.8974f, 102.47792f, (byte) 28);
		spawn(243952, 177.76389f, 651.9322f, 103.68826f, (byte) 76);
		spawn(243952, 181.83261f, 646.933f, 103.7793f, (byte) 76);
		spawn(243952, 187.28308f, 640.9171f, 103.917656f, (byte) 76);
		spawn(243952, 192.8168f, 639.52374f, 103.51535f, (byte) 76);
		spawn(243952, 200.49438f, 636.8419f, 103.07238f, (byte) 83);
		spawn(243952, 173.4114f, 645.6625f, 105.256454f, (byte) 71);
		spawn(243952, 174.15196f, 641.1906f, 105.45718f, (byte) 71);
		spawn(243952, 177.79543f, 635.4868f, 106.21802f, (byte) 71);
		spawn(243952, 157.00626f, 636.19434f, 108.57191f, (byte) 70);
		spawn(243952, 158.0596f, 631.62024f, 108.15757f, (byte) 70);
		spawn(243952, 160.65962f, 627.4136f, 108.646614f, (byte) 70);
		spawn(243952, 171.2628f, 634.41907f, 106.70427f, (byte) 74);
		spawn(243952, 166.74934f, 636.55774f, 106.621475f, (byte) 74);
		spawn(243952, 165.9313f, 640.4021f, 106.746994f, (byte) 74);
		spawn(243952, 215.5644f, 646.4056f, 103.61749f, (byte) 48);
		spawn(243952, 211.67717f, 641.10925f, 103.01532f, (byte) 48);
		spawn(243952, 208.04857f, 636.0795f, 103.368095f, (byte) 48);
		spawn(243952, 205.66212f, 630.268f, 104.34586f, (byte) 48);
		spawn(243952, 211.6032f, 627.817f, 105.14787f, (byte) 111);
		spawn(243952, 218.62885f, 635.5981f, 103.88594f, (byte) 111);
		spawn(243952, 221.8294f, 642.43335f, 104.14719f, (byte) 111);
		spawn(243952, 223.55545f, 646.2172f, 104.554306f, (byte) 111);
		spawn(243952, 210.40349f, 653.2234f, 103.402115f, (byte) 28);
		spawn(243952, 175.12845f, 658.3339f, 104.32683f, (byte) 73);
		spawn(243952, 179.16118f, 668.2577f, 103.84279f, (byte) 59);
		spawn(243952, 201.55246f, 646.47925f, 102.25f, (byte) 115);
		spawn(243952, 208.9045f, 665.3597f, 103.90765f, (byte) 31);
		spawn(243952, 231.32822f, 640.17737f, 104.73019f, (byte) 109);
		spawn(243952, 227.67091f, 633.86053f, 104.25f, (byte) 109);
		spawn(243952, 227.10435f, 624.8397f, 104.44402f, (byte) 118);
		spawn(243952, 220.079f, 627.40656f, 104.80285f, (byte) 116);
		spawn(243952, 208.33537f, 623.8257f, 106.29441f, (byte) 13);
	}

	private void seaOfFlames4() {
		spawn(243952, 238.59293f, 628.3171f, 104.212944f, (byte) 110);
		spawn(243952, 245.87968f, 621.13306f, 103.75752f, (byte) 102);
		spawn(243952, 240.91885f, 617.05084f, 104.38893f, (byte) 102);
		spawn(243952, 251.26411f, 625.3317f, 104.032715f, (byte) 102);
		spawn(243952, 251.50185f, 608.86835f, 103.35186f, (byte) 99);
		spawn(243952, 258.1115f, 598.5f, 104.31947f, (byte) 97);
		spawn(243952, 255.07419f, 589.36816f, 105.98419f, (byte) 89);
		spawn(243952, 261.8764f, 589.25824f, 106.006355f, (byte) 89);
		spawn(243952, 268.6229f, 589.236f, 107.290764f, (byte) 89);
		spawn(243952, 252.43243f, 583.1061f, 107.376816f, (byte) 59);
		spawn(243952, 267.48343f, 581.1005f, 108.12086f, (byte) 1);
		spawn(243952, 272.83896f, 582.24146f, 110.08686f, (byte) 117);
		spawn(243952, 268.56384f, 595.39453f, 107.36648f, (byte) 10);
		spawn(243952, 249.39738f, 576.83734f, 107.87766f, (byte) 75);
		spawn(243952, 253.1255f, 573.2829f, 107.580185f, (byte) 75);
		spawn(243952, 258.31662f, 568.33295f, 107.997955f, (byte) 75);
		spawn(243952, 243.0277f, 565.3803f, 107.0f, (byte) 74);
		spawn(243952, 238.98526f, 569.65094f, 107.35687f, (byte) 74);
		spawn(243952, 248.086f, 559.7841f, 107.36595f, (byte) 74);
		spawn(243952, 249.79907f, 556.34467f, 108.88515f, (byte) 106);
		spawn(243952, 232.03036f, 556.2732f, 107.481026f, (byte) 75);
		spawn(243952, 227.2535f, 560.93353f, 108.08336f, (byte) 75);
		spawn(243952, 236.78029f, 551.41486f, 108.35663f, (byte) 75);
		spawn(243952, 244.13434f, 574.89557f, 108.21176f, (byte) 50);
		spawn(243952, 255.09769f, 562.21533f, 108.364105f, (byte) 100);
		spawn(243952, 219.66756f, 550.1641f, 108.270775f, (byte) 84);
		spawn(243952, 224.71852f, 548.7014f, 108.25107f, (byte) 84);
		spawn(243952, 230.41577f, 545.62866f, 108.775986f, (byte) 84);
		spawn(243952, 223.28186f, 558.4652f, 108.353035f, (byte) 44);
		spawn(243952, 222.50873f, 558.8464f, 108.449005f, (byte) 103);
		spawn(243952, 214.94992f, 539.9176f, 109.07078f, (byte) 87);
		spawn(243952, 219.1197f, 539.34314f, 108.91605f, (byte) 87);
		spawn(243952, 226.3118f, 538.3523f, 109.53391f, (byte) 87);
		spawn(243952, 211.06891f, 536.0202f, 110.10904f, (byte) 60);
		spawn(243952, 224.81389f, 527.98645f, 110.07762f, (byte) 87);
		spawn(243952, 216.22736f, 528.40155f, 109.94649f, (byte) 88);
		spawn(243952, 209.85944f, 528.7804f, 110.727585f, (byte) 88);
		spawn(243952, 202.61653f, 527.0307f, 111.64705f, (byte) 63);
		spawn(243952, 194.71066f, 525.70325f, 112.22413f, (byte) 63);
		spawn(243952, 188.06161f, 526.77997f, 112.49615f, (byte) 56);
		spawn(243952, 181.14055f, 527.9301f, 112.625f, (byte) 56);
		spawn(243952, 181.92523f, 533.1982f, 112.81413f, (byte) 56);
		spawn(243952, 180.05997f, 521.9683f, 113.00396f, (byte) 56);
		spawn(243952, 201.8819f, 519.45337f, 113.04582f, (byte) 19);
		spawn(243952, 205.675f, 531.9391f, 111.43213f, (byte) 45);
		spawn(243952, 209.97952f, 516.0203f, 112.62631f, (byte) 97);
		spawn(243952, 201.97473f, 517.0627f, 113.9655f, (byte) 93);
		spawn(243952, 204.68748f, 532.24817f, 111.667206f, (byte) 40);
		spawn(243952, 227.14633f, 534.9925f, 110.350494f, (byte) 57);
		spawn(243952, 232.47845f, 548.66064f, 108.65604f, (byte) 49);
		spawn(243952, 226.86743f, 522.05145f, 110.35843f, (byte) 97);
		spawn(243952, 220.8648f, 517.5569f, 110.25f, (byte) 97);
		spawn(243952, 214.58116f, 513.5535f, 111.28552f, (byte) 97);
		spawn(243952, 231.74294f, 510.39728f, 110.45911f, (byte) 93);
		spawn(243952, 225.45975f, 509.30273f, 110.25f, (byte) 93);
		spawn(243952, 217.89282f, 506.05917f, 111.27609f, (byte) 107);
		spawn(243952, 209.04369f, 507.65628f, 113.68399f, (byte) 79);
		spawn(243952, 215.01308f, 522.001f, 110.18505f, (byte) 50);
		spawn(243952, 236.78786f, 514.2593f, 112.32723f, (byte) 105);
		spawn(243952, 225.66809f, 502.91193f, 110.588745f, (byte) 73);
		spawn(243952, 219.84604f, 498.44144f, 112.49381f, (byte) 73);
		spawn(243952, 215.09116f, 502.2388f, 112.73924f, (byte) 70);
		spawn(243952, 209.11134f, 504.03574f, 114.122765f, (byte) 70);
		spawn(243952, 211.55698f, 498.2953f, 113.75421f, (byte) 106);
		spawn(243952, 206.87225f, 514.3617f, 113.65961f, (byte) 68);
		spawn(243952, 233.62784f, 501.85117f, 110.73604f, (byte) 109);
		spawn(243952, 235.30669f, 494.98822f, 111.70314f, (byte) 116);
		spawn(243952, 242.61621f, 503.2268f, 112.83754f, (byte) 24);
		spawn(243952, 240.69037f, 494.0711f, 113.654816f, (byte) 83);
		spawn(243952, 237.86275f, 486.43512f, 113.71475f, (byte) 83);
		spawn(243952, 248.88033f, 504.9102f, 113.06438f, (byte) 3);
		spawn(243952, 236.97408f, 503.8999f, 111.631256f, (byte) 64);
		spawn(243952, 219.19989f, 490.82278f, 112.5f, (byte) 103);
		spawn(243952, 214.44879f, 493.22098f, 112.42369f, (byte) 70);
		spawn(243952, 230.58171f, 489.14218f, 112.16268f, (byte) 88);
		spawn(243952, 223.14505f, 489.85794f, 112.35687f, (byte) 88);
		spawn(243952, 230.99808f, 494.90497f, 111.329834f, (byte) 88);
		spawn(243952, 219.1302f, 488.0023f, 112.5f, (byte) 101);
		spawn(243952, 233.2063f, 483.10345f, 112.58182f, (byte) 83);
		spawn(243952, 227.80093f, 485.04068f, 112.18496f, (byte) 83);
		spawn(243952, 222.24724f, 487.56342f, 112.46909f, (byte) 83);
		spawn(243952, 212.44034f, 547.90576f, 109.52983f, (byte) 50);
		spawn(243952, 209.56004f, 544.0701f, 110.52497f, (byte) 50);
		spawn(243952, 216.70564f, 554.6212f, 109.22037f, (byte) 50);
		spawn(243952, 249.69872f, 583.818f, 107.7117f, (byte) 53);
		spawn(243952, 191.75363f, 530.2907f, 112.30882f, (byte) 59);
		spawn(243952, 185.62698f, 521.16547f, 113.148094f, (byte) 98);
		spawn(243952, 226.95226f, 522.12164f, 110.36903f, (byte) 62);
	}

	private void SpawnKrobanBaseRace() {
		final int Kantil_Animar = spawnRace == Race.ASMODIANS ? 833844 : 833843;
		final int RoadAethercannon1 = spawnRace == Race.ASMODIANS ? 703306 : 703302;
		final int RoadAethercannon2 = spawnRace == Race.ASMODIANS ? 703307 : 703303;
		final int RoadAethercannon3 = spawnRace == Race.ASMODIANS ? 703308 : 703304;
		final int Flag03 = spawnRace == Race.ASMODIANS ? 703348 : 703347;
		final int IDF6LF1NPCRa = spawnRace == Race.ASMODIANS ? 834035 : 834034;
		final int IDF6LF1NPCRa01 = spawnRace == Race.ASMODIANS ? 834038 : 834037;
		final int IDF6LF1NPCRa02 = spawnRace == Race.ASMODIANS ? 834047 : 834046;
		final int IDF6LF1NPCRa03 = spawnRace == Race.ASMODIANS ? 834156 : 834155;
		spawn(Kantil_Animar, 299.90918f, 916.87146f, 105.55609f, (byte) 105);
		spawn(RoadAethercannon1, 215.5626f, 916.3464f, 101.32052f, (byte) 59);
		spawn(RoadAethercannon2, 234.21844f, 792.85767f, 104.19027f, (byte) 108);
		spawn(RoadAethercannon3, 222.37325f, 627.35815f, 104.743256f, (byte) 3);
		spawn(Flag03, 278.63245f, 857.02380f, 105.71810f, (byte) 0);
		spawn(Flag03, 226.86122f, 999.90332f, 102.10678f, (byte) 0);
		spawn(Flag03, 140.48065f, 618.59387f, 112.05079f, (byte) 0);
		spawn(Flag03, 158.39459f, 527.29999f, 113.84072f, (byte) 0);
		spawn(IDF6LF1NPCRa, 301.74518f, 911.5706f, 105.5561f, (byte) 1);
		spawn(IDF6LF1NPCRa01, 286.70276f, 940.32965f, 104.82129f, (byte) 65);
		spawn(IDF6LF1NPCRa02, 276.9987f, 959.0241f, 104.63086f, (byte) 24);
		spawn(IDF6LF1NPCRa02, 285.81726f, 959.1506f, 105.89383f, (byte) 31);
		spawn(IDF6LF1NPCRa02, 273.9784f, 960.7828f, 104.69299f, (byte) 21);
		spawn(IDF6LF1NPCRa02, 288.9933f, 958.93866f, 106.11733f, (byte) 30);
		spawn(IDF6LF1NPCRa03, 287.49606f, 961.96747f, 106.27848f, (byte) 31);
		spawn(IDF6LF1NPCRa03, 277.42953f, 962.95776f, 105.60828f, (byte) 21);
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
	public void onInstanceDestroy() {
		isInstanceDestroyed = true;
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

	private void removeEffects(Player player) {
		PlayerEffectController effectController = player.getEffectController();
		effectController.removeEffect(21805);
		effectController.removeEffect(21806);
	}

	@Override
	public void onPlayerLogOut(Player player) {
		removeItems(player);
		removeEffects(player);
	}

	@Override
	public void onLeaveInstance(Player player) {
		removeItems(player);
		removeEffects(player);
	}

	@Override
	public boolean onReviveEvent(Player player) {
		player.getGameStats().updateStatsAndSpeedVisually();
		PlayerReviveService.revive(player, 100, 100, false, 0);
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
		PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_INSTANT_DUNGEON_RESURRECT, 0, 0));
		return TeleportService2.teleportTo(player, mapId, instanceId, 306.14468f, 911.5917f, 105.5561f, (byte) 46);
	}

	@Override
	public boolean onDie(final Player player, Creature lastAttacker) {
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);
		PacketSendUtility.sendPacket(player, new SM_DIE(player.haveSelfRezEffect(), player.haveSelfRezItem(), 0, 8));
		return true;
	}
}
