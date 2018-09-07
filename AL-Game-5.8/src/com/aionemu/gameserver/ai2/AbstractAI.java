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

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.aionemu.commons.callbacks.metadata.ObjectCallback;
import com.aionemu.gameserver.ai2.event.AIEventLog;
import com.aionemu.gameserver.ai2.event.AIEventType;
import com.aionemu.gameserver.ai2.eventcallback.OnHandleAIGeneralEvent;
import com.aionemu.gameserver.ai2.handler.FollowEventHandler;
import com.aionemu.gameserver.ai2.handler.FreezeEventHandler;
import com.aionemu.gameserver.ai2.manager.SimpleAttackManager;
import com.aionemu.gameserver.ai2.manager.WalkManager;
import com.aionemu.gameserver.ai2.poll.AIAnswer;
import com.aionemu.gameserver.ai2.poll.AIAnswers;
import com.aionemu.gameserver.ai2.poll.AIQuestion;
import com.aionemu.gameserver.ai2.scenario.AI2Scenario;
import com.aionemu.gameserver.ai2.scenario.AI2Scenarios;
import com.aionemu.gameserver.configs.main.AIConfig;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.item.ItemAttackType;
import com.aionemu.gameserver.model.templates.npcshout.ShoutEventType;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.world.WorldPosition;
import com.google.common.base.Preconditions;

/**
 * @author ATracer
 */
public abstract class AbstractAI implements AI2 {

	private Creature owner;
	private AIState currentState;
	private AISubState currentSubState;
	private final Lock thinkLock = new ReentrantLock();
	private boolean logging = false;
	protected int skillId;
	protected int skillLevel;
	private volatile AIEventLog eventLog;
	private AI2Scenario scenario;

	AbstractAI() {
		this.currentState = AIState.CREATED;
		this.currentSubState = AISubState.NONE;
		clearScenario();
	}

	public AI2Scenario getScenario() {
		return scenario;
	}

	public void setScenario(AI2Scenario scenario) {
		this.scenario = scenario;
	}

	public void clearScenario() {
		this.scenario = AI2Scenarios.NO_SCENARIO;
	}

	public AIEventLog getEventLog() {
		return eventLog;
	}

	@Override
	public AIState getState() {
		return currentState;
	}

	public final boolean isInState(AIState state) {
		return currentState == state;
	}

	@Override
	public AISubState getSubState() {
		return currentSubState;
	}

	public final boolean isInSubState(AISubState subState) {
		return currentSubState == subState;
	}

	@Override
	public String getName() {
		if (getClass().isAnnotationPresent(AIName.class)) {
			AIName annotation = getClass().getAnnotation(AIName.class);
			return annotation.value();
		}
		return "noname";
	}

	public int getSkillId() {
		return skillId;
	}

	public int getSkillLevel() {
		return skillLevel;
	}

	protected boolean canHandleEvent(AIEventType eventType) {
		switch (this.currentState) {
			case DESPAWNED:
				return StateEvents.DESPAWN_EVENTS.hasEvent(eventType);
			case DIED:
				return StateEvents.DEAD_EVENTS.hasEvent(eventType);
			case CREATED:
				return StateEvents.CREATED_EVENTS.hasEvent(eventType);
			default:
				break;
		}
		switch (eventType) {
			case DIALOG_START:
			case DIALOG_FINISH:
				return isNonFightingState();
			case CREATURE_MOVED:
				return getName().equals("trap") || currentState != AIState.FIGHT && isNonFightingState();
			default:
				break;
		}
		return true;
	}

	public boolean isNonFightingState() {
		return currentState == AIState.WALKING || currentState == AIState.IDLE;
	}

	public synchronized boolean setStateIfNot(AIState newState) {
		if (this.currentState == newState) {
			if (this.isLogging()) {
				AI2Logger.info(this, "Can't change state to " + newState + " from " + currentState);
			}
			return false;
		}
		if (this.isLogging()) {
			AI2Logger.info(this, "Setting AI state to " + newState);
			if (this.currentState == AIState.DIED && newState == AIState.FIGHT) {
				StackTraceElement[] stack = new Throwable().getStackTrace();
				for (StackTraceElement elem : stack) {
					AI2Logger.info(this, elem.toString());
				}
			}
		}
		this.currentState = newState;
		return true;
	}

	public synchronized boolean setSubStateIfNot(AISubState newSubState) {
		if (this.currentSubState == newSubState) {
			if (this.isLogging()) {
				AI2Logger.info(this, "Can't change substate to " + newSubState + " from " + currentSubState);
			}
			return false;
		}
		if (this.isLogging()) {
			AI2Logger.info(this, "Setting AI substate to " + newSubState);
		}
		this.currentSubState = newSubState;
		return true;
	}

	@Override
	public void onGeneralEvent(AIEventType event) {
		if (canHandleEvent(event)) {
			if (this.isLogging()) {
				AI2Logger.info(this, "General event " + event);
			}
			handleGeneralEvent(event);
		}
	}

