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

import java.util.concurrent.Future;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.actions.CreatureActions;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.NpcShoutsService;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldMapInstance;

import javolution.util.FastList;

/**
 * @author Alcapwnd
 */
@InstanceID(301160000)
public class NightmareCircusInstance extends GeneralInstanceHandler {

	private final FastList<Npc> mandurit = FastList.newInstance();
	private final FastList<Npc> boss = FastList.newInstance();
	private Future<?> manduritTask;
	private Future<?> eventTask;
	private boolean isInstanceDestroyed;
	private final FastList<Npc> iuFriend = FastList.newInstance();

	private int getMandurit() {
		int manduritM = 233149;
		if (Rnd.get(100) <= 50) {
			manduritM = 233144;
		}
		return manduritM;
	}

	private void spawnLeftMandurit() {
		int monster = getMandurit();
		mandurit.add((Npc) spawn(monster, 522.04f, 624.54f, 207.350f, (byte) 90, "3011600001", 1));
		mandurit.add((Npc) spawn(monster, 524.46f, 625.32f, 207.496f, (byte) 90, "3011600001", 0));
		mandurit.add((Npc) spawn(monster, 524.35f, 626.77f, 207.764f, (byte) 90, "3011600001", 2));
		// mandurit.add((Npc) spawn(monster, 521.89f, 626.78f, 207.815f, (byte) 90, "3011600001", 0));
	}

	private void spawnRightMandurit() {
		int monster = getMandurit();
		mandurit.add((Npc) spawn(monster, 523.25f, 499.60f, 198.707f, (byte) 30, "3011600002", 1));
		mandurit.add((Npc) spawn(monster, 520.66f, 499.46f, 198.599f, (byte) 30, "3011600002", 0));
		mandurit.add((Npc) spawn(monster, 523.32f, 497.19f, 198.513f, (byte) 30, "3011600002", 2));
		// mandurit.add((Npc) spawn(monster, 520.58f, 497.33f, 198.562f, (byte) 30, "3011600002", 0));
	}

