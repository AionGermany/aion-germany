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
package ai.AgentFight;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.manager.WalkManager;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.services.AgentFightService;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;

import ai.AggressiveNpcAI2;

@AIName("empowered_veille")
// 235064
public class Empowered_VeilleAI2 extends AggressiveNpcAI2 {

	private String walkerId = "LDF4_ADVANCE_NPCPATHGOD_L_04";

	@Override
	protected void handleAttack(Creature creature) {
		getEffectController().removeEffect(21779);
		super.handleAttack(creature);
	}

	@Override
	protected void handleSpawned() {
		Npc boss = getPosition().getWorldMapInstance().getNpc(235064);
		SkillEngine.getInstance().getSkill(getOwner(), 21779, 1, boss).useNoAnimationSkill(); // Agent's Rest
		super.handleSpawned();
		getSpawnTemplate().setWalkerId(walkerId);
		WalkManager.startWalking(this);
		getOwner().setState(1);
		PacketSendUtility.broadcastPacket(getOwner(), new SM_EMOTION(getOwner(), EmotionType.START_EMOTE2, 0, getObjectId()));
	}

	@Override
	protected void handleDied() {
		AgentFightService.getInstance().onVeilleReward();
		super.handleDied();
	}
}