	@Override
	public void onCreatureEvent(AIEventType event, Creature creature) {
		Preconditions.checkNotNull(creature, "Creature must not be null");
		if (canHandleEvent(event)) {
			if (this.isLogging()) {
				AI2Logger.info(this, "Creature event " + event + ": " + creature.getObjectTemplate().getTemplateId());
			}
			handleCreatureEvent(event, creature);
		}
	}

	@Override
	public void onCustomEvent(int eventId, Object... args) {
		if (this.isLogging()) {
			AI2Logger.info(this, "Custom event - id = " + eventId);
		}
		handleCustomEvent(eventId, args);
	}

	/**
	 * Will be hidden for all AI's below NpcAI2
	 *
	 * @return
	 */
	public Creature getOwner() {
		return owner;
	}

	public int getObjectId() {
		return owner.getObjectId();
	}

	public WorldPosition getPosition() {
		return owner.getPosition();
	}

	public VisibleObject getTarget() {
		return owner.getTarget();
	}

	public boolean isAlreadyDead() {
		return owner.getLifeStats().isAlreadyDead();
	}

	void setOwner(Creature owner) {
		this.owner = owner;
	}

	public final boolean tryLockThink() {
		return thinkLock.tryLock();
	}

	public final void unlockThink() {
		thinkLock.unlock();
	}

	@Override
	public final boolean isLogging() {
		return logging;
	}

	public void setLogging(boolean logging) {
		this.logging = logging;
	}

	protected abstract void handleActivate();

	protected abstract void handleDeactivate();

	protected abstract void handleSpawned();

	protected abstract void handleRespawned();

	protected abstract void handleDespawned();

	protected abstract void handleDied();

	protected abstract void handleMoveValidate();

	protected abstract void handleMoveArrived();

	protected abstract void handleAttackComplete();

	protected abstract void handleFinishAttack();

	protected abstract void handleTargetReached();

	protected abstract void handleTargetTooFar();

	protected abstract void handleTargetGiveup();

	protected abstract void handleNotAtHome();

	protected abstract void handleBackHome();

	protected abstract void handleDropRegistered();

	protected abstract void handleAttack(Creature creature);

	protected abstract boolean handleCreatureNeedsSupport(Creature creature);

	protected abstract boolean handleGuardAgainstAttacker(Creature creature);

	protected abstract void handleCreatureSee(Creature creature);

	protected abstract void handleCreatureNotSee(Creature creature);

	protected abstract void handleCreatureMoved(Creature creature);

	protected abstract void handleCreatureAggro(Creature creature);

	protected abstract void handleTargetChanged(Creature creature);

	protected abstract void handleFollowMe(Creature creature);

	protected abstract void handleStopFollowMe(Creature creature);

	protected abstract void handleDialogStart(Player player);

	protected abstract void handleDialogFinish(Player player);

	protected abstract void handleCustomEvent(int eventId, Object... args);

	public abstract boolean onPatternShout(ShoutEventType event, String pattern, int skillNumber);

	@ObjectCallback(OnHandleAIGeneralEvent.class)
	protected void handleGeneralEvent(AIEventType event) {
		if (this.isLogging()) {
			AI2Logger.info(this, "Handle general event " + event);
		}
		logEvent(event);
		switch (event) {
			case MOVE_VALIDATE:
				handleMoveValidate();
				break;
			case MOVE_ARRIVED:
				handleMoveArrived();
				break;
			case SPAWNED:
				handleSpawned();
				break;
			case RESPAWNED:
				handleRespawned();
				break;
			case DESPAWNED:
				handleDespawned();
				break;
			case DIED:
				handleDied();
				break;
			case ATTACK_COMPLETE:
				handleAttackComplete();
				break;
			case ATTACK_FINISH:
				handleFinishAttack();
				break;
			case TARGET_REACHED:
				handleTargetReached();
				break;
			case TARGET_TOOFAR:
				handleTargetTooFar();
				break;
			case TARGET_GIVEUP:
				handleTargetGiveup();
				break;
			case NOT_AT_HOME:
				handleNotAtHome();
				break;
			case BACK_HOME:
				handleBackHome();
				break;
			case ACTIVATE:
				handleActivate();
				break;
			case DEACTIVATE:
				handleDeactivate();
				break;
			case FREEZE:
				FreezeEventHandler.onFreeze(this);
				break;
			case UNFREEZE:
				FreezeEventHandler.onUnfreeze(this);
				break;
			case DROP_REGISTERED:
				handleDropRegistered();
				break;
			default:
				break;
		}
	}

	/**
	 * @param event
	 */
	protected void logEvent(AIEventType event) {
		if (AIConfig.EVENT_DEBUG) {
			if (eventLog == null) {
				synchronized (this) {
					if (eventLog == null) {
						eventLog = new AIEventLog(10);
					}
				}
			}
			eventLog.addFirst(event);
		}
	}

