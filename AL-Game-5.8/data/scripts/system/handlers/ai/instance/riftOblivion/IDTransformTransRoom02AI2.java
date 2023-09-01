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

@AIName("IDTransform_TransRoom_02") // 245397
public class IDTransformTransRoom02AI2 extends NpcAI2 {

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
				IDTransformTransRoom02_66();
			}
			else if (MathUtil.isIn3dRange(getOwner(), creature, 15) && creature.getLevel() == 67) {
				IDTransformTransRoom02_67();
			}
			else if (MathUtil.isIn3dRange(getOwner(), creature, 15) && creature.getLevel() == 68) {
				IDTransformTransRoom02_68();
			}
			else if (MathUtil.isIn3dRange(getOwner(), creature, 15) && creature.getLevel() == 69) {
				IDTransformTransRoom02_69();
			}
			else if (MathUtil.isIn3dRange(getOwner(), creature, 15) && creature.getLevel() == 70) {
				IDTransformTransRoom02_70();
			}
			else if (MathUtil.isIn3dRange(getOwner(), creature, 15) && creature.getLevel() == 71) {
				IDTransformTransRoom02_71();
			}
			else if (MathUtil.isIn3dRange(getOwner(), creature, 15) && creature.getLevel() == 72) {
				IDTransformTransRoom02_72();
			}
			else if (MathUtil.isIn3dRange(getOwner(), creature, 15) && creature.getLevel() == 73) {
				IDTransformTransRoom02_73();
			}
			else if (MathUtil.isIn3dRange(getOwner(), creature, 15) && creature.getLevel() == 74) {
				IDTransformTransRoom02_74();
			}
			else if (MathUtil.isIn3dRange(getOwner(), creature, 15) && creature.getLevel() >= 75 && creature.getLevel() <= 83) {
				IDTransformTransRoom02_75_83();
			}
		}
	}

	// Player Lvl 66
	private void IDTransformTransRoom02_66() {
		// Shadow B.
		despawnNpc(245873);
		despawnNpc(245874);
		despawnNpc(245875);
		despawnNpc(245876);
		despawnNpc(245423);
		despawnNpc(245424);
		despawnNpc(245425);
		despawnNpc(245426);
		spawn(245403, 610.10956f, 602.11737f, 362.29523f, (byte) 0, 15);
		AI2Actions.deleteOwner(IDTransformTransRoom02AI2.this);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawn(244454, 609.6464f, 607.68384f, 354.69708f, (byte) 90);
				spawn(244455, 609.82184f, 597.31396f, 354.69708f, (byte) 30);
				spawn(244456, 615.1253f, 602.52295f, 354.69708f, (byte) 59);
				spawn(244457, 604.41425f, 602.0602f, 354.69708f, (byte) 1);
			}
		}, 2000);
	}

	// Player Lvl 67
	private void IDTransformTransRoom02_67() {
		// Shadow B.
		despawnNpc(245873);
		despawnNpc(245874);
		despawnNpc(245875);
		despawnNpc(245876);
		despawnNpc(245423);
		despawnNpc(245424);
		despawnNpc(245425);
		despawnNpc(245426);
		spawn(245403, 610.10956f, 602.11737f, 362.29523f, (byte) 0, 15);
		AI2Actions.deleteOwner(IDTransformTransRoom02AI2.this);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawn(244495, 609.6464f, 607.68384f, 354.69708f, (byte) 90);
				spawn(244496, 609.82184f, 597.31396f, 354.69708f, (byte) 30);
				spawn(244497, 615.1253f, 602.52295f, 354.69708f, (byte) 59);
				spawn(244498, 604.41425f, 602.0602f, 354.69708f, (byte) 1);
			}
		}, 2000);
	}

	// Player Lvl 68
	private void IDTransformTransRoom02_68() {
		// Shadow B.
		despawnNpc(245873);
		despawnNpc(245874);
		despawnNpc(245875);
		despawnNpc(245876);
		despawnNpc(245423);
		despawnNpc(245424);
		despawnNpc(245425);
		despawnNpc(245426);
		spawn(245403, 610.10956f, 602.11737f, 362.29523f, (byte) 0, 15);
		AI2Actions.deleteOwner(IDTransformTransRoom02AI2.this);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawn(244536, 609.6464f, 607.68384f, 354.69708f, (byte) 90);
				spawn(244537, 609.82184f, 597.31396f, 354.69708f, (byte) 30);
				spawn(244538, 615.1253f, 602.52295f, 354.69708f, (byte) 59);
				spawn(244539, 604.41425f, 602.0602f, 354.69708f, (byte) 1);
			}
		}, 2000);
	}

	// Player Lvl 69
	private void IDTransformTransRoom02_69() {
		// Shadow B.
		despawnNpc(245873);
		despawnNpc(245874);
		despawnNpc(245875);
		despawnNpc(245876);
		despawnNpc(245423);
		despawnNpc(245424);
		despawnNpc(245425);
		despawnNpc(245426);
		spawn(245403, 610.10956f, 602.11737f, 362.29523f, (byte) 0, 15);
		AI2Actions.deleteOwner(IDTransformTransRoom02AI2.this);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawn(244577, 609.6464f, 607.68384f, 354.69708f, (byte) 90);
				spawn(244578, 609.82184f, 597.31396f, 354.69708f, (byte) 30);
				spawn(244579, 615.1253f, 602.52295f, 354.69708f, (byte) 59);
				spawn(244580, 604.41425f, 602.0602f, 354.69708f, (byte) 1);
			}
		}, 2000);
	}

	// Player Lvl 70
	private void IDTransformTransRoom02_70() {
		// Shadow B.
		despawnNpc(245873);
		despawnNpc(245874);
		despawnNpc(245875);
		despawnNpc(245876);
		despawnNpc(245423);
		despawnNpc(245424);
		despawnNpc(245425);
		despawnNpc(245426);
		spawn(245403, 610.10956f, 602.11737f, 362.29523f, (byte) 0, 15);
		AI2Actions.deleteOwner(IDTransformTransRoom02AI2.this);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawn(244618, 609.6464f, 607.68384f, 354.69708f, (byte) 90);
				spawn(244619, 609.82184f, 597.31396f, 354.69708f, (byte) 30);
				spawn(244620, 615.1253f, 602.52295f, 354.69708f, (byte) 59);
				spawn(244621, 604.41425f, 602.0602f, 354.69708f, (byte) 1);
			}
		}, 2000);
	}

	// Player Lvl 71
	private void IDTransformTransRoom02_71() {
		// Shadow B.
		despawnNpc(245873);
		despawnNpc(245874);
		despawnNpc(245875);
		despawnNpc(245876);
		despawnNpc(245423);
		despawnNpc(245424);
		despawnNpc(245425);
		despawnNpc(245426);
		spawn(245403, 610.10956f, 602.11737f, 362.29523f, (byte) 0, 15);
		AI2Actions.deleteOwner(IDTransformTransRoom02AI2.this);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawn(244659, 609.6464f, 607.68384f, 354.69708f, (byte) 90);
				spawn(244660, 609.82184f, 597.31396f, 354.69708f, (byte) 30);
				spawn(244661, 615.1253f, 602.52295f, 354.69708f, (byte) 59);
				spawn(244662, 604.41425f, 602.0602f, 354.69708f, (byte) 1);
			}
		}, 2000);
	}

	// Player Lvl 72
	private void IDTransformTransRoom02_72() {
		// Shadow B.
		despawnNpc(245873);
		despawnNpc(245874);
		despawnNpc(245875);
		despawnNpc(245876);
		despawnNpc(245423);
		despawnNpc(245424);
		despawnNpc(245425);
		despawnNpc(245426);
		spawn(245403, 610.10956f, 602.11737f, 362.29523f, (byte) 0, 15);
		AI2Actions.deleteOwner(IDTransformTransRoom02AI2.this);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawn(244700, 609.6464f, 607.68384f, 354.69708f, (byte) 90);
				spawn(244701, 609.82184f, 597.31396f, 354.69708f, (byte) 30);
				spawn(244702, 615.1253f, 602.52295f, 354.69708f, (byte) 59);
				spawn(244703, 604.41425f, 602.0602f, 354.69708f, (byte) 1);
			}
		}, 2000);
	}

	// Player Lvl 73
	private void IDTransformTransRoom02_73() {
		// Shadow B.
		despawnNpc(245873);
		despawnNpc(245874);
		despawnNpc(245875);
		despawnNpc(245876);
		despawnNpc(245423);
		despawnNpc(245424);
		despawnNpc(245425);
		despawnNpc(245426);
		spawn(245403, 610.10956f, 602.11737f, 362.29523f, (byte) 0, 15);
		AI2Actions.deleteOwner(IDTransformTransRoom02AI2.this);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawn(244741, 609.6464f, 607.68384f, 354.69708f, (byte) 90);
				spawn(244742, 609.82184f, 597.31396f, 354.69708f, (byte) 30);
				spawn(244743, 615.1253f, 602.52295f, 354.69708f, (byte) 59);
				spawn(244744, 604.41425f, 602.0602f, 354.69708f, (byte) 1);
			}
		}, 2000);
	}

	// Player Lvl 74
	private void IDTransformTransRoom02_74() {
		// Shadow B.
		despawnNpc(245873);
		despawnNpc(245874);
		despawnNpc(245875);
		despawnNpc(245876);
		despawnNpc(245423);
		despawnNpc(245424);
		despawnNpc(245425);
		despawnNpc(245426);
		spawn(245403, 610.10956f, 602.11737f, 362.29523f, (byte) 0, 15);
		AI2Actions.deleteOwner(IDTransformTransRoom02AI2.this);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawn(244782, 609.6464f, 607.68384f, 354.69708f, (byte) 90);
				spawn(244783, 609.82184f, 597.31396f, 354.69708f, (byte) 30);
				spawn(244784, 615.1253f, 602.52295f, 354.69708f, (byte) 59);
				spawn(244785, 604.41425f, 602.0602f, 354.69708f, (byte) 1);
			}
		}, 2000);
	}

	// Player Lvl 75-83
	private void IDTransformTransRoom02_75_83() {
		// Shadow B.
		despawnNpc(245873);
		despawnNpc(245874);
		despawnNpc(245875);
		despawnNpc(245876);
		despawnNpc(245423);
		despawnNpc(245424);
		despawnNpc(245425);
		despawnNpc(245426);
		spawn(245403, 610.10956f, 602.11737f, 362.29523f, (byte) 0, 15);
		AI2Actions.deleteOwner(IDTransformTransRoom02AI2.this);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawn(244823, 609.6464f, 607.68384f, 354.69708f, (byte) 90);
				spawn(244824, 609.82184f, 597.31396f, 354.69708f, (byte) 30);
				spawn(244825, 615.1253f, 602.52295f, 354.69708f, (byte) 59);
				spawn(244826, 604.41425f, 602.0602f, 354.69708f, (byte) 1);
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
