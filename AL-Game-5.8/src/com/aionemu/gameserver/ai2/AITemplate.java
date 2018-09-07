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
package com.aionemu.gameserver.ai2;

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.npcshout.ShoutEventType;

/**
 * @author ATracer
 */
public abstract class AITemplate extends AbstractAI {

	@Override
	public void think() {
	}

	@Override
	public boolean canThink() {
		return true;
	}

	@Override
	protected void handleActivate() {
	}

	@Override
	protected void handleDeactivate() {
	}

	@Override
	protected void handleMoveValidate() {
	}

	@Override
	protected void handleMoveArrived() {
	}

	@Override
	protected void handleAttack(Creature creature) {
	}

	@Override
	protected boolean handleCreatureNeedsSupport(Creature creature) {
		return false;
	}

	@Override
	protected boolean handleGuardAgainstAttacker(Creature creature) {
		return false;
	}

	@Override
	protected void handleCreatureSee(Creature creature) {
	}

	@Override
	protected void handleCreatureNotSee(Creature creature) {
	}

	@Override
	protected void handleCreatureMoved(Creature creature) {
	}

	@Override
	protected void handleCreatureAggro(Creature creature) {
	}

	@Override
	protected void handleFollowMe(Creature creature) {
	}

	@Override
	protected void handleStopFollowMe(Creature creature) {
	}

	@Override
	protected void handleDialogStart(Player player) {
	}

	@Override
	protected void handleDialogFinish(Player player) {
	}

	@Override
	protected void handleCustomEvent(int eventId, Object... args) {
	}

	@Override
	protected void handleSpawned() {
	}

	@Override
	protected void handleRespawned() {
	}

	@Override
	protected void handleDespawned() {
	}

	@Override
	protected void handleDied() {
	}

	@Override
	protected void handleTargetReached() {
	}

	@Override
	protected void handleAttackComplete() {
	}

	@Override
	protected void handleFinishAttack() {
	}

	@Override
	protected void handleTargetTooFar() {
	}

	@Override
	protected void handleTargetGiveup() {
	}

	@Override
	protected void handleTargetChanged(Creature creature) {
	}

	@Override
	protected void handleNotAtHome() {
	}

	@Override
	protected void handleBackHome() {
	}

	@Override
	protected void handleDropRegistered() {
	}

	@Override
	public boolean isMayShout() {
		return false;
	}

	@Override
	public boolean onPatternShout(ShoutEventType event, String pattern, int skillNumber) {
		return false;
	}

	@Override
	public AttackIntention chooseAttackIntention() {
		return AttackIntention.SIMPLE_ATTACK;
	}
}
