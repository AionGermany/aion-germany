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
package ai.instance.riftOblivion;

import java.util.List;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.MathUtil;

@AIName("IDTransform_TransRoom_01") // 245396
public class IDTransformTransRoom01AI2 extends NpcAI2 {

	@Override
	protected void handleCreatureSee(Creature creature) {
		checkDistance(this, creature);
	}

	@Override
	protected void handleCreatureMoved(Creature creature) {
		checkDistance(this, creature);
	}

	private void checkDistance(NpcAI2 ai, Creature creature) {
		if (creature instanceof Player && !creature.getLifeStats().isAlreadyDead()) {
			if (MathUtil.isIn3dRange(getOwner(), creature, 15) && creature.getLevel() == 66) {
				IDTransformTransRoom01_66();
			}
			else if (MathUtil.isIn3dRange(getOwner(), creature, 15) && creature.getLevel() == 67) {
				IDTransformTransRoom01_67();
			}
			else if (MathUtil.isIn3dRange(getOwner(), creature, 15) && creature.getLevel() == 68) {
				IDTransformTransRoom01_68();
			}
			else if (MathUtil.isIn3dRange(getOwner(), creature, 15) && creature.getLevel() == 69) {
				IDTransformTransRoom01_69();
			}
			else if (MathUtil.isIn3dRange(getOwner(), creature, 15) && creature.getLevel() == 70) {
				IDTransformTransRoom01_70();
			}
			else if (MathUtil.isIn3dRange(getOwner(), creature, 15) && creature.getLevel() == 71) {
				IDTransformTransRoom01_71();
			}
			else if (MathUtil.isIn3dRange(getOwner(), creature, 15) && creature.getLevel() == 72) {
				IDTransformTransRoom01_72();
			}
			else if (MathUtil.isIn3dRange(getOwner(), creature, 15) && creature.getLevel() == 73) {
				IDTransformTransRoom01_73();
			}
			else if (MathUtil.isIn3dRange(getOwner(), creature, 15) && creature.getLevel() == 74) {
				IDTransformTransRoom01_74();
			}
			else if (MathUtil.isIn3dRange(getOwner(), creature, 15) && creature.getLevel() >= 75 && creature.getLevel() <= 83) {
				IDTransformTransRoom01_75_83();
			}
		}
	}

