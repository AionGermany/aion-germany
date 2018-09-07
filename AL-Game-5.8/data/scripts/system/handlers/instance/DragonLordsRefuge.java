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
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.actions.CreatureActions;
import com.aionemu.gameserver.model.drop.DropItem;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUEST_ACTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldMapInstance;

/**
 * @author Bobobear
 * @modified Luzien
 * @updated 4.3 Eloann
 */
@InstanceID(300520000)
public class DragonLordsRefuge extends GeneralInstanceHandler {

	private final AtomicInteger specNpcKilled = new AtomicInteger();
	private boolean isInstanceDestroyed;
	private Race instanceRace;
	private int killedCount;
	private Future<?> failTask;

	@Override
	public void onDie(Npc npc) {
		if (isInstanceDestroyed) {
			return;
		}

		int npcId = npc.getNpcId();

		switch (npcId) {
			case 219365: // fissurefang
				despawnNpc(219365); // despawn fissurefang corpse
				performSkillToTarget(219361, 219361, 20979); // remove Fissure Buff
				sendMsg(1401533);
				checkIncarnationKills();
				break;
			case 219366: // graviwing
				despawnNpc(219366); // despawn graviwing corpse
				performSkillToTarget(219361, 219361, 20981); // remove Gravity Buff
				sendMsg(1401535);
				checkIncarnationKills();
				break;
			case 219367: // wrathclaw
				despawnNpc(219367); // despawn wrathclaw corpse
				performSkillToTarget(219361, 219361, 20980); // remove Wrath Buff
				sendMsg(1401534);
				checkIncarnationKills();
				break;
			case 219368: // petriscale
				despawnNpc(219368); // despawn petriscale corpse
				performSkillToTarget(219361, 219361, 20982); // remove Petrification Buff
				sendMsg(1401536);
				checkIncarnationKills();
				break;
			case 730695:
				instance.getNpc(219359).getEffectController().removeEffect(20590);
				break;
			case 730696:
				instance.getNpc(219359).getEffectController().removeEffect(20591);
				break;
			case 219359: // Calindi Flamelord
				despawnNpc(730694); // despawn tiamat aetheric field
				despawnNpc(730695); // despawn Surkanas if spawned
				despawnNpc(730696); // despawn Surkanas if spawned
				performSkillToTarget(219360, 219360, 20919); // Transformation
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						despawnNpc(219360); // despawn tiamat woman (1st spawn)
						spawn(219361, 466.7468f, 514.5500f, 417.4044f, (byte) 0);// tiamat dragon 2nd Spawn
						performSkillToTarget(219361, 219361, 20975); // Fissure Buff
						performSkillToTarget(219361, 219361, 20976); // Wrath Buff
						performSkillToTarget(219361, 219361, 20977); // Gravity Buff
						performSkillToTarget(219361, 219361, 20978); // Petrification Buff
						performSkillToTarget(219361, 219361, 20984); // Unbreakable Wing (reflect)
					}
				}, 5000);

				// schedule dragon lords roar skill to block all players before spawn empyrean lords
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						performSkillToTarget(219361, 219361, 20920);
					}
				}, 8000);

				// spawn Kaisinel or Marchutan Gods (depends of group race)
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						spawn((instanceRace == Race.ELYOS ? 219488 : 219491), 504f, 515f, 417.405f, (byte) 60);
					}
				}, 15000);

				// schedule spawn of balaur spiritualists and broadcast messages
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						sendMsg(instanceRace == Race.ELYOS ? 1401531 : 1401532);
						// spawn balaur spritualists (will defend Internal Passages)
						spawn(283163, 463f, 568f, 417.405f, (byte) 105);
						spawn(283164, 545f, 568f, 417.405f, (byte) 78);
						spawn(283165, 545f, 461f, 417.405f, (byte) 46);
						spawn(283166, 463f, 461f, 417.405f, (byte) 17);
					}
				}, 40000);
				break;
			case 219362: // Tiamat Dragon (3rd spawn)
				if (failTask != null && !failTask.isDone()) {
					failTask.cancel(true);
				}
				spawn(701542, 480f, 514f, 417.405f, (byte) 0);// tiamat treasure chest reward
				spawn(730630, 548.18683f, 514.54523f, 420f, (byte) 0, 23); // exit
				spawn(800430, 502.426f, 510.462f, 417.405f, (byte) 0); // kahrun
				spawn(800431, 482.872f, 514.705f, 417.405f, (byte) 0); // kahrun
				spawn(800464, 544.964f, 517.898f, 417.405f, (byte) 113); // reian sorcerer
				spawn(800465, 545.605f, 510.325f, 417.405f, (byte) 17); // reian sorcerer
				break;
			case 283163: // balaur spiritualist (spawn Portal after die)
				healEmpyreanLord(0); // heal Empyrean Lord
				spawn(730675, 460.082f, 571.978f, 417.405f, (byte) 43); // spawn portal to tiamat incarnation
				break;
			case 283164:
				healEmpyreanLord(1);
				spawn(730676, 547.822f, 571.876f, 417.405f, (byte) 18);
				break;
			case 283165:
				healEmpyreanLord(2);
				spawn(730674, 547.909f, 456.568f, 417.405f, (byte) 103);
				break;
			case 283166:
				healEmpyreanLord(3);
				spawn(730673, 459.548f, 456.849f, 417.405f, (byte) 78);
				break;
			case 219361: // Tiamat Dragon (1st spawn) - Players cannot kill tiamat, they must kill 4 incanation before
				break;
			case 219488: // Kaisinel Gods (1st Spawn)
			case 219491: // Marchutan Gods (1st Spawn)
				sendMsg(1401542);
				Npc tiamat = getNpc(219361);
				tiamat.getController().useSkill(20983);
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						despawnNpc(219361);// despawn tiamat dragon
						spawn(730694, 436.7526f, 513.8103f, 420.6662f, (byte) 0, 14); // re-spawn tiamat aetheric field
						spawn(219360, 451.9700f, 514.5500f, 417.4044f, (byte) 0); // re-spawn tiamat woman to initial position
						sendMsg(1401563);// broadcast message of instance failed
						spawn(730630, 548.18683f, 514.54523f, 420f, (byte) 0, 23); // spawn exit
					}
				}, 5000);
				break;
		}
	}

	private void performSkillToTarget(int npcId, int targetId, int skillId) {
		if (isSpawned(npcId) && isSpawned(targetId)) {
			final Npc npc = getNpc(npcId);
			final Npc target = getNpc(targetId);
			SkillEngine.getInstance().getSkill(npc, skillId, 100, target).useSkill();
		}
	}

	private void despawnNpc(int npcId) {
		Npc npc = getNpc(npcId);
		if (npc != null) {
			npc.getController().onDelete();
		}
	}

	private boolean isSpawned(int npcId) {
		Npc npc = getNpc(npcId);
		if (!isInstanceDestroyed && npc != null && !CreatureActions.isAlreadyDead(npc)) {
			return true;
		}
		return false;
	}

	private void startFinalTimer() {
		sendMsg(1401547);// broadcast message for start time
		for (Player p : instance.getPlayersInside()) {
			PacketSendUtility.sendPacket(p, new SM_QUEST_ACTION(0, 1800));
		}

		failTask = ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (isSpawned(219362)) {
					despawnNpc(219362); // despawn tiamat dragon
					spawn(730694, 436.7526f, 513.8103f, 420.6662f, (byte) 0, 14); // re-spawn tiamat aetheric field
					spawn(219360, 451.9700f, 514.5500f, 417.4044f, (byte) 0); // re-spawn tiamat woman to initial position
					sendMsg(1401563); // broadcast message of instance failed
					spawn(730630, 548.18683f, 514.54523f, 420f, (byte) 0, 23); // spawn exit
				}
			}
		}, 1800000);
	}

	private void healEmpyreanLord(int id) {
		int npcId = instanceRace == Race.ELYOS ? 219488 : 219491;
		int skill = 20993 + id;
		Npc npc = instance.getNpc(npcId);
		if (npc != null && !CreatureActions.isAlreadyDead(npc)) {
			SkillEngine.getInstance().getSkill(npc, skill, 60, npc).useNoAnimationSkill(); // heal 7% + def buff
			sendMsg(1401551);
		}
	}

	private void checkIncarnationKills() {
		killedCount = specNpcKilled.incrementAndGet();
		if (killedCount == 4) {
			if (!isSpawned(219361)) {
				return;
			}
			Npc npc = getNpc(219361);
			final int npcId = instanceRace == Race.ELYOS ? 219488 : 219491;
			final int msg = instanceRace == Race.ELYOS ? 1401540 : 1401541;
			npc.getEffectController().removeEffect(20984);// dispel Unbreakable Wing (reflect)
			sendMsg(1401537);
			// schedule spawn of empyrean lords for final attack to tiamat before became exausted
			ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					if (isSpawned(npcId)) {
						despawnNpc(npcId);
						spawn(npcId + 1, 528f, 514f, 417.405f, (byte) 60);
					}
				}
			}, 30000);
			// schedule spawn of Tiamat 3rd Spawn
			ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					if (isSpawned(npcId + 1)) {
						spawn(283137, 461f, 514f, 417.405f, (byte) 0);
						spawn(283134, 461f, 514f, 417.405f, (byte) 0);
						spawn(219362, 461f, 514f, 417.405f, (byte) 0);
						ThreadPoolManager.getInstance().schedule(new Runnable() {

							@Override
							public void run() {
								despawnNpc(283134);
								despawnNpc(283137);
								despawnNpc(219361);
								sendMsg(msg);
								ThreadPoolManager.getInstance().schedule(new Runnable() {

									@Override
									public void run() {
										startFinalTimer();
									}
								}, 10000);
							}
						}, 2000);
					}
				}
			}, 40000);
		}
	}

	@Override
	public void onInstanceDestroy() {
		isInstanceDestroyed = true;
	}

	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
		killedCount = 0;
	}

	@Override
	public boolean onDie(final Player player, Creature lastAttacker) {
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);

		PacketSendUtility.sendPacket(player, new SM_DIE(player.haveSelfRezEffect(), player.haveSelfRezItem(), 0, 8));
		return true;
	}

	@Override
	public boolean onReviveEvent(Player player) {
		PlayerReviveService.revive(player, 25, 25, true, 0);
		player.getGameStats().updateStatsAndSpeedVisually();
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
		TeleportService2.teleportTo(player, mapId, instanceId, 540.33655f, 515.00146f, 417.40436f, (byte) 59);
		return true;
	}

	@Override
	public void onEnterInstance(Player player) {
		if (instanceRace == null) {
			instanceRace = player.getRace();
		}
	}

	@Override
	public void onExitInstance(Player player) {
		TeleportService2.moveToInstanceExit(player, mapId, player.getRace());
	}

	@Override
	public void onDropRegistered(Npc npc) { // Dragon Lord Tiamat Chest for all members
		Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		int npcId = npc.getNpcId();
		switch (npcId) {
			case 701542: // Tiamat's Huge Treasure Chest
				int index = dropItems.size() + 1;
				for (Player player : instance.getPlayersInside()) {
					if (player.isOnline()) {
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188052084, 1));
					}
				}
				break;
		}
	}

	@Override
	public void onPlayerLogOut(Player player) {
		TeleportService2.moveToInstanceExit(player, mapId, player.getRace());
	}
}
