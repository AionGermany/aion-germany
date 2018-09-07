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
package instance.battlefield;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

import javolution.util.FastList;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.ai2.manager.WalkManager;
import com.aionemu.gameserver.controllers.effect.PlayerEffectController;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.actions.PlayerActions;
import com.aionemu.gameserver.model.drop.DropItem;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.instance.InstanceScoreType;
import com.aionemu.gameserver.model.instance.instancereward.InstanceReward;
import com.aionemu.gameserver.model.instance.instancereward.PandaemoniumBattlefieldReward;
import com.aionemu.gameserver.model.instance.playerreward.PandaemoniumBattlefieldPlayerReward;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INSTANCE_SCORE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.AutoGroupService;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.skillengine.model.SkillTemplate;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;

/**
 * @author Rinzler
 */
@InstanceID(302300000)
public class PandaemoniumBattlefieldInstance extends GeneralInstanceHandler {

	private int rank;
	private long startTime;
	private int surkanaGuardTower;
	private int dredgionMainModule;
	private int surkanaShockCannon;
	private Future<?> timerPrepare;
	private Future<?> timerInstance;
	//Preparation Time.
	private int prepareTimerSeconds = 60000; //...1Min
	//Duration Instance Time.
	private int instanceTimerSeconds = 2700000; //...45Min
	private boolean isInstanceDestroyed;
	private PandaemoniumBattlefieldReward instanceReward;
	private List<Integer> movies = new ArrayList<Integer>();
	private final FastList<Future<?>> pandaemoniumTask = FastList.newInstance();
	
	protected PandaemoniumBattlefieldPlayerReward getPlayerReward(Integer object) {
		return (PandaemoniumBattlefieldPlayerReward) instanceReward.getPlayerReward(object);
	}
	
	protected void addPlayerReward(Player player) {
		instanceReward.addPlayerReward(new PandaemoniumBattlefieldPlayerReward(player.getObjectId()));
	}
	
	@SuppressWarnings("unused")
	private boolean containPlayer(Integer object) {
		return instanceReward.containPlayer(object);
	}
	
	@Override
	public InstanceReward<?> getInstanceReward() {
		return instanceReward;
	}
	
	@SuppressWarnings("unused")
	public void onDropRegistered(Npc npc) {
		Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		int npcId = npc.getNpcId();
		switch (npcId) {
			case 0:
			break;
		}
	}
	
