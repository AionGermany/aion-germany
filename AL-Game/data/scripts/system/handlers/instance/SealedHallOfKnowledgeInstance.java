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

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.drop.DropItem;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.StaticDoor;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.instance.InstanceScoreType;
import com.aionemu.gameserver.model.instance.instancereward.SealedHallOfKnowledgeReward;
import com.aionemu.gameserver.model.instance.playerreward.SealedHallOfKnowledgePlayerReward;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INSTANCE_SCORE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.abyss.AbyssPointsService;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;

/**
 * @author Eloann edited by KyrieAnn™
 */
@InstanceID(300480000)
public class SealedHallOfKnowledgeInstance extends GeneralInstanceHandler {

	private long startTime;
	private Future<?> instanceTimer;
	public boolean isInstanceDestroyed;
	private Map<Integer, StaticDoor> doors;
	private SealedHallOfKnowledgeReward instanceReward;
	private int rank;

	protected SealedHallOfKnowledgePlayerReward getPlayerReward(Player player) {
		Integer object = player.getObjectId();
		if (instanceReward.getPlayerReward(object) == null) {
			addPlayerToReward(player);
		}
		return (SealedHallOfKnowledgePlayerReward) instanceReward.getPlayerReward(object);
	}

	private void addPlayerToReward(Player player) {
		instanceReward.addPlayerReward(new SealedHallOfKnowledgePlayerReward(player.getObjectId()));
	}

	public boolean containPlayer(Integer object) {
		return instanceReward.containPlayer(object);
	}

	@Override
	public void onDie(Npc npc) {
		int points = 0;
		int npcId = npc.getNpcId();
		Player player = npc.getAggroList().getMostPlayerDamage();
		switch (npc.getObjectTemplate().getTemplateId()) {
			case 230062:
			case 230063:
			case 230064:
				points = 150;
				break;
			case 230066:
			case 230067:
			case 230078:
			case 230079:
				points = 285;
				break;
			case 230074:
				points = 1125;
				break;
			case 230051:
			case 230052:
			case 230053:
			case 230054:
			case 230055:
			case 230056:
			case 230057:
			case 230058:
				points = 2010;
				break;
			case 230080: // Sheban Intelligence Captain.
				points = 3051;
				break;
			case 230081: // Sheban Intelligence Captain.
				points = 3051;
				break;
			case 272762:
				despawnNpc(npc);
				break;
		}
		if (instanceReward.getInstanceScoreType().isStartProgress()) {
			instanceReward.addNpcKill();
			instanceReward.addPoints(points);
			sendPacket(npc.getObjectTemplate().getNameId(), points);
		}
		switch (npcId) {
			case 230081: // Sheban Intelligence Captain.
				stopInstance(player);
				break;
			case 230080: // Sheban Intelligence Captain.
				stopInstance(player);
				break;
			case 233253:
				spawn(701572, 556.5924f, 416.37885f, 96.81002f, (byte) 43); // Danuar Mysticarium Exit.
				break;
			case 233254:// Frenzied Chairman Nautius.
				spawn(701572, 556.5924f, 416.37885f, 96.81002f, (byte) 43); // Danuar Mysticarium Exit.
				break;
		}
	}

	private int getNpcBonus(int npcId) {
		switch (npcId) {
			case 831145:
			case 831146:
				return 500;
		}
		return 0;
	}

	public void removeItems(Player player) {
		Storage storage = player.getInventory();
		storage.decreaseByItemId(185000127, storage.getItemCountByItemId(185000127)); // Idgel Storage Key.
		storage.decreaseByItemId(185000165, storage.getItemCountByItemId(185000165)); // Idgel Storage Key.
	}

	@Override
	public void handleUseItemFinish(Player player, Npc npc) {
		if (!instanceReward.isStartProgress()) {
			return;
		}
		int rewardetPoints = getNpcBonus(npc.getNpcId());
		instanceReward.addPoints(rewardetPoints);
		sendSystemMsg(player, npc, rewardetPoints);
		sendPacket(rewardetPoints);
	}

