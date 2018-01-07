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
package ai.instance.runatorium;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.ai2.AISubState;
import com.aionemu.gameserver.ai2.AttackIntention;
import com.aionemu.gameserver.ai2.event.AIEventType;
import com.aionemu.gameserver.ai2.handler.TargetEventHandler;
import com.aionemu.gameserver.ai2.manager.EmoteManager;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.actions.CreatureActions;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.WorldPosition;
import com.aionemu.gameserver.world.knownlist.Visitor;

import ai.AggressiveNpcAI2;

/**
 * @author GiGatR00n v4.7.5.x
 */
@AIName("destroyer_kunax")
// 234190
public class DestroyerKunaxAI2 extends AggressiveNpcAI2 {

	private Future<?> BossTasks;
	private int attackCounter = 0;
	private AtomicBoolean isBossAggro = new AtomicBoolean(true);
	private boolean isBossOutsideCircle = false;
	private int NextBossRoar = (100 / 10) * 9;
	private boolean canThink = true;

	@Override
	public boolean canThink() {
		return canThink;
	}

	@Override
	protected void handleTargetGiveup() {
		TargetEventHandler.onTargetGiveup(this);

		// The Destroyer Kunax will stop focusing its target and return to its original position.
		sendMsgByRace(1402578, Race.PC_ALL, 0);
	}

	@Override
	protected void handleDeactivate() {
	}

