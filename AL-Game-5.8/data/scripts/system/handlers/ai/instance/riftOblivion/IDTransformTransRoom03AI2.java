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

@AIName("IDTransform_TransRoom_03") // 245398
public class IDTransformTransRoom03AI2 extends NpcAI2 {

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
				IDTransformTransRoom03_66();
			}
			else if (MathUtil.isIn3dRange(getOwner(), creature, 15) && creature.getLevel() == 67) {
				IDTransformTransRoom03_67();
			}
			else if (MathUtil.isIn3dRange(getOwner(), creature, 15) && creature.getLevel() == 68) {
				IDTransformTransRoom03_68();
			}
			else if (MathUtil.isIn3dRange(getOwner(), creature, 15) && creature.getLevel() == 69) {
				IDTransformTransRoom03_69();
			}
			else if (MathUtil.isIn3dRange(getOwner(), creature, 15) && creature.getLevel() == 70) {
				IDTransformTransRoom03_70();
			}
			else if (MathUtil.isIn3dRange(getOwner(), creature, 15) && creature.getLevel() == 71) {
				IDTransformTransRoom03_71();
			}
			else if (MathUtil.isIn3dRange(getOwner(), creature, 15) && creature.getLevel() == 72) {
				IDTransformTransRoom03_72();
			}
			else if (MathUtil.isIn3dRange(getOwner(), creature, 15) && creature.getLevel() == 73) {
				IDTransformTransRoom03_73();
			}
			else if (MathUtil.isIn3dRange(getOwner(), creature, 15) && creature.getLevel() == 74) {
				IDTransformTransRoom03_74();
			}
			else if (MathUtil.isIn3dRange(getOwner(), creature, 15) && creature.getLevel() >= 75 && creature.getLevel() <= 83) {
				IDTransformTransRoom03_75_83();
			}
		}
	}

	// Player Lvl 66
	private void IDTransformTransRoom03_66() {
		// Shadow C.
		despawnNpc(245877);
		despawnNpc(245878);
		despawnNpc(245879);
		despawnNpc(245880);
		despawnNpc(245427);
		despawnNpc(245428);
		despawnNpc(245429);
		despawnNpc(245430);
		spawn(245404, 385.26727f, 513.22351f, 353.75681f, (byte) 0, 22);
		AI2Actions.deleteOwner(IDTransformTransRoom03AI2.this);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawn(244454, 384.56375f, 515.2426f, 350.37335f, (byte) 91);
				spawn(244455, 382.34543f, 513.1757f, 350.37335f, (byte) 0);
				spawn(244456, 384.6212f, 510.97125f, 350.37335f, (byte) 30);
				spawn(244457, 386.9315f, 513.2064f, 350.37335f, (byte) 60);
			}
		}, 2000);
	}

	// Player Lvl 67
	private void IDTransformTransRoom03_67() {
		// Shadow C.
		despawnNpc(245877);
		despawnNpc(245878);
		despawnNpc(245879);
		despawnNpc(245880);
		despawnNpc(245427);
		despawnNpc(245428);
		despawnNpc(245429);
		despawnNpc(245430);
		spawn(245404, 385.26727f, 513.22351f, 353.75681f, (byte) 0, 22);
		AI2Actions.deleteOwner(IDTransformTransRoom03AI2.this);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawn(244495, 384.56375f, 515.2426f, 350.37335f, (byte) 91);
				spawn(244496, 382.34543f, 513.1757f, 350.37335f, (byte) 0);
				spawn(244497, 384.6212f, 510.97125f, 350.37335f, (byte) 30);
				spawn(244498, 386.9315f, 513.2064f, 350.37335f, (byte) 60);
			}
		}, 2000);
	}

	// Player Lvl 68
	private void IDTransformTransRoom03_68() {
		// Shadow C.
		despawnNpc(245877);
		despawnNpc(245878);
		despawnNpc(245879);
		despawnNpc(245880);
		despawnNpc(245427);
		despawnNpc(245428);
		despawnNpc(245429);
		despawnNpc(245430);
		spawn(245404, 385.26727f, 513.22351f, 353.75681f, (byte) 0, 22);
		AI2Actions.deleteOwner(IDTransformTransRoom03AI2.this);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawn(244536, 384.56375f, 515.2426f, 350.37335f, (byte) 91);
				spawn(244537, 382.34543f, 513.1757f, 350.37335f, (byte) 0);
				spawn(244538, 384.6212f, 510.97125f, 350.37335f, (byte) 30);
				spawn(244539, 386.9315f, 513.2064f, 350.37335f, (byte) 60);
			}
		}, 2000);
	}

	// Player Lvl 69
	private void IDTransformTransRoom03_69() {
		// Shadow C.
		despawnNpc(245877);
		despawnNpc(245878);
		despawnNpc(245879);
		despawnNpc(245880);
		despawnNpc(245427);
		despawnNpc(245428);
		despawnNpc(245429);
		despawnNpc(245430);
		spawn(245404, 385.26727f, 513.22351f, 353.75681f, (byte) 0, 22);
		AI2Actions.deleteOwner(IDTransformTransRoom03AI2.this);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawn(244577, 384.56375f, 515.2426f, 350.37335f, (byte) 91);
				spawn(244578, 382.34543f, 513.1757f, 350.37335f, (byte) 0);
				spawn(244579, 384.6212f, 510.97125f, 350.37335f, (byte) 30);
				spawn(244580, 386.9315f, 513.2064f, 350.37335f, (byte) 60);
			}
		}, 2000);
	}

	// Player Lvl 70
	private void IDTransformTransRoom03_70() {
		// Shadow C.
		despawnNpc(245877);
		despawnNpc(245878);
		despawnNpc(245879);
		despawnNpc(245880);
		despawnNpc(245427);
		despawnNpc(245428);
		despawnNpc(245429);
		despawnNpc(245430);
		spawn(245404, 385.26727f, 513.22351f, 353.75681f, (byte) 0, 22);
		AI2Actions.deleteOwner(IDTransformTransRoom03AI2.this);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawn(244618, 384.56375f, 515.2426f, 350.37335f, (byte) 91);
				spawn(244619, 382.34543f, 513.1757f, 350.37335f, (byte) 0);
				spawn(244620, 384.6212f, 510.97125f, 350.37335f, (byte) 30);
				spawn(244621, 386.9315f, 513.2064f, 350.37335f, (byte) 60);
			}
		}, 2000);
	}

	// Player Lvl 71
	private void IDTransformTransRoom03_71() {
		// Shadow C.
		despawnNpc(245877);
		despawnNpc(245878);
		despawnNpc(245879);
		despawnNpc(245880);
		despawnNpc(245427);
		despawnNpc(245428);
		despawnNpc(245429);
		despawnNpc(245430);
		spawn(245404, 385.26727f, 513.22351f, 353.75681f, (byte) 0, 22);
		AI2Actions.deleteOwner(IDTransformTransRoom03AI2.this);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawn(244659, 384.56375f, 515.2426f, 350.37335f, (byte) 91);
				spawn(244660, 382.34543f, 513.1757f, 350.37335f, (byte) 0);
				spawn(244661, 384.6212f, 510.97125f, 350.37335f, (byte) 30);
				spawn(244662, 386.9315f, 513.2064f, 350.37335f, (byte) 60);
			}
		}, 2000);
	}

	// Player Lvl 72
	private void IDTransformTransRoom03_72() {
		// Shadow C.
		despawnNpc(245877);
		despawnNpc(245878);
		despawnNpc(245879);
		despawnNpc(245880);
		despawnNpc(245427);
		despawnNpc(245428);
		despawnNpc(245429);
		despawnNpc(245430);
		spawn(245404, 385.26727f, 513.22351f, 353.75681f, (byte) 0, 22);
		AI2Actions.deleteOwner(IDTransformTransRoom03AI2.this);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawn(244700, 384.56375f, 515.2426f, 350.37335f, (byte) 91);
				spawn(244701, 382.34543f, 513.1757f, 350.37335f, (byte) 0);
				spawn(244702, 384.6212f, 510.97125f, 350.37335f, (byte) 30);
				spawn(244703, 386.9315f, 513.2064f, 350.37335f, (byte) 60);
			}
		}, 2000);
	}

	// Player Lvl 73
	private void IDTransformTransRoom03_73() {
		// Shadow C.
		despawnNpc(245877);
		despawnNpc(245878);
		despawnNpc(245879);
		despawnNpc(245880);
		despawnNpc(245427);
		despawnNpc(245428);
		despawnNpc(245429);
		despawnNpc(245430);
		spawn(245404, 385.26727f, 513.22351f, 353.75681f, (byte) 0, 22);
		AI2Actions.deleteOwner(IDTransformTransRoom03AI2.this);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawn(244741, 384.56375f, 515.2426f, 350.37335f, (byte) 91);
				spawn(244742, 382.34543f, 513.1757f, 350.37335f, (byte) 0);
				spawn(244743, 384.6212f, 510.97125f, 350.37335f, (byte) 30);
				spawn(244744, 386.9315f, 513.2064f, 350.37335f, (byte) 60);
			}
		}, 2000);
	}

	// Player Lvl 74
	private void IDTransformTransRoom03_74() {
		// Shadow C.
		despawnNpc(245877);
		despawnNpc(245878);
		despawnNpc(245879);
		despawnNpc(245880);
		despawnNpc(245427);
		despawnNpc(245428);
		despawnNpc(245429);
		despawnNpc(245430);
		spawn(245404, 385.26727f, 513.22351f, 353.75681f, (byte) 0, 22);
		AI2Actions.deleteOwner(IDTransformTransRoom03AI2.this);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawn(244782, 384.56375f, 515.2426f, 350.37335f, (byte) 91);
				spawn(244783, 382.34543f, 513.1757f, 350.37335f, (byte) 0);
				spawn(244784, 384.6212f, 510.97125f, 350.37335f, (byte) 30);
				spawn(244785, 386.9315f, 513.2064f, 350.37335f, (byte) 60);
			}
		}, 2000);
	}

	// Player Lvl 75-83
	private void IDTransformTransRoom03_75_83() {
		// Shadow C.
		despawnNpc(245877);
		despawnNpc(245878);
		despawnNpc(245879);
		despawnNpc(245880);
		despawnNpc(245427);
		despawnNpc(245428);
		despawnNpc(245429);
		despawnNpc(245430);
		spawn(245404, 385.26727f, 513.22351f, 353.75681f, (byte) 0, 22);
		AI2Actions.deleteOwner(IDTransformTransRoom03AI2.this);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawn(244823, 384.56375f, 515.2426f, 350.37335f, (byte) 91);
				spawn(244824, 382.34543f, 513.1757f, 350.37335f, (byte) 0);
				spawn(244825, 384.6212f, 510.97125f, 350.37335f, (byte) 30);
				spawn(244826, 386.9315f, 513.2064f, 350.37335f, (byte) 60);
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
