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
package ai.instance.libraryKnowledge;

import java.util.concurrent.atomic.AtomicBoolean;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.manager.WalkManager;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.services.NpcShoutsService;
import com.aionemu.gameserver.utils.PacketSendUtility;
//import com.aionemu.gameserver.world.WorldMapInstance;

import ai.AggressiveNpcAI2;

//TODO Improvment

/**
 * @author FrozenKiller
 */

@AIName("knowledgeleibo")
public class KnowledgeLeiboAI2 extends AggressiveNpcAI2 {

	private boolean canThink = true;
	private AtomicBoolean startedEvent = new AtomicBoolean(false);

	@Override
	public boolean canThink() {
		return canThink;
	}

	@Override
	protected void handleCreatureMoved(Creature creature) {
		super.handleCreatureMoved(creature);
		if (getNpcId() == 857833 && isInRange(creature, 20) && creature instanceof Player) {
			if (startedEvent.compareAndSet(false, true)) {
				canThink = false;
				NpcShoutsService.getInstance().sendMsg(getOwner(), 1501513, getObjectId(), 0, 0);
				getOwner().getMoveController().moveToPoint(625.4812f, 432.1413f, 469.1543f);
				WalkManager.startWalking(this);
				getOwner().setState(1);
				PacketSendUtility.broadcastPacket(getOwner(), new SM_EMOTION(getOwner(), EmotionType.START_EMOTE2, 0, getObjectId()));
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						if (!isAlreadyDead()) {
							despawn();
							// WorldMapInstance instance = getPosition().getWorldMapInstance();
							// instance.getDoors().get(56).setOpen(false);
						}
					}
				}, 6000);
			}
		}
		else if (getNpcId() == 857834 && isInRange(creature, 20) && creature instanceof Player) {
			if (startedEvent.compareAndSet(false, true)) {
				canThink = false;
				NpcShoutsService.getInstance().sendMsg(getOwner(), 1501514, getObjectId(), 0, 0);
				getOwner().getMoveController().moveToPoint(528.0478f, 352.4390f, 469.1403f);
				WalkManager.startWalking(this);
				getOwner().setState(1);
				PacketSendUtility.broadcastPacket(getOwner(), new SM_EMOTION(getOwner(), EmotionType.START_EMOTE2, 0, getObjectId()));
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						if (!isAlreadyDead()) {
							despawn();
						}
					}
				}, 6000);
			}
		}
		else if (getNpcId() == 857835 && isInRange(creature, 40) && creature instanceof Player) {
			if (startedEvent.compareAndSet(false, true)) {
				canThink = false;
				NpcShoutsService.getInstance().sendMsg(getOwner(), 1501516, getObjectId(), 0, 0);
				getOwner().getMoveController().moveToPoint(473.2738f, 373.6968f, 469.1368f);
				WalkManager.startWalking(this);
				getOwner().setState(1);
				PacketSendUtility.broadcastPacket(getOwner(), new SM_EMOTION(getOwner(), EmotionType.START_EMOTE2, 0, getObjectId()));
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						if (!isAlreadyDead()) {
							despawn();
						}
					}
				}, 6000);
			}
		}
		else if (getNpcId() == 857836 && isInRange(creature, 40) && creature instanceof Player) {
			if (startedEvent.compareAndSet(false, true)) {
				canThink = false;
				NpcShoutsService.getInstance().sendMsg(getOwner(), 1501515, getObjectId(), 0, 0);
				getOwner().getMoveController().moveToPoint(473.2738f, 373.6968f, 469.1368f);
				WalkManager.startWalking(this);
				getOwner().setState(1);
				PacketSendUtility.broadcastPacket(getOwner(), new SM_EMOTION(getOwner(), EmotionType.START_EMOTE2, 0, getObjectId()));
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						if (!isAlreadyDead()) {
							despawn();
						}
					}
				}, 6000);
			}
		}
		else if (getNpcId() == 857904 && isInRange(creature, 40) && creature instanceof Player) {
			if (startedEvent.compareAndSet(false, true)) {
				canThink = false;
				NpcShoutsService.getInstance().sendMsg(getOwner(), 1501515, getObjectId(), 0, 0);
				getOwner().getMoveController().moveToPoint(473.2738f, 373.6968f, 469.1368f);
				WalkManager.startWalking(this);
				getOwner().setState(1);
				PacketSendUtility.broadcastPacket(getOwner(), new SM_EMOTION(getOwner(), EmotionType.START_EMOTE2, 0, getObjectId()));
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						if (!isAlreadyDead()) {
							despawn();
						}
					}
				}, 6000);
			}
		}
		else if (getNpcId() == 857905 && isInRange(creature, 40) && creature instanceof Player) {
			if (startedEvent.compareAndSet(false, true)) {
				canThink = false;
				NpcShoutsService.getInstance().sendMsg(getOwner(), 1501524, getObjectId(), 0, 0);
				NpcShoutsService.getInstance().sendMsg(getOwner(), 1501516, getObjectId(), 0, 0);
				getOwner().getMoveController().moveToPoint(539.8622f, 533.9291f, 469.1512f);
				WalkManager.startWalking(this);
				getOwner().setState(1);
				PacketSendUtility.broadcastPacket(getOwner(), new SM_EMOTION(getOwner(), EmotionType.START_EMOTE2, 0, getObjectId()));
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						if (!isAlreadyDead()) {
							despawn();
						}
					}
				}, 6000);
			}
		}
		else if (getNpcId() == 857906 && isInRange(creature, 40) && creature instanceof Player) {
			if (startedEvent.compareAndSet(false, true)) {
				canThink = false;
				NpcShoutsService.getInstance().sendMsg(getOwner(), 1501515, getObjectId(), 0, 0);
				getOwner().getMoveController().moveToPoint(539.8622f, 533.9291f, 469.1512f);
				WalkManager.startWalking(this);
				getOwner().setState(1);
				PacketSendUtility.broadcastPacket(getOwner(), new SM_EMOTION(getOwner(), EmotionType.START_EMOTE2, 0, getObjectId()));
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						if (!isAlreadyDead()) {
							despawn();
						}
					}
				}, 6000);
			}
		}
		else if (getNpcId() == 857907 && isInRange(creature, 40) && creature instanceof Player) {
			if (startedEvent.compareAndSet(false, true)) {
				canThink = false;
				NpcShoutsService.getInstance().sendMsg(getOwner(), 1501517, getObjectId(), 0, 0);
				getOwner().getMoveController().moveToPoint(494.2162f, 613.3935f, 469.1360f);
				WalkManager.startWalking(this);
				getOwner().setState(1);
				PacketSendUtility.broadcastPacket(getOwner(), new SM_EMOTION(getOwner(), EmotionType.START_EMOTE2, 0, getObjectId()));
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						if (!isAlreadyDead()) {
							despawn();
						}
					}
				}, 6000);
			}
		}
		else if (getNpcId() == 857908 && isInRange(creature, 40) && creature instanceof Player) {
			if (startedEvent.compareAndSet(false, true)) {
				canThink = false;
				NpcShoutsService.getInstance().sendMsg(getOwner(), 1501529, getObjectId(), 0, 0);
				getOwner().getMoveController().moveToPoint(402.1526f, 569.6807f, 469.1543f);
				WalkManager.startWalking(this);
				getOwner().setState(1);
				PacketSendUtility.broadcastPacket(getOwner(), new SM_EMOTION(getOwner(), EmotionType.START_EMOTE2, 0, getObjectId()));
				ThreadPoolManager.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						if (!isAlreadyDead()) {
							despawn();
						}
					}
				}, 6000);
			}
		}
	}

	private void despawn() {
		AI2Actions.deleteOwner(this);
	}
}
