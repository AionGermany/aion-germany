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
@AIName("illusion_graendal")
public class IllusionGraendal extends AggressiveNpcAI2 {

	private List<Integer> percents = new ArrayList<Integer>();

	@Override
	protected void handleSpawned() {
		NpcShoutsService.getInstance().sendMsg(getOwner(), 1500744, getObjectId(), 0, 3000);
		addPercent();
		super.handleSpawned();
	}

	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		checkPercentage(getLifeStats().getHpPercentage());
	}

	private void checkPercentage(int hpPercentage) {
		if (hpPercentage > 80 && percents.size() < 7) {
			addPercent();
		}
		for (Integer percent : percents) {
			if (hpPercentage <= percent) {
				switch (percent) {
					case 40:
						shout_mass();
						skill_mass_active();
						break;
					case 41:
						skill_mass_destroy();
						break;
					case 20:
						shout_spawn();
						spawn_support();
						break;
				}
				percents.remove(percent);
				break;
			}
		}
	}

	private void addPercent() {
		percents.clear();
		Collections.addAll(percents, new Integer[] { 40, 41, 20 });
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

	private void shout_mass() {
		NpcShoutsService.getInstance().sendMsg(getOwner(), 1500748, getObjectId(), 0, 1000);
	}

	private void shout_spawn() {
		NpcShoutsService.getInstance().sendMsg(getOwner(), 1500750, getObjectId(), 0, 1000);
	}

	private void spawn_support() {
		spawn(284379, 264.6672f, 265.9347f, 241.8658f, (byte) 90); // idean obscura
		spawn(284380, 264.5786f, 249.9355f, 241.8923f, (byte) 45); // modor's bodyguard
		spawn(284381, 264.6672f, 265.9347f, 241.8658f, (byte) 90); // vengeful reaper
		spawn(284382, 248.3278f, 249.7112f, 241.8719f, (byte) 16); // hoarfrost acheron drake
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
