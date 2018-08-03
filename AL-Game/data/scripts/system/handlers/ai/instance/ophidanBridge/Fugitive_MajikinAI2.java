/*
 * This file is part of aion-lightning <aion-lightning.com>.
 *
 *  aion-lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-lightning.  If not, see <http://www.gnu.org/licenses/>.
 */
package ai.instance.ophidanBridge;

import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import ai.GeneralNpcAI2;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.manager.WalkManager;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.NpcShoutsService;
import com.aionemu.gameserver.utils.PacketSendUtility;

@AIName("fugitive_majikin")// 235780
public class Fugitive_MajikinAI2 extends GeneralNpcAI2 {

	private Future<?> mazikinTask;
	private boolean canThink = true;
	private AtomicBoolean isAggred = new AtomicBoolean(false);
	private AtomicBoolean startedEvent = new AtomicBoolean(false);

	@Override
	public boolean canThink() {
		return canThink;
	}

	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		if (isAggred.compareAndSet(false, true)) {
			switch (getNpcId()) {
				case 235759: // Fugitive Mazikin Leader.
					// The fugitive will get away in 8 minutes!
					NpcShoutsService.getInstance().sendMsg(getOwner(), 1402860, 0);
					// The fugitive will get away in 4 minutes!
					NpcShoutsService.getInstance().sendMsg(getOwner(), 1402861, 240000);
					// The fugitive will get away in 1 minute!
					NpcShoutsService.getInstance().sendMsg(getOwner(), 1402862, 420000);
					mazikinTask = ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							AI2Actions.deleteOwner(Fugitive_MajikinAI2.this);
						}
					}, 480000);
					break;
			}
		}
	}

	@Override
	protected void handleCreatureMoved(Creature creature) {
		super.handleCreatureMoved(creature);
		if (getNpcId() == 235780 && isInRange(creature, 15) && creature instanceof Player) {
			if (startedEvent.compareAndSet(false, true)) {
				canThink = false;
				spawn(235756, 673.1745f, 465.6297f, 599.5734f, (byte) 1);
				// Fugitive Mazikin is attempting to flee to the next sentry post. Pursue the fugitive.
				PacketSendUtility.npcSendPacketTime(getOwner(), SM_SYSTEM_MESSAGE.STR_MSG_IDLDF5_U_01_RA_Wi_Start, 0);
				getOwner().getMoveController().moveToPoint(754.0731f, 487.0167f, 584.6798f);
				WalkManager.startWalking(this);
				getOwner().setState(1);
				PacketSendUtility.broadcastPacket(getOwner(), new SM_EMOTION(getOwner(), EmotionType.START_EMOTE2, 0, getObjectId()));
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						if (!isAlreadyDead()) {
							despawn();
						}
					}
				}, 6000);
			}
		}
		else if (getNpcId() == 235756 && isInRange(creature, 15) && creature instanceof Player) {
			if (startedEvent.compareAndSet(false, true)) {
				switch (Rnd.get(1, 2)) {
					case 1:
						spawn(235757, 524.93365f, 437.3637f, 620.2102f, (byte) 114);
						// Fugitive Mazikin is attempting to flee to one of the two sentry posts. Follow the fugitive to continue the pursuit.
						PacketSendUtility.npcSendPacketTime(getOwner(), SM_SYSTEM_MESSAGE.STR_MSG_IDLDF5_U_01_RA_Wi_Point_01, 0);
						getOwner().getMoveController().moveToPoint(653.04736f, 437.94086f, 603.3876f);
						break;
					case 2:
						spawn(235787, 606.0667f, 556.59625f, 590.5f, (byte) 105);
						// Fugitive Mazikin is attempting to flee to one of the two sentry posts. Follow the fugitive to continue the pursuit.
						PacketSendUtility.npcSendPacketTime(getOwner(), SM_SYSTEM_MESSAGE.STR_MSG_IDLDF5_U_01_RA_Wi_Point_01, 0);
						getOwner().getMoveController().moveToPoint(673.487f, 510.5843f, 596.98785f);
						break;
				}
				canThink = false;
				WalkManager.startWalking(this);
				getOwner().setState(1);
				PacketSendUtility.broadcastPacket(getOwner(), new SM_EMOTION(getOwner(), EmotionType.START_EMOTE2, 0, getObjectId()));
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						if (!isAlreadyDead()) {
							despawn();
						}
					}
				}, 6000);
			}
		}
		else if (getNpcId() == 235757 && isInRange(creature, 15) && creature instanceof Player) {
			if (startedEvent.compareAndSet(false, true)) {
				spawn(235758, 479.04312f, 528.6785f, 597.375f, (byte) 9);
				// Fugitive Mazikin is attempting to flee to the next sentry post. Pursue the fugitive.
				PacketSendUtility.npcSendPacketTime(getOwner(), SM_SYSTEM_MESSAGE.STR_MSG_IDLDF5_U_01_RA_Wi_Point_02, 0);
				getOwner().getMoveController().moveToPoint(537.80994f, 477.11905f, 615.0119f);
				canThink = false;
				WalkManager.startWalking(this);
				getOwner().setState(1);
				PacketSendUtility.broadcastPacket(getOwner(), new SM_EMOTION(getOwner(), EmotionType.START_EMOTE2, 0, getObjectId()));
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						if (!isAlreadyDead()) {
							despawn();
						}
					}
				}, 6000);
			}
		}
		else if (getNpcId() == 235787 && isInRange(creature, 15) && creature instanceof Player) {
			if (startedEvent.compareAndSet(false, true)) {
				spawn(235758, 479.04312f, 528.6785f, 597.375f, (byte) 9);
				// Fugitive Mazikin is attempting to flee to the next sentry post. Pursue the fugitive.
				PacketSendUtility.npcSendPacketTime(getOwner(), SM_SYSTEM_MESSAGE.STR_MSG_IDLDF5_U_01_RA_As_Point_02, 0);
				getOwner().getMoveController().moveToPoint(574.60815f, 565.70074f, 595.37823f);
				canThink = false;
				WalkManager.startWalking(this);
				getOwner().setState(1);
				PacketSendUtility.broadcastPacket(getOwner(), new SM_EMOTION(getOwner(), EmotionType.START_EMOTE2, 0, getObjectId()));
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						if (!isAlreadyDead()) {
							despawn();
						}
					}
				}, 6000);
			}
		}
		else if (getNpcId() == 235758 && isInRange(creature, 15) && creature instanceof Player) {
			if (startedEvent.compareAndSet(false, true)) {
				spawn(235759, 329.5612f, 489.43942f, 607.64343f, (byte) 1);
				// Fugitive Mazikin is attempting to flee using teleport. Pursue the fugitive.
				PacketSendUtility.npcSendPacketTime(getOwner(), SM_SYSTEM_MESSAGE.STR_MSG_IDLDF5_U_01_RA_Wi_Point_03, 0);
				getOwner().getMoveController().moveToPoint(462.4221f, 499.83743f, 597.72363f);
				canThink = false;
				WalkManager.startWalking(this);
				getOwner().setState(1);
				PacketSendUtility.broadcastPacket(getOwner(), new SM_EMOTION(getOwner(), EmotionType.START_EMOTE2, 0, getObjectId()));
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						if (!isAlreadyDead()) {
							despawn();
						}
					}
				}, 6000);
			}
		}
	}

	private void cancelMazikinTask() {
		if (mazikinTask != null && !mazikinTask.isDone()) {
			mazikinTask.cancel(true);
		}
	}

	@Override
	protected void handleDied() {
		cancelMazikinTask();
		super.handleDied();
	}

	private void despawn() {
		AI2Actions.deleteOwner(this);
	}
}
