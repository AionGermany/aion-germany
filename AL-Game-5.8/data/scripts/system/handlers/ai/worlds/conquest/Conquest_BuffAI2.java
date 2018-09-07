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
package ai.worlds.conquest;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;

import javolution.util.FastMap;

/**
 * @author Falke_34, CoolyT
 */
@AIName("conquest_buff")
public class Conquest_BuffAI2 extends NpcAI2 {

	FastMap<Integer, Integer> skillByNpc = new FastMap<Integer, Integer>();

	@Override
	protected void handleDialogStart(Player player) {

		Creature shugo = getOwner();
		int npcId = shugo.getObjectTemplate().getTemplateId();

		// Npc | Skill
		skillByNpc.put(856175, 21924); // Pawrunerk - Boost Attack Power
		skillByNpc.put(856176, 21925); // Chitrunerk - Movement Speed Increase
		skillByNpc.put(856177, 21926); // Rapirunerk - Attack Speed/Casting Speed Increase
		skillByNpc.put(856178, 21927); // Dandrunerk - Boost Defense

		if (!skillByNpc.containsKey(npcId))
			return;

		int skillId = skillByNpc.get(npcId);

		shugo.setTarget(player);
		shugo.getController().useSkill(skillId, 1); // Boost Defense
		shugo.getController().delete();
	}
}
