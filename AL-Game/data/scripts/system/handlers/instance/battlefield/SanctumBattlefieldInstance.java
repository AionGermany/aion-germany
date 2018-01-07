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
import com.aionemu.gameserver.model.instance.instancereward.SanctumBattlefieldReward;
import com.aionemu.gameserver.model.instance.playerreward.SanctumBattlefieldPlayerReward;
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
@InstanceID(302200000)
public class SanctumBattlefieldInstance extends GeneralInstanceHandler {

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
	private SanctumBattlefieldReward instanceReward;
	private List<Integer> movies = new ArrayList<Integer>();
	private final FastList<Future<?>> sanctumTask = FastList.newInstance();
	
	protected SanctumBattlefieldPlayerReward getPlayerReward(Integer object) {
		return (SanctumBattlefieldPlayerReward) instanceReward.getPlayerReward(object);
	}
	
	protected void addPlayerReward(Player player) {
		instanceReward.addPlayerReward(new SanctumBattlefieldPlayerReward(player.getObjectId()));
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
						spawn(220730, 1865.2926f, 1611.8842f, 590.01190f, (byte) 106);
						spawn(220732, 1860.5549f, 1616.6385f, 590.01460f, (byte) 105);
						spawn(220733, 1860.5901f, 1611.9545f, 590.01215f, (byte) 105);
						spawn(220734, 1865.2938f, 1616.5138f, 590.01440f, (byte) 105);
						spawn(220729, 1855.7964f, 1611.9655f, 590.01227f, (byte) 116);
					}
				}, 180000);
			break;
			case 221015: //Frigida Drakan Overseer.
			    points = 3000;
				despawnNpc(npc);
			    ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						spawn(220730, 1861.3127f, 1410.2605f, 590.0112f, (byte) 75);
						spawn(220732, 1866.4400f, 1415.5265f, 590.0084f, (byte) 75);
						spawn(220733, 1861.2185f, 1415.2839f, 590.0084f, (byte) 75);
						spawn(220734, 1866.4982f, 1410.2660f, 590.0094f, (byte) 75);
						spawn(221015, 1866.4473f, 1420.8293f, 590.0084f, (byte) 90);
					}
				}, 180000);
			break;
			case 221016: //Frigida Drakan Overseer.
			    points = 3000;
				despawnNpc(npc);
			    ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						spawn(220730, 1568.4277f, 1480.0232f, 573.01764f, (byte) 45);
						spawn(220732, 1572.3428f, 1476.2856f, 572.87195f, (byte) 45);
						spawn(220733, 1572.5776f, 1480.6360f, 573.01373f, (byte) 45);
						spawn(220734, 1567.8876f, 1475.9675f, 572.87195f, (byte) 44);
						spawn(221016, 1575.2587f, 1473.4773f, 572.87195f, (byte) 44);
					}
				}, 180000);
			break;
			case 221017: //Frigida Drakan Overseer.
			    points = 3000;
				despawnNpc(npc);
			    ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						spawn(220730, 1572.8469f, 1541.8624f, 573.02800f, (byte) 76);
						spawn(220732, 1576.4583f, 1546.0983f, 572.87146f, (byte) 76);
						spawn(220733, 1571.3431f, 1546.4694f, 572.87134f, (byte) 76);
						spawn(220734, 1577.1816f, 1541.9384f, 572.87250f, (byte) 76);
						spawn(221017, 1578.4465f, 1548.3069f, 572.87160f, (byte) 76);
					}
				}, 180000);
			break;
			case 221018: //Frigida Drakan Overseer.
			    points = 3000;
				despawnNpc(npc);
			    ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						spawn(220730, 1441.1407f, 1511.2439f, 573.16920f, (byte) 0);
						spawn(220731, 1444.9304f, 1524.1127f, 573.07190f, (byte) 111);
						spawn(220731, 1444.8011f, 1499.0365f, 573.07190f, (byte) 7);
						spawn(220732, 1434.5879f, 1511.1715f, 573.33260f, (byte) 0);
						spawn(220733, 1436.9176f, 1507.0344f, 573.26605f, (byte) 0);
						spawn(220734, 1436.8138f, 1515.1859f, 573.27026f, (byte) 0);
						spawn(221018, 1429.9945f, 1511.1842f, 573.33000f, (byte) 1);
					}
				}, 180000);
			break;
			case 220716: //Frigida Drakan Commander.
			    points = 3334;
				despawnNpc(npc);
			    sp(220915, 1411.3159f, 1671.2926f, 572.88416f, (byte) 105, 0); //Jucleas.
				sp(220916, 1418.0f, 1670.0f, 572.88416f, (byte) 105, 0); //Sibylla.
				sp(220917, 1412.0f, 1664.0f, 572.88416f, (byte) 105, 0); //Aithra.
				sp(220918, 1422.0f, 1667.0f, 572.88416f, (byte) 105, 0); //Macus.
				sp(220919, 1414.8833f, 1661.1019f, 572.88416f, (byte) 105, 0); //Eumelos.
				sp(220920, 1427.3644f, 1660.996f, 572.88416f, (byte) 27, 0); //Brynner.
				sp(220921, 1411.9657f, 1652.8064f, 573.23083f, (byte) 3, 0); //Mayu.
			break;
			case 220717: //Frigida Drakan Commander.
			    points = 3334;
				despawnNpc(npc);
				sp(220922, 1351.9054f, 1388.6754f, 572.99274f, (byte) 9, 0); //Fasimedes.
				sp(220923, 1352.814f, 1394.9077f, 573.20856f, (byte) 9, 0); //Jupion.
				sp(220924, 1357.7458f, 1385.9131f, 573.20856f, (byte) 9, 0); //Likesan.
				sp(220925, 1354.6298f, 1400.8014f, 573.38635f, (byte) 8, 0); //Jumentis.
				sp(220926, 1362.8323f, 1382.5488f, 573.38635f, (byte) 7, 0); //Charna.
				sp(220927, 1359.7947f, 1395.4246f, 572.99274f, (byte) 8, 0); //Thrasymedes.
				sp(220928, 1362.214f, 1390.118f, 573.0446f, (byte) 9, 0); //Oakley.
			break;
			case 220718: //Frigida Drakan Commander.
			    points = 3334;
				despawnNpc(npc);
				sp(220929, 1848.4662f, 1511.1677f, 590.06964f, (byte) 0, 0); //Lavirintos.
				sp(220930, 1853.6035f, 1519.6f, 590.00854f, (byte) 0, 0); //Boreas.
				sp(220931, 1852.1235f, 1516.9996f, 591.55231f, (byte) 0, 0); //Miragent Guardian.
				sp(220931, 1851.8687f, 1506.1539f, 591.55231f, (byte) 0, 0); //Miragent Guardian.
				sp(220932, 1853.995f, 1502.2582f, 590.00854f, (byte) 0, 0); //Dion.
				sp(220933, 1854.9122f, 1513.9397f, 590.0696f, (byte) 117, 0); //Bellia.
				sp(220934, 1854.8523f, 1508.0864f, 590.0696f, (byte) 5, 0); //Hygea.
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
				spawn(834857, npc.getX(), npc.getY(), npc.getZ(), npc.getHeading()); //Emergency Exit.
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
					sendMovie(player, 949);
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
			case 220915: //Jucleas.
			    despawnNpc(npc);
				deleteNpc(220916); //Sibylla.
				deleteNpc(220917); //Aithra.
				deleteNpc(220918); //Macus.
				deleteNpc(220919); //Eumelos.
				deleteNpc(220920); //Brynner.
				deleteNpc(220921); //Mayu.
				deleteNpc(220716); //Frigida Drakan Commander.
				instanceReward.addPoints(-3334);
				//The Lyceum in Sanctum has been destroyed by the Frigida Fregida Legion.
				sendMsgByRace(1403780, Race.PC_ALL, 0);
				ThreadPoolManager.getInstance().schedule(new Runnable() {
				    @Override
				    public void run() {
						spawn(220915, 1411.3159f, 1671.2926f, 572.88416f, (byte) 105); //Jucleas.
						spawn(220916, 1418.0f, 1670.0f, 572.88416f, (byte) 105); //Sibylla.
						spawn(220917, 1412.0f, 1664.0f, 572.88416f, (byte) 105); //Aithra.
						spawn(220918, 1422.0f, 1667.0f, 572.88416f, (byte) 105); //Macus.
						spawn(220919, 1414.8833f, 1661.1019f, 572.88416f, (byte) 105); //Eumelos.
						spawn(220920, 1427.3644f, 1660.996f, 572.88416f, (byte) 27); //Brynner.
						spawn(220921, 1411.9657f, 1652.8064f, 573.23083f, (byte) 3); //Mayu.
						spawn(220716, 1432.0885f, 1649.6793f, 573.19714f, (byte) 104); //Frigida Drakan Commander.
				    }
			    }, 15000);
			break;
			case 220922: //Fasimedes.
			    despawnNpc(npc);
				deleteNpc(220923); //Jupion.
				deleteNpc(220924); //Likesan.
				deleteNpc(220925); //Jumentis.
				deleteNpc(220926); //Charna.
				deleteNpc(220927); //Thrasymedes.
				deleteNpc(220928); //Oakley.
				deleteNpc(220717); //Frigida Drakan Commander.
				instanceReward.addPoints(-3334);
				//The Hall of Prosperity in Sanctum has been destroyed by the Frigida Fregida Legion.
				sendMsgByRace(1403781, Race.PC_ALL, 0);
				ThreadPoolManager.getInstance().schedule(new Runnable() {
				    @Override
				    public void run() {
						spawn(220922, 1351.9054f, 1388.6754f, 572.99274f, (byte) 9); //Fasimedes.
						spawn(220923, 1352.814f, 1394.9077f, 573.20856f, (byte) 9); //Jupion.
						spawn(220924, 1357.7458f, 1385.9131f, 573.20856f, (byte) 9); //Likesan.
						spawn(220925, 1354.6298f, 1400.8014f, 573.38635f, (byte) 8); //Jumentis.
						spawn(220926, 1362.8323f, 1382.5488f, 573.38635f, (byte) 7); //Charna.
						spawn(220927, 1359.7947f, 1395.4246f, 572.99274f, (byte) 8); //Thrasymedes.
						spawn(220928, 1362.214f, 1390.118f, 573.0446f, (byte) 9); //Oakley.
						spawn(220717, 1387.8679f, 1405.131f, 573.30664f, (byte) 8); //Frigida Drakan Commander.
				    }
			    }, 15000);
			break;
			case 220929: //Lavirintos.
			    despawnNpc(npc);
				deleteNpc(220930); //Boreas.
				deleteNpc(220931); //Miragent Guardian.
				deleteNpc(220931); //Miragent Guardian.
				deleteNpc(220932); //Dion.
				deleteNpc(220933); //Bellia.
				deleteNpc(220934); //Hygea.
				deleteNpc(220718); //Frigida Drakan Commander.
				instanceReward.addPoints(-3334);
				//The Protector's Hall in Sanctum has been destroyed by the Frigida Fregida Legion.
				sendMsgByRace(1403782, Race.PC_ALL, 0);
				ThreadPoolManager.getInstance().schedule(new Runnable() {
				    @Override
				    public void run() {
						spawn(220929, 1848.4662f, 1511.1677f, 590.06964f, (byte) 0); //Lavirintos.
						spawn(220930, 1853.6035f, 1519.6f, 590.00854f, (byte) 0); //Boreas.
						spawn(220931, 1852.1235f, 1516.9996f, 591.55231f, (byte) 0); //Miragent Guardian.
						spawn(220931, 1851.8687f, 1506.1539f, 591.55231f, (byte) 0); //Miragent Guardian.
						spawn(220932, 1853.995f, 1502.2582f, 590.00854f, (byte) 0); //Dion.
						spawn(220933, 1854.9122f, 1513.9397f, 590.0696f, (byte) 117); //Bellia.
						spawn(220934, 1854.8523f, 1508.0864f, 590.0696f, (byte) 5); //Hygea.
						spawn(220718, 1865.9791f, 1510.8478f, 590.73645f, (byte) 106); //Frigida Drakan Commander.
				    }
			    }, 15000);
			break;
		}
	}
	
	@Override
	public void handleUseItemFinish(Player player, Npc npc) {
		switch (npc.getNpcId()) {
			case 220778: //Boardable Sanctum Defense Turret.
				despawnNpc(npc);
				SkillEngine.getInstance().getSkill(npc, 18290, 1, player).useNoAnimationSkill(); //Use Guard Tower.
			break;
			case 220782: //Turret Generator.
				sp(221022, 1794.3324f, 2132.3911f, 545.47992f, (byte) 0, 624, 2000, 0, null);
				sp(220778, 1845.6101f, 2085.2124f, 547.3745f, (byte) 12, 2000);
				sp(220778, 1828.2310f, 2103.7566f, 541.62720f, (byte) 11, 4000);
				sp(220778, 1819.8743f, 2115.7866f, 541.62720f, (byte) 12, 6000);
				sp(220778, 1803.7681f, 2127.4866f, 545.58140f, (byte) 92, 8000);
			break;
			case 221019: //Turret Generator.
				sp(221023, 1838.0583f, 2079.9470f, 546.96509f, (byte) 0, 623, 2000, 0, null);
				sp(220778, 1816.7772f, 2064.3667f, 547.37450f, (byte) 72, 2000);
				sp(220778, 1804.5375f, 2086.6619f, 541.62720f, (byte) 72, 4000);
				sp(220778, 1795.7533f, 2098.5146f, 541.62720f, (byte) 73, 6000);
				sp(220778, 1789.2501f, 2117.1746f, 545.59220f, (byte) 113, 8000);
			break;
			case 221020: //Turret Generator.
				sp(221024, 1904.8365f, 2196.9631f, 551.16876f, (byte) 0, 566, 2000, 0, null);
				sp(220778, 1954.1013f, 2145.0435f, 553.06335f, (byte) 8, 2000);
                sp(220778, 1940.1925f, 2166.4993f, 547.31604f, (byte) 7, 4000);
				sp(220778, 1933.7041f, 2179.7505f, 547.31604f, (byte) 7, 6000);
				sp(220778, 1920.2927f, 2194.3720f, 551.27026f, (byte) 87, 8000);
			break;
			case 221021: //Turret Generator.
				sp(221025, 1930.5814f, 2133.6787f, 552.65393f, (byte) 0, 537, 2000, 0, null);
				sp(220778, 1922.1433f, 2129.7410f, 553.06335f, (byte) 69, 2000);
				sp(220778, 1914.0497f, 2153.7664f, 547.31604f, (byte) 68, 4000);
				sp(220778, 1907.5063f, 2167.0344f, 547.31604f, (byte) 68, 6000);
				sp(220778, 1904.2737f, 2186.3542f, 551.28107f, (byte) 110, 8000);
			break;
			case 834317: //Dredgion Fissure.
			    if (player.getInventory().decreaseByItemId(185000282, 1)) {
				    invadeIDLC1Dredgion(player, 1496.732f, 1557.6948f, 2032.4938f, (byte) 30);
					//The shock cannon at the Captain’s Cabin has been activated!
					sendMsgByRace(1403954, Race.PC_ALL, 10000);
				} else {
					//Wind Gap Pass required.
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1403685));
				}
			break;
			case 834338: //Prison Camp.
			    if (player.getInventory().decreaseByItemId(185000280, 1)) {
				    despawnNpc(npc);
				} else {
					//Prison Camp Key required.
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1403686));
				}
			break;
			case 220822: //Sanctum Chariot.
			    //Sanctum Defense Turret Energy Source.
				if (player.getInventory().decreaseByItemId(185000286, 1)) {
				    sp(834506, 1366.2909f, 1544.2992f, 569.03461f, (byte) 0, 459, 1000, 0, null);
					sp(834507, 1366.2908f, 1478.9144f, 569.04498f, (byte) 0, 510, 1000, 0, null);
				    sp(834506, 1366.2909f, 1544.2992f, 569.03461f, (byte) 0, 501, 3000, 0, null);
					sp(834507, 1366.2908f, 1478.9144f, 569.04498f, (byte) 0, 514, 3000, 0, null);
				    sp(834506, 1366.2909f, 1544.2992f, 569.03461f, (byte) 0, 502, 5000, 0, null);
					sp(834507, 1366.2908f, 1478.9144f, 569.04498f, (byte) 0, 517, 5000, 0, null);
				    sp(834506, 1366.2909f, 1544.2992f, 569.03461f, (byte) 0, 503, 7000, 0, null);
					sp(834507, 1366.2908f, 1478.9144f, 569.04498f, (byte) 0, 518, 7000, 0, null);
				    sp(834506, 1366.2909f, 1544.2992f, 569.03461f, (byte) 0, 504, 9000, 0, null);
					sp(834507, 1366.2908f, 1478.9144f, 569.04498f, (byte) 0, 531, 9000, 0, null);
				    sp(834506, 1366.2909f, 1544.2992f, 569.03461f, (byte) 0, 533, 11000, 0, null);
					sp(834507, 1366.2908f, 1478.9144f, 569.04498f, (byte) 0, 536, 11000, 0, null);
				    sp(834506, 1366.2909f, 1544.2992f, 569.03461f, (byte) 0, 622, 13000, 0, null);
					sp(834507, 1366.2908f, 1478.9144f, 569.04498f, (byte) 0, 620, 13000, 0, null);
				    sp(834506, 1366.2909f, 1544.2992f, 569.03461f, (byte) 0, 621, 15000, 0, null);
					sp(834507, 1366.2908f, 1478.9144f, 569.04498f, (byte) 0, 619, 15000, 0, null);
					sp(834506, 1377.2725f, 1511.7285f, 569.20038f, (byte) 0, 540, 17000, 0, null);
				    sp(220816, 1377.0789f, 1511.7852f, 569.84143f, (byte) 60, 0, 19000, 0, null); //Sanctum Defense Turret 1.
					sp(220966, 1343.6370f, 1511.7704f, 591.88947f, (byte) 29, 0, 20000, 0, null); //Dreadgion Weapon Main Module 1.
					//Charging complete!
					sendMsgByRace(1403977, Race.PC_ALL, 20000);
				} else {
					//There is no energy source.
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1403694));
				}
			break;
			case 220823: //Sanctum Chariot.
			    //Sanctum Defense Turret Energy Source.
				if (player.getInventory().decreaseByItemId(185000286, 1)) {
				    sp(834504, 1609.6289f, 1383.5377f, 563.48401f, (byte) 0, 444, 1000, 0, null);
					sp(834505, 1544.2443f, 1383.5377f, 563.49438f, (byte) 0, 445, 1000, 0, null);
				    sp(834504, 1609.6289f, 1383.5377f, 563.48401f, (byte) 0, 451, 3000, 0, null);
					sp(834505, 1544.2443f, 1383.5377f, 563.49438f, (byte) 0, 456, 3000, 0, null);
				    sp(834504, 1609.6289f, 1383.5377f, 563.48401f, (byte) 0, 452, 5000, 0, null);
					sp(834505, 1544.2443f, 1383.5377f, 563.49438f, (byte) 0, 455, 5000, 0, null);
				    sp(834504, 1609.6289f, 1383.5377f, 563.48401f, (byte) 0, 453, 7000, 0, null);
					sp(834505, 1544.2443f, 1383.5377f, 563.49438f, (byte) 0, 457, 7000, 0, null);
				    sp(834504, 1609.6289f, 1383.5377f, 563.48401f, (byte) 0, 454, 9000, 0, null);
					sp(834505, 1544.2443f, 1383.5377f, 563.49438f, (byte) 0, 458, 9000, 0, null);
				    sp(834504, 1609.6289f, 1383.5377f, 563.48401f, (byte) 0, 311, 11000, 0, null);
					sp(834505, 1544.2443f, 1383.5377f, 563.49438f, (byte) 0, 443, 11000, 0, null);
				    sp(834504, 1609.6289f, 1383.5377f, 563.48401f, (byte) 0, 585, 13000, 0, null);
					sp(834505, 1544.2443f, 1383.5377f, 563.49438f, (byte) 0, 593, 13000, 0, null);
				    sp(834504, 1609.6289f, 1383.5377f, 563.48401f, (byte) 0, 618, 15000, 0, null);
					sp(834505, 1544.2443f, 1383.5377f, 563.49438f, (byte) 0, 589, 15000, 0, null);
					sp(834504, 1577.0582f, 1372.5562f, 564.69690f, (byte) 0, 307, 17000, 0, null);
					sp(221137, 1576.9885f, 1372.4152f, 565.33795f, (byte) 90, 0, 19000, 0, null); //Sanctum Defense Turret 2.
					sp(220967, 1578.1901f, 1354.3147f, 581.95760f, (byte) 58, 0, 20000, 0, null); //Dreadgion Weapon Main Module 2.
					//Charging complete!
					sendMsgByRace(1403977, Race.PC_ALL, 20000);
				} else {
					//There is no energy source.
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1403694));
				}
			break;
		}
	}
	
	protected void invadeIDLC1Dredgion(Player player, float x, float y, float z, byte h) {
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
		sanctumTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
			   /**
				* ■ 1. Drakan Instructor & Base Defence:
				* Drakan Instructors appear in the "Lyceum, Hall Of Prosperity & Artisans Hall".
				* After killing each Instructor, friendly NPCs will appear to protect that spot.
				* At the 5th, 15th, and 20th minute, large scale battle begins at each base.
				* If a leader of a base dies, Drakan Instructor will re-appear.
				*/
				//The Frigida Fregida Legion is attacking the Lyceum in Sanctum.
				sendMsgByRace(1403777, Race.PC_ALL, 0);
				sp(220716, 1432.0885f, 1649.6793f, 573.19714f, (byte) 104, 0); //Frigida Drakan Commander.
				sp(703406, 1432.0885f, 1649.6793f, 573.19714f, (byte) 104, 0); //Frigida Drakan Commander [Flag].
				//The Frigida Fregida Legion is attacking the Hall of Prosperity in Sanctum.
				sendMsgByRace(1403778, Race.PC_ALL, 10000);
				sp(220717, 1387.8679f, 1405.131f, 573.30664f, (byte) 8, 10000); //Frigida Drakan Commander.
				sp(703406, 1387.8679f, 1405.131f, 573.30664f, (byte) 8, 10000); //Frigida Drakan Commander [Flag].
				//The Frigida Fregida Legion is attacking the Protector's Hall in Sanctum.
				sendMsgByRace(1403779, Race.PC_ALL, 20000);
				sp(220718, 1865.9791f, 1510.8478f, 590.73645f, (byte) 106, 20000); //Frigida Drakan Commander.
				sp(703406, 1865.9791f, 1510.8478f, 590.73645f, (byte) 106, 20000); //Frigida Drakan Commander [Flag].
            }
        }, 60000)); //1 Min.
		sanctumTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
			   /**
				* ■ 2. Drakan Battle Overseer:
				* 3 min after the start, 3 Drakan Battle Overseers will appear in 3/5 random locations.
				* They will re-appear 3 min after being killed in a random position.
				*/
				switch (Rnd.get(1, 5)) {
				    case 1:
						spawn(220730, 1865.2926f, 1611.8842f, 590.01190f, (byte) 106);
						spawn(220732, 1860.5549f, 1616.6385f, 590.01460f, (byte) 105);
						spawn(220733, 1860.5901f, 1611.9545f, 590.01215f, (byte) 105);
						spawn(220734, 1865.2938f, 1616.5138f, 590.01440f, (byte) 105);
						spawn(220729, 1855.7964f, 1611.9655f, 590.01227f, (byte) 116); //Frigida Drakan Overseer.
				    break;
					case 2:
						spawn(220730, 1861.3127f, 1410.2605f, 590.0112f, (byte) 75);
						spawn(220732, 1866.4400f, 1415.5265f, 590.0084f, (byte) 75);
						spawn(220733, 1861.2185f, 1415.2839f, 590.0084f, (byte) 75);
						spawn(220734, 1866.4982f, 1410.2660f, 590.0094f, (byte) 75);
						spawn(221015, 1866.4473f, 1420.8293f, 590.0084f, (byte) 90); //Frigida Drakan Overseer.
				    break;
					case 3:
						spawn(220730, 1568.4277f, 1480.0232f, 573.01764f, (byte) 45);
						spawn(220732, 1572.3428f, 1476.2856f, 572.87195f, (byte) 45);
						spawn(220733, 1572.5776f, 1480.6360f, 573.01373f, (byte) 45);
						spawn(220734, 1567.8876f, 1475.9675f, 572.87195f, (byte) 44);
						spawn(221016, 1575.2587f, 1473.4773f, 572.87195f, (byte) 44); //Frigida Drakan Overseer.
					break;
					case 4:
						spawn(220730, 1572.8469f, 1541.8624f, 573.02800f, (byte) 76);
						spawn(220732, 1576.4583f, 1546.0983f, 572.87146f, (byte) 76);
						spawn(220733, 1571.3431f, 1546.4694f, 572.87134f, (byte) 76);
						spawn(220734, 1577.1816f, 1541.9384f, 572.87250f, (byte) 76);
						spawn(221017, 1578.4465f, 1548.3069f, 572.87160f, (byte) 76); //Frigida Drakan Overseer.
					break;
					case 5:
					    spawn(220730, 1441.1407f, 1511.2439f, 573.16920f, (byte) 0);
						spawn(220731, 1444.9304f, 1524.1127f, 573.07190f, (byte) 111);
						spawn(220731, 1444.8011f, 1499.0365f, 573.07190f, (byte) 7);
						spawn(220732, 1434.5879f, 1511.1715f, 573.33260f, (byte) 0);
						spawn(220733, 1436.9176f, 1507.0344f, 573.26605f, (byte) 0);
						spawn(220734, 1436.8138f, 1515.1859f, 573.27026f, (byte) 0);
						spawn(221018, 1429.9945f, 1511.1842f, 573.33000f, (byte) 1); //Frigida Drakan Overseer.
					break;
				}
            }
        }, 180000)); //3 Min.
		sanctumTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
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
				spawn(210038, 1680.2438f, 1615.4294f, 566.652f, (byte) 61); //Sald.
				spawn(703403, 1680.2438f, 1615.4294f, 566.652f, (byte) 61); //Sald [Flag].
            }
        }, 300000)); //5 Min.
		sanctumTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
			   /**
				* ■ 4. Dredgion Infiltration:
				* 5 min or 20 min after the start, a Dredgion will appear over the city.
				* A Shugo that can take you to the ship will appear near the "Hall Of Prosperity".
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
				spawn(834317, 971.33301f, 756.68567f, 2210.9631f, (byte) 0, 17); //Dredgion Fissure.
				spawn(834318, 1436.2812f, 1460.8816f, 572.87805f, (byte) 1); //Cayrunerk.
				spawn(834319, 952.05225f, 753.11850f, 2207.4846f, (byte) 53); //Yamirunerk.
				spawn(834320, 1436.4160f, 1458.1833f, 572.87780f, (byte) 1); //Idorunerk.
				spawn(703401, 1436.4160f, 1458.1833f, 572.87780f, (byte) 1); //Idorunerk [Flag].
            }
        }, 900000)); //15 Min.
		sanctumTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
			   /**
				* ■ 5. Turret Battle:
				* 20 min after the start, Turret Battle will begin and 2 Transport Tanks will appear in the "Lyceum".
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
				spawn(834305, 1380.8705f, 1689.9844f, 573.12286f, (byte) 0); //Koirinerk.
				spawn(834306, 1382.1947f, 1687.0924f, 573.12286f, (byte) 9); //Soirunerk.
				spawn(703410, 1382.1947f, 1687.0924f, 573.12286f, (byte) 9); //Sanctum Tank [Flag].
				spawn(834504, 1609.6289f, 1383.5377f, 563.48401f, (byte) 0, 542); //Turret Core 1.
				spawn(834505, 1544.2443f, 1383.5377f, 563.49438f, (byte) 0, 541); //Turret Core 2.
				spawn(834506, 1366.2909f, 1544.2992f, 569.03461f, (byte) 0, 236); //Turret Core 3.
				spawn(834507, 1366.2908f, 1478.9144f, 569.04498f, (byte) 0, 234); //Turret Core 4.
            }
        }, 1200000)); //20 Min.
		sanctumTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
			   /**
				* ■ 5. Commander Zedas:
				* After 30min, Commander Zedas will appear in the "Elyos Square"
				* The final rank is calculated when Commander Zedas is dead or 5 min have passed.
				*/
				deleteNpc(221522); //Gerion.
				deleteNpc(221523); //Mysteris.
				deleteNpc(221524); //Eremitia.
				//The Assault Leader of the Frigida Legion has appeared.
				sendMsgByRace(1403706, Race.PC_ALL, 0);
				//Commander Zedas of the Frigida Legion has appeared.
				sendMsgByRace(1403707, Race.PC_ALL, 52000);
				//Commander Zedas [Flag].
				spawn(703405, 1532.3103f, 1511.6292f, 565.8826f, (byte) 61);
				//Balaur Group Middle
				sp(220706, 1576.0831f, 1508.3297f, 572.50555f, (byte) 116, 0);
				sp(220709, 1576.1649f, 1514.678f, 572.50555f, (byte) 3, 2000);
				sp(220710, 1571.3501f, 1508.3702f, 572.50555f, (byte) 119, 4000);
				sp(220708, 1571.3691f, 1514.5658f, 572.50555f, (byte) 3, 6000);
				//Balaur Group Right 2
				sp(220706, 1542.7588f, 1536.1654f, 565.9535f, (byte) 34, 8000);
				sp(220708, 1543.8932f, 1531.1261f, 565.93695f, (byte) 34, 10000);
				sp(220710, 1541.026f, 1533.3767f, 565.93823f, (byte) 34, 12000);
				sp(220709, 1546.6664f, 1534.1787f, 565.9389f, (byte) 34, 14000);
				//Balaur Group Right 1
				sp(220706, 1518.434f, 1536.2955f, 565.87897f, (byte) 39, 16000);
				sp(220708, 1520.7388f, 1532.4022f, 565.9386f, (byte) 39, 18000);
				sp(220710, 1516.8375f, 1532.7157f, 565.8791f, (byte) 39, 20000);
				sp(220709, 1522.0991f, 1535.7375f, 565.8786f, (byte) 39, 22000);
				//Balaur Group Center
				sp(220706, 1506.4653f, 1511.5436f, 565.94354f, (byte) 60, 24000);
				sp(220708, 1512.14f, 1511.6593f, 565.9349f, (byte) 60, 26000);
				sp(220710, 1509.4026f, 1508.2102f, 565.93884f, (byte) 60, 28000);
				sp(220709, 1509.2242f, 1515.0634f, 565.93915f, (byte) 60, 30000);
				//Balaur Group Left 1
				sp(220706, 1518.2384f, 1486.9459f, 565.87897f, (byte) 79, 32000);
				sp(220708, 1520.4506f, 1490.7148f, 565.8788f, (byte) 79, 34000);
				sp(220710, 1522.1997f, 1487.0133f, 565.8786f, (byte) 79, 36000);
				sp(220709, 1516.5223f, 1490.344f, 565.87915f, (byte) 79, 38000);
				//Balaur Group Left 2
				sp(220706, 1542.468f, 1486.7886f, 565.94574f, (byte) 84, 40000);
				sp(220708, 1543.9023f, 1491.8698f, 565.93726f, (byte) 87, 42000);
				sp(220710, 1546.442f, 1488.4353f, 565.9389f, (byte) 84, 44000);
				sp(220709, 1541.1467f, 1489.9169f, 565.938f, (byte) 84, 46000);
				//Assassin Center.
				sp(220707, 1523.4915f, 1498.7505f, 565.91974f, (byte) 60, 48000);
				sp(220707, 1519.6729f, 1520.7358f, 565.9195f, (byte) 61, 50000);
				sp(220705, 1532.3103f, 1511.6292f, 565.8826f, (byte) 61, 52000); //Commander Zedas.
            }
        }, 1800000)); //30 Min.
		sanctumTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
			   /**
				* ■ 6. Final Dredgion Battle:
				* After 35min, a windstream will appear near the "Elyos Square"
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
				spawn(220968, 1519.9136f, 1559.5645f, 573.25140f, (byte) 0);
				spawn(220969, 1570.5824f, 1537.7861f, 573.25537f, (byte) 0);
				spawn(220971, 1520.0000f, 1463.0000f, 573.25000f, (byte) 0);
				spawn(220972, 1455.0000f, 1511.5394f, 573.43011f, (byte) 0);
				spawn(220973, 1569.3729f, 1484.9878f, 573.33221f, (byte) 0);
            }
        }, 2100000)); //35 Min.
		sanctumTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
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
		SanctumBattlefieldPlayerReward playerReward = getPlayerReward(player.getObjectId());
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
		sendMsg("[SUCCES]: You have finished <Dredgion Defense: Sanctum>");
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
		SanctumBattlefieldPlayerReward playerReward = getPlayerReward(player.getObjectId());
		if (!playerReward.isRewarded()) {
			playerReward.setRewarded();
			Npc module = instance.getNpc(220866);
			int sanctumRank = instanceReward.getRank();
			switch (sanctumRank) {
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
		instanceReward = new SanctumBattlefieldReward(mapId, instanceId, instance);
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
        for (FastList.Node<Future<?>> n = sanctumTask.head(), end = sanctumTask.tail(); (n = n.getNext()) != end; ) {
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
        sanctumTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
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
        sanctumTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
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
