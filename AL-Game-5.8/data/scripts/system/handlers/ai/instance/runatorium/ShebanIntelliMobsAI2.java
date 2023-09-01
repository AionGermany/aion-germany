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

import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.skillengine.SkillEngine;

import ai.AggressiveNpcAI2;

/**
 * @author GiGatR00n v4.7.5.x
 */
@AIName("sheban_intelligence_mobs")
public class ShebanIntelliMobsAI2 extends AggressiveNpcAI2 {

	private AtomicBoolean isAggrod = new AtomicBoolean(false);
	private int NextHeal = (100 / 3) * 2;
	private int NextAttackBuff = (100 / 3) * 2;
	private Future<?> SkillTasks;

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		int rand = Rnd.get(1, 3);
		switch (getNpcId()) {
			case 234189:// Sheban Intelligent Unit Stitch
			case 234754:// Sheban Elite Medic
				/* There is 1/3 chance to get useful BUFF */
				if (rand == 1) {
					apllyEffect(17358, getOwner());// Circle of Steel (BUFF)
				}
				else if (rand == 2) {
					apllyEffect(20700, getOwner());// Midnight Robe (PHYSICAL_DEFENSE, MAGICAL_DEFEND += 200)
				}
				break;

			default:
				/* There is 1/3 chance to get useful BUFF */
				if (rand == 2) {
					apllyEffect(20700, getOwner());// Midnight Robe (PHYSICAL_DEFENSE, MAGICAL_DEFEND += 200)
				}
		}
	}

	@Override
	protected void handleDespawned() {
		cancelSkillTask();
		super.handleDespawned();
	}

	@Override
	protected void handleBackHome() {
		cancelSkillTask();
		isAggrod.set(false);
		super.handleBackHome();
	}

	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		if (isAggrod.compareAndSet(false, true)) {
			startSkillTask();
			callForHelp(10);
		}
	}

	@Override
	protected void handleDied() {
		cancelSkillTask();
		super.handleDied();
	}

	private void chooseRandomEvent() {
		int npcId = getNpcId();
		switch (npcId) {
			case 234188:// Sheban Intelligent Unit Mongrel
			case 234753:// Sheban Elite Marauder
				Mongrel_MarauderSkills();
				break;

			case 234186:// Sheban Intelligent Unit Ridgeblade
			case 234751:// Sheban Elite Stalwart
				Ridgeblade_StalwartSkills();
				break;

			case 234189:// Sheban Intelligent Unit Stitch
			case 234754:// Sheban Elite Medic
				Stitch_MedicSkills();
				break;

			case 234187:// Sheban Intelligent Unit Hunter
			case 234752:// Sheban Elite Sniper
				Hunter_SniperSkills();
				break;
		}
	}

	/**
	 * Select Random Skills for: Sheban Intelligent Unit Mongrel Sheban Elite Marauder
	 */
	private void Mongrel_MarauderSkills() {
		if (getOwner().isCasting() || isAlreadyDead()) {
			return;
		}
		int rand = Rnd.get(1, 14);
		switch (rand) {
			case 1:
				useSkill(17320);// Frontal Strike
				break;
			case 2:
				useSkill(17321);// Surprise Attack
				break;
			case 3:
				useSkill(17322);// Poison Slash
				break;
			case 4:
				useSkill(17323);// Wide Area Poison Slash
				break;
			case 5:
				useSkill(17324);// Aerial Thrust
				break;
			case 6:
				useSkill(17325);// Wide Area Aerial Thrust
				break;
			case 7:
				useSkill(17326);// Fall
				break;
			case 8:
				useSkill(17327);// Wide Area Crashing Wind Strike
				break;
			case 9:
				useSkill(17328);// Wide Area Dragon Rune Carve I
				break;
			case 10:
				useSkill(17329);// Wide Area Dragon Rune Carve II
				break;
			case 11:
				useSkill(17330);// Wide Area Dragon Rune Carve III
				break;
			case 12:
				useSkill(17331);// Pain Rune
				break;
			case 13:
				useSkill(17332);// Swift Edge
				break;
			case 14:
				useSkill(17333);// Wide Area Swift Edge
				break;
		}
	}

	/**
	 * Select Random Skills for: Sheban Intelligent Unit Ridgeblade Sheban Elite Stalwart
	 */
	private void Ridgeblade_StalwartSkills() {
		if (getOwner().isCasting() || isAlreadyDead()) {
			return;
		}

		/* Choose Attack Skills */
		int rand = Rnd.get(1, 16);
		/* Choose Support Skills */
		if (getLifeStats().getHpPercentage() <= NextAttackBuff) {
			rand = Rnd.get(17, 19);
			NextAttackBuff = getLifeStats().getHpPercentage() - (100 / 3);
			NextAttackBuff = (NextAttackBuff > 0) ? NextAttackBuff : 0;
		}

		switch (rand) {
			case 1:
				useSkill(17294);// Ferocious Strike I
				break;
			case 2:
				useSkill(17295);// Ferocious Strike II
				break;
			case 3:
				useSkill(17296);// Ferocious Strike III
				break;
			case 4:
				useSkill(17297);// Wide Area Ferocious Strike I
				break;
			case 5:
				useSkill(17298);// Wide Area Ferocious Strike II
				break;
			case 6:
				useSkill(17299);// Wide Area Ferocious Strike III
				break;
			case 7:
				useSkill(17300);// Cleave Armor
				break;
			case 8:
				useSkill(17301);// Wide Area Cleave Armor
				break;
			case 9:
				useSkill(17302);// Pull
				break;
			case 10:
				useSkill(17303);// Wide Area Pull
				break;
			case 11:
				useSkill(17304);// Wrathful Strike
				break;
			case 12:
				useSkill(17305);// Wide Area Wrathful Strike
				break;
			case 13:
				useSkill(17306);// Crippling Cut
				break;
			case 14:
				useSkill(17307);// Wide Area Crippling Cut
				break;
			case 15:
				useSkill(17311);// Shield Strike
				break;
			case 16:
				useSkill(17312);// Massive Shield Strike
				break;
			case 17:
				apllyEffect(17308, getOwner());// Rage (BUFF)
				break;
			case 18:
				apllyEffect(17309, getOwner());// Rage_Mid (BUFF)
				break;
			case 19:
				apllyEffect(17310, getOwner());// Rage_High (BUFF)
				break;
		}
	}

	/**
	 * Select Random Skills for: Sheban Intelligent Unit Stitch Sheban Elite Medic
	 */
	private void Stitch_MedicSkills() {
		if (getOwner().isCasting() || isAlreadyDead()) {
			return;
		}

		/* Choose Attack Skills */
		int rand = Rnd.get(1, 7);
		/* Choose Support Skills */
		if (getLifeStats().getHpPercentage() <= NextHeal) {
			rand = Rnd.get(8, 13);
			NextHeal = getLifeStats().getHpPercentage() - (100 / 3);
			NextHeal = (NextHeal > 0) ? NextHeal : 0;
		}

		switch (rand) {
			case 1:
				useSkill(17354);// Divine Touch
				break;
			case 2:
				useSkill(17355);// Wide Area Divine Touch
				break;
			case 3:
				useSkill(17356);// Blindness
				break;
			case 4:
				useSkill(17357);// Wide Area Blindness
				break;
			case 5:
				useSkill(17359);// Hallowed Strike
				break;
			case 6:
				useSkill(17360);// Wide Area Hallowed Strike
				break;
			case 7:
				useSkill(17367);// Wide Area Stun
				break;
			case 8:
				useSkill(17361);// Healing Light (HEAL)
				break;
			case 9:
				useSkill(17362);// Radiant Cure I (HEAL)
				break;
			case 10:
				useSkill(17363);// Light of Recovery (HEAL)
				break;
			case 11:
				useSkill(17364);// Healing Wind I (HEAL)
				break;
			case 12:
				useSkill(17365);// Healing Wind II (HEAL)
				break;
			case 13:
				useSkill(17366);// Healing Wind III (HEAL)
				break;
		}
	}

	/**
	 * Select Random Skills for: Sheban Intelligent Unit Hunter Sheban Elite Sniper
	 */
	private void Hunter_SniperSkills() {
		if (getOwner().isCasting() || isAlreadyDead()) {
			return;
		}
		int rand = Rnd.get(1, 2);
		switch (rand) {
			case 1:
				useSkill(21292);// Powerful Shot
				break;
			case 2:
				useSkill(21293);// Deadeye
				break;
		}
	}

	private void useSkill(int skillId) {
		SkillEngine.getInstance().getSkill(getOwner(), skillId, 65, getOwner().getTarget()).useSkill();
	}

	private void apllyEffect(int skillId, Creature target) {
		// SkillEngine.getInstance().applyEffectDirectly(skillId, getOwner(), target, 0);
		SkillEngine.getInstance().getSkill(getOwner(), skillId, 65, target).useSkill();
	}

	private void cancelSkillTask() {
		if (SkillTasks != null && !SkillTasks.isDone()) {
			SkillTasks.cancel(true);
		}
	}

	private void startSkillTask() {
		SkillTasks = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				if (isAlreadyDead()) {
					cancelSkillTask();
				}
				else {
					chooseRandomEvent();
				}
			}
		}, 100, 6000);
	}
}
