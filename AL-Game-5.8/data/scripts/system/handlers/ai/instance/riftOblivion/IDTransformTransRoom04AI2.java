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

@AIName("IDTransform_TransRoom_04") // 245399
public class IDTransformTransRoom04AI2 extends NpcAI2 {

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
				IDTransformTransRoom04_66();
			}
			else if (MathUtil.isIn3dRange(getOwner(), creature, 15) && creature.getLevel() == 67) {
				IDTransformTransRoom04_67();
			}
			else if (MathUtil.isIn3dRange(getOwner(), creature, 15) && creature.getLevel() == 68) {
				IDTransformTransRoom04_68();
			}
			else if (MathUtil.isIn3dRange(getOwner(), creature, 15) && creature.getLevel() == 69) {
				IDTransformTransRoom04_69();
			}
			else if (MathUtil.isIn3dRange(getOwner(), creature, 15) && creature.getLevel() == 70) {
				IDTransformTransRoom04_70();
			}
			else if (MathUtil.isIn3dRange(getOwner(), creature, 15) && creature.getLevel() == 71) {
				IDTransformTransRoom04_71();
			}
			else if (MathUtil.isIn3dRange(getOwner(), creature, 15) && creature.getLevel() == 72) {
				IDTransformTransRoom04_72();
			}
			else if (MathUtil.isIn3dRange(getOwner(), creature, 15) && creature.getLevel() == 73) {
				IDTransformTransRoom04_73();
			}
			else if (MathUtil.isIn3dRange(getOwner(), creature, 15) && creature.getLevel() == 74) {
				IDTransformTransRoom04_74();
			}
			else if (MathUtil.isIn3dRange(getOwner(), creature, 15) && creature.getLevel() >= 75 && creature.getLevel() <= 83) {
				IDTransformTransRoom04_75_83();
			}
		}
	}

	// Player Lvl 66
	private void IDTransformTransRoom04_66() {
		// Shadow D.
		despawnNpc(244854);
		despawnNpc(244855);
		despawnNpc(244856);
		despawnNpc(244857);
		despawnNpc(245885);
		despawnNpc(245886);
		despawnNpc(245887);
		despawnNpc(245888);
		spawn(245405, 301.12494f, 513.34650f, 352.99631f, (byte) 0, 33);
		AI2Actions.deleteOwner(IDTransformTransRoom04AI2.this);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawn(244490, 301.1525f, 512.97736f, 350.8281f, (byte) 0);
			}
		}, 2000);
	}

	// Player Lvl 67
	private void IDTransformTransRoom04_67() {
		// Shadow D.
		despawnNpc(244854);
		despawnNpc(244855);
		despawnNpc(244856);
		despawnNpc(244857);
		despawnNpc(245885);
		despawnNpc(245886);
		despawnNpc(245887);
		despawnNpc(245888);
		spawn(245405, 301.12494f, 513.34650f, 352.99631f, (byte) 0, 33);
		AI2Actions.deleteOwner(IDTransformTransRoom04AI2.this);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawn(244531, 301.1525f, 512.97736f, 350.8281f, (byte) 0);
			}
		}, 2000);
	}

	// Player Lvl 68
	private void IDTransformTransRoom04_68() {
		// Shadow D.
		despawnNpc(244854);
		despawnNpc(244855);
		despawnNpc(244856);
		despawnNpc(244857);
		despawnNpc(245885);
		despawnNpc(245886);
		despawnNpc(245887);
		despawnNpc(245888);
		spawn(245405, 301.12494f, 513.34650f, 352.99631f, (byte) 0, 33);
		AI2Actions.deleteOwner(IDTransformTransRoom04AI2.this);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawn(244572, 301.1525f, 512.97736f, 350.8281f, (byte) 0);
			}
		}, 2000);
	}

	// Player Lvl 69
	private void IDTransformTransRoom04_69() {
		// Shadow D.
		despawnNpc(244854);
		despawnNpc(244855);
		despawnNpc(244856);
		despawnNpc(244857);
		despawnNpc(245885);
		despawnNpc(245886);
		despawnNpc(245887);
		despawnNpc(245888);
		spawn(245405, 301.12494f, 513.34650f, 352.99631f, (byte) 0, 33);
		AI2Actions.deleteOwner(IDTransformTransRoom04AI2.this);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawn(244613, 301.1525f, 512.97736f, 350.8281f, (byte) 0);
			}
		}, 2000);
	}

	// Player Lvl 70
	private void IDTransformTransRoom04_70() {
		// Shadow D.
		despawnNpc(244854);
		despawnNpc(244855);
		despawnNpc(244856);
		despawnNpc(244857);
		despawnNpc(245885);
		despawnNpc(245886);
		despawnNpc(245887);
		despawnNpc(245888);
		spawn(245405, 301.12494f, 513.34650f, 352.99631f, (byte) 0, 33);
		AI2Actions.deleteOwner(IDTransformTransRoom04AI2.this);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawn(244654, 301.1525f, 512.97736f, 350.8281f, (byte) 0);
			}
		}, 2000);
	}

	// Player Lvl 71
	private void IDTransformTransRoom04_71() {
		// Shadow D.
		despawnNpc(244854);
		despawnNpc(244855);
		despawnNpc(244856);
		despawnNpc(244857);
		despawnNpc(245885);
		despawnNpc(245886);
		despawnNpc(245887);
		despawnNpc(245888);
		spawn(245405, 301.12494f, 513.34650f, 352.99631f, (byte) 0, 33);
		AI2Actions.deleteOwner(IDTransformTransRoom04AI2.this);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawn(244695, 301.1525f, 512.97736f, 350.8281f, (byte) 0);
			}
		}, 2000);
	}

	// Player Lvl 72
	private void IDTransformTransRoom04_72() {
		// Shadow D.
		despawnNpc(244854);
		despawnNpc(244855);
		despawnNpc(244856);
		despawnNpc(244857);
		despawnNpc(245885);
		despawnNpc(245886);
		despawnNpc(245887);
		despawnNpc(245888);
		spawn(245405, 301.12494f, 513.34650f, 352.99631f, (byte) 0, 33);
		AI2Actions.deleteOwner(IDTransformTransRoom04AI2.this);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawn(244736, 301.1525f, 512.97736f, 350.8281f, (byte) 0);
			}
		}, 2000);
	}

	// Player Lvl 73
	private void IDTransformTransRoom04_73() {
		// Shadow D.
		despawnNpc(244854);
		despawnNpc(244855);
		despawnNpc(244856);
		despawnNpc(244857);
		despawnNpc(245885);
		despawnNpc(245886);
		despawnNpc(245887);
		despawnNpc(245888);
		spawn(245405, 301.12494f, 513.34650f, 352.99631f, (byte) 0, 33);
		AI2Actions.deleteOwner(IDTransformTransRoom04AI2.this);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawn(244777, 301.1525f, 512.97736f, 350.8281f, (byte) 0);
			}
		}, 2000);
	}

	// Player Lvl 74
	private void IDTransformTransRoom04_74() {
		// Shadow D.
		despawnNpc(244854);
		despawnNpc(244855);
		despawnNpc(244856);
		despawnNpc(244857);
		despawnNpc(245885);
		despawnNpc(245886);
		despawnNpc(245887);
		despawnNpc(245888);
		spawn(245405, 301.12494f, 513.34650f, 352.99631f, (byte) 0, 33);
		AI2Actions.deleteOwner(IDTransformTransRoom04AI2.this);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawn(244818, 301.1525f, 512.97736f, 350.8281f, (byte) 0);
			}
		}, 2000);
	}

	// Player Lvl 75-83
	private void IDTransformTransRoom04_75_83() {
		// Shadow D.
		despawnNpc(244854);
		despawnNpc(244855);
		despawnNpc(244856);
		despawnNpc(244857);
		despawnNpc(245885);
		despawnNpc(245886);
		despawnNpc(245887);
		despawnNpc(245888);
		spawn(245405, 301.12494f, 513.34650f, 352.99631f, (byte) 0, 33);
		AI2Actions.deleteOwner(IDTransformTransRoom04AI2.this);
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				spawn(244859, 301.1525f, 512.97736f, 350.8281f, (byte) 0);
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
