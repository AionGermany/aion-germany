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
package ai.instance.transidiumAnnex;

import java.util.concurrent.atomic.AtomicBoolean;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.poll.AIAnswer;
import com.aionemu.gameserver.ai2.poll.AIAnswers;
import com.aionemu.gameserver.ai2.poll.AIQuestion;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.MathUtil;

import ai.AggressiveNpcAI2;

@AIName("assult_pod_2")
// 855021
public class AssultPod2AI2 extends AggressiveNpcAI2 {

	@Override
	public void think() {
	}

	private AtomicBoolean startedEvent = new AtomicBoolean(false);

	@Override
	protected void handleCreatureMoved(Creature creature) {
		if (creature instanceof Player) {
			final Player player = (Player) creature;
			if (MathUtil.getDistance(getOwner(), player) <= 5) {
				if (startedEvent.compareAndSet(false, true)) {
					SkillEngine.getInstance().getSkill(getOwner(), 19358, 60, getOwner()).useNoAnimationSkill();
					SkillEngine.getInstance().getSkill(getOwner(), 19922, 60, getOwner()).useNoAnimationSkill();
					AI2Actions.deleteOwner(AssultPod2AI2.this);
					spawn(297188, 381.7059f, 628.5631f, 688.8357f, (byte) 44);
					spawn(297188, 392.7062f, 641.847f, 688.8357f, (byte) 49);
					spawn(297188, 396.56906f, 625.99023f, 688.86523f, (byte) 45);
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							spawn(297193, 379.37097f, 631.6688f, 688.8357f, (byte) 45);
						}
					}, 1000);
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							spawn(297193, 389.8192f, 644.20526f, 688.8357f, (byte) 48);
						}
					}, 3000);
					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							spawn(297192, 393.88156f, 628.3875f, 688.8764f, (byte) 45);
						}
					}, 5000);
				}
			}
		}
	}

	@Override
	public boolean isMoveSupported() {
		return false;
	}

	@Override
	protected AIAnswer pollInstance(AIQuestion question) {
		switch (question) {
			case SHOULD_DECAY:
				return AIAnswers.NEGATIVE;
			case SHOULD_RESPAWN:
				return AIAnswers.NEGATIVE;
			case SHOULD_REWARD:
				return AIAnswers.NEGATIVE;
			default:
				return null;
		}
	}
}
