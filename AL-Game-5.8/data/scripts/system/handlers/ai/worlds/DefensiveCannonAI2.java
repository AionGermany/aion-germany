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
package ai.worlds;

import java.util.concurrent.atomic.AtomicBoolean;

import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.poll.AIAnswer;
import com.aionemu.gameserver.ai2.poll.AIAnswers;
import com.aionemu.gameserver.ai2.poll.AIQuestion;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.skillengine.SkillEngine;

import ai.ActionItemNpcAI2;

/**
 * @author xTz
 */
@AIName("defensive_cannon")
public class DefensiveCannonAI2 extends ActionItemNpcAI2 {

	private final AtomicBoolean canUse = new AtomicBoolean(true);

	@Override
	protected void handleUseItemFinish(Player player) {

		if (canUse.compareAndSet(true, false)) {
			int morphSkill = getMorphSkill();
			SkillEngine.getInstance().getSkill(getOwner(), morphSkill >> 8, morphSkill & 0xFF, player).useNoAnimationSkill();
			AI2Actions.deleteOwner(this);
		}
	}

	private int getMorphSkill() {
		switch (getNpcId()) {
			/**
			 * Invade Vortex 3.5 Theobomos/Bruthonin
			 */
			case 831338: // elyos defensive cannon
				return 0x4F8C3C; // 20364
			case 831339: // asmodian defensive cannon
				return 0x4F8D3C; // 20365
			/**
			 * Danaria Sieges 4.0 needed 186000246 to use skills
			 */
			case 273313: // Empty Aetheric Cannon
			case 272841: // mounted elyos cannon
			case 272848: // mounted elyos sky cannon
				return 0x538900; // 21385
			case 273315: // Empty Etched Cannon
			case 272854: // mounted asmodian cannon
			case 272861: // mounted asmodian sky cannon
				return 0x538A00; // 21386
			/**
			 * Tank Station Abyss 4.5
			 */
			case 251723: // Lightbringer
				return 0x525300; // 21075
			case 251724: // Shadecaster
				return 0x525400; // 21076

		}
		return 0;
	}

	@Override
	protected AIAnswer pollInstance(AIQuestion question) {
		switch (question) {
			case SHOULD_REWARD:
				return AIAnswers.NEGATIVE;
			default:
				return super.pollInstance(question);
		}
	}
}
