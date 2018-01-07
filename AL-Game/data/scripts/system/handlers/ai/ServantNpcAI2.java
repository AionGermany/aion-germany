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
package ai;

import java.util.concurrent.Future;

import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.poll.AIAnswer;
import com.aionemu.gameserver.ai2.poll.AIAnswers;
import com.aionemu.gameserver.ai2.poll.AIQuestion;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.NpcObjectType;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/**
 * @author ATracer
 */
@AIName("servant")
public class ServantNpcAI2 extends GeneralNpcAI2 {

	@Override
	public void think() {
		// servants are not thinking
	}

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		if (getCreator() != null) {
			ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					if (getOwner().getNpcObjectType() != NpcObjectType.TOTEM) {
						AI2Actions.targetCreature(ServantNpcAI2.this, (Creature) getCreator().getTarget());
					}
					healOrAttack();
				}
			}, 200);
		}
	}

	private void healOrAttack() {
		if (skillId == 0) {
			skillId = getSkillList().getRandomSkill().getSkillId();
		}
		int duration = getOwner().getNpcObjectType() == NpcObjectType.TOTEM ? 3000 : 5000;
		Future<?> task = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				getOwner().getController().useSkill(skillId, 1);
			}
		}, 1000, duration);
		getOwner().getController().addTask(TaskId.SKILL_USE, task);
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
