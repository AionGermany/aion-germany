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

import static com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.ai2.AbstractAI;
import com.aionemu.gameserver.controllers.PlayerController;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.actions.NpcActions;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.StaticDoor;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUEST_ACTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;

/**
 * @author Alcapwnd
 */
@InstanceID(300590000)
public class OphidanBridgeInstance extends GeneralInstanceHandler implements PlayerController.Listener {

	private final static int MAX_TIME_SPAWN_ARROUND_PLAYER = 10;
	protected boolean isInstanceDestroyed = false;
	private Map<Integer, StaticDoor> doors;
	private byte entrance = 0;
	private int Powerdevice;
	private int spawnedArroundPlayer = 0;
	private Future<?> SpawnTask;
	@SuppressWarnings("unused")
	private Future<?> failTask;
	private boolean isStartTimer = false;
	private long startTime;
	private byte echec = 0;
	private byte Dissipshield = 0;

	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
		doors = instance.getDoors();
		spawn(230413, 476.74689f, 523.6272f, 598.8186f, (byte) 13); // GENERATEUR POST (230417)
		spawn(230414, 524.53607f, 427.29379f, 621.82062f, (byte) 15); // GENERATEUR NORD (230418)
		spawn(230415, 602.40997f, 556.41998f, 592.16406f, (byte) 113); // GENERATEUR SUD (230419)
		spawn(230416, 666.76001f, 474.20999f, 601.1673f, (byte) 117); // GENERATEUR Defense (230420)
		spawn(231050, 317f, 489.04999f, 607.64343f, (byte) 0);
	}

	@Override
	public void onEnterInstance(Player player) {
		if (isStartTimer) {
			long time = System.currentTimeMillis() - startTime;
			if (time < 900000) {
				PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 900 - (int) time / 1000));
			}
		}
		player.getController().registerListener(this);
		entrance += 1;
		if (player.getRace() == Race.ELYOS) {
			PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 498));
			if (entrance == 1) {
				spawn(701646, 763.51685f, 533.11804f, 576.5853f, (byte) 59);// TANK ELYOS
				spawn(801763, 740.7189f, 536.31616f, 575.6878f, (byte) 1);
			}
		}
		else {
			PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 499));
			if (entrance == 1) {
				spawn(701647, 763.51685f, 533.11804f, 576.5853f, (byte) 59); // TANK ASMO
				spawn(801765, 740.7189f, 536.31616f, 575.6878f, (byte) 1);
			}
		}
	}

	@Override
	public void onLeaveInstance(Player player) {
		super.onLeaveInstance(player);
		player.getController().unregisterListener();
	}

	@Override
	public void onExitInstance(Player player) {
		super.onExitInstance(player);
		player.getController().unregisterListener();
	}

	@Override
	public void onDie(Npc npc) {
		final List<Player> playerList = instance.getPlayersInside();
		switch (npc.getNpcId()) {
			case 231050:
				spawn(730868, 317f, 489.04999f, 607.64343f, (byte) 0);
				break;
			case 230413:
				sendMsg(1401904);
				spawn(230417, 476.74689f, 523.6272f, 598.8186f, (byte) 13); // Disabled GENERATEUR POST
				despawnNpc(npc);
				Powerdevice++;
				if (Powerdevice == 4) {
					spawn(701644, 435.42862f, 496.41296f, 604.8871f, (byte) 1);
				}
				break;
			case 230417:
				spawn(230413, 476.74689f, 523.6272f, 598.8186f, (byte) 13); // Balaur GENERATEUR POST
				despawnNpc(npc);
				break;
			case 230414:
				sendMsg(1401905);
				spawn(230418, 524.53607f, 427.29379f, 621.82062f, (byte) 15); // Disabled GENERATEUR NORD
				despawnNpc(npc);
				Powerdevice++;
				if (Powerdevice == 4) {
					spawn(701644, 435.42862f, 496.41296f, 604.8871f, (byte) 1);
				}
				break;
			case 230418:
				spawn(230414, 524.53607f, 427.29379f, 621.82062f, (byte) 15); // balaur GENERATEUR NORD
				despawnNpc(npc);
				break;
			case 230415:
				sendMsg(1401906);
				spawn(230419, 602.40997f, 556.41998f, 592.16406f, (byte) 113); // Disabled GENERATEUR SUD
				despawnNpc(npc);
				Powerdevice++;
				if (Powerdevice == 4) {
					spawn(701644, 435.42862f, 496.41296f, 604.8871f, (byte) 1);
				}
				break;
			case 230419:
				spawn(230415, 602.40997f, 556.41998f, 592.16406f, (byte) 113); // balaur GENERATEUR SUD
				despawnNpc(npc);
				break;
			case 230416:
				sendMsg(1401907);
				spawn(230420, 666.76001f, 474.20999f, 601.1673f, (byte) 117); // Disabled GENERATEUR Defense
				despawnNpc(npc);
				Powerdevice++;
				if (Powerdevice == 4) {
					spawn(701644, 435.42862f, 496.41296f, 604.8871f, (byte) 1);
				}
				break;
			case 230420:
				echec += 1;
				doors.get(47).setOpen(false);
				spawn(230416, 666.76001f, 474.20999f, 601.1673f, (byte) 117); // balaur GENERATEUR Defense
				despawnNpc(npc);
				break;
			case 233320:
				startFinalTimer();
				despawnNpc(npc);
				if (npc.getX() == 751.5975f || npc.getY() == 518.4685f || npc.getZ() == 578.5f) {
					if (!isStartTimer) {
						isStartTimer = true;
						startTime = System.currentTimeMillis();
						ThreadPoolManager.getInstance().schedule(new Runnable() {

							@Override
							public void run() {
								for (Player player : playerList) {
									PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1401884));
								}
								attackgenerator(getNpc(230420), (Npc) spawn(233319, 697.05707f, 467.85675f, 599.68396f, (byte) 55));
								attackgenerator(getNpc(230420), (Npc) spawn(231189, 695.08307f, 471.75702f, 599.80f, (byte) 55));
								attackgenerator(getNpc(230420), (Npc) spawn(231190, 693.59186f, 462.29324f, 599.875f, (byte) 55));
							}
						}, 300000);
					}
				}
				break;
		}
	}

	private void startFinalTimer() {
		sendMsg(1401892);
		this.sendMessage(1401875, 15 * 60 * 1000);
		this.sendMessage(1401876, 20 * 60 * 1000);
		this.sendMessage(1401877, 24 * 60 * 1000);

		failTask = ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				sendMsg(1401878);
				final Npc vera1 = instance.getNpc(231050);
				if (vera1 != null) {
					SkillEngine.getInstance().getSkill(vera1, 21442, 60, vera1).useNoAnimationSkill();
				}
			}
		}, 1500000);
	}

	private void sendMessage(final int msgId, long delay) {
		if (delay == 0) {
			this.sendMsg(msgId);
		}
		else {
			ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					sendMsg(msgId);
				}
			}, delay);
		}
	}

	private boolean canUseLever() {
		if (isDead(getNpc(230413)) && isDead(getNpc(230414)) && isDead(getNpc(230415)) && isDead(getNpc(230416))) {
			return true;
		}
		return false;
	}

	private boolean isDead(Npc npc) {
		return (npc == null || npc.getLifeStats().isAlreadyDead());
	}

	@Override
	public void handleUseItemFinish(Player player, Npc npc) {
		switch (npc.getNpcId()) {
			case 701644:
				sendMsg(1401879);
				if (canUseLever() && echec == 0) {
					doors.get(47).setOpen(true);
					despawnNpc(npc);
					despawnNpc(instance.getNpc(701155));
					break;
				}
				break;
			case 701646:
				SkillEngine.getInstance().getSkill(npc, 21434, 60, player).useNoAnimationSkill();
				NpcActions.scheduleRespawn(npc);
				despawnNpc(npc);
				for (Player p : instance.getPlayersInside()) {
					if (p.getEffectController().hasAbnormalEffect(21434)) {
						StartSpawn(p);
						break;
					}
				}
				break;
			case 701647:
				SkillEngine.getInstance().getSkill(npc, 21435, 60, player).useNoAnimationSkill();
				NpcActions.scheduleRespawn(npc);
				despawnNpc(npc);
				for (Player p : instance.getPlayersInside()) {
					if (p.getEffectController().hasAbnormalEffect(21435)) {
						StartSpawn(p);
						break;
					}
				}
				break;
		}
	}

	private void SpawnArroundPlayer(final Player player) {

		if (spawnedArroundPlayer < MAX_TIME_SPAWN_ARROUND_PLAYER) {
			SpawnTask = ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					if (player.getEffectController().hasAbnormalEffect(21435) || player.getEffectController().hasAbnormalEffect(21434)) {
						SpawnFront(player);
						SpawnRight(player);
						SpawnLeft(player);
						spawnedArroundPlayer++;
						SpawnArroundPlayer(player);
					}
					else
						cancelTask();
				}
			}, 30000);
		}
	}

	private void cancelTask() {
		if (SpawnTask != null && !SpawnTask.isCancelled()) {
			SpawnTask.cancel(true);
		}
	}

	public void StartSpawn(final Player player) {
		spawnedArroundPlayer = 0;
		SpawnArroundPlayer(player);
	}

	public void SpawnRight(Player player) {
		double radian = Math.toRadians(MathUtil.convertHeadingToDegree(player.getHeading()));
		float x1 = (float) (Math.cos(Math.PI * 0 + (radian + 55)) * 7);
		float y1 = (float) (Math.sin(Math.PI * 0 + (radian + 55)) * 7);
		attacktransformedPlayer(player, (Npc) spawn(231193, (player.getX() + x1), (player.getY() + y1), player.getZ(), player.getHeading()));
	}

	public void SpawnLeft(Player player) {
		double radian = Math.toRadians(MathUtil.convertHeadingToDegree(player.getHeading()));
		float x1 = (float) (Math.cos(Math.PI * 0 + (radian - 55)) * 7);
		float y1 = (float) (Math.sin(Math.PI * 0 + (radian - 55)) * 7);
		attacktransformedPlayer(player, (Npc) spawn(231193, (player.getX() + x1), (player.getY() + y1), player.getZ(), player.getHeading()));
	}

	public void SpawnFront(Player player) {
		double radian = Math.toRadians(MathUtil.convertHeadingToDegree(player.getHeading()));
		float x1 = (float) (Math.cos(Math.PI * 0 + radian) * 5);
		float y1 = (float) (Math.sin(Math.PI * 0 + radian) * 5);
		attacktransformedPlayer(player, (Npc) spawn(231193, (player.getX() + x1), (player.getY() + y1), player.getZ(), player.getHeading()));
	}

	private void despawnNpc(Npc npc) {
		if (npc != null) {
			npc.getController().onDelete();
		}
	}

	private void attacktransformedPlayer(final Player player, final Npc npc) {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (!isInstanceDestroyed) {
					npc.setTarget(player);
					((AbstractAI) npc.getAi2()).setStateIfNot(AIState.WALKING);
					npc.setState(1);
					npc.getMoveController().moveToTargetObject();
					PacketSendUtility.broadcastPacket(npc, new SM_EMOTION(npc, EmotionType.START_EMOTE2, 0, npc.getObjectId()));
				}
			}
		}, 2000);
	}

	private void attackgenerator(final Npc generator, final Npc npc) {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (!isInstanceDestroyed) {
					npc.setTarget(generator);
					((AbstractAI) npc.getAi2()).setStateIfNot(AIState.WALKING);
					npc.setState(1);
					npc.getMoveController().moveToTargetObject();
					PacketSendUtility.broadcastPacket(npc, new SM_EMOTION(npc, EmotionType.START_EMOTE2, 0, npc.getObjectId()));
				}
			}
		}, 1000);
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
		PacketSendUtility.sendPacket(player, STR_REBIRTH_MASSAGE_ME);
		TeleportService2.teleportTo(player, mapId, instanceId, 749.6575f, 558.4166f, 572.97394f, (byte) 90);
		return true;
	}

	@Override
	public void onPlayerUsedSkill(int skillId, Player player) {
		final Npc vera = getNpc(231050);
		VisibleObject target = player.getTarget();
		if (target instanceof Npc) {
			if ((((Npc) target).getNpcId() == 231050) && skillId == 21437) {
				Dissipshield += 1;
				if (Dissipshield == 3) {
					vera.getEffectController().removeEffect(21438);
				}
			}
		}
	}
}