	private void spawnMistressVibaloka() {
		boss.add((Npc) spawn(233153, 549.07922f, 565.66467f, 198.83f, (byte) 60));
		manduritTask = ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				despawnBoss();
				spawnJollyMakekike();
			}
		}, 50000);
	}

	private void spawnJollyMakekike() {
		sendMsg(1500975);
		boss.add((Npc) spawn(233147, 549.07922f, 565.66467f, 198.83f, (byte) 60));
		manduritTask = ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				despawnBoss();
				spawnRingmasterRukibuki();
			}
		}, 90000);
	}

	private void spawnRingmasterRukibuki() {
		sendMsg(1501002);
		boss.add((Npc) spawn(233161, 549.07922f, 565.66467f, 198.83f, (byte) 60));
	}

	private void startEventTimer() {
		manduritTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				if (isInstanceDestroyed) {
					cancelManduritTask();
					return;
				}
				walkerDestroy();
				spawnLeftMandurit();
				spawnRightMandurit();
				organizeAndSpawn();
			}
		}, 1000, 10000);

		eventTask = ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (isInstanceDestroyed) {
					cancelManduritTask();
					return;
				}
				cancelManduritTask();
				spawnMistressVibaloka();
			}
		}, 159000);
	}

	private void despawnMandurit() {
		for (FastList.Node<Npc> n = mandurit.head(), end = mandurit.tail(); (n = n.getNext()) != end;) {
			Npc obj = n.getValue();
			if (obj == null) {
				continue;
			}
			if (obj.isSpawned() && !obj.getLifeStats().isAlreadyDead()) {
				obj.getController().onDelete();
			}
		}
	}

	private void despawnBoss() {
		for (FastList.Node<Npc> n = boss.head(), end = boss.tail(); (n = n.getNext()) != end;) {
			Npc obj = n.getValue();
			if (obj == null) {
				continue;
			}
			if (obj.isSpawned() && !obj.getLifeStats().isAlreadyDead()) {
				obj.getController().onDelete();
			}
		}
	}

	private void despawnIuFriend() {
		for (FastList.Node<Npc> n = iuFriend.head(), end = iuFriend.tail(); (n = n.getNext()) != end;) {
			Npc obj = n.getValue();
			if (obj == null) {
				continue;
			}
			if (obj.isSpawned() && !obj.getLifeStats().isAlreadyDead()) {
				obj.getController().onDelete();
			}
		}
	}

	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
		iuFriend.add((Npc) spawn(831573, 524.7768f, 565.434f, 199.553f, (byte) 60));
		iuFriend.add((Npc) spawn(831550, 472.84296f, 564.04993f, 202.58923f, (byte) 50));
		iuFriend.add((Npc) spawn(831551, 473.07919f, 571.30505f, 202.58923f, (byte) 76));
		iuFriend.add((Npc) spawn(831552, 471.4483f, 572.52191f, 202.58923f, (byte) 76));
		iuFriend.add((Npc) spawn(831553, 471.67184f, 562.4325f, 202.58923f, (byte) 50));
	}

	@Override
	public void onEnterInstance(final Player player) {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				sendMsg(1500974);
				if (player.getRace().equals(Race.ASMODIANS)) {
					SkillEngine.getInstance().applyEffectDirectly(21332, player, player, 0);
				}
				else {
					SkillEngine.getInstance().applyEffectDirectly(21329, player, player, 0);
				}
			}
		}, 1000);
	}

	@Override
	public void onDie(Npc npc) {
		switch (npc.getNpcId()) {
			case 831348:
			case 831349:
				NpcShoutsService.getInstance().sendMsg(npc, 1500997, npc.getObjectId(), 0, 0);
				break;
			case 831740:
				sendMsg(1500991);
				startEventTimer();
				break;
			case 233153:
				sendMsg(1500976);
				break;
			case 233147:
				sendMsg(1501003);
				break;
			case 233161:
				despawnMandurit();
				Npc cage = getNpc(831743);
				if (cage != null) {
					CreatureActions.delete(cage);
				}
				spawn(831742, 522.39825f, 564.69006f, 199.03371f, (byte) 0, 14);
				Player player = npc.getAggroList().getMostPlayerDamage();
				if (player != null) {
					spawn(831574, 519.29773f, 565.52289f, 199.72002f, (byte) 60);

					if (Rnd.get(100) <= 50) {
						spawn(831559, 517.784f, 562.0495f, 198.906f, (byte) 30);
						spawn(831560, 520.41559f, 563.30469f, 198.906f, (byte) 53);
						spawn(831561, 517.84387f, 568.37622f, 198.906f, (byte) 90);
						spawn(831562, 520.56641f, 567.83276f, 198.906f, (byte) 73);
					}
					else {
						spawn(831554, 517.784f, 562.0495f, 198.906f, (byte) 30);
						spawn(831555, 520.41559f, 563.30469f, 198.906f, (byte) 53);
						spawn(831556, 517.84387f, 568.37622f, 198.906f, (byte) 90);
						spawn(831557, 520.56641f, 567.83276f, 198.906f, (byte) 73);
					}
				}

				spawn(831575, 507.42548f, 552.5874f, 199.86153f, (byte) 0);
				spawn(831575, 507.46768f, 556.34448f, 199.86153f, (byte) 0);
				spawn(831575, 507.75433f, 568.13837f, 199.86153f, (byte) 0);
				spawn(831575, 507.77673f, 564.34204f, 199.86153f, (byte) 0);
				spawn(831575, 507.83087f, 571.92407f, 199.86153f, (byte) 0);
				spawn(831575, 507.90619f, 575.8056f, 199.86153f, (byte) 0);

				spawn(831745, 525.2016f, 578.8903f, 198.25f, (byte) 0);
				spawn(831745, 526.8166f, 581.68317f, 198.25f, (byte) 0);
				spawn(831745, 523.51f, 581.672f, 198.25f, (byte) 0);
				spawn(831745, 522.011f, 584.752f, 198.96f, (byte) 0);
				spawn(831745, 525.106f, 584.8903f, 198.96f, (byte) 0);
				spawn(831745, 528.34f, 584.49f, 198.96f, (byte) 0);

				spawn(831745, 524.54f, 549.3835f, 198.79f, (byte) 0);
				spawn(831745, 521.589f, 549.362f, 198.79f, (byte) 0);
				spawn(831745, 518.398f, 549.255f, 198.79f, (byte) 0);
				spawn(831745, 553.230f, 545.867f, 198.87f, (byte) 0);
				spawn(831745, 520.083f, 545.756f, 198.87f, (byte) 0);
				spawn(831745, 521.549f, 543.397f, 198.75f, (byte) 0);

				despawnIuFriend();
				break;
		}
	}

	@Override
	public boolean onDie(final Player player, Creature lastAttacker) {
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);
		PacketSendUtility.sendPacket(player, new SM_DIE(player.haveSelfRezEffect(), player.haveSelfRezItem(), 0, 8));
		return true;
	}

	@Override
	public boolean onReviveEvent(Player player) {
		PlayerReviveService.revive(player, 25, 25, false, 0);
		player.getGameStats().updateStatsAndSpeedVisually();
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
		TeleportService2.teleportTo(player, mapId, instanceId, 510.2436f, 512.10333f, 417.40436f, (byte) 49);
		return true;
	}

	private void cancelManduritTask() {
		if (manduritTask != null && !manduritTask.isDone()) {
			manduritTask.cancel(true);
		}
	}

	private void cancelEventTask() {
		if (eventTask != null && !eventTask.isDone()) {
			eventTask.cancel(true);
		}
	}

	@Override
	public void onInstanceDestroy() {
		cancelManduritTask();
		cancelEventTask();
		despawnMandurit();
		despawnBoss();
		despawnIuFriend();
		isInstanceDestroyed = true;
		FastList.recycle(mandurit);
		FastList.recycle(boss);
		FastList.recycle(iuFriend);
	}

}
