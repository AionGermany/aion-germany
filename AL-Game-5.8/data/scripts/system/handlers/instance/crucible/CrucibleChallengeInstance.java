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

import java.util.Set;
import java.util.concurrent.Future;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.drop.DropItem;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.instance.InstanceScoreType;
import com.aionemu.gameserver.model.instance.StageType;
import com.aionemu.gameserver.model.instance.playerreward.CruciblePlayerReward;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INSTANCE_SCORE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INSTANCE_STAGE_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.services.instance.InstanceService;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author xTz
 */
@InstanceID(300320000)
public class CrucibleChallengeInstance extends CrucibleInstance {

	private int stage1Count;
	private Future<?> bonusTimer;
	private int spawnCount;
	private int dieCount;
	private int rewardCount;

	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
		sp(205668, 345.77954f, 1662.6697f, 95.25f, (byte) 0, 0);
		sp(205682, 383.3434f, 1667.2977f, 97.79293f, (byte) 60, 0);
	}

	@Override
	public void onEnterInstance(Player player) {
		super.onEnterInstance(player);
		sendPacket(0, 0);
		sendEventPacket();
	}

	private void sendPacket(final int nameId, final int points) {
		instance.doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				if (player.isOnline()) {
					PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(instanceReward));
					if (nameId != 0) {
						PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400237, new DescriptionId(nameId * 2 + 1), points));
					}
				}
			}
		});
	}

	private void sendEventPacket() {
		instance.doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				if (player.isOnline()) {
					PacketSendUtility.sendPacket(player, new SM_INSTANCE_STAGE_INFO(2, stageType.getId(), stageType.getType()));
				}
			}
		});
	}

	@Override
	public void onDie(final Npc npc) {
		int points = 0;
		switch (npc.getNpcId()) {
			case 217785:
			case 217784:
				points += 120;
				break;
			case 217786:
			case 217787:
				points += 200;
				break;
			case 217797:
			case 217798:
			case 217799:
				points += 1100;
				break;
			case 217788:
			case 217789:
			case 217790:
			case 217791:
			case 217792:
			case 217793:
				points += 1300;
				break;
			case 217845:
				points += 1400;
				break;
			case 217783:
				points += 1600;
				break;
			case 217800:
			case 217801:
				points += 1650;
				break;
			case 217807:
			case 217808:
			case 217809:
			case 217810:
			case 217811:
			case 217812:
			case 217813:
			case 217814:
			case 217815:
			case 217816:
				points += 2500;
				break;
			case 217847:
				points += 4600;
				break;
			case 217795:
			case 217794:
				points += 5000;
				break;
			case 217806:
			case 218562:
			case 218565:
				points += 5800;
				break;
			case 217819:
				points += 7200;
				break;
		}
		if (points != 0) {
			getPlayerReward(instance.getSoloPlayerObj()).addPoints(points);
			sendPacket(npc.getObjectTemplate().getNameId(), points);
		}
		int npcId = 0;
		switch (npc.getNpcId()) {
			case 217785:
			case 217784:
				npcId = npc.getNpcId();
				stage1Count++;
				if (stage1Count == 2) {
					sp(npcId, 342.855f, 1652.6703f, 95.63069f, (byte) 0, 0);
					sp(npcId, 342.83942f, 1673.2863f, 95.66078f, (byte) 0, 0);
				}
				despawnNpc(npc);
				if (getNpcs(npcId).isEmpty()) {
					setEvent(StageType.START_STAGE_1_ROUND_2, 2000);
					sp(217783, 337.82263f, 1662.9073f, 95.27217f, (byte) 0, 4000);
				}
				break;
			case 217783:
				despawnNpc(npc);
				setEvent(StageType.PASS_STAGE_1, 0);
				sp(217758, 347.24026f, 1660.2524f, 95.35922f, (byte) 0, 0);
				break;
			case 217843:
			case 217845:
				despawnNpc(npc);
				sp(217848, 1309.2858f, 1732.6802f, 316.0825f, (byte) 0, 1000);
				sp(217848, 1306.1871f, 1731.7861f, 316.25168f, (byte) 0, 1000);
				sp(217848, 1308.4879f, 1734.4999f, 316f, (byte) 0, 1000);
				sp(217848, 1308.67419f, 1736.2063f, 316f, (byte) 0, 1000);
				sp(217848, 1306.9414f, 1736.5365f, 316f, (byte) 0, 1000);
				sp(217848, 1305.2534f, 1735.1603f, 315.94586f, (byte) 0, 1000);
				setEvent(StageType.START_STAGE_3_ROUND_2, 2000);
				sp(217847, 1307.2786f, 1734.3274f, 316f, (byte) 0, 3000);
				// to do use skill boss
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						despawnNpcs(getNpcs(217848));
					}
				}, 3000);
				break;
			case 217847:
				despawnNpc(npc);
				sp(205676, 1307.6722f, 1732.9865f, 316.07373f, (byte) 6, 0);
				break;
			case 217788:
			case 217789:
			case 217790:
				despawnNpc(npc);
				setEvent(StageType.START_STAGE_4_ROUND_2, 2000);
				sp(217794, 1271f, 791.5752f, 436.63998f, (byte) 0, 6000);
				break;
			case 217791:
			case 217792:
			case 217793:
				despawnNpc(npc);
				setEvent(StageType.START_STAGE_4_ROUND_2, 2000);
				sp(217795, 1258.8425f, 237.91522f, 405.3968f, (byte) 0, 6000);
				break;
			case 217794:
				despawnNpc(npc);
				setEvent(StageType.PASS_STAGE_4, 0);
				sp(217820, 1266.9661f, 791.5348f, 436.64014f, (byte) 0, 4000);
				setEvent(StageType.START_BONUS_STAGE_4, 4000);
				break;
			case 217795:
				despawnNpc(npc);
				setEvent(StageType.PASS_STAGE_4, 0);
				sp(217820, 1251.1598f, 237.97736f, 405.3968f, (byte) 0, 4000);
				setEvent(StageType.START_BONUS_STAGE_4, 4000);
				break;
			case 217786:
			case 217787:
				despawnNpc(npc);
				break;
			case 217807:
			case 217808:
			case 217809:
			case 217810:
			case 217811:
			case 217812:
			case 217813:
			case 217814:
			case 217815:
			case 217816:
				despawnNpc(npc);
				setEvent(StageType.START_STAGE_5_ROUND_2, 2000);
				switch (Rnd.get(1, 2)) {
					case 1:
						npcId = 217806;
						break;
					case 2:
						npcId = 218562;
						break;
					case 3:
						npcId = 218565;
						break;
				}
				sp(npcId, 332.3786f, 349.31204f, 96.090935f, (byte) 0, 4000);
				break;
			case 217806:
			case 218562:
			case 218565:
				despawnNpc(npc);
				setEvent(StageType.PASS_STAGE_5, 0);
				sp(205678, 346.64798f, 349.25586f, 96.090965f, (byte) 0, 0);
				break;
			case 217819:
				setEvent(StageType.PASS_STAGE_6, 0);

				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						Player player = null;
						if (!instance.getPlayersInside().isEmpty()) {
							player = instance.getPlayersInside().get(0);
						}
						despawnNpc(npc);
						if (player != null) {
							QuestState qs = player.getQuestStateList().getQuestState(player.getRace() == Race.ASMODIANS ? 28209 : 18209);
							if (qs != null && qs.getStatus() == QuestStatus.START) {
								if (qs.getQuestVarById(0) == 1 || qs.getQuestVarById(1) == 4) { // 5 x Vanktrist Spacetwine killed
									sp(730459, 1765.7104f, 1281.2388f, 389.11743f, (byte) 0, 2000);
									return;
								}
							}
						}
						sp(205679, 1765.522f, 1282.1051f, 389.11743f, (byte) 0, 2000);
					}
				}, 4000);
				break;
			case 217837:
			case 217838:
				Player player = npc.getAggroList().getMostPlayerDamage();
				if (player != null) {
					if (player.isInsideZone(ZoneName.get("ILLUSION_STADIUM_8_300320000"))) {
						sp(217820, 1251.1598f, 237.97736f, 405.3968f, (byte) 0, 0);
					}
					else if (player.isInsideZone(ZoneName.get("ILLUSION_STADIUM_13_300320000"))) {
						sp(217820, 1266.9661f, 791.5348f, 436.64014f, (byte) 0, 0);
					}
				}
				despawnNpc(npc);
				break;
			case 217800:
			case 217801:
				despawnNpc(npc);
				if (getNpcs(217800).isEmpty() && getNpcs(217801).isEmpty()) {
					startBonusStage2();
				}
				break;
			case 217802:
				despawnNpc(npc);
				despawnNpcs(getNpcs(217803));
				bonusTimer.cancel(false);
				passStage2();
				break;
			case 217797:
			case 217798:
			case 217799:
				despawnNpc(npc);
				if (getNpcs(217797).isEmpty() && getNpcs(217798).isEmpty() && getNpcs(217799).isEmpty()) {
					startBonusStage2();
				}
				break;
			case 217841:
			case 218561:
				despawnNpc(npc);
				switch (npc.getNpcId()) {
					case 218561:
						npcId = 218560;
						break;
					case 217841:
						npcId = 217840;
						break;
				}
				int rnd = Rnd.get(1, 2);
				switch (rnd) {
					case 1:
						sp(npcId, 1299.7399f, 1733.5763f, 316.6515f, (byte) 0, 0);
						break;
					case 2:
						sp(npcId, 1297.627f, 1729.8733f, 316.875f, (byte) 0, 0);
						break;
				}
				despawnNpc(npc);
				break;
			case 217803:
				despawnNpc(npc);
				dieCount++;
				if (dieCount == 5) {
					sendMsg(1401068, 0, 25);
				}
				if (bonusTimer.isCancelled() && getNpcs(217803).isEmpty()) {
					passStage2();
					Npc poppy = getNpc(217802);
					if (poppy != null) {
						sendMsg(1401072, 0, 25);
						sp(218571, poppy.getX(), poppy.getY(), poppy.getZ(), (byte) 0, 0);
						poppy.getController().onDelete();
					}
				}
				break;
			case 217844:
			case 217842:
				final int fnpcId = npc.getNpcId();
				despawnNpc(npc);
				if (getNpcs(fnpcId).size() == 2) {
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							startStage3Round1_1(fnpcId);
						}
					}, 5000);

				}
				break;
			case 218185:
				despawnNpc(getNpc(218190));
				despawnNpc(getNpc(218191));
				sp(730460, 1759.2914f, 1786.37f, 389.11713f, (byte) 96, 0);
				sendMsg(342495, npc.getObjectId(), true, 5);
				break;
		}
	}

	private void startStage3Round1_1(int npcId) {
		int bossId = 0;
		switch (npcId) {
			case 217844:
				bossId = 217845;
				break;
			case 217842:
				bossId = 217843;
				break;
		}
		sp(bossId, 1287.6239f, 1724.2721f, 317.1485f, (byte) 6, 2000);
		despawnNpcs(getNpcs(npcId));
		despawnNpc(getNpc(217840));
		despawnNpc(getNpc(218560));
		despawnNpc(getNpc(218561));
		despawnNpc(getNpc(217841));
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
			ItemService.addItem(player, 186000130, (int) reward);
			playerReward.setInsignia((int) reward);
			instanceReward.setInstanceScoreType(InstanceScoreType.END_PROGRESS);
		}
		PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(instanceReward));
	}

	private void startBonusStage2() {
		setEvent(StageType.PASS_STAGE_2, 0);

		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				setEvent(StageType.START_BONUS_STAGE_2, 1000);
			}
		}, 2000);

		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (!isInstanceDestroyed) {
					sendMsg(1401067, 0, 25);
					SpawnTemplate template = SpawnEngine.addNewSingleTimeSpawn(mapId, 217802, 1780.5665f, 307.40463f, 469.25f, (byte) 0);
					template.setWalkerId("3003200001");
					SpawnEngine.spawnObject(template, instanceId);
				}
			}
		}, 8000);

		bonusTimer = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				spawnCount++;
				spawnBonus2();
				if (spawnCount == 10) {
					bonusTimer.cancel(false);
				}
			}
		}, 12000, 4000);
	}

	private void spawnBonus2() {
		switch (spawnCount) {
			case 1:
				sp(217803, 1789.923f, 310.853f, 469.25f, (byte) 0, 0);
				break;
			case 2:
				sp(217803, 1780.9126f, 315.63382f, 469.25f, (byte) 0, 0);
				break;
			case 3:
				sp(217803, 1784.4752f, 300.1912f, 469.25f, (byte) 0, 0);
				break;
			case 4:
				sp(217803, 1793.2847f, 312.59842f, 469.25f, (byte) 0, 0);
				break;
			case 5:
				sp(217803, 1777.6609f, 316.1443f, 469.25f, (byte) 0, 0);
				break;
			case 6:
				sp(217803, 1776.5625f, 299.30347f, 469.25f, (byte) 0, 0);
				break;
			case 7:
				sp(217803, 1793.6469f, 301.40973f, 469.25f, (byte) 0, 0);
				break;
			case 8:
				sp(217803, 1797.4891f, 310.50418f, 469.25f, (byte) 0, 0);
				break;
			case 9:
				sp(217803, 1782.552f, 319.43973f, 469.25f, (byte) 0, 0);
				break;
			case 10:
				sp(217803, 1776.1577f, 305.80396f, 469.25f, (byte) 0, 0);
				break;
		}
	}

	@Override
	public void onInstanceDestroy() {
		super.onInstanceDestroy();
		if (bonusTimer != null) {
			bonusTimer.cancel(false);
		}
	}

	private void passStage2() {
		sp(205675, 1784.5883f, 306.98645f, 469.25f, (byte) 0, 0);
	}

	@Override
	public boolean onDie(final Player player, Creature lastAttacker) {
		super.onDie(player, lastAttacker);
		int place = 0;
		if (isInZone(ZoneName.get("ILLUSION_STADIUM_11_300320000"), player)) { // stage 1
			place = 1;
		}
		else if (isInZone(ZoneName.get("ILLUSION_STADIUM_15_300320000"), player)) { // stage 2
			place = 2;
		}
		else if (isInZone(ZoneName.get("ILLUSION_STADIUM_12_300320000"), player)) { // stage 3
			place = 3;
		}
		else if (isInZone(ZoneName.get("ILLUSION_STADIUM_8_300320000"), player)) { // stage 4
			place = 4;
		}
		else if (isInZone(ZoneName.get("ILLUSION_STADIUM_13_300320000"), player)) { // stage 4
			place = 5;
		}
		else if (isInZone(ZoneName.get("ILLUSION_STADIUM_9_300320000"), player)) { // stage 5
			place = 6;
		}
		else if (isInZone(ZoneName.get("ILLUSION_STADIUM_14_300320000"), player)) { // stage 6
			place = 7;
		}
		getPlayerReward(player.getObjectId()).setSpawnPosition(place);
		return true;
	}

	@Override
	public boolean onReviveEvent(Player player) {
		super.onReviveEvent(player);
		switch (getPlayerReward(player.getObjectId()).getSpawnPosition()) {
			case 1:
				teleport(player, 380.35417f, 1663.3583f, 97.60156f, (byte) 0);
				break;
			case 2:
				teleport(player, 1819.8119f, 304.92932f, 469.4142f, (byte) 0);
				break;
			case 3:
				teleport(player, 1354.9386f, 1748.1531f, 318.6173f, (byte) 70);
				break;
			case 4:
				teleport(player, 1294.1417f, 234.49684f, 406.0368f, (byte) 0);
				break;
			case 5:
				teleport(player, 1307.3776f, 790.7324f, 437.29678f, (byte) 0);
				break;
			case 6:
				teleport(player, 381.7477f, 346.63913f, 96.74763f, (byte) 0);
				break;
			case 7:
				teleport(player, 1750.2677f, 1253.5453f, 389.11765f, (byte) 10);
				break;
			default:
				InstanceService.destroyInstance(player.getPosition().getWorldMapInstance());
				break;
		}

		if (player.getInventory().getFirstItemByItemId(186000134) == null) {
			doReward(player);
		}
		else {
			player.getInventory().decreaseByItemId(186000134, 1);
		}
		return true;
	}

	@Override
	public void onExitInstance(Player player) {
		InstanceService.destroyInstance(player.getPosition().getWorldMapInstance());
	}

	@Override
	public void onPlayerLogin(Player player) {
		sendPacket(0, 0);
		sendEventPacket();
		if (getPlayerReward(player.getObjectId()).isRewarded()) {
			doReward(player);
		}
	}

	@Override
	public void onStopTraining(Player player) {
		doReward(player);
	}

	@Override
	public void onChangeStage(StageType type) {
		setEvent(type, 2000);
		int npcId = 0, barrelId = 0;
		Player player = null;
		if (!instance.getPlayersInside().isEmpty()) {
			player = instance.getPlayersInside().get(0);
		}
		switch (stageType) {
			case START_STAGE_1_ROUND_1:
				if (player != null) {
					switch (player.getRace()) {
						case ELYOS:
							npcId = 217784;
							break;
						case ASMODIANS:
							npcId = 217785;
							break;
						default:
							break;
					}
					sp(npcId, 334.85098f, 1657.8495f, 95.77262f, (byte) 0, 4000);
					sp(npcId, 334.74506f, 1668.7478f, 95.67427f, (byte) 0, 4000);
					sp(npcId, 350.63846f, 1663.84f, 95.385f, (byte) 0, 4000);
				}
				break;
			case START_STAGE_2_ROUND_1:
				switch (Rnd.get(1, 2)) {
					case 1:
						sp(217800, 1779.9993f, 305.76697f, 469.25f, (byte) 30, 4000);
						sp(217801, 1779.9845f, 308.82013f, 469.25f, (byte) 90, 4000);
						break;
					case 2:
						sp(217797, 1771.5549f, 307.09192f, 469.25f, (byte) 30, 4000);
						sp(217798, 1775.9717f, 317.69516f, 469.25f, (byte) 90, 4000);
						sp(217799, 1775.9646f, 296.3559f, 469.25f, (byte) 90, 4000);
						break;
				}
				break;
			case START_STAGE_3_ROUND_1:
				switch (Rnd.get(1, 2)) {
					case 1:
						sendMsg(1401085, 0, 25);
						npcId = 217844;
						barrelId = 218560;
						break;
					case 2:
						sendMsg(1401084, 0, 25);
						npcId = 217842;
						barrelId = 217840;
						break;
				}
				sp(barrelId, 1302.4689f, 1732.1583f, 316.4486f, (byte) 0, 2000);
				sp(npcId, 1288.6692f, 1730.5212f, 316.85333f, (byte) 0, 4000);
				sp(npcId, 1296.7896f, 1726.1091f, 316.82837f, (byte) 0, 4000);
				sp(npcId, 1294.3018f, 1730.9f, 316.875f, (byte) 0, 4000);
				sp(npcId, 1292.1957f, 1726.921f, 316.875f, (byte) 0, 4000);
				sp(npcId, 1293.0376f, 1722.3871f, 316.93515f, (byte) 0, 4000);
				break;
			case START_STAGE_4_ROUND_1:
				switch (Rnd.get(1, 3)) {
					case 1:
						npcId = 217788;
						break;
					case 2:
						npcId = 217789;
						break;
					case 3:
						npcId = 217790;
						break;
				}
				sp(217786, 1263.4213f, 791.8533f, 436.64014f, (byte) 60, 4000);
				sp(217786, 1267.2097f, 804.04456f, 436.64008f, (byte) 60, 4000);
				sp(217786, 1267.0653f, 781.0253f, 436.64017f, (byte) 60, 4000);
				sp(npcId, 1253.5984f, 791.6149f, 436.64008f, (byte) 0, 4000);
				break;
			case START_ALTERNATIVE_STAGE_4_ROUND_1:
				switch (Rnd.get(1, 3)) {
					case 1:
						npcId = 217791; // http://www.aiondatabase.com/npc/217805 spawn 2 5%
						break;
					case 2:
						npcId = 217792;
						break;
					case 3:
						npcId = 217793;
						break;
				}
				sp(217787, 1252.525f, 248.50781f, 405.38016f, (byte) 60, 4000);
				sp(217787, 1250.0901f, 237.69656f, 405.39676f, (byte) 60, 4000);
				sp(217787, 1253.0117f, 225.77977f, 405.3801f, (byte) 60, 4000);
				sp(npcId, 1242.3618f, 237.89081f, 405.3801f, (byte) 0, 4000);
				break;
			case START_STAGE_5_ROUND_1:
				if (player != null) {
					switch (player.getRace()) {
						case ELYOS:
							switch (Rnd.get(1, 5)) {
								case 1:
									npcId = 217807;
									break;
								case 2:
									npcId = 217808;
									break;
								case 3:
									npcId = 217809;
									break;
								case 4:
									npcId = 217810;
									break;
								case 5:
									npcId = 217815;
									break;
							}
							break;
						case ASMODIANS:
							switch (Rnd.get(1, 5)) {
								case 1:
									npcId = 217811;
									break;
								case 2:
									npcId = 217812;
									break;
								case 3:
									npcId = 217813;
									break;
								case 4:
									npcId = 217814;
									break;
								case 5:
									npcId = 217816;
									break;
							}
							break;
						default:
							break;
					}
					sp(npcId, 335.7365f, 337.93097f, 96.0909f, (byte) 0, 4000);
				}
				break;
			case START_STAGE_6_ROUND_1:
				sp(217819, 1769.4579f, 1290.9342f, 389.11728f, (byte) 80, 4000);
				break;
			default:
				break;
		}
	}

	private void sp(final int npcId, final float x, final float y, final float z, final byte h, int time) {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (!isInstanceDestroyed) {
					spawn(npcId, x, y, z, h);
				}
			}
		}, time);
	}

	private void setEvent(StageType type, int time) {
		this.stageType = type;
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				sendEventPacket();
			}
		}, time);
	}

	@Override
	public void handleUseItemFinish(Player player, Npc npc) {
		switch (npc.getNpcId()) {
			case 217758:
				spawn(205674, 345.52954f, 1662.6697f, 95.25f, (byte) 0);
				break;
		}
	}

	@Override
	public void onDropRegistered(Npc npc) {
		Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		int npcId = npc.getNpcId();
		int itemId = 0;
		Integer object = instance.getSoloPlayerObj();
		switch (npcId) {
			case 217758:
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, object, npcId, 186000134, 1));
				break;
			case 218571:
				dropItems.clear();
				switch (Rnd.get(1, 3)) {
					case 1:
						itemId = 182006429;
						break;
					case 2:
						itemId = 182006430;
						break;
					case 3:
						itemId = 182006431;
						break;
				}
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, object, npcId, itemId, 1));
				break;
			case 218185:
				dropItems.clear();
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, object, npcId, 188051349, 1));
				break;
			case 217827:
			case 217828:
			case 217829:
				int count = 0;
				switch (rewardCount) {
					case 0:
						switch (Rnd.get(1, 3)) {
							case 1:
								count = 1;
								break;
							case 2:
								count = 10;
								break;
							case 3:
								count = 0;
								break;
						}
						break;
					case 1:
						switch (Rnd.get(1, 3)) {
							case 1:
								count = 2;
								break;
							case 2:
								count = 18;
								break;
							case 3:
								count = 0;
								break;
						}
						break;
					case 2:
						switch (Rnd.get(1, 3)) {
							case 1:
								count = 3;
								break;
							case 2:
								count = 26;
								break;
							case 3:
								count = 250;
								break;
						}
						break;
				}

				switch (npcId) {
					case 217827:
						despawnNpc(getNpc(217828));
						despawnNpc(getNpc(217829));
						break;
					case 217828:
						despawnNpc(getNpc(217827));
						despawnNpc(getNpc(217829));
						break;
					case 217829:
						despawnNpc(getNpc(217827));
						despawnNpc(getNpc(217828));
						break;
				}

				if (count == 0) {
					if (rewardCount == 0) {
						sp(217837, npc.getX(), npc.getY(), npc.getZ(), (byte) 0, 0);
					}
					else if (rewardCount == 1) {
						sp(217838, npc.getX(), npc.getY(), npc.getZ(), (byte) 0, 0);
					}
					despawnNpc(npc);
					return;
				}
				rewardCount++;
				Player player = null;
				if (!instance.getPlayersInside().isEmpty()) {
					player = instance.getPlayersInside().get(0);
				}
				if (player != null) {
					if (player.isInsideZone(ZoneName.get("ILLUSION_STADIUM_8_300320000"))) {
						sp(205667, 1258.8464f, 237.85518f, 405.39673f, (byte) 0, 0);
					}
					else if (player.isInsideZone(ZoneName.get("ILLUSION_STADIUM_13_300320000"))) {
						sp(205677, 1271.5472f, 791.36145f, 436.64017f, (byte) 0, 0);
					}
				}
				dropItems.clear();
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, object, npcId, 186000130, count));
				break;
		}
	}

	@Override
	public void onPlayerLogOut(Player player) {
		TeleportService2.moveToInstanceExit(player, mapId, player.getRace());
	}
}
