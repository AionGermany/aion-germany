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

package quest.daevation;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author Romanz
 */
public class _80295DurableDaevanionWeapon extends QuestHandler {

	private final static int questId = 80295;

	public _80295DurableDaevanionWeapon() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(831387).addOnQuestStart(questId);
		qe.registerQuestNpc(831387).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();

		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc) {
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		QuestState qs = player.getQuestStateList().getQuestState(questId);

		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 831387) {
				if (env.getDialog() == DialogAction.QUEST_SELECT) {
					int plate = player.getEquipment().itemSetPartsEquipped(303);
					int chain = player.getEquipment().itemSetPartsEquipped(302);
					int leather = player.getEquipment().itemSetPartsEquipped(301);
					int cloth = player.getEquipment().itemSetPartsEquipped(300);
					int gunner = player.getEquipment().itemSetPartsEquipped(372);

					if (plate != 5 && chain != 5 && leather != 5 && cloth != 5 && gunner != 5) {
						return sendQuestDialog(env, 1003);
					}
					else {
						return sendQuestDialog(env, 4762);
					}
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}

		if (qs == null) {
			return false;
		}

		int var = qs.getQuestVarById(0);

		if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 831387) {
				switch (env.getDialog()) {
					case QUEST_SELECT:
						if (var == 0) {
							return sendQuestDialog(env, 1011);
						}
					case CHECK_USER_HAS_QUEST_ITEM:
						if (var == 0) {
							return checkQuestItems(env, 0, 1, true, 5, 0);
						}
						break;
					case SELECT_ACTION_1352:
						if (var == 0) {
							return sendQuestDialog(env, 1352);
						}
					default:
						break;
				}
			}
			return false;
		}
		else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 831387) {
				return sendQuestEndDialog(env);
			}
			return false;
		}
		return false;
	}
}
