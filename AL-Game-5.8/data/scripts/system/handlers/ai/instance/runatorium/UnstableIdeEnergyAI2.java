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

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldPosition;
import com.aionemu.gameserver.world.knownlist.Visitor;

/**
 * @author GiGatR00n v4.7.5.x
 */
@AIName("unstable_ide_energy")
// 855008, 855012
public class UnstableIdeEnergyAI2 extends NpcAI2 {

	private Player LuckyPlayer;
	private boolean gotIt = false;

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		switch (getNpcId()) {
			case 855008:
				AttackPlayers();
			case 855012:
				useSkill(21559);// SkillId:21559 (Ide Explosion)
		}
		DeleteNpc();// After 3-Seconds the Unstable Ide Energy will be deleted.
	}

	@Override
	public void handleDespawned() {
		super.handleDespawned();
	}

	@Override
	protected void handleDied() {
		super.handleDied();
	}

	private void useSkill(int skillId) {
		SkillEngine.getInstance().getSkill(getOwner(), skillId, 1, getOwner()).useSkill();
	}

	private Player getLuckyPlayer() {
		LuckyPlayer = null;
		gotIt = false;
		getPosition().getWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player player) {
				if (!gotIt) {
					/* if it couldn't find any lucky player, then it returns the last player */
					LuckyPlayer = player;
					int rand = Rnd.get(1, 3);
					if (rand == 3) {
						gotIt = true;
					}
				}
			}
		});
		return LuckyPlayer;
	}

	private void AttackPlayers() {
		/*
		 * The Players got lucky. No one is intended to be attacked.
		 */
		Player player = getLuckyPlayer();
		if (player == null) {
			return;
		}

		WorldPosition Pos = player.getPosition();
		if (!MathUtil.isInSphere(player, 284.359385f, 334.957915f, 80f, 25f) && !MathUtil.isInSphere(player, 245.880305f, 183.984715f, 80f, 25f)) {
			float r1 = Rnd.get(-15, 15);
			float r2 = Rnd.get(-15, 15);
			getOwner().setXYZH(Pos.getX() + r1, Pos.getY() + r2, Pos.getZ(), Pos.getHeading());
			useSkill(21559);// SkillId:21559 (Ide Explosion)
		}
	}

	private void DeleteNpc() {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				getOwner().getController().onDelete();
			}
		}, 3000);// After 3-Seconds the Unstable Ide Energy will be deleted.
	}
}
