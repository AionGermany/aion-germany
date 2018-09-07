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
package ai.instance.dragonLordsRefuge;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.ai2.poll.AIAnswer;
import com.aionemu.gameserver.ai2.poll.AIAnswers;
import com.aionemu.gameserver.ai2.poll.AIQuestion;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.actions.CreatureActions;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.services.NpcShoutsService;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;

import ai.AggressiveNpcAI2;

/**
 * @author Cheatkiller
 */
@AIName("gravitycrusher")
// 283141, 283142
public class GravityCrusherAI2 extends AggressiveNpcAI2 {

	private Future<?> skillTask;
	private Future<?> transformTask;

	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		transform();
		ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				attackPlayer();
			}
		}, 2000);
	}

	private void transform() {
		transformTask = ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				if (!isAlreadyDead()) {
					if (skillTask != null) {
						skillTask.cancel(true);
					}
					AI2Actions.useSkill(GravityCrusherAI2.this, 20967); // self destruct

					ThreadPoolManager.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							NpcShoutsService.getInstance().sendMsg(getOwner(), 1401554);
							spawn(283140, getOwner().getX(), getOwner().getY(), getOwner().getZ(), getOwner().getHeading());
							AI2Actions.deleteOwner(GravityCrusherAI2.this);
						}
					}, 3000);

				}
			}
		}, 30000);
	}

	@Override
	public void handleMoveArrived() {
		super.handleMoveArrived();
		skillTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				AI2Actions.useSkill(GravityCrusherAI2.this, 20987);
			}
		}, 0, 4000);
	}

	private void attackPlayer() {
		List<Player> players = new ArrayList<Player>();
		for (Player player : getKnownList().getKnownPlayers().values()) {
			if (!CreatureActions.isAlreadyDead(player) && MathUtil.isIn3dRange(player, getOwner(), 200)) {
				players.add(player);
			}
		}
		Player player = !players.isEmpty() ? players.get(Rnd.get(players.size())) : null;
		getOwner().setTarget(player);
		setStateIfNot(AIState.WALKING);
		getOwner().setState(1);
		getOwner().getMoveController().moveToTargetObject();
		PacketSendUtility.broadcastPacket(getOwner(), new SM_EMOTION(getOwner(), EmotionType.START_EMOTE2, 0, getOwner().getObjectId()));
	}

	private void cancelTask() {
		if (skillTask != null && !skillTask.isCancelled()) {
			skillTask.cancel(true);
		}
		if (transformTask != null && !transformTask.isCancelled()) {
			transformTask.cancel(true);
		}
	}

	@Override
	public void handleDied() {
		super.handleDied();
		cancelTask();
	}

	@Override
	public void handleDespawned() {
		super.handleDespawned();
		cancelTask();
	}

	@Override
	public boolean canThink() {
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
