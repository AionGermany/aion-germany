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
package ai.instance.drakenseerLair;

import java.util.concurrent.atomic.AtomicBoolean;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.ai2.AbstractAI;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.NpcShoutsService;
import com.aionemu.gameserver.utils.PacketSendUtility;

@AIName("IDF6_Dragon_Artifact_Boost_C")
public class IDF6_Dragon_Artifact_Boost_CAI2 extends NpcAI2 {

	private Npc IDF6DragonGate;
	private boolean canThink = true;
	private int artifactBoostPhase = 0;
	private AtomicBoolean isAggred = new AtomicBoolean(false);

	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		if (isAggred.compareAndSet(false, true)) {
		}
		if (IDF6DragonGate == null) {
			IDF6DragonGate = (Npc) spawn(703159, 335.19684f, 323.7957f, 319.2191f, (byte) 82);
		}
		checkPercentage(getLifeStats().getHpPercentage());
	}

	private void checkPercentage(int hpPercentage) {
		if (hpPercentage == 99 && artifactBoostPhase < 1) {
			artifactBoostPhase = 1;
			startIDF6DragonRaidC1();
		}
		if (hpPercentage == 80 && artifactBoostPhase < 2) {
			artifactBoostPhase = 2;
			startIDF6DragonRaidC1();
		}
		if (hpPercentage == 60 && artifactBoostPhase < 3) {
			artifactBoostPhase = 3;
			startIDF6DragonRaidC1();
		}
		if (hpPercentage == 40 && artifactBoostPhase < 4) {
			artifactBoostPhase = 4;
			startIDF6DragonRaidC1();
		}
		if (hpPercentage == 8 && artifactBoostPhase < 5) {
			artifactBoostPhase = 5;
			startIDF6DragonRaidC1();
		}
	}

	/**
	 * Dragon Raid C
	 */
	private void startIDF6DragonRaidC1() {
		// Reinforcements have arrived to defend the Shielding Conduits.
		PacketSendUtility.npcSendPacketTime(getOwner(), SM_SYSTEM_MESSAGE.STR_MSG_IDF6_Dragon_Attack_Tower, 0);
		// Prepare for combat! Enemies approaching!
		NpcShoutsService.getInstance().sendMsg(getOwner(), 1402785, 10000);
		// Hold a little longer and you will survive.
		NpcShoutsService.getInstance().sendMsg(getOwner(), 1402833, 20000);
		// Only a few enemies left!
		NpcShoutsService.getInstance().sendMsg(getOwner(), 1402834, 30000);
		dragonRaid((Npc) spawn(220432, 333.3891f, 319.6643f, 318.8283f, (byte) 82), 312.16144f, 285.48962f, 318.85556f, false);
		dragonRaid((Npc) spawn(220433, 333.3891f, 319.6643f, 318.8283f, (byte) 82), 312.16144f, 285.48962f, 318.85556f, false);
		dragonRaid((Npc) spawn(220432, 333.3891f, 319.6643f, 318.8283f, (byte) 82), 312.16144f, 285.48962f, 318.85556f, false);
		dragonRaid((Npc) spawn(220435, 333.3891f, 319.6643f, 318.8283f, (byte) 82), 312.16144f, 285.48962f, 318.85556f, false);
		dragonRaid((Npc) spawn(220436, 333.3891f, 319.6643f, 318.8283f, (byte) 82), 312.16144f, 285.48962f, 318.85556f, false);
		dragonRaid((Npc) spawn(220440, 333.3891f, 319.6643f, 318.8283f, (byte) 82), 312.16144f, 285.48962f, 318.85556f, false);
	}

	private void dragonRaid(final Npc npc, float x, float y, float z, boolean despawn) {
		((AbstractAI) npc.getAi2()).setStateIfNot(AIState.WALKING);
		npc.setState(1);
		npc.getMoveController().moveToPoint(x, y, z);
		PacketSendUtility.broadcastPacket(npc, new SM_EMOTION(npc, EmotionType.START_EMOTE2, 0, npc.getObjectId()));
	}

	@Override
	public boolean canThink() {
		return canThink;
	}

	@Override
	protected void handleBackHome() {
		canThink = true;
		isAggred.set(false);
		super.handleBackHome();
	}
}
