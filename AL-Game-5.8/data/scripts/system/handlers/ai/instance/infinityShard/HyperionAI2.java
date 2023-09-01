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
package ai.instance.infinityShard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;

import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.handler.AggroEventHandler;
import com.aionemu.gameserver.ai2.handler.CreatureEventHandler;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldMapInstance;

import ai.GeneralNpcAI2;

/**
 * @author nightm
 * @reworked Himiko
 */
@AIName("hyperion")
public class HyperionAI2 extends GeneralNpcAI2 {

	private Future<?> Cast;
	private int castc = 0;

	protected List<Integer> percents = new ArrayList<Integer>();

	@Override
	protected void handleCreatureSee(Creature creature) {
		CreatureEventHandler.onCreatureSee(this, creature);
	}

	@Override
	protected void handleCreatureAggro(Creature creature) {
		if (canThink())
			AggroEventHandler.onAggro(this, creature);

	}

	@Override
	protected boolean handleGuardAgainstAttacker(Creature attacker) {
		return AggroEventHandler.onGuardAgainstAttacker(this, attacker);
	}

	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		checkPercentage(getLifeStats().getHpPercentage());
	}

	private void addPercent() {
		percents.clear();
		Collections.addAll(percents, new Integer[] { 100, 85, 80, 70, 60, 55, 54, 50, 46, 45, 40, 35, 25, 23, 20, 18, 10, 8, 5, 3 });
	}

	private synchronized void checkPercentage(int hpPercentage) {
		for (Integer percent : percents) {
			if (hpPercentage <= percent) {
				switch (percent) {
					case 100:
						StartCast();
						break;
					case 85:
						castc = 1;
						StartCast();
						break;
					case 80:
						SkillEngine.getInstance().getSkill(getOwner(), 21253, 60, getOwner()).useNoAnimationSkill();
						FirstWave();
						break;
					case 70:
						castc = 1;
						StartCast2();
						FirstWave();
						break;
					case 60:
						castc = 0;
						StartCast3();
						SecondWave();
						break;
					case 55:
						castc = 0;
						StartCast4();
						SecondWave();
						break;
					case 54:
						SkillEngine.getInstance().getSkill(getOwner(), 21244, 60, getOwner()).useNoAnimationSkill();
						break;
					case 50:
						castc = 0;
						StartCast4();
						SecondWave();
						break;
					case 46:
						SkillEngine.getInstance().getSkill(getOwner(), 21244, 60, getOwner()).useNoAnimationSkill();
						break;
					case 45:
						castc = 1;
						StartCast5();
						SecondWave();
						break;
					case 40:
						SkillEngine.getInstance().getSkill(getOwner(), 21248, 60, getOwner()).useNoAnimationSkill();
						TurrendWave();
						break;
					case 35:
						castc = 1;
						StartCast2();
						break;
					case 25:
						castc = 1;
						StartCast5();
						ThirdWave();
						break;
					case 23:
						SkillEngine.getInstance().getSkill(getOwner(), 21244, 60, getOwner()).useNoAnimationSkill();
						break;
					case 20:
						castc = 0;
						StartCast4();
						ThirdWave();
						break;
					case 18:
						SkillEngine.getInstance().getSkill(getOwner(), 21244, 60, getOwner()).useNoAnimationSkill();
						break;
					case 10:
						SkillEngine.getInstance().getSkill(getOwner(), 21246, 60, getOwner()).useNoAnimationSkill();
						break;
					case 8:
						SkillEngine.getInstance().getSkill(getOwner(), 21249, 60, getOwner()).useNoAnimationSkill();
						break;
					case 5:
						castc = 0;
						StartCast4();
						ThirdWave();
						break;
					case 3:
						SkillEngine.getInstance().getSkill(getOwner(), 21249, 60, getOwner()).useNoAnimationSkill();
						break;
				}
				percents.remove(percent);
				break;
			}
		}
	}

	private void StartCast() {

		Cast = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				if (castc >= 3)
					Cast.cancel(false);
				Cast1();
				castc++;
			}
		}, 1 * 1000 * 1, 5 * 1000 * 1);

	}

	private void Cast1() {
		SkillEngine.getInstance().getSkill(getOwner(), 21241, 60, getOwner()).useNoAnimationSkill();
	}

	private void StartCast2() {

		Cast = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				if (castc >= 3)
					Cast.cancel(false);
				Cast2();
				castc++;
			}
		}, 1 * 1000 * 1, 3 * 1000 * 1);

	}

	private void Cast2() {
		switch (castc) {
			case 1:
				SkillEngine.getInstance().getSkill(getOwner(), 21250, 60, getOwner()).useNoAnimationSkill();
				break;
			case 2:
				SkillEngine.getInstance().getSkill(getOwner(), 21251, 60, getOwner()).useNoAnimationSkill();
				break;
		}
	}

	private void StartCast3() {

		Cast = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				if (castc >= 4)
					Cast.cancel(false);
				Cast3();
				castc++;
			}
		}, 1 * 1000 * 1, 3 * 1000 * 1);

	}

	private void Cast3() {
		switch (castc) {
			case 0:
				SkillEngine.getInstance().getSkill(getOwner(), 21250, 60, getOwner()).useNoAnimationSkill();
				break;
			case 1:
				SkillEngine.getInstance().getSkill(getOwner(), 21251, 60, getOwner()).useNoAnimationSkill();
				break;
			case 2:
				SkillEngine.getInstance().getSkill(getOwner(), 21245, 60, getOwner()).useNoAnimationSkill();
				break;
			case 3:
				SkillEngine.getInstance().getSkill(getOwner(), 21253, 60, getOwner()).useNoAnimationSkill();
				break;
		}
	}

	private void StartCast4() {

		Cast = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				if (castc >= 3)
					Cast.cancel(false);
				Cast4();
				castc++;
			}
		}, 1 * 1000 * 1, 3 * 1000 * 1);

	}

	private void Cast4() {
		switch (castc) {
			case 0:
				SkillEngine.getInstance().getSkill(getOwner(), 21250, 60, getOwner()).useNoAnimationSkill();
				break;
			case 1:
				SkillEngine.getInstance().getSkill(getOwner(), 21251, 60, getOwner()).useNoAnimationSkill();
				break;
			case 2:
				SkillEngine.getInstance().getSkill(getOwner(), 21253, 60, getOwner()).useNoAnimationSkill();
				break;
		}
	}

	private void StartCast5() {

		Cast = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				if (castc >= 3)
					Cast.cancel(false);
				Cast5();
				castc++;
			}
		}, 1 * 1000 * 1, 5 * 1000 * 1);

	}

	private void Cast5() {
		switch (castc) {
			case 1:
				AI2Actions.useSkill(this, 21241);
				break;
			case 2:
				AI2Actions.useSkill(this, 21245);
				break;
		}
	}

	private void FirstWave() {

		spawn(231096, 150.62f, 133.64f, 125.69f, (byte) 0);
		spawn(231097, 132.35f, 159.08f, 119.38f, (byte) 0);
		spawn(231098, 119.91f, 117.47f, 121.10f, (byte) 0);
		spawn(233297, 120.59f, 160.32f, 128.57f, (byte) 0);
		spawn(233298, 108.39f, 143.94f, 125.69f, (byte) 0);

	}

	private void SecondWave() {

		spawn(231096, 150.62f, 133.64f, 125.69f, (byte) 0);
		spawn(231097, 132.35f, 159.08f, 119.38f, (byte) 0);
		spawn(231098, 119.91f, 117.47f, 121.10f, (byte) 0);
		spawn(233297, 120.59f, 160.32f, 128.57f, (byte) 0);
		spawn(233298, 108.39f, 143.94f, 125.69f, (byte) 0);
		spawn(233299, 108.59f, 130.80f, 124.46f, (byte) 0);
		spawn(231103, 123.109f, 145.36f, 112.12f, (byte) 0);
		spawn(231103, 123.26f, 130.70f, 112.17f, (byte) 0);
	}

	private void TurrendWave() {
		spawn(231102, 107.53553f, 142.51953f, 127.03997f, (byte) 0);
		spawn(231102, 113.86417f, 154.06656f, 127.68255f, (byte) 110);
		spawn(231102, 144.52719f, 122.26577f, 127.44639f, (byte) 45);
		spawn(231102, 150.33377f, 132.67754f, 126.57981f, (byte) 50);
	}

	private void ThirdWave() {

		spawn(231096, 150.62f, 133.64f, 125.69f, (byte) 0);
		spawn(231097, 132.35f, 159.08f, 119.38f, (byte) 0);
		spawn(231098, 119.91f, 117.47f, 121.10f, (byte) 0);
		spawn(233297, 120.59f, 160.32f, 128.57f, (byte) 0);
		spawn(233298, 108.39f, 143.94f, 125.69f, (byte) 0);
		spawn(233299, 108.59f, 130.80f, 124.46f, (byte) 0);
		spawn(231103, 123.109f, 145.36f, 112.12f, (byte) 0);
		spawn(231103, 123.26f, 130.70f, 112.17f, (byte) 0);
		spawn(231103, 135.74f, 129.67f, 112.17f, (byte) 0);
		spawn(231103, 139.07f, 142.46f, 112.17f, (byte) 0);
		spawn(231103, 135.26f, 117.27f, 116.74f, (byte) 0);
	}

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		addPercent();
	}

	@Override
	protected void handleDespawned() {
		super.handleDespawned();
		percents.clear();
		despawnAdds();
	}

	private void deleteNpcs(List<Npc> npcs) {
		for (Npc npc : npcs) {
			if (npc != null) {
				npc.getController().onDelete();
			}
		}
	}

	private void despawnAdds() {
		WorldMapInstance instance = getPosition().getWorldMapInstance();
		deleteNpcs(instance.getNpcs(231096));
		deleteNpcs(instance.getNpcs(231097));
		deleteNpcs(instance.getNpcs(231098));
		deleteNpcs(instance.getNpcs(231103));
		deleteNpcs(instance.getNpcs(231104));
		deleteNpcs(instance.getNpcs(233288));
		deleteNpcs(instance.getNpcs(233289));
		deleteNpcs(instance.getNpcs(233292));
		deleteNpcs(instance.getNpcs(233294));
		deleteNpcs(instance.getNpcs(233295));
		deleteNpcs(instance.getNpcs(233296));
		deleteNpcs(instance.getNpcs(233297));
		deleteNpcs(instance.getNpcs(233298));
		deleteNpcs(instance.getNpcs(233299));
	}

	@Override
	protected void handleDied() {
		percents.clear();
		if (Cast != null) {
			Cast.cancel(false);
		}
		super.handleDied();
		despawnAdds();
	}
}