	@Override
	public void onDie(Npc npc) {
		int points = 0;
		int npcId = npc.getNpcId();
		Player player = npc.getAggroList().getMostPlayerDamage();
		switch (npc.getObjectTemplate().getTemplateId()) {
			//Frigida Rendscale Captain.
			case 220989:
			case 220990:
			    points = 341;
				despawnNpc(npc);
			break;
			//Frigida Rendscale Captain.
			case 220984:
			case 220985:
			case 220986:
			    points = 475;
				despawnNpc(npc);
			break;
			//Drakan Guardian.
			case 220740:
			    points = 484;
				despawnNpc(npc);
			break;
			//Frigida Rendscale Captain.
			case 220836:
			case 220837:
			case 220838:
			case 220839:
			case 220840:
			    points = 504;
				despawnNpc(npc);
			break;
			//Frigida Rendscale Captain.
			case 220987:
			case 220988:
			    points = 556;
				despawnNpc(npc);
			break;
			case 220729: //Frigida Drakan Overseer.
			    points = 3000;
				despawnNpc(npc);
			    ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						spawn(220730, 1626.0737f, 1400.7896f, 193.12747f, (byte) 60);
						spawn(220730, 1616.4174f, 1416.2407f, 193.12762f, (byte) 83);
						spawn(220730, 1616.3749f, 1386.3310f, 193.12780f, (byte) 45);
						spawn(220732, 1629.8671f, 1403.6998f, 193.12735f, (byte) 60);
						spawn(220732, 1614.0718f, 1420.1050f, 193.12760f, (byte) 83);
						spawn(220732, 1621.0334f, 1386.4001f, 193.12756f, (byte) 43);
						spawn(220733, 1629.8788f, 1397.4912f, 193.12679f, (byte) 60);
						spawn(220733, 1621.1635f, 1417.4882f, 193.12730f, (byte) 83);
						spawn(220733, 1615.1737f, 1381.2836f, 193.12766f, (byte) 43);
						spawn(220734, 1633.3741f, 1400.7876f, 193.12727f, (byte) 60);
						spawn(220734, 1618.3029f, 1421.2528f, 193.12737f, (byte) 83);
						spawn(220734, 1620.3314f, 1381.8000f, 193.12741f, (byte) 43);
						spawn(220729, 1640.7166f, 1400.8147f, 193.12685f, (byte) 60);
					}
				}, 180000);
			break;
			case 221015: //Frigida Drakan Overseer.
			    points = 3000;
				despawnNpc(npc);
			    ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						spawn(220730, 1334.5089f, 1403.1086f, 208.00000f, (byte) 60);
						spawn(220731, 1322.8188f, 1391.6451f, 208.10526f, (byte) 37);
						spawn(220731, 1314.9281f, 1411.0901f, 208.14000f, (byte) 97);
						spawn(220732, 1337.1184f, 1406.4703f, 208.00000f, (byte) 60);
						spawn(220733, 1337.0092f, 1399.2147f, 208.00000f, (byte) 60);
						spawn(220734, 1339.4343f, 1403.0846f, 208.00000f, (byte) 60);
						spawn(221015, 1345.4376f, 1402.9100f, 208.00000f, (byte) 60);
					}
				}, 180000);
			break;
			case 221016: //Frigida Drakan Overseer.
			    points = 3000;
				despawnNpc(npc);
			    ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						spawn(220730, 1346.6378f, 1282.8210f, 208.125f, (byte) 39);
						spawn(220732, 1352.2228f, 1281.6069f, 208.125f, (byte) 39);
						spawn(220733, 1344.3107f, 1277.1624f, 208.125f, (byte) 39);
						spawn(220734, 1350.0455f, 1276.7498f, 208.125f, (byte) 39);
						spawn(221016, 1352.4276f, 1272.1276f, 208.125f, (byte) 38);
					}
				}, 180000);
			break;
			case 221017: //Frigida Drakan Overseer.
			    points = 3000;
				despawnNpc(npc);
			    ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						spawn(220730, 1162.6453f, 1416.4698f, 208.125f, (byte) 108);
						spawn(220732, 1157.5188f, 1414.8334f, 208.125f, (byte) 108);
						spawn(220733, 1162.1714f, 1421.6080f, 208.125f, (byte) 108);
						spawn(220734, 1157.5342f, 1420.0648f, 208.125f, (byte) 107);
						spawn(221017, 1153.8054f, 1422.7554f, 208.125f, (byte) 106);
					}
				}, 180000);
			break;
			case 221018: //Frigida Drakan Overseer.
			    points = 3000;
				despawnNpc(npc);
			    ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						spawn(220730, 978.2123f, 1131.3398f, 201.39049f, (byte) 4);
						spawn(220732, 976.2579f, 1127.3010f, 201.38469f, (byte) 4);
						spawn(220733, 974.4632f, 1134.0951f, 201.38458f, (byte) 4);
						spawn(220734, 973.1564f, 1130.0658f, 201.38007f, (byte) 4);
						spawn(221018, 969.5107f, 1129.2478f, 201.38405f, (byte) 4);
					}
				}, 180000);
			break;
			case 220716: //Frigida Drakan Commander.
			    points = 3334;
				despawnNpc(npc);
				sp(220935, 1275.4795f, 1169.7278f, 215.21492f, (byte) 29, 0); //Balder.
				sp(220936, 1279.9949f, 1171.2949f, 215.21492f, (byte) 30, 0); //Narvi.
				sp(220937, 1271.3483f, 1171.6832f, 215.21492f, (byte) 31, 0); //Bor.
				sp(220938, 1281.2504f, 1176.6766f, 215.09242f, (byte) 35, 0); //Kalsten.
				sp(220939, 1270.2383f, 1176.2891f, 215.09242f, (byte) 23, 0); //Ve.
				sp(220940, 1277.7382f, 1180.3905f, 214.9424f, (byte) 29, 0); //Mejaina.
				sp(220941, 1274.3058f, 1180.4589f, 214.92348f, (byte) 30, 0); //Godfrid.
			break;
			case 220717: //Frigida Drakan Commander.
			    points = 3334;
				despawnNpc(npc);
				sp(220942, 1340.5422f, 1524.6824f, 209.80017f, (byte) 83, 0); //Vidar.
				sp(220943, 1341.8986f, 1521.3755f, 209.80017f, (byte) 83, 0); //Skadi.
				sp(220944, 1337.2814f, 1523.199f, 209.80017f, (byte) 83, 0); //Njord.
				sp(220945, 1333.9862f, 1521.7096f, 209.80017f, (byte) 83, 0); //Traufnir.
				sp(220946, 1343.4745f, 1518.0729f, 209.80017f, (byte) 83, 0); //Brinhild.
				sp(220947, 1345.1395f, 1523.0059f, 209.80017f, (byte) 83, 0); //Sif.
				sp(220948, 1335.7626f, 1526.3031f, 209.80017f, (byte) 83, 0); //Freyr.
			break;
			case 220718: //Frigida Drakan Commander.
			    points = 3334;
				despawnNpc(npc);
				sp(220949, 1468.1849f, 1342.8291f, 177.16087f, (byte) 30, 20000); //Kvasir.
				sp(220950, 1480.9142f, 1340.9434f, 176.9295f, (byte) 29, 20000); //Fenris's Fangs Elite Soldier.
				sp(220950, 1454.3527f, 1340.7574f, 176.9295f, (byte) 30, 20000); //Fenris's Fangs Elite Soldier.
				sp(220951, 1471.2443f, 1344.69f, 176.9295f, (byte) 19, 20000); //Thialfi.
				sp(220952, 1465.2246f, 1344.8368f, 176.9295f, (byte) 44, 20000); //Lyfjaberga.
				sp(220953, 1462.4227f, 1346.1808f, 176.9295f, (byte) 45, 20000); //Hadubrand.
				sp(220954, 1473.009f, 1346.1925f, 176.9295f, (byte) 16, 20000); //Sigyn.
			break;
			//Dredgion Main Module.
			case 220768:
			    points = 3750;
				despawnNpc(npc);
			break;
			//Dredgion Ship Module.
			case 220966:
			case 220967:
			    points = 10000;
				despawnNpc(npc);
				//Defense Turret has destroyed the Legion's Dredgion.
				sendMsgByRace(1403776, Race.PC_ALL, 2000);
			break;
			//Commander Zedas.
			case 220705:
				points = 30000;
				despawnNpc(npc);
				ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						instance.doOnAllPlayers(new Visitor<Player>() {
							@Override
							public void visit(Player player) {
								stopInstance(player);
							}
						});
					}
				}, 10000);
			break;
			//Captain Jidega.
			case 220752:
				points = 30000;
				killNpc(getNpcs(703398));
				spawn(834858, npc.getX(), npc.getY(), npc.getZ(), npc.getHeading()); //Emergency Exit.
			break;
			//Surkana Shock Cannon.
			case 220766:
			case 220767:
				surkanaShockCannon++;
				if (surkanaShockCannon == 2) {
					killNpc(getNpcs(703399));
					killNpc(getNpcs(703400));
					killNpc(getNpcs(220753));
					killNpc(getNpcs(220754));
					//The shock cannon at the Captain’s Cabin has been deactivated.
				    sendMsgByRace(1403955, Race.PC_ALL, 2000);
				}
			break;
			//Dredgion Main Module.
			case 220866:
			    despawnNpc(npc);
				dredgionMainModule++;
				if (dredgionMainModule == 2) {
					sendMovie(player, 950);
					//The Dredgion was destroyed.
					sendMsgByRace(1403958, Race.PC_ALL, 0);
					ThreadPoolManager.getInstance().schedule(new Runnable() {
						@Override
						public void run() {
							instance.doOnAllPlayers(new Visitor<Player>() {
								@Override
								public void visit(Player player) {
									stopInstance(player);
								}
							});
						}
					}, 60000);
				}
			break;
			case 220854: //Surkana Turret.
				Npc mainModule = instance.getNpc(220866); //Dredgion Main Module.
				surkanaGuardTower++;
				if (mainModule != null) {
					if (surkanaGuardTower == 14) {
						//Dredgion’s Power Core Barrier.
					    mainModule.getEffectController().removeEffect(18298);
				    }
				}
				despawnNpc(npc);
			break;
			// === DREDGION ROOF A ===
			case 220869: //Barrier Power Core 1.
				despawnNpc(npc);
				magicWardEnergy();
				//Activation Stone ready.
				sendMsgByRace(1403956, Race.PC_ALL, 0);
				//The magic ward was activated. Attack and movement speeds increased!
				sendMsgByRace(1403957, Race.PC_ALL, 2000);
				spawn(220978, npc.getX(), npc.getY(), npc.getZ(), npc.getHeading()); //Icy Core 1.
			break;
			case 220870: //Barrier Power Core 2.
				despawnNpc(npc);
				magicWardEnergy();
				//Activation Stone ready.
				sendMsgByRace(1403956, Race.PC_ALL, 0);
				//The magic ward was activated. Attack and movement speeds increased!
				sendMsgByRace(1403957, Race.PC_ALL, 2000);
				spawn(220979, npc.getX(), npc.getY(), npc.getZ(), npc.getHeading()); //Icy Core 2.
			break;
			case 220871: //Barrier Power Core 3.
				despawnNpc(npc);
				magicWardEnergy();
				//Activation Stone ready.
				sendMsgByRace(1403956, Race.PC_ALL, 0);
				//The magic ward was activated. Attack and movement speeds increased!
				sendMsgByRace(1403957, Race.PC_ALL, 2000);
				spawn(220980, npc.getX(), npc.getY(), npc.getZ(), npc.getHeading()); //Icy Core 3.
			break;
			// === DREDGION ROOF B ===
			case 220872: //Barrier Power Core 1.
				despawnNpc(npc);
				magicWardEnergy();
				//Activation Stone ready.
				sendMsgByRace(1403956, Race.PC_ALL, 0);
				//The magic ward was activated. Attack and movement speeds increased!
				sendMsgByRace(1403957, Race.PC_ALL, 2000);
				spawn(221005, npc.getX(), npc.getY(), npc.getZ(), npc.getHeading()); //Icy Core 1.
			break;
			case 220873: //Barrier Power Core 2.
				despawnNpc(npc);
				magicWardEnergy();
				//Activation Stone ready.
				sendMsgByRace(1403956, Race.PC_ALL, 0);
				//The magic ward was activated. Attack and movement speeds increased!
				sendMsgByRace(1403957, Race.PC_ALL, 2000);
				spawn(221006, npc.getX(), npc.getY(), npc.getZ(), npc.getHeading()); //Icy Core 2.
			break;
			case 220874: //Barrier Power Core 3.
				despawnNpc(npc);
				magicWardEnergy();
				//Activation Stone ready.
				sendMsgByRace(1403956, Race.PC_ALL, 0);
				//The magic ward was activated. Attack and movement speeds increased!
				sendMsgByRace(1403957, Race.PC_ALL, 2000);
				spawn(221007, npc.getX(), npc.getY(), npc.getZ(), npc.getHeading()); //Icy Core 3.
			break;
		} if (instanceReward.getInstanceScoreType().isStartProgress()) {
			instanceReward.addNpcKill();
			instanceReward.addPoints(points);
			sendPacket(npc.getObjectTemplate().getNameId(), points);
		} switch (npcId) {
			case 220935: //Balder.
			    despawnNpc(npc);
				deleteNpc(220936); //Narvi.
				deleteNpc(220937); //Bor.
				deleteNpc(220938); //Kalsten.
				deleteNpc(220939); //Ve.
				deleteNpc(220940); //Mejaina.
				deleteNpc(220941); //Godfrid.
				deleteNpc(220716); //Frigida Drakan Commander.
				instanceReward.addPoints(-3334);
				//The Capitol Building has been taken over by the Frigida Fregida Legion.
				sendMsgByRace(1403786, Race.PC_ALL, 0);
				ThreadPoolManager.getInstance().schedule(new Runnable() {
				    @Override
				    public void run() {
						spawn(220935, 1275.4795f, 1169.7278f, 215.21492f, (byte) 29); //Balder.
						spawn(220936, 1279.9949f, 1171.2949f, 215.21492f, (byte) 30); //Narvi.
						spawn(220937, 1271.3483f, 1171.6832f, 215.21492f, (byte) 31); //Bor.
						spawn(220938, 1281.2504f, 1176.6766f, 215.09242f, (byte) 35); //Kalsten.
						spawn(220939, 1270.2383f, 1176.2891f, 215.09242f, (byte) 23); //Ve.
						spawn(220940, 1277.7382f, 1180.3905f, 214.9424f, (byte) 29); //Mejaina.
						spawn(220941, 1274.3058f, 1180.4589f, 214.92348f, (byte) 30); //Godfrid.
						spawn(220716, 1275.3842f, 1217.536f, 214.07533f, (byte) 89); //Frigida Drakan Commander.
				    }
			    }, 15000);
			break;
			case 220942: //Vidar.
			    despawnNpc(npc);
				deleteNpc(220943); //Skadi.
				deleteNpc(220944); //Njord.
				deleteNpc(220945); //Traufnir.
				deleteNpc(220946); //Brinhild.
				deleteNpc(220947); //Sif.
				deleteNpc(220948); //Freyr.
				deleteNpc(220717); //Frigida Drakan Commander.
				instanceReward.addPoints(-3334);
				//The Temple of Gold has been taken over by the Frigida Fregida Legion.
				sendMsgByRace(1403787, Race.PC_ALL, 0);
				ThreadPoolManager.getInstance().schedule(new Runnable() {
				    @Override
				    public void run() {
						spawn(220942, 1340.5422f, 1524.6824f, 209.80017f, (byte) 83); //Vidar.
						spawn(220943, 1341.8986f, 1521.3755f, 209.80017f, (byte) 83); //Skadi.
						spawn(220944, 1337.2814f, 1523.199f, 209.80017f, (byte) 83); //Njord.
						spawn(220945, 1333.9862f, 1521.7096f, 209.80017f, (byte) 83); //Traufnir.
						spawn(220946, 1343.4745f, 1518.0729f, 209.80017f, (byte) 83); //Brinhild.
						spawn(220947, 1345.1395f, 1523.0059f, 209.80017f, (byte) 83); //Sif.
						spawn(220948, 1335.7626f, 1526.3031f, 209.80017f, (byte) 83); //Freyr.
						spawn(220717, 1327.6687f, 1489.3816f, 209.80017f, (byte) 23); //Frigida Drakan Commander.
				    }
			    }, 15000);
			break;
			case 220949: //Kvasir.
			    despawnNpc(npc);
				deleteNpc(220950); //Fenris's Fangs Elite Soldier.
				deleteNpc(220950); //Fenris's Fangs Elite Soldier.
				deleteNpc(220951); //Thialfi.
				deleteNpc(220952); //Lyfjaberga.
				deleteNpc(220953); //Hadubrand.
				deleteNpc(220954); //Sigyn.
				deleteNpc(220718); //Frigida Drakan Commander.
				instanceReward.addPoints(-3334);
				//The Great Temple in Pandaemonium has been taken over by the Frigida Fregida Legion.
				sendMsgByRace(1403788, Race.PC_ALL, 0);
				ThreadPoolManager.getInstance().schedule(new Runnable() {
				    @Override
				    public void run() {
						spawn(220949, 1468.1849f, 1342.8291f, 177.16087f, (byte) 30); //Kvasir.
						spawn(220950, 1480.9142f, 1340.9434f, 176.9295f, (byte) 29); //Fenris's Fangs Elite Soldier.
						spawn(220950, 1454.3527f, 1340.7574f, 176.9295f, (byte) 30); //Fenris's Fangs Elite Soldier.
						spawn(220951, 1471.2443f, 1344.69f, 176.9295f, (byte) 19); //Thialfi.
						spawn(220952, 1465.2246f, 1344.8368f, 176.9295f, (byte) 44); //Lyfjaberga.
						spawn(220953, 1462.4227f, 1346.1808f, 176.9295f, (byte) 45); //Hadubrand.
						spawn(220954, 1473.009f, 1346.1925f, 176.9295f, (byte) 16); //Sigyn.
						spawn(220718, 1466.8002f, 1375.8978f, 177.06723f, (byte) 90); //Frigida Drakan Commander.
				    }
			    }, 15000);
			break;
		}
	}
	
	@Override
	public void handleUseItemFinish(Player player, Npc npc) {
		switch (npc.getNpcId()) {
			case 220780: //Boardable Pandaemonium Defense Turret.
				despawnNpc(npc);
				SkillEngine.getInstance().getSkill(npc, 18290, 1, player).useNoAnimationSkill(); //Use Guard Tower.
			break;
			case 220783: //Turret Generator.
				sp(221022, 2244.9700f, 1277.0400f, 187.97000f, (byte) 0, 239, 2000, 0, null);
				sp(220780, 2236.5474f, 1281.7816f, 188.11302f, (byte) 50, 2000);
				sp(220780, 2245.0496f, 1296.1095f, 188.11317f, (byte) 49, 4000);
				sp(220780, 2261.9040f, 1323.5513f, 188.11629f, (byte) 49, 6000);
				sp(220780, 2286.9258f, 1360.3520f, 190.48573f, (byte) 49, 8000);
			break;
			case 221019: //Turret Generator.
				sp(221023, 2295.8601f, 1353.0900f, 189.38000f, (byte) 0, 209, 2000, 0, null);
				sp(220780, 2293.8800f, 1295.7549f, 188.11633f, (byte) 105, 2000);
				sp(220780, 2268.2783f, 1273.4409f, 188.11330f, (byte) 105, 4000);
			break;
			case 221020: //Turret Generator.
				sp(221024, 2121.4202f, 1358.6936f, 189.91104f, (byte) 0, 299, 2000, 0, null);
				sp(220780, 2129.1816f, 1354.0334f, 190.06018f, (byte) 109, 2000);
				sp(220780, 2142.8557f, 1373.8726f, 190.06052f, (byte) 108, 4000);
				sp(220780, 2158.6926f, 1396.1501f, 190.06296f, (byte) 110, 6000);
				sp(220780, 2175.6953f, 1424.6921f, 192.43256f, (byte) 110, 8000);
			break;
			case 221021: //Turret Generator.
				sp(221025, 2166.4465f, 1429.7555f, 191.36383f, (byte) 0, 297, 2000, 0, null);
				sp(220780, 2131.4832f, 1445.7920f, 192.43256f, (byte) 54, 2000);
				sp(220780, 2120.0066f, 1414.6906f, 190.06288f, (byte) 54, 4000);
				sp(220780, 2112.5032f, 1387.6907f, 190.06018f, (byte) 55, 6000);
				sp(220780, 2106.8875f, 1365.9974f, 190.06032f, (byte) 54, 8000);
			break;
			case 834267: //Dredgion Fissure.
			    if (player.getInventory().decreaseByItemId(185000283, 1)) {
				    invadeIDDC1Dredgion(player, 1496.5532f, 1557.8702f, 2032.4938f, (byte) 30);
					//The shock cannon at the Captain’s Cabin has been activated!
					sendMsgByRace(1403954, Race.PC_ALL, 10000);
				} else {
					//Wind Gap Pass required.
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1403685));
				}
			break;
			case 834288: //Prison Camp.
			    if (player.getInventory().decreaseByItemId(185000281, 1)) {
				    despawnNpc(npc);
				} else {
					//Prison Camp Key required.
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1403686));
				}
			break;
			case 220826: //Pandaemonium Chariot.
			    //Pandaemonium Defense Turret Energy Source.
				if (player.getInventory().decreaseByItemId(185000287, 1)) {
				    sp(834516, 971.10767f, 1147.5275f, 201.09975f, (byte) 0, 2024, 1000, 0, null);
					sp(834517, 978.90906f, 1113.4866f, 201.09975f, (byte) 0, 2110, 1000, 0, null);
				    sp(834516, 971.10767f, 1147.5275f, 201.09975f, (byte) 0, 2128, 3000, 0, null);
					sp(834517, 978.90906f, 1113.4866f, 201.09975f, (byte) 0, 2026, 3000, 0, null);
					sp(834516, 971.10767f, 1147.5275f, 201.09975f, (byte) 0, 1474, 5000, 0, null);
					sp(834517, 978.90906f, 1113.4866f, 201.09975f, (byte) 0, 1437, 5000, 0, null);
					sp(834516, 971.10767f, 1147.5275f, 201.09975f, (byte) 0, 2021, 7000, 0, null);
					sp(834517, 978.90906f, 1113.4866f, 201.09975f, (byte) 0, 2031, 7000, 0, null);
					sp(834516, 971.10767f, 1147.5275f, 201.09975f, (byte) 0, 1834, 9000, 0, null);
					sp(834517, 978.90906f, 1113.4866f, 201.09975f, (byte) 0, 1504, 9000, 0, null);
					sp(834516, 933.62787f, 1121.1869f, 208.64488f, (byte) 0, 1699, 11000, 0, null);
					sp(834517, 933.62787f, 1121.1869f, 208.64488f, (byte) 0, 2030, 11000, 0, null);
					sp(834516, 971.10767f, 1147.5275f, 201.09975f, (byte) 0, 271, 13000, 0, null);
					sp(834517, 978.90906f, 1113.4866f, 201.09975f, (byte) 0, 288, 13000, 0, null);
					sp(834516, 971.10767f, 1147.5275f, 201.09975f, (byte) 0, 283, 15000, 0, null);
					sp(834517, 978.90906f, 1113.4866f, 201.09975f, (byte) 0, 289, 15000, 0, null);
					sp(834516, 933.62787f, 1121.1869f, 208.64488f, (byte) 0, 2127, 17000, 0, null);
					sp(220818, 933.64545f, 1121.3807f, 207.62890f, (byte) 63, 0, 19000, 0, null); //Pandaemonium Defense Turret 1.
					sp(220966, 909.71606f, 1125.7576f, 228.01802f, (byte) 35, 0, 20000, 0, null); //Dreadgion Weapon Main Module 1.
					//Charging complete!
					sendMsgByRace(1403977, Race.PC_ALL, 20000);
				} else {
					//There is no energy source.
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1403694));
				}
			break;
			case 220827: //Pandaemonium Chariot.
			    //Pandaemonium Defense Turret Energy Source.
				if (player.getInventory().decreaseByItemId(185000287, 1)) {
				    sp(834514, 971.25745f, 1512.7778f, 238.62827f, (byte) 0, 2130, 1000, 0, null);
					sp(834515, 1021.9983f, 1563.4001f, 238.72847f, (byte) 0, 1574, 1000, 0, null);
					sp(834514, 971.25745f, 1512.7778f, 238.62827f, (byte) 0, 2022, 3000, 0, null);
					sp(834515, 1021.9983f, 1563.4001f, 238.72847f, (byte) 0, 2126, 3000, 0, null);
					sp(834514, 971.25745f, 1512.7778f, 238.62827f, (byte) 0, 1525, 5000, 0, null);
					sp(834515, 1021.9983f, 1563.4001f, 238.72847f, (byte) 0, 1516, 5000, 0, null);
					sp(834514, 971.25745f, 1512.7778f, 238.62827f, (byte) 0, 2025, 7000, 0, null);
					sp(834515, 1021.9983f, 1563.4001f, 238.72847f, (byte) 0, 1508, 7000, 0, null);
					sp(834514, 971.25745f, 1512.7778f, 238.62827f, (byte) 0, 2125, 9000, 0, null);
					sp(834515, 1021.9983f, 1563.4001f, 238.72847f, (byte) 0, 1512, 9000, 0, null);
					sp(834514, 1001.0653f, 1533.9607f, 242.88528f, (byte) 0, 1485, 11000, 0, null);
					sp(834515, 1001.0653f, 1533.9607f, 242.88528f, (byte) 0, 2109, 11000, 0, null);
					sp(834514, 971.25745f, 1512.7778f, 238.62827f, (byte) 0, 262, 13000, 0, null);
					sp(834515, 1021.9983f, 1563.4001f, 238.72847f, (byte) 0, 264, 13000, 0, null);
					sp(834514, 971.25745f, 1512.7778f, 238.62827f, (byte) 0, 268, 15000, 0, null);
					sp(834515, 1021.9983f, 1563.4001f, 238.72847f, (byte) 0, 269, 15000, 0, null);
					sp(834514, 1001.0653f, 1533.9607f, 242.88528f, (byte) 0, 2035, 17000, 0, null);
					sp(221139, 1000.9909f, 1534.0325f, 243.38289f, (byte) 46, 0, 19000, 0, null); //Pandaemonium Defense Turret 2.
					sp(220967, 979.67236f, 1555.3931f, 258.64360f, (byte) 14, 0, 20000, 0, null); //Dreadgion Weapon Main Module 2.
					//Charging complete!
					sendMsgByRace(1403977, Race.PC_ALL, 20000);
				} else {
					//There is no energy source.
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1403694));
				}
			break;
		}
	}
	
	protected void invadeIDDC1Dredgion(Player player, float x, float y, float z, byte h) {
		TeleportService2.teleportTo(player, mapId, instanceId, x, y, z, h);
	}
	
	private int getTime() {
		long result = (int) (System.currentTimeMillis() - startTime);
		return instanceTimerSeconds - (int) result;
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
	
	private int checkRank(int totalPoints) {
		if (totalPoints >= 164157) { //Rank S.
			rank = 1;
		} else if (totalPoints >= 139674) { //Rank A.
			rank = 2;
		} else if (totalPoints >= 61208) { //Rank B.
			rank = 3;
		} else if (totalPoints >= 39735) { //Rank C.
			rank = 4;
		} else if (totalPoints >= 20000) { //Rank D.
			rank = 5;
		} else {
			rank = 6;
		}
		return rank;
	}
	
	protected void startInstanceTask() {
		pandaemoniumTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
			   /**
				* ■ 1. Drakan Instructor & Base Defence:
				* Drakan Instructors appear in the "Capitol Building, Temple of Gold & Great Temple".
				* After killing each Instructor, friendly NPCs will appear to protect that spot.
				* At the 5th, 15th, and 20th minute, large scale battle begins at each base.
				* If a leader of a base dies, Drakan Instructor will re-appear.
				*/
				//The Frigida Fregida Legion is attacking the National Assembly Building.
				sendMsgByRace(1403783, Race.PC_ALL, 0);
				sp(220716, 1275.3842f, 1217.536f, 214.07533f, (byte) 89, 0); //Frigida Drakan Commander.
				sp(703406, 1275.3842f, 1217.536f, 214.07533f, (byte) 89, 0); //Frigida Drakan Commander [Flag].
				//The Frigida Fregida Legion is attacking the Temple of Gold.
				sendMsgByRace(1403784, Race.PC_ALL, 10000);
				sp(220717, 1327.6687f, 1489.3816f, 209.80017f, (byte) 23, 10000); //Frigida Drakan Commander.
				sp(703406, 1327.6687f, 1489.3816f, 209.80017f, (byte) 23, 10000); //Frigida Drakan Commander [Flag].
				//The Frigida Fregida Legion is attacking the Great Temple in Pandaemonium.
				sendMsgByRace(1403785, Race.PC_ALL, 20000);
				sp(220718, 1466.8002f, 1375.8978f, 177.06723f, (byte) 90, 20000); //Frigida Drakan Commander.
				sp(703406, 1466.8002f, 1375.8978f, 177.06723f, (byte) 90, 20000); //Frigida Drakan Commander [Flag].
            }
        }, 60000)); //1 Min.
		pandaemoniumTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
			   /**
				* ■ 2. Drakan Battle Overseer:
				* 3 min after the start, 3 Drakan Battle Overseers will appear in 3/5 random locations.
				* They will re-appear 3 min after being killed in a random position.
				*/
				switch (Rnd.get(1, 5)) {
				    case 1:
						spawn(220730, 1626.0737f, 1400.7896f, 193.12747f, (byte) 60);
						spawn(220730, 1616.4174f, 1416.2407f, 193.12762f, (byte) 83);
						spawn(220730, 1616.3749f, 1386.3310f, 193.12780f, (byte) 45);
						spawn(220732, 1629.8671f, 1403.6998f, 193.12735f, (byte) 60);
						spawn(220732, 1614.0718f, 1420.1050f, 193.12760f, (byte) 83);
						spawn(220732, 1621.0334f, 1386.4001f, 193.12756f, (byte) 43);
						spawn(220733, 1629.8788f, 1397.4912f, 193.12679f, (byte) 60);
						spawn(220733, 1621.1635f, 1417.4882f, 193.12730f, (byte) 83);
						spawn(220733, 1615.1737f, 1381.2836f, 193.12766f, (byte) 43);
						spawn(220734, 1633.3741f, 1400.7876f, 193.12727f, (byte) 60);
						spawn(220734, 1618.3029f, 1421.2528f, 193.12737f, (byte) 83);
						spawn(220734, 1620.3314f, 1381.8000f, 193.12741f, (byte) 43);
						spawn(220729, 1640.7166f, 1400.8147f, 193.12685f, (byte) 60); //Frigida Drakan Overseer.
				    break;
					case 2:
						spawn(220730, 1334.5089f, 1403.1086f, 208.00000f, (byte) 60);
						spawn(220731, 1322.8188f, 1391.6451f, 208.10526f, (byte) 37);
						spawn(220731, 1314.9281f, 1411.0901f, 208.14000f, (byte) 97);
						spawn(220732, 1337.1184f, 1406.4703f, 208.00000f, (byte) 60);
						spawn(220733, 1337.0092f, 1399.2147f, 208.00000f, (byte) 60);
						spawn(220734, 1339.4343f, 1403.0846f, 208.00000f, (byte) 60);
						spawn(221015, 1345.4376f, 1402.9100f, 208.00000f, (byte) 60); //Frigida Drakan Overseer.
				    break;
					case 3:
						spawn(220730, 1346.6378f, 1282.8210f, 208.125f, (byte) 39);
						spawn(220732, 1352.2228f, 1281.6069f, 208.125f, (byte) 39);
						spawn(220733, 1344.3107f, 1277.1624f, 208.125f, (byte) 39);
						spawn(220734, 1350.0455f, 1276.7498f, 208.125f, (byte) 39);
						spawn(221016, 1352.4276f, 1272.1276f, 208.125f, (byte) 38); //Frigida Drakan Overseer.
					break;
					case 4:
						spawn(220730, 1162.6453f, 1416.4698f, 208.125f, (byte) 108);
						spawn(220732, 1157.5188f, 1414.8334f, 208.125f, (byte) 108);
						spawn(220733, 1162.1714f, 1421.6080f, 208.125f, (byte) 108);
						spawn(220734, 1157.5342f, 1420.0648f, 208.125f, (byte) 107);
						spawn(221017, 1153.8054f, 1422.7554f, 208.125f, (byte) 106); //Frigida Drakan Overseer.
					break;
					case 5:
					    spawn(220730, 978.2123f, 1131.3398f, 201.39049f, (byte) 4);
						spawn(220732, 976.2579f, 1127.3010f, 201.38469f, (byte) 4);
						spawn(220733, 974.4632f, 1134.0951f, 201.38458f, (byte) 4);
						spawn(220734, 973.1564f, 1130.0658f, 201.38007f, (byte) 4);
						spawn(221018, 969.5107f, 1129.2478f, 201.38405f, (byte) 4); //Frigida Drakan Overseer.
					break;
				}
            }
        }, 180000)); //3 Min.
		pandaemoniumTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
			   /**
				* ■ 3. Unfair Battle:
				* 5 min or 20 min after the start, unfair aerial battle with a Dredgion starts.
				* Use the teleporter at the bottom of the map to join the fight.
				* ■ Non-Combat Method:
				* After arriving at the sight, mouth all the turrets.
				* You can also fly up to attack the Dredgion directly.
				* Destroy 4 Dredgion generators (2 on each side) to win the battle.
				* You must protect the Turret Generator from the Balaur.
				* If the generator is destroyed, nearby turrets will not be usable.
				* During the unfair battle, missiles might fall on the ground.
				* They will cause great damage if not dealt quickly.
				*/
				spawn(210039, 1685.2803f, 1400.4742f, 195.3448f, (byte) 60); //Zallad.
				spawn(703404, 1685.2803f, 1400.4742f, 195.3448f, (byte) 60); //Zallad [Flag].
            }
        }, 300000)); //5 Min.
		pandaemoniumTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
			   /**
				* ■ 4. Dredgion Infiltration:
				* 5 min or 20 min after the start, a Dredgion will appear over the city.
				* A Shugo that can take you to the ship will appear near the "Temple of Gold".
				* Buy a ticket before proceeding.
				* - The Shugo is directly connected to the Dredgion.
				* - Once on the Shugo Ship, you can purchase 2 items that will help you infiltrate the Dredgion.
				* ■ Dredgion Infiltration:
				* - Inside the Dredgion, your objective is to kill the captain.
				* Neutralize the cannons next to the boss before attacking.
				* - Destroying Surkanas will disable each cannon for a while, surkanas will reappear after a minute.
				* - The Observer is to be feared.
				* Use bombs or hiding shield to deal with it.
				* ■ TIP1: Divide your force into 3 groups.
				* (Ex: 2 groups for the Captain & 2 group for Surkanas)
				* ■ TIP2: Destroying both Surkanas at the same time will make it easier to kill the captain.
				*/
				spawn(834267, 831.24658f, 2323.5696f, 96.926010f, (byte) 0, 86); //Dredgion Fissure.
				spawn(834268, 1360.9570f, 1435.4115f, 209.09084f, (byte) 49); //Terunerk.
				spawn(834269, 830.87830f, 2343.7078f, 93.522740f, (byte) 26); //Harunerk.
				spawn(834270, 1363.3180f, 1439.0773f, 209.09084f, (byte) 50); //Hemerinerk.
				spawn(703402, 1363.3180f, 1439.0773f, 209.09084f, (byte) 50); //Hemerinerk [Flag].
            }
        }, 900000)); //15 Min.
		pandaemoniumTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
			   /**
				* ■ 5. Turret Battle:
				* 20 min after the start, Turret Battle will begin and 2 Transport Tanks will appear in the Temple of Artisans.
				* Talk to a Shugo behind each Tank to make them start moving to each Turret.
				* ■ How to Proceed:
				* 1. Escorting Transport Tanks:
				* - First of all, you need to take care of Balaurs that are currently attacking the Turrets.
				* - Talk to a Shugo behind each tank to make them start moving to the area where the Defense Turrets are.
				* - On their way, the tank will be attacked multiple times by the Balaur. Protect them at all cost.
				* - Once the tank is destroyed it will re-appear in the Temple of Artisans.
				* ■ 2. Ammo:
				* - Each time you click on the Tank, you will receive one Energy Source.
				* - On each side of the Turret, there is a generator.
				* It can be fueled with the Energy Source you have just obtained.
				* - When both generators have been charged enough times, the Turret will perform a powerful attack on the Dredgion.
				* - The Turret must be activated twice to destroy the Dredgion.
				* - The Balaur will continue to attack the Turret and the Generators.
				* - When a Generator is destroyed, it will re-appear after a while completely empty.
				* ■ 3. Turret Cooling:
				* - Each turret can not be used for a while after firing a shot.
				* - A Réfrigérant can be purchased from a Shugo that appears near the Defense Turret.
				* - A Réfrigérant can be used to shorten the overheating time. 
				*/
				//Resources available at the defense turret. Escort the transport there to collect them.
				sendMsgByRace(1403708, Race.PC_ALL, 0);
				spawn(834255, 1211.1102f, 1502.938f, 213.83618f, (byte) 6); //Koirinerk.
				spawn(834256, 1219.054f, 1505.9453f, 213.83618f, (byte) 67); //Soirunerk.
				spawn(703411, 1219.054f, 1505.9453f, 213.83618f, (byte) 67); //Pandaemonium Tank [Flag].
				spawn(834514, 971.25745f, 1512.7778f, 238.62827f, (byte) 0, 2478); //Turret Core 1.
				spawn(834515, 1021.9983f, 1563.4001f, 238.72847f, (byte) 0, 2479); //Turret Core 2.
				spawn(834516, 971.10767f, 1147.5275f, 201.09975f, (byte) 0, 2476); //Turret Core 3.
				spawn(834517, 978.90906f, 1113.4866f, 201.09975f, (byte) 0, 2477); //Turret Core 4.
            }
        }, 1200000)); //20 Min.
		pandaemoniumTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
			   /**
				* ■ 5. Commander Zedas:
				* After 30min, Commander Zedas will appear in the "Pandaemonium Plaza"
				* The final rank is calculated when Commander Zedas is dead or 5 min have passed.
				*/
				deleteNpc(221525); //Hezolf.
				deleteNpc(221526); //Angulof.
				deleteNpc(221527); //Agehia.
				//The Assault Leader of the Frigida Legion has appeared.
				sendMsgByRace(1403706, Race.PC_ALL, 0);
				//Commander Zedas of the Frigida Legion has appeared.
				sendMsgByRace(1403707, Race.PC_ALL, 52000);
				//Commander Zedas [Flag].
				spawn(703405, 1275.5393f, 1357.4648f, 204.47417f, (byte) 30, 0);
				//Balaur Group Middle
				sp(220706, 1275.6676f, 1339.5999f, 204.42003f, (byte) 90, 0);
				sp(220709, 1278.507f, 1341.325f, 204.42003f, (byte) 90, 2000);
				sp(220710, 1275.5905f, 1343.0381f, 204.42003f, (byte) 90, 4000);
				sp(220708, 1273.281f, 1341.2726f, 204.41998f, (byte) 90, 6000);
				//Balaur Group Right 2
				sp(220706, 1291.0901f, 1348.3013f, 203.86296f, (byte) 110, 8000);
				sp(220708, 1288.228f, 1346.8536f, 204.30399f, (byte) 110, 10000);
				sp(220710, 1288.1512f, 1349.9318f, 203.86296f, (byte) 110, 12000);
				sp(220709, 1290.8704f, 1351.6138f, 203.86296f, (byte) 110, 14000);
				//Balaur Group Right 1
				sp(220706, 1291.787f, 1365.9916f, 203.86296f, (byte) 10, 16000);
				sp(220708, 1291.534f, 1362.9457f, 203.86296f, (byte) 10, 18000);
				sp(220710, 1288.7963f, 1364.3687f, 203.86296f, (byte) 10, 20000);
				sp(220709, 1288.6088f, 1367.7217f, 203.86296f, (byte) 10, 22000);
				//Balaur Group Center
				sp(220706, 1276.1057f, 1375.533f, 203.86296f, (byte) 30, 24000);
				sp(220708, 1278.5265f, 1373.7854f, 203.86296f, (byte) 30, 26000);
				sp(220710, 1275.9517f, 1372.2517f, 203.86296f, (byte) 30, 28000);
				sp(220709, 1273.2019f, 1373.745f, 203.86296f, (byte) 30, 30000);
				//Balaur Group Left 1
				sp(220706, 1260.6913f, 1366.4132f, 203.86296f, (byte) 50, 32000);
				sp(220708, 1263.3645f, 1367.785f, 203.86296f, (byte) 50, 34000);
				sp(220710, 1263.3696f, 1364.8147f, 203.86296f, (byte) 50, 36000);
				sp(220709, 1260.6237f, 1363.0138f, 203.86296f, (byte) 50, 38000);
				//Balaur Group Left 2
				sp(220706, 1260.0386f, 1348.7373f, 203.86296f, (byte) 70, 40000);
				sp(220708, 1259.9845f, 1351.9194f, 203.86296f, (byte) 70, 42000);
				sp(220710, 1262.6718f, 1350.2765f, 203.86296f, (byte) 70, 44000);
				sp(220709, 1262.7139f, 1347.25f, 203.86296f, (byte) 70, 46000);
				//Assassin Center.
				sp(220707, 1301.7385f, 1357.1246f, 204.42256f, (byte) 9, 48000);
                sp(220707, 1249.749f, 1357.608f, 204.4204f, (byte) 65, 50000);
				sp(220705, 1275.5393f, 1357.4648f, 204.47417f, (byte) 30, 52000); //Commander Zedas.
            }
        }, 1800000)); //30 Min.
		pandaemoniumTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
			   /**
				* ■ 6. Final Dredgion Battle:
				* After 35min, a windstream will appear near the "Pandaemonium Plaza"
				* Use the windstream to participate in the final battle.
				* ■ How to Progress:
				* - During the final Dredgion battle, you can fly freely.
				* - If you destroy all Power Generators within 10min, you will destroy the last remaining Dredgion.
				* - Each Dredgion Power Generator is protected by a shield.
				* - It can be deactivated by destroying nearby controllers.
				*/
				//Tsubesda has retreated. Use the wind road to get to the airship and destroy the Dredgion!
				sendMsgByRace(1404024, Race.PC_ALL, 0);
				//Dredgion sighted!
				sendMsgByRace(1403711, Race.PC_ALL, 5000);
				//Airship prepared to intercept the Dredgion.
				sendMsgByRace(1403712, Race.PC_ALL, 10000);
				//Windpath To Dredgion Roof.
				spawn(220974, 1275.5642f, 1408.0000f, 208.47737f, (byte) 0);
				spawn(220975, 1328.5240f, 1360.0000f, 208.47737f, (byte) 0);
				spawn(220976, 1306.0000f, 1319.3477f, 204.75140f, (byte) 0);
				spawn(220977, 1223.5588f, 1358.0000f, 208.47737f, (byte) 0);
				spawn(220981, 1245.9900f, 1319.3500f, 204.75000f, (byte) 0);
            }
        }, 2100000)); //35 Min.
		pandaemoniumTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				instance.doOnAllPlayers(new Visitor<Player>() {
				    @Override
				    public void visit(Player player) {
					    stopInstance(player);
				    }
			    });
            }
        }, 2700000)); //45 Min.
    }
	
	@Override
	public void onEnterInstance(final Player player) {
		if (!instanceReward.containPlayer(player.getObjectId())) {
			addPlayerReward(player);
		}
		PandaemoniumBattlefieldPlayerReward playerReward = getPlayerReward(player.getObjectId());
		if (playerReward.isRewarded()) {
			doReward(player);
		}
		startPrepareTimer();
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
		if (!timerPrepare.isDone()) {
			timerPrepare.cancel(false);
		}
		startTime = System.currentTimeMillis();
		instanceReward.setInstanceScoreType(InstanceScoreType.START_PROGRESS);
		sendPacket(0, 0);
	}
	
	protected void stopInstance(Player player) {
        stopInstanceTask();
		instanceReward.setRank(6);
		instanceReward.setRank(checkRank(instanceReward.getPoints()));
		instanceReward.setInstanceScoreType(InstanceScoreType.END_PROGRESS);
		doReward(player);
		sendMsg("[SUCCES]: You have finished <Dredgion Defense: Pandaemonium>");
		sendPacket(0, 0);
	}
	
   /**
	* Rewards:
    * ■ S: 164,157 Pts
	* Premium Frigida Legion Loot Box (3 Generators destroyed)
	* Major Frigida Legion Loot Box (2 Generators destroyed)
	* ■ A: 139,674 Pts
	* Major Frigida Legion Supply Box
	* Greater Frigida Legion Loot Box
	* ■ B: 61,208 Pts
	* Greater Frigida Legion Supply Box
	* Lesser Frigida Legion Loot Box
	* ■ C: 39,735 Pts
	* Lesser Frigida Legion Supply Box
	* Minor Frigida Legion Loot Box
	* ■ D: 20,000 Pts
	* Minor Frigida Legion Supply Box
	*/
	@Override
	public void doReward(Player player) {
		if (PlayerActions.isAlreadyDead(player)) {
			PlayerReviveService.duelRevive(player);
		}
		PandaemoniumBattlefieldPlayerReward playerReward = getPlayerReward(player.getObjectId());
		if (!playerReward.isRewarded()) {
			playerReward.setRewarded();
			Npc module = instance.getNpc(220866);
			int pandaeRank = instanceReward.getRank();
			switch (pandaeRank) {
				case 1: //Rank S
					//Premium Frigida Legion Supply Box.
					playerReward.setPremiumFrigidaLegionSupplyBox(1);
					ItemService.addItem(player, 188055689, 1);
					//Additional Compensation for the final attack on the Dredgion.
					if (isDead(module)) {
					    //Premium Frigida Legion Loot Box.
					    playerReward.setPremiumFrigidaLegionLootBox(3);
					    ItemService.addItem(player, 188055699, 3);
					    //Major Frigida Legion Loot Box.
					    playerReward.setMajorFrigidaLegionLootBox(2);
					    ItemService.addItem(player, 188055700, 2);
					}
				break;
				case 2: //Rank A
				    //Major Frigida Legion Supply Box.
					playerReward.setMajorFrigidaLegionSupplyBox(1);
					ItemService.addItem(player, 188055690, 1);
					//Additional Compensation for the final attack on the Dredgion.
					if (isDead(module)) {
					    //Greater Frigida Legion Loot Box.
					    playerReward.setGreaterFrigidaLegionLootBox(1);
					    ItemService.addItem(player, 188055701, 1);
					}
				break;
				case 3: //Rank B
				    //Greater Frigida Legion Supply Box.
					playerReward.setGreaterFrigidaLegionSupplyBox(1);
					ItemService.addItem(player, 188055691, 1);
					//Additional Compensation for the final attack on the Dredgion.
					if (isDead(module)) {
					    //Lesser Frigida Legion Loot Box.
						playerReward.setLesserFrigidaLegionLootBox(1);
						ItemService.addItem(player, 188055702, 1);
					}
				break;
				case 4: //Rank C
				    //Lesser Frigida Legion Supply Box.
					playerReward.setLesserFrigidaLegionSupplyBox(1);
					ItemService.addItem(player, 188055692, 1);
					//Additional Compensation for the final attack on the Dredgion.
					if (isDead(module)) {
					    //Minor Frigida Legion Loot Box.
						playerReward.setMinorFrigidaLegionLootBox(1);
						ItemService.addItem(player, 188055703, 1);
					}
				break;
				case 5: //Rank D
				    //Minor Frigida Legion Supply Box.
					playerReward.setMinorFrigidaLegionSupplyBox(1);
					ItemService.addItem(player, 188055693, 1);
					//Additional Compensation for the final attack on the Dredgion.
					if (isDead(module)) {
					    //Minor Frigida Legion Loot Box.
						playerReward.setMinorFrigidaLegionLootBox(1);
						ItemService.addItem(player, 188055703, 1);
					}
				break;
			}
		}
		for (Npc npc: instance.getNpcs()) {
			npc.getController().onDelete();
		}
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				if (!isInstanceDestroyed) {
					for (Player player : instance.getPlayersInside()) {
						onExitInstance(player);
					}
					AutoGroupService.getInstance().unRegisterInstance(instanceId);
				}
			}
		}, 10000);
	}
	
	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
		instanceReward = new PandaemoniumBattlefieldReward(mapId, instanceId, instance);
		instanceReward.setInstanceScoreType(InstanceScoreType.PREPARING);
		startInstanceTask();
		Npc npc = instance.getNpc(220866); //Dredgion Main Module.
		if (npc != null) {
			//Dredgion’s Power Core Barrier.
			SkillEngine.getInstance().getSkill(npc, 18298, 60, npc).useNoAnimationSkill();
		}
	}
	
	@Override
	public void onInstanceDestroy() {
		if (timerInstance != null) {
			timerInstance.cancel(false);
		} if (timerPrepare != null) {
			timerPrepare.cancel(false);
		}
		stopInstanceTask();
		isInstanceDestroyed = true;
		instanceReward.clear();
		movies.clear();
	}
	
	private void stopInstanceTask() {
        for (FastList.Node<Future<?>> n = pandaemoniumTask.head(), end = pandaemoniumTask.tail(); (n = n.getNext()) != end; ) {
            if (n.getValue() != null) {
                n.getValue().cancel(true);
            }
        }
    }
	
	protected void sp(final int npcId, final float x, final float y, final float z, final byte h, final int time) {
        sp(npcId, x, y, z, h, 0, time, 0, null);
    }
	
    protected void sp(final int npcId, final float x, final float y, final float z, final byte h, final int time, final int msg, final Race race) {
        sp(npcId, x, y, z, h, 0, time, msg, race);
    }
	
    protected void sp(final int npcId, final float x, final float y, final float z, final byte h, final int entityId, final int time, final int msg, final Race race) {
        pandaemoniumTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
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
        pandaemoniumTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
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
	
	protected void despawnNpc(Npc npc) {
        if (npc != null) {
            npc.getController().onDelete();
        }
    }
	
	private void deleteNpc(int npcId) {
		if (getNpc(npcId) != null) {
			getNpc(npcId).getController().onDelete();
		}
	}
	
	protected void killNpc(List<Npc> npcs) {
        for (Npc npc: npcs) {
            npc.getController().die();
        }
    }
	
	protected List<Npc> getNpcs(int npcId) {
		if (!isInstanceDestroyed) {
			return instance.getNpcs(npcId);
		}
		return null;
	}
	
	private boolean isDead(Npc npc) {
		return (npc == null || npc.getLifeStats().isAlreadyDead());
	}
	
	@Override
	public void onPlayerLogOut(Player player) {
		removeEffects(player);
		TeleportService2.moveToInstanceExit(player, mapId, player.getRace());
	}
	
	@Override
    public void onExitInstance(Player player) {
		removeEffects(player);
        TeleportService2.moveToInstanceExit(player, mapId, player.getRace());
    }
	
	@Override
	public void onLeaveInstance(Player player) {
		removeEffects(player);
		//"Player Name" has left the battle.
		PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400255, player.getName()));
	}
	
	private void sendMovie(Player player, int movie) {
		if (!movies.contains(movie)) {
			movies.add(movie);
			PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, movie));
		}
	}
	
	private void removeEffects(Player player) {
		PlayerEffectController effectController = player.getEffectController();
		effectController.removeEffect(18290); //포탑 탑승.
		effectController.removeEffect(18300); //포탑 탑승.
	}
	
	private void sendMsg(final String str) {
		instance.doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				PacketSendUtility.sendWhiteMessageOnCenter(player, str);
			}
		});
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
	
	private void magicWardEnergy() {
		for (Player p: instance.getPlayersInside()) {
			SkillTemplate st =  DataManager.SKILL_DATA.getSkillTemplate(18309); //Magic Ward Energy.
			Effect e = new Effect(p, p, st, 1, st.getEffectsDuration(9));
			e.initialize();
			e.applyEffect();
		}
	}
	
	@Override
	public boolean onDie(final Player player, Creature lastAttacker) {
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);
		PacketSendUtility.sendPacket(player, new SM_DIE(player.haveSelfRezEffect(), player.haveSelfRezItem(), 0, 8));
		return true;
	}
}
