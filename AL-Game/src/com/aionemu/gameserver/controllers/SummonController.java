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
package com.aionemu.gameserver.controllers;

import org.apache.commons.lang.NullArgumentException;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Summon;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.summons.SummonMode;
import com.aionemu.gameserver.model.summons.UnsummonType;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.LOG;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SUMMON_UPDATE;
import com.aionemu.gameserver.restrictions.RestrictionsManager;
import com.aionemu.gameserver.services.summons.SummonsService;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.skillengine.model.Skill;
import com.aionemu.gameserver.taskmanager.tasks.PlayerMoveTaskManager;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/**
 * @author ATracer
 * @author RotO (Attack-speed hack protection) modified by Sippolo
 */
public class SummonController extends CreatureController<Summon> {

	private long lastAttackMilis = 0;
	private boolean isAttacked = false;
	private int releaseAfterSkill = -1;

	@Override
	public void notSee(VisibleObject object, boolean isOutOfRange) {
		super.notSee(object, isOutOfRange);
		if (getOwner().getMaster() == null) {
			return;
		}

		if (object.getObjectId() == getOwner().getMaster().getObjectId()) {
			SummonsService.release(getOwner(), UnsummonType.DISTANCE, isAttacked);
		}
	}

	/**
	 * Release summon
	 */
	public void release(final UnsummonType unsummonType) {
		SummonsService.release(getOwner(), unsummonType, isAttacked);
	}

	@Override
	public Summon getOwner() {
		return (Summon) super.getOwner();
	}

	/**
	 * Change to rest mode
	 */
	public void restMode() {
		SummonsService.restMode(getOwner());
	}

	public void setUnkMode() {
		SummonsService.setUnkMode(getOwner());
	}

	/**
	 * Change to guard mode
	 */
	public void guardMode() {
		SummonsService.guardMode(getOwner());
	}

	/**
	 * Change to attackMode
	 */
	public void attackMode(int targetObjId) {
		VisibleObject obj = getOwner().getKnownList().getObject(targetObjId);
		if (obj != null && obj instanceof Creature) {
			SummonsService.attackMode(getOwner());
		}
	}

	@Override
	public void attackTarget(Creature target, int time) {
		Player master = getOwner().getMaster();

		if (!RestrictionsManager.canAttack(master, target)) {
			return;
		}

		int attackSpeed = getOwner().getGameStats().getAttackSpeed().getCurrent();
		long milis = System.currentTimeMillis();
		if (milis - lastAttackMilis < attackSpeed) {
			/**
			 * Hack!
			 */
			return;
		}
		lastAttackMilis = milis;
		super.attackTarget(target, time);
	}

	@Override
	public void onAttack(Creature creature, int skillId, TYPE type, int damage, boolean notifyAttack, LOG log) {
		if (getOwner().getLifeStats().isAlreadyDead()) {
			return;
		}

		// temp
		if (getOwner().getMode() == SummonMode.RELEASE) {
			return;
		}

		super.onAttack(creature, skillId, type, damage, notifyAttack, log);
		PacketSendUtility.broadcastPacket(getOwner(), new SM_ATTACK_STATUS(getOwner(), creature, TYPE.REGULAR, 0, damage, log));
		PacketSendUtility.sendPacket(getOwner().getMaster(), new SM_SUMMON_UPDATE(getOwner()));
	}

	@Override
	public void onDie(final Creature lastAttacker) {
		if (lastAttacker == null) {
			throw new NullArgumentException("lastAttacker");
		}
		super.onDie(lastAttacker);
		SummonsService.release(getOwner(), UnsummonType.UNSPECIFIED, isAttacked);
		Summon owner = getOwner();
		final Player master = getOwner().getMaster();
		PacketSendUtility.broadcastPacket(owner, new SM_EMOTION(owner, EmotionType.DIE, 0, lastAttacker.equals(owner) ? 0 : lastAttacker.getObjectId()));

		if (!master.equals(lastAttacker) && !owner.equals(lastAttacker) && !master.getLifeStats().isAlreadyDead() && !lastAttacker.getLifeStats().isAlreadyDead()) {
			ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					lastAttacker.getAggroList().addHate(master, 1);
				}
			}, 1000);
		}
	}

	public void useSkill(int skillId, Creature target) {
		Creature creature = getOwner();
		boolean petHasSkill = DataManager.PET_SKILL_DATA.petHasSkill(getOwner().getObjectTemplate().getTemplateId(), skillId);
		if (!petHasSkill) {
			// hackers!)
			return;
		}
		Skill skill = SkillEngine.getInstance().getSkill(creature, skillId, 1, target);
		if (skill != null) {
			// If skill succeeds, handle automatic release if expected
			if (skill.useSkill() && skillId == releaseAfterSkill) {
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						SummonsService.release(getOwner(), UnsummonType.UNSPECIFIED, isAttacked);
					}
				}, 1000);
			}
			setReleaseAfterSkill(-1);
		}
	}

	/**
	 * Handle automatic release if Ultra Skill demands it
	 *
	 * @param is
	 *            the skill commanded by summoner, after which pet is automatically dismissed
	 */
	public void setReleaseAfterSkill(int skillId) {
		this.releaseAfterSkill = skillId;
	}

	@Override
	public void onStartMove() {
		super.onStartMove();
		getOwner().getMoveController().setInMove(true);
		getOwner().getObserveController().notifyMoveObservers();
		PlayerMoveTaskManager.getInstance().addPlayer(getOwner());
	}

	@Override
	public void onStopMove() {
		super.onStopMove();
		PlayerMoveTaskManager.getInstance().removePlayer(getOwner());
		getOwner().getObserveController().notifyMoveObservers();
		getOwner().getMoveController().setInMove(false);
	}

	@Override
	public void onMove() {
		getOwner().getObserveController().notifyMoveObservers();
		super.onMove();
	}

	protected Player getMaster() {
		return getOwner().getMaster();
	}
}