	@Override
	protected void handleFinishAttack() {
		// super.handleFinishAttack(); //It causes the Boss' HP Resets to full if there are not any attackers near the boss
	}

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		makeBossStronger();
		BUFF_IdeScale(); /* Added Ide Scale BUFF to the "Destroyer Kunax" */
		checkBossLocation();
	}

	@Override
	protected void handleDespawned() {
		cancelBossTask();
		super.handleDespawned();
	}

	@Override
	protected void handleBackHome() {
		isBossOutsideCircle = false;
		canThink = true;
		super.handleBackHome();
	}

	@Override
	protected void handleMoveArrived() {
		isBossOutsideCircle = false;
		canThink = true;
		super.handleMoveArrived();
	}

	@Override
	protected void handleAttack(Creature creature) {

		super.handleAttack(creature);
		if (isBossAggro.compareAndSet(true, false)) {
			callForHelp(10);
		}
		checkPercentage(getLifeStats().getHpPercentage());
	}

	@Override
	protected void handleDied() {
		cancelBossTask();
		/* Elite NPCs */
		deleteSummons(234751);// Sheban Elite Stalwart.
		deleteSummons(234752);// Sheban Elite Sniper.
		deleteSummons(234753);// Sheban Elite Marauder.
		deleteSummons(234754);// Sheban Elite Medic.
		/* Normal NPCs */
		deleteSummons(234186);// Sheban Intelligence Unit Ridgeblade
		deleteSummons(234187);// Sheban Intelligence Unit Hunter
		deleteSummons(234188);// Sheban Intelligent Unit Mongrel
		deleteSummons(234189);// Sheban Intelligence Unit Stitch
		super.handleDied();
		AI2Actions.deleteOwner(this);
	}

	private void makeBossStronger() {
		getOwner().getLifeStats().increaseHp(TYPE.HP, 500000);
		getOwner().getObjectTemplate().setAggroRange(20);
		getOwner().getObjectTemplate().setAttackRange(5);
	}

	// private int getSkillAccordingToTarget() {
	//
	// double distance = MathUtil.getDistance(getOwner(), getTarget());
	//
	//
	//
	//
	//
	// }

	private boolean canAttack() {
		return !(getOwner().isCasting() || isAlreadyDead() || isBossOutsideCircle || getOwner().getTarget() == null || isInSubState(AISubState.CAST) || attackCounter > 20);
	}

	private boolean chooseBossSkill() {
		if (!canAttack()) {
			return false;
		}
		// if (getOwner().isCasting() || isAlreadyDead() || isBossOutsideCircle || getOwner().getTarget() == null || isInSubState(AISubState.CAST) || attackCounter > 20) {
		// return false;
		// }

		/* Choose Attack Skills */
		int rand = Rnd.get(1, 29);
		/* Choose Support Skills */
		if (getLifeStats().getHpPercentage() <= NextBossRoar) {
			rand = Rnd.get(30, 31);
			NextBossRoar = getLifeStats().getHpPercentage() - (100 / 10);
			NextBossRoar = (NextBossRoar > 0) ? NextBossRoar : 0;
		}

		switch (rand) {
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
				AggressiveShot();// 37m
				break;
			case 6:
			case 7:
			case 8:
				SlaughteringCleave();// 26m
				break;
			case 9:
			case 10:
			case 11:
				Onslaught();// 26m
				break;
			case 12:
			case 13:
				AetherPrison();// 26m
				break;
			case 14:
			case 15:
			case 16:
				CleavingMassacre();// 26m
				break;
			case 17:
			case 18:
				AerialConfinement();// 20m
				break;
			case 19:
			case 20:
			case 21:
			case 22:
			case 23:
			case 24:
			case 25:
				BloodyCrash();// 5m
				break;
			case 26:
			case 27:
			case 28:
			case 29:
				ButcherSweep();// 5m
				break;
			case 30:
			case 31:
				SpawnKunaxWrathBomb();// 2m
				FierceRoar();// 20m
				break;
		}

		// the skill was selected successfully
		return true;
	}

	/*************************************
	 * Destroyer Kunax Skill's AI
	 */
	private void BUFF_IdeScale() {
		// BUFF = Ide Scale (21744) = IDLDF5_Fortress_Re_MainBuff_PD
		applyEffect(21744, getOwner());
	}

	private void AggressiveShot() {
		// SKILL = AggressiveShot (21651) = IDLDF5_Fortress_Re_NorSatkLong_Nr
		// useSkill(21651, getOwner().getTarget());
		skillId = 21651;
		skillLevel = 65;
	}

	private void SlaughteringCleave() {
		// SKILL = SlaughteringCleave (21551) = IDLDF5_Fortress_Re_WingBlade_TA
		// useSkill(21551, getOwner().getTarget());
		skillId = 21551;
		skillLevel = 65;
	}

	private void Onslaught() {
		// SKILL = Onslaught (21552) = IDLDF5_Fortress_Re_SatkLong_Strong
		// useSkill(21552, getOwner().getTarget());
		skillId = 21552;
		skillLevel = 65;
	}

	private void AetherPrison() {
		// SKILL = AetherPrison (21558) = STR_IDLDF5_Fortress_Re_ParaShield_TA
		// useSkill(21558, getOwner().getTarget());
		skillId = 21558;
		skillLevel = 65;
	}

	private void CleavingMassacre() {
		// SKILL = CleavingMassacre (21553) = IDLDF5_Fortress_Re_WingBlade_TA_Strong
		// useSkill(21553, getOwner().getTarget());
		skillId = 21553;
		skillLevel = 65;
	}

	private void AerialConfinement() {
		// SKILL = AerialConfinement (21555) = IDLDF5_Fortress_Re_AreaOpenAerial_forACT
		// useSkill(21555, getOwner());
		skillId = 21555;
		skillLevel = 65;
		AI2Actions.targetSelf(this);
	}

	private void BloodyCrash() {
		// SKILL = BloodyCrash (21556) = IDLDF5_Fortress_Re_CloseAerialTA_Nr
		// useSkill(21556, getOwner().getTarget());
		skillId = 21556;
		skillLevel = 65;
	}

	private void FierceRoar() {
		// SKILL = FierceRoar (21561) = IDLDF5_Fortress_Re_Buff_Notfly
		// useSkill(21561, getOwner());
		skillId = 21561;
		skillLevel = 65;
		AI2Actions.targetSelf(this);
	}

	private void ButcherSweep() {
		// SKILL ButcherSweep (21554) = IDLDF5_Fortress_Re_SatkSA_Nr
		// useSkill(21554, getOwner().getTarget());
		skillId = 21554;
		skillLevel = 65;
	}

	private void AetherCrystalArmor() {
		// SKILL AetherCrystalArmor (21560) = IDLDF5_Fortress_Re_Bonus_Shield
		applyEffect(21560, getOwner());
	}

	/**
	 * Destroyer Kunax Skill's AI
	 ***********************************/

	@SuppressWarnings("unused")
	private void useSkill(int skillId, VisibleObject target) {
		SkillEngine.getInstance().getSkill(getOwner(), skillId, 65, target).useSkill();
	}

	private void applyEffect(int skillId, Creature target) {
		SkillEngine.getInstance().applyEffectDirectly(skillId, getOwner(), target, 0);
		// SkillEngine.getInstance().getSkill(getOwner(), skillId, 65, target).useSkill();
	}

	private void deleteSummons(int npcId) {
		if (getPosition().getWorldMapInstance().getNpcs(npcId) != null) {
			List<Npc> npcs = getPosition().getWorldMapInstance().getNpcs(npcId);
			for (Npc npc : npcs) {
				npc.getController().onDelete();
			}
		}
	}

	@SuppressWarnings("unused")
	private void castSkillTask(final int skillId, int time, final VisibleObject firstTarget) {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (!isAlreadyDead() && !getOwner().isCasting() && !isInSubState(AISubState.CAST)) {
					WorldPosition p = getPosition();
					if (p != null) {
						WorldMapInstance instance = p.getWorldMapInstance();
						if (instance != null) {
							SkillEngine.getInstance().getSkill(getOwner(), skillId, 65, firstTarget).useNoAnimationSkill();
						}
					}
				}
			}
		}, time);
	}

	protected void sendMsgByRace(final int msg, final Race race, int time) {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				getPosition().getWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {

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

	private void SpawnKunaxWrathBomb() {

		// Finds a random target within Destroyer Kunax's Circle
		Player player = getRndTargetInBossCircle(16.0f);
		if (player == null) {

			// If any player find, check the target
			if (getOwner().getTarget() instanceof Player) {
				player = (Player) getOwner().getTarget();
			}
			else {
				return;
			}
		}

		// Get Target Coordinates
		float x = player.getX();
		float y = player.getY();
		float z = player.getZ();

		// Spawn the Blind/Silence Bomb
		spawn(702320, x, y, z, player.getHeading());
	}

	private void cancelBossTask() {
		if (BossTasks != null && !BossTasks.isDone()) {
			BossTasks.cancel(true);
		}
	}

	private void checkBossLocation() {
		BossTasks = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				if (!isBossOutsideCircle) {
					if (!isAlreadyDead() && !getOwner().isCasting()) {
						double distance = getOwner().getDistanceToSpawnLocation();
						if (distance >= 23.01) {

							isBossOutsideCircle = true;
							Player player = getRndTargetInBossCircle(23.01f);
							getOwner().setTarget(player);

							if (player == null) {
								canThink = false;
								getOwner().getMoveController().abortMove();
								EmoteManager.emoteStopAttacking(getOwner());
								setStateIfNot(AIState.WALKING);
								setSubStateIfNot(AISubState.NONE);
								getOwner().setTarget(null);

								// The Destroyer Kunax will stop focusing its target and return to its original position.
								sendMsgByRace(1402578, Race.PC_ALL, 0);

								// Point randPoint = MathUtil.get2DPointOnCircle(new Point((int) 264.4382f, (int)258.58527f), new Point((int) getOwner().getX(), (int)getOwner().getY()), 16);
								Point randPoint = MathUtil.get2DPointInsideCircle(264.4382f, 258.58527f, 14);
								getOwner().getMoveController().moveToPoint(randPoint.x, randPoint.y, 85.81963f);
							}
						}
					}
				}
			}
		}, 100, 100);
	}

	/**
	 * Returns a Random Target that Available in <b>Destroyer Kunax's Circle</b>
	 *
	 * @return (Player)
	 */
	private Player getRndTargetInBossCircle(float CircleRadius) {
		List<Player> players = new ArrayList<Player>();
		for (Player player : getKnownList().getKnownPlayers().values()) {
			if (!CreatureActions.isAlreadyDead(player) && isInBossCircle(player, CircleRadius)) {
				players.add(player);
			}
		}
		if (players.isEmpty()) {
			return null;
		}
		return players.get(Rnd.get(players.size()));
	}

	private boolean isInBossCircle(Creature creature, float CircleRadius) {
		if (MathUtil.isInSphere(creature, 264.4382f, 258.58527f, 85.81963f, CircleRadius)) {
			return true;
		}
		return false;
	}

	private synchronized void checkPercentage(int hpPercentage) {
		attackCounter += 1;
		if (!getOwner().isCasting() && !isAlreadyDead() && !isBossOutsideCircle && !isInSubState(AISubState.CAST) && attackCounter > 20) {
			attackCounter = 0;
			if (hpPercentage <= 25) {
				AetherCrystalArmor();
			}
			else if (hpPercentage <= 50) {
				AetherCrystalArmor();
			}
			else if (hpPercentage <= 85) {
				AetherCrystalArmor();
			}
		}
	}

	@Override
	public AttackIntention chooseAttackIntention() {

		VisibleObject currentTarget = getTarget();
		Creature mostHated = getAggroList().getMostHated();

		if (mostHated == null || mostHated.getLifeStats().isAlreadyDead()) {
			return AttackIntention.FINISH_ATTACK;
		}

		if (currentTarget == null || !currentTarget.getObjectId().equals(mostHated.getObjectId())) {
			onCreatureEvent(AIEventType.TARGET_CHANGED, mostHated);
			return AttackIntention.SWITCH_TARGET;
		}

		int rand = Rnd.get(1, 3);
		if (rand == 2) {
			if (chooseBossSkill()) {
				return AttackIntention.SKILL_ATTACK;
			}
		}

		/*
		 * Specifies whether the Target is in Ground or Air? The Z-axis difference between Boss and Player is "ToleranceZ" 0 = Ground Attacks 1 = Air Attacks (v4.7.5.17)
		 */
		float zBossCoord = getOwner().getZ();
		float zTargetCoord = getOwner().getTarget().getZ();
		float ToleranceZ = 1.605784f + 2f;
		if (Math.abs(zBossCoord - zTargetCoord) > ToleranceZ) {
			getOwner().getController().setSimpleAttackType(1); // Do Air Simple Attacks
		}
		else {
			getOwner().getController().setSimpleAttackType(0); // Do Ground Simple Attacks
		}

		return AttackIntention.SIMPLE_ATTACK;
	}
}
