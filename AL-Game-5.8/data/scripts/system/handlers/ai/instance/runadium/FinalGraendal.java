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
package ai.instance.runadium;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.NpcShoutsService;
import com.aionemu.gameserver.skillengine.SkillEngine;

import ai.AggressiveNpcAI2;

/**
 * @rework Everlight
 */
@AIName("finalgraendal")
public class FinalGraendal extends AggressiveNpcAI2 {

	private List<Integer> percents = new ArrayList<Integer>();

	@Override
	protected void handleSpawned() {
		NpcShoutsService.getInstance().sendMsg(getOwner(), 1500741, getObjectId(), 0, 1000);
		addPercent();
		super.handleSpawned();
	}

	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		checkPercentage(getLifeStats().getHpPercentage());
	}

	private void checkPercentage(int hpPercentage) {
		if (hpPercentage > 95 && percents.size() < 7) {
			addPercent();
		}
		for (Integer percent : percents) {
			if (hpPercentage <= percent) {
				switch (percent) {
					case 95:
						skill2();
						break;
					case 80:
						shout1();
						skill3();
						break;
					case 60:
						shout_spawn();
						spawn_support();
						break;
					case 51:
						shout_mass();
						skill_mass_active();
						break;
					case 50:
						skill_mass_destroy();
						break;
					case 49:
						skill4();
						break;
					case 40:
						shout2();
						skill1();
						break;
					case 30:
						shout_spawn();
						spawn_support2();
						skill2();
						break;
					case 10:
						shout_spawn();
						spawn_support3();
						skill3();
						break;
					case 5:
						shout_died();
						break;
				}
				percents.remove(percent);
				break;
			}
		}
	}

	private void addPercent() {
		percents.clear();
		Collections.addAll(percents, new Integer[] { 95, 80, 60, 51, 50, 40, 30, 10, 5 });
	}

	private void skill1() {
		VisibleObject target = getTarget();
		if (target != null && target instanceof Player) {
			SkillEngine.getInstance().getSkill(getOwner(), 21172, 65, target).useNoAnimationSkill(); // Grendel's Explosive Temper
		}
	}

	private void skill2() {
		VisibleObject target = getTarget();
		if (target != null && target instanceof Player) {
			SkillEngine.getInstance().getSkill(getOwner(), 21171, 65, target).useNoAnimationSkill(); // Grendel's Explosive Temper
		}
	}

	private void skill3() {
		VisibleObject target = getTarget();
		if (target != null && target instanceof Player) {
			SkillEngine.getInstance().getSkill(getOwner(), 21172, 65, target).useNoAnimationSkill();
		}
	}

	private void skill4() {
		VisibleObject target = getTarget();
		if (target != null && target instanceof Player) {
			SkillEngine.getInstance().getSkill(getOwner(), 21172, 65, target).useNoAnimationSkill();
		}
	}

	private void skill_mass_active() {
		VisibleObject target = getTarget();
		if (target != null && target instanceof Player) {
			SkillEngine.getInstance().getSkill(getOwner(), 21177, 65, target).useNoAnimationSkill(); // Vengeful Orb
		}
	}

	private void skill_mass_destroy() {
		VisibleObject target = getTarget();
		if (target != null && target instanceof Player) {
			SkillEngine.getInstance().getSkill(getOwner(), 21178, 65, target).useNoAnimationSkill(); // Vengeful Orb
		}
	}

	private void shout1() {
		NpcShoutsService.getInstance().sendMsg(getOwner(), 1500740, getObjectId(), 0, 1000);
	}

	private void shout2() {
		NpcShoutsService.getInstance().sendMsg(getOwner(), 1500745, getObjectId(), 0, 1000);
	}

	private void shout_mass() {
		NpcShoutsService.getInstance().sendMsg(getOwner(), 1500748, getObjectId(), 0, 1000);
	}

	private void shout_spawn() {
		NpcShoutsService.getInstance().sendMsg(getOwner(), 1500750, getObjectId(), 0, 1000);
	}

	private void shout_died() {
		NpcShoutsService.getInstance().sendMsg(getOwner(), 1500751, getObjectId(), 0, 0);
		NpcShoutsService.getInstance().sendMsg(getOwner(), 1500752, getObjectId(), 0, 8000);
	}

	private void spawn_support() {
		spawn(284659, 264.5786f, 249.9355f, 241.8923f, (byte) 45); // weakened reliquary jotun
		spawn(284660, 248.6734f, 265.8675f, 241.8990f, (byte) 106); // weakened idean lapilima
	}

	private void spawn_support2() {
		spawn(284661, 264.6672f, 265.9347f, 241.8658f, (byte) 90); // weakened idean obscura
		spawn(284662, 248.3278f, 249.7112f, 241.8719f, (byte) 16); // weakened modor guardian
	}

	private void spawn_support3() {
		spawn(284663, 264.5786f, 249.9355f, 241.8923f, (byte) 45); // weakened danuar ghost
		spawn(284661, 264.6672f, 265.9347f, 241.8658f, (byte) 90); // weakened idean obscura
		spawn(284662, 248.3278f, 249.7112f, 241.8719f, (byte) 16); // weakened modor guardian
	}

	@Override
	protected void handleDespawned() {
		percents.clear();
		super.handleDespawned();
	}

	@Override
	protected void handleDied() {
		percents.clear();
		super.handleDied();
	}

	@Override
	protected void handleBackHome() {
		addPercent();
		super.handleBackHome();
	}
}