	// Player Lvl 66
	private void IDTransformTransRoom01_66() {
		// Shadow A.
		despawnNpc(245869);
		despawnNpc(245870);
		despawnNpc(245871);
		despawnNpc(245872);
		despawnNpc(245419);
		despawnNpc(245420);
		despawnNpc(245421);
		despawnNpc(245422);
		spawn(245402, 762.08215f, 514.16248f, 346.31735f, (byte) 0, 18);
		AI2Actions.deleteOwner(IDTransformTransRoom01AI2.this);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawn(244454, 758.6982f, 516.9711f, 339.81848f, (byte) 100);
				spawn(244455, 763.16156f, 510.3873f, 339.81848f, (byte) 41);
				spawn(244456, 764.7666f, 516.008f, 339.81848f, (byte) 71);
				spawn(244457, 757.1974f, 511.36588f, 339.81848f, (byte) 11);
			}
		}, 2000);
	}

	// Player Lvl 67
	private void IDTransformTransRoom01_67() {
		// Shadow A.
		despawnNpc(245869);
		despawnNpc(245870);
		despawnNpc(245871);
		despawnNpc(245872);
		despawnNpc(245419);
		despawnNpc(245420);
		despawnNpc(245421);
		despawnNpc(245422);
		spawn(245402, 762.08215f, 514.16248f, 346.31735f, (byte) 0, 18);
		AI2Actions.deleteOwner(IDTransformTransRoom01AI2.this);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawn(244495, 758.6982f, 516.9711f, 339.81848f, (byte) 100);
				spawn(244496, 763.16156f, 510.3873f, 339.81848f, (byte) 41);
				spawn(244497, 764.7666f, 516.008f, 339.81848f, (byte) 71);
				spawn(244498, 757.1974f, 511.36588f, 339.81848f, (byte) 11);
			}
		}, 2000);
	}

	// Player Lvl 68
	private void IDTransformTransRoom01_68() {
		// Shadow A.
		despawnNpc(245869);
		despawnNpc(245870);
		despawnNpc(245871);
		despawnNpc(245872);
		despawnNpc(245419);
		despawnNpc(245420);
		despawnNpc(245421);
		despawnNpc(245422);
		spawn(245402, 762.08215f, 514.16248f, 346.31735f, (byte) 0, 18);
		AI2Actions.deleteOwner(IDTransformTransRoom01AI2.this);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawn(244536, 758.6982f, 516.9711f, 339.81848f, (byte) 100);
				spawn(244537, 763.16156f, 510.3873f, 339.81848f, (byte) 41);
				spawn(244538, 764.7666f, 516.008f, 339.81848f, (byte) 71);
				spawn(244539, 757.1974f, 511.36588f, 339.81848f, (byte) 11);
			}
		}, 2000);
	}

	// Player Lvl 69
	private void IDTransformTransRoom01_69() {
		// Shadow A.
		despawnNpc(245869);
		despawnNpc(245870);
		despawnNpc(245871);
		despawnNpc(245872);
		despawnNpc(245419);
		despawnNpc(245420);
		despawnNpc(245421);
		despawnNpc(245422);
		spawn(245402, 762.08215f, 514.16248f, 346.31735f, (byte) 0, 18);
		AI2Actions.deleteOwner(IDTransformTransRoom01AI2.this);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawn(244577, 758.6982f, 516.9711f, 339.81848f, (byte) 100);
				spawn(244578, 763.16156f, 510.3873f, 339.81848f, (byte) 41);
				spawn(244579, 764.7666f, 516.008f, 339.81848f, (byte) 71);
				spawn(244580, 757.1974f, 511.36588f, 339.81848f, (byte) 11);
			}
		}, 2000);
	}

	// Player Lvl 70
	private void IDTransformTransRoom01_70() {
		// Shadow A.
		despawnNpc(245869);
		despawnNpc(245870);
		despawnNpc(245871);
		despawnNpc(245872);
		despawnNpc(245419);
		despawnNpc(245420);
		despawnNpc(245421);
		despawnNpc(245422);
		spawn(245402, 762.08215f, 514.16248f, 346.31735f, (byte) 0, 18);
		AI2Actions.deleteOwner(IDTransformTransRoom01AI2.this);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawn(244618, 758.6982f, 516.9711f, 339.81848f, (byte) 100);
				spawn(244619, 763.16156f, 510.3873f, 339.81848f, (byte) 41);
				spawn(244620, 764.7666f, 516.008f, 339.81848f, (byte) 71);
				spawn(244621, 757.1974f, 511.36588f, 339.81848f, (byte) 11);
			}
		}, 2000);
	}

	// Player Lvl 71
	private void IDTransformTransRoom01_71() {
		// Shadow A.
		despawnNpc(245869);
		despawnNpc(245870);
		despawnNpc(245871);
		despawnNpc(245872);
		despawnNpc(245419);
		despawnNpc(245420);
		despawnNpc(245421);
		despawnNpc(245422);
		spawn(245402, 762.08215f, 514.16248f, 346.31735f, (byte) 0, 18);
		AI2Actions.deleteOwner(IDTransformTransRoom01AI2.this);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawn(244659, 758.6982f, 516.9711f, 339.81848f, (byte) 100);
				spawn(244660, 763.16156f, 510.3873f, 339.81848f, (byte) 41);
				spawn(244661, 764.7666f, 516.008f, 339.81848f, (byte) 71);
				spawn(244662, 757.1974f, 511.36588f, 339.81848f, (byte) 11);
			}
		}, 2000);
	}

	// Player Lvl 72
	private void IDTransformTransRoom01_72() {
		// Shadow A.
		despawnNpc(245869);
		despawnNpc(245870);
		despawnNpc(245871);
		despawnNpc(245872);
		despawnNpc(245419);
		despawnNpc(245420);
		despawnNpc(245421);
		despawnNpc(245422);
		spawn(245402, 762.08215f, 514.16248f, 346.31735f, (byte) 0, 18);
		AI2Actions.deleteOwner(IDTransformTransRoom01AI2.this);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawn(244700, 758.6982f, 516.9711f, 339.81848f, (byte) 100);
				spawn(244701, 763.16156f, 510.3873f, 339.81848f, (byte) 41);
				spawn(244702, 764.7666f, 516.008f, 339.81848f, (byte) 71);
				spawn(244703, 757.1974f, 511.36588f, 339.81848f, (byte) 11);
			}
		}, 2000);
	}

	// Player Lvl 73
	private void IDTransformTransRoom01_73() {
		// Shadow A.
		despawnNpc(245869);
		despawnNpc(245870);
		despawnNpc(245871);
		despawnNpc(245872);
		despawnNpc(245419);
		despawnNpc(245420);
		despawnNpc(245421);
		despawnNpc(245422);
		spawn(245402, 762.08215f, 514.16248f, 346.31735f, (byte) 0, 18);
		AI2Actions.deleteOwner(IDTransformTransRoom01AI2.this);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawn(244741, 758.6982f, 516.9711f, 339.81848f, (byte) 100);
				spawn(244742, 763.16156f, 510.3873f, 339.81848f, (byte) 41);
				spawn(244743, 764.7666f, 516.008f, 339.81848f, (byte) 71);
				spawn(244744, 757.1974f, 511.36588f, 339.81848f, (byte) 11);
			}
		}, 2000);
	}

	// Player Lvl 74
	private void IDTransformTransRoom01_74() {
		// Shadow A.
		despawnNpc(245869);
		despawnNpc(245870);
		despawnNpc(245871);
		despawnNpc(245872);
		despawnNpc(245419);
		despawnNpc(245420);
		despawnNpc(245421);
		despawnNpc(245422);
		spawn(245402, 762.08215f, 514.16248f, 346.31735f, (byte) 0, 18);
		AI2Actions.deleteOwner(IDTransformTransRoom01AI2.this);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawn(244782, 758.6982f, 516.9711f, 339.81848f, (byte) 100);
				spawn(244783, 763.16156f, 510.3873f, 339.81848f, (byte) 41);
				spawn(244784, 764.7666f, 516.008f, 339.81848f, (byte) 71);
				spawn(244785, 757.1974f, 511.36588f, 339.81848f, (byte) 11);
			}
		}, 2000);
	}

	// Player Lvl 75-83
	private void IDTransformTransRoom01_75_83() {
		// Shadow A.
		despawnNpc(245869);
		despawnNpc(245870);
		despawnNpc(245871);
		despawnNpc(245872);
		despawnNpc(245419);
		despawnNpc(245420);
		despawnNpc(245421);
		despawnNpc(245422);
		spawn(245402, 762.08215f, 514.16248f, 346.31735f, (byte) 0, 18);
		AI2Actions.deleteOwner(IDTransformTransRoom01AI2.this);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawn(244823, 758.6982f, 516.9711f, 339.81848f, (byte) 100);
				spawn(244824, 763.16156f, 510.3873f, 339.81848f, (byte) 41);
				spawn(244825, 764.7666f, 516.008f, 339.81848f, (byte) 71);
				spawn(244826, 757.1974f, 511.36588f, 339.81848f, (byte) 11);
			}
		}, 2000);
	}

	private void despawnNpc(int npcId) {
		if (getPosition().getWorldMapInstance().getNpcs(npcId) != null) {
			List<Npc> npcs = getPosition().getWorldMapInstance().getNpcs(npcId);
			for (Npc npc : npcs) {
				npc.getController().onDelete();
			}
		}
	}

	@Override
	public boolean isMoveSupported() {
		return false;
	}
}