	void handleCreatureEvent(AIEventType event, Creature creature) {
		switch (event) {
			case ATTACK:
				if (DataManager.TRIBE_RELATIONS_DATA.isFriendlyRelation(getOwner().getTribe(), creature.getTribe())) {
					return;
				}
				handleAttack(creature);
				logEvent(event);
				break;
			case CREATURE_NEEDS_SUPPORT:
				if (!handleCreatureNeedsSupport(creature)) {
					if (creature.getTarget() instanceof Creature) {
						if (!handleCreatureNeedsSupport((Creature) creature.getTarget()) && !handleGuardAgainstAttacker(creature)) {
							handleGuardAgainstAttacker((Creature) creature.getTarget());
						}
					}
				}
				logEvent(event);
				break;
			case CREATURE_SEE:
				handleCreatureSee(creature);
				break;
			case CREATURE_NOT_SEE:
				handleCreatureNotSee(creature);
				break;
			case CREATURE_MOVED:
				handleCreatureMoved(creature);
				break;
			case CREATURE_AGGRO:
				handleCreatureAggro(creature);
				logEvent(event);
				break;
			case TARGET_CHANGED:
				handleTargetChanged(creature);
				break;
			case FOLLOW_ME:
				handleFollowMe(creature);
				logEvent(event);
				break;
			case STOP_FOLLOW_ME:
				handleStopFollowMe(creature);
				logEvent(event);
				break;
			case DIALOG_START:
				handleDialogStart((Player) creature);
				logEvent(event);
				break;
			case DIALOG_FINISH:
				handleDialogFinish((Player) creature);
				logEvent(event);
				break;
			default:
				break;
		}
	}

	@Override
	public boolean poll(AIQuestion question) {
		AIAnswer instanceAnswer = pollInstance(question);
		if (instanceAnswer != null) {
			return instanceAnswer.isPositive();
		}
		switch (question) {
			case DESTINATION_REACHED:
				return isDestinationReached();
			case CAN_SPAWN_ON_DAYTIME_CHANGE:
				return isCanSpawnOnDaytimeChange();
			case CAN_SHOUT:
				return isMayShout();
			default:
				break;
		}
		return false;
	}

	/**
	 * Poll concrete AI instance for the answer.
	 *
	 * @param question
	 * @return null if there is no specific answer
	 */
	protected AIAnswer pollInstance(AIQuestion question) {
		return null;
	}

	@Override
	public AIAnswer ask(AIQuestion question) {
		return AIAnswers.NEGATIVE;
	}

	// TODO move to NPC ai
	protected boolean isDestinationReached() {
		AIState state = currentState;
		switch (state) {
			case FEAR:
				return MathUtil.isNearCoordinates(getOwner(), owner.getMoveController().getTargetX2(), owner.getMoveController().getTargetY2(), owner.getMoveController().getTargetZ2(), 1);
			case FIGHT:
				return SimpleAttackManager.isTargetInAttackRange((Npc) owner);
			case RETURNING:
				SpawnTemplate spawn = getOwner().getSpawn();
				return MathUtil.isNearCoordinates(getOwner(), spawn.getX(), spawn.getY(), spawn.getZ(), 1);
			case FOLLOWING:
				return FollowEventHandler.isInRange(this, getOwner().getTarget());
			case WALKING:
				return currentSubState == AISubState.TALK || WalkManager.isArrivedAtPoint((NpcAI2) this);
			default:
				break;
		}
		return true;
	}

	protected boolean isCanSpawnOnDaytimeChange() {
		return currentState == AIState.DESPAWNED || currentState == AIState.CREATED;
	}

	public abstract boolean isMayShout();

	public abstract AttackIntention chooseAttackIntention();

	@Override
	public boolean onDialogSelect(Player player, int dialogId, int questId, int extendedRewardIndex) {
		return false;
	}

	@Override
	public long getRemainigTime() {
		return 0;
	}

	/**
	 * Spawn object in the same world and instance as AI's owner
	 */
	protected VisibleObject spawn(int npcId, float x, float y, float z, byte heading) {
		return spawn(owner.getWorldId(), npcId, x, y, z, heading, 0, getPosition().getInstanceId());
	}

	/**
	 * Spawn object with staticId in the same world and instance as AI's owner
	 */
	protected VisibleObject spawn(int npcId, float x, float y, float z, byte heading, int staticId) {
		return spawn(owner.getWorldId(), npcId, x, y, z, heading, staticId, getPosition().getInstanceId());
	}

	protected VisibleObject spawn(int worldId, int npcId, float x, float y, float z, byte heading, int staticId, int instanceId) {
		SpawnTemplate template = SpawnEngine.addNewSingleTimeSpawn(worldId, npcId, x, y, z, heading);
		template.setStaticId(staticId);
		return SpawnEngine.spawnObject(template, instanceId);
	}

	@Override
	public int modifyDamage(int damage) {
		return damage;
	}

	@Override
	public int modifyOwnerDamage(int damage) {
		return damage;
	}

	@Override
	public void onIndividualNpcEvent(Creature npc) {
	}

	@Override
	public int modifyHealValue(int value) {
		return value;
	}

	@Override
	public int modifyMaccuracy(int value) {
		return value;
	}

	@Override
	public ItemAttackType modifyAttackType(ItemAttackType type) {
		return type;
	}

	@Override
	public int modifyARange(int value) {
		return value;
	}
}
