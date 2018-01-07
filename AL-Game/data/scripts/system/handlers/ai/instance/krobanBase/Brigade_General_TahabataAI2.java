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
package ai.instance.krobanBase;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.world.WorldPosition;

import ai.AggressiveNpcAI2;

@AIName("brigade_general_tahabata")
public class Brigade_General_TahabataAI2 extends AggressiveNpcAI2 {

	private int phase = 0;
	private AtomicBoolean isAggred = new AtomicBoolean(false);

	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		if (isAggred.compareAndSet(false, true)) {
		}
		checkPercentage(getLifeStats().getHpPercentage());
	}

	private void checkPercentage(int hpPercentage) {
		if (hpPercentage == 90 && phase < 1) {
			phase = 1;
			fireTornado();
			startPhase();
		}
		if (hpPercentage == 50 && phase < 2) {
			phase = 2;
			fireTornado();
			startPhase();
		}
		if (hpPercentage == 20 && phase < 3) {
			phase = 3;
			fireTornado();
			startPhase();
		}
	}

	private void fireTornado() {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawn(243961, 679.88f, 1068.88f, 497.88f, (byte) 0); // IDF6_LF1_Thor_SumStatue_PhyAtk
			}
		}, 5000);
	}

	private void startPhase() {
		AI2Actions.useSkill(this, 20060); // Lava Eruption
	}

	private void startParalyze() {
		AI2Actions.useSkill(this, 20761); // Flame Terror
	}

	@SuppressWarnings("unused")
	private void schedule() {
		if (isAlreadyDead()) {
			return;
		}
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				startParalyze();
			}
		}, 10000);
	}

	@Override
	protected void handleDied() {
		final WorldPosition p = getPosition();
		if (p != null) {
			deleteNpcs(p.getWorldMapInstance().getNpcs(243961)); // IDF6_LF1_Thor_SumStatue_PhyAtk
		}
		super.handleDied();
		AI2Actions.deleteOwner(this);
	}

	private void deleteNpcs(List<Npc> npcs) {
		for (Npc npc : npcs) {
			if (npc != null) {
				npc.getController().onDelete();
			}
		}
	}
}
