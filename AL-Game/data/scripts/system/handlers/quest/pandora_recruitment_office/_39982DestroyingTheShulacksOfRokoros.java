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
package quest.pandora_recruitment_office;

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author QuestGenerator 1.15 by Mariella
 */
public class _39982DestroyingTheShulacksOfRokoros extends QuestHandler {

	private final static int questId = 39982;
	private final static int[] mobs = { 650513, 650514, 650515, 650516, 650517, 650811, 650812, 650813, 650814, 650815, 650524, 650525, 650526, 650527, 650528, 650716, 650717, 650791, 650792, 650508, 650509, 650510, 650511, 650512, 650713, 650714, 650788, 650789, 650806, 650807, 650808, 650809, 650810, 650500, 650501, 650502, 650503, 650797, 650798, 650799, 650800, 650801, 650802 };

	public _39982DestroyingTheShulacksOfRokoros() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(820181).addOnTalkEvent(questId);  // Berio

		for (int mob : mobs) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env, 39900, false);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		DialogAction dialog = env.getDialog();
		int targetId = env.getTargetId();

		if (qs == null) {
			return false;
		}

		if (qs.getStatus() == QuestStatus.START) {
			switch (targetId) {
				case 820181: {
					switch (dialog) {
						case FINISH_DIALOG: {
							return sendQuestSelectionDialog(env);
						}
						default: 
							break;
					}
					break;
				}
				default:
					break;
			}
		}

		return false;
	}

	@Override
	public boolean onKillEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);

		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var  = qs.getQuestVarById(0);
			int var1 = qs.getQuestVarById(1);

			// (0) Step: 0, Count: 35, Mobs : 650513, 650514, 650515, 650516, 650517, 650811, 650812, 650813, 650814, 650815
			// (1) Step: 0, Count: 35, Mobs : 650524, 650525, 650526, 650527, 650528, 650716, 650717, 650791, 650792
			// (2) Step: 0, Count: 35, Mobs : 650508, 650509, 650510, 650511, 650512, 650713, 650714, 650788, 650789, 650806, 650807, 650808, 650809, 650810
			// (3) Step: 0, Count: 35, Mobs : 650500, 650501, 650502, 650503, 650797, 650798, 650799, 650800, 650801, 650802

			switch (var) {
				case 0: {
				// ???
				}
				case 1: {
				// ???
				}
				case 2: {
				// ???
				}
				case 3: {
				// ???
				}
				default:
					break;
			}
			return false;
		}
		return false;
	}
}
