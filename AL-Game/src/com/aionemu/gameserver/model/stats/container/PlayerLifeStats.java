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
package com.aionemu.gameserver.model.stats.container;

import java.util.concurrent.Future;
import java.util.concurrent.locks.ReentrantLock;

import com.aionemu.gameserver.configs.administration.AdminConfig;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.LOG;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_FLY_TIME;
import com.aionemu.gameserver.network.aion.serverpackets.SM_STATUPDATE_HP;
import com.aionemu.gameserver.network.aion.serverpackets.SM_STATUPDATE_MP;
import com.aionemu.gameserver.services.LifeStatsRestoreService;
import com.aionemu.gameserver.taskmanager.tasks.PacketBroadcaster.BroadcastMode;
import com.aionemu.gameserver.taskmanager.tasks.TeamEffectUpdater;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author ATracer, sphinx
 */
public class PlayerLifeStats extends CreatureLifeStats<Player> {

	protected int currentFp;
	private final ReentrantLock fpLock = new ReentrantLock();
	private Future<?> flyRestoreTask;
	private Future<?> flyReduceTask;

	public PlayerLifeStats(Player owner) {
		super(owner, owner.getGameStats().getMaxHp().getCurrent(), owner.getGameStats().getMaxMp().getCurrent());
		this.currentFp = owner.getGameStats().getFlyTime().getCurrent();
	}

	@Override
	protected void onReduceHp() {
		sendHpPacketUpdate();
		triggerRestoreTask();
		sendGroupPacketUpdate();
	}

	@Override
	protected void onReduceMp() {
		sendMpPacketUpdate();
		triggerRestoreTask();
		sendGroupPacketUpdate();
	}

	@Override
	protected void onIncreaseMp(TYPE type, int value, int skillId, LOG log) {
		if (value > 0) {
			sendMpPacketUpdate();
			sendAttackStatusPacketUpdate(type, value, skillId, log);
			sendGroupPacketUpdate();
		}
	}

	@Override
	protected void onIncreaseHp(TYPE type, int value, int skillId, LOG log) {
		if (this.isFullyRestoredHp()) {
			// FIXME: Temp Fix: Reset aggro list when hp is full.
			this.owner.getAggroList().clear();
		}
		if (value > 0) {
			sendHpPacketUpdate();
			sendAttackStatusPacketUpdate(type, value, skillId, log);
			sendGroupPacketUpdate();
		}
	}

	private void sendGroupPacketUpdate() {
		if (owner.isInTeam()) {
			TeamEffectUpdater.getInstance().startTask(owner);
		}
	}

	@Override
	public void synchronizeWithMaxStats() {
		if (isAlreadyDead()) {
			return;
		}

		super.synchronizeWithMaxStats();
		int maxFp = getMaxFp();
		if (currentFp != maxFp) {
			currentFp = maxFp;
		}
	}

	@Override
	public void updateCurrentStats() {
		super.updateCurrentStats();

		if (getMaxFp() < currentFp) {
			currentFp = getMaxFp();
		}

		if (!owner.isFlying() && !owner.isInSprintMode()) {
			triggerFpRestore();
		}
	}

	public void sendHpPacketUpdate() {
		owner.addPacketBroadcastMask(BroadcastMode.UPDATE_PLAYER_HP_STAT);
	}

	public void sendHpPacketUpdateImpl() {
		PacketSendUtility.sendPacket(owner, new SM_STATUPDATE_HP(currentHp, getMaxHp()));
	}

	public void sendMpPacketUpdate() {
		owner.addPacketBroadcastMask(BroadcastMode.UPDATE_PLAYER_MP_STAT);
	}

	public void sendMpPacketUpdateImpl() {
		PacketSendUtility.sendPacket(owner, new SM_STATUPDATE_MP(currentMp, getMaxMp()));
	}

	/**
	 * @return the currentFp
	 */
	@Override
	public int getCurrentFp() {
		return this.currentFp;
	}

	@Override
	public int getMaxFp() {
		return owner.getGameStats().getFlyTime().getCurrent();
	}

	/**
	 * @return FP percentage 0 - 100
	 */
	public int getFpPercentage() {
		return 100 * currentFp / getMaxFp();
	}

	/**
	 * This method is called whenever caller wants to restore creatures's FP
	 *
	 * @param value
	 * @return
	 */
	@Override
	public int increaseFp(TYPE type, int value) {
		return this.increaseFp(type, value, 0, LOG.REGULAR);
	}

