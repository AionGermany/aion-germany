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

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/**
 * @author GiGatR00n v4.7.5.x
 */
@AIName("blind_silence_stuff")
// 702320
public class BlindSilenceStuffAI2 extends NpcAI2 {

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
			if (MathUtil.isIn3dRange(getOwner(), creature, 5)) {
				HowlofEmptiness();
			}
		}
	}

	private void useSkill(int skillId, VisibleObject target) {
		SkillEngine.getInstance().getSkill(getOwner(), skillId, 65, target).useSkill();
	}

	// private void applyEffect(int skillId, Creature target) {
	// SkillEngine.getInstance().applyEffectDirectly(skillId, getOwner(), target, 0);
	// }

	private void HowlofEmptiness() {
		// SKILL HowlofEmptiness (21557) = IDLDF5_Fortress_Re_BlindSilence_SA
		useSkill(21557, getOwner());
		DeleteNpc();// After 10-Seconds It will be despawned and also deleted from the world.
	}

	private void DeleteNpc() {
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				getOwner().getController().onDelete();
			}
		}, 10000);// After 10-Seconds, It will be despawned and also deleted from thw world.
	}
}
