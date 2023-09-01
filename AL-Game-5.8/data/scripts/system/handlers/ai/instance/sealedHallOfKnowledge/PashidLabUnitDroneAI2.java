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

package ai.instance.sealedHallOfKnowledge;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.ai2.AbstractAI;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.services.NpcShoutsService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.knownlist.Visitor;

import ai.AggressiveNpcAI2;

/**
 * @author Ranastic
 */
@AIName("pashid_lab_unit_drone")
public class PashidLabUnitDroneAI2 extends AggressiveNpcAI2 {

	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		if (Rnd.get(1, 100) < 50) {
			spawnHelper();
		}
		shoutEvent();
	}

	private void spawnHelper() {
		Npc helperSpawn = getPosition().getWorldMapInstance().getNpc(230077);// Pashid
																				// Lab
																				// Unit
																				// Sentinel.
		if (helperSpawn != null) {
			return;
		}
		else if (helperSpawn == null) {
			spawn1();
			NpcShoutsService.getInstance().sendMsg(getOwner(), 342822, getObjectId(), 0, 0);
			AI2Actions.useSkill(this, 19498); // Red Alert
		}
	}

	private void spawn1() {
		float direction = Rnd.get(0, 199) / 100f;
		float x1 = (float) (Math.cos(Math.PI * direction) * 5);
		float y1 = (float) (Math.sin(Math.PI * direction) * 5);
		float x2 = (float) (Math.cos(Math.PI * direction) * 2);
		float y2 = (float) (Math.sin(Math.PI * direction) * 2);
		attackPlayer((Npc) spawn(230077, getOwner().getX() + x1, getOwner().getY() + y1, getOwner().getZ(), (byte) 0));
		attackPlayer((Npc) spawn(230077, getOwner().getX() + x2, getOwner().getY() + y2, getOwner().getZ(), (byte) 0));
		return;
	}

	private void shoutEvent() {
		Npc helperSpawn = getPosition().getWorldMapInstance().getNpc(230077);// Pashid
																				// Lab
																				// Unit
																				// Sentinel.
		if (helperSpawn == null) {
			ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					NpcShoutsService.getInstance().sendMsg(getOwner(), 342819, getObjectId(), 20, 0);
				}
			}, 1000);
			ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					NpcShoutsService.getInstance().sendMsg(getOwner(), 342820, getObjectId(), 20, 0);
				}
			}, 10000);
			ThreadPoolManager.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					NpcShoutsService.getInstance().sendMsg(getOwner(), 342821, getObjectId(), 20, 0);
				}
			}, 15000);
		}
	}

	@Override
	protected void handleBackHome() {
		super.handleBackHome();
		despawnHelper();
	}

	@Override
	protected void handleDied() {
		super.handleDied();
		despawnHelper();
	}

	private void despawnHelper() {
		getOwner().getKnownList().doOnAllNpcs(new Visitor<Npc>() {

			@Override
			public void visit(Npc object) {
				Npc helper = getPosition().getWorldMapInstance().getNpc(230077); // Pashid
																					// Lab
																					// Unit
																					// Sentinel.
				if (helper != null)
					helper.getController().onDelete();
			}
		});
	}

	private void attackPlayer(final Npc npc) {
		getOwner().getKnownList().doOnAllPlayers(new Visitor<Player>() {

			@Override
			public void visit(Player object) {
				npc.setTarget(object);
				((AbstractAI) npc.getAi2()).setStateIfNot(AIState.WALKING);
				npc.setState(1);
				npc.getMoveController().moveToTargetObject();
				PacketSendUtility.broadcastPacket(npc, new SM_EMOTION(npc, EmotionType.START_EMOTE2, 0, npc.getObjectId()));
			}
		});
	}

}