	public int increaseFp(TYPE type, int value, int skillId, LOG log) {
		fpLock.lock();

		try {
			if (isAlreadyDead()) {
				return 0;
			}
			int newFp = this.currentFp + value;
			if (newFp > getMaxFp()) {
				newFp = getMaxFp();
			}
			if (currentFp != newFp) {
				onIncreaseFp(type, newFp - currentFp, skillId, log);
				this.currentFp = newFp;
			}
		}
		finally {
			fpLock.unlock();
		}

		return currentFp;

	}

	/**
	 * This method is called whenever caller wants to reduce creatures's MP
	 *
	 * @param value
	 * @return
	 */
	public int reduceFp(int value) {
		fpLock.lock();
		try {
			int newFp = this.currentFp - value;

			if (newFp < 0) {
				newFp = 0;
			}

			this.currentFp = newFp;
		}
		finally {
			fpLock.unlock();
		}

		onReduceFp();

		return currentFp;
	}

	public int setCurrentFp(int value) {
		fpLock.lock();
		try {
			int newFp = value;

			if (newFp < 0) {
				newFp = 0;
			}

			this.currentFp = newFp;
		}
		finally {
			fpLock.unlock();
		}

		onReduceFp();

		return currentFp;
	}

	protected void onIncreaseFp(TYPE type, int value, int skillId, LOG log) {
		if (value > 0) {
			sendAttackStatusPacketUpdate(type, value, skillId, log);
			owner.addPacketBroadcastMask(BroadcastMode.UPDATE_PLAYER_FLY_TIME);
		}
	}

	protected void onReduceFp() {
		owner.addPacketBroadcastMask(BroadcastMode.UPDATE_PLAYER_FLY_TIME);
	}

	public void sendFpPacketUpdateImpl() {
		if (owner == null) {
			return;
		}

		PacketSendUtility.sendPacket(owner, new SM_FLY_TIME(currentFp, getMaxFp()));
	}

	/**
	 * this method should be used only on FlyTimeRestoreService
	 */
	public void restoreFp() {
		// how much fly time restoring per 1 second.
		increaseFp(TYPE.NATURAL_FP, 1);
	}

	public void specialrestoreFp() {
		if (owner.getGameStats().getStat(StatEnum.REGEN_FP, 0).getCurrent() != 0) {
			increaseFp(TYPE.NATURAL_FP, owner.getGameStats().getStat(StatEnum.REGEN_FP, 0).getCurrent() / 3);
		}
	}

	public void triggerFpRestore() {
		cancelFpReduce();

		restoreLock.lock();
		try {
			if (flyRestoreTask == null && !alreadyDead && !isFlyTimeFullyRestored()) {
				this.flyRestoreTask = LifeStatsRestoreService.getInstance().scheduleFpRestoreTask(this);
			}
		}
		finally {
			restoreLock.unlock();
		}
	}

	public void cancelFpRestore() {
		restoreLock.lock();
		try {
			if (flyRestoreTask != null && !flyRestoreTask.isCancelled()) {
				flyRestoreTask.cancel(false);
				this.flyRestoreTask = null;
			}
		}
		finally {
			restoreLock.unlock();
		}
	}

	public void triggerFpReduceByCost(Integer costFp) {
		triggerFpReduce(costFp);
	}

	public void triggerFpReduce() {
		triggerFpReduce(null);
	}

	private void triggerFpReduce(Integer costFp) {
		cancelFpRestore();
		restoreLock.lock();
		try {
			if (flyReduceTask == null && !alreadyDead && owner.getAccessLevel() < AdminConfig.GM_FLIGHT_UNLIMITED && !owner.isUnderNoFPConsum()) {
				this.flyReduceTask = LifeStatsRestoreService.getInstance().scheduleFpReduceTask(this, costFp);
			}
		}
		finally {
			restoreLock.unlock();
		}
	}

	public void cancelFpReduce() {
		restoreLock.lock();
		try {
			if (flyReduceTask != null && !flyReduceTask.isCancelled()) {
				flyReduceTask.cancel(false);
				this.flyReduceTask = null;
			}
		}
		finally {
			restoreLock.unlock();
		}
	}

	public boolean isFlyTimeFullyRestored() {
		return getMaxFp() == currentFp;
	}

	@Override
	public void cancelAllTasks() {
		super.cancelAllTasks();
		cancelFpReduce();
		cancelFpRestore();
	}

	public void triggerRestoreOnRevive() {
		this.triggerRestoreTask();
		triggerFpRestore();
	}
}