	protected void sendSystemMsg(Player player, Creature creature, int rewardPoints) {
		int nameId = creature.getObjectTemplate().getNameId();
		DescriptionId name = new DescriptionId(nameId * 2 + 1);
		PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400237, nameId == 0 ? creature.getName() : name, rewardPoints));
	}

	private int getTime() {
		long result = System.currentTimeMillis() - startTime;
		if (result < 30000) {
			return (int) (30000 - result);
		}
		else if (result < 1800000) { // 30 Minutes.
			return (int) (1800000 - (result - 30000));
		}
		return 0;
	}

	private void sendPacket(final int point) {
		instance.doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(getTime(), instanceReward, null));
			}
		});
	}

	private void sendPacket(final int nameId, final int point) {
		final List<Player> players = instance.getPlayersInside();
		instance.doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				if (nameId != 0 && point != 0) {
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400237, new DescriptionId(nameId * 2 + 1), point));
				}
				PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(getTime(), instanceReward, players));
			}
		});
	}

	private int checkRank(int totalPoints) {
		int timeRemain = getTime();

		if (timeRemain >= 1080000 && totalPoints >= 29911) {
			rank = 1;
		}
		else if (timeRemain >= 960000 && totalPoints >= 21682) {
			rank = 2;
		}
		else if (timeRemain >= 840000 && totalPoints >= 14824) {
			rank = 3;
		}
		else if (timeRemain >= 360000 && totalPoints >= 9338) {
			rank = 4;
		}
		else if (timeRemain >= 120000 && totalPoints >= 6595) {
			rank = 5;
		}
		else if (timeRemain > 1) {
			rank = 6;
		}
		else {
			rank = 8;
		}
		return rank;
	}

	@Override
	public void onEnterInstance(final Player player) {
		if (instanceTimer == null) {
			startTime = System.currentTimeMillis();
			instanceTimer = ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					openFirstDoors();
					// PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_INSTANCE_START_IDLDF5RE_SOLO); //Danuar Mysticarium Start.
					instanceReward.setInstanceScoreType(InstanceScoreType.START_PROGRESS);
					sendPacket(0);
				}
			}, 30000);
		}
		sendPacket(0);
	}

	protected void stopInstance(Player player) {
		stopInstanceTask();
		instanceReward.setRank(checkRank(instanceReward.getPoints()));
		instanceReward.setInstanceScoreType(InstanceScoreType.END_PROGRESS);
		doReward(player);
		spawn(701572, 556.5924f, 416.37885f, 96.81002f, (byte) 17); // Danuar Mysticarium Exit.
		sendPacket(0);
	}

	private void stopInstanceTask() {
		if (instanceTimer != null) {
			instanceTimer.cancel(true);
		}
	}

	@Override
	public void doReward(Player player) {
		if (!instanceReward.isRewarded()) {
			instanceReward.setRewarded();
			switch (rank) {
				case 1: // S
					instanceReward.setBasicAP(1402);
					instanceReward.setSillusCrest(12);
					instanceReward.setCeramiumMedal(1);
					instanceReward.setFavorableBundle(1);
					spawnRankS_NautiusBoss();
					break;
				case 2: // A
					instanceReward.setBasicAP(1020);
					instanceReward.setSillusCrest(8);
					instanceReward.setCeramiumFragments(3);
					instanceReward.setValorBundle(1);
					break;
				case 3: // B
					instanceReward.setBasicAP(892);
					instanceReward.setSillusCrest(7);
					instanceReward.setMithrilMedal(1);
					break;
				case 4: // C
					instanceReward.setBasicAP(765);
					instanceReward.setSillusCrest(6);
					break;
				case 5: // D
					instanceReward.setBasicAP(382);
					instanceReward.setSillusCrest(3);
					break;
				case 6: // F
					break;
			}
			AbyssPointsService.addAp(player, instanceReward.getBasicAP());
			ItemService.addItem(player, 186000239, instanceReward.getSillusCrest());
			ItemService.addItem(player, 186000242, instanceReward.getCeramiumMedal());
			ItemService.addItem(player, 152012578, instanceReward.getCeramiumFragments());
			ItemService.addItem(player, 186000147, instanceReward.getMithrilMedal());
			ItemService.addItem(player, 188052543, instanceReward.getFavorableBundle());
			ItemService.addItem(player, 188052547, instanceReward.getValorBundle());
		}
	}

	@Override
	public void onLeaveInstance(Player player) {
		removeItems(player);
	}

	protected void openFirstDoors() {
		openDoor(3);
	}

	// TODO ?
	// private void sendMsg(final String str) {
	// instance.doOnAllPlayers(new Visitor<Player>() {
	//
	// @Override
	// public void visit(Player player) {
	// PacketSendUtility.sendMessage(player, str);
	// }
	// });
	// }

	// private int getNpcBonus(int npcId) {
	// return instanceReward.getNpcBonus(npcId);
	// }

	@Override
	public void onInstanceDestroy() {
		if (instanceTimer != null) {
			instanceTimer.cancel(false);
		}
		isInstanceDestroyed = true;
		doors.clear();
	}

	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
		instanceReward = new SealedHallOfKnowledgeReward(mapId, instanceId);
		instanceReward.setInstanceScoreType(InstanceScoreType.PREPARING);
		doors = instance.getDoors();
		int npcId = 0;
		switch (Rnd.get(1, 2)) {
			case 1:
				npcId = 230080; // Sheban intelligence Inspector
				break;
			case 2:
				npcId = 230081; // Sheban intelligence Captain
				break;
		}
		spawn(npcId, 517.96814f, 472.28555f, 95.38072f, (byte) 40);
	}

	private void despawnNpc(Npc npc) {
		if (npc != null) {
			npc.getController().onDelete();
		}
	}

	protected void openDoor(int doorId) {
		StaticDoor door = doors.get(doorId);
		if (door != null) {
			door.setOpen(true);
		}
	}

	private void spawnRankS_NautiusBoss() {
		int npcId = 0;
		switch (Rnd.get(1, 2)) {
			case 1:
				npcId = 233253; // Berserk Chairman Nautius
				break;
			case 2:
				npcId = 233254; // Frenzied Chairman Nautius
				break;
		}
		spawn(npcId, 546.451f, 431.18f, 94.78f, (byte) 46);
	}

	@Override
	public void onDropRegistered(Npc npc) {
		Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		int npcId = npc.getNpcId();
		switch (npcId) {
			case 230066:
				switch (Rnd.get(1, 3)) {
					case 1:
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npc.getNpcId(), 185000127, 1));
						break;
					case 2:
						break;
					case 3:
						break;
				}
				break;
			case 230080:
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npc.getNpcId(), 185000165, 1));
			case 230081:
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npc.getNpcId(), 185000165, 1));
		}
	}

	@Override
	public boolean onDie(final Player player, Creature lastAttacker) {
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);
		PacketSendUtility.sendPacket(player, new SM_DIE(player.haveSelfRezEffect(), player.haveSelfRezItem(), 0, 8));
		return true;
	}
}
