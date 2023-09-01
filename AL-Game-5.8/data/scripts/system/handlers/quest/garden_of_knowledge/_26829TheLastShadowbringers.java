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
package quest.garden_of_knowledge;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author Phantom_KNA
 */
public class _26829TheLastShadowbringers extends QuestHandler {

	public _26829TheLastShadowbringers() {
		super(questId);
	}

	private final static int questId = 26829;

	@Override
	public void register() {
		qe.registerOnEnterZone(ZoneName.get("IDETERNITY_02_2ND_DEFENCE_LINE_301550000"), questId);
		qe.registerQuestNpc(806287).addOnTalkEvent(questId); // Ube
		qe.registerQuestNpc(220474).addOnKillEvent(questId); // Fallen Garrem
		qe.registerQuestNpc(220475).addOnKillEvent(questId); // Fallen Garrem
		qe.registerQuestNpc(220479).addOnKillEvent(questId); // Fallen Garrem
		qe.registerQuestNpc(220476).addOnKillEvent(questId); // Fallen Garrem
		qe.registerQuestNpc(220477).addOnKillEvent(questId); // Fallen Garrem
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		int targetId = env.getTargetId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);

		if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 806287) { // Ube
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}

	@Override
	public boolean onKillEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null || qs.getStatus() != QuestStatus.START)
			return false;

		int targetId = env.getTargetId();

		switch (targetId) {
			case 220474:
			case 220475:
			case 220476:
			case 220479:
			case 220477:
				if (qs.getQuestVarById(1) != 9) {
					qs.setQuestVarById(1, qs.getQuestVarById(1) + 1);
					updateQuestStatus(env);
				}
				else {
					qs.setQuestVarById(0, 10);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
				}
		}
		return false;
	}

	@Override
	public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName) {
		return defaultOnEnterZoneEvent(env, zoneName, ZoneName.get("IDETERNITY_02_2ND_DEFENCE_LINE_301550000"));
	}
}
